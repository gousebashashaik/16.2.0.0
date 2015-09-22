/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.view.data.GenericStaticContentViewData;
import uk.co.tui.book.view.data.PassengerDetailsStaticContentViewData;

/**
 * @author thyagaraju.e
 *
 */
public class PassengerStaticContentViewDataPopulator implements Populator<Object, PassengerDetailsStaticContentViewData>{

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /** The page content view data populator. */
    @Resource
    private PageContentViewDataPopulator pageContentViewDataPopulator;

    /**
     * Populate passenger content.
     *
     * @param passengerViewData the passenger view data
     */
    private void populatePassengerContent(final PassengerDetailsStaticContentViewData passengerViewData)
    {
        final GenericStaticContentViewData genericViewData = new GenericStaticContentViewData();
        passengerViewData.setPassengerContentMap(staticContentServ
                    .getPassengerContents());
        pageContentViewDataPopulator.populateGenericContent(genericViewData);
        passengerViewData.setGenericContentViewData(genericViewData);
    }

    /**
     * Calls the passenger content population
     */
    @Override
    public void populate(final Object source,
            final PassengerDetailsStaticContentViewData target)
            throws ConversionException {
        populatePassengerContent(target);

    }

}
