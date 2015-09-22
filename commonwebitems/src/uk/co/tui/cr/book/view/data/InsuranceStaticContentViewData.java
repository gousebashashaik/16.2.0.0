/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thyagaraju.e
 *
 */
public class InsuranceStaticContentViewData {

    /** The insurance content map. */
    private Map<String,String> insuranceContentMap = new HashMap<String, String>();

    /**
     * @return the insuranceContentMap
     */
    public Map<String, String> getInsuranceContentMap() {
        return insuranceContentMap;
    }

    /**
     * @param insuranceContentMap the insuranceContentMap to set
     */
    public void setInsuranceContentMap(Map<String, String> insuranceContentMap) {
        this.insuranceContentMap = insuranceContentMap;
    }
}
