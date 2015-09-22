/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.manage.viewdata.ManageHomePageStaticContentViewData;


/**
 * The Class ManageHomePageStaticContentViewDataPopulator.
 *
 * @author veena.pn
 */
public class ManageHomePageStaticContentViewDataPopulator implements Populator<Object, ManageHomePageStaticContentViewData>
{

    /** The static page content service. */
    @Resource
    private DefaultStaticContentWrapper staticContentServ;



    /**
     * Populate extra content.
     *
     * @param manage
     *           home page the manage home page
     */
    private void populateManageHomePageContent(final ManageHomePageStaticContentViewData pageViewData)
    {

        pageViewData.setManageHomePageContentMap(staticContentServ.getManageHomePageContents());



    }


    /**
     * Calls the extras content population
     */
    @Override
    public void populate(final Object source, final ManageHomePageStaticContentViewData target) throws ConversionException
    {
        populateManageHomePageContent(target);

    }

}
