/**
 *
 */
package uk.co.tui.fj.book.facade;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.fj.book.view.data.AlertViewData;
import uk.co.tui.fj.book.view.data.BookingDetailsViewData;
import uk.co.tui.fj.exception.TUIBusinessException;

/**
 * The Interface BookFacade.
 *
 * @author extps3
 */
public interface BookFacade
{

   /**
    * This method populates BasePackage from Holiday object.
    *
    * @param holiday the holiday
    * @return BasePackage
    */
   BasePackage populatePackage(Holiday holiday);

   /**
    * Check price and availability.
    *
    * @return alertViewDataList
    * @throws TUIBusinessException
    *
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
    * @param urlMap
    * @param hostName
    * @param contextPath
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
    * @return
    */
   boolean isPreAuthSuccess();
}