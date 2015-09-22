/**
 *
 */
package uk.co.tui.fj.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.CarHireSearchResponse;
import uk.co.tui.book.domain.FlightExtraFacilityResponse;
import uk.co.tui.book.domain.PackageExtraFacilityResponse;
import uk.co.tui.book.domain.lite.AgentType;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.ExtrasRequestType;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.SalesChannel;
import uk.co.tui.book.domain.lite.SalesChannelType;
import uk.co.tui.book.domain.lite.Schedule;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.WebAgent;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.services.ExtraFacilitySwapService;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.PackageRefinementService;
import uk.co.tui.book.services.SaveHolidayService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.book.services.inventory.CarHireService;
import uk.co.tui.book.services.inventory.CheckPriceAvailabilityService;
import uk.co.tui.book.services.inventory.FlightExtrasService;
import uk.co.tui.book.services.inventory.PackageExtrasService;
import uk.co.tui.book.utils.ExtraFacilityUtils;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.fj.book.ExtraFacilityUpdator;
import uk.co.tui.fj.book.constants.SessionObjectKeys;
import uk.co.tui.fj.book.facade.FlightExtraFacilityFacade;
import uk.co.tui.fj.book.facade.FlightOptionsPageFacade;
import uk.co.tui.fj.book.facade.PackageSwapFacade;
import uk.co.tui.fj.book.populators.AlertPopulator;
import uk.co.tui.fj.book.populators.PackageViewDataPopulator;
import uk.co.tui.fj.book.store.AlternativeFlightStore;
import uk.co.tui.fj.book.view.data.AlertViewData;
import uk.co.tui.fj.book.view.data.ContentViewData;
import uk.co.tui.fj.book.view.data.FlightOptionsContentViewData;
import uk.co.tui.fj.book.view.data.FlightOptionsViewData;
import uk.co.tui.fj.book.view.data.PackageViewData;
import uk.co.tui.fj.book.view.data.WebAnalyticsALTFLTData;
import uk.co.tui.fj.book.view.data.WebAnalyticsData;
import uk.co.tui.fj.exception.TUISystemException;
import uk.co.tui.fj.util.ExtraFacilityStoreUtils;

/**
 * @author pradeep.as
 *
 */
public class PackageSwapFacadeImpl implements PackageSwapFacade
{

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The flight extras service. */
   @Resource
   private FlightExtrasService flightExtrasServiceLite;

   @Resource
   private PackageExtrasService packageExtrasServiceLite;

   @Resource
   private PackageCartService packageCartService;

   @Resource(name = "fjFlightExtraFacilityFacade")
   private FlightExtraFacilityFacade flightExtraFacilityFacade;

   /** The flight options page facade. */
   @Resource(name = "fjFlightOptionsPageFacade")
   private FlightOptionsPageFacade flightOptionsPageFacade;

   @Resource
   private PackageRefinementService packageRefinementService;

   @Resource
   private UpdateExtraFacilityService updateExtraFacilityService;

   @Resource
   private PropertyReader messagePropertyReader;

   @Resource(name = "defaultInfobookServiceLocator")
   private ServiceLocator<CheckPriceAvailabilityService> infobookServiceLocator;

   /** AlertViewData Populator . */
   @Resource(name = "fjAlertPopulator")
   private AlertPopulator alertPopulator;

   /** The flight view data populator. */
   @Resource(name = "fjFlightViewDataPopulator")
   private Populator<BasePackage, PackageViewData> flightViewDataPopulator;

   @Resource
   private ExtraFacilitySwapService extrasSwapService;

   @Resource
   private SaveHolidayService saveHolidayService;

   @Resource(name = "fjExtraFacilityStroreUtil")
   private ExtraFacilityStoreUtils extraFacilityStroreUtil;

   /** The car hire service. */
   @Resource
   private CarHireService carHireServiceLite;

   /** The price calculation service. */
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;

   /** The package view data populator. */
   @Resource(name = "fjPackageViewDataPopulator")
   private PackageViewDataPopulator packageViewDataPopulator;

   /** The extra facility updator. */
   @Resource(name = "fjExtraFacilityUpdator")
   private ExtraFacilityUpdator extraFacilityUpdator;

   /** The flight options content view data populator. */
   @Resource(name = "fjFlightOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> flightOptionsContentViewDataPopulator;

   private static final TUILogUtils LOGGER = new TUILogUtils("PackageSwapFacadeImpl");

   private static final String WEBANALYTICSDATA = "th_webAnalyticsData";

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.PackageSwapFacade#swapPackage(java.lang.String)
    */
   @Override
   public FlightOptionsViewData swapPackage(final String packageId)
   {

      LOGGER.info("package swapping started");

      final BasePackage selectedPackage = getPackageHolidayFromStore(packageId);
      // For ATCOM package sum of Unit Prices is not Offer Price.
      // The difference should be updated to first room's price
      addFlightPriceToFirstRoom(selectedPackage);
      packageRefinementService.refinePackage(getPackageModel(), selectedPackage);
      final List<Feedback> unAvailableExtras = new ArrayList<Feedback>();
      final List<Feedback> feedBackList = new ArrayList<Feedback>();
      final BasePackage oldPackageModel = packageCartService.getBasePackage();
      // This is a fix done to avoid twice price reduction for packages in
      // store.
      includeDefaultPrice(oldPackageModel);
      packageCartService.updateCart(oldPackageModel);
      populatePackage(selectedPackage, oldPackageModel);
      final List<ExtraFacilityCategory> swapedExtraFacilityCategories =
         swapPackage(selectedPackage, unAvailableExtras, feedBackList);
      filterSwapedExtraFacilityCategories(selectedPackage, swapedExtraFacilityCategories);
      flightOptionsPageFacade.populateAvlQuantityForStandardSeats(selectedPackage);
      selectedPackage.setExtraFacilityCategories(swapedExtraFacilityCategories);
      updateExtrasIfMulticomThirdPartyFlight(selectedPackage);
      updateCrossbrandDetails(selectedPackage, oldPackageModel);
      postSwapUpdation(selectedPackage);
      excludeDefaultPrice(selectedPackage, oldPackageModel);

      // For ATCOM update cart with selected package
      updateCartIfATCOM(selectedPackage);
      try
      {
         feedBackList.addAll(infobookServiceLocator.locateByInventory(
            selectedPackage.getInventory().getInventoryType().toString())
            .updatePriceAndAvailability(selectedPackage));

      }
      catch (final BookServiceException e)
      {
         LOGGER.error(e.getMessage());
         final List<String> carrierCodes =
            Arrays.asList(StringUtils.split(Config.getParameter("multicom_error_codes"), ','));
         if (carrierCodes.contains(e.getErrorCode()))
         {
            return populateThirdPartyViewData();
         }
         packageCartService.addToCart(oldPackageModel);
         priceCalculationService.excludeDefaultExtraPriceFromRoomPrice(
            oldPackageModel.getExtraFacilityCategories(), oldPackageModel);
         throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
      }
      saveHolidayService.resetHolidaySavedIndicator();
      final FlightOptionsViewData viewData = renderSwappedPackage(selectedPackage);
      final List<AlertViewData> alerts = new ArrayList<AlertViewData>();
      if (CollectionUtils.isNotEmpty(unAvailableExtras))
      {
         populateAlertViewData(unAvailableExtras, alerts);
         viewData.setAlertMessages(alerts);
      }
      alertPopulator.populateTotalCostAlert(alerts, feedBackList);
      viewData.setAlertMessages(alerts);
      // populating webAnlyticsALTFLTData.

      return viewData;
   }

   /**
    * @param selectedPackage
    */
   private void updateCartIfATCOM(final BasePackage selectedPackage)
   {
      if (InventoryType.ATCOM == selectedPackage.getInventory().getInventoryType())
      {
         packageCartService.updateCart(selectedPackage);
      }
   }

   /**
    * @param selectedPackage
    */
   private void updateExtrasIfMulticomThirdPartyFlight(final BasePackage selectedPackage)
   {
      if (PackageUtilityService.isMulticomThirdPartyFlight(packageComponentService
         .getFlightItinerary(selectedPackage)))
      {
         packageExtrasServiceLite.updateExtrasForThirdPartyFlight(selectedPackage);
      }
   }

   /**
    * Populate flight price.
    *
    * @param newPackage the target
    */
   private void addFlightPriceToFirstRoom(final BasePackage newPackage)
   {
      if (InventoryType.ATCOM == newPackage.getInventory().getInventoryType())
      {
         final Stay availableAccommodation = packageComponentService.getStay(newPackage);
         final BigDecimal totalPrice = newPackage.getPrice().getAmount().getAmount();
         final List<Room> packageRooms = availableAccommodation.getRooms();
         BigDecimal roomsPrice = BigDecimal.ZERO;
         for (final Room packageRoom : packageRooms)
         {
            roomsPrice = roomsPrice.add(packageRoom.getPrice().getAmount().getAmount());
         }

         final BigDecimal deltaPrice = totalPrice.subtract(roomsPrice);
         final BigDecimal firstRoomsPrice =
            availableAccommodation.getRooms().get(0).getPrices().get(0).getAmount().getAmount();
         final BigDecimal updatedPrice = firstRoomsPrice.add(deltaPrice);
         availableAccommodation.getRooms().get(0).getPrices().get(0).getAmount()
            .setAmount(updatedPrice);
      }
   }

   /**
    * @param selectedPackage
    * @param oldPackageModel
    */
   private void excludeDefaultPrice(final BasePackage selectedPackage,
      final BasePackage oldPackageModel)
   {

      final List<ExtraFacilityCategory> storeExtraFacilities =
         getExtraFacilityCategory(oldPackageModel, null);
      if (CollectionUtils.isNotEmpty(storeExtraFacilities))
      {
         priceCalculationService.excludeDefaultExtraPriceFromRoomPrice(
            getExtraFacilityCategory(oldPackageModel, selectedPackage), selectedPackage);
      }
      else
      {
         priceCalculationService.excludeDefaultExtraPriceFromRoomPrice(
            oldPackageModel.getExtraFacilityCategories(), selectedPackage);
      }
   }

   /**
    * @param oldPackageModel
    */
   private void includeDefaultPrice(final BasePackage oldPackageModel)
   {

      final List<ExtraFacilityCategory> storeExtraFacilities =
         getExtraFacilityCategory(oldPackageModel, null);
      if (CollectionUtils.isNotEmpty(storeExtraFacilities))
      {
         priceCalculationService.includeDefaultExtraPriceFromRoomPriceLite(storeExtraFacilities,
            oldPackageModel);
      }
      else
      {
         priceCalculationService.includeDefaultExtraPriceFromRoomPrice(
            oldPackageModel.getExtraFacilityCategories(), oldPackageModel);
      }
   }

   /**
    * @return viewData
    */
   private FlightOptionsViewData populateThirdPartyViewData()
   {
      final BasePackage packageModel = packageCartService.getBasePackage();
      final FlightOptionsViewData viewData = new FlightOptionsViewData();
      packageViewDataPopulator.populate(packageModel, viewData.getPackageViewData());
      extraFacilityUpdator.updatePackageViewData(packageModel, viewData.getPackageViewData());
      flightExtraFacilityFacade.populateFlightOptionsStaticContentViewData(viewData);
      populateFlightOptionsContentViewData(viewData);
      return viewData;
   }

   /**
    * To obtain the flight extras from Anite, validate the extras and then swap.
    *
    * @param selectedPackage
    * @param unAvailableExtras
    */
   @Override
   public List<ExtraFacilityCategory> swapPackage(final BasePackage selectedPackage,
      final List<Feedback> unAvailableExtras, final List<Feedback> priceDifferenceFeedBack)
   {

      final List<ExtraFacilityCategory> sourceExtras =
         packageCartService.getBasePackage().getExtraFacilityCategories();
      LOGGER.info("selectedPackage.getCode() : " + selectedPackage.getId());
      packageCartService.replace(selectedPackage);
      return switchExtrasByInvokingNewRequest(selectedPackage, unAvailableExtras, sourceExtras);

   }

   /**
    * @param selectedPackage
    * @param unAvailableExtras
    * @param sourceExtras
    * @return swappedextraCategories
    */
   private List<ExtraFacilityCategory> switchExtrasByInvokingNewRequest(
      final BasePackage selectedPackage, final List<Feedback> unAvailableExtras,
      final List<ExtraFacilityCategory> sourceExtras)
   {
      final Map<ExtrasRequestType, List<ExtraFacilityCategory>> extrasGroupedByrequestType =
         getSearchTypesToBeInvoked(sourceExtras);
      List<ExtraFacilityCategory> targetExtraCategories = null;
      final List<ExtraFacilityCategory> combinedTargetExtraCategories =
         new ArrayList<ExtraFacilityCategory>();

      for (final Entry<ExtrasRequestType, List<ExtraFacilityCategory>> eachEntry : extrasGroupedByrequestType
         .entrySet())
      {
         targetExtraCategories =
            getTargetCategories(selectedPackage, eachEntry,
               getCabinClassFromOldPackage(sourceExtras));

         unAvailableExtras.addAll(extrasSwapService.swapExtras(eachEntry.getValue(),
            targetExtraCategories));
         combinedTargetExtraCategories.addAll(targetExtraCategories);
      }
      return combinedTargetExtraCategories;
   }

   private String getCabinClassFromOldPackage(final List<ExtraFacilityCategory> sourceExtras)
   {
      for (final ExtraFacilityCategory category : sourceExtras)
      {
         if (StringUtils.equalsIgnoreCase(category.getCode(),
            ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
         {
            if (StringUtils.isNotEmpty(category.getExtraFacilities().get(0).getCabinClass()))
            {
               return category.getExtraFacilities().get(0).getCabinClass();
            }
            else
            {
               return ExtraFacilityConstants.DEFAULT_SEAT_CABIN_CLASS;
            }

         }
      }
      return ExtraFacilityConstants.DEFAULT_SEAT_CABIN_CLASS;

   }

   /**
    * @param selectedPackage
    * @param eachEntry
    * @return targetExtraCategories
    */
   private List<ExtraFacilityCategory> getTargetCategories(final BasePackage selectedPackage,
      final Entry<ExtrasRequestType, List<ExtraFacilityCategory>> eachEntry, final String cabinClass)
   {
      List<ExtraFacilityCategory> targetExtraCategories = Collections.emptyList();
      switch (eachEntry.getKey())
      {
         case FLIGHTEXTRAS:
            targetExtraCategories = getAvailableFlightExtras(selectedPackage, cabinClass);
            break;

         case ITEMSEARCH:
            targetExtraCategories = getAvailablePackageExtras(selectedPackage);
            break;

         case CARHIRESEARCH:
            targetExtraCategories = getAvailableCarHireExras(selectedPackage);
            break;
         /* no implementation */
         default:
      }
      return targetExtraCategories;
   }

   @Override
   public void postSwapUpdation(final BasePackage selectedPackage)
   {
      if (ExtraFacilityUtils.isPremiumSeatSelected(selectedPackage.getExtraFacilityCategories()))
      {
         flightExtraFacilityFacade.updateBaggageForPremiumSeat(selectedPackage,
            getCabinClassFromPackage());

      }
      updateExtraFacilityService.resetPassengersWithExtrafacilities();
      packageCartService.updateCart(selectedPackage);
      LOGGER.debug("package swapping done");

   }

   private String getCabinClassFromPackage()
   {
      return PackageUtilityService.getCabinClass(getPackageModel());
   }

   /**
    * Gets the available flight extras.
    *
    * @param selectedPackage the selected package
    * @return the available flight extras
    */
   private List<ExtraFacilityCategory> getAvailableFlightExtras(final BasePackage selectedPackage,
      final String cabinClass)
   {

      if (MapUtils.isEmpty(extraFacilityStroreUtil.getFlightExtrasFromStoreForLite(
         selectedPackage.getId(), PackageUtilityService.getCabinClass(selectedPackage))))
      {

         // 1.Get the flight extras from Anite.
         try
         {
            final FlightExtraFacilityResponse flightExtraFacilityResponse =
               flightExtrasServiceLite.getFlightExtra(selectedPackage);
            extraFacilityStroreUtil.updateFlightExtrasToStore(flightExtraFacilityResponse);
         }
         catch (final BookServiceException e)
         {
            LOGGER.error("TUISystemException : " + e.getMessage());
            throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
         }
      }

      return packageExtrasServiceLite
         .createAvailableExtraFacilityCategoriesWithDefaultAllocatedForLite(extraFacilityStroreUtil
            .getFlightExtrasFromStoreForLite(selectedPackage.getId(), cabinClass));

   }

   /**
    * Gets the available package extras.
    *
    * @param selectedPackage the selected package
    * @return the available package extras
    */
   private List<ExtraFacilityCategory> getAvailablePackageExtras(final BasePackage selectedPackage)
   {

      if (CollectionUtils.isEmpty(extraFacilityStroreUtil
         .getPackageExtrasFromStoreLite(selectedPackage.getId())))
      {
         try
         {
            final PackageExtraFacilityResponse itemSearchResponse =
               packageExtrasServiceLite.getPackageExtras(selectedPackage,
                  ExtraFacilityGroup.PACKAGE);
            packageExtrasServiceLite.sortExtraFacilitiesByPrice(itemSearchResponse);
            extraFacilityStroreUtil.updatePackageExtrasToStore(itemSearchResponse);

         }
         catch (final BookServiceException e)
         {
            LOGGER.error("TUISystemException : " + e.getMessage());
            throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
         }
      }
      return packageExtrasServiceLite
         .createAvailableExtraFacilityCategoriesWithDefaultAllocatedLite(extraFacilityStroreUtil
            .getPackageExtrasFromStoreLite(selectedPackage.getId()));
   }

   /**
    * Gets the available car hire exras.
    *
    * @param selectedPackage the selected package
    * @return the available car hire exras
    */
   private List<ExtraFacilityCategory> getAvailableCarHireExras(final BasePackage selectedPackage)
   {

      if (CollectionUtils.isEmpty(extraFacilityStroreUtil
         .getCarHireUpgradeExtrasFromStoreLite(selectedPackage.getId())))
      {
         try
         {
            final CarHireSearchResponse carHireSearchResponse =
               carHireServiceLite.getCarHireOptions(selectedPackage);
            carHireSearchResponse.setPackageId(selectedPackage.getId());
            extraFacilityStroreUtil.updateCarHireExtrasToStore(carHireSearchResponse);
         }
         catch (final BookServiceException e)
         {
            LOGGER.error("TUISystemException : " + e.getMessage());
            throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
         }

      }
      return packageExtrasServiceLite
         .createAvailableExtraFacilityCategoriesWithDefaultAllocatedLite(extraFacilityStroreUtil
            .getCarHireUpgradeExtrasFromStoreLite(selectedPackage.getId()));
   }

   /**
    * Gets the search types to be invoked.
    *
    * @param sourceExtras the source extras
    * @return the search types to be invoked
    */
   private Map<ExtrasRequestType, List<ExtraFacilityCategory>> getSearchTypesToBeInvoked(
      final List<ExtraFacilityCategory> sourceExtras)
   {
      final MultiValueMap extraFacilitiesGroupedByrequestType = new MultiValueMap();
      for (final ExtraFacilityCategory eachExtraCategory : sourceExtras)
      {
         extraFacilitiesGroupedByrequestType.put(eachExtraCategory.getExtrasRequestType(),
            eachExtraCategory);
      }
      return extraFacilitiesGroupedByrequestType;
   }

   /**
    * @param unAvailableExtras
    * @param alerts
    */
   private void populateAlertViewData(final List<Feedback> unAvailableExtras,
      final List<AlertViewData> alerts)
   {
      for (final Feedback feedback : unAvailableExtras)
      {
         final AlertViewData alertViewData = new AlertViewData();

         final String[] subValues =
            { feedback.getDescription(), Integer.toString(feedback.getQuantity()) };

         alertViewData.setMessageHeader(messagePropertyReader.getValue("Extra_Unavailable_Header"));
         alertViewData.setMessageText(messagePropertyReader.substitute("Extra_Unavailable_Text",
            subValues));
         alerts.add(alertViewData);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.PackageSwapFacade#renderSwappedPackage(uk.co.tui
    * .book.view.data.FlightOptionsViewData, uk.co.tui.domain.model.BasePackage)
    */
   private FlightOptionsViewData renderSwappedPackage(final BasePackage packageModel)
   {
      // temporary fix given not according to design.
      final FlightOptionsViewData viewData = new FlightOptionsViewData();
      flightExtraFacilityFacade.populatePackageView(packageModel, viewData);

      flightViewDataPopulator.populate(packageModel, viewData.getPackageViewData());
      viewData.setRenderTeasers(false);
      return viewData;
   }

   /**
    * Populates the flight options content view data after the ajax call happens.
    *
    * @param viewData the view data
    */
   private void populateFlightOptionsContentViewData(final FlightOptionsViewData viewData)
   {
      final FlightOptionsContentViewData flightOptionsContentViewData =
         new FlightOptionsContentViewData();
      final ContentViewData contentViewData = new ContentViewData();
      flightOptionsContentViewDataPopulator.populate(new Object(), contentViewData);
      flightOptionsContentViewData.setFlightContentViewData(contentViewData);
      viewData.setFlightOptionsContentViewData(flightOptionsContentViewData);

   }

   /**
    * Gets the package holiday from store.
    *
    * @param packageId the package id
    * @return the package from store
    */
   private BasePackage getPackageHolidayFromStore(final String packageId)
   {
      final AlternativeFlightStore alternativeFlightStore =
         sessionService.getAttribute(SessionObjectKeys.ALTERNATE_FLIGHT_STORE);
      return alternativeFlightStore.getPackageHoliday(packageId);

   }

   /**
    * The method fetches the package model from the cart.
    *
    * @return BasePackage
    */
   private BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * Mainly used for the web analytics For setting flight duration, date and depAir into
    * WebAnalyticsALTFLTData
    *
    * @return webAnalyticsALTFLTData
    */
   @Override
   public WebAnalyticsData setALTFLT()
   {
      WebAnalyticsData webAnalyticsData = sessionService.getAttribute(WEBANALYTICSDATA);
      if (SyntacticSugar.isNull(webAnalyticsData))
      {
         webAnalyticsData = new WebAnalyticsData();
      }
      final BasePackage packageModel = getPackageModel();
      final WebAnalyticsALTFLTData webAnalyticsALTFLTData = new WebAnalyticsALTFLTData();
      final Itinerary itinerary = packageComponentService.getFlightItinerary(packageModel);
      final Leg outBound = itinerary.getOutBound().get(0);
      final Schedule flightSchedule = outBound.getSchedule();

      webAnalyticsALTFLTData.setDate(DateUtils.getDateInStringFormat(flightSchedule
         .getDepartureDate()));
      webAnalyticsALTFLTData.setDepAir(outBound.getDepartureAirport().getCode());
      webAnalyticsALTFLTData.setDuration(String.valueOf(packageModel.getDuration()));

      webAnalyticsData.setWebAnalyticsALTFLTData(webAnalyticsALTFLTData);
      return webAnalyticsData;
   }

   /**
    * Gets the extra facility category.
    *
    * @param swapFrom the swap from
    * @param swapTo the swap to
    * @return the extra facility category
    */
   @Override
   public List<ExtraFacilityCategory> getExtraFacilityCategory(final BasePackage swapFrom,
      final BasePackage swapTo)
   {
      List<ExtraFacilityCategory> packageExtraFacilityCategories =
         getPackageExtraFacilityCategories(swapTo);
      if (CollectionUtils.isEmpty(packageExtraFacilityCategories))
      {
         packageExtraFacilityCategories = getPackageExtraFacilityCategories(swapFrom);
      }
      return (List<ExtraFacilityCategory>) (CollectionUtils.isEmpty(packageExtraFacilityCategories)
         ? Collections.emptyList() : packageExtraFacilityCategories);
   }

   /**
    * @param packageModel
    */
   public List<ExtraFacilityCategory> getPackageExtraFacilityCategories(
      final BasePackage packageModel)
   {
      return (List<ExtraFacilityCategory>) ((packageModel != null) ? extraFacilityStroreUtil
         .getPackageExtrasFromStoreLite(packageModel.getId()) : Collections.emptyList());
   }

   /**
    * This method removes the default transfer option if the package is offered with free car hire.
    *
    * @param selectedPackage
    * @param swapedExtraFacilityCategories
    */
   private void filterSwapedExtraFacilityCategories(final BasePackage selectedPackage,
      final List<ExtraFacilityCategory> swapedExtraFacilityCategories)
   {
      if (CollectionUtils.isNotEmpty(selectedPackage.getListOfHighlights())
         && selectedPackage.getListOfHighlights().contains(HighLights.FREE_CAR_HIRE))
      {
         CollectionUtils.filter(swapedExtraFacilityCategories, new Predicate()
         {
            @Override
            public boolean evaluate(final Object object)
            {
               return !StringUtils.equalsIgnoreCase(((ExtraFacilityCategory) object).getCode(),
                  ExtraFacilityConstants.TRANSFER);
            }
         });
      }
   }

   private void populatePackage(final BasePackage target, final BasePackage source)
   {

      if (source.getBookingDetails() != null)
      {
         final BookingDetails bookingDetails = new BookingDetails();
         final BookingHistory bookingHistory = new BookingHistory();
         final List<BookingHistory> bookingHistorylist = new ArrayList<BookingHistory>();
         final WebAgent webagent = new WebAgent();
         webagent.setAgentType(AgentType.WEB);
         final SalesChannel salesChannel = new SalesChannel();
         salesChannel.setSalesChannelType(SalesChannelType.WEB);

         bookingHistory.setAgent(source.getBookingDetails().getBookingHistory().get(0).getAgent());
         bookingHistory.setSalesChannel(source.getBookingDetails().getBookingHistory().get(0)
            .getSalesChannel());
         bookingHistory.setBookingType(source.getBookingDetails().getBookingHistory().get(0)
            .getBookingType());

         bookingHistorylist.add(bookingHistory);
         bookingDetails.setBookingHistory(bookingHistorylist);

         target.setBookingDetails(bookingDetails);

         source.setBookingDetails(null);
      }

   }

   /**
    * Method to update cross brand details.
    *
    * @param selectedPackage
    * @param oldPackageModel
    */
   private void updateCrossbrandDetails(final BasePackage selectedPackage,
      final BasePackage oldPackageModel)
   {
      if (oldPackageModel.getCrossBrandType() != null)
      {
         selectedPackage.setCrossBrandType(oldPackageModel.getCrossBrandType());
      }

   }
}
