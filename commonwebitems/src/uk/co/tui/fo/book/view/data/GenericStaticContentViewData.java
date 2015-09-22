/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thyagaraju.e
 *
 */
public class GenericStaticContentViewData {

    private Map<String, String> genericContentMap = new HashMap<String, String>();

    /**
     * @param genericContentMap
     *            the genericContentMap to set
     */
    public void setGenericContentMap(Map<String, String> genericContentMap) {
        this.genericContentMap = genericContentMap;
    }

    /**
     * @return the genericContentMap
     */
    public Map<String, String> getGenericContentMap() {
        return genericContentMap;
    }

}
