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
public class SummaryPanelStaticContentViewData {

    /** The summary Content map. */
    private Map<String,String> summaryContentMap = new HashMap<String, String>();

    public void setSummaryContentMap(Map<String, String> summaryContentMap) {
        this.summaryContentMap = summaryContentMap;
    }

    /**
     * @return the summaryContentMap
     */
    public Map<String, String> getSummaryContentMap() {
        return summaryContentMap;
    }

}
