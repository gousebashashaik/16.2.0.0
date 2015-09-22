/**
 *
 */
package uk.co.tui.book.view.data;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class holds the Flights-Leg related view data.
 *
 * @author madhumathi.m
 *
 */
public class Leg
{

   /** The arrival airport. */
   private Airport arrivalAirport;

   /** The departure airport. */
   private Airport departureAirport;

   /** The schedule. */
   private Schedule schedule;

   /** The jnrDuration. */
   private String jnrDuration = StringUtils.EMPTY;

   /** The eqmtDescription for flight. */
   private String eqmtDescription = StringUtils.EMPTY;

   /** The carrierCode for flight. */
   private String carrierCode = StringUtils.EMPTY;

   /** The carrierName for flight. */
   private String carrierName = StringUtils.EMPTY;

   /**
    * @return the arrivalAirport
    */
   public Airport getArrivalAirport()
   {
      if (this.arrivalAirport == null)
      {
         this.arrivalAirport = new Airport();
      }
      return this.arrivalAirport;
   }

   /**
    * @param arrivalAirport the arrivalAirport to set
    */
   public void setArrivalAirport(final Airport arrivalAirport)
   {
      this.arrivalAirport = arrivalAirport;
   }

   /**
    * @return the departureAirport
    */
   public Airport getDepartureAirport()
   {
      if (this.departureAirport == null)
      {
         this.departureAirport = new Airport();
      }
      return this.departureAirport;
   }

   /**
    * @param departureAirport the departureAirport to set
    */
   public void setDepartureAirport(final Airport departureAirport)
   {
      this.departureAirport = departureAirport;
   }

   /**
    * @return the schedule
    */
   public Schedule getSchedule()
   {
      if (this.schedule == null)
      {
         this.schedule = new Schedule();
      }
      return this.schedule;
   }

   /**
    * @param schedule the schedule to set
    */
   public void setSchedule(final Schedule schedule)
   {
      this.schedule = schedule;
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
    * @return the jnrDuration
    */
   public String getJnrDuration()
   {
      return jnrDuration;
   }

   /**
    * @param jnrDuration the jnrDuration to set
    */
   public void setJnrDuration(final String jnrDuration)
   {
      this.jnrDuration = jnrDuration;
   }

   /**
    * @return the eqmtDescription
    */
   public String getEqmtDescription()
   {
      return eqmtDescription;
   }

   /**
    * @param eqmtDescription the eqmtDescription to set
    */
   public void setEqmtDescription(final String eqmtDescription)
   {
      this.eqmtDescription = eqmtDescription;
   }

   /**
    * @return the carrierCode
    */
   public String getCarrierCode()
   {
      return carrierCode;
   }

   /**
    * @param carrierCode the carrierCode to set
    */
   public void setCarrierCode(final String carrierCode)
   {
      this.carrierCode = carrierCode;
   }

   /**
    * @return the carrierName
    */
   public String getCarrierName()
   {
      return carrierName;
   }

   /**
    * @param carrierName the carrierName to set
    */
   public void setCarrierName(final String carrierName)
   {
      this.carrierName = carrierName;
   }
}
