/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;


/**
 * @author l.furrer
 *
 */
public interface ProductRangeCategoryFacade {

    /**
     * @return All the ProductRangeCategory, the root category (holidaycollections) is automatically removed.
     *
     */
    List<ProductRangeCategoryViewData> getAllProductRangeCategories();

    /**
     * Retrieves the {@link ProductRangeCategoryViewData} for the {@link ProductRangeCategoryModel} with the specified
     * code.
     *
     * @param productRangeCategoryCode
     * @return the view data populated with a list of {@link ProductRangeViewData} objects of the product ranges that
     *         are subcategories of this one
     */
    ProductRangeCategoryViewData getProductRangeCategoryByCode(String productRangeCategoryCode);

    /**
     * retrieves the media of USPs associated with the product range
     *
     * @param visibleItems
     *            The number of items to return
     * @param productRangeCode
     *            The code of the product range
     * @return A product range view data with media its USP media data.
     */
    ProductRangeCategoryViewData getProductOverviewCarouselData(int visibleItems, String productRangeCode);
}
