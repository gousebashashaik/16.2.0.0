/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.List;

/**
 * The Class CabinOptionsViewData.
 *
 * @author anithamani.s
 */
public class CabinOptionsViewData extends RoomOptionsViewData {

    /** The room index. */
    private int cabinIndex;

    /** The list of room view data. */
    private List<CabinViewData> listOfCabinViewData;

    /**
     * @return the cabinIndex
     */
    public int getCabinIndex() {
        return cabinIndex;
    }

    /**
     * @param cabinIndex
     *            the cabinIndex to set
     */
    public void setCabinIndex(final int cabinIndex) {
        this.cabinIndex = cabinIndex;
    }

    /**
     * Gets the list of cabin view data.
     *
     * @return the listOfCabinViewData
     */
    public List<CabinViewData> getListOfCabinViewData() {
        return listOfCabinViewData;
    }

    /**
     * Sets the list of cabin view data.
     *
     * @param listOfCabinViewData
     *            the listOfCabinViewData to set
     */
    public void setListOfCabinViewData(
            final List<CabinViewData> listOfCabinViewData) {
        this.listOfCabinViewData = listOfCabinViewData;
    }

}
