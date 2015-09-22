/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityRestrictions;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.SaveHolidayService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.cr.book.ExtraFacilityUpdator;
import uk.co.tui.cr.book.constants.BookFlowConstants;
import uk.co.tui.cr.book.constants.SessionObjectKeys;
import uk.co.tui.cr.book.data.ClassActWorkshopsCriteria;
import uk.co.tui.cr.book.data.KidsActivityCriteria;
import uk.co.tui.cr.book.facade.CruiseExtraFacilityFacade;
import uk.co.tui.cr.book.facade.PackageExtraFacilityFacade;
import uk.co.tui.cr.book.populators.SummaryPanelUrlPopulator;
import uk.co.tui.cr.book.store.PackageExtraFacilityStore;
import uk.co.tui.cr.book.view.data.CabinSltViewData;
import uk.co.tui.cr.book.view.data.ContentViewData;
import uk.co.tui.cr.book.view.data.CruiseOptionsPageViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewDataContainer;
import uk.co.tui.cr.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.cr.book.view.data.RoomOptionsContentViewData;
import uk.co.tui.cr.book.view.data.RoomOptionsStaticContentViewData;
import uk.co.tui.cr.book.view.data.RoomViewData;
import uk.co.tui.cr.book.view.data.WebAnalyticsData;

/**
 * The Class DefaultCruiseExtraFacilityFacade.
 *
 * @author samantha.gd
 */
public class DefaultCruiseExtraFacilityFacade implements CruiseExtraFacilityFacade
{

   /** The packageCartService. */
   @Resource
   private PackageCartService packageCartService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The package extra facility facade. */
   @Resource(name = "crPackageExtraFacilityFacade")
   private PackageExtraFacilityFacade packageExtraFacilityFacade;

   /** The extra facility updator. */
   @Resource(name = "crExtraFacilityUpdator")
   private ExtraFacilityUpdator extraFacilityUpdator;

   /** The room static content view data populator. */
   @Resource(name = "crCabinStaticContentViewDataPopulator")
   private Populator<Object, RoomOptionsStaticContentViewData> cabinStaticContentViewDataPopulator;

   /** The room options content view data populator. */
   @Resource(name = "crCabinOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> cabinOptionsContentViewDataPopulator;

   /** Package ViewData Populator Service Locator. */
   @Resource
   private ServiceLocator<Populator> packageViewDataPopulatorServiceLocator;

   /** The sessionService. */
   @Resource
   private SessionService sessionService;

   /** The summary panel url populator. */
   @Resource(name = "crSummaryPanelUrlPopulator")
   private SummaryPanelUrlPopulator summaryPanelUrlPopulator;

   /** The update extra facility service. */
   @Resource
   private UpdateExtraFacilityService updateExtraFacilityService;

   /** The save holiday service. */
   @Resource
   private SaveHolidayService saveHolidayService;

   /** Logger for CruiseOptionsPageFacadeImpl class. **/
   private static final TUILogUtils LOG = new TUILogUtils("DefaultCruiseExtraFacilityFacade");

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.cr.book.facade.CruiseExtraFacilityFacade#renderCruiseExtras
    * (uk.co.tui.cr.book.view.data.CruiseOptionsPageViewData)
    */

   @Override
   public void renderCruiseExtras(final CruiseOptionsPageViewData cruiseOptionsViewData)
   {
      cruiseOptionsViewData.setExtraFacilityViewDataContainer(new ExtraFacilityViewDataContainer());
      packageExtraFacilityFacade.renderExtrasStore();
      populateCruiseOptionsAllView(getPackageModel(), cruiseOptionsViewData);
      populateCabinsForWebAnalytics(cruiseOptionsViewData);

   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param packageModel the BasePackage
    * @param cruiseOptionsViewData the FlightOptionsViewData
    */
   @Override
   public void populateCruiseOptionsAllView(final BasePackage packageModel,
      final RoomAndBoardOptionsPageViewData cruiseOptionsViewData)
   {
      populateCruiseOptionsContainerView(cruiseOptionsViewData);
      populateCruiseOptionsPackageView(packageModel, cruiseOptionsViewData);
      populateCruiseOptionsSummaryView(cruiseOptionsViewData);
      // here we are updating the container view data with package view data
      extraFacilityUpdator.updateExtraOptionsContainerView(packageModel,
         cruiseOptionsViewData.getExtraFacilityViewDataContainer());
      packageExtraFacilityFacade.sortExtraFacilityCategoriesInAddedOrder(cruiseOptionsViewData
         .getExtraFacilityViewDataContainer());
      cruiseOptionsViewData
         .setRoomOptionsStaticContentViewData(new RoomOptionsStaticContentViewData());
      populateCabinOptionsStaticContentViewData(cruiseOptionsViewData
         .getRoomOptionsStaticContentViewData());
      populateCabinOptionsContentViewData(cruiseOptionsViewData);

   }

   /**
    * The method is used to populate the List of ExtraFacility which are present as part of Flight
    * response.
    *
    * @param cruiseOptionsViewData the FlightOptionsViewData
    */
   private void populateCruiseOptionsContainerView(
      final RoomAndBoardOptionsPageViewData cruiseOptionsViewData)
   {
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService
            .getAttribute(SessionObjectKeys.PACKAGE_EXTRA_FACILITY_STORE);
      cruiseOptionsViewData.setExtraFacilityViewDataContainer(new ExtraFacilityViewDataContainer());

      final ExtraFacilityViewDataContainer containerViewData =
         cruiseOptionsViewData.getExtraFacilityViewDataContainer();
      // Not invoking the Extra Facility Updator
      final List<String> kidsActivityCategoryList =
         Arrays.asList(new String[] { ExtraFacilityConstants.STAGESCHOOL });
      packageExtraFacilityFacade.getCategoryModelFromStore(packageExtraFacilityStore,
         containerViewData, kidsActivityCategoryList, getCategoryViewData(containerViewData));

   }

   /**
    * Gets the category view data.
    *
    * @param containerViewData the container view data
    * @return the category view data
    */
   private Map<String, ExtraFacilityCategoryViewData> getCategoryViewData(
      final ExtraFacilityViewDataContainer containerViewData)
   {
      final Map<String, ExtraFacilityCategoryViewData> categoryMap =
         new HashMap<String, ExtraFacilityCategoryViewData>();
      categoryMap.put(ExtraFacilityConstants.STAGESCHOOL, containerViewData.getStageSchool());
      return categoryMap;
   }

   /**
    * The method is used to populate the List of ExtraFacility as part of Item Search response.
    *
    * @param packageModel the BasePackage
    * @param cruiseOptionsViewData the FlightOptionsViewData
    */
   private void populateCruiseOptionsPackageView(final BasePackage packageModel,
      final RoomAndBoardOptionsPageViewData cruiseOptionsViewData)
   {
      (packageViewDataPopulatorServiceLocator.locateByPackageType(packageModel.getPackageType()
         .toString())).populate(packageModel, cruiseOptionsViewData.getPackageViewData());
      extraFacilityUpdator.updatePackageViewData(packageModel,
         cruiseOptionsViewData.getPackageViewData());
      extraFacilityUpdator.updateContainerViewData(cruiseOptionsViewData.getPackageViewData(),
         cruiseOptionsViewData.getExtraFacilityViewDataContainer());

   }

   /**
    * The method is used to populate the List of ExtraFacility which are present as part of Flight
    * response.
    *
    * @param cruiseOptionsViewData the FlightOptionsViewData
    */
   private void populateCruiseOptionsSummaryView(
      final RoomAndBoardOptionsPageViewData cruiseOptionsViewData)
   {
      summaryPanelUrlPopulator.populate(BookFlowConstants.CRUISEOPTIONS,
         cruiseOptionsViewData.getSummaryViewData());
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.cr.book.facade.CruiseExtraFacilityFacade#
    * removeProductForKidsActivity(java.util.List)
    */
   @Override
   public RoomAndBoardOptionsPageViewData removeProductForKidsActivity(
      final List<KidsActivityCriteria> criteria)
   {
      final BasePackage packageModel = getPackageModel();
      for (final KidsActivityCriteria data : criteria)
      {
         updateExtraFacilityService.removeExtraFacility(data.getExtraCategoryCode(),
            data.getExtraCode(), data.getPassengerId());

      }
      packageCartService.updateCart(packageModel);
      saveHolidayService.resetHolidaySavedIndicator();
      // Updates the Quantity of All Extras.
      final RoomAndBoardOptionsPageViewData viewData = new CruiseOptionsPageViewData();

      // Populates the ViewData.
      populateCruiseOptionsAllView(packageModel, viewData);
      return viewData;
   }

   /**
    * Gets the package model.
    *
    * @return BasePackage
    */
   private BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * Populate room options static content view data.
    *
    * @param cabinOptionsStaticContentViewData the view data
    */
   @Override
   public void populateCabinOptionsStaticContentViewData(
      final RoomOptionsStaticContentViewData cabinOptionsStaticContentViewData)
   {
      cabinStaticContentViewDataPopulator.populate(new Object(), cabinOptionsStaticContentViewData);

   }

   /**
    * Populates the room extras content.
    *
    * @param cruiseOptionsPageViewData the cruise options page view data
    */
   @Override
   public void populateCabinOptionsContentViewData(
      final RoomAndBoardOptionsPageViewData cruiseOptionsPageViewData)
   {
      final RoomOptionsContentViewData cabinOptionsContentViewData =
         new RoomOptionsContentViewData();
      final ContentViewData cabinContent = new ContentViewData();
      cabinOptionsContentViewDataPopulator.populate(new Object(), cabinContent);
      cabinOptionsContentViewData.setRoomContentViewData(cabinContent);
      cruiseOptionsPageViewData.setRoomOptionsContentViewData(cabinOptionsContentViewData);

   }

   /**
    * The method updates the Kids Activity Extras.
    *
    * @param criteria the criteria
    * @return viewData
    */
   @Override
   public RoomAndBoardOptionsPageViewData updateProductForKidsActivity(
      final List<KidsActivityCriteria> criteria)
   {
      // Get the BasePackage from cart
      final BasePackage packageModel = getPackageModel();
      final MultiValueMap extraPassengerMapping = new MultiValueMap();

      extraPassengerMappingForKidsActivity(criteria, extraPassengerMapping);
      removeKidsActivityExtras(extraPassengerMapping, criteria);
      packageExtraFacilityFacade.updateKidsAcitivityExtrasLite(extraPassengerMapping, criteria,
         packageModel);
      for (final String eachkey : ((Map<String, List<String>>) extraPassengerMapping).keySet())
      {
         updateExtraFacilityQuantity(eachkey, packageModel);
      }
      packageCartService.updateCart(packageModel);
      saveHolidayService.resetHolidaySavedIndicator();
      // Updates the Quantity of All Extras.

      final RoomAndBoardOptionsPageViewData viewData = new CruiseOptionsPageViewData();

      // Populates the ViewData.
      populateCruiseOptionsAllView(packageModel, viewData);
      return viewData;
   }

   // Copied the method from Extra Facility Updator
   /**
    * Update extra facility quantity.
    *
    * @param extraCode the extra code
    * @param packageModel the package model
    */
   private void updateExtraFacilityQuantity(final String extraCode, final BasePackage packageModel)
   {
      final List<String> kidsActivityCategoryList =
         Arrays.asList(new String[] { ExtraFacilityConstants.STAGESCHOOL });
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (kidsActivityCategoryList.contains(categoryModel.getCode()))
         {
            for (final ExtraFacility extra : categoryModel.getExtraFacilities())
            {
               packageExtraFacilityFacade.updateExtraFacilitySelectedQuantityForChild(extraCode,
                  extra);
            }
            continue;
         }
      }
   }

   /**
    * Removes the kids activity extras.
    *
    * @param extraPassengerMapping the extra passenger mapping
    * @param criteria the criteria
    */
   private void removeKidsActivityExtras(final MultiValueMap extraPassengerMapping,
      final List<KidsActivityCriteria> criteria)
   {
      if (extraPassengerMapping.isEmpty() && CollectionUtils.isNotEmpty(criteria))
      {
         updateExtraFacilityService.removeExtraCategoryFromPackage(criteria.get(0)
            .getExtraCategoryCode());
      }
   }

   /**
    * Extra passenger mapping for kids activity.
    *
    * @param criteria the criteria
    * @param extraPassengerMapping the extra passenger mapping
    */
   private void extraPassengerMappingForKidsActivity(final List<KidsActivityCriteria> criteria,
      final MultiValueMap extraPassengerMapping)
   {
      for (final KidsActivityCriteria data : criteria)
      {
         if (data.isSelected())
         {
            extraPassengerMapping.put(data.getExtraCode(), data.getPassengerId());
         }
      }
   }

   /**
    * Updating the Class Act Workshop Option ExtraFacility.
    *
    * @param workshopsCriteria the workshops criteria
    * @return ExtraOptionsViewData
    */
   @Override
   public RoomAndBoardOptionsPageViewData updateProductForWorkshopOptions(
      final List<ClassActWorkshopsCriteria> workshopsCriteria)
   {
      final BasePackage packageModel = getPackageModel();
      final RoomAndBoardOptionsPageViewData extraOptionsViewData = new CruiseOptionsPageViewData();
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
         new HashMap<List<ExtraFacility>, List<String>>();

      for (final ClassActWorkshopsCriteria workshopExtra : workshopsCriteria)
      {
         processEachWorkshopLite(packageModel, extraOptionsViewData, exFacSelectionComposition,
            workshopExtra);
      }
      addClassActWorkshopsLite(packageModel, exFacSelectionComposition);
      packageCartService.updateCart(packageModel);

      saveHolidayService.resetHolidaySavedIndicator();
      populateCruiseOptionsAllView(packageModel, extraOptionsViewData);
      return extraOptionsViewData;
   }

   /**
    * Process each workshop lite.
    *
    * @param packageModel the package model
    * @param extraOptionsViewData the extra options view data
    * @param exFacSelectionComposition the ex fac selection composition
    * @param workshopExtra the workshop extra
    */
   private void processEachWorkshopLite(final BasePackage packageModel,
      final RoomAndBoardOptionsPageViewData extraOptionsViewData,
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition,
      final ClassActWorkshopsCriteria workshopExtra)
   {
      if (workshopExtra.getQuantity() == 0)
      {
         removeWorkshopOptions(extraOptionsViewData, workshopExtra);
      }
      else
      {
         allocateWorkshopOptionsLite(packageModel, exFacSelectionComposition, workshopExtra);
      }
   }

   /**
    * Removes the workshop options.
    *
    * @param extraOptionsViewData the extra options view data
    * @param workshopExtra the workshop extra
    */
   private void removeWorkshopOptions(final RoomAndBoardOptionsPageViewData extraOptionsViewData,
      final ClassActWorkshopsCriteria workshopExtra)
   {
      packageExtraFacilityFacade.removeInfantEquipment(workshopExtra.getExtraCategoryCode(),
         workshopExtra.getExtraCode());
      packageCartService.updateCart(packageCartService.getBasePackage());
      for (final ExtraFacilityViewData extraFacilityViewData : extraOptionsViewData
         .getExtraFacilityViewDataContainer().getWorkshopOptions().getExtraFacilityViewData())
      {
         if (StringUtils.equalsIgnoreCase(workshopExtra.getExtraCode(),
            extraFacilityViewData.getCode()))
         {
            extraFacilityViewData.setSelected(false);
         }
      }
   }

   /**
    * Allocate workshop options lite.
    *
    * @param packageModel the package model
    * @param exFacSelectionComposition the ex fac selection composition
    * @param workshopExtra the workshop extra
    */
   @SuppressWarnings("boxing")
   private void allocateWorkshopOptionsLite(final BasePackage packageModel,
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition,
      final ClassActWorkshopsCriteria workshopExtra)
   {
      final ExtraFacility selectedExtraFacility =
         packageExtraFacilityFacade.fetchExtraFacilityFromPackageExtraStoreLite(
            packageModel.getId(), workshopExtra.getExtraCategoryCode(),
            workshopExtra.getExtraCode());
      // PAX age is 16+ for Class Act Workshops, hence below code.
      if (selectedExtraFacility != null)
      {

         final ExtraFacilityRestrictions restrictions =
            selectedExtraFacility.getExtraFacilityRestrictions();
         exFacSelectionComposition.put(
            Arrays.asList(selectedExtraFacility),
            PassengerUtils.getPassengersIdWithAgeRang(restrictions.getMinAge(),
               restrictions.getMaxAge(), packageModel.getPassengers()).subList(0,
               workshopExtra.getQuantity()));
      }
   }

   /**
    * Adds the class act workshops lite.
    *
    * @param packageModel the package model
    * @param exFacSelectionComposition the ex fac selection composition
    */
   private void addClassActWorkshopsLite(final BasePackage packageModel,
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition)
   {
      if (!exFacSelectionComposition.isEmpty())
      {
         updateExtraFacilityService.updateExtraLite(packageModel, exFacSelectionComposition);
         for (final Entry<List<ExtraFacility>, List<String>> eachExtraEntry : exFacSelectionComposition
            .entrySet())
         {
            final ExtraFacility eachSelectedExtra = eachExtraEntry.getKey().get(0);
            packageExtraFacilityFacade.getExtraFacilityFromEachSelectedLite(packageModel,
               eachExtraEntry, eachSelectedExtra);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.cr.book.facade.CruiseOptionsPageFacade#
    * populateCabinsForWebAnalytics(uk.co.tui.cr.book.view.data. CruiseOptionsPageViewData)
    */
   @Override
   public void populateCabinsForWebAnalytics(
      final RoomAndBoardOptionsPageViewData cruiseOptionsPageViewData)
   {
      WebAnalyticsData target = sessionService.getAttribute(SessionObjectKeys.WEBANALYTICS);
      if (SyntacticSugar.isNull(target))
      {
         target = new WebAnalyticsData();
      }

      final BasePackage packageModel = packageCartService.getBasePackage();
      final int indexVal = generateCruiseIndex(packageModel);

      final List<RoomViewData> roomViewDataList =
         cruiseOptionsPageViewData.getPackageViewData().getAccomViewData().get(indexVal)
            .getRoomViewData();
      final List<CabinSltViewData> packageModelRooms = new ArrayList<CabinSltViewData>();
      for (final RoomViewData roomViewData : roomViewDataList)
      {
         final CabinSltViewData room = new CabinSltViewData();
         room.setRoomCode(roomViewData.getRoomCode());
         room.setRoomDescription(roomViewData.getDescription());
         room.setRoomPrice(roomViewData.getPrice());
         room.setSellingCode(roomViewData.getSellingCode());
         LOG.debug("Cabin code:" + room.getRoomCode() + "Cabin price:" + room.getRoomPrice());
         packageModelRooms.add(room);
      }

      target.setPackageModelCabins(packageModelRooms);

      sessionService.setAttribute(SessionObjectKeys.WEBANALYTICS, target);

   }

   /**
    * @param packageModel
    * @return indexVal
    */
   private int generateCruiseIndex(final BasePackage packageModel)
   {
      return packageComponentService.getAllStays(packageModel).indexOf(
         packageComponentService.getCruise(packageModel));
   }

}
