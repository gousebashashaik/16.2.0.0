/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.view.data.PackageViewData;
import uk.co.tui.book.view.data.SummaryPanelStaticContentViewData;

/**
 * The Class SummaryPanelContentViewDataPopulator.
 *
 * @author thyagaraju.e
 */
public class SummaryPanelContentViewDataPopulator implements Populator<Object, PackageViewData>{

    /** The static page content service. */
    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /**
     * Populate summary content.
     *
     * @param viewdata the viewdata
     */
    private void populateSummaryContent(final PackageViewData viewdata) {

        SummaryPanelStaticContentViewData summaryViewData = new SummaryPanelStaticContentViewData();
        summaryViewData.setSummaryContentMap(staticContentServ
                .getSummaryContents());
        viewdata.setSummaryPanelStaticContentViewData(summaryViewData);

    }

    /**
     * Calls the summary content population
     */
    @Override
    public void populate(Object source, PackageViewData target)
            throws ConversionException {
        populateSummaryContent(target);

    }

}
