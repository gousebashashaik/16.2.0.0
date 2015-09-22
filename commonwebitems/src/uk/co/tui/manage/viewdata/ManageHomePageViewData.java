/**
 *
 */
package uk.co.tui.manage.viewdata;

import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.manage.criteria.BookingSearchCriteria;


/**
 * @author veena.pn
 *
 */
public class ManageHomePageViewData
{

    private ManageHomePageStaticContentViewData manageHomePageContentViewData;
    private ErrorViewData error;
    private BookingSearchCriteria bookingSearchCriteria;
    private PageResponse pageResponse;

    /**
     * @return the manageHomePageContentViewData
     */
    public ManageHomePageStaticContentViewData getManageHomePageContentViewData()
    {
        return manageHomePageContentViewData;
    }

    /**
     * @param manageHomePageContentViewData
     *           the manageHomePageContentViewData to set
     */
    public void setManageHomePageContentViewData(final ManageHomePageStaticContentViewData manageHomePageContentViewData)
    {
        this.manageHomePageContentViewData = manageHomePageContentViewData;
    }

    /**
     * @return the error
     */
    public ErrorViewData getError()
    {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(ErrorViewData error)
    {
        this.error = error;
    }

    /**
     * @return the bookingSearchCriteria
     */
    public BookingSearchCriteria getBookingSearchCriteria()
    {
        return bookingSearchCriteria;
    }

    /**
     * @param bookingSearchCriteria the bookingSearchCriteria to set
     */
    public void setBookingSearchCriteria(BookingSearchCriteria bookingSearchCriteria)
    {
        this.bookingSearchCriteria = bookingSearchCriteria;
    }

    /**
     * @return the pageResponse
     */
    public PageResponse getPageResponse()
    {
        return pageResponse;
    }

    /**
     * @param pageResponse the pageResponse to set
     */
    public void setPageResponse(PageResponse pageResponse)
    {
        this.pageResponse = pageResponse;
    }




}
