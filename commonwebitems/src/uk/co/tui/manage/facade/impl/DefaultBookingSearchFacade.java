/**
 *
 */
package uk.co.tui.manage.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.tui.components.model.SearchBookingComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.helper.Pagination;
import uk.co.portaltech.tui.web.view.data.BookingResultsViewData;
import uk.co.portaltech.tui.web.view.data.BookingSummaryViewData;
import uk.co.travel.domain.manage.request.BookingSearchRequest;
import uk.co.travel.domain.manage.response.BookingSearchResponse;
import uk.co.travel.domain.manage.response.BookingSummary;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.manage.criteria.BookingSearchCriteria;
import uk.co.tui.manage.facade.BookingSearchFacade;
import uk.co.tui.manage.services.exception.AmendNCancelServiceException;
import uk.co.tui.manage.services.inventory.BookingSearchService;


/**
 * @author veena.pn
 *
 */
public class DefaultBookingSearchFacade implements BookingSearchFacade
{

    /*
     * (non-Javadoc)
     *
     * @see uk.co.tui.manage.facade.BookingSearchFacade#findBookings(uk.co.tui.manage.criteria.BookingSearchCriteria)
     */
    @Resource
    private BookingSearchService bookingSearchService;

    @Resource
    private Populator<BookingSearchResponse, BookingResultsViewData> bookingResultsPopulator;

    @Autowired
    private ComponentFacade componentFacade;

    private static final String SEARCH_RESULTS = "AMEND_SEARCH_RESULTS";

    @Resource
    private Pagination pagination;

    @Resource
    private SessionService sessionService;

    private static final Logger LOGGER = Logger.getLogger(DefaultBookingSearchFacade.class);

    @Override
    public BookingResultsViewData findBookings(final BookingSearchCriteria bookingSearchCriteria)
            throws SearchResultsBusinessException
    {

        LOGGER.info("Entering Default Display Booking Facade");

        BookingSearchRequest bookingSearchRequest = null;
        final BookingResultsViewData bookingresults = new BookingResultsViewData();
        SearchBookingComponentModel searchComponent;

        try
        {
            searchComponent = componentFacade.getComponent("searchBookingComponent_comp");
        }
        catch (final NoSuchComponentException e)
        {
            Log.error("No search panel component");
            throw new SearchResultsBusinessException("3001", e);
        }
        bookingSearchRequest = getBookingSearchRequest(bookingSearchCriteria);

        try
        {
            final BookingSearchResponse bookingResponse = bookingSearchService.searchBookings(bookingSearchRequest);
            if (bookingResponse.getBookingSummary() != null)
            {
                sortByDeptDate(bookingResponse.getBookingSummary());
            }
            bookingResultsPopulator.populate(bookingResponse, bookingresults);
            bookingSearchCriteria.setOffset(searchComponent.getOffset());
            bookingresults.setTotalNoOfResults(bookingResponse.getTotalNoOfBookings());
            bookingresults.setOffset(searchComponent.getOffset());
            bookingresults.setMaxNoOfPages(Integer.parseInt(Config.getParameter("anc.pagination.maxpages")));
            handlePagedResults(bookingresults, bookingSearchCriteria, bookingResponse);
        }
        catch (final AmendNCancelServiceException e)
        {
            Log.error("Caught AmendNCancelServiceException ", e);
        }
        return bookingresults;
    }

    private void sortByDeptDate(final List<BookingSummary> bookingSummaryList)
    {
        Collections.sort(bookingSummaryList, new Comparator<BookingSummary>()
        {

            @Override
            public int compare(final BookingSummary obj1, final BookingSummary obj2)
            {
                return obj1.getDepartureDate().compareTo(obj2.getDepartureDate());
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.tui.manage.facade.BookingSearchFacade#getPagedData(uk.co.tui.manage.criteria.BookingSearchCriteria)
     */
    @Override
    public BookingResultsViewData getPagedData(final BookingSearchCriteria bookingSearchCriteria)
            throws SearchResultsBusinessException
    {
        LOGGER.info("Entering Default Display Booking Facade for paged data");
        final BookingResultsViewData bookingresults = new BookingResultsViewData();
        BookingSearchResponse bookingResponse = sessionService.getAttribute(SEARCH_RESULTS);

        if (bookingResponse == null)
        {
            LOGGER.info("response is not found in session");
            try
            {
                bookingResponse = bookingSearchService.searchBookings(getBookingSearchRequest(bookingSearchCriteria));
            }
            catch (final AmendNCancelServiceException e)
            {
                Log.error("Caught AmendNCancelServiceException ", e);
            }
        }
        bookingResultsPopulator.populate(bookingResponse, bookingresults);

        handlePagedResults(bookingresults, bookingSearchCriteria, bookingResponse);


        return bookingresults;

    }

    /**
     * @param bookingresults
     * @param bookingSearchCriteria
     */
    private void handlePagedResults(final BookingResultsViewData results, final BookingSearchCriteria criteria,
            final BookingSearchResponse response)
    {
        if (results.getBookingsSummary() != null)
        {
            final List<BookingSummaryViewData> paginatedHolidays = pagination.paginateBookingSearchResults(results, criteria);
            results.setBookingsSummary(paginatedHolidays);
        }

        sessionService.setAttribute(SEARCH_RESULTS, response);

    }

    private BookingSearchRequest getBookingSearchRequest(final BookingSearchCriteria bookingSearchCriteria)
    {

        final BookingSearchRequest bookingSearchRequest = new BookingSearchRequest();
        bookingSearchRequest.setDepartureDateFrom(bookingSearchCriteria.getDepartureDate());
        bookingSearchRequest.setDepartureDateTo(new Date());
        bookingSearchRequest.setPaxSurName(bookingSearchCriteria.getSecPassengerName());
        bookingSearchRequest.setBookingReferenceNum(bookingSearchCriteria.getBookingRefereneceId());

        return bookingSearchRequest;
    }

}
