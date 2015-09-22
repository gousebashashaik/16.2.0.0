/**
 *
 */
package uk.co.tui.book.facade;

import uk.co.tui.book.view.data.ConfirmationViewData;
import uk.co.tui.book.view.data.PackageViewData;

/**
 * The Interface ConfirmationFacade.
 *
 * @author thyagaraju.e
 */
public interface ConfirmationFacade {

    /**
     * Render package view data.
     *
     */
    PackageViewData renderPackageViewData();

    /**
     * Populate confirmation static content view data.
     *
     * @param viewData
     *            the view data
     */
    void populateConfirmationStaticContentViewData(ConfirmationViewData viewData);

    /**
     * Populate confirmation content view data.
     *
     * @param viewData
     *            the view data
     */
    void populateConfirmationContentViewData(ConfirmationViewData viewData);

}
