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
 * $RCSfile: ItineraryViewDataPopulator.java$
 *
 * $Revision: $
 *
 * $Date: Jan 14, 2015$
 *
 *
 *
 * $Log: $
 */
package uk.co.tui.flights.populators;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.flights.constants.SearchandbrowseConstants;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.ItineraryData;
import uk.co.tui.flights.data.LegData;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.service.FlightSearchService;
import uk.co.tui.flights.web.view.data.ItineraryViewData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author phanisekhar.n
 *
 */
public class ItineraryViewDataPopulator implements Populator<List<Itinerary>, ItineraryViewData>
{

   private static final TUILogUtils LOGGER = new TUILogUtils("ItineraryViewDataPopulator");

   @Resource
   private TUIConfigService tuiConfigService;

   @Resource
   private FlightSearchService flightSearchService;

   @Resource
   private CMSSiteService cmsSiteService;



   private static final String HOURS = " hrs ";

   private static final String MINUTES = " min";

   private static final String DREAMLINER = "DREAMLINEAR787";

   private static final String FO_LAI_THRESHOLD = "FlightOnlySeats.LAI_threshold";

   private static final String DATE_FORMAT = "yyyy-MM-dd";


   /**
    * @param source
    * @param target
    */
   @Override
   public void populate(final List<Itinerary> source, final ItineraryViewData target) throws ConversionException
   {
      final List<ItineraryData> itineryDataList = new ArrayList<ItineraryData>();

      if (validateSearchType(target))
      {
         populateItineraryForGroupedDeals(source, itineryDataList);
      }
      else if (StringUtils.equalsIgnoreCase("pricegrid", target.getSearchType())
            || StringUtils.equalsIgnoreCase("backtosearch", target.getSearchType()))
      {
         populateLimitedItineraryData(source, itineryDataList);
      }
      else
      {
         populateIndividualItineraryData(source, itineryDataList);
      }

      target.setItinerary(itineryDataList);
   }

   /**
    * Validating based on the search type.
    *
    * @param target
    * @return boolean
    */
   private boolean validateSearchType(final ItineraryViewData target)
   {

      return (StringUtils.equalsIgnoreCase("ins", target.getSearchType())
            || StringUtils.equalsIgnoreCase("lazy", target.getSearchType())
            || StringUtils.equalsIgnoreCase("price", target.getSearchType()) || StringUtils.equalsIgnoreCase("destination",
            target.getSearchType()));
   }

   /**
    * @param source
    * @param itineryDataList
    */
   private void populateItineraryForGroupedDeals(final List<Itinerary> source, final List<ItineraryData> itineryDataList)
   {
      for (final Itinerary itinerary : source)
      {
         final ItineraryData itineraryData = new ItineraryData();

         itineraryData.setMinAvail(itinerary.getMinAvail());
         itineraryData.setPrice(itinerary.getPriceList().get(SearchandbrowseConstants.ZERO_VALUE).getAmount().getAmount());
         itineraryData.setPricePP(itinerary.getPriceList().get(SearchandbrowseConstants.ZERO_VALUE).getRate().getAmount());
         itineraryData.setDuration(itinerary.getDuration());
         itineraryData.setCount(itinerary.getCount());

         populateLegs(itinerary, itineraryData);

         itineryDataList.add(itineraryData);
      }
   }

   /**
    * @param source
    * @param itineryDataList
    */
   private void populateLimitedItineraryData(final List<Itinerary> source, final List<ItineraryData> itineryDataList)
   {
      for (final Itinerary itinerary : source)
      {
         final ItineraryData itineraryData = new ItineraryData();
         itineraryData.setDepartureDt(itinerary.getOutBound().get(SearchandbrowseConstants.ZERO_VALUE).getSchedule()
               .getDepartureDate());

         itineraryData.setReturnDt(CollectionUtils.isNotEmpty(itinerary.getInBound()) ? itinerary.getInBound()
               .get(SearchandbrowseConstants.ZERO_VALUE).getSchedule().getDepartureDate() : null);

         itineraryData.setPricePP(itinerary.getPriceList().get(SearchandbrowseConstants.ZERO_VALUE).getRate().getAmount());
         itineraryData.setDuration(itinerary.getDuration());
         itineraryData.setId(itinerary.getSequenceNo());

         setOutboundAirportCodes(itinerary, itineraryData);

         itineryDataList.add(itineraryData);
      }
   }

   /**
    * @param itinerary
    * @param itineraryData
    */
   private void setOutboundAirportCodes(final Itinerary itinerary, final ItineraryData itineraryData)
   {
      final LegData legData = new LegData();

      AirportData airportData = null;

      airportData = new AirportData();
      airportData.setId(itinerary.getOutBound().get(0).getDepartureAirport().getCode());
      legData.setDepartureAirportData(airportData);

      airportData = new AirportData();
      airportData.setId(itinerary.getOutBound().get(0).getArrivalAirport().getCode());
      legData.setArrivalAirportData(airportData);

      itineraryData.setOutbound(legData);
   }

   /**
    * @param source
    * @param itineryDataList
    */
   private void populateIndividualItineraryData(final List<Itinerary> source, final List<ItineraryData> itineryDataList)
   {
      for (final Itinerary itinerary : source)
      {
         final ItineraryData itineraryData = new ItineraryData();



         itineraryData.setDepartureDate(itinerary.getOutBound().get(SearchandbrowseConstants.ZERO_VALUE).getSchedule()
               .getDepartureDate().toString());

         itineraryData.setReturnDate(CollectionUtils.isNotEmpty(itinerary.getInBound()) ? itinerary.getInBound()
               .get(SearchandbrowseConstants.ZERO_VALUE).getSchedule().getDepartureDate().toString() : StringUtils.EMPTY);


         itineraryData.setMinAvail(itinerary.getMinAvail());
         populateLegs(itinerary, itineraryData);
         itineraryData.setPrice(itinerary.getPriceList().get(SearchandbrowseConstants.ZERO_VALUE).getAmount().getAmount());
         itineraryData.setPricePP(itinerary.getPriceList().get(SearchandbrowseConstants.ZERO_VALUE).getRate().getAmount());
         itineraryData.setDiscount(itinerary.getOutBound().get(SearchandbrowseConstants.ZERO_VALUE).getDiscounts()
               .get(SearchandbrowseConstants.ZERO_VALUE).getPrice().getAmount().getAmount());
         itineraryData.setDiscountPP(itinerary.getOutBound().get(SearchandbrowseConstants.ZERO_VALUE).getDiscounts()
               .get(SearchandbrowseConstants.ZERO_VALUE).getPrice().getRate().getAmount());
         itineraryData.setDuration(itinerary.getDuration());
         itineraryData.setCount(itinerary.getCount());
         populateDreamLiner(itinerary, itineraryData);
         populateLimAvail(itinerary, itineraryData);
         itineraryData.setId(itinerary.getSequenceNo());

         itineryDataList.add(itineraryData);
      }
   }

   /**
    * @param itinerary
    * @param itineraryData
    */
   private void populateLimAvail(final Itinerary itinerary, final ItineraryData itineraryData)
   {
      if (itinerary.getMinAvail() <= Integer.parseInt(tuiConfigService.getConfigValue(FO_LAI_THRESHOLD)))
      {
         itineraryData.setLimAvail(Boolean.TRUE);
      }
      else
      {
         itineraryData.setLimAvail(Boolean.FALSE);
      }

   }

   /**
    * @param itinerary
    * @param itineraryData
    */
   private void populateDreamLiner(final Itinerary itinerary, final ItineraryData itineraryData)
   {

      if (verifyLegType(itinerary))
      {
         if (verifyOutBoundEqType(itinerary))
         {
            itineraryData.setDreamliner(Boolean.TRUE);
         }

         if (verifyInBoundEqType(itinerary))
         {
            itineraryData.setDreamliner(Boolean.TRUE);
         }
      }
      else if (CollectionUtils.isNotEmpty(itinerary.getOutBound()))
      {
         if (verifyOutBoundEqType(itinerary))
         {
            itineraryData.setDreamliner(Boolean.TRUE);
         }
      }
      else if (CollectionUtils.isNotEmpty(itinerary.getInBound()))
      {
         if (verifyInBoundEqType(itinerary))
         {
            itineraryData.setDreamliner(Boolean.TRUE);
         }
      }
      else
      {
         itineraryData.setDreamliner(Boolean.FALSE);
      }

   }

   /**
    * @param itinerary
    * @return boolean
    */
   private boolean verifyLegType(final Itinerary itinerary)
   {
      return CollectionUtils.isNotEmpty(itinerary.getOutBound()) && CollectionUtils.isNotEmpty(itinerary.getInBound());
   }

   /**
    * @param itinerary
    * @return boolean
    */
   private boolean verifyInBoundEqType(final Itinerary itinerary)
   {

      return itinerary.getInBound().get(0).getCarrier().getEquipementType() != null
            && DREAMLINER.equals(itinerary.getInBound().get(0).getCarrier().getEquipementType().toString());
   }

   /**
    * @param itinerary
    * @return boolean
    */
   private boolean verifyOutBoundEqType(final Itinerary itinerary)
   {
      return itinerary.getOutBound().get(0).getCarrier().getEquipementType() != null
            && DREAMLINER.equals(itinerary.getOutBound().get(0).getCarrier().getEquipementType().toString());
   }

   /**
    * @param itinerary
    * @param itineraryData
    */
   private void populateLegs(final Itinerary itinerary, final ItineraryData itineraryData)
   {


      itineraryData.setOutbound(populateLegData(itinerary.getOutBound().get(SearchandbrowseConstants.ZERO_VALUE)));
      itineraryData.setInbound(CollectionUtils.isNotEmpty(itinerary.getInBound()) ? populateLegData(itinerary.getInBound().get(
            SearchandbrowseConstants.ZERO_VALUE)) : null);
   }

   /**
    * @param leg
    * @return LegData
    */
   private LegData populateLegData(final Leg leg)
   {
      final LegData legData = new LegData();
      legData.setArrivalAirport(leg.getArrivalAirport());
      legData.setDepartureAirport(leg.getDepartureAirport());
      legData.setSchedule(leg.getSchedule());
      legData.setInventoryId(leg.getInventoryId());
      legData.setExternalInventory(leg.isExternalInventory());
      legData.setJourneyDays(calculateDays(leg.getSchedule().getDepartureDate(), leg.getSchedule().getArrivalDate()));
      legData.setFlightno(populateFlightno(leg));
      legData.setJourneyDuration(formatFlightDuration(leg.getJourneyDuration()));
      legData.setArrivalAirportData(getAirportData(leg.getArrivalAirport().getCode()));
      legData.setDepartureAirportData(getAirportData(leg.getDepartureAirport().getCode()));
      return legData;

   }

   private AirportData getAirportData(final String airportCode)
   {
      AirportData airportData = new AirportData();
      try
      {
         airportData = flightSearchService.getAirportDataForCode(airportCode, cmsSiteService.getCurrentCatalogVersion());
      }
      catch (final FlightsServiceException e)
      {
         LOGGER.error(e);
      }
      return airportData;
   }

   /**
    * @param leg
    * @return string
    */
   private String populateFlightno(final Leg leg)
   {
      final StringBuilder flightno = new StringBuilder();
      flightno.append(leg.getCarrier().getCode()).append(" ").append(leg.getCarrier().getNumber());
      return flightno.toString();
   }

   /**
    * @param time
    * @return string
    */


   private String formatFlightDuration(final String time)
   {
      final int length = time.length();
      final StringBuilder journeyDuration = new StringBuilder();
      journeyDuration.append(time.substring(0, length - CommonwebitemsConstants.TWO)).append(HOURS)
            .append(String.format("%02d", Integer.valueOf(time.substring(length - CommonwebitemsConstants.TWO, length))))
            .append(MINUTES);
      return journeyDuration.toString();
   }

   /**
    * @param start
    * @param end
    * @return int
    */
   private int calculateDays(final Date start, final Date end)
   {
      final LocalDate sDate = new LocalDate(formatDate(start));
      final LocalDate eDate = new LocalDate(formatDate(end));
      final Days d = Days.daysBetween(sDate, eDate);
      return d.getDays();
   }

   /**
    * @param start
    * @return int
    */
   private String formatDate(final Date start)
   {
      final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

      return sdf.format(start);
   }
}
