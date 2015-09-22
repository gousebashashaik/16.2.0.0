/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.media.services.ProductMediaService;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.accommodation.MainstreamAccommodationService;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.components.model.AdvertisedPackagesCollectionComponentModel;
import uk.co.portaltech.tui.converters.AccommodationOption;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.model.ItineraryLeg;
import uk.co.portaltech.tui.populators.MediaDataPopulator;
import uk.co.portaltech.tui.populators.MediaPopulatorLite;
import uk.co.portaltech.tui.populators.NewFacilityPopulator;
import uk.co.portaltech.tui.populators.TripAdvisorPopulator;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.services.LinkedItemService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.productfinder.ProductFinder;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationRoomViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.ArticleViewData;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;
import uk.co.portaltech.tui.web.view.data.EssentialInformationViewData;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;
import uk.co.portaltech.tui.web.view.data.FacilityViewDataPriorityComparator;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SustainableTourismComponentViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.TwoCentreSelectorViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;
import uk.co.tui.web.common.enums.AccommodationPageType;
import uk.co.tui.web.common.enums.CarouselLookupType;

/**
 * @author gagan, l.furrer
 *
 *
 */
public class DefaultAccomodationFacade implements AccommodationFacade
{

   private static final String STAY = "-STAY";

   private static final String HOTEL = "hotel_";

   @Resource
   private MainstreamAccommodationService accommodationService;

   @Resource
   private NewFacilityPopulator newFacilityPopulator;

   @Resource
   private AirportService airportService;

   @Autowired
   private ConfigurablePopulator<AccommodationModel, AccommodationViewData, AccommodationOption> accommodationConfiguredPopulator;

   @Autowired
   private ConfigurablePopulator<ItemModel, AccommodationViewData, AccommodationOption> accommodationAttractionExcursionConfiguredPopulator;

   @Resource
   private Converter<AccommodationModel, AccommodationViewData> accommodationConverter;

   @Resource
   private Converter<CategoryModel, CategoryData> categoryConverter;

   @Resource
   private Converter<ItemModel, AccommodationViewData> accommodationAttractionExcursionConverter;

   @Resource
   private Converter<BenefitModel, BenefitViewData> defaultBenefitConverter;

   @Resource
   private Converter<ArticleModel, ArticleViewData> articleConverter;

   @Resource
   private TripAdvisorPopulator tripAdvisorPopulator;

   @Resource
   private Populator<ArticleModel, ArticleViewData> articlePopulator;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private SessionService sessionService;

   @Resource
   private CategoryService categoryService;

   @Resource
   private MediaDataPopulator mediaDataPopulator;

   @Resource
   private Populator<Collection<MediaContainerModel>, List<MediaViewData>> featureListImagesPopulator;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private ProductService productService;

   private Populator<AccommodationModel, AccommodationViewData> accommodationInfoPopulator;

   @Resource
   private Populator<List<AccommodationModel>, List<ItineraryLeg>> essentialInfoPopulator;

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   // for avoiding Magic Number
   private static final int NUMBER_FIVE = 5;

   private static final int NUMBER_SIX = 6;

   private static final String FEATURE_DESCRIPTOR_CODE = "holiday_type";

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource(name = "siteAwareKeyGenerator")
   private SiteAwareKeyGenerator keyGenerator;

   @Resource
   private LinkedItemService linkedItemService;

   @Autowired
   private Map<String, ProductFinder> finders;

   @Resource
   private ProductMediaService productMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   private static final TUILogUtils LOG = new TUILogUtils("DefaultAccomodationFacade");

   private static final int TWO = 2;

   @Required
   public void setAccommodationInfoPopulator(
      final Populator<AccommodationModel, AccommodationViewData> accommodationInfoPopulator)
   {
      this.accommodationInfoPopulator = accommodationInfoPopulator;
   }

   /**
    * @param accommodationModel
    * @return accommodationViewData
    */
   private AccommodationViewData getAccommodationDataForModel(
      final AccommodationModel accommodationModel)
   {
      AccommodationViewData accommodationViewData;
      accommodationViewData = accommodationConverter.convert(accommodationModel);
      return accommodationViewData;
   }

   /**
    * @param accommodationCode
    * @return accommodationModel
    */
   private AccommodationModel getAccommodationForCode(final String accommodationCode)
   {

      return accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode,
         cmsSiteService.getCurrentCatalogVersion(), null);
   }

   @Override
   public AccommodationViewData getAccommodationByCode(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationByCode", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         if (accommodationModel != null)
         {
            accommodationViewData = getAccommodationDataForModel(accommodationModel);
            accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
               Arrays.asList(AccommodationOption.BASIC));
         }
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getRoomsForTheAccomodation
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getRoomsForAccommodation(final String accommodationCode,
      final int visibleItems)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getRoomsForAccommodation", accommodationCode,
            Integer.valueOf(visibleItems));
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ROOMS));
         List<AccommodationRoomViewData> roomsData = accommodationViewData.getRoomsData();
         if (visibleItems > 0 && roomsData.size() > visibleItems)
         {
            roomsData = roomsData.subList(0, visibleItems);
         }
         accommodationViewData.setRoomsData(roomsData);
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationLocationInfo
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationLocationPanelInfo(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getAccommodationLocationPanelInfo", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationEditorialInfo
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationEditorialInfo(final String accommodationCode,
      final String pageType)
   {

      final String facilitiesOverview = "facilitiesOverview";
      final String roomsOverview = "roomsOverview";
      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getAccommodationEditorialInfo", accommodationCode, pageType);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationViewData.setDescription("");
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.EDITORIAL_INFO));
         final Map<String, List<Object>> viewData =
            accommodationViewData.getFeatureCodesAndValues();

         if (viewData != null)
         {
            switch (AccommodationPageType.valueOf(pageType.toUpperCase()))
            {
               case ROOMS:
               case TWIN_CENTRE_HOTEL_ROOMS:
               case CRUISE_AND_STAY_HOTEL_ROOMS:
               case TOUR_AND_STAY_HOTEL_ROOMS:
                  final List<Object> rooms = viewData.get(roomsOverview);
                  if (CollectionUtils.isNotEmpty(rooms))
                  {
                     accommodationViewData.setDescription(rooms.get(0).toString());
                  }
                  break;
               case FACILITIES:
               case FACILITIES_CRUISE:
               case CRUISE_AND_STAY_HOTEL_FACILITIES:
               case TWIN_CENTRE_HOTEL_FACILITIES:
               case TOUR_AND_STAY_HOTEL_FACILITIES:
                  final List<Object> facilities = viewData.get(facilitiesOverview);
                  if (CollectionUtils.isNotEmpty(facilities))
                  {
                     accommodationViewData.setDescription(facilities.get(0).toString());
                  }
                  break;
               default:
                  final List<Object> introduction = viewData.get("introduction");
                  if (CollectionUtils.isNotEmpty(introduction))
                  {
                     accommodationViewData.setDescription(introduction.get(0).toString());
                  }
            }
         }

         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationEditorialInfo
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationEditorialInfo(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationEditorialInfo", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PRIMARY_IMAGE,
               AccommodationOption.EDITORIAL_INFO, AccommodationOption.EDITORIAL_INTRO));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }
      return accommodationViewData;
   }

   /*
    * This method creates a list based on the priority field.The first element has priority 1, the
    * second one has priority 2 and so on.
    */
   private List<FacilityViewData> populateFacilityPriorityList(
      final List<FacilityViewData> facilityList)
   {

      final String key = "priority";
      Collections.sort(facilityList, new FacilityViewDataPriorityComparator());
      final List<FacilityViewData> facilityPriorityList = new ArrayList<FacilityViewData>();
      int count = 0;
      for (final FacilityViewData iter : facilityList)
      {
         if (iter.getFeatureCodesAndValues().containsKey(key)
            && iter.getGalleryImages().size() >= 0
            && CollectionUtils.isNotEmpty(iter.getFeatureCodesAndValues().get(key)))
         {
            if (!"0".equals(iter.getFeatureCodesAndValues().get(key).get(0)))
            {
               facilityPriorityList.add(iter);
               count++;
            }
         }
         if (count >= NUMBER_FIVE)
         {
            break;
         }
      }
      if (facilityPriorityList.size() == 0)
      {
         return null;
      }
      return facilityPriorityList;
   }

   @Override
   public AccommodationViewData getAccommodationFacilities(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData;
      final AccommodationViewData accommodationNewViewData = new AccommodationViewData();
      final String key =
         keyGenerator.generate("getUnPrioritizedAccomFacilities", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         accommodationViewData = getUnPrioritizedAccomFacilities(accommodationCode);

         final List<FacilityViewData> failityList = accommodationViewData.getFacilities();

         final List<FacilityViewData> facilityPriorityList =
            populateFacilityPriorityList(failityList);

         if (facilityPriorityList != null)
         {
            accommodationViewData.setFacilities(facilityPriorityList);
         }

         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }
      final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);

      if (accommodationViewData != null)
      {

         newFacilityPopulator.populate(accommodationModel, accommodationNewViewData);

         accommodationViewData.putFeatureCodesAndValues(accommodationNewViewData
            .getFeatureCodesAndValues());
      }

      return accommodationViewData;
   }

   @Override
   public AccommodationViewData getAccommodationFacilitiesHighlights(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getAccommodationFacilitiesHighlights", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         accommodationViewData = populateBasicFacilities(accommodationCode);

         final List<FacilityViewData> failityList = accommodationViewData.getFacilities();

         final List<FacilityViewData> facilityPriorityList =
            populateFacilityPriorityList(failityList);

         if (facilityPriorityList != null)
         {
            accommodationViewData.setFacilities(facilityPriorityList);
         }

         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /**
    * @param accommodationCode
    * @return AccommodationViewData
    */
   @Override
   public AccommodationViewData getUnPrioritizedAccomFacilities(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final AccommodationViewData accommodationNewViewData = new AccommodationViewData();
      final String key =
         keyGenerator.generate("getUnPrioritizedAccomFacilities", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);
      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         accommodationViewData = populateBasicFacilities(accommodationCode);
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }
      final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
      newFacilityPopulator.populate(accommodationModel, accommodationNewViewData);
      if (accommodationViewData != null)
      {
         accommodationViewData.putFeatureCodesAndValues(accommodationNewViewData
            .getFeatureCodesAndValues());
      }
      return accommodationViewData;
   }

   /**
    * @param accommodationCode
    * @return AccommodationViewData
    */
   private AccommodationViewData populateBasicFacilities(final String accommodationCode)
   {
      AccommodationViewData accommodationViewData;
      final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
      accommodationViewData = getAccommodationDataForModel(accommodationModel);
      accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
         Arrays.asList(AccommodationOption.BASIC, AccommodationOption.FACILITIES));
      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getBenefitsForTheAccomodation
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getBenefitsForAccomodation(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getBenefitsForAccomodation", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final BrandDetails brandDetails =
            sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> brandPks = brandDetails.getRelevantBrands();
         final List<BenefitModel> benefitsForAccommodation =
            accommodationService.getBenefitsForAccommodation(accommodationCode,
               cmsSiteService.getCurrentCatalogVersion(), brandPks);
         final List<BenefitViewData> listOfAccommodationBenefits = new ArrayList<BenefitViewData>();
         for (final BenefitModel benefitModel : benefitsForAccommodation)
         {
            listOfAccommodationBenefits.add(defaultBenefitConverter.convert(benefitModel));
         }
         accommodationViewData = new AccommodationViewData();
         accommodationViewData.setAccommodationBenefits(listOfAccommodationBenefits);
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#
    * getAccommodationEditorialIntroduction(java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationEditorialIntroduction(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getAccommodationEditorialIntroduction", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.EDITORIAL_INTRO));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#
    * getRequiredAccommodationEditorialIntroduction(java.lang.String)
    */
   @Override
   public AccommodationViewData getRequiredAccommodationEditorialIntroduction(
      final String accommodationCode)
   {

      final AccommodationViewData accommodationViewData =
         getAccommodationEditorialIntroduction(accommodationCode);
      final AccommodationViewData requiredAccommodationViewData = new AccommodationViewData();
      requiredAccommodationViewData.putFeatureCodesAndValues(accommodationViewData
         .getFeatureCodesAndValues());
      requiredAccommodationViewData.setProductRanges(accommodationViewData.getProductRanges());
      requiredAccommodationViewData.setReviewCount(accommodationViewData.getReviewCount());
      requiredAccommodationViewData.setRatingBar(accommodationViewData.getRatingBar());
      requiredAccommodationViewData.setAccomwifi(accommodationViewData.isAccomwifi());

      return requiredAccommodationViewData;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#
    * getRequiredComponentAccommodationEditorialIntroduction(java.lang.String)
    */
   @Override
   public AccommodationViewData getRequiredComponentAccommodationEditorialIntroduction(
      final String accommodationCode)
   {

      final AccommodationViewData accommodationViewData =
         getAccommodationEditorialIntroduction(accommodationCode);
      final AccommodationViewData requiredAccommodationViewData = new AccommodationViewData();
      requiredAccommodationViewData.putFeatureCodesAndValues(accommodationViewData
         .getFeatureCodesAndValues());
      return requiredAccommodationViewData;
   }

   @Override
   public ItineraryViewData getItineraryData(final String accommodationCode)
   {

      return linkedItemService.getDetailedItinerary(accommodationCode);
   }

   @Override
   public ItineraryViewData getItineraryDataForLaplandResorts(final String locationCode)
   {
      final List<AccommodationModel> accommodationsForLocation =
         accommodationService.getBookableAccommodationsForLocation(locationCode);

      if (CollectionUtils.isNotEmpty(accommodationsForLocation))
      {
         for (final AccommodationModel accommodationModel : accommodationsForLocation)
         {
            if (!accommodationService.hasHolidayTypeDayTrip(accommodationModel))
            {
               return linkedItemService.getItinerary(accommodationModel.getCode());
            }
         }
      }

      return null;
   }

   @Override
   public ItineraryViewData getItineraryDataForAllinone(final String accommodationCode)
   {

      return linkedItemService.getItineraryDataForAttrationandExcursion(accommodationCode);
   }

   @Override
   public TwoCentreSelectorViewData getTwoCentreSelectorData(final String accommodationCode)
   {
      return linkedItemService.getTwoCentreSelectorData(accommodationCode);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationDisclaimer
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationDisclaimer(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final AccommodationViewData accommodationFeatureValue = new AccommodationViewData();
      final String key = keyGenerator.generate("getAccommodationDisclaimer", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         if (accommodationModel != null)
         {
            accommodationViewData = getAccommodationDataForModel(accommodationModel);
            accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
               Arrays.asList(AccommodationOption.BASIC, AccommodationOption.DISCLAIMER));
            // put in cache
            accomDataCache.put(key, accommodationViewData);
            accommodationFeatureValue.putFeatureCodesAndValues(accommodationViewData
               .getFeatureCodesAndValues());
            return accommodationFeatureValue;
         }

      }
      // Fixing: Correctness - Possible null pointer dereference
      if (accommodationViewData != null)
      {
         accommodationFeatureValue.putFeatureCodesAndValues(accommodationViewData
            .getFeatureCodesAndValues());
      }

      return accommodationFeatureValue;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getHealthAndSafetyEditorial
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getHealthAndSafetyEditorial(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getHealthAndSafetyEditorial", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.HEALTHANDSAFETY));

         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getPassportAndVisasEditorial
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getPassportAndVisasEditorial(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getPassportAndVisasEditorial", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PASSPORTANDVISA));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   @Override
   public AccommodationViewData getAccommodationAllInclusive(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationAllInclusive", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ALL_INCLUSIVE));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationProductRanges
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationProductRanges(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationProductRanges", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);

         if (accommodationModel != null)
         {
            accommodationViewData = getAccommodationDataForModel(accommodationModel);
            accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
               Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PRODUCTRANGE));
            // put in cache
            accomDataCache.put(key, accommodationViewData);
         }

      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getGettingAroundEditorial
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getGettingAroundEditorial(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getGettingAroundEditorial", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.GETTINGAROUND));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationAtAGlance
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationAtAGlance(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationAtAGlance", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.AT_A_GLANCE));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationLocationInfo
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationLocationInfo(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationLocationInfo", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#
    * getAccommodationWhenToGoEditorial(java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationWhenToGoEditorial(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getAccommodationWhenToGoEditorial", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.WHEN_TO_GO));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationEntertainment
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationEntertainment(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationEntertainment", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ENTERTAINMENT,
               AccommodationOption.GALLERY));
         accommodationViewData.getGalleryImages().addAll(getMediaViewDatas(accommodationModel));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /**
    * @param accommodationModel
    * @return mediaDatalist
    */
   private List<MediaViewData> getMediaViewDatas(final AccommodationModel accommodationModel)
   {
      final ArrayList<MediaViewData> mediaDatalist = new ArrayList<MediaViewData>();
      mediaPopulatorLite.populate(productMediaService.getImageMedias(accommodationModel),
         mediaDatalist);
      return mediaDatalist;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#
    * getAccommodationThumbnailMapData(java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationThumbnailMapData(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getAccommodationThumbnailMapData", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {
         final ItemModel itemModel =
            accommodationService.getAccommodationAttractionExcursionbyCode(accommodationCode,
               cmsSiteService.getCurrentCatalogVersion());
         accommodationViewData = accommodationAttractionExcursionConverter.convert(itemModel);
         accommodationAttractionExcursionConfiguredPopulator.populate(itemModel,
            accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.THUMBNAILMAP));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#
    * getAccommodationNameComponentData(java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationNameComponentData(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getAccommodationNameComponentData", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.NAME));

         cruiseSiteAccommodationProcessing(accommodationViewData, accommodationModel);

         // put in cache
         accomDataCache.put(key, accommodationViewData);

      }

      return accommodationViewData;
   }

   /**
    * @param accommodationViewData
    * @param accommodationModel
    */
   private void cruiseSiteAccommodationProcessing(
      final AccommodationViewData accommodationViewData, final AccommodationModel accommodationModel)
   {
      if (tuiUtilityService.getSiteBrand().equals(BrandType.CR.toString()))
      {
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.PRODUCTRANGE));
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationKeyFactsData
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationKeyFactsData(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationKeyFactsData", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_KEYFACTS));
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#
    * getArticlesForTheAccommodation(java.lang.String)
    */
   @Override
   public AccommodationViewData getArticlesForAccommodation(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getArticlesForAccommodation", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         final List<ArticleModel> articles = accommodationModel.getArticles();
         if (articles != null && !articles.isEmpty())
         {
            final List<ArticleViewData> articleViewDataList = new ArrayList<ArticleViewData>();
            for (final ArticleModel articleModel : articles)
            {
               final ArticleViewData articleViewData = articleConverter.convert(articleModel);
               articlePopulator.populate(articleModel, articleViewData);
               articleViewDataList.add(articleViewData);
            }
            accommodationViewData.setArticles(articleViewDataList);
         }

         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getReviewsForAccommodation
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getTripAdvisorSummaryData(final String accommodationCode,
      final Integer maxUserReview)
   {

      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getTripAdvisorSummaryData", accommodationCode, maxUserReview);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         accommodationViewData =
            tripAdvisorPopulator.getTripAdvisorSummaryData(accommodationCode, maxUserReview);
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getAccommodationHighlights
    * (java.lang.String, java.lang.Integer)
    */
   @Override
   public AccommodationViewData getAccommodationHighlights(final String accommodationCode,
      final Integer highlightsNumber)
   {
      AccommodationViewData accommodationViewData = null;
      if (StringUtils.isNotBlank(accommodationCode))
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         accommodationViewData = getAccommodationDataForModel(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.HIGHLIGHTS));
         if (highlightsNumber != null)
         {
            final int highlightsSize = highlightsNumber.intValue();
            final List<Object> list = accommodationViewData.getFeatureValues("usps");
            if (list != null && list.size() > highlightsSize)
            {
               accommodationViewData.putFeatureValue("usps", list.subList(0, highlightsSize));
            }
         }
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getTripAdvisorReviewData
    * (java.lang.String, java.lang.Integer)
    */
   @Override
   public AccommodationViewData getTripAdvisorReviewData(final String accommodationCode,
      final Integer maxUserReview)
   {
      AccommodationViewData accommodationViewData = null;
      final String key =
         keyGenerator.generate("getTripAdvisorReviewData", accommodationCode, maxUserReview);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         accommodationViewData =
            tripAdvisorPopulator.getTripAdvisorReviews(accommodationCode, maxUserReview);
         // put in cache
         accomDataCache.put(key, accommodationViewData);
      }

      return accommodationViewData;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getDealsData(java.util .List)
    */
   @Override
   public List<AccommodationViewData> getAccommodationDealsData(final List<ResultData> list)
   {
      return getAccommodationsForAccommOptions(list, Arrays.asList(AccommodationOption.BASIC,
         AccommodationOption.CATEGORIES, AccommodationOption.LOCATION_INFO));
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#
    * getAccommodationsWithEndecaData(java.util.List)
    */
   @Override
   public List<AccommodationViewData> getAccommodationsWithEndecaData(final List<ResultData> list)
   {
      return getAccommodationsForAccommOptions(list, Arrays.asList(AccommodationOption.BASIC,
         AccommodationOption.THUMBNAILMAP, AccommodationOption.GALLERY,
         AccommodationOption.HIGHLIGHTS, AccommodationOption.T_RATING,
         AccommodationOption.PRODUCTRANGE));
   }

   @Override
   public List<AccommodationViewData> getAccommodationsWithLocationFromEndecaData(
      final List<ResultData> list, final boolean multiCentre)
   {
      if (multiCentre)
      {
         return getAccommodationsForAccommOptions(list, Arrays.asList(AccommodationOption.BASIC,
            AccommodationOption.THUMBNAILMAP, AccommodationOption.GALLERY,
            AccommodationOption.HIGHLIGHTS, AccommodationOption.T_RATING));
      }
      else
      {
         return getAccommodationsForAccommOptions(list, Arrays.asList(AccommodationOption.BASIC,
            AccommodationOption.THUMBNAILMAP, AccommodationOption.GALLERY,
            AccommodationOption.HIGHLIGHTS, AccommodationOption.T_RATING,
            AccommodationOption.ACCOM_LOCATION_DATA));
      }

   }

   @Override
   public List<AccommodationViewData> getAccommodationsWithDetailData(final List<ResultData> list)
   {
      return getAccommodationsForAccommOptions(list, Arrays.asList(AccommodationOption.BASIC,
         AccommodationOption.PRIMARY_IMAGE, AccommodationOption.HIGHLIGHTS,
         AccommodationOption.ACCOM_LOCATION_DATA));
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#
    * getAccommodationPriceDataFromEndeca(uk.co.portaltech.tui.web .view.data.SearchResultData)
    */
   @Override
   public AccommodationViewData getAccommodationPriceDataFromEndeca(
      final SearchResultData<ResultData> searchResultData)
   {
      final AccommodationViewData accommodationViewData = new AccommodationViewData();
      if (searchResultData != null && searchResultData.getResults() != null)
      {
         for (final ResultData resultData : searchResultData.getResults())
         {
            if (resultData.getCode() != null)
            {
               accommodationViewData.setPriceFrom(roundingPriceValue(resultData.getPriceFrom()));
               accommodationViewData.setAvailableFrom(resultData.getAvailableFrom());
               if (!StringUtils.isEmpty(resultData.getDeparturePoint()))
               {
                  accommodationViewData.setDeparturePoint(airportService.getAirportByCode(
                     resultData.getDeparturePoint()).getName());
               }
               accommodationViewData.setRoomOccupancy(resultData.getRoomOccupancy());
               accommodationViewData.setStayPeriod(resultData.getStayPeriod());
               accommodationViewData.setName(resultData.getName());
               accommodationViewData.setCode(resultData.getCode());
               accommodationViewData.settRating(resultData.gettRating());
               accommodationViewData.setBoardBasis(resultData.getBoardBasis());
               break;
            }
         }
      }
      return accommodationViewData;
   }

   private List<AccommodationViewData> getAccommodationsForAccommOptions(
      final List<ResultData> list, final List<AccommodationOption> options)
   {
      final List<AccommodationViewData> accommodations = new ArrayList<AccommodationViewData>();
      if (list != null && !list.isEmpty())
      {
         for (final ResultData resultData : list)
         {
            final String codeFromEndeca = correctCode(resultData.getCode());
            if (!StringUtils.isEmpty(codeFromEndeca))
            {
               final AccommodationModel accommodationModel =
                  getAccommodationForCode(codeFromEndeca);

               AccommodationViewData accommodationViewData = null;
               if (accommodationModel != null)
               {
                  accommodationViewData = getAccommodationDataForModel(accommodationModel);
                  accommodationConfiguredPopulator.populate(accommodationModel,
                     accommodationViewData, options);
                  if (viewSelector.checkIsMobile())
                  {
                     setDestinationForAccommodationForMobile(accommodationModel,
                        accommodationViewData);
                  }
                  accommodationViewData.setAvailableFrom(resultData.getAvailableFrom());
                  accommodationViewData.setPriceFrom(roundingPriceValue(resultData.getPriceFrom()));

                  if (resultData.getDeparturePoint() != null)
                  {
                     accommodationViewData.setDeparturePoint(airportService.getAirportName(
                        cmsSiteService.getCurrentCatalogVersion(), resultData.getDeparturePoint()));
                     accommodationViewData.setDepartureCode(resultData.getDeparturePoint());
                  }
                  // Newly Adding For Board Basis For Latest Deal--Start
                  if (CollectionUtils.isNotEmpty(resultData.getBoardBasis()))
                  {
                     accommodationViewData.setBoardBasis(resultData.getBoardBasis());
                  }
                  // Newly Adding For Board Basis For Latest Deal--End
                  accommodationViewData.setRoomOccupancy(resultData.getRoomOccupancy());
                  accommodationViewData.setStayPeriod(resultData.getStayPeriod());
                  accommodationViewData =
                     getMediaDataForAccommodationOptions(accommodationViewData, accommodationModel);
                  accommodationViewData.setBoardBasis(resultData.getBoardBasis());

                  accommodations.add(accommodationViewData);
               }
            }
         }
      }

      if (accommodations.isEmpty())
      {
         return Collections.emptyList();
      }
      return accommodations;
   }

   private String roundingPriceValue(final String s)
   {
      if (s != null && s.length() > 0)
      {
         final BigDecimal bd = new BigDecimal(s.trim()).setScale(0, RoundingMode.HALF_UP);
         return bd.toString();
      }
      else
      {
         return "";
      }
   }

   private String correctCode(final String code)
   {
      String codeFromEndeca = code;
      final int length = NUMBER_SIX;
      if (!StringUtils.isBlank(codeFromEndeca))
      {
         final int codeLength = codeFromEndeca.length();
         if (codeLength < length)
         {
            final int numOfZerosToAppend = length - codeLength;
            for (int i = 0; i < numOfZerosToAppend; i++)
            {
               codeFromEndeca = "0" + codeFromEndeca;

            }
         }
      }
      return codeFromEndeca;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getAccommodationsByLocation
    * (java.lang.String)
    */
   @Override
   public Map<String, List<AccommodationViewData>> getTHAccommodationsByResort(
      final String locationCode, final int visibleItems)
   {
      final LocationModel locationModel =
         (LocationModel) categoryService.getCategoryForCode(locationCode);
      if (locationModel.getType().equals(LocationType.RESORT))
      {
         return getTHAccommodations(locationCode, visibleItems,
            Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));
      }
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getAccommodations(java .lang.String,
    * int,java.lang.String)
    */
   @Override
   public Map<String, List<AccommodationViewData>> getTHAccommodations(final String locationCode,
      final int visibleItems, final List<AccommodationOption> options)
   {
      final List<AccommodationViewData> villaViewDataList = new ArrayList<AccommodationViewData>();
      final List<AccommodationViewData> hotelViewDataList = new ArrayList<AccommodationViewData>();
      final Map<String, List<AccommodationViewData>> accomTypeAccomViewDataMap =
         new HashMap<String, List<AccommodationViewData>>();
      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPks = brandDetails.getRelevantBrands();
      List<AccommodationModel> accommodationsForLocation =
         accommodationService.getBookableAccommodationsForLocation(locationCode);

      accommodationsForLocation =
         accommodationService.getAccommodationsFilteredByBrand(accommodationsForLocation, brandPks);
      if (CollectionUtils.isNotEmpty(accommodationsForLocation))
      {
         for (final AccommodationModel accommodationModel : accommodationsForLocation)
         {
            if (visibleItems == -1
               || villaViewDataList.size() + hotelViewDataList.size() < visibleItems)
            {
               final AccommodationViewData accommodation =
                  getAccommodationDataForModel(accommodationModel);
               accommodationConfiguredPopulator
                  .populate(accommodationModel, accommodation, options);
               if ("VILLA".equalsIgnoreCase(accommodationModel.getType().toString()))
               {
                  villaViewDataList.add(accommodation);
               }
               else
               {
                  accommodationInfoPopulator.populate(accommodationModel, accommodation);
                  if (viewSelector.checkIsMobile() && accommodation != null)
                  {
                     accommodation.setPriceFrom(getPriceFromEndeca(accommodationModel,
                        accommodation, CarouselLookupType.ENDECA_PLACES_TO_STAY));
                  }
                  if (accommodation != null)
                  {
                     accommodation.setMultiCentre(linkedItemService
                        .isMultiCentre(accommodationModel));

                  }
                  hotelViewDataList.add(accommodation);
               }
            }
            else
            {
               break;
            }
         }
      }
      accomTypeAccomViewDataMap.put("VILLA", villaViewDataList);
      accomTypeAccomViewDataMap.put("HOTEL", hotelViewDataList);
      return accomTypeAccomViewDataMap;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getAccommodationsByAccomType
    * (java.lang.String, int,java.lang.String)
    */
   @Override
   public List<AccommodationViewData> getAccommodationsByAccomType(final String locationCode,
      final int visibleItems, final String accommodationTypeContext)
   {
      final String key =
         keyGenerator.generate("getAccommodationsByAccomType", locationCode,
            Integer.valueOf(visibleItems));
      final CacheWrapper<String, List<AccommodationViewData>> accommodationListCache =
         cacheUtil.getAccommodationListCacheWrapper();
      // get from cache
      List<AccommodationViewData> accomTypeList = accommodationListCache.get(key);

      if (CollectionUtils.isEmpty(accomTypeList))
      {
         accomTypeList = new ArrayList<AccommodationViewData>();
         final Map<String, List<AccommodationViewData>> accomTypeAccomViewDataMap =
            getTHAccommodations(locationCode, visibleItems, Arrays.asList(
               AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO,
               AccommodationOption.PRODUCTRANGE));
         for (final Map.Entry<String, List<AccommodationViewData>> accomType : accomTypeAccomViewDataMap
            .entrySet())
         {
            for (final Iterator<AccommodationViewData> accommodationViewDataIterator =
               accomType.getValue().iterator(); accommodationViewDataIterator.hasNext();)
            {
               final AccommodationViewData accommodationViewData =
                  accommodationViewDataIterator.next();
               if (accommodationViewData.getAccommodationType().toString()
                  .contains(accommodationTypeContext.toUpperCase()))
               {
                  accomTypeList.add(accommodationViewData);
               }
            }

         }
         // put in cache
         accommodationListCache.put(key, accomTypeList);
      }

      return accomTypeList;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getAccommodationsByLocation
    * (java.lang.String)
    */
   @Override
   public List<AccommodationViewData> getAccommodationsByResort(final String locationCode,
      final int visibleItems)
   {

      final String key =
         keyGenerator.generate("getAccommodationsByResort", locationCode,
            Integer.valueOf(visibleItems));
      final CacheWrapper<String, List<AccommodationViewData>> accommodationListCache =
         cacheUtil.getAccommodationListCacheWrapper();
      // get from cache
      List<AccommodationViewData> listOfAccommodations = accommodationListCache.get(key);

      if (CollectionUtils.isEmpty(listOfAccommodations))
      {

         final LocationModel locationModel =
            (LocationModel) categoryService.getCategoryForCode(locationCode);
         if (locationModel.getType().equals(LocationType.RESORT))
         {
            listOfAccommodations = getAccommodations(locationCode, visibleItems);
            // put in cache
            accommodationListCache.put(key, listOfAccommodations);

            return listOfAccommodations;
         }
      }

      return null;
   }

   public List<AccommodationViewData> getNewAccommodationsByResort(final String locationCode,
      final int visibleItems)
   {

      final String key =
         keyGenerator.generate("getAccommodationsByResort", locationCode,
            Integer.valueOf(visibleItems));
      final CacheWrapper<String, List<AccommodationViewData>> accommodationListCache =
         cacheUtil.getAccommodationListCacheWrapper();
      // get from cache
      List<AccommodationViewData> listOfAccommodations = accommodationListCache.get(key);

      if (CollectionUtils.isEmpty(listOfAccommodations))
      {

         final LocationModel locationModel =
            (LocationModel) categoryService.getCategoryForCode(locationCode);
         if (locationModel.getType().equals(LocationType.RESORT)
            || locationModel.getType().equals(LocationType.DESTINATION)
            || locationModel.getType().equals(LocationType.REGION))
         {
            listOfAccommodations = getAccommodations(locationCode, visibleItems);
            // put in cache
            accommodationListCache.put(key, listOfAccommodations);

            return listOfAccommodations;
         }
      }

      return listOfAccommodations;
   }

   @Override
   public List<AccommodationViewData> getAllAccommodationsByLocationFDCodeAndAccomType(
      final LocationModel location, final String noncoreHolidayType)
   {

      final List<AccommodationViewData> accommodations = new ArrayList<AccommodationViewData>();
      List<AccommodationModel> accommodationModels = Collections.emptyList();
      accommodationModels =
         accommodationService.getAccommodationsByFeatureDescriptorCodeAndValuesAndAccomType(
            AccommodationType.SHIP, FEATURE_DESCRIPTOR_CODE, noncoreHolidayType, location,
            cmsSiteService.getCurrentCatalogVersion());

      for (final AccommodationModel accommodationModel : accommodationModels)
      {
         final AccommodationViewData accommodation =
            accommodationConverter.convert(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodation,
            Arrays.asList(AccommodationOption.INTRODUCTION));
         if (CollectionUtils.isNotEmpty(accommodation.getFeatureValues("introduction")))
         {
            accommodation.setCode(accommodationModel.getCode());
            accommodation.setName(accommodationModel.getName());

            final MediaModel thumbnail = accommodationModel.getThumbnail();
            if (thumbnail != null)
            {
               final MediaViewData mediaData = new MediaViewData();
               mediaDataPopulator.populate(thumbnail, mediaData);
               accommodation.setThumbnail(mediaData);

               final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
               featureListImagesPopulator.populate(accommodationModel.getGalleryImages(),
                  imageDataList);
               accommodation.setGalleryImages(imageDataList);
            }
            accommodations.add(accommodation);
         }
      }

      return accommodations;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getAccommodations(java .lang.String,
    * int)
    */
   @Override
   public List<AccommodationViewData> getAccommodations(final String locationCode,
      final int visibleItems)
   {

      final String key =
         keyGenerator.generate("getAccommodations", locationCode, Integer.valueOf(visibleItems));
      final CacheWrapper<String, List<AccommodationViewData>> accommodationListCache =
         cacheUtil.getAccommodationListCacheWrapper();
      // get from cache
      List<AccommodationViewData> listOfAccommodations = accommodationListCache.get(key);

      if (CollectionUtils.isEmpty(listOfAccommodations))
      {

         listOfAccommodations = new ArrayList<AccommodationViewData>();
         List<AccommodationModel> accommodationsForLocation =
            accommodationService.getBookableAccommodationsForLocation(locationCode);

         accommodationsForLocation =
            accommodationService.getAccommodationsFilteredByBrand(accommodationsForLocation,
               tuiUtilityService.getSiteReleventBrandPks());

         if (CollectionUtils.isNotEmpty(accommodationsForLocation))
         {
            for (final AccommodationModel accommodationModel : accommodationsForLocation)
            {
               if (visibleItems == -1 || listOfAccommodations.size() < visibleItems)
               {
                  final AccommodationViewData accommodation =
                     accommodationConverter.convert(accommodationModel);
                  accommodationConfiguredPopulator.populate(accommodationModel, accommodation,
                     Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO,
                        AccommodationOption.T_RATING));
                  accommodationInfoPopulator.populate(accommodationModel, accommodation);
                  if (viewSelector.checkIsMobile())
                  {
                     accommodation.setPriceFrom(getPriceFromEndeca(accommodationModel,
                        accommodation, CarouselLookupType.ENDECA_PLACES_TO_STAY));
                  }
                  listOfAccommodations.add(accommodation);
               }
               else
               {
                  break;
               }
            }
            accommodationListCache.put(key, listOfAccommodations);
         }
      }

      return listOfAccommodations;
   }

   /**
    * @param accommodation
    * @param accommodationModel
    * @return accom price from Endeca.
    *
    *         This method returns the accommodation price from Endeca.
    */
   private String getPriceFromEndeca(final AccommodationModel accommodationModel,
      final AccommodationViewData accommodation, final CarouselLookupType carouselLookupType)
   {

      final ProductFinder productFinder = finders.get("productFinder_" + carouselLookupType);
      final SearchRequestData searchRequestData = new SearchRequestData();
      searchRequestData.setRelevantItem(accommodationModel);
      searchRequestData.setProductCode(accommodation.getCode());
      final AccommodationViewData accom =
         getAccommodationPriceDataFromEndeca((SearchResultData) productFinder.accomSearch(
            searchRequestData, carouselLookupType));
      return accom.getPriceFrom();
   }

   /**
    * @param viewData
    * @param accommodation
    * @return AccommodationViewData
    *
    *         This method returns the media for accommodation Options
    */
   private AccommodationViewData getMediaDataForAccommodationOptions(
      final AccommodationViewData viewData, final AccommodationModel accommodation)
   {

      viewData.setGalleryImages(getMediaViewDatas(accommodation));
      return viewData;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getAccommodationAwardData
    * (java.lang.String)
    */
   @Override
   public SustainableTourismComponentViewData getAccommodationTravelLifeAwardInfo(
      final String accommodationCode)
   {
      final SustainableTourismComponentViewData sustainableTourismViewData =
         new SustainableTourismComponentViewData();
      sustainableTourismViewData.setDescription("");
      sustainableTourismViewData.setTitle("");

      if (accommodationCode != null && !accommodationCode.isEmpty())
      {
         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         final AccommodationViewData accommodationViewData =
            accommodationConverter.convert(accommodationModel);
         accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
            Arrays.asList(AccommodationOption.AWARD_INFO));
         final Map<String, List<Object>> viewData =
            accommodationViewData.getFeatureCodesAndValues();
         if (viewData != null)
         {
            for (final Entry<String, List<Object>> entry : viewData.entrySet())
            {
               final String desc = entry.getKey();

               if ("FCU044".equalsIgnoreCase(desc) && !(entry.getValue().isEmpty()))
               {
                  sustainableTourismViewData.setTitle("TRAVELIFE BRONZE");
                  sustainableTourismViewData.setDescription(entry.getValue().get(0).toString());
               }
               else if ("FCU045".equalsIgnoreCase(desc) && !(entry.getValue().isEmpty()))
               {
                  sustainableTourismViewData.setTitle("TRAVELIFE GOLD");
                  sustainableTourismViewData.setDescription(entry.getValue().get(0).toString());
               }
               else if ("FCU046".equalsIgnoreCase(desc) && !(entry.getValue().isEmpty()))
               {
                  sustainableTourismViewData.setTitle("TRAVELIFE SILVER");
                  sustainableTourismViewData.setDescription(entry.getValue().get(0).toString());
               }
               else
               {
                  continue;
               }

            }
         }
      }

      return sustainableTourismViewData;

   }

   /**
    * Method to add destination details in deals for mobile session.
    *
    * @param accommodation
    * @param viewData
    */
   private void setDestinationForAccommodationForMobile(final AccommodationModel accommodation,
      final AccommodationViewData viewData)
   {

      final AccommodationModel accommodationModel = accommodation;
      final AccommodationViewData accommodationViewData = viewData;
      if (accommodationModel != null && accommodationModel.getSupercategories() != null)
      {
         for (final CategoryModel categorys : accommodationModel.getSupercategories())
         {
            if (categorys instanceof LocationModel)
            {
               for (final CategoryModel category : categorys.getAllSupercategories())
               {
                  if (category instanceof LocationModel && isLocationModelCheck(category)
                     && accomViewDataCheck(accommodationViewData))
                  {
                     accommodationViewData.getCategories().add(categoryConverter.convert(category));

                  }
               }
            }
         }
      }
   }

   /**
    * Checks if is location model check.
    *
    * @param category the category
    * @return true, if checks if is location model check
    */
   private boolean isLocationModelCheck(final CategoryModel category)
   {
      return "DESTINATION".equals(((LocationModel) category).getType().toString())
         || "REGION".equals(((LocationModel) category).getType().toString());
   }

   /**
    * @param accommodationViewData
    * @return boolean
    */
   private boolean accomViewDataCheck(final AccommodationViewData accommodationViewData)
   {
      return accommodationViewData != null && accommodationViewData.getCategories() != null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getVillaDetailsComponentData
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccomWithClassifiedFacilityList(final String accomCode)
   {
      final AccommodationViewData accommodationData = populateBasicFacilities(accomCode);
      // populate basic facilities
      // villa layout facilities
      final List<FacilityViewData> villaLayoutFacility = new ArrayList<FacilityViewData>();
      final List<FacilityViewData> facilityPriorityList = new ArrayList<FacilityViewData>();
      boolean flag = true;
      for (final FacilityViewData facility : accommodationData.getFacilities())
      {
         // "FT000V" is villa layout code
         if (StringUtils.equalsIgnoreCase("FT000V", facility.getParentFacilityType()))
         {
            villaLayoutFacility.add(facility);
         }
         if (flag && CollectionUtils.isNotEmpty(facility.getGalleryImages()))
         {
            facilityPriorityList.add(facility);
         }
         if (flag && facilityPriorityList.size() == TWO)
         {
            flag = false;
         }
      }
      accommodationData.setVillaLayoutFacility(villaLayoutFacility);

      // removing villa layout facilities
      accommodationData.getFacilities().removeAll(villaLayoutFacility);

      accommodationData.setPriorityBasedFacilityData(facilityPriorityList);

      // removing priotitized facilities, now it has only other facilities
      accommodationData.getFacilities().removeAll(facilityPriorityList);

      return accommodationData;
   }

   @Override
   // @Cacheable(cacheName = "advertisedPackagesCache", keyGenerator =
   // @KeyGenerator(name = GENERIC_KEY_GENERATOR))
   public List<AccommodationViewData> getAdvertisedPackagesCollectionData(
      final AdvertisedPackagesCollectionComponentModel advertisedPackagesCollectionComponent)
   {

      final List<String> accommList = new ArrayList<String>();
      final List<AccommodationViewData> accommData = new ArrayList<AccommodationViewData>();

      if (advertisedPackagesCollectionComponent != null
         && advertisedPackagesCollectionComponent.getVisibleItems() != null)
      {
         if (advertisedPackagesCollectionComponent.getAccommodation1() != null)
         {
            accommList.add(advertisedPackagesCollectionComponent.getAccommodation1());
         }
         if (advertisedPackagesCollectionComponent.getAccommodation2() != null)
         {
            accommList.add(advertisedPackagesCollectionComponent.getAccommodation2());
         }
         if (advertisedPackagesCollectionComponent.getAccommodation3() != null)
         {
            accommList.add(advertisedPackagesCollectionComponent.getAccommodation3());
         }
         if (advertisedPackagesCollectionComponent.getAccommodation4() != null)
         {
            accommList.add(advertisedPackagesCollectionComponent.getAccommodation4());
         }
         if (advertisedPackagesCollectionComponent.getAccommodation5() != null)
         {
            accommList.add(advertisedPackagesCollectionComponent.getAccommodation5());
         }
         if (!accommList.isEmpty())
         {
            final ProductFinder productFinder =
               finders.get("productFinder_" + CarouselLookupType.ENDECA_GET_PRICE);
            final SearchRequestData searchRequestData = new SearchRequestData();
            searchRequestData.setProductCodes(accommList);
            final SearchResultData<ResultData> searchResultData =
               (SearchResultData<ResultData>) productFinder.priceForRecSearch(searchRequestData);
            if (searchResultData != null && searchResultData.getResults() != null)
            {
               for (final ResultData resultData : searchResultData.getResults())
               {
                  final AccommodationModel accommModel =
                     getAccommodationForCode(resultData.getCode());
                  if (accommModel != null)
                  {
                     final AccommodationViewData accommodationViewData =
                        getAccommodationByCode(resultData.getCode());
                     getMediaDataForAccommodationOptions(accommodationViewData, accommModel);
                     accommodationViewData.setPriceFrom(resultData.getPriceFrom());
                     accommodationViewData.setName(resultData.getName());
                     accommData.add(accommodationViewData);
                  }
               }
            }
         }

      }

      return accommData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getAccommodationDisclaimer
    * (java.lang.String)
    */
   @Override
   public AccommodationViewData getAccommodationSpecialOffer(final String accommodationCode)
   {

      AccommodationViewData accommodationViewData = null;
      final String key = keyGenerator.generate("getAccommodationSpecialOffer", accommodationCode);
      final CacheWrapper<String, AccommodationViewData> accomDataCache =
         cacheUtil.getAccommodationDataCacheWrapper();
      // get from cache
      accommodationViewData = accomDataCache.get(key);

      if (accommodationViewData == null && StringUtils.isNotBlank(accommodationCode))
      {

         final AccommodationModel accommodationModel = getAccommodationForCode(accommodationCode);
         if (accommodationModel != null)
         {
            accommodationViewData = getAccommodationDataForModel(accommodationModel);
            accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
               Arrays.asList(AccommodationOption.SPECIALOFFER));
            // put in cache
            accomDataCache.put(key, accommodationViewData);

         }
      }

      return accommodationViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getEssentialInformationData
    * (java.lang.String)
    */
   @Override
   public EssentialInformationViewData getEssentialInformationData(final String accommodationCode,
      final boolean multiStay)
   {

      final EssentialInformationViewData essentialData = new EssentialInformationViewData();
      List<AccommodationModel> accommodations = new ArrayList<AccommodationModel>();

      if (multiStay)
      {
         accommodations = linkedItemService.getLinkedItemsOfAccommodationType(accommodationCode);
      }
      else
      {
         accommodations.add(getAccommodationForCode(accommodationCode));
      }
      if (CollectionUtils.isNotEmpty(accommodations))
      {
         final List<ItineraryLeg> itineraryLegs = new ArrayList<ItineraryLeg>();
         essentialInfoPopulator.populate(accommodations, itineraryLegs);
         essentialData.setEssentialInfo(itineraryLegs);
      }

      return essentialData;
   }

   @Override
   public boolean isNonBookableUnit(final String accommodationCode)
   {
      AccommodationModel accommodation = null;
      try
      {
         accommodation = (AccommodationModel) productService.getProductForCode(accommodationCode);
         Validate.notNull(accommodation);
      }
      catch (final ModelNotFoundException exc)
      {
         LOG.error("The Product model with code " + accommodationCode + " does not exist in PIM.",
            exc);
         return true;
      }
      return accommodationService.isNonBookableUnit(accommodation);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#
    * updateFacilitesUrlForMultiCentre(java.lang.String,
    * uk.co.portaltech.tui.web.view.data.AccommodationViewData)
    */
   @Override
   public String updateViewAllUrlForMultiCentre(final String multiCentreCode, final String tabName,
      final String tabCount)
   {

      final StringBuilder tab = new StringBuilder();
      tab.append(HOTEL).append(tabName).append(STAY).append(tabCount);

      final AccommodationModel multiCentreModel = getAccommodationForCode(multiCentreCode);
      tuiProductUrlResolver.setOverrideSubPageType(tab.toString());

      return tuiProductUrlResolver.resolve(multiCentreModel);

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccommodationFacade#getRecommendedAccomPriceInfo
    * (uk.co.portaltech.tui.web.view.data .SearchResultData, java.util.List)
    */
   @Override
   public List<AccommodationViewData> getRecommendedAccomPriceInfo(
      final SearchResultData<ResultData> searchResultData, final List<String> productCodes)
   {
      final List<AccommodationViewData> accomSearchViewData =
         new ArrayList<AccommodationViewData>();

      if (searchResultData != null && searchResultData.getResults() != null)
      {
         for (final ResultData resultData : searchResultData.getResults())
         {

            final AccommodationModel accommModel = getAccommodationForCode(resultData.getCode());

            AccommodationViewData accommodationViewData;
            accommodationViewData = getAccommodationDataForModel(accommModel);

            accommodationViewData.setPriceFrom(resultData.getPriceFrom());
            accommodationViewData.setAvailableFrom(resultData.getAvailableFrom());
            if (!StringUtils.isEmpty(resultData.getDeparturePoint()))
            {
               accommodationViewData.setDeparturePoint(airportService.getAirportByCode(
                  resultData.getDeparturePoint()).getName());
            }

            accommodationViewData.setStayPeriod(resultData.getStayPeriod());
            accommodationViewData.setName(resultData.getName());
            accommodationViewData.settRating(resultData.gettRating());
            accommodationViewData.setBoardBasis(resultData.getBoardBasis());
            accommodationViewData.setAvailableFrom(resultData.getAvailableFrom());
            accommodationViewData.setCode(resultData.getCode());

            accommodationViewData.setCommercialPriority(resultData.getCommercialPriority());
            accommodationViewData.setStayPeriod(resultData.getStayPeriod());
            accomSearchViewData.add(accommodationViewData);
         }
      }

      return accomSearchViewData;
   }

}
