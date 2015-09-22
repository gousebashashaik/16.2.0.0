/**
 *
 */
package uk.co.tui.manage.populators;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.EMPTY;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.Deposit;
import uk.co.tui.book.domain.lite.PaymentReference;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.PromotionalDiscount;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.manage.services.inventory.LowDepositTopUpCalcService;
import uk.co.tui.manage.viewdata.BookingDetailsViewData;
import uk.co.tui.manage.viewdata.BookingHistoryViewData;
import uk.co.tui.manage.viewdata.DepositViewData;
import uk.co.tui.manage.viewdata.PackageViewData;
import uk.co.tui.manage.viewdata.PaymentReferenceViewData;
import uk.co.tui.manage.viewdata.PriceBreakDownViewData;
import uk.co.tui.manage.viewdata.PriceViewData;

/**
 * @author veena.pn
 *
 */

public class PackagePriceViewDataPopulator implements Populator<BasePackage, PackageViewData>
{
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private CurrencyResolver currencyResolver;

   /** The price calculation service. */
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   @Resource
   private LowDepositTopUpCalcService lowDepositTopUpCalcService;

   private static final int TWO = 2;

   @Override
   public void populate(final BasePackage source, final PackageViewData target)
      throws ConversionException
   {
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      if (source.getPrice() != null)
      {
         target.setRoundUpTotalCost(source.getPrice().getAmount().getAmount());
         target.setCurrencyAppendedRoundUpTotalCost(CurrencyUtils
            .getCurrencySymbol(defaultCurrencyCode)
            + source.getPrice().getAmount().getAmount().setScale(TWO, RoundingMode.HALF_UP));
      }
      populateDeposits(source, target);
      populateBookingDetails(source, target);
      populateAccomPrices(source, target);

      populatePerPersonPrice(source, target);

      final BigDecimal onlineDiscount = source.getDiscount().getPrice().getAmount().getAmount();
      if (onlineDiscount.compareTo(BigDecimal.ZERO) != 0)
      {
         target.setCurrencyAppendedOnlineDiscount(CurrencyUtils
            .getCurrencySymbol(defaultCurrencyCode)
            + onlineDiscount.multiply(BigDecimal.valueOf(-1)).setScale(TWO, RoundingMode.HALF_UP));
      }
      populatePriceBreakDownViewData(source, target);
   }

   /**
    * @param source
    * @param target
    */
   private void populatePriceBreakDownViewData(final BasePackage source,
      final PackageViewData target)
   {
      final List<PriceBreakDownViewData> priceBreakDownViewDatas =
         new ArrayList<PriceBreakDownViewData>();

      final PriceBreakDownViewData basicPrice = new PriceBreakDownViewData();
      final PriceBreakDownViewData extrasPrice = new PriceBreakDownViewData();
      final PriceBreakDownViewData onlineDiscount = new PriceBreakDownViewData();
      final PriceBreakDownViewData totalPrice = new PriceBreakDownViewData();
      final PriceBreakDownViewData amendmendPrice = new PriceBreakDownViewData();

      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();

      BigDecimal viewDataBasicPrice = priceCalculationService.calculateBasicCost(source);
      final List<Price> priceAdjustmentList =
         lowDepositTopUpCalcService.getViewableComponentAdj(source);
      viewDataBasicPrice = manageBasicCost(priceAdjustmentList, viewDataBasicPrice);
      BigDecimal viewDataOnlineDiscountPrice =
         source.getDiscount().getPrice().getAmount().getAmount();

      if (viewDataOnlineDiscountPrice.signum() == -1)
      {
         viewDataOnlineDiscountPrice = viewDataOnlineDiscountPrice.negate();
      }

      viewDataBasicPrice =
         viewDataBasicPrice.add(viewDataOnlineDiscountPrice).setScale(TWO, RoundingMode.HALF_UP);
      basicPrice.setDescription(getDescription("BASICHOLIDAY"));
      basicPrice.setPrice(viewDataBasicPrice.setScale(TWO, RoundingMode.HALF_UP));
      basicPrice.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + viewDataBasicPrice.setScale(TWO, RoundingMode.HALF_UP).toString());
      priceBreakDownViewDatas.add(basicPrice);

      extrasPrice.setDescription(getDescription("OPTION&EXTRAS"));
      extrasPrice.setPrice(priceCalculationService.calculateExtraFacilitiesTotalCost(source));
      extrasPrice.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + extrasPrice.getPrice().setScale(TWO, RoundingMode.HALF_UP));
      priceBreakDownViewDatas.add(extrasPrice);

      if (viewDataOnlineDiscountPrice.compareTo(BigDecimal.ZERO) != 0)
      {
         onlineDiscount.setDescription(getDescription("ONLINEDISCOUNT"));
         onlineDiscount.setPrice(viewDataOnlineDiscountPrice.setScale(TWO, RoundingMode.HALF_UP));
         onlineDiscount.setCurrencyAppendedPrice("- "
            + CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
            + viewDataOnlineDiscountPrice.setScale(TWO, RoundingMode.HALF_UP).toString());
         priceBreakDownViewDatas.add(onlineDiscount);
      }

      populatePromotionalChargesIfApplicable(source.getPromotionalDiscount(),
         priceBreakDownViewDatas);

      BigDecimal amendmentprice = BigDecimal.ZERO;
      amendmentprice = priceCalculationService.addAmendmentCharges(source);
      if (amendmentprice.compareTo(BigDecimal.ZERO) != 0)
      {
         amendmendPrice.setDescription(getDescription("AMENDMENTCHARGES"));
         amendmendPrice.setPrice(amendmentprice.setScale(TWO, RoundingMode.HALF_UP));
         amendmendPrice.setCurrencyAppendedPrice(CurrencyUtils
            .getCurrencySymbol(defaultCurrencyCode)
            + (amendmentprice.setScale(TWO, RoundingMode.HALF_UP)).toString());
         priceBreakDownViewDatas.add(amendmendPrice);
      }

      priceAdjustmentList.addAll(lowDepositTopUpCalcService.getViewableBookingAdj(source));
      priceBreakDownViewDatas.addAll(populatePriceAdjustment(priceAdjustmentList,
         defaultCurrencyCode));

      totalPrice.setDescription(getDescription("TOTALPRICE"));
      totalPrice.setPrice(source.getPrice().getAmount().getAmount()
         .setScale(TWO, RoundingMode.HALF_UP));
      totalPrice.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + (source.getPrice().getAmount().getAmount().setScale(TWO, RoundingMode.HALF_UP))
            .toString());
      priceBreakDownViewDatas.add(totalPrice);

      target.setPriceBreakdownViewData(priceBreakDownViewDatas);

   }

   /**
    * @param priceAdjustmentList
    * @param viewDataBasicPrice
    * @return basic holiday cost
    */
   private BigDecimal manageBasicCost(final List<Price> priceAdjustmentList,
      final BigDecimal viewDataBasicPrice)
   {
      BigDecimal basicCost = viewDataBasicPrice;
      if (isNotEmpty(priceAdjustmentList))
      {
         basicCost = manageComponentAdj(priceAdjustmentList, basicCost);
      }
      return basicCost;
   }

   /**
    * @param sourceList
    * @param viewDataBasicPrice
    * @return basic holiday cost
    */
   private BigDecimal manageComponentAdj(final List<Price> sourceList,
      final BigDecimal viewDataBasicPrice)
   {
      BigDecimal basicCost = viewDataBasicPrice;
      BigDecimal amount;
      for (final Price price : sourceList)
      {
         amount = BigDecimal.ZERO;
         amount = price.getAmount().getAmount();
         if (amount.signum() < 0)
         {
            amount = amount.multiply(BigDecimal.valueOf(-1));
            basicCost = basicCost.add(amount);
         }
         else
         {
            basicCost = basicCost.subtract(amount);
         }
      }
      return basicCost;
   }

   /**
    * populate priceBreakDownViewData list with priceAdjustment list
    *
    * @param priceAdjustmentList
    * @param defaultCurrencyCode
    * @return priceAdjustmentViewDataList
    */
   private List<PriceBreakDownViewData> populatePriceAdjustment(
      final List<Price> priceAdjustmentList, final String defaultCurrencyCode)
   {
      final List<PriceBreakDownViewData> priceAdjustmentViewDataList =
         new ArrayList<PriceBreakDownViewData>();
      PriceBreakDownViewData priceAdjustment;
      if (isNotEmpty(priceAdjustmentList))
      {
         for (int counter = 0; counter < priceAdjustmentList.size(); counter++)
         {
            priceAdjustment = new PriceBreakDownViewData();
            priceAdjustment.setDescription(getDescription("PRICEADJUSTMENT"));
            priceAdjustment.setPrice(priceAdjustmentList.get(counter).getAmount().getAmount()
               .setScale(TWO, RoundingMode.HALF_UP));
            priceAdjustment.setCurrencyAppendedPrice(getPriceSymbol(priceAdjustmentList
               .get(counter).getAmount().getAmount(), defaultCurrencyCode));
            priceAdjustmentViewDataList.add(priceAdjustment);
         }
      }
      return priceAdjustmentViewDataList;
   }

   /**
    * @param amount
    * @return currency appended symbol
    */
   private String getPriceSymbol(final BigDecimal amount, final String defaultCurrencyCode)
   {
      BigDecimal tempAmount = amount;
      String symbol = EMPTY;
      if (tempAmount.signum() < 0)
      {
         symbol = "- ";
         tempAmount =
            tempAmount.multiply(BigDecimal.valueOf(-1)).setScale(TWO, RoundingMode.HALF_UP);
      }
      symbol += CurrencyUtils.getCurrencySymbol(defaultCurrencyCode) + tempAmount;
      return symbol;
   }

   private String getCurrencyAppendedPriceForDiscounts(final String defaultCurrencyCode,
      final BigDecimal discount)
   {
      return "-" + " " + CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + discount.multiply(BigDecimal.valueOf(-1)).setScale(TWO, RoundingMode.HALF_UP);

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
         promotionalDiscountPrice.setCurrencyAppendedPrice(getCurrencyAppendedPriceForDiscounts(
            currencyResolver.getSiteCurrency(), promotionalDis));
         target.add(promotionalDiscountPrice);
      }
   }

   private void populateDeposits(final BasePackage source, final PackageViewData target)
   {
      final List<DepositViewData> depositDatas = new ArrayList<DepositViewData>();
      final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd MMM yyyy");
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      final List<uk.co.tui.book.domain.lite.Deposit> copiedDeposits =
         lowDepositTopUpCalcService.getDeposits(source);
      if (CollectionUtils.isNotEmpty(copiedDeposits))
      {

         for (final Deposit deposit : copiedDeposits)
         {
            final DepositViewData depositData = new DepositViewData();
            depositData.setDepositDueDate(dateFormatter.format(deposit.getBalanceDueDate()));
            depositData.setDepositAmount(deposit.getOutstandingBalance());
            setDepositData(deposit, depositData);
            final BigDecimal viewDataBasicPrice =
               depositData.getDepositAmount().getAmount().getAmount()
                  .setScale(TWO, RoundingMode.HALF_UP);
            depositData.setCurrencyAppendedDepositPrice(CurrencyUtils
               .getCurrencySymbol(defaultCurrencyCode)
               + viewDataBasicPrice.setScale(TWO, RoundingMode.HALF_UP).toString());
            depositDatas.add(depositData);
         }
         target.setDeposits(depositDatas);
      }
   }

   /**
    * @param deposit
    * @param depositData
    */
   private void setDepositData(final Deposit deposit, final DepositViewData depositData)
   {
      if (checkForLowDeposit(deposit))
      {
         depositData.setDepositType("LOW_DEPOSIT");

      }
      else
      {
         depositData.setDepositType("BALANCE");
      }
   }

   /**
    * @param deposit
    * @return boolean
    */
   private boolean checkForLowDeposit(final Deposit deposit)
   {
      return deposit.getDepositType() != null
         && "LOW_DEPOSIT".equalsIgnoreCase(deposit.getDepositType().toString());
   }

   private void populateBookingDetails(final BasePackage source, final PackageViewData target)
   {
      final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd MMM yyyy");
      final BookingDetailsViewData bookingDetailsData = new BookingDetailsViewData();
      final List<BookingHistoryViewData> bookinghistoryDataList =
         new ArrayList<BookingHistoryViewData>();

      bookingDetailsData.setBookingReferenceNumber(source.getBookingRefNum());
      bookingDetailsData.setAmountDue(source.getBookingDetails().getAmountDue());
      bookingDetailsData.setCurrencyAppendedAmountDue(CurrencyUtils
         .getCurrencySymbol(currencyResolver.getSiteCurrency())
         + source.getBookingDetails().getAmountDue().getAmount()
            .setScale(TWO, RoundingMode.HALF_UP));
      bookingDetailsData.setAmountPaid(source.getBookingDetails().getAmountPaid());
      bookingDetailsData.setCurrencyAppendedAmountPaid(CurrencyUtils
         .getCurrencySymbol(currencyResolver.getSiteCurrency())
         + source.getBookingDetails().getAmountPaid().getAmount()
            .setScale(TWO, RoundingMode.HALF_UP));
      final Date dueDate = source.getBookingDetails().getDueDate();
      bookingDetailsData.setDueDate(dateFormatter.format(dueDate));

      for (final BookingHistory bkgHistory : source.getBookingDetails().getBookingHistory())
      {
         final List<PaymentReferenceViewData> paymentRefViewDataList =
            new ArrayList<PaymentReferenceViewData>();
         final BookingHistoryViewData bookinghistoryData = new BookingHistoryViewData();

         for (final PaymentReference payRef : bkgHistory.getPaymentReferences())
         {
            final PaymentReferenceViewData paymentRefViewData = new PaymentReferenceViewData();
            paymentRefViewData.setAmountPaid(payRef.getAmountPaid());
            paymentRefViewData.setCurrencyAppendedAmtPaid(CurrencyUtils
               .getCurrencySymbol(currencyResolver.getSiteCurrency())
               + payRef.getAmountPaid().getAmount().setScale(TWO, RoundingMode.HALF_UP));
            final String dateString = dateFormatter.format(payRef.getPaymentDate());
            paymentRefViewData.setPaymentDate(dateString);

            paymentRefViewDataList.add(paymentRefViewData);

         }

         bookinghistoryData.setPaymentReferenceViewDatas(paymentRefViewDataList);
         bookinghistoryDataList.add(bookinghistoryData);
      }
      bookingDetailsData.setBookingHistory(bookinghistoryDataList);
      target.setBookingDetails(bookingDetailsData);

   }

   private void populateAccomPrices(final BasePackage source, final PackageViewData target)
   {
      final PriceViewData priceData = new PriceViewData();
      final Stay availableAccommodation = packageComponentService.getStay(source);
      if (availableAccommodation != null && availableAccommodation.getPrice() != null)
      {

         priceData.setCode(availableAccommodation.getPrice().getCode());
         priceData.setQuantity(availableAccommodation.getPrice().getQuantity());
         priceData.setAmount(availableAccommodation.getPrice().getAmount());
         priceData.setDescription(availableAccommodation.getPrice().getDescription());
         target.getAvailableAccommodationViewData().setAccomPrice(priceData);

      }

   }

   private void populatePerPersonPrice(final BasePackage source, final PackageViewData target)
   {
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      target.setCurrencyAppendedPricePerPerson(getDescription("PRICEPERPERSON",
         CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
            + source.getPrice().getRate().getAmount().setScale(TWO, RoundingMode.HALF_UP)));

   }

   /**
    * Gets the description from content csv.
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
