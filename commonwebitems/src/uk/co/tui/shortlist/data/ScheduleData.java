/**
 *
 */
package uk.co.tui.shortlist.data;

import org.joda.time.DateTime;

/**
 * @author akhileshvarma.d
 *
 */
public class ScheduleData
{

   private String arrTime;

   private String depTime;

   private DateTime arrivalDate;

   private DateTime departureDate;

   /**
    * @return the arrTime
    */
   public String getArrTime()
   {
      return arrTime;
   }

   /**
    * @param arrTime the arrTime to set
    */
   public void setArrTime(final String arrTime)
   {
      this.arrTime = arrTime;
   }

   /**
    * @return the depTime
    */
   public String getDepTime()
   {
      return depTime;
   }

   /**
    * @param depTime the depTime to set
    */
   public void setDepTime(final String depTime)
   {
      this.depTime = depTime;
   }

   /**
    * @return the arrivalDate
    */
   public DateTime getArrivalDate()
   {
      return arrivalDate;
   }

   /**
    * @param arrivalDate the arrivalDate to set
    */
   public void setArrivalDate(final DateTime arrivalDate)
   {
      this.arrivalDate = arrivalDate;
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
}
