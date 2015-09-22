/**
 *
 */
package uk.co.tui.cr.book.facade;

import uk.co.tui.cr.book.view.data.ConfirmationViewData;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.SummaryPanelViewData;


/**
 * The Interface ConfirmationFacade.
 *
 * @author uday.g
 */
public interface ConfirmationFacade {

    /**
     * This method sets the confirmation page URL.
     *
     * @param currentPage
     *            the current page
     * @return SummaryPanelViewData
     */
    SummaryPanelViewData setConfirmationURL(String currentPage);

    /**
     * Render package view data.
     *
     * @return the package view data
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
