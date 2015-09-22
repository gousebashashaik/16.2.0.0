/**
 *
 */
package uk.co.tui.th.book.facade;

import uk.co.tui.th.book.view.data.TermsAndConditionsViewData;



/**
 * @author thyagaraju.e
 *
 */
public interface TermsAndConditionsFacade {

    /**
     * Populate Privacy Policy.
     *
     * @param termsAndConditionsViewData the terms and conditions view data
     */
    void populatePrivacyPolicy(TermsAndConditionsViewData termsAndConditionsViewData);

    /**
     * Populate special needs.
     *
     * @param termsAndConditionsViewData the terms and conditions view data
     */
    void populateSpecialNeeds(
            TermsAndConditionsViewData termsAndConditionsViewData);

}
