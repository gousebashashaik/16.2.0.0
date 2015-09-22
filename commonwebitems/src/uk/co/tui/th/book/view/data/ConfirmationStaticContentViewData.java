/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thyagaraju.e
 *
 */
public class ConfirmationStaticContentViewData {

    /** The confirmation content map. */
    private Map<String,String> confirmationContentMap = new HashMap<String, String>();

    /**
     * @return the confirmationContentMap
     */
    public Map<String, String> getConfirmationContentMap() {
        return confirmationContentMap;
    }

    /**
     * @param confirmationContentMap the confirmationContentMap to set
     */
    public void setConfirmationContentMap(Map<String, String> confirmationContentMap) {
        this.confirmationContentMap = confirmationContentMap;
    }

}
