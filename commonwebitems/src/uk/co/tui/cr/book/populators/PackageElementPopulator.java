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

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.MultiCenterPackageInfo;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.MultiCenterUnits;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageSailingInfo;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;
import uk.co.portaltech.tui.model.RoomAvailabilityIndicatorModel;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.tui.book.domain.lite.AgentType;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.BookingType;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Cabin;
import uk.co.tui.book.domain.lite.Discount;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Memo;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.MultiCenterHolidayPackage;
import uk.co.tui.book.domain.lite.PackageAdditionals;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.SalesChannel;
import uk.co.tui.book.domain.lite.SalesChannelType;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.SupplierProductDetails;
import uk.co.tui.book.domain.lite.WebAgent;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.impl.DefaultPackageIdService;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.cr.book.constants.SessionObjectKeys;

/**
 * @author ramkishore.p
 *
 */
public class PackageElementPopulator implements Populator<PackageItemValue, BasePackage>
{

   /** The currency resolver. */
   @Resource
   private CurrencyResolver currencyResolver;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;

   /** The tui utility service. */
   @Resource
   private TuiUtilityService tuiUtilityService;

   /** The DefaultPackageIdService. */
   @Resource
   private DefaultPackageIdService packageIdService;

   /** The Constant YES. */
   private static final String YES = "Y";

   private static final String CRUISE_STAY = "CRZ";

   private static final String HOTEL_STAY = "ACC";

   /** The Constant INDEX_ZERO. */
   private static final int INDEX_ZERO = 0;

   private static final String TOTAL_COST_CODE = "TC";

   /**
    * The DISCOUNT_CODE
    */
   private static final String DISCOUNT_CODE = "SAV";

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final PackageItemValue source, final BasePackage target)
      throws ConversionException
   {
      final PackageSailingInfo packageSailingInfo = source.getPckSailingInfo().get(INDEX_ZERO);

      populateHighlights(source, target);
      populatePrice(source, target);
      populateDiscount(source, target);

      populateCabinDetails(source, target);
      populateRoomDetails(source, target);

      populateSiteBrand(target);
      populateInventory(packageSailingInfo.getMultiCenterPackageInfo(), target);
      populateMultiCentreInfo(packageSailingInfo.getMultiCenterPackageInfo(), target);
      populatePackageAdditionals(packageSailingInfo.getMultiCenterPackageInfo(), target);

      addFlightPriceToFirstRoom(source, target);

      populateBookingDetails(target);
      populatePackageDetails(source, target);
      // TODO: Needs to remove hard coded brand type
      target.setBrandType(Brand.CR);
      target.setExtraFacilityCategories(new ArrayList<ExtraFacilityCategory>());
      target.setMemos(new ArrayList<Memo>());
      target.setId(packageIdService.getPackageId(target));
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
    * Populate price.
    *
    * @param packageItemValue the package item value
    * @param target the target
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
      price.setCode(DISCOUNT_CODE);
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getDiscounts())));
      price.setRate(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getDiscountsPP())));
      discountModel.setPrice(price);
      target.setDiscount(discountModel);
   }

   /**
    * To populate RoomDetails for the package.
    *
    * @param packageItemValue the packageItemValue
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateCabinDetails(final PackageItemValue packageItemValue,
      final BasePackage target)
   {
      final List<Room> cabins = new ArrayList<Room>();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      final List<RoomDetails> cabinDetailsList =
         getAccom(packageSailingInfo.getMulitCenterUnits(), CRUISE_STAY).getRoomDetails();
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

      populateRooms(target, cabins, cabinDetailsList, boardUpgraded, altBoardBasisSelected,
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
      packageComponentService.getCruise(target).setRooms(cabins);
   }

   /**
    * @param multiCentreData
    * @return Accom
    */
   private MultiCenterUnits getAccom(final List<MultiCenterUnits> multiCentreData,
      final String stayType)
   {
      MultiCenterUnits sourceAccom = null;
      for (final MultiCenterUnits multiCentreDatum : multiCentreData)
      {
         if (StringUtils.equalsIgnoreCase(stayType, multiCentreDatum.getStayType()))
         {
            sourceAccom = multiCentreDatum;
            break;
         }
      }
      return sourceAccom;
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
         configurePassengers(eachRoomDetail, roomModel, target.getPassengers());
         populateRoomDetail(rooms, boardUpgraded, altBoardBasisSelected, daystoDepart,
            eachRoomDetail, roomModel);
      }
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

   @SuppressWarnings(BOXING)
   private void populateRoomDetails(final PackageItemValue packageItemValue,
      final BasePackage target)
   {
      final List<Room> rooms = new ArrayList<Room>();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      final List<RoomDetails> roomDetailsList =
         getAccom(packageSailingInfo.getMulitCenterUnits(), HOTEL_STAY).getRoomDetails();
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
         daystoDepart, false);

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
    * Populate site brand.
    *
    * @param target the target
    */
   private void populateSiteBrand(final BasePackage target)
   {
      target.setSiteBrand(tuiUtilityService.getSiteBrand());
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
    * Populate inventory type.
    *
    * @param source the source
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

         final List<RoomDetails> cabinDetails =
            getAccom(packageSailingInfo.getMulitCenterUnits(), CRUISE_STAY).getRoomDetails();
         BigDecimal cabinsPrice = BigDecimal.ZERO;
         for (final RoomDetails roomDetail : cabinDetails)
         {
            cabinsPrice = cabinsPrice.add(new BigDecimal(roomDetail.getPrice()));
         }

         final List<RoomDetails> roomDetails =
            getAccom(packageSailingInfo.getMulitCenterUnits(), HOTEL_STAY).getRoomDetails();
         BigDecimal roomsPrice = BigDecimal.ZERO;
         for (final RoomDetails roomDetail : roomDetails)
         {
            roomsPrice = roomsPrice.add(new BigDecimal(roomDetail.getPrice()));
         }

         final Stay stay = packageComponentService.getAllStays(target).get(INDEX_ZERO);
         final BigDecimal accomPrice = roomsPrice.add(cabinsPrice);
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

}
