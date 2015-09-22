/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.manage.viewdata.PackageViewData;
import uk.co.tui.manage.viewdata.SummaryPanelStaticContentViewData;


/**
 * @author gopinath.n
 */
public class CostofHolidaySummaryPanelContentViewDataPopulator implements Populator<PackageHoliday, PackageViewData>
{

    /** The static page content service. */
    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /**
     * Populate summary content.
     *
     * @param viewData
     *           the package view data
     */
    private void populateSummaryContent(final PackageViewData viewData)
    {
        final SummaryPanelStaticContentViewData summaryViewData = new SummaryPanelStaticContentViewData();
        summaryViewData.setSummaryContentMap(staticContentServ.getSummaryContents());
        viewData.setSummaryPanelStaticContentViewData(summaryViewData);

    }

    /**
     * Calls the summary content population
     */
    @Override
    public void populate(final PackageHoliday source, final PackageViewData target) throws ConversionException
    {
        populateSummaryContent(target);

    }

}
