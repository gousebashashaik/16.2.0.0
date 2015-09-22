/**
 *
 */
package uk.co.tui.shortlist.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.constants.GeneratedTravelConstants.Enumerations.WeatherTypeName;
import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.enums.BoardBasis;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.media.services.AccommodationMediaService;
import uk.co.portaltech.travel.media.services.ProductMediaService;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.model.WeatherTypeValueModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.travel.services.room.RoomsService;
import uk.co.portaltech.tui.jackson.databind.HTMLCharacterEscapes;
import uk.co.portaltech.tui.populators.MediaPopulatorLite;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Deposit;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.LowDeposit;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Schedule;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.price.calculator.ExtraFacilityCostCalculator;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.services.data.RoomAvailabilityIndicatorData;
import uk.co.tui.shortlist.account.populator.WishlistEntry;
import uk.co.tui.shortlist.view.data.AirportViewData;
import uk.co.tui.shortlist.view.data.AncillariesViewData;
import uk.co.tui.shortlist.view.data.FlightScheduleViewData;
import uk.co.tui.shortlist.view.data.LegViewData;
import uk.co.tui.shortlist.view.data.PartyCompositionViewData;
import uk.co.tui.shortlist.view.data.SavedHolidayAccommViewData;
import uk.co.tui.shortlist.view.data.SavedHolidayFlightViewData;
import uk.co.tui.shortlist.view.data.SavedHolidayPriceViewData;
import uk.co.tui.shortlist.view.data.SavedHolidayRoomViewData;
import uk.co.tui.shortlist.view.data.SavedHolidayViewData;
import uk.co.tui.shortlist.view.data.TripadvisorViewData;

/**
 * @author thirunavukkarasu.k
 *
 */
@SuppressWarnings("deprecation")
public class SavedHolidayViewDataPopulator
{

   @Autowired
   private DroolsPriorityProviderService droolsPriorityProviderService;

   @Autowired
   private RoomAvailabilityIndicatorData roomAvailabilityIndicatorData;

   @Autowired
   private ExtraFacilityCostCalculator extraFacilityCostCalculator;

   /** Session */
   @Autowired
   private SessionService sessionService;

   /** The Product Service . */
   @Autowired
   private ProductService productService;

   @Autowired
   private FeatureService featureService;

   @Autowired
   private AccommodationService accommodationService;

   @Autowired
   private CMSSiteService cmsSiteService;

   /** The Rooms Service. */
   @Autowired
   private RoomsService roomsService;

   @Autowired
   private PriceCalculationServiceImpl priceCalculationService;

   @Resource(name = "tuiConfigService")
   private TUIConfigService tuiConfigService;

   private static final Logger LOG = Logger.getLogger(SavedHolidayViewDataPopulator.class);

   @Resource
   private ConfigurationService configurationService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private ProductMediaService productMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   @Resource(name = "tuiUtilityService")
   private TuiUtilityService tuiUtilityService;

   @Resource
   private AccommodationMediaService accommodationMediaService;

   private static final String CURRENCY = "GBP";

   private static final String HOLIDAY_FACILITY_CODES =
      "customeraccount.compare.holidayfacilitycodes";

   private static final String FREE_CHILD_PLACE = "Free child place";

   private static final String FREE_CHILD_PLACES = "Free child places";

   private static final String COACH_TRANSFER = "Coach Transfer";

   private static final String FREE_CAR_HIRE = "Car Hire";

   @Resource
   private TUIUrlResolver<ProductModel> bookAccommodationUrlResolver;

   private static final String EQUALS = "=";

   private static final String AND = "&";

   private static final String FROM = "airports[]";

   private static final String WHERE_TO = "units[]";

   private static final String IS_FLEXIBLE = "flexibility";

   private static final String FLEXIBLE_DAYS = "flexibleDays";

   private static final String MULTI_SELECT = "multiSelect";

   private static final String DURATION = "duration";

   private static final String ADULTS = "noOfAdults";

   private static final String SENIORS = "noOfSeniors";

   private static final String CHILDREN = "noOfChildren";

   private static final String CHILDREN_AGE = "childrenAge";

   private static final String WHEN = "when";

   private static final String PACKAGE_ID = "packageId";

   private static final String INDEX = "index";

   private static final String BRAND_TYPE = "brandType";

   private static final String COLON = ":";

   private static final String LATESTCRITERIA = "latestCriteria";

   private static final String TRUE = "true";

   private static final String DEFAULT_FLEXIBLE_DAYS = "3";

   private static final String ROOM_TITLE = "roomTitle";

   private static final String CHILD_WORLD_CARE_FUND = "Child World Care Fund";

   private static final String ADULT_WORLD_CARE_FUND = "Adult World Care Fund";

   private static final String PRICE_CHANGE = "priceChange";

   private static final String MINUS = "m:";

   private static final String PLUS = "p:";

   private static final String N = "N";

   private static final int TWO = 2;

   private static final int SIX = 6;

   /**
    * This method populates packageViewData object from BasePackage object.
    *
    * @param source - BasePackage
    * @param target - PackageViewData object
    * @throws ConversionException the conversion exception
    */
   public void populate(final BasePackage source, final SavedHolidayViewData target,
      final BigDecimal priceDiff, final WishlistEntry wishlistEntry) throws ConversionException
   {

      populateFlightItinearary(source, target);
      final List<HighLights> highlight = source.getListOfHighlights();

      populateCoachTransferAndCarHire(target, highlight);
      populatePriceBreakDownData(source, target, priceDiff);
      populateAccommodationViewData(source, target);
      populatePartyCompositionViewData(source, target);
      target.setPackageId(source.getWishListEntryId());
      populateUrl(source, target);
      final int personCount = getPersonCount(target);

      final boolean outbound =
         ((PackageHoliday) source).getItinerary().getOutBound().get(0).isExternalInventory();
      final boolean inbound =
         ((PackageHoliday) source).getItinerary().getInBound().get(0).isExternalInventory();

      if ((!outbound) && (!inbound))
      {
         populateLowDepositForTarget(source, target, personCount);
      }

      if (null != source.getDiscount().getPrice())
      {
         target.setPricePerPerson(source.getPrice().getRate().getAmount());

         target.setOnlineDiscount(source.getDiscount().getPrice().getAmount().getAmount()
            .divide(new BigDecimal(personCount), TWO, RoundingMode.HALF_UP)
            .setScale(0, RoundingMode.HALF_UP));

         hideDiscountForThirdPartyFlights(source, target);

      }
   }

   /**
    * @param target
    * @param highlight
    */
   private void populateCoachTransferAndCarHire(final SavedHolidayViewData target,
      final List<HighLights> highlight)
   {
      for (final HighLights high : highlight)
      {
         if (high.compareTo(HighLights.COACH_TRANSFER) == 0)
         {
            target.setCoachTransfer(COACH_TRANSFER);
         }
         if (high.compareTo(HighLights.FREE_CAR_HIRE) == 0)
         {
            target.setFreeCarHire(FREE_CAR_HIRE);
         }
      }
   }

   /**
    * @param source
    * @param target
    * @param personCount
    */
   private void populateLowDepositForTarget(final BasePackage source,
      final SavedHolidayViewData target, final int personCount)
   {
      for (final Deposit lowDeposit : source.getDeposits())
      {
         if (lowDeposit != null)
         {
            if (lowDeposit instanceof LowDeposit)
            {
               target.setDepositPP(((LowDeposit) lowDeposit).getPersonPrice().getAmount()
                  .getAmount());
            }
            else
            {
               target.setStandardDepositPP(lowDeposit.getDepositAmount().getAmount().getAmount()
                  .divide(new BigDecimal(personCount), TWO, RoundingMode.HALF_UP)
                  .setScale(0, RoundingMode.HALF_UP));
            }
         }
      }
   }

   /**
    * Gets number of passenger for shortlisted holiday
    *
    * @param target
    * @return personcount.
    */
   private int getPersonCount(final SavedHolidayViewData target)
   {
      int personCount = target.getPaxViewData().getNoOfAdults();
      final int seniorCount = target.getPaxViewData().getNoOfSeniors();
      final int childrenCount = target.getPaxViewData().getNoOfChildren();
      if (seniorCount > 0)
      {
         personCount = personCount + seniorCount;
      }
      if (childrenCount > 0)
      {
         personCount = personCount + childrenCount;
      }
      return personCount;
   }

   /**
    * This method is written to set the Discount as 0 for 3PF flights. to hide ..
    *
    * @param source
    * @param target
    */
   private void hideDiscountForThirdPartyFlights(final BasePackage source,
      final SavedHolidayViewData target)
   {
      final List<Leg> legList = new ArrayList<Leg>();

      legList.addAll(packageComponentService.getFlightItinerary(source).getOutBound());
      legList.addAll(packageComponentService.getFlightItinerary(source).getInBound());

      for (final Leg leg : legList)
      {
         if (leg != null && leg.isExternalInventory())
         {
            target.setOnlineDiscount(new BigDecimal(0.0));
            break;
         }

      }

   }

   /**
    * @param source
    * @param target
    */
   private void populateUrl(final BasePackage source, final SavedHolidayViewData target)
   {

      final StringBuilder accomPageUrl = new StringBuilder();
      final AccommodationModel accomodationModel =
         getAccommodationModel(packageComponentService.getHotel(source).getCode());
      final SearchResultsRequestData searchParameterInSession =
         sessionService.getAttribute(LATESTCRITERIA);

      accomPageUrl.append(bookAccommodationUrlResolver.resolve(accomodationModel));

      accomPageUrl
         .append(ADULTS)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(source.getPassengers(),
               EnumSet.of(PersonType.ADULT))).append(AND);
      accomPageUrl
         .append(CHILDREN)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(source.getPassengers(),
               EnumSet.of(PersonType.CHILD, PersonType.INFANT))).append(AND);
      accomPageUrl.append(CHILDREN_AGE).append(EQUALS)
         .append(getChildrenAgeAsString(PassengerUtils.getChildAges(source.getPassengers())))
         .append(AND);
      accomPageUrl.append(DURATION).append(EQUALS).append(source.getDuration()).append(AND);
      final List<Leg> outBoundLegList =
         packageComponentService.getFlightItinerary(source).getOutBound();
      final Stay stay = packageComponentService.getStay(source);

      // The search parameter would not be in session for Short listed pages where the source for
      // this Build would be only package model
      accomPageUrl.append(FROM).append(EQUALS).append(getDepartureAirportCode(outBoundLegList))
         .append(AND);
      accomPageUrl.append(WHERE_TO).append(EQUALS).append(stay.getCode()).append(COLON)
         .append(stay.getType()).append(AND);
      if (searchParameterInSession != null)
      {
         accomPageUrl.append(FLEXIBLE_DAYS).append(EQUALS)
            .append(searchParameterInSession.getFlexibleDays()).append(AND);
         accomPageUrl.append(IS_FLEXIBLE).append(EQUALS)
            .append(searchParameterInSession.isFlexibility()).append(AND);
      }
      else
      {
         accomPageUrl.append(FLEXIBLE_DAYS).append(EQUALS).append(DEFAULT_FLEXIBLE_DAYS)
            .append(AND);
         accomPageUrl.append(IS_FLEXIBLE).append(EQUALS).append(TRUE).append(AND);
      }
      accomPageUrl.append(WHEN).append(EQUALS).append(getDepartureDate(outBoundLegList))
         .append(AND);

      accomPageUrl
         .append(SENIORS)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(source.getPassengers(),
               EnumSet.of(PersonType.SENIOR))).append(AND);

      accomPageUrl.append(PACKAGE_ID).append(EQUALS).append(source.getWishListEntryId())
         .append(AND);
      accomPageUrl.append(INDEX).append(EQUALS).append("1").append(AND);
      accomPageUrl.append(MULTI_SELECT).append(EQUALS).append(TRUE).append(AND);
      accomPageUrl.append(BRAND_TYPE).append(EQUALS).append(source.getBrandType().getId());
      getPriceDifferenceValue(target, accomPageUrl);
      target.getAccomViewData().setAccomUrl(accomPageUrl.toString());

   }

   private String getChildrenAgeAsString(final List<Integer> childrenAge)
   {
      final StringBuilder ages = new StringBuilder("");
      String comma = "";
      for (final Integer age : childrenAge)
      {
         ages.append(comma).append(age.intValue());
         comma = ",";
      }
      return ages.toString();
   }

   /**
    * To return the departure airport code.
    *
    * @param outBoundLegList
    * @return airport code.
    */
   private String getDepartureAirportCode(final List<Leg> outBoundLegList)
   {
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return outBoundLegList.get(0).getDepartureAirport().getCode();
      }
      return "";
   }

   /**
    *
    * To return the DepartureDate.
    *
    * @param outBoundLegList
    * @return Departure Date
    */
   private String getDepartureDate(final List<Leg> outBoundLegList)
   {
      final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return dateFormat.format(outBoundLegList.get(0).getSchedule().getDepartureDate());
      }

      return "";
   }

   /**
    * Method to return AccommodationModel from accommodation Code from PIM.
    *
    * @param accomCode
    * @return AccommodationModel
    */
   private AccommodationModel getAccommodationModel(final String accomCode)
   {
      return (AccommodationModel) productService.getProductForCode(accomCode);
   }

   /**
    * Populate price break down data.
    *
    * @param source the source
    * @param target the target
    */
   @SuppressWarnings("boxing")
   private void populatePriceBreakDownData(final BasePackage source,
      final SavedHolidayViewData target, final BigDecimal priceDiff)
   {
      final SavedHolidayPriceViewData basicPrice = new SavedHolidayPriceViewData();
      final SavedHolidayPriceViewData extrasPrice = new SavedHolidayPriceViewData();
      final SavedHolidayPriceViewData totalPrice = new SavedHolidayPriceViewData();
      final SavedHolidayPriceViewData onlineDiscount = new SavedHolidayPriceViewData();

      final BigDecimal viewDataBasicPrice = priceCalculationService.calculateBasicCost(source);

      final BigDecimal viewDataOnlineDiscountPrice =
         source.getDiscount().getPrice().getAmount().getAmount();

      final List<Deposit> depositAmount = source.getDeposits();
      BigDecimal totalDeposit = new BigDecimal(0);
      if (depositAmount != null && depositAmount.size() > 0)
      {
         for (final Deposit deposit : depositAmount)
         {
            totalDeposit = totalDeposit.add(deposit.getDepositAmount().getAmount().getAmount());
         }
      }

      target.setDeposit(totalDeposit);
      // TODO: Null check for Login Issue
      if (priceDiff != null)
      {
         final int priceDiffInt = priceDiff.intValue();

         if (priceDiffInt != 0)
         {
            target.setPriceDecreased(true);
            if (priceDiffInt > 0)
            {
               target.setPriceDecreased(false);
            }
         }
         target.setPriceDiff(priceDiff.abs());
      }
      basicPrice.setDescription("Basic Holiday");
      if (viewDataBasicPrice != null)
      {
         basicPrice.setPrice(getZeroTrimedValue(viewDataBasicPrice));
         basicPrice.setCurrencyAppendedPrice(CurrencyUtils
            .getCurrencySymbol(SavedHolidayViewDataPopulator.CURRENCY)
            + getZeroTrimedValue(viewDataBasicPrice).toString());
      }
      extrasPrice.setDescription("Options & extras (total)");
      extrasPrice.setPrice(priceCalculationService.calculateExtraFacilitiesTotalCost(source));
      extrasPrice.setCurrencyAppendedPrice(CurrencyUtils
         .getCurrencySymbol(SavedHolidayViewDataPopulator.CURRENCY)
         + getZeroTrimedValue(extrasPrice.getPrice()));

      onlineDiscount.setDescription("Online Discount");
      onlineDiscount.setPrice(getZeroTrimedValue(viewDataOnlineDiscountPrice));
      onlineDiscount.setCurrencyAppendedPrice("-" + " "
         + CurrencyUtils.getCurrencySymbol(SavedHolidayViewDataPopulator.CURRENCY)
         + getZeroTrimedValue(viewDataOnlineDiscountPrice).toString());

      totalPrice.setDescription("Total Price");
      totalPrice.setPrice(getZeroTrimedValue(source.getPrice().getAmount().getAmount()));
      totalPrice.setCurrencyAppendedPrice(getZeroTrimedValue(
         source.getPrice().getAmount().getAmount()).toString());
      target.setCurrencyAppendedRoundUpTotalCost(source.getPrice().getAmount().getAmount()
         .setScale(0, RoundingMode.UP).toString());

      target.getPriceBreakdownViewData().add(basicPrice);
      target.getPriceBreakdownViewData().add(extrasPrice);
      target.getPriceBreakdownViewData().add(onlineDiscount);
      target.getPriceBreakdownViewData().add(totalPrice);

      getIndividualAncillariesPrice(source, target);
   }

   /**
    * Calculate extra facilities total cost.
    *
    * @param pkg
    * @param target
    */
   @SuppressWarnings("boxing")
   public void getIndividualAncillariesPrice(final BasePackage pkg,
      final SavedHolidayViewData target)
   {

      BigDecimal extrasTotalCost = BigDecimal.ZERO;
      final List<AncillariesViewData> ancillariesList = new ArrayList<AncillariesViewData>();
      final Map<String, Integer> quntityMap = new HashMap<String, Integer>();
      int extrasCount = 0;
      int adultFund = 0;
      int childFund = 0;
      int baggageWeight = 0;
      String extraDesc = null;
      List<String> luggageList = new ArrayList<String>();

      for (final ExtraFacilityCategory extraCategory : pkg.getExtraFacilityCategories())
      {
         Map<String, List<BigDecimal>> multiValueMap = new TreeMap<String, List<BigDecimal>>();

         for (final ExtraFacility extra : extraCategory.getExtraFacilities())
         {

            if (extraFacilityChargeable(extra.getSelection()))
            {
               if (extra instanceof BaggageExtraFacility)
               {
                  baggageWeight = ((BaggageExtraFacility) extra).getBaggageWeight();
               }
               else
               {
                  baggageWeight = 0;
               }
               extrasTotalCost = extraFacilityCostCalculator.calculate(extra);
               extraDesc = extra.getDescription();

               if (extrasTotalCost.compareTo(BigDecimal.ZERO) > 0)
               {
                  if (!multiValueMap.containsKey(extraDesc))
                  {
                     if (extraDesc.contains("World Care Fund - Adult"))
                     {
                        multiValueMap.put(ADULT_WORLD_CARE_FUND, new ArrayList<BigDecimal>());
                        adultFund = 1;
                        extrasCount = extrasCount + 1;
                     }
                     else if (extraDesc.contains("World Care Fund - Child"))
                     {
                        multiValueMap.put(CHILD_WORLD_CARE_FUND, new ArrayList<BigDecimal>());
                        childFund = 1;
                     }
                     else
                     {
                        if (baggageWeight > 0)
                        {
                           luggageList.add(baggageWeight + " kg luggage");
                        }
                        else
                        {
                           luggageList = new ArrayList<String>();
                        }
                        multiValueMap.put(extraDesc, new ArrayList<BigDecimal>());
                        extrasCount = extrasCount + 1;
                     }

                  }
                  if (adultFund == 1 && multiValueMap.containsKey(ADULT_WORLD_CARE_FUND))
                  {
                     multiValueMap.get(ADULT_WORLD_CARE_FUND).add(extrasTotalCost);
                     adultFund = 0;
                     quntityMap.put("adult", getPAXQuantity(extra));
                  }
                  else if (childFund == 1 && multiValueMap.containsKey(CHILD_WORLD_CARE_FUND))
                  {
                     multiValueMap.get(CHILD_WORLD_CARE_FUND).add(extrasTotalCost);
                     childFund = 0;
                     quntityMap.put("child", getPAXQuantity(extra));
                  }
                  else
                  {
                     multiValueMap.get(extraDesc).add(extrasTotalCost);
                     quntityMap.put(extraDesc, getPAXQuantity(extra));
                  }

               }
               if (extra instanceof InsuranceExtraFacility)
               {
                  BigDecimal excessWaiverCost = BigDecimal.ZERO;
                  final String insuranceWaiver =
                     ((InsuranceExtraFacility) extra).getInsuranceExcessWaiver().getDescription();
                  final InsuranceExtraFacility insuranceExtra = (InsuranceExtraFacility) extra;
                  if (insuranceExtra.getInsuranceExcessWaiver().getPrice().getAmount() != null)
                  {
                     excessWaiverCost =
                        insuranceExtra.getInsuranceExcessWaiver().getPrice().getAmount()
                           .getAmount();
                     multiValueMap.put(insuranceWaiver, new ArrayList<BigDecimal>());
                     multiValueMap.get(insuranceWaiver).add(excessWaiverCost);
                     quntityMap.put(insuranceWaiver, getPAXQuantity(extra));
                     extrasCount = extrasCount + 1;
                  }
               }
               if (multiValueMap.containsKey("Taxi Transfer"))
               {
                  target.setCoachTransfer("");
               }
            }
         }
         if (extrasTotalCost.compareTo(BigDecimal.ZERO) == 0
            && multiValueMap.containsKey(extraDesc))
         {
            multiValueMap = new HashMap<String, List<BigDecimal>>();
         }

         for (final Map.Entry<String, List<BigDecimal>> entry : multiValueMap.entrySet())
         {
            final AncillariesViewData ancillayViewData = new AncillariesViewData();
            BigDecimal ancillaryCost = BigDecimal.ZERO;

            if (CollectionUtils.isNotEmpty(luggageList))
            {
               ancillayViewData.setAncillaryName(luggageList.get(0));
               luggageList.remove(0);
            }
            else
            {
               ancillayViewData.setAncillaryName(entry.getKey());
            }
            if (entry.getKey().contains(ADULT_WORLD_CARE_FUND))
            {
               ancillayViewData.setCount(quntityMap.get("adult").intValue());

            }
            else if (entry.getKey().contains(CHILD_WORLD_CARE_FUND))
            {
               ancillayViewData.setCount(quntityMap.get("child").intValue());

            }
            else
            {
               ancillayViewData.setCount(quntityMap.get(entry.getKey()).intValue());
            }
            for (final BigDecimal cost : entry.getValue())
            {
               ancillaryCost = ancillaryCost.add(cost);
            }

            ancillayViewData.setPrice(ancillaryCost);
            ancillariesList.add(ancillayViewData);
         }
         target.setAncillariesBreakup(ancillariesList);
      }
      target.setExtrasCount(extrasCount);
   }

   @SuppressWarnings("boxing")
   public Integer getPAXQuantity(final ExtraFacility extra)
   {
      final List<Price> extraPrices = extra.getPrices();
      int total = 0;
      for (final Price eachPriceModel : extraPrices)
      {
         if (eachPriceModel.getQuantity() != null)
         {

            total = total + eachPriceModel.getQuantity().intValue();
         }
      }
      return Integer.valueOf(total);
   }

   /**
    * Extra facility chargeable.
    *
    * @param facilitySelectionType the selection type
    * @return true, if successful
    */
   private boolean extraFacilityChargeable(final FacilitySelectionType facilitySelectionType)
   {
      return EnumSet.of(FacilitySelectionType.SELECTABLE, FacilitySelectionType.INCLUDED).contains(
         facilitySelectionType);
   }

   /**
    * This method gives the BigDecimal value in rounded mode if the scale value is zero
    *
    * @param wholeValue
    */
   @SuppressWarnings("boxing")
   private BigDecimal getZeroTrimedValue(final BigDecimal wholeValue)
   {
      final BigDecimal integerPart = BigDecimal.valueOf(wholeValue.intValue());
      if (wholeValue.subtract(integerPart).doubleValue() == 0)
      {
         return wholeValue.setScale(0);
      }
      else
      {
         return wholeValue.setScale(TWO, RoundingMode.HALF_UP);
      }
   }

   /**
    * Populate accommodation view data.
    *
    * @param source the source
    * @param target the target
    */
   @SuppressWarnings("boxing")
   private void populateAccommodationViewData(final BasePackage source,
      final SavedHolidayViewData target)
   {
      final SavedHolidayAccommViewData accommViewData = new SavedHolidayAccommViewData();
      final Stay availableaccomm = packageComponentService.getHotel(source);

      final List<Room> rooms = populateAccommodationRoomsData(target, availableaccomm);

      final AccommodationModel accommodationModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(availableaccomm.getCode(),
            cmsSiteService.getCurrentCatalogVersion());

      if (accommodationModel != null)
      {
         final String brandType = accommodationModel.getBrands().get(0).getCode();

         if (StringUtils.isNotBlank(brandType))
         {
            target.setBrandType(brandType);

         }

         final List<CategoryModel> categories =
            new ArrayList(accommodationModel.getSupercategories());

         final FlightItinerary flightItinerary =
            (FlightItinerary) packageComponentService.getFlightItinerary(source);
         final List<Leg> outLeg = flightItinerary.getOutBound();
         String depDate = null;
         for (final Leg leg : outLeg)
         {
            final Schedule flightModel = leg.getSchedule();
            depDate = flightModel.getDepartureDate().toString();
         }
         final DateFormat df2 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");

         final Date date1 = formationOfDate(depDate, df2);

         target.setDepartureMonth(new SimpleDateFormat("MMMM").format(date1));

         final Integer earlyCsp = accommodationModel.getEarlySalesCommercialPriority();
         populateAccommodationEarlySalePriority(target, earlyCsp);

         final Map<String, String> locationMap = new HashMap<String, String>();
         populateLocationViewData(accommViewData, accommodationModel, categories,
            new SimpleDateFormat("MMM").format(date1), locationMap);
         target.setLocationMap(locationMap);
         populateAccommodationThumbnailImage(accommViewData, accommodationModel);
         accommViewData.setAccomName(accommodationModel.getName());
         accommViewData.setBoardBasisName(BoardBasis
            .valueOf(rooms.get(0).getBoardBasis().getCode()).getValue());

         populateAccommodationOfficialRating(accommViewData, availableaccomm);
         accommViewData.setAccomCode(accommodationModel.getCode());
         populateRoomViewData(source, accommViewData);
         getUspData(accommodationModel, accommViewData, categories);
         target.setAccomViewData(accommViewData);

         target.setLowAvailabilityIndicator(getRoomAvailability(source, accommodationModel));

         getTripAdvisorRating(accommodationModel, target);

         getNoOfRoomsAndFloors(accommodationModel, accommViewData);

         getAccomLocationInfo(accommodationModel, accommViewData);

         populateGalleryDataForMobile(accommodationModel, target);

         target.setVideoPresent(Boolean.toString(checkIfGalleryVideoPresent(accommodationModel)));

      }
   }

   /**
    * @param target
    * @param earlyCsp
    */
   private void populateAccommodationEarlySalePriority(final SavedHolidayViewData target,
      final Integer earlyCsp)
   {
      if (earlyCsp != null)
      {
         target.setEarlySalesCommercialPriority(earlyCsp.intValue());
      }
   }

   /**
    * @param accommViewData
    * @param availableaccomm
    */
   private void populateAccommodationOfficialRating(
      final SavedHolidayAccommViewData accommViewData, final Stay availableaccomm)
   {
      if (availableaccomm.getOfficialRating() != null)
      {
         accommViewData.setRating(availableaccomm.getOfficialRating().getValue());
      }
   }

   /**
    * @param accommViewData
    * @param accommodationModel
    */
   private void populateAccommodationThumbnailImage(
      final SavedHolidayAccommViewData accommViewData, final AccommodationModel accommodationModel)
   {
      if (accommodationModel.getThumbnail() != null)
      {
         accommViewData.setAccomImageUrl(accommodationModel.getThumbnail().getURL());
      }
   }

   /**
    * @param depDate
    * @param df2
    * @return
    */
   private Date formationOfDate(final String depDate, final DateFormat df2)
   {
      Date date1 = new Date();
      try
      {
         if (StringUtils.isNotBlank(depDate))
         {
            date1 = df2.parse(depDate);
         }
      }
      catch (final ParseException e)
      {
         LOG.error("Date Parser Exception", e);
      }
      return date1;
   }

   /**
    * @param target
    * @param availableaccomm
    * @return
    */
   private List<Room> populateAccommodationRoomsData(final SavedHolidayViewData target,
      final Stay availableaccomm)
   {
      int noOfFreeChildren = 0;

      final List<Room> rooms = availableaccomm.getRooms();
      if (rooms != null)
      {
         noOfFreeChildren = calculationOfFreeKids(target, rooms);

      }
      if (noOfFreeChildren == 1)
      {
         target.setFreeChildPlace(FREE_CHILD_PLACE);

      }
      else if (noOfFreeChildren > 1)
      {
         target.setFreeChildPlace(FREE_CHILD_PLACES);
      }
      return rooms;
   }

   /**
    * @param target
    * @param noOfFreeChildren
    * @param rooms
    * @return
    */
   private int calculationOfFreeKids(final SavedHolidayViewData target, final List<Room> rooms)
   {
      int noOfFreeChildren = 0;

      for (final Room roomModel : rooms)
      {
         if (isFreeKidsAvailableForRoom(roomModel))
         {
            noOfFreeChildren++;
         }
         if (roomModel.getQuantity() != null)
         {
            target.setAvailableRooms(roomModel.getQuantity().intValue());
         }
      }
      return noOfFreeChildren;
   }

   /**
    * @param roomModel
    * @return
    */
   private boolean isFreeKidsAvailableForRoom(final Room roomModel)
   {
      return roomModel.getFreeKids() != null && roomModel.getFreeKids().booleanValue();
   }

   /**
    * Modified gallery data for mobile
    *
    * @param accommodationModel
    * @param target
    */
   private void populateGalleryDataForMobile(final AccommodationModel accommodationModel,
      final SavedHolidayViewData target)
   {
      final ArrayList<MediaViewData> mediaDatalist = new ArrayList<MediaViewData>();
      mediaPopulatorLite.populate(productMediaService.getImageMedias(accommodationModel),
         mediaDatalist);
      target.setGalleryImages(HTMLCharacterEscapes.writeValue(mediaDatalist));
   }

   /**
    *
    * @param accommodationModel
    * @param target
    */
   private void getTripAdvisorRating(final AccommodationModel accommodationModel,
      final SavedHolidayViewData target)
   {
      final TripadvisorViewData tripadvisorData = new TripadvisorViewData();
      if (accommodationModel.getType() != null)
      {
         if (accommodationModel.getReviewRating() != null)
         {
            tripadvisorData.setAverageRating(accommodationModel.getReviewRating());
         }
         if (accommodationModel.getReviewsCount() != null)
         {
            tripadvisorData.setReviewsCount(accommodationModel.getReviewsCount().intValue());
         }
         tripadvisorData.setRatingBar(accommodationModel.getRatingsBar());
      }

      target.setTripadvisorData(tripadvisorData);

   }

   /**
    * Helper method to get the limited rooms availability Indicator
    *
    * @param source
    * @param accommodationModel
    */
   @SuppressWarnings("boxing")
   private boolean getRoomAvailability(final BasePackage source,
      final AccommodationModel accommodationModel)
   {

      final FlightItinerary flightItinerary =
         (FlightItinerary) packageComponentService.getFlightItinerary(source);
      final List<Leg> outLegs = flightItinerary.getOutBound();
      if (accommodationModel != null)
      {
         final String brandType = accommodationModel.getBrands().get(0).getCode();
         if (StringUtils.isNotBlank(brandType))
         {
            roomAvailabilityIndicatorData.setBrandName(brandType);
         }
      }
      roomAvailabilityIndicatorData.setChannel(configurationService.getConfiguration().getString(
         "channel", StringUtils.EMPTY));

      for (final Leg leg : outLegs)
      {
         final Schedule flightSchedule = leg.getSchedule();
         roomAvailabilityIndicatorData.setDaysToDeparture(getDaysToDeparture(new DateTime(
            flightSchedule.getDepartureDate())));
      }
      for (final Room room : packageComponentService.getHotel(source).getRooms())
      {
         if (room != null && room.getQuantity() != null)
         {
            roomAvailabilityIndicatorData.setInventoryCount(room.getQuantity().intValue());
         }
      }
      if (accommodationModel != null)
      {
         final AccommodationType accommodationType = accommodationModel.getType();
         if (accommodationType != null)
         {
            roomAvailabilityIndicatorData.setAccomodationType(accommodationType.getCode());
         }
      }
      roomAvailabilityIndicatorData.setLowAvailabilityIndicator(false);
      droolsPriorityProviderService.applyLowAvailabilityRules(roomAvailabilityIndicatorData);

      return roomAvailabilityIndicatorData.isLowAvailabilityIndicator();
   }

   /**
    * Get the No of days to Departure
    *
    * @paramdepartureDate
    */
   private int getDaysToDeparture(final DateTime departureDate)
   {

      if (departureDate != null)
      {
         return DateUtils.subtractDates(departureDate, DateUtils.currentdateTime());
      }
      return 0;
   }

   /**
    * Populate Location view data.
    *
    * @param accommViewData
    * @param accommodationModel
    * @param categories
    */
   @SuppressWarnings("deprecation")
   private void populateLocationViewData(final SavedHolidayAccommViewData accommViewData,
      final AccommodationModel accommodationModel, final List<CategoryModel> categories,
      final String depMonth, final Map locationMap)
   {

      if (CollectionUtils.isNotEmpty(categories))
      {
         for (final CategoryModel category : categories)
         {
            if (category instanceof LocationModel)
            {
               final LocationModel location = (LocationModel) category;
               locationMap.put(location.getType().getCode(), location.getName());
               populateLocationViewData(accommViewData, accommodationModel,
                  location.getSupercategories(), depMonth, locationMap);
               if (LocationType.RESORT.equals(location.getType()))
               {
                  accommViewData.setResortName(location.getName());
                  final List<CategoryModel> subCategories = location.getSupercategories();
                  populateLocationViewData(accommViewData, accommodationModel, subCategories,
                     depMonth, locationMap);
               }
               if (LocationType.DESTINATION.equals(location.getType()))
               {
                  if (location.getWeather() != null)
                  {
                     final List<WeatherTypeModel> weatherModel =
                        location.getWeather().getWeatherTypes();
                     for (final WeatherTypeModel weatherMod : weatherModel)
                     {
                        if (WeatherTypeName.TEMPERATURE.equalsIgnoreCase(weatherMod
                           .getWeatherTypeName().toString()))
                        {
                           for (final WeatherTypeValueModel weatherTypeValue : weatherMod
                              .getWeatherTypeValues())
                           {
                              if (weatherTypeValue.getMonthType().toString()
                                 .equalsIgnoreCase(depMonth))
                              {
                                 accommViewData
                                    .setAverageTemperature(weatherTypeValue.getAverage());
                              }
                           }
                        }
                        else if (WeatherTypeName.RAINFALL.equalsIgnoreCase(weatherMod
                           .getWeatherTypeName().toString()))
                        {
                           for (final WeatherTypeValueModel weatherTypeValue : weatherMod
                              .getWeatherTypeValues())
                           {
                              if (weatherTypeValue.getMonthType().toString()
                                 .equalsIgnoreCase(depMonth))
                              {
                                 accommViewData.setAverageRainfall(weatherTypeValue.getAverage());
                              }
                           }
                        }

                     }

                  }

                  accommViewData.setDestinationName(location.getName());
                  continue;
               }
               else if (LocationType.REGION.equals(location.getType()))
               {
                  accommViewData.setDestinationName(location.getName());
                  continue;
               }
               else if (LocationType.COUNTRY.equals(location.getType()))
               {
                  accommViewData.setDestinationName(location.getName());
                  continue;
               }
            }

            if (category instanceof ProductRangeModel)
            {
               if (category.getCode() != null)
               {
                  final String siteName = cmsSiteService.getCurrentSite().getUid();
                  final String brands = category.getBrands().toString();
                  if (brands.contains(siteName))
                  {
                     accommViewData.setDifferentiatedCode(category.getCode());
                     accommViewData.setDifferentiatedType(category.getName());
                  }

                  final Map<String, List<Object>> straplineData =
                     featureService.getValuesForFeatures(
                        Arrays.asList(new String[] { "name", "strapline" }), category, new Date(),
                        null);
                  accommViewData.putFeatureCodesAndValues(straplineData);
                  LOG.info("straplineData -> " + straplineData);

               }

               accommViewData.setDifferentiatedProduct(true);
            }
         }
      }
   }

   /**
    * Populate room view data.
    *
    * @param source the source
    * @param target the target
    */
   @SuppressWarnings("boxing")
   private void populateRoomViewData(final BasePackage source,
      final SavedHolidayAccommViewData target)
   {
      final Map<String, List<String>> roomsMap = new HashMap<String, List<String>>();
      final Map<String, Boolean> roomsIndicatorMap = new HashMap<String, Boolean>();
      final Map<String, Integer> roomsAvailableCountMap = new HashMap<String, Integer>();
      final List<Map<String, Boolean>> roomsIndicatorMapList =
         new ArrayList<Map<String, Boolean>>();
      final List<Map<String, Integer>> roomsAvailableCountMapList =
         new ArrayList<Map<String, Integer>>();
      final List<String> roomsList = new ArrayList<String>();
      for (final Room room : packageComponentService.getHotel(source).getRooms())
      {
         final SavedHolidayRoomViewData roomViewData = new SavedHolidayRoomViewData();
         final AccommodationModel accomModel =
            getAccommodationModel(packageComponentService.getHotel(source).getCode());
         final RoomModel roomModel =
            roomsService.getRoomForAccommodation(room.getCode(), accomModel);

         final Boolean isRoomAvailable = new Boolean(getRoomAvailability(source, accomModel));

         // TODO : Need to pass the brand type
         if (roomModel != null)
         {
            try
            {
               final Map<String, List<Object>> roomsTitle =
                  featureService.getValuesForFeatures(Arrays.asList(new String[] { ROOM_TITLE }),
                     roomModel, new Date(), null);

               // Added in order to remove junk character like
               // <sup>&#174;</sup>
               String roomTitle =
                  Normalizer.normalize(roomsTitle.get(ROOM_TITLE).get(0).toString(),
                     Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
               if (roomTitle.contains("<sup></sup>"))
               {
                  roomTitle =
                     roomTitle.substring(0, roomTitle.indexOf("<sup>"))
                        + roomTitle
                           .substring(roomTitle.indexOf("</sup>") + SIX, roomTitle.length());
               }

               if (roomsTitle.containsKey(ROOM_TITLE) && roomsTitle.get(ROOM_TITLE) != null
                  && CollectionUtils.isNotEmpty(roomsTitle.get(ROOM_TITLE)))
               {
                  roomViewData.setRoomType(roomsTitle.containsKey(ROOM_TITLE) ? roomTitle : "");
               }
               roomsIndicatorMap.put(roomTitle, isRoomAvailable);
               roomsAvailableCountMap.put(roomTitle, room.getQuantity());
               roomsIndicatorMapList.add(roomsIndicatorMap);
               roomsAvailableCountMapList.add(roomsAvailableCountMap);
               roomsList.add(roomTitle);

            }
            catch (final Exception e)
            {
               LOG.error("Exception caught while populating roomdata" + e);
            }
         }
      }
      List<String> roomsInformation = null;
      final Set<String> roomSet = new HashSet<String>(roomsList);
      for (final String roomTitle : roomSet)
      {
         roomsInformation = new ArrayList<String>();
         roomsInformation.add(roomTitle);
         final Integer selectedRoomCount =
            Integer.valueOf(Collections.frequency(roomsList, roomTitle));
         roomsInformation.add(selectedRoomCount.toString());
         for (final Map<String, Boolean> roomsIndMap : roomsIndicatorMapList)
         {
            final Boolean roomsInd = roomsIndMap.get(roomTitle);
            roomsInformation.add(roomsInd.toString());
            if (roomsInd.toString() != null)
            {
               break;
            }
         }
         for (final Map<String, Integer> roomsAvilableMap : roomsAvailableCountMapList)
         {
            final Integer avialableRoomCount = roomsAvilableMap.get(roomTitle);
            roomsInformation.add(avialableRoomCount.toString());
            if (avialableRoomCount.toString() != null)
            {
               break;
            }
         }

         roomsMap.put(roomTitle, roomsInformation);
      }
      target.setRoomsMap(roomsMap);
   }

   /**
    * Populate passenger view data.
    *
    * @param source the source
    * @param target the target
    */
   @SuppressWarnings("boxing")
   private void populatePartyCompositionViewData(final BasePackage source,
      final SavedHolidayViewData target)
   {
      final PartyCompositionViewData paxData = new PartyCompositionViewData();
      paxData.setNoOfAdults(getPersonTypeCountFromPassengers(source.getPassengers(),
         PersonType.ADULT));
      paxData.setNoOfSeniors(getPersonTypeCountFromPassengers(source.getPassengers(),
         PersonType.SENIOR));
      paxData.setNoOfChildren(getPersonTypeCountFromPassengers(source.getPassengers(),
         PersonType.CHILD));
      paxData.setNoOfInfants(getPersonTypeCountFromPassengers(source.getPassengers(),
         PersonType.INFANT));
      final String paxComposition = getPartyComposition(paxData);
      paxData.setPaxComposition(paxComposition);
      target.setPaxViewData(paxData);

   }

   /**
    * This method creates a string representing all the passengers.
    *
    * @param paxData
    * @return paxComposition
    */
   @SuppressWarnings("boxing")
   private String getPartyComposition(final PartyCompositionViewData paxData)
   {
      final StringBuilder paxComposition = new StringBuilder("");
      final int noOfAdult = paxData.getNoOfAdults();
      final int noOfSenior = paxData.getNoOfSeniors();
      final int noOfChildren = paxData.getNoOfChildren() + paxData.getNoOfInfants();
      if (noOfAdult > 0)
      {
         appendAdults(paxComposition, paxData);
      }
      if (noOfSenior > 0)
      {
         appendSeniors(paxComposition, paxData);
      }
      if (noOfChildren > 0)
      {
         appendChildren(paxComposition, paxData);
      }
      return paxComposition.toString();
   }

   /**
    * Appends the children to the party composition.
    *
    * @param paxComposition
    * @param paxData
    */
   @SuppressWarnings("boxing")
   private void appendChildren(final StringBuilder paxComposition,
      final PartyCompositionViewData paxData)
   {
      final String comma = ", ";
      final int noOfChildren = paxData.getNoOfChildren() + paxData.getNoOfInfants();
      final List<Integer> childAges = paxData.getChildAges();

      if (noOfChildren > 1)
      {
         paxComposition.append(noOfChildren + " children ");
      }
      else
      {
         paxComposition.append(noOfChildren + " child ");
      }
      for (final Integer integer : childAges)
      {
         paxComposition.append(integer);
         if (integer.intValue() > 1)
         {
            paxComposition.append(" yrs");
         }
         else
         {
            paxComposition.append(" yr");
         }
         if (childAges.indexOf(integer) < childAges.size() - 1)
         {
            paxComposition.append(comma);
         }
      }

   }

   /**
    * Appends the Seniors to the party composition.
    *
    * @param paxComposition
    * @param paxData
    */
   private void appendSeniors(final StringBuilder paxComposition,
      final PartyCompositionViewData paxData)
   {
      final String comma = ", ";

      final int noOfSenior = paxData.getNoOfSeniors();
      final int noOfChildren = paxData.getNoOfChildren() + paxData.getNoOfInfants();

      if (noOfSenior > 1)
      {
         paxComposition.append(noOfSenior + " seniors");
      }
      else
      {
         paxComposition.append(noOfSenior + " senior");
      }
      if (noOfChildren > 0)
      {
         paxComposition.append(comma);
      }

   }

   /**
    * Appends the adults to the party composition.
    *
    * @param paxComposition
    * @param paxData
    */
   private void appendAdults(final StringBuilder paxComposition,
      final PartyCompositionViewData paxData)
   {
      final String comma = ", ";
      final int noOfAdult = paxData.getNoOfAdults();
      final int noOfSenior = paxData.getNoOfSeniors();
      final int noOfChildren = paxData.getNoOfChildren() + paxData.getNoOfInfants();

      if (noOfAdult > 1)
      {
         paxComposition.append(noOfAdult + " adults");
      }
      else
      {
         paxComposition.append(noOfAdult + " adult");
      }
      if (noOfSenior > 0 || noOfChildren > 0)
      {
         paxComposition.append(comma);
      }
   }

   /**
    * This method returns count of person type for given collection of Passengers
    *
    * @param passengers
    * @param type
    * @return int
    */
   private int getPersonTypeCountFromPassengers(final Collection<Passenger> passengers,
      final PersonType type)
   {
      final EnumSet<PersonType> adultType = EnumSet.of(type);
      return PassengerUtils.getPersonTypeCountFromPassengers(passengers, adultType);
   }

   /**
    * Populate flight itinearary.
    *
    * @param source the source
    * @param target the target
    */
   @SuppressWarnings("boxing")
   private void populateFlightItinearary(final BasePackage source, final SavedHolidayViewData target)
   {
      final FlightItinerary flightItinerary =
         (FlightItinerary) packageComponentService.getFlightItinerary(source);

      final SavedHolidayFlightViewData flightViewData = new SavedHolidayFlightViewData();
      final List<Leg> inLegs = flightItinerary.getInBound();
      final List<Leg> outLegs = flightItinerary.getOutBound();
      populateOutboundLegData(outLegs, flightViewData);
      populateInboundLegData(inLegs, flightViewData);
      populateDuration(source, flightViewData);
      getInboundDreamLiner(source, flightViewData);
      getOutboundDreamLiner(source, flightViewData);
      target.setFlightViewData(flightViewData);
   }

   /**
    * Populate flight duration.
    *
    * @param packageModel the Inclusive package Model
    * @param flightViewData the target
    */
   @SuppressWarnings("boxing")
   private void populateDuration(final BasePackage packageModel,
      final SavedHolidayFlightViewData flightViewData)
   {
      flightViewData.setDuration(packageModel.getDuration().intValue());
   }

   /**
    * Populate outbound leg data.
    *
    * @param outLegs the out legs
    * @param outFlights the out flights
    */
   private void populateOutboundLegData(final List<Leg> outLegs,
      final SavedHolidayFlightViewData outFlights)
   {
      for (final Leg leg : outLegs)
      {

         final LegViewData outBoundLeg = new LegViewData();
         final AirportViewData depAirport = new AirportViewData();
         final AirportViewData arrAirport = new AirportViewData();
         final FlightScheduleViewData schedule = new FlightScheduleViewData();
         populateArrivalAirportDetails(leg.getArrivalAirport(), arrAirport);
         populateDepartureAirportDetail(leg.getDepartureAirport(), depAirport);
         outBoundLeg.setArrivalAirport(arrAirport);
         outBoundLeg.setDepartureAirport(depAirport);
         outBoundLeg.setSchedule(populateScheduleData(leg, schedule));

         final DateTime arrivalDate = new DateTime(leg.getSchedule().getArrivalDate());
         final DateTime depDate = new DateTime(leg.getSchedule().getDepartureDate());
         if (!arrivalDate.equals(depDate))
         {
            final int departureDateDiff =
               uk.co.tui.common.DateUtils.subtractDates(arrivalDate, depDate);
            outFlights.setDepartureDateDiff(departureDateDiff);
         }

         if (leg.getCarrier() != null && leg.getCarrier().getCarrierInformation() != null)
         {
            outBoundLeg.setCarrierCode(leg.getCarrier().getCarrierInformation()
               .getMarketingAirlineCode());
            outBoundLeg.setCarrierName(leg.getCarrier().getCarrierInformation()
               .getMarketingAirlineName());
         }
         outFlights.getOutboundSectors().add(outBoundLeg);
      }
   }

   /**
    * Populate inbound leg data.
    *
    * @param inLegs the in legs
    * @param inFlights the in flights
    */
   private void populateInboundLegData(final List<Leg> inLegs,
      final SavedHolidayFlightViewData inFlights)
   {
      for (final Leg leg : inLegs)
      {
         final LegViewData inBoundLeg = new LegViewData();
         final AirportViewData depAirport = new AirportViewData();
         final AirportViewData arrAirport = new AirportViewData();
         final FlightScheduleViewData schedule = new FlightScheduleViewData();
         populateArrivalAirportDetails(leg.getArrivalAirport(), arrAirport);
         populateDepartureAirportDetail(leg.getDepartureAirport(), depAirport);
         inBoundLeg.setArrivalAirport(arrAirport);
         inBoundLeg.setDepartureAirport(depAirport);
         inBoundLeg.setSchedule(populateScheduleData(leg, schedule));

         final DateTime arrivalDate = new DateTime(leg.getSchedule().getArrivalDate());
         final DateTime depDate = new DateTime(leg.getSchedule().getDepartureDate());
         if (!arrivalDate.equals(depDate))
         {
            final int arrivalDateDiff =
               uk.co.tui.common.DateUtils.subtractDates(arrivalDate, depDate);
            inFlights.setArrivalDateDiff(arrivalDateDiff);
         }
         if (leg.getCarrier() != null && leg.getCarrier().getCarrierInformation() != null)
         {
            inBoundLeg.setCarrierCode(leg.getCarrier().getCarrierInformation()
               .getMarketingAirlineCode());
            inBoundLeg.setCarrierName(leg.getCarrier().getCarrierInformation()
               .getMarketingAirlineName());

         }
         inFlights.getInboundSectors().add(inBoundLeg);
      }
   }

   /**
    * Populate arrival airport details.
    *
    * @param airport the airport
    * @param arrAirport the arr airport
    */
   private void populateArrivalAirportDetails(final Port airport, final AirportViewData arrAirport)
   {
      arrAirport.setCode(airport.getCode());
      arrAirport.setName(airport.getName());
   }

   /**
    * Populate departure airport detail.
    *
    * @param airport the airport
    * @param depAirport the dep airport
    */
   private void populateDepartureAirportDetail(final Port airport, final AirportViewData depAirport)
   {
      depAirport.setCode(airport.getCode());
      depAirport.setName(airport.getName());
   }

   /**
    * Populate schedule data.
    *
    * @param legmodel the legmodel
    * @param schedule the schedule
    * @return the schedule
    */
   @SuppressWarnings("deprecation")
   private FlightScheduleViewData populateScheduleData(final Leg legmodel,
      final FlightScheduleViewData schedule)
   {

      final Schedule flightSchedule = legmodel.getSchedule();

      schedule.setArrivalDate(DateUtils.customFormatFlightDate(flightSchedule.getArrivalDate()));
      final Calendar c = Calendar.getInstance();
      c.setTime(flightSchedule.getArrivalDate());
      schedule.setArrTime(flightSchedule.getArrivalTime());

      schedule
         .setDepartureDate(DateUtils.customFormatFlightDate(flightSchedule.getDepartureDate()));
      schedule.setDepTime(flightSchedule.getDepartureTime());
      final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
      schedule.setArrivalDateInMiliSeconds(formatter.format(flightSchedule.getArrivalDate()));
      schedule.setDepartureDateInMiliSeconds(formatter.format(flightSchedule.getDepartureDate()));

      return schedule;
   }

   /**
    * Method to get USP Values
    *
    * @param accommodationModel
    * @param accommViewData
    */
   private void getUspData(final AccommodationModel accommodationModel,
      final SavedHolidayAccommViewData accommViewData, final List<CategoryModel> categories)
   {

      final Map<String, List<Object>> uspData =
         featureService.getValuesForFeatures(Arrays.asList(new String[] { "usps" }),
            accommodationModel, new Date(), accommodationModel.getBrands().get(0).getCode());

      if (uspData != null)
      {
         accommViewData.putFeatureCodesAndValues(uspData);
      }

   }

   /**
    * Helper method to get the Hotel Floors and Rooms
    *
    * @param accommodationModel
    * @param accommViewData
    */
   private void getNoOfRoomsAndFloors(final AccommodationModel accommodationModel,
      final SavedHolidayAccommViewData accommViewData)
   {
      final Map<String, List<Object>> hotelInfo =
         featureService.getValuesForFeatures(
            Arrays.asList(new String[] { "noOfFloors", "noOfrooms" }), accommodationModel,
            new Date(), accommodationModel.getBrands().get(0).getCode());

      if (hotelInfo != null)
      {
         accommViewData.setHotelRoomsAndFloors(hotelInfo);
      }

   }

   /**
    * Helper method to get the accommodation location information
    *
    * @param accommodationModel
    * @param accommViewData
    */
   private void getAccomLocationInfo(final AccommodationModel accommodationModel,
      final SavedHolidayAccommViewData accommViewData)
   {
      final Map<String, List<Object>> locationInfo =
         featureService.getValuesForFeatures(
            Arrays.asList(new String[] { "locationInformation", "latitude", "longitude" }),
            accommodationModel, new Date(), accommodationModel.getBrands().get(0).getCode());

      if (locationInfo != null)
      {
         accommViewData.setLocationInformation(locationInfo);
      }

   }

   /**
    * Helper method to fetch the dreamliner information
    *
    * @param source
    * @param flightViewData
    */

   public void getInboundDreamLiner(final BasePackage source,
      final SavedHolidayFlightViewData flightViewData)
   {
      boolean isDreamLiner = false;
      final FlightItinerary itenary =
         (FlightItinerary) packageComponentService.getFlightItinerary(source);
      final List<Leg> inBound = itenary.getInBound();

      for (final Leg leg : inBound)
      {
         if (null != leg.getCarrier().getEquipementType()
            && StringUtils
               .equals(leg.getCarrier().getEquipementType().toString(), "DREAMLINEAR787"))
         {
            isDreamLiner = true;
         }
      }
      flightViewData.setInboundDreamLiner(isDreamLiner);
   }

   /**
    * @param source
    * @param flightViewData
    */
   private void getOutboundDreamLiner(final BasePackage source,
      final SavedHolidayFlightViewData flightViewData)
   {

      boolean isDreamLiner = false;
      final FlightItinerary itenary =
         (FlightItinerary) packageComponentService.getFlightItinerary(source);

      final List<Leg> outBound = itenary.getOutBound();

      for (final Leg leg : outBound)
      {
         if (null != leg.getCarrier().getEquipementType()
            && StringUtils
               .equals(leg.getCarrier().getEquipementType().toString(), "DREAMLINEAR787"))
         {
            isDreamLiner = true;
         }
      }
      flightViewData.setOutboundDreamLiner(isDreamLiner);
   }

   /**
    * private method to get the Holiday Facilities
    *
    * @param accommodationModel
    * @param accommViewData
    */
   private void getHolidayFacilities(final AccommodationModel accommodationModel,
      final SavedHolidayAccommViewData accommViewData)
   {

      final Map<String, String> accomCodeandValue = new HashMap<String, String>();
      final Map<String, Boolean> holidayFacilities = new HashMap<String, Boolean>();

      final String[] facilityCodes =
         tuiConfigService.getConfigValue(HOLIDAY_FACILITY_CODES).split(",");

      final List<FacilityModel> facilityList =
         (List<FacilityModel>) accommodationModel.getFacilities();
      for (final FacilityModel facilityModel : facilityList)
      {
         accomCodeandValue
            .put(facilityModel.getType().getCode(), facilityModel.getType().getName());
      }

      for (final String code : facilityCodes)
      {
         if (accomCodeandValue.containsKey(code))
         {
            holidayFacilities.put(code, new Boolean(true));
         }
         else
         {
            holidayFacilities.put(code, new Boolean(false));
         }
      }
      accommViewData.setHolidayFacilities(holidayFacilities);

   }

   /**
    * @param source
    */
   private boolean checkIfGalleryVideoPresent(final AccommodationModel source)
   {
      return CollectionUtils.isNotEmpty(accommodationMediaService.getVideoMedias(source,
         tuiUtilityService.getBrandForModel(source)));
   }

   /**
    * @param target
    * @param accomPageUrl
    *
    *           This method will check if there is a price change and appends the value to the
    *           request URL.
    */
   private void getPriceDifferenceValue(final SavedHolidayViewData target,
      final StringBuilder accomPageUrl)
   {

      BigDecimal price = target.getPriceDiff();
      int isPriceZero = 0;
      if (price != null)
      {
         price = price.setScale(0, RoundingMode.HALF_UP);
         isPriceZero = price.compareTo(BigDecimal.ZERO);
      }
      final StringBuilder builder = new StringBuilder();
      // checks if there is price change and appends the value if the price has changed.
      if (isPriceZero == 0)
      {
         builder.append(N);
         accomPageUrl.append(AND).append(PRICE_CHANGE).append(EQUALS).append(builder);

      }
      else
      {
         // append the price change value if price has changed or not.
         if (target.isPriceDecreased())
         {
            builder.append(MINUS);
         }
         else
         {
            builder.append(PLUS);
         }
         builder.append(price);
         accomPageUrl.append(AND).append(PRICE_CHANGE).append(EQUALS).append(builder);

      }

   }
}
