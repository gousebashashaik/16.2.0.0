/**
 *
 */
package uk.co.tui.fj.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.fj.book.ExtraFacilityUpdator;
import uk.co.tui.fj.book.facade.ConfirmationFacade;
import uk.co.tui.fj.book.populators.PackageViewDataPopulator;
import uk.co.tui.fj.book.view.data.ConfirmationContentViewData;
import uk.co.tui.fj.book.view.data.ConfirmationStaticContentViewData;
import uk.co.tui.fj.book.view.data.ConfirmationViewData;
import uk.co.tui.fj.book.view.data.ContentViewData;
import uk.co.tui.fj.book.view.data.PackageViewData;
import uk.co.tui.fj.book.view.data.SummaryPanelViewData;


/**
 * The Class ConfirmationFacadeImpl represents the population of BookingDetails ViewData for the Confirmation page.
 *
 * @author thyagaraju.e
 */
public class ConfirmationFacadeImpl implements ConfirmationFacade
{

    @Resource
    private PackageCartService packageCartService;

    /** The package view data populator. */
    @Resource(name = "fjPackageViewDataPopulator")
    private PackageViewDataPopulator packageViewDataPopulator;

    @Resource(name = "fjExtraFacilityUpdator")
    private ExtraFacilityUpdator extraFacilityUpdator;

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /** The confirmation content view data populator. */
    @Resource(name = "fjConfirmationContentViewDataPopulator")
    private Populator<Object, ContentViewData> confirmationContentViewDataPopulator;

    /**
     * Render package view data.
     *
     * @return the package view data
     */
    // TODO : Need to move this to the common populator once we have
    // populateBookingDetailsViewData integrated with controller.
    @Override
    public PackageViewData renderPackageViewData()
    {
        final PackageViewData packageViewData = new PackageViewData();
        final BasePackage packageModel = getPackageFromCart();
        packageViewDataPopulator.populate(packageModel, packageViewData);
        extraFacilityUpdator.updatePackageViewData(packageModel, packageViewData);
        return packageViewData;
    }

    /**
     * Populate confirmation static content view data.
     *
     * @param viewData
     *           the view data
     */
    @Override
    public void populateConfirmationStaticContentViewData(final ConfirmationViewData viewData)
    {
        final ConfirmationStaticContentViewData confirmationStaticContentViewData = new ConfirmationStaticContentViewData();
        confirmationStaticContentViewData.setConfirmationContentMap(staticContentServ.getConfirmationContents());

        viewData.setConfirmationStaticContentViewData(confirmationStaticContentViewData);
    }

    /**
     * Populates the Confirmation Content View data.
     */
    @Override
    public void populateConfirmationContentViewData(final ConfirmationViewData viewData)
    {
        final ConfirmationContentViewData confirmationContentViewData = new ConfirmationContentViewData();
        final ContentViewData contentViewData = new ContentViewData();
        confirmationContentViewDataPopulator.populate(new Object(), contentViewData);
        confirmationContentViewData.setConfirmationViewData(contentViewData);
        viewData.setConfirmationContentViewData(confirmationContentViewData);
    }

    /**
     * gets the package model from cart.
     *
     * @return the package from cart
     */
    private BasePackage getPackageFromCart()
    {
        return packageCartService.getBasePackage();
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.tui.th.book.facade.ConfirmationFacade#setConfirmationURL(java.lang.String)
     */
    @Override
    public SummaryPanelViewData setConfirmationURL(final String currentPage)
    {

        return null;
    }

}
