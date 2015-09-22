/**
 *
 */
package uk.co.tui.manage.viewdata;

/**
 * @author sunil.bd
 *
 */
public class LegViewData
{

   private AirportViewData arrivalAirport;

   private AirportViewData departureAirport;

   private FlightScheduleViewData schedule;

   private FlightViewData carrier;

   private PriceViewData price;

   private String inventoryId;

   private String journeyDuration;

   private boolean externalInventory;

   /** The selected. */
   private boolean isDreamLiner;

   /**
    * @return the schedule
    */
   public FlightScheduleViewData getSchedule()
   {
      return schedule;
   }

   /**
    * @param schedule the schedule to set
    */
   public void setSchedule(final FlightScheduleViewData schedule)
   {
      this.schedule = schedule;
   }

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
    * @return the carrier
    */
   public FlightViewData getCarrier()
   {
      return carrier;
   }

   /**
    * @param carrier the carrier to set
    */
   public void setCarrier(final FlightViewData carrier)
   {
      this.carrier = carrier;
   }

   /**
    * @return the price
    */
   public PriceViewData getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final PriceViewData price)
   {
      this.price = price;
   }

   /**
    * @return the inventoryId
    */
   public String getInventoryId()
   {
      return inventoryId;
   }

   /**
    * @param inventoryId the inventoryId to set
    */
   public void setInventoryId(final String inventoryId)
   {
      this.inventoryId = inventoryId;
   }

   /**
    * @return the journeyDuration
    */
   public String getJourneyDuration()
   {
      return journeyDuration;
   }

   /**
    * @param journeyDuration the journeyDuration to set
    */
   public void setJourneyDuration(final String journeyDuration)
   {
      this.journeyDuration = journeyDuration;
   }

   /**
    * @return the externalInventory
    */
   public boolean isExternalInventory()
   {
      return externalInventory;
   }

   /**
    * @param externalInventory the externalInventory to set
    */
   public void setExternalInventory(final boolean externalInventory)
   {
      this.externalInventory = externalInventory;
   }

   /**
    * @return the isDreamLiner
    */
   public boolean isDreamLiner()
   {
      return isDreamLiner;
   }

   /**
    * @param isDreamLiner the isDreamLiner to set
    */
   public void setDreamLiner(final boolean isDreamLiner)
   {
      this.isDreamLiner = isDreamLiner;
   }

}
