/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.tui.constants.FilterConstants;
import uk.co.portaltech.tui.utils.ConfigurationUtils;
import uk.co.portaltech.tui.web.view.data.CommonData;
import uk.co.portaltech.tui.web.view.data.CommonFilterData;
import uk.co.portaltech.tui.web.view.data.DestinationOptionData;
import uk.co.portaltech.tui.web.view.data.FilterData;
import uk.co.portaltech.tui.web.view.data.FilterPanel;
import uk.co.portaltech.tui.web.view.data.FilterRequest;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.MainFilterRequest;
import uk.co.portaltech.tui.web.view.data.RoomAllocation;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SliderData;
import uk.co.portaltech.tui.web.view.data.SliderMainData;
import uk.co.portaltech.tui.web.view.data.SliderRequest;


/**
 * @author niranjani.r
 *
 */
public class BackToSearchFilterPopulator implements Populator<HolidayViewData, SearchResultsRequestData>
{
    @Resource
    private ConfigurationUtils configurationUtils;


    private static final int NUM_ZERO = 0;

    private static final int NUM_ONE = 1;

    private static final int NUM_TWO = 2;

    private static final int NUM_THREE = 3;

    private static final int NUM_FOUR = 4;

    private static final String STR_AND = "AND";

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final HolidayViewData source, final SearchResultsRequestData target) throws ConversionException
    {
        // to get the filter Panel search request data.
        final FilterPanel filterPanel = source.getSearchResult().getFilterPanel();
        filterPanel.setPriceView(target.getPriceView());
        final List filterDataList = new ArrayList();
        filterDataList.add(null);
        filterDataList.add(null);
        filterDataList.add(null);
        filterDataList.add(null);
        filterDataList.add(null);

        // To set all the filter data into search results.
        final List<CommonData> boardBasisFilter = new ArrayList<CommonData>();
        final List<CommonData> holidayOperatorFilter = new ArrayList<CommonData>();
        final List<CommonData> collectionFilter = new ArrayList<CommonData>();
        final List<CommonData> bestForFilter = new ArrayList<CommonData>();
        final List<CommonData> holidayTypeFilter = new ArrayList<CommonData>();



        // to get all the filter data of each filters in the request.
        final MainFilterRequest filterRequest = target.getFilters();
        final List<FilterRequest> boardBasisList = filterRequest.getBoardBasis();
        final List<FilterRequest> holidayOperatorList = filterRequest.getHolidayOperator();
        final List<FilterRequest> collectionList = filterRequest.getCollections();
        final List<FilterRequest> bestForList = filterRequest.getBestfor();
        final List<FilterRequest> holidayTypeList = filterRequest.getHolidayType();
        final List<FilterRequest> departurePointsList = filterRequest.getDeparturePoints();
        final List<FilterRequest> insoltsList = filterRequest.getInslots();
        final List<FilterRequest> outslotsList = filterRequest.getOutslots();
        final List<FilterRequest> accommodationTypeList = filterRequest.getAccommodationType();

        final DestinationOptionData destinationOptionData = target.getBackToDestinationOptionData();

        final List<FilterRequest> featureList = filterRequest.getFeatures();
        final SliderRequest officialRating = filterRequest.getFcRating();
        final SliderRequest tripRatings = filterRequest.getTripadvisorrating();
        final List<RoomAllocation> roomsFilter = target.getRooms();

        // setting the board basis filter
        if (roomsFilter != null && !roomsFilter.isEmpty())
        {
            filterPanel.getFilterVisibility().put(FilterConstants.NOOFROOMFILTER, Boolean.TRUE);
        }

        if (boardBasisList != null && !boardBasisList.isEmpty())
        {
            for (final FilterRequest filter : boardBasisList)
            {
                final CommonData filterValue = new CommonData();
                filterValue.setId(filter.getId());
                filterValue.setName(filter.getName());
                filterValue.setNoAccommodations(filter.getNoAccommodations());
                filterValue.setTooltip(configurationUtils.getBoardBasisToolTip(filter.getId()));
                filterValue.setValue(filter.getValue());
                filterValue.setSelected(filter.isSelected());
                boardBasisFilter.add(filterValue);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.BOARDBASISFILTER, Boolean.TRUE);
            final CommonFilterData boardBasisFilterData = new CommonFilterData();
            boardBasisFilterData.setName(FilterConstants.BOARDBASISNAME);
            boardBasisFilterData.setId(FilterConstants.BOARDBASISID);
            boardBasisFilterData.setValues(boardBasisFilter);
            boardBasisFilterData.setType(FilterConstants.CHECKBOX);
            boardBasisFilterData.setFilterType("OR");
            filterDataList.set(NUM_ZERO, boardBasisFilterData);
        }
        if (holidayOperatorList != null && !holidayOperatorList.isEmpty())
        {

            for (final FilterRequest filter : holidayOperatorList)
            {
                final CommonData filterValue = new CommonData();
                filterValue.setId(filter.getId());
                filterValue.setName(filter.getName());
                filterValue.setNoAccommodations(filter.getNoAccommodations());
                filterValue.setValue(filter.getValue());
                filterValue.setSelected(filter.isSelected());
                holidayOperatorFilter.add(filterValue);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.HOLIDAYOPERATORFILTER, Boolean.TRUE);
            final CommonFilterData holidayOperatorFilterData = new CommonFilterData();
            holidayOperatorFilterData.setValues(holidayOperatorFilter);
            holidayOperatorFilterData.setName(FilterConstants.HOLIDAY_OPERATOR_NAME);
            holidayOperatorFilterData.setId(FilterConstants.HOLIDAY_OPERATOR_CODE);
            holidayOperatorFilterData.setType(FilterConstants.CHECKBOX);
            holidayOperatorFilterData.setFilterType(STR_AND);
            filterDataList.set(NUM_ONE, holidayOperatorFilterData);

        }
        if (collectionList != null && !collectionList.isEmpty())
        {

            for (final FilterRequest filter : collectionList)
            {
                final CommonData filterValue = new CommonData();
                filterValue.setId(filter.getId());
                filterValue.setName(filter.getName());
                filterValue.setNoAccommodations(filter.getNoAccommodations());
                filterValue.setValue(filter.getValue());
                filterValue.setSelected(filter.isSelected());
                collectionFilter.add(filterValue);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.COLLECTIONFILTER, Boolean.TRUE);
            final CommonFilterData collectionFilterData = new CommonFilterData();
            collectionFilterData.setName(FilterConstants.COLLECTION_NAME);
            collectionFilterData.setId(FilterConstants.COLLECTION_ID);
            collectionFilterData.setType(FilterConstants.CHECKBOX);
            collectionFilterData.setFilterType("OR");
            collectionFilterData.setValues(collectionFilter);
            filterDataList.set(NUM_TWO, collectionFilterData);
        }
        if (bestForList != null && !bestForList.isEmpty())
        {

            for (final FilterRequest filter : bestForList)
            {
                final CommonData filterValue = new CommonData();
                filterValue.setId(filter.getId());
                filterValue.setName(filter.getName());
                filterValue.setNoAccommodations(filter.getNoAccommodations());
                filterValue.setValue(filter.getValue());
                filterValue.setSelected(filter.isSelected());
                bestForFilter.add(filterValue);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.BESTFORFILTER, Boolean.TRUE);
            final CommonFilterData bestForFilterData = new CommonFilterData();
            bestForFilterData.setName(FilterConstants.BESTFOR_NAME);
            bestForFilterData.setType(FilterConstants.CHECKBOX);
            bestForFilterData.setId(FilterConstants.BESTFOR_ID);
            bestForFilterData.setFilterType(STR_AND);
            bestForFilterData.setValues(bestForFilter);
            filterDataList.set(NUM_THREE, bestForFilterData);
        }
        if (holidayTypeList != null && !holidayTypeList.isEmpty())
        {

            for (final FilterRequest filter : holidayTypeList)
            {
                final CommonData filterValue = new CommonData();
                filterValue.setId(filter.getId());
                filterValue.setName(filter.getName());
                filterValue.setNoAccommodations(filter.getNoAccommodations());
                filterValue.setValue(filter.getValue());
                filterValue.setSelected(filter.isSelected());
                holidayTypeFilter.add(filterValue);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.HOLIDAYTYPEFILTER, Boolean.TRUE);
            final CommonFilterData holidayTypeFilterData = new CommonFilterData();
            holidayTypeFilterData.setName(FilterConstants.HOLIDAYTYPENAME);
            holidayTypeFilterData.setId(FilterConstants.HOLIDAYTYPEID);
            holidayTypeFilterData.setType(FilterConstants.CHECKBOX);
            holidayTypeFilterData.setFilterType("OR");
            holidayTypeFilterData.setValues(holidayTypeFilter);
            filterDataList.set(NUM_FOUR, holidayTypeFilterData);
        }
        filterDataList.removeAll(Collections.singletonList(null));
        final FilterData generalFilters = filterPanel.getGeneralOptions();
        if (generalFilters != null)
        {
            generalFilters.setFilters(filterDataList);
            filterPanel.setGeneralOptions(generalFilters);

        }
        else
        {
            final FilterData generelfilter = new FilterData();
            generelfilter.setName(FilterConstants.GENERAL_OPTIONS);
            generelfilter.setFilters(filterDataList);
            filterPanel.setGeneralOptions(generelfilter);

        }
        final List flightOptions = updateFlightOptionFilter(filterPanel, departurePointsList, insoltsList, outslotsList);
        final FilterData flightFilters = filterPanel.getFlightOptions();
        if (flightFilters != null)
        {
            flightFilters.setFilters(flightOptions);
            filterPanel.setFlightOptions(flightFilters);

        }
        else
        {
            final FilterData flight = new FilterData();
            flight.setName(FilterConstants.FLIGHT_OPTIONS);
            flight.setFilters(flightOptions);
            filterPanel.setFlightOptions(flight);

        }
        final List hotelOptions = updateAccommodationOptionsFilter(filterPanel, accommodationTypeList, featureList);
        final FilterData hotelFilters = filterPanel.getAccommodationOptions();
        if (hotelFilters != null)
        {
            hotelFilters.setFilters(hotelOptions);
            filterPanel.setAccommodationOptions(hotelFilters);

        }
        else
        {
            final FilterData hotel = new FilterData();
            hotel.setName(FilterConstants.ACCOMMODATION_OPTIONS);
            hotel.setFilters(hotelOptions);
            filterPanel.setAccommodationOptions(hotel);
        }
        final SliderData offRating = updateOfficialRatingSlider(filterPanel, officialRating);
        final SliderData tripRating = updateTripadvisorRatingSlider(filterPanel, tripRatings);
        final List ratingList = new ArrayList();
        ratingList.add(null);
        ratingList.add(null);
        ratingList.set(NUM_ZERO, offRating);
        ratingList.set(NUM_ONE, tripRating);
        ratingList.removeAll(Collections.singletonList(null));

        final SliderMainData rating = filterPanel.getRating();

        if (rating != null)
        {
            rating.setFilters(ratingList);
            filterPanel.setRating(rating);
        }
        else
        {
            final SliderMainData ratingData = new SliderMainData();
            ratingData.setName(FilterConstants.RATINGS);
            ratingData.setFilters(ratingList);
            filterPanel.setRating(ratingData);
        }

        source.getSearchResult().setFilterPanel(filterPanel);
        source.getSearchResult().getFilterPanel().setDestinationOptions(destinationOptionData);
    }


    /**
     * @param filterPanel
     * @param departurePoint
     * @param inslots
     * @param outslots
     * @return list.
     */
    private List updateFlightOptionFilter(final FilterPanel filterPanel, final List<FilterRequest> departurePoint,
            final List<FilterRequest> inslots, final List<FilterRequest> outslots)
    {
        final List filterDataList = new ArrayList();
        filterDataList.add(null);
        filterDataList.add(null);
        filterDataList.add(null);
        if (departurePoint != null && !departurePoint.isEmpty())
        {
            final List<CommonData> departurePoints = new ArrayList<CommonData>();
            for (final FilterRequest request : departurePoint)
            {
                final CommonData depPt = new CommonData();
                depPt.setId(request.getId());
                depPt.setName(request.getName());
                depPt.setValue(request.getValue());
                depPt.setCategoryCode(request.getCategoryCode());
                depPt.setSelected(request.isSelected());
                departurePoints.add(depPt);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.DEPAIRPORTFILTER, Boolean.TRUE);
            final CommonFilterData departure = new CommonFilterData();
            departure.setName(FilterConstants.DEPARTURE_POINTS);
            departure.setId("departurePoints");
            departure.setType(FilterConstants.CHECKBOX);
            departure.setFilterType("OR");
            departure.setValues(departurePoints);
            filterDataList.set(NUM_ZERO, departure);

        }
        final CommonFilterData inSlotsData = updateInslots(filterPanel, inslots);
        final CommonFilterData outSlotsData = updateOutSlots(filterPanel, outslots);
        filterDataList.set(NUM_ONE, outSlotsData);
        filterDataList.set(NUM_TWO, inSlotsData);
        filterDataList.removeAll(Collections.singletonList(null));
        return filterDataList;

    }

    /**
     * @param filterpanel
     * @param inslots
     * @return departureTime
     */
    private CommonFilterData updateInslots(final FilterPanel filterpanel, final List<FilterRequest> inslots)
    {
        if (inslots != null && !inslots.isEmpty())
        {
            final List<CommonData> inslotsList = new ArrayList<CommonData>();
            for (final FilterRequest inslot : inslots)
            {
                final CommonData slot = new CommonData();
                slot.setId(inslot.getId());
                slot.setValue(inslot.getValue());
                slot.setName(inslot.getName());
                slot.setSelected(inslot.isSelected());
                inslotsList.add(slot);
            }
            filterpanel.getFilterVisibility().put(FilterConstants.DEPCOMINGBACKFILTER, Boolean.TRUE);
            final CommonFilterData departureTime = new CommonFilterData();
            departureTime.setName(FilterConstants.INBOUND_TEXT);
            departureTime.setId(FilterConstants.INBOUND_CODE);
            departureTime.setType(FilterConstants.CHECKBOX);
            departureTime.setFilterType(STR_AND);
            departureTime.setValues(inslotsList);

            return departureTime;

        }
        return null;
    }

    /**
     * @param filterpanel
     * @param outslots
     * @return departure time.
     */
    private CommonFilterData updateOutSlots(final FilterPanel filterpanel, final List<FilterRequest> outslots)
    {
        if (outslots != null && !outslots.isEmpty())
        {
            final List<CommonData> outslotsList = new ArrayList<CommonData>();
            for (final FilterRequest inslot : outslots)
            {
                final CommonData slot = new CommonData();
                slot.setId(inslot.getId());
                slot.setValue(inslot.getValue());
                slot.setName(inslot.getName());
                slot.setSelected(inslot.isSelected());
                outslotsList.add(slot);
            }
            filterpanel.getFilterVisibility().put(FilterConstants.DEPGOINGOUTFILTER, Boolean.TRUE);
            final CommonFilterData departureTime = new CommonFilterData();
            departureTime.setName(FilterConstants.OUTBOUNND_TEXT);
            departureTime.setId(FilterConstants.OUTBOUND_CODE);
            departureTime.setType(FilterConstants.CHECKBOX);
            departureTime.setFilterType(STR_AND);
            departureTime.setValues(outslotsList);

            return departureTime;

        }
        return null;
    }

    /**
     * @param filterPanel
     * @param accommodationTypeList
     * @param featureList
     * @return list of accommodation options.
     */
    private List updateAccommodationOptionsFilter(final FilterPanel filterPanel, final List<FilterRequest> accommodationTypeList,
            final List<FilterRequest> featureList)
    {
        final CommonFilterData featureData = new CommonFilterData();
        final CommonFilterData accommType = new CommonFilterData();
        final List<CommonData> accommTypeFilters = new ArrayList<CommonData>();
        final List<CommonData> featureFilters = new ArrayList<CommonData>();
        final List filterDataList = new ArrayList();
        filterDataList.add(null);
        filterDataList.add(null);
        if (accommodationTypeList != null && !accommodationTypeList.isEmpty())
        {
            for (final FilterRequest accomm : accommodationTypeList)
            {
                final CommonData data = new CommonData();
                data.setId(accomm.getId());
                data.setName(accomm.getName());
                data.setNoAccommodations(accomm.getNoAccommodations());
                data.setCategoryCode(accomm.getCategoryCode());
                data.setSelected(accomm.isSelected());
                data.setValue(accomm.getValue());
                accommTypeFilters.add(data);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.ACCOM_TYPE_FILTER, Boolean.TRUE);
            accommType.setName(FilterConstants.ACCOMM_TYPE_NAME);
            accommType.setType(FilterConstants.CHECKBOX);
            accommType.setId(FilterConstants.ACCOMM_TYPE_ID);
            accommType.setFilterType("OR");
            accommType.setValues(accommTypeFilters);
            filterDataList.set(NUM_ZERO, accommType);
        }
        if (featureList != null && !featureList.isEmpty())
        {
            for (final FilterRequest feature : featureList)
            {
                final CommonData data = new CommonData();
                data.setId(feature.getId());
                data.setName(feature.getName());
                data.setValue(feature.getValue());
                data.setNoAccommodations(feature.getNoAccommodations());
                data.setSelected(feature.isSelected());
                data.setCategoryCode(feature.getCategoryCode());
                featureFilters.add(data);
            }
            filterPanel.getFilterVisibility().put(FilterConstants.FEATURESFILTER, Boolean.TRUE);
            featureData.setName(FilterConstants.FEATURES);
            featureData.setType(FilterConstants.CHECKBOX);
            featureData.setId(FilterConstants.FEATURES_ID);
            featureData.setFilterType(STR_AND);
            featureData.setValues(featureFilters);
            filterDataList.set(NUM_ONE, featureData);
        }
        filterDataList.removeAll(Collections.singletonList(null));
        return filterDataList;

    }

    private SliderData updateOfficialRatingSlider(final FilterPanel filter, final SliderRequest ratingSlider)
    {

        if (ratingSlider != null)
        {
            filter.getFilterVisibility().put(FilterConstants.FCRATINGFILTER, Boolean.TRUE);
            final List<Double> values = new ArrayList<Double>();
            final List<Double> limit = new ArrayList<Double>();
            final SliderData rating = new SliderData();

            rating.setId(FilterConstants.FCRATINGID);
            rating.setName(FilterConstants.FCRATINGNAME);
            rating.setType(FilterConstants.SLIDER);
            rating.setTrackType("minRange");
            rating.setCode(ratingSlider.getCode());
            values.add(Math.floor(Double.parseDouble(ratingSlider.getMax())));
            limit.add(Math.floor(Double.parseDouble(ratingSlider.getMin())));
            limit.add(Math.floor(Double.parseDouble("5")));
            rating.setValues(values);
            rating.setLimit(limit);
            final List<Long> range1 = new ArrayList<Long>();
            range1.add(Long.valueOf(FilterConstants.MINRANGE));

            range1.add(Long.valueOf(FilterConstants.MAXRANGE));
            rating.setSteps(FilterConstants.RATINGSTEPS);

            rating.setRange(range1);

            return rating;
        }
        else
        {
            return null;
        }

    }

    private SliderData updateTripadvisorRatingSlider(final FilterPanel filter, final SliderRequest ratingSlider)
    {

        if (ratingSlider != null)
        {
            filter.getFilterVisibility().put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.TRUE);
            final List<Double> values = new ArrayList<Double>();
            final List<Double> limit = new ArrayList<Double>();
            final SliderData minimumTripAdvisor = new SliderData();

            minimumTripAdvisor.setId(FilterConstants.TARATINGID);
            minimumTripAdvisor.setName(FilterConstants.TARATINGNAME);
            minimumTripAdvisor.setType(FilterConstants.SLIDER);
            minimumTripAdvisor.setTrackType(FilterConstants.TRACKTYPE);
            minimumTripAdvisor.setCode(ratingSlider.getCode());
            values.add(Math.floor(Double.parseDouble(ratingSlider.getMax())));
            limit.add(Math.floor(Double.parseDouble(ratingSlider.getMin())));
            limit.add(Math.floor(Double.parseDouble("5")));
            minimumTripAdvisor.setValues(values);
            minimumTripAdvisor.setLimit(limit);
            final List<Long> range1 = new ArrayList<Long>();
            range1.add(Long.valueOf(FilterConstants.MINRANGE));
            range1.add(Long.valueOf(FilterConstants.MAXRANGE));
            minimumTripAdvisor.setSteps(FilterConstants.RATINGSTEPS);
            minimumTripAdvisor.setRange(range1);

            return minimumTripAdvisor;
        }
        else
        {
            return null;
        }

    }
}
