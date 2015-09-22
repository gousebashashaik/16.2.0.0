/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;
import java.util.Map;

/**
 * @author omonikhide
 *
 */
public interface HasFeatures {
    /**
     *
     * @return all feature descriptors and their respective values
     */
    Map<String, List<Object>> getFeatureCodesAndValues();

    /**
     * This method puts feature descriptors and their values by passing in a hashmap of feature descriptor as key and
     * list of feature values as value.<br/>
     *
     * @param featureCodeAndValues
     *            a map containing feature descriptors and their values
     */
    void putFeatureCodesAndValues(Map<String, List<Object>> featureCodeAndValues);

    /**
     *
     * @param featureCode
     *            The feature descriptor
     * @param featureValues
     *            A list of the feature values for the specific feature descriptor
     */
    void putFeatureValue(String featureCode, List<Object> featureValues);

    /**
     *
     * @param featureCode
     *            The feature descriptor
     * @return A list of the feature values for the specific feature descriptor
     */
    List<Object> getFeatureValues(String featureCode);

}
