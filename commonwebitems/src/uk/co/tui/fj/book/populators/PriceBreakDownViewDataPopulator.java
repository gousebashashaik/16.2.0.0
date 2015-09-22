/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.PaymentReference;
import uk.co.tui.book.domain.lite.PromotionalDiscount;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.fj.book.view.data.PackageViewData;
import uk.co.tui.fj.book.view.data.PriceBreakDownViewData;

/**
 * The Class PriceBreakDownViewDataPopulator.
 *
 * @author samantha.gd
 */
public class PriceBreakDownViewDataPopulator implements Populator<BasePackage, PackageViewData>
{

   /** The price calculation service. */
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   @Resource
   private CurrencyResolver currencyResolver;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final BasePackage source, final PackageViewData target)
      throws ConversionException
   {
      populate(source, target.getPriceBreakdownViewData());
   }

   /**
    * Populate.
    *
    * @param source the source
    * @param target the target
    * @throws ConversionException the conversion exception
    */
   public void populate(final BasePackage source, final List<PriceBreakDownViewData> target)
      throws ConversionException
   {

      final PriceBreakDownViewData basicPrice = new PriceBreakDownViewData();
      final PriceBreakDownViewData extrasPrice = new PriceBreakDownViewData();
      final PriceBreakDownViewData onlineDiscount = new PriceBreakDownViewData();
      final PriceBreakDownViewData totalPrice = new PriceBreakDownViewData();

      BigDecimal viewDataBasicPrice = priceCalculationService.calculateBasicCost(source);

      BigDecimal viewDataOnlineDiscountPrice =
         source.getDiscount().getPrice().getAmount().getAmount();
      if (viewDataOnlineDiscountPrice.signum() == -1)
      {
         viewDataOnlineDiscountPrice = viewDataOnlineDiscountPrice.negate();
      }
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      basicPrice.setDescription(getDescription("BASICHOLIDAY"));
      if (!PackageUtilityService.isMulticomThirdPartyFlight(packageComponentService
         .getFlightItinerary(source)) && viewDataOnlineDiscountPrice.compareTo(BigDecimal.ZERO) > 0)
      {
         viewDataBasicPrice =
            viewDataBasicPrice.add(viewDataOnlineDiscountPrice).setScale(TWO, RoundingMode.HALF_UP);
         onlineDiscount.setDescription(getDescription("ONLINEDISCOUNT"));

         onlineDiscount.setPrice(viewDataOnlineDiscountPrice.setScale(TWO, RoundingMode.HALF_UP));
         onlineDiscount.setCurrencyAppendedPrice("-" + " "
            + CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
            + (viewDataOnlineDiscountPrice.setScale(TWO, RoundingMode.HALF_UP)).toString());
         target.add(onlineDiscount);
      }
      basicPrice.setPrice(viewDataBasicPrice.setScale(TWO, RoundingMode.HALF_UP));
      basicPrice.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + viewDataBasicPrice.setScale(TWO, RoundingMode.HALF_UP).toString());
      target.add(basicPrice);
      extrasPrice.setDescription(getDescription("OPTION&EXTRAS"));
      extrasPrice.setPrice(priceCalculationService.calculateExtraFacilitiesTotalCost(source));
      extrasPrice.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + extrasPrice.getPrice().setScale(TWO, RoundingMode.HALF_UP));

      totalPrice.setDescription(getDescription("TOTALPRICE"));
      totalPrice.setPrice(source.getPrice().getAmount().getAmount()
         .setScale(TWO, RoundingMode.HALF_UP));
      totalPrice.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + (source.getPrice().getAmount().getAmount().setScale(TWO, RoundingMode.HALF_UP))
            .toString());
      target.add(extrasPrice);

      populateCardChargesIfApplicable(source.getBookingDetails(), target);
      populatePromotionalChargesIfApplicable(source.getPromotionalDiscount(), target);
      target.add(totalPrice);
   }

   /**
    * Populate promotional charges if applicable.
    *
    * @param promotionalDiscount the promotional discount
    * @param target the target
    */
   private void populatePromotionalChargesIfApplicable(
      final PromotionalDiscount promotionalDiscount, final List<PriceBreakDownViewData> target)
   {
      if (SyntacticSugar.isNotNull(promotionalDiscount))
      {
         final PriceBreakDownViewData promotionalDiscountPrice = new PriceBreakDownViewData();
         final BigDecimal promotionalDis = promotionalDiscount.getPrice().getAmount().getAmount();
         promotionalDiscountPrice.setDescription(getDescription("PROMOTIONAL_DISCOUNT"));
         promotionalDiscountPrice.setPrice(promotionalDis);
         promotionalDiscountPrice.setCurrencyAppendedPrice("-" + " "
            + CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency())
            + promotionalDis.multiply(BigDecimal.valueOf(-1)).setScale(TWO, RoundingMode.HALF_UP));
         target.add(promotionalDiscountPrice);
      }
   }

   /**
    * Populate card charges if applicable.
    *
    * @param bookingDetails the booking details
    * @param target the target
    */
   private void populateCardChargesIfApplicable(final BookingDetails bookingDetails,
      final List<PriceBreakDownViewData> target)
   {
      if (SyntacticSugar.isNotNull(bookingDetails)
         && CollectionUtils.isNotEmpty(bookingDetails.getBookingHistory().get(0)
            .getPaymentReferences()))
      {
         final PriceBreakDownViewData cardCharges = new PriceBreakDownViewData();
         final BigDecimal cardCharge =
            getTotalCardCharges(bookingDetails.getBookingHistory().get(0).getPaymentReferences());
         cardCharges.setDescription(getDescription("CARD_CHARGES"));
         cardCharges.setPrice(cardCharge);
         cardCharges.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
            .getSiteCurrency()) + cardCharge.setScale(0, RoundingMode.HALF_UP));
         target.add(cardCharges);
      }
   }

   /**
    * Gets the total card charges.
    *
    * @param paymentReferences the payment references
    * @return totalCardCharge
    */
   private BigDecimal getTotalCardCharges(final List<PaymentReference> paymentReferences)
   {
      BigDecimal totalCardCharge = BigDecimal.ZERO;
      for (final PaymentReference eachPaymentReference : paymentReferences)
      {
         totalCardCharge = totalCardCharge.add(eachPaymentReference.getCardCharges().getAmount());

      }
      return totalCardCharge;
   }

   /**
    * Gets the description from content.
    *
    * @param key the key
    * @param value the value
    * @return the description
    */
   private String getDescription(final String key, final String... value)
   {
      final Map<String, String> summaryContentMap = staticContentServ.getSummaryContents();

      if (StringUtils.isNotEmpty(key))
      {
         final String summaryContent = summaryContentMap.get(key);
         if (SyntacticSugar.isNotNull(summaryContent))
         {
            if (SyntacticSugar.isEmpty(value))
            {
               return summaryContent;
            }
            return StringUtil.substitute(summaryContent, value);
         }
      }
      return StringUtils.EMPTY;
   }

}
