/**
 *
 */
package uk.co.tui.shortlist.populators;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;
import static uk.co.tui.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.ProductRangeService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.Discount;
import uk.co.tui.book.domain.lite.EquipmentType;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.Flight;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.FlightLeg;
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
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.domain.lite.SupplierProductDetails;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.impl.DefaultPackageIdService;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.shortlist.data.ScheduleData;
import uk.co.tui.shortlist.data.ShortlistAccomData;
import uk.co.tui.shortlist.data.ShortlistFlightData;
import uk.co.tui.shortlist.data.ShortlistHolidayData;
import uk.co.tui.shortlist.data.ShortlistLegData;
import uk.co.tui.shortlist.data.ShortlistPaxDetails;
import uk.co.tui.shortlist.data.ShortlistRoomData;
import uk.co.tui.util.CatalogAndCategoryUtil;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistHolidayPackagePopulator implements
   Populator<ShortlistHolidayData, BasePackage>
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
   
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;
   
   /** The Airport Service . */
   @Resource
   private AirportService airportService;
   
   @Resource
   private CurrencyResolver currencyResolver;
   
   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;
   
   /** The DefaultPackageIdService. */
   @Resource
   private DefaultPackageIdService packageIdService;
   
   /** The session service. */
   @Resource
   private SessionService sessionService;
   
   /** The tui utility service. */
   @Resource
   private TuiUtilityService tuiUtilityService;
   
   /** The productRangeService. */
   @Resource
   private ProductRangeService productRangeService;
   
   /** The catalog and category util. */
   @Resource
   private CatalogAndCategoryUtil catalogAndCategoryUtil;
   
   /** The Constant ADULT_AGE. */
   private static final Integer ADULT_AGE = Integer.valueOf(30);
   
   /** The Constant LATESTCRITERIA. */
   private static final String LATESTCRITERIA = "latestCriteria";
   
   /** The deposit populator. */
   @Resource
   private ShortlistHolidayDepositComponentPopulator shortlistHolidayDepositComponentPopulator;
   
   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final ShortlistHolidayData source, final BasePackage target)
      throws ConversionException
   {
      target.setPackageType(PackageType.INCLUSIVE);
      target.setDuration(Integer.valueOf(source.getDuration()));
      populatePrice(source, target);
      final AccommodationModel accomodationModel =
         getAccommodationModel(source.getAccomodation().getAccomCode());
      populateAvailableAccomodationType(accomodationModel, target);
      final Stay availableAccommodation = packageComponentService.getStay(target);
      
      populateAvailableAccomodation(source, availableAccommodation);
      
      populateItinerary(source, target);
      
      final ShortlistAccomData accomodation = source.getAccomodation();
      populatePaxDetails(accomodation.getRoomDetails(), target);
      populateRoomDetails(source, target);
      // populate brand information
      populateBrandType(accomodation, target);
      populateSiteBrand(target);
      populateInventoryType(source.getTracs(), target);
      populateInventoryInfo(source.getInventoryInfo(), target);
      populateDiscount(source, target);
      populateProductType(accomodationModel, target);
      if (target.getInventory().getInventoryType() == InventoryType.TRACS)
      {
         populateDepositComponent(source, target);
      }
      target.setExtraFacilityCategories(new ArrayList<ExtraFacilityCategory>());
      target.setMemos(new ArrayList<Memo>());
      target.setId(packageIdService.getPackageId(target));
      
   }
   
   /**
    * Populate price.
    *
    * @param holiday the holiday
    * @param target the target
    */
   private void populatePrice(final ShortlistHolidayData source, final BasePackage target)
   {
      
      final Price price = new Price();
      price.setCode("TC");
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(source.getTotalPrice())));
      price.setRate(convertBigDecimalToMoneyLite(new BigDecimal(source.getPerPersonPrice())));
      target.setPrice(price);
   }
   
   /**
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
    * @param availableAccomModel the target
    */
   private void populateAvailableAccomodation(final ShortlistHolidayData holiday,
      final Stay availableAccomModel)
   {
      final ShortlistAccomData accomodation = holiday.getAccomodation();
      final Rating officialRating = new Rating();
      availableAccomModel.setCode(accomodation.getAccomCode());
      officialRating.setValue(accomodation.getOfficialRating());
      availableAccomModel.setOfficialRating(officialRating);
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
    * This method adds duration with destinations arrival date
    *
    * @param holiday
    */
   private Date populateAccomEndDate(final ShortlistHolidayData holiday)
   {
      return holiday.getAccomodation().getAccomStartDate().plusDays(holiday.getDuration()).toDate();
   }
   
   /**
    * To populate RoomDetails for the package.
    *
    * @param holiday the holiday
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateRoomDetails(final ShortlistHolidayData holiday, final BasePackage target)
   {
      final List<Room> rooms = new ArrayList<Room>();
      final List<ShortlistRoomData> roomDetailsList = holiday.getAccomodation().getRoomDetails();
      
      for (final ShortlistRoomData eachRoomDetail : roomDetailsList)
      {
         final Room roomModel = new Room();
         
         populateBoardbasis(roomModel, eachRoomDetail);
         roomModel.setCode(eachRoomDetail.getRoomCode());
         roomModel.setFreeKids(StringUtils.equalsIgnoreCase(
            eachRoomDetail.getOffer().getFreeKids(), "Y") ? Boolean.TRUE : Boolean.FALSE);
         configurePassengers(eachRoomDetail, roomModel, target.getPassengers());
         populateRoomPrice(roomModel, eachRoomDetail);
         populateDiscount(roomModel, eachRoomDetail);
         roomModel.setDefaultRoom(Boolean.TRUE);
         rooms.add(roomModel);
      }
      packageComponentService.getStay(target).setRooms(rooms);
   }
   
   /**
    * Populate boardbasis.
    *
    * @param boardUpgraded the board upgraded
    * @param altBoardBasisSelected the alt board basis selected
    * @param roomModel the room model
    * @param eachRoomDetail the each room detail
    */
   @SuppressWarnings(BOXING)
   private void populateBoardbasis(final Room roomModel, final ShortlistRoomData eachRoomDetail)
   {
      if (isNotNull(eachRoomDetail))
      {
         final uk.co.tui.book.domain.lite.BoardBasis boardBasisModel =
            new uk.co.tui.book.domain.lite.BoardBasis();
         boardBasisModel.setCode(eachRoomDetail.getBoardBasisCode());
         boardBasisModel.setDescription(boardBasisProperty.getValue(eachRoomDetail
            .getBoardBasisCode() + ".value"));
         roomModel.setBoardBasis(boardBasisModel);
         roomModel.setQuantity(eachRoomDetail.getNoOfRooms());
      }
   }
   
   /**
    * Populate room price.
    *
    * @param roomModel the room model
    * @param eachRoomDetail the each room detail
    */
   private void populateRoomPrice(final Room roomModel, final ShortlistRoomData eachRoomDetail)
   {
      final Price price = new Price();
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(eachRoomDetail.getPrice())));
      price
         .setRate(convertBigDecimalToMoneyLite(new BigDecimal(eachRoomDetail.getPricePerPerson())));
      price.setCode("P" + eachRoomDetail.getRoomCode());
      price.setCode("Room Charges");
      final List<Price> prices = new ArrayList<Price>();
      prices.add(price);
      roomModel.setPrices(prices);
   }
   
   /**
    * Populate discount.
    *
    * @param roomModel the room model
    * @param eachRoomDetail the each room detail
    */
   private void populateDiscount(final Room roomModel, final ShortlistRoomData eachRoomDetail)
   {
      final List<Discount> discounts = new ArrayList<Discount>();
      final Discount discount = new Discount();
      final Price disc = new Price();
      disc.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(eachRoomDetail.getDiscount())));
      disc.setRate(convertBigDecimalToMoneyLite(new BigDecimal(eachRoomDetail
         .getDiscountPerPerson())));
      disc.setCode("D" + eachRoomDetail.getRoomCode());
      discount.setPrice(disc);
      discounts.add(discount);
      roomModel.setDiscounts(discounts);
   }
   
   /**
    * This method is to configure all the passengers into each room of the accommodation.
    *
    * @param eachRoomDetail the each room detail
    * @param roomModel the room model
    * @param allPassengers the all passengers
    */
   @SuppressWarnings(BOXING)
   private void configurePassengers(final ShortlistRoomData eachRoomDetail, final Room roomModel,
      final List<Passenger> allPassengers)
   {
      final List<Passenger> roomPassengers = new ArrayList<Passenger>();
      final List<ShortlistPaxDetails> paxDetails = eachRoomDetail.getOccupancy().getPaxDetail();
      for (final ShortlistPaxDetails paxDetail : paxDetails)
      {
         for (final Passenger passenger : allPassengers)
         {
            if (passenger.getId() == paxDetail.getId())
            {
               roomPassengers.add(passenger);
            }
         }
      }
      roomModel.setPassgengers(roomPassengers);
      
   }
   
   /**
    * Populate pax details.
    *
    * @param rooms the rooms
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populatePaxDetails(final List<ShortlistRoomData> rooms, final BasePackage target)
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
   private Map<Integer, PersonType> getPassengerMap(final List<ShortlistRoomData> rooms)
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
   private int getTotalPaxCount(final List<ShortlistRoomData> rooms)
   {
      int paxCount = 0;
      for (final ShortlistRoomData room : rooms)
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
   private int getAdultCount(final List<ShortlistRoomData> rooms)
   {
      int adultCount = 0;
      for (final ShortlistRoomData room : rooms)
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
   private int getChildCount(final List<ShortlistRoomData> rooms)
   {
      int childCount = 0;
      for (final ShortlistRoomData room : rooms)
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
   private int getInfantCount(final List<ShortlistRoomData> rooms)
   {
      int childCount = 0;
      for (final ShortlistRoomData room : rooms)
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
      return (resultRequestData != null) ? resultRequestData.getChildAges()
         : new ArrayList<Integer>();
   }
   
   /**
    * Populate brand type.
    *
    * @param accomodation the accomodation
    * @param target the target
    */
   private void populateBrandType(final ShortlistAccomData accomodation, final BasePackage target)
   {
      final String brand = getBrandInformation(accomodation.getBrandType());
      if (StringUtils.isNotEmpty(brand))
      {
         target.setBrandType(Brand.valueOf(brand));
      }
   }
   
   /**
    * Gets the brand information.
    *
    * @param brandType the brand code
    * @return the brand information
    */
   private String getBrandInformation(final String brandType)
   {
      if (StringUtils.equalsIgnoreCase(brandType, "F"))
      {
         return "FC";
      }
      else if (StringUtils.equalsIgnoreCase(brandType, "T"))
      {
         return "TH";
      }
      else if (StringUtils.equalsIgnoreCase(brandType, "T_F"))
      {
         return "TH_FC";
      }
      return StringUtils.EMPTY;
   }
   
   private void populateSiteBrand(final BasePackage target)
   {
      target.setSiteBrand(tuiUtilityService.getSiteBrand());
      
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
    * To populate the itinerary to the inclusive package object.
    *
    * @param holiday the holiday
    * @param target the target
    */
   
   private void populateItinerary(final ShortlistHolidayData holiday, final BasePackage target)
   {
      final Itinerary flightItineraryModel = new FlightItinerary();
      
      final List<uk.co.tui.book.domain.lite.Leg> outBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();
      final List<uk.co.tui.book.domain.lite.Leg> inBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();
      
      final List<ShortlistLegData> outboundlegs = holiday.getItinerary().getOutbound();
      final List<ShortlistLegData> inboundlegs = holiday.getItinerary().getInbound();
      
      populateLegModels(outboundlegs, outBoundLegModelList);
      populateLegModels(inboundlegs, inBoundLegModelList);
      
      flightItineraryModel.setOutBound(outBoundLegModelList);
      flightItineraryModel.setInBound(inBoundLegModelList);
      ((PackageHoliday) target).setItinerary(flightItineraryModel);
   }
   
   /**
    * To populate the LegModels.
    *
    * @param outboundlegs the legs
    * @param legModelList the leg model list
    */
   private void populateLegModels(final List<ShortlistLegData> outboundlegs,
      final List<uk.co.tui.book.domain.lite.Leg> legModelList)
   {
      final FlightLeg legModel = new FlightLeg();
      for (final ShortlistLegData eachLeg : outboundlegs)
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
         populateAtcomId(legModel, eachLeg);
         populateRouteCode(legModel, eachLeg);
         populateCycDate(legModel, eachLeg);
         populateFlightSeqCode(legModel, eachLeg);
         populateExternalInventory(legModel, eachLeg);
         setAirports(eachLeg, legModel);
         legModelList.add(legModel);
      }
   }
   
   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateFlightSeqCode(final FlightLeg legModel, final ShortlistLegData eachLeg)
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
   private void populateCycDate(final FlightLeg legModel, final ShortlistLegData eachLeg)
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
   private void populateRouteCode(final FlightLeg legModel, final ShortlistLegData eachLeg)
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
   private void populateAtcomId(final FlightLeg legModel, final ShortlistLegData eachLeg)
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
   private void populateExternalInventory(final FlightLeg legModel, final ShortlistLegData leg)
   {
      legModel.setExternalInventory(BooleanUtils.isTrue(leg.getExternalInventory()));
   }
   
   /**
    * To populate the Arrival Airport and Departure Airport to the Leg object.
    *
    * @param eachLeg the each leg
    * @param legModel the leg model
    */
   private void setAirports(final ShortlistLegData eachLeg, final FlightLeg legModel)
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
   private void populateFlight(final ShortlistLegData eachLeg, final Carrier carrier)
   {
      if (eachLeg.getCarrier().isDreamLine())
      {
         // FIXME:
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
   private void populateCarrierInformation(final ShortlistLegData eachLeg,
      final CarrierInformation carrierInfoModel, final Carrier carrier)
   {
      final ShortlistFlightData flight = eachLeg.getCarrier();
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
    * To get the FlightCode. The previous way of taking carrier code was wrong. It was strictly
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
   private void populateFlightSchedule(final ShortlistLegData eachLeg,
      final uk.co.tui.book.domain.lite.Schedule scheduleModel)
   {
      final ScheduleData schedule = eachLeg.getSchedule();
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
    * @param tracs
    * @param target
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
    * @param tracs
    * @param target
    */
   private void populateInventoryInfo(final uk.co.tui.shortlist.data.InventoryInfo tracsDetails,
      final BasePackage target)
   {
      final SupplierProductDetails supplierDetails = new SupplierProductDetails();
      if (target.getInventory().getInventoryType().equals(InventoryType.TRACS))
      {
         supplierDetails.setProductCode(tracsDetails.getProductCode());
         supplierDetails.setSubProductcode(tracsDetails.getSubProductCode());
         supplierDetails.setPrimeSubProductCode(tracsDetails.getPrimeSubProductCode());
         supplierDetails.setSellingCode(tracsDetails.getSellingCode());
         supplierDetails.setSupplierNumber(tracsDetails.getTracsPackageId().substring(1));
         supplierDetails.setPromoCode(supplierDetails.getProductCode());
      }
      else
      {
         supplierDetails.setPromoCode(tracsDetails.getProm());
         supplierDetails.setProductCode(tracsDetails.getAtcomId());
         supplierDetails.setSellingCode(tracsDetails.getSellingCode());
         supplierDetails.setSupplierNumber(null);
      }
      target.getInventory().setSupplierProductDetails(supplierDetails);
   }
   
   /**
    * Populate discount.
    *
    * @param holiday the holiday
    * @param target the target
    */
   private void populateDiscount(final ShortlistHolidayData holiday, final BasePackage target)
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
   private void populateDepositComponent(final ShortlistHolidayData holiday,
      final BasePackage target)
   {
      if (isNotNull(holiday.getDepositAmount())
         && holiday.getDepositAmount().compareTo(BigDecimal.ZERO) > 0)
      {
         shortlistHolidayDepositComponentPopulator.populate(holiday, target);
      }
   }
   
}
