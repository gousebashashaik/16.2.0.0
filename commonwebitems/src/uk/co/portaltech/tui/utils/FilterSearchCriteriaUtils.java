/**
 *
 */
package uk.co.portaltech.tui.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author chandrasekhar.v
 *
 */
public final class FilterSearchCriteriaUtils
{
   private FilterSearchCriteriaUtils()
   {

   }

   public static boolean isFilterRequest(final HolidaySearchContext context)
   {
      if (StringUtils.isNotBlank(context.getSearchType()))
      {

         if ("Filter".equalsIgnoreCase(context.getSearchType()))
         {
            return true;

         }
         else
         {

            // budget
            if (context.getBudget() != null)
            {
               return StringUtils.isNotBlank(context.getBudget().getId()) || StringUtils
                  .isNotBlank(context.getBudget().getMin());
            }

            // temperature
            if (context.getTemperature() != null)
            {

               return (StringUtils.isNotBlank(context.getTemperature().getId()))
                  || StringUtils.isNotBlank(context.getTemperature().getMin());
            }

            // fcrating
            if (context.getFcRating() != null)
            {
               return StringUtils.isNotBlank(context.getFcRating().getId()) && StringUtils
                  .isNotBlank(context.getFcRating().getMin());

            }
            // tARating
            if (context.gettARating() != null)
            {
               return StringUtils.isNotBlank(context.getFcRating().getId()) || StringUtils
                  .isNotBlank(context.getFcRating().getMin());
            }
            // tARating
            if (context.gettARating() != null)
            {
               return StringUtils.isNotBlank(context.gettARating().getMin()) || StringUtils
                  .isNotBlank(context.gettARating().getId())
                  && StringUtils.isNotBlank(context.gettARating().getMin());
            }
            // inbound and Outbound
            if (CollectionUtils.isEmpty(context.getInBound())
               || CollectionUtils.isEmpty(context.getOutBound()))
            {
               return true;
            }
            // Departure Points need to be added.

            // bestfor
            if (context.getBestFor() != null
               && !CollectionUtils.isEmpty(context.getBestFor().getValues()))
            {
               return true;
            }
            // holidayType : Currently Values are not coming from UI.
            if (context.getHolidayType() != null
               && !CollectionUtils.isEmpty(context.getHolidayType().getValues()))
            {
               return true;
            }
            //
            // destinationValues
            if (CollectionUtils.isNotEmpty(context.getDestinationOptions()))
            {
               return true;
            }
            if (context.getFeatures() != null && !context.getFeatures().isEmpty())
            {
               return true;
            }
         }
      }
      return false;
   }

   public static boolean isFilterRequest(final SearchResultsRequestData requestData)
   {

      if (StringUtils.isNotBlank(requestData.getSearchRequestType())
         && "Filter".equalsIgnoreCase(requestData.getSearchRequestType()))
      {
         return true;
      }
      return false;
   }
}
