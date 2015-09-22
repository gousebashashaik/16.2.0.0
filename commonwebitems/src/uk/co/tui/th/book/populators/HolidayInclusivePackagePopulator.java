/**
 *
 */

package uk.co.tui.th.book.populators;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static uk.co.portaltech.commons.DateUtils.currentdateTime;
import static uk.co.portaltech.commons.DateUtils.subtractDates;
import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;
import static uk.co.tui.th.book.constants.BookFlowConstants.BOXING;
import static uk.co.tui.th.book.constants.BookFlowConstants.BRAND_NAME;
import static uk.co.tui.th.book.constants.BookFlowConstants.CHANNEL;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.product.ProductService;
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
import uk.co.portaltech.travel.model.results.Accomodation;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.model.results.Schedule;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.productrange.MainstreamProductRangeService;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;
import uk.co.portaltech.tui.model.RoomAvailabilityIndicatorModel;
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
import uk.co.tui.book.domain.lite.Room;
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
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.services.item.LinkedItemService;
import uk.co.tui.th.book.constants.SessionObjectKeys;
import uk.co.tui.util.CatalogAndCategoryUtil;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * The Class HolidayInclusivePackagePopulator.
 *
 * @author sunilkumar.sahu
 * @version $Revision: 1.0 $
 */

public class HolidayInclusivePackagePopulator implements Populator<Object, BasePackage>
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
   @Resource(name = "thDepositPopulator")
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

   /** The Constant LATESTCRITERIA. */
   private static final String LATESTCRITERIA = "latestCriteria";

   /** The Constant YES. */
   private static final String YES = "Y";

   /** The service locator. */
   @Resource(name = "inventoryServiceLocator")
   private ServiceLocator<Populator> serviceLocator;

   @Resource(name = "mainStreamTrvelLinkedItemService")
   private LinkedItemService linkedItemService;

   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;

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
    * @param target - InclusivePackage object
    */

   @SuppressWarnings(BOXING)
   @Override
   public void populate(final Object source, final BasePackage target)
   {
      final Holiday holiday = (Holiday) source;
      target.setPackageType(PackageType.INCLUSIVE);

      final AccommodationModel accomodationModel =
         getAccommodationModel(holiday.getAccomodation().getAccomCode());
      final List<String> featureCodes = new ArrayList<String>();
      featureCodes.add("holiday_type");
      featureCodes.add(T_RATING);
      populateBookingDetails(target);
      final Map<String, String> featureValueMap =
         featureService.getFirstValueForFeaturesAsStrings(featureCodes, accomodationModel,
            new Date(), brandUtils.getFeatureServiceBrand(accomodationModel.getBrands()));
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
      populateRoomDetails(holiday, target);
      // populate brand information
      populateBrandType(accomodation, target);
      populateSiteBrand(target);
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
    * Populate brand type.
    *
    * @param accomodation the accomodation
    * @param target the target
    */
   private void populateBrandType(final Accomodation accomodation, final BasePackage target)
   {
      final String brand = getBrandInformation(accomodation.getBrandType());
      if (StringUtils.isNotEmpty(brand))
      {
         target.setBrandType(Brand.valueOf(brand));
      }
   }

   /**
    * Populate site brand.
    *
    * @param target the target
    */
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
      else if (StringUtils.equalsIgnoreCase(brandType, "E"))
      {
         return "FJ";
      }
      return StringUtils.EMPTY;
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
      availableAccomModel.setDuration(holiday.getDuration());
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
    * To populate RoomDetails for the package.
    *
    * @param holiday the holiday
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateRoomDetails(final Holiday holiday, final BasePackage target)
   {
      final List<Room> rooms = new ArrayList<Room>();
      final List<RoomDetails> roomDetailsList = holiday.getAccomodation().getRoomDetails();
      boolean boardUpgraded = false;
      final String altBoard = (String) sessionService.getAttribute(SessionObjectKeys.ALT_BOARD);
      BoardBasisDataResponse altBoardBasisSelected = null;

      // as part of US6360
      // if alternate board is chosen, get it from session
      if (StringUtils.isNotEmpty(altBoard)
         && (!StringUtils.equalsIgnoreCase(altBoard, roomDetailsList.get(0).getBoardBasisCode())))
      {
         altBoardBasisSelected = getSelectedBoardBasis(holiday.getBoardBasisData(), altBoard);
         // this is a fix to the multiple window navigation
         // where the board upgrade would lead to technical difficulties page
         boardUpgraded = isBoardUpgraded(boardUpgraded, altBoardBasisSelected);
      }

      final int daystoDepart = getDaysToDeparture(holiday.getItinerary().getDepartureTime());

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
                        holiday.getPrice()))));

      }
      packageComponentService.getStay(target).setRooms(rooms);

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
         final Room roomModel = new Room();
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
         roomModel.setQuantity(eachRoomDetail.getNoOfRooms());
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
            if (passenger.getId() == paxDetail.getId())
            {
               roomPassengers.add(passenger);
            }
         }
      }
      roomModel.setPassgengers(roomPassengers);

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
            childAges =
               setAgeOfPassenger(passengerModel, childAges, Integer.valueOf(eachPax.getAge()));
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

   private List<Integer> setAgeOfPassenger(final Passenger passengerModel,
      final List<Integer> childAges, final Integer eachAge)
   {
      for (final Integer age : childAges)
      {
         if (isValidInfant(passengerModel, age.intValue()))
         {
            passengerModel.setAge(age);
            childAges.remove(age);
            return childAges;
         }
         else
         {
            passengerModel.setAge(eachAge);
         }
      }
      if (childAges.isEmpty())
      {
         passengerModel.setAge(eachAge);
      }
      return childAges;
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
            childAges =
               setAgeOfPassenger(passengerModel, childAges, Integer.valueOf(eachPax.getAge()));
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
