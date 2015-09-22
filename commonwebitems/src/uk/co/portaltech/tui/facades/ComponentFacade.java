/**
 *
 */
package uk.co.portaltech.tui.facades;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.web.view.data.ABTestViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.BoardBasisData;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.FeatureListViewData;
import uk.co.portaltech.tui.web.view.data.HasFeatures;
import uk.co.portaltech.tui.web.view.data.HeroCarouselViewData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MediaPromoViewData;
import uk.co.portaltech.tui.web.view.data.PlacesOfInterestViewData;
import uk.co.portaltech.tui.web.view.data.PlacesToStayWrapper;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.SearchPanelViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.ThingsToDoWrapper;
import uk.co.portaltech.tui.web.view.data.UpSellCarouselWrapper;
import uk.co.portaltech.tui.web.view.data.WidenSearchCriteriaData;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationFilterDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationStandardWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.DestinationQuickSearchDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.FlightAvailabilityViewDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.ProductCrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityRequest;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityResponse;
import uk.co.tui.exception.SearchResultsBusinessException;

/**
 * @author omonikhide
 * 
 */
public interface ComponentFacade
{

   // New Method Added To Feth Data For VillaDetailsComponent
   /**
    * 
    * @param villaCode
    * @return AccommodationViewData
    * @description This method returns data for villa details component.
    */
   AccommodationViewData getVillaDetailsComponentData(String villaCode);

   <T extends AbstractCMSComponentModel> T getComponent(String componentUid)
      throws NoSuchComponentException;

   List<AccommodationCarouselViewData> getAllAccomodationCarouselsData(String componentUid,
      String locationCode) throws NoSuchComponentException;

   HeroCarouselViewData getHeroCarouselData(final String componentUid, int index, Integer pageSize)
      throws NoSuchComponentException;

   /**
    * @param componentUid
    * @param index
    * @param size
    * @return
    * @throws NoSuchComponentException
    */
   MediaPromoViewData getMediaCarouselData(String componentUid, int index, Integer size)
      throws NoSuchComponentException;

   ThingsToDoWrapper getThingsToDoCarousels(String componentUid, String locationCode,
      String productCode, String pageType, String seoPageType, int visibleItems)
      throws NoSuchComponentException;

   ThingsToDoWrapper getThingsToDoCarouselsforTouristBoard(String componentUid,
      String locationCode, String productCode, String pageType, String seoPageType, int visibleItems)
      throws NoSuchComponentException;

   CrossSellWrapper getCrossSellCarousels(String componentUid, String locationCode,
      String productCode, String pageType, String seoPageType, String acommodationTypeContext)
      throws NoSuchComponentException;

   CrossSellWrapper getCrossSellCarousels(String componentUid, String locationCode,
      String productCode, String pageType, String seoPageType) throws NoSuchComponentException;

   VillaAvailabilityResponse getVillaAvailability(String componentUid,
      VillaAvailabilityRequest villaAvailabilityRequest) throws NoSuchComponentException;

   /**
    * Retrieves the information of the selected A/B variation for an A/B test session.
    * 
    * @param componentUid Uid of the A/B Test Component that manages this A/B test session
    * @return The {@link ABTestViewData} object that contains the information of the selected A/B
    *         variation for this A/B test session
    */
   ABTestViewData getABTestData(String componentUid);

   /**
    * Retrieves the information of the selected A/B variation for an A/B test session from the
    * specified Variant Group.
    * 
    * @param componentUid Uid of the A/B Test Component that manages this A/B test session
    * @param variantName The Variant Group name that manages this A/B test session
    * @return The {@link ABTestViewData} object that contains the information of the selected A/B
    *         variation for this A/B test session
    */
   ABTestViewData getABTestData(String componentUid, String variantName);

   /**
    * Retrieves the Variant Test name for first time users.
    * 
    * @return the Variant Name.
    */
   String getVariantForNewUser(String testCode);

   /**
    * @param componentUid
    * @param testNames
    */
   ABTestViewData getVariant(String componentUid, Set<String> testNames);

   LocationData getThingsToDoAndSeeEditorialData(String componentUid, String locationCode)
      throws NoSuchComponentException;

   /**
    * retrieves accommodation carousel data for a location
    * 
    * @param locationCode The code for the location
    * @return The location view data with a list of carousel view data.
    * @throws NoSuchComponentException
    */
   LocationData getAccommodationCarouselData(String locationCode, String pageType, String seoType,
      String componentUid) throws NoSuchComponentException;

   List<AccommodationViewData> getDealsData(String locationCode, String productCode,
      String pageType, String seoPageType, String componentUid) throws NoSuchComponentException;

   /**
    * Retrieves highlights (USPs) associated with a location
    * 
    * @param componentUid Uid of the Highlights Component
    * @param locationCode The code for the location
    * @return The {@link LocationData} object with a map containing USPs features and their
    *         respective values
    */
   LocationData getLocationHighlights(String componentUid, String locationCode);

   /**
    * Retrieves highlights (USPs) associated with a location
    * 
    * @param componentUid Uid of the Highlights Component
    * @param locationCode The code for the location
    * @return The {@link LocationData} object with a map containing USPs features and their
    *         respective values
    */
   LocationData getLocationAtAGlance(String componentUid, String locationCode);

   /**
    * Retrieves highlights (USPs) associated with an accommodation
    * 
    * @param componentUid Uid of the Highlights Component
    * @param accommodationCode The code for the accommodation
    * @return The {@link AccommodationViewData} object with a map containing USPs features and their
    *         respective values
    */
   AccommodationViewData getAccommodationHighlights(String componentUid, String accommodationCode);

   /**
    * Retrieves highlights (USPs) associated with a Product Range
    * 
    * @param componentUid Uid of the Highlights Component
    * @param productRangeCode The code for the Product Range
    * @return The {@link ProductRangeViewData} object with a map containing USPs features and their
    *         respective values
    */
   ProductRangeViewData getProductRangeHighlights(String componentUid, String productRangeCode);

   /**
    * retrieves hero carousel data related to a location or product also depending on the lookup
    * type
    * 
    * @param componentUid The component id to get configured lookuptype
    * @param code The accommodation or location code
    * @return A list of carousel image data that would be associated with an accommodation or
    *         location
    */
   HasFeatures getHeroCarouselViewData(String componentUid, String code, String type);

   HasFeatures getMediaSearchResultsViewData(String lookuptype, String code, String type);

   /**
    * retrieves hero carousel data related to a location or product also depending on the lookup
    * type for Attraction Light Box component
    * 
    * @param componentUid The component id to get configured lookuptype
    * @param code The accommodation or location code
    * @return A list of carousel image data that would be associated with an accommodation or
    *         location
    */
   HasFeatures getHeroCarouselViewDataForAttractionLightBox(String componentUid, String code,
      String type);

   /**
    * Retrieves hero carousel data related to a location or product also depending on the lookup
    * type for Confirmation page count down image component
    * 
    * @param componentUid The component id to get configured lookuptype
    * @param code The accommodation or location code
    * @return A list of carousel image data that would be associated with an accommodation or
    *         location
    */
   HasFeatures getConfPageHeroCarouselViewData(String componentUid, String code, String type);

   /**
    * retrieves image carousel data related to a location also depending on the lookup type
    * 
    * @param componentUid The component id to get configured lookuptype
    * @param code The location code
    * @return A list of carousel image data that would be associated with location
    */
   HasFeatures getImageCarouselViewData(String componentUid, String code, String type);

   /**
    * Retrieves product cross-selll carousel data
    * 
    * @param componentUid Uid of the component
    * @param locationCode The location code
    * @return location data containing a list location data displayed in the carousel component
    * @throws NoSuchComponentException
    */
   ProductCrossSellWrapper getProductCrossSellCarouselData(String componentUid, String pageType,
      String seoType, String locationCode, String productCode) throws NoSuchComponentException;

   DestinationQuickSearchDataWrapper getQuickSearchResultData(String searchParameter)
      throws NoSuchComponentException;

   /**
    * @param categoryCode
    * @param component
    * @return TopPlacesWrapper object
    */
   TopPlacesWrapper getTopPlacesToStayData(String categoryCode, String pageType,
      String seoPageType, TopPlacesToStayCarouselComponentModel component);

   /**
    * @param categoryCode
    * @param component
    * @return
    */
   TopPlacesWrapper getTopPlacesForDestQS(String categoryCode, String pageType, String seoPageType,
      DestinationQSAndTopPlacesComponentModel component);

   /**
    * Retrieves a list of location data for places to stay carousel
    * 
    * @param locationCode The location code
    * @param componentId The component Id
    * @return list of LocationData
    */
   PlacesToStayWrapper getPlacesToStayCarouselData(String locationCode, String pageType,
      String seoType, String componentId);

   /**
    * Retrieves price data provided by Endeca related to an accommodation for booking
    * 
    * @param componentUid UID of Booking Component
    * @param accommodationCode The accommodation code
    * @return Accommodation view data containing the result searching performed on Endeca
    */
   AccommodationViewData getAccommodationPrice(String componentUid, String accommodationCode);

   AccommodationFilterDataWrapper getAccommodationResultAndFilterData(String componentUid,
      String pageType, String seoType, String categoryCode, List<String> facetOptionNames,
      List<String> filterParams) throws NoSuchComponentException;

   /**
    * 
    * @param categoryCode
    * @param pageType
    * @param seoPageType
    * @return InspirationalMapWrapper
    * @throws NoSuchComponentException
    */
   MapDataWrapper getInteractiveMapData(String categoryCode, String pageType, String seoPageType,
      String componentUid) throws NoSuchComponentException;

   /**
    * @param productCode
    * @return ExcursionViewData
    * 
    */
   PriceAndAvailabilityWrapper getExcursionViewDataForPriceAndAvailability(String productCode)
      throws NoSuchComponentException;

   HolidayViewData getHolidayPackagesResultData(SearchResultsRequestData searchParameter,
      String siteBrandId) throws SearchResultsBusinessException;

   /**
    * 
    * @return SearchPanelComponenntModel
    * @throws SearchResultsBusinessException
    * 
    */
   SearchPanelComponentModel getSearchPanelComponent() throws SearchResultsBusinessException;

   /**
    * @return
    * @throws SearchResultsBusinessException
    * 
    *            To get new search panel component
    */
   NewSearchPanelComponentModel getNewSearchPanelComponent() throws SearchResultsBusinessException;

   /**
    * This method checks if variantGroup is there in the system. If yes then it returns its
    * percentage else retrun 0
    * 
    * @param variantCode
    * @return percentage
    */
   int getVariantPercentageByCode(String variantCode);

   /**
    * @param componentUid
    * @param code
    * @param type
    * @return
    */
   HasFeatures getMediaCarouselViewData(String componentUid, String code, String type);

   SearchResultsViewData datesOnlySearch(WidenSearchCriteriaData widenSearchRequest);

   /**
    * @param accommCode
    * @return boardMap
    * @description This method takes accommodationCode as argument and return the board basis datas
    *              available for accommodation with particular accommodationCode. Before returning
    *              the boardbasis data splits the data in two types of board list a)normal
    *              accommodation b)complex accommodation
    */
   Map<String, List<BoardBasisData>> getAccommodationBoardBasisComponentData(String accommCode);

   // Flight Option Page
   /**
    * @param requestData
    * @return
    * @throws SearchResultsBusinessException
    */
   HolidayViewData getSingleAccommSearchData(SearchResultsRequestData requestData)
      throws SearchResultsBusinessException;

   /**
    * @return
    * @throws SearchResultsBusinessException
    */
   HolidayFinderComponentModel getHolidayFinderComponent() throws SearchResultsBusinessException;

   SearchPanelViewData getSearchPanelData() throws SearchResultsBusinessException;

   /**
    * 
    * @param productCode
    * @param componentId
    * @return
    * @throws SearchResultsBusinessException
    */
   FlightAvailabilityViewDataWrapper getFlightAvailability(String productCode, String componentId)
      throws SearchResultsBusinessException;

   /**
    * This method is used to get the Accommodation Standard for Florida.
    * 
    * @param componentUid the component id.
    * @param locationCode the locationCode
    * @param pageType the pageType
    * @param seoPageType the seoPageType
    * @return AccommodationStandardWrapper
    * @throws NoSuchComponentException
    */
   AccommodationStandardWrapper getAccommodationStandard(String componentUid, String locationCode,
      String pageType, String seoPageType) throws NoSuchComponentException;

   /**
    * @param locationCode YTODO
    * @return
    */
   FeatureListViewData getFeatureListViewData(String locationCode, String noncoreHolidayType);

   /**
    * @param locationCode
    * @return
    */
   PlacesOfInterestViewData getPlacesOfInterestViewData(String locationCode);

   /**
    * @param locationCode
    * @param pageType
    * @param seoType
    * @param componentId
    * @return
    */
   PlacesToStayWrapper getDayTripPlacesToStayCarouselData(String locationCode, String pageType,
      String seoType, String componentId);

   /**
    * @param componentUid
    * @param locationCode
    * @param productCode
    * @param pageType
    * @param seoPageType
    * @param acommodationTypeContext
    * @return
    * @throws NoSuchComponentException
    */
   UpSellCarouselWrapper getUpsellHolidayCarousels(String componentUid, String locationCode,
      String productCode, String pageType, String seoPageType, String acommodationTypeContext)
      throws NoSuchComponentException;

   /**
    * Retrieves price data provided by Endeca related to booking
    * 
    * @param componentUid UID of Booking Component
    * @param locationCode The category code
    * @return Accommodation view data containing the result searching performed on Endeca
    */
   AccommodationViewData getLeadPrice(String componentUid, String locationCode);

   /**
    * @param componentUid
    * @param pageType
    * @param seoType
    * @param locationCode
    * @param productCode
    * @param placesToStayCarousels
    * @return
    * @throws NoSuchComponentException
    */
   ProductCrossSellWrapper getProductCrossSellCarouselData(String componentUid, String pageType,
      String seoType, String locationCode, String productCode,
      List<PlacesToStayCarouselModel> placesToStayCarousels) throws NoSuchComponentException;

   /**
    * @param locationCode
    * @return
    */
   ItineraryViewData getNonBookableAttractions(String locationCode);

   /**
    * @param searchRequestData
    * @param siteBrand
    * @return
    * @throws SearchResultsBusinessException
    */
   HolidayViewData getReccommendedHolidayPackageData(SearchResultsRequestData searchRequestData,
      String siteBrand) throws SearchResultsBusinessException;

   /**
    * @param productCodes
    * @param pageType
    * @param componentUid
    * @return
    */
   List<AccommodationViewData> getRecommendedAccomPriceInfo(List<String> productCodes,
      String pageType, String componentUid);

   /**
    * @param viewData
    */
   void setFeaturePrority(HolidayViewData viewData);

   /**
    * @param componentUid
    * @param locationCode
    * @return
    * @throws NoSuchComponentException
    */
   LocationData getThingsToSeeAndDoEditorialData(String componentUid, String locationCode)
      throws NoSuchComponentException;

   /**
    * @param crossSellWrapper
    * @return
    */
   CrossSellWrapper checkForCruiseLocation(CrossSellWrapper crossSellWrapper);

}
