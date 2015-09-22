/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;


/**
 * @author sunilkumar.murthy
 *
 */

public class FlightOptionsAnalyticsPopulator implements Populator<Object, Map<String, WebAnalytics>>
{


    @Resource
    private AnalyticsHelper analyticsHelper;

    private static final String WHERE_TO = "WhereTo";
    private static final String DEP_AIR = "DepAir";
    private static final String DEP_DATE = "DepDate";
    private static final String MONTH_YEAR = "MonthYear";
    private static final String PARTY = "Party";
    private static final String DUR = "Dur";
    private static final String SUM = "Sum";
    private static final String DISC = "Disc";
    private static final String LIM_AV_B = "LimAvB";
    private static final String BOARD = "Board";
    private static final String LIM_AV_S = "LimAvS";
    private static final String DREAM_FLIGHT = "DreamFl";
    private static final String SUB_PROD = "SubProd";
    private static final String TUI_ANC = "TuiAnc";
    private static final String NULL = "null";

    @Resource
    private DroolsPriorityProviderService droolsPriorityProviderService;


    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final Object source, final Map<String, WebAnalytics> analyticMap) throws ConversionException
    {

        if (source instanceof BookFlowAccommodationViewData)
        {
            final BookFlowAccommodationViewData bookFlowAccommodationViewData = (BookFlowAccommodationViewData) source;
            final SearchResultViewData requestData = bookFlowAccommodationViewData.getPackageData();
            populateWhereTo(analyticMap, requestData);
            populateAirport(analyticMap, requestData);
            populateDepartureDate(analyticMap, requestData);
            populateSubProductCode(analyticMap, requestData);
            populateDuration(analyticMap, requestData);
            populateTuiAncAnalyticData(analyticMap, requestData);
            populateLimitedAvailabilityIndicator(analyticMap, requestData);
            populatePartyComposition(analyticMap, requestData);
            populateDreamlinerIndicator(analyticMap, requestData);
            populateprice(analyticMap, requestData);
            populateBoard(analyticMap, requestData);

        }

    }

    /**
     * Duration population for Analytics
     *
     * @param analyticMap
     * @param packageData
     */
    private void populateBoard(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {

        if (packageData.getAlternateBoard() != null)
        {
            final List<BoardBasisType> boardBasisData = packageData.getAlternateBoard();
            for (final BoardBasisType boardBasisType : boardBasisData)
            {
                if (boardBasisType.getBoardbasisCode() != null && boardBasisType.isDefaultBoardBasis())
                {
                    final WebAnalytics webAnalyticDur = new WebAnalytics(BOARD, String.valueOf(boardBasisType.getBoardbasisCode()));
                    analyticMap.put(BOARD, webAnalyticDur);
                }
            }
        }
        else
        {
            final WebAnalytics webAnalyticDur = new WebAnalytics(BOARD, NULL);
            analyticMap.put(BOARD, webAnalyticDur);
        }

    }



    /**
     * @param analyticMap
     */
    private void populateSubProductCode(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {
        if (packageData.getSubProductCode() != null)
        {
            final WebAnalytics webAnalyticDepAir = new WebAnalytics(SUB_PROD, packageData.getSubProductCode());
            analyticMap.put(SUB_PROD, webAnalyticDepAir);
        }
    }

    /**
     * Method To Add The WhereTo Parameter To The Analytics populateWhereTo() Map<String, WebAnalytics>
     * analyticMap,SearchResultViewData packageData
     *
     */
    public void populateWhereTo(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {
        if (StringUtils.isNotEmpty(packageData.getAccommodation().getCode()))
        {
            final WebAnalytics webAnalyticDepAir = new WebAnalytics(WHERE_TO, "H" + packageData.getAccommodation().getCode());
            analyticMap.put(WHERE_TO, webAnalyticDepAir);
        }
        else
        {
            final WebAnalytics webAnalyticDepAir = new WebAnalytics(WHERE_TO, NULL);
            analyticMap.put(WHERE_TO, webAnalyticDepAir);
        }

    }


    /**
     * Method To Add The WhereTo Parameter To The Analytics populateWhereTo() Map<String, WebAnalytics>
     * analyticMap,SearchResultViewData packageData
     *
     */
    public void populateAirport(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {
        if (CollectionUtils.isNotEmpty(packageData.getItinerary().getOutbounds()))
        {
            final WebAnalytics webAnalyticDepAir = new WebAnalytics(DEP_AIR,
                    (packageData.getItinerary().getOutbounds().get(0)).getDepartureAirportCode());
            analyticMap.put(DEP_AIR, webAnalyticDepAir);
        }

    }



    /**
     * Method To Add The Duration Parameter To The Analytics populateDuration() Map<String, WebAnalytics>
     * analyticMap,SearchResultViewData packageData
     *
     */
    private void populateDuration(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {
        if (packageData.getDuration() != 0)
        {
            final WebAnalytics webAnalyticDur = new WebAnalytics(DUR, String.valueOf(packageData.getDuration()));
            analyticMap.put(DUR, webAnalyticDur);
        }
        else
        {
            final WebAnalytics webAnalyticDur = new WebAnalytics(DUR, String.valueOf(0));
            analyticMap.put(DUR, webAnalyticDur);
        }

    }



    /**
     * Method To Add The Depair,MonthYear Parameters To The Analytics populateDepartureDate() Map<String, WebAnalytics>
     * analyticMap,SearchResultViewData packageData
     *
     */
    public void populateDepartureDate(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {

        if (StringUtils.isNotEmpty(packageData.getItinerary().getDepartureDate()))
        {
            final LocalDate departureDate = DateUtils.toDate(packageData.getItinerary().getDepartureDate());
            analyticsHelper.addFormattedDate(analyticMap, departureDate);
            analyticsHelper.addMonthYear(analyticMap, departureDate);
        }
        else
        {
            final WebAnalytics webAnalyticDepDate = new WebAnalytics(DEP_DATE, NULL);
            analyticMap.put(DEP_DATE, webAnalyticDepDate);

            final WebAnalytics webAnalyticMonthYear = new WebAnalytics(MONTH_YEAR, NULL);
            analyticMap.put(MONTH_YEAR, webAnalyticMonthYear);
        }

    }


    /**
     * Method To Add The TuiAnc Ancelliraies Parameters To The Analytics populateTuiAncAnalyticData() Map<String,
     * WebAnalytics> analyticMap,SearchResultViewData packageData
     *
     */
    public void populateTuiAncAnalyticData(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {
        if (packageData.isWorldCare())
        {
            final SearchResultPriceViewData searchResultPriceViewData = packageData.getPrice();
            if (StringUtils.isNotEmpty(searchResultPriceViewData.getTotalParty()))
            {
                analyticsHelper.getPriceRoundedValue(searchResultPriceViewData.getTotalParty());
            }

            int count = 0;
            double worldcarefund = 0;
            for (final SearchResultRoomsData room : packageData.getAccommodation().getRooms())
            {
                count += room.getOccupancy().getAdults() + room.getOccupancy().getChildren();
                final WorldCareFundRuleOutputData worldCare = droolsPriorityProviderService.getWorldCareFundCharges();

                worldcarefund += (room.getOccupancy().getAdults() * Double.parseDouble(worldCare.getAdultCharge()))
                        + (room.getOccupancy().getChildren() * Double.parseDouble(worldCare.getChildCharge()));
            }

            String worldCareAnalyticsData = StringUtils.EMPTY;
            if (packageData.isCoachTransfer())
            {
                worldCareAnalyticsData = "TFJ|" + count + "|" + 0 + "-";
            }
            worldCareAnalyticsData = worldCareAnalyticsData.concat("WCF|" + count + "|" + Math.round(worldcarefund));
            analyticMap.put(TUI_ANC, new WebAnalytics(TUI_ANC, worldCareAnalyticsData));

        }
        else
        {
            analyticMap.put(TUI_ANC, new WebAnalytics(TUI_ANC, NULL));

        }
    }




    /**
     * Method To Add The Limited Availability Boards Parameters To The Analytics populateLimitedAvailabilityIndicator()
     * Map<String, WebAnalytics> analyticMap,SearchResultViewData packageData
     *
     */
    private void populateLimitedAvailabilityIndicator(final Map<String, WebAnalytics> analyticMap,
            final SearchResultViewData packageData)
    {
        if (CollectionUtils.isNotEmpty(packageData.getAccommodation().getRooms()))
        {
            final SearchResultRoomsData searchResultRoomsData = packageData.getAccommodation().getRooms().get(0);
            if (searchResultRoomsData.isAvailability())
            {
                final WebAnalytics webAnalyticLimAvB = new WebAnalytics(LIM_AV_B, String.valueOf(searchResultRoomsData
                        .getSellingout()));
                analyticMap.put(LIM_AV_B, webAnalyticLimAvB);
                final WebAnalytics webAnalyticLimAvS = new WebAnalytics(LIM_AV_S, String.valueOf(searchResultRoomsData
                        .getSellingout()));
                analyticMap.put(LIM_AV_S, webAnalyticLimAvS);

            }
            else
            {
                final WebAnalytics webAnalyticLimAvB = new WebAnalytics(LIM_AV_B, NULL);
                analyticMap.put(LIM_AV_B, webAnalyticLimAvB);
                final WebAnalytics webAnalyticLimAvS = new WebAnalytics(LIM_AV_S, NULL);
                analyticMap.put(LIM_AV_S, webAnalyticLimAvS);
            }

        }

    }





    /**
     * Party Composition for rooms population for Analytics
     *
     * @param analyticMap
     * @param packageData
     *           Party=R1-A2/C0/I0,R2-A2/C1/I0
     */
    private void populatePartyComposition(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {

        int count = 0;
        String party = "";
        final StringBuilder partyComp = new StringBuilder();
        for (final SearchResultRoomsData room : packageData.getAccommodation().getRooms())
        {
            count++;
            partyComp.append("R");
            partyComp.append(count);
            partyComp.append("-");
            partyComp.append("A");
            partyComp.append(room.getOccupancy().getAdults());
            partyComp.append("/");
            partyComp.append("C");
            partyComp.append(room.getOccupancy().getChildren());
            partyComp.append("/");
            partyComp.append("I");
            partyComp.append(room.getOccupancy().getInfant());
            partyComp.append(",");
        }
        if (StringUtils.isNotEmpty(partyComp.toString()))
        {
            party = partyComp.toString();
            party = StringUtils.substring(party, 0, party.length() - 1);
        }
        final WebAnalytics webAnalyticDur = new WebAnalytics(PARTY, party);
        analyticMap.put(PARTY, webAnalyticDur);

    }

    /**
     * Function to Populate Dremaliner data for Analytics
     *
     * @param packageData
     */
    public void populateDreamlinerIndicator(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData packageData)
    {

        final List<SearchResultFlightDetailViewData> outbounds = packageData.getItinerary().getInbounds();
        final List<SearchResultFlightDetailViewData> inbounds = packageData.getItinerary().getOutbounds();
        String dreamlinerIndicator = "No";
        if (analyticsHelper.isdreamliner(outbounds) || analyticsHelper.isdreamliner(inbounds))
        {
            dreamlinerIndicator = "Yes";
        }
        analyticMap.put(DREAM_FLIGHT, new WebAnalytics(DREAM_FLIGHT, dreamlinerIndicator));

    }


    /**
     * Population of Price Data for Analytics
     *
     * @param analyticMap
     * @param resultViewData
     */
    public void populateprice(final Map<String, WebAnalytics> analyticMap, final SearchResultViewData resultViewData)
    {

        int sum = 0;
        int discount = 0;

        final SearchResultPriceViewData searchResultPriceViewData = resultViewData.getPrice();
        if (StringUtils.isNotEmpty(searchResultPriceViewData.getTotalParty()))
        {
            sum = analyticsHelper.getPriceRoundedValue(searchResultPriceViewData.getTotalParty());
        }
        if (StringUtils.isNotEmpty(searchResultPriceViewData.getDiscount()))
        {
            discount = analyticsHelper.getPriceRoundedValue(searchResultPriceViewData.getDiscount());
        }
        analyticMap.put(SUM, new WebAnalytics(SUM, String.valueOf(sum)));
        analyticMap.put(DISC, new WebAnalytics(DISC, String.valueOf(discount)));
    }

}
