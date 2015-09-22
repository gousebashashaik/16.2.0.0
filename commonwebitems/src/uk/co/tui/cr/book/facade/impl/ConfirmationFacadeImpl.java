/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.cr.book.ExtraFacilityUpdator;
import uk.co.tui.cr.book.facade.ConfirmationFacade;
import uk.co.tui.cr.book.view.data.ConfirmationContentViewData;
import uk.co.tui.cr.book.view.data.ConfirmationStaticContentViewData;
import uk.co.tui.cr.book.view.data.ConfirmationViewData;
import uk.co.tui.cr.book.view.data.ContentViewData;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.SummaryPanelViewData;

/**
 * The Class ConfirmationFacadeImpl represents the population of BookingDetails ViewData for the
 * Confirmation page.
 *
 * @author uday.g
 */
public class ConfirmationFacadeImpl implements ConfirmationFacade
{

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** Package ViewData Populator Service Locator. */
   @Resource
   private ServiceLocator<Populator> packageViewDataPopulatorServiceLocator;

   /** The extra facility updator. */
   @Resource(name = "crExtraFacilityUpdator")
   private ExtraFacilityUpdator extraFacilityUpdator;

   /** The static content serv. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The confirmation content view data populator. */
   @Resource(name = "crConfirmationContentViewDataPopulator")
   private Populator<Object, ContentViewData> confirmationContentViewDataPopulator;

   /**
    * Render package view data.
    *
    * @return the package view data
    */
   @Override
   public PackageViewData renderPackageViewData()
   {
      final PackageViewData packageViewData = new PackageViewData();
      final BasePackage packageModel = getPackageFromCart();
      (packageViewDataPopulatorServiceLocator.locateByPackageType(packageModel.getPackageType()
         .toString())).populate(packageModel, packageViewData);
      extraFacilityUpdator.updatePackageViewData(packageModel, packageViewData);
      return packageViewData;
   }

   /**
    * Populate confirmation static content view data.
    *
    * @param viewData the view data
    */
   @Override
   public void populateConfirmationStaticContentViewData(final ConfirmationViewData viewData)
   {
      final ConfirmationStaticContentViewData confirmationStaticContentViewData =
         new ConfirmationStaticContentViewData();
      confirmationStaticContentViewData.setConfirmationContentMap(staticContentServ
         .getConfirmationContents());

      viewData.setConfirmationStaticContentViewData(confirmationStaticContentViewData);
   }

   /**
    * Populates the Confirmation Content View data.
    *
    * @param viewData the view data
    */
   @Override
   public void populateConfirmationContentViewData(final ConfirmationViewData viewData)
   {
      final ConfirmationContentViewData confirmationContentViewData =
         new ConfirmationContentViewData();
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
    * @see uk.co.tui.th.book.facade.ConfirmationFacade#setConfirmationURL(java.lang .String)
    */
   @Override
   public SummaryPanelViewData setConfirmationURL(final String currentPage)
   {

      return null;
   }

}
