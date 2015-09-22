/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

public class SingleAccomSearchResultViewData
{
    private boolean multipleDates;

    private boolean multipleAirports;

    private boolean multipleFilghts;

    private String wireframeType;

    /**
     * @return the multipleDates
     */
    public boolean isMultipleDates() {
        return multipleDates;
    }

    /**
     * @param multipleDates the multipleDates to set
     */
    public void setMultipleDates(boolean multipleDates) {
        this.multipleDates = multipleDates;
    }

    /**
     * @return the multipleAirports
     */
    public boolean isMultipleAirports() {
        return multipleAirports;
    }

    /**
     * @param multipleAirports the multipleAirports to set
     */
    public void setMultipleAirports(boolean multipleAirports) {
        this.multipleAirports = multipleAirports;
    }

    /**
     * @return the multipleFilghts
     */
    public boolean isMultipleFilghts() {
        return multipleFilghts;
    }

    /**
     * @param multipleFilghts the multipleFilghts to set
     */
    public void setMultipleFilghts(boolean multipleFilghts) {
        this.multipleFilghts = multipleFilghts;
    }

    /**
     * @return the wireframeType
     */
    public String getWireframeType() {
        return wireframeType;
    }

    /**
     * @param wireframeType the wireframeType to set
     */
    public void setWireframeType(String wireframeType) {
        this.wireframeType = wireframeType;
    }

}
