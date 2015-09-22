/**
 *
 */
package uk.co.tui.fo.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.fo.book.view.data.ExtraOptionsStaticContentViewData;
import uk.co.tui.fo.book.view.data.GenericStaticContentViewData;
import uk.co.tui.fo.book.view.data.InsuranceStaticContentViewData;


/**
 * Note : Currently there is no valid source to pass as parameter, hence passing
 * it as Object The Class ExtraStaticContentViewDataPopulator.
 *
 * @author thyagaraju.e
 */
public class ExtraStaticContentViewDataPopulator
        implements
            Populator<Object, ExtraOptionsStaticContentViewData> {

    /** The static page content service. */
    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    @Resource(name = "foPageContentViewDataPopulator")
    private PageContentViewDataPopulator pageContentViewDataPopulator;

    /**
     * Populate extra content.
     *
     * @param extraViewData
     *            the extra view data
     */
    private void populateExtraContent(
            final ExtraOptionsStaticContentViewData extraViewData) {
        final GenericStaticContentViewData genericViewData = new GenericStaticContentViewData();
        // final NavigationStaticContentViewData navigationViewData = new

        final InsuranceStaticContentViewData insuranceViewData = new InsuranceStaticContentViewData();

        extraViewData.setExtraContentMap(staticContentServ.getExtraContents());

        pageContentViewDataPopulator.populateGenericContent(genericViewData);

        populateInsuranceContent(insuranceViewData);

        extraViewData.setGenericContentViewData(genericViewData);

        extraViewData.setInsuranceStaticContentViewData(insuranceViewData);
    }

    /**
     * Populate insurance content.
     *
     * @param insuranceViewData
     *            the insurance view data
     */
    private void populateInsuranceContent(
            final InsuranceStaticContentViewData insuranceViewData) {
        insuranceViewData.setInsuranceContentMap(staticContentServ
                .getInsuranceContents());

    }

    /**
     * Calls the Extra content population
     */
    @Override
    public void populate(final Object source,
            final ExtraOptionsStaticContentViewData target)
            throws ConversionException {
        populateExtraContent(target);

    }

}
