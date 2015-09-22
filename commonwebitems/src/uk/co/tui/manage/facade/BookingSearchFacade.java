/**
 *
 */
package uk.co.tui.manage.facade;

import uk.co.portaltech.tui.web.view.data.BookingResultsViewData;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.manage.criteria.BookingSearchCriteria;


/**
 * @author veena.pn
 *
 */
public interface BookingSearchFacade
{
    BookingResultsViewData findBookings(BookingSearchCriteria bookingSearchCriteria) throws SearchResultsBusinessException;

    /**
     * @param bookingSearchCriteria
     * @return
     */
    BookingResultsViewData getPagedData(BookingSearchCriteria bookingSearchCriteria) throws SearchResultsBusinessException;
}
