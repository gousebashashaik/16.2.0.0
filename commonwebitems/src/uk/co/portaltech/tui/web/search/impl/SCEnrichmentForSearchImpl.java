/**
 *
 */
package uk.co.portaltech.tui.web.search.impl;

import static uk.co.portaltech.commons.DateUtils.addDuration;
import static uk.co.portaltech.commons.DateUtils.subtractDates;
import static uk.co.portaltech.commons.DateUtils.subtractDuration;
import static uk.co.portaltech.commons.DateUtils.toDate;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.search.SCEnrichmentForSearch;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.WidenSearchCriteriaData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


public class SCEnrichmentForSearchImpl implements SCEnrichmentForSearch
{

    private static final String BOXING = "boxing";

    @Resource
    private ConfigurationService configurationService;

    @Resource
    private TuiUtilityService tuiUtilityService;

    //added for test case execution
    public SCEnrichmentForSearchImpl()
    {

    }

    public SCEnrichmentForSearchImpl(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }

    @SuppressWarnings(BOXING)
    @Override
    public void enrichCriteria(final WidenSearchCriteriaData widenSearchCriteria, final SearchPanelComponentModel searchComponent)
    {

        final SearchResultsRequestData searchParameter = widenSearchCriteria.getSearchRequest();

        createSearchDates(searchParameter);

        searchParameter.setSearchRequestType("datesonly");

        final int infantCount = getInfantCount(searchParameter.getChildrenAge(), searchComponent.getInfantAge());
        searchParameter.setInfantCount(infantCount);

        if (searchParameter.getNoOfChildren() > 0)
        {
            searchParameter.setChildCount(searchParameter.getNoOfChildren() - infantCount);
        }

        searchParameter.setChildAges(searchParameter.getChildrenAge());

    }

    @SuppressWarnings(BOXING)
    private int getInfantCount(final List<Integer> childrenAge, final int infantAge)
    {
        int infantCount = 0;

        if (CollectionUtils.isNotEmpty(childrenAge))
        {
            for (final Integer age : childrenAge)
            {
                if (age < infantAge)
                {
                    infantCount++;

                }
            }
        }
        return infantCount;
    }


    @SuppressWarnings(BOXING)
    @Override
    public void enrichCriteriaForHolidayFinder(final WidenSearchCriteriaData widenSearchCriteria,
            final HolidayFinderComponentModel holidayFinderComponent)
    {

        final SearchResultsRequestData searchParameter = widenSearchCriteria.getSearchRequest();

        createSearchDates(searchParameter);

        searchParameter.setSearchRequestType("datesonly");

        final int infantCount = getInfantCount(searchParameter.getChildrenAge(), holidayFinderComponent.getInfantAge());
        searchParameter.setInfantCount(infantCount);

        if (searchParameter.getNoOfChildren() > 0)
        {
            searchParameter.setChildCount(searchParameter.getNoOfChildren() - infantCount);
        }

        searchParameter.setChildAges(searchParameter.getChildrenAge());

    }


    @SuppressWarnings(BOXING)
    @Override
    public void enrichCriteriaForNewSearchPanel(final WidenSearchCriteriaData widenSearchCriteria,
            final NewSearchPanelComponentModel searchPanelComponent)
    {

        final SearchResultsRequestData searchParameter = widenSearchCriteria.getSearchRequest();

        createSearchDates(searchParameter);

        searchParameter.setSearchRequestType("datesonly");

        final int infantCount = getInfantCount(searchParameter.getChildrenAge(), searchPanelComponent.getInfantAge());
        searchParameter.setInfantCount(infantCount);

        if (searchParameter.getNoOfChildren() > 0)
        {
            searchParameter.setChildCount(searchParameter.getNoOfChildren() - infantCount);
        }

        searchParameter.setChildAges(searchParameter.getChildrenAge());

    }

    /**
     * @param searchParameter
     */
    private void createSearchDates(final SearchResultsRequestData searchParameter)
    {
        if (StringUtils.isNotEmpty(searchParameter.getDepartureDate())
                && ((subtractDates(toDate(searchParameter.getDepartureDate()), toDate(searchParameter.getWhen()))) > 0))
        {
            searchParameter.setUntil(addDuration(searchParameter.getDepartureDate(), searchParameter.getFlexibleDays()));
            searchParameter.setWhen(subtractDuration(searchParameter.getDepartureDate(), searchParameter.getFlexibleDays()));
        }
        else
        {
            searchParameter.setDepartureDate(searchParameter.getWhen());
            searchParameter.setUntil(addDuration(searchParameter.getWhen(), searchParameter.getFlexibleDays()));
            searchParameter.setWhen(subtractDuration(searchParameter.getWhen(), searchParameter.getFlexibleDays()));
        }

        //Added below condition for  inventory switch to  @com , mainly for boundary condition of dates
        populateAtcomSwitchDate(searchParameter);
    }

    /**
     * Conditon for population
     *
     * if difference between departure date and switch date falls between flexible days the when and until has to be
     * restricted
     *
     * @param searchParameter
     */
    public void populateAtcomSwitchDate(final SearchResultsRequestData searchParameter)
    {
        final LocalDate departureDate = DateUtils.toDate(searchParameter.getDepartureDate());

        final LocalDate switchDate = DateUtils.toDate(configurationService.getConfiguration().getString(
                tuiUtilityService.getSiteBrand() + CommonwebitemsConstants.DOT + CommonwebitemsConstants.TRACS_END_DATE_PROPERTY,
                "01-11-2015"));

        if ((DateUtils.isEqualOrAfter(departureDate, switchDate))
                && (DateUtils.subtractDates(departureDate, switchDate) <= searchParameter.getFlexibleDays()))
        {
            searchParameter.setWhen(DateUtils.format(switchDate));
        }
        else if ((DateUtils.isBefore(departureDate, switchDate))
                && (DateUtils.subtractDates(switchDate, departureDate) <= searchParameter.getFlexibleDays()))
        {
            searchParameter.setUntil(DateUtils.format(switchDate));
        }

    }

}
