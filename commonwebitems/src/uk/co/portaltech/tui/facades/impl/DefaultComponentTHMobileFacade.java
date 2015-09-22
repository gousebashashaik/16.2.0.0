/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.search.EnrichmentService;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsBusinessException;


/**
 * @author srinivasulu.vk
 *
 */
public class DefaultComponentTHMobileFacade extends DefaultComponentFacade
{

    private static final TUILogUtils LOG = new TUILogUtils("DefaultComponentTHMobileFacade");

    @Resource
    private EnrichmentService enrichmentService;

    @Resource
    private SessionService sessionService;

    @Resource
    private ViewSelector viewSelector;

    @Resource
    private SearchFacade searchFacade;

    private static final String LATESTCRITERIA = "latestCriteria";

    private static final String PAGINATE = "paginate";
    private static final String SORT = "sort";
    private static final String SEARCHRESULTS = "searchResults";

    private static final String FILTERVILLAS = Config.getString("th_villa", "");
    private static final String FILTERNONCOREPRODUCTS = Config.getString("filterNonCoreProducts", "");

    @Override
    public HolidayViewData getHolidayPackagesResultData(final SearchResultsRequestData searchParameter, final String siteBrand)
            throws SearchResultsBusinessException

    {
        final HolidayViewData holidayViewData = new HolidayViewData();
        holidayViewData.setSearchRequest(cloneSearchCriteria(searchParameter));
        if (viewSelector.checkIsMobile())
        {
            enrichmentService.enrichForHolidayFinder(getHolidayFinderComponent(), searchParameter);
        }
        else
        {
            enrichmentService.enrich(getSearchPanelComponent(), searchParameter);
        }

        if (StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), PAGINATE)
                || StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), SORT))
        {
            holidayViewData.setSearchResult(populateSearchResultsViewData(searchParameter, siteBrand));
            return holidayViewData;
        }

        //single accommodation search
        if (searchParameter.isSingleAccomSearch())
        {
            // latest search criteria is added because if we do any room change and go to accom
            // details page we take latest criteria from session to get the holiday.
            sessionService.setAttribute(LATESTCRITERIA, searchParameter);
            holidayViewData.setSearchResult(getSingleAccomHolidayPackagesResultData(searchParameter, siteBrand));

            //This map is populated when wishList map is not in seesion and holiday packages are returned from cache.
            populateWishListMap(holidayViewData);
            filterAccommodations(holidayViewData.getSearchResult());
            return holidayViewData;
        }

        if (isFollowOnSearch(searchParameter))
        {
            // latest search criteria is added because if we do any room change and go to accom
            // details page we take latest criteria from session to get the holiday.
            sessionService.setAttribute(LATESTCRITERIA, searchParameter);
            holidayViewData.setSearchResult(searchFacade.getHolidayPackagesResultDataForFacets(searchParameter, siteBrand));
            filterAccommodations(holidayViewData.getSearchResult());
            return holidayViewData;
        }

        sessionService.setAttribute(LATESTCRITERIA, searchParameter);
        holidayViewData.setSearchResult(searchFacade.getHolidayPackagesResultData(searchParameter, siteBrand));

        //This map is populated when wishList map is not in seesion and holiday packages are returned from cache.
        populateWishListMap(holidayViewData);

        filterAccommodations(holidayViewData.getSearchResult());
        return holidayViewData;
    }

    private SearchResultsViewData populateSearchResultsViewData(final SearchResultsRequestData searchParameter,
            final String siteBrand) throws SearchResultsBusinessException
    {

        LOG.info("This search Criteria is ignored except for page no's and sort criteria. First: " + searchParameter.getFirst()
                + " Offset: " + searchParameter.getOffset() + "Sort By: " + searchParameter.getSortBy());
        SearchResultsViewData searchResultsViewData = new SearchResultsViewData();

        final EndecaSearchResult endecaSearchResults = sessionService.getAttribute(SEARCHRESULTS);

        //checking previous search is Main Search / Follow On Search
        if (checkIsFollowSearchResult(endecaSearchResults))
        {

            if (StringUtils.equalsIgnoreCase(SORT, searchParameter.getSearchRequestType()))
            {

                LOG.info("sorting is applied on raw results");
            }

            //paginate and populate the vew data.
            searchFacade.populatePaginatedViewData(searchParameter, searchResultsViewData, endecaSearchResults, siteBrand);
            filterAccommodations(searchResultsViewData);
            return searchResultsViewData;

        }
        else
        {

            SearchResultsRequestData searchParameterInSession = sessionService.getAttribute(LATESTCRITERIA);

            if (StringUtils.equalsIgnoreCase(PAGINATE, searchParameter.getSearchRequestType()) && searchParameterInSession != null)
            {
                // to update the new page
                searchParameterInSession.setFirst(searchParameter.getFirst());
                searchParameterInSession.setOffset(searchParameter.getOffset());
                //If its an initial search pagination...use the View cache route  and If sort is not happened on previous results

                //pagination on main search results with out sorting from View Cache
                searchResultsViewData = searchFacade.getHolidayPackagesResultData(searchParameterInSession, siteBrand);
                filterAccommodations(searchResultsViewData);
                return searchResultsViewData;

            }
            else if (StringUtils.equalsIgnoreCase(SORT, searchParameter.getSearchRequestType()) && searchParameterInSession != null)
            {
                // to update the current search Criteria with any new sort
                // parameter
                searchParameterInSession.setSortBy(searchParameter.getSortBy());
                searchParameterInSession.setOffset(searchParameter.getOffset() * searchParameter.getFirst());
                //this is done if its a sort request post pagination request. it works for all other scenarios as well.
                searchParameterInSession.setFirst(1);
            }

            //temporary fix to handle the pagination for holiday context from mobile
            if (searchParameterInSession == null)
            {
                searchParameterInSession = searchParameter;
            }

            //getting results from Raw Cache / Endeca
            final EndecaSearchResult endecaSearchResult = searchFacade.getPackagesFromEndecaCache(searchParameterInSession);


            if (StringUtils.isNotBlank(endecaSearchResult.getTotalNumRecs()))
            {
                searchResultsViewData.setEndecaResultsCount(Integer.parseInt(endecaSearchResult.getTotalNumRecs()));
            }
            //sorting is applied on raw results
            //Storing Sorted main Results into Session.
            //paginate and populate the vew data.
            searchFacade.populatePaginatedViewData(searchParameter, searchResultsViewData, endecaSearchResult, siteBrand);
            filterAccommodations(searchResultsViewData);
            return searchResultsViewData;
        }
    }

    private void filterAccommodations(final SearchResultsViewData searchResultsViewData)
    {
        final List<SearchResultViewData> accommViewDataList = new ArrayList<SearchResultViewData>();
        final List<String> nonCoreProducts = Arrays.asList(FILTERNONCOREPRODUCTS.split(","));
        for (final SearchResultViewData accommodationViewData : searchResultsViewData.getHolidays())
        {
            if (nonCoreProducts.contains(accommodationViewData.getAccommodation().getAccomType()) || FILTERVILLAS
                    .contains(accommodationViewData.getAccommodation().getAccomType()))
            {
                accommViewDataList.add(accommodationViewData);
            }
        }
        searchResultsViewData.getHolidays().removeAll(accommViewDataList);
    }

}
