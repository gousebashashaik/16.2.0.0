/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.travel.domain.manage.response.UpdatePaymentResponse;
import uk.co.tui.book.anite.request.CardPayment;
import uk.co.tui.book.anite.request.PaymentTypes;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.manage.viewdata.PaymentConfirmationViewData;

/**
 * @author veena.pn
 *
 */
public class PaymentConfirmationViewDataPopulator implements
   Populator<UpdatePaymentResponse, PaymentConfirmationViewData>
{

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    *
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final UpdatePaymentResponse source, final PaymentConfirmationViewData target)
      throws ConversionException
   {
      final BasePackage packageModel = packageCartService.getBasePackage();

      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      BigDecimal feeAmt = BigDecimal.ZERO;
      BigDecimal amt = BigDecimal.ZERO;
      for (final uk.co.tui.book.anite.request.PaymentType payment : target.getPaymentInfo()
         .getPaymentType())
      {
         if(PaymentTypes.CCPAY.equals(payment.getPaymentType())) {
            feeAmt = ((CardPayment) payment).getCardCharges();
            target.setCardType(((CardPayment) payment).getCardInformation().getCardType());
         }
      }
      target.setCreditCardCharges(feeAmt);

      final SimpleDateFormat formatter = new SimpleDateFormat("E dd MMM yyyy");

      if (source.getBookingReferenceNumber() != null)
      {
         target.setBookingRef(packageModel.getBookingRefNum());
      }

      if (source.getAmtPaid() != null)
      {
         target.setTransactionAmount(source.getAmtPaid().getAmount().add(feeAmt));
      }

      final PackageHoliday packageholiday = (PackageHoliday) packageModel;
      target.setDepDate(packageholiday.getItinerary().getInBound().get(0).getSchedule()
         .getDepartureDate());
      if (packageholiday.getInventory() != null)
      {
         target.setInventoryType(packageholiday.getInventory().getInventoryType().name());
      }

      target.setRoundUpTotalCost(packageModel.getPrice().getAmount().getAmount());
      target.setCurrencyAppendedRoundUpTotalCost(CurrencyUtils
         .getCurrencySymbol(defaultCurrencyCode)
         + packageModel.getPrice().getAmount().getAmount().setScale(TWO, RoundingMode.HALF_UP));
      target.setTotalPriceExclusive(packageModel.getPrice().getAmount().getAmount()
         .subtract(feeAmt));
      target.setCurrencyAppendedTotalPriceExclusive(CurrencyUtils
         .getCurrencySymbol(defaultCurrencyCode)
         + packageModel.getPrice().getAmount().getAmount().subtract(feeAmt)
            .setScale(TWO, RoundingMode.HALF_UP));

      if (source.getAmtPaid() != null)
      {
         target.setCurrencyAppendedAmtPaid(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
            + source.getAmtPaid().getAmount().add(feeAmt).setScale(TWO, RoundingMode.HALF_UP));
         amt =
            packageModel.getBookingDetails().getAmountDue().getAmount()
               .subtract(source.getAmtPaid().getAmount());
      }
      if (amt != null && BigDecimal.ZERO.compareTo(amt) < 0)
      {
         target.setDueAmount(amt);
         target.setCurrencyAppendedDueAmount(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
            + amt.setScale(TWO, RoundingMode.HALF_UP));
         target.setDueDate(formatter.format(packageModel.getBookingDetails().getDueDate()));
      }
      else
      {
         target.setDueAmount(amt);
         target.setCurrencyAppendedDueAmount(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
            + BigDecimal.ZERO.setScale(TWO, RoundingMode.HALF_UP));
      }

   }

}
