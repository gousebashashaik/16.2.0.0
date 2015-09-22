/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;
import java.util.Map;

/**
 * @author gopinath.n
 *
 */
public class AccommodationViewDataForMobile {

    private String name;
    private String locationMapUrl;
    private String mainSrc;
    private String priceFrom;
    private String tRating;
    private Map<String, List<Object>> featureCodesAndValues;
    private List<ProductRangeViewData> productRanges;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the locationMapUrl
     */
    public String getLocationMapUrl() {
        return locationMapUrl;
    }

    /**
     * @param locationMapUrl
     *            the locationMapUrl to set
     */
    public void setLocationMapUrl(String locationMapUrl) {
        this.locationMapUrl = locationMapUrl;
    }

    /**
     * @return the mainSrc
     */
    public String getMainSrc() {
        return mainSrc;
    }

    /**
     * @param mainSrc
     *            the mainSrc to set
     */
    public void setMainSrc(String mainSrc) {
        this.mainSrc = mainSrc;
    }

    /**
     * @return the priceFrom
     */
    public String getPriceFrom() {
        return priceFrom;
    }

    /**
     * @param priceFrom
     *            the priceFrom to set
     */
    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    /**
     * @return the tRating
     */
    public String gettRating() {
        return tRating;
    }

    /**
     * @param tRating
     *            the tRating to set
     */
    public void settRating(String tRating) {
        this.tRating = tRating;
    }

    /**
     * @return the featureCodesAndValues
     */
    public Map<String, List<Object>> getFeatureCodesAndValues() {
        return featureCodesAndValues;
    }

    /**
     * @param featureCodesAndValues
     *            the featureCodesAndValues to set
     */
    public void setFeatureCodesAndValues(
            Map<String, List<Object>> featureCodesAndValues) {
        this.featureCodesAndValues = featureCodesAndValues;
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

}
