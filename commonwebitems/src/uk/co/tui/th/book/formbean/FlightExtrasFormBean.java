/**
 *
 */
package uk.co.tui.th.book.formbean;


/**
 * The class to hold the selected flightExtras form data.
 *
 * @author madhumathi.m
 *
 */
public class FlightExtrasFormBean {

    /** String to hold the ExtraFacilityCategory code. */
    private String extraCategoryCode;

    /** String to hold ExtraFacilityCode. */
    private String extrafacilityCode;

    /** String to hold the selected passenger ID. */
    private String passengerId;

    /**
     * @return the extraCategoryCode
     */
    public String getExtraCategoryCode() {
        return extraCategoryCode;
    }

    /**
     * @param extraCategoryCode the extraCategoryCode to set
     */
    public void setExtraCategoryCode(String extraCategoryCode) {
        this.extraCategoryCode = extraCategoryCode;
    }

    /**
     * @return the extrafacilityCode
     */
    public String getExtrafacilityCode() {
        return extrafacilityCode;
    }

    /**
     * @param extrafacilityCode the extrafacilityCode to set
     */
    public void setExtrafacilityCode(String extrafacilityCode) {
        this.extrafacilityCode = extrafacilityCode;
    }

    /**
     * @return the passengerId
     */
    public String getPassengerId() {
        return passengerId;
    }

    /**
     * @param passengerId the passengerId to set
     */
    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

}
