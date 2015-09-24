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
package uk.co.portaltech.tui.flights.facades;


import java.util.List;
import java.util.Map;

import uk.co.tui.exception.FlightsBusinessException;
import uk.co.tui.flights.data.Airport;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.web.view.data.FlightItemsViewData;
import uk.co.tui.flights.web.view.data.FlightStatusViewData;





/**
 * This interface delegates request from controller to service
 *
 * @author lokesh.k
 */
public interface FlightStatusFacade
{
   FlightStatusViewData searchByFlightnumber(String flightNumber) throws FlightsBusinessException;

   FlightStatusViewData searchByDepartureAirport(String depAirPortCode) throws FlightsBusinessException;

   FlightStatusViewData searchByArrivalAirport(String arrAirportCode) throws FlightsBusinessException;

   FlightStatusViewData fetchAllDepartures() throws FlightsBusinessException;

   FlightStatusViewData fetchAllArrivals() throws FlightsBusinessException;

   Map<String, List<AirportData>> getDepartureAutoSuggestData(final String depAirPortCode, final String arrAirPortCode,
         final String flightNumber) throws FlightsBusinessException;

   Map<String, List<AirportData>> getArrivalAutoSuggestData(final String depAirPortCode, final String arrAirPortCode,
         final String flightNumber) throws FlightsBusinessException;

   FlightStatusViewData searchByMultipleFields(String flightNumber, String depAirPortCode, String arrAirportCode, String evntType)
         throws FlightsBusinessException;

   Map<String, Airport> getAirportData() throws FlightsBusinessException;

   FlightItemsViewData searchByFlightNumber(final String flightNumber, final String depAirPortKey, final String arrAirportKey,
         final String evntType) throws FlightsBusinessException;

   Map<String, List<AirportData>> airportsAutoSuggest(String flightNumber, String depAirPortKey, String arrAirportKey,
         String event) throws FlightsBusinessException;

}
