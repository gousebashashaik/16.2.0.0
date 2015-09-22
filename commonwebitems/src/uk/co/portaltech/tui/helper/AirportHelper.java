/**
 *
 */
package uk.co.portaltech.tui.helper;

import static uk.co.portaltech.commons.Collections.map;
import static uk.co.portaltech.tui.web.view.data.AirportData.toAirportData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.Collections.MapFn;
import uk.co.portaltech.travel.model.airport.Airport;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author manju.ts
 *
 */
public class AirportHelper
{

   @Resource(name = "airportsService")
   private AirportService airportService;

   private static final MapFn<Airport, AirportData> AIRPORT_TO_AIRPORT_DATA_MAPPER =
      new MapFn<Airport, AirportData>()
      {
         @Override
         public AirportData call(final Airport input)
         {
            return toAirportData(input);
         }
      };

   /**
    * @param request
    * @returns departure list of departure airportData objects for given array of airport codes
    */
   public List<AirportData> getAirports(final SearchResultsRequestData request)
   {

      final List<String> airportsCollection = new ArrayList<String>();
      for (final AirportData airport : request.getAirports())
      {
         airportsCollection.add(airport.getId());
      }
      final List<Airport> airportList = new ArrayList<Airport>();
      final List<Airport> requestedAirports = new ArrayList<Airport>();

      if (CollectionUtils.isEmpty(airportsCollection))
      {
         return new ArrayList<AirportData>();
      }

      final List<String> depCodes = airportsCollection;
      for (final String airprt : airportsCollection)
      {
         airportList.addAll(airportService.search(airprt));
      }
      if (CollectionUtils.isNotEmpty(airportList))
      {
         for (final Airport airport : airportList)
         {
            for (final String depCode : depCodes)
            {
               if (StringUtils.equals(airport.code(), depCode))
               {
                  requestedAirports.add(airport);
               }
            }
         }
      }

      if (!CollectionUtils.isEmpty(requestedAirports))
      {
         return map(requestedAirports, AIRPORT_TO_AIRPORT_DATA_MAPPER);
      }
      return new ArrayList<AirportData>();
   }

}
