/**
 *
 */
package uk.co.portaltech.tui.web.url.builders;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author laxmibai.p
 *
 */
public interface RequestBuilder {

    /**
     * @param source
     * @param searchPanelComponentModel
     * @return URL
     */
    String builder(BookFlowAccommodationViewData source,
            SearchPanelComponentModel searchPanelComponentModel);

    /**
     * @param source
     * @param searchPanelComponentModel
     * @param requestData
     * @return request URL
     */
    String builder(BookFlowAccommodationViewData source,
            SearchPanelComponentModel searchPanelComponentModel,
            SearchResultsRequestData requestData);

    /**
     * @param resultsview
     * @param searchPanelComponent
     * @param searchParameter
     */
    void builder(SearchResultViewData resultsview,
            SearchPanelComponentModel searchPanelComponent,
            SearchResultsRequestData searchParameter);

    /**
     * @param source
     * @param holidayFinderComponent
     * @return URL
     */
    String builder(BookFlowAccommodationViewData source,
            HolidayFinderComponentModel holidayFinderComponent);

    /**
     * @param source
     * @param holidayFinderComponent
     * @param requestData
     * @return request URL
     */
    String builder(BookFlowAccommodationViewData source,
            HolidayFinderComponentModel holidayFinderComponent,
            SearchResultsRequestData requestData);

    /**
     * @param resultsview
     * @param holidayFinderComponent
     * @param searchParameter
     */
    void builder(SearchResultViewData resultsview,
            HolidayFinderComponentModel holidayFinderComponent,
            SearchResultsRequestData searchParameter);
}
