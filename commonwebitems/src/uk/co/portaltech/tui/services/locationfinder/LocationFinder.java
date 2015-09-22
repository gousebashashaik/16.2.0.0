/**
 *
 */
package uk.co.portaltech.tui.services.locationfinder;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;

/**
 * @author omonikhide
 *
 */
public interface LocationFinder {

    SearchResultData<LocationModel> search(SearchRequestData request);
}
