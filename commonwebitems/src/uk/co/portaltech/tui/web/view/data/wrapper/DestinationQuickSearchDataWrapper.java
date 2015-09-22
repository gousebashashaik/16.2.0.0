/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;

/**
 * @author gagan
 *
 */
public class DestinationQuickSearchDataWrapper {

    private List<AccommodationViewData> accommodations;

    private List<LocationData> locations;

    /**
     * @return the accommodations
     */
    public List<AccommodationViewData> getAccommodations() {
        return accommodations;
    }

    /**
     * @param accommodations the accommodations to set
     */
    public void setAccommodations(List<AccommodationViewData> accommodations) {
        this.accommodations = accommodations;
    }

    /**
     * @return the locations
     */
    public List<LocationData> getLocations() {
        return locations;
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(List<LocationData> locations) {
        this.locations = locations;
    }

}
