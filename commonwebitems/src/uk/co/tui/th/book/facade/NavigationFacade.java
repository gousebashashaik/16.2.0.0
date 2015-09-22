/**
 *
 */
package uk.co.tui.th.book.facade;

import java.util.List;

import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.book.page.enums.PageType;
import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.th.book.view.data.AlertViewData;
import uk.co.tui.th.exception.TUIBusinessException;


/**
 * The Interface NavigationFacade.
 *
 * @author samantha.gd
 */
public interface NavigationFacade {

    /**
     * Builds the navigation data.
     *
     * @param brand
     *            the brand
     * @param packageType
     *            the package type
     * @param pageType
     *            the page type
     * @return the page response
     */
    PageResponse buildNavigationData(Brand brand, PackageType packageType,
            PageType pageType);

    /**
     * Process holiday.
     *
     * @throws TUIBusinessException
     *             the tUI business exception
     * @throws FlightSoldOutException
     *             the flight sold out exception
     */
    void processHoliday() throws TUIBusinessException, FlightSoldOutException;

    /**
     * Navigate.
     *
     * @param inventorySystem
     *            the inventory system
     * @return the list
     * @throws TUIBusinessException
     *             the tUI business exception
     * @throws FlightSoldOutException
     *             the flight sold out exception
     */
    List<AlertViewData> navigate(String inventorySystem)
            throws TUIBusinessException, FlightSoldOutException;

    /**
     * Removes the payment request.
     */
    void removePaymentRequest();

    /**
     * Handle book service exception.
     *
     * @param e
     *            the e
     * @throws FlightSoldOutException
     *             the flight sold out exception
     * @throws TUIBusinessException
     *             the tUI business exception
     */
    void handleBookServiceException(BookServiceException e)
            throws FlightSoldOutException, TUIBusinessException;

}
