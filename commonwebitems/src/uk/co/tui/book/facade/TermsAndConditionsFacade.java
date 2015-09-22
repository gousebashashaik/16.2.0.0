/**
 *
 */
package uk.co.tui.book.facade;

import uk.co.tui.book.view.data.TermsAndConditionsViewData;

/**
 * @author thyagaraju.e
 *
 */
public interface TermsAndConditionsFacade {

    /**
     * Populate terms and conditions.
     *
     * @param termsAndConditionsViewData the terms and conditions view data
     */
    void populatePrivacyPolicy(TermsAndConditionsViewData termsAndConditionsViewData);

    /**
     * Populate special needs.
     *
     * @param termsAndConditionsViewData the terms and conditions view data
     */
    void populateSpecialNeeds(TermsAndConditionsViewData termsAndConditionsViewData);

}
