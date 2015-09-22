/**
 *
 */
package uk.co.portaltech.tui.facades;


import de.hybris.platform.category.model.CategoryModel;

import uk.co.portaltech.travel.model.results.SmerchConfiguration;
import uk.co.portaltech.tui.components.model.ProductPromoComponentModel;
import uk.co.portaltech.tui.web.view.data.DealsResult;
import uk.co.portaltech.tui.web.view.data.MerchandiserRequest;
import uk.co.portaltech.tui.web.view.data.MerchandiserResponse;
import uk.co.portaltech.tui.web.view.data.MerchandiserViewData;
import uk.co.portaltech.tui.web.view.data.ProductPromoViewData;
import uk.co.tui.exception.SearchResultsBusinessException;
import java.util.List;
import java.util.Map;
import uk.co.portaltech.tui.web.view.data.DealsCategoryViewData;



public interface MerchandiserFacade {

    /**get Deals Search result**/
    DealsResult getDeals(MerchandiserRequest  dealsRequest) throws SearchResultsBusinessException;

    /**get Inventory Search result**/
    MerchandiserResponse getInvGroups(MerchandiserRequest  invGroupsRequest) throws SearchResultsBusinessException;

    Map<String, List<DealsCategoryViewData>> getDealsCategoryCollection();



    List<ProductPromoViewData> getProductPromoViewData(ProductPromoComponentModel component);

    /**Get result from Endeca for Inventory*/
    MerchandiserViewData getInvPackages(MerchandiserRequest request)
            throws SearchResultsBusinessException;

    /**Get result from Endeca for Deals*/
    DealsResult getDealsResult(MerchandiserRequest request)
            throws SearchResultsBusinessException;

    /**
     * @param dealsRequest
     * @return SmerchConfiguration
     * @throws SearchResultsBusinessException
     */
    SmerchConfiguration getSmerchConfiguration(MerchandiserRequest dealsRequest) throws SearchResultsBusinessException;

    boolean isDealsApplicable(CategoryModel location);
}
