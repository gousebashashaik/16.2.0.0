/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;

/**
 * @author ramkishore.p
 *
 */
public class CabinOptionsViewData extends RoomOptionsViewData {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    /** The list of CabinTypeViewData. */
    private List<CabinTypeViewData> listOfCabinTypeViewData;

    /**
     * Gets the list of cabin type view data.
     *
     * @return the list of cabin type view data
     */
    public List<CabinTypeViewData> getListOfCabinTypeViewData() {
        return listOfCabinTypeViewData;
    }

    /**
     * Sets the list of cabin type view data.
     *
     * @param listOfCabinTypeViewData
     *            the new list of cabin type view data
     */
    public void setListOfCabinTypeViewData(
            final List<CabinTypeViewData> listOfCabinTypeViewData) {
        this.listOfCabinTypeViewData = listOfCabinTypeViewData;
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
