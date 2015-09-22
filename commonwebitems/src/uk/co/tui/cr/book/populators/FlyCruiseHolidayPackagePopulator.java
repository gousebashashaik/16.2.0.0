/**
 *
 */
package uk.co.tui.cr.book.populators;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static uk.co.portaltech.commons.DateUtils.currentdateTime;
import static uk.co.portaltech.commons.DateUtils.subtractDates;
import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;
import static uk.co.tui.cr.book.constants.BookFlowConstants.BOXING;
import static uk.co.tui.cr.book.constants.BookFlowConstants.BRAND_NAME;
import static uk.co.tui.cr.book.constants.BookFlowConstants.CHANNEL;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.results.InventoryDetails;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.model.results.Schedule;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.productrange.MainstreamProductRangeService;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageSailingInfo;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;
import uk.co.portaltech.travel.thirdparty.endeca.constants.EndecasearchConstants;
import uk.co.portaltech.tui.model.RoomAvailabilityIndicatorModel;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.AgentType;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.BookingType;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Cabin;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.Discount;
import uk.co.tui.book.domain.lite.EquipmentType;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.Flight;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.FlyCruiseHolidayPackage;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Memo;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.Rating;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.SalesChannel;
import uk.co.tui.book.domain.lite.SalesChannelType;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.domain.lite.SupplierProductDetails;
import uk.co.tui.book.domain.lite.WebAgent;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.impl.DefaultPackageIdService;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.cr.book.constants.SessionObjectKeys;
import uk.co.tui.cruise.search.data.MainSearchCriteria;
import uk.co.tui.util.CatalogAndCategoryUtil;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author ramkishore.p
 *
 */
public class FlyCruiseHolidayPackagePopulator implements Populator<Object, BasePackage>
{

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** Offset1. */
   private static final int SCHEDULE_TIME_OFFSET1 = 4;

   /** Offset2. */
   private static final int SCHEDULE_TIME_OFFSET2 = 2;

   /** The t Rating of the accommodation. */
   private static final String T_RATING = "tRating";

   private static final int INDEX_ZERO = 0;

   private static final int INDEX_TWO = 2;

   private static final int INDEX_THREE = 3;

   /** The Constant YES. */
   private static final String YES = "Y";

   /** The Airport Service . */
   @Resource
   private AirportService airportService;

   @Resource
   private CurrencyResolver currencyResolver;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;

   /** The tui utility service. */
   @Resource
   private TuiUtilityService tuiUtilityService;

   /** The DefaultPackageIdService. */
   @Resource
   private DefaultPackageIdService packageIdService;

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private CMSSiteService cmsSiteService;

   /** The Feature Service . */
   @Resource
   private FeatureService featureService;

   @Resource
   private BrandUtils brandUtils;

   /** The productRangeService. */
   @Resource
   private MainstreamProductRangeService productRangeService;

   /** The catalog and category util. */
   @Resource
   private CatalogAndCategoryUtil catalogAndCategoryUtil;


   /*
    * (non-Javadoc)
    *
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final Object source, final BasePackage target) throws ConversionException
   {
      final PackageItemValue packageItemValue = (PackageItemValue) source;

      final AccommodationModel shipModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(packageItemValue.getPckShip()
            .getCode(), cmsSiteService.getCurrentCatalogVersion());

      populateBookingDetails(target);

      final List<String> featureCodes = new ArrayList<String>();
      featureCodes.add("holiday_type");
      featureCodes.add(T_RATING);
      final Map<String, String> featureValueMap =
         featureService.getFirstValueForFeaturesAsStrings(featureCodes, shipModel, new Date(),
            brandUtils.getFeatureServiceBrand(shipModel.getBrands()));

      populateStayType(shipModel, target);

      final Stay stay = packageComponentService.getStay(target);

      populateRatings(featureValueMap.get(T_RATING), stay);

      populateStay(packageItemValue, stay);

      populateHighlights(packageItemValue, target);

      target.setDuration(Integer.valueOf(packageItemValue.getPckSailingInfo().get(0)
         .getCruiseDuration()));

      populateItinerary(packageItemValue, target);

      populatePaxDetails(packageItemValue, target);

      populatePrice(packageItemValue, target);

      populateDiscount(packageItemValue, target);

      populateRoomDetails(packageItemValue, target);

      target.setBrandType(Brand.CR);

      populateSiteBrand(target);

      populateInventoryType(target);

      addFlightPriceToFirstRoom(packageItemValue, target);

      populateInventory(packageItemValue, target);

      populateProductType(shipModel, target);

      target.setExtraFacilityCategories(new ArrayList<ExtraFacilityCategory>());
      target.setMemos(new ArrayList<Memo>());
      target.setId(packageIdService.getPackageId(target));

   }

   /**
    * Populate available accomodation Type
    *
    * @param shipModel the AccommodationModel
    * @param target the BasePackage
    */
   private void populateStayType(final AccommodationModel shipModel, final BasePackage target)
   {
      final Stay stay = new Cruise();
      stay.setType(StayType.valueOf(shipModel.getType().toString()));
      ((FlyCruiseHolidayPackage) target).setStay(stay);
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
    * Populate stay.
    *
    * @param packageItemValue the packageItemValue
    * @param stay the stay
    */
   private void populateStay(final PackageItemValue packageItemValue, final Stay stay)
   {

      stay.setCode(packageItemValue.getPckShip().getCode());
      ((Cruise) stay).setJourneyCode(packageItemValue.getCruiseId());
      ((Cruise) stay)
         .setJourneyName(packageItemValue.getPckItineraries().get(INDEX_ZERO).getName());
      final Price price = new Price();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getTotalPrice())));
      stay.setPrice(price);
      stay.setStartDate(packageSailingInfo.getDeptDate().toDate());
      stay.setEndDate(populateAccomEndDate(packageSailingInfo));
      stay.setDuration(packageSailingInfo.getCruiseDuration());
      populateInventoryDetails(packageSailingInfo.getMulitCenterUnits().get(0)
         .getInventoryDetails(), stay);
   }

   /**
    * To set the atcom details.
    *
    * @param inventoryDetails the inventory details
    * @param target the target
    */
   private void populateInventoryDetails(final InventoryDetails inventoryDetails, final Stay target)
   {
      final SupplierProductDetails supplierDetails = new SupplierProductDetails();
      supplierDetails.setPromoCode(inventoryDetails.getProm());
      supplierDetails.setProductCode(inventoryDetails.getAtcomId());
      supplierDetails.setSellingCode(inventoryDetails.getSellingCode());
      supplierDetails.setSupplierNumber(null);
      target.setSupplierProductDetails(supplierDetails);
   }

   /**
    * Populate highlights.
    *
    * @param packageItemValue the packageItemValue
    * @param target the target
    */
   private void populateHighlights(final PackageItemValue packageItemValue, final BasePackage target)
   {
      final List<HighLights> highlights = new ArrayList<HighLights>();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      populateTransferHighlights(packageSailingInfo, highlights);
      if (packageSailingInfo.isWorldCare())
      {
         highlights.add(HighLights.WORLD_CARE_FUND);
      }
      target.setListOfHighlights(highlights);
   }

   private void populateSiteBrand(final BasePackage target)
   {
      target.setSiteBrand(tuiUtilityService.getSiteBrand());
   }

   /**
    * Gets passenger details from Holiday object for Flight only
    *
    * @param packageItemValue
    * @param target
    */
   @SuppressWarnings(BOXING)
   private void populatePaxDetails(final PackageItemValue packageItemValue, final BasePackage target)
   {
      final List<Passenger> passengerList = new ArrayList<Passenger>();
      final List<RoomDetails> rooms =
         packageItemValue.getPckSailingInfo().get(0).getMulitCenterUnits().get(0).getRoomDetails();
      final Map<Integer, PersonType> passengerMap = getPassengerMap(rooms);
      List<Integer> childAges = getChildAges();
      for (final RoomDetails eachRoomDetails : rooms)

      {

         for (final PaxDetail eachPax : eachRoomDetails.getOccupancy().getPaxDetail())

         {

            final Passenger passengerModel = new Passenger();

            passengerModel.setId(Integer.valueOf(eachPax.getId()));

            passengerModel.setType(passengerMap.get(Integer.valueOf(eachPax.getId())));

            passengerModel.setName("Passenger" + eachPax.getId());

            childAges = setAgeOfPassenger(passengerModel , childAges, Integer.valueOf(eachPax.getAge()));

            passengerModel.setAddresses(new ArrayList<Address>());

            passengerModel.setExtraFacilities(new ArrayList<ExtraFacility>());

            passengerList.add(passengerModel);

         }

      }
      Collections.sort(passengerList, new Comparator<Passenger>()
      {
         @Override
         public int compare(final Passenger passenger1, final Passenger passenger2)
         {
            return passenger1.getId().intValue() - passenger2.getId().intValue();
         }
      });
      target.setPassengers(passengerList);

   }

   /**
    * Gets the child ages.
    *
    * @return the child ages
    */
   @SuppressWarnings(BOXING)
   private List<Integer> getChildAges()
   {
      final MainSearchCriteria mainSearchCriteria =
               sessionService.getAttribute(EndecasearchConstants.MAIN_SEARCH_CRITERIA);
            return (mainSearchCriteria != null) ? mainSearchCriteria.getChildAges()
               : new ArrayList<Integer>();
   }

   private List<Integer> setAgeOfPassenger(final Passenger passengerModel,
      final List<Integer> childAges, Integer eachAge)
   {
      for (final Integer age : childAges)
      {
         if (isValidInfant(passengerModel, age.intValue()))
         {
            passengerModel.setAge(age);
            childAges.remove(age);
            return childAges;
         }else{
            passengerModel.setAge(eachAge);
         }
      }
      if(childAges.isEmpty()){
         passengerModel.setAge(eachAge);
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
    * To populate RoomDetails for the package.
    *
    * @param packageItemValue the packageItemValue
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateRoomDetails(final PackageItemValue packageItemValue,
      final BasePackage target)
   {
      final List<Room> rooms = new ArrayList<Room>();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      final List<RoomDetails> roomDetailsList =
         packageSailingInfo.getMulitCenterUnits().get(0).getRoomDetails();
      boolean boardUpgraded = false;
      final String altBoard = (String) sessionService.getAttribute(SessionObjectKeys.ALT_BOARD);
      BoardBasisDataResponse altBoardBasisSelected = null;

      // if alternate board is chosen, get it from session
      if (StringUtils.isNotEmpty(altBoard)
         && (!StringUtils.equalsIgnoreCase(altBoard, roomDetailsList.get(0).getBoardBasisCode())))
      {
         final List<BoardBasisDataResponse> boardBasisDataResponses =
            new ArrayList<BoardBasisDataResponse>();
         boardBasisDataResponses.addAll(packageSailingInfo.getBoardInfo());
         altBoardBasisSelected = getSelectedBoardBasis(boardBasisDataResponses, altBoard);
         // this is a fix to the multiple window navigation
         // where the board upgrade would lead to technical difficulties page
         boardUpgraded = isBoardUpgraded(boardUpgraded, altBoardBasisSelected);
      }

      final int daystoDepart =
         getDaysToDeparture(packageSailingInfo.getDefaultFlight().getDepartureTime());

      populateRooms(target, rooms, roomDetailsList, boardUpgraded, altBoardBasisSelected,
         daystoDepart);

      if (boardUpgraded)
      {
         rooms
            .get(0)
            .getPrice()
            .getAmount()
            .setAmount(
               rooms
                  .get(0)
                  .getPrice()
                  .getAmount()
                  .getAmount()
                  .add(
                     new BigDecimal(altBoardBasisSelected.getPrice()).subtract(new BigDecimal(
                        packageSailingInfo.getPackagePrice().getTotalPrice()))));

      }
      packageComponentService.getStay(target).setRooms(rooms);

   }

   /**
    * Populate rooms.
    *
    * @param target the target
    * @param rooms the rooms
    * @param roomDetailsList the room details list
    * @param boardUpgraded the board upgraded
    * @param altBoardBasisSelected the alt board basis selected
    * @param daystoDepart the daysto depart
    */
   private void populateRooms(final BasePackage target, final List<Room> rooms,
      final List<RoomDetails> roomDetailsList, final boolean boardUpgraded,
      final BoardBasisDataResponse altBoardBasisSelected, final int daystoDepart)
   {
      populateaAllRooms(target, rooms, roomDetailsList, boardUpgraded, altBoardBasisSelected,
         daystoDepart);
   }

   /**
    * Populate rooms.
    *
    * @param target the target
    * @param rooms the rooms
    * @param roomDetailsList the room details list
    * @param boardUpgraded the board upgraded
    * @param altBoardBasisSelected the alt board basis selected
    * @param daystoDepart the daysto depart
    */
   private void populateaAllRooms(final BasePackage target, final List<Room> rooms,
      final List<RoomDetails> roomDetailsList, final boolean boardUpgraded,
      final BoardBasisDataResponse altBoardBasisSelected, final int daystoDepart)
   {
      for (final RoomDetails eachRoomDetail : roomDetailsList)
      {
         final Room roomModel = new Cabin();
         configurePassengers(eachRoomDetail, roomModel, target.getPassengers());
         populateRoomDetail(rooms, boardUpgraded, altBoardBasisSelected, daystoDepart,
            eachRoomDetail, roomModel);
      }
   }

   /**
    * Populate room details.
    *
    * @param rooms the rooms
    * @param boardUpgraded the board upgraded
    * @param altBoardBasisSelected the alt board basis selected
    * @param daystoDepart the daysto depart
    * @param eachRoomDetail the each room detail
    * @param roomModel the room model
    */
   @SuppressWarnings(BOXING)
   private void populateRoomDetail(final List<Room> rooms, final boolean boardUpgraded,
      final BoardBasisDataResponse altBoardBasisSelected, final int daystoDepart,
      final RoomDetails eachRoomDetail, final Room roomModel)
   {
      uk.co.tui.book.domain.lite.BoardBasis boardBasisModel = null;

      populateRoom(eachRoomDetail, roomModel, daystoDepart);
      if (isNotNull(eachRoomDetail))
      {
         boardBasisModel = new uk.co.tui.book.domain.lite.BoardBasis();
         if (boardUpgraded)
         {
            boardBasisModel.setCode(altBoardBasisSelected.getBoardbasisCode());
            boardBasisModel.setDescription(boardBasisProperty.getValue(altBoardBasisSelected
               .getBoardbasisCode() + ".value"));
            boardBasisModel.setDefaultBoard(true);
         }
         else
         {
            boardBasisModel.setCode(eachRoomDetail.getBoardBasisCode());
            boardBasisModel.setDescription(boardBasisProperty.getValue(eachRoomDetail
               .getBoardBasisCode() + ".value"));
         }
         roomModel.setBoardBasis(boardBasisModel);
         roomModel.setQuantity(Integer.valueOf(eachRoomDetail.getNoOfRooms()));
      }
      rooms.add(roomModel);
   }

   /**
    * Populate room.
    *
    * @param eachRoomDetail the each room detail
    * @param roomModel the room model
    * @param daystoDepart the daysto depart
    */
   private void populateRoom(final RoomDetails eachRoomDetail, final Room roomModel,
      final int daystoDepart)
   {
      final RoomAvailabilityIndicatorModel roomAvailabilityIndicatorModel =
         new RoomAvailabilityIndicatorModel();
      roomAvailabilityIndicatorModel.setDaysToDeparture(daystoDepart);
      roomAvailabilityIndicatorModel.setInventoryCount(eachRoomDetail.getNoOfRooms());
      populateRoomConfigurationData(roomAvailabilityIndicatorModel);

      roomModel.setCode(eachRoomDetail.getRoomCode());
      roomModel.setFreeKids(StringUtils.equalsIgnoreCase(eachRoomDetail.getOffer().getFreeKids(),
         "Y") ? Boolean.TRUE : Boolean.FALSE);

      final Price price = new Price();
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(eachRoomDetail.getPrice())));
      price
         .setRate(convertBigDecimalToMoneyLite(new BigDecimal(eachRoomDetail.getPricePerPerson())));
      price.setCode("P" + eachRoomDetail.getRoomCode());
      price.setCode("Room Charges");
      final List<Price> prices = new ArrayList<Price>();
      prices.add(price);
      roomModel.setPrices(prices);

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
      roomModel.setDefaultRoom(Boolean.TRUE);
   }

   /**
    * Populate channel and brand_name from local.properties.
    *
    * @param roomAvailabilityIndicatorModel the room availability indicator model
    */
   private void populateRoomConfigurationData(
      final RoomAvailabilityIndicatorModel roomAvailabilityIndicatorModel)
   {
      roomAvailabilityIndicatorModel.setChannel(getConfigValue(CHANNEL));
      roomAvailabilityIndicatorModel.setBrandName(getConfigValue(BRAND_NAME));
   }

   /**
    * To get the config value from local.properties for the config key as string.
    *
    * @param configKey the config key
    * @return config value
    */
   private String getConfigValue(final String configKey)
   {
      return Config.getString(configKey, StringUtils.EMPTY);
   }

   /**
    * This method is to configure all the passengers into each room of the accommodation.
    *
    * @param eachRoomDetail the each room detail
    * @param roomModel the room model
    * @param allPassengers the all passengers
    */
   @SuppressWarnings(BOXING)
   private void configurePassengers(final RoomDetails eachRoomDetail, final Room roomModel,
      final List<Passenger> allPassengers)
   {
      final List<Passenger> roomPassengers = new ArrayList<Passenger>();
      final List<PaxDetail> paxDetails = eachRoomDetail.getOccupancy().getPaxDetail();
      for (final PaxDetail paxDetail : paxDetails)
      {
         for (final Passenger passenger : allPassengers)
         {
            if (passenger.getId().intValue() == paxDetail.getId())
            {
               roomPassengers.add(passenger);
            }
         }
      }
      roomModel.setPassgengers(roomPassengers);

   }

   /**
    * Gets the selected board basis from the list of alternate boardbasis.
    *
    * @param bBasis the b basis
    * @param selectedBoard the selected board
    * @return the selected board basis
    */
   private BoardBasisDataResponse getSelectedBoardBasis(final List<BoardBasisDataResponse> bBasis,
      final String selectedBoard)
   {
      return (BoardBasisDataResponse) CollectionUtils.find(bBasis, new Predicate()
      {
         @Override
         public boolean evaluate(final Object bB)
         {
            return StringUtils.equalsIgnoreCase(((BoardBasisDataResponse) bB).getBoardbasisCode(),
               selectedBoard);
         }
      });
   }

   /**
    * To check whether the board is upgraded or not
    *
    * @param boardUpgraded
    * @param altBoardBasisSelected
    * @return The board upgraded boolean
    */
   private boolean isBoardUpgraded(final boolean boardUpgraded,
      final BoardBasisDataResponse altBoardBasisSelected)
   {
      return (altBoardBasisSelected != null) ? true : boardUpgraded;
   }

   /**
    * Get the No of days to Departure.
    *
    * @param departureDate the departure date
    * @return int
    */
   private int getDaysToDeparture(final DateTime departureDate)
   {
      if (isNotNull(departureDate))
      {
         return subtractDates(departureDate, currentdateTime());
      }
      return 0;
   }

   /**
    * @param packageItemValue
    * @param target
    */
   private void populatePrice(final PackageItemValue packageItemValue, final BasePackage target)
   {
      final Price price = new Price();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      price.setCode("TC");
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getTotalPrice())));
      price.setRate(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getPricePP())));
      target.setPrice(price);

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
    * Populate discount.
    *
    * @param packageItemValue the holiday
    * @param target the target
    */
   private void populateDiscount(final PackageItemValue packageItemValue, final BasePackage target)
   {
      final Discount discountModel = new Discount();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      final Price price = new Price();
      price.setCode("SAV");
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getDiscounts())));
      price.setRate(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getDiscountsPP())));
      discountModel.setPrice(price);
      target.setDiscount(discountModel);
   }

   /**
    *
    * This method adds duration with destinations arrival date
    *
    * @param packageSailingInfo
    */
   private Date populateAccomEndDate(final PackageSailingInfo packageSailingInfo)
   {
      return packageSailingInfo.getDeptDate().plusDays(packageSailingInfo.getCruiseDuration())
         .toDate();
   }

   /**
    * @param packageItemValue
    * @param target
    */
   private void populateItinerary(final PackageItemValue packageItemValue, final BasePackage target)
   {
      final Itinerary flightItinerary = new FlightItinerary();

      final List<uk.co.tui.book.domain.lite.Leg> outBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();
      final List<uk.co.tui.book.domain.lite.Leg> inBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();

      final List<Leg> outboundlegs =
         packageItemValue.getPckSailingInfo().get(0).getDefaultFlight().getOutbound();
      final List<Leg> inboundlegs =
         packageItemValue.getPckSailingInfo().get(0).getDefaultFlight().getInbound();

      populateLegModels(outboundlegs, outBoundLegModelList);
      populateLegModels(inboundlegs, inBoundLegModelList);

      flightItinerary.setOutBound(outBoundLegModelList);
      flightItinerary.setInBound(inBoundLegModelList);
      ((PackageHoliday) target).setItinerary(flightItinerary);

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
   private void populateFlightSeqCode(final FlightLeg legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getRouteCd()))
      {
         legModel.setFlightSeqCode(StringUtils.substring(eachLeg.getRouteCd(),
            CommonwebitemsConstants.SEVEN, CommonwebitemsConstants.EIGHT));
      }
   }

   /**
    * Populate transfer highlights.
    *
    * @param packageSailingInfo the packageSailingInfo
    * @param highlights the highlights
    */
   private void populateTransferHighlights(final PackageSailingInfo packageSailingInfo,
      final List<HighLights> highlights)
   {
      if (StringUtils.equalsIgnoreCase(packageSailingInfo.getOffers().getCoachTransfer(), YES))
      {
         highlights.add(HighLights.COACH_TRANSFER);
      }
      if (StringUtils.equalsIgnoreCase(packageSailingInfo.getOffers().getFreeCarHire(), YES))
      {
         highlights.add(HighLights.FREE_CAR_HIRE);
      }
   }

   /**
    * To populate the product type in to the package model.
    *
    * @param shipModel the shipModel
    * @param target the target
    */
   private void populateProductType(final AccommodationModel shipModel, final BasePackage target)
   {
      final Collection<ProductRangeModel> productRangeModels =
         productRangeService.getProductRangesForProductByBrand(shipModel,
            tuiUtilityService.getSiteReleventBrandPks(),
            catalogAndCategoryUtil.getActiveCatalogversion());
      for (final ProductRangeModel productRangeModel : productRangeModels)
      {
         target.setProductType(productRangeModel.getCode());
      }

   }

   /**
    * @param target
    */
   private void populateInventoryType(final BasePackage target)
   {
      final Inventory inventory = new Inventory();
      inventory.setInventoryType(InventoryType.ATCOM);
      target.setInventory(inventory);
   }

   /**
    * Populate flight price.
    *
    * @param packageItemValue the packageItemValue
    * @param target the target
    */
   private void addFlightPriceToFirstRoom(final PackageItemValue packageItemValue,
      final BasePackage target)
   {
      if (target.getInventory().getInventoryType().toString()
         .equalsIgnoreCase(InventoryType.ATCOM.toString()))
      {
         final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
         final BigDecimal totalPrice =
            new BigDecimal(packageSailingInfo.getPackagePrice().getTotalPrice());
         final List<RoomDetails> roomDetails =
            packageSailingInfo.getMulitCenterUnits().get(0).getRoomDetails();
         BigDecimal roomsPrice = BigDecimal.ZERO;
         for (final RoomDetails roomDetail : roomDetails)
         {
            roomsPrice = roomsPrice.add(new BigDecimal(roomDetail.getPrice()));
         }

         final Stay stay = packageComponentService.getStay(target);
         final BigDecimal flightPrice = totalPrice.subtract(roomsPrice);
         final BigDecimal firstRoomsPrice =
            stay.getRooms().get(0).getPrices().get(0).getAmount().getAmount();
         final BigDecimal updatedPrice = firstRoomsPrice.add(flightPrice);
         stay.getRooms().get(0).getPrices().get(0).getAmount().setAmount(updatedPrice);
      }

   }

   /**
    * To set the atcom details
    *
    * @param packageItemValue
    * @param target
    */
   private void populateInventory(final PackageItemValue packageItemValue, final BasePackage target)
   {
      final InventoryDetails inventoryDetails =
         packageItemValue.getPckSailingInfo().get(0).getMulitCenterUnits().get(0)
            .getInventoryDetails();
      final SupplierProductDetails supplierDetails = new SupplierProductDetails();
      supplierDetails.setPromoCode(inventoryDetails.getProm());
      supplierDetails.setProductCode(inventoryDetails.getAtcomId());
      supplierDetails.setSellingCode(inventoryDetails.getSellingCode());
      supplierDetails.setSupplierNumber(null);
      target.getInventory().setSupplierProductDetails(supplierDetails);
   }

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
