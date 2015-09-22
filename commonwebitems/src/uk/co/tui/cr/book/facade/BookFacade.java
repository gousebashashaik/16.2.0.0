/**
 *
 */
package uk.co.tui.cr.book.facade;

import java.util.List;
import java.util.Map;

import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.cr.book.view.data.AlertViewData;
import uk.co.tui.cr.book.view.data.BookingDetailsViewData;
import uk.co.tui.cr.exception.TUIBusinessException;

/**
 * @author uday.g
 *
 */
public interface BookFacade
{
   /**
    * This method populates BasePackage from Holiday object.
    *
    * @param holiday the holiday
    * @return BasePackage
    */

   /**
    * Check price and availability.
    *
    * @return alertViewDataList
    * @throws TUIBusinessException
    *
    */
   List<uk.co.tui.cr.book.view.data.AlertViewData> checkPriceAndAvailability()
      throws TUIBusinessException, FlightSoldOutException;

   /**
    * Infant Not yet born.
    *
    * @return alertViewDataList
    */
   AlertViewData checkInfantNotYetBornCase();

   /**
    * @param urlMap
    * @param hostName
    * @return Payment PAge Relative Url.
    */
   String getPaymentPageRelativeUrl(Map<String, String> urlMap, String hostName, String contextPath)
      throws FlightSoldOutException, TUIBusinessException;

   /**
    * @return BookingDetailsViewData
    */
   BookingDetailsViewData confirmBooking();

   /**
    * Flush session objects.
    */
   void flushSessionObjects();

   /**
    * Checks if is pre auth success.
    *
    * @return true, if is pre auth success
    */
   boolean isPreAuthSuccess();
}
