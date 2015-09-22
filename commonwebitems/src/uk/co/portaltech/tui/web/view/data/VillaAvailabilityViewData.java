/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import org.joda.time.DateTime;

/**
 * @author arya.ap
 *
 */
public class VillaAvailabilityViewData
{

   private String when;

   private DateTime departureDate;

   private String label;

   private int price;

   private int pricePP;

   private boolean availability;

   private boolean freeChildPlace;

   private boolean carHire;

   private int duration;

   private String airportCode;

   private String airportName;

   private String startday;

   private String endday;

   private String startDate;

   private String endDate;

   private long departureDateInMillis;

   /**
    * @return the availableFrom
    */

   /**
    * @return the label
    */
   public String getLabel()
   {
      return label;
   }

   /**
    * @return the when
    */

   /**
    * @return the when
    */
   public String getWhen()
   {
      return when;
   }

   /**
    * @param when the when to set
    */
   public void setWhen(final String when)
   {
      this.when = when;
   }

   /**
    * @param label the label to set
    */
   public void setLabel(final String label)
   {
      this.label = label;
   }

   /**
    * @return the price
    */
   public int getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final int price)
   {
      this.price = price;
   }

   /**
    * @return the availability
    */
   public boolean isAvailability()
   {
      return availability;
   }

   /**
    * @param availability the availability to set
    */
   public void setAvailability(final boolean availability)
   {
      this.availability = availability;
   }

   /**
    * @return the freeChildPlace
    */
   public boolean isFreeChildPlace()
   {
      return freeChildPlace;
   }

   /**
    * @param freeChildPlace the freeChildPlace to set
    */
   public void setFreeChildPlace(final boolean freeChildPlace)
   {
      this.freeChildPlace = freeChildPlace;
   }

   /**
    * @return the duration
    */
   public int getDuration()
   {
      return duration;
   }

   /**
    * @param duration the duration to set
    */
   public void setDuration(final int duration)
   {
      this.duration = duration;
   }

   /**
    * @return the carHire
    */
   public boolean isCarHire()
   {
      return carHire;
   }

   /**
    * @param carHire the carHire to set
    */
   public void setCarHire(final boolean carHire)
   {
      this.carHire = carHire;
   }

   /**
    * @return the pricePP
    */
   public int getPricePP()
   {
      return pricePP;
   }

   /**
    * @param pricePP the pricePP to set
    */
   public void setPricePP(final int pricePP)
   {
      this.pricePP = pricePP;
   }

   /**
    * @return the airportCode
    */
   public String getAirportCode()
   {
      return airportCode;
   }

   /**
    * @param airportCode the airportCode to set
    */
   public void setAirportCode(final String airportCode)
   {
      this.airportCode = airportCode;
   }

   /**
    * @return the airportName
    */
   public String getAirportName()
   {
      return airportName;
   }

   /**
    * @param airportName the airportName to set
    */
   public void setAirportName(final String airportName)
   {
      this.airportName = airportName;
   }

   /**
    * @return the departureDate
    */
   public DateTime getDepartureDate()
   {
      return departureDate;
   }

   /**
    * @param departureDate the departureDate to set
    */
   public void setDepartureDate(final DateTime departureDate)
   {
      this.departureDate = departureDate;
   }

   /**
    * @return the startday
    */
   public String getStartday()
   {
      return startday;
   }

   /**
    * @param startday the startday to set
    */
   public void setStartday(final String startday)
   {
      this.startday = startday;
   }

   /**
    * @return the endday
    */
   public String getEndday()
   {
      return endday;
   }

   /**
    * @param endday the endday to set
    */
   public void setEndday(final String endday)
   {
      this.endday = endday;
   }

   /**
    * @return the startDate
    */
   public String getStartDate()
   {
      return startDate;
   }

   /**
    * @param startDate the startDate to set
    */
   public void setStartDate(final String startDate)
   {
      this.startDate = startDate;
   }

   /**
    * @return the endDate
    */
   public String getEndDate()
   {
      return endDate;
   }

   /**
    * @param endDate the endDate to set
    */
   public void setEndDate(final String endDate)
   {
      this.endDate = endDate;
   }

   /**
    * @return the departureDateInMillis
    */
   public long getDepartureDateInMillis()
   {
      return departureDateInMillis;
   }

   /**
    * @param departureDateInMillis the departureDateInMillis to set
    */
   public void setDepartureDateInMillis(final long departureDateInMillis)
   {
      this.departureDateInMillis = departureDateInMillis;
   }
}
