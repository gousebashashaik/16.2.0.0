/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.domain.lite.Money;


/**
 * @author veena.pn
 *
 */
public class BookingDetailsViewData
{

    private String bookingReferenceNumber;

    private List<BookingHistoryViewData> bookingHistory;

    private Money amountPaid;

    private Money amountDue;

    /** The currency appended amount due. */
    private String currencyAppendedAmountDue = StringUtils.EMPTY;

    /** The currency appended amount paid. */
    private String currencyAppendedAmountPaid = StringUtils.EMPTY;

    private String dueDate;



    /**
     * @return the bookingReferenceNumber
     */
    public String getBookingReferenceNumber()
    {
        return bookingReferenceNumber;
    }

    /**
     * @param bookingReferenceNumber
     *           the bookingReferenceNumber to set
     */
    public void setBookingReferenceNumber(final String bookingReferenceNumber)
    {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    /**
     * @return the bookingHistory
     */
    public List<BookingHistoryViewData> getBookingHistory()
    {
        return bookingHistory;
    }

    /**
     * @param bookingHistory
     *           the bookingHistory to set
     */
    public void setBookingHistory(final List<BookingHistoryViewData> bookingHistory)
    {
        this.bookingHistory = bookingHistory;
    }

    /**
     * @return the amountPaid
     */
    public Money getAmountPaid()
    {
        return amountPaid;
    }

    /**
     * @param amountPaid
     *           the amountPaid to set
     */
    public void setAmountPaid(final Money amountPaid)
    {
        this.amountPaid = amountPaid;
    }

    /**
     * @return the amountDue
     */
    public Money getAmountDue()
    {
        return amountDue;
    }

    /**
     * @param amountDue
     *           the amountDue to set
     */
    public void setAmountDue(final Money amountDue)
    {
        this.amountDue = amountDue;
    }

    /**
     * @return the currencyAppendedAmountDue
     */
    public String getCurrencyAppendedAmountDue()
    {
        return currencyAppendedAmountDue;
    }

    /**
     * @param currencyAppendedAmountDue
     *           the currencyAppendedAmountDue to set
     */
    public void setCurrencyAppendedAmountDue(final String currencyAppendedAmountDue)
    {
        this.currencyAppendedAmountDue = currencyAppendedAmountDue;
    }

    /**
     * @return the currencyAppendedAmountPaid
     */
    public String getCurrencyAppendedAmountPaid()
    {
        return currencyAppendedAmountPaid;
    }

    /**
     * @param currencyAppendedAmountPaid the currencyAppendedAmountPaid to set
     */
    public void setCurrencyAppendedAmountPaid(String currencyAppendedAmountPaid)
    {
        this.currencyAppendedAmountPaid = currencyAppendedAmountPaid;
    }

    /**
     * @return the dueDate
     */
    public String getDueDate()
    {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }


}
