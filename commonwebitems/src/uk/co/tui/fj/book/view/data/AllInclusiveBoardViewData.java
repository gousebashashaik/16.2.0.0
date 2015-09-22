/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunilkumar.sahu
 *
 */
public class AllInclusiveBoardViewData {


    /** The featureCodesAndValues. */
    private Map<String, List<Object>> featureCodesAndValues = new HashMap<String, List<Object>>();


    /**
     * @return the featureCodesAndValues
     */
    public Map<String, List<Object>> getFeatureCodesAndValues() {
        return featureCodesAndValues;
    }

    /**
     * @param featureCodesAndValues the featureCodesAndValues to set
     */
    public void setFeatureCodesAndValues(
            Map<String, List<Object>> featureCodesAndValues) {
        this.featureCodesAndValues = featureCodesAndValues;
    }

}
