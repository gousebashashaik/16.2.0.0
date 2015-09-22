/**
 *
 */
package uk.co.tui.shortlist.data;

import org.joda.time.DateTime;

import uk.co.tui.shortlist.view.data.AirportViewData;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistLegData
{
   private AirportViewData arrivalAirport;

   private AirportViewData departureAirport;

   private ScheduleData schedule;

   private ShortlistFlightData carrier = new ShortlistFlightData();

   private Boolean externalInventory;

   private DateTime cycDate;

   private String routeCd;

   private String atcomId;

   /**
    * @return the arrivalAirport
    */
   public AirportViewData getArrivalAirport()
   {
      return arrivalAirport;
   }

   /**
    * @param arrivalAirport the arrivalAirport to set
    */
   public void setArrivalAirport(final AirportViewData arrivalAirport)
   {
      this.arrivalAirport = arrivalAirport;
   }

   /**
    * @return the departureAirport
    */
   public AirportViewData getDepartureAirport()
   {
      return departureAirport;
   }

   /**
    * @param departureAirport the departureAirport to set
    */
   public void setDepartureAirport(final AirportViewData departureAirport)
   {
      this.departureAirport = departureAirport;
   }

   /**
    * @return the schedule
    */
   public ScheduleData getSchedule()
   {
      return schedule;
   }

   /**
    * @param schedule the schedule to set
    */
   public void setSchedule(final ScheduleData schedule)
   {
      this.schedule = schedule;
   }

   /**
    * @return the carrier
    */
   public ShortlistFlightData getCarrier()
   {
      return carrier;
   }

   /**
    * @param carrier the carrier to set
    */
   public void setCarrier(final ShortlistFlightData carrier)
   {
      this.carrier = carrier;
   }

   /**
    * @return the externalInventory
    */
   public Boolean getExternalInventory()
   {
      return externalInventory;
   }

   /**
    * @param externalInventory the externalInventory to set
    */
   public void setExternalInventory(final Boolean externalInventory)
   {
      this.externalInventory = externalInventory;
   }

   /**
    * @return the cycDate
    */
   public DateTime getCycDate()
   {
      return cycDate;
   }

   /**
    * @param cycDate the cycDate to set
    */
   public void setCycDate(final DateTime cycDate)
   {
      this.cycDate = cycDate;
   }

   /**
    * @return the routeCd
    */
   public String getRouteCd()
   {
      return routeCd;
   }

   /**
    * @param routeCd the routeCd to set
    */
   public void setRouteCd(final String routeCd)
   {
      this.routeCd = routeCd;
   }

   /**
    * @return the atcomId
    */
   public String getAtcomId()
   {
      return atcomId;
   }

   /**
    * @param atcomId the atcomId to set
    */
   public void setAtcomId(final String atcomId)
   {
      this.atcomId = atcomId;
   }

}
