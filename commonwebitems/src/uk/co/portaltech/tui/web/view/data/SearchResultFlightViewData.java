/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFlightViewData {

    private String departureDate;
    private String departureAirport;
    private String flightText;
    private boolean dreamlinerLogo;
    private List<SearchResultFlightDetailViewData> outbounds = new ArrayList<SearchResultFlightDetailViewData>();
    private List<SearchResultFlightDetailViewData> inbounds  = new ArrayList<SearchResultFlightDetailViewData>();
    private String departureDateForMobile;
    /**
     * @return the departureDate
     */
    public String getDepartureDate() {
        return departureDate;
    }
    /**
     * @return the departureAirport
     */
    public String getDepartureAirport() {
        return departureAirport;
    }
    /**
     * @return the flightText
     */
    public String getFlightText() {
        return flightText;
    }

    /**
     * @return the outbounds
     */
    public List<SearchResultFlightDetailViewData> getOutbounds() {
        return outbounds;
    }
    /**
     * @return the inbounds
     */
    public List<SearchResultFlightDetailViewData> getInbounds() {
        return inbounds;
    }
    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
    /**
     * @param departureAirport the departureAirport to set
     */
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }
    /**
     * @param flightText the flightText to set
     */
    public void setFlightText(String flightText) {
        this.flightText = flightText;
    }

    /**
     * @param outbounds the outbounds to set
     */
    public void setOutbounds(List<SearchResultFlightDetailViewData> outbounds) {
        this.outbounds = outbounds;
    }
    /**
     * @param inbounds the inbounds to set
     */
    public void setInbounds(List<SearchResultFlightDetailViewData> inbounds) {
        this.inbounds = inbounds;
    }
    /**
     * @return the dreamlinerLogo
     */
    public boolean isDreamlinerLogo() {
        return dreamlinerLogo;
    }
    /**
     * @param dreamlinerLogo the dreamlinerLogo to set
     */
    public void setDreamlinerLogo(boolean dreamlinerLogo) {
        this.dreamlinerLogo = dreamlinerLogo;
    }
    /**
     * @return the departureDateForMobile
     */
    public String getDepartureDateForMobile() {
        return departureDateForMobile;
    }
    /**
     * @param departureDateForMobile the departureDateForMobile to set
     */
    public void setDepartureDateForMobile(String departureDateForMobile) {
        this.departureDateForMobile = departureDateForMobile;
    }

}
