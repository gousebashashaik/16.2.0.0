/**
 *
 */
package uk.co.tui.fj.book.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;

import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.FlightExtraFacilityResponse;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.LegExtraFacility;

/**
 * The Store to hold all Flight ExtraFacilities for different Packages.
 *
 * @author madhumathi.m
 *
 */
public class FlightExtraFacilityStore
{

   private Map<String, Map<String, List<ExtraFacility>>> seatGroupedFlightExtrasResponse =
      new HashMap<String, Map<String, List<ExtraFacility>>>();

   private Map<String, Map<String, Map<String, List<ExtraFacility>>>> cabinWiseGroupedFlightExtrasResponse =
      new HashMap<String, Map<String, Map<String, List<ExtraFacility>>>>();

   private static final Logger LOG = Logger.getLogger(FlightExtraFacilityStore.class);

   /**
    * Flushes the data stored in the store.
    */
   public void flush()
   {
      this.seatGroupedFlightExtrasResponse = null;
      this.cabinWiseGroupedFlightExtrasResponse = null;
   }

   /**
    * Gets the seat grouped flight extras response.
    *
    * @return the seat grouped flight extras response
    */
   private Map<String, Map<String, List<ExtraFacility>>> getSeatGroupedFlightExtrasResponse()
   {
      return seatGroupedFlightExtrasResponse;
   }

   /**
    * Gets the cabin wisegrouped flight extras response.
    *
    * @return the cabin wisegrouped flight extras response
    */
   private Map<String, Map<String, Map<String, List<ExtraFacility>>>> getCabinWiseGroupedFlightExtrasResponse()
   {
      return cabinWiseGroupedFlightExtrasResponse;
   }

   /**
    * Adds the flight extra facility response store.
    *
    * @param flightExtraFacilityResponse the flight extra facility response
    */
   public void addFlightExtraFacilityResponseToStore(
      final FlightExtraFacilityResponse flightExtraFacilityResponse)
   {
      // adding the seat extra facilities
      updateGroupedSeatFlightExtraFacilityResponseToStore(
         flightExtraFacilityResponse,
         getGroupedExtraFacilityMap(createCombinedSeatExtrasListFromAllLegs(flightExtraFacilityResponse
            .getLegExtras())));
      // adding the seat extra facilities
      updateGroupedFlightExtraFacilityResponseToStore(
         flightExtraFacilityResponse,
         getGroupedCabClsExtraFacilityMap(createCombinedFlightExtrasListFromAllLegs(flightExtraFacilityResponse
            .getLegExtras())));

   }

   /**
    * Update grouped seat flight extra facilities response to store.
    *
    * @param flightExtraFacilityResponse the flight extra facility response
    * @param extraFacilityMap the extra facility map
    */
   private void updateGroupedSeatFlightExtraFacilityResponseToStore(
      final FlightExtraFacilityResponse flightExtraFacilityResponse,
      final Map<List<String>, List<ExtraFacility>> extraFacilityMap)
   {
      final Map<String, List<ExtraFacility>> extraMap = new HashMap<String, List<ExtraFacility>>();
      for (final Entry<List<String>, List<ExtraFacility>> eachEntry : extraFacilityMap.entrySet())
      {
         extraMap.put(eachEntry.getKey().get(0), eachEntry.getValue());
      }
      this.getSeatGroupedFlightExtrasResponse().put(flightExtraFacilityResponse.getPackageId(),
         extraMap);

   }

   /**
    * Update grouped flight extra facility response to store excluding the seat extras
    *
    * @param flightExtraFacilityResponse the flight extra facility response
    * @param extraFacilityMap the extra facility map
    */
   private void updateGroupedFlightExtraFacilityResponseToStore(
      final FlightExtraFacilityResponse flightExtraFacilityResponse,
      final Map<List<String>, List<ExtraFacility>> extraFacilityMap)
   {

      final Map<String, Map<String, List<ExtraFacility>>> cabClsExtrasMap =
         new HashMap<String, Map<String, List<ExtraFacility>>>();
      for (final Entry<List<String>, List<ExtraFacility>> cabClseachEntry : extraFacilityMap
         .entrySet())
      {
         final Map<String, List<ExtraFacility>> extraFacilityGroupedMap =
            new HashMap<String, List<ExtraFacility>>();
         for (final Entry<List<String>, List<ExtraFacility>> extCodeMap : getGroupedExtraFacilityMap(
            cabClseachEntry.getValue()).entrySet())
         {
            extraFacilityGroupedMap.put(extCodeMap.getKey().get(0), extCodeMap.getValue());
         }
         cabClsExtrasMap.put(cabClseachEntry.getKey().get(0), extraFacilityGroupedMap);
      }
      this.getCabinWiseGroupedFlightExtrasResponse().put(
         flightExtraFacilityResponse.getPackageId(), cabClsExtrasMap);
   }

   /**
    * Creates the combined seat extras list from all legs.
    *
    * @param legExtraFacilityList the leg extra facility list
    * @return the list
    */
   private List<ExtraFacility> createCombinedSeatExtrasListFromAllLegs(
      final List<LegExtraFacility> legExtraFacilityList)
   {
      final List<ExtraFacility> combinedExtraFacilityList = new ArrayList<ExtraFacility>();

      for (final LegExtraFacility legExtraFacility : legExtraFacilityList)
      {

         for (final ExtraFacilityCategory extraFacilityCategory : legExtraFacility
            .getExtraFacilityCategories())
         {
            if (extraFacilityCategory.getCode().equalsIgnoreCase(
               ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
            {
               combinedExtraFacilityList.addAll(extraFacilityCategory.getExtraFacilities());
            }

         }
      }
      return combinedExtraFacilityList;
   }

   /**
    * Creates the combined flight extras list from all legs.
    *
    * @param legExtraFacilityList the leg extra facility list
    * @return the list
    */
   private List<ExtraFacility> createCombinedFlightExtrasListFromAllLegs(
      final List<LegExtraFacility> legExtraFacilityList)
   {
      final List<ExtraFacility> combinedExtraFacilityList = new ArrayList<ExtraFacility>();

      for (final LegExtraFacility legExtraFacility : legExtraFacilityList)
      {

         for (final ExtraFacilityCategory extraFacilityCategory : legExtraFacility
            .getExtraFacilityCategories())
         {
            if (!extraFacilityCategory.getCode().equalsIgnoreCase(
               ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
            {
               combinedExtraFacilityList.addAll(extraFacilityCategory.getExtraFacilities());
            }

         }
      }
      return combinedExtraFacilityList;
   }

   /**
    * Gets the grouped extra facility map.
    *
    * @param combinedExtraFacilityList the combined extra facility list
    * @return the grouped extra facility map
    */
   @SuppressWarnings("unchecked")
   private Map<List<String>, List<ExtraFacility>> getGroupedExtraFacilityMap(
      final List<ExtraFacility> combinedExtraFacilityList)
   {
      Map<List<String>, List<ExtraFacility>> extraFacilityMap =
         new HashMap<List<String>, List<ExtraFacility>>();
      try
      {
         final StringBuilder queryForParsing = new StringBuilder("SELECT * FROM ");
         queryForParsing.append(ExtraFacility.class.getName());
         queryForParsing.append(" GROUP BY extraFacilityCode ");
         final Query query = new Query();
         query.parse(queryForParsing.toString());
         final QueryResults queryResults = query.execute(combinedExtraFacilityList);
         extraFacilityMap = extraFacilityMap.getClass().cast(queryResults.getGroupByResults());
      }
      catch (final QueryParseException qpe)
      {
         LOG.error("Query parse exception", qpe);
         return Collections.emptyMap();
      }
      catch (final QueryExecutionException qee)
      {
         LOG.error("Query Execution", qee);
         return Collections.emptyMap();
      }

      return extraFacilityMap;

   }

   /**
    * Gets the grouped cab cls extra facility map.
    *
    * @param combinedExtraFacilityList the combined extra facility list
    * @return the grouped cab cls extra facility map
    */
   @SuppressWarnings("unchecked")
   private Map<List<String>, List<ExtraFacility>> getGroupedCabClsExtraFacilityMap(
      final List<ExtraFacility> combinedExtraFacilityList)
   {
      Map<List<String>, List<ExtraFacility>> extraFacilityMap =
         new HashMap<List<String>, List<ExtraFacility>>();
      try
      {
         final StringBuilder queryForParsing = new StringBuilder("SELECT * FROM ");
         queryForParsing.append(ExtraFacility.class.getName());
         queryForParsing.append(" GROUP BY cabinClass");
         final Query query = new Query();
         query.parse(queryForParsing.toString());
         final QueryResults queryResults = query.execute(combinedExtraFacilityList);
         extraFacilityMap = extraFacilityMap.getClass().cast(queryResults.getGroupByResults());
      }
      catch (final QueryParseException qpe)
      {
         LOG.error("Query parse exception", qpe);
         return Collections.emptyMap();
      }
      catch (final QueryExecutionException qee)
      {
         LOG.error("Query Execution", qee);
         return Collections.emptyMap();
      }

      return extraFacilityMap;

   }

   /**
    * Gets the extra facility from all legs based on cab cls.
    *
    * @param packageId the package id
    * @param extraFacilityCode the extra facility code
    * @param cabClsCode the cab cls code
    * @return the extra facility from all legs based on cab cls
    */
   public List<ExtraFacility> getExtraFacilityFromAllLegsBasedOnCabCls(final String packageId,
      final String extraFacilityCode, final String cabClsCode)
   {
      List<ExtraFacility> flightExtras = new ArrayList<ExtraFacility>();
      // Map<String, Map<String, List<ExtraFacility>>> flightExtrasMap =

      final Map<String, Map<String, List<ExtraFacility>>> flightExtrasMap =
         this.getCabinWiseGroupedFlightExtrasResponse().get(packageId);
      Map<String, List<ExtraFacility>> cabWiseflightExtrasMap =
         new HashMap<String, List<ExtraFacility>>();
      if (MapUtils.isNotEmpty(flightExtrasMap))
      {
         cabWiseflightExtrasMap = flightExtrasMap.get(cabClsCode);
      }
      if (MapUtils.isNotEmpty(cabWiseflightExtrasMap))
      {
         flightExtras = cabWiseflightExtrasMap.get(extraFacilityCode);
      }
      if (CollectionUtils.isEmpty(flightExtras))
      {
         flightExtras = getFlightSeatExtraFacilities(packageId, extraFacilityCode);
      }
      return flightExtras;
   }

   /**
    * Gets the flight seat extra facilities.
    *
    * @param packageId the package id
    * @param extraFacilityCode the extra facility code
    * @return the flight seat extra facilities
    */
   private List<ExtraFacility> getFlightSeatExtraFacilities(final String packageId,
      final String extraFacilityCode)
   {
      if (MapUtils.isNotEmpty(this.getSeatGroupedFlightExtrasResponse().get(packageId)))
      {
         return this.getSeatGroupedFlightExtrasResponse().get(packageId).get(extraFacilityCode);
      }
      return Collections.emptyList();
   }

   /**
    * Gets the extra facility from all legs based on cabin class.
    *
    * @param packageId the package id
    * @param cabinCode the cabin code
    * @return the extra facility from all legs based on cabin class
    */
   public Map<String, List<ExtraFacility>> getExtraFacilityFromAllLegsBasedOnCabinClass(
      final String packageId, final String cabinCode)
   {

      final Map<String, Map<String, List<ExtraFacility>>> cabBasedExtFacMap =
         this.getCabinWiseGroupedFlightExtrasResponse().get(packageId);
      final Map<String, List<ExtraFacility>> extraFacilityModels =
         new LinkedHashMap<String, List<ExtraFacility>>();
      if (MapUtils.isNotEmpty(getSeatGroupedFlightExtrasResponse().get(packageId)))
      {
         extraFacilityModels.putAll(getSeatGroupedFlightExtrasResponse().get(packageId));
      }
      if (MapUtils.isNotEmpty(cabBasedExtFacMap))
      {
         extraFacilityModels.putAll(cabBasedExtFacMap.get(cabinCode));
      }
      return extraFacilityModels;
   }
}
