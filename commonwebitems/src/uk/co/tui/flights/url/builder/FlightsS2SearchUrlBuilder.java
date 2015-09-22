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
 * $RCSfile: LOBNameComponentController.java$
 *
 * $Revision: $
 *
 * $Date: Jan 14, 2015$
 *
 *
 *
 * $Log: $
 */
package uk.co.tui.flights.url.builder;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import uk.co.tui.common.DateUtils;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;


/**
 * @author phanisekhar.n
 *
 */
public class FlightsS2SearchUrlBuilder
{
   private static final Logger LOGGER = Logger.getLogger(FlightsS2SearchUrlBuilder.class.getName());

   @Resource
   private ConfigurationService configurationService;

   @SuppressWarnings("boxing")
   public String buildS2Url(final FlightSearchCriteria flightSearchCriteria) throws SearchResultsBusinessException
   {

      final String basicURL = configurationService.getConfiguration().getString("flights.s2.search.url");
      final StringBuilder requestURL = new StringBuilder(basicURL);
      if (StringUtils.isNotEmpty(basicURL))
      {

         //http://flights.thomson.co.uk/thomson/en-GB/booking/select?step=Home&OneWayRoundTrip=RoundTrip&depDay=21&depMon=2015-04&retDay=28&retMon=2015-04&depAP=LGW&retAP=BVC&numAdults=2&numChildren=0&numInfants=0

         final boolean isOneway = flightSearchCriteria.isOneWay();


         requestURL.append("&OneWayRoundTrip=").append(isOneway ? "OneWay" : "RoundTrip");
         requestURL.append("&depDay=").append(new LocalDate(flightSearchCriteria.getDepartureDate()).getDayOfMonth());
         requestURL.append("&depMon=").append(
               DateUtils.formatdate(flightSearchCriteria.getDepartureDate(), "yyyy-MM-dd", "yyyy-MM"));

         if (!isOneway)
         {
            requestURL.append("&retDay=").append(new LocalDate(flightSearchCriteria.getReturnDate()).getDayOfMonth());
            requestURL.append("&retMon=").append(
                  DateUtils.formatdate(flightSearchCriteria.getReturnDate(), "yyyy-MM-dd", "yyyy-MM"));
         }
         requestURL.append("&depAP=").append(flightSearchCriteria.getDepartureAirportCode().get(0));
         requestURL.append("&retAP=").append(flightSearchCriteria.getArrivalAirportCode().get(0));
         requestURL.append("&numAdults=").append(flightSearchCriteria.getAdultCount());
         requestURL.append("&numChildren=").append(flightSearchCriteria.getChildCount());
         requestURL.append("&numInfants=").append(flightSearchCriteria.getInfantCount());

      }
      LOGGER.info("flights s2 search url: " + requestURL.toString());
      return requestURL.toString();
   }



}
