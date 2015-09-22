/**
 *
 */
package uk.co.tui.fj.book.view.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author akshay.an
 *
 */
public class AgeProfile {

    /** The age range. */
    private String ageRange;

    /** The age code. */
    private String ageCode;

    /** The selected. */
    private String selected = StringUtils.EMPTY;

    /**
     * @return the ageRange
     */
    public String getAgeRange() {
        return ageRange;
    }

    /**
     * @param ageRange the ageRange to set
     */
    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
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
     * @return the selected
     */
    public String getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(String selected) {
        this.selected = selected;
    }

}
