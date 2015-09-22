/**
 *
 */
package uk.co.tui.book.view.data;

import java.util.List;

/**
 * @author srikanth.bs
 *
 */
public class PassengerInsuranceViewData {

    /** Holds list of passenger view data */
    private List<PassengerData> passengerDataList;

    /** Holds true or false.If it is a family insurance then it is true, else it is false */
    private boolean familyInsPresent;

    /**
     * @return the passengerDataList
     */
    public List<PassengerData> getPassengerDataList() {
        return passengerDataList;
    }

    /**
     * @param passengerDataList the passengerDataList to set
     */
    public void setPassengerDataList(List<PassengerData> passengerDataList) {
        this.passengerDataList = passengerDataList;
    }

    /**
     * @return the familyInsPresent
     */
    public boolean isFamilyInsPresent() {
        return familyInsPresent;
    }

    /**
     * @param familyInsPresent the familyInsPresent to set
     */
    public void setFamilyInsPresent(boolean familyInsPresent) {
        this.familyInsPresent = familyInsPresent;
    }

}
