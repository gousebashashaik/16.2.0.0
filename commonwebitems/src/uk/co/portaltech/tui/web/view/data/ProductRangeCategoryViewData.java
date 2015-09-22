/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.commercefacades.product.data.CategoryData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pts
 *
 */
public class ProductRangeCategoryViewData extends CategoryData implements HasFeatures {
    private String                               prCategoryName;
    private String                               prCategoryDescription;
    private List<ProductRangeCollectionViewData> prCollection;
    private List<ProductRangeViewData>           productRanges;

    private final Map<String, List<Object>> featureCodesAndValues;


    public ProductRangeCategoryViewData() {
        this.featureCodesAndValues = new HashMap<String, List<Object>>();
    }

    /**
     * @return the prCategoryName
     */
    public String getPrCategoryName() {
        return prCategoryName;
    }

    /**
     * @param prCategoryName
     *            the prCategoryName to set
     */
    public void setPrCategoryName(String prCategoryName) {
        this.prCategoryName = prCategoryName;
    }

    /**
     * @return the prCategoryDescription
     */
    public String getPrCategoryDescription() {
        return prCategoryDescription;
    }

    /**
     * @param prCategoryDescription
     *            the prCategoryDescription to set
     */
    public void setPrCategoryDescription(String prCategoryDescription) {
        this.prCategoryDescription = prCategoryDescription;
    }

    /**
     * @return the prCollection
     */
    public List<ProductRangeCollectionViewData> getPrCollection() {
        return prCollection;
    }

    /**
     * @param prCollection
     *            the prCollection to set
     */
    public void setPrCollection(List<ProductRangeCollectionViewData> prCollection) {
        this.prCollection = prCollection;
    }

    /**
     * @return the productRanges
     */
    public List<ProductRangeViewData> getProductRanges() {
        return productRanges;
    }

    /**
     * @param productRanges
     *            the productRanges to set
     */
    public void setProductRanges(List<ProductRangeViewData> productRanges) {
        this.productRanges = productRanges;
    }


    @Override
    public Map<String, List<Object>> getFeatureCodesAndValues() {
        return this.featureCodesAndValues;
    }

    @Override
    public void putFeatureCodesAndValues(Map<String, List<Object>> featureCodeAndValues) {
        this.featureCodesAndValues.putAll(featureCodeAndValues);
    }

    @Override
    public void putFeatureValue(String featureCode, List<Object> featureValues) {
        featureCodesAndValues.put(featureCode, featureValues);
    }

    @Override
    public List<Object> getFeatureValues(String featureCode) {
        return featureCodesAndValues.get(featureCode);
    }

}
