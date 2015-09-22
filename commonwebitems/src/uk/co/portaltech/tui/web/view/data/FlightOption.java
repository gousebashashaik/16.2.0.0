/**
 *
 */
package uk.co.portaltech.tui.web.view.data;


/**
 * @author Manju.ts
 *
 */
public class FlightOption {

    private FilterRequest inbound;
    private FilterRequest outbound;
    private FilterRequest departurePoints;
    /**
     * @return the inbound
     */
    public FilterRequest getInbound() {
        return inbound;
    }
    /**
     * @param inbound the inbound to set
     */
    public void setInbound(FilterRequest inbound) {
        this.inbound = inbound;
    }
    /**
     * @return the outbound
     */
    public FilterRequest getOutbound() {
        return outbound;
    }
    /**
     * @param outbound the outbound to set
     */
    public void setOutbound(FilterRequest outbound) {
        this.outbound = outbound;
    }
    /**
     * @return the departurePoints
     */
    public FilterRequest getDeparturePoints() {
        return departurePoints;
    }
    /**
     * @param departurePoints the departurePoints to set
     */
    public void setDeparturePoints(FilterRequest departurePoints) {
        this.departurePoints = departurePoints;
    }



}
