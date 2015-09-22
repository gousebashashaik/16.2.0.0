/**
 *
 */
package uk.co.tui.th.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.tui.th.book.facade.TermsAndConditionsFacade;
import uk.co.tui.th.book.view.data.TermsAndConditionsViewData;

/**
 * @author thyagaraju.e
 *
 */
public class TermsAndConditionsFacadeImpl implements TermsAndConditionsFacade{

    /** The terms and conditions view data populator. */
    @Resource(name="thPrivacyPolicyViewDataPopulator")
    private Populator<Object, TermsAndConditionsViewData> privacyPolicyViewDataPopulator;

    /** The terms and conditions view data populator. */
    @Resource(name="thSpecialNeedsViewDataPopulator")
    private Populator<Object, TermsAndConditionsViewData> specialNeedsViewDataPopulator;

    /**
     * Populates the Privacy Policy content
     */
    @Override
    public void populatePrivacyPolicy(
            TermsAndConditionsViewData termsAndConditionsViewData) {
        privacyPolicyViewDataPopulator.populate(new Object(), termsAndConditionsViewData);
    }

    /**
     * Populates the Special Needs content
     */
    @Override
    public void populateSpecialNeeds(
            TermsAndConditionsViewData termsAndConditionsViewData) {
        specialNeedsViewDataPopulator.populate(new Object(),
                termsAndConditionsViewData);
    }

}
