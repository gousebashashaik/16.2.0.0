/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.MultiValueFilterComponentModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.components.model.AccommodationStandardComponentModel;
import uk.co.portaltech.tui.components.model.AccommodationsResultComponentModel;
import uk.co.portaltech.tui.components.model.AttractionResultPaneComponentModel;
import uk.co.portaltech.tui.components.model.AvailableFlightsComponentModel;
import uk.co.portaltech.tui.components.model.BookingComponentModel;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.DealsComponentModel;
import uk.co.portaltech.tui.components.model.InteractiveMapComponentModel;
import uk.co.portaltech.tui.components.model.LatestVillaAvailabilityDateComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselModel;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ProductLeadPriceComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesCarouselModel;
import uk.co.portaltech.tui.components.model.UpsellToHolidayComponentModel;
import uk.co.portaltech.tui.components.model.VillaAvailabilityComponentModel;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FlightAvailabilityViewData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationFilterDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.AttractionResultDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.DestinationQuickSearchDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityRequest;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityResponse;
import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.web.common.enums.SortParameters;

/**
 * The Interface SearchFacade.
 * 
 * @author Abi
 */
public interface SearchFacade
{

   /**
    * Gets the deals data.
    * 
    * @param locationCode the location code
    * @param pageType the page type
    * @param seoPageType the seo page type
    * @param item the item
    * @return the deals data
    */
   List<AccommodationViewData> getDealsData(String locationCode, String pageType,
      String seoPageType, DealsComponentModel item);

   /**
    * Gets the places to stay data.
    * 
    * @param locationCode the location code
    * @param pageType the page type
    * @param seoPageType the seo page type
    * @param item the item
    * @return the places to stay data
    */
   List<AccommodationViewData> getPlacesToStayData(String locationCode, String pageType,
      String seoPageType, AccommodationCarouselModel item);

   /**
    * Gets the things to do view data.
    * 
    * @param locationCode the location code
    * @param pageType the page type
    * @param seoPageType the seo page type
    * @param item the item
    * @return the things to do view data
    */
   SearchResultData getThingsToDoViewData(String locationCode, String pageType, String seoPageType,
      ThingsToDoCarouselModel item);

   /**
    * Gets the tourist board things to do view data.
    * 
    * @param locationCode the location code
    * @param pageType the page type
    * @param seoPageType the seo page type
    * @param item the item
    * @return the tourist board things to do view data
    */
   SearchResultData getTouristBoardThingsToDoViewData(String locationCode, String pageType,
      String seoPageType, ThingsToDoCarouselModel item);

   SearchResultData getProductCrossSellData(String locationCode, String pageType, String seoType,
      ProductCrossSellCarouselComponentModel item, String productCode);

   /**
    * Gets the quick search result data.
    * 
    * @param searchParameter the search parameter
    * @return the quick search result data
    */
   DestinationQuickSearchDataWrapper getQuickSearchResultData(String searchParameter);

   /**
    * Gets the places to stay carousel data.
    * 
    * @param locationCode the location code
    * @param pageType the page type
    * @param seoType the seo type
    * @param item the item
    * @return the places to stay carousel data
    */
   List<LocationData> getPlacesToStayCarouselData(String locationCode, String pageType,
      String seoType, PlacesToStayCarouselModel item);

   /**
    * Gets the accommodation price data.
    * 
    * @param accommodationCode the accommodation code
    * @param bookingComponentModel the booking component model
    * @return the accommodation price data
    */
   AccommodationViewData getAccommodationPriceData(String accommodationCode,
      BookingComponentModel bookingComponentModel);

   /**
    * Gets the top places to stay data.
    * 
    * @param locationCode the location code
    * @param pageType the page type
    * @param seoPageType the seo page type
    * @param carouselModel the carousel model
    * @return the top places to stay data
    */
   SearchResultData getTopPlacesToStayData(String locationCode, String pageType,
      String seoPageType, TopPlacesCarouselModel carouselModel);

   /**
    * Gets the accommodation result and filter data.
    * 
    * @param categoryCode the category code
    * @param pageType the page type
    * @param seoType the seo type
    * @param facetOptionNames the facet option names
    * @param filterParams the filter params
    * @param item the item
    * @return the accommodation result and filter data
    */
   AccommodationFilterDataWrapper getAccommodationResultAndFilterData(String categoryCode,
      String pageType, String seoType, List<String> facetOptionNames, List<String> filterParams,
      AccommodationsResultComponentModel item);

   /**
    * Gets the attraction result pane data.
    * 
    * @param categoryCode the category code
    * @param pageType the page type
    * @param seoType the seo type
    * @param sortBy the sort by
    * @param item the item
    * @return the attraction result pane data
    */
   AttractionResultDataWrapper getAttractionResultPaneData(String categoryCode, String pageType,
      String seoType, String sortBy, AttractionResultPaneComponentModel item);

   /**
    * Gets the interactive map data.
    * 
    * @param categoryCode the category code
    * @param pageType the page type
    * @param seoPageType the seo page type
    * @param component the component
    * @return the interactive map data
    */
   MapDataWrapper getInteractiveMapData(String categoryCode, String pageType, String seoPageType,
      InteractiveMapComponentModel component);

   /**
    * Gets the cross sell carousel data.
    * 
    * @param locationCode the location code
    * @param productCode the product code
    * @param pageType the page type
    * @param seoType the seo type
    * @param item the item
    * @param acommodationTypeContext the acommodation type context
    * @return the cross sell carousel data
    */
   SearchResultData getCrossSellCarouselData(String locationCode, String productCode,
      String pageType, String seoType, CrossSellCarouselComponentModel item,
      String acommodationTypeContext);

   /**
    * Gets the cross sell carousel data.
    * 
    * @param locationCode the location code
    * @param productCode the product code
    * @param pageType the page type
    * @param seoType the seo type
    * @param item the item
    * @return the cross sell carousel data
    */
   SearchResultData getCrossSellCarouselData(String locationCode, String productCode,
      String pageType, String seoType, CrossSellCarouselComponentModel item);

   /**
    * Gets the villa availability.
    * 
    * @param item the item
    * @param villaAvailabilityRequest the villa availability request
    * @return the villa availability
    */
   VillaAvailabilityResponse getVillaAvailability(VillaAvailabilityComponentModel item,
      VillaAvailabilityRequest villaAvailabilityRequest);

   /**
    * Gets the first available date for villa.
    * 
    * @param latestVillaAvailabilityComponentModel the latest villa availability component model
    * @return the first available date for villa
    */
   DateTime getFirstAvailableDateForVilla(
      LatestVillaAvailabilityDateComponentModel latestVillaAvailabilityComponentModel);

   /**
    * Gets the holiday packages result data for single accom.
    * 
    * @param searchParameter the search parameter
    * @param siteBrand the site brand
    * @return the holiday packages result data for single accom
    * @throws SearchResultsBusinessException the search results business exception
    */
   SearchResultsViewData getHolidayPackagesResultDataForSingleAccom(
      SearchResultsRequestData searchParameter, String siteBrand)
      throws SearchResultsBusinessException;

   /**
    * Gets the holiday packages result data for facets.
    * 
    * @param searchParameter the search parameter
    * @param siteBrand the site brand
    * @return the holiday packages result data for facets
    * @throws SearchResultsBusinessException the search results business exception
    */
   SearchResultsViewData getHolidayPackagesResultDataForFacets(
      SearchResultsRequestData searchParameter, String siteBrand)
      throws SearchResultsBusinessException;

   /**
    * Gets the holiday packages result data for single accom facets.
    * 
    * @param searchParameter the search parameter
    * @param siteBrand the site brand
    * @return the holiday packages result data for single accom facets
    * @throws SearchResultsBusinessException the search results business exception
    */
   SearchResultsViewData getHolidayPackagesResultDataForSingleAccomFacets(
      SearchResultsRequestData searchParameter, String siteBrand)
      throws SearchResultsBusinessException;

   /**
    * Created for No Results widen Criteia search.
    * 
    * @param searchParameter the search parameter
    * @return the dates only search result
    * @throws SearchResultsBusinessException the search results business exception
    */
   SearchResultsViewData getDatesOnlySearchResult(SearchResultsRequestData searchParameter)
      throws SearchResultsBusinessException;

   /**
    * This method will return the SearchResultViewData in sorted order based on the user provided
    * sort preference.
    * 
    * @param searchParameter the search parameter
    * @param siteBrandID the site brand id
    * @return sorted SearchResultsViewData
    * @throws SearchResultsBusinessException the search results business exception
    */
   SearchResultsViewData getSortedHolidayPackagesResultData(
      SearchResultsRequestData searchParameter, String siteBrandID)
      throws SearchResultsBusinessException;

   /**
    * This method sorts SearchResultsViewData in the given order.
    * 
    * @param searchResult the search result
    * @param searchParameter the search parameter
    * @return the search results view data
    */
   SearchResultsViewData sortSearchResultsViewData(final SearchResultsViewData searchResult,
      SearchResultsRequestData searchParameter);

   /**
    * This method returns the array of SortParameters for given SortCriteria code.
    * 
    * @param sortCriteriaCode the sort criteria code
    * @return array of SortParameters
    */

   SortParameters[] getSortParameters(String sortCriteriaCode);

   /**
    * Retrieve the information of particular accommodation.
    * 
    * @param resultRequestData the result request data
    * @param packageid the packageid
    * @param finalpos the finalpos
    * @param index the index
    * @param brandType the brand type
    * @return BookFlowAccommodationViewData
    * @throws SearchResultsBusinessException the search results business exception
    */

   BookFlowAccommodationViewData getPackageSummaryAccommodationData(
      SearchResultsRequestData resultRequestData, String packageid, String finalpos, String index,
      String brandType) throws SearchResultsBusinessException;

   BookFlowAccommodationViewData getPackageForShortlist(SearchResultsRequestData resultRequestData,
      String packageid, String brandType) throws SearchResultsBusinessException;

   /**
    * This method checks if the given package is found or not.
    * 
    * @param searchResultViewData the search result view data
    * @param packageId the package id
    * @return boolean
    */
   boolean isPackageFound(SearchResultViewData searchResultViewData, String packageId);

   /**
    * User criteria in session.
    * 
    * @return true, if successful
    */
   boolean userCriteriaInSession();

   /**
    * Facet search results in session.
    * 
    * @return true, if successful
    */
   boolean facetSearchResultsInSession();

   /**
    * Populate paginated view data.
    * 
    * @param searchParameter the search parameter
    * @param searchResultsViewData the search results view data
    * @param searchResults the search results
    * @param siteBrand the site brand
    * @return the search results view data
    * @throws SearchResultsBusinessException the search results business exception
    */
   SearchResultsViewData populatePaginatedViewData(SearchResultsRequestData searchParameter,
      final SearchResultsViewData searchResultsViewData, EndecaSearchResult searchResults,
      String siteBrand) throws SearchResultsBusinessException;

   /**
    * Sort.
    * 
    * @param searchParameter the search parameter
    * @param searchResult the search result
    */
   void sort(SearchResultsRequestData searchParameter, final EndecaSearchResult searchResult);

   /**
    * Gets the packages from endeca cache.
    * 
    * @param searchParameter the search parameter
    * @return the packages from endeca cache
    * @throws SearchResultsBusinessException the search results business exception
    */
   EndecaSearchResult getPackagesFromEndecaCache(SearchResultsRequestData searchParameter)
      throws SearchResultsBusinessException;

   EndecaSearchResult getRecommendedPackagesFromEndecaCache(SearchResultsRequestData searchParameter)
      throws SearchResultsBusinessException;

   /**
    * Gets the selected package.
    * 
    * @param resultRequestData the result request data
    * @param selectedPackageID the selected package id
    * @return the selected package
    * @throws SearchResultsBusinessException the search results business exception
    */
   Holiday getSelectedPackage(SearchResultsRequestData resultRequestData, String selectedPackageID)
      throws SearchResultsBusinessException;

   /**
    * Checks if board basis is upgraded.
    * 
    * @param boardBasisCode the board basis code
    * @return true, if board basis is upgraded
    */
   boolean isBoardBasisUpgrade(String boardBasisCode);

   /**
    * 
    * @return
    * @throws SearchResultsBusinessException
    */
   Map<String, List<FlightAvailabilityViewData>> getFlightAvailability(String productCode,
      AvailableFlightsComponentModel availableFlightsComponentModel)
      throws SearchResultsBusinessException;

   /**
    * This method is used to get the Accommodation Standard data for Florida.
    * 
    * @param locationCode the locationCode
    * @param pageType the pageType
    * @param seoPageType the seoPageType
    * @param item the itemModel
    * @param acommodationTypeContext the acommodationTypeContext
    * @return SearchResultData
    */
   SearchResultData getAccommodationStandardData(String locationCode, String pageType,
      String seoPageType, AccommodationStandardComponentModel item);

   /**
    * @param locationCode
    * @param productCode
    * @param pageType
    * @param seoType
    * @param item
    * @param acommodationTypeContext
    * @return
    */
   SearchResultData getUpSellCarouselData(String locationCode, String productCode, String pageType,
      String seoType, UpsellToHolidayComponentModel item, String acommodationTypeContext);

   /**
    * @param accommodationCode
    * @param productLeadPriceComponentModel
    * @return
    */
   AccommodationViewData getProductLeadPriceData(String locationCode,
      ProductLeadPriceComponentModel productLeadPriceComponentModel);

   /**
    * @param locationCode
    * @param pageType
    * @param seoType
    * @param item
    * @param productCode
    * @return
    */
   SearchResultData getProductCrossSellCarouselData(String locationCode, String pageType,
      String seoType, PlacesToStayCarouselModel item, String productCode);

   /**
    * @param productCodes
    * @return
    */
   List<AccommodationViewData> getRecommendedAccomPriceInfo(List<String> productCodes);

   /**
    * Gets the holiday packages result data.
    * 
    * @param searchParameter the search parameter
    * @param siteBrandID the site brand id
    * @return the holiday packages result data
    * @throws SearchResultsBusinessException the search results business exception
    */
   SearchResultsViewData getHolidayPackagesResultData(SearchResultsRequestData searchParameter,
      String siteBrandID) throws SearchResultsBusinessException;

   /**
    * @param searchParameter
    * @param siteBrandID
    * @return
    * @throws SearchResultsBusinessException
    */
   SearchResultsViewData getRecommendedHolidayPackagesResultData(
      SearchResultsRequestData searchParameter, String siteBrandID)
      throws SearchResultsBusinessException;

   /**
    * @param accomCode
    * @return
    */
   AccommodationModel getAccommodationModel(String accomCode);

   String getBackToSearchURL(SearchResultsRequestData searchResultsRequestData);

   /**
    * @param filterComponent
    * @return facilityList
    */
   List<FacilityModel> getFacilityList(MultiValueFilterComponentModel filterComponent);

   /**
    * @param holidayViewData
    * @return durationMap
    */
   Map<String, String> populateDurationMap(HolidayViewData holidayViewData);

   PageResponse getPageResponse();

   /**
    * @param locationCode
    * @param pageType
    * @param seoType
    * @param carouselModel
    * @return
    */
   List<LocationData> getPlacesToStayCarouselDataCruise(String locationCode, String pageType,
      String seoType, PlacesToStayCarouselModel carouselModel);

}
