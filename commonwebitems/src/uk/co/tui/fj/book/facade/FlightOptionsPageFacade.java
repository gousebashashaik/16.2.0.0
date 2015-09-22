/**
 *
 */
package uk.co.tui.fj.book.facade;


import uk.co.tui.book.criteria.FlightFilterCriteria;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.exception.FlightOptionsPageException;
import uk.co.tui.fj.book.view.data.FlightOptionsViewData;
import uk.co.tui.fj.exception.TUIBusinessException;


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
     * @param viewData the view data
     * @throws TUIBusinessException the tUI business exception
     */
    void renderFlightOptions(FlightOptionsViewData viewData)
            throws TUIBusinessException;

    /**
     * Gets the availability calendar.
     *
     * @param filterCriteria
     *            the filter criteria
     * @return the availability calendar
     * @throws FlightOptionsPageException
     *             the flight options page exception
     */
    FlightOptionsViewData renderAvailabilityCalendar(FlightFilterCriteria filterCriteria) throws FlightOptionsPageException;

    /**
     * Populate package view.
     *
     * @param packageModel
     *            the package model
     * @param viewData
     *            the view data
     */
    void populatePackageView(BasePackage packageModel, FlightOptionsViewData viewData);

    /**
     * Populate flight options static content view data.
     *
     * @param viewData
     *            the view data
     */
    void populateFlightOptionsStaticContentViewData(FlightOptionsViewData viewData);

    /**
     * Update package into cart.
     */
    void updatePackageIntoCart();

    /**
     * The method to render the multicom thirdParty flights.
     *
     * @return viewData
     */
    FlightOptionsViewData renderFlightOptionsForMulticomThirdPartyFlight();

    /**
     * Populate avl quantity for standard seats.
     *
     * @param packageModel
     *            the package model
     */
    void populateAvlQuantityForStandardSeats(BasePackage packageModel);

}
