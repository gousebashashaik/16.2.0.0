/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * @author sunilkumar.sahu
 *
 */
public class PassengerExtraFacilityMapping {


        /**
        * Holds the Passenger Ids.
        */
       private List<Integer> passengers ;

        /**
         * @return the passengers
         */
        public List<Integer> getPassengers() {
           if(CollectionUtils.isEmpty(this.passengers))
           {
              this.passengers = new ArrayList<Integer>();
           }
            return passengers;
        }

        /**
         * @param passengers the passengers to set
         */
        public void setPassengers(List<Integer> passengers) {
            this.passengers = passengers;
        }
}
