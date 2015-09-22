/**
 *
 */
package uk.co.tui.manage.facade;

import uk.co.travel.domain.manage.request.BookingSearchRequest;
import uk.co.travel.domain.manage.response.DisplayMemoResponse;
import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.manage.criteria.BookingSearchCriteria;
import uk.co.tui.manage.services.exception.AmendNCancelServiceException;
import uk.co.tui.manage.viewdata.ManageHomePageViewData;
import uk.co.tui.manage.viewdata.SecurityQuestionsResultViewData;
import uk.co.tui.manage.viewdata.bookingsummary.BookingSummaryPageViewData;


/**
 * @author veena.pn
 *
 */
public interface DisplayBookingFacade
{


    BookingSummaryPageViewData displayBooking(BookingSearchCriteria bookingSearchCriteria, String hostName);

    /**
     * @param homePageViewData
     */
    void populateManageHomePageStaticContentViewData(ManageHomePageViewData homePageViewData);

    void populateBookingSummaryPageStaticContentViewData(BookingSummaryPageViewData homePageViewData);

    DisplayMemoResponse getBookingMemo(final BookingSearchRequest bookingSearchRequest,
            BookingSummaryPageViewData bookingSummaryPageViewData) throws AmendNCancelServiceException;

    /**
     * @param leadPaxShortAddress
     * @param contactNumber
     * @return passengerAddressViewData
     */
    SecurityQuestionsResultViewData validateLeadPaxDetails(String leadPaxShortAddress, String contactNumber);

    BookingSummaryPageViewData displayBooking(String hostName);



    /**
     * @param pageType
     * @return
     */
    PageResponse makeSecuredPage(String pageType);
}
