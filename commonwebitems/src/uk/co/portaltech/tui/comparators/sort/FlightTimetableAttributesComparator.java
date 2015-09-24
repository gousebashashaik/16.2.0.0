/**
 *
 */
package uk.co.portaltech.tui.comparators.sort;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.route.FlightRouteDTO;


/**
 * @author perlagangi.r
 *
 */
public enum FlightTimetableAttributesComparator implements Comparator<Object>
{

   DEP_TIME_ASCENDING
   {
      @Override
      public int compare(final Object object1, final Object object2)
      {
         final FlightRouteDTO route1 = (FlightRouteDTO) object1;
         final FlightRouteDTO route2 = (FlightRouteDTO) object2;

         final String route1Departure = route1.getDepartureTime();
         final String route2Departure = route2.getDepartureTime();
         int deptime1 = 0;

         int deptime2 = 0;

         deptime1 = getRoute1DepartureTime(route1Departure, deptime1);

         deptime2 = getRoute1DepartureTime(route2Departure, deptime2);

         final int diff = deptime1 - deptime2;
         if (diff > 0)
         {
            return 1;
         }
         if (diff < 0)
         {
            return -1;
         }
         return 0;
      }

      /**
       * @param route1Departure
       * @param deptime1
       * @return
       */
      private int getRoute1DepartureTime(final String route1Departure, int deptime1)
      {
         int updateddeptime = deptime1;
         if (!StringUtils.isBlank(route1Departure))
         {
            updateddeptime = Integer.parseInt(route1Departure);
         }
         return updateddeptime;
      }
   };




   public static Comparator<Object> getComparator(final List<FlightTimetableAttributesComparator> sortComparators)
   {
      return new Comparator<Object>()
      {
         @Override
         public int compare(final Object o1, final Object o2)
         {
            return compareSort(sortComparators, o1, o2);
         }
      };
   }

   /**
    * @param sortComparators
    * @param o1
    * @param o2
    * @return integer
    */
   private static int compareSort(final List<FlightTimetableAttributesComparator> sortComparators, final Object o1,
         final Object o2)
   {
      for (final FlightTimetableAttributesComparator sortComparator : sortComparators)
      {
         final int result = sortComparator.compare(o1, o2);
         if (result != 0)
         {
            return result;
         }
      }
      return 0;
   }
}
