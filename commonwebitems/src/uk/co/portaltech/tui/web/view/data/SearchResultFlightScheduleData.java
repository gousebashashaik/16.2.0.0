/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

public class SearchResultFlightScheduleData
{

   private String departureDate;

   private String departureTime;

   private long departureDateTimeInMilli;

   private String arrivalDate;

   private String arrivalTime;

   private long arrivalDateTimeInMilli;

   private boolean overlapDay;

   private String departureDateForMobile;

   private String arrivalDateForMobile;

   /**
    * @return the departureDateForMobile
    */
   public String getDepartureDateForMobile()
   {
      return departureDateForMobile;
   }

   /**
    * @param departureDateForMobile the departureDateForMobile to set
    */
   public void setDepartureDateForMobile(final String departureDateForMobile)
   {
      this.departureDateForMobile = departureDateForMobile;
   }

   /**
    * @return the arrivalDateForMobile
    */
   public String getArrivalDateForMobile()
   {
      return arrivalDateForMobile;
   }

   /**
    * @param arrivalDateForMobile the arrivalDateForMobile to set
    */
   public void setArrivalDateForMobile(final String arrivalDateForMobile)
   {
      this.arrivalDateForMobile = arrivalDateForMobile;
   }

   /**
    * @return the departureDate
    */
   public String getDepartureDate()
   {
      return departureDate;
   }

   /**
    * @return the departureTime
    */
   public String getDepartureTime()
   {
      return departureTime;
   }

   /**
    * @return the arrivalDate
    */
   public String getArrivalDate()
   {
      return arrivalDate;
   }

   /**
    * @return the arrivalTime
    */
   public String getArrivalTime()
   {
      return arrivalTime;
   }

   /**
    * @return the overlapDay
    */
   public boolean getOverlapDay()
   {
      return overlapDay;
   }

   /**
    * @param departureDate the departureDate to set
    */
   public void setDepartureDate(final String departureDate)
   {
      this.departureDate = departureDate;
   }

   /**
    * @param departureTime the departureTime to set
    */
   public void setDepartureTime(final String departureTime)
   {
      this.departureTime = departureTime;
   }

   /**
    * @param arrivalDate the arrivalDate to set
    */
   public void setArrivalDate(final String arrivalDate)
   {
      this.arrivalDate = arrivalDate;
   }

   /**
    * @param arrivalTime the arrivalTime to set
    */
   public void setArrivalTime(final String arrivalTime)
   {
      this.arrivalTime = arrivalTime;
   }

   /**
    * @param overlapDay the overlapDay to set
    */
   public void setOverlapDay(final boolean overlapDay)
   {
      this.overlapDay = overlapDay;
   }

   /**
    * @return the departureDateTimeInMilli
    */
   public long getDepartureDateTimeInMilli()
   {
      return departureDateTimeInMilli;
   }

   /**
    * @return the arrivalDateTimeInMilli
    */
   public long getArrivalDateTimeInMilli()
   {
      return arrivalDateTimeInMilli;
   }

   /**
    * @param departureDateTimeInMilli the departureDateTimeInMilli to set
    */
   public void setDepartureDateTimeInMilli(final long departureDateTimeInMilli)
   {
      this.departureDateTimeInMilli = departureDateTimeInMilli;
   }

   /**
    * @param arrivalDateTimeInMilli the arrivalDateTimeInMilli to set
    */
   public void setArrivalDateTimeInMilli(final long arrivalDateTimeInMilli)
   {
      this.arrivalDateTimeInMilli = arrivalDateTimeInMilli;
   }

}
