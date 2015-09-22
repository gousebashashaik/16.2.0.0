/**
 *
 */

package uk.co.tui.util;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.travel.model.results.Holiday;


/**
 *
 */
public enum HolidayComparator implements Comparator<Holiday>
{
    /**
     * The comparator Enums should be same as SortParameters(uk.co.portaltech.tui.enums.SortParameters) in order to sort
     * results based on the desired property
     */

    PRICE_ASCENDING
    {
        private double diff;

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            diff = holidayObj1.getTpp().subtract(holidayObj2.getTpp()).doubleValue();
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
    },

    PRICE_DESCENDING
    {
        private double diff;

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            diff = holidayObj2.getTpp().subtract(holidayObj1.getTpp()).doubleValue();
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
    },

    NAME_ASCENDING
    {
        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            String accom1 = holidayObj1.getAccomodation().getAccomodationName();
            String accom2 = holidayObj2.getAccomodation().getAccomodationName();

            if(StringUtils.isBlank(accom1) && StringUtils.isBlank(accom1))
            {
                return 0;
            }
            if(StringUtils.isBlank(accom1))
            {
                return 1;
            }
            if(StringUtils.isBlank(accom2))
            {
                return -1;
            }

            return accom1.compareTo(accom2);
        }
    },

    NAME_DESCENDING
    {
        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            String accom1 = holidayObj1.getAccomodation().getAccomodationName();
            String accom2 = holidayObj2.getAccomodation().getAccomodationName();

            if(StringUtils.isBlank(accom1) && StringUtils.isBlank(accom1))
            {
                return 0;
            }
            if(StringUtils.isBlank(accom1))
            {
                return 1;
            }
            if(StringUtils.isBlank(accom2))
            {
                return -1;
            }

            return accom2.compareTo(accom1);
        }
    },


    COMMERICAL_PRIORITY_ASCENDING
    {
        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            return holidayObj1.getIndex() - holidayObj2.getIndex();

        }
    },

    COMMERICAL_PRIORITY_DESCENDING
    {
        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            return holidayObj2.getIndex() - holidayObj1.getIndex();
        }
    },


    TRATING_ASCENDING
    {
        private double diff;

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            diff = Double.parseDouble(holidayObj1.getOfficialRating()) - Double.parseDouble(holidayObj2.getOfficialRating());
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
    },

    TRATING_DESCENDING
    {
        private double diff;

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            diff = Double.parseDouble(holidayObj2.getOfficialRating()) - Double.parseDouble(holidayObj1.getOfficialRating());
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
    },


    SAVINGS_AMOUNT_ASCENDING
    {
        private double diff;

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            diff = holidayObj1.getPpDiscount().subtract(holidayObj2.getPpDiscount()).doubleValue();
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
    },

    SAVINGS_AMOUNT_DESCENDING
    {
        private double diff;

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            diff = holidayObj2.getPpDiscount().subtract(holidayObj1.getPpDiscount()).doubleValue();
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
    },

    TRIP_ADVISOR_RATING_ASCENDING
    {
        private double diff;
        private static final String DOUBLE_PARSE_PARAM_ASC="0.0";

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            if (StringUtils.isEmpty(holidayObj1.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj2.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(holidayObj1.getTripAdvisorRating())
                        - Double.parseDouble(DOUBLE_PARSE_PARAM_ASC);
            }
            else if (StringUtils.isEmpty(holidayObj2.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj1.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(DOUBLE_PARSE_PARAM_ASC)
                        - Double.parseDouble(holidayObj2.getTripAdvisorRating());
            }
            else if (StringUtils.isNotEmpty(holidayObj1.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj2.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(holidayObj1.getTripAdvisorRating()) - Double.parseDouble(holidayObj2.getTripAdvisorRating());
            }

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
    },


    TRIP_ADVISOR_RATING_DESCENDING
    {
        private double diff;
        private static final String DOUBLE_PARSE_PARAM_DESC="0.0";

        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            if (StringUtils.isEmpty(holidayObj1.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj2.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(holidayObj2.getTripAdvisorRating())
                        - Double.parseDouble(DOUBLE_PARSE_PARAM_DESC);
            }
            else if (StringUtils.isEmpty(holidayObj2.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj1.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(DOUBLE_PARSE_PARAM_DESC)
                        - Double.parseDouble(holidayObj1.getTripAdvisorRating());
            }
            else if (StringUtils.isNotEmpty(holidayObj1.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj2.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(holidayObj2.getTripAdvisorRating())
                        - Double.parseDouble(holidayObj1.getTripAdvisorRating());
            }
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
    },

    DEPARTURE_DATE_ASCENDING
    {
        private DateTime dateObj1, dateObj2;
        private boolean dateDiff;
        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            if((CollectionUtils.isNotEmpty(holidayObj1.getItinerary().getOutbound())) && (CollectionUtils.isNotEmpty(holidayObj2.getItinerary().getOutbound())))
            {
                dateObj1 = holidayObj1.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate();
                dateObj2 = holidayObj2.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate();
                dateDiff = dateObj2.isBefore(dateObj1);
            }

            if (dateDiff){
                return 1;
            }else{
                return -1;
            }
        }
    },

    DEPARTURE_DATE_DESCENDING
    {
        private DateTime dateObj1, dateObj2;
        private boolean dateDiff;
        @Override
        public int compare(final Holiday holidayObj1, final Holiday holidayObj2)
        {
            if((CollectionUtils.isNotEmpty(holidayObj1.getItinerary().getOutbound())) && (CollectionUtils.isNotEmpty(holidayObj2.getItinerary().getOutbound())))
            {
                dateObj1 = holidayObj1.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate();
                dateObj2 = holidayObj2.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate();
                dateDiff = dateObj1.isBefore(dateObj2);
            }

            if (dateDiff){
                return 1;
            }else{
                return -1;
            }
        }
    };

    /**
     * This method will get the list of comparator and iterate through it, sort the result and return
     *
     * @param sortComparators
     * @return Comparator<SearchResultViewData>
     */
    public static Comparator<Holiday> getComparator(final List<HolidayComparator> sortComparators)
    {
        return new Comparator<Holiday>()
        {
            @Override
            public int compare(final Holiday o1, final Holiday o2)
            {
                for (final HolidayComparator sortComparator : sortComparators)
                {
                    final int result = sortComparator.compare(o1, o2);
                    if (result != 0)
                    {
                        return result;
                    }
                }
                return 0;
            }
        };
    }

}
