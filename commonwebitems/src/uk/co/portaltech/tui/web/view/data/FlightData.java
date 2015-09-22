/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author extcs5
 *
 */
public class FlightData
{

   private String name;

   private FilterData departureTimeGoing;

   private FilterData departureTimeComing;

   private FilterData departureAirports;

   /**
    * @return the departureTimeGoing
    */
   public FilterData getDepartureTimeGoing()
   {
      return departureTimeGoing;
   }

   /**
    * @param departureTimeGoing the departureTimeGoing to set
    */
   public void setDepartureTimeGoing(final FilterData departureTimeGoing)
   {
      this.departureTimeGoing = departureTimeGoing;
   }

   /**
    * @return the departureTimeComing
    */
   public FilterData getDepartureTimeComing()
   {
      return departureTimeComing;
   }

   /**
    * @param departureTimeComing the departureTimeComing to set
    */
   public void setDepartureTimeComing(final FilterData departureTimeComing)
   {
      this.departureTimeComing = departureTimeComing;
   }

   /**
    * @return the departureAirports
    */
   public FilterData getDepartureAirports()
   {
      return departureAirports;
   }

   /**
    * @param departureAirports the departureAirports to set
    */
   public void setDepartureAirports(final FilterData departureAirports)
   {
      this.departureAirports = departureAirports;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

}
