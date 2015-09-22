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
 * $RCSfile: DefaultFlightSearchPanelFacade.java$
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

import static uk.co.portaltech.commons.Collections.toList;
import static uk.co.tui.flights.commons.Collections.map;
import static uk.co.tui.flights.commons.DateUtils.toDate;
import static uk.co.tui.flights.data.AirportData.toAirportSearchData;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import uk.co.portaltech.commons.DateRangeProviderUtil;
import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.travel.services.airport.AirportServiceException;
import uk.co.portaltech.travel.services.route.FlightRouteIndexService;
import uk.co.portaltech.travel.services.route.IndexingException;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.flights.facades.FlightSearchPanelFacade;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.commons.Collections.MapFn;
import uk.co.tui.flights.data.Airport;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.AirportSearchResult;
import uk.co.tui.flights.data.SearchError;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.service.FlightSearchService;

/**
 * This class is used for search and browse pages.
 *
 * @author K Vijay Kumar Reddy
 */
public class DefaultFlightSearchPanelFacade implements FlightSearchPanelFacade
{

   private static final TUILogUtils LOGGER = new TUILogUtils("DefaultFlightSearchPanelFacade");

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private FlightSearchService flightSearchService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private FlightRouteIndexService flightRouteIndexService;

   @Resource
   private AirportService airportsService;

   private static final String NO_MATCH_FOUND = "NO_MATCH_FOUND";

   private static final String INVALID_ROUTE_ON = "INVALID_ROUTE_ON";

   private static final String INVALID_ROUTE_TO = "INVALID_ROUTE_TO";

   private static final String SEARCH_PANELCOMPONENT_ID = "COM_FO_SEARCH_PANEL";

   private static final String DATE_FORMAT = "dd-MM-yy";

   private static final String NO_ROUTE_FOUND_TO_SELECTED_DESTINATIONS =
      "No Route Found to selected destinations";

   private static final String NO_ROUTE_FOUND_FOR_SELECTED_DATES =
      "No Route Found for selected Dates";

   private static final String NO_MATCH_FOUND_FOR_THE_SEARCH_KEY =
      "No Match found for the search key";

   private static final String SIMILAR_SPELLINGS = "Similar spellings";

   private static final String NOMATCH = "NOMATCH";

   private static final String DEPARTURE_DATE_FORMAT = "yyyy-MM-dd";

   private static final String ERROR_CODE = "6002";

   private static final MapFn<String, LocalDate> LOCATE_DATE_MAPPER =
      new MapFn<String, LocalDate>()
      {
         @Override
         public LocalDate call(final String date)
         {
            return toDate(date);
         }
      };

   private static final MapFn<uk.co.portaltech.travel.model.airport.Airport, AirportData> AIRPORT_TO_AIRPORT_DATA_MAPPER =
      new MapFn<uk.co.portaltech.travel.model.airport.Airport, AirportData>()
      {
         @Override
         public AirportData call(final uk.co.portaltech.travel.model.airport.Airport input)
         {
            return toAirportSearchData(input);
         }
      };

   /**
    * This method fetches Departure Airport list to be shown in the 'Departing From' airport list
    * overlay
    */
   @Override
   public Map<String, List<AirportData>> getDepartingFromAirports(
      final List<String> arrivingAirportCodes, final List<String> departureDates)
      throws SearchResultsBusinessException
   {

      try
      {
         Map<String, List<AirportData>> departingFromAirportsMap;
         if (checkArrivalsDatesIsEmpty(arrivingAirportCodes, departureDates))
         {
            departingFromAirportsMap =
               flightSearchService.fetchDepartingFromAirports(cmsSiteService
                  .getCurrentCatalogVersion());
         }
         else
         {
            departingFromAirportsMap =
               flightSearchService.fetchDepartingFromAirports(
                  cmsSiteService.getCurrentCatalogVersion(), arrivingAirportCodes,
                  toLocalDates(departureDates));
         }

         return departingFromAirportsMap;
      }
      catch (final FlightsServiceException e)
      {
         throw new SearchResultsBusinessException(ERROR_CODE, e);
      }

   }

   @Override
   public SearchPanelComponentModel getSearchPanelComponent()
   {
      SearchPanelComponentModel component = null;
      try
      {
         component = (SearchPanelComponentModel) getComponent(SEARCH_PANELCOMPONENT_ID);
      }
      catch (final NoSuchComponentException e)
      {
         LOGGER.error("No search panel component", e);
      }
      return component;
   }

   @Override
   public <T extends AbstractCMSComponentModel> T getComponent(final String componentUid)
      throws NoSuchComponentException
   {
      try
      {
         return cmsComponentService.getAbstractCMSComponent(componentUid);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOGGER.error("No such component exception for component id" + componentUid, e);
         throw new NoSuchComponentException(componentUid);
      }
   }

   /**
    * Following method is used to fetch departure dates in the Search Panel according to the data
    * selected in From and To fields
    */
   @Override
   public List<String> getFlightDates(final List<String> airportCodeList,
      final List<String> unitCodeList, final String seasonEndDate)
      throws SearchResultsBusinessException
   {

      try
      {
         final List<String> flightDates =
            flightRouteIndexService.findDepartureFlightDates(airportCodeList, unitCodeList);
         return filterFlightDates(seasonEndDate, flightDates);
      }
      catch (final IndexingException e)
      {
         throw new SearchResultsBusinessException("6002", e);
      }
   }

   /**
    * @param seasonEndDate
    * @param flightDates
    * @return List<String>
    */
   private List<String> filterFlightDates(final String seasonEndDate, final List<String> flightDates)
   {
      final List<String> validFlightDates = new ArrayList<String>();

      for (final String dates : flightDates)
      {
         if (!DateUtils.toDate(dates).isAfter(
            DateUtils.toDate(seasonEndDate, DEPARTURE_DATE_FORMAT)))
         {
            validFlightDates.add(dates);
         }
      }

      return validFlightDates;
   }

   /**
    * This method fetches all departure dates when other fields data is empty
    */
   @Override
   public List<String> getAllFlightsDates(final String seasonEndDate)
      throws SearchResultsBusinessException
   {
      try
      {
         final List<String> flightDates = flightRouteIndexService.findAllDepartureFlightDates();
         return filterFlightDates(seasonEndDate, flightDates);
      }
      catch (final IndexingException e)
      {
         throw new SearchResultsBusinessException(ERROR_CODE, e);
      }

   }

   /**
    * This method fetches all return dates in the search panel according to the data selected in
    * From, To and Departure date selected
    */
   @Override
   public List<String> getReturnFlightDates(final List<String> airportCodeList,
      final List<String> unitCodeList, final String departureDate, final String seasonEndDate)
      throws SearchResultsBusinessException
   {
      try
      {
         final List<String> flightReturnDates =
            flightRouteIndexService.findDepartureFlightDates(airportCodeList, unitCodeList);

         if (flightReturnDates != null && departureDate != null)
         {
            final Iterator<String> allFlightDatesIter = flightReturnDates.iterator();
            checkDates(departureDate, allFlightDatesIter);
         }
         return filterFlightDates(seasonEndDate, flightReturnDates);
      }
      catch (final IndexingException e)
      {
         throw new SearchResultsBusinessException(ERROR_CODE, e);
      }
   }

   /**
    *
    */
   private void checkDates(final String departureDate, final Iterator<String> allFlightDatesIter)
   {
      while (allFlightDatesIter.hasNext())
      {
         if (notFutureReturnDate(departureDate, allFlightDatesIter.next()))
         {
            allFlightDatesIter.remove();
         }
         else
         {
            break;
         }
      }
   }

   private boolean notFutureReturnDate(final String departureDate, final String returnDate)
   {
      final DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
      return formatter.parseDateTime(departureDate).getMillis() >= formatter.parseDateTime(
         returnDate).getMillis();
   }

   /**
    * This method fetches Return Dates when only Departure date is selected in the Search panel
    */
   @Override
   public List<String> getAllReturnFlightsDates(final String date, final String seasonEndDate)
      throws SearchResultsBusinessException
   {
      final List<String> allFlightDates = flightRouteIndexService.findAllDepartureFlightDates();

      if (allFlightDates != null && date != null)
      {
         final Iterator<String> allFlightDatesIter = allFlightDates.iterator();
         checkDates(date, allFlightDatesIter);

      }

      return filterFlightDates(seasonEndDate, allFlightDates);
   }

   /**
    * Following method is used for Auto suggest functionality in Search Panel 'Departing From' field
    * under airport list overlay
    */
   @Override
   public AirportSearchResult find(final String key, final List<String> arrivals,
      final List<String> dates) throws SearchResultsBusinessException
   {
      final AirportSearchResult searchResult = new AirportSearchResult();
      final List<uk.co.portaltech.travel.model.airport.Airport> airports =
         new ArrayList<uk.co.portaltech.travel.model.airport.Airport>();

      try
      {
         processDepartureSearch(key, arrivals, dates, searchResult, airports);
      }
      catch (final AirportServiceException ex)
      {
         LOGGER.error("AirportServiceException", ex);
         searchResult.setError(new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_FOR_THE_SEARCH_KEY));
         searchResult.setAirports(Collections.<AirportData> emptyList());
         return searchResult;
      }

      searchResult.setAirports(getNonUKResultList(searchResult, airports));

      verifyInvalidRoute(searchResult);
      getSorted(searchResult.getAirports());
      return searchResult;
   }

   /**
    * temporary fix Added this method to exclude UK airports and groups from lucene results.
    */
   private List<AirportData> getNonUKResultList(final AirportSearchResult searchResult,
      final List<uk.co.portaltech.travel.model.airport.Airport> airports)
   {
      final String[] ukcode = new String[] { "GBR", "IRL" };
      final List<AirportData> allAirports =
         map(airportsService.sortAirports(airports), AIRPORT_TO_AIRPORT_DATA_MAPPER);
      final List<String> ukAirportList =
         airportsService.getAirportCodesOfCountires(cmsSiteService.getCurrentCatalogVersion(),
            Arrays.asList(ukcode));
      final List<AirportData> nonUkAirportList = new ArrayList<AirportData>();
      ukAirportList.addAll(airportsService.getAllAirportGroupcodes());

      for (final AirportData airportData : allAirports)
      {
         if (!ukAirportList.contains(airportData.getId()))
         {
            nonUkAirportList.add(airportData);
         }
      }

      if (CollectionUtils.isEmpty(nonUkAirportList))
      {
         searchResult.setNomatch(false);
         searchResult.setError(new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_FOR_THE_SEARCH_KEY));
      }
      return nonUkAirportList;
   }

   private void processDepartureSearch(final String key, final List<String> arrivals,
      final List<String> dates, final AirportSearchResult searchResult,
      final List<uk.co.portaltech.travel.model.airport.Airport> airports)
   {

      boolean isFuzzyResult = false;

      if (checkArrivalsDatesIsEmpty(arrivals, dates))
      {
         airports.addAll(airportsService.searchDepartureAutoSuggest(key));
         isFuzzyResult = isFuzzy(airports, isFuzzyResult);
         checkEmptyAirports(searchResult, airports);
      }
      else
      {

         final List<String> selectedAirports = new ArrayList<String>();
         if (CollectionUtils.isNotEmpty(arrivals))
         {
            for (final String arrival : arrivals)
            {
               selectedAirports.add(arrival.split(":")[0].toString());
            }
         }
         final uk.co.portaltech.travel.model.airport.Airports aiportsResult =
            airportsService.searchDepartureAutoSuggest(key, selectedAirports, toLocalDates(dates));
         airports.addAll(aiportsResult.airportsWithRoute());
         getupdatedairports(airports, aiportsResult);
         isFuzzyResult = aiportsResult.isNomatch();
         searchResult.setError(searchError(aiportsResult, selectedAirports));
         checkForNoFlights(searchResult, airports, aiportsResult);

      }

      searchResult.setNomatch(isFuzzyResult);
   }

   /**
    * @param arrivals
    * @param dates
    * @return
    */
   private boolean checkArrivalsDatesIsEmpty(final List<String> arrivals, final List<String> dates)
   {
      return CollectionUtils.isEmpty(arrivals) && CollectionUtils.isEmpty(dates);
   }

   /**
    *
    */
   private void checkForNoFlights(final AirportSearchResult searchResult,
      final List<uk.co.portaltech.travel.model.airport.Airport> airports,
      final uk.co.portaltech.travel.model.airport.Airports aiportsResult)
   {
      if (checkError(searchResult))
      {
         airports.addAll(aiportsResult.matchedAirports());
         for (final uk.co.portaltech.travel.model.airport.Airport ap : airports)
         {
            ap.withAvailability(false);
         }
      }
   }

   private void checkEmptyAirports(final AirportSearchResult searchResult,
      final List<uk.co.portaltech.travel.model.airport.Airport> airports)
   {
      if (CollectionUtils.isEmpty(airports))
      {
         searchResult.setError(new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_FOR_THE_SEARCH_KEY));
         searchResult.setAirports(Collections.<AirportData> emptyList());
      }
   }

   private boolean isFuzzy(final List<uk.co.portaltech.travel.model.airport.Airport> airports,
      final boolean isFuzzyResult)
   {

      final Airport dummy = new Airport(NOMATCH, SIMILAR_SPELLINGS);

      if (airports.remove(dummy))
      {
         return true;
      }
      return isFuzzyResult;
   }

   private SearchError searchError(
      final uk.co.portaltech.travel.model.airport.Airports airportsResult,
      final List<String> arrivals)
   {
      return airportsResult.hasRoutes() ? null : determineError(airportsResult, arrivals);
   }

   private SearchError determineError(
      final uk.co.portaltech.travel.model.airport.Airports airportsResult,
      final List<String> arrivals)
   {
      final List<AirportData> airportDatas =
         map(airportsResult.matchedAirports(), AIRPORT_TO_AIRPORT_DATA_MAPPER);
      final List<AirportData> airportEntries = new ArrayList<AirportData>();
      airportEntries.addAll(airportDatas);

      final Collection<AirportData> matchedUnits = getMatchedArrivals(arrivals);

      if (!airportsResult.hasAirportsToSelectedDestinations()
         && !airportsResult.hasAirportsOnSelectedDates())
      {
         return null;
      }
      return airportsResult.hasAirportsToSelectedDestinations() ? new SearchError(INVALID_ROUTE_ON,
         NO_ROUTE_FOUND_FOR_SELECTED_DATES, airportEntries, toList(matchedUnits))
         : new SearchError(INVALID_ROUTE_TO, NO_ROUTE_FOUND_TO_SELECTED_DESTINATIONS,
            airportEntries, toList(matchedUnits));

   }

   private List<AirportData> getMatchedArrivals(final List<String> arrivalCodes)
   {
      final Set<uk.co.portaltech.travel.model.airport.Airport> airportList =
         new HashSet<uk.co.portaltech.travel.model.airport.Airport>();

      if (null != arrivalCodes)
      {
         for (final String code : arrivalCodes)
         {
            airportList.addAll(airportsService.searchDepartureAutoSuggest(code));
         }
      }
      return processAirportList(arrivalCodes, airportList);
   }

   private List<AirportData> processAirportList(final List<String> airportCodes,
      final Set<uk.co.portaltech.travel.model.airport.Airport> airportList)
   {

      List<uk.co.portaltech.travel.model.airport.Airport> requestedAirports = null;

      if (!CollectionUtils.isEmpty(airportList))
      {
         requestedAirports = new ArrayList<uk.co.portaltech.travel.model.airport.Airport>();

         for (final uk.co.portaltech.travel.model.airport.Airport airport : airportList)
         {
            requestedAirports = processRequestedAirports(airportCodes, requestedAirports, airport);
         }

         if (!CollectionUtils.isEmpty(requestedAirports))
         {
            return map(requestedAirports, AIRPORT_TO_AIRPORT_DATA_MAPPER);
         }

      }
      return new ArrayList<AirportData>();
   }

   private List<uk.co.portaltech.travel.model.airport.Airport> processRequestedAirports(
      final List<String> airportCodes,
      final List<uk.co.portaltech.travel.model.airport.Airport> requestedAirports,
      final uk.co.portaltech.travel.model.airport.Airport airport)
   {
      for (final String airportCode : airportCodes)
      {
         if (StringUtils.equals(airport.code(), airportCode))
         {
            requestedAirports.add(airport);
         }
      }
      return requestedAirports;
   }

   /**
    * This method is used to get the arrival airport list in the 'Arriving To' field in the search
    * panel
    */
   @Override
   public Map<String, List<AirportData>> getArrivalData(final List<String> airports,
      final List<String> dates) throws SearchResultsBusinessException
   {

      try
      {
         Map<String, List<AirportData>> arrivalAirportGuide;
         if (checkArrivalsDatesIsEmpty(airports, dates))
         {
            arrivalAirportGuide =
               flightSearchService.fetchArrivalAirports(cmsSiteService.getCurrentCatalogVersion());
         }
         else
         {
            arrivalAirportGuide =
               flightSearchService.fetchArrivalAirports(cmsSiteService.getCurrentCatalogVersion(),
                  airports, toLocalDates(dates));
         }
         return arrivalAirportGuide;
      }
      catch (final FlightsServiceException e)
      {
         throw new SearchResultsBusinessException(ERROR_CODE, e);
      }

   }

   /**
    * Following method is used for Auto suggest functionality in Search Panel 'Arriving To' field
    * under airport list overlay
    */
   @Override
   public AirportSearchResult findArriving(final String key, final List<String> departures,
      final List<String> dates) throws SearchResultsBusinessException
   {
      final AirportSearchResult searchResult = new AirportSearchResult();
      final List<uk.co.portaltech.travel.model.airport.Airport> airports =
         new ArrayList<uk.co.portaltech.travel.model.airport.Airport>();
      boolean isFuzzyResult = false;

      try
      {
         if (checkArrivalsDatesIsEmpty(departures, dates))
         {
            airports.addAll(airportsService.searchArrivalAutoSuggest(key));
            isFuzzyResult = isFuzzy(airports, isFuzzyResult);
            checkEmptyAirports(searchResult, airports);
         }
         else
         {
            final uk.co.portaltech.travel.model.airport.Airports aiportsResult =
               airportsService.searchArrivalAutoSuggest(key, departures, toLocalDates(dates));
            airports.addAll(aiportsResult.airportsWithRoute());

            getupdatedairports(airports, aiportsResult);
            isFuzzyResult = aiportsResult.isNomatch();
            searchResult.setError(searchArrivingError(aiportsResult, departures));
            checkForNoFlights(searchResult, airports, aiportsResult);
         }

         searchResult.setNomatch(isFuzzyResult);
      }
      catch (final AirportServiceException ex)
      {
         LOGGER.error("AirportServiceException", ex);
         searchResult.setError(new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_FOR_THE_SEARCH_KEY));
         searchResult.setAirports(Collections.<AirportData> emptyList());
         return searchResult;
      }

      searchResult.setAirports(getNonUKResultList(searchResult, airports));

      verifyInvalidRoute(searchResult);
      getSorted(searchResult.getAirports());

      return searchResult;
   }

   /**
    * @param airports
    * @param aiportsResult
    */
   private void getupdatedairports(
      final List<uk.co.portaltech.travel.model.airport.Airport> airports,
      final uk.co.portaltech.travel.model.airport.Airports aiportsResult)
   {
      final List<uk.co.portaltech.travel.model.airport.Airport> updatedairports =
         new ArrayList<uk.co.portaltech.travel.model.airport.Airport>();
      updatedairports.addAll(aiportsResult.matchedAirports());

      updatedairports.removeAll(airports);

      if (CollectionUtils.isNotEmpty(updatedairports))
      {
         for (final uk.co.portaltech.travel.model.airport.Airport ap : updatedairports)
         {
            ap.withAvailability(false);
         }
      }
      airports.addAll(updatedairports);
   }

   /**
    *
    */
   private void verifyInvalidRoute(final AirportSearchResult searchResult)
   {
      final String[] ukcodes = { "GBR", "IRL" };
      boolean isUk = false;

      if (checkError(searchResult))
      {
         final List<AirportData> airportDatalist =
            (List<AirportData>) searchResult.getError().getEntry();

         for (final AirportData ad : airportDatalist)
         {
            if (Arrays.asList(ukcodes).contains(ad.getCountryCode()))
            {
               isUk = true;
            }
            ad.setAvailable(false);
         }
         verifyNonUk(searchResult, isUk, airportDatalist);
         searchResult.setError(null);
      }
      else
      {
         checkForSimilarspellings(searchResult);
      }
   }

   /**
    * @param searchResult
    * @param isUk
    * @param airportDatalist
    */
   private void verifyNonUk(final AirportSearchResult searchResult, final boolean isUk,
      final List<AirportData> airportDatalist)
   {
      if (!isUk)
      {
         searchResult.setAirports(airportDatalist);
      }
   }

   /**
    * @param searchResult
    */
   private void checkForSimilarspellings(final AirportSearchResult searchResult)
   {
      if (CollectionUtils.isNotEmpty(searchResult.getAirports()))
      {
         for (final AirportData airportData : searchResult.getAirports())
         {
            if (airportData.getName().contains("Similar spellings"))
            {
               searchResult.getAirports().remove(airportData);
               break;
            }
         }
      }
   }

   /**
    *
    */
   private boolean checkError(final AirportSearchResult searchResult)
   {
      return searchResult.getError() != null
         && (INVALID_ROUTE_TO.equals(searchResult.getError().getCode()) || INVALID_ROUTE_ON
            .equals(searchResult.getError().getCode()));
   }

   private SearchError searchArrivingError(
      final uk.co.portaltech.travel.model.airport.Airports airportsResult,
      final List<String> departures)
   {
      return airportsResult.hasRoutes() ? null : determineArrivingError(airportsResult, departures);
   }

   private SearchError determineArrivingError(
      final uk.co.portaltech.travel.model.airport.Airports airportsResult,
      final List<String> departures)
   {
      final List<AirportData> airportDatas =
         map(airportsResult.matchedAirports(), AIRPORT_TO_AIRPORT_DATA_MAPPER);
      final List<AirportData> airportEntries = new ArrayList<AirportData>();

      airportEntries.addAll(airportDatas);

      final Collection<AirportData> matchedUnits = getMatchedDepartures(departures);

      if (!airportsResult.hasAirportsToSelectedDestinations()
         && !airportsResult.hasAirportsOnSelectedDates())
      {
         return null;
      }
      return airportsResult.hasAirportsToSelectedDestinations() ? new SearchError(INVALID_ROUTE_ON,
         NO_ROUTE_FOUND_FOR_SELECTED_DATES, airportEntries, toList(matchedUnits))
         : new SearchError(INVALID_ROUTE_TO, NO_ROUTE_FOUND_TO_SELECTED_DESTINATIONS,
            airportEntries, toList(matchedUnits));

   }

   private List<AirportData> getMatchedDepartures(final List<String> departureCodes)
   {
      final Set<uk.co.portaltech.travel.model.airport.Airport> airportList =
         new HashSet<uk.co.portaltech.travel.model.airport.Airport>();

      if (null != departureCodes)
      {
         for (final String code : departureCodes)
         {
            airportList.addAll(airportsService.searchArrivalAutoSuggest(code));
         }
      }
      return processAirportList(departureCodes, airportList);
   }

   /**
    * This method is used to get Where we fly arriving list according to the season selected
    */
   @Override
   public Map<String, List<AirportData>> getWhereWeFlyArrivings(final List<String> airports,
      final String departureDate, final String returnDate) throws SearchResultsBusinessException
   {
      Map<String, List<AirportData>> whereWeFlyArrivings = null;

      try
      {
         whereWeFlyArrivings = fetchWhereWeFlyArrivings(airports, departureDate, returnDate);

      }
      catch (final FlightsServiceException e)
      {
         throw new SearchResultsBusinessException(ERROR_CODE, e);
      }

      return whereWeFlyArrivings;

   }

   /**
    *
    */
   private Map<String, List<AirportData>> fetchWhereWeFlyArrivings(final List<String> airports,
      final String departureDate, final String returnDate) throws FlightsServiceException
   {
      Map<String, List<AirportData>> arrivings = null;
      if (verifyDatesIsNotEmpty(departureDate, returnDate))
      {
         arrivings =
            flightSearchService.fetchArrivalAirports(cmsSiteService.getCurrentCatalogVersion(),
               airports,
               toLocalDates(DateRangeProviderUtil.getSeasonDates(departureDate, returnDate)));
      }
      else if (CollectionUtils.isNotEmpty(airports)
         && verifyDatesAreNotEmpty(departureDate, returnDate))
      {
         arrivings =
            flightSearchService.fetchArrivalAirports(cmsSiteService.getCurrentCatalogVersion(),
               airports, null);
      }
      return arrivings;
   }

   /**
    * @param departureDate
    * @param returnDate
    *
    */
   private boolean verifyDatesAreNotEmpty(final String departureDate, final String returnDate)
   {
      return StringUtils.isEmpty(departureDate) || StringUtils.isEmpty(returnDate);
   }

   /**
    * @param departureDate
    * @param returnDate
    *
    */
   private boolean verifyDatesIsNotEmpty(final String departureDate, final String returnDate)
   {
      return StringUtils.isNotEmpty(departureDate) && StringUtils.isNotEmpty(returnDate);
   }

   private List<LocalDate> toLocalDates(final List<String> dates)
   {
      return map(dates, LOCATE_DATE_MAPPER);
   }

   /**
    * This method is used fetch all airports in the database.
    *
    * @return Map<String, List<AirportData>>
    * @throws SearchResultsBusinessException
    */
   @Override
   public Map<String, List<AirportData>> fetchAllAirports() throws SearchResultsBusinessException
   {

      Map<String, List<AirportData>> airportsData;
      try
      {
         airportsData =
            flightSearchService.fetchArrivalAirports(cmsSiteService.getCurrentCatalogVersion());
      }
      catch (final FlightsServiceException e)
      {
         throw new SearchResultsBusinessException(ERROR_CODE, e);
      }

      return airportsData;

   }

   @Override
   public List<String> getAllValidDates(final String departureDate, final int flexibility)
   {
      return flightSearchService.getAllValidDates(departureDate, flexibility);
   }

   private List<AirportData> getSorted(final List<AirportData> airportDatalist)
   {
      if (CollectionUtils.isNotEmpty(airportDatalist))
      {
         final Set<AirportData> airportDataSet = new LinkedHashSet<AirportData>(airportDatalist);
         airportDatalist.clear();
         airportDatalist.addAll(airportDataSet);

         Collections.sort(airportDatalist, new Comparator<AirportData>()
         {
            @Override
            public int compare(final AirportData o1, final AirportData o2)
            {
               return o1.getName().compareTo(o2.getName());
            }
         });
      }

      return airportDatalist;
   }

}
