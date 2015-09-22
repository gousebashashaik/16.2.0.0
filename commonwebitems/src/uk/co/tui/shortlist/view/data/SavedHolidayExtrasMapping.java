/**
 *
 */
package uk.co.tui.shortlist.view.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * @author Sravani
 *
 */
public class SavedHolidayExtrasMapping
{

   /**
    * Holds the Passenger Ids.
    */
   private List<Integer> passengers;

   /**
    * @return the passengers
    */
   public List<Integer> getPassengers()
   {
      if (CollectionUtils.isEmpty(this.passengers))
      {
         this.passengers = new ArrayList<Integer>();
      }
      return passengers;
   }

   /**
    * @param passengers the passengers to set
    */
   public void setPassengers(final List<Integer> passengers)
   {
      this.passengers = passengers;
   }
}
