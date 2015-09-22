/**
 *
 */
package uk.co.tui.fo.book.populators;

import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.BookFlowConstants;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.PaymentReference;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.PromotionalDiscount;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.config.BookFlowTUIConfigService;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.fo.book.view.data.BookingDetailsViewData;
import uk.co.tui.fo.book.view.data.ConfirmationViewData;
import uk.co.tui.fo.book.view.data.ExtraOptionsViewData;
import uk.co.tui.fo.book.view.data.FlightOptionsViewData;
import uk.co.tui.fo.book.view.data.PackageViewData;
import uk.co.tui.fo.book.view.data.PassengerDetailsViewData;
import uk.co.tui.fo.book.view.data.PriceBreakDownViewData;

/**
 * @author prashant.godi
 *
 */
public class BookFlowAnalyticsPopulator implements Populator<Object, Map<String, WebAnalytics>>
{
   /** Package Analytics Helper . */
   @Resource
   private AnalyticsHelper analyticsHelper;

   @Resource
   private BookFlowTUIConfigService bookFlowTuiConfigService;

   /** Package Cart Service . */
   @Resource
   private PackageCartService packageCartService;

   /** session Service. */
   @Resource
   private SessionService sessionService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The Constant TOTAL_PRICE. */
   private static final String TOTAL_PRICE = "total price";

   /** Adding Fields To Capture The Filter Details For Analytics. */
   private static final String MONTHYEAR_FORMAT = "MMM yyyy";

   /** The Constant for DISCOUNT. */
   private static final String DISCOUNT = "Online Discount";

   /** The Constant for integer TEN. */
   private static final int TEN = 10;

   /** The Constant for zero. */
   private static final String ZERO = "0";

   /** The Constant for PAYTYPE. */
   private static final String PAYTYPE = "PayType";

   /** The Constant for AMOUNT. */
   private static final String AMOUNT = "Amount";

   /** The Constant for LIMAVB. */
   private static final String LIMAVS = "LimAvS";

   /** The Constant for PROMOVALUE. */
   private static final String PROMOVALUE = "PromoValue";

   /** The Constant for PROMOCODE. */
   private static final String PROMOCODE = "PromoCode";

   /** The Constant for TRACSREF. */
   private static final String TRACSREF = "Tracsref";

   /** The Constant for EML. */

   /** The Constant for RESULTS */
   private static final String RESULTS = "Results";

   /** The Constant for DREAMFL. */
   private static final String DREAMFL = "DreamFl";

   /** The Constant for PAX. */
   private static final String PAX = "Pax";

   /** The Constant for DUR. */
   private static final String DURATION = "Dur";

   /** The Constant for DEPDATE. */
   private static final String DEPDATE = "DepDate";

   /** The Constant for MONTHYEAR. */
   private static final String MONTHYEAR = "MonthYear";

   /** The Constant for DEPAIR. */
   private static final String DEPAIR = "DepAir";

   /** The Constant for WHERETO. */
   private static final String WHERETO = "WhereTo";

   /** The Constant for SUM. */
   private static final String SUM = "Sum";

   /** The Constant for DISC. */
   private static final String DISC = "Disc";

   /** The Constant for TUIANC. */
   private static final String TUIANC = "TuiAnc";

   /** The Constant FLTINV. */
   private static final String FLTINV = "FltInv";

   /** The Constant CARRIER. */
   private static final String CARRIER = "carrier";

   /** The Constant FltType. */
   private static final String FLTTYPE = "FltType";

   /** The Constant PARTY. */
   private static final String PARTY = "Party";

   /** The Constant for DELIMITER. */
   private static final String DELIMITER = "|";

   /** The Constant for HYPHEN. */
   private static final String HYPHEN = "-";

   /** The Constant for null. */
   private static final String NULL = "null";

   /** Constant for Insurance. */
   private static final String INSURANCE = "INS";

   /** Constant for Outbound’. */
   private static final String OUTBOUND = "Outbound";

   /** Constant for Inbound’. */
   private static final String INBOUND = "Inbound";

   /** Constant for oneWay. */
   private static final String RETURN = "Return";

   private static final String LATESTCRITERIA = "flightsearchcriteria";

   private static final int TWO = 2;

   /**
    * Analytics populator for Flight Options page.
    *
    * @param source - it is a ViewData object contains all the data related to the book flow page.
    * @param analyticMap -it is target object for the analytics
    */
   @Override
   public void populate(final Object source, final Map<String, WebAnalytics> analyticMap)
   {

      // Flight options
      populateAnalyticsForFlightOptions(source, analyticMap);

      // Extra Options Page
      populateAnalyticsForExtraOptions(source, analyticMap);

      // Passengers Details page
      populateAnalyticsForPassengerDetails(source, analyticMap);

      // Confirmation Page
      populateAnalyticsForConfirmation(source, analyticMap);

   }

   /**
    * Populate analytics for confirmation.
    *
    * @param source the source
    * @param analyticMap the analytic map
    */
   private void populateAnalyticsForConfirmation(final Object source,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (source instanceof ConfirmationViewData)
      {
         final ConfirmationViewData confirmationViewData = (ConfirmationViewData) source;
         final PackageViewData packageViewData = confirmationViewData.getPackageViewData();
         populateConfirmationsParams(confirmationViewData, analyticMap);
         populateGenericParams(packageViewData, analyticMap);
      }
   }

   /**
    * Populate analytics for passenger details.
    *
    * @param source the source
    * @param analyticMap the analytic map
    */
   private void populateAnalyticsForPassengerDetails(final Object source,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (source instanceof PassengerDetailsViewData)
      {
         final PassengerDetailsViewData passengerDetailsViewData =
            (PassengerDetailsViewData) source;
         final PackageViewData packageViewData = passengerDetailsViewData.getPackageViewData();
         populateGenericParams(packageViewData, analyticMap);
      }
   }

   /**
    * Populate analytics for extra options.
    *
    * @param source the source
    * @param analyticMap the analytic map
    */
   private void populateAnalyticsForExtraOptions(final Object source,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (source instanceof ExtraOptionsViewData)
      {
         final ExtraOptionsViewData extraOptionsViewData = (ExtraOptionsViewData) source;
         final PackageViewData packageViewData = extraOptionsViewData.getPackageViewData();
         populateGenericParams(packageViewData, analyticMap);
      }
   }

   /**
    * Populate analytics for flight options.
    *
    * @param source the source
    * @param analyticMap the analytic map
    */
   private void populateAnalyticsForFlightOptions(final Object source,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (source instanceof FlightOptionsViewData)
      {
         final FlightOptionsViewData flightOptionsViewData = (FlightOptionsViewData) source;
         final PackageViewData packageViewData = flightOptionsViewData.getPackageViewData();
         populateGenericParams(packageViewData, analyticMap);
      }
   }

   /**
    * Populate confirmations params.
    *
    * @param confirmationViewData the confirmation view data
    * @param analyticMap the analytic map
    */
   private void populateConfirmationsParams(final ConfirmationViewData confirmationViewData,
      final Map<String, WebAnalytics> analyticMap)
   {
      populateResults(analyticMap);
      populateTracsref(analyticMap, confirmationViewData.getBookingDetailsViewData());
      populatePromotionalDiscount(analyticMap);
      populateAmount(analyticMap, confirmationViewData.getBookingDetailsViewData());
      populatePayType(analyticMap);
   }

   /**
    * Populate generic params.
    *
    * @param packageViewData the package view data
    * @param analyticMap the analytic map
    */
   private void populateGenericParams(final PackageViewData packageViewData,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (isNotNull(packageViewData))
      {
         populateWhereTo(analyticMap);
         populateAirport(analyticMap, packageViewData);
         populateDepartureDate(analyticMap, packageViewData);
         populatePartyComposition(analyticMap);
         populateDuration(analyticMap);
         populatePrice(analyticMap, packageViewData);
         populateDreamFI(analyticMap);
         populateLimAvS(analyticMap);
         populatePax(analyticMap, packageViewData);
         populateTuiAnc(analyticMap);
         populateFltInv(analyticMap);
         populateCarrier(analyticMap);
         populateFltType(analyticMap);

      }
   }

   /**
    * populates Limited Availability Indicator Parameter - Seats Limited Availability Indicator on
    * it then It will populate with the numerical value that references the number of seats left and
    * not the number of rooms no indicator then populate â€˜nullâ€™.
    *
    * @param analyticMap the analytic map
    */
   @SuppressWarnings("boxing")
   private void populateLimAvS(final Map<String, WebAnalytics> analyticMap)
   {
      String limAvS = NULL;
      final ExtraFacility extraFacility = getSeatExtraFacility(getCartPackageModel());
      final int availableQuantity = getExtraFacilityQuantity(extraFacility);
      if (compareThresholdValue(availableQuantity,
         getSeatsThreshold(extraFacility.getExtraFacilityCode())))
      {
         limAvS = String.valueOf(availableQuantity);
      }
      analyticMap.put(LIMAVS, new WebAnalytics(LIMAVS, StringUtils.defaultIfEmpty(limAvS, NULL)));

   }

   /**
    * Gets the extra facility quantity.
    *
    * @param extraFacility the extra facility
    * @return the extra facility quantity
    */
   private int getExtraFacilityQuantity(final ExtraFacility extraFacility)
   {
      if (nonStandardSeatOptions(extraFacility))
      {
         return extraFacility.getQuantity().intValue();
      }
      return packageComponentService.getFlightItinerary(getCartPackageModel()).getMinAvail();
   }

   /**
    * Non standard seat options.
    *
    * @param extraFacility the extra facility
    * @return true, if successful
    */
   private boolean nonStandardSeatOptions(final ExtraFacility extraFacility)
   {
      return StringUtils.equals(extraFacility.getExtraFacilityCode(),
         ExtraFacilityConstants.PREMIUM_SEAT)
         || StringUtils.equals(extraFacility.getExtraFacilityCode(),
            ExtraFacilityConstants.EXTRA_LEG_ROOM)
         || StringUtils.equals(extraFacility.getExtraFacilityCode(),
            ExtraFacilityConstants.SEATS_WITH_EXTRA_SPACE);
   }

   /**
    * Gets the seat extra facility.
    *
    * @param packageModel the package model
    * @return the seat extra facility
    */
   private ExtraFacility getSeatExtraFacility(final BasePackage packageModel)
   {
      ExtraFacility facility = null;
      for (final ExtraFacilityCategory cat : packageModel.getExtraFacilityCategories())
      {
         if (StringUtils.equals(cat.getCode(), ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
         {
            facility = cat.getExtraFacilities().get(0);
         }
      }
      return facility;
   }

   /**
    * Compare threshold value.
    *
    * @param availableQuantity the available quantity
    * @param seatThreshold the seat threshold
    * @return true, if successful
    */
   private boolean compareThresholdValue(final int availableQuantity, final int seatThreshold)
   {
      return availableQuantity > 0 && availableQuantity <= seatThreshold;
   }

   /**
    * Gets the seat threshold.
    *
    * @param seatCode the seat code
    * @return the seat threshold
    */
   private int getSeatsThreshold(final String seatCode)
   {

      if (StringUtils.equalsIgnoreCase(seatCode, ExtraFacilityConstants.PREMIUM_SEAT))
      {
         return bookFlowTuiConfigService.getPremiumSeatsThreshold();
      }
      else if (StringUtils.equalsIgnoreCase(seatCode, ExtraFacilityConstants.EXTRA_LEG_ROOM))
      {
         return bookFlowTuiConfigService.getExtraLegRoomSeatsThreshold();
      }
      else if (StringUtils
         .equalsIgnoreCase(seatCode, ExtraFacilityConstants.SEATS_WITH_EXTRA_SPACE))
      {
         return bookFlowTuiConfigService.getExtaSpaceSeatsThreshold();
      }
      return bookFlowTuiConfigService.getStandardSeatsThreshold();
   }

   /**
    * Party Composition for rooms population for Analytics.
    *
    * @param analyticMap the analytic map
    */
   private void populatePartyComposition(final Map<String, WebAnalytics> analyticMap)

   {
      String party = StringUtils.EMPTY;
      final FlightSearchCriteria flightSearchCriteria = sessionService.getAttribute(LATESTCRITERIA);
      final StringBuilder partyComp = new StringBuilder();
      if (flightSearchCriteria != null)
      {
         partyComp.append("A");
         partyComp.append(flightSearchCriteria.getAdultCount());
         partyComp.append("/");
         partyComp.append("C");
         partyComp.append(flightSearchCriteria.getChildCount());
         partyComp.append("/");
         partyComp.append("I");
         partyComp.append(flightSearchCriteria.getInfantCount());

      }
      party = partyComp.toString();
      final WebAnalytics webAnalyticParty = new WebAnalytics(PARTY, party);
      analyticMap.put(PARTY, webAnalyticParty);
   }

   public void populateFltType(final Map<String, WebAnalytics> analyticMap)
   {

      final FlightSearchCriteria flightSearchCriteria = sessionService.getAttribute(LATESTCRITERIA);
      final boolean isOnway = flightSearchCriteria.isOneWay();
      if (isOnway)
      {
         getFlightOnlyType(analyticMap, flightSearchCriteria);

      }
      else
      {
         final WebAnalytics fltTypeReturn = new WebAnalytics(FLTTYPE, RETURN);
         analyticMap.put(FLTTYPE, fltTypeReturn);
      }
   }

   /**
    * @param analyticMap
    * @param flightSearchCriteria
    */
   private void getFlightOnlyType(final Map<String, WebAnalytics> analyticMap,
      final FlightSearchCriteria flightSearchCriteria)
   {
      if (flightSearchCriteria.getFlightsonlyType() == TWO)
      {
         final WebAnalytics fltTypeOneWay = new WebAnalytics(FLTTYPE, OUTBOUND);
         analyticMap.put(FLTTYPE, fltTypeOneWay);
      }
      else
      {
         final WebAnalytics fltTypeOneWay = new WebAnalytics(FLTTYPE, INBOUND);
         analyticMap.put(FLTTYPE, fltTypeOneWay);
      }
   }

   /**
    * Populate carrier.
    *
    * @param analyticMap the analytic map
    */
   private void populateCarrier(final Map<String, WebAnalytics> analyticMap)
   {
      final BasePackage packageModel = getCartPackageModel();
      final Itinerary flightItinerary = packageComponentService.getFlightItinerary(packageModel);
      String inBoundCarrierCode = "";
      // krus:if chks added for Flightonly - oneway
      if (CollectionUtils.isNotEmpty(flightItinerary.getInBound()))
      {
         inBoundCarrierCode =
            flightItinerary.getInBound().get(0).getCarrier().getCarrierInformation()
               .getMarketingAirlineCode().toString();
      }
      String outBoundCarrierCode = "";
      if (CollectionUtils.isNotEmpty(flightItinerary.getOutBound()))
      {
         outBoundCarrierCode =
            flightItinerary.getOutBound().get(0).getCarrier().getCarrierInformation()
               .getMarketingAirlineCode().toString();
      }
      analyticMap.put(CARRIER, new WebAnalytics(CARRIER, outBoundCarrierCode + "|"
         + inBoundCarrierCode));

   }

   /**
    * Populates flight inventory.
    *
    * @param analyticMap the analytic map
    */
   private void populateFltInv(final Map<String, WebAnalytics> analyticMap)
   {
      final BasePackage packageModel = getCartPackageModel();
      final Itinerary flightItinerary = packageComponentService.getFlightItinerary(packageModel);
      final boolean inBoundFlight = inBoundFlightFromExternalInventory(flightItinerary);
      final boolean outBoundFlight = outBoundFlightFromExternalInventory(flightItinerary);
      checkFlightsBasedOnInAndOutBounds(analyticMap, packageModel, inBoundFlight, outBoundFlight);
   }

   /**
    * Check flights based on in and out bounds.
    *
    * @param analyticMap the analytic map
    * @param packageModel the package model
    * @param inBoundFlight the in bound flight
    * @param outBoundFlight the out bound flight
    */
   private void checkFlightsBasedOnInAndOutBounds(final Map<String, WebAnalytics> analyticMap,
      final BasePackage packageModel, final boolean inBoundFlight, final boolean outBoundFlight)
   {
      if (threePF(inBoundFlight, outBoundFlight))
      {
         analyticMap.put(FLTINV, new WebAnalytics(FLTINV, "3PF"));
      }
      else if (tracsFlight(inBoundFlight, outBoundFlight))
      {
         analyticMap.put(FLTINV, new WebAnalytics(FLTINV, packageModel.getInventory()
            .getInventoryType().toString()));
      }
      else
      {
         analyticMap.put(FLTINV, new WebAnalytics(FLTINV, "Mixed"));
      }
   }

   /**
    * Out bound flight from external inventory.
    *
    * @param flightItinerary the flight itinerary
    * @return true, if successful
    */
   private boolean outBoundFlightFromExternalInventory(final Itinerary flightItinerary)
   {
      boolean outBoundFlight = false;
      if (flightItinerary.getOutBound() != null)
      {
         outBoundFlight = isFlightFromExternalInventory(flightItinerary.getOutBound());
      }
      return outBoundFlight;
   }

   /**
    * In bound flight from external inventory.
    *
    * @param flightItinerary the flight itinerary
    * @return true, if successful
    */
   private boolean inBoundFlightFromExternalInventory(final Itinerary flightItinerary)
   {
      // krus:null check added Flight only one way
      boolean inBoundFlight = false;
      if (flightItinerary.getInBound() != null)
      {
         inBoundFlight = isFlightFromExternalInventory(flightItinerary.getInBound());
      }
      return inBoundFlight;
   }

   /**
    * Checks if Tracs flight or not.
    *
    * @param inBoundFlight the in bound flight
    * @param outBoundFlight the out bound flight
    * @return true, if successful
    */
   private boolean tracsFlight(final boolean inBoundFlight, final boolean outBoundFlight)
   {
      return !inBoundFlight && !outBoundFlight;
   }

   /**
    * Checks if 3PF Flight or not.
    *
    * @param inBoundFlight the in bound flight
    * @param outBoundFlight the out bound flight
    * @return true, if successful
    */
   private boolean threePF(final boolean inBoundFlight, final boolean outBoundFlight)
   {
      return inBoundFlight && outBoundFlight;
   }

   /**
    * Checks if is flight from external inventory.
    *
    * @param legModel the leg model
    * @return true, if is flight from external inventory
    */
   private static boolean isFlightFromExternalInventory(final List<Leg> legModel)
   {
      for (final Leg leg : legModel)
      {
         if (leg.isExternalInventory())
         {
            return true;
         }
      }
      return false;
   }

   /**
    * The value of the parameter will be ‘Debit’, ‘Credit’ or whatever other payment method was
    * used.
    *
    * @param analyticMap the analytic map
    */
   private void populatePayType(final Map<String, WebAnalytics> analyticMap)
   {
      String payType = NULL;
      final BasePackage packageModel = getCartPackageModel();
      if (packageModel.getBookingDetails() != null)
      {
         final BookingDetails bookingDetails = packageModel.getBookingDetails();
         if (CollectionUtils.isNotEmpty(bookingDetails.getBookingHistory().get(0)
            .getPaymentReferences()))
         {
            final PaymentReference paymentReferenceModel =
               bookingDetails.getBookingHistory().get(0).getPaymentReferences().get(0);
            payType = paymentReferenceModel.getPaymentType();
         }
      }

      analyticMap.put(PAYTYPE, new WebAnalytics(PAYTYPE, payType));
   }

   /**
    * The value of the deposit paid The value should not include a decimal point and the value
    * should be rounded up or down.
    *
    * @param analyticMap the analytic map
    * @param bookingDetailsViewData the booking details view data
    */
   private void populateAmount(final Map<String, WebAnalytics> analyticMap,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      int amount = 0;
      String afterRemovalOfPoundSymbol;
      final String paidAmount = bookingDetailsViewData.getCurrencyAppendedTotalPaid();
      afterRemovalOfPoundSymbol = paidAmount.substring(1, paidAmount.length());
      amount = analyticsHelper.getPriceRoundedValue(afterRemovalOfPoundSymbol);
      analyticMap.put(AMOUNT, new WebAnalytics(AMOUNT, String.valueOf(amount)));
   }

   /**
    * The value of the promo code used.
    *
    * @param analyticMap the analytic map
    */
   private void populatePromotionalDiscount(final Map<String, WebAnalytics> analyticMap)
   {
      String promoValue = NULL;
      String promoCode = NULL;

      final BasePackage packageModel = getCartPackageModel();
      if (packageModel.getPromotionalDiscount() != null)
      {
         final PromotionalDiscount promotionalDiscount = packageModel.getPromotionalDiscount();
         promoCode = promotionalDiscount.getCode();
         promoValue = String.valueOf(promotionalDiscount.getPrice().getAmount().getAmount());
         promoValue = removeMinusSymbol(promoValue);
      }
      analyticMap.put(PROMOVALUE, new WebAnalytics(PROMOVALUE, promoValue));
      analyticMap.put(PROMOCODE, new WebAnalytics(PROMOCODE, promoCode));
   }

   /**
    * The TRACS booking reference.
    *
    * @param analyticMap the analytic map
    * @param bookingDetailsViewData the booking details view data
    */
   private void populateTracsref(final Map<String, WebAnalytics> analyticMap,
      final BookingDetailsViewData bookingDetailsViewData)
   {
      final String tracsref = bookingDetailsViewData.getBookingReference();
      analyticMap.put(TRACSREF, new WebAnalytics(TRACSREF, tracsref));
   }

   /**
    * The number of search results on the page at the point of leaving the page
    *
    * @param analyticMap
    */
   private void populateResults(final Map<String, WebAnalytics> analyticMap)
   {
      final String results = NULL;
      analyticMap.put(RESULTS, new WebAnalytics(RESULTS, results));
   }

   /**
    * This will populate either â€˜Yesâ€™ or â€˜Noâ€™ depending on whether the package selected has
    * Dreamliner flights or not.
    *
    * @param analyticMap the analytic map
    */
   private void populateDreamFI(final Map<String, WebAnalytics> analyticMap)
   {
      String dreamFl = "No";
      final BasePackage packageModel = getCartPackageModel();
      final Itinerary flightItenary = packageComponentService.getFlightItinerary(packageModel);
      if (checkDreamLinerPresence(flightItenary))
      {
         dreamFl = "Yes";
      }
      analyticMap.put(DREAMFL, new WebAnalytics(DREAMFL, dreamFl));
   }

   /**
    * Check dream liner presence.
    *
    * @param flightItenary the flight itenary
    * @return true, if successful
    */
   private boolean checkDreamLinerPresence(final Itinerary flightItenary)
   {
      final boolean dreamLinerInBoundFlag = checkDreamLinerFlagPresence(flightItenary.getInBound());
      final boolean dreamLinerOutBoundFlag =
         checkDreamLinerFlagPresence(flightItenary.getOutBound());
      return dreamLinerInBoundFlag || dreamLinerOutBoundFlag;
   }

   /**
    * Check dream liner flag presence.
    *
    * @param legs the legs
    * @return true, if successful
    */
   private boolean checkDreamLinerFlagPresence(final List<Leg> legs)
   {
      boolean dreamLinerFlag = false;
      for (final Leg leg : legs)
      {
         dreamLinerFlag = isFlightDreamLiner(leg);
      }
      return dreamLinerFlag;
   }

   /**
    * Checks if is flight dream liner.
    *
    * @param leg the leg
    * @return dreamLinerFlag
    */
   private boolean isFlightDreamLiner(final Leg leg)
   {
      boolean dreamLinerFlag = false;
      if (null != leg.getCarrier().getEquipementType()
         && StringUtils.equals(leg.getCarrier().getEquipementType().toString(), "DREAMLINEAR787"))
      {
         dreamLinerFlag = true;
      }
      return dreamLinerFlag;
   }

   /**
    * The value passed will be the numerical value of how many passengers are in the package. e.g.
    * if the packages selected was for 2 adults, 1 child and 0 infants the value would be â€˜3â€™
    * note: MUST exclude infants
    *
    * @param analyticMap the analytic map
    * @param packageViewData the package view data
    */
   private void populatePax(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      int paxSize = 0;
      if (packageViewData.getPaxViewData() != null)
      {
         // Adults + Seniors + chidren(Exclude Infants)
         paxSize =
            packageViewData.getPaxViewData().getNoOfAdults()
               + packageViewData.getPaxViewData().getNoOfSeniors()
               + packageViewData.getPaxViewData().getNoOfChildren();

      }
      analyticMap.put(PAX, new WebAnalytics(PAX, String.valueOf(paxSize)));
   }

   /**
    * Duration population for Analytics numerical value of the number of nights for the package
    * selected.
    *
    * @param analyticMap the analytic map
    */
   private void populateDuration(final Map<String, WebAnalytics> analyticMap)
   {
      final BasePackage basePackageModel = packageCartService.getBasePackage();

      final FlightSearchCriteria flightSearchCriteria = sessionService.getAttribute(LATESTCRITERIA);
      if (flightSearchCriteria.isOneWay())
      {
         analyticMap.put(DURATION, new WebAnalytics(DURATION, "null"));

      }
      else
      {
         analyticMap.put(DURATION,
            new WebAnalytics(DURATION, String.valueOf(basePackageModel.getDuration())));
      }
   }

   /**
    * Date population for AccommodationAnalytics. It will be in DD format e.g. if a customer
    * searches for a holiday departing on 03/05/2013, DepDate=03 and not DepDate=3
    *
    * It will be in MM/YYYY format e.g. if a customer searches for 13/05/2013, MonthYear=05/2013 and
    * not 5/2013.
    *
    * @param analyticMap the analytic map
    * @param packageViewData the package view data
    */
   public void populateDepartureDate(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      if (CollectionUtils.isNotEmpty(packageViewData.getFlightViewData().get(0)
         .getOutboundSectors()))
      {
         final List<uk.co.tui.fo.book.view.data.Leg> outbounds =
            packageViewData.getFlightViewData().get(0).getOutboundSectors();
         final String date = outbounds.get(0).getSchedule().getDepartureDate();
         final String depDate = getdayOfTheDate(date);
         final String monthYear = getMonthAndYear(date);
         final LocalDate departureDate = DateUtils.toDate(monthYear, MONTHYEAR_FORMAT);
         analyticsHelper.addMonthYear(analyticMap, departureDate);
         analyticMap.put(DEPDATE, new WebAnalytics(DEPDATE, formatDate(depDate)));
      }
      else
      {
         analyticMap.put(MONTHYEAR, new WebAnalytics("MonthYear", ""));
         analyticMap.put(DEPDATE, new WebAnalytics(DEPDATE, ""));
      }
   }

   /**
    * Airport Data Population for for Accommodation Page Analytics. The value of the parameter must
    * be the 3 character airport code of associated with the departure airport of the package
    * selected
    *
    * @param analyticMap the analytic map
    * @param packageViewData the package view data
    */
   public void populateAirport(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      String depAirportCode = NULL;

      if (CollectionUtils.isNotEmpty(packageViewData.getFlightViewData().get(0)
         .getOutboundSectors()))
      {
         final List<uk.co.tui.fo.book.view.data.Leg> outbounds =
            packageViewData.getFlightViewData().get(0).getOutboundSectors();
         depAirportCode = (outbounds.get(0)).getDepartureAirport().getCode();
      }
      final WebAnalytics webAnalyticDepAir = new WebAnalytics(DEPAIR, depAirportCode);
      analyticMap.put(DEPAIR, webAnalyticDepAir);
   }

   /**
    * WhereTo population for Accommodation Page Analytics. The value of the parameter MUST be the
    * Epic Code of the Unit selected
    *
    * @param analyticMap the analytic map
    */
   public void populateWhereTo(final Map<String, WebAnalytics> analyticMap)
   {

      final FlightSearchCriteria flightSearchCriteria = sessionService.getAttribute(LATESTCRITERIA);
      String arrivalAirportCode = NULL;
      if (StringUtils.isNotEmpty(flightSearchCriteria.getArrivalAirportCode().get(0).toString()))
      {
         arrivalAirportCode = flightSearchCriteria.getArrivalAirportCode().get(0).toString();
      }
      final WebAnalytics webAnalyticWhereTo = new WebAnalytics(WHERETO, arrivalAirportCode);
      analyticMap.put(WHERETO, webAnalyticWhereTo);
   }

   /**
    * Population of Price Data for Analytics. The value is to be rounded either up or down such that
    * we have no decimal places. If the decimal value is .50 please round this up. The value passed
    * is to have no â€˜Â£â€™ sign or decimal places
    *
    * @param analyticMap the analytic map
    * @param packageViewData the package view data
    */
   public void populatePrice(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {

      int sum = 0;
      int discount = 0;
      if (CollectionUtils.isNotEmpty(packageViewData.getPriceBreakdownViewData()))
      {
         for (final PriceBreakDownViewData priceBreakdownViewData : packageViewData
            .getPriceBreakdownViewData())
         {
            sum = getSum(priceBreakdownViewData, sum);
            discount = getDiscount(priceBreakdownViewData, discount);
            analyticMap.put(SUM, new WebAnalytics(SUM, String.valueOf(sum)));
            analyticMap.put(DISC, new WebAnalytics(DISC, String.valueOf(discount)));
         }
      }
   }

   /**
    * Gets the discount.
    *
    * @param priceBreakdownViewData the price breakdown view data
    * @param discount the discount
    * @return discount
    */
   private int getDiscount(final PriceBreakDownViewData priceBreakdownViewData, final int discount)
   {
      int discountPrice = discount;
      if (StringUtils.equalsIgnoreCase(priceBreakdownViewData.getDescription(), DISCOUNT))
      {
         discountPrice =
            analyticsHelper.getPriceRoundedValue(priceBreakdownViewData.getPrice().toString());
      }
      return discountPrice;
   }

   /**
    * Gets the sum.
    *
    * @param priceBreakdownViewData the price breakdown view data
    * @param sum the sum
    * @return sum
    */
   private int getSum(final PriceBreakDownViewData priceBreakdownViewData, final int sum)
   {
      int totalSum = sum;
      if (StringUtils.equalsIgnoreCase(priceBreakdownViewData.getDescription(), TOTAL_PRICE))
      {
         totalSum =
            analyticsHelper.getPriceRoundedValue(priceBreakdownViewData.getPrice().toString());
      }
      return totalSum;
   }

   /**
    * populates TRACS Ancillaries.
    *
    * @param analyticMap the analytic map
    */
   private void populateTuiAnc(final Map<String, WebAnalytics> analyticMap)
   {
      String tuiAnc = NULL;
      final StringBuilder tuiAncComp = new StringBuilder();
      final BasePackage packageModel = getCartPackageModel();
      final List<ExtraFacilityCategory> extraFacilityCategories =
         packageModel.getExtraFacilityCategories();
      tuiAnc =
         constructTuiAncWithExtraFacilities(tuiAncComp, extraFacilityCategories, packageModel
            .getInventory().getInventoryType());
      if (StringUtils.isEmpty(tuiAnc) || tuiAnc == null)
      {
         tuiAnc = NULL;
      }
      analyticMap.put(TUIANC, new WebAnalytics(TUIANC, tuiAnc));
   }

   /**
    * Construct tui anc with extra facilities.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacilityCategories the extra facility categories
    * @param inventoryType
    * @return tuiAnc
    */
   private String constructTuiAncWithExtraFacilities(final StringBuilder tuiAncComp,
      final List<ExtraFacilityCategory> extraFacilityCategories, final InventoryType inventoryType)
   {
      for (final ExtraFacilityCategory extraFacilityCategory : extraFacilityCategories)
      {
         if (StringUtil.equalsIgnoreCase("charity", extraFacilityCategory.getCode()))
         {
            appendWCFToTuiAnc(tuiAncComp, extraFacilityCategory);
         }
         else if (StringUtil.isNotEquals(INSURANCE, extraFacilityCategory.getCode()))
         {
            appendExtraFacilitiesToTuiAnc(tuiAncComp, extraFacilityCategory, inventoryType);
         }
      }
      return removeLastCharacter(tuiAncComp.toString());
   }

   /**
    * Append extra facilities to tui anc.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacilityCategory the extra facility category
    * @param inventoryType
    */
   private void appendExtraFacilitiesToTuiAnc(final StringBuilder tuiAncComp,
      final ExtraFacilityCategory extraFacilityCategory, final InventoryType inventoryType)
   {
      if (isBagAtcom(extraFacilityCategory, inventoryType))
      {
         for (final ExtraFacility extraFacility : extraFacilityCategory.getExtraFacilities())
         {
            constructTuiAncCompBagAtcom(tuiAncComp, (BaggageExtraFacility) extraFacility);
         }
      }
      else
      {
         for (final ExtraFacility extraFacility : extraFacilityCategory.getExtraFacilities())
         {
            constructTuiAncComp(tuiAncComp, extraFacility);
         }
      }
   }

   /**
    * Checks if is bag atcom.
    *
    * @param extraFacilityCategory the extra facility category
    * @param inventoryType the inventory type
    * @return true, if is bag atcom
    */
   private boolean isBagAtcom(final ExtraFacilityCategory extraFacilityCategory,
      final InventoryType inventoryType)
   {
      return StringUtils.equalsIgnoreCase(extraFacilityCategory.getCode(),
         ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE)
         && inventoryType == InventoryType.ATCOM;
   }

   /**
    * Construct tui anc comp bag atcom.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacility the extra facility
    */
   private void constructTuiAncCompBagAtcom(final StringBuilder tuiAncComp,
      final BaggageExtraFacility extraFacility)
   {
      if ((extraFacility.getSelection() == FacilitySelectionType.SELECTABLE)
         && (StringUtil
            .equalsIgnoreCase(extraFacility.getExtraFacilityGroup().toString(), "flight") && !((tuiAncComp
            .toString()).contains(String.valueOf(extraFacility.getBaggageWeight())))))
      {

         tuiAncComp.append("Bag");
         tuiAncComp.append(extraFacility.getBaggageWeight());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(extraFacility.getPassengers().size());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(((extraFacility.getPrices().get(0).getAmount().getAmount())
            .multiply(BigDecimal.valueOf(extraFacility.getPassengers().size()))).setScale(0,
            RoundingMode.HALF_UP));
         tuiAncComp.append(HYPHEN);
      }

   }

   /**
    * Construct tui anc comp.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacility the extra facility
    */
   private void constructTuiAncComp(final StringBuilder tuiAncComp,
      final ExtraFacility extraFacility)
   {
      constructTuiAncCompIfExtraFacilityIsSelectable(tuiAncComp, extraFacility);
   }

   /**
    * Construct tui anc comp if extra facility is selectable.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacility the extra facility
    */
   private void constructTuiAncCompIfExtraFacilityIsSelectable(final StringBuilder tuiAncComp,
      final ExtraFacility extraFacility)
   {
      if (StringUtil.equalsIgnoreCase(extraFacility.getExtraFacilityGroup().toString(), "flight")
         && !((tuiAncComp.toString()).contains(extraFacility.getInventoryCode())))
      {
         tuiAncComp.append(extraFacility.getInventoryCode());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(extraFacility.getPassengers().size());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(((extraFacility.getPrices().get(0).getAmount().getAmount())
            .multiply(BigDecimal.valueOf(extraFacility.getPassengers().size()))).setScale(0,
            RoundingMode.HALF_UP));
         tuiAncComp.append(HYPHEN);
      }
      else if (!(StringUtil.equalsIgnoreCase(extraFacility.getExtraFacilityGroup().toString(),
         "flight")))
      {
         tuiAncComp.append(extraFacility.getInventoryCode());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(extraFacility.getPassengers().size());
         tuiAncComp.append(DELIMITER);

         populatePrice(tuiAncComp, extraFacility.getPrices());
         tuiAncComp.append(HYPHEN);
      }
   }

   /**
    * Populate price.
    *
    * @param tuiAncComp the tui anc comp
    * @param priceModels the price models
    */
   private void populatePrice(final StringBuilder tuiAncComp, final List<Price> priceModels)
   {
      for (final Price price : priceModels)
      {
         if (price.getAmount() != null)
         {
            tuiAncComp.append(price.getAmount().getAmount().setScale(0, RoundingMode.HALF_UP));
            break;
         }
      }
   }

   /**
    * Append wcf to tui anc.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacilityCategory the extra facility category
    */
   private void appendWCFToTuiAnc(final StringBuilder tuiAncComp,
      final ExtraFacilityCategory extraFacilityCategory)
   {
      BigDecimal wcfCost = BigDecimal.ZERO;
      BigDecimal noOfPassenger = BigDecimal.ZERO;
      final BasePackage packageModel = getCartPackageModel();
      for (final ExtraFacility extraFacility : extraFacilityCategory.getExtraFacilities())
      {
         if (packageModel.getInventory().getInventoryType() == InventoryType.ATCOM)
         {
            wcfCost =
               wcfCost.add(extraFacility.getPrices().get(0).getAmount().getAmount()).add(
                  extraFacility.getPrices().get(extraFacility.getPrices().size() - 1).getAmount()
                     .getAmount());
         }
         else
         {
            wcfCost = wcfCost.add(extraFacility.getPrices().get(0).getAmount().getAmount());
         }

         noOfPassenger =
            noOfPassenger.add(BigDecimal.valueOf(extraFacility.getPassengers().size()));
      }
      tuiAncComp.append("WCF");
      tuiAncComp.append(DELIMITER);
      tuiAncComp.append(noOfPassenger);
      tuiAncComp.append(DELIMITER);
      tuiAncComp.append(wcfCost.setScale(0, RoundingMode.HALF_UP));
      tuiAncComp.append(HYPHEN);
   }

   /**
    * remove the end of the character and returns the updated string.
    *
    * @param string the string
    * @return string
    */
   private String removeLastCharacter(final String string)
   {
      if (StringUtils.isNotEmpty(string))
      {
         return StringUtils.substring(string, 0, string.length() - 1);
      }
      return string;
   }

   /**
    * This will append zero if the depDate is lessThan 10.
    *
    * @param depDate the dep date
    * @return depDate
    */
   private String formatDate(final String depDate)
   {
      final int date = Integer.parseInt(depDate);
      if (date < TEN)
      {
         return ZERO + depDate;
      }
      return depDate;
   }

   /**
    * Gets the Month and year from date string eg: mon 17th mar 2014 ----- mar 2014.
    *
    * @param date the date
    * @return date
    */
   private String getMonthAndYear(final String date)
   {
      return date.substring(date.length() - BookFlowConstants.EIGHT, date.length());
   }

   /**
    * Gets the dayDate from the date string eg: mon 17th mar 2014 ----- 17.
    *
    * @param date the date
    * @return the day of the date
    */
   private String getdayOfTheDate(final String date)
   {
      String modifiedDate =
         date.substring(BookFlowConstants.FOUR, date.length() - BookFlowConstants.NINE);
      modifiedDate = modifiedDate.replaceAll("[A-Za-z]", "");
      return modifiedDate;
   }

   /**
    * Gets the cart's package model.
    *
    * @return BasePackage
    */
   private BasePackage getCartPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * Removes the Minus symbol.
    *
    * @param amount the amount
    * @return updatedPaidAmount
    */
   private String removeMinusSymbol(final String amount)
   {
      return amount.replaceAll("-", "");
   }

}
