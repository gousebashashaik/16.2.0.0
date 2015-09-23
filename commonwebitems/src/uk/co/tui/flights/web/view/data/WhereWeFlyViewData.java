/**
 *
 */
package uk.co.tui.flights.web.view.data;

import java.util.List;
import java.util.Map;

import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.AirportSearchResult;
import uk.co.tui.flights.pojo.AirportMapData;
import uk.co.tui.flights.pojo.ContinentData;


/**
 * This class holds response data required for where we fly page
 *
 * @author lokesh.k
 *
 */
public class WhereWeFlyViewData
{
   private List<AirportMapData> airportMapData;

   private Map<String, List<AirportData>> destinationData;

   private List<AirportData> ukAirportList;
   private List<AirportData> overseasAirportList;

   private AirportSearchResult airportSearchResult;

   private List<ContinentData> continentData;

   /**
    * @return the overseasAirportList
    */
   public List<AirportData> getOverseasAirportList()
   {
      return overseasAirportList;
   }

   /**
    * @param overseasAirportList
    *           the overseasAirportList to set
    */
   public void setOverseasAirportList(final List<AirportData> overseasAirportList)
   {
      this.overseasAirportList = overseasAirportList;
   }

   /**
    * @return the airportMapData
    */
   public List<AirportMapData> getAirportMapData()
   {
      return airportMapData;
   }

   /**
    * @param airportMapData
    *           the airportMapData to set
    */
   public void setAirportMapData(final List<AirportMapData> airportMapData)
   {
      this.airportMapData = airportMapData;
   }

   /**
    * @return the destinationData
    */
   public Map<String, List<AirportData>> getDestinationData()
   {
      return destinationData;
   }

   /**
    * @param destinationData
    *           the destinationData to set
    */
   public void setDestinationData(final Map<String, List<AirportData>> destinationData)
   {
      this.destinationData = destinationData;
   }

   /**
    * @return the ukAirportList
    */
   public List<AirportData> getUkAirportList()
   {
      return ukAirportList;
   }

   /**
    * @param ukAirportList
    *           the ukAirportList to set
    */
   public void setUkAirportList(final List<AirportData> ukAirportList)
   {
      this.ukAirportList = ukAirportList;
   }

   /**
    * @return the airportSearchResult
    */
   public AirportSearchResult getAirportSearchResult()
   {
      return airportSearchResult;
   }

   /**
    * @param airportSearchResult
    *           the airportSearchResult to set
    */
   public void setAirportSearchResult(final AirportSearchResult airportSearchResult)
   {
      this.airportSearchResult = airportSearchResult;
   }

   /**
    * @return the continentData
    */
   public List<ContinentData> getContinentData()
   {
      return continentData;
   }

   /**
    * @param continentData
    *           the continentData to set
    */
   public void setContinentData(final List<ContinentData> continentData)
   {
      this.continentData = continentData;
   }

}
