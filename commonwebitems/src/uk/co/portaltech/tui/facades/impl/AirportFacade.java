package uk.co.portaltech.tui.facades.impl;

import static uk.co.portaltech.commons.Collections.map;
import static uk.co.portaltech.commons.Collections.toList;
import static uk.co.portaltech.commons.DateUtils.toDate;
import static uk.co.portaltech.tui.web.view.data.AirportData.toAirportData;
import static uk.co.portaltech.tui.web.view.data.AirportGuideData.toAirportGuideData;
import static uk.co.portaltech.tui.web.view.data.wrapper.UnitData.toUnitData;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.Collections.MapFn;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.airport.Airport;
import uk.co.portaltech.travel.model.airport.AirportGuide;
import uk.co.portaltech.travel.model.airport.Airports;
import uk.co.portaltech.travel.model.unit.Unit;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.travel.services.airport.AirportServiceException;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.AirportGuideData;
import uk.co.portaltech.tui.web.view.data.AirportSearchResult;
import uk.co.portaltech.tui.web.view.data.SearchError;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.tui.async.logging.TUILogUtils;

public class AirportFacade
{

   private static final String NO_MATCH_FOUND = "NO_MATCH_FOUND";

   @Resource
   private AirportService airportsService;

   @Resource(name = "mainStreamTravelLocationService")
   private MainStreamTravelLocationService mstravelLocationService;

   @Resource
   private CMSSiteService cmsSiteService;

   private static final String INVALID_ROUTE_TO = "INVALID_ROUTE_TO";

   private static final String INVALID_ROUTE_ON = "INVALID_ROUTE_ON";
   
   private static final String AIRPORT_QUERY = "SELECT {pk} FROM {Airport} WHERE {code}=?code and {catalogVersion}=?catalogVersion";

   @Resource
   private ViewSelector viewSelector;
   
   @Resource
   private FlexibleSearchService flexibleSearchService;

   private static final TUILogUtils LOGGER = new TUILogUtils("AirportFacade");

   private final MapFn<Unit, UnitData> unitToUnitDataMapper = new MapFn<Unit, UnitData>()
   {
      @Override
      public UnitData call(final Unit input)
      {
         return toUnitData(input);

      }
   };

   private static final MapFn<Airport, AirportData> AIRPORT_TO_AIRPORT_DATA_MAPPER =
      new MapFn<Airport, AirportData>()
      {
         @Override
         public AirportData call(final Airport input)
         {
            return toAirportData(input);
         }
      };

   private static final MapFn<String, LocalDate> LOCATE_DATE_MAPPER =
      new MapFn<String, LocalDate>()
      {
         @Override
         public LocalDate call(final String date)
         {
            return toDate(date);
         }
      };

   private static final String FJ = "FJ";
   private static final String TH = "TH";
   private static final String FC = "FC";
   
   private static final String NO_MATCH_FOUND_SEARCH_KEY = "No Match found for the search key";

   public AirportFacade(final AirportService airportService, final CMSSiteService cmsSiteService)
   {
      this.airportsService = airportService;
      this.cmsSiteService = cmsSiteService;
   }

   public AirportFacade()
   {

   }

   public AirportSearchResult find(final String key, final List<String> units,
      final List<String> dates, final List<String> releventBrands)
   {
      final AirportSearchResult searchResult = new AirportSearchResult();
      final List<Airport> airports = new ArrayList<Airport>();
      boolean isFuzzyResult = false;

      try
      {
         if (CollectionUtils.isEmpty(units) && CollectionUtils.isEmpty(dates))
         {
            airports.addAll(airportsService.search(key));
            // added by RukminiKrishna
            final Airport dummy = new Airport("NOMATCH", "Similar spellings");
            if (airports.remove(dummy))
            {
               isFuzzyResult = true;
            }

            removeNonUKAirportsFromTHandFC(airports, releventBrands, searchResult);
            if (CollectionUtils.isEmpty(airports))
            {
               searchResult.setError(new SearchError(NO_MATCH_FOUND, 
                    NO_MATCH_FOUND_SEARCH_KEY));
               searchResult.setAirports(Collections.<AirportData> emptyList());
            }
         }
         else
         {
            final Airports aiportsResult =
               airportsService.search(key, units, toLocalDates(dates), releventBrands);
            airports.addAll(aiportsResult.airportsWithRoute());
            isFuzzyResult = aiportsResult.isNomatch();
            searchResult.setError(searchError(aiportsResult, units, releventBrands));
            removeNonUKAirportsFromTHandFC(airports, releventBrands, searchResult);
         }
         // Added by RukminiKrishna
         searchResult.setNomatch(isFuzzyResult);
      }
      catch (final AirportServiceException ex)
      {
         LOGGER.error("AirportServiceException", ex);
         searchResult
            .setError(new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_SEARCH_KEY));
         searchResult.setAirports(Collections.<AirportData> emptyList());
         return searchResult;
      }

      searchResult.setAirports(map(airportsService.sortAirports(airports),
         AIRPORT_TO_AIRPORT_DATA_MAPPER));
      return searchResult;
   }
   
   /**
    * Remove non UK airports from TH and FC.
    * @param airports
    * @param releventBrands
    * @param searchResult 
    */
   private void removeNonUKAirportsFromTHandFC(List<Airport> airports, List<String> releventBrands, AirportSearchResult searchResult)
   {
      List<Airport> airportListToBeRemoved = new ArrayList<Airport>();
      if(releventBrands.contains(TH) || releventBrands.contains(FC))
      {
         for(Airport airport : airports)
         {
            airportListToBeRemoved.add(isNonUKAirport(airport));
         }
      }
      airports.removeAll(airportListToBeRemoved);
      updateErrorMessageWhenAirportListIsEmpty(airports, searchResult);
   }
   
   /**
    * Updated the error message when no airports are found.
    * @param airports
    * @param searchResult
    */
   private void updateErrorMessageWhenAirportListIsEmpty(List<Airport> airports, AirportSearchResult searchResult)
   {
      if(CollectionUtils.isEmpty(airports))
      {
         searchResult.setError(new SearchError(NO_MATCH_FOUND, NO_MATCH_FOUND_SEARCH_KEY));
      }
   }
   
   /**
    * This method checks whether its non UK airport.
    * If yes then it returns the airport to be removed.
    * @param airport
    * @return airport to be removed
    */
   private Airport isNonUKAirport(Airport airport)
   {
      final FlexibleSearchQuery searchAirport = new FlexibleSearchQuery(AIRPORT_QUERY);
      searchAirport.addQueryParameter("code", airport.code());
      searchAirport.addQueryParameter("catalogVersion", cmsSiteService.getCurrentCatalogVersion().getPk());
      if (airport.children().isEmpty())
      {
         final AirportModel airportModel = flexibleSearchService.searchUnique(searchAirport);
         if (!("GBR".equals(airportModel.getCountry())))
         {
            return airport;
         }
      }
      else if ("SI".equals(airport.code()))
      {
         return airport;
      }
      return null;
   }

   private SearchError searchError(final Airports airportsResult, final List<String> units,
      final List<String> releventBrands)
   {
      return airportsResult.hasRoutes() ? null : determineError(airportsResult, units,
         releventBrands);
   }

   private SearchError determineError(final Airports airportsResult, final List<String> units,
      final List<String> releventBrands)
   {
      final List<AirportData> airportDatas =
         map(airportsResult.matchedAirports(), AIRPORT_TO_AIRPORT_DATA_MAPPER);
      final List<AirportData> airportEntries = new ArrayList<AirportData>();
      airportEntries.add(airportDatas.get(0));

      final Collection<UnitData> matchedUnits = getMatchedUnits(units, releventBrands);

      if (!airportsResult.hasAirportsToSelectedDestinations()
         && !airportsResult.hasAirportsOnSelectedDates())
      {
         return null;
      }
      return airportsResult.hasAirportsToSelectedDestinations() ? new SearchError(INVALID_ROUTE_ON,
         "No Route Found for selected Dates", airportEntries, toList(matchedUnits))
         : new SearchError(INVALID_ROUTE_TO, "No Route Found to selected destinations",
            airportEntries, toList(matchedUnits));

   }

   public List<UnitData> getMatchedUnits(final List<String> unitCodes,
      final List<String> releventBrands)
   {

      final Set<Unit> unitList = new HashSet<Unit>();
      final List<Unit> requestedUnits = new ArrayList<Unit>();

      if (null != unitCodes)
      {
         for (final String code : unitCodes)
         {
            unitList.addAll(mstravelLocationService.searchDestination(code, releventBrands, null));
         }
      }
      if (!CollectionUtils.isEmpty(unitList))
      {
         for (final Unit unit : unitList)
         {
            for (final String unitCode : unitCodes)
            {
               if (StringUtils.equals(unit.id(), unitCode))
               {
                  requestedUnits.add(unit);
               }
            }
         }

         if (!CollectionUtils.isEmpty(requestedUnits))
         {
            return map(requestedUnits, unitToUnitDataMapper);
         }

      }
      return new ArrayList<UnitData>();

   }

   private List<LocalDate> toLocalDates(final List<String> dates)
   {
      return map(dates, LOCATE_DATE_MAPPER);
   }

   public AirportGuideData getAirportGuide(final List<String> units, final List<String> dates,
      final List<String> releventBrands)
   {
      AirportGuide airportGuide;
      if (CollectionUtils.isEmpty(units) && CollectionUtils.isEmpty(dates))
      {
         if (releventBrands.contains(FJ))
         {
            airportGuide =
               airportsService.fetchAirportGuideForFalcon(
                  cmsSiteService.getCurrentCatalogVersion(), FJ);
         }
         else
         {
            airportGuide =
               airportsService.fetchAirportGuide(cmsSiteService.getCurrentCatalogVersion());
         }

      }
      else
      {
         airportGuide =
            airportsService.fetchAirportGuide(cmsSiteService.getCurrentCatalogVersion(), units,
               toLocalDates(dates), releventBrands);

      }

      final AirportGuideData airportGuideData = toAirportGuideData(airportGuide);

      if (viewSelector.checkIsMobile())
      {
         boolean londonGroupPresent = false;
         int index = 0;
         for (final AirportData airport : airportGuideData.getAirports())
         {
            index++;
            if (airport.getGroup().contains("LN"))
            {
               londonGroupPresent = true;
               break;
            }
         }
         if (londonGroupPresent)
         {
            final List<AirportData> airportDataList = airportGuideData.getAirports();
            final AirportData airportData = new AirportData();
            airportData.setId("LN");
            airportData.setName("London");
            airportData.setAvailable(true);
            airportDataList.add(index - 1, airportData);
            int index1 = 0;
            for (final AirportData aiData : airportGuideData.getAirportGroups())
            {
               index1++;
               if (aiData.getId().contains("LN"))
               {
                  break;
               }
            }
            airportGuideData.getAirportGroups().remove(index1 - 1);
         }
      }

      return airportGuideData;
   }
}
