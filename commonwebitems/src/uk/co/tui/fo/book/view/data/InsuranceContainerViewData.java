/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author akshay.an
 *
 */
public class InsuranceContainerViewData {

    /** The ins pas view data. */
    private List<InsurancePassengerViewData> insPasViewData;

    /** The family ins view data. */
    private List<InsuranceViewData> insViewData;

    /** The ins criteria. */
    private List<InsuranceCriteria> insCriteria;

    /** The covered all passenger. */
    private boolean coveredAllPassenger;

    /** The over age message. */
    private String overAgeMessage = StringUtils.EMPTY;


    /**
     * @return the insPasViewData
     */
    public List<InsurancePassengerViewData> getInsPasViewData() {
        return insPasViewData;
    }

    /**
     * @param insPasViewData the insPasViewData to set
     */
    public void setInsPasViewData(List<InsurancePassengerViewData> insPasViewData) {
        this.insPasViewData = insPasViewData;
    }

    /**
     * @return the insViewData
     */
    public List<InsuranceViewData> getInsViewData() {
        return insViewData;
    }

    /**
     * @param insViewData the insViewData to set
     */
    public void setInsViewData(List<InsuranceViewData> insViewData) {
        this.insViewData = insViewData;
    }

    /**
     * @return the insCriteria
     */
    public List<InsuranceCriteria> getInsCriteria() {
        return insCriteria;
    }

    /**
     * @param insCriteria the insCriteria to set
     */
    public void setInsCriteria(List<InsuranceCriteria> insCriteria) {
        this.insCriteria = insCriteria;
    }

    /**
     * @return the coveredAllPassenger
     */
    public boolean isCoveredAllPassenger() {
        return coveredAllPassenger;
    }

    /**
     * @param coveredAllPassenger the coveredAllPassenger to set
     */
    public void setCoveredAllPassenger(boolean coveredAllPassenger) {
        this.coveredAllPassenger = coveredAllPassenger;
    }

    /**
     * @return the overAgeMessage
     */
    public String getOverAgeMessage() {
        return overAgeMessage;
    }

    /**
     * @param overAgeMessage the overAgeMessage to set
     */
    public void setOverAgeMessage(String overAgeMessage) {
        this.overAgeMessage = overAgeMessage;
    }
}
