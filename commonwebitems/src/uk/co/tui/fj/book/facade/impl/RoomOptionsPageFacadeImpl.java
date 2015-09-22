/**
 *
 */
package uk.co.tui.fj.book.facade.impl;

import static uk.co.tui.th.book.constants.BookFlowConstants.ROOMOPTIONS;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.travel.model.AniteRoomModel;
import uk.co.portaltech.travel.model.BoardBasisContentModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.services.RoomService;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.anite.response.RoomSearchResponse;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.criteria.RoomSelectionCriteria;
import uk.co.tui.book.domain.AltRoom;
import uk.co.tui.book.domain.BoardBasis;
import uk.co.tui.book.domain.Occupancy;
import uk.co.tui.book.domain.PackageExtraFacilityResponse;
import uk.co.tui.book.domain.RoomDetails;
import uk.co.tui.book.domain.RoomFeature;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.ItemCodeAlertService;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.PromotionalCodeValidationService;
import uk.co.tui.book.services.SaveHolidayService;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.book.services.config.BookFlowTUIConfigService;
import uk.co.tui.book.services.inventory.AlternateRoomsService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.book.validator.LimitedAvailabilityAlertStrategy;
import uk.co.tui.fj.book.ExtraFacilityUpdator;
import uk.co.tui.fj.book.constants.SessionObjectKeys;
import uk.co.tui.fj.book.facade.PackageSwapFacade;
import uk.co.tui.fj.book.facade.RoomOptionsPageFacade;
import uk.co.tui.fj.book.populators.AlertPopulator;
import uk.co.tui.fj.book.populators.PackageViewDataPopulator;
import uk.co.tui.fj.book.populators.RoomExtrasContainerPopulator;
import uk.co.tui.fj.book.populators.RoomViewDataPopulator;
import uk.co.tui.fj.book.populators.SummaryPanelUrlPopulator;
import uk.co.tui.fj.book.store.PackageExtraFacilityStore;
import uk.co.tui.fj.book.view.data.AlertViewData;
import uk.co.tui.fj.book.view.data.BoardBasisViewData;
import uk.co.tui.fj.book.view.data.ContentViewData;
import uk.co.tui.fj.book.view.data.ExtraFacilityViewDataContainer;
import uk.co.tui.fj.book.view.data.OccupancyViewData;
import uk.co.tui.fj.book.view.data.RmSltViewData;
import uk.co.tui.fj.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.fj.book.view.data.RoomImage;
import uk.co.tui.fj.book.view.data.RoomOptionsContentViewData;
import uk.co.tui.fj.book.view.data.RoomOptionsStaticContentViewData;
import uk.co.tui.fj.book.view.data.RoomOptionsViewData;
import uk.co.tui.fj.book.view.data.RoomViewData;
import uk.co.tui.fj.book.view.data.WebAnalyticsData;
import uk.co.tui.fj.exception.TUIBusinessException;
import uk.co.tui.fj.exception.TUISystemException;
import uk.co.tui.util.CurrencyUtil;

/**
 * The Class RoomOptionsPageFacadeImpl.
 *
 * @author samantha.gd
 */
public class RoomOptionsPageFacadeImpl implements RoomOptionsPageFacade
{

   /** Logger for RoomOptionsPageFacadeImpl class. **/
   private static final TUILogUtils LOG = new TUILogUtils("RoomOptionsPageFacadeImpl");

   /** The alternate rooms service. */
   @Resource
   private AlternateRoomsService alternateRoomsService;

   /** The room view data populator. */
   @Resource(name = "fjRoomViewDataPopulator")
   private RoomViewDataPopulator roomViewDataPopulator;

   /** The summary panel url populator. */
   @Resource(name = "fjSummaryPanelUrlPopulator")
   private SummaryPanelUrlPopulator summaryPanelUrlPopulator;

   /** The package view data populator. */
   @Resource(name = "fjPackageViewDataPopulator")
   private PackageViewDataPopulator packageViewDataPopulator;

   /** The extra facility updator. */
   @Resource(name = "fjExtraFacilityUpdator")
   private ExtraFacilityUpdator extraFacilityUpdator;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The occupancy view data populator. */
   @Resource(name = "fjOccupancyViewDataPopulator")
   private Populator<Occupancy, OccupancyViewData> occupancyViewDataPopulator;

   @Resource(name = "fjRoomExtrasContainerPopulator")
   private RoomExtrasContainerPopulator roomExtrasContainerPopulator;

   /** The room static content view data populator. */
   @Resource(name = "fjRoomStaticContentViewDataPopulator")
   private Populator<Object, RoomOptionsStaticContentViewData> roomStaticContentViewDataPopulator;

   /** The room options content view data populator. */
   @Resource(name = "fjRoomOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> roomOptionsContentViewDataPopulator;

   /** AlertViewData Populator . */
   @Resource(name = "fjAlertPopulator")
   private AlertPopulator alertPopulator;

   @Resource
   private PackageCartService packageCartService;

   /** The promotional code validation service. */
   @Resource
   private PromotionalCodeValidationService promotionalCodeValidationService;

   /** The static content utils. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   @Resource
   private UpdateExtraFacilityService updateExtraFacilityService;

   /** The Constant MIN_OCCUPANCY. */
   private static final String MIN_OCCUPANCY = "minOccupancy";

   /** The Constant MAX_OCCUPANCY. */
   private static final String MAX_OCCUPANCY = "maxOccupancy";

   /** The Constant room title. */
   private static final String ROOM_TITLE = "roomTitle";

   /** The Constant description. */
   private static final String DESCRIPTION = "shortDescription";

   /** The Constant USP. */
   private static final String USP = "usps";

   private static final String WEBANALYTICS = "fj_webAnalyticsData";

   /** The Constant LAI_WARNING_MESSAGE. */
   private static final String LAI_WARNING_MESSAGE = "fj_room_lai_warning_message";

   @Resource
   private SaveHolidayService saveHolidayService;

   @Resource(name = "fjPackageSwapFacade")
   private PackageSwapFacade packageSwapFacade;

   /** The price calculation service. */
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;

   /** The item code alert service. */
   @Resource
   private ItemCodeAlertService itemCodeAlertService;

   /** The Room service. */
   @Resource
   private RoomService roomService;

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private CurrencyResolver currencyResolver;

   @Resource
   private BookFlowTUIConfigService bookFlowTuiConfigService;

   @Resource
   private LimitedAvailabilityAlertStrategy limitedAvailabilityAlertStrategy;

   private static List mediaList = new ArrayList<String>();

   private static final int TWO = 2;

   static
   {

      mediaList.add("small");
      mediaList.add("medium");
      mediaList.add("large");
      mediaList.add("xlarge");
   }

   /**
    * This method communicates to inventory to get the list of alternate rooms, and returns the view
    * data populated with the room details.
    *
    * @return the alternate rooms
    */

   private RoomAndBoardOptionsPageViewData getAlternateRooms()
   {
      final BasePackage packageModel = getPackageModel();

      // this will remove the promotional code from the packageModel in
      // backward flow
      promotionalCodeValidationService.removePromotionalCode(packageModel);

      RoomSearchResponse roomSearchResponse =
         sessionService.getAttribute(SessionObjectKeys.ROOM_SEARCH_RESPONSE);

      // Check if the response is already there in the session
      if (roomSearchResponse == null)
      {
         // get alternate rooms from the inventory
         try
         {
            roomSearchResponse = alternateRoomsService.getAlternateRooms(packageModel);
         }
         catch (final BookServiceException e)
         {
            LOG.error("TUI System Exception : " + e.getMessage());
            throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
         }
      }
      final List<Feedback> feedBackList =
         alternateRoomsService.updatePackageModel(roomSearchResponse, false);
      // Use this feedback to create alerts on to the page
      sessionService.setAttribute(SessionObjectKeys.ROOM_SEARCH_RESPONSE, roomSearchResponse);

      final RoomSearchResponse updatedRoomSearchResponse =
         populateSelectedSellingCodeRoomSearchResponse(roomSearchResponse, false);

      if (!alternateRoomsService.isSufficientRoomsAvailable(updatedRoomSearchResponse))
      {
         return null;
      }
      final RoomAndBoardOptionsPageViewData pageViewData =
         populateRoomOptionsViewData(updatedRoomSearchResponse);
      List<AlertViewData> alertViewDataList = new ArrayList<AlertViewData>();
      alertViewDataList = alertPopulator.populateTotalCostAlert(alertViewDataList, feedBackList);
      pageViewData.setAlertMessages(alertViewDataList);
      // for thomson Atcom flow the board basis section has to be disabled
      return ignoreBoardBasisDataForAtcom(pageViewData);
   }

   /**
    * @param pageViewData
    * @return page view data
    */
   private RoomAndBoardOptionsPageViewData ignoreBoardBasisDataForAtcom(
      final RoomAndBoardOptionsPageViewData pageViewData)
   {
      // for thomson Atcom flow the board basis section has to be disabled
      // hence setting the flag here
      pageViewData.setEnableBoardBasisComponent(InventoryType.TRACS == packageCartService
         .getBasePackage().getInventory().getInventoryType() ? true : bookFlowTuiConfigService
         .isBoardBasisForAtcom());
      return pageViewData;
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
    * Gets the room and board options page's view data.
    *
    * @param roomSearchResponse the room search response
    * @return the room and board options page view data
    */
   private RoomAndBoardOptionsPageViewData getRoomAndBoardOptionsPageViewData(
      final RoomSearchResponse roomSearchResponse)
   {

      // Populate the RoomOptionsViewData
      final RoomAndBoardOptionsPageViewData roomAndBoardOptionsViewData =
         populateRoomOptionsViewData(roomSearchResponse);
      populateRoomOptionsContainerView(roomAndBoardOptionsViewData);
      populateRoomOptionsPackageView(getPackageModel(), roomAndBoardOptionsViewData);
      populateRoomOptionsSummaryView(roomAndBoardOptionsViewData);
      return roomAndBoardOptionsViewData;
   }

   /**
    * The method is used to populate the List of ExtraFacility as part of Item Search response.
    *
    * @param packageModel
    * @param viewData
    */
   private void populateRoomOptionsPackageView(final BasePackage packageModel,
      final RoomAndBoardOptionsPageViewData viewData)
   {
      updatePackageIntoCart();
      packageViewDataPopulator.populate(packageModel, viewData.getPackageViewData());
      extraFacilityUpdator.updatePackageViewData(packageModel, viewData.getPackageViewData());
      if (viewData.getExtraFacilityViewDataContainer() == null)
      {
         viewData.setExtraFacilityViewDataContainer(new ExtraFacilityViewDataContainer());
      }
      extraFacilityUpdator.updateContainerViewData(viewData.getPackageViewData(),
         viewData.getExtraFacilityViewDataContainer());
      populateRoomOptionsStaticContentViewData(viewData);
      populateRoomOptionsContentViewData(viewData);
   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Latecheckout response.
    *
    * @param viewData the RoomAndBoardOptionsPageViewData
    */
   private void populateRoomOptionsSummaryView(final RoomAndBoardOptionsPageViewData viewData)
   {
      summaryPanelUrlPopulator.populate(ROOMOPTIONS, viewData.getSummaryViewData());
   }

   /**
    * Populate room options container view. this containerviewdata holds the extras from which the
    * extras view data is rendered
    *
    * @param viewData the view data
    */
   private void populateRoomOptionsContainerView(final RoomAndBoardOptionsPageViewData viewData)
   {
      final String packageCode = getPackageModel().getId();
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService.getAttribute("PackageExtraFacilityStore");
      if (packageExtraFacilityStore != null)
      {

         final List<ExtraFacilityCategory> extraFacilityCategoryModelList =
            packageExtraFacilityStore.getExtraFacilityLite(packageCode);
         final ExtraFacilityViewDataContainer containerViewData =
            new ExtraFacilityViewDataContainer();

         populateRoomExtrasContainer(extraFacilityCategoryModelList, containerViewData);
         viewData.setExtraFacilityViewDataContainer(containerViewData);
      }
   }

   private void populateRoomExtrasContainer(
      final List<ExtraFacilityCategory> extraFacilityCategoryModelList,
      final ExtraFacilityViewDataContainer containerViewData)
   {
      if (CollectionUtils.isNotEmpty(extraFacilityCategoryModelList))
      {
         for (final ExtraFacilityCategory extraFacilityCategoryModel : extraFacilityCategoryModelList)
         {
            if (uk.co.tui.book.domain.lite.ExtraFacilityGroup.ACCOMMODATION
               .equals(extraFacilityCategoryModel.getExtraFacilityGroup()))
            {

               roomExtrasContainerPopulator.populate(
                  extraFacilityCategoryModel.getExtraFacilities(), containerViewData);

            }
         }
      }
   }

   /**
    * Update selected room.
    *
    * @param roomCriteria the room criteria
    * @return the room and board options page view data
    */
   @Override
   public RoomAndBoardOptionsPageViewData updateSelectedRoom(
      final RoomSelectionCriteria roomCriteria)
   {
      final BasePackage packageModel = getPackageModel();
      final RoomSearchResponse roomSearchResponse =
         sessionService.getAttribute(SessionObjectKeys.ROOM_SEARCH_RESPONSE);
      final AltRoom selectedRoom =
         alternateRoomsService.getSelectedRoom(roomSearchResponse, roomCriteria);
      final String selectedBoard = getSelectedRoomBoard(selectedRoom);
      final boolean isItemSearchRequired =
         alternateRoomsService.itemSearchReqd(roomCriteria.getSellingCode());
      alternateRoomsService.updatePackage(roomCriteria.getRoomIndex(), selectedRoom, packageModel);
      // as part of DE9460 update the package with rooms and selling code
      // before triggering Item search
      final RoomSearchResponse updatedRoomSearchResponse =
         alternateRoomsService.getAlternateRooms(roomCriteria, true);

      // as part of DE9887
      // this is to ensure the alternate rooms are also reflected with the
      // same BB if available else select the default BB
      updateAlternateRoomsBoard(selectedBoard, updatedRoomSearchResponse);

      // if there is a package change -- moved to a different selling code of
      // room
      if (isItemSearchRequired)
      {
         final PackageExtraFacilityResponse itemSearchResponse = performItemSearchAndUpdateStore();
         compareExtrasAndUpdatePackage(itemSearchResponse);
      }

      saveHolidayService.resetHolidaySavedIndicator();
      return getRoomAndBoardOptionsPageViewData(updatedRoomSearchResponse);
   }

   /**
    * Update alternate rooms board to the Boardbasis as the selected room's.
    *
    * @param selectedBoard the selected board
    * @param updatedRoomSearchResponse the updated room search response
    */
   private void updateAlternateRoomsBoard(final String selectedBoard,
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
    * @param selectedRoom
    * @return String
    */
   private String getSelectedRoomBoard(final AltRoom selectedRoom)
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
    * @param itemSearchResponse
    */
   private void compareExtrasAndUpdatePackage(final PackageExtraFacilityResponse itemSearchResponse)
   {
      // perform comparison of package extras and get the list of extras that
      // are matching
      final List<ExtraFacilityCategory> swappedExtraCategories =
         alternateRoomsService.getSwappedExtras(itemSearchResponse.getExtraFacilityCategoryList());
      // update the package model with the returned list of extras.
      alternateRoomsService.updatePackageWithSwappedExtras(swappedExtraCategories,
         ExtraFacilityGroup.ACCOMMODATION);
      final BasePackage packageModel = getPackageModel();
      // DE28988
      postSwapUpdation(packageModel);
      priceCalculationService.excludeDefaultExtraPriceFromRoomPrice(packageModel,
         findCorrespondingRoomsFromStore(packageComponentService.getStay(packageModel).getRooms()));

   }

   public void postSwapUpdation(final BasePackage selectedPackage)
   {
      updateExtraFacilityService.resetPassengersWithExtrafacilities();
      packageCartService.updateCart(selectedPackage);
   }

   /**
    * @return itemSearchResponse
    */
   private PackageExtraFacilityResponse performItemSearchAndUpdateStore()
   {
      PackageExtraFacilityResponse itemSearchResponse;
      try
      {
         itemSearchResponse = alternateRoomsService.performItemSearch(getPackageModel());
         updateStore(itemSearchResponse);

      }
      catch (final BookServiceException e)
      {
         LOG.error("TUISystemException : " + e.getMessage());
         throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
      }
      return itemSearchResponse;
   }

   /**
    * update the store with the new set of extras returned
    *
    * @param itemSearchResponse
    */
   private void updateStore(final PackageExtraFacilityResponse itemSearchResponse)
   {
      final PackageExtraFacilityStore packageExtraStore = new PackageExtraFacilityStore();
      packageExtraStore.add(itemSearchResponse);
      sessionService.setAttribute("PackageExtraFacilityStore", packageExtraStore);
   }

   /**
    * Populates the roomoptionsviewdata.
    *
    * @param roomSearchResponse the room search response
    * @return the room and board options page view data
    */
   private RoomAndBoardOptionsPageViewData populateRoomOptionsViewData(
      final RoomSearchResponse roomSearchResponse)
   {
      // get all the rooms from the response
      final Map<String, List<AltRoom>> alternateRooms = roomSearchResponse.getAlternateRooms();
      final RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData =
         new RoomAndBoardOptionsPageViewData();
      final List<RoomOptionsViewData> roomsViewData = new ArrayList<RoomOptionsViewData>();
      // iterate over each room in the response to populate the view data
      for (final Map.Entry<String, List<AltRoom>> entry : alternateRooms.entrySet())
      {

         final RoomOptionsViewData roomOptionsViewData = new RoomOptionsViewData();

         // 1. set the room index
         roomOptionsViewData.setRoomIndex(Integer.parseInt(entry.getKey()));

         // 2. get roomviewdata for the allocated room and the alternate
         // rooms
         roomOptionsViewData.setListOfRoomViewData(getRoomViewDataForEachRoom(entry.getValue()));

         // 3. set the room occupancy
         roomOptionsViewData.setOccupancy(getOccupancyViewData(entry.getValue()));

         // 4.add it to the list
         roomsViewData.add(roomOptionsViewData);
      }
      // set RoomOptionsViewData to the page view data
      roomAndBoardOptionsPageViewData.setRoomOptionsViewData(roomsViewData);

      roomAndBoardOptionsPageViewData
         .setAlternateBoardBasis(populateAlternateBoardBasis(roomAndBoardOptionsPageViewData));
      populateBoardBasisToolTips(roomAndBoardOptionsPageViewData);

      if (StringUtils.equalsIgnoreCase(packageComponentService.getStay(getPackageModel()).getType()
         .toString(), StayType.VILLA.toString()))
      {
         roomAndBoardOptionsPageViewData.setVillaAcommodation(true);
      }

      return roomAndBoardOptionsPageViewData;
   }

   /**
    * Populate board basis tool tips.
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
    * Populate alternate board basis.
    *
    * @param roomAndBoardOptionsPageViewData the room and board options page view data
    */
   private List<BoardBasisViewData> populateAlternateBoardBasis(
      final RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData)
   {
      final List<BoardBasisViewData> alternateBoardBasis = new ArrayList<BoardBasisViewData>();
      for (final RoomOptionsViewData roomOptionsViewData : roomAndBoardOptionsPageViewData
         .getRoomOptionsViewData())
      {
         populateAlternateBoardBasisAndUpdateBoardBasisPrice(alternateBoardBasis,
            roomOptionsViewData);
      }
      return alternateBoardBasis;
   }

   /**
    * Populate alternate board basis and update board basis price.
    *
    * @param alternateBoardBasis the alternate board basis
    * @param roomOptionsViewData the room options view data
    */
   private void populateAlternateBoardBasisAndUpdateBoardBasisPrice(
      final List<BoardBasisViewData> alternateBoardBasis,
      final RoomOptionsViewData roomOptionsViewData)
   {
      for (final RoomViewData roomViewData : roomOptionsViewData.getListOfRoomViewData())
      {
         if (roomViewData.isSelected())
         {
            if (roomOptionsViewData.getRoomIndex() == 0)
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
    * Gets the room view data for each room.
    *
    * @param roomsList the rooms list
    * @return the room view data for each room
    */
   private List<RoomViewData> getRoomViewDataForEachRoom(final List<AltRoom> roomsList)
   {
      final List<RoomViewData> roomsViewData = new ArrayList<RoomViewData>();
      AltRoom selectedRoom = null;
      for (final AltRoom room : roomsList)
      {
         if (room.isSelected())
         {
            selectedRoom = room;
         }
      }

      final Integer duration = getPackageModel().getDuration();
      BigDecimal differencePrice = BigDecimal.ZERO;
      for (final AltRoom room : roomsList)
      {
         final RoomViewData roomViewData = new RoomViewData();
         roomViewDataPopulator.populate(room, roomViewData);
         roomViewData.setListOfBoardBasis(populateBoardBasis(room));
         populateRoomFeatures(room, roomViewData);
         roomViewData.setRoomWarningMessage(getRoomLaiWarningMessage(LAI_WARNING_MESSAGE,
            String.valueOf(roomViewData.getQuantity())));
         differencePrice = populatePriceDifference(room, selectedRoom);
         populatePerNightPerPersonPrice(duration, roomsList.get(0).getOccupancy(), roomViewData,
            differencePrice);
         roomViewData
            .setDifferencePrice(CurrencyUtil.getCurrencyAppendedPrice(
               differencePrice.setScale(TWO, RoundingMode.HALF_UP),
               currencyResolver.getSiteCurrency()));
         roomsViewData.add(roomViewData);
      }
      setAlternateRoomstoDisplay(roomsViewData);
      return roomsViewData;
   }

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
    * Populate board basis.
    *
    * @param room the room
    */
   private List<BoardBasisViewData> populateBoardBasis(final AltRoom room)
   {
      final List<BoardBasisViewData> boardBasisViewData = new ArrayList<BoardBasisViewData>();
      BigDecimal priceDifference = BigDecimal.ZERO;
      BigDecimal perPersonPerNightPrice = BigDecimal.ZERO;
      final List<BoardBasisContentModel> bbContentModel =
         alternateRoomsService.getBoardBasisContentModel(room.getRoomDetails().getAccomCode());
      final String currencyCode = currencyResolver.getSiteCurrency();
      for (final BoardBasis bb : room.getBoardBasis())
      {
         final BoardBasisViewData bbViewData = new BoardBasisViewData();
         bbViewData.setBoardBasisCode(bb.getCode());
         bbViewData.setSelected(bb.isSelected());
         bbViewData.setDefaultBoardBasis(bb.isDefault());
         priceDifference = alternateRoomsService.populatePriceDifference(room, bb);
         bbViewData.setDifferencePrice(priceDifference);
         bbViewData.setCurrencyAppendedDifferencePrice(CurrencyUtil.getCurrencyAppendedPrice(
            priceDifference, currencyCode));
         bbViewData.setBoardBasisName(boardBasisProperty.getValue(bb.getCode() + ".value"));
         perPersonPerNightPrice =
            alternateRoomsService.populatePerPersonPerNightPrice(room, priceDifference);
         bbViewData.setPerPersonPrice(perPersonPerNightPrice);
         bbViewData.setCurrencyAppendedPerPersonPrice(CurrencyUtil.getCurrencyAppendedPrice(
            perPersonPerNightPrice, currencyCode));

         final BoardBasisContentModel matchingBoard =
            getMatchingBoardContent(bbContentModel,
               boardBasisProperty.getValue(bb.getCode() + ".value"));

         if (matchingBoard != null)
         {
            LOG.debug("Matching board basis : " + matchingBoard.getName());
            bbViewData.setFeatureCodesAndValues(alternateRoomsService
               .getBoardBasisFeatures(matchingBoard));
            bbViewData
               .setBoardDescription(alternateRoomsService.getBoardDescription(matchingBoard));
         }
         else
         {
            LOG.error("****************No matching boardcontent returned from PIM for "
               + bb.getCode() + "***************");
         }
         boardBasisViewData.add(bbViewData);
      }
      return boardBasisViewData;
   }

   /**
    * Gets the matching board content.
    *
    * @param bbContentModel the boardb content model
    * @param board the board
    * @return the matching board content
    */
   private BoardBasisContentModel getMatchingBoardContent(
      final List<BoardBasisContentModel> bbContentModel, final String board)
   {
      return (BoardBasisContentModel) CollectionUtils.find(bbContentModel, new Predicate()
      {
         @Override
         public boolean evaluate(final Object boardContent)
         {
            return StringUtils.equalsIgnoreCase(((BoardBasisContentModel) boardContent).getName(),
               board);
         }
      });
   }

   /**
    * Populate per night per person price.
    *
    * @param duration the duration
    * @param occupancy the occupancy
    * @param roomViewData the room view data
    * @param differencePrice the room view data
    */
   private void populatePerNightPerPersonPrice(final Integer duration, final Occupancy occupancy,
      final RoomViewData roomViewData, final BigDecimal differencePrice)
   {
      // get the total count of ppl allocated in the room
      // Fix DE12831:populating per person price excluding infants as Search
      // results(as suggested)
      final int roomTotalOccupancy = occupancy.getPricedAdults() + occupancy.getPricedChilds();
      final BigDecimal perPersonPrice =
         differencePrice.divide(new BigDecimal(duration.intValue() * roomTotalOccupancy), TWO,
            RoundingMode.HALF_UP);
      roomViewData.setPerPersonPrice(perPersonPrice);
      roomViewData.setCurrencyAppendedPerPersonPrice(CurrencyUtil.getCurrencyAppendedPrice(
         perPersonPrice, currencyResolver.getSiteCurrency()));
   }

   /**
    * Sets the roomsto display.
    *
    * @param roomsViewData the new rooms to display
    */
   private void setAlternateRoomstoDisplay(final List<RoomViewData> roomsViewData)
   {
      final BasePackage pckg = getPackageModel();
      int maxAltRooms =
         alternateRoomsService.getAlternateRoomsDisplayCount(pckg.getBrandType().toString());
      for (final RoomViewData roomViewData : roomsViewData)
      {
         if (maxAltRooms > 0)
         {
            roomViewData.setShouldDisplay(true);
            maxAltRooms--;
         }
         else
         {
            roomViewData.setShouldDisplay(false);
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
    * Populate room features such as min, max occupancy.
    *
    * @param room the room
    * @param roomViewData the room view data
    */
   private void populateRoomFeatures(final AltRoom room, final RoomViewData roomViewData)

   {
      final RoomDetails roomDetails = room.getRoomDetails();
      // ATCOM inventory rooms only will have the room title from VRP
      if (StringUtils.equalsIgnoreCase(getPackageModel().getInventory().getInventoryType()
         .toString(), InventoryType.ATCOM.toString()))
      {
         populateAniteRoomDetails(room, roomViewData, roomDetails);
      }
      else
      {
         populateTracsRoomDetails(room, roomViewData, roomDetails);
      }
      roomViewData.setLimitedAvailability(limitedAvailabilityAlertStrategy.displayAlert(
         room.getQtyAvailable(), "ROOM"));
      if (roomViewData.isLimitedAvailability())
      {
         roomViewData.setLimitedAvailabilityText(alternateRoomsService.getLAIText());
      }
   }

   /**
    * @param room
    * @param roomViewData
    * @param roomDetails
    */
   private void populateAniteRoomDetails(final AltRoom room, final RoomViewData roomViewData,
      final RoomDetails roomDetails)
   {
      populateAniteRoomFeatures(room, roomViewData);
      populateAniteRoomOccupancy(room, roomViewData);
      final AniteRoomModel aniteRoomModel =
         roomService.getAniteRoomForCodeAndAccomCode(room.getRoomDetails().getRoomCode(), room
            .getRoomDetails().getSellingCode(), cmsSiteService.getCurrentCatalogVersion());
      if (SyntacticSugar.isNotNull(aniteRoomModel))
      {

         roomTitle(room, roomViewData, aniteRoomModel);
         roomViewData.setRoomImage(new ArrayList<RoomImage>());
      }
      else
      {
         itemCodeAlertService.updateRoomsMissingContent(roomDetails.getAccomCode(),
            roomDetails.getRoomCode());
         LOG.debug("************ No roomModel returned for this accommodation "
            + roomDetails.getAccomCode() + " *********");
      }
   }

   /**
    * @param room
    * @param roomViewData
    */
   private void populateAniteRoomOccupancy(final AltRoom room, final RoomViewData roomViewData)
   {
      roomViewData.setMaxOccupancy(room.getMaxOcc());
      roomViewData.setMinOccupancy(room.getMinOcc());
   }

   /**
    * @param room
    * @param roomViewData
    * @param aniteRoomModel
    */
   private void roomTitle(final AltRoom room, final RoomViewData roomViewData,
      final AniteRoomModel aniteRoomModel)
   {
      if (StringUtils.isEmpty(room.getTitle()))
      {
         room.setTitle(aniteRoomModel.getShortRoomTitle());
         roomViewData.setRoomTitle(aniteRoomModel.getShortRoomTitle());
      }
   }

   /**
    * @param room
    * @param roomViewData
    */
   private void populateAniteRoomFeatures(final AltRoom room, final RoomViewData roomViewData)
   {
      if (CollectionUtils.isNotEmpty(room.getRoomFeatures()))
      {
         final List<String> features = new ArrayList<String>();
         for (final RoomFeature roomFeature : room.getRoomFeatures())
         {
            features.add(roomFeature.getName());
         }
         roomViewData.setRoomFeatures(features);
      }
      else
      {
         roomViewData.setRoomFeatures(alternateRoomsService.getAniteRoomFeatures(room
            .getRoomDetails().getSellingCode(), room.getRoomDetails().getRoomCode()));
      }
   }

   /**
    * @param room
    * @param roomViewData
    * @param roomDetails
    */
   private void populateTracsRoomDetails(final AltRoom room, final RoomViewData roomViewData,
      final RoomDetails roomDetails)
   {
      final RoomModel roomModel =
         alternateRoomsService.getRoomModel(room, roomDetails.getAccomCode());

      if (SyntacticSugar.isNotNull(roomModel))
      {

         roomViewData.putFeatureCodesAndValues(alternateRoomsService
            .featureCodesandvalues(roomModel));
         final String maxOccupancy = roomViewData.getFeatureValue(MAX_OCCUPANCY);
         final String minOccupancy = roomViewData.getFeatureValue(MIN_OCCUPANCY);

         if (StringUtils.isNotBlank(maxOccupancy))
         {
            roomViewData.setMaxOccupancy(Integer.parseInt(maxOccupancy));
         }

         if (StringUtils.isNotBlank(minOccupancy))
         {
            roomViewData.setMinOccupancy(Integer.parseInt(minOccupancy));
         }

         roomViewData.setRoomTitle(roomViewData.getFeatureValue(ROOM_TITLE));
         roomViewData.setDescription(roomViewData.getFeatureValue(DESCRIPTION));
         roomViewData.setRoomFeatures(roomViewData.getRoomFeatureasList(USP));

         getRoomImages(roomModel.getGalleryImages(), roomViewData);

      }
      else
      {
         itemCodeAlertService.updateRoomsMissingContent(roomDetails.getAccomCode(),
            roomDetails.getRoomCode());
         LOG.debug("************ No roomModel returned for this accommodation "
            + roomDetails.getAccomCode() + " *********");
      }
   }

   /**
    * Gets the room images.
    *
    * @param galleryImages the gallery images
    * @return the room images
    */
   private List<RoomImage> getRoomImages(final Collection<MediaContainerModel> galleryImages,
      final RoomViewData roomViewData)
   {
      final List<RoomImage> roomImages = new ArrayList<RoomImage>();
      if (CollectionUtils.isNotEmpty(galleryImages))
      {
         for (final MediaContainerModel mediaContainerModel : galleryImages)
         {
            roomImagePopulate(roomViewData, roomImages, mediaContainerModel);
         }

         roomViewData.setRoomImage(roomImages);
         return roomImages;
      }
      // incase PIM has not returned any images for the current room, send a
      // dummy roomimage object
      // this would be handled from the UI end
      return roomImages;
   }

   /**
    * @param roomViewData
    * @param roomImages
    * @param mediaContainerModel
    */
   private void roomImagePopulate(final RoomViewData roomViewData,
      final List<RoomImage> roomImages, final MediaContainerModel mediaContainerModel)
   {
      for (final MediaModel media : mediaContainerModel.getMedias())
      {
         if (mediaList.contains(media.getMediaFormat().getName()))
         {
            final RoomImage roomImage = new RoomImage();
            roomImage.setAltText(media.getAltText());
            roomImage.setUrl(media.getURL());
            roomImage.setCode(media.getCode());
            roomImage.setSize(media.getMediaFormat().getName());
            roomImage.setDescription(media.getDescription());
            roomImages.add(roomImage);
         }
         else
         {

            roomViewData.setRoomAllocationImage(media.getURL());
         }

      }
      // incase PIM has not returned any images for the current room, send a
      // dummy roomimage object
      // this would be handled from the UI end

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.tui.th.book.facade.RoomOptionsPageFacade#updateSelectedRoomAllocation
    * (uk.co.tui.th.book.criteria.RoomSelectionCriteria)
    */
   @Override
   public RoomAndBoardOptionsPageViewData updateSelectedRoomAllocation(
      final RoomSelectionCriteria roomCriteria) throws TUIBusinessException
   {
      RoomAndBoardOptionsPageViewData viewData = null;
      RoomSearchResponse roomSearchResponse;
      try
      {
         roomSearchResponse = alternateRoomsService.formAlternateRoomRequest(roomCriteria);
      }
      catch (final BookServiceException bookServiceException)
      {
         LOG.error("TUIBusinessException : " + bookServiceException.getMessage());
         throw new TUIBusinessException(bookServiceException.getErrorCode(),
            bookServiceException.getCustomMessage(), bookServiceException);
      }
      if (alternateRoomsService.isSufficientRoomsAvailable(roomSearchResponse))
      {
         alternateRoomsService.updatePackageModel(roomSearchResponse, true);
         sessionService.setAttribute(SessionObjectKeys.ROOM_SEARCH_RESPONSE, roomSearchResponse);
         final RoomSearchResponse updatedRoomSearchResponse =
            populateSelectedSellingCodeRoomSearchResponse(roomSearchResponse, true);
         if (!alternateRoomsService.isSufficientRoomsAvailable(updatedRoomSearchResponse))
         {
            return null;
         }

         processPackageChange(roomCriteria.getSellingCode());

         saveHolidayService.resetHolidaySavedIndicator();
         // This itself takes care of updating quantity of LCD
         viewData = getRoomAndBoardOptionsPageViewData(updatedRoomSearchResponse);
      }
      return viewData;
   }

   /**
    * If there is a package change perform itemsearch and update the store
    *
    * @param sellingCode the selling code
    */
   private void processPackageChange(final String sellingCode)
   {
      if (alternateRoomsService.itemSearchReqd(sellingCode))
      {
         // Package changed.. So perform item search again & check if LCD
         // can be rendered or not?
         compareExtrasAndUpdatePackage(performItemSearchAndUpdateStore());
      }
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

   /**
    * Update board basis price.
    *
    * @param alternateBoardBasis the alternate board basis
    * @param roomViewData the room view data
    */
   private void updateBoardBasisPrice(final List<BoardBasisViewData> alternateBoardBasis,
      final RoomViewData roomViewData)
   {
      BigDecimal totalPriceDifference = BigDecimal.ZERO;
      BigDecimal totalPerPersonPrice = BigDecimal.ZERO;
      final String currencyCode = currencyResolver.getSiteCurrency();
      for (int i = 0; i < roomViewData.getListOfBoardBasis().size(); i++)
      {
         totalPriceDifference =
            alternateBoardBasis.get(i).getDifferencePrice()
               .add(roomViewData.getListOfBoardBasis().get(i).getDifferencePrice());
         totalPerPersonPrice = calculateTotalPriceDifference(totalPriceDifference);
         alternateBoardBasis.get(i).setDifferencePrice(totalPriceDifference);
         alternateBoardBasis.get(i).setCurrencyAppendedDifferencePrice(
            CurrencyUtil.getCurrencyAppendedPrice(totalPriceDifference, currencyCode));
         alternateBoardBasis.get(i).setPerPersonPrice(totalPerPersonPrice);
         alternateBoardBasis.get(i).setCurrencyAppendedPerPersonPrice(
            CurrencyUtil.getCurrencyAppendedPrice(totalPerPersonPrice, currencyCode));
      }
   }

   /**
    * Calculate the total price difference
    *
    * @param totalPriceDifference
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
    * Process change board basis.
    *
    * @param boardBasisCode the board basis code
    * @return the room options view data
    */
   @Override
   public RoomAndBoardOptionsPageViewData updateSelectedBoardBasis(final String boardBasisCode)
   {
      alternateRoomsService.updateSelectedBoardBasis(boardBasisCode);
      final RoomSearchResponse roomSearchResponse =
         sessionService.getAttribute(SessionObjectKeys.ROOM_SEARCH_RESPONSE);
      saveHolidayService.resetHolidaySavedIndicator();
      return getRoomAndBoardOptionsPageViewData(roomSearchResponse);
   }

   /**
    * Populate selected selling code room search response.
    *
    * @param roomSearchResponse the room search response
    * @param changed
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
      return alternateRoomsService.getAlternateRooms(roomCriteria, changed);
   }

   /**
    * Populate room options static content view data.
    *
    * @param viewData the view data
    */
   private void populateRoomOptionsStaticContentViewData(
      final RoomAndBoardOptionsPageViewData viewData)
   {
      final RoomOptionsStaticContentViewData roomOptionsStaticContentViewData =
         new RoomOptionsStaticContentViewData();
      roomStaticContentViewDataPopulator.populate(new Object(), roomOptionsStaticContentViewData);
      viewData.setRoomOptionsStaticContentViewData(roomOptionsStaticContentViewData);
   }

   /**
    * Populates the room extras content
    */
   private void populateRoomOptionsContentViewData(final RoomAndBoardOptionsPageViewData viewData)
   {
      final RoomOptionsContentViewData roomOptionsContentViewData =
         new RoomOptionsContentViewData();
      final ContentViewData roomContent = new ContentViewData();
      roomOptionsContentViewDataPopulator.populate(new Object(), roomContent);
      roomOptionsContentViewData.setRoomContentViewData(roomContent);
      viewData.setRoomOptionsContentViewData(roomOptionsContentViewData);

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.tui.th.book.facade.RoomOptionsPageFacade#populateRoomsForWebAnalytics
    * (uk.co.tui.th.book.view.data.RoomAndBoardOptionsPageViewData)
    */
   @Override
   public void populateRoomsForWebAnalytics(
      final RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData)
   {

      WebAnalyticsData target = sessionService.getAttribute(WEBANALYTICS);
      if (SyntacticSugar.isNull(target))
      {
         target = new WebAnalyticsData();
      }
      final List<RoomViewData> roomViewDataList =
         roomAndBoardOptionsPageViewData.getPackageViewData().getAccomViewData().get(0)
            .getRoomViewData();
      final List<RmSltViewData> packageModelRooms = new ArrayList<RmSltViewData>();
      for (final RoomViewData roomViewData : roomViewDataList)
      {
         final RmSltViewData room = new RmSltViewData();
         room.setRoomCode(roomViewData.getRoomCode());
         room.setRoomDescription(roomViewData.getDescription());
         room.setRoomPrice(roomViewData.getPrice());
         room.setSellingCode(roomViewData.getSellingCode());
         LOG.debug("Room code:" + room.getRoomCode() + "room price:" + room.getRoomPrice());
         packageModelRooms.add(room);
      }

      target.setPackageModelRooms(packageModelRooms);

      sessionService.setAttribute(WEBANALYTICS, target);

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.tui.th.book.facade.FlightOptionsPageFacade#updatePackageIntoCart
    * (uk.co.tui.domain.model.BasePackage)
    */
   private void updatePackageIntoCart()
   {
      packageCartService.updateCart(getPackageModel());
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
    * Gets the Warning Message from content csv.
    *
    * @param key the key
    * @param value the value
    * @return the description
    */
   private String getRoomLaiWarningMessage(final String key, final String... value)
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

   private Map<String, AltRoom> findCorrespondingRoomsFromStore(final Collection<Room> packageRooms)
   {
      final RoomSearchResponse roomSearchResponse =
         sessionService.getAttribute(SessionObjectKeys.ROOM_SEARCH_RESPONSE);

      int index = -1;
      final Map<String, AltRoom> selectedRooms = new HashMap<String, AltRoom>();
      final Map<String, List<AltRoom>> alternateRooms = roomSearchResponse.getAlternateRooms();
      for (final Room selectedRoom : packageRooms)
      {

         for (final AltRoom room : alternateRooms.get(String.valueOf(++index)))
         {
            if (StringUtils.equalsIgnoreCase(room.getRoomDetails().getRoomCode(),
               selectedRoom.getCode()))
            {
               selectedRooms.put(selectedRoom.getCode().concat(String.valueOf(index)), room);
               break;
            }
         }
      }
      return selectedRooms;
   }

   /**
    * Gets the RoomAndBoardOptionsPageViewData.
    *
    * @return the roomAndBoardOptionsPageViewData
    */
   @Override
   public RoomAndBoardOptionsPageViewData createRoomAndBoardOptionsPageViewData()
   {
      final RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData = getAlternateRooms();
      populateRoomOptionsStaticContentViewData(roomAndBoardOptionsPageViewData);
      return roomAndBoardOptionsPageViewData;
   }

}
