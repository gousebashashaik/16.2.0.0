/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.manage.viewdata.ManageBookingSummaryPageStaticContentViewData;


/**
 * @author veena.pn
 *
 */
public class BookingSummaryPageStaticContentViewDataPopulator implements
        Populator<Object, ManageBookingSummaryPageStaticContentViewData>
{


    /** The static page content service. */
    @Resource
    private DefaultStaticContentWrapper staticContentServ;



    /**
     * Populate booking summary content.
     *
     * @param Manage
     *           Booking Summary Page Static Content View Data
     */
    private void populateBookingSummaryPageContent(final ManageBookingSummaryPageStaticContentViewData pageViewData)
    {

        pageViewData.setManageBookingSummaryContentMap(staticContentServ.getBookingSummaryPageContents());



    }


    /**
     * Calls the booking summary content population
     */
    @Override
    public void populate(final Object source, final ManageBookingSummaryPageStaticContentViewData target)
            throws ConversionException
    {
        populateBookingSummaryPageContent(target);

    }


}
