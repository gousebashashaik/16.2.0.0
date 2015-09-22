/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author narendra.bm
 *
 */
public class PlacesOfInterestViewData {
    private List<AttractionViewData> attractionViewData;

    /**
     * @return the attractionViewData
     */
    public List<AttractionViewData> getAttractionViewData() {
        return attractionViewData;
    }
    /**
     * @param attractionViewData the attractionViewData to set
     */
    public void setAttractionViewData(List<AttractionViewData> attractionViewData) {
        this.attractionViewData = attractionViewData;
    }
}
