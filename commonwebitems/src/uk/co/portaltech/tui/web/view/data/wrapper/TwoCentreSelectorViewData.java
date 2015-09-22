/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.List;

import uk.co.portaltech.tui.model.ItineraryLeg;

/**
 * @author narendra.bm
 *
 */
public class TwoCentreSelectorViewData {

    private List<ItineraryLeg> itineraryLegs;

    /**
     * @return the itineraryLegs
     */
    public List<ItineraryLeg> getItineraryLegs() {
        return itineraryLegs;
    }

    /**
     * @param itineraryLegs the itineraryLegs to set
     */
    public void setItineraryLegs(List<ItineraryLeg> itineraryLegs) {
        this.itineraryLegs = itineraryLegs;
    }

}
