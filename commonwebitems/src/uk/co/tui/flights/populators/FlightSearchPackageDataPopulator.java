/**
 *
 */
package uk.co.tui.flights.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.tui.book.domain.lite.FlightSearchPackage;
import uk.co.tui.flights.web.view.data.ItineraryViewData;


/**
 * @author harivardhan.a
 *
 */
public class FlightSearchPackageDataPopulator implements Populator<FlightSearchPackage, ItineraryViewData>
{


	@Override
	public void populate(final FlightSearchPackage source, final ItineraryViewData target) throws ConversionException
	{

		target.setTotoalcount(source.getTotoalcount());
		target.setFlightDepPt(source.getFlightDepPt());
		target.setFlightArrPt(source.getFlightArrPt());
		target.setOutSlots(source.getOutSlots());
		target.setInSlots(source.getInSlots());
		target.setPrice(source.getPrice());
		target.setPricePerPerson(source.getPricePerPerson());

		//target.setDreamLiner(source.isDreamliner());
	}

}
