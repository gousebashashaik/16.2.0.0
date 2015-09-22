/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.portaltech.travel.model.results.PaxDetail;

/**
 *
 */
public class Occupancy
{

    private int adults;
    private int seniors;
    private int children;
    private List<PaxDetail> paxDetail;
    private int infant;
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
     * @return the seniors
     */
    public int getSeniors() {
        return seniors;
    }
    /**
     * @param seniors the seniors to set
     */
    public void setSeniors(int seniors) {
        this.seniors = seniors;
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
     * @return the paxDetail
     */
    public List<PaxDetail> getPaxDetail() {
        return paxDetail;
    }
    /**
     * @param paxDetail the paxDetail to set
     */
    public void setPaxDetail(List<PaxDetail> paxDetail) {
        this.paxDetail = paxDetail;
    }
    /**
     * @return the infant
     */
    public int getInfant() {
        return infant;
    }
    /**
     * @param infant the infant to set
     */
    public void setInfant(int infant) {
        this.infant = infant;
    }


}
