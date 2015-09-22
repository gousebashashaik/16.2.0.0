/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.web.view.data.FlightAvailabilityViewData;

/**
 * @author
 *
 */
public class FlightAvailabilityViewDataWrapper
{
    private Map<String, List<FlightAvailabilityViewData>> flightAvailabilityMap;

    /**
     * @return the flightAvailabilityMap
     */
    public Map<String, List<FlightAvailabilityViewData>> getFlightAvailabilityMap() {
        return flightAvailabilityMap;
    }

    /**
     * @param flightAvailabilityMap the flightAvailabilityMap to set
     */
    public void setFlightAvailabilityMap(
            Map<String, List<FlightAvailabilityViewData>> flightAvailabilityMap) {
        this.flightAvailabilityMap = flightAvailabilityMap;
    }

}
