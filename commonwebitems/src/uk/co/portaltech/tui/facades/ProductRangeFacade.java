/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.web.view.data.CollectionGroupViewData;
import uk.co.portaltech.tui.web.view.data.CollectionViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCollectionViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.tui.web.common.enums.ProductRangeForCrossSell;


/**
 * @author omonikhide
 *
 */
public interface ProductRangeFacade
{

    ProductRangeViewData getProductRangeBenefitsData(String productRangeCode);

    /**
     * returns list of Product Ranges(Holiday Collections)
     *
     * @return Map<String, List<ProductRangeCollectionViewData>>
     */
    Map<String, ProductRangeCategoryViewData> getAllProductRanges();

    /**
     * This method returns the Product Range Category Data based on the passed value.
     *
     * @param productRangeCategoryCode
     * @return ProductRangeCategoryViewData
     */
    ProductRangeCategoryViewData getProductRange(String productRangeCategoryCode);

    /**
     * Retrieves highlights associated with a Product Range
     *
     * @param productRangeCode
     *           The code for the Product Range
     * @param highlightsNumber
     *           Number of highlights to retrieves
     * @return The {@link ProductRangeViewData} object with a map containing USPs features and their respective values
     */
    ProductRangeViewData getProductRangeHighlights(String productRangeCode, Integer highlightsNumber);

    /**
     * This method returns the Product from the Product Range based on the passed value.
     *
     * @param productRangeCode
     * @return The {@link ProductRangeViewData} object
     */
    ProductRangeCollectionViewData getProduct(String productRangeCode);

    /**
     *
     * Retrieves the Product Range associated with the given code along with its Product USPs.
     *
     * @param productRangeCode
     *           The code for the Product Range
     * @return The {@link ProductRangeViewData} object containing basic info about the Product Range and the list of
     *         Product USPs associated with it.
     */
    ProductRangeViewData getProductRangeUsps(String productRangeCode);

    ProductRangeViewData getProductRangeByCode(String productRangeCode);

    /**
     * Retrieves the product Range associated with the given productRange codes.
     *
     * @param productRangeCodes
     * @return List of ProductRangeCollectionViewData
     */
    List<ProductRangeViewData> getProductRanges(List<ProductRangeForCrossSell> productRangeCodes);


    /**
     * @param collectionList
     * @return list of CollectionViewData
     */
    List<CollectionViewData> getAllCollectionData(List<CollectionGroupViewData> collectionList);

    /**
     * @param brand
     * @return list of ProductRangeViewData
     */
    List<ProductRangeViewData> getProductRangesByBrand(String brand);
}
