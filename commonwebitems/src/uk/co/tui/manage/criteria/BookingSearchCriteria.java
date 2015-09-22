/**
 *
 */
package uk.co.tui.manage.criteria;

import java.util.Date;


/**
 * @author veena.pn
 *
 */
public class BookingSearchCriteria
{

    /**
     * The booking Reference ID
     */
    private String bookingRefereneceId;
    /**
     * The Departure Date
     */
    private Date departureDate;

    /**
     *
     * The surname
     */
    private String secPassengerName;


    private int first;

    private int offset;


    /**
     * @param bookingReference
     * @param date
     * @param surname
     */
    public BookingSearchCriteria(String bookingReference, Date date, String surname)
    {
        this.bookingRefereneceId = bookingReference;
        setDepartureDate(date);
        this.secPassengerName = surname;
    }

    /**
     * @return the bookingRefereneceId
     */
    public String getBookingRefereneceId()
    {
        return bookingRefereneceId;
    }

    /**
     * @param bookingRefereneceId
     *           the bookingRefereneceId to set
     */
    public void setBookingRefereneceId(final String bookingRefereneceId)
    {
        this.bookingRefereneceId = bookingRefereneceId;
    }

    /**
     * @return the departureDate
     */
    public Date getDepartureDate()
    {
        if (this.departureDate == null)
        {
            return null;
        }
        return new Date(this.departureDate.getTime());
    }

    /**
     * @param departureDate
     *           the departureDate to set
     */
    public void setDepartureDate(final Date departureDate)
    {
        if (departureDate == null)
        {
            this.departureDate = null;
        }
        else
        {
            this.departureDate = new Date(departureDate.getTime());
        }
    }


    /**
     * @return the first
     */
    public int getFirst()
    {
        return first;
    }

    /**
     * @param first
     *           the first to set
     */
    public void setFirst(final int first)
    {
        this.first = first;
    }

    /**
     * @return the offset
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * @param offset
     *           the offset to set
     */
    public void setOffset(final int offset)
    {
        this.offset = offset;
    }

    /**
     * @return the sec_passengerName
     */
    public String getSecPassengerName()
    {
        return secPassengerName;
    }

    /**
     * @param sec_passengerName
     *           the sec_passengerName to set
     */
    public void setSecPassengerName(final String sec_passengerName)
    {
        this.secPassengerName = sec_passengerName;
    }





}
