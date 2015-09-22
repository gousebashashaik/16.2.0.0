/**
 *
 */
package uk.co.portaltech.tui.comparators.sort;

import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;

/**
 * @author shyamaprasada.vs
 *  This class defines all the possible sorting strategies as enum,
 *  returns appropriate comparator to sort the results
 */
public enum  SearchResultViewDataComparator implements Comparator<SearchResultViewData>{

    /**
     * The comparator Enums  should be same as SortParameters(uk.co.portaltech.tui.enums.SortParameters)
     * in order to sort results based on the desired property
     */

    PRICE_ASCENDING {
        private double diff;
        @Override
         public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
         diff = Double.parseDouble(holidayObj1.getPrice().getTotalParty()) -
               Double.parseDouble(holidayObj2.getPrice().getTotalParty());
            if(diff>0){
                return 1;
            }
            if(diff<0){
                return -1;
            }
            return 0;
       }
     },

     PRICE_DESCENDING {
        private double diff;
        @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            diff = Double.parseDouble(holidayObj2.getPrice().getTotalParty()) -
                   Double.parseDouble(holidayObj1.getPrice().getTotalParty());
            if(diff>0){
                return 1;
            }
            if(diff<0){
                return -1;
            }
            return 0;
        }
    },

     NAME_ASCENDING{
            @Override
            public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            return holidayObj1.getAccommodation().getName().compareTo(holidayObj2.getAccommodation().getName());
        }
    },

     NAME_DESCENDING {
    @Override
     public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
        return holidayObj2.getAccommodation().getName().compareTo(holidayObj1.getAccommodation().getName());
      }
     },


     COMMERICAL_PRIORITY_ASCENDING {
    @Override
     public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
        return holidayObj1.getAccommodation().getCommercialPriority() -
                     holidayObj2.getAccommodation().getCommercialPriority();

     }
    },

     COMMERICAL_PRIORITY_DESCENDING {
        @Override
         public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            return holidayObj2.getAccommodation().getCommercialPriority() -
            holidayObj1.getAccommodation().getCommercialPriority();
      }
    },


     TRATING_ASCENDING {
      private double diff;
        @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            diff = Double.parseDouble(holidayObj1.getAccommodation().getRatings().getOfficialRating()) -
                   Double.parseDouble(holidayObj2.getAccommodation().getRatings().getOfficialRating());
            if(diff>0){
                return 1;
            }
            if(diff<0){
                return -1;
            }
            return 0;
        }
     },

     TRATING_DESCENDING {
       private double diff;
        @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            diff = Double.parseDouble(holidayObj2.getAccommodation().getRatings().getOfficialRating()) -
                    Double.parseDouble(holidayObj1.getAccommodation().getRatings().getOfficialRating());
            if(diff>0){
                return 1;
            }
            if(diff<0){
                return -1;
            }
            return 0;
        }
     },


     SAVINGS_AMOUNT_ASCENDING {
     private double diff;
      @Override
      public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
        diff = Double.parseDouble(holidayObj1.getPrice().getDiscount())-
                Double.parseDouble(holidayObj2.getPrice().getDiscount());
        if(diff>0){
            return 1;
        }
        if(diff<0){
            return -1;
        }
        return 0;
      }
     },

     SAVINGS_AMOUNT_DESCENDING {
      private double diff;
        @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            diff = Double.parseDouble(holidayObj2.getPrice().getDiscount())-
                    Double.parseDouble(holidayObj1.getPrice().getDiscount());
            if(diff>0){
                return 1;
            }
            if(diff<0){
                return -1;
            }
            return 0;
        }
    },

     TRIP_ADVISOR_RATING_ASCENDING {
        private double diff;
        @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            diff = Double.parseDouble(holidayObj1.getAccommodation().getRatings().getTripAdvisorRating()) -
                    Double.parseDouble(holidayObj2.getAccommodation().getRatings().getTripAdvisorRating());
            if(diff>0){
                return 1;
            }
            if(diff<0){
                return -1;
            }
            return 0;
        }
    },

     TRIP_ADVISOR_RATING_DESCENDING {
        private double diff;
        @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            diff = Double.parseDouble(holidayObj2.getAccommodation().getRatings().getTripAdvisorRating()) -
                    Double.parseDouble(holidayObj1.getAccommodation().getRatings().getTripAdvisorRating());
            if(diff>0){
                return 1;
            }
            if(diff<0){
                return -1;
            }

            return 0;
        }
    },

     DEPARTURE_DATE_ASCENDING {
        private LocalDate dateObj1, dateObj2;
        @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
                dateObj1 = DateUtils.toDate(holidayObj1.getItinerary().getDepartureDate());
                dateObj2 = DateUtils.toDate(holidayObj2.getItinerary().getDepartureDate());
                return dateObj1.compareTo(dateObj2);
        }
     },

     DEPARTURE_DATE_DESCENDING {
       private LocalDate dateObj1, dateObj2;
         @Override
        public int compare(SearchResultViewData holidayObj1, SearchResultViewData holidayObj2){
            dateObj1 = DateUtils.toDate(holidayObj2.getItinerary().getDepartureDate());
            dateObj2 = DateUtils.toDate(holidayObj1.getItinerary().getDepartureDate());
            return dateObj1.compareTo(dateObj2);

        }
    };



/**
 * This method will get the list of  comparator and iterate through it, sort the result and return
 * @param sortComparators
 * @return Comparator<SearchResultViewData>
 */
    public static Comparator<SearchResultViewData> getComparator(final List<SearchResultViewDataComparator> sortComparators) {
        return new Comparator<SearchResultViewData>() {
           @Override
            public int compare(SearchResultViewData o1, SearchResultViewData o2) {
                for (SearchResultViewDataComparator sortComparator : sortComparators) {
                    int result = sortComparator.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }


}
