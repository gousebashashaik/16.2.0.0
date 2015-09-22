/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * The Map data wrapper for mobile.
 * This is used to have the map data
 * required for mobile website.
 *
 * @author niranjankumar.d
 *
 */
public class MapDataWrapperForMobile {

    // Get the list of map data for mobile component.
    private List<MapPositionsData>  positions;

    /**
     * @return the positions
     */
    public List<MapPositionsData> getPositions() {
        return positions;
    }

    /**
     * @param positions the positions to set
     */
    public void setPositions(List<MapPositionsData> positions) {
        this.positions = positions;
    }


}
