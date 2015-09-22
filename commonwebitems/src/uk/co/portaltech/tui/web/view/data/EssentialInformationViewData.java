/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.portaltech.tui.model.ItineraryLeg;

/**
 * @author
 *
 */
public class EssentialInformationViewData {

    private List<ItineraryLeg> essentialInfo;

    /**
     * @return the essentialInfo
     */
    public List<ItineraryLeg> getEssentialInfo() {
        return essentialInfo;
    }

    /**
     * @param essentialInfo the essentialInfo to set
     */
    public void setEssentialInfo(List<ItineraryLeg> essentialInfo) {
        this.essentialInfo = essentialInfo;
    }
}
