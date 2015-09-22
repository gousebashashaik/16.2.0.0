/**
 *
 */
package uk.co.tui.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.ExtraFacilityUpdator;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.PromotionalDiscount;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.facade.ValidatePromoCodeFacade;
import uk.co.tui.book.populators.PackageViewDataPopulator;
import uk.co.tui.book.services.PromotionalCodeValidationService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.book.validator.PromoCodeValidator;
import uk.co.tui.book.view.data.PassengerDetailsStaticContentViewData;
import uk.co.tui.book.view.data.PassengerDetailsViewData;
import uk.co.tui.book.view.data.PromotionalCodeViewData;

/**
 * @author thyagaraju.e
 *
 */
public class ValidatePromoCodeFacadeImpl implements ValidatePromoCodeFacade
{

   /** Logger for ValidatePromoCodeFacadeImpl class **/
   // private static final Logger LOG = Logger

   private static final TUILogUtils LOG = new TUILogUtils("ValidatePromoCodeFacadeImpl");

   /** The promotional code validation service. */
   @Resource
   private PromotionalCodeValidationService promotionalCodeValidationService;

   @Resource
   private PackageViewDataPopulator packageViewDataPopulator;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The passenger static content view data populator. */
   @Resource
   private Populator<Object, PassengerDetailsStaticContentViewData> passengerStaticContentViewDataPopulator;

   /** The extra facility updator. */
   @Resource
   private ExtraFacilityUpdator extraFacilityUpdator;

   /** The message property reader. */
   @Resource
   private PropertyReader messagePropertyReader;

   /** The promo code validator locator. */
   @Resource
   private ServiceLocator<PromoCodeValidator> promoCodeValidatorLocator;

   /** The Constant PROMOCODE_LENGTH. */
   public static final String PROMOCODE_LENGTH = "format.%s.promocode.length";

   /** The Constant MIN_TRACS_PROMOCODE_LENGTH. */
   private static final int MIN_PROMOCODE_LENGTH = 1;

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.book.facade.ValidatePromoCodeFacade#validate(java.lang.String)
    */
   @Override
   public PassengerDetailsViewData validate(final String promoCode)
   {
      final PassengerDetailsViewData passengerDetailsViewData = new PassengerDetailsViewData();
      final PromotionalCodeViewData promotionalCodeViewData =
         passengerDetailsViewData.getPromotionalCodeViewData();

      promotionalCodeViewData.setPromotionalCode(promoCode);

      final BasePackage packageModel = packageCartService.getBasePackage();

      final String formattedPromoCode = formatPromotionalCode(promoCode);
      String validPromoCode = StringUtils.EMPTY;
      if (isPromoCodeValid(formattedPromoCode))
      {
         validPromoCode = formattedPromoCode;
      }

      try
      {
         final PromotionalDiscount promotionalDiscountModel =
            promotionalCodeValidationService.validatePromotionalCode(validPromoCode);

         promotionalCodeViewData.setApplicable(true);
         promotionalCodeViewData.setPromotionalDiscount(promotionalDiscountModel.getPrice()
            .getAmount().getAmount().multiply(BigDecimal.valueOf(-1))
            .setScale(TWO, RoundingMode.HALF_UP));
         promotionalCodeViewData.setTotalPrice(packageModel.getPrice().getAmount().getAmount()
            .setScale(TWO, RoundingMode.HALF_UP));

      }
      catch (final BookServiceException e)
      {

         promotionalCodeViewData.setApplicable(false);
         String messageToDisplay =
            messagePropertyReader.getValue(e.getErrorCode().replaceAll("[^0-9]", ""));
         if (StringUtil.emptyOrBlank(messageToDisplay))
         {
            messageToDisplay = messagePropertyReader.getValue("promotional_code_default_error");
         }
         promotionalCodeViewData.setPromotionalCodeFailueMessage(messageToDisplay);
         promotionalCodeViewData.setPromotionalDiscount(BigDecimal.ZERO);
         promotionalCodeViewData.setTotalPrice(BigDecimal.ZERO);

         LOG.error("TUISystemException : ", e);
      }

      packageViewDataPopulator
         .populate(packageModel, passengerDetailsViewData.getPackageViewData());

      // This will update the extras in the summary panel as part of AJAX
      // response
      extraFacilityUpdator.updatePackageViewData(packageModel,
         passengerDetailsViewData.getPackageViewData());
      populatePassengerStaticContentViewData(passengerDetailsViewData);

      return passengerDetailsViewData;
   }

   /**
    * Checks if is promo code valid.
    *
    * @param formattedPromoCode the formatted promo code
    * @return true, if is promo code valid
    */
   private boolean isPromoCodeValid(final String formattedPromoCode)
   {
      return promoCodeValidatorLocator.locateByInventory(
         packageCartService.getBasePackage().getInventory().getInventoryType().toString())
         .validate(formattedPromoCode);
   }

   /**
    * Format promotional code.
    *
    * @return String
    */
   private String formatPromotionalCode(final String promoCode)
   {
      return StringUtils.leftPad(
         StringUtils.strip(promoCode),
         Config.getInt(
            String.format(PROMOCODE_LENGTH, packageCartService.getBasePackage().getInventory()
               .getInventoryType().toString().toLowerCase()), MIN_PROMOCODE_LENGTH), '0');

   }

   /**
    * Populate passenger static content view data.
    *
    * @param viewData the view data
    */
   private void populatePassengerStaticContentViewData(final PassengerDetailsViewData viewData)
   {
      final PassengerDetailsStaticContentViewData passengerDetailsStaticContentViewData =
         new PassengerDetailsStaticContentViewData();
      passengerStaticContentViewDataPopulator.populate(new Object(),
         passengerDetailsStaticContentViewData);
      viewData.setPassengerDetailsStaticContentViewData(passengerDetailsStaticContentViewData);
   }

}
