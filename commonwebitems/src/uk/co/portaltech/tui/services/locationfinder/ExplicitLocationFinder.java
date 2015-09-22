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
public class ExplicitLocationFinder implements LocationFinder {

    /* (non-Javadoc)
     * @see uk.co.portaltech.tui.services.locationfinder.LocationFinder#search(uk.co.portaltech.tui.catalog.Data.SearchRequestData)
     */
    @Override
    public SearchResultData<LocationModel> search(SearchRequestData request) {


        //would be implemented when data exist in hmc
        return null;
    }



}
