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
 * $RCSfile: FlightSearchResultsFacade.java$
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

import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.web.view.data.DreamLinerTooltipViewData;
import uk.co.tui.flights.web.view.data.ItineraryViewData;


/**
 *
 *
 */
public interface FlightSearchResultsFacade
{
   /**
    * @param flightSearchCriteria
    * @param seasonEndDate
    */
   ItineraryViewData searchFlights(final FlightSearchCriteria flightSearchCriteria,final String seasonEndDate) throws SearchResultsBusinessException;

   /**
    * @param flightSearchCriteria
    *
    */
   String getS2Url(final FlightSearchCriteria flightSearchCriteria) throws SearchResultsBusinessException;

   /**
    * @param flightSearchCriteria
    *
    */
   boolean isTracsSearch(final FlightSearchCriteria flightSearchCriteria);

   /**
    * @param code
    *
    */
   DreamLinerTooltipViewData dreamLinerdata(final String code);

   /**
    * @param airportCode
    *
    */
   AirportData getAirportDetailsForCode(final String airportCode) throws SearchResultsBusinessException;

}
