/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.SmerchConfiguration;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;
import uk.co.portaltech.tui.web.view.data.ErrorData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.exception.SearchResultsBusinessException;

/**
 * @author laxmibai.p
 *
 */
public interface DeepLinkFacade {



    DeepLinkRequestViewData getDeepLinkRequestViewData(DeepLinkRequestData deepLinkRequestData,SearchPanelComponentModel searchPanel);

    SearchResultsRequestData getSearchResultsRequestData(DeepLinkRequestViewData deepLinkRequestViewData);

    DeepLinkRequestData getDeepLinkRequestData(HttpServletRequest request);

    Object getSearchResultsViewData(HttpServletRequest request, SearchPanelComponentModel searchPanel) throws SearchResultsBusinessException;

    boolean tamperData(List<ErrorData> errors);

    /**
     * @param results
     * @return
     */
    DeepLinkRequestData getDeepLinkRequestData(SmerchConfiguration results);

    DeepLinkRequestViewData getDeepLinkRequestViewDataForHolidayFinder(DeepLinkRequestData deepLinkRequestData,HolidayFinderComponentModel holidayFinder);

    SearchResultsRequestData getFlightOptionsDeepLinkViewData(DeepLinkRequestViewData deepLinkRequestViewData);

    DeepLinkRequestViewData getDeepLinkRequestViewDataForNewSearch(DeepLinkRequestData deepLinkRequestData,NewSearchPanelComponentModel newSearchPanel);


    /**
     * @param deepLinkRequestData
     * @param seachPanelComp
     * @return
     */
    DeepLinkRequestViewData getDeepLinkRequestViewDataForSmerch(
            DeepLinkRequestData deepLinkRequestData,
            SearchPanelComponentModel seachPanelComp);

    /**
     * @param deepLinkRequestData
     * @param holidayFinderComponent
     * @return
     */
    DeepLinkRequestViewData getDeepLinkRequestViewDataForSmerchMobile(DeepLinkRequestData deepLinkRequestData,
            HolidayFinderComponentModel holidayFinderComponent);
}
