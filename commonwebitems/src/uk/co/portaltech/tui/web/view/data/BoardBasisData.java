/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaurav.b
 *
 */
public class BoardBasisData implements HasFeatures {

    private String boardbasisCode;
    private String name;
    private String price;
    private boolean defaultBoardBasis;

    private Map<String, List<Object>> featureCodesAndValues = new HashMap<String, List<Object>>();

    /**
     * @return the boardbasisCode
     */
    public String getBoardbasisCode() {
        return boardbasisCode;
    }

    /**
     * @param boardbasisCode
     *            the boardbasisCode to set
     */
    public void setBoardbasisCode(String boardbasisCode) {
        this.boardbasisCode = boardbasisCode;
    }

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
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return the defaultBoardBasis
     */
    public boolean isDefaultBoardBasis() {
        return defaultBoardBasis;
    }

    /**
     * @param defaultBoardBasis
     *            the defaultBoardBasis to set
     */
    public void setDefaultBoardBasis(boolean defaultBoardBasis) {
        this.defaultBoardBasis = defaultBoardBasis;
    }

    @Override
    public Map<String, List<Object>> getFeatureCodesAndValues() {
        return featureCodesAndValues;
    }

    @Override
    public void putFeatureCodesAndValues(
            Map<String, List<Object>> featureCodesAndValues) {
        this.featureCodesAndValues.putAll(featureCodesAndValues);
    }

    @Override
    public void putFeatureValue(String featureCode, List<Object> featureValues) {
        featureCodesAndValues.put(featureCode, featureValues);
    }

    public void putFeatureValueNonObjectType(String featureCode,
            List<? extends Object> featureValues) {
        featureCodesAndValues.put(featureCode, (List<Object>) featureValues);
    }

    @Override
    public List<Object> getFeatureValues(String featureCode) {
        return featureCodesAndValues.get(featureCode);
    }

}
