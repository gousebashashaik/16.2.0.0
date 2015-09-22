/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abi
 *
 */
public class PlacesToStayWrapper {

    private String                      topLocationName;
    private List<LocationData>          locations;
 private List<LocationData>          villaLocations;
    private List<LocationData>          hotelLocations;


    public PlacesToStayWrapper() {
        locations = new ArrayList<LocationData>();
    }
    /**
     * @return the topLocationName
     */
    public String getTopLocationName() {
        return topLocationName;
    }

    /**
     * @param topLocationName the topLocationName to set
     */
    public void setTopLocationName(String topLocationName) {
        this.topLocationName = topLocationName;
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

    /**
     * @return the villaLocations
     */
    public List<LocationData> getVillaLocations() {
        return villaLocations;
    }

    /**
     * @param villaLocations the villaLocations to set
     */
    public void setVillaLocations(List<LocationData> villaLocations) {
        this.villaLocations = villaLocations;
    }
    /**
     * @return the hotelLocations
     */
    public List<LocationData> getHotelLocations() {
        return hotelLocations;
    }
    /**
     * @param hotelLocations the hotelLocations to set
     */
    public void setHotelLocations(List<LocationData> hotelLocations) {
        this.hotelLocations = hotelLocations;
    }


}
