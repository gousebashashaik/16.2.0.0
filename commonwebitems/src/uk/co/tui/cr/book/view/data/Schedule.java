/**
 *
 */
package uk.co.tui.cr.book.view.data;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.tui.book.enums.FlightSlot;
import uk.co.tui.book.utils.FlightUtils;

/**
 * This class holds the flight schedule related view data.
 *
 * @author madhumathi.m
 *
 */
public class Schedule
{

   /**
    * Holds the arrival time of the flight.
    */
   private String arrTime = StringUtils.EMPTY;

   /**
    * Holds the departure time of the flight.
    */
   private String depTime = StringUtils.EMPTY;

   /**
    * Holds the arrival date of the flight.
    */
   private String arrivalDate = StringUtils.EMPTY;

   /**
    * Holds the departure date of the flight.
    */
   private String departureDate = StringUtils.EMPTY;

   /** The time of flight. */
   private String timeOfFlight = StringUtils.EMPTY;

   /** The departure date in mili seconds. */
   private String departureDateInMiliSeconds;

   /** The Arrival date in mili seconds. */
   private String arrivalDateInMiliSeconds;

   /** The slot. */
   private FlightSlot slot;

   /**
    * Holds the number of days from current date
    */
   private int flightOffsetDays;

   /** The flight offset days summary. */
   private String flightOffsetDaysSummary = StringUtils.EMPTY;

   /**
    * @return the flightOffsetDays
    */
   public int getFlightOffsetDays()
   {
      return flightOffsetDays;
   }

   /**
    * @param flightOffsetDays the flightOffsetDays to set
    */
   public void setFlightOffsetDays(final int flightOffsetDays)
   {
      this.flightOffsetDays = flightOffsetDays;
   }

   /**
    * Gets the slot.
    *
    * @return the slot
    */
   public FlightSlot getSlot()
   {
      return this.slot;
   }

   /**
    * Sets the slot.
    */
   public void setSlot()
   {
      if (StringUtils.isNotEmpty(depTime))
      {
         this.slot = FlightUtils.findFlightSlot(this.depTime);
      }
   }

   /**
    * Gets the time of flight.
    *
    * @return the time of flight
    */
   public String getTimeOfFlight()
   {
      return timeOfFlight;
   }

   /**
    * Sets the time of flight.
    *
    * @param timeOfFlight the new time of flight
    */
   public void setTimeOfFlight(final String timeOfFlight)
   {
      this.timeOfFlight = timeOfFlight;
   }

   /**
    * Gets the arr time.
    *
    * @return the arrTime
    */
   public String getArrTime()
   {
      return this.arrTime;
   }

   /**
    * Sets the arr time.
    *
    * @param arrTime the arrTime to set
    */
   public void setArrTime(final String arrTime)
   {
      this.arrTime = arrTime;
   }

   /**
    * Gets the dep time.
    *
    * @return the depTime
    */
   public String getDepTime()
   {
      return this.depTime;
   }

   /**
    * Sets the dep time.
    *
    * @param depTime the depTime to set
    */
   public void setDepTime(final String depTime)
   {
      this.depTime = depTime;
   }

   /**
    * Return the string representation of the object.
    *
    * @return the string representation of the object.
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return ToStringBuilder.reflectionToString(this);
   }

   /**
    * Gets the arrival date.
    *
    * @return the arrivalDate
    */
   public String getArrivalDate()
   {
      return arrivalDate;
   }

   /**
    * Sets the arrival date.
    *
    * @param arrivalDate the arrivalDate to set
    */
   public void setArrivalDate(final String arrivalDate)
   {
      this.arrivalDate = arrivalDate;
   }

   /**
    * Gets the departure date.
    *
    * @return the departureDate
    */
   public String getDepartureDate()
   {
      return departureDate;
   }

   /**
    * Sets the departure date.
    *
    * @param departureDate the departureDate to set
    */
   public void setDepartureDate(final String departureDate)
   {
      this.departureDate = departureDate;
   }

   /**
    * Gets the departure date in mili seconds.
    *
    * @return the departureDateInMiliSeconds
    */
   public String getDepartureDateInMiliSeconds()
   {
      return departureDateInMiliSeconds;
   }

   /**
    * Sets the departure date in mili seconds.
    *
    * @param departureDateInMiliSeconds the departureDateInMiliSeconds to set
    */
   public void setDepartureDateInMiliSeconds(final String departureDateInMiliSeconds)
   {
      this.departureDateInMiliSeconds = departureDateInMiliSeconds;
   }

   /**
    * Gets the arrival date in mili seconds.
    *
    * @return the arrivalDateInMiliSeconds
    */
   public String getArrivalDateInMiliSeconds()
   {
      return arrivalDateInMiliSeconds;
   }

   /**
    * Sets the arrival date in mili seconds.
    *
    * @param arrivalDateInMiliSeconds the arrivalDateInMiliSeconds to set
    */
   public void setArrivalDateInMiliSeconds(final String arrivalDateInMiliSeconds)
   {
      this.arrivalDateInMiliSeconds = arrivalDateInMiliSeconds;
   }

   /**
    * @return the flightOffsetDaysSummary
    */
   public String getFlightOffsetDaysSummary()
   {
      return flightOffsetDaysSummary;
   }

   /**
    * @param flightOffsetDaysSummary the flightOffsetDaysSummary to set
    */
   public void setFlightOffsetDaysSummary(final String flightOffsetDaysSummary)
   {
      this.flightOffsetDaysSummary = flightOffsetDaysSummary;
   }

}
