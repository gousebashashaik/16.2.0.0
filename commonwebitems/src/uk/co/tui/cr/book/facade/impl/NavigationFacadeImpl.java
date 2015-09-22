/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.domain.factory.PackageFactory;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.book.exception.PackageNotInCartException;
import uk.co.tui.book.page.enums.PageType;
import uk.co.tui.book.page.request.PageRequest;
import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.book.payment.request.PaymentRequest;
import uk.co.tui.book.resolver.PackageTypeResolver;
import uk.co.tui.book.services.NavigationPageResolver;
import uk.co.tui.book.services.PackageRefinementService;
import uk.co.tui.book.services.PromotionalCodeValidationService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.UpdateDonationExtraFacilityService;
import uk.co.tui.book.services.inventory.CheckPriceAvailabilityService;
import uk.co.tui.book.services.payment.PackagePaymentService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.cr.book.constants.SessionObjectKeys;
import uk.co.tui.cr.book.facade.NavigationFacade;
import uk.co.tui.cr.book.populators.AlertPopulator;
import uk.co.tui.cr.book.view.data.AlertViewData;
import uk.co.tui.cr.exception.TUIBusinessException;
import uk.co.tui.cr.exception.TUISystemException;

/**
 * The Class NavigationFacadeImpl.
 *
 * @author samantha.gd
 */
public class NavigationFacadeImpl implements NavigationFacade
{

   /** Logger for NavigationFacadeImpl class *. */
   private static final TUILogUtils LOG = new TUILogUtils("NavigationFacadeImpl");

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The model service. */
   @Resource
   private ModelService packageModelService;

   /** The package refinement service. */
   @Resource
   private PackageRefinementService packageRefinementService;

   /** The service locator. */
   @Resource(name = "infobookServiceLocator")
   private ServiceLocator<CheckPriceAvailabilityService> infobookServiceLocator;

   /** The update donation extra locator. */
   @Resource(name = "atcomUpdateDonationExFacilityService")
   private UpdateDonationExtraFacilityService updateDonationExFacilityService;

   /** The promotional code validation service. */
   @Resource
   private PromotionalCodeValidationService promotionalCodeValidationService;

   /** AlertViewData Populator . */
   @Resource(name = "crAlertPopulator")
   private AlertPopulator alertPopulator;

   /** The Payment Service . */
   @Resource
   private PackagePaymentService packagePaymentService;

   /** Custom Sold Out Error Code. */

   public static final String CUSTOM_SOLDOUTERRORCODE = "99999";

   /** The tui utility service. */
   @Resource(name = "tuiUtilityService")
   private TuiUtilityService tuiUtilityService;

   /** The package type resolver. */
   @Resource
   private PackageTypeResolver packageTypeResolver;

   /** The populator service locator. */
   @Resource
   private ServiceLocator<Populator<Object, BasePackage>> populatorServiceLocator;

   /** The navigation page resolver. */
   @Resource
   private NavigationPageResolver navigationPageResolver;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.book.facade.NavigationFacade#processHoliday(java.lang.String)
    */
   @Override
   public void processHoliday() throws TUIBusinessException, FlightSoldOutException
   {
      if (sessionService.getAttribute(SessionObjectKeys.SELECTEDHOLIDAY) != null)
      {
         clearCart();
         addSessionPackageToCart();
         removeCachedHoliday();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.book.facade.NavigationFacade#navigateNew(java.lang.String)
    */
   @Override
   public List<AlertViewData> navigate(final String inventorySystem) throws TUIBusinessException,
      FlightSoldOutException
   {
      checkCartExistence();
      removePromotionalCode();
      removePaymentRequest();
      List<Feedback> feedback = Collections.emptyList();
      try
      {
         feedback = performDefaultAvailabilityCheck(inventorySystem);
      }
      catch (final BookServiceException e)
      {
         handleBookServiceException(e);
      }
      return alertPopulator.populateTotalCostAlert(new ArrayList<AlertViewData>(), feedback);
   }

   /**
    * Check cart existence.
    */
   private void checkCartExistence()
   {
      if (!packageCartService.cartExist())
      {
         throw new PackageNotInCartException("140003", "Package Not in Cart!");
      }
   }

   /**
    * Removes the promotional code.
    */
   private void removePromotionalCode()
   {
      promotionalCodeValidationService.removePromotionalCode(packageCartService.getBasePackage());
   }

   /**
    * When the user navigates from paymentpage back to any other bookflow pages, the generated
    * payment token is perged and the payment request will be removed from the session.
    *
    */
   @Override
   public void removePaymentRequest()
   {
      if (sessionService.getAttribute(SessionObjectKeys.PAYMENTREQUEST) != null)
      {
         try
         {
            packagePaymentService.purgePaymentToken((PaymentRequest) sessionService
               .getAttribute(SessionObjectKeys.PAYMENTREQUEST));
         }
         catch (final uk.co.tui.exception.TUIBusinessException e)
         {
            LOG.error("Remove Payment Request Exception", e);
         }
         sessionService.removeAttribute(SessionObjectKeys.PAYMENTREQUEST);
      }
   }

   /**
    * Checks if is soldout error.
    *
    * @param errorCode the error code
    * @param soldOutErrorCodes the sold out error codes
    * @return true if the error is of type soldout error
    */
   private boolean isSoldoutError(final String errorCode, final List<String> soldOutErrorCodes)
   {
      // Comparison logic has been moved to TibcoSoapFaultHandler
      return PackageUtilityService.isSoldOutErrorMatches(errorCode, soldOutErrorCodes);
   }

   /**
    * This method adds the session stored holiday to cart.
    */
   private void addSessionPackageToCart()
   {
      final Object holiday = sessionService.getAttribute(SessionObjectKeys.SELECTEDHOLIDAY);
      final PackageType packageType = packageTypeResolver.resolve(holiday);

      final BasePackage packageHoliday = PackageFactory.createPackage(packageType);
      packageHoliday.setPackageType(packageType);

      populatorServiceLocator.locateByPackageType(packageType.toString()).populate(holiday,
         packageHoliday);

      packageModelService.save(packageHoliday);
      packageRefinementService.addDefaultExtras(packageHoliday);
      updateDonationExFacilityService.deductCharityPriceFromRoomPrice(packageHoliday);
      packageCartService.updateCart(packageHoliday);
   }

   /**
    * Removes the cached holiday.
    */
   private void removeCachedHoliday()
   {
      tuiUtilityService.removeSessionObjects(Arrays.asList(new String[] {
         SessionObjectKeys.SELECTEDHOLIDAY, SessionObjectKeys.ALT_BOARD,
         SessionObjectKeys.ITINERARY_PAGE_URL, SessionObjectKeys.ALTERNATE_FLIGHT_STORE,
         SessionObjectKeys.CABIN_SEARCH_RESPONSE, SessionObjectKeys.ROOM_SEARCH_RESPONSE,
         SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE,
         SessionObjectKeys.PACKAGE_EXTRA_FACILITY_STORE,
         SessionObjectKeys.PASSENGER_DETAILS_FORM_BEAN,
         SessionObjectKeys.INSURANCE_CONTAINER_VIEW_DATA, "wishListMap", "SEARCHRESULTS",
         "holidayIndex", "searchResults", "filterMap", "breadCrumb",
         "NEW_BOOKFLOW_PROGRESS_INDICATOR", "holidayResult" }));
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.book.facade.NavigationFacade#buildNavigationData(java.lang. String)
    */
   @Override
   public PageResponse buildNavigationData(final Brand brand, final PackageType packageType,
      final PageType pageType)
   {
      final PageRequest pageRequest = new PageRequest();
      pageRequest.setBrand(brand);
      if (!packageCartService.getBasePackage().getListOfHighlights()
         .contains(HighLights.LAPLAND_DAYTRIP))
      {
         pageRequest.setPackageType(packageType);
      }
      pageRequest.setCurrentPage(pageType);
      return navigationPageResolver.resolveNextPage(pageRequest);
   }

   /**
    * Clear cart.
    */
   private void clearCart()
   {
      packageCartService.remove();
   }

   /**
    * Handle book service exception.
    *
    * @param e the e
    * @throws FlightSoldOutException the multicom third party exception
    * @throws TUIBusinessException the tUI business exception
    */
   @Override
   public void handleBookServiceException(final BookServiceException e)
      throws FlightSoldOutException, TUIBusinessException
   {
      final List<String> multicomErrorCodes =
         Arrays.asList(StringUtils.split(Config.getParameter("multicom_error_codes"), ','));
      if (multicomErrorCodes.contains(e.getErrorCode()))
      {
         throw new FlightSoldOutException(e.getErrorCode(), e);
      }
      LOG.error("TUISystemException : " + e.getMessage());
      final List<String> soldOutErrorCodes =
         Arrays.asList(StringUtils.split(Config.getParameter("soldOutErrorCodes"), ','));
      if (isSoldoutError(e.getErrorCode(), soldOutErrorCodes))
      {
         throw new TUIBusinessException(e.getErrorCode(), e.getCustomMessage(), e);
      }
      else
      {
         throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
      }
   }

   /**
    * Perform default info book.
    *
    * @param inventorySystem the inventory system
    * @return the list
    * @throws BookServiceException the book service exception
    */
   private List<Feedback> performDefaultAvailabilityCheck(final String inventorySystem)
      throws BookServiceException
   {
      return infobookServiceLocator.locateByInventory(inventorySystem).updatePriceAndAvailability(
         packageCartService.getBasePackage());
   }

}
