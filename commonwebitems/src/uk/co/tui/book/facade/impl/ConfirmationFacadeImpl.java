/**
 *
 */
package uk.co.tui.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.tui.book.ExtraFacilityUpdator;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.facade.ConfirmationFacade;
import uk.co.tui.book.populators.PackageViewDataPopulator;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.view.data.ConfirmationContentViewData;
import uk.co.tui.book.view.data.ConfirmationStaticContentViewData;
import uk.co.tui.book.view.data.ConfirmationViewData;
import uk.co.tui.book.view.data.ContentViewData;
import uk.co.tui.book.view.data.PackageViewData;

/**
 * The Class ConfirmationFacadeImpl represents the population of BookingDetails
 * ViewData for the Confirmation page.
 *
 * @author thyagaraju.e
 */
public class ConfirmationFacadeImpl implements ConfirmationFacade {

    @Resource
    private PackageCartService packageCartService;

    @Resource
    private PackageViewDataPopulator packageViewDataPopulator;

    @Resource
    private ExtraFacilityUpdator extraFacilityUpdator;

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /** The confirmation content view data populator. */
    @Resource
    private Populator<Object, ContentViewData> confirmationContentViewDataPopulator;

    /**
     * Render package view data.
     *
     * @return the package view data
     */
    // TODO : Need to move this to the common populator once we have
    // populateBookingDetailsViewData integrated with controller.
    @Override
    public PackageViewData renderPackageViewData() {
        final PackageViewData packageViewData = new PackageViewData();
        final BasePackage packageModel = packageCartService.getBasePackage();
        packageViewDataPopulator.populate(packageModel, packageViewData);
        extraFacilityUpdator.updatePackageViewData(packageModel,
                packageViewData);
        return packageViewData;
    }

    /**
     * Populate confirmation static content view data.
     *
     * @param viewData
     *            the view data
     */

    @Override
    public void populateConfirmationStaticContentViewData(
            final ConfirmationViewData viewData) {
        final ConfirmationStaticContentViewData confirmationStaticContentViewData = new ConfirmationStaticContentViewData();
        confirmationStaticContentViewData
                .setConfirmationContentMap(staticContentServ
                        .getConfirmationContents());
        viewData.setConfirmationStaticContentViewData(confirmationStaticContentViewData);
    }

    /**
     * Populates the Confirmation Content View data.
     */
    @Override
    public void populateConfirmationContentViewData(
            final ConfirmationViewData viewData) {
        final ConfirmationContentViewData confirmationContentViewData = new ConfirmationContentViewData();
        final ContentViewData contentViewData = new ContentViewData();
        confirmationContentViewDataPopulator.populate(new Object(),
                contentViewData);
        confirmationContentViewData.setConfirmationViewData(contentViewData);
        viewData.setConfirmationContentViewData(confirmationContentViewData);
    }

}
