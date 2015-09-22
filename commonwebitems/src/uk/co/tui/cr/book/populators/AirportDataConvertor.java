/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.Collections;
import uk.co.portaltech.commons.Collections.MapFn;
import uk.co.portaltech.travel.model.airport.Airport;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.tui.web.view.data.AirportData;

/**
 * The Class AirportDataToAirportPopulator.
 * 
 * @author samantha.gd
 */
public class AirportDataConvertor
{

   /** The airport service. */

   @Resource
   private AirportService airportsService;

   /** The cr airport data to airport populator. */
   @Resource
   private Populator<AirportData, uk.co.tui.book.domain.Airport> crAirportDataToAirportPopulator;

   /** The airport to airport data mapper. */
   private final MapFn<Airport, AirportData> airportToAirportDataMapper =
      new MapFn<Airport, AirportData>()
      {
         @Override
         public AirportData call(final Airport input)
         {
            return AirportData.toAirportData(input);
         }
      };

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */

   /**
    * Fetch airport details.
    * 
    * @param airportCodes the airport codes
    * @return the list
    */
   public List<uk.co.tui.book.domain.Airport> fetchAirportDetails(final List<String> airportCodes)
   {
      final List<AirportData> airportData = getAirports(airportCodes);
      final List<uk.co.tui.book.domain.Airport> targetAirports =
         new ArrayList<uk.co.tui.book.domain.Airport>();
      for (final AirportData airportDatum : airportData)
      {
         final uk.co.tui.book.domain.Airport target = new uk.co.tui.book.domain.Airport();
         crAirportDataToAirportPopulator.populate(airportDatum, target);
         targetAirports.add(target);
      }
      return targetAirports;
   }

   /**
    * Gets the airports.
    * 
    * @param departureCodes the departure codes
    * @return the airports
    * @returns departure list of departure airportData objects for given array of airport codes
    */
   private List<AirportData> getAirports(final List<String> departureCodes)
   {
      final List<Airport> airportList = new ArrayList<Airport>();
      final Set<Airport> requestedAirports = new HashSet<Airport>();

      final Collection<String> airportsCollection = new ArrayList<String>();
      airportsCollection.addAll(departureCodes);

      for (final String airport : airportsCollection)
      {
         airportList.addAll(airportsService.getAirportsByCode(airport));
      }
      if (CollectionUtils.isNotEmpty(airportList))
      {
         for (final Airport airport : airportList)
         {
            for (final String depCode : departureCodes)
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
         return Collections.map(requestedAirports, airportToAirportDataMapper);
      }
      return new ArrayList<AirportData>();
   }

}
