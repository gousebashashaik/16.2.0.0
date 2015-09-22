package uk.co.portaltech.tui.facades.impl;

import static uk.co.portaltech.commons.Collections.map;
import static uk.co.portaltech.commons.Collections.toList;
import static uk.co.portaltech.commons.DateUtils.toDate;
import static uk.co.portaltech.tui.web.view.data.wrapper.ProductData.toProductData;
import static uk.co.portaltech.tui.web.view.data.wrapper.UnitData.toUnitData;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.commons.Collections.MapFn;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.unit.Unit;
import uk.co.portaltech.travel.model.unit.Units;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.travel.services.airport.AirportServiceException;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.travel.thirdparty.endeca.FacetOption;
import uk.co.portaltech.travel.thirdparty.endeca.FacetValue;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.CountryViewDataConverter;
import uk.co.portaltech.tui.converters.DestinationConverter;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.converters.MostPopularMultipleDestinationConverter;
import uk.co.portaltech.tui.converters.MostPopularSingleDestinationConverter;
import uk.co.portaltech.tui.converters.ProductDestinationConverter;
import uk.co.portaltech.tui.converters.ProductToDestinationConverter;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.NonGeoItemFacade;
import uk.co.portaltech.tui.helper.DeepLinkHelper;
import uk.co.portaltech.tui.model.CollectionToBestForModel;
import uk.co.portaltech.tui.resolvers.TuiCategoryUrlResolver;
import uk.co.portaltech.tui.services.HolidayTypeService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.ArticleViewData;
import uk.co.portaltech.tui.web.view.data.BestForViewData;
import uk.co.portaltech.tui.web.view.data.CountryViewData;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.DestinationGuideCollectionViewData;
import uk.co.portaltech.tui.web.view.data.DestinationGuideViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MostPopularDestinationdata;
import uk.co.portaltech.tui.web.view.data.SearchBDestinationGuideViewData;
import uk.co.portaltech.tui.web.view.data.SearchError;
import uk.co.portaltech.tui.web.view.data.SearchPanelCollectionViewData;
import uk.co.portaltech.tui.web.view.data.SuggestionViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.DestinationSearchResult;
import uk.co.portaltech.tui.web.view.data.wrapper.LocationCategoryResult;
import uk.co.portaltech.tui.web.view.data.wrapper.LocationSearchResult;
import uk.co.portaltech.tui.web.view.data.wrapper.ProductData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.portaltech.util.CatalogUtil;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author omonikhide
 *
 */
public class DefaultLocationFacade implements LocationFacade
{
   /**
     *
     */
   private static final int FIVE = 5;

   /**
     *
     */
   private static final String RECOMMENDED = "recommended";

   private static final String RESORT = "RESORT";

   private static final String COUNTRY = "COUNTRY";

   private static final String DESTINATION = "DESTINATION";

   private static final String HOTEL = "HOTEL";

   private static final String APARTMENT_HOTEL = "APARTMENT_HOTEL";

   private static final String NILE_CRUISE = "NileCruise";

   private static final String GULET_CRUISE = "GuletCruise";

   private static final String SAFARI = "ThemePark";

   private static final String DAY_TRIP = "DayTrip";

   private static final String TWIN_OR_MULTI_CENTER = "Twin_MultiCentre";

   private static final String NILE_CRUISE_AND_STAY = "NileCruiseandStay";

   private static final String GULET_CRUISE_AND_STAY = "GuletCruiseandStay";

   private static final String TOUR_AND_STAY = "TourandStay";

   private static final String COUNTRY_LAPLAND = "country.lapland";

   private static final String TRUE_VALUE = "true";

   private static final String BOXING = "boxing";

   private static final String NO_MATCH_FOUND_FOR_SEARCH_KEY = "No match found for the search key";

   private static final String NOT_A_LAPLAND_SPECIFIC_SEARCH = "Not a lapland specific search";

   private static final String REGION = "REGION";

   private static final String OVERVIEW = "overview";

   private static final String FALSE_VALUE = "false";

   private static final String PRODUCT_RANGE = "ProductRange";

   private static final String VILLA = "VILLA";

   private static final String ZERO = "0";

   private static final String REGEX = "^[A-Za-z0-9]{3,6}[&]{1}[A-Za-z0-9]{3,6}$";

   private static final String REGEX_FOR_LOCATION = "^[A-Za-z0-9]{3,6}$";

   private static final String AMP = "&";

   private static final String DESTINATION_GUIDE_CONTINENTS_DEFAULT_OREDER =
      "EEE,AAA,BBB,FFF,STO,CCC";

   private static final String ANY_WHERE_SEARCH = "anyWhereSearch";

   private static final String CONTINENT_AND_COUNTRY_HIERARCHY_CACHE =
      "continentAndCountryHierarchyCache";

   private static final String DESTINATION_GUIDE_CONTINENT_ORDER =
      ".DestinationGuide.ContinentOrder";

   @Resource
   private HolidayTypeService holidayTypeService;

   @Resource
   private DefaultAccomodationFacade accomodationFacade;

   @Resource
   private CategoryService categoryService;

   @Resource
   private LocationService tuiLocationService;

   @Resource(name = "mainStreamTravelLocationService")
   private MainStreamTravelLocationService mstravelLocationService;

   @Resource
   private ProductService productService;

   @Resource
   private LocationConverter locationConverter;

   @Resource
   private ProductDestinationConverter converter;

   @Resource
   private CountryViewDataConverter countryViewDataConverter;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private DestinationConverter destinationConverter;

   @Resource
   private ConfigurablePopulator<LocationModel, LocationData, LocationOption> defaultLocationConfiguredPopulator;

   @Resource
   private Converter<ArticleModel, ArticleViewData> articleConverter;

   @Resource
   private Populator<ArticleModel, ArticleViewData> articlePopulator;

   @Resource
   private ProductToDestinationConverter prodToDestConverter;

   @Resource
   private TuiCategoryUrlResolver tuiCategoryModelUrlResolver;

   @Resource
   private CMSComponentService cmsComponentService;

   private Populator<LocationModel, LocationData> locationBasicPopulator;

   @Resource
   private SessionService sessionService;

   @Resource(name = "tuiLocationService")
   private LocationService locationService;

   @Resource
   private CatalogUtil catalogUtil;

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource(name = "deepLinkHelper")
   private DeepLinkHelper deepLinkHelper;

   @Resource(name = "airportsService")
   private AirportService airportService;

   @Resource(name = "siteAwareKeyGenerator")
   private SiteAwareKeyGenerator keyGenerator;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private NonGeoItemFacade nonGeoItemFacade;

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private MostPopularMultipleDestinationConverter mostPopularMultipleDestinationConverter;

   @Resource
   private MostPopularSingleDestinationConverter mostPopularSingleDestinationConverter;

   private static final String NO_ROUTE_FOUND_FROM = "INVALID_ROUTE_FROM";

   private static final String INVALID_ROUTE_ON = "INVALID_ROUTE_ON";

   private static final String NO_MATCH_FOUND = "NO_MATCH_FOUND";

   private static final String MULTI_SELECT_FALSE = "MULTI_SELECT_FALSE";

   private static final String MULTI_SELECT_TRUE = "MULTI_SELECT_TRUE";

   private static final String COLLECTIONS = "Collections";

   private static final String SEARCH_PANELCOMPONENT_ID = "WF_COM_300B";

   private static final String ICELAND_RESORT_CODES = "iceland.relatedcodes";

   private static final String DESTINATION_GUIDE_COLLECTION_PAGE_URL =
      "destinationguide.collectionspage.url";

   private static String icelandResortCodes;

   private static String lapLandCode;

   private static final String COUNTRIES = "Countries";

   private static final TUIConfigService TUI_CONFIG_SERVICE = (TUIConfigService) Registry
      .getApplicationContext().getBean("tuiConfigService");

   private static final TUILogUtils LOG = new TUILogUtils("DefaultLocationFacade");

   private final MapFn<String, LocalDate> locateDateMapper = new MapFn<String, LocalDate>()
   {
      @Override
      public LocalDate call(final String date)
      {
         return toDate(date);
      }
   };

   private final MapFn<Unit, UnitData> unitToUnitDataMapper = new MapFn<Unit, UnitData>()
   {
      @Override
      public UnitData call(final Unit input)
      {
         return toUnitData(input);

      }
   };

   private final MapFn<UnitData, ProductData> unitDataToProductDataMapper =
      new MapFn<UnitData, ProductData>()
      {
         @Override
         public ProductData call(final UnitData input)
         {
            return toProductData(input);
         }
      };

   /*
    * This static block used to get iceland and related codes from PIM for once in a class level
    */
   static
   {

      icelandResortCodes = TUI_CONFIG_SERVICE.getConfigValue(ICELAND_RESORT_CODES);
      lapLandCode = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);
   }

   /**
    * @param destinationConverter
    * @param prodToDestConverter
    */
   public DefaultLocationFacade(final DestinationConverter destinationConverter,
                                final ProductToDestinationConverter prodToDestConverter,
                                final ProductDestinationConverter converter)
   {
      this.prodToDestConverter = prodToDestConverter;
      this.destinationConverter = destinationConverter;
      this.converter = converter;
   }

   /**
    * @param mstravelLocationService
    */
   public DefaultLocationFacade(final MainStreamTravelLocationService mstravelLocationService)
   {
      this.mstravelLocationService = mstravelLocationService;

   }

   public DefaultLocationFacade()
   {

   }

   /*
    * setter method for setting mstravelLocationService
    */
   public void setMstravelLocationService(
      final MainStreamTravelLocationService mstravelLocationService)
   {
      this.mstravelLocationService = mstravelLocationService;
   }

   @Required
   public void setLocationBasicPopulator(
      final Populator<LocationModel, LocationData> locationBasicPopulator)
   {
      this.locationBasicPopulator = locationBasicPopulator;
   }

   /**
    * @param locationModel
    * @return
    */
   private LocationData getLocationDataFromModel(final LocationModel locationModel)
   {
      LocationData locationData;
      locationData = locationConverter.convert(locationModel);
      return locationData;
   }

   /**
    * @param locationCode
    * @return
    */
   private LocationModel getLocationForCode(final String locationCode)
   {
      return (LocationModel) categoryService.getCategoryForCode(locationCode);
   }

   @Override
   public LocationData getHealthAndSafetyEditorial(final String locationCode)
   {

      final String key = keyGenerator.generate("getHealthAndSafetyEditorial", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.HEALTHANDSAFETY));
         // put in cache
         locationDataCache.put(key, locationData);

      }

      return locationData;
   }

   @Override
   public LocationData getPassportAndVisaEditorial(final String locationCode)
   {

      final String key = keyGenerator.generate("getPassportAndVisaEditorial", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.PASSPORTANDVISA));
         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;
   }

   @Override
   public LocationData getWhenToGoEditorial(final String locationCode)
   {

      final String key = keyGenerator.generate("getWhenToGoEditorial", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {
         final BrandDetails brandDetails =
            sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> brandPks = brandDetails.getRelevantBrands();
         LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.WHENTOGO));

         if (!LocationType.COUNTRY.equals(locationModel.getType())
            && CollectionUtils.isEmpty(locationData.getFeatureValues("whenToGo")))
         {
            locationModel =
               tuiLocationService.getLocationForItem(locationModel, LocationType.COUNTRY, brandPks);
            locationData = getLocationDataFromModel(locationModel);
            defaultLocationConfiguredPopulator.populate(locationModel, locationData,
               Arrays.asList(LocationOption.WHENTOGO));
         }

         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;
   }

   @Override
   public LocationData getGettingAroundEditorial(final String locationCode)
   {

      final String key = keyGenerator.generate("getGettingAroundEditorial", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.GETTINGAROUND));

         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getWhereToGo(java.lang.String )
    */
   @Override
   public LocationData getWhereToGoData(final String locationCode)
   {

      final String key = keyGenerator.generate("getWhereToGoData", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.CONTENT));

         // put in cache
         locationDataCache.put(key, locationData);

      }

      return locationData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getThumbnailMapData(java. lang.String)
    */
   @Override
   public LocationData getThumbnailMapData(final String locationCode)
   {

      final String key = keyGenerator.generate("getThumbnailMapData", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.THUMBNAILMAP));
         // put in cache
         locationDataCache.put(key, locationData);
      }

      return locationData;
   }

   @Override
   public List<LocationData> getChildLocationsData(final String locationCode,
      final int visibleItems, final String accommodationTypeContext)
   {

      final String key =
         keyGenerator.generate("getChildLocationsData1", locationCode,
            Integer.valueOf(visibleItems), accommodationTypeContext);
      // get from cache
      final CacheWrapper<String, List<LocationData>> locationDataCache =
         cacheUtil.getLocationListCacheWrapper();
      List<LocationData> childLocationsData = locationDataCache.get(key);
      List<LocationModel> subLocationModels = new ArrayList<LocationModel>();

      if (CollectionUtils.isEmpty(childLocationsData))
      {
         final LocationModel locationModel = getLocationForCode(locationCode);
         childLocationsData = new ArrayList<LocationData>();
         // filtering by brand
         final BrandDetails brandDetails =
            (BrandDetails) sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> relevantBrands = brandDetails.getRelevantBrands();
         for (final CategoryModel categoryModel : locationModel.getCategories())
         {
            if (categoryModel instanceof LocationModel)
            {
               subLocationModels.add((LocationModel) categoryModel);
            }
         }
         subLocationModels =
            tuiLocationService.getLocationsFilteredByBrand(subLocationModels, relevantBrands);
         getIterateSubLocation(visibleItems, key, locationDataCache, childLocationsData,
            subLocationModels);
      }

      return childLocationsData;
   }

   /**
    * @param visibleItems
    * @param key
    * @param locationDataCache
    * @param childLocationsData
    * @param subLocationModels
    */
   private void getIterateSubLocation(final int visibleItems, final String key,
      final CacheWrapper<String, List<LocationData>> locationDataCache,
      final List<LocationData> childLocationsData, final List<LocationModel> subLocationModels)
   {
      if (subLocationModels != null)
      {
         final Iterator<LocationModel> childLocationsItr = subLocationModels.iterator();
         while (childLocationsItr.hasNext())
         {
            if (childLocationsData.size() < visibleItems)
            {
               final LocationModel currentChildLocationModel = childLocationsItr.next();
               final LocationData childLocationData =
                  locationConverter.convert(currentChildLocationModel);
               locationBasicPopulator.populate(currentChildLocationModel, childLocationData);
               checkForNullPoc(currentChildLocationModel, childLocationData);

               childLocationsData.add(childLocationData);
            }
            else
            {
               break;
            }
         }
         // put in cache
         locationDataCache.put(key, childLocationsData);
      }
   }

   /**
    * @param currentChildLocationModel
    * @param childLocationData
    */
   private void checkForNullPoc(final LocationModel currentChildLocationModel,
      final LocationData childLocationData)
   {
      if (currentChildLocationModel.getIsPOC() != null)
      {
         childLocationData.setPoc(currentChildLocationModel.getIsPOC().booleanValue());
      }
   }

   @Override
   public List<LocationData> getChildLocationsData(final String locationCode, final int visibleItems)
   {

      final String key =
         keyGenerator.generate("getChildLocationsData2", locationCode,
            Integer.valueOf(visibleItems));
      // get from cache
      final CacheWrapper<String, List<LocationData>> locationDataCache =
         cacheUtil.getLocationListCacheWrapper();
      List<LocationData> childLocationsData = locationDataCache.get(key);

      if (CollectionUtils.isEmpty(childLocationsData))
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         childLocationsData = new ArrayList<LocationData>();
         if (locationModel != null && locationModel.getCategories() != null)
         {
            final Iterator<CategoryModel> childLocationsItr =
               locationModel.getCategories().iterator();

            iterateChildLocationData(visibleItems, childLocationsData, childLocationsItr);
            // put in cache
            locationDataCache.put(key, childLocationsData);
         }
      }

      return childLocationsData;
   }

   /**
    * @param visibleItems
    * @param childLocationsData
    * @param childLocationsItr
    */
   private void iterateChildLocationData(final int visibleItems,
      final List<LocationData> childLocationsData, final Iterator<CategoryModel> childLocationsItr)
   {
      while (childLocationsItr.hasNext())
      {
         if (childLocationsData.size() < visibleItems)
         {
            final LocationModel currentChildLocationModel =
               (LocationModel) childLocationsItr.next();
            final LocationData childLocationData =
               locationConverter.convert(currentChildLocationModel);
            locationBasicPopulator.populate(currentChildLocationModel, childLocationData);
            childLocationsData.add(childLocationData);
         }
         else
         {
            break;
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getLocationKeyFactsData(java .lang.String)
    */
   @Override
   public LocationData getLocationKeyFactsData(final String locationCode)
   {

      final String key = keyGenerator.generate("getLocationKeyFactsData", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.KEY_FACTS));
         // put in cache
         locationDataCache.put(key, locationData);

      }

      return locationData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getArticlesForTheLocation (java.lang.String)
    */
   @Override
   public LocationData getArticlesForTheLocation(final String locationCode)
   {

      final String key = keyGenerator.generate("getArticlesForTheLocation", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         final List<ArticleModel> articles = locationModel.getArticles();
         if (articles != null && !articles.isEmpty())
         {
            final List<ArticleViewData> articleViewDataList = new ArrayList<ArticleViewData>();
            iterateArticleViewDataList(articles, articleViewDataList);
            locationData.setArticles(articleViewDataList);
         }
         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;

   }

   /**
    * @param articles
    * @param articleViewDataList
    */
   private void iterateArticleViewDataList(final List<ArticleModel> articles,
      final List<ArticleViewData> articleViewDataList)
   {
      for (final ArticleModel articleModel : articles)
      {
         final ArticleViewData articleViewData = articleConverter.convert(articleModel);
         articlePopulator.populate(articleModel, articleViewData);
         articleViewDataList.add(articleViewData);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getThingsToDoAndSeeEditorial
    * (java.lang.String)
    */
   @Override
   public LocationData getThingsToDoAndSeeEditorial(final String locationCode)
   {

      final String key = keyGenerator.generate("getThingsToDoAndSeeEditorial", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.CONTENT));

         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;
   }

   /*
    * Adds the features codes and value with the tab data.
    */
   @Override
   public LocationData getThingsToSeeAndDoEditorial(final String locationCode)
   {
      final LocationModel locationModel = getLocationForCode(locationCode);
      final LocationData locationData = getLocationDataFromModel(locationModel);
      defaultLocationConfiguredPopulator.populate(locationModel, locationData,
         Arrays.asList(LocationOption.THINGSTOSEEANDDO));

      return locationData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getLOcationData(java.lang .String)
    */
   @Override
   public LocationData getLocationData(final String locationCode)
   {

      final String key = keyGenerator.generate("getLocationData", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         try
         {
            final CategoryModel category = categoryService.getCategoryForCode(locationCode);
            if (category instanceof LocationModel)
            {
               final LocationModel location = (LocationModel) category;
               locationData = getLocationDataFromModel(location);
               defaultLocationConfiguredPopulator.populate(location, locationData,
                  Arrays.asList(LocationOption.BASIC, LocationOption.CONTENT));
            }
         }
         catch (final UnknownIdentifierException notFound)
         {
            LOG.warn("NO Location not for code = " + locationCode, notFound);
         }

         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getLocationHighlights(java .lang.String,
    * java.lang.Integer)
    */
   @Override
   public LocationData getLocationHighlights(final String locationCode,
      final Integer highlightsNumber)
   {

      final String key =
         keyGenerator.generate("getLocationHighlights", locationCode, highlightsNumber);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.HIGHLIGHTS));
         getLocationDataFeatureValues(highlightsNumber, locationData);

         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;
   }

   /**
    * @param highlightsNumber
    * @param locationData
    */
   private void getLocationDataFeatureValues(final Integer highlightsNumber,
      final LocationData locationData)
   {
      if (highlightsNumber != null)
      {
         final int highlightsSize = highlightsNumber.intValue();
         final List<Object> list = locationData.getFeatureValues("usps");
         if (list != null && list.size() > highlightsSize)
         {
            locationData.putFeatureValue("usps", list.subList(0, highlightsSize));
         }
      }
   }

   @Override
   public boolean areProductsAvailableInLocation(final String locationCode)
   {
      return areProductsAvailableInLocation((LocationModel) categoryService
         .getCategoryForCode(locationCode));
   }

   @Override
   public boolean areProductsAvailableInLocation(final LocationModel locationModel)
   {
      if (LocationType.RESORT.equals(locationModel.getType())
         || LocationType.DESTINATION.equals(locationModel.getType()))
      {
         return !locationModel.getProducts().isEmpty();
      }
      else
      {
         return isProductAvailable(locationModel);
      }
   }

   /**
    * @param locationModel
    * @return boolean
    */
   private boolean isProductAvailable(final LocationModel locationModel)
   {
      boolean productAvailable = false;
      final Iterator<CategoryModel> subCategoriesItr =
         locationModel.getAllSubcategories().iterator();
      while (subCategoriesItr.hasNext())
      {
         final LocationModel currentCategory = (LocationModel) subCategoriesItr.next();
         if ((LocationType.RESORT.equals(currentCategory.getType()) || LocationType.DESTINATION
            .equals(currentCategory.getType())) && !currentCategory.getProducts().isEmpty())
         {
            productAvailable = true;
            return productAvailable;
         }

      }
      return productAvailable;
   }

   @Override
   public LocationData getLocationInteractiveMapData(final String locationCode)
   {

      final String key = keyGenerator.generate("getLocationInteractiveMapData", locationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final LocationModel locationModel = getLocationForCode(locationCode);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.SUBCATEGORIES));

         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getEndecaResortViewData(java .util.List)
    */
   @Override
   public List<LocationData> getEndecaResortViewData(final List<ResultData> resortsResultData)
   {
      final List<LocationData> locationDataLst = new ArrayList<LocationData>();
      for (final ResultData resultData : resortsResultData)
      {
         final FacetOption dimensionNavigationData = resultData.getFacetOption();
         if (dimensionNavigationData != null)
         {
            final List<FacetValue> dimensionRefinementLst =
               dimensionNavigationData.getFacetValues();
            iterateDimensionRefinementList(locationDataLst, dimensionRefinementLst);

         }

      }
      if (locationDataLst.isEmpty())
      {
         return Collections.emptyList();
      }
      return locationDataLst;
   }

   /**
    * @param locationDataLst
    * @param dimensionRefinementLst
    */
   private void iterateDimensionRefinementList(final List<LocationData> locationDataLst,
      final List<FacetValue> dimensionRefinementLst)
   {
      for (final FacetValue dimensionRefinementData : dimensionRefinementLst)
      {
         final String code = dimensionRefinementData.getCode();
         if (StringUtils.isEmpty(code))
         {
            continue;
         }
         final LocationData locationData = getLocationData(code);
         if (locationData != null)
         {
            locationDataLst.add(locationData);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getLocationForItem(java.lang .String)
    */
   @Override
   public LocationData getLocationForAccommodation(final String accommodationCode)
   {

      final String key = keyGenerator.generate("getLocationForAccommodation", accommodationCode);
      // get from cache
      final CacheWrapper<String, LocationData> locationDataCache =
         cacheUtil.getLocationDataCacheWrapper();
      LocationData locationData = locationDataCache.get(key);

      if (locationData == null)
      {

         final AccommodationModel accommodation =
            (AccommodationModel) productService.getProductForCode(accommodationCode);
         final BrandDetails brandDetails =
            sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> brandPks = brandDetails.getRelevantBrands();
         final LocationModel locationModel =
            tuiLocationService.getLocationForItem(accommodation, LocationType.RESORT, brandPks);
         locationData = getLocationDataFromModel(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.BASIC));

         // put in cache
         locationDataCache.put(key, locationData);

      }
      return locationData;
   }

   /*
    * This method gets all the countries.
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getAllCountries()
    */
   @Override
   public List<DestinationData> getAllCountries(final List<String> releventBrands,
      final String multiSelect)
   {
      final List<DestinationData> destinationDataList = new ArrayList<DestinationData>();
      final String laplandCode = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);
      final List<LocationModel> locationModelList =
         mstravelLocationService.getAllCountries(releventBrands);
      for (final LocationModel locationModel : locationModelList)
      {
         final DestinationData locationData = destinationConverter.convert(locationModel);
         if (StringUtils.equalsIgnoreCase(laplandCode, locationData.getId())
            && StringUtils.equalsIgnoreCase(FALSE_VALUE, multiSelect))
         {

            locationData.setMultiSelect(false);
            destinationDataList.add(locationData);
         }
         else if (StringUtils.equalsIgnoreCase(TRUE_VALUE, multiSelect))
         {
            if (!StringUtils.equalsIgnoreCase(laplandCode, locationData.getId()))
            {
               destinationDataList.add(locationData);
            }

         }
         else if (StringUtils.isEmpty(multiSelect))
         {
            destinationDataList.add(locationData);
         }

      }
      return sortDestinationData(destinationDataList);
   }

   /*
    * This method gets the country and its child categories up to destination level.
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getChildCategories(java.lang .String)
    */
   @Override
   public CountryViewData getChildCategories(final String key, final List<String> releventBrandPks)
   {
      CountryViewData resultCountryData = null;
      final String laplandCode = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);
      List<LocationModel> subList = null;

      final LocationModel country = mstravelLocationService.getCountry(key, releventBrandPks);
      resultCountryData = countryViewDataConverter.convert(country);
      tuiCategoryModelUrlResolver.setOverrideSubPageType(OVERVIEW);
      final String url = tuiCategoryModelUrlResolver.resolve(country);
      String imageurl = null;
      if (country.getThumbnail() != null)
      {
         imageurl = country.getThumbnail().getURL();
      }
      subList = (List) (country.getCategories());
      if (resultCountryData != null && CollectionUtils.isNotEmpty(subList))
      {
         if (!subList.get(0).getType().equals(LocationType.REGION))
         {
            final List<DestinationData> destinationDataList = toDestinationDataList(subList);
            resultCountryData.setChildren(sortDestinationData(destinationDataList));
         }
         else
         {
            final List<DestinationData> regionsData = new ArrayList<DestinationData>();

            // get all region models and convert into region data
            iterateRegionData(subList, regionsData);
            resultCountryData.setChildren(sortDestinationData(regionsData));
         }
         resultCountryData.setUrl(url);
         resultCountryData.setImageurl(imageurl);
         isMultiSelect(resultCountryData, laplandCode);
         checkForIcelandAndRelatedCodes(resultCountryData);

      }
      return resultCountryData;
   }

   /**
    * @param resultCountryData
    */
   private void checkForIcelandAndRelatedCodes(final CountryViewData resultCountryData)
   {
      if (StringUtils.contains(icelandResortCodes, resultCountryData.getId()))
      {
         resultCountryData.setFewDaysFlag(true);
      }
   }

   /**
    * @param resultCountryData
    * @param laplandCode
    */
   private void isMultiSelect(final CountryViewData resultCountryData, final String laplandCode)
   {
      if (StringUtils.equals(laplandCode, resultCountryData.getId()))
      {
         resultCountryData.setMultiSelect(false);
      }
   }

   /**
    * @param subList
    * @param regionsData
    */
   private void iterateRegionData(final List<LocationModel> subList,
      final List<DestinationData> regionsData)
   {
      List<LocationModel> destinationList;
      for (final LocationModel regionModel : subList)
      {
         List<DestinationData> destinationsData = new ArrayList<DestinationData>();
         destinationList = (List) regionModel.getCategories();
         if (!(CollectionUtils.isEmpty(destinationList))
            && destinationList.get(0).getType().equals(LocationType.DESTINATION))
         {
            // convert all destination model to destination data
            destinationsData = toDestinationDataList(destinationList);
         }
         // add all destination data to region data
         final DestinationData regionData = destinationConverter.convert(regionModel);
         if (StringUtils.contains(icelandResortCodes, regionData.getId()))
         {
            regionData.setFewDaysFlag(true);
         }
         regionData.setChildren(sortDestinationData(destinationsData));
         regionsData.add(regionData);
      }
   }

   public CountryViewData getChildCategoriesForDestination(final String key,
      final List<String> airports, final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandPks, final String type, final boolean availability,
      final String multiSelect)
   {

      CountryViewData resultCountryData = new CountryViewData();
      final int visibleItems = -1;
      List<LocationModel> subList = null;
      List<DestinationData> resortsData = null;
      List<DestinationData> hotelsData = null;
      LocationModel destModel = null;

      if (DESTINATION.equals(type))
      {
         destModel = mstravelLocationService.getDestination(key, releventBrandPks);
         resultCountryData = countryViewDataConverter.convert(destModel);
         resortsData = new ArrayList<DestinationData>();
         subList = (List) (destModel.getCategories());
         if (CollectionUtils.isNotEmpty(subList))
         {
            for (final LocationModel resortModel : subList)
            {
               hotelsData = new ArrayList<DestinationData>();
               final List<AccommodationViewData> accommodationViewDatas =
                  accomodationFacade.getNewAccommodationsByResort(resortModel.getCode(),
                     visibleItems);
               if (CollectionUtils.isNotEmpty(accommodationViewDatas)
                  && accommodationViewDatas != null)
               {
                  hotelsData = toHotelDataList(accommodationViewDatas);
                  if (CollectionUtils.isNotEmpty(hotelsData) && hotelsData != null)
                  {
                     final DestinationData resortData = destinationConverter.convert(resortModel);
                     resortData.setChildren(hotelsData);
                     resortsData.add(resortData);
                  }
               }
            }
            resultCountryData.setId(key);
            resultCountryData.setType(LocationType.DESTINATION);
            resultCountryData.setChildren(sortDestinationData(resortsData));
         }
         else
         {
            final List<AccommodationViewData> accommodationViewDatas =
               accomodationFacade.getNewAccommodationsByResort(key, visibleItems);
            if (CollectionUtils.isNotEmpty(accommodationViewDatas)
               && accommodationViewDatas != null)
            {
               hotelsData = toHotelDataList(accommodationViewDatas);
               if (CollectionUtils.isNotEmpty(hotelsData) && hotelsData != null)
               {
                  resultCountryData.setId(key);
                  resultCountryData.setType(LocationType.DESTINATION);
                  resultCountryData.setAvailable(availability);
                  if (resultCountryData.isAvailable())
                  {
                     for (final DestinationData hotel : hotelsData)
                     {
                        setAvailabilityForHotel(hotel);
                     }
                  }
                  resultCountryData.setChildren(sortDestinationData(hotelsData));
               }
            }
         }
      }
      else if (REGION.equals(type))
      {
         final String regionName = mstravelLocationService.getDestinationName(key);
         final List<AccommodationViewData> accommodationViewDatas =
            accomodationFacade.getNewAccommodationsByResort(key, visibleItems);
         if (CollectionUtils.isNotEmpty(accommodationViewDatas) && accommodationViewDatas != null)
         {
            hotelsData = toHotelDataList(accommodationViewDatas);
            if (CollectionUtils.isNotEmpty(hotelsData) && hotelsData != null)
            {
               resultCountryData.setName(regionName);
               resultCountryData.setId(key);
               resultCountryData.setType(LocationType.REGION);
               resultCountryData.setAvailable(availability);
               if (resultCountryData.isAvailable())
               {
                  for (final DestinationData hotel : hotelsData)
                  {
                     setAvailabilityForHotel(hotel);
                  }
               }
               resultCountryData.setChildren(sortDestinationData(hotelsData));
            }
         }
      }
      else
      {
         final String resortName = mstravelLocationService.getDestinationName(key);
         final List<AccommodationViewData> accommodationViewDatas =
            accomodationFacade.getNewAccommodationsByResort(key, visibleItems);
         if (CollectionUtils.isNotEmpty(accommodationViewDatas) && accommodationViewDatas != null)
         {
            hotelsData = toHotelDataList(accommodationViewDatas);
            if (CollectionUtils.isNotEmpty(hotelsData) && hotelsData != null)
            {
               resultCountryData.setName(resortName);
               resultCountryData.setId(key);
               resultCountryData.setType(LocationType.RESORT);
               resultCountryData.setAvailable(availability);
               if (resultCountryData.isAvailable())
               {
                  for (final DestinationData hotel : hotelsData)
                  {
                     setAvailabilityForHotel(hotel);
                  }
               }
               resultCountryData.setChildren(sortDestinationData(hotelsData));
            }
         }
      }
      if (CollectionUtils.isNotEmpty(resortsData) && resortsData != null)
      {
         Collection<Unit> units = new ArrayList<Unit>();
         if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
         {
            units = getAvailabilityForNewSearchPanel(resortsData, releventBrands);
         }
         else
         {
            units = getAvailabilityForNewSearchPanel(airports, dates, resortsData, releventBrands);
         }
         final Collection<UnitData> unitDatas = new ArrayList<UnitData>();
         for (final Unit unit : units)
         {
            final UnitData unitData = new UnitData(unit.id(), unit.name(), unit.type());
            unitData.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
            unitDatas.add(unitData);
         }

         setAvailabilityForNewSearchPanel(resultCountryData, unitDatas);
      }
      return resultCountryData;

   }

   /*
    * This method gets the country and its child categories up to destination level.
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getChildCategories(java.lang .String)
    */
   @Override
   public CountryViewData getChildCategoriesForNewSearchPanel(final String key,
      final List<String> airports, final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandPks, final String multiSelect)
   {
      CountryViewData resultCountryData = null;

      List<LocationModel> destinationList = null;
      final String laplandCode = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);
      List<LocationModel> subList = null;
      final LocationModel country = mstravelLocationService.getCountry(key, releventBrandPks);
      resultCountryData = countryViewDataConverter.convert(country);
      tuiCategoryModelUrlResolver.setOverrideSubPageType("overview");
      List<DestinationData> resortsData = null;
      final String url = tuiCategoryModelUrlResolver.resolve(country);
      String imageurl = null;
      if (country.getThumbnail() != null)
      {
         imageurl = country.getThumbnail().getURL();
      }
      subList = (List) (country.getCategories());

      if (resultCountryData != null && CollectionUtils.isNotEmpty(subList))
      {

         if (!subList.get(0).getType().equals(LocationType.REGION))
         {
            final List<DestinationData> destinationsData = new ArrayList<DestinationData>();

            List<LocationModel> resortList = null;
            // convert all destination model to destination data
            for (final LocationModel destionationModel : subList)
            {
               resortsData = new ArrayList<DestinationData>();

               resortList = (List) destionationModel.getCategories();
               if (CollectionUtils.isNotEmpty(resortList)
                  && resortList.get(0).getType().equals(LocationType.RESORT))
               {
                  for (final LocationModel resortModel : resortList)
                  {

                     final DestinationData resortData = destinationConverter.convert(resortModel);
                     resortsData.add(resortData);
                  }

               }

               final DestinationData destinationData =
                  destinationConverter.convert(destionationModel);

               if (CollectionUtils.isNotEmpty(resortsData))
               {
                  destinationData.setChildren(sortDestinationData(resortsData));
               }
               destinationsData.add(destinationData);
            }

            resultCountryData.setChildren(sortDestinationData(destinationsData));
         }
         else
         {
            final List<DestinationData> regionsData = new ArrayList<DestinationData>();

            // get all region models and convert into region data
            for (final LocationModel regionModel : subList)
            {
               final List<DestinationData> destinationsData = new ArrayList<DestinationData>();

               destinationList = (List) regionModel.getCategories();
               if (CollectionUtils.isNotEmpty(destinationList) && destinationList != null)
               {
                  List<LocationModel> resortList = null;
                  // convert all destination model to destination data
                  for (final LocationModel destionationModel : destinationList)
                  {
                     resortsData = new ArrayList<DestinationData>();

                     resortList = (List) destionationModel.getCategories();
                     if (CollectionUtils.isNotEmpty(resortList)
                        && resortList.get(0).getType().equals(LocationType.RESORT))
                     {
                        for (final LocationModel resortModel : resortList)
                        {

                           final DestinationData resortData =
                              destinationConverter.convert(resortModel);
                           resortsData.add(resortData);
                        }
                     }

                     final DestinationData destinationData =
                        destinationConverter.convert(destionationModel);
                     if (CollectionUtils.isNotEmpty(resortsData))
                     {
                        destinationData.setChildren(sortDestinationData(resortsData));
                     }
                     destinationsData.add(destinationData);
                  }
               }
               // add all destination data to region data
               final DestinationData regionData = destinationConverter.convert(regionModel);
               if (CollectionUtils.isNotEmpty(destinationsData))
               {
                  regionData.setChildren(sortDestinationData(destinationsData));
               }
               regionsData.add(regionData);
            }
            resultCountryData.setChildren(sortDestinationData(regionsData));
         }
         resultCountryData.setUrl(url);
         resultCountryData.setImageurl(imageurl);

      }
      if (resultCountryData != null && StringUtils.equals(laplandCode, resultCountryData.getId()))
      {
         resultCountryData.setMultiSelect(false);
      }
      return resultCountryData;
   }

   public CountryViewData getChildCategoriesForSearchB(final String key,
      final List<String> releventBrandPks)
   {
      CountryViewData resultCountryData = null;
      final String lapland = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);
      List<LocationModel> subList = null;
      List<LocationModel> destSubList = null;

      final LocationModel country = mstravelLocationService.getCountry(key, releventBrandPks);
      resultCountryData = countryViewDataConverter.convert(country);
      tuiCategoryModelUrlResolver.setOverrideSubPageType(OVERVIEW);
      final String url = tuiCategoryModelUrlResolver.resolve(country);
      String imageurl = null;
      if (country.getThumbnail() != null)
      {
         imageurl = country.getThumbnail().getURL();
      }
      subList = (List) (country.getCategories());
      if (resultCountryData != null && CollectionUtils.isNotEmpty(subList))
      {
         if (!subList.get(0).getType().equals(LocationType.REGION))
         {
            final List<DestinationData> destData = new ArrayList<DestinationData>();

            if (subList.get(0).getType().equals(LocationType.DESTINATION))
            {
               for (final LocationModel destination : subList)
               {
                  List<DestinationData> resortData = new ArrayList<DestinationData>();
                  destSubList = (List) destination.getCategories();
                  if (!(CollectionUtils.isEmpty(destSubList))
                     && destSubList.get(0).getType().equals(LocationType.RESORT))
                  {

                     resortData = toDestinationDataList(destSubList);

                  }
                  final DestinationData destinationData = destinationConverter.convert(destination);
                  destinationData.setChildren(sortDestinationData(resortData));
                  destData.add(destinationData);

               }
               resultCountryData.setChildren(sortDestinationData(destData));

            }
            else
            {
               final List<DestinationData> destinationDataList = toDestinationDataList(subList);
               resultCountryData.setChildren(sortDestinationData(destinationDataList));
            }

         }
         else
         {
            final List<DestinationData> regionsData = new ArrayList<DestinationData>();

            List<LocationModel> destinationList = null;
            // get all region models and convert into region data
            for (final LocationModel regionModel : subList)
            {
               List<DestinationData> destinationsData = new ArrayList<DestinationData>();
               destinationList = (List) regionModel.getCategories();
               if (!(CollectionUtils.isEmpty(destinationList))
                  && destinationList.get(0).getType().equals(LocationType.DESTINATION))
               {
                  // convert all destination model to destination data
                  destinationsData = toDestinationDataListForSearchB(destinationList);
               }
               if (!(CollectionUtils.isEmpty(destinationList))
                  && destinationList.get(0).getType().equals(LocationType.RESORT))
               {

                  destinationsData = toDestinationDataList(destinationList);
               }
               // add all destination data to region data
               final DestinationData regionData = destinationConverter.convert(regionModel);
               regionData.setChildren(sortDestinationData(destinationsData));
               regionsData.add(regionData);
            }
            resultCountryData.setChildren(sortDestinationData(regionsData));
         }
         resultCountryData.setUrl(url);
         resultCountryData.setImageurl(imageurl);
         isMultiSelect(resultCountryData, lapland);
      }
      return resultCountryData;
   }

   public CountryViewData getChildCategoriesForMobileAtRegionLevel(final LocationModel location)
   {
      CountryViewData resultCountryData = null;
      final String lapland = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);
      List<LocationModel> subList = null;

      resultCountryData = countryViewDataConverter.convert(location);
      tuiCategoryModelUrlResolver.setOverrideSubPageType(OVERVIEW);
      final String url = tuiCategoryModelUrlResolver.resolve(location);

      subList = (List) (location.getCategories());
      if (resultCountryData != null && CollectionUtils.isNotEmpty(subList))
      {
         if (subList.get(0).getType().equals(LocationType.REGION))
         {
            final List<DestinationData> destinationDataList = toDestinationDataList(subList);
            resultCountryData.setChildren(sortDestinationData(destinationDataList));
         }
         else
         {
            final List<DestinationData> regionsData = new ArrayList<DestinationData>();

            iterateRegionData(subList, regionsData);
            resultCountryData.setChildren(sortDestinationData(regionsData));
         }
         resultCountryData.setUrl(url);

         isMultiSelect(resultCountryData, lapland);
      }
      return resultCountryData;
   }

   public CountryViewData getChildCategoriesForMobile(final LocationModel location)
   {
      CountryViewData resultCountryData = null;
      final String lapland = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);
      List<LocationModel> subList = null;

      resultCountryData = countryViewDataConverter.convert(location);
      tuiCategoryModelUrlResolver.setOverrideSubPageType("overview");
      final String url = tuiCategoryModelUrlResolver.resolve(location);

      subList = (List) (location.getCategories());
      if (resultCountryData != null && subList != null)
      {
         if (!subList.get(0).getType().equals(LocationType.REGION))
         {
            final List<DestinationData> destinationDataList = toDestinationDataList(subList);
            resultCountryData.setChildren(sortDestinationData(destinationDataList));
         }
         else
         {
            final List<DestinationData> regionsData = new ArrayList<DestinationData>();

            iterateRegionData(subList, regionsData);
            resultCountryData.setChildren(sortDestinationData(regionsData));
         }
         resultCountryData.setUrl(url);

         isMultiSelect(resultCountryData, lapland);
      }
      return resultCountryData;
   }

   /**
    * @param locationList
    * @return destinationDataList
    */
   private List<DestinationData> toDestinationDataList(final List<LocationModel> locationList)
   {
      final List<DestinationData> destinationDataList = new ArrayList<DestinationData>();
      for (final LocationModel locationModel : locationList)
      {
         final DestinationData destination = destinationConverter.convert(locationModel);
         if (StringUtils.contains(icelandResortCodes, destination.getId()))
         {
            destination.setFewDaysFlag(true);
         }
         destinationDataList.add(destination);
      }
      return destinationDataList;
   }

   /**
    * @param locationList
    * @return destinationDataList
    */
   private List<DestinationData> toDestinationDataListForSearchB(
      final List<LocationModel> locationList)
   {
      final List<DestinationData> destinationDataList = new ArrayList<DestinationData>();
      for (final LocationModel locationModel : locationList)
      {
         if (!(LocationType.RESORT.equals(locationModel.getType())))
         {
            final DestinationData destination = destinationConverter.convert(locationModel);
            destinationDataList.add(destination);
         }
      }
      return destinationDataList;
   }

   private List<DestinationData> toHotelDataList(final List<AccommodationViewData> accomList)
   {
      final List<DestinationData> destinationDataList = new ArrayList<DestinationData>();
      for (final AccommodationViewData accomModel : accomList)
      {
         final DestinationData accommodationData = new DestinationData();
         converter.populate(accomModel, accommodationData);
         destinationDataList.add(accommodationData);
      }
      return destinationDataList;
   }

   public List<DestinationData> sortDestinationData(final List<DestinationData> destinations)
   {

      java.util.Collections.sort(destinations, new Comparator()
      {
         @Override
         public int compare(final Object lhs, final Object rhs)
         {
            final DestinationData destData1 = (DestinationData) lhs;
            final DestinationData destData2 = (DestinationData) rhs;
            return destData1.getName().compareToIgnoreCase(destData2.getName());
         }
      });
      return destinations;
   }

   public DestinationGuideViewData fetchDestinationGuide(
      final SearchPanelComponentModel searchPanelComponent, final List<String> airports,
      final List<String> dates, final List<String> releventBrands, final String multiSelect,
      final List<String> flightRouteBrands)
   {
      final DestinationGuideViewData destGuideViewData = new DestinationGuideViewData();
      final List<SuggestionViewData> suggestionViewDataList = new ArrayList<SuggestionViewData>();
      final SuggestionViewData mostPopularDestinations = new SuggestionViewData();
      final SuggestionViewData holidayTypes = new SuggestionViewData();
      final List<LocationModel> popularDestinations = new ArrayList<LocationModel>();
      final List<ProductRangeModel> besHolidayTypes = new ArrayList<ProductRangeModel>();

      if (searchPanelComponent == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
      }

      if (checkCondition(searchPanelComponent) && searchPanelComponent.getBestforfamily() == null
         && searchPanelComponent.getHolidaytypes() == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
         return destGuideViewData;
      }

      if (searchPanelComponent != null && searchPanelComponent.getMostpopulardestinations() != null)
      {
         final List<ItemModel> locationItems =
            searchPanelComponent.getMostpopulardestinations().getItems();
         if (CollectionUtils.isNotEmpty(locationItems))
         {
            for (final ItemModel item : locationItems)
            {
               if (item instanceof LocationModel)
               {
                  popularDestinations.add((LocationModel) item);

               }
            }
         }
      }

      populatePopularDestinations(suggestionViewDataList, mostPopularDestinations,
         popularDestinations, airports, dates, releventBrands, multiSelect, flightRouteBrands);

      if (searchPanelComponent != null && searchPanelComponent.getHolidaytypes() != null
         && CollectionUtils.isNotEmpty(searchPanelComponent.getHolidaytypes().getItems()))
      {
         final List<ItemModel> prItems = searchPanelComponent.getHolidaytypes().getItems();

         for (final ItemModel item : prItems)
         {
            if (item instanceof ProductRangeModel)
            {
               besHolidayTypes.add((ProductRangeModel) item);
            }
         }
      }

      populateHolidayTypes(suggestionViewDataList, holidayTypes, besHolidayTypes, airports, dates,
         releventBrands, multiSelect);

      destGuideViewData.setSuggestions(suggestionViewDataList);
      return destGuideViewData;

   }

   public DestinationGuideViewData optimizedFetchDestinationGuide(
      final SearchPanelComponentModel searchPanelComponent, final List<String> airports,
      final List<String> dates, final List<String> releventBrands, final String multiSelect)
   {

      final DestinationGuideViewData destGuideViewData = new DestinationGuideViewData();
      final List<SuggestionViewData> suggestionViewDataList = new ArrayList<SuggestionViewData>();
      final SuggestionViewData mostPopularDestinations = new SuggestionViewData();
      final SuggestionViewData holidayTypes = new SuggestionViewData();

      final List<ItemModel> locationItems = new ArrayList<ItemModel>();
      final List<ItemModel> prItems = new ArrayList<ItemModel>();

      if (searchPanelComponent == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
      }

      if (checkCondition(searchPanelComponent) && searchPanelComponent.getBestforfamily() == null
         && searchPanelComponent.getHolidaytypes() == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
         return destGuideViewData;
      }

      if (searchPanelComponent != null && searchPanelComponent.getMostpopulardestinations() != null)
      {
         locationItems.addAll(searchPanelComponent.getMostpopulardestinations().getItems());
      }

      optimizedPopulatePopularDestinations(suggestionViewDataList, mostPopularDestinations,
         locationItems, airports, dates, releventBrands, multiSelect);

      if (searchPanelComponent != null && searchPanelComponent.getHolidaytypes() != null
         && CollectionUtils.isNotEmpty(searchPanelComponent.getHolidaytypes().getItems()))
      {
         prItems.addAll(searchPanelComponent.getHolidaytypes().getItems());
      }

      optimizedPopulateHolidayTypes(suggestionViewDataList, holidayTypes, prItems, airports, dates,
         releventBrands, multiSelect);

      destGuideViewData.setSuggestions(suggestionViewDataList);

      return destGuideViewData;

   }

   /**
    * @param searchPanelComponent
    * @return added to fix boolean expression complexity violation
    */
   private boolean checkCondition(final SearchPanelComponentModel searchPanelComponent)
   {
      return searchPanelComponent != null && searchPanelComponent.getBestforbeaches() == null
         && searchPanelComponent.getMostpopulardestinations() == null;
   }

   public SearchBDestinationGuideViewData fetchDestinationGuideForNewSearchPanel(
      final SearchPanelComponentModel searchPanelComponent, final List<String> airports,
      final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandpks, final String multiSelect)
   {

      final SearchBDestinationGuideViewData destGuideViewData =
         new SearchBDestinationGuideViewData();
      final List<SuggestionViewData> suggestionViewDataList = new ArrayList<SuggestionViewData>();
      final SearchBDestinationGuideViewData countryList = new SearchBDestinationGuideViewData();
      final SuggestionViewData holidayTypes = new SuggestionViewData();

      final List<LocationModel> popularDestinations = new ArrayList<LocationModel>();
      final List<ProductRangeModel> besHolidayTypes = new ArrayList<ProductRangeModel>();

      if (searchPanelComponent == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
      }

      if (checkCondition(searchPanelComponent) && searchPanelComponent.getBestforfamily() == null
         && searchPanelComponent.getHolidaytypes() == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
         return destGuideViewData;
      }
      if (searchPanelComponent != null && searchPanelComponent.getMostpopulardestinations() != null)
      {
         final List<ItemModel> locationItems =
            searchPanelComponent.getMostpopulardestinations().getItems();
         if (CollectionUtils.isNotEmpty(locationItems))
         {
            for (final ItemModel item : locationItems)
            {
               if (item instanceof LocationModel)
               {
                  popularDestinations.add((LocationModel) item);

               }
            }
         }

      }
      populatePopularDestinationsForNewSearchPanel(countryList, popularDestinations, airports,
         dates, releventBrands, releventBrandpks, multiSelect);

      if (searchPanelComponent != null && searchPanelComponent.getHolidaytypes() != null)
      {
         final List<ItemModel> prItems = searchPanelComponent.getHolidaytypes().getItems();
         if (CollectionUtils.isNotEmpty(prItems))
         {
            for (final ItemModel item : prItems)
            {
               if (item instanceof ProductRangeModel)
               {
                  besHolidayTypes.add((ProductRangeModel) item);
               }
            }
         }
      }
      populateHolidayTypes(suggestionViewDataList, holidayTypes, besHolidayTypes, airports, dates,
         releventBrands, multiSelect);

      destGuideViewData.setCountryList(countryList.getCountryList());

      return destGuideViewData;

   }

   public SearchBDestinationGuideViewData fetchDestinationGuideForSearchB(
      final NewSearchPanelComponentModel searchPanelComponent, final List<String> airports,
      final List<String> dates, final List<String> releventBrands, final String multiSelect)
   {

      final SearchBDestinationGuideViewData destGuideViewData =
         new SearchBDestinationGuideViewData();
      final List<SuggestionViewData> suggestionViewDataList = new ArrayList<SuggestionViewData>();

      final SuggestionViewData holidayTypes = new SuggestionViewData();

      List<ProductRangeModel> besHolidayTypes = new ArrayList<ProductRangeModel>();

      if (searchPanelComponent == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
         return destGuideViewData;
      }

      if (searchPanelCheck(searchPanelComponent) && searchPanelComponent.getBestforfamily() == null
         && searchPanelComponent.getHolidaytypes() == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
         return destGuideViewData;
      }

      // populating collection data
      besHolidayTypes = getbestHolidayTypes(searchPanelComponent, besHolidayTypes);
      populateHolidayTypes(suggestionViewDataList, holidayTypes, besHolidayTypes, airports, dates,
         releventBrands, multiSelect);

      // get the most popular data for the destination guide B

      // get A-Z Section for destination Guide B

      destGuideViewData.setSuggestions(suggestionViewDataList);
      return destGuideViewData;

   }

   /**
    * @param searchPanelComponent
    * @return
    */

   private boolean searchPanelCheck(final NewSearchPanelComponentModel searchPanelComponent)
   {

      return searchPanelComponent != null && searchPanelComponent.getBestforbeaches() == null
         && searchPanelComponent.getMostpopulardestinations() == null;
   }

   public List<LocationModel> getPopularDestinations(
      final NewSearchPanelComponentModel searchPanelComponent,
      final List<LocationModel> popularDestinations)
   {
      if (searchPanelComponent.getMostpopulardestinations() != null)
      {
         final List<ItemModel> locationItems =
            searchPanelComponent.getMostpopulardestinations().getItems();
         if (CollectionUtils.isNotEmpty(locationItems))
         {
            for (final ItemModel item : locationItems)
            {
               if (item instanceof LocationModel)
               {
                  popularDestinations.add((LocationModel) item);

               }
            }
         }
      }
      return popularDestinations;
   }

   public List<ProductRangeModel> getbestHolidayTypes(
      final NewSearchPanelComponentModel searchPanelComponent,
      final List<ProductRangeModel> besHolidayTypes)
   {
      final List<ProductRangeModel> bestHolidayTypes = new ArrayList<ProductRangeModel>();
      if (searchPanelComponent != null && searchPanelComponent.getHolidaytypes() != null)
      {
         final List<ItemModel> prItems = searchPanelComponent.getHolidaytypes().getItems();
         if (CollectionUtils.isNotEmpty(prItems))
         {
            for (final ItemModel item : prItems)
            {
               if (item instanceof ProductRangeModel)
               {
                  besHolidayTypes.add((ProductRangeModel) item);
               }
            }
         }
      }
      final Set holidayTypes = new HashSet(besHolidayTypes);
      bestHolidayTypes.clear();
      bestHolidayTypes.addAll(holidayTypes);
      return bestHolidayTypes;

   }

   public DestinationGuideViewData fetchDestinationGuideForMobile(
      final HolidayFinderComponentModel holidayFinderComponent, final List<String> airports,
      final List<String> dates, final List<String> releventBrands)
   {

      final DestinationGuideViewData destGuideViewData = new DestinationGuideViewData();
      List<SuggestionViewData> suggestionViewDataList = new ArrayList<SuggestionViewData>();
      final SuggestionViewData mostPopularDestinations = new SuggestionViewData();
      final SuggestionViewData mostPopularRegionDestinations = new SuggestionViewData();
      final SuggestionViewData mostPopularDestDestinations = new SuggestionViewData();

      final List<LocationModel> popularDestinations = new ArrayList<LocationModel>();
      final List<LocationModel> regionPopularDestinations = new ArrayList<LocationModel>();
      final List<LocationModel> destPopularDestinations = new ArrayList<LocationModel>();

      if (holidayFinderComponent == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
      }

      if (holidayFinderComponent != null
         && holidayFinderComponent.getMostpopulardestinations() == null)
      {
         destGuideViewData.setSuggestions(suggestionViewDataList);
         return destGuideViewData;
      }

      if (holidayFinderComponent != null
         && holidayFinderComponent.getMostpopulardestinations() != null)
      {
         final List<ItemModel> locationItems =
            holidayFinderComponent.getMostpopulardestinations().getItems();
         if (CollectionUtils.isNotEmpty(locationItems))
         {
            for (final ItemModel item : locationItems)
            {
               if (item instanceof LocationModel)
               {
                  popularDestinations.add((LocationModel) item);
               }
            }
         }
      }

      if (holidayFinderComponent != null
         && holidayFinderComponent.getRegionmostpopulardestinations() != null)
      {
         final List<ItemModel> regionItems =
            holidayFinderComponent.getRegionmostpopulardestinations().getItems();
         if (CollectionUtils.isNotEmpty(regionItems))
         {
            for (final ItemModel item : regionItems)
            {
               if (item instanceof LocationModel)
               {
                  regionPopularDestinations.add((LocationModel) item);
               }
            }
         }
      }
      if (holidayFinderComponent != null
         && holidayFinderComponent.getDestmostpopulardestinations() != null)
      {
         final List<ItemModel> destItems =
            holidayFinderComponent.getDestmostpopulardestinations().getItems();
         if (CollectionUtils.isNotEmpty(destItems))
         {
            for (final ItemModel item : destItems)
            {
               if (item instanceof LocationModel)
               {
                  destPopularDestinations.add((LocationModel) item);
               }
            }
         }
      }

      populatePopularDestinationsForMobile(suggestionViewDataList, mostPopularDestinations,
         popularDestinations, airports, dates, releventBrands);

      populateRegionPopularDestinations(suggestionViewDataList, mostPopularRegionDestinations,
         regionPopularDestinations, airports, dates, releventBrands);

      populateDestPopularDestinations(suggestionViewDataList, mostPopularDestDestinations,
         destPopularDestinations, airports, dates, releventBrands);

      suggestionViewDataList = modifySugestionViewDataList(suggestionViewDataList);

      destGuideViewData.setSuggestions(suggestionViewDataList);

      return destGuideViewData;

   }

   /**
    * @param suggestionViewDataList
    * @return
    */
   private List<SuggestionViewData> modifySugestionViewDataList(
      final List<SuggestionViewData> suggestionViewDataList)
   {
      final List<SuggestionViewData> modifiedList = new ArrayList<SuggestionViewData>();
      final List<DestinationData> destList = new ArrayList<DestinationData>();
      final SuggestionViewData suggData = new SuggestionViewData();
      suggData.setId(COUNTRIES);
      suggData.setName(COUNTRIES);
      suggData.setType(RECOMMENDED);
      for (final SuggestionViewData suggestionViewData : suggestionViewDataList)
      {
         for (final DestinationData destData : suggestionViewData.getChildren())
         {
            destList.add(destData);
         }
      }
      java.util.Collections.sort(destList, new Comparator()
      {
         @Override
         public int compare(final Object lhs, final Object rhs)
         {
            final DestinationData data1 = (DestinationData) lhs;
            final DestinationData data2 = (DestinationData) rhs;
            return data1.getName().compareToIgnoreCase(data2.getName());
         }
      });
      suggData.setChildren(destList);
      modifiedList.add(suggData);
      return modifiedList;
   }

   /**
    * @param suggestionViewDataList
    * @param holidayTypes
    * @param besHolidayTypes
    */
   private void populateHolidayTypes(final List<SuggestionViewData> suggestionViewDataList,
      final SuggestionViewData holidayTypes, final List<ProductRangeModel> besHolidayTypes,
      final List<String> airports, final List<String> dates, final List<String> releventBrands,
      final String multiSelect)
   {
      List<DestinationData> destList;
      if (besHolidayTypes != null)
      {
         destList = createDestListForProduct(besHolidayTypes, releventBrands);
         holidayTypeService.getHolidaysForProductRange(destList, airports, dates, multiSelect);
         holidayTypes.setId(COLLECTIONS);
         holidayTypes.setName(COLLECTIONS);
         holidayTypes.setType(RECOMMENDED);
         holidayTypes.setChildren(destList);
         suggestionViewDataList.add(holidayTypes);
      }
   }

   private void optimizedPopulateHolidayTypes(
      final List<SuggestionViewData> suggestionViewDataList, final SuggestionViewData holidayTypes,
      final List<ItemModel> prItems, final List<String> airports, final List<String> dates,
      final List<String> releventBrands, final String multiSelect)
   {
      List<DestinationData> destList = null;
      if (CollectionUtils.isNotEmpty(prItems))
      {
         destList = optimizedCreateDestListForProduct(prItems, releventBrands);
         holidayTypeService.getHolidaysForProductRange(destList, airports, dates, multiSelect);
         holidayTypes.setId(COLLECTIONS);
         holidayTypes.setName(COLLECTIONS);
         holidayTypes.setType(RECOMMENDED);
         holidayTypes.setChildren(destList);
         suggestionViewDataList.add(holidayTypes);
      }
   }

   /**
    * @param suggestionViewDataList
    * @param mostPopularDestinations
    * @param popularDestinations
    */
   private void populatePopularDestinations(final List<SuggestionViewData> suggestionViewDataList,
      final SuggestionViewData mostPopularDestinations,
      final List<LocationModel> popularDestinations, final List<String> airports,
      final List<String> dates, final List<String> releventBrands, final String multiSelect,
      final List<String> flightRouteBrands)
   {
      List<DestinationData> destList;
      if (popularDestinations != null)
      {
         destList = createDestinationList(popularDestinations, releventBrands);
         getAvailability(destList, airports, dates, flightRouteBrands, multiSelect);
         mostPopularDestinations.setId(COUNTRIES);
         // This line has to change and should be picking the heading from
         // the component
         mostPopularDestinations.setName(COUNTRIES);
         mostPopularDestinations.setType(RECOMMENDED);
         mostPopularDestinations.setChildren(destList);
         suggestionViewDataList.add(mostPopularDestinations);
      }
   }

   private void optimizedPopulatePopularDestinations(
      final List<SuggestionViewData> suggestionViewDataList,
      final SuggestionViewData mostPopularDestinations, final List<ItemModel> locationItems,
      final List<String> airports, final List<String> dates, final List<String> releventBrands,
      final String multiSelect)
   {
      List<DestinationData> destList = null;
      if (CollectionUtils.isNotEmpty(locationItems))
      {
         destList = optimizedCreateDestinationList(locationItems, releventBrands);
         getAvailability(destList, airports, dates, releventBrands, multiSelect);
         mostPopularDestinations.setId(COUNTRIES);
         // This line has to change and should be picking the heading from
         // the component
         mostPopularDestinations.setName(COUNTRIES);
         mostPopularDestinations.setType(RECOMMENDED);
         mostPopularDestinations.setChildren(destList);
         suggestionViewDataList.add(mostPopularDestinations);
      }
   }

   private void populatePopularDestinationsForNewSearchPanel(
      final SearchBDestinationGuideViewData countryList,
      final List<LocationModel> popularDestinations, final List<String> airports,
      final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandpks, final String multiSelect)
   {
      List<DestinationData> destList;
      if (popularDestinations != null)
      {
         destList =
            createDestinationListForNewSearchPanel(popularDestinations, airports, dates,
               releventBrands, releventBrandpks, multiSelect);
         getAvailability(destList, airports, dates, releventBrands, multiSelect);
         countryList.setCountryList(destList);
      }
   }

   private void populatePopularDestinationsForMobile(
      final List<SuggestionViewData> suggestionViewDataList,
      final SuggestionViewData mostPopularDestinations,
      final List<LocationModel> popularDestinations, final List<String> airports,
      final List<String> dates, final List<String> releventBrands)
   {
      List<DestinationData> destList;
      if (popularDestinations != null)
      {
         destList = createDestinationList(popularDestinations, releventBrands);

         Collection<Unit> units = Collections.emptyList();
         if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
         {
            units = getAvailabilityForMobile(popularDestinations, releventBrands);
         }
         else
         {
            units = getAvailabilityForMobile(airports, dates, popularDestinations, releventBrands);
         }
         for (final Unit unit : units)
         {
            for (final DestinationData destination : destList)
            {
               if (StringUtils.equals(destination.getName(), unit.name()))
               {
                  destination.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
                  destination.setAvailable(true);
                  break;
               }
            }
         }
         mostPopularDestinations.setId(COUNTRIES);
         // This line has to change and should be picking the heading from
         // the component
         mostPopularDestinations.setName(COUNTRIES);
         mostPopularDestinations.setType(RECOMMENDED);
         mostPopularDestinations.setChildren(destList);
         suggestionViewDataList.add(mostPopularDestinations);
      }
   }

   private void populateRegionPopularDestinations(
      final List<SuggestionViewData> suggestionViewDataList,
      final SuggestionViewData mostPopularRegionDestinations,
      final List<LocationModel> regionPopularDestinations, final List<String> airports,
      final List<String> dates, final List<String> releventBrands)
   {
      List<DestinationData> destList;
      if (regionPopularDestinations != null)
      {
         destList =
            createDestinationListforRegions(regionPopularDestinations, releventBrands, airports,
               dates);
         mostPopularRegionDestinations.setChildren(destList);
         suggestionViewDataList.add(mostPopularRegionDestinations);
      }
   }

   private void populateDestPopularDestinations(
      final List<SuggestionViewData> suggestionViewDataList,
      final SuggestionViewData mostPopularDestDestinations,
      final List<LocationModel> destPopularDestinations, final List<String> airports,
      final List<String> dates, final List<String> releventBrands)
   {
      List<DestinationData> destList;
      if (destPopularDestinations != null)
      {
         destList =
            createDestinationListforDests(destPopularDestinations, releventBrands, airports, dates);
         mostPopularDestDestinations.setChildren(destList);
         suggestionViewDataList.add(mostPopularDestDestinations);
      }
   }

   public List<LocationModel> getMaximumFiveItems(final List<LocationModel> alllocations)
   {

      if ((alllocations != null) && (alllocations.size() > FIVE))
      {
         return alllocations.subList(0, FIVE);
      }
      else
      {
         return alllocations;
      }
   }

   @SuppressWarnings(BOXING)
   public Collection<Unit> getAvailabilityForNewSearchPanel(final List<DestinationData> destList,
      final List<String> releventBrands)
   {
      final List<String> countryCodes = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(destList))
      {
         for (final DestinationData location : destList)
         {
            countryCodes.add(location.getId());
         }
      }

      return mstravelLocationService.getAvailabilityForDestinations(countryCodes, releventBrands);

   }

   @SuppressWarnings(BOXING)
   public Collection<Unit> getAvailabilityForNewSearchPanel(final List<String> airports,
      final List<String> dates, final List<DestinationData> destList,
      final List<String> releventBrands)
   {
      final List<String> countryCodes = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(destList))
      {
         for (final DestinationData location : destList)
         {
            countryCodes.add(location.getId());
         }
      }

      return mstravelLocationService.getAvailabilityForDestinations(countryCodes, airports,
         toLocalDates(dates), releventBrands);

   }

   @SuppressWarnings(BOXING)
   public Collection<Unit> getAvailabilityForMobile(final List<LocationModel> destList,
      final List<String> releventBrands)
   {
      final List<String> countryCodes = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(destList))
      {
         for (final LocationModel location : destList)
         {
            countryCodes.add(location.getCode());
         }
      }

      return mstravelLocationService.getAvailabilityForDestinations(countryCodes, releventBrands);

   }

   @SuppressWarnings(BOXING)
   public Collection<Unit> getAvailabilityForMobile(final List<String> airports,
      final List<String> dates, final List<LocationModel> destList,
      final List<String> releventBrands)
   {
      final List<String> countryCodes = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(destList))
      {
         for (final LocationModel location : destList)
         {
            countryCodes.add(location.getCode());
         }
      }

      return mstravelLocationService.getAvailabilityForDestinations(countryCodes, airports,
         toLocalDates(dates), releventBrands);

   }

   @SuppressWarnings("boxing")
   public void getAvailability(final List<DestinationData> destList, final List<String> airports,
      final List<String> dates, final List<String> releventBrands, final String multiSelect)
   {
      if (CollectionUtils.isNotEmpty(destList))
      {
         for (final DestinationData location : destList)
         {

            getAvailableLocation(location, airports, dates, releventBrands, multiSelect);

         }
      }

   }

   @Override
   public void getAvailableLocation(final DestinationData location, final List<String> airports,
      final List<String> dates, final List<String> releventBrands, final String multiSelect)
   {

      if ((CollectionUtils.isEmpty(airports) || (airports == null))
         && (CollectionUtils.isEmpty(dates) || (dates == null)))
      {
         final String key = location.getName();

         final String cacheKey =
            keyGenerator.generate(location.getId(), StringUtils.join(releventBrands, ","));
         // get from cache
         final CacheWrapper<String, DestinationData> destGuideLocationsCache =
            cacheUtil.getDestGuideLocationsCacheWrapper();
         final DestinationData locationData = destGuideLocationsCache.get(cacheKey);

         if (locationData != null)
         {
            location.setMultiSelect(locationData.isMultiSelect());
            location.setAvailable(locationData.isAvailable());
         }
         else
         {

            try
            {
               final Collection<Unit> units =
                  mstravelLocationService.searchDestination(key, releventBrands, multiSelect);
               boolean match = false;
               if (CollectionUtils.isNotEmpty(units))
               {
                  for (final Unit unit : units)
                  {
                     if (StringUtils.equalsIgnoreCase(unit.name(), key)
                        || (StringUtils.equalsIgnoreCase(key, unit.getProductRangeType())))
                     {
                        location.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
                        if (StringUtils.equalsIgnoreCase(TRUE_VALUE, multiSelect)
                           && StringUtils.equalsIgnoreCase(FALSE_VALUE, unit.getMultiSelect()))
                        {
                           location.setAvailable(false);
                        }
                        else
                        {
                           location.setAvailable(true);
                        }
                        match = true;
                        break;
                     }

                  }
               }

               else if (CollectionUtils.isEmpty(units) || (!match))
               {
                  location.setAvailable(false);
               }
               // With multiSelect true we do not get lapland unit from
               // index for lapland key so explicit ly set its multiSelect
               // as false.
               setLapland(key, location);
            }
            catch (final AirportServiceException ex)
            {
               location.setAvailable(false);
               setLapland(key, location);
               LOG.error("Error while extracting from index", ex);
            }
            destGuideLocationsCache.put(cacheKey, location);
         }

      }
      else
      {
         availableLocationForCriteria(location, airports, dates, releventBrands, multiSelect);
      }

   }

   /**
    * @param key
    * @param location "Lapland" should be always set to multiSelect false
    */
   private void setLapland(final String key, final DestinationData location)
   {
      if (StringUtils.endsWithIgnoreCase(key, "lapland"))
      {
         location.setMultiSelect(false);
      }
   }

   /**
    * @param airports
    * @param dates Method is to get availability for a key when searched with route selected
    */
   @SuppressWarnings("boxing")
   private void availableLocationForCriteria(final DestinationData location,
      final List<String> airports, final List<String> dates, final List<String> releventBrands,
      final String multiSelect)
   {

      final String key = location.getName();

      // Caching the data if the airport keys are actual airport codes and no
      // dates are selected.. Aim is to avoid as many index searches as
      // possible
      if (isSingleAirport(airports) && CollectionUtils.isEmpty(dates)
         && areTheseAirportCodes(airports))
      {
         final String cacheKey =
            keyGenerator.generate(location.getId(), StringUtils.join(releventBrands, ","),
               StringUtils.join(airports, ","));
         // get from cache
         final CacheWrapper<String, DestinationData> destGuideLocationsCache =
            cacheUtil.getDestGuideLocationsCacheWrapper();
         final DestinationData locationData = destGuideLocationsCache.get(cacheKey);
         if (locationData != null)
         {
            location.setMultiSelect(locationData.isMultiSelect());
            location.setAvailable(locationData.isAvailable());
         }
         else
         {
            setLocationAvailability(location, airports, dates, releventBrands, multiSelect, key);
            destGuideLocationsCache.put(cacheKey, location);
         }
      }
      else
      {
         setLocationAvailability(location, airports, dates, releventBrands, multiSelect, key);
      }
   }

   /**
    * @param location
    * @param airports
    * @param dates
    * @param releventBrands
    * @param multiSelect
    * @param key
    */
   private void setLocationAvailability(final DestinationData location,
      final List<String> airports, final List<String> dates, final List<String> releventBrands,
      final String multiSelect, final String key)
   {
      Units unitResult;
      try
      {
         unitResult =
            mstravelLocationService.searchDestination(key, airports, toLocalDates(dates),
               releventBrands, multiSelect);
         boolean match = false;
         final List<Unit> allRouts = unitResult.unitWithRoute();
         if (CollectionUtils.isNotEmpty(allRouts))
         {
            for (final Unit unit : allRouts)
            {
               if (StringUtils.equalsIgnoreCase(unit.name(), key)
                  || (StringUtils.equalsIgnoreCase(key, unit.getProductRangeType())))
               {
                  location.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
                  if (StringUtils.equalsIgnoreCase(TRUE_VALUE, multiSelect)
                     && StringUtils.equalsIgnoreCase(FALSE_VALUE, unit.getMultiSelect()))
                  {
                     location.setAvailable(false);
                  }
                  else
                  {

                     location.setAvailable(true);
                  }
                  match = true;
                  break;
               }

            }

         }
         else if (CollectionUtils.isEmpty(allRouts) || (!match))
         {
            location.setAvailable(false);
         }
         setLapland(key, location);

      }
      catch (final AirportServiceException ex)
      {
         location.setAvailable(false);
         LOG.error("Error while extracting from index", ex);

      }
   }

   /**
    * This method checks if the airport keys returned from the search panel are the actual airport
    * codes
    */
   private boolean areTheseAirportCodes(final List<String> airportCodes)
   {

      boolean result = true;

      // platform cache should take care of caching these.. there should'nt be
      // a performance overhead.
      final List<String> ukAirportCodes = airportService.getAllUKAirportCodes();

      if (CollectionUtils.isNotEmpty(airportCodes))
      {
         for (final String key : airportCodes)
         {
            if (!ukAirportCodes.contains(key))
            {
               // Even if one key is not a airport code, return false and
               // break.
               // This scenario will never occur once the new search panel
               // implementation for airport guide is 100%
               result = false;
               break;
            }
         }
      }

      return result;
   }

   private boolean isSingleAirport(final List<String> airportCodes)
   {
      boolean result = false;
      if (airportCodes != null && airportCodes.size() > 1)
      {
         result = true;
      }
      return result;
   }

   /**
    * @param productRanges
    * @return destList
    */
   public List<DestinationData> createDestListForProduct(
      final List<ProductRangeModel> productRanges, final List<String> releventBrands)
   {
      final List<DestinationData> destList = new ArrayList<DestinationData>();

      for (final ProductRangeModel product : productRanges)
      {
         if (BrandUtils.brandCodesExistInBrandTypes(product.getBrands(), releventBrands))
         {
            DestinationData destinationData = new DestinationData();
            destinationData = prodToDestConverter.convert(product, destinationData);
            destList.add(destinationData);
         }
      }
      return destList;
   }

   public List<DestinationData> optimizedCreateDestListForProduct(final List<ItemModel> prItems,
      final List<String> releventBrands)
   {

      final List<DestinationData> destList = new ArrayList<DestinationData>();

      for (final ItemModel items : prItems)
      {
         if (items instanceof ProductRangeModel)
         {
            final ProductRangeModel product = (ProductRangeModel) items;
            if (CollectionUtils.isNotEmpty(product.getBrands())
               && releventBrands.contains(brandUtils.getFeatureServiceBrand(product.getBrands())))
            {
               DestinationData destinationData = new DestinationData();
               destinationData = prodToDestConverter.convert(product, destinationData);
               destList.add(destinationData);
            }
         }
      }
      return destList;
   }

   /**
    * @param listOfLocations
    * @return destList
    */
   public List<DestinationData> createDestinationList(final List<LocationModel> listOfLocations,
      final List<String> releventBrands)
   {
      final List<DestinationData> destList = new ArrayList<DestinationData>();

      for (final LocationModel location : listOfLocations)
      {
         if (BrandUtils.brandCodesExistInBrandTypes(location.getBrands(), releventBrands))
         {
            DestinationData destData = new DestinationData();
            destData = destinationConverter.convert(location, destData);
            destList.add(destData);
         }
      }

      return destList;
   }

   public boolean checkIfRelevantBrandExist(final List<String> releventBrands,
      final LocationModel location)
   {
      final String relevantBrand = releventBrands.get(0);

      if (location != null && location.getBrands() != null)
      {

         for (final BrandType bt : location.getBrands())
         {
            final String code = bt.getCode();

            if (relevantBrand.equalsIgnoreCase(code))
            {
               return true;
            }

         }

      }
      return false;

   }

   public List<DestinationData> optimizedCreateDestinationList(final List<ItemModel> locationItems,
      final List<String> releventBrands)
   {
      final List<DestinationData> destList = new ArrayList<DestinationData>();

      for (final ItemModel item : locationItems)
      {
         if (item instanceof LocationModel)
         {
            final LocationModel location = (LocationModel) item;
            if (checkIfRelevantBrandExist(releventBrands, location))
            {
               DestinationData destData = new DestinationData();
               destData = destinationConverter.convert(location, destData);
               if (StringUtils.contains(icelandResortCodes, destData.getId()))
               {
                  destData.setFewDaysFlag(true);
               }
               destList.add(destData);
            }
         }
      }

      return destList;
   }

   public List<DestinationData> createDestinationListForNewSearchPanel(
      final List<LocationModel> listOfLocations, final List<String> airports,
      final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandpks, final String multiSelect)
   {
      final List<DestinationData> destList = new ArrayList<DestinationData>();
      CountryViewData countryViewData = null;
      String countryCode = null;
      for (final LocationModel location : listOfLocations)
      {
         countryCode = location.getCode();
         countryViewData =
            availableRoutesForChildForNewSearchPanel(airports, countryCode, dates, releventBrands,
               releventBrandpks, multiSelect);
         if (BrandUtils.brandCodesExistInBrandTypes(location.getBrands(), releventBrands))
         {
            DestinationData destData = new DestinationData();
            final List<DestinationData> chidren = countryViewData.getChildren();
            destData = destinationConverter.convert(location, destData);
            destData.setChildren(chidren);
            destList.add(destData);
         }
      }

      return destList;
   }

   public List<DestinationData> createDestinationListforRegions(
      final List<LocationModel> listOfLocations, final List<String> releventBrands,
      final List<String> airports, final List<String> dates)
   {
      final List<DestinationData> destList = new ArrayList<DestinationData>();
      for (final LocationModel location : listOfLocations)
      {

         if (BrandUtils.brandCodesExistInBrandTypes(location.getBrands(), releventBrands))
         {
            final CountryViewData countryData = getChildCategoriesForMobileAtRegionLevel(location);

            final List<LocationModel> regionList = new ArrayList<LocationModel>();
            final List<LocationModel> destinationList = new ArrayList<LocationModel>();
            final List<LocationModel> resortList = new ArrayList<LocationModel>();

            for (final ItemModel subCategory : location.getAllSubcategories())
            {
               if (subCategory instanceof LocationModel)
               {
                  filterSubCategory(regionList, destinationList, resortList, subCategory);
               }
            }

            DestinationData destDataforlocation = new DestinationData();
            destDataforlocation = destinationConverter.convert(location, destDataforlocation);
            destDataforlocation.setChildren(countryData.getChildren());
            destDataforlocation.setAvailable(countryData.isAvailable());
            destList.add(destDataforlocation);
         }
      }
      return destList;
   }

   public List<DestinationData> createDestinationListforDests(
      final List<LocationModel> listOfLocations, final List<String> releventBrands,
      final List<String> airports, final List<String> dates)
   {
      final List<DestinationData> destList = new ArrayList<DestinationData>();
      for (final LocationModel location : listOfLocations)
      {
         if (BrandUtils.brandCodesExistInBrandTypes(location.getBrands(), releventBrands))
         {
            final CountryViewData countryData = getChildCategoriesForMobile(location);

            final List<LocationModel> regionList = new ArrayList<LocationModel>();
            final List<LocationModel> destinationList = new ArrayList<LocationModel>();
            final List<LocationModel> resortList = new ArrayList<LocationModel>();

            for (final ItemModel subCategory : location.getAllSubcategories())
            {
               if (subCategory instanceof LocationModel)
               {
                  filterSubCategory(regionList, destinationList, resortList, subCategory);
               }
            }

            DestinationData destDataforlocation = new DestinationData();
            destDataforlocation = destinationConverter.convert(location, destDataforlocation);
            destDataforlocation.setChildren(countryData.getChildren());
            destDataforlocation.setAvailable(countryData.isAvailable());
            destList.add(destDataforlocation);
         }
      }
      return destList;
   }

   /**
    * @param airports
    * @param key
    * @param dates
    * @return unitToUnittDataMapper
    */
   @Override
   public DestinationSearchResult getDestination(final List<String> airports,
      final List<String> selectedUnits, final String key, final List<String> dates,
      final List<String> releventBrands, final String siteBrand, final String multiSelect)
   {
      final DestinationSearchResult destinationSearchResult = new DestinationSearchResult();
      final LocationSearchResult locationSearchResult = new LocationSearchResult();
      boolean isFuzzyResult = false;
      final Unit nomatchunit = new Unit("NOMATCH", "Similar spellings");
      final Collection<Unit> units = new ArrayList<Unit>();
      try
      {
         if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
         {
            units.addAll(mstravelLocationService
               .searchDestination(key, releventBrands, multiSelect));

            lapLandError(destinationSearchResult, units, selectedUnits, multiSelect,
               releventBrands, key);
            if (units.remove(nomatchunit))
            {
               isFuzzyResult = true;
            }

         }
         else
         {
            final Units unitResult =
               mstravelLocationService.searchDestination(key, airports, toLocalDates(dates),
                  releventBrands, multiSelect);
            units.addAll(unitResult.unitWithRoute());
            destinationSearchResult.setNomatch(unitResult.isNomatch());
            destinationSearchResult.setError(searchError(unitResult, airports, key, multiSelect,
               releventBrands, dates, selectedUnits));
            isFuzzyResult = unitResult.isNomatch();
         }
         if (isFuzzyResult)
         {
            destinationSearchResult.setNomatch(true);
         }
      }
      catch (final AirportServiceException ex)
      {

         LOG.error("AirportServiceException", ex);
         if (StringUtils.endsWithIgnoreCase(ex.getCode(), "1007"))
         {
            destinationSearchResult.setError(new SearchError(NO_MATCH_FOUND,
               NO_MATCH_FOUND_FOR_SEARCH_KEY));
            return destinationSearchResult;
         }
         else if (StringUtils.equalsIgnoreCase(ex.getCode(), "1012"))
         {
            destinationSearchResult.setError(exceptionalCase(key, multiSelect, releventBrands,
               selectedUnits));
         }

      }

      locationSearchResult.setUnits(map(units, unitToUnitDataMapper));
      final LocationCategoryResult locationCategoryResult =
         getCategoriesList(locationSearchResult, siteBrand);
      destinationSearchResult.setData(locationCategoryResult);
      return destinationSearchResult;

   }

   private SearchError exceptionalCase(final String key, final String multiSelect,
      final List<String> releventBrands, final List<String> selectedUnits)
   {
      try
      {
         final Collection<Unit> appositResults =
            mstravelLocationService.searchDestination(key, releventBrands,
               getAppositeMultiSelect(multiSelect));
         if (CollectionUtils.isNotEmpty(appositResults))
         {
            List<UnitData> matchedSelectedUnits = null;
            matchedSelectedUnits =
               DeepLinkHelper.getMatchedSelectedUnits(selectedUnits, releventBrands, multiSelect);

            final List<UnitData> unitDatas = map(appositResults, unitToUnitDataMapper);

            final List<UnitData> unitEntries = new ArrayList<UnitData>();
            boolean exactMatch = false;
            for (final UnitData unitData : unitDatas)
            {
               if (StringUtils.equals(key, unitData.getName()))
               {
                  unitEntries.add(unitData);
                  exactMatch = true;
                  break;
               }
            }
            if (!exactMatch)
            {
               unitEntries.add(unitDatas.get(0));
            }

            if (!unitEntries.get(0).isMultiSelect())
            {
               return new SearchError(MULTI_SELECT_TRUE, NOT_A_LAPLAND_SPECIFIC_SEARCH,
                  unitEntries, matchedSelectedUnits);

            }
            else
            {
               return new SearchError(MULTI_SELECT_FALSE, NOT_A_LAPLAND_SPECIFIC_SEARCH,
                  unitEntries, matchedSelectedUnits);
            }
         }
         else
         {
            return new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_FOR_SEARCH_KEY);
         }
      }
      catch (final AirportServiceException ex)
      {
         LOG.error("AirportServiceException!!!", ex);
         return new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_FOR_SEARCH_KEY);
      }
   }

   private void lapLandError(final DestinationSearchResult destinationSearchResult,
      final Collection<Unit> units, final List<String> selectedUnits, final String multiSelet,
      final List<String> releventBrands, final String key)
   {

      if (CollectionUtils.isEmpty(units) && StringUtils.isNotEmpty(multiSelet))
      {

         final Collection<Unit> unitList =
            mstravelLocationService.searchDestination(key, releventBrands,
               getAppositeMultiSelect(multiSelet));
         final List<Unit> lapLands = toList(unitList);

         if (CollectionUtils.isNotEmpty(lapLands))
         {
            List<UnitData> matchedSelectedUnits = null;
            matchedSelectedUnits =
               DeepLinkHelper.getMatchedSelectedUnits(selectedUnits, releventBrands, multiSelet);

            final List<UnitData> unitDatas = map(unitList, unitToUnitDataMapper);

            final List<UnitData> unitEntries = new ArrayList<UnitData>();
            boolean exactMatch = false;
            for (final UnitData unitData : unitDatas)
            {
               if (StringUtils.equals(key, unitData.getName()))
               {
                  unitEntries.add(unitData);
                  exactMatch = true;
                  break;
               }
            }
            if (!exactMatch)
            {
               unitEntries.add(unitDatas.get(0));
            }

            if (StringUtils.equalsIgnoreCase(lapLands.get(0).getMultiSelect(), FALSE_VALUE))
            {
               destinationSearchResult.setError(new SearchError(MULTI_SELECT_TRUE,
                  NOT_A_LAPLAND_SPECIFIC_SEARCH, unitEntries, matchedSelectedUnits));
               return;
            }
            if (StringUtils.equalsIgnoreCase(lapLands.get(0).getMultiSelect(), TRUE_VALUE))
            {
               destinationSearchResult.setError(new SearchError(MULTI_SELECT_FALSE,
                  NOT_A_LAPLAND_SPECIFIC_SEARCH, unitEntries, matchedSelectedUnits));
               return;
            }

         }
         destinationSearchResult.setError(new SearchError(NO_MATCH_FOUND,
            NO_MATCH_FOUND_FOR_SEARCH_KEY));

      }
      else if (CollectionUtils.isEmpty(units) && StringUtils.isEmpty(multiSelet))
      {
         destinationSearchResult.setError(new SearchError(NO_MATCH_FOUND,
            NO_MATCH_FOUND_FOR_SEARCH_KEY));
      }
   }

   private String getAppositeMultiSelect(final String multiSelect)
   {
      if (StringUtils.equals(TRUE_VALUE, multiSelect))
      {
         return FALSE_VALUE;
      }
      else
      {
         return TRUE_VALUE;
      }
   }

   private SearchError searchError(final Units unitsResult, final List<String> airports,
      final String key, final String multiSelect, final List<String> releventBrands,
      final List<String> dates, final List<String> selectedUnits)
   {
      return unitsResult.hasRoutes() ? null : determineError(unitsResult, airports, key,
         multiSelect, releventBrands, dates, selectedUnits);
   }

   private SearchError determineError(final Units unitsResult, final List<String> airports,
      final String key, final String multiSelect, final List<String> releventBrands,
      final List<String> dates, final List<String> selectedUnits)
   {
      if (StringUtils.isNotEmpty(multiSelect))
      {

         final Collection<Unit> selectedUnit =
            mstravelLocationService.searchDestination(key, releventBrands, multiSelect);
         if (CollectionUtils.isNotEmpty(selectedUnit)
            && toList(selectedUnit).get(0).getMultiSelect().equals(multiSelect))
         {
            return routeError(unitsResult, airports, key);
         }
         final Units lapLandResult =
            mstravelLocationService.searchDestination(key, airports, toLocalDates(dates),
               releventBrands, getAppositeMultiSelect(multiSelect));
         if (CollectionUtils.isNotEmpty(lapLandResult.unitWithRoute()))
         {
            List<UnitData> matchedSelectedUnits = null;
            matchedSelectedUnits =
               DeepLinkHelper.getMatchedSelectedUnits(selectedUnits, releventBrands, multiSelect);
            final List<UnitData> unitDatas =
               map(lapLandResult.unitWithRoute(), unitToUnitDataMapper);

            final List<UnitData> unitEntries = new ArrayList<UnitData>();
            boolean exactMatch = false;
            for (final UnitData unitData : unitDatas)
            {
               if (StringUtils.equals(key, unitData.getName()))
               {
                  unitEntries.add(unitData);
                  exactMatch = true;
                  break;
               }
            }
            if (!exactMatch)
            {
               unitEntries.add(unitDatas.get(0));
            }

            if (!unitEntries.get(0).isMultiSelect())
            {
               return new SearchError(MULTI_SELECT_TRUE, NOT_A_LAPLAND_SPECIFIC_SEARCH,
                  unitEntries, matchedSelectedUnits);

            }
            else
            {
               return new SearchError(MULTI_SELECT_FALSE, NOT_A_LAPLAND_SPECIFIC_SEARCH,
                  unitEntries, matchedSelectedUnits);
            }

         }
      }
      return routeError(unitsResult, airports, key);

   }

   private SearchError routeError(final Units unitsResult, final List<String> airports,
      final String key)
   {
      final List<UnitData> locationUnits = map(unitsResult.matchedUnit(), unitToUnitDataMapper);
      List<AirportData> matchedAirports = null;
      matchedAirports = deepLinkHelper.getMatchedAirprots(airports);
      final List<UnitData> unitEntries = new ArrayList<UnitData>();
      boolean exactMatch = false;
      for (final UnitData unitData : locationUnits)
      {
         if (StringUtils.equals(key, unitData.getName()))
         {
            unitEntries.add(unitData);
            exactMatch = true;
            break;
         }
      }
      if (!exactMatch)
      {
         unitEntries.add(locationUnits.get(0));
      }
      return unitsResult.hasUnitsToSelectedAirports() ? new SearchError(INVALID_ROUTE_ON,
         "No Route Found for selected Dates", unitEntries, matchedAirports) : new SearchError(
         NO_ROUTE_FOUND_FROM, "No Route Found to selected aiports", unitEntries, matchedAirports);
   }

   /**
    * @param locationSearchResult
    * @return locationCategoryResult
    */
   private LocationCategoryResult getCategoriesList(
      final LocationSearchResult locationSearchResult, final String siteBrand)
   {

      final List<UnitData> unitsList = locationSearchResult.getUnits();
      return getLocationCategoryResult(unitsList, siteBrand);
   }

   /**
    * @param unitsList
    * @return locationCategoryResult
    */
   private LocationCategoryResult getLocationCategoryResult(final List<UnitData> unitsList,
      final String siteBrand)
   {

      final SearchPanelComponentModel component = getSearchPanelComponent();
      String laplandCode;

      final LocationCategoryResult locationCategoryResult = new LocationCategoryResult();
      final Collection<UnitData> countries = new ArrayList<UnitData>();
      final Collection<UnitData> destinations = new ArrayList<UnitData>();
      final Collection<UnitData> hotels = new ArrayList<UnitData>();
      final Collection<UnitData> safariAndStays = new ArrayList<UnitData>();

      final Collection<ProductData> productRanges = new ArrayList<ProductData>();
      final Collection<ProductData> other = new ArrayList<ProductData>();
      final Integer maxHotels = component.getMaxHotelsViewable();
      final Integer maxCountries = component.getMaxCountriesViewable();
      final Integer maxDestinations = component.getMaxDestinationsViewable();
      final Collection<UnitData> cruises = new ArrayList<UnitData>();
      final Collection<UnitData> lapLands = new ArrayList<UnitData>();
      final Collection<UnitData> multiCenters = new ArrayList<UnitData>();
      final Collection<UnitData> tourAndStay = new ArrayList<UnitData>();

      laplandCode = TUI_CONFIG_SERVICE.getConfigValue(COUNTRY_LAPLAND);

      final int maxCruises =
         getMaxConfigValue(TUI_CONFIG_SERVICE.getConfigValue("searchPanel.maxCruiseViewable"));
      final int maxMulticenters =
         getMaxConfigValue(TUI_CONFIG_SERVICE.getConfigValue("searchPanel.maxMultiCenterViewable"));
      final int maxLaplands =
         getMaxConfigValue(TUI_CONFIG_SERVICE.getConfigValue("searchPanel.maxLaplandViewable"));
      final int maxSafaris =
         getMaxConfigValue(TUI_CONFIG_SERVICE.getConfigValue("searchPanel.maxSafariViewable"));
      final int maxTourAndStay =
         getMaxConfigValue(TUI_CONFIG_SERVICE.getConfigValue("searchPanel.maxTourAndStayViewable"));

      for (final UnitData unitData : unitsList)
      {
         if (COUNTRY.equalsIgnoreCase(unitData.getType()))
         {
            addCountry(countries, unitData);
            countries.add(unitData);
         }
         else if (("FC".equalsIgnoreCase(siteBrand))
            && (DESTINATION.equalsIgnoreCase(unitData.getType())
               || RESORT.equalsIgnoreCase(unitData.getType()) || REGION.equalsIgnoreCase(unitData
               .getType())))
         {
            destinations.add(unitData);

         }
         else if ("TH".equalsIgnoreCase(siteBrand)
            && (DESTINATION.equalsIgnoreCase(unitData.getType())
               || RESORT.equalsIgnoreCase(unitData.getType()) || REGION.equalsIgnoreCase(unitData
               .getType())))
         {
            addDestinations(destinations, lapLands, unitData, laplandCode);
         }
         else if ("FJ".equalsIgnoreCase(siteBrand)
            && (DESTINATION.equalsIgnoreCase(unitData.getType())
               || RESORT.equalsIgnoreCase(unitData.getType()) || REGION.equalsIgnoreCase(unitData
               .getType())))
         {
            addDestinations(destinations, lapLands, unitData, laplandCode);
         }
         else if (("FC".equalsIgnoreCase(siteBrand))
            && (HOTEL.equalsIgnoreCase(unitData.getType()) || APARTMENT_HOTEL
               .equalsIgnoreCase(unitData.getType())))
         {
            addParentToAccom(unitData);
            hotels.add(unitData);

         }
         else if (("TH".equalsIgnoreCase(siteBrand))
            && (isHotelTypeCheck(unitData) || "SELF_CATERED".equalsIgnoreCase(unitData.getType())))
         {
            addHotels(hotels, lapLands, unitData);
         }
         else if (("FJ".equalsIgnoreCase(siteBrand))
            && (isHotelTypeCheck(unitData) || "SELF_CATERED".equalsIgnoreCase(unitData.getType())))
         {
            addHotels(hotels, lapLands, unitData);
         }
         else if ("productrange".equalsIgnoreCase(unitData.getType()))
         {
            addProductRanges(productRanges, other, unitData);
         }
         else if ((("TH").equalsIgnoreCase(siteBrand)) && unitDataTypeCheck(unitData))
         {
            cruises.add(unitData);
         }

         else if (("TH".equalsIgnoreCase(siteBrand))
            && (SAFARI.equalsIgnoreCase(unitData.getType())))
         {
            safariAndStays.add(unitData);
         }
         else if ("TH".equalsIgnoreCase(siteBrand) && DAY_TRIP.equalsIgnoreCase(unitData.getType()))
         {
            unitData.setMultiSelect(false);
            lapLands.add(unitData);
         }
         else if ("TH".equalsIgnoreCase(siteBrand)
            && TWIN_OR_MULTI_CENTER.equalsIgnoreCase(unitData.getType()))
         {
            multiCenters.add(unitData);
         }
         else if ("TH".equalsIgnoreCase(siteBrand)
            && TOUR_AND_STAY.equalsIgnoreCase(unitData.getType()))
         {
            tourAndStay.add(unitData);
         }
         else if ((("FJ").equalsIgnoreCase(siteBrand)) && unitDataTypeCheck(unitData))
         {
            cruises.add(unitData);
         }

         else if (("FJ".equalsIgnoreCase(siteBrand))
            && (SAFARI.equalsIgnoreCase(unitData.getType())))
         {
            safariAndStays.add(unitData);
         }
         else if ("FJ".equalsIgnoreCase(siteBrand) && DAY_TRIP.equalsIgnoreCase(unitData.getType()))
         {
            unitData.setMultiSelect(false);
            lapLands.add(unitData);
         }
         else if ("FJ".equalsIgnoreCase(siteBrand)
            && TWIN_OR_MULTI_CENTER.equalsIgnoreCase(unitData.getType()))
         {
            multiCenters.add(unitData);
         }
      }

      locationCategoryResult.setDestinations(maxUnits(destinations, maxDestinations));

      locationCategoryResult.setCountries(maxUnits(countries, maxCountries));

      locationCategoryResult.setHotels(maxUnits(hotels, maxHotels));

      locationCategoryResult.setLapland(maxUnits(lapLands, Integer.valueOf(maxLaplands)));

      locationCategoryResult.setNileOrGuletCruise(maxUnits(cruises, Integer.valueOf(maxCruises)));

      locationCategoryResult.setSafari(maxUnits(safariAndStays, Integer.valueOf(maxSafaris)));

      locationCategoryResult.setTwinOrMultiCenter(maxUnits(multiCenters,
         Integer.valueOf(maxMulticenters)));

      locationCategoryResult
         .setTourAndStay(maxUnits(multiCenters, Integer.valueOf(maxTourAndStay)));

      if (CollectionUtils.isNotEmpty(productRanges))
      {
         locationCategoryResult.setCollections(productRanges);
      }
      if (CollectionUtils.isNotEmpty(other))
      {
         locationCategoryResult.setOther(other);
      }

      return locationCategoryResult;
   }

   private boolean isHotelTypeCheck(final UnitData unitData)
   {
      return HOTEL.equalsIgnoreCase(unitData.getType())
         || APARTMENT_HOTEL.equalsIgnoreCase(unitData.getType())
         || VILLA.equalsIgnoreCase(unitData.getType())
         || "ship".equalsIgnoreCase(unitData.getType());
   }

   /**
    * @param unitData
    * @return
    */
   private boolean unitDataTypeCheck(final UnitData unitData)
   {
      return unitData.getType().equalsIgnoreCase(NILE_CRUISE)
         || unitData.getType().equalsIgnoreCase(GULET_CRUISE)
         || unitData.getType().equalsIgnoreCase(NILE_CRUISE_AND_STAY)
         || unitData.getType().equalsIgnoreCase(GULET_CRUISE_AND_STAY);
   }

   private void addParentToAccom(final UnitData unitData)
   {
      // If parent of unit is null string null("null")is added in uniData as
      // UI expects it in string . so here we are validating for string "null"
      if (StringUtils.isNotEmpty(unitData.getParent()) && (!("null".equals(unitData.getParent()))))
      {
         unitData.setParentName(((LocationModel) categoryService.getCategoryForCode(unitData
            .getParent())).getName());
      }
   }

   private int getMaxConfigValue(final String value)
   {
      if (StringUtils.isNotEmpty(value) && (!StringUtils.equals(value, ZERO)))
      {
         return Integer.parseInt(value);
      }
      return 0;
   }

   private void addHotels(final Collection<UnitData> hotels, final Collection<UnitData> laplands,
      final UnitData unitData)
   {
      if (StringUtils.contains(icelandResortCodes, unitData.getId()))
      {
         unitData.setFewDaysFlag(true);
      }
      if (!unitData.isMultiSelect())
      {
         unitData.setType("lap" + "_" + unitData.getType());
         addParentToAccom(unitData);
         laplands.add(unitData);
      }
      else
      {
         addParentToAccom(unitData);
         hotels.add(unitData);
      }
   }

   private void addCountry(final Collection<UnitData> countries, final UnitData unitData)
   {
      if (!unitData.isMultiSelect())
      {
         unitData.setType("lap" + "_" + unitData.getType());
      }
      if (StringUtils.contains(icelandResortCodes, unitData.getId()))
      {
         unitData.setFewDaysFlag(true);
      }
      countries.add(unitData);
   }

   private void addDestinations(final Collection<UnitData> destinations,
      final Collection<UnitData> laplands, final UnitData unitData, final String laplandCode)
   {
      if (StringUtils.contains(icelandResortCodes, unitData.getId()))
      {
         unitData.setFewDaysFlag(true);
      }

      if (unitData.getParent().equalsIgnoreCase(laplandCode))
      {
         unitData.setMultiSelect(false);
         unitData.setType("lap" + "_" + unitData.getType());
         laplands.add(unitData);
      }
      else
      {
         destinations.add(unitData);
      }
   }

   private List<UnitData> maxUnits(final Collection<UnitData> units, final Integer maxConfigValue)
   {
      final List<UnitData> unitDataList = new ArrayList<UnitData>();
      int count = 0;
      if (CollectionUtils.isNotEmpty(units))
      {
         for (final UnitData unitData : units)
         {

            if (maxConfigValue != null && count == maxConfigValue.intValue())
            {
               break;
            }
            if (maxConfigValue == null)
            {
               unitDataList.add(unitData);
            }
            else if ((count < maxConfigValue.intValue()) && (maxConfigValue.intValue() > 0)
               && !unitDataList.contains(unitData))
            {
               count++;
               unitDataList.add(unitData);
            }
         }
      }
      if (CollectionUtils.isEmpty(unitDataList))
      {
         return Collections.emptyList();
      }
      return sortUnits(unitDataList);
   }

   /**
    * @param unitData
    */
   private void addProductRanges(final Collection<ProductData> productRanges,
      final Collection<ProductData> other, final UnitData unitData)
   {
      if (unitData.getType() != null && !("".equalsIgnoreCase(unitData.getType())))
      {
         final List<ProductData> pdata = map(Arrays.asList(unitData), unitDataToProductDataMapper);
         for (final ProductData products : pdata)
         {
            if (products != null && products.isNonCoreProduct())
            {
               final ProductData product = addProductRange(pdata);
               other.add(product);
            }
            else if (products != null)
            {
               final ProductData product = addProductRange(pdata);
               productRanges.add(product);
            }
         }
      }
   }

   /**
    * @param pdata
    * @return
    */
   private ProductData addProductRange(final List<ProductData> pdata)
   {
      final ProductData product = new ProductData();
      product.setCode(pdata.get(0).getName());
      product.setId(pdata.get(0).getId());
      product.setName(pdata.get(0).getName());
      product.setType(pdata.get(0).getType());
      product.setMultiSelect(pdata.get(0).isMultiSelect());
      product.setNonCoreProduct(pdata.get(0).isNonCoreProduct());
      return product;
   }

   /**
    * @param dates
    * @return list of LocalDate
    */
   private List<LocalDate> toLocalDates(final List<String> dates)
   {
      return map(dates, locateDateMapper);
   }

   public List<UnitData> sortUnits(final List<UnitData> units)
   {

      java.util.Collections.sort(units, new Comparator()
      {
         @Override
         public int compare(final Object lhs, final Object rhs)
         {
            final UnitData unit1 = (UnitData) lhs;
            final UnitData unit2 = (UnitData) rhs;
            return unit1.getName().compareToIgnoreCase(unit2.getName());
         }
      });
      return units;
   }

   public CountryViewData availableRoutesForChild(final List<String> airports,
      final String countryCode, final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandPks)
   {

      final CountryViewData countryData = getChildCategories(countryCode, releventBrandPks);
      final List<LocationModel> destinations =
         getCountryChildren(countryCode, DESTINATION, releventBrandPks);
      final List<LocationModel> regions = getCountryChildren(countryCode, REGION, releventBrandPks);
      final List<LocationModel> resorts = getCountryChildren(countryCode, RESORT, releventBrandPks);
      final List<LocationModel> allLocations = new ArrayList<LocationModel>();
      allLocations.addAll(regions);
      allLocations.addAll(destinations);
      allLocations.addAll(resorts);
      Collection<Unit> units = Collections.emptyList();
      if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
      {
         units = getAvailabilityForMobile(allLocations, releventBrands);
      }
      else
      {
         units = getAvailabilityForMobile(airports, dates, allLocations, releventBrands);
      }
      final Collection<UnitData> unitDatas = new ArrayList<UnitData>();
      for (final Unit unit : units)
      {
         final UnitData unitData = new UnitData(unit.id(), unit.name(), unit.type());
         unitData.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
         unitDatas.add(unitData);
      }
      setAvailability(countryData, unitDatas);
      return countryData;
   }

   public CountryViewData availableRoutesForChildForNewSearchPanel(final List<String> airports,
      final String countryCode, final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandPks, final String multiSelect)
   {

      final CountryViewData countryData =
         getChildCategoriesForNewSearchPanel(countryCode, airports, dates, releventBrands,
            releventBrandPks, multiSelect);
      final List<LocationModel> destinations =
         getCountryChildren(countryCode, DESTINATION, releventBrandPks);
      final List<LocationModel> regions = getCountryChildren(countryCode, REGION, releventBrandPks);
      final List<LocationModel> resorts = getCountryChildren(countryCode, RESORT, releventBrandPks);
      final List<LocationModel> allLocations = new ArrayList<LocationModel>();
      allLocations.addAll(regions);
      allLocations.addAll(destinations);
      allLocations.addAll(resorts);
      Collection<Unit> units;
      if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
      {
         units = getAvailabilityForMobile(allLocations, releventBrands);
      }
      else
      {
         units = getAvailabilityForMobile(airports, dates, allLocations, releventBrands);
      }
      final Collection<UnitData> unitDatas = new ArrayList<UnitData>();
      for (final Unit unit : units)
      {
         final UnitData unitData = new UnitData(unit.id(), unit.name(), unit.type());
         unitData.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
         unitDatas.add(unitData);
      }
      setAvailabilityForNewSearchPanel(countryData, unitDatas);
      return countryData;
   }

   public CountryViewData availableRoutesForChildForSearchB(final List<String> airports,
      final String countryCode, final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandPks)
   {

      final CountryViewData countryData =
         getChildCategoriesForSearchB(countryCode, releventBrandPks);
      final List<LocationModel> destinations =
         getCountryChildren(countryCode, DESTINATION, releventBrandPks);
      final List<LocationModel> regions = getCountryChildren(countryCode, REGION, releventBrandPks);
      final List<LocationModel> resorts = getCountryChildren(countryCode, RESORT, releventBrandPks);
      final List<LocationModel> allLocations = new ArrayList<LocationModel>();
      allLocations.addAll(regions);
      allLocations.addAll(destinations);
      allLocations.addAll(resorts);
      Collection<Unit> units;
      if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
      {
         units = getAvailabilityForMobile(allLocations, releventBrands);
      }
      else
      {
         units = getAvailabilityForMobile(airports, dates, allLocations, releventBrands);
      }
      final Collection<UnitData> unitDatas = new ArrayList<UnitData>();
      for (final Unit unit : units)
      {
         final UnitData unitData = new UnitData(unit.id(), unit.name(), unit.type());
         unitData.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
         unitDatas.add(unitData);
      }
      setAvailability(countryData, unitDatas);
      return countryData;
   }

   /**
    * @param airports
    * @param dates
    * @return This method returns All countries available to route selected, If No route selected
    *         then returns all list of countries.
    */
   @Override
   public List<DestinationData> getCountries(final List<String> airports, final List<String> dates,
      final List<String> releventBrandpks, final String multiSelect,
      final List<String> flightRouteBrands)
   {

      if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
      {
         return getAllExistingCountries(releventBrandpks, multiSelect);
      }

      final List<DestinationData> allCountries = getAllCountries(releventBrandpks, multiSelect);
      final boolean dataCanBeCached =
         areTheseAirportCodes(airports) && CollectionUtils.isEmpty(dates);
      final Set<DestinationData> availableCountries = new HashSet<DestinationData>();
      for (final DestinationData destinationData : allCountries)
      {

         if (dataCanBeCached)
         {
            Collections.sort(airports);

            final String cacheKey =
               keyGenerator.generate(destinationData.getId(),
                  StringUtils.join(flightRouteBrands, ","), StringUtils.join(airports, ","));
            // get from cache
            final CacheWrapper<String, DestinationData> destGuideLocationsCache =
               cacheUtil.getDestGuideLocationsCacheWrapper();
            final DestinationData locationData = destGuideLocationsCache.get(cacheKey);

            if (locationData != null)
            {
               destinationData.setMultiSelect(locationData.isMultiSelect());
               destinationData.setAvailable(locationData.isAvailable());
               availableCountries.add(destinationData);
            }
            else
            {
               getCotuntiesDestinationData(airports, dates, flightRouteBrands, multiSelect,
                  availableCountries, destinationData);
               destGuideLocationsCache.put(cacheKey, destinationData);
            }
         }
         else
         {
            getCotuntiesDestinationData(airports, dates, flightRouteBrands, multiSelect,
               availableCountries, destinationData);
         }

      }

      if (CollectionUtils.isNotEmpty(availableCountries))
      {
         return sortDestinationData(toList(availableCountries));
      }

      return new ArrayList<DestinationData>();

   }

   /**
    * @param airports
    * @param dates
    * @param releventBrands
    * @param multiSelect
    * @param availableCountries
    * @param destinationData
    */
   private void getCotuntiesDestinationData(final List<String> airports, final List<String> dates,
      final List<String> releventBrands, final String multiSelect,
      final Set<DestinationData> availableCountries, final DestinationData destinationData)
   {
      Units unitResult = null;
      try
      {
         unitResult =
            mstravelLocationService.searchDestination(destinationData.getName(), airports,
               toLocalDates(dates), releventBrands, multiSelect);
      }
      catch (final AirportServiceException ex)
      {
         LOG.error("there was a problem while searching destinationData.getName() from index ", ex);
      }
      if (unitResult != null)
      {
         final List<Unit> units = unitResult.unitWithRoute();
         if (CollectionUtils.isNotEmpty(units))
         {
            for (final Unit unit : units)
            {
               destinationData.setMultiSelect(BooleanUtils.toBoolean(unit.getMultiSelect()));
               if (StringUtils.contains(icelandResortCodes, unit.id()))
               {
                  destinationData.setFewDaysFlag(true);
               }
               if (StringUtils.equals(destinationData.getId(), unit.id()))
               {
                  destinationData.setAvailable(true);
                  availableCountries.add(destinationData);

               }
            }
         }
         else if (viewSelector.checkIsMobile())
         {
            availableCountries.add(destinationData);
         }
      }
   }

   /**
    * @return All existing countries with availability true- if no criteria selected
    */
   private List<DestinationData> getAllExistingCountries(final List<String> releventBrands,
      final String multiSelect)
   {
      List<DestinationData> allCountries;
      allCountries = getAllCountries(releventBrands, multiSelect);

      // Set availability true to all existing list of countries
      if (CollectionUtils.isNotEmpty(allCountries))
      {
         for (final DestinationData destinationData : allCountries)
         {
            if (StringUtils.contains(icelandResortCodes, destinationData.getId()))
            {
               destinationData.setFewDaysFlag(true);
            }
            destinationData.setAvailable(true);

         }
      }
      return allCountries;
   }

   /**
    * @param countryData
    * @param units
    */
   private void setAvailability(final CountryViewData countryData, final Collection<UnitData> units)
   {
      if (units != null)
      {
         for (final UnitData unit : units)
         {
            if (unit != null)
            {
               setAvailabilityStatus(countryData, unit);
            }
         }
      }
   }

   /**
    * @param countryData
    * @param units
    */
   private void setAvailabilityForNewSearchPanel(final CountryViewData countryData,
      final Collection<UnitData> units)
   {
      if (units != null && CollectionUtils.isNotEmpty(units))
      {
         for (final UnitData unit : units)
         {
            if (unit != null && countryData != null)
            {
               setAvailabilityStatusForNewSearchPanel(countryData, unit);
            }
         }
      }
   }

   /**
    * @param countryData
    * @param unit
    */
   private void setAvailabilityStatusForNewSearchPanel(final CountryViewData countryData,
      final UnitData unit)
   {
      if (countryData.getChildren().get(0).getType().equals((LocationType.REGION).toString()))
      {
         for (final DestinationData region : countryData.getChildren())
         {
            if ((region.getChildren() != null) && (!region.getChildren().isEmpty()))
            {
               setAvailabilityForDestination(unit, region);
               for (final DestinationData destination : region.getChildren())
               {
                  if ((destination.getChildren() != null)
                     && (!destination.getChildren().isEmpty())
                     && (!destination.getChildren().isEmpty())
                     && destination.getChildren().get(0).getType()
                        .equals((LocationType.RESORT).toString()))
                  {
                     setAvailabilityForDestination(unit, destination);
                  }
               }
            }
            if (StringUtils.equals(region.getName(), unit.getName()))
            {
               region.setAvailable(true);
               countryData.setAvailable(true);
            }
         }
      }
      else if (countryData.getChildren().get(0).getType()
         .equals((LocationType.DESTINATION).toString()))
      {
         setAvailabilityForDestination(countryData, unit);
         for (final DestinationData resort : countryData.getChildren())
         {
            if ((resort.getChildren() != null) && (!resort.getChildren().isEmpty())
               && resort.getChildren().get(0).getType().equals((LocationType.RESORT).toString()))
            {
               setAvailabilityForDestination(unit, resort);
            }
         }
      }
      else if (countryData.getChildren().get(0).getType().equals((LocationType.RESORT).toString()))
      {
         setAvailabilityForResort(countryData, unit);
      }
   }

   /**
    * @param unit
    * @param region
    */
   private void setAvailabilityForDestination(final UnitData unit, final DestinationData region)
   {
      for (final DestinationData destination : region.getChildren())
      {
         if (StringUtils.equals(destination.getName(), unit.getName()))
         {
            destination.setMultiSelect(unit.isMultiSelect());
            destination.setAvailable(true);
         }
      }
   }

   /**
    * @param countryData
    * @param unit
    */
   private void setAvailabilityForDestination(final CountryViewData countryData, final UnitData unit)
   {
      for (final DestinationData destination : countryData.getChildren())
      {
         if (StringUtils.equals(destination.getId(), unit.getId()))
         {
            destination.setMultiSelect(unit.isMultiSelect());
            destination.setAvailable(true);
            countryData.setAvailable(true);
         }
      }
   }

   /**
    * @param countryData
    * @param unit
    */
   private void setAvailabilityForResort(final CountryViewData countryData, final UnitData unit)
   {
      for (final DestinationData resort : countryData.getChildren())
      {
         if (StringUtils.equals(resort.getId(), unit.getId()))
         {
            resort.setMultiSelect(unit.isMultiSelect());
            resort.setAvailable(true);
            countryData.setAvailable(true);
            if ((resort.getChildren() != null) && (!resort.getChildren().isEmpty()))
            {
               for (final DestinationData hotel : resort.getChildren())
               {
                  setAvailabilityForHotel(hotel);
               }
            }
         }
      }
   }

   /**
    * @param hotel
    */
   private void setAvailabilityForHotel(final DestinationData hotel)
   {
      if (hotelTypeCheck(hotel) || "SHIP".equals(hotel.getType())
         || "VILLA".equals(hotel.getType()))
      {
         hotel.setMultiSelect(true);
         hotel.setAvailable(true);
      }
   }

   private boolean hotelTypeCheck(final DestinationData hotel)
   {
      return "HOTEL".equals(hotel.getType()) || "APARTMENT_HOTEL".equals(hotel.getType())
         || "SELF_CATERED".equals(hotel.getType());
   }

   /**
    * @param countryData
    * @param unit
    */
   private void setAvailabilityStatus(final CountryViewData countryData, final UnitData unit)
   {
      if (countryData.getChildren().get(0).getType().equals((LocationType.REGION).toString()))
      {
         for (final DestinationData region : countryData.getChildren())
         {
            setAvailability(unit, region);
            if (StringUtils.equals(region.getName(), unit.getName()))
            {
               region.setAvailable(true);
               countryData.setAvailable(true);
            }
         }
      }
      else
      {
         setAvailability(countryData, unit);
      }
   }

   /**
    * @param unit
    * @param region
    */
   private void setAvailability(final UnitData unit, final DestinationData region)
   {
      for (final DestinationData destination : region.getChildren())
      {
         if (StringUtils.equals(destination.getName(), unit.getName()))
         {
            destination.setMultiSelect(unit.isMultiSelect());
            destination.setAvailable(true);
         }
      }
   }

   /**
    * @param countryData
    * @param unit
    */
   private void setAvailability(final CountryViewData countryData, final UnitData unit)
   {
      for (final DestinationData destination : countryData.getChildren())
      {
         if (StringUtils.equals(destination.getId(), unit.getId()))
         {
            destination.setMultiSelect(unit.isMultiSelect());
            destination.setAvailable(true);
            countryData.setAvailable(true);
         }
      }
   }

   private List<LocationModel> getCountryChildren(final String countryCode, final String type,
      final List<String> releventBrandPks)
   {
      final List<LocationModel> regionList = new ArrayList<LocationModel>();
      final List<LocationModel> destinationList = new ArrayList<LocationModel>();
      final List<LocationModel> resortList = new ArrayList<LocationModel>();
      final LocationModel country =
         mstravelLocationService.getCountry(countryCode, releventBrandPks);

      for (final ItemModel subCategory : country.getAllSubcategories())
      {
         if (subCategory instanceof LocationModel)
         {
            filterSubCategory(regionList, destinationList, resortList, subCategory);
         }
      }

      if (DESTINATION.equals(type))
      {
         return destinationList;
      }
      if (RESORT.equals(type))
      {
         return resortList;
      }
      return regionList;

   }

   /**
    * @param regionList
    * @param destinationList
    * @param subCategory
    */
   private void filterSubCategory(final List<LocationModel> regionList,
      final List<LocationModel> destinationList, final List<LocationModel> resortList,
      final ItemModel subCategory)
   {
      final LocationModel locModel = (LocationModel) subCategory;
      if (locModel.getType().equals(LocationType.DESTINATION))
      {
         destinationList.add(locModel);
      }
      if (locModel.getType().equals(LocationType.REGION))
      {
         regionList.add(locModel);
      }
      if (locModel.getType().equals(LocationType.RESORT))
      {
         resortList.add(locModel);
      }
   }

   private SearchPanelComponentModel getSearchPanelComponent()
   {
      SearchPanelComponentModel component = new SearchPanelComponentModel();
      try
      {
         component = cmsComponentService.getAbstractCMSComponent(SEARCH_PANELCOMPONENT_ID);
      }
      catch (final CMSItemNotFoundException e)
      {

         LOG.error("No such component", e);
      }
      return component;
   }

   @Override
   public LocationData getBasicLocationDetails(final String locationCode)
   {
      if (StringUtils.isNotBlank(locationCode))
      {
         return getLocationDataFromModel(getLocationForCode(locationCode));
      }
      else
      {
         return null;
      }
   }

   /**
    * @param airports
    * @param dates
    * @param releventBrandpks
    * @return List<DestinationData>
    * @description
    */

   public List<DestinationData> getContinentAndCountryList(final List<String> airports,
      final List<String> dates, final List<String> flightRouteBrands,
      final List<String> releventBrandpks, final String multiSelect)
   {

      if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
      {
         return setAvailabilityForContinentHierarchyForAnyWhereSearch(airports, dates,
            flightRouteBrands, releventBrandpks, multiSelect);
      }
      else
      {
         final List<DestinationData> allContinentHierarchy =
            getContinentAndCountryHierarchy(releventBrandpks);
         setAvailabilityForContinentHierarchy(allContinentHierarchy, airports, dates,
            flightRouteBrands, multiSelect);
         return allContinentHierarchy;

      }

   }

   private List<DestinationData> setAvailabilityForContinentHierarchyForAnyWhereSearch(
      final List<String> airports, final List<String> dates, final List<String> flightRouteBrands,
      final List<String> releventBrandpks, final String multiSelect)
   {
      final String siteUid = cmsSiteService.getCurrentSite().getUid();
      final String continentCodes =
         TUI_CONFIG_SERVICE.getConfigValue(siteUid + DESTINATION_GUIDE_CONTINENT_ORDER,
            DESTINATION_GUIDE_CONTINENTS_DEFAULT_OREDER);

      final String key =
         keyGenerator.generate(CONTINENT_AND_COUNTRY_HIERARCHY_CACHE, ANY_WHERE_SEARCH,
            releventBrandpks, continentCodes);
      final CacheWrapper<String, List<DestinationData>> continentAndCountryHierarchyCache =
         cacheUtil.getContinentAndCountryHierarchyCacheWrapper();
      final List<DestinationData> allContinentHierarchyWithAvailability =
         continentAndCountryHierarchyCache.get(key);

      if (allContinentHierarchyWithAvailability == null)
      {
         final List<DestinationData> allContinentHierarchy = new ArrayList<DestinationData>();
         final List<LocationModel> allContinentModels =
            locationService.getAllContinents(catalogUtil.getActiveCatalogVersion(),
               releventBrandpks);

         getContinentList(releventBrandpks, allContinentHierarchy, allContinentModels,
            continentCodes);

         setAvailabilityForContinentHierarchy(allContinentHierarchy, airports, dates,
            flightRouteBrands, multiSelect);

         if (CollectionUtils.isNotEmpty(allContinentHierarchy))
         {
            continentAndCountryHierarchyCache.put(key, allContinentHierarchy);
         }

         return allContinentHierarchy;
      }

      return allContinentHierarchyWithAvailability;

   }

   /**
    * @param releventBrandpks
    * @return List<DestinationData>
    */
   private List<DestinationData> getContinentAndCountryHierarchy(final List<String> releventBrandpks)
   {
      final String siteUid = cmsSiteService.getCurrentSite().getUid();
      final String continentCodes =
         TUI_CONFIG_SERVICE.getConfigValue(siteUid + DESTINATION_GUIDE_CONTINENT_ORDER,
            DESTINATION_GUIDE_CONTINENTS_DEFAULT_OREDER);
      final String key =
         keyGenerator.generate(CONTINENT_AND_COUNTRY_HIERARCHY_CACHE, releventBrandpks,
            continentCodes);

      final CacheWrapper<String, List<DestinationData>> continentAndCountryHierarchyCache =
         cacheUtil.getContinentAndCountryHierarchyCacheWrapper();
      List<DestinationData> allContinentHierarchy = continentAndCountryHierarchyCache.get(key);

      if (allContinentHierarchy == null)
      {
         allContinentHierarchy = new ArrayList<DestinationData>();
         final List<LocationModel> allContinentModels =
            locationService.getAllContinents(catalogUtil.getActiveCatalogVersion(),
               releventBrandpks);

         getContinentList(releventBrandpks, allContinentHierarchy, allContinentModels,
            continentCodes);

         if (CollectionUtils.isNotEmpty(allContinentHierarchy))
         {
            continentAndCountryHierarchyCache.put(key, allContinentHierarchy);
         }

         return allContinentHierarchy;
      }

      return allContinentHierarchy;
   }

   /**
    * @param releventBrandpks
    * @param allContinents
    * @param allContinentModels
    */
   private void getContinentList(final List<String> releventBrandpks,
      final List<DestinationData> allContinents, final List<LocationModel> allContinentModels,
      final String continentCodes)
   {
      // iterate continent models

      final Map<String, DestinationData> continents = new HashMap<String, DestinationData>();
      for (final Iterator iterator = allContinentModels.iterator(); iterator.hasNext();)
      {
         final LocationModel continentModel = (LocationModel) iterator.next();

         final DestinationData continentData = destinationConverter.convert(continentModel);
         final List<LocationModel> countriesUnderContinent =
            mstravelLocationService.getCountriesUnderContinent(continentModel.getCode(),
               releventBrandpks);
         final List<DestinationData> countryDataList = new ArrayList<DestinationData>();

         if (CollectionUtils.isEmpty(countriesUnderContinent))
         {
            continue;
         }
         getCountriesUnderContinent(countriesUnderContinent, countryDataList);

         continentData.setChildren(countryDataList);
         continents.put(continentData.getId(), continentData);
      }

      orderingOfContinentsBasedOnConfiguration(continents, allContinents, continentCodes);

   }

   /**
    *
    */
   private void orderingOfContinentsBasedOnConfiguration(
      final Map<String, DestinationData> continents, final List<DestinationData> allContinents,
      final String continentCodes)
   {

      if (!continentCodes.isEmpty())
      {
         final String[] continentCodesOrder = continentCodes.split(",");
         for (final String continentCode : continentCodesOrder)
         {

            final DestinationData continentData = continents.get(continentCode);

            if (continentData != null)
            {
               allContinents.add(continentData);
            }
         }
      }
   }

   /**
    * @param countriesUnderContinent
    * @param countryDataList
    */
   private void getCountriesUnderContinent(final List<LocationModel> countriesUnderContinent,
      final List<DestinationData> countryDataList)
   {
      // iterate country models
      for (final Iterator iterator2 = countriesUnderContinent.iterator(); iterator2.hasNext();)
      {
         final LocationModel countryModel = (LocationModel) iterator2.next();
         final DestinationData countryData = destinationConverter.convert(countryModel);

         countryDataList.add(countryData);
      }
      sortDestinationData(countryDataList);
   }

   /**
    * @param allContinentHierarchy
    * @description populate the availability for continent hierarchy
    */
   private void setAvailabilityForContinentHierarchy(
      final List<DestinationData> allContinentHierarchy, final List<String> airports,
      final List<String> dates, final List<String> releventBrandpks, final String multiSelect)
   {

      for (final Iterator iterator = allContinentHierarchy.iterator(); iterator.hasNext();)
      {
         final DestinationData continentData = (DestinationData) iterator.next();

         for (final Iterator iterator2 = continentData.getChildren().iterator(); iterator2
            .hasNext();)
         {
            final DestinationData countryData = (DestinationData) iterator2.next();

            setAvailabilityForCountry(countryData, airports, dates, releventBrandpks, multiSelect);

            if (countryData.isAvailable())
            {
               continentData.setAvailable(true);
               continentData.setMultiSelect(true);
            }
            else
            {
               continentData.setAvailable(false);
            }

         }

      }

   }

   /**
    * @param destinationData
    * @param airports
    * @param dates
    * @param releventBrandpks
    * @param multiSelect
    */
   private void setAvailabilityForCountry(final DestinationData destinationData,
      final List<String> airports, final List<String> dates, final List<String> releventBrandpks,
      final String multiSelect)
   {

      Units unitResult = null;
      try
      {
         unitResult =
            mstravelLocationService.searchDestination(destinationData.getName(), airports,
               toLocalDates(dates), releventBrandpks, multiSelect);
      }
      catch (final AirportServiceException ex)
      {
         LOG.error("there was a problem while searching destinationData.getName() from index ");
      }
      getAvailabilityForUnitWithAvailableRoute(destinationData, unitResult);

   }

   /**
    * @param destinationData
    * @param airports
    * @param dates
    * @param releventBrandpks
    * @param multiSelect
    */
   private void setAvailabilityForLocationByName(final DestinationData destinationData,
      final List<String> airports, final List<String> dates, final List<String> releventBrandpks,
      final String multiSelect)
   {

      Units unitResult = null;
      try
      {
         unitResult =
            mstravelLocationService.searchDestination(destinationData.getName(), airports,
               toLocalDates(dates), releventBrandpks, multiSelect);
      }
      catch (final AirportServiceException ex)
      {
         LOG.error("there was a problem while searching destinationData.getID from index ");
      }
      getAvailabilityForUnitWithAvailableRoute(destinationData, unitResult);

   }

   /**
    * @param destinationData
    * @param unitResult
    */
   private void getAvailabilityForUnitWithAvailableRoute(final DestinationData destinationData,
      final Units unitResult)
   {
      if (unitResult != null)
      {
         final List<Unit> units = unitResult.unitWithRoute();

         destinationData.setAvailable(false);

         if (CollectionUtils.isNotEmpty(units))
         {
            setAvailabilityForDestinationByCheckingAvailableUnits(destinationData, units);
         }

      }
   }

   /**
    * @param destinationData
    * @param units
    */
   private void setAvailabilityForDestinationByCheckingAvailableUnits(
      final DestinationData destinationData, final List<Unit> units)
   {
      for (final Unit unit : units)
      {

         if (StringUtils.equals(destinationData.getId(), unit.id()))
         {
            destinationData.setAvailable(true);

            setMutliSelectAndFewDaysFlag(destinationData, unit);

            break;

         }

      }
   }

   /**
    * @param destinationData
    * @param unit
    */
   private void setMutliSelectAndFewDaysFlag(final DestinationData destinationData, final Unit unit)
   {
      if (StringUtils.contains(icelandResortCodes, unit.id()))
      {
         destinationData.setFewDaysFlag(true);
      }

      if (StringUtils.equals(destinationData.getId(), lapLandCode))
      {
         destinationData.setMultiSelect(false);
      }
      else
      {
         destinationData.setMultiSelect(Boolean.parseBoolean(unit.getMultiSelect()));
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.LocationFacade#getMostPopularDestinationData(java.util.List)
    */
   @Override
   public List<MostPopularDestinationdata> getMostPopularDestinationData(final List<String> data,
      final List<String> airports, final List<String> dates, final List<String> releventBrands,
      final List<String> releventBrandpks, final String multiSelect)
   {

      if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
      {
         return getAvailabilityForMostPopularForAnyWhereSearch(data, airports, dates,
            releventBrands, multiSelect);
      }
      else
      {
         final List<MostPopularDestinationdata> mostpopularData = getMostPopularCachedData(data);
         return getAvailabilityForMostPopular(mostpopularData, airports, dates, releventBrands,
            multiSelect);

      }

   }

   private List<MostPopularDestinationdata> getAvailabilityForMostPopularForAnyWhereSearch(
      final List<String> data, final List<String> airports, final List<String> dates,
      final List<String> brands, final String multiSelect)
   {

      final String key =
         keyGenerator.generate("mostPopularDestinationdataCache" + data + brands
            + "forAnyWhereSearch");
      final CacheWrapper<String, List<MostPopularDestinationdata>> mostPopularDestinationdataCache =
         cacheUtil.getMostPopularDestinationdataCacheWrapper();

      List<MostPopularDestinationdata> mostpopularData = mostPopularDestinationdataCache.get(key);
      if (mostpopularData == null)
      {

         mostpopularData = new ArrayList<MostPopularDestinationdata>();

         populateMostPopularList(mostpopularData, data);
         getAvailabilityForMostPopular(mostpopularData, airports, dates, brands, multiSelect);

         if (CollectionUtils.isNotEmpty(mostpopularData))
         {
            mostPopularDestinationdataCache.put(key, mostpopularData);
         }

         return mostpopularData;

      }
      return mostpopularData;
   }

   /**
    * @param data
    * @return List<MostPopularDestinationdata>
    */
   private List<MostPopularDestinationdata> getMostPopularCachedData(final List<String> data)
   {
      final String key = keyGenerator.generate("mostPopularDestinationdataCache" + data);
      final CacheWrapper<String, List<MostPopularDestinationdata>> mostPopularDestinationdataCache =
         cacheUtil.getMostPopularDestinationdataCacheWrapper();

      List<MostPopularDestinationdata> mostpopularData = mostPopularDestinationdataCache.get(key);
      if (mostpopularData == null)
      {
         mostpopularData = new ArrayList<MostPopularDestinationdata>();

         populateMostPopularList(mostpopularData, data);
         if (CollectionUtils.isNotEmpty(mostpopularData))
         {
            mostPopularDestinationdataCache.put(key, mostpopularData);
         }

      }
      return mostpopularData;
   }

   /**
    * @param mostpopularData
    * @param data
    */
   private void populateMostPopularList(final List<MostPopularDestinationdata> mostpopularData,
      final List<String> data)
   {
      for (final String locationCode : data)
      {
         final MostPopularDestinationdata destinationData = new MostPopularDestinationdata();
         final List<String> locationCodes = getEachLocation(locationCode);
         if (checkForSingleLocationCode(locationCode))
         {
            mostPopularSingleDestinationConverter.populate(locationCode, destinationData);
            mostpopularData.add(destinationData);
         }
         else if (!(CollectionUtils.isEmpty(locationCodes)))
         {
            mostPopularMultipleDestinationConverter.populate(locationCodes, destinationData);
            mostpopularData.add(destinationData);
         }

      }
   }

   /**
    * @param entry
    * @return string list
    */
   private List<String> getEachLocation(final String entry)
   {
      final String mostPopularEntry = entry;
      final List<String> locList = new ArrayList<String>();
      final Pattern regex = Pattern.compile(REGEX);
      final Matcher match = regex.matcher(mostPopularEntry);
      if (match.find())
      {
         final StringTokenizer strTok = new StringTokenizer(mostPopularEntry, AMP);
         while (strTok.hasMoreTokens())
         {
            final String loc = strTok.nextToken();
            locList.add(loc);
         }
         return locList;
      }
      else
      {
         LOG.debug("NO Location Found with the name ---- " + entry);
         return Collections.<String> emptyList();
      }
   }

   private boolean checkForSingleLocationCode(final String entry)
   {
      boolean isLocation = false;
      final Pattern regex = Pattern.compile(REGEX_FOR_LOCATION);
      final Matcher match = regex.matcher(entry);
      if (match.find())
      {
         isLocation = true;
      }
      return isLocation;

   }

   /**
    * @param populardata
    * @param airports
    * @param dates
    * @param brands
    * @param multiSel
    * @return MostPopularDestinationdata list
    */
   private List<MostPopularDestinationdata> getAvailabilityForMostPopular(
      final List<MostPopularDestinationdata> populardata, final List<String> airports,
      final List<String> dates, final List<String> brands, final String multiSel)
   {
      final List<MostPopularDestinationdata> mostPopularDestinationdata =
         new ArrayList<MostPopularDestinationdata>();
      for (final MostPopularDestinationdata mostpopDestination : populardata)
      {
         final List<DestinationData> popData = new ArrayList<DestinationData>();
         final List<DestinationData> destData = mostpopDestination.getLocationDatas();
         for (final DestinationData location : destData)
         {
            setAvailabilityForLocationByName(location, airports, dates, brands, multiSel);
            popData.add(location);
         }
         mostpopDestination.setLocationDatas(popData);
         mostPopularDestinationdata.add(mostpopDestination);
      }
      return mostPopularDestinationdata;

   }

   /**
    * @param componentModel
    * @return SearchPanelCollectionViewData
    */
   public SearchPanelCollectionViewData getCollectionData(
      final NewSearchPanelComponentModel componentModel)
   {

      final String siteUid = cmsSiteService.getCurrentSite().getUid();
      final String key = keyGenerator.generate("destinationGuideCollectionDataCache" + siteUid);
      final CacheWrapper<String, SearchPanelCollectionViewData> destinationGuideCollectionDataCache =
         cacheUtil.getDestinationGuideCollectionDataCacheWrapper();

      SearchPanelCollectionViewData searchPanelCollectionViewData =
         destinationGuideCollectionDataCache.get(key);

      if (searchPanelCollectionViewData == null)
      {

         final List<DestinationGuideCollectionViewData> collectionsViewData =
            new ArrayList<DestinationGuideCollectionViewData>();

         populateCollectionViewDataList(componentModel, collectionsViewData);

         searchPanelCollectionViewData = new SearchPanelCollectionViewData();

         searchPanelCollectionViewData.setChildren(collectionsViewData);

         searchPanelCollectionViewData.setId(COLLECTIONS);
         searchPanelCollectionViewData.setName(COLLECTIONS);
         searchPanelCollectionViewData.setType(RECOMMENDED);
         searchPanelCollectionViewData.setUrl(TUI_CONFIG_SERVICE
            .getConfigValue((siteUid + "." + DESTINATION_GUIDE_COLLECTION_PAGE_URL).toLowerCase()));

         if (CollectionUtils.isNotEmpty(searchPanelCollectionViewData.getChildren()))
         {
            destinationGuideCollectionDataCache.put(key, searchPanelCollectionViewData);
         }

      }

      return searchPanelCollectionViewData;

   }

   /**
    * @param componentModel
    * @param collectionsViewData
    */
   private void populateCollectionViewDataList(final NewSearchPanelComponentModel componentModel,
      final List<DestinationGuideCollectionViewData> collectionsViewData)
   {
      if (componentModel != null && componentModel.getHolidaytypes() != null)
      {
         final List<ItemModel> prItems = componentModel.getHolidaytypes().getItems();
         if (CollectionUtils.isNotEmpty(prItems))
         {
            populateDestinationGuideCollectionViewDataList(collectionsViewData, prItems);
         }
      }
   }

   /**
    * @param collectionsViewData
    * @param prItems
    */
   private void populateDestinationGuideCollectionViewDataList(
      final List<DestinationGuideCollectionViewData> collectionsViewData,
      final List<ItemModel> prItems)
   {
      for (final ItemModel item : prItems)
      {
         if (item instanceof CollectionToBestForModel)
         {

            final String collectionCode = ((CollectionToBestForModel) item).getCollectionCode();

            final DestinationGuideCollectionViewData destinationGuideCollectionViewData =
               nonGeoItemFacade.getCollectionProductRangeData(tuiUtilityService
                  .getItemMap(getProductData((CollectionToBestForModel) item)));

            destinationGuideCollectionViewData.setId(collectionCode);
            destinationGuideCollectionViewData.setType(PRODUCT_RANGE);
            destinationGuideCollectionViewData.setAvailable(true);
            destinationGuideCollectionViewData.setMultiSelect(true);
            destinationGuideCollectionViewData.setBestFor(getBestForCodesForCollection(item));

            collectionsViewData.add(destinationGuideCollectionViewData);

         }
      }
   }

   /**
    * @param item
    * @return List<BestForViewData>
    */
   private List<BestForViewData> getBestForCodesForCollection(final ItemModel item)
   {
      final String bestForCodes = ((CollectionToBestForModel) item).getBestForCodes();

      List<String> list = null;

      if (StringUtils.isNotBlank(bestForCodes))
      {
         list = new ArrayList<String>(Arrays.asList(bestForCodes.split(",")));
      }

      final List<BestForViewData> bestForViewDatas = new ArrayList<BestForViewData>();

      if (CollectionUtils.isNotEmpty(list))
      {
         for (final Iterator iterator = list.iterator(); iterator.hasNext();)
         {
            final BestForViewData bestForViewData = new BestForViewData();

            final String string = (String) iterator.next();
            bestForViewData.setName(string);

            bestForViewDatas.add(bestForViewData);
         }
      }
      return bestForViewDatas;
   }

   /**
    * @param component
    * @return string list
    */
   private List<String> getProductData(final CollectionToBestForModel component)
   {
      final List<String> list = new ArrayList<String>();
      list.add(component.getDiffProductDataMap());
      return list;

   }

   /**
    * @param countryCode
    * @param airports
    * @param dates
    * @param brands
    * @param brandPks
    * @param multiSel
    * @return DestinationData
    */
   @Override
   public DestinationData getRegionAndDestinationDataForCountry(final String countryCode,
      final List<String> airports, final List<String> dates, final List<String> brandPks,
      final List<String> brands, final String multiSelect)
   {

      if (CollectionUtils.isEmpty(airports) && CollectionUtils.isEmpty(dates))
      {
         return getCachedRegionDestinationDataWithAvailability(countryCode, airports, dates,
            brandPks, brands, multiSelect);
      }
      else
      {
         final DestinationData countryData = getCachedRegionDestinationData(countryCode, brandPks);
         isCountryAvaliable(countryData, airports, dates, brands, multiSelect);
         return countryData;
      }

   }

   private DestinationData getCachedRegionDestinationDataWithAvailability(final String countryCode,
      final List<String> airports, final List<String> dates, final List<String> brandPks,
      final List<String> flightrouteBrands, final String multiSelect)
   {
      final String key =
         keyGenerator.generate("regionDestinationDataCache" + countryCode + brandPks
            + "forAnyWhereSearch");
      final CacheWrapper<String, DestinationData> regionDestinationDataCache =
         cacheUtil.getRegionDestinationDataCacheWrapper();

      DestinationData countryData = regionDestinationDataCache.get(key);
      if (countryData == null)
      {

         countryData = new DestinationData();
         getSubCategoriesUderCountry(countryCode, brandPks, countryData);

         isCountryAvaliable(countryData, airports, dates, flightrouteBrands, multiSelect);
         if (countryData != null)
         {
            regionDestinationDataCache.put(key, countryData);
         }

         return countryData;

      }
      return countryData;
   }

   /**
    * @param countryCode
    * @param brands
    * @param countryData
    */
   private void getSubCategoriesUderCountry(final String countryCode, final List<String> brands,
      final DestinationData countryData)
   {
      final List<LocationModel> regiondestinationList =
         mstravelLocationService.getRegionsDestinationsForCountry(countryCode, brands);
      final LocationModel country =
         mstravelLocationService.getLocationModelForCodeWithBrands(countryCode, brands);
      final String url = tuiCategoryModelUrlResolver.resolve(country);
      final List<DestinationData> regionDestinationList = new ArrayList<DestinationData>();
      countryData.setUrl(url);
      destinationConverter.populate(country, countryData);
      for (final LocationModel model : regiondestinationList)
      {
         final DestinationData regionDestinationData = new DestinationData();
         destinationConverter.populate(model, regionDestinationData);
         getDestinationForRegion(model.getCode(), brands, regionDestinationData);
         regionDestinationList.add(regionDestinationData);
      }
      sortDestinationData(regionDestinationList);
      countryData.setChildren(regionDestinationList);
   }

   /**
    * @param countryCode
    * @param brands
    * @return DestinationData
    */
   private DestinationData getCachedRegionDestinationData(final String countryCode,
      final List<String> brands)
   {
      final String key = keyGenerator.generate("regionDestinationDataCache" + countryCode);
      final CacheWrapper<String, DestinationData> regionDestinationDataCache =
         cacheUtil.getRegionDestinationDataCacheWrapper();

      DestinationData countryData = regionDestinationDataCache.get(key);
      if (countryData == null)
      {
         countryData = new DestinationData();
         getSubCategoriesUderCountry(countryCode, brands, countryData);
         if (CollectionUtils.isNotEmpty(countryData.getChildren()))
         {
            regionDestinationDataCache.put(key, countryData);
         }
      }

      return countryData;
   }

   /**
    * @param regionCode
    * @param brands
    * @param regionDestinationData
    */
   private void getDestinationForRegion(final String regionCode, final List<String> brands,
      final DestinationData regionDestinationData)
   {
      final List<LocationModel> destinationList =
         mstravelLocationService.getDestinationsForRegions(regionCode, brands);
      final List<DestinationData> destList = new ArrayList<DestinationData>();

      for (final LocationModel model : destinationList)
      {
         final DestinationData destinationData = new DestinationData();
         destinationConverter.populate(model, destinationData);
         destList.add(destinationData);
      }
      sortDestinationData(destList);
      regionDestinationData.setChildren(destList);

   }

   /**
    * @param country
    * @param airports
    * @param dates
    * @param brands
    * @param multiSel
    */
   private void setAvailabilityForRegionAndDestination(final DestinationData country,
      final List<String> airports, final List<String> dates, final List<String> brands,
      final String multiSel)
   {
      for (final DestinationData data : country.getChildren())
      {
         if (StringUtils.equalsIgnoreCase(data.getType(), DESTINATION))
         {
            setAvailabilityForLocationByName(data, airports, dates, brands, multiSel);

         }
         else if (StringUtils.equalsIgnoreCase(data.getType(), REGION))
         {
            setRegionDestinationAvailabilty(data, airports, dates, brands, multiSel);

         }
         setCountryAvailabilty(data, country);
      }

   }

   /**
    * @param country
    * @param airports
    * @param dates
    * @param brands
    * @param multiSel
    */
   private void isCountryAvaliable(final DestinationData country, final List<String> airports,
      final List<String> dates, final List<String> brands, final String multiSel)
   {
      if (CollectionUtils.isEmpty(country.getChildren()))
      {
         setAvailabilityForLocationByName(country, airports, dates, brands, multiSel);

      }
      else
      {
         setAvailabilityForRegionAndDestination(country, airports, dates, brands, multiSel);
      }
   }

   /**
    * @param data
    * @param country
    */
   private void setCountryAvailabilty(final DestinationData data, final DestinationData country)
   {
      if (data.isAvailable())
      {
         country.setAvailable(true);
      }
      else
      {
         country.setAvailable(false);
      }
   }

   /**
    * @param region
    * @param airports
    * @param dates
    * @param brands
    * @param multiSel
    */
   private void setRegionDestinationAvailabilty(final DestinationData region,
      final List<String> airports, final List<String> dates, final List<String> brands,
      final String multiSel)
   {
      if (CollectionUtils.isNotEmpty(region.getChildren()))
      {
         for (final DestinationData destData : region.getChildren())
         {
            setAvailabilityForLocationByName(destData, airports, dates, brands, multiSel);
            if (destData.isAvailable())
            {
               region.setAvailable(true);
            }
            else
            {
               region.setAvailable(false);
            }
         }
      }
      else
      {
         setAvailabilityForLocationByName(region, airports, dates, brands, multiSel);
      }

   }

}
