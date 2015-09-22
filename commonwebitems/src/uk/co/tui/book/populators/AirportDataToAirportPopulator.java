/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.tui.book.domain.Airport;

/**
 * The Class AirportDataToAirportPopulator.
 *
 * @author samantha.gd
 */
public class AirportDataToAirportPopulator implements
        Populator<AirportData, Airport> {

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(AirportData airportData, Airport airport)
            throws ConversionException {
        airport.setChildren(airportData.getChildren());
        airport.setCode(airportData.getId());
        airport.setGroup(airportData.getGroups());
        airport.setName(airportData.getName());
        airport.setSynonym(airportData.getSynonym());
    }

}
