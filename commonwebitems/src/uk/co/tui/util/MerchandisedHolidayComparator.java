/**
 *
 */

package uk.co.tui.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.results.MerchandisedHoliday;


/**
 *
 */
public enum MerchandisedHolidayComparator implements Comparator<MerchandisedHoliday>
{
    /**
     * The comparator Enums should be same as SortParameters(uk.co.portaltech.tui.enums.SortParameters) in order to sort
     * results based on the desired property
     */

    PRICE_ASCENDING
    {
        private double diff;

        @Override
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            diff = new BigDecimal(holidayObj1.getPriceFrom()).subtract(new BigDecimal(holidayObj2.getPriceFrom())).doubleValue();
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
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            diff = new BigDecimal(holidayObj2.getPriceFrom()).subtract(new BigDecimal(holidayObj1.getPriceFrom())).doubleValue();
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
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            String accom1 = holidayObj1.getName();
            String accom2 = holidayObj2.getName();

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
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            String accom1 = holidayObj1.getName();
            String accom2 = holidayObj2.getName();

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
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            return holidayObj1.getIndex() - holidayObj2.getIndex();

        }
    },

    COMMERICAL_PRIORITY_DESCENDING
    {
        @Override
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            return holidayObj2.getIndex() - holidayObj1.getIndex();
        }
    },


    TRATING_ASCENDING
    {
        private double diff;

        @Override
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            diff = Double.parseDouble(holidayObj1.gettRating()) - Double.parseDouble(holidayObj2.gettRating());
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
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            diff = Double.parseDouble(holidayObj2.gettRating()) - Double.parseDouble(holidayObj1.gettRating());
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

        @Override
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            if (StringUtils.isEmpty(holidayObj1.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj2.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(holidayObj1.getTripAdvisorRating())
                        - Double.parseDouble(DEFAULT_RATING);
            }
            else if (StringUtils.isEmpty(holidayObj2.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj1.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(DEFAULT_RATING)
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

        @Override
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            if (StringUtils.isEmpty(holidayObj1.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj2.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(holidayObj2.getTripAdvisorRating())
                        - Double.parseDouble(DEFAULT_RATING);
            }
            else if (StringUtils.isEmpty(holidayObj2.getTripAdvisorRating())&&StringUtils.isNotEmpty(holidayObj1.getTripAdvisorRating()))
            {
                diff = Double.parseDouble(DEFAULT_RATING)
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
        private Date dateObj1, dateObj2;
        private boolean dateDiff;
        @Override
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            if((holidayObj1.getAvailableFrom()!=null) && (holidayObj2.getAvailableFrom()!=null))
            {
                dateObj1 = holidayObj1.getAvailableFrom();
                dateObj2 = holidayObj2.getAvailableFrom();
                dateDiff = dateObj2.before(dateObj1);
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
        private Date dateObj1, dateObj2;
        private boolean dateDiff;
        @Override
        public int compare(final MerchandisedHoliday holidayObj1, final MerchandisedHoliday holidayObj2)
        {
            if((holidayObj1.getAvailableFrom()!=null) && (holidayObj2.getAvailableFrom()!=null))
            {
                dateObj1 = holidayObj1.getAvailableFrom();
                dateObj2 = holidayObj2.getAvailableFrom();
                dateDiff = dateObj1.before(dateObj2);
            }

            if (dateDiff){
                return 1;
            }else{
                return -1;
            }
        }
    };


    private static final String DEFAULT_RATING = "0.0";

    /**
     * This method will get the list of comparator and iterate through it, sort the result and return
     *
     * @param sortComparators
     * @return Comparator<SearchResultViewData>
     */
    public static Comparator<MerchandisedHoliday> getComparator(final List<MerchandisedHolidayComparator> sortComparators)
    {
        return new Comparator<MerchandisedHoliday>()
        {
            @Override
            public int compare(final MerchandisedHoliday o1, final MerchandisedHoliday o2)
            {
                for (final MerchandisedHolidayComparator sortComparator : sortComparators)
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
