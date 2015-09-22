/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.tui.cr.book.facade.TermsAndConditionsFacade;
import uk.co.tui.cr.book.view.data.TermsAndConditionsViewData;


/**
 * @author thyagaraju.e
 *
 */
public class TermsAndConditionsFacadeImpl implements TermsAndConditionsFacade
{

    /** The terms and conditions view data populator. */
    @Resource(name = "crPrivacyPolicyViewDataPopulator")
    private Populator<Object, TermsAndConditionsViewData> privacyPolicyViewDataPopulator;

    /** The terms and conditions view data populator. */
    @Resource(name = "crSpecialNeedsViewDataPopulator")
    private Populator<Object, TermsAndConditionsViewData> specialNeedsViewDataPopulator;

    /**
     * Populates the Privacy Policy content
     */
    @Override
    public void populatePrivacyPolicy(final TermsAndConditionsViewData termsAndConditionsViewData)
    {
        privacyPolicyViewDataPopulator.populate(new Object(), termsAndConditionsViewData);
    }

    /**
     * Populates the Special Needs content
     */
    @Override
    public void populateSpecialNeeds(final TermsAndConditionsViewData termsAndConditionsViewData)
    {
        specialNeedsViewDataPopulator.populate(new Object(), termsAndConditionsViewData);
    }

}
