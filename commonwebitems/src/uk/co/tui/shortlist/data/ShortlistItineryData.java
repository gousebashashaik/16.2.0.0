/**
 *
 */
package uk.co.tui.shortlist.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistItineryData
{

   private List<ShortlistLegData> inbound = new ArrayList<ShortlistLegData>();

   private List<ShortlistLegData> outbound = new ArrayList<ShortlistLegData>();

   private DateTime departureTime;

   private String departureAirport;

   /**
    * @return the inbound
    */
   public List<ShortlistLegData> getInbound()
   {
      return inbound;
   }

   /**
    * @param inbound the inbound to set
    */
   public void setInbound(final List<ShortlistLegData> inbound)
   {
      this.inbound = inbound;
   }

   /**
    * @return the outbound
    */
   public List<ShortlistLegData> getOutbound()
   {
      return outbound;
   }

   /**
    * @param outbound the outbound to set
    */
   public void setOutbound(final List<ShortlistLegData> outbound)
   {
      this.outbound = outbound;
   }

   /**
    * @return the departureTime
    */
   public DateTime getDepartureTime()
   {
      return departureTime;
   }

   /**
    * @param departureTime the departureTime to set
    */
   public void setDepartureTime(final DateTime departureTime)
   {
      this.departureTime = departureTime;
   }

   /**
    * @return the departureAirport
    */
   public String getDepartureAirport()
   {
      return departureAirport;
   }

   /**
    * @param departureAirport the departureAirport to set
    */
   public void setDepartureAirport(final String departureAirport)
   {
      this.departureAirport = departureAirport;
   }

}
