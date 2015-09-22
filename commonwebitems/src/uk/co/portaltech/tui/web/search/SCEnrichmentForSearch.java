/**
 *
 */
package uk.co.portaltech.tui.web.search;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.web.view.data.WidenSearchCriteriaData;

public interface SCEnrichmentForSearch {
    void enrichCriteria(WidenSearchCriteriaData widenSearchCriteria,SearchPanelComponentModel searchComponent);

    /**
     * @param widenSearchCriteria
     * @param holidayFinderComponent
     */
    void enrichCriteriaForHolidayFinder(
            WidenSearchCriteriaData widenSearchCriteria,
            HolidayFinderComponentModel holidayFinderComponent);

    /**
     * @param widenSearchCriteria
     * @param searchPanelComponent
     */
    void enrichCriteriaForNewSearchPanel(
            WidenSearchCriteriaData widenSearchCriteria,
            NewSearchPanelComponentModel searchPanelComponent);

}
