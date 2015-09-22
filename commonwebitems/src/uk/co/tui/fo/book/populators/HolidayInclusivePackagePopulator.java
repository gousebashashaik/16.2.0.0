/**
 *
 */

package uk.co.tui.fo.book.populators;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;
import static uk.co.tui.fj.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.results.Accomodation;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.model.results.Schedule;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.productrange.MainstreamProductRangeService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.AgentType;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.BookingType;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.CrossSellingBrand;
import uk.co.tui.book.domain.lite.Discount;
import uk.co.tui.book.domain.lite.EquipmentType;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.Flight;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.Hotel;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Memo;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.Rating;
import uk.co.tui.book.domain.lite.SalesChannel;
import uk.co.tui.book.domain.lite.SalesChannelType;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.domain.lite.WebAgent;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.ThirdPartyFlightPriceCalculationService;
import uk.co.tui.book.services.impl.DefaultPackageIdService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.services.item.LinkedItemService;
import uk.co.tui.util.CatalogAndCategoryUtil;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * The Class HolidayInclusivePackagePopulator.
 *
 * @author sunilkumar.sahu
 * @version $Revision: 1.0 $
 */

public class HolidayInclusivePackagePopulator implements Populator<Holiday, BasePackage>
{
   
   /** Offset1. */
   private static final int SCHEDULE_TIME_OFFSET1 = 4;
   
   /** Offset2. */
   private static final int SCHEDULE_TIME_OFFSET2 = 2;
   
   private static final int INDEX_ZERO = 0;
   
   private static final int INDEX_TWO = 2;
   
   private static final int INDEX_THREE = 3;
   
   /** The Product Service . */
   @Resource
   private ProductService productService;
   
   /** The Feature Service . */
   @Resource
   private FeatureService featureService;
   
   /** The Airport Service . */
   @Resource
   private AirportService airportService;
   
   /** The deposit populator. */
   @Resource(name = "foDepositPopulator")
   private HolidayDepositComponentPopulator depositPopulator;
   
   /** The productRangeService. */
   @Resource
   private MainstreamProductRangeService productRangeService;
   
   /** The catalog and category util. */
   @Resource
   private CatalogAndCategoryUtil catalogAndCategoryUtil;
   
   /** The tui utility service. */
   @Resource
   private TuiUtilityService tuiUtilityService;
   
   /** The session service. */
   @Resource
   private SessionService sessionService;
   
   @Resource
   private ThirdPartyFlightPriceCalculationService thirdPartyFlightPriceCalculationService;
   
   /** The t Rating of the accommodation. */
   private static final String T_RATING = "tRating";
   
   /** The Constant ADULT_AGE. */
   private static final Integer ADULT_AGE = Integer.valueOf(30);
   
   /** The Constant LATESTCRITERIA. */
   private static final String LATESTCRITERIA = "latestCriteria";
   
   /** The Constant YES. */
   private static final String YES = "Y";
   
   /** The service locator. */
   @Resource(name = "inventoryServiceLocator")
   private ServiceLocator<Populator> serviceLocator;
   
   @Resource(name = "mainStreamTrvelLinkedItemService")
   private LinkedItemService linkedItemService;
   
   @Resource
   private BrandUtils brandUtils;
   
   @Resource
   private CurrencyResolver currencyResolver;
   
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;
   
   /** The DefaultPackageIdService. */
   @Resource
   private DefaultPackageIdService packageIdService;
   
   /**
    * This method populates InclusivePackage object from Holiday object.
    *
    * @param holiday - Holiday object
    * @param target - InclusivePackage object
    */
   
   @SuppressWarnings(BOXING)
   @Override
   public void populate(final Holiday holiday, final BasePackage target)
   {
      target.setPackageType(PackageType.INCLUSIVE);
      
      final AccommodationModel accomodationModel =
         getAccommodationModel(holiday.getAccomodation().getAccomCode());
      
      populateBookingDetails(target);
      
      final Map<String, String> featureValueMap =
         featureService.getFirstValueForFeaturesAsStrings(Arrays.asList("holiday_type", T_RATING),
            accomodationModel, new Date(),
            brandUtils.getFeatureServiceBrand(accomodationModel.getBrands()));
      populateAvailableAccomodationType(accomodationModel, target);
      final Stay availableAccommodation = packageComponentService.getStay(target);
      populateRatings(featureValueMap.get(T_RATING), availableAccommodation);
      populateAvailableAccomodation(holiday, availableAccommodation);
      
      populateHighlights(holiday, target);
      updateHighlightsIfLapland(featureValueMap.get("holiday_type"), target.getListOfHighlights());
      
      target.setDuration(Integer.valueOf(holiday.getDuration()));
      populateItinerary(holiday, target);
      populatePrice(holiday, target);
      populateDiscount(holiday, target);
      final Accomodation accomodation = holiday.getAccomodation();
      if (holiday.getAccomodation() != null)
      {
         populatePaxDetails(accomodation.getRoomDetails(), target);
      }
      else
      {
         populatePaxDetails(holiday, target);
      }
      // populate brand information
      populateBrandType(accomodation, target);
      target.setSiteBrand(tuiUtilityService.getSiteBrand());
      populateInventoryType(holiday.getTracs(), target);
      // Aligning with TRACS by adding flight price to 1st room thereby making
      // only rooms price constitute to total package price
      addFlightPriceToFirstRoom(holiday, target);
      serviceLocator.locateByInventory(target.getInventory().getInventoryType().toString())
         .populate(holiday, target);
      if (target.getInventory().getInventoryType() == InventoryType.TRACS)
      {
         populateDepositComponent(holiday, target);
      }
      populateProductType(accomodationModel, target);
      if (PackageUtilityService.isMulticomThirdPartyFlight(packageComponentService
         .getFlightItinerary(target)))
      {
         populateFlightMarkupPrice(holiday, target);
      }
      target.setExtraFacilityCategories(new ArrayList<ExtraFacilityCategory>());
      target.setMemos(new ArrayList<Memo>());
      target.setId(packageIdService.getPackageId(target));
   }
   
   /**
    * Populate flight price.
    *
    * @param holiday the holiday
    * @param target the target
    */
   private void addFlightPriceToFirstRoom(final Holiday holiday, final BasePackage target)
   {
      if (target.getInventory().getInventoryType().toString()
         .equalsIgnoreCase(InventoryType.ATCOM.toString()))
      {
         final BigDecimal totalPrice = new BigDecimal(holiday.getTotalPrice());
         final List<RoomDetails> roomDetails = holiday.getAccomodation().getRoomDetails();
         BigDecimal roomsPrice = BigDecimal.ZERO;
         for (final RoomDetails roomDetail : roomDetails)
         {
            roomsPrice = roomsPrice.add(new BigDecimal(roomDetail.getPrice()));
         }
         
         final Stay availableAccommodation = packageComponentService.getStay(target);
         final BigDecimal flightPrice = totalPrice.subtract(roomsPrice);
         final BigDecimal firstRoomsPrice =
            availableAccommodation.getRooms().get(0).getPrices().get(0).getAmount().getAmount();
         final BigDecimal updatedPrice = firstRoomsPrice.add(flightPrice);
         availableAccommodation.getRooms().get(0).getPrices().get(0).getAmount()
            .setAmount(updatedPrice);
      }
      
   }
   
   /**
    * The method populates the third Party flight price.
    *
    * @param holiday
    * @param packageModel
    */
   @SuppressWarnings("boxing")
   private void populateFlightMarkupPrice(final Holiday holiday, final BasePackage packageModel)
   {
      BigDecimal totalFlightMarkupCost = BigDecimal.ZERO;
      totalFlightMarkupCost =
         getTotalFlightMarkupCost(holiday.getAccomodation().getRoomDetails(),
            holiday.getTotalPrice());
      thirdPartyFlightPriceCalculationService.calculateMarkupCost(packageModel,
         totalFlightMarkupCost);
      
   }
   
   /**
    * @param roomDetails
    * @param totalPrice
    * @return markupCost
    */
   private BigDecimal getTotalFlightMarkupCost(final List<RoomDetails> roomDetails,
      final String totalPrice)
   {
      BigDecimal price = BigDecimal.ZERO;
      for (final RoomDetails eachRoomDetail : roomDetails)
      {
         price = price.add(new BigDecimal(eachRoomDetail.getPrice()));
      }
      return new BigDecimal(totalPrice).subtract(price);
   }
   
   /**
    * Populate inventory type.
    *
    * @param tracs the tracs
    * @param target the target
    */
   private void populateInventoryType(final String tracs, final BasePackage target)
   {
      final Inventory inventory = new Inventory();
      // For ATCOM package the tracsPackageId would contain '/' ,
      // we reset the inventory type to ATCOM
      inventory.setInventoryType(StringUtils.isBlank(tracs) ? InventoryType.ATCOM
         : InventoryType.TRACS);
      target.setInventory(inventory);
   }
   
   /**
    * Populate brand type.
    *
    * @param accomodation the accomodation
    * @param target the target
    */
   private void populateBrandType(final Accomodation accomodation, final BasePackage target)
   {
      final String brandType = accomodation.getBrandType();
      populateBrandInformation(brandType, target);
   }
   
   /**
    * Populates the brand information.
    *
    * @param brandType the brand code
    */
   private void populateBrandInformation(final String brandType, final BasePackage target)
   {
      for (final Brand brand : Brand.values())
      {
         if (brand.getId().equalsIgnoreCase(brandType))
         {
            target.setBrandType(Brand.valueOf(brand.getCode()));
            break;
         }
      }
      populateCrossBrandInformation(brandType, target);
   }
   
   /**
    * Populate cross brand information.
    *
    * @param brandType the brand type
    * @param target the target
    */
   private void populateCrossBrandInformation(final String brandType, final BasePackage target)
   {
      if (target.getBrandType() == null)
      {
         for (final CrossSellingBrand crossBrand : CrossSellingBrand.values())
         {
            if (crossBrand.getId().equalsIgnoreCase(brandType))
            {
               target.setCrossBrandType(crossBrand);
               target.setBrandType(Brand.valueOf(crossBrand.getSuperBrand()));
               break;
            }
         }
      }
   }
   
   /**
    * To populate the product type in to the package model.
    *
    * @param accomodationModel the holiday
    * @param target the target
    */
   private void populateProductType(final AccommodationModel accomodationModel,
      final BasePackage target)
   {
      final Collection<ProductRangeModel> productRangeModels =
         productRangeService.getProductRangesForProductByBrand(accomodationModel,
            tuiUtilityService.getSiteReleventBrandPks(),
            catalogAndCategoryUtil.getActiveCatalogversion());
      for (final ProductRangeModel productRangeModel : productRangeModels)
      {
         target.setProductType(productRangeModel.getCode());
      }
      
   }
   
   /**
    * Populate price.
    *
    * @param holiday the holiday
    * @param target the target
    */
   private void populatePrice(final Holiday holiday, final BasePackage target)
   {
      
      final Price price = new Price();
      price.setCode("TC");
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(holiday.getTotalPrice())));
      price.setRate(convertBigDecimalToMoneyLite(new BigDecimal(holiday.getPerPersonPrice())));
      target.setPrice(price);
   }
   
   /**
    * Populate discount.
    *
    * @param holiday the holiday
    * @param target the target
    */
   private void populateDiscount(final Holiday holiday, final BasePackage target)
   {
      final Discount discountModel = new Discount();
      final Price price = new Price();
      price.setCode("SAV");
      price.setAmount(convertBigDecimalToMoneyLite(holiday.getTotalDiscount()));
      price.setRate(convertBigDecimalToMoneyLite(new BigDecimal(holiday.getPerPersonDiscount())
         .abs()));
      discountModel.setPrice(price);
      target.setDiscount(discountModel);
   }
   
   /**
    * Populate low deposit component.
    *
    * @param holiday the holiday
    * @param target the target
    */
   private void populateDepositComponent(final Holiday holiday, final BasePackage target)
   {
      if (isNotNull(holiday.getDepositAmount())
         && holiday.getDepositAmount().compareTo(BigDecimal.ZERO) > 0)
      {
         depositPopulator.populate(holiday, target);
      }
   }
   
   /**
    * Populate available accomodation Type
    *
    * @param accomodationModel the AccommodationModel
    * @param target the BasePackage
    */
   private void populateAvailableAccomodationType(final AccommodationModel accomodationModel,
      final BasePackage target)
   {
      
      final Stay availableAccomModel = new Hotel();
      // introduced as part of the villa US to identify and classify the
      // package as a villa holiday
      availableAccomModel.setType(StayType.valueOf(accomodationModel.getType().toString()));
      ((PackageHoliday) target).setStay(availableAccomModel);
   }
   
   /**
    * Populate available accomodation.
    *
    * @param holiday the holiday
    * @param availableAccomModel the availableAccommodation
    */
   private void populateAvailableAccomodation(final Holiday holiday, final Stay availableAccomModel)
   {
      final Accomodation accomodation = holiday.getAccomodation();
      availableAccomModel.setCode(accomodation.getAccomCode());
      final Price price = new Price();
      price.setAmount(convertBigDecimalToMoneyLite(accomodation.getPrice().getValue()));
      availableAccomModel.setPrice(price);
      // TODO: rework on below part.
      availableAccomModel.setStartDate(holiday.getAccomodation().getAccomStartDate().toDate());
      // TODO:Following method needs to revisit
      availableAccomModel.setEndDate(populateAccomEndDate(holiday));
   }
   
   /**
    *
    * This method takes the value as BigDecimal and returns the MoneyModel object.
    *
    * @param value the value as BigDecimal.
    * @return MoneyModel
    */
   private Money convertBigDecimalToMoneyLite(final BigDecimal value)
   {
      final Money money = new Money();
      money.setAmount(value);
      money.setCurrency(Currency.getInstance(currencyResolver.getSiteCurrency()));
      return money;
   }
   
   /**
    *
    * This method adds duration with destinations arrival date
    *
    * @param holiday
    */
   private Date populateAccomEndDate(final Holiday holiday)
   {
      return holiday.getAccomodation().getAccomStartDate().plusDays(holiday.getDuration()).toDate();
   }
   
   /**
    * To populate the itinerary to the inclusive package object.
    *
    * @param holiday the holiday
    * @param target the target
    */
   
   private void populateItinerary(final Holiday holiday, final BasePackage target)
   {
      final Itinerary flightItineraryModel = new FlightItinerary();
      
      final List<uk.co.tui.book.domain.lite.Leg> outBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();
      final List<uk.co.tui.book.domain.lite.Leg> inBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();
      
      final List<Leg> outboundlegs = holiday.getItinerary().getOutbound();
      final List<Leg> inboundlegs = holiday.getItinerary().getInbound();
      
      populateLegModels(outboundlegs, outBoundLegModelList);
      if (CollectionUtils.isNotEmpty(inboundlegs))
      {
         populateLegModels(inboundlegs, inBoundLegModelList);
      }
      flightItineraryModel.setOutBound(outBoundLegModelList);
      flightItineraryModel.setInBound(inBoundLegModelList);
      ((PackageHoliday) target).setItinerary(flightItineraryModel);
   }
   
   /**
    * To populate the LegModels.
    *
    * @param legs the legs
    * @param legModelList the leg model list
    */
   private void populateLegModels(final List<Leg> legs,
      final List<uk.co.tui.book.domain.lite.Leg> legModelList)
   {
      final uk.co.tui.book.domain.lite.Leg legModel = new FlightLeg();
      for (final Leg eachLeg : legs)
      {
         
         final uk.co.tui.book.domain.lite.Schedule scheduleModel =
            new uk.co.tui.book.domain.lite.Schedule();
         populateFlightSchedule(eachLeg, scheduleModel);
         
         final CarrierInformation carrierInfoModel = new CarrierInformation();
         final Carrier carrier = new Flight();
         populateCarrierInformation(eachLeg, carrierInfoModel, carrier);
         
         carrier.setCarrierInformation(carrierInfoModel);
         populateFlight(eachLeg, carrier);
         legModel.setSchedule(scheduleModel);
         legModel.setCarrier(carrier);
         populateExternalInventory(legModel, eachLeg);
         setAirports(eachLeg, legModel);
         populateAtcomId((FlightLeg) legModel, eachLeg);
         populateRouteCode((FlightLeg) legModel, eachLeg);
         populateCycDate((FlightLeg) legModel, eachLeg);
         populateFlightSeqCode((FlightLeg) legModel, eachLeg);
         legModelList.add(legModel);
      }
   }
   
   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateFlightSeqCode(final FlightLeg legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getRouteCd()))
      {
         legModel.setFlightSeqCode(StringUtils.substring(eachLeg.getRouteCd(),
            CommonwebitemsConstants.SEVEN, CommonwebitemsConstants.EIGHT));
      }
   }
   
   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateCycDate(final FlightLeg legModel, final Leg eachLeg)
   {
      if (eachLeg.getCycDate() != null)
      {
         legModel.setCycDate(eachLeg.getCycDate().toDate());
      }
   }
   
   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateRouteCode(final FlightLeg legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getRouteCd()))
      {
         legModel.setRouteCode(eachLeg.getRouteCd());
      }
   }
   
   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateAtcomId(final FlightLeg legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getAtcomId()))
      {
         legModel.setAtcomId(eachLeg.getAtcomId());
      }
   }
   
   /**
    * Populate external inventory.
    *
    * @param legModel the leg model
    * @param leg the flight leg
    */
   private void populateExternalInventory(final uk.co.tui.book.domain.lite.Leg legModel,
      final Leg leg)
   {
      legModel.setExternalInventory(BooleanUtils.isTrue(leg.isExternalInventory()));
   }
   
   /**
    * To populate the Arrival Airport and Departure Airport to the Leg object.
    *
    * @param eachLeg the each leg
    * @param legModel the leg model
    */
   private void setAirports(final Leg eachLeg, final uk.co.tui.book.domain.lite.Leg legModel)
   {
      legModel.setDepartureAirport(getAirportName(eachLeg.getDepartureAirport().getCode()));
      legModel.setArrivalAirport(getAirportName(eachLeg.getArrivalAirport().getCode()));
   }
   
   /**
    * @param airportModel
    * @return Airport
    */
   public uk.co.tui.book.domain.lite.Port populateAirport(final AirportModel airportModel)
   {
      final uk.co.tui.book.domain.lite.Port airport = new uk.co.tui.book.domain.lite.Port();
      airport.setCode(airportModel.getCode());
      airport.setName(airportModel.getName());
      return airport;
   }
   
   /**
    * To populate the Flight to the Leg object.
    *
    * @param eachLeg the each leg
    * @param carrier the flight model
    */
   private void populateFlight(final Leg eachLeg, final Carrier carrier)
   {
      if (eachLeg.getCarrier().isDreamLine())
      {
         carrier.setEquipementType(EquipmentType.DREAMLINEAR787);
      }
   }
   
   /**
    * To populate the CarrierInformation to the Leg object.
    *
    * @param eachLeg the each leg
    * @param carrierInfoModel the carrier info model
    * @param carrier
    */
   /**
    * To populate the CarrierInformation to the Leg object.
    *
    * @param eachLeg the each leg
    * @param carrierInfoModel the carrier info model
    * @param carrier
    */
   private void populateCarrierInformation(final Leg eachLeg,
      final CarrierInformation carrierInfoModel, final Carrier carrier)
   {
      final uk.co.portaltech.travel.model.results.Flight flight = eachLeg.getCarrier();
      final String flightNumber = getCarrierNumber(flight.getCode());
      final String carrierCode =
         getActualCarrierCode(flight.getCarrier(), getCarrierCode(flight.getCode()));
      carrierInfoModel.setMarketingAirlineName(getAirlinesName(carrierCode));
      carrierInfoModel.setMarketingAirlineCode(carrierCode);
      carrier.setCode(carrierCode);
      carrier.setNumber(flightNumber);
   }
   
   /**
    * @param carrierCode
    * @return
    */
   private String getAirlinesName(final String carrierCode)
   {
      if (StringUtils.equalsIgnoreCase(carrierCode, "TOM"))
      {
         return "Thomson Airways";
      }
      return carrierCode;
   }
   
   /**
    * @param inventoryCarrierCode
    * @param derivedCarrierCode
    * @return
    */
   private String getActualCarrierCode(final String inventoryCarrierCode,
      final String derivedCarrierCode)
   {
      if (StringUtils.isEmpty(inventoryCarrierCode)
         || !StringUtils.equals(inventoryCarrierCode, derivedCarrierCode))
      {
         return derivedCarrierCode;
      }
      return inventoryCarrierCode;
   }
   
   /*
    * The previous way of taking carrier code was wrong. It was strictly assuming that the carrier
    * name consists of 3 chars. but that is wrong and implemented as below now.
    */
   
   /**
    * To get the FlightCode. The previous way of taking carrier code was wrong. It was strictly
    * assuming that the carrier name consists of 3 chars. but that is wrong and implemented as below
    * now.
    *
    * @param code the code
    */
   private static String getCarrierNumber(final String code)
   {
      
      if (StringUtils.isNumeric(String.valueOf(code.charAt(INDEX_TWO))))
      {
         return code.substring(INDEX_TWO);
      }
      return code.substring(INDEX_THREE);
   }
   
   /**
    * To get the CarrierName. The previous way of taking carrier code was wrong. It was strictly
    * assuming that the carrier name consists of 3 chars. but that is wrong and implemented as below
    * now.
    *
    * @param code the code
    */
   private static String getCarrierCode(final String code)
   {
      if (StringUtils.isNumeric(String.valueOf(code.charAt(INDEX_TWO))))
      {
         return code.substring(INDEX_ZERO, INDEX_TWO).trim();
      }
      return code.substring(INDEX_ZERO, INDEX_THREE).trim();
   }
   
   /**
    * To populate the FlightSchedule to the Leg object.
    *
    * @param eachLeg the each leg
    * @param scheduleModel the schedule model
    */
   private void populateFlightSchedule(final Leg eachLeg,
      final uk.co.tui.book.domain.lite.Schedule scheduleModel)
   {
      final Schedule schedule = eachLeg.getSchedule();
      if (isNotNull(schedule))
      {
         scheduleModel.setArrivalTime(new StringBuilder(StringUtils.leftPad(schedule.getArrTime(),
            SCHEDULE_TIME_OFFSET1)).insert(SCHEDULE_TIME_OFFSET2, ":").toString());
         scheduleModel.setDepartureTime(new StringBuilder(StringUtils.leftPad(
            schedule.getDepTime(), SCHEDULE_TIME_OFFSET1)).insert(SCHEDULE_TIME_OFFSET2, ":")
            .toString());
         scheduleModel.setArrivalDate(schedule.getArrivalDate().toDate());
         scheduleModel.setDepartureDate(schedule.getDepartureDate().toDate());
      }
   }
   
   /**
    * For populating the T rating of the accommodation.
    *
    * @param officialRating the source
    * @param target the target
    */
   private void populateRatings(final String officialRating, final Stay target)
   {
      final Rating ratingModel = new Rating();
      ratingModel.setValue(StringUtils.EMPTY);
      if (StringUtils.isNotEmpty(officialRating))
      {
         ratingModel.setValue(officialRating);
         target.setOfficialRating(ratingModel);
      }
   }
   
   /**
    * Method to return AccommodationModel from accommodation Code from PIM.
    *
    * @param accomCode the accom code
    * @return AccommodationModel
    */
   private AccommodationModel getAccommodationModel(final String accomCode)
   {
      return (AccommodationModel) productService.getProductForCode(accomCode);
   }
   
   /**
    * Method to getAirport Name from PIM.
    *
    * @param airportCode the airport code
    * @return String- airportName
    */
   private Port getAirportName(final String airportCode)
   {
      if (isNotEmpty(airportCode))
      {
         return populateAirport(airportService.getAirportByCode(airportCode));
      }
      return null;
   }
   
   /**
    * Populate pax details.
    *
    * @param rooms the rooms
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populatePaxDetails(final List<RoomDetails> rooms, final BasePackage target)
   {
      final List<Passenger> passengerList = new ArrayList<Passenger>();
      final int passengerCount = getTotalPaxCount(rooms);
      final Map<Integer, PersonType> passengerMap = getPassengerMap(rooms);
      List<Integer> childAges = getChildAges();
      for (int i = 1; i <= passengerCount; i++)
      {
         final Passenger passengerModel = new Passenger();
         passengerModel.setId(i);
         passengerModel.setType(passengerMap.get(i));
         passengerModel.setName("Passenger" + i);
         childAges = setPassengerAgeByType(passengerModel, childAges);
         passengerModel.setAddresses(new ArrayList<Address>());
         passengerModel.setExtraFacilities(new ArrayList<ExtraFacility>());
         passengerList.add(passengerModel);
      }
      target.setPassengers(passengerList);
   }
   
   /**
    * Gets passenger details from Holiday object for Flight only
    *
    * @param holiday
    * @param target TODO: Should get passenger details from Holiday object directly
    */
   @SuppressWarnings(BOXING)
   private void populatePaxDetails(final Holiday holiday, final BasePackage target)
   {
      final List<Passenger> passengerList = new ArrayList<Passenger>();
      // TODO: Should get passenger details from Holiday object directly
      final List<RoomDetails> rooms = holiday.getAccomodation().getRoomDetails();
      final int passengerCount = getTotalPaxCount(rooms);
      final Map<Integer, PersonType> passengerMap = getPassengerMap(rooms);
      List<Integer> childAges = getChildAges();
      for (int i = 1; i <= passengerCount; i++)
      {
         final Passenger passengerModel = new Passenger();
         passengerModel.setId(Integer.valueOf(i));
         passengerModel.setType(passengerMap.get(Integer.valueOf(i)));
         passengerModel.setName("Passenger" + i);
         childAges = setPassengerAgeByType(passengerModel, childAges);
         passengerModel.setAddresses(new ArrayList<Address>());
         passengerModel.setExtraFacilities(new ArrayList<ExtraFacility>());
         passengerList.add(passengerModel);
      }
      target.setPassengers(passengerList);
   }
   
   /**
    * Sets the passenger age by type.
    *
    * @param passengerModel the new passenger age by type
    */
   private List<Integer> setPassengerAgeByType(final Passenger passengerModel,
      final List<Integer> childAges)
   {
      if (passengerModel.getType() == PersonType.CHILD
         || passengerModel.getType() == PersonType.INFANT)
      {
         return setAgeOfPassenger(passengerModel, childAges);
      }
      if (passengerModel.getType() == PersonType.ADULT)
      {
         passengerModel.setAge(ADULT_AGE);
         return childAges;
      }
      return childAges;
   }
   
   /**
    * Sets the age of passenger.
    *
    * @param passengerModel the new age of passenger
    */
   private List<Integer> setAgeOfPassenger(final Passenger passengerModel,
      final List<Integer> childAges)
   {
      for (final Integer age : childAges)
      {
         if (isValidChild(passengerModel, age.intValue())
            || isValidInfant(passengerModel, age.intValue()))
         {
            passengerModel.setAge(age);
            childAges.remove(age);
            return childAges;
         }
      }
      return childAges;
   }
   
   /**
    * Checks if is valid infant.
    *
    * @param passengerModel the passenger model
    * @param age the age
    * @return true, if is valid infant
    */
   private boolean isValidInfant(final Passenger passengerModel, final int age)
   {
      return passengerModel.getType() == PersonType.INFANT && age < INDEX_TWO;
   }
   
   /**
    * Checks if is valid child.
    *
    * @param passengerModel the passenger model
    * @param age the age
    * @return true, if is valid child
    */
   private boolean isValidChild(final Passenger passengerModel, final int age)
   {
      return passengerModel.getType() == PersonType.CHILD && age >= INDEX_TWO;
   }
   
   /**
    * Method to create a map of passenger id with PassengerType.
    *
    * @param rooms the rooms
    * @return passengerMap
    */
   @SuppressWarnings(BOXING)
   private Map<Integer, PersonType> getPassengerMap(final List<RoomDetails> rooms)
   {
      
      final int adultCount = getAdultCount(rooms);
      final int childCount = getChildCount(rooms);
      final int totalCount = adultCount + childCount + getInfantCount(rooms);
      final Map<Integer, PersonType> passengerMap = new HashMap<Integer, PersonType>();
      for (int id = 1; id <= totalCount; id++)
      {
         addPassengerToMap(adultCount, childCount, passengerMap, id);
      }
      return passengerMap;
   }
   
   /**
    * Adds the passenger to map.
    *
    * @param adultCount the adult count
    * @param childCount the child count
    * @param passengerMap the passenger map
    * @param id the id
    */
   private void addPassengerToMap(final int adultCount, final int childCount,
      final Map<Integer, PersonType> passengerMap, final int id)
   {
      if (id <= adultCount)
      {
         passengerMap.put(Integer.valueOf(id), PersonType.ADULT);
      }
      else if (id > (adultCount) && id <= (adultCount + childCount))
      {
         passengerMap.put(Integer.valueOf(id), PersonType.CHILD);
      }
      else
      {
         passengerMap.put(Integer.valueOf(id), PersonType.INFANT);
      }
   }
   
   /**
    * Gets the total pax count.
    *
    * @param rooms the rooms
    * @return the total pax count
    */
   private int getTotalPaxCount(final List<RoomDetails> rooms)
   {
      int paxCount = 0;
      for (final RoomDetails room : rooms)
      {
         paxCount += room.getOccupancy().getAdults();
         paxCount += room.getOccupancy().getChildren();
         paxCount += room.getOccupancy().getInfant();
      }
      return paxCount;
   }
   
   /**
    * Gets the adult count.
    *
    * @param rooms the rooms
    * @return the adult count
    */
   private int getAdultCount(final List<RoomDetails> rooms)
   {
      int adultCount = 0;
      for (final RoomDetails room : rooms)
      {
         adultCount += room.getOccupancy().getAdults();
      }
      return adultCount;
   }
   
   /**
    * Gets the child count.
    *
    * @param rooms the rooms
    * @return the child count
    */
   private int getChildCount(final List<RoomDetails> rooms)
   {
      int childCount = 0;
      for (final RoomDetails room : rooms)
      {
         childCount += room.getOccupancy().getChildren();
      }
      return childCount;
   }
   
   /**
    * Gets the child count.
    *
    * @param rooms the rooms
    * @return the child count
    */
   private int getInfantCount(final List<RoomDetails> rooms)
   {
      int childCount = 0;
      for (final RoomDetails room : rooms)
      {
         childCount += room.getOccupancy().getInfant();
      }
      return childCount;
   }
   
   /**
    * Gets the child ages.
    *
    * @return the child ages
    */
   @SuppressWarnings(BOXING)
   private List<Integer> getChildAges()
   {
      final SearchResultsRequestData resultRequestData =
         sessionService.getAttribute(LATESTCRITERIA);
      return (resultRequestData != null) ? resultRequestData.getChildrenAge()
         : new ArrayList<Integer>();
   }
   
   /**
    * Populate highlights.
    *
    * @param holiday the holiday
    * @param target the target
    */
   private void populateHighlights(final Holiday holiday, final BasePackage target)
   {
      final List<HighLights> highlights = new ArrayList<HighLights>();
      populateTransferHighlights(holiday, highlights);
      
      if (holiday.isWorldCare())
      {
         highlights.add(HighLights.WORLD_CARE_FUND);
      }
      populateMultiCenter(holiday, highlights);
      target.setListOfHighlights(highlights);
   }
   
   /**
    * Populate multi center.
    *
    * @param holiday the holiday
    * @param highlights the highlights
    */
   private void populateMultiCenter(final Holiday holiday, final List<HighLights> highlights)
   {
      final AccommodationModel accommodationModel =
         getAccommodationModel(holiday.getAccomodation().getAccomCode());
      if (linkedItemService.isMultiCentre(accommodationModel))
      {
         highlights.add(HighLights.MULTI_CENTRE);
      }
      
   }
   
   /**
    * Update Highlights
    *
    * @param highlights
    * @param holidayType
    */
   private void updateHighlightsIfLapland(final String holidayType,
      final List<HighLights> highlights)
   {
      if (StringUtils.isNotEmpty(holidayType) && "DAYTRIP".equalsIgnoreCase(holidayType))
      {
         highlights.add(HighLights.LAPLAND_DAYTRIP);
      }
   }
   
   /**
    * Populate transfer highlights.
    *
    * @param holiday the holiday
    * @param highlights the highlights
    */
   private void populateTransferHighlights(final Holiday holiday, final List<HighLights> highlights)
   {
      if (StringUtils.equalsIgnoreCase(holiday.getOffer().getCoachTransfer(), YES))
      {
         highlights.add(HighLights.COACH_TRANSFER);
      }
      if (StringUtils.equalsIgnoreCase(holiday.getOffer().getFreeCarHire(), YES))
      {
         highlights.add(HighLights.FREE_CAR_HIRE);
      }
   }
   
   /**
    * Populate Booking Details
    *
    * @param target the base package
    *
    */
   private void populateBookingDetails(final BasePackage target)
   {
      
      final BookingDetails bookingDetails = new BookingDetails();
      final BookingHistory bookingHistory = new BookingHistory();
      final List<BookingHistory> bookingHistorylist = new ArrayList<BookingHistory>();
      final WebAgent webagent = new WebAgent();
      webagent.setAgentType(AgentType.WEB);
      final SalesChannel salesChannel = new SalesChannel();
      salesChannel.setSalesChannelType(SalesChannelType.WEB);
      
      bookingHistory.setBookingType(BookingType.NEW);
      bookingHistory.setSalesChannel(salesChannel);
      bookingHistory.setAgent(webagent);
      
      bookingHistorylist.add(bookingHistory);
      bookingDetails.setBookingHistory(bookingHistorylist);
      
      target.setBookingDetails(bookingDetails);
      
   }
   
}
