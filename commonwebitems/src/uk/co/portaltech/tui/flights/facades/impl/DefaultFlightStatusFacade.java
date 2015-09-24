/* *************************************************************************
 * Copyright (C)2014 TUI UK Ltd															*
 * TUI UK Ltd,																					*
 * Columbus House, 																			*
 * Westwood Way,																				*
 * Westwood Business Park, 																*
 * Coventry,																					*
 * United Kingdom 																			*
 * CV4 8TT																						*
 * Telephone -																					*
 * All rights reserved - The copyright notice above does not evidence 		*
 * any actual or intended publication of this source code. 						*
 * RCSfile: FlightArrivalAutoComparatorTest.java									*
 * Revision:																					*
 * Date:																							*
 * Author: lokesh.k																			*
 * Log:																							*
 *	*************************************************************************/
package uk.co.portaltech.tui.flights.facades.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.flights.facades.FlightStatusFacade;
import uk.co.tui.exception.FlightsBusinessException;
import uk.co.tui.flights.data.Airport;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.decorator.FlightStatusViewDataDecorator;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.service.FlightStatusService;
import uk.co.tui.flights.web.view.data.FlightItemsViewData;
import uk.co.tui.flights.web.view.data.FlightStatusViewData;
import uk.co.tui.services.flightstatus.pojo.FlightStatusDetails;



/**
 * This class redirects request to service class
 *
 * @author lokesh.k
 */
public class DefaultFlightStatusFacade implements FlightStatusFacade
{

	@Resource
	private FlightStatusService flightStatusService;

	@Resource
	private FlightStatusViewDataDecorator flightStatusViewDataDecorator;

	private static final String NODATA = "nodata";
	private static final String ERROR_CODE = "6012";
	private static final String ARRIVING_TO = "arriveTo";
	private static final String DEPARTURE_FROM = "departFrom";
	private static final String FLIGHT_NUMBER = "flightnumber";



	private List<FlightStatusDetails> listOfFlights;

	@Override
	public FlightStatusViewData searchByFlightnumber(final String flightNumber) throws FlightsBusinessException
	{
		try
		{
			listOfFlights = flightStatusService.searchByFlightnumber(flightNumber);
			return flightStatusViewDataDecorator.decorateSearchByFlightNo(getAirportData(), listOfFlights, FLIGHT_NUMBER);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
		catch (final ParseException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}

	@Override
	public FlightStatusViewData searchByDepartureAirport(final String depAirPortCode) throws FlightsBusinessException
	{
		try
		{
			listOfFlights = flightStatusService.searchByDepartureAirport(depAirPortCode);
			return flightStatusViewDataDecorator.decorateSearchByAirports(getAirportData(), listOfFlights, DEPARTURE_FROM,
					DEPARTURE_FROM);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}

	@Override
	public FlightStatusViewData searchByArrivalAirport(final String arrAirportCode) throws FlightsBusinessException
	{
		try
		{
			listOfFlights = flightStatusService.searchByArrivalAirport(arrAirportCode);
			return flightStatusViewDataDecorator.decorateSearchByAirports(getAirportData(), listOfFlights, ARRIVING_TO, ARRIVING_TO);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}


	@Override
	public FlightStatusViewData fetchAllDepartures() throws FlightsBusinessException
	{
		try
		{
			listOfFlights = flightStatusService.fetchAllDepartures();
			return flightStatusViewDataDecorator.decorateAllDepartures(getAirportData(), listOfFlights);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}

	@Override
	public FlightStatusViewData fetchAllArrivals() throws FlightsBusinessException
	{
		try
		{
			listOfFlights = flightStatusService.fetchAllArrivals();
			return flightStatusViewDataDecorator.decorateAllArrivals(getAirportData(), listOfFlights);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}


	@Override
	public Map<String, List<AirportData>> getDepartureAutoSuggestData(final String depAirPortCode, final String arrAirPortCode,
			final String flightNumber) throws FlightsBusinessException
	{

		Map<String, List<AirportData>> airportMap = new HashMap<String, List<AirportData>>();
		try
		{
			listOfFlights = flightStatusService.searchgetByDepartureAutoSuggestData(depAirPortCode, arrAirPortCode, flightNumber);
			if (CollectionUtils.isNotEmpty(listOfFlights))
			{
				airportMap = flightStatusViewDataDecorator.getAutoSuggestedDecorator(listOfFlights, "departure", getAirportData());
			}


		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
		return airportMap;
	}

	@Override
	public Map<String, List<AirportData>> getArrivalAutoSuggestData(final String depAirPortCode, final String arrAirPortCode,
			final String flightNumber) throws FlightsBusinessException
	{
		Map<String, List<AirportData>> airportMap = new HashMap<String, List<AirportData>>();
		try
		{
			listOfFlights = flightStatusService.searchByArrivalAutoSuggestData(depAirPortCode, arrAirPortCode, flightNumber);
			airportMap = flightStatusViewDataDecorator.getAutoSuggestedDecorator(listOfFlights, "arrival", getAirportData());
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
		return airportMap;
	}

	@Override
	public FlightStatusViewData searchByMultipleFields(final String flightNumber, final String depAirPortKey,
			final String arrAirportKey, final String evntType) throws FlightsBusinessException
	{

		final String tmpFlightNumber = getUpdateAirportCode(flightNumber);
		final String tmpDepAirPortCode = getUpdateAirportCode(depAirPortKey);
		final String tmpArrAirportCode = getUpdateAirportCode(arrAirportKey);

		try
		{
			listOfFlights = flightStatusService.searchByMultipleFields(tmpFlightNumber, tmpDepAirPortCode, tmpArrAirportCode,
					evntType);
			return flightStatusViewDataDecorator.decorateSearchByMultipleFields(getAirportData(), listOfFlights, DEPARTURE_FROM);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}

	@Override
	public FlightItemsViewData searchByFlightNumber(final String flightNumber, final String depAirPortKey,
			final String arrAirportKey, final String evntType) throws FlightsBusinessException
	{

		final String tmpFlightNumber = validateFlightVariables(flightNumber);
		final String tmpDepAirPortCode = validateFlightVariables(depAirPortKey);
		final String tmpArrAirportCode = validateFlightVariables(arrAirportKey);

		final String carriercode = getCarrierName(tmpFlightNumber);

		try
		{

			if (!StringUtils.isBlank(carriercode))
			{

				listOfFlights = flightStatusService.searchByMultipleFields(tmpFlightNumber, tmpDepAirPortCode, tmpArrAirportCode,
						evntType);

				return flightStatusViewDataDecorator.decorateSearchByFlightNumbers(listOfFlights);
			}
			else
			{

				throw new FlightsBusinessException(ERROR_CODE);
			}

		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}

	@Override
	public Map<String, List<AirportData>> airportsAutoSuggest(final String flightNumber, final String depAirPortKey,
			final String arrAirportKey, final String event) throws FlightsBusinessException
	{

		final String tmpFlightNumber = validateFlightVariables(flightNumber);
		final String tmpDepAirPortCode = validateFlightVariables(depAirPortKey);
		final String tmpArrAirportCode = validateFlightVariables(arrAirportKey);
		Map<String, List<AirportData>> airportMap = new HashMap<String, List<AirportData>>();

		try
		{
			listOfFlights = flightStatusService.searchByAirportAutoSuggestFields(tmpFlightNumber, tmpDepAirPortCode,
					tmpArrAirportCode, event);

			if (CollectionUtils.isNotEmpty(listOfFlights))
			{

				airportMap = flightStatusViewDataDecorator.getAutoSuggestedDecorator(listOfFlights, event, getAirportData());
			}
			else
			{
				airportMap.put("ukAirports", new ArrayList<AirportData>());
				airportMap.put("osAirports", new ArrayList<AirportData>());
			}
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
		return airportMap;
	}

	@Override
	public Map<String, Airport> getAirportData() throws FlightsBusinessException
	{
		try
		{
			return flightStatusService.getAirportData();
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException(ERROR_CODE, e);
		}
	}

	/**
	 * @param code
	 * @return
	 */
	private String getUpdateAirportCode(final String code)
	{
		String tmpCode = null;

		if (!NODATA.equalsIgnoreCase(code))
		{
			tmpCode = code;
		}
		return tmpCode;
	}

	/**
	 * @param code
	 * @return
	 */
	private String validateFlightVariables(final String code)
	{
		String tmpCode = null;

		if (StringUtils.isNotEmpty(code))
		{
			tmpCode = code;
		}

		return tmpCode;
	}

	private String getCarrierName(final String code)
	{
		if (StringUtils.isBlank(code))
		{
			return null;
		}

		return code.replaceAll("[0-9]+$", "");

	}


}
