package uk.co.portaltech.tui.facades.impl;

/*
 * Originating Unit: Portal Technology Systems Ltd http://www.portaltech.co.uk
 *
 * Copyright Portal Technology Systems Ltd.
 *
 * $Id: $
 */

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.travel.thirdparty.endeca.TabResult;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel;
import uk.co.portaltech.tui.components.model.HeroCarouselComponentModel;
import uk.co.portaltech.tui.components.model.MediaPromoComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselModel;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TouristBoardBannerComponentModel;
import uk.co.portaltech.tui.components.model.UpsellToHolidayComponentModel;
import uk.co.portaltech.tui.converters.AccommodationOption;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.enums.MultiStayNames;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.AttractionFacade;
import uk.co.portaltech.tui.facades.CarouselFacade;
import uk.co.portaltech.tui.facades.ExcursionFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.populators.CarouselDataPopulator;
import uk.co.portaltech.tui.services.LinkedItemService;
import uk.co.portaltech.tui.services.locationfinder.LocationFinder;
import uk.co.portaltech.tui.services.mediafinder.MediaFinder;
import uk.co.portaltech.tui.services.productfinder.ProductFinder;
import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.AttractionCarouselViewData;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.LocationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.ThingsToDoWrapper;
import uk.co.portaltech.tui.web.view.data.UpSellCarouselWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.ProductCrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.TUISystemException;
import uk.co.tui.web.common.enums.CarouselLookupType;
import uk.co.tui.web.common.enums.TopPlacesCarouselType;

public class DefaultCarouselFacade implements CarouselFacade
{

   private static final String HOTEL_DATA = "hotel";

   private static final String VILLA_DATA = "villa";

   private static final String LOCATION_DATA = "Location";

   private static final int VISIBLE_ITEMS = 5;

   private static final String CHECK_THE_LOOK_UP_TYPE_FIELD =
      " found. Check the lookupType field or create a Finder for the lookupType.";

   @Autowired
   private Map<String, ProductFinder> finders;

   @Autowired
   private Map<String, LocationFinder> locationFinders;

   @Autowired
   private Map<String, MediaFinder> mediaFinders;

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private CarouselDataPopulator carouselDataPopulator;

   @Resource
   private Converter<AccommodationModel, AccommodationViewData> accommodationConverter;

   @Resource
   private ConfigurablePopulator<AccommodationModel, AccommodationViewData, AccommodationOption> accommodationConfiguredPopulator;

   @Resource
   private ConfigurablePopulator<LocationModel, LocationData, LocationOption> locationConfiguredPopulator;

   @Resource(name = "locationConverter")
   private Converter<LocationModel, LocationData> locationConvertor;

   @Resource
   private SearchFacade searchFacade;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private LocationFacade locationFacade;

   @Resource
   private AccommodationFacade accomodationFacade;

   @Resource
   private ExcursionFacade excursionFacade;

   @Resource
   private AttractionFacade attractionFacade;

   @Resource
   private LinkedItemService linkedItemService;

   private static final TUILogUtils LOG = new TUILogUtils("DefaultCarouselFacade");

   @Override
   public List<AccommodationViewData> getAccommodationViewData(
      final AccommodationCarouselModel accommodationCarouselModel, final String locationCode,
      final String pageType, final String seoPageType, final int offset, final int size)
   {
      int pageSize = size;
      final List<AccommodationViewData> accommodationViewDatas =
         new ArrayList<AccommodationViewData>();
      if (accommodationCarouselModel == null)
      {
         return Collections.<AccommodationViewData> emptyList();
      }
      if (CarouselLookupType.MANUAL.equals(accommodationCarouselModel.getLookupType()))
      {
         final ProductFinder finder =
            finders.get("productFinder_" + accommodationCarouselModel.getLookupType());

         if (finder == null)
         {

            LOG.error("No product finder for lookup type "
               + accommodationCarouselModel.getLookupType() + CHECK_THE_LOOK_UP_TYPE_FIELD);

         }

         // Generate a SearchRequest object to pass to our ProductFinder
         final SearchRequestData searchRequest = new SearchRequestData();
         searchRequest.setRelevantItem(accommodationCarouselModel);
         searchRequest.setCategoryCode(locationCode);
         /*
          * This part is used to check and set the pageSize(amount of visible items) as well as the
          * specific index of the item(s) that are to be retrieved if the index is not set then it
          * means the method is been called at page load or one of the tabs have been clicked on so
          * it should return a list of associated Items(accommodation) which starts from 0 index to
          * the maximum items specified for the carousels. If this is a request for a single item
          * then the index is set,thus pagesize is set to one.
          */
         if (pageSize == 0)
         {
            pageSize = 1;
         }
         searchRequest.setPageSize(pageSize);
         searchRequest.setOffset(offset);
         if (accommodationCarouselModel.getAccommodationType() != null)
         {
            searchRequest.setAccommodationTypeContext(accommodationCarouselModel
               .getAccommodationType().getCode());
         }

         // this need re-factoring
         List<AccommodationModel> accommodationModels = Collections.emptyList();
         if (finder != null)
         {
            accommodationModels =
               (List<AccommodationModel>) finder.search(searchRequest).getResults();
         }

         for (final AccommodationModel accommodationModel : accommodationModels)
         {
            final AccommodationViewData accommodationData =
               accommodationConverter.convert(accommodationModel);

            accommodationConfiguredPopulator.populate(accommodationModel, accommodationData, Arrays
               .asList(AccommodationOption.PRIMARY_IMAGE, AccommodationOption.BASIC,
                  AccommodationOption.SUMMARY, AccommodationOption.DESCRIPTION,
                  AccommodationOption.T_RATING));
            accommodationViewDatas.add(accommodationData);
         }
         return accommodationViewDatas;
      }
      else if (CarouselLookupType.ENDECA_PLACES_TO_STAY.equals(accommodationCarouselModel
         .getLookupType())
         || CarouselLookupType.CR_STAY_ENDECA_PLACES_TO_STAY.equals(accommodationCarouselModel
            .getLookupType()))
      {
         return searchFacade.getPlacesToStayData(locationCode, pageType, seoPageType,
            accommodationCarouselModel);
      }
      return accommodationViewDatas;
   }

   @Override
   public List<MediaViewData> getHeroCarouselMediaData(
      final HeroCarouselComponentModel carouselModel, final int offset, final int pageSizes)
   {
      int pageSize = pageSizes;

      final MediaFinder finder = mediaFinders.get("mediaFinder_" + carouselModel.getLookupType());

      if (finder == null)
      {
         throw new TUISystemException("No mediaFinder for lookup type "
            + carouselModel.getLookupType() + CHECK_THE_LOOK_UP_TYPE_FIELD);
      }

      // Generate a SearchRequest object to pass to our MediaFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      if (pageSize == 0)
      {
         pageSize = 1;
      }
      searchRequest.setPageSize(pageSize);
      searchRequest.setOffset(offset);
      searchRequest.setRelevantItem(carouselModel);

      return carouselDataPopulator.convertAllMedia(finder.search(searchRequest).getResults());

   }

   /*
    * (non-Javadoc)
    *
    * @see
    * uk.co.portaltech.tui.facades.CarouselFacade#getHeroCarouselMediaDataByProductCode(java.lang
    * .String, int, int)
    */
   @Override
   public AccommodationViewData getHeroCarouselMediaDataByProductCode(final String productCode,
      final int offset, final int pageSize)
   {
      final AccommodationModel accommodationModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(productCode,
            cmsSiteService.getCurrentCatalogVersion(), null);

      final AccommodationViewData accommodationData =
         accommodationConverter.convert(accommodationModel);
      accommodationConfiguredPopulator.populate(accommodationModel, accommodationData,
         Arrays.asList(AccommodationOption.GALLERY_IMAGE));

      return accommodationData;
   }

   /**
    *
    * This method gets first image Url of the Carousel to the given product code for the meta data
    *
    */
   @Override
   public String getMetaImageUrlByProductCode(final String productCode)
   {
      final AccommodationViewData accommodationData =
         getHeroCarouselMediaDataByProductCode(productCode, 0, 0);
      return getCarouselFirstImageUrl(accommodationData.getImages());
   }

   /**
    * Method that process the Gallery images collection and gets the first url for Meta data
    *
    * @param images
    * @return
    */
   private String getCarouselFirstImageUrl(final Collection<ImageData> images)
   {
      String imageUrl = "";
      if (!images.isEmpty())
      {
         final Iterator<ImageData> iterator = images.iterator();
         while (iterator.hasNext())
         {
            final ImageData imageData = iterator.next();
            imageUrl = imageData.getUrl();
            LOG.info("Meta Image URL" + imageUrl);
            break;
         }

      }
      return imageUrl;

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.CarouselFacade#getThingsToDoViewData(java.lang.String,
    * java.lang.String, int, int)
    */
   @Override
   public ThingsToDoWrapper getThingsToDoViewData(final String locationCode, final String pageType,
      final String seoPageType, final ThingsToDoCarouselComponentModel item)
   {

      ThingsToDoWrapper thingsToDoWrapper = new ThingsToDoWrapper();

      for (final ThingsToDoCarouselModel thingsToDoCarousel : item.getThingsToDoCarousels())
      {
         final SearchResultData searchResult =
            searchFacade.getThingsToDoViewData(locationCode, pageType, seoPageType,
               thingsToDoCarousel);
         thingsToDoWrapper.setHeading(searchResult.getHeading());
         final List<ResultData> result = searchResult.getResults();
         if (result != null && !result.isEmpty())
         {

            final List<ResultData> sights = new ArrayList<ResultData>();
            final List<ResultData> events = new ArrayList<ResultData>();
            final List<ResultData> excursions = new ArrayList<ResultData>();

            for (final ResultData resultData : result)
            {
               if (SearchResultType.EXCURSION.equals(resultData.getType()))
               {
                  excursions.add(resultData);
               }
               else if (SearchResultType.ATTRACTION.equals(resultData.getType()))
               {
                  if ("SIGHT".equals(resultData.getSearchResultSubtype()))
                  {
                     sights.add(resultData);
                  }
                  else if ("EVENT".equals(resultData.getSearchResultSubtype()))
                  {
                     events.add(resultData);
                  }
                  else
                  {
                     // If it is any other type such as 'OTHER' treat it as a sight.
                     sights.add(resultData);
                  }
               }
            }
            thingsToDoWrapper = getThingsToDoWrapper(events, sights, excursions, thingsToDoWrapper);
         }
      }
      return thingsToDoWrapper;

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.CarouselFacade#getThingsToDoViewData(java.lang.String,
    * java.lang.String, int, int)
    */
   @Override
   public ThingsToDoWrapper getThingsToDoForTouristBoardViewData(final String locationCode,
      final String pageType, final String seoPageType, final TouristBoardBannerComponentModel item)
   {

      ThingsToDoWrapper thingsToDoWrapper = new ThingsToDoWrapper();

      for (final ThingsToDoCarouselModel thingsToDoCarousel : item.getThingsToDoCarousels())
      {
         final SearchResultData searchResult =
            searchFacade.getTouristBoardThingsToDoViewData(locationCode, pageType, seoPageType,
               thingsToDoCarousel);
         thingsToDoWrapper.setHeading(searchResult.getHeading());
         final List<ResultData> result = searchResult.getResults();
         if (result != null && !result.isEmpty())
         {

            final List<ResultData> sights = new ArrayList<ResultData>();
            final List<ResultData> events = new ArrayList<ResultData>();
            final List<ResultData> excursions = new ArrayList<ResultData>();

            for (final ResultData resultData : result)
            {
               if (SearchResultType.EXCURSION.equals(resultData.getType()))
               {
                  excursions.add(resultData);
               }
               else if (SearchResultType.ATTRACTION.equals(resultData.getType()))
               {
                  if ("SIGHT".equals(resultData.getSearchResultSubtype()))
                  {
                     sights.add(resultData);
                  }
                  else if ("EVENT".equals(resultData.getSearchResultSubtype()))
                  {
                     events.add(resultData);
                  }
                  else
                  {
                     // If it is any other type such as 'OTHER' treat it as a sight.
                     sights.add(resultData);
                  }
               }
            }
            thingsToDoWrapper = getThingsToDoWrapper(events, sights, excursions, thingsToDoWrapper);
         }
      }
      return thingsToDoWrapper;

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.CarouselFacade#getLocationViewData(java.lang.String,
    * uk.co.portaltech.tui.components.model.CrossSellCarouselModel)
    */
   @Override
   public List<LocationData> getLocationViewData(final String locationCode,
      final CrossSellCarouselComponentModel item)
   {
      final List<LocationData> crossSellViewData = new ArrayList<LocationData>();
      final LocationFinder locationFinder =
         locationFinders.get("locationFinder_" + item.getLookupType());

      if (locationFinder == null)
      {
         throw new TUISystemException("No location finder for lookup type " + item.getLookupType()
            + CHECK_THE_LOOK_UP_TYPE_FIELD);
      }
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(locationCode);
      /*
       * This part is used to check and set the pageSize(amount of visible items) as well as the
       * specific index of the item(s) that are to be retrieved if the index is not set then it
       * means the method is been called at page load or one of the tabs have been clicked on so it
       * should return a list of associated Items(accommodation) which starts from 0 index to the
       * maximum items specified for the carousels. If this is a request for a single item then the
       * index is set,thus pagesize is set to one.
       */
      // Handle pagination later
      final List<LocationModel> locationModels = locationFinder.search(searchRequest).getResults();
      for (final LocationModel locationModel : locationModels)
      {
         final LocationData locationData = locationConvertor.convert(locationModel);
         locationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.BASIC, LocationOption.GALLERY));
         crossSellViewData.add(locationData);
      }
      return crossSellViewData;
   }

   @Override
   public ProductCrossSellWrapper getProductCrossSellCarouselHolidaysData(
      final String locationCode, final String pageType, final String seoType,
      final PlacesToStayCarouselModel item, final String productCode)
   {

      final SearchResultData productCrossSell =
         searchFacade.getProductCrossSellCarouselData(locationCode, pageType, seoType, item,
            productCode);
      final ProductCrossSellWrapper productCrossSellWrapper = new ProductCrossSellWrapper();
      final List<AccommodationCarouselViewData> carouselViewDatas =
         new ArrayList<AccommodationCarouselViewData>();
      productCrossSellWrapper.setHeading(productCrossSell.getHeading());

      if (CollectionUtils.isNotEmpty(productCrossSell.getResults()))
      {
         carouselViewDatas.add(new AccommodationCarouselViewData(accomodationFacade
            .getAccommodationsWithEndecaData(productCrossSell.getResults()), Integer
            .valueOf(VISIBLE_ITEMS)));

         if (CollectionUtils.isNotEmpty(carouselViewDatas))
         {
            // Added to get name and duration of each accom in case of
            // multicentre.
            for (final AccommodationCarouselViewData carouselViewData : carouselViewDatas)
            {
               for (final AccommodationViewData accomViewData : carouselViewData
                  .getAccomodationDatas())
               {
                  accomViewData.setMultiCentre(true);
                  accomViewData.setMultiCentreDatas(linkedItemService
                     .getMultiCentreData(accomViewData.getCode()));
               }
            }
            productCrossSellWrapper.setAccommodationCarousels(carouselViewDatas);
         }
         else
         {
            productCrossSellWrapper.setAccommodationCarousels(Collections
               .<AccommodationCarouselViewData> emptyList());
         }
      }
      return productCrossSellWrapper;
   }

   @Override
   public ProductCrossSellWrapper getProductCrossSellCarouselData(final String locationCode,
      final String pageType, final String seoType,
      final ProductCrossSellCarouselComponentModel item, final String productCode)
   {
      final SearchResultData productCrossSell =
         searchFacade.getProductCrossSellData(locationCode, pageType, seoType, item, productCode);
      final ProductCrossSellWrapper productCrossSellWrapper = new ProductCrossSellWrapper();
      final List<AccommodationCarouselViewData> carouselViewDatas =
         new ArrayList<AccommodationCarouselViewData>();
      productCrossSellWrapper.setHeading(productCrossSell.getHeading());
      final List<TabResult> tabResults = productCrossSell.getTabResults();

      final AccommodationModel accommodationModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(productCode,
            cmsSiteService.getCurrentCatalogVersion(), null);
      final boolean multiCentre = linkedItemService.isMultiCentre(accommodationModel);
      if (CollectionUtils.isNotEmpty(tabResults))
      {
         tabResults.removeAll(Collections.singleton(null));
         for (final TabResult tabResult : tabResults)
         {
            final List<ResultData> accommodations = new ArrayList<ResultData>();
            if (tabResult.getResult() != null && !tabResult.getResult().isEmpty())
            {
               for (final ResultData resultData : tabResult.getResult())
               {
                  if (SearchResultType.ACCOMMODATION.equals(resultData.getType()))
                  {
                     accommodations.add(resultData);

                  }
               }
               String carouselCode = "";
               String carouselTitle = tabResult.getTitle();
               carouselTitle = MultiStayNames.valueOf(carouselTitle.toUpperCase()).getValue();
               if (carouselTitle != null)
               {
                  carouselCode = carouselTitle.replaceAll(" ", "");
                  carouselCode = carouselCode.replaceAll("[^A-Za-z]", "");
               }
               if (CollectionUtils.isNotEmpty(accommodations))
               {
                  carouselViewDatas.add(new AccommodationCarouselViewData(carouselTitle,
                     carouselCode, accomodationFacade.getAccommodationsWithLocationFromEndecaData(
                        accommodations, multiCentre), "", Integer.valueOf(VISIBLE_ITEMS)));
               }
            }
         }

         if (CollectionUtils.isNotEmpty(carouselViewDatas))
         {
            // Added to get name and duration of each accom in case of multicentre.
            if (multiCentre)
            {
               for (final AccommodationCarouselViewData carouselViewData : carouselViewDatas)
               {
                  for (final AccommodationViewData accomViewData : carouselViewData
                     .getAccomodationDatas())
                  {
                     accomViewData.setMultiCentre(true);
                     accomViewData.setMultiCentreDatas(linkedItemService
                        .getMultiCentreData(accomViewData.getCode()));
                  }
               }
            }

            productCrossSellWrapper.setAccommodationCarousels(carouselViewDatas);
         }
         else
         {
            productCrossSellWrapper.setAccommodationCarousels(Collections
               .<AccommodationCarouselViewData> emptyList());
         }
      }
      return productCrossSellWrapper;
   }

   /*
    * (non-Javadoc)
    *
    * @see
    * uk.co.portaltech.tui.facades.CarouselFacade#getTopPlacesToStayData(uk.co.portaltech.tui.components
    * .model. TopPlacesToStayCarouselComponentModel, java.lang.String, int)
    */
   @Override
   public TopPlacesWrapper getTopPlacesToStayData(
      final TopPlacesToStayCarouselComponentModel component, final String locationCode,
      final String pageType, final String seoPageType, final int i)
   {
      TopPlacesWrapper wrapper = null;
      final List<TopPlacesCarouselModel> topPlacesCarousels = component.getTopPlacesCarousels();
      if (topPlacesCarousels != null && !topPlacesCarousels.isEmpty())
      {
         final TopPlacesCarouselModel carouselModel = topPlacesCarousels.iterator().next();

         if (CarouselLookupType.ENDECA_TOP_PLACES_TO_STAY.equals(carouselModel.getLookupType())
            || CarouselLookupType.CR_STAY_ENDECA_TOP_PLACES_TO_STAY.equals(carouselModel
               .getLookupType()))
         {
            SearchResultData topPlacesToStayResult = null;
            List<ResultData> searchResult = null;
            topPlacesToStayResult =
               searchFacade.getTopPlacesToStayData(locationCode, pageType, seoPageType,
                  carouselModel);
            searchResult = topPlacesToStayResult.getResults();
            wrapper = processResult(searchResult, topPlacesCarousels);
         }
      }
      if (locationFacade != null)
      {
         wrapper.setLocation(locationFacade.getLocationData(locationCode));
      }
      return wrapper;
   }

   /*
    * (non-Javadoc)
    *
    * @see
    * uk.co.portaltech.tui.facades.CarouselFacade#getTopPlacesToStayData(uk.co.portaltech.tui.components
    * .model. DestinationQSAndTopPlacesComponentModel, java.lang.String, int)
    */
   @Override
   public TopPlacesWrapper getTopPlacesForDestQS(
      final DestinationQSAndTopPlacesComponentModel component, final String locationCode,
      final String pageType, final String seoPageType, final int i)
   {
      TopPlacesWrapper wrapper = null;
      final List<TopPlacesCarouselModel> topPlacesCarousels = component.getTopPlacesCarousels();
      if (topPlacesCarousels != null && !topPlacesCarousels.isEmpty())
      {
         final TopPlacesCarouselModel carouselModel = topPlacesCarousels.iterator().next();

         if (CarouselLookupType.ENDECA_TOP_PLACES_TO_STAY.equals(carouselModel.getLookupType()))
         {
            final SearchResultData topPlacesToStayResult =
               searchFacade.getTopPlacesToStayData(locationCode, pageType, seoPageType,
                  carouselModel);
            final List<ResultData> searchResult = topPlacesToStayResult.getResults();
            wrapper = processResult(searchResult, topPlacesCarousels);
         }
      }
      if (wrapper != null)
      {
         wrapper.setLocation(locationFacade.getLocationData(locationCode));
      }
      return wrapper;
   }

   /**
    *
    * @param searchResult
    * @param topPlacesCarousels
    * @return TopPlacesWrapper
    * @description This method extract top places(hotel,villa and resort) data from List<ResultData>
    *              and set the data to TopPlacesWraper object and return it.
    */
   private TopPlacesWrapper processResult(final List<ResultData> searchResult,
      final List<TopPlacesCarouselModel> topPlacesCarousels)
   {

      final TopPlacesWrapper wrapper = new TopPlacesWrapper();
      if (searchResult == null)
      {
         LOG.error("Empty Result from the Endeca for top places to stay");
         final AccommodationCarouselViewData accm = new AccommodationCarouselViewData();
         final AccommodationCarouselViewData villa = new AccommodationCarouselViewData();

         accm.setAccomodationDatas(Collections.<AccommodationViewData> emptyList());
         villa.setAccomodationDatas(Collections.<AccommodationViewData> emptyList());

         wrapper.setAccommodationsData(accm);
         wrapper.setVillaData(villa);

         final LocationCarouselViewData loc = new LocationCarouselViewData();
         loc.setLocationDatas(Collections.<LocationData> emptyList());
         wrapper.setLocationsData(loc);
         return wrapper;
      }
      final List<ResultData> hotelResultData = new ArrayList<ResultData>();
      final List<ResultData> villaResultData = new ArrayList<ResultData>();

      List<ResultData> resortsResultData = new ArrayList<ResultData>();
      for (final ResultData resultData : searchResult)
      {
         if (resultData == null)
         {
            continue;
         }
         if (HOTEL_DATA.equalsIgnoreCase(resultData.getAccommodationType()))
         {
            hotelResultData.add(resultData);
         }
         else if (VILLA_DATA.equalsIgnoreCase(resultData.getAccommodationType()))
         {
            villaResultData.add(resultData);
         }
         else if (LOCATION_DATA.equals(resultData.getTypeOfResult()))
         {
            resortsResultData.add(resultData);
         }
      }

      wrapper.setAccommodationsData(getAccommodationCarouselViewData(hotelResultData,
         topPlacesCarousels, TopPlacesCarouselType.HOTEL));
      wrapper.setVillaData(getAccommodationCarouselViewData(villaResultData, topPlacesCarousels,
         TopPlacesCarouselType.VILLA));

      boolean locationsCarouselFound = false;
      if (!resortsResultData.isEmpty())
      {
         TopPlacesCarouselModel carouselModel = null;
         for (final TopPlacesCarouselModel topCarouselModel : topPlacesCarousels)
         {
            if (TopPlacesCarouselType.RESORT.equals(topCarouselModel.getTopPlacesCarouselType()))
            {
               carouselModel = topCarouselModel;
               locationsCarouselFound = true;
               break;
            }
         }

         if (carouselModel != null)
         {
            if (resortsResultData.size() > carouselModel.getVisibleItems().intValue())
            {
               resortsResultData =
                  resortsResultData.subList(0, carouselModel.getVisibleItems().intValue());
            }
            final List<LocationData> locationDatas =
               locationFacade.getEndecaResortViewData(resortsResultData);
            wrapper.setLocationsData(new LocationCarouselViewData(carouselModel.getTitle(),
               carouselModel.getUid(), locationDatas, carouselModel.getTopPlacesCarouselType()
                  .getCode(), carouselModel.getVisibleItems()));
         }
      }

      if (resortsResultData.isEmpty() || !locationsCarouselFound)
      {
         final LocationCarouselViewData loc = new LocationCarouselViewData();
         loc.setLocationDatas(Collections.<LocationData> emptyList());
         wrapper.setLocationsData(loc);
      }
      return wrapper;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.CarouselFacade#getCrossSellCarouselsData(java.lang.String,
    * java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public CrossSellWrapper getCrossSellCarouselsData(final String locationCode,
      final String productCode, final String pageType, final String seoPageType,
      final CrossSellCarouselComponentModel item, final String acommodationTypeContext)
   {
      CrossSellWrapper crossSellWrapper = new CrossSellWrapper();

      final SearchResultData searchResult =
         searchFacade.getCrossSellCarouselData(locationCode, productCode, pageType, seoPageType,
            item, acommodationTypeContext);
      crossSellWrapper.setHeading(searchResult.getHeading());

      final List<TabResult> tabResults = searchResult.getTabResults();

      if (tabResults != null && !tabResults.isEmpty())
      {
         // bug here somewhere null is added into the result
         tabResults.removeAll(Collections.singleton(null));
         for (final TabResult tabResult : tabResults)
         {
            final List<ResultData> locations = new ArrayList<ResultData>();
            final List<ResultData> accommodations = new ArrayList<ResultData>();
            final List<ResultData> attractions = new ArrayList<ResultData>();
            final List<ResultData> excursions = new ArrayList<ResultData>();

            if (tabResult.getResult() != null && !tabResult.getResult().isEmpty())
            {

               for (final ResultData resultData : tabResult.getResult())
               {

                  if (SearchResultType.ACCOMMODATION.equals(resultData.getType()))
                  {
                     accommodations.add(resultData);
                  }
                  else if (SearchResultType.EXCURSION.equals(resultData.getType()))
                  {
                     excursions.add(resultData);
                  }
                  else if (SearchResultType.ATTRACTION.equals(resultData.getType()))
                  {
                     attractions.add(resultData);
                  }
                  else
                  {
                     if (resultData.getFacetOption() != null)
                     {
                        final String location = resultData.getFacetOption().getFacetOptionName();

                        if (StringUtils.isNotBlank(location) && isLocationTypeCheck(location))
                        {
                           locations.add(resultData);
                        }

                     }
                  }
               }
               String carouselCode = "";
               final String carouselTitle = tabResult.getTitle();
               if (carouselTitle != null)
               {
                  carouselCode = carouselTitle.replaceAll(" ", "");

                  carouselCode = carouselCode.replaceAll("[^A-Za-z]", "");
               }
               crossSellWrapper =
                  getCrossCell(crossSellWrapper, locations, accommodations, excursions,
                     attractions, carouselTitle, carouselCode);

            }
         }
      }
      return crossSellWrapper;

   }

   /**
    * @param location
    * @return
    */
   private boolean isLocationTypeCheck(final String location)
   {
      return "country".equals(location) || "region".equals(location)
         || "destination".equals(location) || "resort".equals(location);
   }

   @Override
   public UpSellCarouselWrapper getUpSellCarouselsData(final String locationCode,
      final String productCode, final String pageType, final String seoPageType,
      final UpsellToHolidayComponentModel item, final String acommodationTypeContext)
   {
      List<AccommodationViewData> result = null;
      final UpSellCarouselWrapper crossSellWrapper = new UpSellCarouselWrapper();

      final SearchResultData searchResult =
         searchFacade.getUpSellCarouselData(locationCode, productCode, pageType, seoPageType, item,
            acommodationTypeContext);

      final List<ResultData> resultDataList = searchResult.getResults();
      if (resultDataList != null && !resultDataList.isEmpty())
      {
         result = accomodationFacade.getAccommodationsWithDetailData(resultDataList);
      }

      crossSellWrapper.setHeading(searchResult.getHeading());
      crossSellWrapper.setAccommodationViewDatas(result);
      return crossSellWrapper;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.CarouselFacade#getCrossSellCarouselsData(java.lang.String,
    * java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public CrossSellWrapper getCrossSellCarouselsData(final String locationCode,
      final String productCode, final String pageType, final String seoPageType,
      final CrossSellCarouselComponentModel item)
   {
      final CrossSellWrapper crossSellWrapper = new CrossSellWrapper();

      final SearchResultData searchResult =
         searchFacade.getCrossSellCarouselData(locationCode, productCode, pageType, seoPageType,
            item);
      crossSellWrapper.setHeading(searchResult.getHeading());

      final List<TabResult> tabResults = searchResult.getTabResults();
      gettabResults(tabResults, crossSellWrapper);
      return crossSellWrapper;

   }

   private List<TabResult> gettabResults(final List<TabResult> result,
      final CrossSellWrapper crossSellWrap)
   {
      CrossSellWrapper crossSellWrapper = crossSellWrap;
      final List<TabResult> tabResults = result;
      if (tabResults != null && !tabResults.isEmpty())
      {
         // bug here somewhere null is added into the result
         tabResults.removeAll(Collections.singleton(null));
         for (final TabResult tabResult : tabResults)
         {
            final List<ResultData> locations = new ArrayList<ResultData>();
            final List<ResultData> accommodations = new ArrayList<ResultData>();
            final List<ResultData> attractions = new ArrayList<ResultData>();
            final List<ResultData> excursions = new ArrayList<ResultData>();

            if (tabResult.getResult() != null && !tabResult.getResult().isEmpty())
            {

               for (final ResultData resultData : tabResult.getResult())
               {

                  if (SearchResultType.ACCOMMODATION.equals(resultData.getType()))
                  {
                     accommodations.add(resultData);
                  }
                  else if (SearchResultType.EXCURSION.equals(resultData.getType()))
                  {
                     excursions.add(resultData);
                  }
                  else if (SearchResultType.ATTRACTION.equals(resultData.getType()))
                  {
                     attractions.add(resultData);
                  }
                  else
                  {
                     if (resultData.getFacetOption() != null)
                     {
                        final String location = resultData.getFacetOption().getFacetOptionName();
                        if (StringUtils.isNotBlank(location) && isLocationTypeCheck(location))
                        {
                           locations.add(resultData);

                        }
                     }
                  }
               }
               String carouselCode = "";
               final String carouselTitle = tabResult.getTitle();
               if (carouselTitle != null)
               {
                  carouselCode = carouselTitle.replaceAll(" ", "");

                  carouselCode = carouselCode.replaceAll("[^A-Za-z]", "");
               }
               crossSellWrapper =
                  getCrossCell(crossSellWrapper, locations, accommodations, excursions,
                     attractions, carouselTitle, carouselCode);

            }
         }
      }
      return tabResults;

   }

   // for reducing Cyclomatic complexity.
   /**
    * @param crossSellWrappers
    * @param location
    * @param accommodation
    * @param excursion
    * @param attraction
    * @param title
    * @param code
    * @return CrossSellWrapper
    */
   private CrossSellWrapper getCrossCell(final CrossSellWrapper crossSellWrappers,
      final List<ResultData> location, final List<ResultData> accommodation,
      final List<ResultData> excursion, final List<ResultData> attraction, final String title,
      final String code)
   {

      final CrossSellWrapper crossSellWrapper = crossSellWrappers;
      final List<ResultData> locations = location;
      final List<ResultData> accommodations = accommodation;
      final List<ResultData> attractions = attraction;
      final List<ResultData> excursions = excursion;
      final String carouselTitle = title;
      final String carouselCode = code;
      final Integer visibleItems = Integer.valueOf(VISIBLE_ITEMS);
      if (locations != null && !locations.isEmpty())
      {
         crossSellWrapper.getLocationCarousels().add(
            new LocationCarouselViewData(carouselTitle, carouselCode, locationFacade
               .getEndecaResortViewData(locations), "", visibleItems));

      }
      if (accommodations != null && !accommodations.isEmpty())
      {
         crossSellWrapper.getAccommodationCarousels().add(
            new AccommodationCarouselViewData(carouselTitle, carouselCode, accomodationFacade
               .getAccommodationsWithEndecaData(accommodations), "", visibleItems));
      }

      if ((excursions != null && !excursions.isEmpty())
         || (attractions != null && !attractions.isEmpty()))
      {
         final AttractionCarouselViewData carouselViewData =
            new AttractionCarouselViewData(carouselTitle, carouselCode, visibleItems);

         if (!excursions.isEmpty())
         {
            carouselViewData.setExcursions(excursionFacade.getExcursionsWithEndecaData(excursions));
         }
         if (!attractions.isEmpty())
         {
            carouselViewData
               .setAttractions(attractionFacade.getAttractionEnrichedData(attractions));
         }
         crossSellWrapper.getAttractionCarousels().add(carouselViewData);

      }

      return crossSellWrapper;

   }

   /**
    * @param event
    * @param sight
    * @param excursion
    * @param thingsToDoWrapper
    * @return ThingsToDoWrapper
    */
   // for reducing Cyclomatic complexity.
   private ThingsToDoWrapper getThingsToDoWrapper(final List<ResultData> event,
      final List<ResultData> sight, final List<ResultData> excursion,
      final ThingsToDoWrapper thingsToDoWrapper)
   {

      final List<ResultData> sights = sight;
      final List<ResultData> events = event;
      final List<ResultData> excursions = excursion;
      if (!excursions.isEmpty())
      {
         thingsToDoWrapper.setExcursions(excursionFacade.getExcursionsWithEndecaData(excursions));
      }

      if (!sights.isEmpty())
      {
         thingsToDoWrapper.setSights(attractionFacade.getAttractionEnrichedData(sights));
      }
      if (!events.isEmpty())
      {
         thingsToDoWrapper.setEvents(attractionFacade.getAttractionEnrichedData(events));
      }
      return thingsToDoWrapper;
   }

   /**
    * @param accommodationsResultDatas
    * @param topPlacesCarousels
    * @param carouselType
    * @return AccommodationCarouselViewData
    *
    * @description This method returns the accommodation carousl wrapper after setting data of same
    *              carousel type passed as argument.
    */
   // for reducing Cyclomatic complexity.
   private AccommodationCarouselViewData getAccommodationCarouselViewData(
      final List<ResultData> accommodationsResultDatas,
      final List<TopPlacesCarouselModel> topPlacesCarousels,
      final TopPlacesCarouselType carouselType)
   {
      AccommodationCarouselViewData accommCarouselViewData = new AccommodationCarouselViewData();
      List<ResultData> accommodationsResultData = accommodationsResultDatas;
      boolean accomCarouselFound = false;

      if (!accommodationsResultData.isEmpty())
      {

         for (final TopPlacesCarouselModel topCarouselModel : topPlacesCarousels)
         {
            if (carouselType.equals(topCarouselModel.getTopPlacesCarouselType()))
            {
               if (accommodationsResultData.size() > topCarouselModel.getVisibleItems().intValue())
               {
                  accommodationsResultData =
                     accommodationsResultData.subList(0, topCarouselModel.getVisibleItems()
                        .intValue());
               }
               final List<AccommodationViewData> accomodationDatas =
                  accomodationFacade.getAccommodationsWithEndecaData(accommodationsResultData);

               accommCarouselViewData =
                  new AccommodationCarouselViewData(topCarouselModel.getTitle(),
                     topCarouselModel.getUid(), accomodationDatas, topCarouselModel
                        .getTopPlacesCarouselType().getCode(), topCarouselModel.getVisibleItems());
               accomCarouselFound = true;
               break;
            }
         }
      }
      if (accommodationsResultData.isEmpty() || !accomCarouselFound)
      {
         accommCarouselViewData.setAccomodationDatas(Collections.<AccommodationViewData>emptyList());
      }
      return accommCarouselViewData;
   }

   @Override
   public List<MediaViewData> getMediaCarouselMediaData(
      final MediaPromoComponentModel carouselModel, final int offset, final int pageSizes)
   {
      int pageSize = pageSizes;

      final MediaFinder finder = mediaFinders.get("mediaFinder_" + carouselModel.getLookupType());

      if (finder == null)
      {
         throw new TUISystemException("No mediaFinder for lookup type "
            + carouselModel.getLookupType() + CHECK_THE_LOOK_UP_TYPE_FIELD);
      }

      // Generate a SearchRequest object to pass to our MediaFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      if (pageSize == 0)
      {
         pageSize = 1;
      }
      searchRequest.setPageSize(pageSize);
      searchRequest.setOffset(offset);
      searchRequest.setRelevantItem(carouselModel);

      return carouselDataPopulator.convertAllMedia(finder.search(searchRequest).getResults());

   }
}
