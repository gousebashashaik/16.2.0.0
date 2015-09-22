/**
 *
 */
package uk.co.portaltech.tui.web.url.builders;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.tui.utils.ConfigurationUtils;
import uk.co.portaltech.tui.utils.PackageIdGenerator;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsLocationData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author ext
 *
 */
public class HolidayOptionsRequestBuilder implements RequestBuilder {

    @Resource(name = "configurationService")
    private ConfigurationService configService;

    @Resource
    private ConfigurationUtils configurationUtils;

    @Resource
    private SessionService sessionService;

    private String iscapeServerUrl = StringUtils.EMPTY;

    private static final String EQUAL = "=";
    private static final String AMP = "&";

    private String roomCodes = StringUtils.EMPTY;
    private static final String HYBRIS_FLIGHT_OPTIONS_PAGE = "/destinations/book/flightoptions?JSESSIONID_TH=";

    private static final String SMARTPHONE = "Smartphone";
    private static final String JSESSIONID = "httpSessionId";

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.builder.RequestBuilder#build(java.lang.Object[])
     */
    @Override
    public String builder(
            final BookFlowAccommodationViewData bookFlowAccomViewData,
            final SearchPanelComponentModel searchPanelComponentModel,
            final SearchResultsRequestData requestData) {

        return buildIscapeHolidayOptionsUrl(
                bookFlowAccomViewData.getPackageData(),
                searchPanelComponentModel, requestData);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
     * .tui.web.view.data.SearchResultViewData,
     * uk.co.portaltech.travel.model.SearchPanelComponentModel,
     * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
     */
    @Override
    public void builder(final SearchResultViewData resultsview,
            final SearchPanelComponentModel searchPanelComponent,
            final SearchResultsRequestData searchParameter) {
        resultsview.getAccommodation().setUrl(
                buildIscapeHolidayOptionsUrl(resultsview, searchPanelComponent,
                        searchParameter));
    }

    /**
     * @param resultsview
     * @param searchPanelComponent
     * @param searchParameter
     * @return URL
     */
    private String buildIscapeHolidayOptionsUrl(
            final SearchResultViewData resultsview,
            final SearchPanelComponentModel searchPanelComponent,
            final SearchResultsRequestData searchParameter) {

        iscapeServerUrl = configService
                .getConfiguration()
                .getString("iscape.holiday.options.url",
                        "http://www.thomson.co.uk/thomson/page/byo/booking/traveloptions.page?");
        final StringBuilder requestUrl = new StringBuilder(iscapeServerUrl);
        requestUrl
                .append("packageNumber")
                .append(EQUAL)
                .append(PackageIdGenerator
                        .generateIscapeTHPackageId(resultsview))
                .append(populateGeoHierarchicalData(resultsview
                        .getAccommodation()))
                .append(setPartyComposition(resultsview.getAccommodation()
                        .getRooms(), searchPanelComponent))
                .append(AMP)
                .append("maxPartySize")
                .append(EQUAL)
                .append(searchPanelComponent.getMaxPaxConfig())
                .append(AMP)
                .append("moreDestinations")
                .append(EQUAL)
                .append(Boolean.FALSE)
                .append(AMP)
                .append("moreResorts")
                .append(EQUAL)
                .append(Boolean.FALSE)
                // requestData.getDepartureDate() 28-09-2013
                .append(AMP)
                .append("departureDate")
                .append(EQUAL)
                .append(DateUtils.formatdate(resultsview.getItinerary()
                        .getDepartureDate()))
                .append(AMP)
                .append("departureMargin")
                .append(EQUAL)
                .append(searchParameter.getFlexibleDays())
                .append(AMP)
                .append("duration")
                .append(EQUAL)
                .append(resultsview.getDuration())
                .append(AMP)
                .append("dayTripSearch")
                .append(EQUAL)
                .append(Boolean.FALSE)
                .append(AMP)
                .append("departureAirportCode")
                .append(EQUAL)
                .append(resultsview.getItinerary().getOutbounds().get(0)
                        .getDepartureAirportCode())
                .append(AMP)
                .append("accommodationType")
                .append(EQUAL)
                .append(setAccommodationType(resultsview.getAccommodation()))
                .append(AMP)
                .append("rating")
                .append(EQUAL)
                .append(setRating(resultsview.getAccommodation().getRatings()
                        .getOfficialRating()))
                .append(AMP)
                .append("numberOfRooms")
                .append(EQUAL)
                .append(resultsview.getAccommodation().getRooms().size())
                .append(AMP)
                .append("tracsPkgId")
                .append(EQUAL)
                .append(resultsview.getTracsPackageId())
                .append(AMP)
                .append("sellingCode")
                .append(EQUAL)
                .append(resultsview.getSellingCode())
                .append(AMP)
                .append("roomCodes")
                .append(EQUAL)
                .append(roomCodes)
                .append(AMP)
                .append("boardBasisCode")
                .append(EQUAL)
                .append(setBoardBasisCode(resultsview.getAlternateBoard(),
                        searchParameter.getSelectedBoardBasis())).append(AMP)
                .append("pid").append(EQUAL).append(resultsview.getPackageId())
                .append(AMP).append("requestFrom").append(EQUAL)
                .append("Phoenix");

        return requestUrl.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.builder.RequestBuilder#build(java.lang.Object[])
     */

    @Override
    public String builder(
            final BookFlowAccommodationViewData bookFlowAccomViewData,
            final HolidayFinderComponentModel holidayFinderComponent,
            final SearchResultsRequestData requestData) {

        return buildIscapeHolidayOptionsUrl(
                bookFlowAccomViewData.getPackageData(), holidayFinderComponent,
                requestData);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
     * .tui.web.view.data.SearchResultViewData,
     * uk.co.portaltech.travel.model.SearchPanelComponentModel,
     * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
     */

    @Override
    public void builder(final SearchResultViewData resultsview,
            final HolidayFinderComponentModel holidayFinderComponent,
            final SearchResultsRequestData searchParameter) {
        resultsview.getAccommodation().setUrl(
                buildIscapeHolidayOptionsUrl(resultsview,
                        holidayFinderComponent, searchParameter));
    }

    /**
     * @param resultsview
     * @param holidayFinderComponent
     * @param searchParameter
     * @return URL
     */
    private String buildIscapeHolidayOptionsUrl(
            final SearchResultViewData resultsview,
            final HolidayFinderComponentModel holidayFinderComponent,
            final SearchResultsRequestData searchParameter) {
        final String sessionId = sessionService.getAttribute(JSESSIONID);
        if ((StringUtils.equalsIgnoreCase(
                configurationUtils.getMoovwebSwitchTH(), "on"))
                && (StringUtils.equalsIgnoreCase(
                        configService.getConfiguration().getString(
                                "filterThirdPartyFlights.mobile.th", "true"),
                        "true"))
                && !(isThirdPartyFlight(resultsview))
                && (sessionService.getAttribute(SMARTPHONE) != null)) {
            iscapeServerUrl = configService
                    .getConfiguration()
                    .getString("iscape.holiday.options.moovweb.url",
                            "http://m.thomson.co.uk/thomson/page/byo/booking/traveloptions.page?");
        } else {
            return HYBRIS_FLIGHT_OPTIONS_PAGE + sessionId;
        }
        final StringBuilder requestUrl = new StringBuilder(iscapeServerUrl);
        requestUrl
                .append("packageNumber")
                .append(EQUAL)
                .append(PackageIdGenerator
                        .generateIscapeTHPackageId(resultsview))
                .append(populateGeoHierarchicalData(resultsview
                        .getAccommodation()))
                .append(setPartyComposition(resultsview.getAccommodation()
                        .getRooms(), holidayFinderComponent))
                .append(AMP)
                .append("maxPartySize")
                .append(EQUAL)
                .append(holidayFinderComponent.getMaxPaxConfig())
                .append(AMP)
                .append("moreDestinations")
                .append(EQUAL)
                .append(Boolean.FALSE)
                .append(AMP)
                .append("moreResorts")
                .append(EQUAL)
                .append(Boolean.FALSE)
                // requestData.getDepartureDate() 28-09-2013
                .append(AMP)
                .append("departureDate")
                .append(EQUAL)
                .append(DateUtils.formatdate(resultsview.getItinerary()
                        .getDepartureDate()))
                .append(AMP)
                .append("departureMargin")
                .append(EQUAL)
                .append(searchParameter.getFlexibleDays())
                .append(AMP)
                .append("duration")
                .append(EQUAL)
                .append(resultsview.getDuration())
                .append(AMP)
                .append("dayTripSearch")
                .append(EQUAL)
                .append(Boolean.FALSE)
                .append(AMP)
                .append("departureAirportCode")
                .append(EQUAL)
                .append(resultsview.getItinerary().getOutbounds().get(0)
                        .getDepartureAirportCode())
                .append(AMP)
                .append("accommodationType")
                .append(EQUAL)
                .append(setAccommodationType(resultsview.getAccommodation()))
                .append(AMP)
                .append("rating")
                .append(EQUAL)
                .append(setRating(resultsview.getAccommodation().getRatings()
                        .getOfficialRating()))
                .append(AMP)
                .append("numberOfRooms")
                .append(EQUAL)
                .append(resultsview.getAccommodation().getRooms().size())
                .append(AMP)
                .append("tracsPkgId")
                .append(EQUAL)
                .append(resultsview.getTracsPackageId())
                .append(AMP)
                .append("sellingCode")
                .append(EQUAL)
                .append(resultsview.getSellingCode())
                .append(AMP)
                .append("roomCodes")
                .append(EQUAL)
                .append(roomCodes)
                .append(AMP)
                .append("boardBasisCode")
                .append(EQUAL)
                .append(setBoardBasisCode(resultsview.getAlternateBoard(),
                        searchParameter.getSelectedBoardBasis())).append(AMP)
                .append("pid").append(EQUAL).append(resultsview.getPackageId())
                .append(AMP).append("requestFrom").append(EQUAL)
                .append("Phoenix");

        return requestUrl.toString();
    }

    private String setBoardBasisCode(final List<BoardBasisType> boardBasisList,
            final String selectedBoardBasis) {
        if (StringUtils.isNotBlank(selectedBoardBasis)) {
            return selectedBoardBasis;
        }
        for (final BoardBasisType boardBasis : boardBasisList) {
            if (boardBasis.isDefaultBoardBasis()) {
                return boardBasis.getBoardbasisCode();
            }
        }

        return StringUtils.EMPTY;
    }

    private String setAccommodationType(
            final SearchResultAccomodationViewData accommodation) {
        if (StringUtils.isNotBlank(accommodation.getAccomType())) {
            return new StringBuilder().append(accommodation.getAccomType())
                    .toString();
        } else {
            return String.valueOf(0);
        }

    }

    private String setRating(final String rating) {
        if (StringUtils.isNotBlank(rating)) {
            return new StringBuilder().append(rating).toString();
        } else {
            return String.valueOf(0);
        }
    }

    private String populateGeoHierarchicalData(
            final SearchResultAccomodationViewData accommodation) {
        final SearchResultsLocationData location = accommodation.getLocation();
        final StringBuilder geoHierarchy = new StringBuilder();
        if (StringUtils.isNotBlank(location.getCountry().getCode())) {
            geoHierarchy.append(AMP).append("tuiCtryCode").append(EQUAL)
                    .append(location.getCountry().getCode());
        }
        if (StringUtils.isNotBlank(location.getResort().getCode())) {
            geoHierarchy.append(AMP).append("tuiResortCode").append(EQUAL)
                    .append(location.getResort().getCode());
        }
        if (StringUtils.isNotBlank(accommodation.getCode())) {
            geoHierarchy.append(AMP).append("tuiAccomCode").append(EQUAL)
                    .append(accommodation.getCode()).toString();
        }
        return geoHierarchy.toString();
    }

    private String setPartyComposition(final List<SearchResultRoomsData> rooms,
            final SearchPanelComponentModel searchPanelComponentModel) {
        // room codes list
        final List<String> roomCodeList = new ArrayList<String>();

        final StringBuilder numAdults = new StringBuilder();
        final StringBuilder numChildren = new StringBuilder();
        final StringBuilder childInfantAges = new StringBuilder();
        for (final SearchResultRoomsData room : rooms) {
            int totalAdults = 0;
            int totalChildren = 0;
            int totalInfants = 0;

            // room code population
            roomCodeList.add(room.getRoomCode());
            // end

            totalAdults += room.getOccupancy().getAdults();
            totalChildren += room.getOccupancy().getChildren();
            totalInfants += room.getOccupancy().getInfant();

            if (totalChildren > 0 || totalInfants > 0) {
                final StringBuilder childAges = new StringBuilder();
                for (final PaxDetail pax : room.getOccupancy().getPaxDetail()) {
                    if (pax.getAge() <= searchPanelComponentModel
                            .getMaxChildAge().intValue()) {
                        if (pax.getAge() < searchPanelComponentModel
                                .getInfantAge().intValue()) {
                            childAges.append("%3C2").append("-");
                        } else {
                            childAges.append(pax.getAge()).append("-");
                        }
                    }
                }
                childInfantAges.append(childAges).append("|");
            }
            numAdults.append(totalAdults).append("|");
            numChildren.append(totalChildren + totalInfants).append("|");
        }
        StringUtils.removeEnd(numAdults.toString(), "|");
        StringUtils.removeEnd(numChildren.toString(), "|");
        StringUtils.removeEnd(childInfantAges.toString(), "-|");

        final StringBuilder partyComposition = new StringBuilder();
        partyComposition.append(AMP).append("numAdults").append(EQUAL)
                .append(numAdults).append(AMP).append("numChildren")
                .append(EQUAL).append(numChildren).append(AMP)
                .append("childInfantAges").append(EQUAL)
                .append(childInfantAges);

        // room codes
        roomCodes = StringUtils.join(roomCodeList, ",");

        return partyComposition.toString();

    }

    private String setPartyComposition(final List<SearchResultRoomsData> rooms,
            final HolidayFinderComponentModel holidayFinderComponent) {
        // room codes list
        final List<String> roomCodeList = new ArrayList<String>();

        final StringBuilder numAdults = new StringBuilder();
        final StringBuilder numChildren = new StringBuilder();
        final StringBuilder childInfantAges = new StringBuilder();
        for (final SearchResultRoomsData room : rooms) {
            int totalAdults = 0;
            int totalChildren = 0;
            int totalInfants = 0;

            // room code population
            roomCodeList.add(room.getRoomCode());
            // end

            totalAdults += room.getOccupancy().getAdults();
            totalChildren += room.getOccupancy().getChildren();
            totalInfants += room.getOccupancy().getInfant();

            if (totalChildren > 0 || totalInfants > 0) {
                final StringBuilder childAges = new StringBuilder();
                for (final PaxDetail pax : room.getOccupancy().getPaxDetail()) {
                    if (pax.getAge() <= holidayFinderComponent.getMaxChildAge()
                            .intValue()) {
                        if (pax.getAge() < holidayFinderComponent
                                .getInfantAge().intValue()) {
                            childAges.append("%3C2").append("-");
                        } else {
                            childAges.append(pax.getAge()).append("-");
                        }
                    }
                }
                childInfantAges.append(childAges).append("|");
            }
            numAdults.append(totalAdults).append("|");
            numChildren.append(totalChildren + totalInfants).append("|");
        }
        StringUtils.removeEnd(numAdults.toString(), "|");
        StringUtils.removeEnd(numChildren.toString(), "|");
        StringUtils.removeEnd(childInfantAges.toString(), "-|");

        final StringBuilder partyComposition = new StringBuilder();
        partyComposition.append(AMP).append("numAdults").append(EQUAL)
                .append(numAdults).append(AMP).append("numChildren")
                .append(EQUAL).append(numChildren).append(AMP)
                .append("childInfantAges").append(EQUAL)
                .append(childInfantAges);

        // room codes
        roomCodes = StringUtils.join(roomCodeList, ",");

        return partyComposition.toString();

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
     * .tui.web.view.data.BookFlowAccommodationViewData,
     * uk.co.portaltech.travel.model.SearchPanelComponentModel)
     */
    @Override
    public String builder(final BookFlowAccommodationViewData source,
            final SearchPanelComponentModel searchPanelComponentModel) {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
     * .tui.web.view.data.BookFlowAccommodationViewData,
     * uk.co.portaltech.travel.model.HolidayFinderComponentModel)
     */
    @Override
    public String builder(final BookFlowAccommodationViewData source,
            final HolidayFinderComponentModel holidayFinderComponent) {

        return null;
    }

    private boolean isThirdPartyFlight(final SearchResultViewData resultsview) {
        return (resultsview.getItinerary().getOutbounds().get(0)
                .getExternalInventory().booleanValue())
                || (resultsview.getItinerary().getInbounds().get(0)
                        .getExternalInventory().booleanValue());

    }

}
