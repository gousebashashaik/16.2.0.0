/**
 *
 */
package uk.co.portaltech.tui.web.search;

import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.WidenSearchCriteriaData;

public interface CriteriaWidenService
{

   WidenSearchCriteriaData widenSearchCriteria(SearchResultsRequestData searchResults,
      String widenType);

}
