/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.io.Serializable;

/**
 * @author sunilkumar.sahu
 *
 */
public class RoomOptionsContentViewData implements Serializable {

    /** The room content view data. */
    private ContentViewData roomContentViewData;

    /**
     * @return the roomContentViewData
     */
    public ContentViewData getRoomContentViewData() {
        return roomContentViewData;
    }

    /**
     * @param roomContentViewData
     *            the roomContentViewData to set
     */
    public void setRoomContentViewData(final ContentViewData roomContentViewData) {
        this.roomContentViewData = roomContentViewData;
    }

}
