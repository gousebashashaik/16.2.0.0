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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.route.FlightRouteDTO;
import uk.co.portaltech.tui.comparators.sort.FlightTimetableAttributesComparator;
import uk.co.portaltech.tui.flights.facades.FlightRouteFacade;
import uk.co.tui.flights.service.FlightRouteService;


/**
 *
 */
public class DefaultFlightRouteFacade implements FlightRouteFacade
{

   @Resource
   private FlightRouteService flightRouteService;

   private static final String DEP_TIME_ASCENDING = "DEP_TIME_ASCENDING";

   /**
    * This method extracts flight route timetable results for the given departure airport and arrival airport
    */
   @Override
   public List<FlightRouteDTO> searchByDepartureAndArrivalAirports(final String departureCode, final String arrivalCode)
   {
      final List<FlightRouteDTO> timeTableInformationData = flightRouteService.searchByDepartureAndArrivalAirports(departureCode,
            arrivalCode);


      Collections.sort(timeTableInformationData,
            FlightTimetableAttributesComparator.getComparator(getFlightRouteComparatorList(DEP_TIME_ASCENDING)));

      return timeTableInformationData;
   }

   private static List<FlightTimetableAttributesComparator> getFlightRouteComparatorList(final String comperatorCode)
   {
      final List<FlightTimetableAttributesComparator> comparators = new ArrayList<FlightTimetableAttributesComparator>();
      comparators.add(FlightTimetableAttributesComparator.valueOf(comperatorCode));
      return comparators;

   }

}
