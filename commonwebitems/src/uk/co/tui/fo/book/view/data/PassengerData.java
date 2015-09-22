/**
 *
 */
package uk.co.tui.fo.book.view.data;


/**
 * @author sreevidhya.r
 *
 */
public class PassengerData {

    /** The lead. */
    private boolean isLead;

    /** The ageCode. */
    private String ageCode;

    /** The min ageRange. */
    private String minAge;

    /** The max ageRange. */
    private String maxAge;

    /**  number Of Passenger In This AgeRange. */
    private String countOfPaxs;

    /**  Number of passengers in this range has selected insurance */
    private String noOfPaxsInsSelected;

    /** Display Text */
    private String displayText;

    /**
     * @return the isLead
     */
    public boolean isLead() {
        return isLead;
    }

    /**
     * @param isLead the isLead to set
     */
    public void setLead(boolean isLead) {
        this.isLead = isLead;
    }

    /**
     * @return the ageCode
     */
    public String getAgeCode() {
        return ageCode;
    }

    /**
     * @param ageCode the ageCode to set
     */
    public void setAgeCode(String ageCode) {
        this.ageCode = ageCode;
    }

    /**
     * @return the minAge
     */
    public String getMinAge() {
        return minAge;
    }

    /**
     * @param minAge the minAge to set
     */
    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    /**
     * @return the maxAge
     */
    public String getMaxAge() {
        return maxAge;
    }

    /**
     * @param maxAge the maxAge to set
     */
    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * @return the countOfPaxs
     */
    public String getCountOfPaxs() {
        return countOfPaxs;
    }

    /**
     * @param countOfPaxs the countOfPaxs to set
     */
    public void setCountOfPaxs(String countOfPaxs) {
        this.countOfPaxs = countOfPaxs;
    }

    /**
     * @return the noOfPaxsInsSelected
     */
    public String getNoOfPaxsInsSelected() {
        return noOfPaxsInsSelected;
    }

    /**
     * @param noOfPaxsInsSelected the noOfPaxsInsSelected to set
     */
    public void setNoOfPaxsInsSelected(String noOfPaxsInsSelected) {
        this.noOfPaxsInsSelected = noOfPaxsInsSelected;
    }

    /**
     * @return the displayText
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * @param displayText the displayText to set
     */
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

}
