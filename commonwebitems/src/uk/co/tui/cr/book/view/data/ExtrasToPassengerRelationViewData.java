/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * The class to hold the Passenger and applicable Extras.
 *
 * @author madhumathi.m
 *
 */
public class ExtrasToPassengerRelationViewData {

    /**
     * The Passenger
     */
    private PassengerViewData passenger;

    /**
     * The Applicable ExtraFacilities.
     */
    private List<ExtraFacilityViewData> extras;

    /**
     * @return the passenger
     */
    public PassengerViewData getPassenger() {
        if (this.passenger == null) {
            this.passenger = new PassengerViewData();
        }
        return passenger;
    }

    /**
     * @param passenger
     *            the passenger to set
     */
    public void setPassenger(PassengerViewData passenger) {
        this.passenger = passenger;
    }

    /**
     * @return the extras
     */
    public List<ExtraFacilityViewData> getExtras() {
        if (this.extras == null) {
            this.extras = new ArrayList<ExtraFacilityViewData>();
        }

        return extras;
    }

    /**
     * @param extras
     *            the extras to set
     */
    public void setExtras(List<ExtraFacilityViewData> extras) {
        this.extras = extras;
    }

}
