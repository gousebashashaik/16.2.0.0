/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.tui.manage.criteria.BookingSearchCriteria;


/**
 * @author veena.pn
 *
 */
public class BookingResultsViewData
{
    /**
     * The results available
     */
    private boolean resultsAvailable;
    /**
     * The list of booking summary
     */
    private List<BookingSummaryViewData> bookingsSummary;

    private BookingSearchCriteria bookingSearchCriteria;


    private int totalNoOfResults;

    private int offset;


    private int maxNoOfPages;

    /**
     * @return the resultsAvailable
     */
    public boolean isResultsAvailable()
    {
        return resultsAvailable;
    }

    /**
     * @param resultsAvailable
     *           the resultsAvailable to set
     */
    public void setResultsAvailable(final boolean resultsAvailable)
    {
        this.resultsAvailable = resultsAvailable;
    }

    /**
     * @return the bookingsSummary
     */
    public List<BookingSummaryViewData> getBookingsSummary()
    {
        return bookingsSummary;
    }

    /**
     * @param bookingsSummary
     *           the bookingsSummary to set
     */
    public void setBookingsSummary(final List<BookingSummaryViewData> bookingsSummary)
    {
        this.bookingsSummary = bookingsSummary;
    }

    /**
     * @return the bookingSearchCriteria
     */
    public BookingSearchCriteria getBookingSearchCriteria()
    {
        return bookingSearchCriteria;
    }

    /**
     * @return the totalNoOfResults
     */
    public int getTotalNoOfResults()
    {
        return totalNoOfResults;
    }

    /**
     * @param totalNoOfResults
     *           the totalNoOfResults to set
     */
    public void setTotalNoOfResults(final int totalNoOfResults)
    {
        this.totalNoOfResults = totalNoOfResults;
    }

    /**
     * @param bookingSearchCriteria
     *           the bookingSearchCriteria to set
     */
    public void setBookingSearchCriteria(final BookingSearchCriteria bookingSearchCriteria)
    {
        this.bookingSearchCriteria = bookingSearchCriteria;
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
     * @return the maxNoOfPages
     */
    public int getMaxNoOfPages()
    {
        return maxNoOfPages;
    }

    /**
     * @param maxNoOfPages
     *           the maxNoOfPages to set
     */
    public void setMaxNoOfPages(final int maxNoOfPages)
    {
        this.maxNoOfPages = maxNoOfPages;
    }


}
