/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightScheduleData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.SingleAccomSearchResultViewData;
import uk.co.tui.web.common.enums.SingleAccomWireframeType;

public class SingleAccomSearchResultsPopulator implements
   Populator<SearchResultsViewData, SingleAccomSearchResultViewData>
{

   /**
    * This method populates holiday object required for view of single search results
    *
    * @param source - SearchResultsViewData
    * @param target - SingleAccomSearchResultViewData
    */
   @Override
   public void populate(final SearchResultsViewData source,
      final SingleAccomSearchResultViewData target) throws ConversionException
   {

      final Iterator<SearchResultViewData> itr = source.getHolidays().iterator();
      final Set<String> dates = new TreeSet<String>();
      final Set<String> airports = new TreeSet<String>();
      final Set<String> flights = new TreeSet<String>();

      iterateSearchResultViewData(target, itr, dates, airports, flights);

      // initial setting of wireframe type
      target.setWireframeType(SingleAccomWireframeType.SINGLEDATEAIRPORTFLIGHT.getCode());

      // deterministic setting of wireframe type
      setWireframeTypes(target);

      source.setSingleAccomViewData(target);
      source.setSingleAccomSearch(true);

   }

   /**
    * @param target
    * @param itr
    * @param dates
    * @param airports
    * @param flights
    */
   private void iterateSearchResultViewData(final SingleAccomSearchResultViewData target,
      final Iterator<SearchResultViewData> itr, final Set<String> dates,
      final Set<String> airports, final Set<String> flights)
   {
      while (itr.hasNext())
      {
         final SearchResultViewData viewData = itr.next();

         setMultipleDatesFlag(target, dates, viewData);

         setMultipleAirportsFlag(target, airports, viewData);

         setMultipleFlightsFlag(target, flights, viewData);

         if (target.isMultipleDates() && target.isMultipleAirports() && target.isMultipleFilghts())
         {
            break;
         }
      }
   }

   /**
    * @param target
    * @param flights
    * @param viewData
    */
   private void setMultipleFlightsFlag(final SingleAccomSearchResultViewData target,
      final Set<String> flights, final SearchResultViewData viewData)
   {
      if (!target.isMultipleFilghts())
      {
         addFlights(flights, viewData);

         if (flights.size() > 1)
         {
            target.setMultipleFilghts(true);
         }
      }
   }

   /**
    * @param flights
    * @param viewData
    */
   private void addFlights(final Set<String> flights, final SearchResultViewData viewData)
   {
      final List<SearchResultFlightDetailViewData> outBounds =
         viewData.getItinerary().getOutbounds();
      final List<SearchResultFlightDetailViewData> inBounds = viewData.getItinerary().getInbounds();

      if (CollectionUtils.isNotEmpty(outBounds) && CollectionUtils.isNotEmpty(inBounds))
      {
         final SearchResultFlightScheduleData flightScheduleOutBound =
            outBounds.get(0).getSchedule();
         final SearchResultFlightScheduleData flightScheduleInBound = inBounds.get(0).getSchedule();

         flights.add((new StringBuilder())
            .append(flightScheduleOutBound.getDepartureDateTimeInMilli())
            .append(flightScheduleOutBound.getArrivalDateTimeInMilli())
            .append(flightScheduleInBound.getDepartureDateTimeInMilli())
            .append(flightScheduleInBound.getArrivalDateTimeInMilli()).toString());
      }
   }

   /**
    * @param target
    * @param airports
    * @param viewData
    */
   private void setMultipleAirportsFlag(final SingleAccomSearchResultViewData target,
      final Set<String> airports, final SearchResultViewData viewData)
   {
      if (!target.isMultipleAirports())
      {
         airports.add(viewData.getItinerary().getDepartureAirport());

         if (airports.size() > 1)
         {
            target.setMultipleAirports(true);
         }

      }
   }

   /**
    * @param target
    * @param dates
    * @param viewData
    */
   private void setMultipleDatesFlag(final SingleAccomSearchResultViewData target,
      final Set<String> dates, final SearchResultViewData viewData)
   {
      if (!target.isMultipleDates())
      {
         dates.add(viewData.getItinerary().getDepartureDate());

         if (dates.size() > 1)
         {
            target.setMultipleDates(true);
         }

      }
   }

   /**
    * @param target
    */
   private void setWireframeTypes(final SingleAccomSearchResultViewData target)
   {
      if (target.isMultipleDates())
      {
         setMultipleDateWireframes(target);
      }
      else
      {
         setSingleDateWireframes(target);
      }
   }

   /**
    * @param target
    */
   private void setSingleDateWireframes(final SingleAccomSearchResultViewData target)
   {
      if (target.isMultipleAirports())
      {
         target.setWireframeType(SingleAccomWireframeType.SINGLEDATEMULTIPLEAIRPORTS.getCode());
      }
      else if (target.isMultipleFilghts())
      {
         target.setWireframeType(SingleAccomWireframeType.SINGLEDATEAIRPORTMULTIPLEFLIGHTS
            .getCode());
      }
   }

   /**
    * @param target
    */
   private void setMultipleDateWireframes(final SingleAccomSearchResultViewData target)
   {
      if (target.isMultipleAirports())
      {
         target.setWireframeType(SingleAccomWireframeType.MULTIPLEDATESAIRPORTS.getCode());
      }
      else
      {
         target.setWireframeType(SingleAccomWireframeType.MULTIPLEDATESSINGLEAIRPORT.getCode());
      }
   }

}
