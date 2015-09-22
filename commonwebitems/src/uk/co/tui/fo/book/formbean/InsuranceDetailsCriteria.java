/**
 *
 */
package uk.co.tui.fo.book.formbean;

import java.util.HashMap;
import java.util.Map;

import uk.co.tui.fo.book.formbean.annotation.ValidateFormat;


/**
 * The Class InsuranceDetailsFormBeans.
 *
 * @author akshay.an
 */
public class InsuranceDetailsCriteria {
    /** insurance max value */
    private static final int INSURANCE_MAX = 25;

    /** The insurance code. */
    @ValidateFormat(max = INSURANCE_MAX)
    private String insuranceCode;

    /** The excess waiver. */
    private boolean excessWaiver;

    /** The selected. */
    private Map<String, String> selected = new HashMap<String, String>();

    /**
     * @return the insuranceCode
     */
    public String getInsuranceCode() {
        return insuranceCode;
    }

    /**
     * @param insuranceCode
     *            the insuranceCode to set
     */
    public void setInsuranceCode(final String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

    /**
     * @return the excessWaiver
     */
    public boolean isExcessWaiver() {
        return excessWaiver;
    }

    /**
     * @param excessWaiver
     *            the excessWaiver to set
     */
    public void setExcessWaiver(final boolean excessWaiver) {
        this.excessWaiver = excessWaiver;
    }

    /**
     * @return the selected
     */
    public Map<String, String> getSelected() {
        return selected;
    }

    /**
     * @param selected
     *            the selected to set
     */
    public void setSelected(final Map<String, String> selected) {
        this.selected = selected;
    }
}
