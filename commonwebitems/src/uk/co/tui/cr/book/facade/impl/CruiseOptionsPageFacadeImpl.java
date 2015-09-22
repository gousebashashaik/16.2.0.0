/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.travel.model.AniteRoomModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.services.RoomService;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.anite.response.RoomSearchResponse;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.criteria.RoomSelectionCriteria;
import uk.co.tui.book.domain.AltRoom;
import uk.co.tui.book.domain.BoardBasis;
import uk.co.tui.book.domain.Occupancy;
import uk.co.tui.book.domain.RoomDetails;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.PackageAdditionals;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.filter.FilterInput;
import uk.co.tui.book.services.ItemCodeAlertService;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.SaveHolidayService;
import uk.co.tui.book.services.inventory.AlternateRoomsService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.book.validator.LimitedAvailabilityAlertStrategy;
import uk.co.tui.cr.book.constants.SessionObjectKeys;
import uk.co.tui.cr.book.criteria.DeckPlanCriteria;
import uk.co.tui.cr.book.facade.CruiseExtraFacilityFacade;
import uk.co.tui.cr.book.facade.CruiseOptionsPageFacade;
import uk.co.tui.cr.book.facade.PackageSwapFacade;
import uk.co.tui.cr.book.filter.CabinFilter;
import uk.co.tui.cr.book.populators.RoomViewDataPopulator;
import uk.co.tui.cr.book.view.data.BoardBasisViewData;
import uk.co.tui.cr.book.view.data.CabinOptionsViewData;
import uk.co.tui.cr.book.view.data.CabinTypeViewData;
import uk.co.tui.cr.book.view.data.CabinViewData;
import uk.co.tui.cr.book.view.data.CruiseOptionsPageViewData;
import uk.co.tui.cr.book.view.data.DeckPlanViewData;
import uk.co.tui.cr.book.view.data.DeckViewData;
import uk.co.tui.cr.book.view.data.OccupancyViewData;
import uk.co.tui.cr.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.cr.book.view.data.RoomImage;
import uk.co.tui.cr.book.view.data.RoomOptionsViewData;
import uk.co.tui.cr.exception.TUIBusinessException;
import uk.co.tui.cr.exception.TUISystemException;

/**
 * The Class CruiseOptionsPageFacadeImpl.
 *
 * @author ramkishore.p
 */
public class CruiseOptionsPageFacadeImpl implements CruiseOptionsPageFacade
{

   /** Logger for CruiseOptionsPageFacadeImpl class. **/
   private static final TUILogUtils LOG = new TUILogUtils("CruiseOptionsPageFacadeImpl");

   /** The alternate rooms service. */
   @Resource
   private AlternateRoomsService alternateRoomsService;

   /** The packageCartService. */
   @Resource
   private PackageCartService packageCartService;

   /** The sessionService. */
   @Resource
   private SessionService sessionService;

   /** The room view data populator. */
   @Resource(name = "crRoomViewDataPopulator")
   private RoomViewDataPopulator roomViewDataPopulator;

   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;

   /** The Room service. */
   @Resource
   private RoomService roomService;

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** The item code alert service. */
   @Resource
   private ItemCodeAlertService itemCodeAlertService;

   /** The static content utils. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The price calculation service. */
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;

   /** The package swap facade. */
   @Resource(name = "crPackageSwapFacade")
   private PackageSwapFacade packageSwapFacade;

   /** The occupancy view data populator. */
   @Resource(name = "crOccupancyViewDataPopulator")
   private Populator<Occupancy, OccupancyViewData> occupancyViewDataPopulator;

   /** The cruise extra facility facade. */
   @Resource
   private CruiseExtraFacilityFacade cruiseExtraFacilityFacade;

   /** The save holiday service. */
   @Resource
   private SaveHolidayService saveHolidayService;

   /** The cabin filter. */
   @Resource
   private CabinFilter cabinFilter;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The occupancy view data populator. */
   @Resource
   private Populator<DeckPlanCriteria, DeckPlanViewData> deckPlanViewDataPopulator;

   /** The Constant LAI_WARNING_MESSAGE. */
   private static final String LAI_WARNING_MESSAGE = "cr_cabin_lai_warning_message";

   /** The Constant CABINS_LA_TEXT. */
   private static final String CABINS_LA_TEXT = "Cabins_LA_Text";

   /** The Constant PREMIER_SERVICE. */
   private static final String PREMIER_SERVICE = "PRS";

   /** The Constant DECK. */
   private static final String DECK = "DK";

   /** The Constant DECK_DX. */
   private static final String DECK_DX = "DX";

   /** The Constant PIPE. */
   private static final String PIPE = " | ";

   /** The Constant for underscore */
   private static final String UNDER_SCORE = "_";

   /** The Constant for white space */
   private static final String WHITE_SPACE = " ";

   /** The CRUISE_ITINERARY_TWO */
   private static final int CRUISE_ITINERARY_TWO = 2;

   private static final int TWO = 2;

   @Resource
   private LimitedAvailabilityAlertStrategy limitedAvailabilityAlertStrategy;

   /**
    * This method communicates to inventory to get the list of alternate cabins, and returns the
    * view data populated with the cabin details.
    *
    * @return the alternate cabins
    */
   @Override
   public CruiseOptionsPageViewData getAlternateCabins()
   {
      final BasePackage packageModel = getPackageModel();
      RoomSearchResponse roomSearchResponse =
         sessionService.getAttribute(SessionObjectKeys.CABIN_SEARCH_RESPONSE);
      // Check if the response is already there in the session
      if (roomSearchResponse == null)
      {
         // get alternate cabins from the inventory
         try
         {
            roomSearchResponse = alternateRoomsService.getAlternateCabins(packageModel);
         }
         catch (final BookServiceException e)
         {
            LOG.error("TUI System Exception : " + e.getMessage());
            throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
         }
      }

      // Reset Fixed centre(Hotel), package supplement prices to zero on load of page.
      resetFixedPackageComponentPricesToZero(packageModel);

      alternateRoomsService.updatePackageModelWithCabins(roomSearchResponse, false);
      sessionService.setAttribute(SessionObjectKeys.CABIN_SEARCH_RESPONSE, roomSearchResponse);
      if (!alternateRoomsService.isSufficientRoomsAvailable(roomSearchResponse))
      {
         return null;
      }

      return (CruiseOptionsPageViewData) populateCabinOptionsViewData(roomSearchResponse);
   }

   /**
    * Resets the Fixed Package Component Prices To Zero.
    *
    * @param packageModel the packageModel
    */
   private void resetFixedPackageComponentPricesToZero(final BasePackage packageModel)
   {
      if (packageComponentService.isMultiCentre(packageModel))
      {
         resetFixedCentrePriceToZero(packageModel);
         resetPackageAdditionalPriceToZero(packageModel);
      }
   }

   /**
    * Makes the Fixed centre Price To Zero.
    *
    * @param packageModel the packageModel
    */
   private void resetFixedCentrePriceToZero(final BasePackage packageModel)
   {
      Stay fixedCentreCruise = packageComponentService.getHotel(packageModel);
      if (fixedCentreCruise == null)
      {
         // This is to get the fixed(second cruise itinerary) centre for Back To Back cruise.
         fixedCentreCruise = packageComponentService.getCruise(packageModel, TWO);
      }
      for (final Room room : fixedCentreCruise.getRooms())
      {
         room.getPrice().getAmount().setAmount(BigDecimal.ZERO);
         if (null != room.getBoardBasis().getUpgradePrice())
         {
            room.getBoardBasis().getUpgradePrice().setAmount(BigDecimal.ZERO);
         }
      }
   }

   /**
    * Makes the package supplement Price To Zero.
    *
    * @param packageModel the packageModel
    */
   private void resetPackageAdditionalPriceToZero(final BasePackage packageModel)
   {
      final PackageAdditionals packageAdditionals =
         packageComponentService.getPackageAdditionals(packageModel);
      if (packageAdditionals != null)
      {
         packageAdditionals.getPrice().getAmount().setAmount(BigDecimal.ZERO);
      }
   }

   /**
    * Populates the cruiseOptionsPageViewData.
    *
    * @param roomSearchResponse the room search response
    * @return the room and board options page view data
    */
   private RoomAndBoardOptionsPageViewData populateCabinOptionsViewData(
      final RoomSearchResponse roomSearchResponse)
   {
      // get all the rooms from the response
      final Map<String, List<AltRoom>> alternateCabins = roomSearchResponse.getAlternateRooms();
      final RoomAndBoardOptionsPageViewData cruiseOptionsPageViewData =
         new CruiseOptionsPageViewData();
      final List<RoomOptionsViewData> cabinsViewData = new ArrayList<RoomOptionsViewData>();
      // iterate over each room in the response to populate the view data
      for (final Map.Entry<String, List<AltRoom>> entry : alternateCabins.entrySet())
      {

         final RoomOptionsViewData cabinOptionsViewData = new CabinOptionsViewData();

         // 1. set the cabin index
         cabinOptionsViewData.setRoomIndex(Integer.parseInt(entry.getKey()));

         // 2. get cabin type view data for the allocated cabin and the
         // alternate
         ((CabinOptionsViewData) cabinOptionsViewData)
            .setListOfCabinTypeViewData(getCabinTypeViewData(entry.getValue()));

         // 3. set the cabin occupancy
         cabinOptionsViewData.setOccupancy(getOccupancyViewData(entry.getValue()));

         // 4.add it to the list
         cabinsViewData.add(cabinOptionsViewData);
      }
      // set RoomOptionsViewData to the page view data
      cruiseOptionsPageViewData.setRoomOptionsViewData(cabinsViewData);

      cruiseOptionsPageViewData
         .setAlternateBoardBasis(populateAlternateBoardBasis(cruiseOptionsPageViewData));

      populateBoardBasisToolTips(cruiseOptionsPageViewData);

      cruiseExtraFacilityFacade.populateCabinOptionsContentViewData(cruiseOptionsPageViewData);

      return cruiseOptionsPageViewData;
   }

   /**
    * /** Populate board basis tool tips.
    *
    * @param roomAndBoardOptionsPageViewData the room and board options page view data
    */
   private void populateBoardBasisToolTips(
      final RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData)
   {
      final Map<String, String> roomContents = staticContentServ.getRoomContents();
      final List<BoardBasisViewData> boardBasisList =
         roomAndBoardOptionsPageViewData.getAlternateBoardBasis();

      for (final BoardBasisViewData boardBasis : boardBasisList)
      {
         if (roomContents.containsKey(boardBasis.getBoardBasisCode()))
         {
            boardBasis.setBoardBasisToolTip(roomContents.get(boardBasis.getBoardBasisCode()));
         }
      }
   }

   /**
    * Gets the cabin type view data.
    *
    * @param roomsList the rooms list
    * @return cabin type view data.
    */

   private List<CabinTypeViewData> getCabinTypeViewData(final List<AltRoom> roomsList)
   {
      // Group cabins by cabin type and order by price.
      final FilterInput<CabinViewData> filterInput =
         new FilterInput<CabinViewData>(getCabinViewDataForEachCabin(roomsList), new Object());
      return cabinFilter.filter(filterInput);
   }

   /**
    * Gets the room view data for each room.
    *
    * @param roomsList the rooms list
    * @return the room view data for each room
    */
   private List<CabinViewData> getCabinViewDataForEachCabin(final List<AltRoom> roomsList)
   {
      final List<CabinViewData> cabinsViewData = new ArrayList<CabinViewData>();
      AltRoom selectedRoom = null;
      for (final AltRoom room : roomsList)
      {
         if (room.isSelected())
         {
            selectedRoom = room;
         }
      }
      BigDecimal differencePrice = BigDecimal.ZERO;
      for (final AltRoom room : roomsList)
      {
         final CabinViewData cabinViewData = new CabinViewData();
         roomViewDataPopulator.populate(room, cabinViewData);
         cabinViewData.setListOfBoardBasis(populateBoardBasis(room));
         populateCabinFeatures(room, cabinViewData);
         cabinViewData.setRoomWarningMessage(getCabinLaiWarningMessage(LAI_WARNING_MESSAGE,
            String.valueOf(cabinViewData.getQuantity())));
         differencePrice = populatePriceDifference(room, selectedRoom);
         populatePerNightPerPersonPrice(getDuration(), roomsList.get(0).getOccupancy(),
            cabinViewData, differencePrice);
         cabinViewData.setDifferencePrice(CurrencyUtils.getCurrencyAppendedPrice(differencePrice
            .setScale(TWO, RoundingMode.HALF_UP)));
         cabinViewData.setMinOccupancy(room.getMinOcc());
         cabinViewData.setMaxOccupancy(room.getMaxOcc());
         cabinsViewData.add(cabinViewData);
      }
      setAlternateCabinstoDisplay(cabinsViewData);

      return cabinsViewData;
   }

   /**
    * Gets the duration.
    *
    * @return the duration.
    */
   private int getDuration()
   {
      final BasePackage basePackage = getPackageModel();
      int duration = packageComponentService.getCruise(basePackage).getDuration();
      final Stay cruiseItineraryTwo =
         packageComponentService.getCruise(basePackage, CRUISE_ITINERARY_TWO);
      if (cruiseItineraryTwo != null)
      {
         duration = duration + cruiseItineraryTwo.getDuration();
      }
      return duration;
   }

   /**
    * Populate board basis.
    *
    * @param room the room
    * @return the list
    */
   private List<BoardBasisViewData> populateBoardBasis(final AltRoom room)
   {
      final List<BoardBasisViewData> boardBasisViewData = new ArrayList<BoardBasisViewData>();
      BigDecimal priceDifference = BigDecimal.ZERO;
      BigDecimal perPersonPerNightPrice = BigDecimal.ZERO;

      for (final BoardBasis bb : room.getBoardBasis())
      {
         final BoardBasisViewData bbViewData = new BoardBasisViewData();
         bbViewData.setBoardBasisCode(bb.getCode());
         bbViewData.setSelected(bb.isSelected());
         bbViewData.setDefaultBoardBasis(bb.isDefault());
         priceDifference = alternateRoomsService.populatePriceDifference(room, bb);
         bbViewData.setDifferencePrice(priceDifference);
         bbViewData.setCurrencyAppendedDifferencePrice(CurrencyUtils
            .getCurrencyAppendedPrice(priceDifference));
         bbViewData.setBoardBasisName(boardBasisProperty.getValue("CR." + bb.getCode() + ".value"));
         perPersonPerNightPrice =
            alternateRoomsService.populatePerPersonPerNightPrice(room, priceDifference, StayType.SHIP);
         bbViewData.setPerPersonPrice(perPersonPerNightPrice);
         bbViewData.setCurrencyAppendedPerPersonPrice(CurrencyUtils
            .getCurrencyAppendedPrice(perPersonPerNightPrice));

         boardBasisViewData.add(bbViewData);
      }
      return boardBasisViewData;
   }

   /**
    * Populate room features such as min, max occupancy.
    *
    * @param room the room
    * @param roomViewData the room view data
    */
   private void populateCabinFeatures(final AltRoom room, final CabinViewData roomViewData)
   {
      final RoomDetails cabinDetails = room.getRoomDetails();
      if (StringUtils.equalsIgnoreCase(getPackageModel().getInventory().getInventoryType()
         .toString(), InventoryType.ATCOM.toString()))
      {
         populateAniteCabinDetails(room, roomViewData, cabinDetails);
      }
      roomViewData.setLimitedAvailability(limitedAvailabilityAlertStrategy.displayAlert(
         room.getQtyAvailable(), "CABIN"));
      if (roomViewData.isLimitedAvailability())
      {
         roomViewData.setLimitedAvailabilityText(staticContentServ.getCabinContents().get(
            CABINS_LA_TEXT));
      }
   }

   /**
    * Populate anite cabin details.
    *
    * @param room the room
    * @param cabinViewData the cabin view data
    * @param cabinDetails the cabin details
    */
   private void populateAniteCabinDetails(final AltRoom room, final CabinViewData cabinViewData,
      final RoomDetails cabinDetails)
   {
      final AniteRoomModel aniteRoomModel =
         roomService.getAniteRoomForCodeAndAccomCode(room.getRoomDetails().getRoomCode(), room
            .getRoomDetails().getSellingCode(), cmsSiteService.getCurrentCatalogVersion());

      if (SyntacticSugar.isNotNull(aniteRoomModel))
      {
         updateCabinViewData(room, cabinViewData, aniteRoomModel);
      }
      else
      {
         itemCodeAlertService.updateRoomsMissingContent(cabinDetails.getAccomCode(),
            cabinDetails.getRoomCode());
         LOG.debug("************ No roomModel returned for this accommodation "
            + cabinDetails.getAccomCode() + " *********");
      }
   }

   /**
    * Update cabin view data.
    *
    * @param room the room
    * @param cabinViewData the cabin view data
    * @param aniteRoomModel the anite room model
    */
   private void updateCabinViewData(final AltRoom room, final CabinViewData cabinViewData,
      final AniteRoomModel aniteRoomModel)
   {
      cabinViewData.setRoomTitle(aniteRoomModel.getShortRoomTitle());
      final Map<String, String> cabinTypeDescription = staticContentServ.getCabinTypeDescriptions();
      cabinViewData.setDescription(cabinTypeDescription.get(StringUtils.replace(
         cabinViewData.getRoomTitle(), WHITE_SPACE, UNDER_SCORE)));
      cabinViewData.setMaxOccupancy(Integer.parseInt(aniteRoomModel.getMaxOcc()));
      cabinViewData.setMinOccupancy(Integer.parseInt(aniteRoomModel.getMinOcc()));
      populateAniteCabinFeatures(room, cabinViewData);
      final List<FeatureValueSetModel> featureValueSets = aniteRoomModel.getFeatureValueSets();
      populateDeckInfo(featureValueSets, cabinViewData);
      setPremierService(featureValueSets, cabinViewData);

      cabinViewData.setRoomImage(new ArrayList<RoomImage>());
   }

   /**
    * Populate deck info.
    *
    * @param featureValueSets the feature value sets
    * @param cabinViewData the cabin view data
    */
   private void populateDeckInfo(final List<FeatureValueSetModel> featureValueSets,
      final CabinViewData cabinViewData)
   {
      for (final FeatureValueSetModel featureValueSetModel : featureValueSets)
      {
         final FeatureDescriptorModel featureDescriptorModel =
            featureValueSetModel.getFeatureDescriptor();
         if (isDeckInfoRequired(featureDescriptorModel))
         {
            populateDeckViewData(cabinViewData, featureValueSetModel);
         }
      }

   }

   private boolean isDeckInfoRequired(final FeatureDescriptorModel featureDescriptorModel)
   {
      return (SyntacticSugar.isNotNull(featureDescriptorModel))
         && (StringUtils.startsWith(featureDescriptorModel.getCode(), DECK) || StringUtils
            .startsWith(featureDescriptorModel.getCode(), DECK_DX));
   }

   /**
    * Populate deck view data.
    *
    * @param cabinViewData the cabin view data
    * @param featureValueSetModel the feature value set model
    */
   private void populateDeckViewData(final CabinViewData cabinViewData,
      final FeatureValueSetModel featureValueSetModel)
   {
      final List<DeckViewData> deckViewDatas = new ArrayList<DeckViewData>();
      StringBuilder deckNo = new StringBuilder();
      for (final FeatureValueModel featureValueModel : featureValueSetModel.getFeatureValues())
      {
         final DeckViewData deckViewData = new DeckViewData();
         deckViewData.setDeckNo((String) featureValueModel.getValue());
         deckNo = deckNo.append(featureValueModel.getValue()).append(PIPE);
         deckViewDatas.add(deckViewData);
      }
      cabinViewData.setDeckTitle(deckNo.toString());
      cabinViewData.setDeckViewData(deckViewDatas);
   }

   /**
    * Sets the premier service.
    *
    * @param featureValueSets the feature value sets
    * @param cabinViewData the cabin view data
    */
   private void setPremierService(final List<FeatureValueSetModel> featureValueSets,
      final CabinViewData cabinViewData)
   {
      // Sets Premier Service, if included.
      for (final FeatureValueSetModel featureValueSetModel : featureValueSets)
      {
         final FeatureDescriptorModel featureDescriptorModel =
            featureValueSetModel.getFeatureDescriptor();
         if (SyntacticSugar.isNotNull(featureDescriptorModel)
            && StringUtils.equalsIgnoreCase(featureDescriptorModel.getCode(), PREMIER_SERVICE))
         {
            cabinViewData.setPremierServiceIncluded(true);
            break;
         }
      }
   }

   /**
    * Populate anite cabin features.
    *
    * @param room the room
    * @param cabinViewData the cabin view data
    */
   private void populateAniteCabinFeatures(final AltRoom room, final CabinViewData cabinViewData)
   {
      cabinViewData.setRoomFeatures(alternateRoomsService.getAniteCabinFeatures(room
         .getRoomDetails().getSellingCode(), room.getRoomDetails().getRoomCode()));

   }

   /**
    * Gets the Warning Message from content csv.
    *
    * @param key the key
    * @param value the value
    * @return the description
    */
   private String getCabinLaiWarningMessage(final String key, final String... value)
   {
      if (StringUtils.isNotEmpty(key))
      {
         final String roomContent = fetchStaticContent().get(key);
         if (SyntacticSugar.isNotNull(roomContent))
         {
            if (SyntacticSugar.isEmpty(value))
            {
               return roomContent;
            }
            return StringUtil.substitute(roomContent, value);
         }
      }
      return StringUtils.EMPTY;
   }

   /**
    * Fetches the static content on type.
    *
    * @return the map
    */
   private Map<String, String> fetchStaticContent()
   {
      return staticContentServ.getRoomContents();
   }

   /**
    * Populate price difference.
    *
    * @param room the room
    * @param selectedRoom the selected room
    * @return the big decimal
    */
   private BigDecimal populatePriceDifference(final AltRoom room, final AltRoom selectedRoom)
   {
      final BasePackage packageModel = packageCartService.getBasePackage();
      BigDecimal selectedRoomPrice =
         priceCalculationService.computeRoomCostExcludingExtras(selectedRoom, packageModel
            .getProductType(), packageSwapFacade.getExtraFacilityCategory(packageModel, null),
            packageModel.getInventory().getInventoryType());
      selectedRoomPrice = selectedRoomPrice.subtract(selectedRoom.getFlightSupplement());
      BigDecimal alternateRoomPrice =
         priceCalculationService.computeRoomCostExcludingExtras(room,
            packageModel.getProductType(), packageSwapFacade.getExtraFacilityCategory(packageModel,
               null), packageModel.getInventory().getInventoryType());
      alternateRoomPrice = alternateRoomPrice.subtract(room.getFlightSupplement());
      return alternateRoomPrice.subtract(selectedRoomPrice);
   }

   /**
    * Populate per night per person price.
    *
    * @param duration the duration
    * @param occupancy the occupancy
    * @param cabinViewData the room view data
    * @param differencePrice the room view data
    */
   private void populatePerNightPerPersonPrice(final int duration, final Occupancy occupancy,
      final CabinViewData cabinViewData, final BigDecimal differencePrice)
   {
      // get the total count of ppl allocated in the room
      // Fix DE12831:populating per person price excluding infants as Search
      // results(as suggested)
      final int roomTotalOccupancy = occupancy.getPricedAdults() + occupancy.getPricedChilds();
      final BigDecimal perPersonPrice =
         differencePrice.divide(new BigDecimal(duration * roomTotalOccupancy), TWO,
            RoundingMode.HALF_UP);
      cabinViewData.setPerPersonPrice(perPersonPrice);
      cabinViewData.setCurrencyAppendedPerPersonPrice(CurrencyUtils
         .getCurrencyAppendedPrice(perPersonPrice));
   }

   /**
    * Sets the roomsto display.
    *
    * @param cabinsViewData the new rooms to display
    */
   private void setAlternateCabinstoDisplay(final List<CabinViewData> cabinsViewData)
   {
      final BasePackage pckg = getPackageModel();
      int maxAltRooms =
         alternateRoomsService.getAlternateRoomsDisplayCount(pckg.getBrandType().toString());
      for (final CabinViewData cabinViewData : cabinsViewData)
      {
         if (maxAltRooms > 0)
         {
            cabinViewData.setShouldDisplay(true);
            maxAltRooms--;
         }
         else
         {
            cabinViewData.setShouldDisplay(false);
         }
      }
   }

   /**
    * Gets the occupancy view data.
    *
    * @param rooms the rooms
    * @return the occupancy view data
    */
   private OccupancyViewData getOccupancyViewData(final List<AltRoom> rooms)
   {
      final OccupancyViewData occupancyViewData = new OccupancyViewData();
      if (CollectionUtils.isNotEmpty(rooms))
      {
         occupancyViewDataPopulator.populate(rooms.get(0).getOccupancy(), occupancyViewData);
      }
      return occupancyViewData;
   }

   /**
    * Populate alternate board basis.
    *
    * @param roomAndBoardOptionsPageViewData the room and board options page view data
    * @return the list
    */
   private List<BoardBasisViewData> populateAlternateBoardBasis(
      final RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData)
   {
      final List<BoardBasisViewData> alternateBoardBasis = new ArrayList<BoardBasisViewData>();
      for (final RoomOptionsViewData cabinOptionsViewData : roomAndBoardOptionsPageViewData
         .getRoomOptionsViewData())
      {
         populateAlternateBoardBasisAndUpdateBoardBasisPrice(alternateBoardBasis,
            cabinOptionsViewData);
      }
      return alternateBoardBasis;
   }

   /**
    * Populate alternate board basis and update board basis price.
    *
    * @param alternateBoardBasis the alternate board basis
    * @param cabinOptionsViewData the room options view data
    */
   private void populateAlternateBoardBasisAndUpdateBoardBasisPrice(
      final List<BoardBasisViewData> alternateBoardBasis,
      final RoomOptionsViewData cabinOptionsViewData)
   {
      for (final CabinTypeViewData cabinTypeViewData : ((CabinOptionsViewData) cabinOptionsViewData)
         .getListOfCabinTypeViewData())
      {
         populateAlternateBoardBasis(alternateBoardBasis, cabinOptionsViewData, cabinTypeViewData);
      }
   }

   /**
    * Populate alternate board basis.
    *
    * @param alternateBoardBasis the alternate board basis
    * @param cabinOptionsViewData the cabin options view data
    * @param cabinTypeViewData the cabin type view data
    */
   private void populateAlternateBoardBasis(final List<BoardBasisViewData> alternateBoardBasis,
      final RoomOptionsViewData cabinOptionsViewData, final CabinTypeViewData cabinTypeViewData)
   {
      for (final CabinViewData roomViewData : cabinTypeViewData.getListOfCabinViewData())
      {
         if (roomViewData.isSelected())
         {
            if (cabinOptionsViewData.getRoomIndex() == 0)
            {
               alternateBoardBasis.addAll(roomViewData.getListOfBoardBasis());
            }
            else
            {
               updateBoardBasisPrice(alternateBoardBasis, roomViewData);
            }
         }
      }
   }

   /**
    * Update board basis price.
    *
    * @param alternateBoardBasis the alternate board basis
    * @param cabinViewData the room view data
    */
   private void updateBoardBasisPrice(final List<BoardBasisViewData> alternateBoardBasis,
      final CabinViewData cabinViewData)
   {
      BigDecimal totalPriceDifference = BigDecimal.ZERO;
      BigDecimal totalPerPersonPrice = BigDecimal.ZERO;
      for (int i = 0; i < cabinViewData.getListOfBoardBasis().size(); i++)
      {
         totalPriceDifference =
            alternateBoardBasis.get(i).getDifferencePrice()
               .add(cabinViewData.getListOfBoardBasis().get(i).getDifferencePrice());
         totalPerPersonPrice = calculateTotalPriceDifference(totalPriceDifference);
         alternateBoardBasis.get(i).setDifferencePrice(totalPriceDifference);
         alternateBoardBasis.get(i).setCurrencyAppendedDifferencePrice(
            CurrencyUtils.getCurrencyAppendedPrice(totalPriceDifference));
         alternateBoardBasis.get(i).setPerPersonPrice(totalPerPersonPrice);
         alternateBoardBasis.get(i).setCurrencyAppendedPerPersonPrice(
            CurrencyUtils.getCurrencyAppendedPrice(totalPerPersonPrice));
      }
   }

   /**
    * Calculate the total price difference.
    *
    * @param totalPriceDifference the total price difference
    * @return the big decimal
    */
   private BigDecimal calculateTotalPriceDifference(final BigDecimal totalPriceDifference)
   {
      BigDecimal calculatedTotalPriceDifference = BigDecimal.ZERO;
      int numberOfPersonsInPacakge = 0;
      final BasePackage packageModel = getPackageModel();
      @SuppressWarnings("boxing")
      final int duration = packageModel.getDuration().intValue();
      for (final Passenger passenger : packageModel.getPassengers())
      {
         if (!StringUtils.equalsIgnoreCase(passenger.getType().toString(), "infant"))
         {
            numberOfPersonsInPacakge++;
         }
      }
      final BigDecimal divisor =
         BigDecimal.valueOf(duration).multiply(BigDecimal.valueOf(numberOfPersonsInPacakge));
      calculatedTotalPriceDifference =
         totalPriceDifference.divide(divisor, TWO, RoundingMode.HALF_UP);
      return calculatedTotalPriceDifference;
   }

   /**
    * Gets the package model.
    *
    * @return the package model
    */
   private BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * Update the selected cabin.
    *
    * @param cabinCriteria the cabin criteria
    * @return updated cabin
    */
   @Override
   public CruiseOptionsPageViewData updateSelectedCabin(final RoomSelectionCriteria cabinCriteria)
   {
      final BasePackage packageModel = getPackageModel();
      final RoomSearchResponse roomSearchResponse =
         sessionService.getAttribute(SessionObjectKeys.CABIN_SEARCH_RESPONSE);
      final AltRoom selectedRoom =
         alternateRoomsService.getSelectedRoom(roomSearchResponse, cabinCriteria);
      final String selectedBoard = getSelectedCabinBoard(selectedRoom);

      alternateRoomsService.updatePackageWithCabinAttributes(cabinCriteria.getRoomIndex(),
         selectedRoom, packageModel);

      final RoomSearchResponse updatedRoomSearchResponse =
         alternateRoomsService.getAlternateCabins(cabinCriteria, true);

      updateAlternateCabinsBoard(selectedBoard, updatedRoomSearchResponse);

      final RoomAndBoardOptionsPageViewData pageViewData =
         populateCabinOptionsViewData(updatedRoomSearchResponse);
      cruiseExtraFacilityFacade.populateCruiseOptionsAllView(getPackageModel(), pageViewData);

      return (CruiseOptionsPageViewData) pageViewData;
   }

   /**
    * Gets the selected cabin board.
    *
    * @param selectedRoom the selected room
    * @return String
    */
   private String getSelectedCabinBoard(final AltRoom selectedRoom)
   {
      for (final BoardBasis bb : selectedRoom.getBoardBasis())
      {
         if (bb.isSelected())
         {
            return bb.getCode();
         }
      }
      return null;
   }

   /**
    * Update alternate rooms board to the Boardbasis as the selected room's.
    *
    * @param selectedBoard the selected board
    * @param updatedRoomSearchResponse the updated room search response
    */
   private void updateAlternateCabinsBoard(final String selectedBoard,
      final RoomSearchResponse updatedRoomSearchResponse)
   {
      for (final Map.Entry<String, List<AltRoom>> entry : updatedRoomSearchResponse
         .getAlternateRooms().entrySet())
      {
         for (final AltRoom room : entry.getValue())
         {
            alternateRoomsService.updateStoreBoardBasis(room, selectedBoard);
         }
      }
   }

   /**
    * Change cabin allocation.
    *
    * @param cabinCriteria the cabin criteria
    * @return CruiseOptionsPageViewData
    * @throws TUIBusinessException the tUI business exception
    */
   @Override
   public CruiseOptionsPageViewData updateSelectedCabinAllocation(
      final RoomSelectionCriteria cabinCriteria) throws TUIBusinessException
   {

      RoomAndBoardOptionsPageViewData viewData = null;
      RoomSearchResponse roomSearchResponse;
      try
      {
         roomSearchResponse = alternateRoomsService.formAlternateCabinRequest(cabinCriteria);
      }
      catch (final BookServiceException bookServiceException)
      {
         LOG.error("TUIBusinessException : " + bookServiceException.getMessage());
         throw new TUIBusinessException(bookServiceException.getErrorCode(),
            bookServiceException.getCustomMessage(), bookServiceException);
      }
      if (alternateRoomsService.isSufficientRoomsAvailable(roomSearchResponse))
      {
         alternateRoomsService.updatePackageModelWithCabins(roomSearchResponse, true);
         sessionService.setAttribute(SessionObjectKeys.CABIN_SEARCH_RESPONSE, roomSearchResponse);
         final RoomSearchResponse updatedRoomSearchResponse =
            populateSelectedSellingCodeRoomSearchResponse(roomSearchResponse, true);
         if (!alternateRoomsService.isSufficientRoomsAvailable(updatedRoomSearchResponse))
         {
            return null;
         }

         saveHolidayService.resetHolidaySavedIndicator();
         // This itself takes care of updating quantity of LCD
         viewData = populateCabinOptionsViewData(updatedRoomSearchResponse);
         // populate all view
         cruiseExtraFacilityFacade.populateCruiseOptionsAllView(getPackageModel(), viewData);
         cruiseExtraFacilityFacade.populateCabinsForWebAnalytics(viewData);
      }
      return (CruiseOptionsPageViewData) viewData;

   }

   /**
    * Populate selected selling code room search response.
    *
    * @param roomSearchResponse the room search response
    * @param changed the changed
    * @return the room search response
    */
   private RoomSearchResponse populateSelectedSellingCodeRoomSearchResponse(
      final RoomSearchResponse roomSearchResponse, final boolean changed)
   {
      final AltRoom room = getDefaultFirstRoom(roomSearchResponse);
      final RoomSelectionCriteria roomCriteria = new RoomSelectionCriteria();
      roomCriteria.setRoomCode(room.getRoomDetails().getRoomCode());
      roomCriteria.setRoomIndex(0);
      roomCriteria.setSellingCode(room.getRoomDetails().getSellingCode());
      return alternateRoomsService.getAlternateCabins(roomCriteria, changed);
   }

   /**
    * Gets the default first room.
    *
    * @param roomSearchResponse the room search response
    * @return the default first room
    */
   private AltRoom getDefaultFirstRoom(final RoomSearchResponse roomSearchResponse)
   {

      for (final AltRoom room : roomSearchResponse.getAlternateRooms().get("0"))
      {
         if (room.isSelected())
         {
            return room;
         }
      }
      return null;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.tui.cr.book.facade.CruiseOptionsPageFacade#updateSelectedBoardBasis
    * (java.lang.String)
    */
   @Override
   public CruiseOptionsPageViewData updateSelectedBoardBasis(final String boardBasisCode)
   {
      alternateRoomsService.updateSelectedBoardBasisForCruise(boardBasisCode);
      final RoomSearchResponse roomSearchResponse =
         sessionService.getAttribute(SessionObjectKeys.CABIN_SEARCH_RESPONSE);
      saveHolidayService.resetHolidaySavedIndicator();
      final RoomAndBoardOptionsPageViewData pageViewData =
         populateCabinOptionsViewData(roomSearchResponse);
      cruiseExtraFacilityFacade.populateCruiseOptionsAllView(getPackageModel(), pageViewData);
      return (CruiseOptionsPageViewData) pageViewData;

   }

   /**
    * Gets the Deck plan.
    *
    * @param deckPlanCriteria the deck plan criteria
    * @return DeckPlanViewData
    */
   @Override
   public DeckPlanViewData getDeckPlanViewData(final DeckPlanCriteria deckPlanCriteria)
   {
      final DeckPlanViewData deckPlanViewData = new DeckPlanViewData();
      deckPlanViewDataPopulator.populate(deckPlanCriteria, deckPlanViewData);
      return deckPlanViewData;
   }

}
