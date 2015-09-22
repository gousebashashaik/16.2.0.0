/**
 *
 */
package uk.co.tui.book.constants;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author sunilkumar.sahu
 *
 */
public final class BookFlowConstants
{

   public static final String PASSENGER_PAGE_NOT_FOUND =
      "No such request method for PassengerDetailsPage";

   public static final String CHANNEL = "channel";

   public static final String BRAND_NAME = "brand_name";

   public static final String FLIGHTOPTIONS = "flightoptions";

   public static final String ROOMOPTIONS = "roomoptions";

   public static final String EXTRAOPTIONS = "extraoptions";

   public static final String PASSENGERDETAILS = "passengerdetails";

   public static final String PAYMENTPAGE = "paymentpage";

   public static final String EMPTY = StringUtils.EMPTY;

   public static final String MEAL = "MEAL";

   public static final String SEAT = "SEAT";

   public static final String BAG = "BAG";

   public static final String FLIGHT = "FLIGHT";

   public static final String EXTRA = "EXTRA";

   public static final List<String> TRANSFER_OPTION_CODES = Arrays.asList(new String[] { "TXX",
      "HMZ", "ECW", "DEF_HMZ" });

   public static final String UNCHECKED = "unchecked";

   public static final String BOXING = "boxing";

   public static final int ZERO = 0;

   public static final int ONE = 1;

   public static final int MINUS = -1;

   public static final int THREE = 3;

   public static final int HUNDRED = 100;

   public static final int EIGHT = 8;

   public static final int NINE = 9;

   public static final int FOUR = 4;

   public static final String INCLUDED = "Included";

   // Data protection notice related constants
   public static final String CAR_HIRE = "Car Hire";

   public static final List<String> LOCATION_CATAGORIES = Arrays.asList(new String[] {
      "accommodations", "destinations", "countries", "resorts", "regions", "continents" });

   public static final List<String> SEAT_POSITION_INDEX = Arrays.asList(new String[] { "XX1",
      "XX2", "XX4", "XX3" });

   private BookFlowConstants()
   {
      super();
   }

}
