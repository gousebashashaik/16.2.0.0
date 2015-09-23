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
package uk.co.tui.flights.web.view.data;

import java.util.List;

import uk.co.tui.flights.pojo.FlightStatusAutoSuggestData;
import uk.co.tui.services.flightstatus.pojo.FlightStatusDetails;


/**
 * This class is used for flight status view data.
 *
 * @author lokesh.k
 *
 */
public class FlightStatusViewData
{

	private List<FlightStatusDetails> todaysFlightStatusData;
	private List<FlightStatusDetails> tomorrowsFlightStatusData;

	private List<FlightStatusAutoSuggestData> departureAutoSuggestData;
	private List<FlightStatusAutoSuggestData> arrivalAutoSuggestData;


	private List<String> flightNumbers;

	private String currentFeedtime;

	/**
	 * @return the todaysFlightStatusData
	 */
	public List<FlightStatusDetails> getTodaysFlightStatusData()
	{
		return todaysFlightStatusData;
	}

	/**
	 * @param todaysFlightStatusData
	 *           the todaysFlightStatusData to set
	 */
	public void setTodaysFlightStatusData(final List<FlightStatusDetails> todaysFlightStatusData)
	{
		this.todaysFlightStatusData = todaysFlightStatusData;
	}

	/**
	 * @return the tomorrowsFlightStatusData
	 */
	public List<FlightStatusDetails> getTomorrowsFlightStatusData()
	{
		return tomorrowsFlightStatusData;
	}

	/**
	 * @param tomorrowsFlightStatusData
	 *           the tomorrowsFlightStatusData to set
	 */
	public void setTomorrowsFlightStatusData(final List<FlightStatusDetails> tomorrowsFlightStatusData)
	{
		this.tomorrowsFlightStatusData = tomorrowsFlightStatusData;
	}

	/**
	 * @return the departureAutoSuggestData
	 */
	public List<FlightStatusAutoSuggestData> getDepartureAutoSuggestData()
	{
		return departureAutoSuggestData;
	}

	/**
	 * @param departureAutoSuggestData
	 *           the departureAutoSuggestData to set
	 */
	public void setDepartureAutoSuggestData(final List<FlightStatusAutoSuggestData> departureAutoSuggestData)
	{
		this.departureAutoSuggestData = departureAutoSuggestData;
	}

	/**
	 * @return the arrivalAutoSuggestData
	 */
	public List<FlightStatusAutoSuggestData> getArrivalAutoSuggestData()
	{
		return arrivalAutoSuggestData;
	}

	/**
	 * @param arrivalAutoSuggestData
	 *           the arrivalAutoSuggestData to set
	 */
	public void setArrivalAutoSuggestData(final List<FlightStatusAutoSuggestData> arrivalAutoSuggestData)
	{
		this.arrivalAutoSuggestData = arrivalAutoSuggestData;
	}

	/**
	 * @return the currentFeedtime
	 */
	public String getCurrentFeedtime()
	{
		return currentFeedtime;
	}

	/**
	 * @param currentFeedtime
	 *           the currentFeedtime to set
	 */
	public void setCurrentFeedtime(final String currentFeedtime)
	{
		this.currentFeedtime = currentFeedtime;
	}

	/**
	 * @return the flightNumbers
	 */
	public List<String> getFlightNumbers()
	{
		return flightNumbers;
	}

	/**
	 * @param flightNumbers
	 *           the flightNumbers to set
	 */
	public void setFlightNumbers(final List<String> flightNumbers)
	{
		this.flightNumbers = flightNumbers;
	}



}
