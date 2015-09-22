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
public class PassengerDetailsStaticContentViewData {
    /** The passenger content map. */
    private Map<String,String> passengerContentMap = new HashMap<String, String>();

    private GenericStaticContentViewData genericContentViewData;

    private SummaryPanelStaticContentViewData summaryContentViewData;

    /**
     * @param passengerContentMap the passengerContentMap to set
     */
    public void setPassengerContentMap(final Map<String, String> passengerContentMap) {
        this.passengerContentMap = passengerContentMap;
    }

    /**
     * Puts all the passenger content.
     */
    public Map<String, String> getPassengerContentMap() {
        return passengerContentMap;
    }

    /**
     * @return the genericContentViewData
     */
    public GenericStaticContentViewData getGenericContentViewData() {
        return genericContentViewData;
    }

    /**
     * @param genericContentViewData the genericContentViewData to set
     */
    public void setGenericContentViewData(
            final GenericStaticContentViewData genericContentViewData) {
        this.genericContentViewData = genericContentViewData;
    }

    /**
     * @return the summaryContentViewData
     */
    public SummaryPanelStaticContentViewData getSummaryContentViewData() {
        return summaryContentViewData;
    }

    /**
     * @param summaryContentViewData the summaryContentViewData to set
     */
    public void setSummaryContentViewData(
            final SummaryPanelStaticContentViewData summaryContentViewData) {
        this.summaryContentViewData = summaryContentViewData;
    }
}