/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package uk.co.portaltech.tui.flights.facades.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import uk.co.portaltech.tui.flights.facades.FlightsMapFacade;
import uk.co.tui.exception.FlightsBusinessException;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.WhereWeFlyData;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.service.FlightsMapService;


/**
 * This method invokes the respective service method based on request
 *
 */
public class DefaultFlightsMapFacade implements FlightsMapFacade
{
	@Resource
	private FlightsMapService flightsMapService;

	@Resource
	private DefaultFlightSearchPanelFacade flightSearchPanelFacade;









	@Override
	public Map<String, List<AirportData>> getDepartingFromAirports() throws FlightsBusinessException
	{
		Map<String, List<AirportData>> departingAirportsData;
		try
		{
			departingAirportsData = flightSearchPanelFacade.fetchAllAirports();
		}
		catch (final SearchResultsBusinessException e)
		{
			throw new FlightsBusinessException("6004", e);
		}
		final Map<String, List<AirportData>> departingAirports = new TreeMap<String, List<AirportData>>();
		for (final Entry<String, List<AirportData>> destinations : departingAirportsData.entrySet())
		{
			departingAirports.put(destinations.getValue().get(0).getCountryName(), destinations.getValue());
		}
		return departingAirports;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.flights.facades.FlightsMapFacade#getWhereWeFlyData()
	 */
	@Override
	public WhereWeFlyData getWhereWeFlyData() throws FlightsServiceException
	{
		return flightsMapService.getWhereWeFlyDestinations();
	}
}
