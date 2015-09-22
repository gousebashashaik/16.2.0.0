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
public class RoomOptionsStaticContentViewData {

    /** The room content map. */
    private Map<String,String> roomContentMap = new HashMap<String, String>();

    private GenericStaticContentViewData genericContentViewData;

    private SummaryPanelStaticContentViewData summaryContentViewData;

    /**
     * @param roomContentMap the roomContentMap to set
     */
    public void setRoomContentMap(final Map<String, String> roomContentMap) {
        this.roomContentMap = roomContentMap;
    }

    /**
     * Puts all the room content.
     */
    public Map<String, String> getRoomContentMap() {
        return roomContentMap;
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
        if (this.summaryContentViewData == null) {
            return new SummaryPanelStaticContentViewData();
        }
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
