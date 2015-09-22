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
public class NavigationStaticContentViewData {

    /** The generic navigation map. */
    private Map<String,String> genericNavigationMap = new HashMap<String, String>();

    /**
     * @return the genericNavigationMap
     */
    public Map<String, String> getGenericNavigationMap() {
        return genericNavigationMap;
    }

    /**
     * @param genericNavigationMap the genericNavigationMap to set
     */
    public void setGenericNavigationMap(Map<String, String> genericNavigationMap) {
        this.genericNavigationMap = genericNavigationMap;
    }
}
