/**
 *
 */
package uk.co.portaltech.tui.flights.facades;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import java.text.ParseException;
import java.util.List;

import uk.co.tui.exception.FlightsBusinessException;
import uk.co.tui.flights.pojo.SeasonsData;


/**
 * @author vijaya.putheti
 *
 */
public interface FlightWhereWeFlyFacade
{

	AbstractPageModel getTabDetailsForContinents(final String subPageType) throws FlightsBusinessException;

	void setSeasonFlightAvailability(List<String> flightDepartureDates, List<SeasonsData> seasons) throws ParseException;

}
