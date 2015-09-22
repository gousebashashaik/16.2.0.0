/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;

/**
 * The Class RoomOptionsViewData.
 *
 * @author samantha.gd
 */
public class RoomOptionsViewData implements Cloneable {

    /** The room index. */
    private int roomIndex;

    /** The list of room view data. */
    private List<RoomViewData> listOfRoomViewData;

    /** The occupancy. */
    private OccupancyViewData occupancy;

    /**
     * Gets the room index.
     *
     * @return the room index
     */
    public int getRoomIndex() {
        return roomIndex;
    }

    /**
     * Sets the room index.
     *
     * @param roomIndex
     *            the new room index
     */
    public void setRoomIndex(final int roomIndex) {
        this.roomIndex = roomIndex;
    }

    /**
     * Gets the list of room view data.
     *
     * @return the list of room view data
     */
    public List<RoomViewData> getListOfRoomViewData() {
        return listOfRoomViewData;
    }

    /**
     * Sets the list of room view data.
     *
     * @param listOfRoomViewData
     *            the new list of room view data
     */
    public void setListOfRoomViewData(
            final List<RoomViewData> listOfRoomViewData) {
        this.listOfRoomViewData = listOfRoomViewData;
    }

    /**
     * Gets the occupancy.
     *
     * @return the occupancy
     */
    public OccupancyViewData getOccupancy() {
        return occupancy;
    }

    /**
     * Sets the occupancy.
     *
     * @param occupancy
     *            the new occupancy
     */
    public void setOccupancy(final OccupancyViewData occupancy) {
        this.occupancy = occupancy;
    }

    /**
     * Clone.
     *
     * @return the object
     * @throws CloneNotSupportedException
     *             the clone not supported exception
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();

    }
}
