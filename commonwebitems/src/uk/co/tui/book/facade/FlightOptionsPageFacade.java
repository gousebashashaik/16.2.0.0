/**
 *
 */
package uk.co.tui.book.facade;

import java.util.Set;

import uk.co.portaltech.tui.exception.TUIBusinessException;
import uk.co.tui.book.criteria.FlightFilterCriteria;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.exception.FlightOptionsPageException;
import uk.co.tui.book.view.data.FlightOptionsViewData;

/**
 * The Interface FlightOptionsPageFacade.
 *
 * @author sunilkumar.sahu
 */
public interface FlightOptionsPageFacade
{

   /**
    * Render flight options.
    *
    * @return the flight options view data
    * @throws TUIBusinessException the tUI business exception
    */
   FlightOptionsViewData renderFlightOptions() throws TUIBusinessException;

   /**
    * Gets the availability calendar.
    *
    * @param filterCriteria the filter criteria
    * @return the availability calendar
    * @throws FlightOptionsPageException
    */
   FlightOptionsViewData renderAvailabilityCalendar(FlightFilterCriteria filterCriteria)
      throws FlightOptionsPageException;

   /**
    * @param packageModel
    * @param viewData
    */
   void populatePackageView(BasePackage packageModel, FlightOptionsViewData viewData);

   /**
    * Populate flight options static content view data.
    *
    * @param viewData the view data
    */
   void populateFlightOptionsStaticContentViewData(FlightOptionsViewData viewData);

   /**
    * Update package into cart.
    */
   void updatePackageIntoCart();

   /**
    * The method to render the multicom thirdParty flights viewData.
    *
    * @return viewData
    */
   FlightOptionsViewData renderFlightOptionsForMulticomThirdPartyFlight();

   /**
    * Populate avl quantity for standard seats.
    *
    * @param packageModel the package model
    */
   void populateAvlQuantityForStandardSeats(BasePackage packageModel);

   /**
    * @param viewData
    * @param carrierCodes
    */
   void populateThirdPartyFlightStaticContentViewData(FlightOptionsViewData viewData,
      Set<String> carrierCodes);

   /**
    * @param viewData
    */
   void populateThirdPartyFlightInfo(FlightOptionsViewData viewData);

}
