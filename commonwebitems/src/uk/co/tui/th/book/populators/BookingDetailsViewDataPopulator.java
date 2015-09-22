/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.accommodation.MainstreamAccommodationService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.Deposit;
import uk.co.tui.book.domain.lite.DepositType;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PaymentReference;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.th.book.constants.BookFlowConstants;
import uk.co.tui.th.book.view.data.BookingDetailsViewData;
import uk.co.tui.th.book.view.data.DepositData;

/**
 * Booking details will be populated in to view data
 *
 * @author raghavendra.dm
 *
 */
public class BookingDetailsViewDataPopulator
{
   
   /** Week offset */
   private static final int WEEK_OFFSET = 5;
   
   /** Credit card payment type */
   private static final String CREDIT_CARD_PAYMENT_TYPE = "Credit";
   
   /** Credit card key */
   private static final String PERCENTAGE_CREDIT_CARD_CHARGE_KEY = "percentage.creditcard.charge";
   
   /** Default credit card charge */
   private static final String DEFAULT_CREDIT_CARD_CHARGE = "2%";
   
   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;
   
   @Resource
   private CurrencyResolver currencyResolver;
   
   @Resource
   private MainstreamAccommodationService accommodationService;
   
   @Resource
   private CMSSiteService cmsSiteService;
   
   @Resource
   private TUIUrlResolver<LocationModel> tuiCategoryModelUrlResolver;
   
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;
   
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;
   
   /** The Constant DATE_FORMAT. */
   private static final String DATE_FORMAT = "dd-MM-yyyy";
   
   private static final int TWO = 2;
   
   /**
    * @param packageModel package available in cart
    * @return bookingDetailsModel model to be saved with booking details of payment
    */
   public BookingDetailsViewData populate(final BasePackage packageModel,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      
      final List<Passenger> passengers = packageModel.getPassengers();
      final List<PaymentReference> paymentReferences =
         packageModel.getBookingDetails().getBookingHistory().get(0).getPaymentReferences();
      final Passenger passenger = getLeadPassenger(passengers);
      bookingDetailsViewData.setBookingReference(getBookingReferenceNumber(packageModel
         .getBookingDetails()));
      bookingDetailsViewData
         .setMulticomBookingReferenceNumber(getMulticomBookingRefNum(packageModel
            .getBookingDetails()));
      bookingDetailsViewData.setPassengerName(getPassengerName(passengers));
      String title = passenger.getTitle();
      title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
      bookingDetailsViewData.setTitle(title);
      bookingDetailsViewData.setEmailId(getEmailAddress(passengers));
      
      bookingDetailsViewData.setNoOfDaysLeft(calculateDaysToGo(packageComponentService.getStay(
         packageModel).getStartDate()));
      bookingDetailsViewData.setCurrencyAppendedTotalCost(getTotelCost(packageModel.getPrice()));
      bookingDetailsViewData.setCurrencyAppendedTotalPaid(getCurrencySymbol(paymentReferences)
         + getTotalAmountPaid(paymentReferences));
      bookingDetailsViewData.setPercentageCreditCardCharge(Config.getString(
         PERCENTAGE_CREDIT_CARD_CHARGE_KEY, DEFAULT_CREDIT_CARD_CHARGE));
      bookingDetailsViewData.setCreditCardPayment(isCreditCardPayment(paymentReferences));
      
      populateAPIandAccountURL(packageModel, bookingDetailsViewData);
      
      bookingDetailsViewData.setPaymentOptionsUrl(Config.getParameter("TH_PAYMENT_OPTIONS_URL"));
      
      bookingDetailsViewData.setDepositsDetails(populateDepositDetails(packageModel));
      
      bookingDetailsViewData.setEticketEmailDate(getETicketEmailDate(packageComponentService
         .getFlightItinerary(packageModel).getOutBound().get(0).getSchedule().getDepartureDate()));
      
      return bookingDetailsViewData;
   }
   
   private void populateAPIandAccountURL(final BasePackage packageModel,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      final String switchOffStartDate = Config.getParameter("apilink.switchoff.seasonstart.date");
      final String switchOffEndDate = Config.getParameter("apilink.switchoff.seasonend.date");
      if (StringUtils.isNotEmpty(switchOffStartDate) && StringUtils.isNotEmpty(switchOffEndDate))
      {
         final Date seasonStartSwitchDate =
            DateUtils.convertDateFromGivenFormat(switchOffStartDate, DATE_FORMAT);
         
         final Date seasonEndSwitchDate =
            DateUtils.convertDateFromGivenFormat(switchOffEndDate, DATE_FORMAT);
         final Date departureDate = getDepartureDate(packageModel);
         if (departureDateBtSwitchOffRange(seasonStartSwitchDate, seasonEndSwitchDate,
            departureDate))
         {
            bookingDetailsViewData.setSiteUrl(null);
            bookingDetailsViewData.setMyAccountUrl(null);
         }
         else
         {
            bookingDetailsViewData.setSiteUrl(Config.getParameter("SITE_URL"));
            bookingDetailsViewData.setMyAccountUrl(Config.getParameter("MY_THOMSON_ACCOUNT_URL"));
         }
         
      }
      else
      {
         bookingDetailsViewData.setSiteUrl(Config.getParameter("SITE_URL"));
         bookingDetailsViewData.setMyAccountUrl(Config.getParameter("MY_THOMSON_ACCOUNT_URL"));
      }
   }
   
   /**
    * @param packageModel
    * @return
    */
   private Date getDepartureDate(final BasePackage packageModel)
   {
      final Itinerary flightItineraryModel =
         packageComponentService.getFlightItinerary(packageModel);
      final Leg flightLeg = flightItineraryModel.getOutBound().get(0);
      Date departureDate = flightLeg.getSchedule().getDepartureDate();
      if (packageModel.getInventory().getInventoryType() == InventoryType.ATCOM)
      {
         departureDate = ((FlightLeg) flightLeg).getCycDate();
      }
      return departureDate;
   }
   
   /**
    * @param seasonStartSwitchDate
    * @param seasonEndSwitchDate
    * @param departureDate
    * @return
    */
   private boolean departureDateBtSwitchOffRange(final Date seasonStartSwitchDate,
      final Date seasonEndSwitchDate, final Date departureDate)
   {
      return (departureDate.after(seasonStartSwitchDate) || org.apache.commons.lang.time.DateUtils
         .isSameDay(departureDate, seasonStartSwitchDate))
         && (departureDate.before(seasonEndSwitchDate) || org.apache.commons.lang.time.DateUtils
            .isSameDay(departureDate, seasonEndSwitchDate));
   }
   
   /**
    * @param bookingDetails
    * @return multicomBookingReferenceNumber
    */
   private String getMulticomBookingRefNum(final BookingDetails bookingDetails)
   {
      return StringUtils.substringAfter(bookingDetails.getBookingHistory().get(0)
         .getBookingReference(), "|");
   }
   
   /**
    * @param paymentReferences
    * @return currency symbol.
    */
   private String getCurrencySymbol(final List<PaymentReference> paymentReferences)
   {
      for (final PaymentReference paymentReference : paymentReferences)
      {
         if (null != paymentReference.getAmountPaid())
         {
            return paymentReference.getAmountPaid().getCurrency().getSymbol();
         }
      }
      
      return "";
   }
   
   /**
    * @param bookingDetails
    * @return String
    */
   private String getBookingReferenceNumber(final BookingDetails bookingDetails)
   {
      return StringUtils.substringBefore(bookingDetails.getBookingHistory().get(0)
         .getBookingReference(), "|");
   }
   
   /**
    * @param departureDate
    * @return String
    */
   private String getETicketEmailDate(final Date departureDate)
   {
      
      // Revisit below code.
      
      @SuppressWarnings("deprecation")
      final Calendar calendar = Utilities.getDefaultCalendar();
      
      @SuppressWarnings("deprecation")
      final Calendar currentDate = Utilities.getDefaultCalendar();
      currentDate.setTime(new Date());
      calendar.setTime(departureDate);
      
      calendar.add(Calendar.WEEK_OF_YEAR, -WEEK_OFFSET);
      if (calendar.after(currentDate))
      {
         return DateUtils.customConfirmationDate(calendar.getTime());
      }
      return null;
   }
   
   /**
     *
     */
   private String getEmailAddress(final List<Passenger> passengers)
   {
      final Passenger passenger = getLeadPassenger(passengers);
      final Address address = passenger.getAddresses().iterator().next();
      
      return address.getEmail();
   }
   
   /**
    * @param paymentReferenceModels
    *
    */
   private String getTotalAmountPaid(final List<PaymentReference> paymentReferenceModels)
   {
      
      BigDecimal totalAmoutPaid = BigDecimal.ZERO;
      
      for (final PaymentReference paymentReference : paymentReferenceModels)
      {
         totalAmoutPaid = totalAmoutPaid.add(paymentReference.getAmountPaid().getAmount());
         if (paymentReference.getCardCharges() != null)
         {
            totalAmoutPaid = totalAmoutPaid.add(paymentReference.getCardCharges().getAmount());
         }
      }
      
      return totalAmoutPaid.setScale(TWO, BigDecimal.ROUND_HALF_UP).toString();
   }
   
   /**
    * @param totalPrice
    * @return String
    */
   private String getTotelCost(final Price totalPrice)
   {
      final Money totalPriceModel = totalPrice.getAmount();
      return totalPriceModel.getCurrency().getSymbol()
         + totalPriceModel.getAmount().setScale(TWO, BigDecimal.ROUND_HALF_UP).toString();
   }
   
   /**
    * @param endDate
    * @return int
    */
   private int calculateDaysToGo(final Date endDate)
   {
      return Days.daysBetween(new DateTime(), new DateTime(endDate)).getDays();
   }
   
   /**
    * @param passengers
    * @return String
    */
   private String getPassengerName(final List<Passenger> passengers)
   {
      final Passenger passenger = getLeadPassenger(passengers);
      return passenger.getFirstname() + " " + passenger.getLastname();
   }
   
   /**
    * To return Destination guide with name and urls.
    *
    * @return bookingDetailsViewData
    */
   public BookingDetailsViewData getBookingDetailsDestinationGuide()
   {
      final BasePackage packageModel = getPackageFromCart();
      final BookingDetailsViewData bookingDetailsViewData = new BookingDetailsViewData();
      final AccommodationModel accommodationModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(packageComponentService
            .getStay(packageModel).getCode(), cmsSiteService.getCurrentCatalogVersion());
      resolveDestinationNameAndUrls(accommodationModel.getSupercategories(),
         bookingDetailsViewData, 0);
      return bookingDetailsViewData;
   }
   
   /**
    * Populate deposit details.
    *
    * @param packageModel deposit models available in package
    * @return the list
    */
   private DepositData populateDepositDetails(final BasePackage packageModel)
   {
      final DepositData depositData = new DepositData();
      final List<Deposit> depositModels =
         priceCalculationService.getDepositWithPayImmediatePrices(packageModel);
      for (final Deposit depositModel : depositModels)
      {
         if (BooleanUtils.toBoolean(depositModel.getSelected()))
         {
            updateDepositData(depositData, depositModels, depositModel);
         }
      }
      return depositData;
   }
   
   /**
    * This method updates the DepositData.
    *
    * @param depositData
    * @param depositModels
    * @param depositModel
    */
   private void updateDepositData(final DepositData depositData, final List<Deposit> depositModels,
      final Deposit depositModel)
   {
      if (depositModel.getDepositType() == DepositType.LOW_DEPOSIT)
      {
         depositData.setLowDepositDueAmount(getCurrencyAppendedAmount(depositModel
            .getOutstandingBalance().getAmount().getAmount()));
         depositData.setLowDepositDueDate(DateUtils.customConfirmationDate(depositModel
            .getBalanceDueDate()));
         updateFurtherPayments(depositModels, depositData);
         
      }
      else if (depositModel.getDepositType() == DepositType.STANDARD_DEPOSIT)
      {
         depositData.setDepositDueAmount(getCurrencyAppendedAmount(depositModel
            .getOutstandingBalance().getAmount().getAmount()));
         depositData.setDepositDueDate(DateUtils.customConfirmationDate(depositModel
            .getBalanceDueDate()));
      }
   }
   
   /**
    * This method updates the further payments.
    *
    * @param depositModels
    * @param depositData
    */
   private void updateFurtherPayments(final List<Deposit> depositModels,
      final DepositData depositData)
   {
      for (final Deposit depositModel : depositModels)
      {
         if (depositModel.getDepositType() == DepositType.STANDARD_DEPOSIT)
         {
            final BigDecimal outStandingBalance = getOutStandingBalance(depositModel);
            if (outStandingBalance.compareTo(BigDecimal.ZERO) == 1)
            {
               depositData.setDepositDueAmount(getCurrencyAppendedAmount(outStandingBalance));
               depositData.setDepositDueDate(DateUtils.customConfirmationDate(depositModel
                  .getBalanceDueDate()));
               break;
            }
         }
      }
   }
   
   /**
    * Gets the outstanding balance.
    *
    * @param depositModel
    * @return BigDecimal
    */
   private BigDecimal getOutStandingBalance(final Deposit depositModel)
   {
      if (depositModel.getOutstandingBalance() != null
         && depositModel.getOutstandingBalance().getAmount() != null)
      {
         return depositModel.getOutstandingBalance().getAmount().getAmount();
      }
      
      return BigDecimal.ZERO;
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
   
   /**
    * @param supercategories
    * @param bookingDetailsViewData
    */
   private void resolveDestinationNameAndUrls(final Collection<CategoryModel> supercategories,
      final BookingDetailsViewData bookingDetailsViewData, final int counter)
   {
      int localCounter = 0;
      for (final CategoryModel category : supercategories)
      {
         final String type = category.getItemtype();
         if (!StringUtils.equalsIgnoreCase("Location", type))
         {
            continue;
         }
         localCounter = populateLocations(category, counter, bookingDetailsViewData);
         if (localCounter == TWO)
         {
            break;
         }
      }
   }
   
   /**
    * @param bookingDetailsViewData
    * @param category
    */
   private int setDestinationNameAndUrl(final BookingDetailsViewData bookingDetailsViewData,
      final CategoryModel category, final int cntr)
   {
      int counter = cntr;
      populateDestOneName(bookingDetailsViewData, category, counter);
      populateDestTwoName(bookingDetailsViewData, category);
      counter++;
      populateDestinations(bookingDetailsViewData, category, counter);
      return counter;
   }
   
   private void populateDestOneName(final BookingDetailsViewData bookingDetailsViewData,
      final CategoryModel category, final int counter)
   {
      if (counter == 0)
      {
         bookingDetailsViewData.setDestOneName(category.getName());
         bookingDetailsViewData.setDestOneUrl(tuiCategoryModelUrlResolver
            .resolve((LocationModel) category));
      }
   }
   
   private void populateDestTwoName(final BookingDetailsViewData bookingDetailsViewData,
      final CategoryModel category)
   {
      
      bookingDetailsViewData.setDestTwoName(category.getName());
      bookingDetailsViewData.setDestTwoUrl(tuiCategoryModelUrlResolver
         .resolve((LocationModel) category));
      
   }
   
   private void populateDestinations(final BookingDetailsViewData bookingDetailsViewData,
      final CategoryModel category, final int counter)
   {
      if ((counter <= 1) && !category.getSupercategories().isEmpty())
      {
         final List<CategoryModel> superCategories =
            new ArrayList<CategoryModel>(category.getSupercategories());
         resolveDestinationNameAndUrls(superCategories, bookingDetailsViewData, counter);
      }
   }
   
   private int populateLocations(final CategoryModel category, final int counter,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      if (!BookFlowConstants.LOCATION_CATAGORIES.contains(category.getCode()))
      {
         return populateLocationCategories(category, counter, bookingDetailsViewData);
      }
      return 0;
   }
   
   /**
    * @param category
    */
   private int populateLocationCategories(final CategoryModel category, final int counter,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      int count = 0;
      count = populateResortAndDestination(category, counter, bookingDetailsViewData);
      count = populateRegionAndCountry(category, counter, bookingDetailsViewData);
      return count;
   }
   
   private int populateResortAndDestination(final CategoryModel category, final int counter,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      int count = 0;
      if (LocationType.RESORT.getCode().equals(((LocationModel) category).getType().getCode()))
      {
         count = setDestinationNameAndUrl(bookingDetailsViewData, category, counter);
      }
      else if (LocationType.DESTINATION.getCode().equals(
         ((LocationModel) category).getType().getCode()))
      {
         count = setDestinationNameAndUrl(bookingDetailsViewData, category, counter);
      }
      return count;
   }
   
   private int populateRegionAndCountry(final CategoryModel category, final int counter,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      int count = 0;
      if (LocationType.REGION.getCode().equals(((LocationModel) category).getType().getCode()))
      {
         count = setDestinationNameAndUrl(bookingDetailsViewData, category, counter);
      }
      else if (LocationType.COUNTRY.getCode()
         .equals(((LocationModel) category).getType().getCode()))
      {
         count = setDestinationNameAndUrl(bookingDetailsViewData, category, counter);
      }
      return count;
   }
   
   /**
    * Gets the currency appended amount.
    *
    * @param amount amount
    * @return currencyAppendedAmount amount string after appending currency symbol
    */
   private String getCurrencyAppendedAmount(final BigDecimal amount)
   {
      final String currencyAppendedAmount =
         amount.setScale(TWO, BigDecimal.ROUND_HALF_UP).toString();
      return CurrencyUtils.getCurrencySymbol(Currency.getInstance(
         currencyResolver.getSiteCurrency()).toString())
         + currencyAppendedAmount;
   }
   
   /**
    * @param passengers
    * @return passenger
    */
   private Passenger getLeadPassenger(final List<Passenger> passengers)
   {
      Passenger passenger = null;
      for (final Passenger passengerModel : passengers)
      {
         if (passengerModel.isLead())
         {
            passenger = passengerModel;
         }
      }
      return passenger;
   }
   
   /**
    * Checks if the payment is done by credit card.
    *
    * @param paymentReferences
    * @return boolean
    */
   private boolean isCreditCardPayment(final List<PaymentReference> paymentReferences)
   {
      for (final PaymentReference paymentReference : paymentReferences)
      {
         return StringUtils.equalsIgnoreCase(paymentReference.getPaymentType(),
            CREDIT_CARD_PAYMENT_TYPE);
      }
      return false;
   }
}
