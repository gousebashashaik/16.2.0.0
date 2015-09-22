/**
 *
 */
package uk.co.portaltech.tui.helper;

import de.hybris.platform.product.ProductService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.thirdparty.endeca.BackingObject;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.MonthYearViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.VillaAvailabilitySearchRequestData;
import uk.co.portaltech.tui.web.view.data.VillaAvailabilityViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.AccomInfoViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.SearchParams;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityRequest;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityResponse;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityViewDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaEndecaInfo;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author
 *
 */
public class VillaAvailabilityHelper
{

    @Resource
    private ProductService productService;

    @Resource
    private AirportService airportService;

    //This should be read from Search panel.
    private static final int SEASON_LENGTH = 18;


    private static final TUILogUtils LOG = new TUILogUtils("VillaAvailabilityHelper");
    public static final Map<String, Integer> DAYS_OF_THE_WEEK = new HashMap<String, Integer>();
    static
    {
        DAYS_OF_THE_WEEK.put("Mon", Integer.valueOf(CommonwebitemsConstants.ONE));
        DAYS_OF_THE_WEEK.put("Tue", Integer.valueOf(CommonwebitemsConstants.TWO));
        DAYS_OF_THE_WEEK.put("Wed", Integer.valueOf(CommonwebitemsConstants.THREE));
        DAYS_OF_THE_WEEK.put("Thu", Integer.valueOf(CommonwebitemsConstants.FOUR));
        DAYS_OF_THE_WEEK.put("Fri", Integer.valueOf(CommonwebitemsConstants.FIVE));
        DAYS_OF_THE_WEEK.put("Sat", Integer.valueOf(CommonwebitemsConstants.SIX));
        DAYS_OF_THE_WEEK.put("Sun", Integer.valueOf(CommonwebitemsConstants.SEVEN));
    }

    /**
     * @param villaAvailabilityRequest
     * @param searchRequest
     * @param villaAvailability
     *
     * @return villaAvailabilityResponse
     *
     */
    public VillaAvailabilityResponse createResponse(final VillaAvailabilityRequest villaAvailabilityRequest,
            final VillaAvailabilitySearchRequestData searchRequest, final SearchResultData<ResultData> villaAvailability)
    {
        final VillaAvailabilityResponse villaAvailabilityResponse = new VillaAvailabilityResponse();
        final Map<String, BackingObject> availabilityMap = new HashMap<String, BackingObject>();
        final DateTime firstAvailableDate = findFirstAvailableDate(villaAvailability, availabilityMap, villaAvailabilityRequest);
        createVillaAvailabilityView(villaAvailabilityRequest, searchRequest, availabilityMap, villaAvailabilityResponse,
                firstAvailableDate);
        return villaAvailabilityResponse;
    }

    /**
     * @param villaAvailabilityRequest
     * @param searchRequest
     * @param availabilityMap
     * @param villaAvailabilityResponse
     * @param firstAvailableDate
     */
    private void createVillaAvailabilityView(final VillaAvailabilityRequest villaAvailabilityRequest,
            final VillaAvailabilitySearchRequestData searchRequest, final Map<String, BackingObject> availabilityMap,
            final VillaAvailabilityResponse villaAvailabilityResponse, final DateTime firstAvailableDate)
    {
        final AccommodationModel accommodationModel = (AccommodationModel) productService
                .getProductForCode(villaAvailabilityRequest.getCode());
        final SearchParams searchParams = new SearchParams();
        searchParams.setDuration(searchRequest.getStay());
        final AccomInfoViewData accomInfo = new AccomInfoViewData();
        accomInfo.setName(accommodationModel.getName());
        accomInfo.setCode(villaAvailabilityRequest.getCode());
        accomInfo.setType(accommodationModel.getItemtype());
        final List<AccomInfoViewData> accomInfos = new ArrayList<AccomInfoViewData>();
        accomInfos.add(accomInfo);
        searchParams.setUnits(accomInfos);
        searchParams.setNoOfAdults(searchRequest.getAdults());
        searchParams.setNoOfChildren(searchRequest.getChildren());

        //Major Assumption: View will cater to only displaying 7 set of data maximum spanned across 3 months.
        // So adding/subtracting 7 to the firstAvailable date all the time to get a maximum of 7 available/non available entries
        constructVillaAvailabilityView(villaAvailabilityRequest, searchRequest, availabilityMap, villaAvailabilityResponse,
                firstAvailableDate, searchParams);

        //sorting the data based on Departure date in backward traversal.
        if (scrollingBack(villaAvailabilityRequest.getDirection()))
        {
            for (final VillaAvailabilityViewDataWrapper viewData : villaAvailabilityResponse.getVillaAvailabilityData())
            {
                //Sorting each group specific data..
                Collections.sort(viewData.getDays(), new Comparator<VillaAvailabilityViewData>()
                {
                    @Override
                    public int compare(final VillaAvailabilityViewData arg0, final VillaAvailabilityViewData arg1)
                    {

                        return arg1.getDepartureDate().isBefore(arg0.getDepartureDate()) ? 1 : 0;
                    }
                });
            }
        }
    }

    /**
     * @param villaAvailabilityRequest
     * @param searchRequest
     * @param availabilityMap
     * @param villaAvailabilityResponse
     * @param firstAvailableDate
     * @param searchParams
     */
    private void constructVillaAvailabilityView(final VillaAvailabilityRequest villaAvailabilityRequest,
            final VillaAvailabilitySearchRequestData searchRequest, final Map<String, BackingObject> availabilityMap,
            final VillaAvailabilityResponse villaAvailabilityResponse, final DateTime firstAvailableDate,
            final SearchParams searchParams)
    {
        final List<VillaAvailabilityViewDataWrapper> villaAvailabilityList = new ArrayList<VillaAvailabilityViewDataWrapper>();
        final Map<String, VillaAvailabilityViewDataWrapper> monthYearGrouped = new HashMap<String, VillaAvailabilityViewDataWrapper>();
        villaAvailabilityResponse.setFirstAvailableDate(DateUtils.format(firstAvailableDate));
        villaAvailabilityResponse.setLastAvailableDate(DateUtils.format(DateUtils.dateAfterSoManyMonths(firstAvailableDate,
                SEASON_LENGTH)));
        groupMonthData(villaAvailabilityRequest, searchRequest, availabilityMap, villaAvailabilityResponse, firstAvailableDate,
                searchParams, villaAvailabilityList, monthYearGrouped);
        //Sorting the grouped data based on Date...for UI...needs to check if this can be done within the loop (Possible REFACTOR)
        Collections.sort(villaAvailabilityList, new Comparator<VillaAvailabilityViewDataWrapper>()
        {
            @Override
            public int compare(final VillaAvailabilityViewDataWrapper arg0, final VillaAvailabilityViewDataWrapper arg1)
            {

                return arg1.getMonthYear().getDate().isBefore(arg0.getMonthYear().getDate()) ? 1 : 0;
            }
        });

        villaAvailabilityResponse.setVillaAvailabilityData(villaAvailabilityList);
    }

    /**
     * @param searchRequest
     * @param availabilityMap
     * @param villaAvailabilityResponse
     * @param firstAvailableDateParam
     * @param searchParams
     * @param villaAvailabilityList
     * @param monthYearGrouped
     * @return DateTime
     */
    private DateTime groupMonthData(final VillaAvailabilityRequest villaAvailabilityRequest,
            final VillaAvailabilitySearchRequestData searchRequest, final Map<String, BackingObject> availabilityMap,
            final VillaAvailabilityResponse villaAvailabilityResponse, final DateTime firstAvailableDateParam,
            final SearchParams searchParams, final List<VillaAvailabilityViewDataWrapper> villaAvailabilityList,
            final Map<String, VillaAvailabilityViewDataWrapper> monthYearGrouped)
    {
        DateTime firstAvailableDate = firstAvailableDateParam;
        VillaAvailabilityViewDataWrapper villaAvailWrapper;
        VillaEndecaInfo villaEndecaInfo = null;
        if (!availabilityMap.isEmpty())
        {
            firstAvailableDate = createFromAvailableMap(villaAvailabilityRequest, availabilityMap, firstAvailableDate,
                    monthYearGrouped);
            villaAvailabilityList.addAll(monthYearGrouped.values());

            //Season Range should be populated with first available data provided from Endeca.
            villaAvailabilityResponse.setSeasonRange(populateSeasonRange(new DateTime()));
            villaAvailabilityResponse.setVillaAvailabilityData(villaAvailabilityList);
            villaAvailabilityResponse.setSearchParams(searchParams);
        }
        else
        {
            LOG.error("Missing Endeca response for Villa Availability");
            //Decide to construct 7 week data... Note: This will be empty and unavailable.
            if (StringUtils.isNotEmpty(searchRequest.getStartDate()))
            {
                firstAvailableDate = DateUtils.toDateTime(searchRequest.getStartDate(), "yyyy-MM-dd");
                for (int i = 0; i <= CommonwebitemsConstants.SIX; i++)
                {
                    final VillaAvailabilityViewData availabilityViewData = new VillaAvailabilityViewData();
                    villaEndecaInfo = generateMonthAndYear(firstAvailableDate);
                    if (monthYearGrouped.containsKey(villaEndecaInfo.toString()))
                    {
                        villaAvailWrapper = monthYearGrouped.get(villaEndecaInfo.toString());
                    }
                    else
                    {
                        villaAvailWrapper = new VillaAvailabilityViewDataWrapper();
                    }

                    villaAvailWrapper.setMonthYear(villaEndecaInfo);
                    availabilityViewData.setDepartureDate(firstAvailableDate);
                    availabilityViewData.setWhen(DateUtils.format(firstAvailableDate));
                    availabilityViewData.setLabel(firstAvailableDate.dayOfWeek().getAsText());

                    villaAvailWrapper.getDays().add(availabilityViewData);

                    monthYearGrouped.put(villaEndecaInfo.toString(), villaAvailWrapper);
                    firstAvailableDate = processDateUsingDirection(firstAvailableDate, villaAvailabilityRequest.getDirection());
                    if (futureDateOrPastDate(firstAvailableDate, villaAvailabilityRequest.getDirection()))
                    {
                        break;
                    }
                }
            }
            villaAvailabilityList.addAll(monthYearGrouped.values());
            //Season Range should be populated with first available data provided from Endeca.
            villaAvailabilityResponse.setSeasonRange(populateSeasonRange(new DateTime()));
            villaAvailabilityResponse.setSearchParams(searchParams);
        }
        return firstAvailableDate;
    }

    /**
     * @param nextDate
     * @param direction
     * @return boolean
     */
    private boolean futureDateOrPastDate(final DateTime nextDate, final String direction)
    {
        final DateTime currentDate = new DateTime();
        if (scrollingBack(direction))
        {
            return nextDate.isAfter(currentDate);
        }
        return currentDate.isAfter(nextDate);
    }

    public boolean scrollingBack(final String direction)
    {
        if (StringUtils.isNotEmpty(direction) && StringUtils.contains(direction, "back"))
        {
            return true;
        }
        return false;
    }

    /**
     * @param firstAvailableDate
     * @return DateTime
     */
    private DateTime processDateUsingDirection(final DateTime firstAvailableDate, final String direction)
    {
        if (scrollingBack(direction))
        {
            return firstAvailableDate.minusDays(CommonwebitemsConstants.SEVEN);
        }
        else
        {
            return firstAvailableDate.plusDays(CommonwebitemsConstants.SEVEN);
        }
    }

    /**
     * @param availabilityMap
     * @param firstAvailableDateParam
     * @param monthYearGrouped
     * @return DateTime
     */
    private DateTime createFromAvailableMap(final VillaAvailabilityRequest villaAvailabilityRequest,
            final Map<String, BackingObject> availabilityMap, final DateTime firstAvailableDateParam,
            final Map<String, VillaAvailabilityViewDataWrapper> monthYearGrouped)
    {
        VillaAvailabilityViewDataWrapper villaAvailWrapper;
        VillaEndecaInfo villaEndecaInfo;

        DateTime firstAvailableDate = firstAvailableDateParam;
        if (scrollingBack(villaAvailabilityRequest.getDirection()))
        {
            firstAvailableDate = villaAvailabilityRequest.getStartDate();

        }

        for (int i = 0; i <= CommonwebitemsConstants.SIX; i++)
        {
            final VillaAvailabilityViewData availabilityViewData = new VillaAvailabilityViewData();

            final String key = firstAvailableDate.getDayOfMonth() + "-" + firstAvailableDate.getMonthOfYear() + "-"
                    + firstAvailableDate.getYear();


            final BackingObject backObj = availabilityMap.get(key);
            if (backObj != null)
            {

                villaEndecaInfo = generateMonthAndYear(firstAvailableDate);
                if (monthYearGrouped.containsKey(villaEndecaInfo.toString()))
                {
                    villaAvailWrapper = monthYearGrouped.get(villaEndecaInfo.toString());
                }
                else
                {
                    villaAvailWrapper = new VillaAvailabilityViewDataWrapper();
                }

                villaAvailWrapper.setMonthYear(villaEndecaInfo);
                availabilityViewData.setDepartureDate(backObj.getDate());
                availabilityViewData.setWhen(DateUtils.format(backObj.getDate()));
                availabilityViewData.setLabel(backObj.getDate().dayOfWeek().getAsText());
                availabilityViewData.setPrice(backObj.getPrice().intValue());
                availabilityViewData.setPricePP(backObj.getPricePP().intValue());
                availabilityViewData.setAvailability(true);
                availabilityViewData.setFreeChildPlace(true);
                availabilityViewData.setCarHire(StringUtils.contains(backObj.getCarHire(), "Y") ? true : false);
                final String airportCode = backObj.getHoliday().getItinerary().getOutbound().get(0).getDepartureAirport().getCode();
                availabilityViewData.setAirportCode(airportCode);
                availabilityViewData.setAirportName(airportService.getAirportByCode(airportCode).getName());
                availabilityViewData.setDuration(backObj.getStays());
            }
            else
            {
                villaEndecaInfo = generateMonthAndYear(firstAvailableDate);
                if (monthYearGrouped.containsKey(villaEndecaInfo.toString()))
                {
                    villaAvailWrapper = monthYearGrouped.get(villaEndecaInfo.toString());
                }
                else
                {
                    villaAvailWrapper = new VillaAvailabilityViewDataWrapper();
                }

                villaAvailWrapper.setMonthYear(villaEndecaInfo);
                availabilityViewData.setDepartureDate(firstAvailableDate);
                availabilityViewData.setWhen(DateUtils.format(firstAvailableDate));
                availabilityViewData.setLabel(firstAvailableDate.dayOfWeek().getAsText());
            }
            villaAvailWrapper.getDays().add(availabilityViewData);

            monthYearGrouped.put(villaEndecaInfo.toString(), villaAvailWrapper);
            firstAvailableDate = processDateUsingDirection(firstAvailableDate, villaAvailabilityRequest.getDirection());
        }
        return firstAvailableDate;
    }

    /**
     * @param villaAvailability
     * @param availabilityMap
     * @return DateTime
     */
    private DateTime findFirstAvailableDate(final SearchResultData<ResultData> villaAvailability,
            final Map<String, BackingObject> availabilityMap, final VillaAvailabilityRequest villaAvailabilityRequest)
    {
        final List<DateTime> availableDates = new ArrayList<DateTime>();
        DateTime firstAvailableDate = villaAvailabilityRequest.getStartDate();
        if (villaAvailability != null && CollectionUtils.isNotEmpty(villaAvailability.getResults()))
        {
            for (final ResultData result : villaAvailability.getResults())
            {
                groupBackingObjects(availabilityMap, availableDates, result);
            }

            if (CollectionUtils.isNotEmpty(availableDates))
            {
                Collections.sort(availableDates);
                firstAvailableDate = availableDates.get(0);
            }
        }
        return firstAvailableDate;
    }

    /**
     * @param availabilityMap
     * @param availableDates
     * @param result
     */
    private void groupBackingObjects(final Map<String, BackingObject> availabilityMap, final List<DateTime> availableDates,
            final ResultData result)
    {
        if (result != null && CollectionUtils.isNotEmpty(result.getBackingObject()))
        {
            for (final BackingObject backingObject : result.getBackingObject())
            {
                availableDates.add(backingObject.getDate());
                final String key = backingObject.getDate().getDayOfMonth() + "-" + backingObject.getDate().getMonthOfYear() + "-"
                        + backingObject.getDate().getYear();

                availabilityMap.put(key, backingObject);

            }
        }
    }

    /**
     * @param date
     * @return VillaEndecaInfo
     */
    private VillaEndecaInfo generateMonthAndYear(final DateTime date)
    {
        final VillaEndecaInfo villaDateInfo = new VillaEndecaInfo();
        villaDateInfo.setMonth(StringUtils.EMPTY + date.monthOfYear().getAsShortText());
        villaDateInfo.setYear(StringUtils.EMPTY + date.getYear());
        villaDateInfo.setDate(date);
        villaDateInfo.setId(date.minusDays(date.getDayOfMonth() - 1).toString("dd-MM-yyyy"));
        return villaDateInfo;
    }




    /**
     * @return List<MonthYearViewData>
     */
    @SuppressWarnings("boxing")
    private List<MonthYearViewData> populateSeasonRange(final DateTime start)
    {

        final String defaultDayOfweek = "Fri";
        final List<MonthYearViewData> monthYear = new ArrayList<MonthYearViewData>();
        DateTime startDate = new DateTime();
        startDate = start;
        final DateTime seasonEndDate = startDate.plusMonths(SEASON_LENGTH);
        while (startDate.isBefore(seasonEndDate))
        {
            final StringBuilder monthYearSB = new StringBuilder();
            monthYearSB.append(startDate.monthOfYear().getAsShortText()).append(" ").append(startDate.year().getAsShortText());
            final MonthYearViewData monthYearView = new MonthYearViewData(startDate.toString("dd-MM-yyyy"), monthYearSB.toString());
            monthYear.add(monthYearView);
            //Adding month's and also ensuring only First day of the specific month is passed from now on in the loop.
            startDate = startDate.plusMonths(1);
            startDate = startDate.minusDays(startDate.getDayOfMonth() - 1);
            final DateTime dateWithFirstDay = startDate.withDayOfWeek(DateUtils.DAYS_OF_THE_WEEK.get(defaultDayOfweek));
            if (dateWithFirstDay.getMonthOfYear() == startDate.getMonthOfYear())
            {
                startDate = dateWithFirstDay;
            }
            else
            {
                startDate = dateWithFirstDay.plusDays(CommonwebitemsConstants.SEVEN);
            }
        }
        return monthYear;
    }


}
