/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

public class SearchResultFlightDetailViewData
{

   private String departureAirport;

   private String departureAirportCode;

   private String arrivalAirport;

   private String arrivalAirportCode;

   private boolean dreamLinerIndicator;

   private String haulType;

   private SearchResultFlightScheduleData schedule = new SearchResultFlightScheduleData();

   private FlightCarrier carrier = new FlightCarrier();

   private Boolean externalInventory;

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
    * @return the departureAirport
    */
   public String getDepartureAirport()
   {
      return departureAirport;
   }

   /**
    * @return the arrivalAirport
    */
   public String getArrivalAirport()
   {
      return arrivalAirport;
   }

   /**
    * @return the schedule
    */
   public SearchResultFlightScheduleData getSchedule()
   {
      return schedule;
   }

   /**
    * @param departureAirport the departureAirport to set
    */
   public void setDepartureAirport(final String departureAirport)
   {
      this.departureAirport = departureAirport;
   }

   /**
    * @param arrivalAirport the arrivalAirport to set
    */
   public void setArrivalAirport(final String arrivalAirport)
   {
      this.arrivalAirport = arrivalAirport;
   }

   /**
    * @param schedule the schedule to set
    */
   public void setSchedule(final SearchResultFlightScheduleData schedule)
   {
      this.schedule = schedule;
   }

   /**
    * @return the carrier
    */
   public FlightCarrier getCarrier()
   {
      return carrier;
   }

   /**
    * @param carrier the carrier to set
    */
   public void setCarrier(final FlightCarrier carrier)
   {
      this.carrier = carrier;
   }

   /**
    * @return the haulType
    */
   public String getHaulType()
   {
      return haulType;
   }

   /**
    * @param haulType the haulType to set
    */
   public void setHaulType(final String haulType)
   {
      this.haulType = haulType;
   }

   /**
    * @return the arrivalAirportCode
    */
   public String getArrivalAirportCode()
   {
      return arrivalAirportCode;
   }

   /**
    * @param arrivalAirportCode the arrivalAirportCode to set
    */
   public void setArrivalAirportCode(final String arrivalAirportCode)
   {
      this.arrivalAirportCode = arrivalAirportCode;
   }

   /**
    * @return the departureAirportCode
    */
   public String getDepartureAirportCode()
   {
      return departureAirportCode;
   }

   /**
    * @param departureAirportCode the departureAirportCode to set
    */
   public void setDepartureAirportCode(final String departureAirportCode)
   {
      this.departureAirportCode = departureAirportCode;
   }

   /**
    * @return the dreamLinerIndicator
    */
   public boolean isDreamLinerIndicator()
   {
      return dreamLinerIndicator;
   }

   /**
    * @param dreamLinerIndicator the dreamLinerIndicator to set
    */
   public void setDreamLinerIndicator(final boolean dreamLinerIndicator)
   {
      this.dreamLinerIndicator = dreamLinerIndicator;
   }

}
