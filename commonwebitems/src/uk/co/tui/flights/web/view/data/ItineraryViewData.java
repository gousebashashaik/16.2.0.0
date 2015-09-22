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
 * $RCSfile: ItineraryViewData.java$
 *
 * $Revision: $
 *
 * $Date: Jan 14, 2015$
 *
 *
 *
 * $Log: $
 */
package uk.co.tui.flights.web.view.data;

import java.util.List;

import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.ItineraryData;



/**
 *
 */
public class ItineraryViewData
{


   private List<ItineraryData> itinerary;

   private String seasonEndDate;

   private FlightSearchCriteria flightSearchCriteria;

   private DreamLinerTooltipViewData dreamLiner;

   private AirportData depAirportData;

   private AirportData arrAirportData;

   private String tracsEndDate;

   /**
    * @return the tracsEndDate
    */
   public String getTracsEndDate()
   {
      return tracsEndDate;
   }

   /**
    * @param tracsEndDate
    *           the tracsEndDate to set
    */
   public void setTracsEndDate(final String tracsEndDate)
   {
      this.tracsEndDate = tracsEndDate;
   }



   /**
    * @return the itinerary
    */
   public List<ItineraryData> getItinerary()
   {
      return itinerary;
   }

   /**
    * @param itinerary
    *           the itinerary to set
    */
   public void setItinerary(final List<ItineraryData> itinerary)
   {
      this.itinerary = itinerary;
   }

   /**
    * @return the seasonEndDate
    */
   public String getSeasonEndDate()
   {
      return seasonEndDate;
   }

   /**
    * @param seasonEndDate
    *           the seasonEndDate to set
    */
   public void setSeasonEndDate(final String seasonEndDate)
   {
      this.seasonEndDate = seasonEndDate;
   }

   /**
    * @return the flightSearchCriteria
    */
   public FlightSearchCriteria getFlightSearchCriteria()
   {
      return flightSearchCriteria;
   }

   /**
    * @param flightSearchCriteria
    *           the flightSearchCriteria to set
    */
   public void setFlightSearchCriteria(final FlightSearchCriteria flightSearchCriteria)
   {
      this.flightSearchCriteria = flightSearchCriteria;
   }

   /**
    * @return the dltooltipMsg
    */
   public DreamLinerTooltipViewData getDreamLiner()
   {
      return dreamLiner;
   }

   /**
    * @param dltooltipMsg
    *           the dltooltipMsg to set
    */
   public void setDreamLiner(final DreamLinerTooltipViewData dltooltipMsg)
   {
      this.dreamLiner = dltooltipMsg;
   }

   /**
    * @return the depAirportData
    */
   public AirportData getDepAirportData()
   {
      return depAirportData;
   }

   /**
    * @param depAirportData
    *           the depAirportData to set
    */
   public void setDepAirportData(final AirportData depAirportData)
   {
      this.depAirportData = depAirportData;
   }

   /**
    * @return the arrAirportData
    */
   public AirportData getArrAirportData()
   {
      return arrAirportData;
   }

   /**
    * @param arrAirportData
    *           the arrAirportData to set
    */
   public void setArrAirportData(final AirportData arrAirportData)
   {
      this.arrAirportData = arrAirportData;
   }


}
