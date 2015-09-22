/**
 *
 */
package uk.co.tui.fo.book.facade;


import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.fo.book.view.data.FlightOptionsViewData;
import uk.co.tui.fo.exception.TUIBusinessException;


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
     * @throws TUIBusinessException
     *            the tUI business exception
     */
    FlightOptionsViewData renderFlightOptions() throws TUIBusinessException;

    /**
     * Populate package view.
     *
     * @param packageModel
     *           the package model
     * @param viewData
     *           the view data
     */
    void populatePackageView(BasePackage packageModel, FlightOptionsViewData viewData);

    /**
     * Populate flight options static content view data.
     *
     * @param viewData
     *           the view data
     */
    void populateFlightOptionsStaticContentViewData(FlightOptionsViewData viewData);

    /**
     * Update package into cart.
     */
    void updatePackageIntoCart();

}
