/**
 *
 */
package uk.co.tui.fo.book.facade;

import java.text.ParseException;

import uk.co.tui.fo.book.formbean.InsuranceDetailsCriteria;
import uk.co.tui.fo.book.view.data.ExtraOptionsViewData;
import uk.co.tui.fo.book.view.data.InsuranceContainerViewData;

/**
 * @author akshay.an
 *
 */
public interface InsuranceFacade {

    /**
     * Render insurance extras.
     *
     * @throws ParseException
     */
    void renderInsuranceExtras(
            InsuranceContainerViewData insuranceContainerViewData)
            throws ParseException;

    /**
     * Adds the insurance.
     *
     * @param extraOptionsViewData
     *            the extra options view data
     */
    void addInsurance(InsuranceDetailsCriteria insuranceDetailsCriteria,
            ExtraOptionsViewData extraOptionsViewData);

    /**
     * Removes the insurance.
     *
     * @param extraOptionsViewData
     *            the extra options view data
     * @param insuranceCode
     * @return true, if successful
     */
    boolean removeInsurance(final String insuranceCode,
            ExtraOptionsViewData extraOptionsViewData);

    /**
     * Select insurance.
     *
     * @param extraOptionsViewData
     *            the extra options view data
     */
    void selectInsurance(InsuranceDetailsCriteria insuranceDetailsCriteria,
            ExtraOptionsViewData extraOptionsViewData);
}
