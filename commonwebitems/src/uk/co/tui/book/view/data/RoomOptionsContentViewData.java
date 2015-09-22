/**
 *
 */
package uk.co.tui.book.view.data;

import java.io.Serializable;

/**
 * @author sunilkumar.sahu
 *
 */
public class RoomOptionsContentViewData implements Serializable {

    /** The room view data. */
    private ContentViewData roomContentViewData;

    /**
     * Gets the room Content View Data
     *
     * @return the roomContentViewData
     */
    public ContentViewData getRoomContentViewData() {
        return roomContentViewData;
    }

    /**
     * Sets the room view data
     *
     * @param roomContentViewData
     *            the roomContentViewData to set
     */
    public void setRoomContentViewData(final ContentViewData roomContentViewData) {
        this.roomContentViewData = roomContentViewData;
    }

}
