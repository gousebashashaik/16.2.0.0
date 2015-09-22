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
 * $RCSfile: DefaultFlightSearchResultsFacade.java$
 *
 * $Revision: $
 *
 * $Date: Jan 14, 2015$
 *
 *
 *
 * $Log: $
 */
package uk.co.portaltech.tui.flights.facades.impl;


import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.tui.flights.facades.FlightSearchResultsFacade;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.decorator.FlightSearchResultDecorator;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.service.FlightSearchService;
import uk.co.tui.flights.url.builder.FlightsS2SearchUrlBuilder;
import uk.co.tui.flights.web.view.data.DreamLinerTooltipViewData;
import uk.co.tui.flights.web.view.data.ItineraryViewData;


/**
 * This class used for flight search results page
 *
 */
public class DefaultFlightSearchResultsFacade implements FlightSearchResultsFacade
{

   private static final TUILogUtils LOGGER = new TUILogUtils("DefaultFlightSearchResultsFacade");

   @Resource
   private FlightSearchService flightSearchService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private SessionService sessionService;

   @Resource
   private FlightsS2SearchUrlBuilder flightsS2SearchUrlBuilder;

   @Resource
   private FlightSearchResultDecorator flightSearchResultDecorator;

   @Resource
   private Populator<List<Itinerary>, ItineraryViewData> itineraryViewDataPopulator;

   @Resource
   private Populator<List<ContentValueModel>, DreamLinerTooltipViewData> dreamLinerToolTipDataPopulator;

   private static final String DREAMLINER_CODE = "970853";

   private static final String FLIGHT_SEARCH_RESULTS = "flightsearchresults";


   /**
    * This method gets the search results
    *
    *
    * @param flightSearchCriteria
    *
    * @return flightSearchResults
    */
   @Override
   public ItineraryViewData searchFlights(final FlightSearchCriteria flightSearchCriteria, final String seasonEndDate)
         throws SearchResultsBusinessException
   {

      final ItineraryViewData itineraryViewData = new ItineraryViewData();
      try
      {
         final List<Itinerary> itinerarieslist = flightSearchService.getFlightSearchResults(flightSearchCriteria);
         addToSession(flightSearchCriteria, itinerarieslist);

         itineraryViewDataPopulator.populate(itinerarieslist, itineraryViewData);
         final DreamLinerTooltipViewData dreamlinerData = dreamLinerdata(DREAMLINER_CODE);
         flightSearchResultDecorator.decorateFlightSearchResults(itineraryViewData, flightSearchCriteria, dreamlinerData,
               seasonEndDate, cmsSiteService.getCurrentCatalogVersion());

      }
      catch (final FlightsServiceException e)
      {
         throw new SearchResultsBusinessException("6002",e);
      }
      return itineraryViewData;
   }

   /**
    * @param flightSearchCriteria
    * @return String
    *
    */
   @Override
   public String getS2Url(final FlightSearchCriteria flightSearchCriteria) throws SearchResultsBusinessException
   {
      return flightsS2SearchUrlBuilder.buildS2Url(flightSearchCriteria);
   }

   /**
    * @param code
    * @return DreamLinerTooltipViewData
    *
    */
   @Override
   public DreamLinerTooltipViewData dreamLinerdata(final String code)
   {
      final DreamLinerTooltipViewData dreamlinerData = new DreamLinerTooltipViewData();

      final List<ContentValueModel> contentValues = flightSearchService.getDreamLinerData(code);
      dreamLinerToolTipDataPopulator.populate(contentValues, dreamlinerData);

      return dreamlinerData;
   }

   /**
    * @param airportCode
    * @return AirportData
    *
    */
   @Override
   public AirportData getAirportDetailsForCode(final String airportCode) throws SearchResultsBusinessException
   {
      AirportData airportData;
      try
      {
         airportData = flightSearchService.getAirportDataForCode(airportCode, cmsSiteService.getCurrentCatalogVersion());
      }
      catch (final FlightsServiceException e)
      {
         throw new SearchResultsBusinessException("6002",e);
      }

      return airportData;
   }

   /**
    * @param flightSearchCriteria
    */
   @Override
   public boolean isTracsSearch(final FlightSearchCriteria flightSearchCriteria)
   {
      return flightSearchService.isTracsSearch(flightSearchCriteria);

   }

   /**
    * @param searchCriteria
    * @param flightSearchResultsList
    */
   private void addToSession(final FlightSearchCriteria searchCriteria, final List<Itinerary> flightSearchResultsList)
   {
      if (StringUtils.isNotEmpty(searchCriteria.getIsPriceGrid()) && ("Y").equalsIgnoreCase(searchCriteria.getIsPriceGrid()))
      {
         appendResultsInSession(flightSearchResultsList);
      }
      else
      {

         sessionService.setAttribute(FLIGHT_SEARCH_RESULTS, flightSearchResultsList);
      }
   }

   /**
    * @param flightSearchResultsList
    */
   private void appendResultsInSession(final List<Itinerary> flightSearchResultsList)
   {
      final List<Itinerary> resultsInSession = sessionService.getAttribute(FLIGHT_SEARCH_RESULTS);
      final List<Itinerary> results = new ArrayList<Itinerary>();
      if (org.apache.commons.collections.CollectionUtils.isNotEmpty(resultsInSession))
      {
         results.addAll(flightSearchResultsList);
         results.addAll(resultsInSession);
         sessionService.setAttribute(FLIGHT_SEARCH_RESULTS, results);
      }
      else
      {
         LOGGER.warn("No initial serach results in the session may redirect to tech difficulties while book integration");
         sessionService.setAttribute(FLIGHT_SEARCH_RESULTS, flightSearchResultsList);
      }
   }


}
