/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author uday.g
 *
 */
public class ConfirmationStaticContentViewData {

    /** The confirmation content map. */
    private Map<String, String> confirmationContentMap = new HashMap<String, String>();

    /**
     * @return the confirmationContentMap
     */
    public Map<String, String> getConfirmationContentMap() {
        return confirmationContentMap;
    }

    /**
     * @param confirmationContentMap
     *            the confirmationContentMap to set
     */
    public void setConfirmationContentMap(
            final Map<String, String> confirmationContentMap) {
        this.confirmationContentMap = confirmationContentMap;
    }

}
