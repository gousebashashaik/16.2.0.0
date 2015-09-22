/**
 *
 */
package uk.co.tui.fo.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.tui.fo.book.facade.TermsAndConditionsFacade;
import uk.co.tui.fo.book.view.data.TermsAndConditionsViewData;

/**
 * @author thyagaraju.e
 * 
 */
public class TermsAndConditionsFacadeImpl implements TermsAndConditionsFacade {

    /** The terms and conditions view data populator. */
    @Resource
    private Populator<Object, TermsAndConditionsViewData> foPrivacyPolicyViewDataPopulator;

    /** The special needs view data populator. */
    @Resource
    private Populator<Object, TermsAndConditionsViewData> foSpecialNeedsViewDataPopulator;

    /**
     * Populates the Privacy Policy content
     */
    @Override
    public void populatePrivacyPolicy(
            final TermsAndConditionsViewData termsAndConditionsViewData) {
        foPrivacyPolicyViewDataPopulator.populate(new Object(),
                termsAndConditionsViewData);

    }

    /**
     * Populates the Special Needs Content
     */
    @Override
    public void populateSpecialNeeds(
            final TermsAndConditionsViewData termsAndConditionsViewData) {
        foSpecialNeedsViewDataPopulator.populate(new Object(),
                termsAndConditionsViewData);
    }

}
