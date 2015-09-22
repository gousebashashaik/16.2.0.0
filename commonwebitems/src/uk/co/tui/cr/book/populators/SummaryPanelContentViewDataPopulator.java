/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.SummaryPanelStaticContentViewData;

/**
 * Note : Currently there is no valid source to pass as parameter, hence passing
 * it as Object The Class SummaryPanelContentViewDataPopulator.
 *
 * @author thyagaraju.e
 */
public class SummaryPanelContentViewDataPopulator
        implements
            Populator<Object, PackageViewData> {

    /** The static page content service. */
    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /**
     * Populate summary content.
     *
     * @param viewData
     *            the package view data
     */
    private void populateSummaryContent(final PackageViewData viewData) {
        final SummaryPanelStaticContentViewData summaryViewData = new SummaryPanelStaticContentViewData();
        summaryViewData.setSummaryContentMap(staticContentServ
                .getSummaryContents());
        viewData.setSummaryPanelStaticContentViewData(summaryViewData);

    }

    /**
     * Calls the summary content population
     */
    @Override
    public void populate(final Object source, final PackageViewData target)
            throws ConversionException {
        populateSummaryContent(target);

    }

}
