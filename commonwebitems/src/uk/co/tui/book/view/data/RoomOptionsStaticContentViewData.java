/**
 *
 */
package uk.co.tui.book.view.data;

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
}
