/**
 *
 */
package uk.co.portaltech.tui.services;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;


public interface NonCoreProductsService {

    void checkingNonCoreProducts(AccommodationModel source,
            SearchResultViewData target,String siteBrand);


}
