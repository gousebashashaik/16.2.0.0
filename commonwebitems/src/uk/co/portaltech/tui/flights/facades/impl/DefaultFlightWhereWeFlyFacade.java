/**
 *
 */
package uk.co.portaltech.tui.flights.facades.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.tui.flights.facades.FlightWhereWeFlyFacade;
import uk.co.tui.exception.FlightsBusinessException;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.pojo.SeasonsData;
import uk.co.tui.flights.service.FlightWhereWeFlyService;


/**
 * @author vijaya.putheti
 *
 */
public class DefaultFlightWhereWeFlyFacade implements FlightWhereWeFlyFacade
{

	@Resource
	private FlightWhereWeFlyService flightWhereWeFlyService;

	@Override
	public AbstractPageModel getTabDetailsForContinents(final String subPageType) throws FlightsBusinessException
	{
		AbstractPageModel pageModel;
		try
		{
			pageModel = flightWhereWeFlyService.getTabDetailsForContinents(subPageType);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException("6004", e);
		}
		return pageModel;
	}


	@Override
	public void setSeasonFlightAvailability(final List<String> flightDepartureDates, final List<SeasonsData> seasons)
			throws ParseException
	{


		flightWhereWeFlyService.setSeasonFlightAvailability(flightDepartureDates, seasons);

	}




}
