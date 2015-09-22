/**
 *
 */
package uk.co.tui.cr.book.populators;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.results.InventoryDetails;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.productrange.MainstreamProductRangeService;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.MultiCenterPackageInfo;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.MultiCenterUnits;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageSailingInfo;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;
import uk.co.portaltech.tui.model.RoomAvailabilityIndicatorModel;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.book.domain.lite.AgentType;
import uk.co.tui.book.domain.lite.BackToBackCruisePackage;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.BookingType;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Cabin;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.Discount;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Memo;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.MultiCenterHolidayPackage;
import uk.co.tui.book.domain.lite.PackageAdditionals;
import uk.co.tui.book.domain.lite.Passenger;
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
import uk.co.tui.util.CatalogAndCategoryUtil;

/**
 * The Class BackToBackCruisePackagePopulator.
 *
 * @author ramkishore.p
 */
public class BackToBackCruisePackagePopulator implements Populator<Object, BasePackage>
{

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The currency resolver. */
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

   /** The accommodation service. */
   @Resource
   private AccommodationService accommodationService;

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** The Feature Service . */
   @Resource
   private FeatureService featureService;

   /** The brand utils. */
   @Resource
   private BrandUtils brandUtils;

   /** The productRangeService. */
   @Resource
   private MainstreamProductRangeService productRangeService;

   /** The catalog and category util. */
   @Resource
   private CatalogAndCategoryUtil catalogAndCategoryUtil;

   /** The cr itinerary populator. */
   @Resource
   private Populator<PackageItemValue, Itinerary> crItineraryPopulator;

   /** The cr passenger populator. */
   @Resource
   private Populator<PackageItemValue, BasePackage> crPassengerPopulator;

   /** The t Rating of the accommodation. */
   private static final String T_RATING = "tRating";

   /** The Constant INDEX_ZERO. */
   private static final int INDEX_ZERO = 0;

   /** The Constant INDEX_ONE. */
   private static final int INDEX_ONE = 1;

   /** The Constant YES. */
   private static final String YES = "Y";

   /** The Constant TOTAL_COST_CODE. */
   private static final String TOTAL_COST_CODE = "TC";

   /** The Constant DISCOUNT_CODE. */
   private static final String DISCOUNT_CODE = "SAV";

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
      final PackageSailingInfo packageSailingInfo =
         packageItemValue.getPckSailingInfo().get(INDEX_ZERO);

      populateItinerary(packageItemValue, target);
      populatePaxDetails(packageItemValue, target);
      populateStay(target, packageItemValue, packageSailingInfo);

      populateInventory(packageSailingInfo.getMultiCenterPackageInfo(), target);
      populateMultiCentreInfo(packageSailingInfo.getMultiCenterPackageInfo(), target);
      populatePackageAdditionals(packageSailingInfo.getMultiCenterPackageInfo(), target);

      populatePrice(packageSailingInfo, target);
      populateDiscount(packageSailingInfo, target);
      populateHighlights(packageItemValue, target);
      populatePackageDetails(packageItemValue, target);

      addFlightPriceToFirstRoom(packageSailingInfo, target);

      target.setBrandType(Brand.CR);
      populateBookingDetails(target);
      populateSiteBrand(target);
      target.setExtraFacilityCategories(new ArrayList<ExtraFacilityCategory>());
      target.setMemos(new ArrayList<Memo>());
      target.setId(packageIdService.getPackageId(target));
   }

   /**
    * Populates Stay.
    *
    * @param target
    * @param packageItemValue
    * @param packageSailingInfo
    */
   private void populateStay(final BasePackage target, final PackageItemValue packageItemValue,
      final PackageSailingInfo packageSailingInfo)
   {
      final List<Stay> cruises = new ArrayList<Stay>();
      final Stay cruiseItineraryOne =
         populateCruiseStay(packageItemValue,
            packageSailingInfo.getMulitCenterUnits().get(INDEX_ZERO), target);
      ((Cruise) cruiseItineraryOne).setJourneyName(packageItemValue.getPckItineraries()
         .get(INDEX_ZERO).getName());
      cruiseItineraryOne.setRooms(populateCabinDetails(packageSailingInfo, packageSailingInfo
         .getMulitCenterUnits().get(INDEX_ZERO), target.getPassengers()));
      cruises.add(cruiseItineraryOne);

      final Stay cruiseItineraryTwo =
         populateCruiseStay(packageItemValue,
            packageSailingInfo.getMulitCenterUnits().get(INDEX_ONE), target);
      ((Cruise) cruiseItineraryTwo).setJourneyName(packageItemValue.getPckItineraries()
         .get(INDEX_ONE).getName());
      cruiseItineraryTwo.setRooms(populateCabinDetails(packageSailingInfo, packageSailingInfo
         .getMulitCenterUnits().get(INDEX_ONE), target.getPassengers()));
      cruises.add(cruiseItineraryTwo);
      ((BackToBackCruisePackage) target).setCruises(cruises);
   }

   /**
    * @param target
    * @param packageItemValue
    * @param stay
    */
   private void populateFromShipModel(final BasePackage target,
      final PackageItemValue packageItemValue, final Stay stay)
   {
      final AccommodationModel shipModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(packageItemValue.getPckShip()
            .getCode(), cmsSiteService.getCurrentCatalogVersion());
      stay.setType(StayType.valueOf(shipModel.getType().toString()));
      populateRatings(getFeatureValueMap(shipModel).get(T_RATING), stay);
      populateProductType(shipModel, target);
   }

   /**
    * @param source
    * @param target
    */
   private void populatePackageDetails(final PackageItemValue source, final BasePackage target)
   {
      final MultiCenterPackageInfo packageInfo =
         source.getPckSailingInfo().get(INDEX_ZERO).getMultiCenterPackageInfo();
      packageInfo.getCenters();
      packageInfo.getCode();
      packageInfo.getDepDate();
      packageInfo.getDiscount();
      packageInfo.getDiscpp();
      packageInfo.getId();
      packageInfo.getPrice();
      packageInfo.getPricePP();
      target.setDuration(Integer.valueOf(packageInfo.getStay()));
   }

   /**
    * Gets the feature value map.
    *
    * @param shipModel the ship model
    * @return the feature value map
    */
   private Map<String, String> getFeatureValueMap(final AccommodationModel shipModel)
   {
      final List<String> featureCodes = new ArrayList<String>();
      featureCodes.add("holiday_type");
      featureCodes.add(T_RATING);
      return featureService.getFirstValueForFeaturesAsStrings(featureCodes, shipModel, new Date(),
         brandUtils.getFeatureServiceBrand(shipModel.getBrands()));
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
    * @param sourceAccom
    * @param target the stay
    * @return stay
    */
   private Stay populateCruiseStay(final PackageItemValue packageItemValue,
      final MultiCenterUnits sourceAccom, final BasePackage target)
   {
      final Stay stay = new Cruise();
      stay.setCode(packageItemValue.getPckShip().getCode());
      ((Cruise) stay).setJourneyCode(sourceAccom.getId());
      final Price price = new Price();
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(packageItemValue
         .getPckSailingInfo().get(INDEX_ZERO).getPackagePrice().getTotalPrice())));
      stay.setPrice(price);
      stay.setStartDate(sourceAccom.getStartDate().toDate());
      stay.setEndDate(populateAccomEndDate(sourceAccom));
      stay.setDuration(sourceAccom.getStay());
      stay.setType(StayType.SHIP);
      populateFromShipModel(target, packageItemValue, stay);
      populateInventoryDetails(sourceAccom.getInventoryDetails(), stay);

      return stay;
   }

   /**
    * This method adds duration with destinations arrival date.
    *
    * @param sourceAccom the package sailing info
    * @return the date
    */
   private Date populateAccomEndDate(final MultiCenterUnits sourceAccom)
   {
      return sourceAccom.getStartDate().plusDays(sourceAccom.getStay()).toDate();
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
    * Gets passenger details from Holiday object for Flight only.
    *
    * @param packageItemValue the package item value
    */
   @SuppressWarnings(BOXING)
   private void populatePaxDetails(final PackageItemValue packageItemValue, final BasePackage target)
   {
      crPassengerPopulator.populate(packageItemValue, target);
   }

   /**
    * To populate RoomDetails for the package.
    *
    * @param packageSailingInfo
    *
    * @param multiCenterUnits the packageItemValue
    * @param passengers
    * @return cabins
    */
   @SuppressWarnings(BOXING)
   private List<Room> populateCabinDetails(final PackageSailingInfo packageSailingInfo,
      final MultiCenterUnits multiCenterUnits, final List<Passenger> passengers)
   {
      final List<Room> cabins = new ArrayList<Room>();
      final List<RoomDetails> cabinDetailsList = multiCenterUnits.getRoomDetails();
      boolean boardUpgraded = false;
      final String altBoard = (String) sessionService.getAttribute(SessionObjectKeys.ALT_BOARD);
      BoardBasisDataResponse altBoardBasisSelected = null;

      // if alternate board is chosen, get it from session
      if (StringUtils.isNotEmpty(altBoard)
         && (!StringUtils.equalsIgnoreCase(altBoard, cabinDetailsList.get(0).getBoardBasisCode())))
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

      populateRooms(passengers, cabins, cabinDetailsList, boardUpgraded, altBoardBasisSelected,
         daystoDepart, true);

      if (boardUpgraded)
      {
         cabins
            .get(0)
            .getPrice()
            .getAmount()
            .setAmount(
               cabins
                  .get(0)
                  .getPrice()
                  .getAmount()
                  .getAmount()
                  .add(
                     new BigDecimal(altBoardBasisSelected.getPrice()).subtract(new BigDecimal(
                        packageSailingInfo.getPackagePrice().getTotalPrice()))));

      }
      return cabins;
   }

   /**
    * Populate rooms.
    *
    * @param rooms the rooms
    * @param roomDetailsList the room details list
    * @param boardUpgraded the board upgraded
    * @param altBoardBasisSelected the alt board basis selected
    * @param daystoDepart the daysto depart
    */
   private void populateRooms(final List<Passenger> passengers, final List<Room> rooms,
      final List<RoomDetails> roomDetailsList, final boolean boardUpgraded,
      final BoardBasisDataResponse altBoardBasisSelected, final int daystoDepart,
      final boolean isCruise)
   {
      for (final RoomDetails eachRoomDetail : roomDetailsList)
      {
         Room roomModel = null;
         if (isCruise)
         {
            roomModel = new Cabin();
         }
         else
         {
            roomModel = new Room();
         }
         configurePassengers(eachRoomDetail, roomModel, passengers);
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
    * To check whether the board is upgraded or not.
    *
    * @param boardUpgraded the board upgraded
    * @param altBoardBasisSelected the alt board basis selected
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
    * Populate price.
    *
    * @param packageSailingInfo the package item value
    * @param target the target
    */
   private void populatePrice(final PackageSailingInfo packageSailingInfo, final BasePackage target)
   {
      final Price price = new Price();
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
    * @param packageSailingInfo the holiday
    * @param target the target
    */
   private void populateDiscount(final PackageSailingInfo packageSailingInfo,
      final BasePackage target)
   {
      final Discount discountModel = new Discount();
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
    * Populate itinerary.
    *
    * @param packageItemValue the package item value
    * @param target the target
    */
   private void populateItinerary(final PackageItemValue packageItemValue, final BasePackage target)
   {
      final Itinerary flightItinerary = new FlightItinerary();
      crItineraryPopulator.populate(packageItemValue, flightItinerary);
      ((BackToBackCruisePackage) target).setItinerary(flightItinerary);
   }

   /**
    * Populate airport.
    *
    * @param airportModel the airport model
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
    * Populate inventory type.
    *
    * @param source
    *
    * @param target the target
    */
   private void populateInventory(final MultiCenterPackageInfo source, final BasePackage target)
   {
      final Inventory inventory = new Inventory();
      final SupplierProductDetails productDetails = new SupplierProductDetails();
      inventory.setInventoryType(InventoryType.ATCOM);
      productDetails.setProductCode(source.getAtcomId());
      productDetails.setSellingCode(source.getCode());
      productDetails.setPromoCode(source.getProm());
      inventory.setSupplierProductDetails(productDetails);
      target.setInventory(inventory);
   }

   /**
    * Populate multicentre info.
    *
    * @param source
    *
    * @param target the target
    */
   private void populateMultiCentreInfo(final MultiCenterPackageInfo source,
      final BasePackage target)
   {
      final SupplierProductDetails productDetails = new SupplierProductDetails();
      productDetails.setProductCode(source.getAtcomId());
      productDetails.setSellingCode(source.getCode());
      productDetails.setPromoCode(source.getProm());
      ((MultiCenterHolidayPackage) target).setSupplierProductDetails(productDetails);
      ((MultiCenterHolidayPackage) target).setItinCode(extractItinCode(source.getItin()));
   }

   /**
    * @param itin
    */
   private String extractItinCode(final String itin)
   {
      final String delimiter = "/";
      final List<String> itinCd = Arrays.asList(itin.split(delimiter));
      return itinCd.get(0);
   }

   /**
    * @param source
    * @param target
    */
   private void populatePackageAdditionals(final MultiCenterPackageInfo source,
      final BasePackage target)
   {
      final PackageAdditionals packageAdditionals = new PackageAdditionals();
      packageAdditionals.setPrices(getPackageAdditionalPrices(source));
      packageAdditionals.setDiscounts(getPackageAdditionalDiscounts(source));
      target.setPackageAdditionals(packageAdditionals);
   }

   /**
    * @param source
    */
   private List<Price> getPackageAdditionalPrices(final MultiCenterPackageInfo source)
   {
      final List<Price> prices = new ArrayList<Price>();
      prices.add(getPrice(TOTAL_COST_CODE, Double.parseDouble(source.getPrice()),
         Double.parseDouble(source.getPricePP())));
      return prices;
   }

   /**
    * @param source
    */
   private List<Discount> getPackageAdditionalDiscounts(final MultiCenterPackageInfo source)
   {
      final List<Discount> discounts = new ArrayList<Discount>();
      final Discount discount = new Discount();
      discount.setPrice(getPrice(DISCOUNT_CODE, Double.parseDouble(source.getDiscount()),
         Double.parseDouble(source.getDiscpp())));
      discounts.add(discount);
      return discounts;
   }

   /**
    * @param priceCode
    * @param amount
    * @param rate
    * @return Price
    */
   private Price getPrice(final String priceCode, final double amount, final double rate)
   {
      final Price price = new Price();
      price.setCode(priceCode);
      price.setRate(convertBigDecimalToMoneyLite(new BigDecimal(rate)));
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(amount)));
      return price;
   }

   /**
    * Populate flight price.
    *
    * @param packageSailingInfo the packageItemValue
    * @param target the target
    */
   private void addFlightPriceToFirstRoom(final PackageSailingInfo packageSailingInfo,
      final BasePackage target)
   {

      if (target.getInventory().getInventoryType().toString()
         .equalsIgnoreCase(InventoryType.ATCOM.toString()))
      {
         final BigDecimal totalPrice =
            new BigDecimal(packageSailingInfo.getPackagePrice().getTotalPrice());

         final List<RoomDetails> cruiseOneCabinDetails =
            packageSailingInfo.getMulitCenterUnits().get(INDEX_ZERO).getRoomDetails();
         BigDecimal caruiseOneCabinsPrice = BigDecimal.ZERO;
         for (final RoomDetails roomDetail : cruiseOneCabinDetails)
         {
            caruiseOneCabinsPrice =
               caruiseOneCabinsPrice.add(new BigDecimal(roomDetail.getPrice()));
         }

         final List<RoomDetails> cruiseTwoCabinDetails =
            packageSailingInfo.getMulitCenterUnits().get(INDEX_ONE).getRoomDetails();
         BigDecimal cruiseTwoCabinsPrice = BigDecimal.ZERO;
         for (final RoomDetails roomDetail : cruiseTwoCabinDetails)
         {
            cruiseTwoCabinsPrice = cruiseTwoCabinsPrice.add(new BigDecimal(roomDetail.getPrice()));
         }

         final Stay stay = packageComponentService.getAllStays(target).get(INDEX_ZERO);
         final BigDecimal accomPrice = cruiseTwoCabinsPrice.add(caruiseOneCabinsPrice);
         final BigDecimal packageAdditionalsPrice =
            new BigDecimal(packageSailingInfo.getMultiCenterPackageInfo().getPrice());
         final BigDecimal flightPrice =
            totalPrice.subtract(accomPrice.add(packageAdditionalsPrice));
         final BigDecimal firstRoomsPrice =
            stay.getRooms().get(0).getPrices().get(0).getAmount().getAmount();
         final BigDecimal updatedPrice = firstRoomsPrice.add(flightPrice);
         stay.getRooms().get(0).getPrices().get(0).getAmount().setAmount(updatedPrice);
      }
   }

   /**
    * To set the atcom details.
    *
    * @param inventoryDetails the package item value
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
    * Populate booking details.
    *
    * @param target the target
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
