/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.components.model.AdvertisedPackagesCollectionComponentModel;
import uk.co.portaltech.tui.converters.AccommodationOption;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.EssentialInformationViewData;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SustainableTourismComponentViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.TwoCentreSelectorViewData;





/**
 * @author gagan<br/>
 *         l.furrer<br/>
 *         s.consolino o.ikhide
 *
 */
public interface AccommodationFacade
{

    /**
     * gets an accommodation according to the specified code
     *
     * @param accommodationCode
     * @return the specified accommodation
     */
    AccommodationViewData getAccommodationByCode(String accommodationCode);

    AccommodationViewData getRoomsForAccommodation(String accomodationCode, int visibleItems);

    /**
     * Gets an accommodation location info data
     *
     * @param accommodationCode
     *           the specific accommodation code
     *
     * @return A standard accommodation view data with accommodation location info data populated in the
     *         featureCodesAndValues map.
     */
    AccommodationViewData getAccommodationLocationPanelInfo(String accommodationCode);

    /**
     * Gets a list of accommodation editorial data
     *
     * @param accommodationCode
     *           the specific accommodation code
     *
     * @return A standard accommodation view data with a list of accommodation editorial data in it.
     */
    AccommodationViewData getAccommodationEditorialInfo(String accommodationCode);

    /**
     * Gets a list of accommodation editorial data
     *
     * @param accommodationCode
     *           the specific accommodation code
     *
     * @return A standard accommodation view data with a list of accommodation editorial data in it.
     */
    AccommodationViewData getAccommodationEditorialInfo(String accommodationCode, String pageType);


    /**
     * Gets a list of accommodation benefit data
     *
     * @param accommodationCode
     *           the specific accommodation code
     *
     * @return A standard accommodation view data with a list of accommodation benefit data in it.
     */

    AccommodationViewData getBenefitsForAccomodation(String accommodationCode);

    /**
     * Gets a list of Facility items associated to the specified accommodation.
     *
     * @param accommodationCode
     *           the code of the accommodation
     * @return an {@link AccommodationViewData} populated with a list of {@link FacilityData}.
     */
    AccommodationViewData getAccommodationFacilities(String accommodationCode);

    /**
     * Gets a list of Facility highlight items associated to the specified accommodation.
     *
     * @param accommodationCode
     *           the code of the accommodation
     * @return an {@link AccommodationViewData} populated with a list of {@link FacilityData}.
     */
    AccommodationViewData getAccommodationFacilitiesHighlights(String accommodationCode);


    /**
     * @param accommodationCode
     * @return An a{@link AccommodationViewData} with the editorial info populated in the featureCodeAndValues map.
     */
    AccommodationViewData getRequiredAccommodationEditorialIntroduction(String accommodationCode);

    /**
     * @param accommodationCode
     * @return An a{@link AccommodationViewData} with the editorial info populated in the featureCodeAndValues map.
     */
    AccommodationViewData getRequiredComponentAccommodationEditorialIntroduction(String accommodationCode);

    /**
     *
     * @param accommodationCode
     * @return
     */
    AccommodationViewData getAccommodationDisclaimer(String accommodationCode);

    /**
     * Gets the accommodation health and safety editorial for an accommodation
     *
     * @param accommodationCode
     *           The accommodation code
     * @return An a{@link AccommodationViewData} with the health and safety editorial populated in the
     *         featureCodesAndValues map.
     */


    AccommodationViewData getHealthAndSafetyEditorial(String accommodationCode);

    /**
     * Gets the accommodation Passport and visas editorial for an accommodation
     *
     * @param accommodationCode
     *           The accommodation code
     * @return An a{@link AccommodationViewData} with the passport and visas editorial populated in the
     *         featureCodeAndValues map.
     */
    AccommodationViewData getPassportAndVisasEditorial(String accommodationCode);

    AccommodationViewData getAccommodationAllInclusive(String accommodationCode);

    /**
     * Gets the accommodation at glance data
     *
     * @param accommodationCode
     *           The accommodation code
     * @return An a{@link AccommodationViewData} with the passport and visas editorial populated in the
     *         featureCodeAndValues map.
     */
    AccommodationViewData getAccommodationAtAGlance(String accommodationCode);

    /**
     * Gets a list of All ProductRanges to which this accommodation belongs.
     *
     * @param accommodationCode
     *           the code of the accommodation
     * @return {@link AccommodationViewData} populated with a list of ProductRangeViewData.
     */
    AccommodationViewData getAccommodationProductRanges(String accommodationCode);

    /**
     * Gets the accommodation at entertainment data
     *
     * @param accommodationCode
     *           The accommodation code
     * @return An a{@link AccommodationViewData} with the entertainment data populated in the featureCodeAndValues map.
     */
    AccommodationViewData getAccommodationEntertainment(String accommodationCode);

    /**
     * Gets the getting around editorial data for an accommodation
     *
     * @param accommodationCode
     *           the code of the accommodation
     * @return An a{@link AccommodationViewData} with the passport and visas editorial populated in the
     *         featureCodesAndValues map.
     */
    AccommodationViewData getGettingAroundEditorial(String accommodationCode);

    /**
     * Gets an accommodation info data
     *
     * @param accommodationCode
     *           the specific accommodation code
     *
     * @return A standard accommodation view data with accommodation location info data, transfer time data, distance to
     *         airport data and its corresponding resort introduction text and thumbnail image.
     */
    AccommodationViewData getAccommodationLocationInfo(String accommodationCode);

    /**
     * Gets an accommodation when to go data
     *
     * @param accommodationCode
     *           the specific accommodation code
     *
     * @return An a{@link AccommodationViewData} with the whenToGo editorial populated in the featureCodesAndValues map.
     */
    AccommodationViewData getAccommodationWhenToGoEditorial(String accommodationCode);

    /**
     * Gets an accommodation thumbnail map data
     *
     * @param accommodationCode
     *           The accommodation code
     * @return An a{@link AccommodationViewData} with the thumbnail map data(longitude and latitude) populated in the
     *         featureCodesAndValues map.
     */
    AccommodationViewData getAccommodationThumbnailMapData(String accommodationCode);

    /**
     * Gets data for the accommodation name component
     *
     * @param accommodationCode
     *           The accommodation identifier code
     * @return An a{@link AccommodationViewData} with the accommodation name component data(accommodation name,
     *         Accommodation T-rating and its super-category product range type) populated in the featureCodesAndValues
     *         map.
     */
    AccommodationViewData getAccommodationNameComponentData(String accommodationCode);

    /**
     * Gets data for the accommodation key facts component
     *
     * @param accommodationCode
     *           The accommodation identifier code
     * @return An a{@link AccommodationViewData} with keyfacts data(language, currency, flight duration, transfer time,
     *         timezones) populated in the featureCodesAndValues map.
     */
    AccommodationViewData getAccommodationKeyFactsData(String accommodationCode);

    /**
     * retrieves articles associated with location
     *
     * @param accommodationCode
     *           The code for the location
     * @return The accommodation view data containing a list of articles
     */
    AccommodationViewData getArticlesForAccommodation(String accommodationCode);

    /**
     * Retrieves reviews about an accommodation from third party Trip advisor
     *
     * @param accommodationCode
     *           The code for the location
     * @param maxUserReview
     *           The maximum number of user reviews to return from trip advisor
     * @return The accommodation view data with summary reviews attached to it
     */
    AccommodationViewData getTripAdvisorSummaryData(String accommodationCode, Integer maxUserReview);

    /**
     * Retrieves highlights (USPs) associated with an accommodation
     *
     * @param accommodationCode
     *           The code for the accommodation
     * @param highlightsNumber
     *           Number of highlights to retrieves
     * @return The {@link AccommodationViewData} object with a map containing USPs features and their respective values
     */
    AccommodationViewData getAccommodationHighlights(String accommodationCode, Integer highlightsNumber);

    /**
     * retrieves accommodations for the search result and data is enriched
     *
     * @param list
     *           of search result data contains accommodation PK and other merchandised information The code for the
     *           location
     * @return The accommodation view data containing a list of articles
     */
    List<AccommodationViewData> getAccommodationDealsData(List<ResultData> list);

    /**
     * Retrieves reviews about an accommodation from third party Trip advisor
     *
     * @param accommodationCode
     *           The code for the location
     * @param maxUserReview
     *           The maximum number of user reviews to return from trip advisor
     * @return The accommodation view data with its user reviews attached to it
     */
    AccommodationViewData getTripAdvisorReviewData(String accommodationCode, Integer maxUserReview);

    /**
     * retrieves accommodations for the search result and data is enriched
     *
     * @param list
     *           of search result data contains accommodation PK and other merchandised information The code for the
     *           location
     * @return The accommodation view data containing a list of articles
     */
    List<AccommodationViewData> getAccommodationsWithEndecaData(List<ResultData> list);

    /**
     *
     * @param searchResultData
     * @return
     */
    AccommodationViewData getAccommodationPriceDataFromEndeca(SearchResultData<ResultData> searchResultData);

    /**
     * retrieves accommodations of a specific resort
     *
     * @param locationCode
     *           The code of the location to search the accommodations for
     * @param visibleItems
     * @return The map of accommTypeAccomViewData
     */
    Map<String, List<AccommodationViewData>> getTHAccommodationsByResort(String locationCode, int visibleItems);


    /**
     * retrieves accommodations of a specific location - could be destination or region as well not specific to resort
     *
     * @param locationCode
     *           The code of the location to search the accommodations for
     * @param visibleItems
     * @return The map of accommTypeAccomViewData
     */
    Map<String, List<AccommodationViewData>> getTHAccommodations(String locationCode, int visibleItems,
            List<AccommodationOption> options);

    /**
     * Gets an accommodation Award data
     *
     * @param accommodationCode
     *           the specific accommodation code
     *
     * @return SustainableTourismComponentViewData
     */

    SustainableTourismComponentViewData getAccommodationTravelLifeAwardInfo(String accommodationCode);

    List<AccommodationViewData> getAdvertisedPackagesCollectionData(
            AdvertisedPackagesCollectionComponentModel advertisedPackagesCollectionComponent);

    /**
     * retrieves accommodations of a specific location ByAccomType - could be destination or region as well not specific
     * to resort
     *
     * @param locationCode
     *           The code of the location to search the accommodations for
     * @param visibleItems
     * @param accommodationTypeContext
     * @returnThe accommodation view data containing a list of articles
     */

    List<AccommodationViewData> getAccommodationsByAccomType(String locationCode, int visibleItems, String accommodationTypeContext);


    /**
     * retrieves accommodations of a specific resort
     *
     * @param locationCode
     *           The code of the location to search the accommodations for
     * @param visibleItems
     * @return The list of accommodations
     */
    List<AccommodationViewData> getAccommodationsByResort(String locationCode, int visibleItems);

    /**
     * retrieves accommodations of a specific location - could be destination or region as well not specific to resort
     *
     * @param locationCode
     *           The code of the location to search the accommodations for
     * @param visibleItems
     * @return The list of accommodations
     */
    List<AccommodationViewData> getAccommodations(String locationCode, int visibleItems);


    /**
     * retrieves villa details
     *
     * @param accomCode
     * @return AccommodationViewData
     */
    AccommodationViewData getAccomWithClassifiedFacilityList(String accomCode);

    /**
     *
     * @param accommodationCode
     * @return AccommodationViewData
     * @description This Method Retrieve All The Facilities Available For An Accommodation.
     */
    AccommodationViewData getUnPrioritizedAccomFacilities(String accommodationCode);

    /**
     * @param accommodationCode
     * @return
     */
    ItineraryViewData getItineraryData(String accommodationCode);

    /**
     * @param accommodationCode
     * @return
     */
    TwoCentreSelectorViewData getTwoCentreSelectorData(String accommodationCode);

    /**
     *
     * @param accommodationCode
     * @return
     */
    EssentialInformationViewData getEssentialInformationData(String accommodationCode, boolean multiStay);

    /**
     * @param location
     *           YTODO
     * @return
     */
    List<AccommodationViewData> getAllAccommodationsByLocationFDCodeAndAccomType(LocationModel location, String noncoreHolidayType);

    /**
     * retrieves special offer data
     *
     * @param accomCode
     * @return AccommodationViewData
     */

    AccommodationViewData getAccommodationSpecialOffer(String accommodationCode);

    /**
     * @param list
     * @return
     */
    List<AccommodationViewData> getAccommodationsWithDetailData(List<ResultData> list);

    /**
     * @param list
     * @param multiCentre
     * @return
     */
    List<AccommodationViewData> getAccommodationsWithLocationFromEndecaData(List<ResultData> list, boolean multiCentre);

    /**
     * @param accommodationCode
     * @return
     */
    boolean isNonBookableUnit(String accommodationCode);

    String updateViewAllUrlForMultiCentre(String multiCentreCode, String tabName, String tabCount);


    /**
     * @param productCode
     * @return
     */
    ItineraryViewData getItineraryDataForAllinone(String productCode);

    /**
     * @param locationCode
     * @return
     */
    ItineraryViewData getItineraryDataForLaplandResorts(String locationCode);

    /**
     * @param priceForRecSearch
     * @param productCodes
     * @return
     */
    List<AccommodationViewData> getRecommendedAccomPriceInfo(SearchResultData<ResultData> priceForRecSearch,
            List<String> productCodes);

    /**
     * @param accommodationCode
     * @return
     */
    AccommodationViewData getAccommodationEditorialIntroduction(String accommodationCode);
}
