/*
 * Copyright (C)2006 TUI UK Ltd
 *
 * TUI UK Ltd, Columbus House, Westwood Way, Westwood Business Park, Coventry, United Kingdom CV4
 * 8TT
 *
 * Telephone - (024)76282828
 *
 * All rights reserved - The copyright notice above does not evidence any actual or intended
 * publication of this source code.
 *
 * $RCSfile: FlightSearchPanelFacade.java$
 *
 * $Revision: $
 *
 * $Date: Jan 14, 2015$
 *
 *
 *
 * $Log: $
 */
package uk.co.portaltech.tui.flights.facades;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.AirportSearchResult;




/**
 *
 *
 */
public interface FlightSearchPanelFacade
{
   Map<String, List<AirportData>> getDepartingFromAirports(final List<String> arrivingAirportCodes,
         final List<String> departureDates) throws SearchResultsBusinessException;

   SearchPanelComponentModel getSearchPanelComponent();

   <T extends AbstractCMSComponentModel> T getComponent(final String componentUid) throws NoSuchComponentException;

   List<String> getFlightDates(final List<String> airportCodeList, final List<String> unitCodeList,String seasonEndDate)
         throws SearchResultsBusinessException;

   List<String> getAllFlightsDates(String seasonEndDate) throws SearchResultsBusinessException;

   List<String> getReturnFlightDates(final List<String> airportCodeList, final List<String> unitCodeList,
         final String departureDate, String seasonEndDate) throws SearchResultsBusinessException;

   List<String> getAllReturnFlightsDates(final String date, String seasonEndDate) throws SearchResultsBusinessException;

   AirportSearchResult find(final String key, final List<String> arrivals, final List<String> dates)
         throws SearchResultsBusinessException;

   Map<String, List<AirportData>> getArrivalData(final List<String> airports, final List<String> dates)
         throws SearchResultsBusinessException;

   AirportSearchResult findArriving(final String key, final List<String> departures, final List<String> dates)
         throws SearchResultsBusinessException;

   Map<String, List<AirportData>> getWhereWeFlyArrivings(final List<String> airports, final String departureDate,
         final String returnDate) throws SearchResultsBusinessException;

   Map<String, List<AirportData>> fetchAllAirports() throws SearchResultsBusinessException;

   List<String> getAllValidDates(final String departureDate, final int flexibility); 

}
