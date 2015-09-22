/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.model.ItineraryLeg;


/**
 * @author narendra.bm
 *
 */
public class ItineraryViewData
{
    private Map<Integer, List<ItineraryLeg>> itinerary;

    /**
     * @return the itinerary
     */
    public Map<Integer, List<ItineraryLeg>> getItinerary() {
        return itinerary;
    }

    /**
     * @param itinerary the itinerary to set
     */
    public void setItinerary(Map<Integer, List<ItineraryLeg>> itinerary) {
        this.itinerary = itinerary;
    }
}
