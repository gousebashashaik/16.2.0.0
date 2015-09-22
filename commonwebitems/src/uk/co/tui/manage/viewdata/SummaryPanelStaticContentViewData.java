/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.HashMap;
import java.util.Map;


/**
 * @author sumit.ks
 *
 */
public class SummaryPanelStaticContentViewData
{


    /** The summary Content map. */
    private Map<String, String> summaryContentMap = new HashMap<String, String>();

    /**
     * @param summaryContentMap
     *           the summaryContentMap to set
     */
    public void setSummaryContentMap(final Map<String, String> summaryContentMap)
    {
        this.summaryContentMap = summaryContentMap;
    }

    /**
     * @return the summaryContentMap
     */
    public Map<String, String> getSummaryContentMap()
    {
        return summaryContentMap;
    }


}
