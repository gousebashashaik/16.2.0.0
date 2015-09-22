/**
 *
 */
package uk.co.tui.book.facade;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.exception.TUIBusinessException;
import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.book.view.data.AlertViewData;
import uk.co.tui.book.view.data.BookingDetailsViewData;

/**
 * The Interface BookFacade.
 *
 * @author extps3
 */
public interface BookFacade
{

   /**
    * Check price and availability.
    *
    * @return alertViewDataList
    * @throws TUIBusinessException the tUI business exception
    */
   List<AlertViewData> checkPriceAndAvailability() throws TUIBusinessException,
      FlightSoldOutException;

   /**
    * Infant Not yet born.
    *
    * @return alertViewDataList
    */
   AlertViewData checkInfantNotYetBornCase();

   /**
    * Gets the payment page relative url.
    *
    * @param urlMap the url map
    * @param hostName the host name
    * @return Payment PAge Relative Url.
    * @throws TUIBusinessException
    */
   String getPaymentPageRelativeUrl(Map<String, String> urlMap, String hostName, String contextPath)
      throws FlightSoldOutException, TUIBusinessException;

   /**
    * Confirm booking.
    *
    * @return BookingDetailsViewData
    */
   BookingDetailsViewData confirmBooking();

   /**
    * Flush session objects.
    */
   void flushSessionObjects();

   /**
    * @return
    */
   boolean isPreAuthSuccess();
}