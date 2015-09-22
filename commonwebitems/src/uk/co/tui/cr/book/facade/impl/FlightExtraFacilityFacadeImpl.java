/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.ExtraFacilityQuantityCalculationService;
import uk.co.tui.book.services.SaveHolidayService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.ExtraFacilityUtils;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.cr.book.ExtraFacilityUpdator;
import uk.co.tui.cr.book.constants.BookFlowConstants;
import uk.co.tui.cr.book.constants.SessionObjectKeys;
import uk.co.tui.cr.book.facade.FlightExtraFacilityFacade;
import uk.co.tui.cr.book.populators.FlightExtrasContainerPopulator;
import uk.co.tui.cr.book.populators.SummaryPanelUrlPopulator;
import uk.co.tui.cr.book.store.FlightExtraFacilityStore;
import uk.co.tui.cr.book.view.data.BaggageExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ContentViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.FlightOptionsContentViewData;
import uk.co.tui.cr.book.view.data.FlightOptionsStaticContentViewData;
import uk.co.tui.cr.book.view.data.FlightOptionsViewData;

/**
 * The facade for handling the Flight extras.
 *
 * @author akshay.an
 *
 */

public class FlightExtraFacilityFacadeImpl implements FlightExtraFacilityFacade
{

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The extra facility updator. */
   @Resource(name = "crExtraFacilityUpdator")
   private ExtraFacilityUpdator extraFacilityUpdator;

   /** The update extra facility service. */
   @Resource
   private UpdateExtraFacilityService updateExtraFacilityService;

   /** The extra facility quantity calculation service. */
   @Resource
   private ExtraFacilityQuantityCalculationService extraFacilityQuantityCalculationService;

   /** The flight static content view data populator. */
   @Resource(name = "crFlightStaticContentViewDataPopulator")
   private Populator<Object, FlightOptionsStaticContentViewData> flightStaticContentViewDataPopulator;

   /** The flight options content view data populator. */
   @Resource(name = "crFlightOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> flightOptionsContentViewDataPopulator;

   /** The extra facility view data container populator. */
   @Resource(name = "crFlightExtrasContainerPopulator")
   private FlightExtrasContainerPopulator flightExtrasContainerPopulator;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The static content. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The summary panel url populator. */
   @Resource(name = "crSummaryPanelUrlPopulator")
   private SummaryPanelUrlPopulator summaryPanelUrlPopulator;

   /** The save holiday service. */
   @Resource
   private SaveHolidayService saveHolidayService;

   /** The Constant SUPRESS_WARNING_BOXING. */
   private static final String SUPRESS_WARNING_BOXING = "boxing";

   /** Package ViewData Populator Service Locator */
   @Resource
   private ServiceLocator<Populator> packageViewDataPopulatorServiceLocator;

   /** The message property reader. */
   @Resource
   private PropertyReader flightMealPropertyReader;

   /**
    * The method fetches the package model from the cart.
    *
    * @return PackageModel
    */
   private BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * The method updates the BaggageExtras.
    *
    * @param baggageAllocationMap the baggage allocation map
    * @return viewData
    */
   @Override
   public FlightOptionsViewData updateProductForBaggageExtras(
      final Map<String, String> baggageAllocationMap)
   {
      final BasePackage packageModel = getPackageModel();
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
         new HashMap<List<ExtraFacility>, List<String>>();
      for (final Entry<String, String> eachselectedEntry : baggageAllocationMap.entrySet())
      {
         exFacSelectionComposition.put(
            fetchSelectedExtraFacility(eachselectedEntry.getKey(), packageModel.getId()),
            StringUtil.split(eachselectedEntry.getValue(), ","));

      }

      updateExtraFacilityService.updateExtra(packageModel, exFacSelectionComposition);
      // To do - need to revisit this logic
      updateExtraFacilityQuantity(packageModel);
      saveHolidayService.resetHolidaySavedIndicator();
      final FlightOptionsViewData viewData = new FlightOptionsViewData();
      populatePackageView(packageModel, viewData);

      enrichWithTeasersViewData(viewData);
      updateViewDataWithTotalSelected(PassengerUtils.getPersonTypeCountFromPassengers(
         packageModel.getPassengers(), EnumSet.of(PersonType.ADULT, PersonType.CHILD)), viewData
         .getExtraFacilityViewDataContainer().getBaggageOptions(), exFacSelectionComposition);
      return viewData;
   }

   /**
    * @param personTypeCountFromPassengers
    * @param baggageOptions
    * @param exFacSelectionComposition
    */
   private void updateViewDataWithTotalSelected(final int personTypeCountFromPassengers,
      final ExtraFacilityCategoryViewData baggageOptions,
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition)
   {
      for (final ExtraFacilityViewData bagData : baggageOptions.getExtraFacilityViewData())
      {
         for (final Entry<List<ExtraFacility>, List<String>> paxAllocation : exFacSelectionComposition
            .entrySet())
         {
            updateTotalSelection(personTypeCountFromPassengers, bagData, paxAllocation);
         }
      }

   }

   /**
    * This method update TotalSelection
    *
    * @param personTypeCountFromPassengers PersonType count from passengers
    * @param bagData ExtraFacilityViewData
    * @param paxAllocation List of pax allocation
    */
   private void updateTotalSelection(final int personTypeCountFromPassengers,
      final ExtraFacilityViewData bagData,
      final Entry<List<ExtraFacility>, List<String>> paxAllocation)
   {
      if ((paxAllocation.getKey().get(0).getExtraFacilityCode().equals(bagData.getCode()))
         && (paxAllocation.getValue().size() == personTypeCountFromPassengers))
      {
         ((BaggageExtraFacilityViewData) bagData).setTotalSelected(true);

      }
   }

   /**
    * The method updates the SeatExtras.
    *
    * @param seatExtrasCode the seat extras code
    * @return viewData
    */
   @Override
   public FlightOptionsViewData updateProductForSeatExtras(final String seatExtrasCode)
   {
      final BasePackage packageModel = getPackageModel();
      final FlightOptionsViewData viewData = new FlightOptionsViewData();

      final String selectedSeatCabinClass =
         fetchSelectedExtraFacility(seatExtrasCode, packageModel.getId()).get(0).getCabinClass();
      final String existingSeatCabinClass = PackageUtilityService.getCabinClass(packageModel);
      if (!StringUtils.equalsIgnoreCase(existingSeatCabinClass, selectedSeatCabinClass))
      {
         return updateInterCabinSeatExtra(packageModel, seatExtrasCode, selectedSeatCabinClass);
      }

      // check whether selected seat is premium.
      if (isPremiumSeat(seatExtrasCode))
      {
         viewData.setBaggageSectionDisplayed(false);
         updateBaggageForPremiumSeat(packageModel, selectedSeatCabinClass);
      }
      else if (isBaggageAlreadyExistsInPackage(packageModel))
      {

         /*
          * adds the default baggage allowance on deselection of premium seat.
          */
         addDefaultBaggage(packageModel);
      }

      final Collection<Passenger> applicablePasengers =
         PassengerUtils.getApplicablePassengers(packageModel.getPassengers(),
            EnumSet.of(PersonType.ADULT, PersonType.CHILD, PersonType.SENIOR));

      updateExtraFacilityService.updateExtraFacility(applicablePasengers,
         fetchSelectedExtraFacility(seatExtrasCode, packageModel.getId()), packageModel);

      // To do - need to revisit this logic
      updateExtraFacilityQuantity(packageModel);
      saveHolidayService.resetHolidaySavedIndicator();
      populatePackageView(packageModel, viewData);

      extraFacilityUpdator.setSelectionFlagForSeatExtra(viewData, seatExtrasCode);
      enrichWithTeasersViewData(viewData);
      return viewData;
   }

   /**
    * On selection of seat from different cabin we update the default baggage corresponding to the
    * selected seat cabin and the meals corresponds to the meal in selected cabin.
    *
    * @param seatExtrasCode the seat extras code
    * @return viewData
    */
   private FlightOptionsViewData updateInterCabinSeatExtra(final BasePackage packageModel,
      final String seatExtrasCode, final String selectedSeatCabinClass)
   {
      final FlightOptionsViewData viewData = new FlightOptionsViewData();
      final FlightExtraFacilityStore flightExtraStore =
         sessionService.getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
      final Map<String, List<ExtraFacility>> validExtraFacilitiesMap =
         flightExtraStore.getExtraFacilityFromAllLegsBasedOnCabinClass(packageModel.getId(),
            selectedSeatCabinClass);

      if (isPremiumSeat(seatExtrasCode))
      {
         viewData.setBaggageSectionDisplayed(false);
         updateBaggageForPremiumSeat(packageModel, selectedSeatCabinClass);
      }
      else
      {
         // add the default baggage
         updateDefaultBaggageForSelectedSeat(packageModel, validExtraFacilitiesMap);
      }

      // updates the meals from the map
      updateMealCorrespondingToSelectedSeatCabin(packageModel, validExtraFacilitiesMap);
      final Collection<Passenger> applicablePasengers =
         PassengerUtils.getApplicablePassengers(packageModel.getPassengers(),
            EnumSet.of(PersonType.ADULT, PersonType.CHILD, PersonType.SENIOR));
      // updates the selected seat extrafacility.
      updateExtraFacilityService.updateExtraFacility(applicablePasengers,
         fetchSelectedExtraFacility(seatExtrasCode, packageModel.getId()), packageModel);
      updateExtraFacilityQuantity(packageModel);
      saveHolidayService.resetHolidaySavedIndicator();
      populatePackageView(packageModel, viewData);
      extraFacilityUpdator.setSelectionFlagForSeatExtra(viewData, seatExtrasCode);
      enrichWithTeasersViewData(viewData);
      return viewData;
   }

   /**
    * The method updates the meals corresponds to the cabin of selected seat.
    *
    * @param packageModel
    * @param validExtraFacilitiesMap
    */
   private void updateMealCorrespondingToSelectedSeatCabin(final BasePackage packageModel,
      final Map<String, List<ExtraFacility>> validExtraFacilitiesMap)
   {
      final Map<List<ExtraFacility>, List<String>> extrasToPassengersMap =
         new HashMap<List<ExtraFacility>, List<String>>();
      for (final ExtraFacilityCategory category : packageModel.getExtraFacilityCategories())
      {
         if (StringUtils.endsWithIgnoreCase(category.getCode(),
            ExtraFacilityConstants.FLIGHT_MEAL_CATEGORY))
         {
            for (final ExtraFacility extra : category.getExtraFacilities())
            {
               final List<String> passengers = getPassengersId(extra);
               extrasToPassengersMap.put(validExtraFacilitiesMap.get(flightMealPropertyReader
                  .getValue(extra.getExtraFacilityCode())), passengers);
            }
         }
      }
      updateExtraFacilityService.updateExtra(packageModel, extrasToPassengersMap);
   }

   /**
    * The Method returns the packageId's list for the selected extra facility.
    *
    * @param extra
    * @return passengersId List.
    */
   private List<String> getPassengersId(final ExtraFacility extra)
   {
      final List<String> passengers = new ArrayList<String>();
      for (final Passenger passenger : extra.getPassengers())
      {
         passengers.add(passenger.getId().toString());
      }
      return passengers;
   }

   /**
    * The Method returns the packageId's list for the selected passengers list.
    *
    * @param pasengersList
    * @return passengersId List.
    */
   private List<String> getPassengersId(final Collection<Passenger> pasengersList)
   {
      final List<String> passengers = new ArrayList<String>();
      for (final Passenger passenger : pasengersList)
      {
         passengers.add(passenger.getId().toString());
      }
      return passengers;
   }

   /**
    * The Method adds default baggage corresponding to the cabin of the selected seat extra.
    *
    * @param packageModel
    * @param validExtraFacilitiesMap
    */
   private void updateDefaultBaggageForSelectedSeat(final BasePackage packageModel,
      final Map<String, List<ExtraFacility>> validExtraFacilitiesMap)
   {
      final Map<List<ExtraFacility>, List<String>> extrasToPassengersMap =
         new HashMap<List<ExtraFacility>, List<String>>();
      for (final List<ExtraFacility> extras : validExtraFacilitiesMap.values())
      {
         if (StringUtils.endsWithIgnoreCase(extras.get(0).getExtraFacilityCategory().getCode(),
            ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE) && extras.get(0).isSelected())
         {
            final Collection<Passenger> applicablePasengers =
               PassengerUtils.getApplicablePassengers(packageModel.getPassengers(),
                  EnumSet.of(PersonType.ADULT, PersonType.CHILD, PersonType.SENIOR));
            final List<String> passengers = getPassengersId(applicablePasengers);
            extrasToPassengersMap.put(extras, passengers);
         }
      }
      updateExtraFacilityService.updateExtra(packageModel, extrasToPassengersMap);
   }

   /**
    * Adds the default baggage.
    *
    * @param packageModel the package model
    */
   private void addDefaultBaggage(final BasePackage packageModel)
   {
      final List<ExtraFacility> baggage = getExistingBaggageExtras(packageModel);
      if (CollectionUtils.isNotEmpty(baggage)
         && String.valueOf(((BaggageExtraFacility) baggage.get(0)).getBaggageWeight()).equals(
            Config.getParameter("baggageOfferedOnSelectionOfPremiumSeat")))
      {
         final List<ExtraFacility> defaultBaggage = getDefaultBaggageExtrasLite(packageModel);
         addDefaultBaggageExtrasToPackageModel(packageModel, defaultBaggage);
      }
   }

   /**
    * Update baggage for premium seat.
    *
    * @param packageModel the package model
    */
   @Override
   public void updateBaggageForPremiumSeat(final BasePackage packageModel,
      final String selectedSeatCabinClass)
   {
      addDefaultBaggageExtrasToPackageModel(packageModel, getDefaultBaggageExtrasLite(packageModel));
      updateBaggageWeightOnSelectionOfPremiumSeat(packageModel, selectedSeatCabinClass);
   }

   /**
    * The method iterates through the ExtraFacilities and sets the default baggage extra into
    * package Model
    *
    * @param packageModel
    */
   @SuppressWarnings(SUPRESS_WARNING_BOXING)
   private List<ExtraFacility> getDefaultBaggageExtrasLite(final BasePackage packageModel)
   {

      final FlightExtraFacilityStore flightExtraStore =
         (FlightExtraFacilityStore) sessionService
            .getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
      for (final List<ExtraFacility> extras : flightExtraStore
         .getExtraFacilityFromAllLegsBasedOnCabinClass(packageModel.getId(),
            getCabinClassFromPackage()).values())
      {
         if (StringUtils.equalsIgnoreCase(extras.get(0).getExtraFacilityCategory().getCode(),
            ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE) && extras.get(0).isSelected())
         {
            return ExtraFacilityUtils.deepCopyExtraFacilityList(extras);
         }
      }
      return Collections.emptyList();
   }

   /**
    * The method assigns the configured baggage weight to the default selected baggage in the
    * package model on selection of premium seat.
    *
    * @param packageModel the package model
    */
   @SuppressWarnings(SUPRESS_WARNING_BOXING)
   private void updateBaggageWeightOnSelectionOfPremiumSeat(final BasePackage packageModel,
      final String selectedSeatCabinClass)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (StringUtil.equalsIgnoreCase(categoryModel.getCode(),
            ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
         {
            for (final ExtraFacility extra : categoryModel.getExtraFacilities())
            {
               final BaggageExtraFacility bagExtra = (BaggageExtraFacility) extra;
               bagExtra.setBaggageWeight(Integer.parseInt(Config
                  .getParameter("baggageOfferedOnSelectionOfPremiumSeat")));
               bagExtra.setCabinClass(selectedSeatCabinClass);
            }
         }
      }

   }

   /**
    * Checks whether selected seat is Premium.
    *
    * @param seatExtrasCode the seat extras code
    * @return true if premium seat.
    */
   private boolean isPremiumSeat(final String seatExtrasCode)
   {
      return StringUtils.equalsIgnoreCase(seatExtrasCode, ExtraFacilityConstants.PREMIUM_SEAT);
   }

   /**
    * The method updates the MealExtras.
    *
    * @param mealAllocationMap the meal allocation map
    * @return viewData
    */
   @Override
   public FlightOptionsViewData updateProductForMealExtras(
      final Map<String, String> mealAllocationMap)
   {
      final BasePackage packageModel = getPackageModel();
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
         new HashMap<List<ExtraFacility>, List<String>>();

      for (final Entry<String, String> eachselectedEntry : mealAllocationMap.entrySet())
      {
         exFacSelectionComposition.put(
            fetchSelectedExtraFacility(eachselectedEntry.getKey(), packageModel.getId()),
            StringUtil.split(eachselectedEntry.getValue(), ","));

      }

      updateExtraFacilityService.updateExtra(packageModel, exFacSelectionComposition);
      // To do - need to revisit this logic
      updateExtraFacilityQuantity(packageModel);
      saveHolidayService.resetHolidaySavedIndicator();
      final FlightOptionsViewData viewData = new FlightOptionsViewData();
      populatePackageView(packageModel, viewData);
      enrichWithTeasersViewData(viewData);
      return viewData;
   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param packageModel the PackageModel
    * @param viewData the FlightOptionsViewData
    */
   @Override
   public void populatePackageView(final BasePackage packageModel,
      final FlightOptionsViewData viewData)
   {
      updatePackageIntoCart();
      populateContainerView(viewData, packageModel.getId());
      (packageViewDataPopulatorServiceLocator.locateByPackageType(packageModel.getPackageType()
         .toString())).populate(packageModel, viewData.getPackageViewData());
      extraFacilityUpdator.updatePackageViewData(packageModel, viewData.getPackageViewData());
      extraFacilityUpdator.updateBaggageDisplayIndicator(packageModel, viewData);
      extraFacilityUpdator.updateSeatExtrasRelativePrice(viewData);
      extraFacilityUpdator.updateContainerViewData(viewData.getPackageViewData(),
         viewData.getExtraFacilityViewDataContainer());
      populateFlightOptionsStaticContentViewData(viewData);
      populateFlightOptionsContentViewData(viewData);
      summaryPanelUrlPopulator.populate(BookFlowConstants.FLIGHTOPTIONS,
         viewData.getSummaryViewData());

   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param viewData the FlightOptionsViewData
    * @param packageCode the package code
    */
   private void populateContainerView(final FlightOptionsViewData viewData, final String packageCode)
   {
      final FlightExtraFacilityStore flightExtraStore =
         sessionService.getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
      final Map<String, List<ExtraFacility>> validExtraFacilitiesMap =
         flightExtraStore.getExtraFacilityFromAllLegsBasedOnCabinClass(packageCode,
            getCabinClassFromPackage());

      for (final List<ExtraFacility> extraFacilityModels : validExtraFacilitiesMap.values())
      {
         flightExtrasContainerPopulator.populate(extraFacilityModels,
            viewData.getExtraFacilityViewDataContainer());
      }
      extraFacilityUpdator.sortSeatsByIndex(viewData.getExtraFacilityViewDataContainer()
         .getSeatOptions());
      extraFacilityUpdator.sortMealAlphabetically(viewData.getExtraFacilityViewDataContainer()
         .getMealOptions());
      extraFacilityUpdator.sortBaggageBasedOnWeight(viewData.getExtraFacilityViewDataContainer()
         .getBaggageOptions());
   }

   /**
    * The method fetches the selected ExtraFacility from store.
    *
    * @param extraCode the extra code
    * @param packageId the package id
    * @return List of selected ExtraFacilities
    */
   private List<ExtraFacility> fetchSelectedExtraFacility(final String extraCode,
      final String packageId)
   {
      final FlightExtraFacilityStore flightExtraStore =
         sessionService.getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
      return ExtraFacilityUtils
         .deepCopyExtraFacilityList(flightExtraStore.getExtraFacilityFromAllLegsBasedOnCabCls(
            packageId, extraCode, getCabinClassFromPackage()));

   }

   /**
    * Update extra facility quantity.
    *
    * @param packageModel the package model
    */
   private void updateExtraFacilityQuantity(final BasePackage packageModel)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (ExtraFacilityGroup.FLIGHT.equals(categoryModel.getExtraFacilityGroup()))
         {
            updateSelectedQty(packageModel, categoryModel);
         }
      }
   }

   /**
    * Update selected qty.
    *
    * @param packageModel the package model
    * @param categoryModel the category model
    */
   @SuppressWarnings(SUPRESS_WARNING_BOXING)
   private void updateSelectedQty(final BasePackage packageModel,
      final ExtraFacilityCategory categoryModel)
   {
      for (final ExtraFacility extras : categoryModel.getExtraFacilities())
      {
         if (isExtraSelected(extras).booleanValue()
            && !StringUtil.equalsIgnoreCase(extras.getExtraFacilityCategory().getCode(),
               ExtraFacilityConstants.DONATION_CATEGORY))
         {

            extraFacilityQuantityCalculationService.updateExtraFacilitySelectedQuantity(extras,
               packageModel.getPassengers());

         }
      }
   }

   /**
    * Checks if extra is selected.
    *
    * @param extra the extra
    * @return the boolean
    */
   private Boolean isExtraSelected(final ExtraFacility extra)
   {
      return Boolean.valueOf(BooleanUtils.toBoolean(Boolean.valueOf(extra.isSelected()))
         && CollectionUtils.isNotEmpty(extra.getPassengers()));
   }

   /**
    * The method checks whether the baggage Category is already present in the package Model.
    *
    * @param packageModel the package model
    * @return isBagExtraPresent
    */
   private boolean isBaggageAlreadyExistsInPackage(final BasePackage packageModel)
   {
      boolean isBagExtraPresent = false;
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (StringUtil.equalsIgnoreCase(categoryModel.getCode(),
            ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
         {
            isBagExtraPresent = true;
            break;
         }
      }
      return isBagExtraPresent;
   }

   /**
    * Gets the existing baggage extras.
    *
    * @param packageModel the package model
    * @return the existing baggage extras
    */
   @SuppressWarnings(SUPRESS_WARNING_BOXING)
   private List<ExtraFacility> getExistingBaggageExtras(final BasePackage packageModel)
   {

      for (final ExtraFacilityCategory category : packageModel.getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(category.getCode(),
            ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
         {
            return category.getExtraFacilities();
         }

      }
      return Collections.emptyList();
   }

   /**
    * Adding the default baggage extra into package model and setting the passenger into the extra.
    *
    * @param packageModel the package model
    * @param extrasList the extras list
    */
   private void addDefaultBaggageExtrasToPackageModel(final BasePackage packageModel,
      final List<ExtraFacility> extrasList)
   {

      if (CollectionUtils.isNotEmpty(extrasList))
      {
         final List<Passenger> applicablePassengers =
            new ArrayList<Passenger>(PassengerUtils.getApplicablePassengers(
               packageModel.getPassengers(),
               EnumSet.of(PersonType.ADULT, PersonType.SENIOR, PersonType.CHILD)));
         final List<String> applicablePassengerIds = new ArrayList<String>();

         for (final Passenger passenger : applicablePassengers)
         {
            applicablePassengerIds.add(passenger.getId().toString());
         }

         final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
            new HashMap<List<ExtraFacility>, List<String>>();
         exFacSelectionComposition.put(extrasList, applicablePassengerIds);
         updateExtraFacilityService.updateExtra(packageModel, exFacSelectionComposition);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.FlightOptionsPageFacade#updatePackageIntoCart
    * (uk.co.tui.domain.model.PackageModel)
    */
   /**
    * Update package into cart.
    */
   private void updatePackageIntoCart()
   {
      packageCartService.updateCart(getPackageModel());
   }

   /**
    * Populate flight options static content view data.
    *
    * @param viewData the view data
    */
   @Override
   public void populateFlightOptionsStaticContentViewData(final FlightOptionsViewData viewData)
   {
      final FlightOptionsStaticContentViewData flightOptionsStaticContentViewData =
         new FlightOptionsStaticContentViewData();
      flightStaticContentViewDataPopulator.populate(new Object(),
         flightOptionsStaticContentViewData);
      viewData.setFlightOptionsStaticContentViewData(flightOptionsStaticContentViewData);

   }

   /**
    * Enrich with teasers view data.
    *
    * @param viewData the view data
    */
   private void enrichWithTeasersViewData(final FlightOptionsViewData viewData)
   {
      final FlightOptionsViewData flvViewData =
         sessionService.getAttribute(SessionObjectKeys.FLIGHTOPTIONSVIEWDATA);
      viewData.setAirportTeaserViewData(flvViewData.getAirportTeaserViewData());
      viewData.setDateTeaserViewData(flvViewData.getDateTeaserViewData());
      viewData.setDurationTeaserViewData(flvViewData.getDurationTeaserViewData());
      viewData.setCalendarViewData(flvViewData.getCalendarViewData());
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
    * Fetches the static content on type.
    *
    * @return the map
    */
   public Map<String, String> fetchStaticContent()
   {
      return staticContentServ.getFlightContents();
   }

   private String getCabinClassFromPackage()
   {
      return PackageUtilityService.getCabinClass(getPackageModel());
   }

}