/**
 *
 */
package uk.co.portaltech.tui.helper;

import java.util.List;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.MerchandisedHoliday;
import uk.co.portaltech.travel.model.results.MerchandisedResult;
import uk.co.portaltech.tui.web.view.data.BookingResultsViewData;
import uk.co.portaltech.tui.web.view.data.BookingSummaryViewData;
import uk.co.portaltech.tui.web.view.data.MerchandiserRequest;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.tui.manage.criteria.BookingSearchCriteria;


/**
 *
 */
public class Pagination
{

    /**
     *
     */


    public List<Holiday> paginateResults(final EndecaSearchResult searchResult, final SearchResultsRequestData requestData)
    {
        final Pageable p = new Pageable<Holiday>(searchResult.getHolidays(), requestData.getOffset());
        p.setPage(requestData.getFirst());
        p.setPageSize(requestData.getOffset());
        return p.getListForPage();
    }

    public List<SearchResultViewData> paginateSearchResultsViewData(final SearchResultsViewData searchResult, final int page,
            final int offset)
    {
        final SeachResultsViewDataPageable p = new SeachResultsViewDataPageable(searchResult.getHolidays(),
                offset);
        p.setPage(page);
        p.setPageSize(offset);
        return p.getListForPage();
    }

    public List<MerchandisedHoliday> paginateMerchandiserResults(final MerchandisedResult searchResult,
            final MerchandiserRequest requestData)
    {
        final Pageable p = new Pageable<MerchandisedHoliday>(searchResult.getHolidays(), requestData.getOffset());
        p.setPage(requestData.getFirst());
        p.setPageSize(requestData.getOffset());
        return p.getListForPage();
    }



    /**
     * @param bookingresults
     * @param bookingSearchCriteria
     * @return
     */

    public List<BookingSummaryViewData> paginateBookingSearchResults(final BookingResultsViewData bookingresults,
            final BookingSearchCriteria bookingSearchCriteria)
    {
        final Pageable p = new Pageable<BookingSummaryViewData>(bookingresults.getBookingsSummary(),
                bookingSearchCriteria.getOffset());
        p.setPage(bookingSearchCriteria.getFirst());
        p.setPageSize(bookingSearchCriteria.getOffset());
        return p.getListForPage();
    }


}
