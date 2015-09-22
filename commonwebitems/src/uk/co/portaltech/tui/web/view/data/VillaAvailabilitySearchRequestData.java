/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author extvk6
 *
 */
public class VillaAvailabilitySearchRequestData extends SearchRequestData {

    private int adults;

    private int children;

    private String startDate;

    private String endDate;

    private int stay;

    /**
     * @return the adults
     */
    public int getAdults() {
        return adults;
    }

    /**
     * @param adults the adults to set
     */
    public void setAdults(int adults) {
        this.adults = adults;
    }

    /**
     * @return the children
     */
    public int getChildren() {
        return children;
    }

     /**
     * @param children the children to set
     */
    public void setChildren(int children) {
        this.children = children;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the stay
     */
    public int getStay() {
        return stay;
    }

    /**
     * @param stay the stay to set
     */
    public void setStay(int stay) {
        this.stay = stay;
    }



}
