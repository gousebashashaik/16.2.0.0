/**
 *
 */
package uk.co.portaltech.tui.helper;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;

/**
 * Helper Class for Analytics populator
 */
public class AnalyticsHelper
{

   private static final String DATE_SEPERATOR = "/";

   private static final int TWO = 2;

   /**
    * Population of MonthYear
    *
    * @param analyticMap
    * @param departureDate
    */

   public void addMonthYear(final Map<String, WebAnalytics> analyticMap,
      final LocalDate departureDate)
   {
      final StringBuilder yearWithMonth = new StringBuilder();
      yearWithMonth.append(DateUtils.formattedMonth(departureDate));
      yearWithMonth.append(DATE_SEPERATOR);
      yearWithMonth.append(departureDate.getYear());
      analyticMap.put("MonthYear", new WebAnalytics("MonthYear", yearWithMonth.toString()));
   }

   /**
    * @param analyticMap
    * @param departureDate
    */
   public void addFormattedDate(final Map<String, WebAnalytics> analyticMap,
      final LocalDate departureDate)
   {
      analyticMap.put("DepDate",
         new WebAnalytics("DepDate", DateUtils.formattedDate(departureDate)));
   }

   /**
    * Function to round the price value to integer
    *
    * @param price
    */
   public int getPriceRoundedValue(final String price)
   {
      return (int) Math.round(Double.parseDouble(price));
   }

   /**
    * Function to find if the route has dreamliner flights or not
    *
    * @param route - flightData for either outbound/inbound
    * @return boolean - true/false depending on, if a route has dreamliner
    */
   public boolean isdreamliner(final List<SearchResultFlightDetailViewData> route)
   {

      if (CollectionUtils.isNotEmpty(route))
      {
         for (final SearchResultFlightDetailViewData flightsData : route)
         {
            if (flightsData.isDreamLinerIndicator())
            {

               return true;
            }
         }
      }
      return false;
   }

   public int getInfantCount(final List<Integer> childrenAge)
   {
      int infantCount = 0;
      if (CollectionUtils.isNotEmpty(childrenAge))
      {
         for (final Integer age : childrenAge)
         {
            if (age.intValue() < TWO)
            {
               infantCount++;

            }
         }
      }
      return infantCount;
   }

}
