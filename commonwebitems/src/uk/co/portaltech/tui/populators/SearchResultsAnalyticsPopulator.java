/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.services.DurationHowLongService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.ABTestViewData;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

/**
 * @author vivek.vk
 *
 */
public class SearchResultsAnalyticsPopulator implements
   Populator<Object, Map<String, WebAnalytics>>
{

   @Resource
   private AnalyticsHelper analyticsHelper;

   @Resource
   private DurationHowLongService durationHowLongService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   /**
     *
     */
   private static final String SEARCH_TYPE = "Standard";

   private static final String DUR = "Dur";

   private static final String DELIMITTER = "|";

   private static final String WHERETO = "WhereTo";

   private static final String DEPAIR = "DepAir";

   private static final int TWO = 2;

   /** Added constructor for execution of test cases */
   public SearchResultsAnalyticsPopulator()
   {

   }

   public SearchResultsAnalyticsPopulator(final AnalyticsHelper analyticsHelper)
   {
      this.analyticsHelper = analyticsHelper;
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final Object source, final Map<String, WebAnalytics> analyticMap)
      throws ConversionException
   {
      if (source instanceof SearchResultsRequestData)
      {
         final SearchResultsRequestData requestData = (SearchResultsRequestData) source;
         populateWhereTo(analyticMap, requestData);
         populateAirport(analyticMap, requestData);
         populateDepartureDate(analyticMap, requestData);
         populateFlexibility(analyticMap, requestData);
         populatePaxConfig(analyticMap, requestData);
         if (requestData.getDuration() > 0)
         {
            analyticMap.put(
               DUR,
               new WebAnalytics(DUR, String.valueOf(getDefValueForDurationRange(requestData
                  .getDuration()))));

         }
      }
      if (source instanceof SearchResultsViewData)
      {
         final SearchResultsViewData resultViewData = (SearchResultsViewData) source;
         analyticMap.put("Results",
            new WebAnalytics("Results", String.valueOf(resultViewData.getEndecaResultsCount())));
         // TODO: As per Search 1.0 requirements this will always be 'Standard'. Will be changed in
         // later sprints.
         analyticMap.put("SchScope", new WebAnalytics("SchScope", SEARCH_TYPE));
         if (!analyticMap.containsKey(DUR))
         {
            if (StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(),
               BrandType.FC.getCode()))
            {
               analyticMap.put(
                  DUR,
                  new WebAnalytics(DUR, String.valueOf(getDefValueForDurationRange(resultViewData
                     .getPrioritizedDuration()))));
            }
            else if (StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(),
               BrandType.RT.getCode()))
            {
               analyticMap.put(
                  DUR,
                  new WebAnalytics(DUR, String.valueOf(getDefValueForDurationRange(resultViewData
                     .getPrioritizedDuration()))));
            }
            else
            {
               analyticMap.put(DUR,
                  new WebAnalytics(DUR, String.valueOf(resultViewData.getPrioritizedDuration())));

            }
         }

      }
      if (source instanceof ABTestViewData)
      {
         final ABTestViewData aBTestViewData = (ABTestViewData) source;
         analyticMap.put("TestName", new WebAnalytics("TestName", aBTestViewData.getTestName()));
      }

   }

   /**
    * Population of Flexibility for Analytics
    *
    * @param analyticMap
    * @param requestData
    */
   public void populateFlexibility(final Map<String, WebAnalytics> analyticMap,
      final SearchResultsRequestData requestData)
   {
      final WebAnalytics webAnalyticFlexDate = new WebAnalytics("FlexDate", "No");
      if (requestData.isFlexibility())
      {
         webAnalyticFlexDate.setValue("Yes");
      }
      analyticMap.put("FlexDate", webAnalyticFlexDate);
   }

   /**
    * Population of Pax config for Analytics
    *
    * @param analyticMap
    * @param requestData
    */
   public void populatePaxConfig(final Map<String, WebAnalytics> analyticMap,
      final SearchResultsRequestData requestData)
   {
      analyticMap.put(
         "Adults",
         new WebAnalytics("Adults", String.valueOf(requestData.getNoOfAdults()
            + requestData.getNoOfSeniors())));
      final int infant = getInfantCount(requestData.getChildrenAge());
      int childCount = 0;
      if (CollectionUtils.isNotEmpty(requestData.getChildrenAge()))
      {
         childCount = requestData.getChildrenAge().size() - infant;
      }
      analyticMap.put("Children", new WebAnalytics("Children", String.valueOf(childCount)));
      analyticMap.put("Infants", new WebAnalytics("Infants", String.valueOf(infant)));
      // Added total no. of passengers(adults + childrens, excluding infants) as pax.

      analyticMap.put(
         "Pax",
         new WebAnalytics("Pax", String.valueOf((Integer.parseInt(analyticMap.get("Adults")
            .getValue())) + Integer.parseInt(analyticMap.get("Children").getValue()))));

   }

   /**
    * Date population for Search Results Page Analytics
    *
    * @param analyticMap
    * @param requestData
    */
   public void populateDepartureDate(final Map<String, WebAnalytics> analyticMap,
      final SearchResultsRequestData requestData)
   {
      if ((StringUtils.isNotEmpty(requestData.getWhen()))
         || (StringUtils.isNotEmpty(requestData.getDepartureDate())))
      {
         LocalDate departureDate;
         if (StringUtils.isNotEmpty(requestData.getDepartureDate()))
         {
            departureDate = DateUtils.toDate(requestData.getDepartureDate());
         }
         else
         {
            departureDate = DateUtils.toDate(requestData.getWhen());
         }
         analyticsHelper.addFormattedDate(analyticMap, departureDate);
         analyticsHelper.addMonthYear(analyticMap, departureDate);
      }
   }

   /**
    * WhereTo population for Search Results Page Analytics
    *
    * @param analyticMap
    * @param requestData
    */
   public void populateWhereTo(final Map<String, WebAnalytics> analyticMap,
      final SearchResultsRequestData requestData)
   {

      if (CollectionUtils.isNotEmpty(requestData.getUnits()))
      {
         final List<String> unitCodes = new ArrayList<String>();
         for (final UnitData unit : requestData.getUnits())
         {
            if (StringUtils.equalsIgnoreCase(unit.getType(), "HOTEL")
               || StringUtils.equalsIgnoreCase(unit.getType(), "APARTMENT_HOTEL")
               || StringUtils.equalsIgnoreCase(unit.getType(), "SELF_CATERED"))
            {
               unitCodes.add("H" + unit.getId());
            }
            else
            {
               unitCodes.add("L" + unit.getId());
            }

         }

         final WebAnalytics webAnalyticDepAir =
            new WebAnalytics(WHERETO,
               org.springframework.util.StringUtils.collectionToDelimitedString(unitCodes,
                  DELIMITTER));
         analyticMap.put(WHERETO, webAnalyticDepAir);
      }
      else
      {
         final WebAnalytics webAnalyticDepAir = new WebAnalytics(WHERETO, "Any Geo");
         analyticMap.put(WHERETO, webAnalyticDepAir);
      }

   }

   /**
    * Airport Data Population for for Search Results Page Analytics
    *
    * @param analyticMap
    * @param requestData
    */
   public void populateAirport(final Map<String, WebAnalytics> analyticMap,
      final SearchResultsRequestData requestData)
   {
      if (CollectionUtils.isNotEmpty(requestData.getAirports()))
      {
         final List<String> airportCodes = new ArrayList<String>();
         for (final AirportData airport : requestData.getAirports())
         {
            airportCodes.add(airport.getId());
         }
         final WebAnalytics webAnalyticDepAir =
            new WebAnalytics(DEPAIR,
               org.springframework.util.StringUtils.collectionToDelimitedString(airportCodes,
                  DELIMITTER));
         analyticMap.put(DEPAIR, webAnalyticDepAir);
      }
      else
      {
         analyticMap.put(DEPAIR, new WebAnalytics(DEPAIR, "Any"));
      }
   }

   private int getInfantCount(final List<Integer> childrenAge)
   {
      int infantCount = 0;

      if (CollectionUtils.isNotEmpty(childrenAge))
      {
         for (final Integer age : childrenAge)
         {
            if (age < TWO)
            {
               infantCount++;

            }
         }
      }
      return infantCount;
   }

   private String getDefValueForDurationRange(final int value)
   {

      if (StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(), BrandType.FC.getCode()))
      {
         if (durationHowLongService.getDurationForCode(value, "FC") != null
            && !durationHowLongService.getDurationForCode(value, "FC").isEmpty())
         {
            return durationHowLongService.getDurationForCode(value, "FC").get(0).getStay();
         }
      }
      else
      {
         if (durationHowLongService.getDurationForCode(value, "TH") != null
            && !durationHowLongService.getDurationForCode(value, "TH").isEmpty())
         {
            return durationHowLongService.getDurationForCode(value, "TH").get(0).getStay();
         }

      }

      return String.valueOf(value);
   }

}
