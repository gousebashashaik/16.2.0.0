/**
 *
 */
package uk.co.tui.cr.book.populators;

import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.BookFlowConstants;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.DepositType;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.PaymentReference;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.PromotionalDiscount;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Schedule;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.inventory.AlternateRoomsService;
import uk.co.tui.cr.book.constants.SessionObjectKeys;
import uk.co.tui.cr.book.view.data.AccommodationViewData;
import uk.co.tui.cr.book.view.data.BookingDetailsViewData;
import uk.co.tui.cr.book.view.data.CabinSltViewData;
import uk.co.tui.cr.book.view.data.ConfirmationViewData;
import uk.co.tui.cr.book.view.data.CruiseOptionsPageViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ExtraOptionsViewData;
import uk.co.tui.cr.book.view.data.FlightOptionsViewData;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.PassengerDetailsViewData;
import uk.co.tui.cr.book.view.data.PriceBreakDownViewData;
import uk.co.tui.cr.book.view.data.RmSltViewData;
import uk.co.tui.cr.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.cr.book.view.data.RoomViewData;
import uk.co.tui.cr.book.view.data.WebAnalyticsALTFLTData;
import uk.co.tui.cr.book.view.data.WebAnalyticsData;

/**
 * The Class BookFlowAnalyticsPopulator.
 *
 * @author srikanth.bs
 *
 *         Analytics Populator Class for FC BookFlow
 */

public class BookFlowAnalyticsPopulator implements Populator<Object, Map<String, WebAnalytics>>
{

   /** Package Analytics Helper . */
   @Resource
   private AnalyticsHelper analyticsHelper;

   /** Package Cart Service . */
   @Resource
   private PackageCartService packageCartService;

   /** session Service. */
   @Resource
   private SessionService sessionService;

   /** The alternate rooms service. */
   @Resource
   private AlternateRoomsService alternateRoomsService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The Constant TOTAL_PRICE. */
   private static final String TOTAL_PRICE = "total price";

   /** Adding Fields To Capture The Filter Details For Analytics. */
   private static final String MONTHYEAR_FORMAT = "MMM yyyy";

   /** The Constant LOGGER. */
   private static final Logger LOGGER = Logger.getLogger(BookFlowAnalyticsPopulator.class);

   /** The Constant for DISCOUNT. */
   private static final String DISCOUNT = "Online Discount";

   /** The Constant for integer TEN. */
   private static final int TEN = 10;

   /** The Constant for zero. */
   private static final String ZERO = "0";

   /** The Constant for one. */
   private static final int ONE = 1;

   /** The Constant for PROMOVALUE. */
   private static final String PROMOVALUE = "PromoValue";

   /** The Constant for PROMOCODE. */
   private static final String PROMOCODE = "PromoCode";

   /** The Constant for PAYTYPE. */
   private static final String PAYTYPE = "PayType";

   /** The Constant for AMOUNT. */
   private static final String AMOUNT = "Amount";

   /** The Constant for TRACSREF. */
   private static final String TRACSREF = "Tracsref";

   /** The Constant for LOWDEP. */
   private static final String LOWDEP = "LowDep";

   /** The Constant for MINPAY. */
   private static final String MINPAY = "MinPay";

   /** The Constant for LIMAVC. */
   private static final String LIMAVC = "LimAvC";

   /** The Constant for DREAMFL. */
   private static final String DREAMFL = "DreamFl";

   /** The Constant for BOARD. */
   private static final String BOARD = "Board";

   /** The Constant for PAX. */
   private static final String PAX = "Pax";

   /** The Constant for CRSHIP. */
   private static final String CRSHIP = "CRShip";

   /** The Constant for DUR. */
   private static final String CRDUR = "CRDur";

   /** The Constant for CRTYPE. */
   private static final String CRTYPE = "CRType";

   /** The Constant for DEPDATE. */
   private static final String DEPDATE = "DepDate";

   /** The Constant for MONTHYEAR. */
   private static final String MONTHYEAR = "MonthYear";

   /** The Constant for DEPAIR. */
   private static final String DEPAIR = "DepAir";

   /** The Constant for CRWHERETO. */
   private static final String CRWHERETO = "CRWhereTo";

   /** The Constant for CRWHERETO. */
   private static final String CRHOTEL = "CRHotel";

   /** The Constant for SUM. */
   private static final String SUM = "Sum";

   /** The Constant for DISC. */
   private static final String DISC = "Disc";

   /** The Constant for PARTY. */
   private static final String PARTY = "Party";

   /** The Constant for TUIANC. */
   private static final String TUIANC = "TuiAnc";

   /** The Constant for PLUS DELIMITER. */
   private static final String DELIMITER = "|";

   /** The Constant for DELIMITER. */
   private static final String PLUS = "+";

   /** The Constant for HYPHEN. */
   private static final String HYPHEN = "-";

   /** The Constant for COMMA. */
   private static final String COMMA = ",";

   /** The Constant for null. */
   private static final String NULL = "null";

   /** Constant for Insurance. */
   private static final String INSURANCE = "INS";

   /** The Constant for CRCABIN. */
   private static final String CRCABIN = "CRCabin";

   /** The Constant for CRANC. */
   private static final String CRANC = "CRAnc";

   /** The Constant for ALTFLT. */
   private static final String ALTFLT = "AltFlt";

   /** The Constant FLTINV. */
   private static final String FLTINV = "FltInv";

   /** The Constant CARRIER. */
   private static final String CARRIER = "carrier";

   /** The Constant for DUR. */
   private static final String STDUR = "STDur";

   /** The Constant for RMSLT. */
   private static final String STR_M_SLT = "STRmSlt";

   /**
    * Analytics populator for Flight Options page.
    *
    * @param source - it is a ViewData object contains all the data related to the book flow page.
    * @param analyticMap -it is target object for the analytics
    */
   @Override
   public void populate(final Object source, final Map<String, WebAnalytics> analyticMap)
   {

      // Cruise options page
      populateAnalyticsForCruiseOptions(source, analyticMap);

      // Flight options
      populateAnalyticsForFlightOptions(source, analyticMap);

      // Hotel options Page
      populateAnalyticsForHotelOptions(source, analyticMap);

      // Extra Options Page
      populateAnalyticsForExtraOptions(source, analyticMap);

      // Passengers Details page
      populateAnalyticsForPassengerDetails(source, analyticMap);

      // Confirmation Page
      populateAnalyticsForConfirmation(source, analyticMap);

   }

   /**
    * Populate analytics for cruise options.
    *
    * @param source the source
    * @param analyticMap the analytic map
    */

   private void populateAnalyticsForCruiseOptions(final Object source,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (source instanceof CruiseOptionsPageViewData)
      {
         final CruiseOptionsPageViewData cruiseOptionsPageViewData =
            (CruiseOptionsPageViewData) source;
         final PackageViewData packageViewData = cruiseOptionsPageViewData.getPackageViewData();
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
         populateCRCabin(analyticMap, packageViewData);
         populateCRAnc(analyticMap);

      }
   }

   /**
    * Populate analytics for Hotel options.
    *
    * @param source the source
    * @param analyticMap the analytic map
    */

   private void populateAnalyticsForHotelOptions(final Object source,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (source instanceof RoomAndBoardOptionsPageViewData)
      {
         final RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData =
            (RoomAndBoardOptionsPageViewData) source;
         final PackageViewData packageViewData =
            roomAndBoardOptionsPageViewData.getPackageViewData();
         populateGenericParams(packageViewData, analyticMap);
         populateCRCabin(analyticMap, packageViewData);
         populateCRAnc(analyticMap);
         populateAltFlt(analyticMap);

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
         populateCRCabin(analyticMap, packageViewData);
         populateCRAnc(analyticMap);
         populateAltFlt(analyticMap);
         populateFltInv(analyticMap);
         populateCarrier(analyticMap);
         populateRmSlt(analyticMap, packageViewData);

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
         populateCRCabin(analyticMap, packageViewData);
         populateCRAnc(analyticMap);
         populateAltFlt(analyticMap);
         populateFltInv(analyticMap);
         populateCarrier(analyticMap);
         populateLowDeposit(analyticMap);
         populateMinPay(analyticMap);
         populateRmSlt(analyticMap, packageViewData);
      }
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
         populateGenericParams(packageViewData, analyticMap);
         populateCRCabin(analyticMap, packageViewData);
         populateCRAnc(analyticMap);
         populateAltFlt(analyticMap);
         populateFltInv(analyticMap);
         populateCarrier(analyticMap);
         populateLowDeposit(analyticMap);
         populateMinPay(analyticMap);
         populatePromotionalDiscount(analyticMap);
         populateTracsref(analyticMap, confirmationViewData.getBookingDetailsViewData());
         populateAmount(analyticMap, confirmationViewData.getBookingDetailsViewData());
         populatePayType(analyticMap);
         populateRmSlt(analyticMap, packageViewData);
      }
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
         populateCRWhereTo(analyticMap);
         populateAirport(analyticMap, packageViewData);
         populateDepartureDate(analyticMap, packageViewData);
         populatePartyComposition(analyticMap, packageViewData);
         populateDuration(analyticMap, packageViewData);
         populateCruiseShip(analyticMap, packageViewData);
         populateLimAvC(analyticMap);
         populateDreamFI(analyticMap);
         populatePax(analyticMap, packageViewData);
         populateBoard(analyticMap);
         populatePrice(analyticMap, packageViewData);
         populateTuiAnc(analyticMap, packageViewData);
         populateCRType(analyticMap, packageViewData);
         populateStayDuration(analyticMap);
         populateCRHotel(analyticMap, packageViewData);

      }
   }

   /**
    * @param analyticMap
    */
   private void populateCRHotel(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {

      String hotelCode = NULL;

      final BasePackage packageModel = packageCartService.getBasePackage();
      final Stay stay = packageComponentService.getStay(packageModel);
      if (stay.getType() != StayType.SHIP)
      {
         final int indexVal = generateHotelIndex(packageModel);
         if (StringUtils
            .isNotEmpty(packageViewData.getAccomViewData().get(indexVal).getAccomCode()))
         {
            hotelCode = packageViewData.getAccomViewData().get(indexVal).getAccomCode();
         }
         hotelCode = "H" + hotelCode;
      }
      final WebAnalytics webAnalyticHotelCode = new WebAnalytics(CRHOTEL, hotelCode);
      analyticMap.put(CRHOTEL, webAnalyticHotelCode);

   }

   /**
    * Duration population for Analytics numerical value of the number of nights for the package
    * selected.
    *
    * @param analyticMap the analytic map
    *
    */
   private void populateStayDuration(final Map<String, WebAnalytics> analyticMap)

   {
      String stayDuration = NULL;
      final BasePackage packageModel = getCartPackageModel();
      final Stay stay = packageComponentService.getStay(packageModel);
      if (stay.getType() != StayType.SHIP)
      {

         stayDuration = String.valueOf(stay.getDuration());

      }
      final WebAnalytics webAnalyticDur = new WebAnalytics(STDUR, stayDuration);
      analyticMap.put(STDUR, webAnalyticDur);
   }

   /**
    * Get the details on what bases alternative flight has be selected. Date or Duration or Dep Air
    *
    * @param analyticMap the analytic map
    */
   private void populateAltFlt(final Map<String, WebAnalytics> analyticMap)
   {
      String altFlt = NULL;
      final WebAnalyticsData webAnalyticsData =
         sessionService.getAttribute(SessionObjectKeys.WEBANALYTICS);
      if (SyntacticSugar.isNotNull(webAnalyticsData)
         && SyntacticSugar.isNotNull(webAnalyticsData.getWebAnalyticsALTFLTData()))
      {
         altFlt = constructAltFlt(webAnalyticsData);
      }

      if (StringUtils.isEmpty(altFlt) || altFlt == null)
      {
         altFlt = NULL;
      }
      analyticMap.put(ALTFLT, new WebAnalytics(ALTFLT, altFlt));
   }

   /**
    * Construct alt flt.
    *
    * @param webAnalyticsData the web analytics data
    * @return altFlt
    */
   private String constructAltFlt(final WebAnalyticsData webAnalyticsData)
   {
      String altFlt;
      final StringBuilder altFltComp = new StringBuilder();
      final WebAnalyticsALTFLTData webAnalyticsALTFLTData =
         webAnalyticsData.getWebAnalyticsALTFLTData();
      final BasePackage packageModel = getCartPackageModel();
      final Itinerary itinerary = packageComponentService.getFlightItinerary(packageModel);
      final Leg outBound = itinerary.getOutBound().get(0);
      final Schedule flightSchedule = outBound.getSchedule();
      final String presentFlightDepDate =
         DateUtils.getDateInStringFormat(flightSchedule.getDepartureDate());
      if (!StringUtils.equalsIgnoreCase(presentFlightDepDate, webAnalyticsALTFLTData.getDate()))
      {
         altFltComp.append("DATE");
         altFltComp.append(DELIMITER);
      }
      final String pastFlightDuration = String.valueOf(packageModel.getDuration());
      if (!StringUtils.equalsIgnoreCase(pastFlightDuration, webAnalyticsALTFLTData.getDuration()))
      {
         altFltComp.append("DURATION");
         altFltComp.append(DELIMITER);
      }
      if (!StringUtils.equalsIgnoreCase(outBound.getDepartureAirport().getCode(),
         webAnalyticsALTFLTData.getDepAir()))
      {
         altFltComp.append("DEP AIR");
         altFltComp.append(DELIMITER);
      }
      altFlt = removeLastCharacter(altFltComp.toString());
      return altFlt;
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
    * The value of the parameter will be ‘Yes’ or ‘No’.
    *
    * @param analyticMap the analytic map
    */
   private void populateLowDeposit(final Map<String, WebAnalytics> analyticMap)
   {

      String lowDep = "No";

      final BasePackage packageModel = getCartPackageModel();
      if (CollectionUtils.isNotEmpty(packageModel.getDeposits()))
      {
         for (final uk.co.tui.book.domain.lite.Deposit deposit : packageModel.getDeposits())
         {
            if (isLowDepositAvailable(deposit))
            {
               lowDep = "Yes";
            }
         }
      }
      analyticMap.put(LOWDEP, new WebAnalytics(LOWDEP, lowDep));

   }

   /**
    * Checks if is low deposit available.
    *
    * @param deposit the deposit
    * @return true, if is low deposit available
    */
   private boolean isLowDepositAvailable(final uk.co.tui.book.domain.lite.Deposit deposit)
   {
      return deposit.getDepositType() == DepositType.LOW_DEPOSIT
         && BooleanUtils.toBoolean(deposit.getApplicable());
   }

   /**
    * Populate min pay.
    *
    * @param analyticMap the analytic map
    */
   private void populateMinPay(final Map<String, WebAnalytics> analyticMap)
   {
      int minPay = 0;
      boolean lowDepositAvailable = false;
      final BasePackage packageModel = getCartPackageModel();
      if (CollectionUtils.isNotEmpty(packageModel.getDeposits()))
      {
         for (final uk.co.tui.book.domain.lite.Deposit deposit : packageModel.getDeposits())
         {
            lowDepositAvailable =
               populateifLowDepositAvailable(analyticMap, deposit, lowDepositAvailable);
            populateifStandardDepositAvailable(analyticMap, deposit, lowDepositAvailable);
         }
      }
      else
      {
         minPay =
            analyticsHelper.getPriceRoundedValue(packageModel.getPrice().getAmount().getAmount()
               .toString());
         analyticMap.put(MINPAY, new WebAnalytics(MINPAY, String.valueOf(minPay)));
      }
   }

   /**
    * Populateif standard deposit available.
    *
    * @param analyticMap the analytic map
    * @param deposit the deposit
    * @param lowDepositAvailable the low deposit available
    * @return true if standard deposit is there
    */
   private boolean populateifStandardDepositAvailable(final Map<String, WebAnalytics> analyticMap,
      final uk.co.tui.book.domain.lite.Deposit deposit, final boolean lowDepositAvailable)
   {
      int minPay = 0;
      if (checkStandardDepositIfLowDepositNotAvailable(lowDepositAvailable, deposit))
      {
         minPay =
            analyticsHelper.getPriceRoundedValue(deposit.getDepositAmount().getAmount().getAmount()
               .toString());
         analyticMap.put(MINPAY, new WebAnalytics(MINPAY, String.valueOf(minPay)));
         return true;
      }
      return false;
   }

   /**
    * Populateif low deposit available.
    *
    * @param analyticMap the analytic map
    * @param deposit the deposit
    * @param lowDepositAvailable the low deposit available
    * @return true, if successful
    */
   private boolean populateifLowDepositAvailable(final Map<String, WebAnalytics> analyticMap,
      final uk.co.tui.book.domain.lite.Deposit deposit, final boolean lowDepositAvailable)
   {
      int minPay = 0;
      if (lowDepositAvailable)
      {
         return true;
      }
      else if (isLowDepositAvailable(deposit))
      {
         minPay =
            analyticsHelper.getPriceRoundedValue(deposit.getDepositAmount().getAmount().getAmount()
               .toString());
         analyticMap.put(MINPAY, new WebAnalytics(MINPAY, String.valueOf(minPay)));
         return true;
      }
      return false;

   }

   /**
    * Check standard deposit if low deposit not available.
    *
    * @param lowDepositAvailable the low deposit available
    * @param deposit the deposit
    * @return true, if successful
    */
   private boolean checkStandardDepositIfLowDepositNotAvailable(final boolean lowDepositAvailable,
      final uk.co.tui.book.domain.lite.Deposit deposit)
   {
      return !lowDepositAvailable && isStandardDepositAvailable(deposit);
   }

   /**
    * Checks if is standard deposit available.
    *
    * @param deposit the deposit
    * @return isStandardDepositAvailable i.e. true or false
    */
   private boolean isStandardDepositAvailable(final uk.co.tui.book.domain.lite.Deposit deposit)
   {
      return deposit.getDepositType() == DepositType.STANDARD_DEPOSIT
         && BooleanUtils.toBoolean(deposit.getApplicable());
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
    * Removes the Minus symbol.
    *
    * @param amount the amount
    * @return updatedPaidAmount
    */
   private String removeMinusSymbol(final String amount)
   {
      return amount.replaceAll("-", "");
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

   // -- ************************ Populate Cabin Start
   // *******************************

   /**
    * Room selection.
    *
    * @param analyticMap the analytic map
    * @param packageViewData the package view data
    */
   private void populateCRCabin(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      String cabinSlt;
      final StringBuilder cabinSltComp = new StringBuilder();
      final WebAnalyticsData webAnalyticsData =
         sessionService.getAttribute(SessionObjectKeys.WEBANALYTICS);
      final List<CabinSltViewData> defaultCabins = webAnalyticsData.getPackageModelCabins();
      if (CollectionUtils.isNotEmpty(defaultCabins))
      {
         getCabinDetails(packageViewData, cabinSltComp, defaultCabins);
      }

      cabinSlt = cabinSltComp.toString();
      if (StringUtils.isEmpty(cabinSlt) || cabinSlt == null)
      {
         cabinSlt = NULL;
      }
      analyticMap.put(CRCABIN, new WebAnalytics(CRCABIN, cabinSlt));
   }

   private void getCabinDetails(final PackageViewData packageViewData,
      final StringBuilder cabinSltComp, final List<CabinSltViewData> defaultCabins)
   {
      int count = 0;
      final StringBuilder cabinRoomDesc = new StringBuilder();
      final StringBuilder cabinRoomPrice = new StringBuilder();

      final BasePackage packageModel = packageCartService.getBasePackage();
      final int indexVal = generateCruiseIndex(packageModel);

      for (final RoomViewData roomViewData : packageViewData.getAccomViewData().get(indexVal)
         .getRoomViewData())
      {
         boolean isPresentInPackageModel = false;
         isPresentInPackageModel =
            isDefaultCabinAndPresentCabinAreSame(defaultCabins, roomViewData);
         count = count + 1;
         final BigDecimal defaultRoomPrice = BigDecimal.ZERO;

         constructCabinDescAndPriceIndependetly(roomViewData, isPresentInPackageModel,
            defaultRoomPrice, cabinRoomDesc, cabinRoomPrice);

      }

      constructCabinslt(cabinSltComp, cabinRoomDesc, cabinRoomPrice);

   }

   private void constructCabinslt(final StringBuilder cabinSltComp,
      final StringBuilder cabinRoomDesc, final StringBuilder cabinRoomPrice)
   {
      final String[] roomsDesc = cabinRoomDesc.toString().split("\\|");
      final String[] roomsPrice = cabinRoomPrice.toString().split("\\|");

      if (areMoreThanTwoRooms(roomsDesc, roomsPrice))
      {
         cabinSltComp.append(removeLastCharacter(cabinRoomDesc.toString()));
         cabinSltComp.append(HYPHEN);
         cabinSltComp.append(removeLastCharacter(cabinRoomPrice.toString()));
      }
      else
      {
         // The hash code would be printed in case there is no
         // description(meaning no content)
         cabinSltComp.append(cabinRoomDesc.toString());
         if (!StringUtils.endsWith(cabinSltComp.toString(), DELIMITER))
         {
            cabinSltComp.append(DELIMITER);
         }
         cabinSltComp.append(cabinRoomPrice.toString());
      }
   }

   /**
    * check more than two rooms are selected or not.
    *
    * @param roomsDesc the rooms desc
    * @param roomsPrice the rooms price
    * @return true, if successful
    */
   private boolean areMoreThanTwoRooms(final String[] roomsDesc, final String[] roomsPrice)
   {
      final int one = 1;
      return roomsDesc.length > one && roomsPrice.length > one;
   }

   private void constructCabinDescAndPriceIndependetly(final RoomViewData roomViewData,
      final boolean isPresentInPackageModel, final BigDecimal defaultRoomPrice,
      final StringBuilder rmlSltRoomDesc, final StringBuilder rmlSltRoomPrice)
   {
      if (!isPresentInPackageModel)
      {
         rmlSltRoomDesc.append(roomViewData.getDescription());
         rmlSltRoomDesc.append(DELIMITER);
         LOGGER.info("Web Analytics: CRCABIN: Previous cabin price is:" + defaultRoomPrice
            + " And present cabin price is:" + roomViewData.getPrice());
         LOGGER.info("Total change in the cabin price is :" + roomViewData.getPrice());
         rmlSltRoomPrice.append(roomViewData.getPrice().setScale(0, RoundingMode.HALF_UP));
         rmlSltRoomPrice.append(DELIMITER);
      }
      else
      {
         rmlSltRoomDesc.append(roomViewData.getDescription());
         rmlSltRoomDesc.append(DELIMITER);
         rmlSltRoomPrice.append(0);
         rmlSltRoomPrice.append(DELIMITER);
      }

   }

   /**
    * Checks if is default room and present room are same.
    *
    * @param defaultCabins the default rooms
    * @param roomViewData the room view data
    * @return isPresentInPackageModel
    */
   private boolean isDefaultCabinAndPresentCabinAreSame(final List<CabinSltViewData> defaultCabins,
      final RoomViewData roomViewData)
   {
      boolean isPresentInPackageModel = false;
      for (final CabinSltViewData defaultCabin : defaultCabins)
      {
         if (StringUtils.equalsIgnoreCase(defaultCabin.getRoomCode(), roomViewData.getRoomCode()))
         {
            isPresentInPackageModel = true;
         }
      }
      return isPresentInPackageModel;
   }

   // -- ************************ Populate Cabin End
   // *******************************

   /**
    * populates Limited Availability Indicator Parameter - Seats Limited Availability Indicator on
    * it then It will populate with the numerical value that references the number of seats left and
    * not the number of rooms no indicator then populate ‘null’.
    *
    * @param analyticMap the analytic map
    */
   @SuppressWarnings("boxing")
   private void populateLimAvC(final Map<String, WebAnalytics> analyticMap)
   {
      String limAvC = null;
      final StringBuilder limAvCComp = new StringBuilder();
      final BasePackage packageModel = getCartPackageModel();
      if (SyntacticSugar.isNotNull(packageComponentService.getCruise(packageModel)))
      {
         limAvC = getAccomodationFromPackageAndConstructLimAvC(limAvCComp, packageModel);
      }

      if (StringUtils.isEmpty(limAvC) || limAvC == null)
      {
         limAvC = NULL;
      }
      analyticMap.put(LIMAVC, new WebAnalytics(LIMAVC, limAvC));
   }

   /**
    * Gets the accomodation from package and construct lim av c.
    *
    * @param limAvCComp the lim av b comp
    * @param packageModel the package model
    * @return limAvC
    */
   private String getAccomodationFromPackageAndConstructLimAvC(final StringBuilder limAvCComp,
      final BasePackage packageModel)
   {
      String limAvC = null;
      final Stay availableAccom = packageComponentService.getCruise(packageModel);
      if (SyntacticSugar.isNotNull(availableAccom.getRooms()))
      {
         constructLimAvC(limAvCComp, availableAccom);
         limAvC = removeLastCharacter(limAvCComp.toString());
      }
      return limAvC;
   }

   /**
    * Construct lim av c.
    *
    * @param limAvCComp the lim av c comp
    * @param availableAccom the available accom
    */
   private void constructLimAvC(final StringBuilder limAvCComp, final Stay availableAccom)
   {
      boolean indicator;
      final int limAvcIndicator = alternateRoomsService.getLimitedAvailabilityThreshold();
      for (final Room selectedRoom : availableAccom.getRooms())
      {
         indicator =
            alternateRoomsService.isLimitedRooms(selectedRoom.getQuantity().intValue(),
               limAvcIndicator);
         if (indicator)
         {
            limAvCComp.append(selectedRoom.getQuantity());
            limAvCComp.append(DELIMITER);
         }
         else
         {
            limAvCComp.append(NULL);
            limAvCComp.append(DELIMITER);
         }
      }
   }

   /**
    * This will populate either ‘Yes’ or ‘No’ depending on whether the package selected has
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
    * Populate package type.
    *
    * @param analyticMap the analytic map
    */
   private void populateCRType(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {

      String inboundDepAirportCode = null;
      String outboundArrAirportCode = null;
      String crType = null;

      final BasePackage packageModel = getCartPackageModel();

      if (CollectionUtils
         .isNotEmpty(packageViewData.getFlightViewData().get(0).getInboundSectors()))
      {
         final List<uk.co.tui.cr.book.view.data.Leg> inbounds =
            packageViewData.getFlightViewData().get(0).getInboundSectors();
         inboundDepAirportCode = (inbounds.get(0)).getDepartureAirport().getCode();
      }

      if (CollectionUtils.isNotEmpty(packageViewData.getFlightViewData().get(0)
         .getOutboundSectors()))
      {
         final List<uk.co.tui.cr.book.view.data.Leg> outbounds =
            packageViewData.getFlightViewData().get(0).getOutboundSectors();
         outboundArrAirportCode = (outbounds.get(0)).getArrivalAirport().getCode();
      }

      crType = populateCRType(inboundDepAirportCode, outboundArrAirportCode, packageModel);

      analyticMap.put(CRTYPE, new WebAnalytics(CRTYPE, crType));
   }

   private String populateCRType(final String inboundDepAirportCode,
      final String outboundArrAirportCode, final BasePackage packageModel)
   {
      String crType;
      if (inboundDepAirportCode.equalsIgnoreCase(outboundArrAirportCode))
      {
         crType = packageModel.getPackageType().getDescription();
      }
      else if (packageModel.getPackageType().getDescription().equalsIgnoreCase("BackToBack"))
      {
         crType = "BackToBack_Repositional";
      }
      else
      {
         crType = "Repositional";
      }
      return crType;
   }

   /**
    * Populate board.
    *
    * @param analyticMap the analytic map
    */
   private void populateBoard(final Map<String, WebAnalytics> analyticMap)
   {

      final BasePackage packageModel = getCartPackageModel();
      String stayBoardData = null;
      String cruiseBoardData = null;
      final StringBuilder boardValue = new StringBuilder();
      final List<Stay> stayList = packageComponentService.getAllStays(packageModel);
      for (final Stay stay : stayList)
      {
         if (stay.getType() == StayType.SHIP)
         {
            cruiseBoardData = obtainBoardBasisCode(stay);
         }
         else
         {
            stayBoardData = obtainBoardBasisCode(stay);
         }
      }
      getBoardBasisCodeValue(stayBoardData, cruiseBoardData, boardValue, stayList, packageModel);

      analyticMap.put(BOARD, new WebAnalytics(BOARD, boardValue.toString()));
   }

   /**
    * @param stayBoardData
    * @param cruiseBoardData
    * @param boardValue
    * @param stayList
    */
   private void getBoardBasisCodeValue(final String stayBoardData, final String cruiseBoardData,
      final StringBuilder boardValue, final List<Stay> stayList, final BasePackage packageModel)
   {
      if (packageModel.getPackageType() == PackageType.BTB)
      {
         boardValue.append(cruiseBoardData);
         boardValue.append(DELIMITER);
         boardValue.append(cruiseBoardData);
      }
      else if (stayList.size() > 1)
      {
         boardValue.append(cruiseBoardData);
         boardValue.append(DELIMITER);
         boardValue.append(stayBoardData);
      }
      else
      {
         boardValue.append(cruiseBoardData);
      }
   }

   /**
    * Obtain board basis code.
    *
    * @param stay the stay
    * @return the string
    */
   private String obtainBoardBasisCode(final Stay stay)
   {
      String board = NULL;
      final Collection<Room> selectedRooms = stay.getRooms();
      for (final Room room : selectedRooms)
      {
         if (room.getBoardBasis() != null)
         {
            board = room.getBoardBasis().getCode();
         }
      }
      return board;
   }

   /**
    * The value passed will be the numerical value of how many passengers are in the package. e.g.
    * if the packages selected was for 2 adults, 1 child and 0 infants the value would be '3' note:
    * MUST exclude infants
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
    * Captures the Cruise Ship on the package selected by the customer.
    *
    * @param analyticMap the analytic map
    * @param packageViewData the package view data
    */
   private void populateCruiseShip(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      final BasePackage packageModel = getCartPackageModel();
      final int indexval = generateCruiseIndex(packageModel);

      final WebAnalytics webAnalyticDur =
         new WebAnalytics(CRSHIP, String.valueOf(packageViewData.getAccomViewData().get(indexval)
            .getDestinationName()));
      analyticMap.put(CRSHIP, webAnalyticDur);
   }

   /**
    * @param packageModel
    * @return indexval
    */
   private int generateCruiseIndex(final BasePackage packageModel)
   {

      return packageComponentService.getAllStays(packageModel).indexOf(
         packageComponentService.getCruise(packageModel));
   }

   /**
    * Duration population for Analytics numerical value of the number of nights for the package
    * selected.
    *
    * @param analyticMap the analytic map
    *
    */
   private void populateDuration(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      final BasePackage packageModel = getCartPackageModel();

      final int indexval = generateCruiseIndex(packageModel);
      String duration =
         String.valueOf(packageViewData.getAccomViewData().get(indexval).getDuration());
      final List<Stay> cruises = packageComponentService.getCruises(packageModel);
      if (CollectionUtils.isNotEmpty(cruises))
      {
         duration =
            String.valueOf(cruises.get(indexval).getDuration() + PLUS
               + cruises.get(ONE).getDuration());
      }
      final WebAnalytics webAnalyticDur = new WebAnalytics(CRDUR, duration);
      analyticMap.put(CRDUR, webAnalyticDur);
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
         final List<uk.co.tui.cr.book.view.data.Leg> outbounds =
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
         final List<uk.co.tui.cr.book.view.data.Leg> outbounds =
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
   public void populateCRWhereTo(final Map<String, WebAnalytics> analyticMap)
   {
      String journeyCode = NULL;

      final BasePackage packageModel = getCartPackageModel();
      if (packageModel.getPackageType() == PackageType.BTB)
      {
         final StringBuilder journeyCodes = new StringBuilder();
         final List<Stay> cruises = packageComponentService.getCruises(packageModel);
         for (final Stay cruise : cruises)
         {
            journeyCodes.append("C").append(((Cruise) cruise).getJourneyCode());
            if (cruises.indexOf(cruise) < (cruises.size() - ONE))
            {
               journeyCodes.append(DELIMITER);
            }
         }

         final WebAnalytics webAnalyticWhereTo =
            new WebAnalytics(CRWHERETO, journeyCodes.toString());
         analyticMap.put(CRWHERETO, webAnalyticWhereTo);
      }
      else
      {
         journeyCode = ((Cruise) packageComponentService.getCruise(packageModel)).getJourneyCode();
         final WebAnalytics webAnalyticWhereTo = new WebAnalytics(CRWHERETO, "C" + journeyCode);
         analyticMap.put(CRWHERETO, webAnalyticWhereTo);
      }
   }

   /**
    * Population of Price Data for Analytics. The value is to be rounded either up or down such that
    * we have no decimal places. If the decimal value is .50 please round this up. The value passed
    * is to have no ‘£’ sign or decimal places
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
    * Party Composition for rooms population for Analytics.
    *
    * @param analyticMap the analytic map
    * @param packageViewData Party=R1-A2/C0/I0,R2-A2/C1/I0
    */
   private void populatePartyComposition(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {

      int count = 0;
      String party = NULL;
      final StringBuilder partyComp = new StringBuilder();
      final BasePackage packageModel = getCartPackageModel();
      final int indexval = generateCruiseIndex(packageModel);
      final AccommodationViewData accommodationViewData =
         packageViewData.getAccomViewData().get(indexval);
      if (accommodationViewData.getRoomViewData() != null)
      {
         for (final RoomViewData room : accommodationViewData.getRoomViewData())
         {
            count++;
            partyComp.append("R");
            partyComp.append(count);
            partyComp.append(HYPHEN);
            partyComp.append("A");
            partyComp.append(room.getNoOfAdults());
            partyComp.append("/");
            partyComp.append("C");
            partyComp.append(room.getNoOfChildren());
            partyComp.append("/");
            partyComp.append("I");
            partyComp.append(room.getNoOfInfants());
            partyComp.append(COMMA);
         }
         party = removeLastCharacter(partyComp.toString());
      }
      final WebAnalytics webAnalyticParty = new WebAnalytics(PARTY, party);
      analyticMap.put(PARTY, webAnalyticParty);
   }

   // ************************ START of CRANC
   // *******************************************

   /**
    * Populates Cruise Ancillaries - Latest
    *
    * */
   private void populateCRAnc(final Map<String, WebAnalytics> analyticMap)
   {
      String crAnc = NULL;
      final StringBuilder crAncComp = new StringBuilder();
      final BasePackage packageModel = getCartPackageModel();
      final List<ExtraFacilityCategory> extraFacilityCategories =
         packageModel.getExtraFacilityCategories();
      crAnc =
         constructCRAncWithExtraFacilities(crAncComp, extraFacilityCategories, packageModel
            .getInventory().getInventoryType());
      if (StringUtils.isEmpty(crAnc) || crAnc == null)
      {
         crAnc = NULL;
      }
      analyticMap.put(CRANC, new WebAnalytics(CRANC, crAnc));
   }

   private String constructCRAncWithExtraFacilities(final StringBuilder crAncComp,
      final List<ExtraFacilityCategory> extraFacilityCategories, final InventoryType inventoryType)
   {
      for (final ExtraFacilityCategory extraFacilityCategory : extraFacilityCategories)
      {
         appendExtraFacilitiesToCRAnc(crAncComp, extraFacilityCategory, inventoryType);
      }
      return removeLastCharacter(crAncComp.toString());
   }

   private void appendExtraFacilitiesToCRAnc(final StringBuilder crAncComp,
      final ExtraFacilityCategory extraFacilityCategory, final InventoryType inventoryType)
   {
      if (inventoryType == InventoryType.ATCOM)
      {
         for (final ExtraFacility extraFacility : extraFacilityCategory.getExtraFacilities())
         {
            constructCRAncComp(crAncComp, extraFacility);
         }
      }

   }

   private void constructCRAncComp(final StringBuilder crAncComp, final ExtraFacility extraFacility)
   {
      if (extraFacility.getSelection() == FacilitySelectionType.SELECTABLE)
      {
         constructCRAncCompIfExtraFacilityIsSelectable(crAncComp, extraFacility);

      }
   }

   private void constructCRAncCompIfExtraFacilityIsSelectable(final StringBuilder crAncComp,
      final ExtraFacility extraFacility)
   {
      if (StringUtils.equalsIgnoreCase(extraFacility.getExtraFacilityGroup().toString(), "cruise"))
      {
         crAncComp.append(extraFacility.getInventoryCode());
         crAncComp.append(DELIMITER);
         crAncComp.append(extraFacility.getPassengers().size());
         crAncComp.append(DELIMITER);
         crAncComp.append((extraFacility.getPrices().get(0).getAmount().getAmount()).setScale(0,
            RoundingMode.HALF_UP));
         crAncComp.append(HYPHEN);
      }
   }

   // ************************ END of CRANC
   // *******************************************

   /**
    * populates TRACS Ancillaries.
    *
    * @param analyticMap the analytic map
    */
   private void populateTuiAnc(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      String tuiAnc = NULL;
      final StringBuilder tuiAncComp = new StringBuilder();
      final BasePackage packageModel = getCartPackageModel();
      final List<ExtraFacilityCategory> extraFacilityCategories =
         packageModel.getExtraFacilityCategories();
      tuiAnc =
         constructTuiAncWithExtraFacilities(tuiAncComp, extraFacilityCategories, packageModel
            .getInventory().getInventoryType(), packageViewData);
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
      final List<ExtraFacilityCategory> extraFacilityCategories, final InventoryType inventoryType,
      final PackageViewData packageViewData)
   {
      for (final ExtraFacilityCategory extraFacilityCategory : extraFacilityCategories)
      {
         constructTuiAncWithExtras(tuiAncComp, inventoryType, packageViewData,
            extraFacilityCategory);
      }
      return removeLastCharacter(tuiAncComp.toString());
   }

   private void constructTuiAncWithExtras(final StringBuilder tuiAncComp,
      final InventoryType inventoryType, final PackageViewData packageViewData,
      final ExtraFacilityCategory extraFacilityCategory)
   {
      if (StringUtils.equalsIgnoreCase("charity", extraFacilityCategory.getCode()))
      {
         appendWCFToTuiAnc(tuiAncComp, extraFacilityCategory);
      }
      else if (StringUtils.equalsIgnoreCase("seat", extraFacilityCategory.getCode()))
      {
         appendSeatExtraFacilitiesToTuiAnc(tuiAncComp, extraFacilityCategory, inventoryType,
            packageViewData);
      }
      else if (StringUtil.isNotEquals(INSURANCE, extraFacilityCategory.getCode()))
      {
         appendExtraFacilitiesToTuiAnc(tuiAncComp, extraFacilityCategory, inventoryType,
            packageViewData);
      }
   }

   /**
    * Append extra facilities to tui anc.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacilityCategory the extra facility category
    * @param inventoryType
    */
   private void appendExtraFacilitiesToTuiAnc(final StringBuilder tuiAncComp,
      final ExtraFacilityCategory extraFacilityCategory, final InventoryType inventoryType,
      final PackageViewData packageViewData)
   {

      if (StringUtils.equalsIgnoreCase(extraFacilityCategory.getExtraFacilityGroup().toString(),
         "flight"))
      {
         appenedExtraFacilities(tuiAncComp, extraFacilityCategory, inventoryType, packageViewData);
      }
      else
      {
         for (final ExtraFacility extraFacility : extraFacilityCategory.getExtraFacilities())
         {
            constructTuiAncComp(tuiAncComp, extraFacility);
         }
      }
   }

   private void appenedExtraFacilities(final StringBuilder tuiAncComp,
      final ExtraFacilityCategory extraFacilityCategory, final InventoryType inventoryType,
      final PackageViewData packageViewData)
   {
      // Checks if its a Baggage Extra or Seat Extra
      if ((isBagAtcom(extraFacilityCategory, inventoryType))
         && (CollectionUtils.isNotEmpty(packageViewData.getExtraFacilityCategoryViewData())))
      {
         for (final ExtraFacilityCategoryViewData extraFacilityCategoryViewData : packageViewData
            .getExtraFacilityCategoryViewData())
         {
            constructBagExtras(extraFacilityCategoryViewData, tuiAncComp);
         }
      }
   }

   private void constructBagExtras(
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final StringBuilder tuiAncComp)
   {
      if (isSeatOrBag(extraFacilityCategoryViewData,
         ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
      {
         for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
            .getExtraFacilityViewData())
         {
            constructTuiAncCompBagAtcom1(tuiAncComp, extraFacilityViewData);
         }
      }
   }

   private void appendSeatExtraFacilitiesToTuiAnc(final StringBuilder tuiAncComp,
      final ExtraFacilityCategory extraFacilityCategory, final InventoryType inventoryType,
      final PackageViewData packageViewData)
   {
      if ((isSeatAtcom(extraFacilityCategory, inventoryType))
         && (CollectionUtils.isNotEmpty(packageViewData.getExtraFacilityCategoryViewData())))
      {
         for (final ExtraFacilityCategoryViewData extraFacilityCategoryViewData : packageViewData
            .getExtraFacilityCategoryViewData())
         {
            constructSeatExtras(extraFacilityCategoryViewData, extraFacilityCategory, tuiAncComp);
         }
      }
   }

   private void constructSeatExtras(
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityCategory extraFacilityCategory, final StringBuilder tuiAncComp)
   {
      if (isSeatOrBag(extraFacilityCategoryViewData,
         ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
      {
         for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
            .getExtraFacilityViewData())
         {
            final ExtraFacility extraFacility = extraFacilityCategory.getExtraFacilities().get(0);
            constructTuiAncForSeats(tuiAncComp, extraFacilityViewData, extraFacility);
         }
      }
   }

   /**
    * Construct isSeatOrBag check
    *
    * @param extraFacilityCategoryViewData the extraFacilityCategoryViewData
    * @param constantCode the constantCode
    */

   private boolean isSeatOrBag(final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final String constantCode)
   {
      if ("seat".equalsIgnoreCase(constantCode))
      {
         return StringUtils.equalsIgnoreCase(
            extraFacilityCategoryViewData.getExtraFacilityCategoryCode(),
            ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE);
      }
      else if ("bag".equalsIgnoreCase(constantCode))
      {
         return StringUtils.equalsIgnoreCase(
            extraFacilityCategoryViewData.getExtraFacilityCategoryCode(),
            ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE);
      }
      else
      {
         return false;
      }
   }

   /* Extra Updation */
   /**
    * Construct tui anc comp bag atcom.
    *
    * @param tuiAncComp the tui anc comp
    * @param extraFacility the extra facility
    */
   private void constructTuiAncCompBagAtcom1(final StringBuilder tuiAncComp,
      final ExtraFacilityViewData extraFacility)
   {
      final String bagcheck = "BAG" + extraFacility.getWeightCode();
      if (StringUtils.equalsIgnoreCase(extraFacility.getSelection().toString(), "Selectable")
         && (StringUtils.equalsIgnoreCase(extraFacility.getGroupCode().toString(), "flight"))
         && !((tuiAncComp.toString()).contains(bagcheck)))
      {
         tuiAncComp.append("Bag");
         tuiAncComp.append(extraFacility.getWeightCode());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(extraFacility.getPassengerExtraFacilityMapping().getPassengers().size());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(((extraFacility.getPrice()).multiply(BigDecimal.valueOf(extraFacility
            .getPassengerExtraFacilityMapping().getPassengers().size()))).setScale(0,
            RoundingMode.HALF_UP));
         tuiAncComp.append(HYPHEN);
      }
   }

   /* For Seats */
   private void constructTuiAncForSeats(final StringBuilder tuiAncComp,
      final ExtraFacilityViewData extraFacilityViewData, final ExtraFacility extraFacility)
   {
      if (StringUtils.equalsIgnoreCase(extraFacilityViewData.getGroupCode().toString(), "flight")
         && !((tuiAncComp.toString()).contains(extraFacility.getInventoryCode())))
      {
         tuiAncComp.append(extraFacility.getInventoryCode());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append(extraFacilityViewData.getPassengerExtraFacilityMapping().getPassengers()
            .size());
         tuiAncComp.append(DELIMITER);
         tuiAncComp.append((extraFacilityViewData.getPrice()).setScale(0, RoundingMode.HALF_UP));
         tuiAncComp.append(HYPHEN);
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

   private boolean isSeatAtcom(final ExtraFacilityCategory extraFacilityCategory,
      final InventoryType inventoryType)
   {
      return StringUtils.equalsIgnoreCase(extraFacilityCategory.getCode(),
         ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE) && inventoryType == InventoryType.ATCOM;
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
      if (StringUtils.equalsIgnoreCase(extraFacility.getExtraFacilityGroup().toString(), "package"))
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
    * Room selection.
    *
    * @param analyticMap the analytic map
    * @param packageViewData the package view data
    */
   private void populateRmSlt(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      final StringBuilder rmSltComp = new StringBuilder();
      final WebAnalyticsData webAnalyticsData =
         sessionService.getAttribute(SessionObjectKeys.WEBANALYTICS);

      if (SyntacticSugar.isNotNull(webAnalyticsData)
         && CollectionUtils.isNotEmpty(webAnalyticsData.getPackageModelRooms()))
      {
         final List<RmSltViewData> defaultRooms = webAnalyticsData.getPackageModelRooms();
         getRmsltDetails(packageViewData, rmSltComp, defaultRooms);
      }

      populateRmSlt(analyticMap, rmSltComp);
   }

   private void populateRmSlt(final Map<String, WebAnalytics> analyticMap,
      final StringBuilder rmSltComp)
   {
      String rmSlt;
      rmSlt = rmSltComp.toString();
      if (StringUtils.isEmpty(rmSlt) || rmSlt == null)
      {
         rmSlt = NULL;
      }
      analyticMap.put(STR_M_SLT, new WebAnalytics(STR_M_SLT, rmSlt));
   }

   /**
    * Gets the rmslt details like room selected and its price.
    *
    * @param packageViewData the package view data
    * @param rmSltComp the rm slt comp
    * @param defaultRooms the default rooms
    */
   private void getRmsltDetails(final PackageViewData packageViewData,
      final StringBuilder rmSltComp, final List<RmSltViewData> defaultRooms)
   {
      int count = 0;
      final StringBuilder rmlSltRoomDesc = new StringBuilder();
      final StringBuilder rmlSltRoomPrice = new StringBuilder();

      final BasePackage packageModel = packageCartService.getBasePackage();
      final int indexVal = generateHotelIndex(packageModel);

      for (final RoomViewData roomViewData : packageViewData.getAccomViewData().get(indexVal)
         .getRoomViewData())
      {
         boolean isPresentInPackageModel = false;
         isPresentInPackageModel = isDefaultRoomAndPresentRoomAreSame(defaultRooms, roomViewData);
         count = count + 1;
         final BigDecimal defaultRoomPrice = BigDecimal.ZERO;

         constructRmsltDescAndPriceIndependetly(roomViewData, isPresentInPackageModel,
            defaultRoomPrice, rmlSltRoomDesc, rmlSltRoomPrice);
      }

      constructRmslt(rmSltComp, rmlSltRoomDesc, rmlSltRoomPrice);

   }

   /**
    * Construct rmslt based room selected append hyphen accordingly .
    *
    * @param rmSltComp the rm slt comp
    * @param rmlSltRoomDesc the rml slt room desc
    * @param rmlSltRoomPrice the rml slt room price
    */
   private void constructRmslt(final StringBuilder rmSltComp, final StringBuilder rmlSltRoomDesc,
      final StringBuilder rmlSltRoomPrice)
   {
      final String[] roomsDesc = rmlSltRoomDesc.toString().split("\\|");
      final String[] roomsPrice = rmlSltRoomPrice.toString().split("\\|");

      if (areMoreThanTwoRooms(roomsDesc, roomsPrice))
      {
         rmSltComp.append(removeLastCharacter(rmlSltRoomDesc.toString()));
         rmSltComp.append(HYPHEN);
         rmSltComp.append(removeLastCharacter(rmlSltRoomPrice.toString()));
      }
      else
      {
         // The hash code would be printed in case there is no
         // description(meaning no content)
         rmSltComp.append(rmlSltRoomDesc.toString());
         if (!StringUtils.endsWith(rmSltComp.toString(), DELIMITER))
         {
            rmSltComp.append(DELIMITER);
         }
         rmSltComp.append(rmlSltRoomPrice.toString());
      }
   }

   /**
    * Construct rmslt desc and price independetly.
    *
    * @param roomViewData the room view data
    * @param isPresentInPackageModel the is present in package model
    * @param defaultRoomPrice the default room price
    * @param rmlSltRoomDesc the rml slt room desc
    * @param rmlSltRoomPrice the rml slt room price
    */
   private void constructRmsltDescAndPriceIndependetly(final RoomViewData roomViewData,
      final boolean isPresentInPackageModel, final BigDecimal defaultRoomPrice,
      final StringBuilder rmlSltRoomDesc, final StringBuilder rmlSltRoomPrice)
   {
      if (!isPresentInPackageModel)
      {
         rmlSltRoomDesc.append(roomViewData.getDescription());
         rmlSltRoomDesc.append(DELIMITER);
         LOGGER.info("Web Analytics: RMSLT: Previous room price is:" + defaultRoomPrice
            + " And present room price is:" + roomViewData.getPrice());
         LOGGER.info("Total change in the room price is :" + roomViewData.getPrice());
         rmlSltRoomPrice.append(roomViewData.getPrice().setScale(0, RoundingMode.HALF_UP));
         rmlSltRoomPrice.append(DELIMITER);
      }
      else
      {
         rmlSltRoomDesc.append(roomViewData.getDescription());
         rmlSltRoomDesc.append(DELIMITER);
         rmlSltRoomPrice.append(0);
         rmlSltRoomPrice.append(DELIMITER);
      }

   }

   /**
    * Checks if is default room and present room are same.
    *
    * @param defaultRooms the default rooms
    * @param roomViewData the room view data
    * @return isPresentInPackageModel
    */
   private boolean isDefaultRoomAndPresentRoomAreSame(final List<RmSltViewData> defaultRooms,
      final RoomViewData roomViewData)
   {
      boolean isPresentInPackageModel = false;
      for (final RmSltViewData defaultRoom : defaultRooms)
      {
         if (StringUtils.equalsIgnoreCase(defaultRoom.getRoomCode(), roomViewData.getRoomCode()))
         {
            isPresentInPackageModel = true;
         }
      }
      return isPresentInPackageModel;
   }

   /**
    * @param packageModel
    * @return indexval
    */
   private int generateHotelIndex(final BasePackage packageModel)
   {

      return packageComponentService.getAllStays(packageModel).indexOf(
         packageComponentService.getHotel(packageModel));

   }

}
