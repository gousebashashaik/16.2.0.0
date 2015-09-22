/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;


/**
 * @author veena.pn
 *
 */
public class BookingSummaryViewData
{

    /**
     * The booking Reference
     */
    private String bookingReference;
    /**
     * The lead passenger name
     */
    private String leadPassengerName;
    /**
     * The other passengers
     */
    private List<String> otherPassengers;
    /**
     * The booking shop name
     */
    private String bookingShopName;
    /**
     * The booking destination
     */
    private String bookingDestination;
    /**
     * The booking address
     */
    private BookingAddressViewData bookingAddress;

    private String departureDate;

    private String destinationName;

    /**
     * @return the bookingReference
     */
    public String getBookingReference()
    {
        return bookingReference;
    }

    /**
     * @param bookingReference
     *           the bookingReference to set
     */
    public void setBookingReference(final String bookingReference)
    {
        this.bookingReference = bookingReference;
    }

    /**
     * @return the leadPassengerName
     */
    public String getLeadPassengerName()
    {
        return leadPassengerName;
    }

    /**
     * @param leadPassengerName
     *           the leadPassengerName to set
     */
    public void setLeadPassengerName(final String leadPassengerName)
    {
        this.leadPassengerName = leadPassengerName;
    }

    /**
     * @return the otherPassengers
     */
    public List<String> getOtherPassengers()
    {
        return otherPassengers;
    }

    /**
     * @param otherPassengers
     *           the otherPassengers to set
     */
    public void setOtherPassengers(final List<String> otherPassengers)
    {
        this.otherPassengers = otherPassengers;
    }

    /**
     * @return the bookingShopName
     */
    public String getBookingShopName()
    {
        return bookingShopName;
    }

    /**
     * @param bookingShopName
     *           the bookingShopName to set
     */
    public void setBookingShopName(final String bookingShopName)
    {
        this.bookingShopName = bookingShopName;
    }

    /**
     * @return the bookingDestination
     */
    public String getBookingDestination()
    {
        return bookingDestination;
    }

    /**
     * @param bookingDestination
     *           the bookingDestination to set
     */
    public void setBookingDestination(final String bookingDestination)
    {
        this.bookingDestination = bookingDestination;
    }

    /**
     * @return the bookingAddress
     */
    public BookingAddressViewData getBookingAddress()
    {
        return bookingAddress;
    }

    /**
     * @param bookingAddress
     *           the bookingAddress to set
     */
    public void setBookingAddress(final BookingAddressViewData bookingAddress)
    {
        this.bookingAddress = bookingAddress;
    }

    /**
     * @return the departureDate
     */
    public String getDepartureDate()
    {
        return departureDate;
    }

    /**
     * @param departureDate
     *           the departureDate to set
     */
    public void setDepartureDate(final String departureDate)
    {
        this.departureDate = departureDate;
    }

    /**
     * @return the destinationName
     */
    public String getDestinationName()
    {
        return destinationName;
    }

    /**
     * @param destinationName
     *           the destinationName to set
     */
    public void setDestinationName(final String destinationName)
    {
        this.destinationName = destinationName;
    }



}
