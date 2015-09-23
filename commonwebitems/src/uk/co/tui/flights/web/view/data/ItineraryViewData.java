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

import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.CrossSellAirportData;
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

   private List<AirportData> depAirportData;

   private List<AirportData> arrAirportData;

   private String tracsEndDate;

   private String totoalcount;

   private List<Itinerary> itineraryList;

   private List<String> flightDepPt;

   private List<String> flightArrPt;

   private List<String> outSlots;

   private List<String> inSlots;

   private List<String> price;

   private List<String> pricePerPerson;

   private int itineraryCount;

   private String searchType;

   private CrossSellAirportData crossSellAirportData;

   private List<AirportData> departureFlightsInfo;

   private List<AirportData> arrivalFLightsInfo;

   private String sessionTimeoutPage;

   private boolean sessionTimeerrorflag;



   /**
	 * @return the sessionTimeoutPage
	 */
	public String getSessionTimeoutPage()
	{
		return sessionTimeoutPage;
	}

	/**
	 * @param sessionTimeoutPage the sessionTimeoutPage to set
	 */
	public void setSessionTimeoutPage(String sessionTimeoutPage)
	{
		this.sessionTimeoutPage = sessionTimeoutPage;
	}

	/**
	 * @return the sessionTimeerrorflag
	 */
	public boolean isSessionTimeerrorflag()
	{
		return sessionTimeerrorflag;
	}

	/**
	 * @param sessionTimeerrorflag the sessionTimeerrorflag to set
	 */
	public void setSessionTimeerrorflag(boolean sessionTimeerrorflag)
	{
		this.sessionTimeerrorflag = sessionTimeerrorflag;
	}

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
   public List<AirportData> getDepAirportData()
   {
      return depAirportData;
   }

   /**
    * @param depAirportData
    *           the depAirportData to set
    */
   public void setDepAirportData(final List<AirportData> depAirportData)
   {
      this.depAirportData = depAirportData;
   }

   /**
    * @return the arrAirportData
    */
   public List<AirportData> getArrAirportData()
   {
      return arrAirportData;
   }

   /**
    * @param arrAirportData
    *           the arrAirportData to set
    */
   public void setArrAirportData(final List<AirportData> arrAirportData)
   {
      this.arrAirportData = arrAirportData;
   }

   /**
    * @return the totoalcount
    */
   public String getTotoalcount()
   {
      return totoalcount;
   }

   /**
    * @param totoalcount
    *           the totoalcount to set
    */
   public void setTotoalcount(final String totoalcount)
   {
      this.totoalcount = totoalcount;
   }

   /**
    * @return the itineraryList
    */
   public List<Itinerary> getItineraryList()
   {
      return itineraryList;
   }

   /**
    * @param itineraryList
    *           the itineraryList to set
    */
   public void setItineraryList(final List<Itinerary> itineraryList)
   {
      this.itineraryList = itineraryList;
   }

   /**
    * @return the flightDepPt
    */
   public List<String> getFlightDepPt()
   {
      return flightDepPt;
   }

   /**
    * @param flightDepPt
    *           the flightDepPt to set
    */
   public void setFlightDepPt(final List<String> flightDepPt)
   {
      this.flightDepPt = flightDepPt;
   }

   /**
    * @return the flightArrPt
    */
   public List<String> getFlightArrPt()
   {
      return flightArrPt;
   }

   /**
    * @param flightArrPt
    *           the flightArrPt to set
    */
   public void setFlightArrPt(final List<String> flightArrPt)
   {
      this.flightArrPt = flightArrPt;
   }

   /**
    * @return the outSlots
    */
   public List<String> getOutSlots()
   {
      return outSlots;
   }

   /**
    * @param outSlots
    *           the outSlots to set
    */
   public void setOutSlots(final List<String> outSlots)
   {
      this.outSlots = outSlots;
   }

   /**
    * @return the inSlots
    */
   public List<String> getInSlots()
   {
      return inSlots;
   }

   /**
    * @param inSlots
    *           the inSlots to set
    */
   public void setInSlots(final List<String> inSlots)
   {
      this.inSlots = inSlots;
   }

   /**
    * @return the price
    */
   public List<String> getPrice()
   {
      return price;
   }

   /**
    * @param price
    *           the price to set
    */
   public void setPrice(final List<String> price)
   {
      this.price = price;
   }

   /**
    * @return the pricePerPerson
    */
   public List<String> getPricePerPerson()
   {
      return pricePerPerson;
   }

   /**
    * @param pricePerPerson
    *           the pricePerPerson to set
    */
   public void setPricePerPerson(final List<String> pricePerPerson)
   {
      this.pricePerPerson = pricePerPerson;
   }

   /**
    * @return the itineraryCount
    */
   public int getItineraryCount()
   {
      return itineraryCount;
   }

   /**
    * @param itineraryCount
    *           the itineraryCount to set
    */
   public void setItineraryCount(final int itineraryCount)
   {
      this.itineraryCount = itineraryCount;
   }

   /**
    * @return the searchType
    */
   public String getSearchType()
   {
      return searchType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchType(final String searchType)
   {
      this.searchType = searchType;
   }

   /**
    * @return the crossSellAirportData
    */
   public CrossSellAirportData getCrossSellAirportData()
   {
      return crossSellAirportData;
   }

   /**
    * @param crossSellAirportData
    *           the crossSellAirportData to set
    */
   public void setCrossSellAirportData(final CrossSellAirportData crossSellAirportData)
   {
      this.crossSellAirportData = crossSellAirportData;
   }

   /**
    * @return the departureFlightsInfo
    */
   public List<AirportData> getDepartureFlightsInfo()
   {
      return departureFlightsInfo;
   }

   /**
    * @param departureFlightsInfo
    *           the departureFlightsInfo to set
    */
   public void setDepartureFlightsInfo(final List<AirportData> departureFlightsInfo)
   {
      this.departureFlightsInfo = departureFlightsInfo;
   }

   /**
    * @return the arrivalFLightsInfo
    */
   public List<AirportData> getArrivalFLightsInfo()
   {
      return arrivalFLightsInfo;
   }

   /**
    * @param arrivalFLightsInfo
    *           the arrivalFLightsInfo to set
    */
   public void setArrivalFLightsInfo(final List<AirportData> arrivalFLightsInfo)
   {
      this.arrivalFLightsInfo = arrivalFLightsInfo;
   }


}
