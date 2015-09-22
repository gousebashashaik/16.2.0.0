/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.LocationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;

/**
 * @author pts
 *
 */
public class TopPlacesWrapper {
    private LocationCarouselViewData locationsData;
    private AccommodationCarouselViewData accommodationsData;
    private AccommodationCarouselViewData villaData;
    private LocationData location;
    /**
     * @return the location
     */
    public LocationData getLocation() {
        return location;
    }
    /**
     * @param location the location to set
     */
    public void setLocation(LocationData location) {
        this.location = location;
    }
    /**
     * @return the locationsData
     */
    public LocationCarouselViewData getLocationsData() {
        return locationsData;
    }
    /**
     * @param locationsData the locationsData to set
     */
    public void setLocationsData(LocationCarouselViewData locationsData) {
        this.locationsData = locationsData;
    }
    /**
     * @return the accommodationsData
     */
    public AccommodationCarouselViewData getAccommodationsData() {
        return accommodationsData;
    }
    /**
     * @param accommodationsData the accommodationsData to set
     */
    public void setAccommodationsData(AccommodationCarouselViewData accommodationsData) {
        this.accommodationsData = accommodationsData;
    }
    /**
     * @return the villaData
     */
    public AccommodationCarouselViewData getVillaData() {
        return villaData;
    }
    /**
     * @param villaData the villaData to set
     */
    public void setVillaData(AccommodationCarouselViewData villaData) {
        this.villaData = villaData;
    }

}
