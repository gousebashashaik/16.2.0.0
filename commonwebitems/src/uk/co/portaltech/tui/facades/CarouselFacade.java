package uk.co.portaltech.tui.facades;

/*
 * Originating Unit: Portal Technology Systems Ltd http://www.portaltech.co.uk
 * 
 * Copyright Portal Technology Systems Ltd.
 * 
 * $Id: $
 */

import java.util.List;

import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel;
import uk.co.portaltech.tui.components.model.HeroCarouselComponentModel;
import uk.co.portaltech.tui.components.model.MediaPromoComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselModel;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TouristBoardBannerComponentModel;
import uk.co.portaltech.tui.components.model.UpsellToHolidayComponentModel;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ThingsToDoWrapper;
import uk.co.portaltech.tui.web.view.data.UpSellCarouselWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.ProductCrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;

/**
 * @author James Johnstone This Class deals with the retrieval of a Carousel and the relevant data
 *         for its attached Products. It acts as a go-between for the web layer and the business
 *         logic and retrieves only the details needed by the web layer.
 */
public interface CarouselFacade
{

   /**
    * This method generates a List of CarouselData objects representing the Products or the Media
    * which are attached to our given Carousel, at the given index.
    *
    * NOTE: This method will only work if there is a relevant Finder(Product/Media) implementation
    * for the lookupType of the Carousel we are using.
    *
    * @param carouselCode A String representing the code of the Carousel who's products we want.
    * @param index An int representing the index of the products to retrieve.
    * @param pageSize indicates the number of items to be returned
    * @return A List of CarouselData(Product/Media) for the given details.
    */
   List<AccommodationViewData> getAccommodationViewData(
      AccommodationCarouselModel accommodationCarouselModel, String locationCode, String pageType,
      String seoPageType, int index, int visibleItems);

   /**
    * This method generates and executes a FlexibleSearchQuery to search the database for a Carousel
    * matching our provided code.
    *
    * @param code A String representing the code of the Carousel we are looking for.
    * @return A CarouselModel representing the Carousel with the matching code.
    */
   List<MediaViewData> getHeroCarouselMediaData(HeroCarouselComponentModel carouselModel,
      int offset, int pageSize);

   List<MediaViewData> getMediaCarouselMediaData(MediaPromoComponentModel carouselModel,
      int offset, int pageSize);

   AccommodationViewData getHeroCarouselMediaDataByProductCode(String productCode, int offset,
      int pageSize);

   /**
    * This method returns a list of things to do data based on the carousel look-up type. most of
    * the cases the data is populated based on the endeca searcvh result
    *
    * @param locationCode
    * @param item
    * @return A list of things to do items
    */
   ThingsToDoWrapper getThingsToDoViewData(String locationCode, String pageType,
      String seoPageType, ThingsToDoCarouselComponentModel item);

   /**
    * This method returns a list of things to do data based on the carousel look-up type. most of
    * the cases the data is populated based on the endeca searcvh result
    *
    * @param locationCode
    * @param item
    * @return A list of things to do items
    */
   ThingsToDoWrapper getThingsToDoForTouristBoardViewData(String locationCode, String pageType,
      String seoPageType, TouristBoardBannerComponentModel item);

   /**
    * This method returns a list of things to do data based on the carousel look-up type. most of
    * the cases the data is populated based on the endeca searcvh result
    *
    * @param locationCode
    * @param item
    * @return A list of things to do items
    */
   List<LocationData> getLocationViewData(String locationCode, CrossSellCarouselComponentModel item);

   /**
    * It gets search result object and iterates and fetches accommodation data with a list of
    * product cross-sell carousel data
    *
    * @param locationCode The location code
    * @param pageType type of the page. ex. cruise only page
    * @param seoPageType
    * @param item The carousel component model
    * @param productCode accommodation code of the product.
    * @return accommodation data with a list of product cross-sell carousel data
    */
   ProductCrossSellWrapper getProductCrossSellCarouselData(String locationCode, String pageType,
      String seoType, ProductCrossSellCarouselComponentModel item, String productCode);

   /**
    *
    * @param component
    * @param locationCode
    * @param pageType
    * @param seoPageType
    * @param i
    * @return TopPlacesWrapper
    */
   TopPlacesWrapper getTopPlacesToStayData(TopPlacesToStayCarouselComponentModel component,
      String locationCode, String pageType, String seoPageType, int i);

   /**
    *
    * @param componentUid
    * @param locationCode
    * @param pageType
    * @param seoPageType
    * @return CrossSellWrapper
    */
   CrossSellWrapper getCrossSellCarouselsData(String locationCode, String productCode,
      String pageType, String seoPageType, CrossSellCarouselComponentModel component,
      String acommodationTypeContext);

   /**
    *
    * @param component
    * @param locationCode
    * @param pageType
    * @param seoPageType
    * @param i
    * @return TopPlacesWrapper
    */
   TopPlacesWrapper getTopPlacesForDestQS(DestinationQSAndTopPlacesComponentModel component,
      String locationCode, String pageType, String seoPageType, int i);

   /**
    *
    * 
    * @param locationCode
    * @param productCode
    * @param pageType
    * @param seoPageType
    * @param component
    * @return CrossSellWrapper
    */
   CrossSellWrapper getCrossSellCarouselsData(String locationCode, String productCode,
      String pageType, String seoPageType, CrossSellCarouselComponentModel component);

   /**
    * @param locationCode
    * @param productCode
    * @param pageType
    * @param seoPageType
    * @param item
    * @param acommodationTypeContext
    * @return
    */
   UpSellCarouselWrapper getUpSellCarouselsData(String locationCode, String productCode,
      String pageType, String seoPageType, UpsellToHolidayComponentModel item,
      String acommodationTypeContext);

   /**
    * @param locationCode
    * @param pageType
    * @param seoType
    * @param item
    * @param productCode
    * @return
    */
   ProductCrossSellWrapper getProductCrossSellCarouselHolidaysData(String locationCode,
      String pageType, String seoType, PlacesToStayCarouselModel item, String productCode);

   /**
    * This method gets first image Url of the Carousel to the given product code for the meta data.
    * 
    * @param productCode A String representing the productcode of Carousel image we are looking for.
    * @return String A String representing the carousel first image url.
    */
   String getMetaImageUrlByProductCode(String productCode);
}
