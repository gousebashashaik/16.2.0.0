/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.view.data.FlightOptionsStaticContentViewData;
import uk.co.tui.book.view.data.GenericStaticContentViewData;


/**
 * The Class FlightStaticContentViewDataPopulator.
 *
 * @author thyagaraju.e
 */
public class FlightStaticContentViewDataPopulator implements Populator<Object, FlightOptionsStaticContentViewData> {

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    @Resource
    private PageContentViewDataPopulator pageContentViewDataPopulator;

    /**
     * Populate flight content.
     *
     * @param flightViewData the flight view data
     */
    private void populateFlightContent(final FlightOptionsStaticContentViewData flightViewData)
    {
        final GenericStaticContentViewData genericViewData = new GenericStaticContentViewData();

        flightViewData.setFlightContentMap(staticContentServ
                    .getFlightContents());

        pageContentViewDataPopulator.populateGenericContent(genericViewData);

        flightViewData.setGenericContentViewData(genericViewData);
    }

    /**
     * Calls the population of flight content
     */
    @Override
    public void populate(final Object source,
            final FlightOptionsStaticContentViewData target)
            throws ConversionException {
        populateFlightContent(target);

    }

}
