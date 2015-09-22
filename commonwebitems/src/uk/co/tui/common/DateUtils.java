package uk.co.tui.common;

import de.hybris.platform.util.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * The Class DateUtils.
 */
@SuppressWarnings("deprecation")
public final class DateUtils {
    /**
     * The constant hourly gap
     */
    private static final int HOURLYGAP = 24;
    /** The Constant DATE_FORMAT. */
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    /** The Constant DATE_FORMAT_1. */
    private static final String DATE_FORMAT_1 = "yyyy-MM-dd";

    /** The Constant DATE_FORMAT_2. */
    private static final String DATE_FORMAT_2 = "dd/MM/yyyy";

    /** The Constant DATE_FORMAT_3. */
    private static final String DATE_FORMAT_5 = "dd/MM/yyyy HH:mm";
    private static final String DATE_FORMAT_6 = "E dd MMM yyyy";

    private static final String DATE_FORMAT_7 = "dd MMM yyyy";

    /** The Constant TIME_FORMAT. */
    private static final String TIME_FORMAT = "HH:mm";

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(DateUtils.class);

    /** The Constant TEN. */
    private static final int TEN = 10;

    /** The Constant ZERO. */
    private static final Object ZERO = "0";

    /** The Constant YEAR_MONTHS. */
    private static final int YEAR_MONTHS = 12;

    /** The Constant DELIMITER. */
    private static final String DELIMITER = "/";

    /** The Constant TIME_FORMAT_WITHOUTCOLON. */
    private static final String TIME_FORMAT_WITHOUTCOLON = "HHmm";

    /** The Constant TIME_FORMAT_WITHCOLON. */
    private static final String TIME_FORMAT_WITHCOLON = "HH:mm";

    /**
     * To Display the full name of the month The Constant MONTH_PATTERN.
     */
    private static final String MONTH_PATTERN = "MMM";

    /**
     * To Display the full name of the month The Constant MONTH_PATTERN.
     */
    private static final String DAY_PATTERN = "E";

    /**
     * Space to append in Date The Constant SPACE.
     * */
    private static final String SPACE = " ";

    private static Map<String, Integer> daysOfTheWeek = new HashMap<String, Integer>();
    /**
     * Monday constant
     */
    private static final int DAY_ONE = 1;
    /**
     * Tuesday constant
     */
    private static final int DAY_TWO = 2;
    /**
     * Wednesday constant
     */
    private static final int DAY_THREE = 3;
    /**
     * Thursday constant
     */
    private static final int DAY_FOUR = 4;
    /**
     * Friday constant
     */
    private static final int DAY_FIVE = 5;
    /**
     * Saturday constant
     */
    private static final int DAY_SIX = 6;
    /**
     * Sunday constant
     */
    private static final int DAY_SEVEN = 7;

    /**
     * Sub string position constant
     */
    private static final int SUBSTRING_POS_TWO = 2;
    /**
     * Sub string position constant
     */
    private static final int SUBSTRING_POS_FOUR = 4;
    /**
     * The Minute constant
     */
    private static final int CUSTOM_FORMAT_MINUTE =60;
    /**
     * Duration constant
     */
    private static final long  DURATION = 3600000;
    /**
     * Minute
     */
    private static final int MINUTE = 60;


    static {
        daysOfTheWeek.put("Mon", Integer.valueOf(DAY_ONE));
        daysOfTheWeek.put("Tue", Integer.valueOf(DAY_TWO));
        daysOfTheWeek.put("Wed", Integer.valueOf(DAY_THREE));
        daysOfTheWeek.put("Thu", Integer.valueOf(DAY_FOUR));
        daysOfTheWeek.put("Fri", Integer.valueOf(DAY_FIVE));
        daysOfTheWeek.put("Sat", Integer.valueOf(DAY_SIX));
        daysOfTheWeek.put("Sun", Integer.valueOf(DAY_SEVEN));
    }

    /**
     * Instantiates a new date utils.
     */
    private DateUtils() {

    }

    /**
     * Format the time from the given hhmm format to hh:mm format. for a given
     * input 0830 the output is 08:30
     *
     * @param time
     *            the time
     * @return the string
     */
    public static String formatTime(final String time) {
        final DateTimeFormatter fmt = DateTimeFormat
                .forPattern(TIME_FORMAT_WITHOUTCOLON);
        // construct date time from local midnight + parsed hhmm
        final DateTime localDateTime = new LocalDate().toDateTime(fmt
                .parseDateTime(time).toLocalTime());
        return formatTime(localDateTime);
    }

    /**
     * Format time.
     *
     * @param localDate
     *            the local date
     * @return the string
     */
    public static String formatTime(final DateTime localDate) {
        return localDate.toString(DateTimeFormat.forPattern(TIME_FORMAT));
    }

    /**
     * Format.
     *
     * @param localDate
     *            the local date
     * @return the string
     */
    public static String format(final DateTime localDate) {
        return localDate.toString(DateTimeFormat.forPattern(DATE_FORMAT));
    }

    /**
     * @param localDate
     * @return localDate in string format
     */
    public static String newDateFormat(final DateTime localDate) {
        return localDate.toString(DateTimeFormat.forPattern(DATE_FORMAT_6));
    }

    /**
     * @param localDate
     * @return localDate in string format
     */
    public static String formatForDb(final DateTime localDate) {
        return localDate.toString(DateTimeFormat.forPattern(DATE_FORMAT_1));
    }

    /**
     * @param dateString
     *            the date string
     * @return the string
     */
    public static String formatdate(final String dateString) {
        final DateFormat from = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = from.parse(dateString);
        } catch (final ParseException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_1);
        return format.format(date);

    }

    /**
     * @param dateString
     * @param fromFormat
     * @param toFormat
     * @return String
     */
    public static String formatdate(final String dateString,
            final String fromFormat, final String toFormat) {
        final DateFormat from = new SimpleDateFormat(fromFormat);
        Date date = null;
        try {
            date = from.parse(dateString);
        } catch (final ParseException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
        final SimpleDateFormat format = new SimpleDateFormat(toFormat);
        return format.format(date);
    }

    /**
     * Format.
     *
     * @param localDate
     *            the local date
     * @return the string
     */
    public static String format(final LocalDate localDate) {
        return localDate.toString(DateTimeFormat.forPattern(DATE_FORMAT));
    }

    /**
     * Format.
     *
     * @param localDate
     *            the local date
     * @param dateFormat
     *            the date format
     * @return the string
     */
    public static String format(final LocalDate localDate,
            final String dateFormat) {
        return localDate.toString(DateTimeFormat.forPattern(dateFormat));
    }

    /**
     * Subtract dates.
     *
     * @param start
     *            the start
     * @param end
     *            the end
     * @return the int
     */
    public static int subtractDates(final DateTime start, final DateTime end) {
        return Days.daysBetween(end.toDateMidnight(), start.toDateMidnight())
                .getDays();
    }

    /**
     * @param depDate
     * @param arrDate
     * @param depTime
     * @param arrTime
     * @return the string
     */
    public static String substractTimes(final String depDate,
            final String arrDate, final String depTime, final String arrTime) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                DATE_FORMAT_5);
        try {
            final Date departDate = simpleDateFormat.parse(getDate(depDate,
                    depTime));
            final Date arrivalDate = simpleDateFormat.parse(getDate(arrDate,
                    arrTime));

            final long diff = arrivalDate.getTime() - departDate.getTime();
            final double duration = diff / ((double) DURATION);

            final double hours = (int) duration;
            final double minutes = (duration - (int) duration) * MINUTE;
            // System.out.println((int) hours + "hrs" + " " +

            return (int) hours + "hrs" + " " + Math.round(minutes) + "min";

        } catch (final ParseException e) {
            LOG.error("Error while parsing dates");
            return null;
        }
    }

    /**
     * @param date
     * @param time
     * @return the string
     */
    private static String getDate(final String date, final String time) {
        return date.replaceAll("-", "/") + " "
                + StringUtils.substring(time, 0, SUBSTRING_POS_TWO) + ":"
                + StringUtils.substring(time, SUBSTRING_POS_TWO, SUBSTRING_POS_FOUR);
    }

    /**
     * Subtract dates.
     *
     * @param start
     *            the start
     * @param end
     *            the end
     * @return the int
     */
    public static int subtractDates(final LocalDate start, final LocalDate end) {
        return Days.daysBetween(end, start).getDays();
    }

    /**
     * Currentdate.
     *
     * @return the local date
     */
    public static LocalDate currentdate() {
        return new LocalDate();
    }

    /**
     * Currentdate time.
     *
     * @return the date time
     */
    public static DateTime currentdateTime() {
        return new DateTime();
    }

    /**
     * Formatted month.
     *
     * @param departureDate
     *            the departure date
     * @return the string
     */
    public static String formattedMonth(final LocalDate departureDate) {
        final StringBuilder monthField = new StringBuilder();
        if (departureDate.getMonthOfYear() < TEN) {
            monthField.append(ZERO);
        }
        monthField.append(departureDate.getMonthOfYear());
        return monthField.toString();
    }

    /**
     * Formatted date.
     *
     * @param departureDate
     *            the departure date
     * @return the string
     */
    public static String formattedDate(final LocalDate departureDate) {
        final StringBuilder dateField = new StringBuilder();
        if (departureDate.getDayOfMonth() < TEN) {
            dateField.append(ZERO);
        }
        dateField.append(departureDate.getDayOfMonth());
        return dateField.toString();
    }

    /**
     * No of months between dates.
     *
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     * @return the int
     */
    public static int noOfMonthsBetweenDates(final LocalDate startDate,
            final LocalDate endDate) {
        return (endDate.getYear() * YEAR_MONTHS + endDate.getMonthOfYear()
                - startDate.getYear() * YEAR_MONTHS + startDate
                    .getMonthOfYear()) + 1;
    }

    /**
     * @param startDate
     * @param months
     * @return DateTime
     */
    public static DateTime dateAfterSoManyMonths(final DateTime startDate,
            final int months) {
        return startDate.plusMonths(months);
    }

    /**
     * Change format.
     *
     * @param dateString
     *            the date string
     * @return the date
     */
    public static Date changeFormat(final String dateString) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_2).parse(dateString);
        } catch (final ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param dateValue
     * @return Date
     */
    public static Date convertDate(final String dateValue) {
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_6);
        Date date = null;
        try {
            date = format.parse(dateValue);
            return date;
        } catch (final ParseException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param date1
     * @param inputDate
     * @return boolean
     */
    public static boolean isEqualOrAfter(final Date date1, final String inputDate) {

        final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_2);
        Date date = null;
        boolean flag = false;
        try {
            date = df.parse(inputDate);
            flag = date1.equals(date) || date1.after(date);
        } catch (final ParseException e) {
            LOG.error("not able to parse date.", e);
        }
        return flag;

    }
    /**
     * @param date1
     * @param inputDate
     * @return boolean
     */
    public static boolean isEqualOrAfter(String firstDate,  String secDate) {

         SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date date1 = null,date2=null;
        boolean flag = false;
        try {
            date1 = df.parse(firstDate);
            date2 = df.parse(secDate);
            flag = date1.equals(date2) || date1.after(date2);
        } catch (final ParseException e) {
            LOG.error("not able to parse date.", e);
        }
        return flag;

    }


    /**
     * @param date1
     * @param inputDate
     * @return boolean
     */
    public static boolean isEqualOrBefore(final Date date1, final String inputDate) {

        final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_2);
        Date date = null;
        boolean flag = false;
        try {
            date = df.parse(inputDate);
            flag = date1.equals(date) || date1.before(date);
        } catch (final ParseException e) {
            LOG.error("not able to parse date.", e);
        }
        return flag;

    }

    /**
     * Gets the date in string format.
     *
     * @param date
     *            the date
     * @return the date in string format
     */
    public static String getDateInStringFormat(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_2);
        return sdf.format(date);
    }

    /**
     * Formats the given day, month and year to Date.
     *
     * @param day
     *            the day
     * @param month
     *            the month
     * @param year
     *            the year
     * @return the Date
     */
    public static Date toUtilDate(final String day, final String month,
            final String year) {
        if (checkDayMonthYearIsNotEmpty(day, month, year)) {
            final StringBuilder dob = new StringBuilder();
            dob.append(day).append(DELIMITER).append(month).append(DELIMITER)
                    .append(year);
            try {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        DATE_FORMAT_2);
                simpleDateFormat.setLenient(false);
                return simpleDateFormat.parse(dob.toString());
            } catch (final ParseException parseException) {
                LOG.error(parseException.getMessage(), parseException);
            }
        }
        return null;
    }

    /**
     * @param day
     * @param month
     * @param year
     * @return boolean value
     */
    private static boolean checkDayMonthYearIsNotEmpty(final String day,
            final String month, final String year) {
        return StringUtils.isNotEmpty(day) && StringUtils.isNotEmpty(month)
                && StringUtils.isNotEmpty(year);
    }

    /**
     * Custom format time.
     *
     * @param time
     *            the time
     * @return the string
     */
    public static String customFormatTime(final String time) {
        final DateTimeFormatter fmt = DateTimeFormat
                .forPattern(TIME_FORMAT_WITHCOLON);
        // construct date time from local midnight + parsed hh:mm
        final DateTime localDateTime = new LocalDate().toDateTime(fmt
                .parseDateTime(time).toLocalTime());
        final StringBuilder customTime = new StringBuilder();
        customTime.append(localDateTime.getHourOfDay() + "hrs "
                + localDateTime.getMinuteOfDay() % CUSTOM_FORMAT_MINUTE
                + "mins");
        return customTime.toString();
    }

    /**
     * Custom format flight date for ex.Tue 1st Oct 2013.
     *
     * @param depArrDate
     *            the dep arr date
     * @return the string
     */
    public static String customFormatFlightDate(final Date depArrDate) {
        final StringBuilder fullDate = new StringBuilder();
        final LocalDate dueDate = new LocalDate(depArrDate);
        final String day = new SimpleDateFormat(DAY_PATTERN).format(depArrDate);
        fullDate.append(day).append(SPACE);
        final int dayOfMonth = dueDate.getDayOfMonth();
        fullDate.append(dayOfMonth).append(SPACE);
        final String month = new SimpleDateFormat(MONTH_PATTERN)
                .format(depArrDate);
        fullDate.append(month).append(SPACE);
        fullDate.append(Integer.toString(dueDate.getYear()));
        return fullDate.toString();
    }

    /**
     * Calculate time of flight. Takes arrival and departure time & computes the
     * time to travel. Renders the journey duration in the form 3hrs 20mins
     *
     * @param arrTime
     *            the arr time
     * @param depTime
     *            the dep time
     * @return the string
     */
    public static String calculateTimeOfFlight(final String arrTime,
            final String depTime) {
        final int arrHours = NumberUtils.stringToInt(StringUtils.substring(
                arrTime, 0, SUBSTRING_POS_TWO));
        final int arrMins = NumberUtils.stringToInt(StringUtils.substring(
                arrTime, SUBSTRING_POS_TWO, SUBSTRING_POS_FOUR));
        final int depHours = NumberUtils.stringToInt(StringUtils.substring(
                depTime, 0, SUBSTRING_POS_TWO));
        final int depMins = NumberUtils.stringToInt(StringUtils.substring(
                depTime, SUBSTRING_POS_TWO, SUBSTRING_POS_FOUR));
        int hourlyGap = arrHours - depHours;
        if (hourlyGap < 0) {
            hourlyGap %= HOURLYGAP;
        }
        return Math.abs(hourlyGap) + "hrs " + Math.abs(arrMins - depMins)
                + "mins";
    }

    /**
     * Adds the days to date.
     *
     * @param date
     *            the date
     * @param days
     *            the days
     * @return the date
     */
    public static Date addDaysToDate(final Date date, final int days) {

        final Calendar calendar = Utilities.getDefaultCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * Are dates same.
     *
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @return true, if successful
     */
    public static boolean areDatesSame(final Date date1, final Date date2) {
        return org.apache.commons.lang.time.DateUtils.isSameDay(date1, date2);
    }

    /**
     * Gets the date in string format.
     *
     * @param date
     *            the date
     * @return the date in string format
     */
    public static String getDateInStringFormat(final Date date,
            final String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * Method checks for date pattern yyyy-MM-dd
     *
     * @param dateStr
     * @return boolen
     */
    public static boolean verfiyDateFormat(final String dateStr) {
        final Pattern datePattern = Pattern
                .compile("((20)\\d{2})-([1-9]|0[1-9]|1[0-2])-(0[1-9]|[1-9]|[12][0-9]|3[01])");
        final Matcher dateMatcher = datePattern.matcher(dateStr);
        return dateMatcher.matches();
    }

    /**
     * @param todayDate
     * @param days
     * @return DateTime
     */
    public static DateTime addDaysToDate(final DateTime todayDate,
            final int days) {

        return todayDate.plusDays(days);
    }

    public static String amendnCancelDateFormat(final DateTime localDate)
     {

         return localDate.toString(DateTimeFormat.forPattern(DATE_FORMAT_7));
     }
}
