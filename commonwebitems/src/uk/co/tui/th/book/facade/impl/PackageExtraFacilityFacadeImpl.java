/*
 * Copyright (C)2006 TUI UK Ltd
 * 
 * TUI UK Ltd, Columbus House, Westwood Way, Westwood Business Park, Coventry, United Kingdom CV4
 * 8TT
 * 
 * Telephone - (024)76282828
 * 
 * All rights reserved - The copyright notice above does not evidence any actual or intended
 * publication of this source code.
 * 
 * $RCSfile: ExtraOptionsPageFacadeImpl.java$
 * 
 * $Revision: $
 * 
 * $Date: Jul 1, 2013$
 * 
 * Author: anithamani.s
 * 
 * 
 * $Log: $
 */
package uk.co.tui.th.book.facade.impl;

import static uk.co.tui.th.book.constants.BookFlowConstants.EMPTY;
import static uk.co.tui.th.book.constants.BookFlowConstants.EXTRAOPTIONS;
import static uk.co.tui.th.book.constants.BookFlowConstants.ROOMOPTIONS;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.CarHireSearchResponse;
import uk.co.tui.book.domain.PackageExtraFacilityResponse;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.CarHireExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.InclusivePackage;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.PriceType;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.ExtraFacilityClassificationService;
import uk.co.tui.book.services.ExtraFacilityQuantityCalculationService;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.PromotionalCodeValidationService;
import uk.co.tui.book.services.SaveHolidayService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.UpdateDonationExtraFacilityService;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.book.services.UpdateLateCheckoutService;
import uk.co.tui.book.services.config.BookFlowTUIConfigService;
import uk.co.tui.book.services.inventory.CarHireService;
import uk.co.tui.book.services.inventory.PackageExtrasService;
import uk.co.tui.book.utils.ExtraFacilityUtils;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.book.validator.impl.DefaultLateCheckOutOnOffStrategy;
import uk.co.tui.exception.TUISystemException;
import uk.co.tui.th.book.ExtraFacilityUpdator;
import uk.co.tui.th.book.constants.BookFlowConstants;
import uk.co.tui.th.book.constants.SessionObjectKeys;
import uk.co.tui.th.book.data.AncillaryCriteria;
import uk.co.tui.th.book.data.InfantActivityCriteria;
import uk.co.tui.th.book.data.KidsActivityCriteria;
import uk.co.tui.th.book.facade.PackageExtraFacilityFacade;
import uk.co.tui.th.book.facade.PackageSwapFacade;
import uk.co.tui.th.book.populators.AlertPopulator;
import uk.co.tui.th.book.populators.CarHireUpgradeViewDataPopulatorLite;
import uk.co.tui.th.book.populators.ExtraFacilityViewDataPopulatorLite;
import uk.co.tui.th.book.populators.PackageExtrasContainerPopulator;
import uk.co.tui.th.book.populators.PackageViewDataPopulator;
import uk.co.tui.th.book.populators.PassengerViewDataPopulator;
import uk.co.tui.th.book.populators.RoomExtrasContainerPopulator;
import uk.co.tui.th.book.populators.SelectedCarHireViewDataPopulator;
import uk.co.tui.th.book.populators.SummaryPanelUrlPopulator;
import uk.co.tui.th.book.store.CarHireUpgradeExtraFacilityStore;
import uk.co.tui.th.book.store.PackageExtraFacilityStore;
import uk.co.tui.th.book.view.data.AlertViewData;
import uk.co.tui.th.book.view.data.ContentViewData;
import uk.co.tui.th.book.view.data.CruiseOptionsViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityViewDataContainer;
import uk.co.tui.th.book.view.data.ExtraOptionsContentViewData;
import uk.co.tui.th.book.view.data.ExtraOptionsStaticContentViewData;
import uk.co.tui.th.book.view.data.ExtraOptionsViewData;
import uk.co.tui.th.book.view.data.ExtrasToPassengerRelationViewData;
import uk.co.tui.th.book.view.data.InsuranceContainerViewData;
import uk.co.tui.th.book.view.data.PassengerViewData;
import uk.co.tui.th.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.th.book.view.data.RoomOptionsContentViewData;
import uk.co.tui.th.book.view.data.RoomOptionsStaticContentViewData;
import uk.co.tui.th.util.ExtraFacilityStoreUtils;

/**
 * The Class PackageExtraFacilityFacadeImpl.
 *
 * @author anithamani.s
 */
public class PackageExtraFacilityFacadeImpl implements PackageExtraFacilityFacade
{
   
   /** Logger for PackageExtraFacilityFacadeImpl class *. */
   private static final Logger LOG = Logger.getLogger(PackageExtraFacilityFacadeImpl.class);
   
   /** The Constant PACKAGE_EXTRA_FACILITY_STORE. */
   private static final String PACKAGE_EXTRA_FACILITY_STORE = "PackageExtraFacilityStore";
   
   @Resource
   private PackageExtrasService packageExtrasServiceLite;
   
   /** The session service. */
   @Resource
   private SessionService sessionService;
   
   /** The package view data populator. */
   @Resource(name = "thPackageViewDataPopulator")
   private PackageViewDataPopulator packageViewDataPopulator;
   
   @Resource(name = "thRoomExtrasContainerPopulator")
   private RoomExtrasContainerPopulator roomExtrasContainerPopulator;
   
   @Resource(name = "thPackageExtrasContainerPopulator")
   private PackageExtrasContainerPopulator packageExtrasContainerPopulator;
   
   /** The room static content view data populator. */
   @Resource(name = "thRoomStaticContentViewDataPopulator")
   private Populator<Object, RoomOptionsStaticContentViewData> roomStaticContentViewDataPopulator;
   
   /** The room options content view data populator. */
   @Resource(name = "thRoomOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> roomOptionsContentViewDataPopulator;
   
   /** The summary panel url populator. */
   @Resource(name = "thSummaryPanelUrlPopulator")
   private SummaryPanelUrlPopulator summaryPanelUrlPopulator;
   
   /** The extra facility updator. */
   @Resource(name = "thExtraFacilityUpdator")
   private ExtraFacilityUpdator extraFacilityUpdator;
   
   /** The package model service. */
   @Resource
   private ModelService packageModelService;
   
   /** The update extra facility service. */
   @Resource
   private UpdateExtraFacilityService updateExtraFacilityService;
   
   /** The extra facility quantity calculation service. */
   @Resource
   private ExtraFacilityQuantityCalculationService extraFacilityQuantityCalculationService;
   
   /** The extra facility view data populator. */
   
   @Resource(name = "thExtraFacilityViewDataPopulatorLite")
   private ExtraFacilityViewDataPopulatorLite extraFacilityViewDataPopulatorLite;
   
   /** The passenger view data populator. */
   @Resource(name = "thPassengerViewDataPopulator")
   private PassengerViewDataPopulator passengerViewDataPopulator;
   
   /** The extra facility classification service. */
   @Resource
   private ExtraFacilityClassificationService extraFacilityClassificationService;
   
   /** The package swap. */
   @Resource(name = "thPackageSwapFacade")
   private PackageSwapFacade packageSwap;
   
   /** The update donation ex facility service locator. */
   @Resource(name = "updateDonationExtraLocator")
   private ServiceLocator<UpdateDonationExtraFacilityService> serviceLocator;
   
   /** The update late checkout service. */
   @Resource
   private UpdateLateCheckoutService updateLateCheckoutService;
   
   /** The message property reader. */
   @Resource
   private PropertyReader messagePropertyReader;
   
   @Resource
   private CarHireService carHireServiceLite;
   
   /** The extra static content view data populator. */
   @Resource(name = "thExtraStaticContentViewDataPopulator")
   private Populator<Object, ExtraOptionsStaticContentViewData> extraStaticContentViewDataPopulator;
   
   /** The extra options content view data populator. */
   @Resource(name = "thExtraOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> extraOptionsContentViewDataPopulator;
   
   @Resource(name = "thCarHireUpgradeViewDataPopulatorLite")
   private CarHireUpgradeViewDataPopulatorLite carHireUpgradeViewDataPopulatorLite;
   
   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;
   
   /** The alert populator. */
   @Resource(name = "thAlertPopulator")
   private AlertPopulator alertPopulator;
   
   /** The promotional code validation service. */
   @Resource
   private PromotionalCodeValidationService promotionalCodeValidationService;
   
   /** The save holiday service. */
   @Resource
   private SaveHolidayService saveHolidayService;
   
   @Resource(name = "thExtraFacilityStroreUtil")
   private ExtraFacilityStoreUtils extraFacilityStroreUtil;
   
   private static final String BOXING = "boxing";
   
   @Resource
   private BookFlowTUIConfigService bookFlowTuiConfigService;
   
   /** Package ViewData Populator Service Locator */
   @Resource
   private ServiceLocator<Populator> packageViewDataPopulatorServiceLocator;
   
   @Resource(name = "thSelectedCarHireViewDataPopulator")
   private SelectedCarHireViewDataPopulator selectedCarHireViewDataPopulator;
   
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;
   
   @Resource
   private CurrencyResolver currencyResolver;
   
   @Resource
   private DefaultLateCheckOutOnOffStrategy lateCheckOutOnOffStrategy;
   
   /**
    * this method renders all the extras associated with the package.
    *
    * @param extraOptionsViewData the extra options view data
    */
   @Override
   public void renderPackageExtras(final ExtraOptionsViewData extraOptionsViewData)
   {
      final BasePackage packageModel = getPackageModel();
      final List<ExtraFacilityCategory> storeExtraCategories =
         extraFacilityStroreUtil.getPackageExtrasFromStoreLite(packageModel.getId());
      
      if (CollectionUtils.isEmpty(storeExtraCategories))
      {
         final PackageExtraFacilityResponse packageExtraFacilityResponse = getPackageExtras();
         final PackageExtraFacilityStore packageExtraFacilityStore =
            new PackageExtraFacilityStore();
         packageExtrasServiceLite.sortExtraFacilitiesByPrice(packageExtraFacilityResponse);
         packageExtraFacilityStore.add(packageExtraFacilityResponse);
         sessionService.setAttribute(PACKAGE_EXTRA_FACILITY_STORE, packageExtraFacilityStore);
      }
      
      renderCarUpgradeOptions(extraOptionsViewData, packageModel);
      populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      populateAccommodationType(packageModel, extraOptionsViewData);
   }
   
   /**
    * @return PackageExtraFacilityResponse
    */
   private PackageExtraFacilityResponse getPackageExtras()
   {
      PackageExtraFacilityResponse packageExtraFacilityResponse;
      try
      {
         packageExtraFacilityResponse =
            packageExtrasServiceLite
               .getPackageExtras(getPackageModel(), ExtraFacilityGroup.PACKAGE);
      }
      catch (final BookServiceException e)
      {
         LOG.error("TUISystemException : " + e.getMessage());
         throw new TUISystemException(e.getErrorCode(), e);
      }
      return packageExtraFacilityResponse;
   }
   
   /**
    * This method renders the Accommodation Extras associated with the package.
    *
    * @param roomOptionsViewData the room options view data
    */
   @Override
   public void renderAccomExtras(final RoomAndBoardOptionsPageViewData roomOptionsViewData)
   {
      
      final BasePackage packageModel = getPackageModel();
      if (isItemSearchRequired()
         && CollectionUtils.isEmpty(extraFacilityStroreUtil
            .getPackageExtrasFromStoreLite(packageModel.getId())))
      {
         final PackageExtraFacilityResponse packageExtraFacilityResponse = getPackageExtras();
         packageExtrasServiceLite.sortExtraFacilitiesByPrice(packageExtraFacilityResponse);
         final PackageExtraFacilityStore packageExtraStore = new PackageExtraFacilityStore();
         packageExtraStore.add(packageExtraFacilityResponse);
         sessionService.setAttribute(PACKAGE_EXTRA_FACILITY_STORE, packageExtraStore);
      }
      populateRoomOptionsAllView(packageModel, roomOptionsViewData);
   }
   
   private boolean isItemSearchRequired()
   {
      return lateCheckOutOnOffStrategy.lateCheckOutApplicable();
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
   
   @Override
   public ExtraOptionsViewData updateProductForDonationOptions(final String extraCategory,
      final String code)
   {
      final BasePackage packageModel = getPackageModel();
      if (StringUtil.emptyOrBlank(code))
      {
         updateExtraFacilityService.removeExtraCategoryFromPackage(extraCategory);
      }
      else
      {
         updateProductForDonationFacilityLite(StringUtil.split(code, ","));
      }
      saveHolidayService.resetHolidaySavedIndicator();
      final ExtraOptionsViewData viewData = new ExtraOptionsViewData();
      // Populates the ViewData.
      populateExtraOptionsAllView(packageModel, viewData);
      packageModelService.save(packageModel);
      return viewData;
   }
   
   /**
    * This method updates Late Checkout into Package Model.
    *
    * @param extraCategory the extra category
    * @param extraCode the extra code
    * @param quantity the quantity
    * @return the room and board options page view data
    */
   @SuppressWarnings(BOXING)
   @Override
   public RoomAndBoardOptionsPageViewData updateProductForLateCheckOut(final String extraCategory,
      final String extraCode, final String quantity)
   {
      final BasePackage packageModel = getPackageModel();
      final RoomAndBoardOptionsPageViewData viewData = new RoomAndBoardOptionsPageViewData();
      if (StringUtils.equalsIgnoreCase(quantity, EMPTY))
      {
         updateExtraFacilityService.removeExtraCategoryFromPackage(extraCategory);
      }
      else
      {
         addProductForLateCheckOutLite(extraCode, quantity, packageModel);
      }
      saveHolidayService.resetHolidaySavedIndicator();
      populateRoomOptionsAllView(packageModel, viewData);
      return viewData;
   }
   
   /**
    * Adds the Late checkout to the holiday.
    *
    * @param extraCode the extra code
    * @param quantity the quantity
    * @param packageModel the package model
    */
   private void addProductForLateCheckOutLite(final String extraCode, final String quantity,
      final BasePackage packageModel)
   {
      final ExtraFacility selectedPackageExtraFacility =
         fetchSelectedPackageExtraFacilityLite(extraCode, packageModel.getId());
      updateLateCheckoutService.addLateCheckoutLite(packageModel, selectedPackageExtraFacility,
         quantity);
      
   }
   
   /**
    * Update product for donation facility.
    *
    * @param donationCodes the donation codes
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void updateProductForDonationFacilityLite(final List<String> donationCodes)
   {
      final BasePackage packageModel = getPackageModel();
      final List<ExtraFacility> selectedExtraFacilityList = new ArrayList<ExtraFacility>();
      for (final String eachCode : donationCodes)
      {
         selectedExtraFacilityList.add(fetchSelectedPackageExtraFacilityLite(eachCode,
            packageModel.getId()));
      }
      final String inventoryType = packageModel.getInventory().getInventoryType().toString();
      serviceLocator.locateByInventory(inventoryType).associateDonationExtraToPassengersLite(
         packageModel, selectedExtraFacilityList);
      serviceLocator.locateByInventory(inventoryType).updateDonationFacilitySelectedQuantity(
         packageModel);
   }
   
   /**
    * The method updates the Transfer Option Extras.
    *
    * @param extraCategory the extra category
    * @param transferCode the transfer code
    * @return the extra options view data
    */
   @SuppressWarnings(BOXING)
   @Override
   public ExtraOptionsViewData updateProductForTransferExtras(final String extraCategory,
      final String transferCode)
   {
      final BasePackage packageModel = getPackageModel();
      final boolean carHireExtra = isCarHireTransfer();
      // Reset the already selected transfer option
      removeDefaultTransferCategories();
      ExtraFacility selectedExtraFacility;
      final ExtraOptionsViewData extraOptionsViewData = new ExtraOptionsViewData();
      updateStoreTransferExtrasLite(transferCode, false);
      final boolean isCarHireAvailable = isCarHireAvailable(packageModel);
      if (!StringUtils.equalsIgnoreCase(transferCode, ExtraFacilityConstants.NOTF))
      {
         selectedExtraFacility =
            fetchSelectedPackageExtraFacilityLite(transferCode, getPackageModel().getId());
         if (selectedExtraFacility == null)
         {
            return updateProductForCarHireUpgradeOptions(transferCode);
         }
         else
         {
            updateSelectionTypeLite(selectedExtraFacility, isCarHireAvailable);
            carHireUpgradeIfAvailable(packageModel, extraOptionsViewData, isCarHireAvailable);
         }
         
         final List<ExtraFacility> extraFacilityModelList = new ArrayList<ExtraFacility>();
         extraFacilityModelList.add(selectedExtraFacility);
         updateExtraFacilityService.updateExtraFacilityLite(
            PassengerUtils.getApplicablePassengers(packageModel.getPassengers(),
               EnumSet.of(PersonType.SENIOR, PersonType.ADULT, PersonType.CHILD)),
            Arrays.asList(selectedExtraFacility), packageModel);
         extraFacilityQuantityCalculationService.updateTransferExtraFacilityQuantity(
            packageModel.getExtraFacilityCategories(), packageModel.getPassengers(),
            selectedExtraFacility.getExtraFacilityCode());
      }
      else
      {
         
         selectedExtraFacility =
            fetchSelectedPackageExtraFacilityLite(transferCode, getPackageModel().getId());
         updateExtraFacilityService.updateExtraFacilityLite(
            PassengerUtils.getApplicablePassengers(packageModel.getPassengers(),
               EnumSet.of(PersonType.SENIOR, PersonType.ADULT, PersonType.CHILD)),
            Arrays.asList(selectedExtraFacility), packageModel);
         extraFacilityQuantityCalculationService.updateTransferExtraFacilityQuantity(
            packageModel.getExtraFacilityCategories(), packageModel.getPassengers(),
            selectedExtraFacility.getExtraFacilityCode());
         carHireUpgradeIfAvailable(packageModel, extraOptionsViewData, isCarHireAvailable);
         if (carHireExtra)
         {
            updateCarHireExtraDiscription();
         }
      }
      saveHolidayService.resetHolidaySavedIndicator();
      // Populates the ViewData.
      populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      populateAccommodationType(packageModel, extraOptionsViewData);
      return extraOptionsViewData;
   }
   
   /**
    * @param packageModel
    * @param extraOptionsViewData
    */
   private void carHireUpgradeIfAvailable(final BasePackage packageModel,
      final ExtraOptionsViewData extraOptionsViewData, final boolean isCarHireAvailable)
   {
      if (isCarHireAvailable)
      {
         getDefaultCarHireUpgradeViewData(packageModel, extraOptionsViewData);
      }
      
   }
   
   /**
    * Gets the default car hire upgrade view data.
    *
    * @param packageModel the package model
    * @param extraOptionsViewData the extra options view data
    */
   private void getDefaultCarHireUpgradeViewData(final BasePackage packageModel,
      final ExtraOptionsViewData extraOptionsViewData)
   {
      resetCarHireUpgrades(packageModel);
      final CarHireUpgradeExtraFacilityStore carHireUpgradeExtraFacilityStore =
         getCarHireUpgradeExtraFacilityStore();
      
      final List<ExtraFacilityCategoryViewData> extraFacilityCategories =
         new ArrayList<ExtraFacilityCategoryViewData>();
      carHireUpgradeViewDataPopulatorLite.populate(
         carHireUpgradeExtraFacilityStore.getExtraFacilityLite(packageModel.getId()),
         extraFacilityCategories);
      extraOptionsViewData.getExtraFacilityViewDataContainer().setCarHireUpgrade(
         extraFacilityCategories);
      populateBasicCarType(extraOptionsViewData, extraFacilityCategories);
   }
   
   /**
    * This method updates the Infant Options Extras.
    *
    * @param infantExtraCriteria the infant extra criteria
    * @return the extra options view data
    */
   @SuppressWarnings(BOXING)
   @Override
   public ExtraOptionsViewData updateProductForInfantOptions(
      final List<InfantActivityCriteria> infantExtraCriteria)
   {
      
      final BasePackage packageModel = getPackageModel();
      final ExtraOptionsViewData extraOptionsViewData = new ExtraOptionsViewData();
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
         new HashMap<List<ExtraFacility>, List<String>>();
      
      for (final InfantActivityCriteria infantExtra : infantExtraCriteria)
      {
         processEachInfantActivityLite(packageModel, extraOptionsViewData,
            exFacSelectionComposition, infantExtra);
      }
      addInfantEquipmentLite(packageModel, exFacSelectionComposition);
      
      saveHolidayService.resetHolidaySavedIndicator();
      populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      return extraOptionsViewData;
   }
   
   /**
    * @param packageModel
    * @param extraOptionsViewData
    * @param exFacSelectionComposition
    * @param infantExtra
    */
   private void processEachInfantActivityLite(final BasePackage packageModel,
      final ExtraOptionsViewData extraOptionsViewData,
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition,
      final InfantActivityCriteria infantExtra)
   {
      if (infantExtra.getQuantity() == 0)
      {
         removeInfantOptions(extraOptionsViewData, infantExtra);
      }
      else
      {
         allocateInfantOptionsLite(packageModel, exFacSelectionComposition, infantExtra);
      }
      
   }
   
   /**
    * @param packageModel
    * @param exFacSelectionComposition
    * @param infantExtra
    */
   @SuppressWarnings("boxing")
   private void allocateInfantOptionsLite(final BasePackage packageModel,
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition,
      final InfantActivityCriteria infantExtra)
   {
      final ExtraFacility selectedExtraFacility =
         fetchExtraFacilityFromPackageExtraStoreLite(packageModel.getId(),
            infantExtra.getExtraCategoryCode(), infantExtra.getExtraCode());
      // Infant age is 0-3 for infant equipment, hence below code.
      if (selectedExtraFacility != null)
      {
         exFacSelectionComposition.put(
            Arrays.asList(selectedExtraFacility),
            PassengerUtils.getPassengersIdWithAgeRang(BookFlowConstants.ZERO,
               Integer.valueOf(Config.getParameter("INFANT_EQPMT_MAX_AGE")),
               packageModel.getPassengers()).subList(0, infantExtra.getQuantity()));
      }
   }
   
   /**
    * @param extraOptionsViewData
    * @param infantExtra
    */
   private void removeInfantOptions(final ExtraOptionsViewData extraOptionsViewData,
      final InfantActivityCriteria infantExtra)
   {
      removeInfantEquipment(infantExtra.getExtraCategoryCode(), infantExtra.getExtraCode());
      for (final ExtraFacilityViewData extraFacilityViewData : extraOptionsViewData
         .getExtraFacilityViewDataContainer().getInfantOptions().getExtraFacilityViewData())
      {
         if (StringUtils.equalsIgnoreCase(infantExtra.getExtraCode(),
            extraFacilityViewData.getCode()))
         {
            extraFacilityViewData.setSelected(false);
         }
      }
   }
   
   /**
    * Removes the infant equipment.
    *
    * @param infantExtraCategoryCode the infant extra category code
    * @param infantExtraCode the infant extra code
    */
   private void removeInfantEquipment(final String infantExtraCategoryCode,
      final String infantExtraCode)
   {
      updateExtraFacilityService.removeExtraFacility(infantExtraCategoryCode, infantExtraCode);
   }
   
   @SuppressWarnings(BOXING)
   private void addInfantEquipmentLite(final BasePackage packageModel,
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition)
   {
      if (!exFacSelectionComposition.isEmpty())
      {
         updateExtraFacilityService.updateExtraLite(packageModel, exFacSelectionComposition);
         // TODO:Should be changed.
         for (final Entry<List<ExtraFacility>, List<String>> eachExtraEntry : exFacSelectionComposition
            .entrySet())
         {
            final ExtraFacility eachSelectedExtra = eachExtraEntry.getKey().get(0);
            getExtraFacilityFromEachSelectedLite(packageModel, eachExtraEntry, eachSelectedExtra);
         }
      }
   }
   
   /**
    * @param eachExtraEntry
    * @param eachSelectedExtra
    */
   private void getExtraFacilityFromEachSelectedLite(final BasePackage packageModel,
      final Entry<List<ExtraFacility>, List<String>> eachExtraEntry,
      final ExtraFacility eachSelectedExtra)
   {
      for (final ExtraFacilityCategory extraFacilityCategory : packageModel
         .getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(extraFacilityCategory.getCode(), eachSelectedExtra
            .getExtraFacilityCategory().getCode()))
         {
            for (final ExtraFacility extraFacilityModel : extraFacilityCategory
               .getExtraFacilities())
            {
               updateQuantityLite(eachExtraEntry, eachSelectedExtra, extraFacilityModel);
            }
         }
      }
   }
   
   /**
    * @param eachExtraEntry
    * @param eachSelectedExtra
    * @param extraFacilityModel
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void updateQuantityLite(final Entry<List<ExtraFacility>, List<String>> eachExtraEntry,
      final ExtraFacility eachSelectedExtra, final ExtraFacility extraFacilityModel)
   {
      if (StringUtils.equalsIgnoreCase(extraFacilityModel.getExtraFacilityCode(),
         eachSelectedExtra.getExtraFacilityCode()))
      {
         extraFacilityQuantityCalculationService.updateExtraFacilitySelectedQuantity(
            extraFacilityModel, Integer.valueOf(eachExtraEntry.getValue().size()));
      }
   }
   
   /**
    * To check if the selected extra facility is from transfer option category.
    *
    * @param extraFacilityCategoryCode the extra facility category code
    * @return boolean
    */
   private boolean transferOptionExtraFacilityPresent(final String extraFacilityCategoryCode)
   {
      return ExtraFacilityConstants.TRANSFER_OPTION_CATEGORY_CODES
         .contains(extraFacilityCategoryCode);
   }
   
   /**
    * To remove the default transfer option since each of the transfer option is having individual
    * extra facility category code.
    */
   private void removeDefaultTransferCategories()
   {
      updateExtraFacilityService.removeExtraCategoryFromPackage(ExtraFacilityConstants.TRANSFER);
      updateExtraFacilityService
         .removeExtraSuperCategoryFromPackage(ExtraFacilityConstants.CAR_HIRE);
   }
   
   /**
    * Gets the package extra facility store.
    *
    * @return the package extra facility store
    */
   private PackageExtraFacilityStore getPackageExtraFacilityStore()
   {
      return (PackageExtraFacilityStore) sessionService.getAttribute(PACKAGE_EXTRA_FACILITY_STORE);
      
   }
   
   // To knock out ConfigurableRoomOptionsViewDataPopulator.java -start
   
   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Latecheckout response.
    *
    * @param viewData the RoomAndBoardOptionsPageViewData
    */
   private void populateRoomOptionsContainerView(final RoomAndBoardOptionsPageViewData viewData)
   {
      final String packageCode = getPackageModel().getId();
      final PackageExtraFacilityStore packageExtraFacilityStore = getPackageExtraFacilityStore();
      if (packageExtraFacilityStore != null)
      {
         final List<ExtraFacilityCategory> extraFacilityCategoryModelList =
            packageExtraFacilityStore.getExtraFacilityLite(packageCode);
         final ExtraFacilityViewDataContainer containerViewData =
            new ExtraFacilityViewDataContainer();
         for (final ExtraFacilityCategory extraFacilityCategoryModel : extraFacilityCategoryModelList)
         {
            if (uk.co.tui.book.domain.lite.ExtraFacilityGroup.ACCOMMODATION
               .equals(extraFacilityCategoryModel.getExtraFacilityGroup()))
            {
               roomExtrasContainerPopulator.populate(
                  extraFacilityCategoryModel.getExtraFacilities(), containerViewData);
            }
         }
         viewData.setExtraFacilityViewDataContainer(containerViewData);
      }
   }
   
   /**
    * The method is used to populate the List of ExtraFacility as part of Item Search response.
    *
    * @param packageModel the package model
    * @param viewData the view data
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
    * Fetch selected package extra facility.
    *
    * @param extraCode the extra code
    * @param packageId the package id
    * @return the extra facility model
    */
   private ExtraFacility fetchSelectedPackageExtraFacilityLite(final String extraCode,
      final String packageId)
   {
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService.getAttribute(PACKAGE_EXTRA_FACILITY_STORE);
      final List<ExtraFacilityCategory> extraFacilityCategoryList =
         packageExtraFacilityStore.getExtraFacilityLite(packageId);
      for (final ExtraFacilityCategory exFacilityCategory : extraFacilityCategoryList)
      {
         for (final ExtraFacility extra : exFacilityCategory.getExtraFacilities())
         {
            if (extraCode.equalsIgnoreCase(extra.getExtraFacilityCode()))
            {
               return ExtraFacilityUtils.deepCopyExtra(extra);
            }
         }
      }
      return null;
   }
   
   /**
    * Fetch selected package ex fac category.
    *
    * @param packageId the package id
    * @param extraCategoryCode the extra category code
    * @param extraFacilityCode the extra facility code
    * @return the list
    */
   private ExtraFacility fetchExtraFacilityFromPackageExtraStoreLite(final String packageId,
      final String extraCategoryCode, final String extraFacilityCode)
   {
      ExtraFacility extra = null;
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService.getAttribute(PACKAGE_EXTRA_FACILITY_STORE);
      final List<ExtraFacilityCategory> extraFacilityCategoryList =
         packageExtraFacilityStore.getExtraFacilityLite(packageId);
      for (final ExtraFacilityCategory exFacilityCategory : extraFacilityCategoryList)
      {
         if (StringUtils.equalsIgnoreCase(exFacilityCategory.getCode(), extraCategoryCode))
         {
            extra = getExtraFromCategoryLite(extraFacilityCode, exFacilityCategory);
            break;
         }
      }
      return extra;
   }
   
   /**
    * @param extraFacilityCode
    * @param exFacilityCategory
    */
   private ExtraFacility getExtraFromCategoryLite(final String extraFacilityCode,
      final ExtraFacilityCategory exFacilityCategory)
   {
      for (final ExtraFacility extraFacility : exFacilityCategory.getExtraFacilities())
      {
         if (StringUtils.equalsIgnoreCase(extraFacility.getExtraFacilityCode(), extraFacilityCode))
         {
            return extraFacility;
         }
      }
      return null;
   }
   
   // To knock out ConfigurableRoomOptionsViewDataPopulator.java -end
   // To knock out ConfigurableRoExtraOptionsViewDataPopulator.java -start
   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param packageModel the BasePackage
    * @param extraOptionsViewData the FlightOptionsViewData
    */
   @Override
   public void populateExtraOptionsAllView(final BasePackage packageModel,
      final ExtraOptionsViewData extraOptionsViewData)
   {
      populateExtraOptionsContainerView(extraOptionsViewData);
      populateExtraOptionsPackageView(packageModel, extraOptionsViewData);
      populateExtraOptionsSummaryView(extraOptionsViewData);
      // here we are updating the container view data with package view data
      extraFacilityUpdator.updateExtraOptionsContainerView(packageModel,
         extraOptionsViewData.getExtraFacilityViewDataContainer());
      displayTopThreeExcursions(extraOptionsViewData.getExtraFacilityViewDataContainer()
         .getExcursionOptions());
      sortExtraFacilityCategoriesInAddedOrder(extraOptionsViewData
         .getExtraFacilityViewDataContainer());
      final List<ExtraFacilityViewData> transfeOptions =
         extraOptionsViewData.getExtraFacilityViewDataContainer().getTransferOptions()
            .getExtraFacilityViewData();
      updateTransferOptionContainer(extraOptionsViewData.getExtraFacilityViewDataContainer(),
         packageModel);
      if (extraOptionsViewData.getExtraFacilityViewDataContainer().getCarHireUpgrade() != null)
      {
         final ExtraFacilityViewData carHireExtraFacilityViewData =
            getSelectedCarHireViewData(extraOptionsViewData);
         if (carHireExtraFacilityViewData != null)
         {
            resetTransferIfCarSelected(extraOptionsViewData, carHireExtraFacilityViewData);
         }
      }
      updateRelativePriceDifference(transfeOptions);
      // To handle the scenario when default transfer is not
      // available.
      handleNoTransferOption(extraOptionsViewData);
   }
   
   /**
    * Reset transfer if car selected.
    *
    * @param extraOptionsViewData the extraOptionsViewData
    * @param carHireExtraFacilityViewData the car hire extra facility view data
    */
   private void resetTransferIfCarSelected(final ExtraOptionsViewData extraOptionsViewData,
      final ExtraFacilityViewData carHireExtraFacilityViewData)
   {
      final List<ExtraFacilityViewData> transfeOptions =
         extraOptionsViewData.getExtraFacilityViewDataContainer().getTransferOptions()
            .getExtraFacilityViewData();
      final int carHireIndex = transfeOptions.size();
      // Reset if any other transfer option is selected
      for (final ExtraFacilityViewData transferOption : transfeOptions)
      {
         if (transferOption.isSelected() && carHireExtraFacilityViewData.isSelected())
         {
            transferOption.setSelected(false);
         }
         transferOption.setIncluded(false);
      }
      transfeOptions.add(carHireIndex, carHireExtraFacilityViewData);
   }
   
   /**
    * Gets the selected car hire view data.
    *
    * @param extraOptionsViewData the extra options view data
    * @return the selected car hire view data
    */
   private ExtraFacilityViewData getSelectedCarHireViewData(
      final ExtraOptionsViewData extraOptionsViewData)
   {
      for (final ExtraFacilityCategoryViewData extraFacilityCategoryViewData : extraOptionsViewData
         .getExtraFacilityViewDataContainer().getCarHireUpgrade())
      {
         for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
            .getExtraFacilityViewData())
         {
            if (extraFacilityViewData.isSelected())
            {
               return extraFacilityViewData;
            }
         }
      }
      return getDefaultCarHire(extraOptionsViewData);
   }
   
   /**
    * Gets the default car hire.
    *
    * @param extraOptionsViewData the extra options view data
    * @return the default car hire
    */
   private ExtraFacilityViewData getDefaultCarHire(final ExtraOptionsViewData extraOptionsViewData)
   {
      for (final ExtraFacilityCategoryViewData extraFacilityCategoryViewData : extraOptionsViewData
         .getExtraFacilityViewDataContainer().getCarHireUpgrade())
      {
         for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
            .getExtraFacilityViewData())
         {
            if (extraFacilityViewData.isFree())
            {
               return extraFacilityViewData;
            }
         }
      }
      return null;
   }
   
   /**
    * Populate room options all view.
    *
    * @param packageModel the package model
    * @param roomOptionsViewData the room options view data
    */
   private void populateRoomOptionsAllView(final BasePackage packageModel,
      final RoomAndBoardOptionsPageViewData roomOptionsViewData)
   {
      populateRoomOptionsContainerView(roomOptionsViewData);
      populateRoomOptionsPackageView(packageModel, roomOptionsViewData);
      populateRoomOptionsSummaryView(roomOptionsViewData);
   }
   
   /**
    * The method is used to populate the List of ExtraFacility as part of Item Search response.
    *
    * @param packageModel the BasePackage
    * @param extraOptionsViewData the FlightOptionsViewData
    */
   private void populateExtraOptionsPackageView(final BasePackage packageModel,
      final ExtraOptionsViewData extraOptionsViewData)
   {
      updatePackageIntoCart();
      (packageViewDataPopulatorServiceLocator.locateByPackageType(packageModel.getPackageType()
         .toString())).populate(packageModel, extraOptionsViewData.getPackageViewData());
      extraFacilityUpdator.updatePackageViewData(packageModel,
         extraOptionsViewData.getPackageViewData());
      extraFacilityUpdator.updateContainerViewData(extraOptionsViewData.getPackageViewData(),
         extraOptionsViewData.getExtraFacilityViewDataContainer());
      populateExtraOptionsStaticContentViewData(extraOptionsViewData);
      populateExtraOptionsContentViewData(extraOptionsViewData);
   }
   
   /**
    * The method is used to populate the List of ExtraFacility which are present as part of Flight
    * response.
    *
    * @param extraOptionsViewData the FlightOptionsViewData
    */
   private void populateExtraOptionsContainerView(final ExtraOptionsViewData extraOptionsViewData)
   {
      final PackageExtraFacilityStore packageExtraFacilityStore = getPackageExtraFacilityStore();
      
      final ExtraFacilityViewDataContainer containerViewData =
         extraOptionsViewData.getExtraFacilityViewDataContainer();
      final List<String> kidsActivityCategoryList =
         extraFacilityUpdator.getKidsActivityCriteriaList();
      
      final Map<String, ExtraFacilityCategoryViewData> categoryMap =
         extraFacilityUpdator.getCategoryViewData(containerViewData);
      getCategoryModelFromStore(packageExtraFacilityStore, containerViewData,
         kidsActivityCategoryList, categoryMap);
      
      processIfInfantOptionsNotNull(containerViewData);
      
      if (!containerViewData.getTransferOptions().getExtraFacilityViewData().isEmpty())
      {
         // To handle the scenario when default transfer is not available.
         handleNoTransferOption(extraOptionsViewData);
      }
      applyAncilliaryRules(bookFlowTuiConfigService.getExcursionMinAdultQuantity(),
         extraOptionsViewData.getExtraFacilityViewDataContainer().getExcursionOptions());
      applyAttractionRules(bookFlowTuiConfigService.getExcursionMinAdultQuantity(),
         extraOptionsViewData.getExtraFacilityViewDataContainer().getAttractionOptions());
      if (extraOptionsViewData.getInsuranceContainerViewData() == null)
      {
         extraOptionsViewData
            .setInsuranceContainerViewData((InsuranceContainerViewData) sessionService
               .getAttribute("insuranceContainerViewData"));
      }
   }
   
   /**
    * @param containerViewData
    */
   private void processIfInfantOptionsNotNull(final ExtraFacilityViewDataContainer containerViewData)
   {
      if (containerViewData.getInfantOptions() != null
         && CollectionUtils.isNotEmpty(containerViewData.getInfantOptions()
            .getExtraFacilityViewData()))
      {
         extraFacilityUpdator.sortInfantEquipBasedOnPrice(containerViewData.getInfantOptions());
      }
   }
   
   /**
    * @param packageExtraFacilityStore
    * @param containerViewData
    * @param kidsActivityCategoryList
    * @param categoryMap
    */
   private void getCategoryModelFromStore(
      final PackageExtraFacilityStore packageExtraFacilityStore,
      final ExtraFacilityViewDataContainer containerViewData,
      final List<String> kidsActivityCategoryList,
      final Map<String, ExtraFacilityCategoryViewData> categoryMap)
   {
      for (final ExtraFacilityCategory extraFacilityCategoryModel : packageExtraFacilityStore
         .getExtraFacilityLite(getPackageModel().getId()))
      {
         if (kidsActivityCategoryList.contains(extraFacilityCategoryModel.getCode()))
         {
            populateKidsActivityCategoryViewDataLite(extraFacilityCategoryModel,
               categoryMap.get(extraFacilityCategoryModel.getCode()));
            continue;
         }
         packageExtrasContainerPopulator.populate(extraFacilityCategoryModel.getExtraFacilities(),
            containerViewData);
      }
      displayTopThreeExcursions(containerViewData.getExcursionOptions());
      
   }
   
   /**
    * Display top three excursions.
    *
    * @param excursionOptions the excursion options
    */
   private void displayTopThreeExcursions(final List<ExtraFacilityCategoryViewData> excursionOptions)
   {
      if (CollectionUtils.isNotEmpty(excursionOptions))
      {
         final int maxExcursionElements = getMaxExcursionElements();
         int displayableElements = 1;
         for (final ExtraFacilityCategoryViewData viewData : excursionOptions)
         {
            if (excursionNeedsToBeDisplayed(maxExcursionElements, displayableElements, viewData))
            {
               viewData.setDisplay(true);
            }
            displayableElements++;
         }
      }
   }
   
   /**
    * Sort extra facility categories based on the added excursion order.
    *
    * @param containerViewData the excursion options
    */
   private void sortExtraFacilityCategoriesInAddedOrder(
      final ExtraFacilityViewDataContainer containerViewData)
   {
      final List<ExtraFacilityCategoryViewData> selectedExcursions =
         new ArrayList<ExtraFacilityCategoryViewData>();
      final List<ExtraFacilityCategoryViewData> excursionOptions =
         containerViewData.getExcursionOptions();
      addSelectedExcursions(excursionOptions, selectedExcursions);
      addUnselectedExcursions(excursionOptions, selectedExcursions);
      containerViewData.setExcursionOptions(selectedExcursions);
   }
   
   /**
    * Adds the unselected excursions.
    *
    * @param excursionOptions the excursion options
    * @param selectedExcursions the selected excursions
    */
   private void addUnselectedExcursions(final List<ExtraFacilityCategoryViewData> excursionOptions,
      final List<ExtraFacilityCategoryViewData> selectedExcursions)
   {
      for (final ExtraFacilityCategoryViewData category : excursionOptions)
      {
         if (!category.isSelected())
         {
            selectedExcursions.add(category);
         }
      }
   }
   
   /**
    * Adds the selected excursions.
    *
    * @param excursionOptions the excursion options
    * @param selectedExcursions the selected excursions
    */
   private void addSelectedExcursions(final List<ExtraFacilityCategoryViewData> excursionOptions,
      final List<ExtraFacilityCategoryViewData> selectedExcursions)
   {
      for (final ExtraFacilityCategoryViewData category : excursionOptions)
      {
         if (category.isSelected())
         {
            selectedExcursions.add(category);
         }
         
      }
   }
   
   /**
    * Applies the Rule of Excursion Extra.
    *
    * @return criteria
    */
   private int getMaxExcursionElements()
   {
      return bookFlowTuiConfigService.getMaxExcursionElements();
   }
   
   /**
    * Excursion needs to be displayed.
    *
    * @param maxExcursionElements the max excursion elements
    * @param displayableElements the displayable elements
    * @param viewData the view data
    * @return true, if successful
    */
   private boolean excursionNeedsToBeDisplayed(final int maxExcursionElements,
      final int displayableElements, final ExtraFacilityCategoryViewData viewData)
   {
      return displayableElements <= maxExcursionElements || viewData.isSelected();
   }
   
   /**
    * Apply attraction rules.
    *
    * @param attractionOptions the attraction options
    */
   private void applyAttractionRules(final int minAdultCountRequiredForExtra,
      final Map<String, List<ExtraFacilityCategoryViewData>> attractionOptions)
   {
      for (final Entry<String, List<ExtraFacilityCategoryViewData>> attractionEntry : attractionOptions
         .entrySet())
      {
         applyAncilliaryRules(minAdultCountRequiredForExtra, attractionEntry.getValue());
      }
   }
   
   /**
    * The method to apply the rule on Excursion extra and setting the minimum number of adults
    * required to opt that extra.
    *
    * @param excursionOptions the excursion options
    */
   private void applyAncilliaryRules(final int minAdultCountRequiredForExtra,
      final List<ExtraFacilityCategoryViewData> excursionOptions)
   {
      for (final ExtraFacilityCategoryViewData category : excursionOptions)
      {
         category.setMinAdultCountRequiredForExtra(minAdultCountRequiredForExtra);
      }
   }
   
   /**
    * The method sets the quantity of the swim Extra based on the configuration.
    *
    * @param extraViewData the extra view data
    */
   private void setSwimAcademyQuantity(final List<ExtraFacilityViewData> extraViewData)
   {
      for (final ExtraFacilityViewData extra : extraViewData)
      {
         if (StringUtils.equalsIgnoreCase(extra.getCode(), "SWN"))
         {
            extra.setQuantity(bookFlowTuiConfigService.getSwimExtraOneToOneSessionMaxSelectable());
            continue;
         }
         extra.setQuantity(bookFlowTuiConfigService.getSwimExtraGroupSessionMaxSelectable());
      }
   }
   
   /**
    * The method to populate the CategoryViewDataFor Kids Activity Categories.
    *
    * @param categoryViewData the category view data
    */
   private void populateKidsActivityCategoryViewDataLite(
      final ExtraFacilityCategory extraFacilityCategory,
      final ExtraFacilityCategoryViewData categoryViewData)
   {
      Map<Passenger, List<ExtraFacility>> extrasToPassengerMap = MapUtils.EMPTY_MAP;
      if (StringUtils.equalsIgnoreCase(extraFacilityCategory.getCode(),
         ExtraFacilityConstants.CRECHEACADEMY))
      {
         extrasToPassengerMap =
            extraFacilityClassificationService.classifyExtraFacilityByConfiguredAgeLite(
               getPackageModel().getPassengers(), extraFacilityCategory.getExtraFacilities(),
               bookFlowTuiConfigService.getCrecheExtraMinAge());
      }
      else
      {
         extrasToPassengerMap =
            extraFacilityClassificationService.classifyExtraFacilityByAgeLite(getPackageModel()
               .getPassengers(), extraFacilityCategory.getExtraFacilities());
      }
      
      // Fetches the Map of Passenger and Extras.
      if (MapUtils.isNotEmpty(extrasToPassengerMap))
      {
         
         getEntrySetFromExtrasToPassengerMapLite(categoryViewData, extrasToPassengerMap);
         categoryViewData.setExtraFacilityCategoryCode(extraFacilityCategory.getCode());
         categoryViewData.setExtraFacilityGroupCode(extraFacilityCategory.getExtraFacilities()
            .get(0).getExtraFacilityGroup().toString());
         categoryViewData.setAvailable(true);
      }
      
   }
   
   /**
    * @param categoryViewData
    * @param extrasToPassengerMap
    */
   private void getEntrySetFromExtrasToPassengerMapLite(
      final ExtraFacilityCategoryViewData categoryViewData,
      final Map<Passenger, List<ExtraFacility>> extrasToPassengerMap)
   {
      for (final Entry<Passenger, List<ExtraFacility>> extrasMap : extrasToPassengerMap.entrySet())
      {
         final PassengerViewData passengerView = new PassengerViewData();
         
         // Populates the Passenger Viewdata.
         passengerViewDataPopulator.populate(extrasMap.getKey(), passengerView);
         
         /**
          * Sets the selectable flag as true when a passenger opt for swim or stage Extra.
          */
         
         setSwimOrStageExtraSelectionFlag(extrasMap.getKey(), passengerView);
         final List<ExtraFacilityViewData> extrasViewData = new ArrayList<ExtraFacilityViewData>();
         
         // Populates the ExtraFacilityViewdata.
         populateExtraViewDataFromExtraModelLite(extrasMap.getValue(), extrasViewData);
         if (extrasMap.getValue().get(0).getExtraFacilityCategory().getCode()
            .equalsIgnoreCase(ExtraFacilityConstants.SWIMACADEMY))
         {
            setSwimAcademyQuantity(extrasViewData);
         }
         // Sets the selection flag based on passenger opted for that
         // extra.
         setSelectionFlagOfExtra(extrasViewData, extrasMap.getKey());
         final ExtrasToPassengerRelationViewData relationData =
            new ExtrasToPassengerRelationViewData();
         relationData.setPassenger(passengerView);
         relationData.setExtras(extrasViewData);
         categoryViewData.getExtrasToPassengerMapping().add(relationData);
      }
   }
   
   /**
    * The method is used to set the selection flag of each extra corresponding to each passenger.
    *
    * @param extrasViewData the extras view data
    * @param passenger the passenger
    */
   private void setSelectionFlagOfExtra(final List<ExtraFacilityViewData> extrasViewData,
      final Passenger passenger)
   {
      for (final ExtraFacilityViewData extra : extrasViewData)
      {
         extra.setSelected(false);
      }
      if (CollectionUtils.isNotEmpty(passenger.getExtraFacilities()))
      {
         setExtrasSelected(extrasViewData, passenger);
      }
      
   }
   
   /**
    * @param extrasViewData
    * @param passenger
    */
   private void setExtrasSelected(final List<ExtraFacilityViewData> extrasViewData,
      final Passenger passenger)
   {
      for (final ExtraFacility passengerExtra : passenger.getExtraFacilities())
      {
         for (final ExtraFacilityViewData extra : extrasViewData)
         {
            if (StringUtil.equalsIgnoreCase(extra.getCode(), passengerExtra.getExtraFacilityCode()))
            {
               extra.setSelected(true);
            }
         }
      }
   }
   
   /**
    * The method to set the Swim or Stage Selection Flag for each PassengerView.
    *
    * @param passengerModel the passenger model
    * @param passengerViewData the passenger view data
    */
   private void setSwimOrStageExtraSelectionFlag(final Passenger passengerModel,
      final PassengerViewData passengerViewData)
   {
      if (bookFlowTuiConfigService.isStageAndSwimExtraMutuallyExclusive()
         && CollectionUtils.isNotEmpty(passengerModel.getExtraFacilities()))
      {
         flagSwimOrStageExtraSelected(passengerModel, passengerViewData);
      }
      
   }
   
   /**
    * @param passengerModel
    * @param passengerViewData
    */
   private void flagSwimOrStageExtraSelected(final Passenger passengerModel,
      final PassengerViewData passengerViewData)
   {
      for (final ExtraFacility extra : passengerModel.getExtraFacilities())
      {
         if (StringUtil.equalsIgnoreCase(extra.getExtraFacilityCategory().getCode(),
            ExtraFacilityConstants.SWIMACADEMY)
            || StringUtil.equalsIgnoreCase(extra.getExtraFacilityCategory().getCode(),
               ExtraFacilityConstants.STAGEACADEMY))
         {
            passengerViewData.setSwimOrStageExtraSelected(true);
         }
      }
   }
   
   /**
    * The Method Populates the ExtraFacilityViewData.
    *
    * @param modelExtras the model extras
    * @param extrasViewData the extras view data
    */
   private void populateExtraViewDataFromExtraModelLite(final List<ExtraFacility> modelExtras,
      final List<ExtraFacilityViewData> extrasViewData)
   {
      for (final ExtraFacility extraModel : modelExtras)
      {
         final ExtraFacilityViewData extra = new ExtraFacilityViewData();
         extraFacilityViewDataPopulatorLite.populate(extraModel, extra);
         extra.setCurrencyAppendedPrice(getCurrencyAppendedPrice(
            extraModel.getPrices()
               .get(extraFacilityUpdator.getPriceIndexBasedOnExFacilityTypeLite(extraModel))
               .getRate().getAmount(), currencyResolver.getSiteCurrency()));
         extrasViewData.add(extra);
      }
   }
   
   /**
    * Handle no transfer option.
    *
    * @param extraOptionsViewData the extra options view data
    */
   private void handleNoTransferOption(final ExtraOptionsViewData extraOptionsViewData)
   {
      final boolean isDefaultTransferAvailable =
         CollectionUtils.exists(extraOptionsViewData.getExtraFacilityViewDataContainer()
            .getTransferOptions().getExtraFacilityViewData(), new Predicate()
         {
            @Override
            public boolean evaluate(final Object object)
            {
               return ((ExtraFacilityViewData) object).isSelected();
            }
         });
      
      final ExtraFacilityCategoryViewData transferOptionsViewData =
         extraOptionsViewData.getExtraFacilityViewDataContainer().getTransferOptions();
      transferOptionsViewData.setNoTransferOpted(!isDefaultTransferAvailable);
      
   }
   
   /**
    * The method is used to populate the List of ExtraFacility which are present as part of Flight
    * response.
    *
    * @param viewData the FlightOptionsViewData
    */
   private void populateExtraOptionsSummaryView(final ExtraOptionsViewData viewData)
   {
      summaryPanelUrlPopulator.populate(EXTRAOPTIONS, viewData.getSummaryViewData());
   }
   
   // To knock out ConfigurableRoExtraOptionsViewDataPopulator.java -end
   
   /**
    * The method updates the Kids Activity Extras.
    *
    * @param criteria the criteria
    * @return viewData
    */
   @Override
   public ExtraOptionsViewData updateProductForKidsActivity(
      final List<KidsActivityCriteria> criteria)
   {
      // Get the BasePackage from cart
      final BasePackage packageModel = getPackageModel();
      final MultiValueMap extraPassengerMapping = new MultiValueMap();
      
      extraPassengerMappingForKidsActivity(criteria, extraPassengerMapping);
      removeKidsActivityExtras(extraPassengerMapping, criteria);
      updateKidsAcitivityExtrasLite(extraPassengerMapping, criteria, packageModel);
      for (final String eachkey : ((Map<String, List<String>>) extraPassengerMapping).keySet())
      {
         updateExtraFacilityQuantity(eachkey, packageModel);
      }
      saveHolidayService.resetHolidaySavedIndicator();
      // Updates the Quantity of All Extras.
      
      final ExtraOptionsViewData viewData = new ExtraOptionsViewData();
      
      // Populates the ViewData.
      populateExtraOptionsAllView(packageModel, viewData);
      return viewData;
   }
   
   /**
    * @param extraPassengerMapping
    * @param criteria
    */
   private void updateKidsAcitivityExtrasLite(final MultiValueMap extraPassengerMapping,
      final List<KidsActivityCriteria> criteria, final BasePackage packageModel)
   {
      final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
         new HashMap<List<ExtraFacility>, List<String>>();
      for (final Entry<String, List<String>> eachEntry : ((Map<String, List<String>>) extraPassengerMapping)
         .entrySet())
      {
         final ExtraFacility extraSelected =
            fetchExtraFacilityFromPackageExtraStoreLite(packageModel.getId(), criteria.get(0)
               .getExtraCategoryCode(), eachEntry.getKey().toString());
         if (extraSelected != null)
         {
            exFacSelectionComposition.put(Arrays.asList(extraSelected), eachEntry.getValue());
         }
      }
      if (CollectionUtils.isNotEmpty(criteria))
      {
         updateExtraFacilityService.updateExtraLite(packageModel, exFacSelectionComposition);
      }
   }
   
   /**
    * @param extraPassengerMapping
    * @param criteria
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
    * @param criteria
    * @param extraPassengerMapping
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
    * The method updates the Kids Activity Extras.
    *
    * @param criteria the criteria
    * @return viewData
    */
   @Override
   public ExtraOptionsViewData updateProductForSwimActivity(
      final List<KidsActivityCriteria> criteria)
   {
      // Get the BasePackage from cart
      final BasePackage packageModel = getPackageModel();
      final MultiValueMap exFacSelectionComposition = new MultiValueMap();
      updateExtraFacilityServiceForKidsActivity(criteria, packageModel, exFacSelectionComposition);
      if (!exFacSelectionComposition.isEmpty())
      {
         // Creates the relation between Extra and Passenger and updates the
         // Package model.
         updateExtraFacilityService.updateSwimExtra(packageModel, exFacSelectionComposition);
         // TODO : should be re looked.
         for (final KidsActivityCriteria data : criteria)
         {
            if (data.getQuantity() != 0)
            {
               updateSwimExtraQuantity(packageModel, data.getQuantity(), data.getExtraCode(),
                  getSelectedPassenger(packageModel, data.getPassengerId()));
            }
         }
      }
      saveHolidayService.resetHolidaySavedIndicator();
      // Updates the Quantity of All Extras.
      final ExtraOptionsViewData viewData = new ExtraOptionsViewData();
      
      // Populates the ViewData.
      populateExtraOptionsAllView(packageModel, viewData);
      return viewData;
   }
   
   /**
    * @param criteria
    * @param packageModel
    * @param exFacSelectionComposition
    */
   private void updateExtraFacilityServiceForKidsActivity(
      final List<KidsActivityCriteria> criteria, final BasePackage packageModel,
      final MultiValueMap exFacSelectionComposition)
   {
      for (final KidsActivityCriteria data : criteria)
      {
         if (data.getQuantity() == 0)
         {
            updateExtraFacilityService.removeExtraFacility(data.getExtraCategoryCode(),
               data.getExtraCode(), data.getPassengerId());
            continue;
         }
         final ExtraFacility selectedPackageExtraFacility =
            fetchSelectedPackageExtraFacilityLite(data.getExtraCode(), packageModel.getId());
         exFacSelectionComposition.put(data.getPassengerId(), selectedPackageExtraFacility);
      }
   }
   
   /**
    * Removes the product for kids activity.
    *
    * @param criteria the criteria
    * @return the extra options view data
    */
   @Override
   public ExtraOptionsViewData removeProductForKidsActivity(
      final List<KidsActivityCriteria> criteria)
   {
      final BasePackage packageModel = getPackageModel();
      for (final KidsActivityCriteria data : criteria)
      {
         updateExtraFacilityService.removeExtraFacility(data.getExtraCategoryCode(),
            data.getExtraCode(), data.getPassengerId());
         
      }
      saveHolidayService.resetHolidaySavedIndicator();
      // Updates the Quantity of All Extras.
      final ExtraOptionsViewData viewData = new ExtraOptionsViewData();
      
      populateExtraOptionsContainerView(viewData);
      populateExtraOptionsPackageView(packageModel, viewData);
      // here we are updating the container view data with package view data
      extraFacilityUpdator.updateExtraOptionsContainerView(packageModel,
         viewData.getExtraFacilityViewDataContainer());
      
      return viewData;
   }
   
   /**
    * Update extra facility quantity.
    *
    * @param extraCode the extra code
    * @param packageModel the package model
    */
   private void updateExtraFacilityQuantity(final String extraCode, final BasePackage packageModel)
   {
      final List<String> kidsActivityCategoryList =
         extraFacilityUpdator.getKidsActivityCriteriaList();
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (kidsActivityCategoryList.contains(categoryModel.getCode()))
         {
            for (final ExtraFacility extra : categoryModel.getExtraFacilities())
            {
               updateExtraFacilitySelectedQuantityForChild(extraCode, extra);
            }
            continue;
         }
         updateExcursionOrAttraction(extraCode, categoryModel);
      }
   }
   
   /**
    * @param extraCode
    * @param categoryModel
    */
   private void updateExcursionOrAttraction(final String extraCode,
      final ExtraFacilityCategory categoryModel)
   {
      if (checkExcursionOrAttraction(categoryModel))
      {
         for (final ExtraFacility extra : categoryModel.getExtraFacilities())
         {
            if (StringUtil.equalsIgnoreCase(extraCode, extra.getExtraFacilityCode()))
            {
               extraFacilityQuantityCalculationService.updateExtraFacilitySelectedQuantity(extra,
                  extra.getPassengers().size());
            }
         }
         return;
      }
      else
      {
         iterateCategoryModelForExcursionOrAttraction(extraCode, categoryModel);
      }
   }
   
   /**
    * @param extraCode
    * @param categoryModel
    */
   private void iterateCategoryModelForExcursionOrAttraction(final String extraCode,
      final ExtraFacilityCategory categoryModel)
   {
      for (final ExtraFacility extra : categoryModel.getExtraFacilities())
      {
         if (StringUtil.equalsIgnoreCase(extraCode, extra.getExtraFacilityCode()))
         {
            extraFacilityQuantityCalculationService.updateExtraFacilitySelectedQuantity(extra,
               extra.getPassengers());
         }
      }
   }
   
   /**
    * @param categoryModel
    * @return boolean
    */
   private boolean checkExcursionOrAttraction(final ExtraFacilityCategory categoryModel)
   {
      return StringUtil.equalsIgnoreCase(categoryModel.getSuperCategoryCode(),
         ExtraFacilityConstants.EXCURSION)
         || StringUtil.equalsIgnoreCase(categoryModel.getSuperCategoryCode(),
            ExtraFacilityConstants.ATTRACTION);
   }
   
   /**
    * Update extra facility selected quantity for child.
    *
    * @param extraCode the extra code
    * @param extra the extra
    */
   private void updateExtraFacilitySelectedQuantityForChild(final String extraCode,
      final ExtraFacility extra)
   {
      if (StringUtil.equalsIgnoreCase(extraCode, extra.getExtraFacilityCode()))
      {
         extraFacilityQuantityCalculationService.updateExtraFacilitySelectedQuantity(extra, extra
            .getPassengers().size(),
            extra.getPrices().get(extraFacilityUpdator.getPriceIndexBasedOnExFacilityType(extra))
               .getPriceType());
      }
   }
   
   /**
    * Update swim extra quantity.
    *
    * @param packageModel the package model
    * @param quantity the quantity
    * @param extraCode the extra code
    * @param passenger the passenger
    */
   private void updateSwimExtraQuantity(final BasePackage packageModel, final int quantity,
      final String extraCode, final Passenger passenger)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (StringUtil.equalsIgnoreCase(categoryModel.getCode(),
            ExtraFacilityConstants.SWIMACADEMY))
         {
            iterateCategoryModelForSwimExtra(quantity, extraCode, passenger, categoryModel);
         }
      }
   }
   
   /**
    * @param quantity
    * @param extraCode
    * @param passenger
    * @param categoryModel
    */
   private void iterateCategoryModelForSwimExtra(final int quantity, final String extraCode,
      final Passenger passenger, final ExtraFacilityCategory categoryModel)
   {
      for (final ExtraFacility extra : categoryModel.getExtraFacilities())
      {
         if (StringUtil.equalsIgnoreCase(extra.getExtraFacilityCode(), extraCode)
            && extra.getPassengers().contains(passenger))
         {
            extraFacilityQuantityCalculationService.updateExtraFacilitySelectedQuantity(extra,
               quantity, PriceType.CHILD);
         }
      }
   }
   
   /**
    * Gets the selected passenger according to updation of extrafacility.
    *
    * @param packageModel the package model
    * @param passengerId the passenger id
    * @return the selected passenger
    */
   private Passenger getSelectedPassenger(final BasePackage packageModel, final String passengerId)
   {
      for (final Passenger passenger : packageModel.getPassengers())
      {
         if (StringUtils.equalsIgnoreCase(passenger.getId().toString(), passengerId))
         {
            return passenger;
         }
      }
      return new Passenger();
   }
   
   /**
    * Perform package comparison and update the package model.
    *
    * @param extraOptionsViewData the extra options view data
    */
   @Override
   public void doPackageComparison(final ExtraOptionsViewData extraOptionsViewData)
   {
      
      // this will remove the promotional code from the packageModel in
      // backward flow
      final BasePackage packageModel = getPackageModel();
      promotionalCodeValidationService.removePromotionalCode(packageModel);
      if (isFlightExtrasSearchRequired())
      {
         
         final List<Feedback> unAvailableExtras = new ArrayList<Feedback>();
         final List<Feedback> feedBackList = java.util.Collections.emptyList();
         final List<ExtraFacilityCategory> swappedExtras =
            packageSwap.swapPackage(packageModel, unAvailableExtras, feedBackList);
         
         if (CollectionUtils.isNotEmpty(swappedExtras))
         {
            packageModel.setExtraFacilityCategories(swappedExtras);
         }
         packageSwap.postSwapUpdation(packageModel);
         final List<AlertViewData> alerts = new ArrayList<AlertViewData>();
         if (CollectionUtils.isNotEmpty(unAvailableExtras))
         {
            populateAlertViewData(unAvailableExtras, alerts);
         }
         alerts.addAll(alertPopulator.populateTotalCostAlert(alerts, feedBackList));
         extraOptionsViewData.setAlertMessages(alerts);
      }
   }
   
   /**
    * Populate alert view data.
    *
    * @param unAvailableExtras the un available extras
    * @param alerts the alerts
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
   
   /**
    * Checks if flight extras search is required.
    *
    * @return searchRequiredFlag. true, if is flight extras search required
    */
   public boolean isFlightExtrasSearchRequired()
   {
      
      final BasePackage packageModel = getPackageModel();
      boolean searchRequiredFlag = false;
      
      if (packageModel.getListOfHighlights().contains(HighLights.LAPLAND_DAYTRIP))
      {
         searchRequiredFlag = false;
         
      }
      else
      {
         
         searchRequiredFlag =
            sessionService.getAttribute(SessionObjectKeys.ORIGINALPACKAGECODE) != null
               && !StringUtils.equalsIgnoreCase(packageComponentService.getStay(packageModel)
                  .getCode(), sessionService.getAttribute(SessionObjectKeys.ORIGINALPACKAGECODE)
                  .toString());
      }
      
      return searchRequiredFlag;
   }
   
   private void updateStoreTransferExtrasLite(final String transferCode,
      final boolean resetFreeTransferOption)
   {
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService.getAttribute(PACKAGE_EXTRA_FACILITY_STORE);
      final List<ExtraFacilityCategory> extraFacilityCategoryList =
         packageExtraFacilityStore.getExtraFacilityLite(getPackageModel().getId());
      for (final ExtraFacilityCategory exFacilityCategory : extraFacilityCategoryList)
      {
         if (transferOptionExtraFacilityPresent(exFacilityCategory.getCode()))
         {
            iterateExtraFacilityCategoryForTransferLite(transferCode, exFacilityCategory,
               resetFreeTransferOption);
         }
      }
   }
   
   /**
    * @param transferCode
    * @param exFacilityCategory
    */
   private void iterateExtraFacilityCategoryForTransferLite(final String transferCode,
      final ExtraFacilityCategory exFacilityCategory, final boolean resetFreeTransferOption)
   {
      for (final ExtraFacility extraFacilityModel : exFacilityCategory.getExtraFacilities())
      {
         if (StringUtils.equalsIgnoreCase(extraFacilityModel.getExtraFacilityCode(), transferCode))
         {
            extraFacilityModel.setSelected(true);
         }
         else
         {
            extraFacilityModel.setSelected(false);
         }
         // This will reset the selection type if we have a free car hire and
         // coach transfer is also free
         if (resetFreeTransferOption)
         {
            extraFacilityModel.setSelection(FacilitySelectionType.SELECTABLE);
         }
      }
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.PackageExtraFacilityFacade#
    * updateProductForExcursions(java.util.List)
    */
   @Override
   public ExtraOptionsViewData updateProductForExcursions(final List<AncillaryCriteria> criteria)
   {
      final BasePackage packageModel = getPackageModel();
      updateExtraFacilityService.removeExtraCategoryFromPackage(criteria.get(0).getCategoryCode());
      if (!removalRequired(criteria))
      {
         updateAncilliary(criteria, packageModel);
      }
      saveHolidayService.resetHolidaySavedIndicator();
      final ExtraOptionsViewData viewData = new ExtraOptionsViewData();
      populateExtraOptionsAllView(packageModel, viewData);
      return viewData;
   }
   
   /**
    * Update ancilliary.
    *
    * @param criteria the criteria
    * @param packageModel the package model
    */
   private void updateAncilliary(final List<AncillaryCriteria> criteria,
      final BasePackage packageModel)
   {
      if (validateIfAdultExtraSelected(criteria, packageModel.getId()))
      {
         final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
            new HashMap<List<ExtraFacility>, List<String>>();
         for (final AncillaryCriteria data : criteria)
         {
            if (data.getQuantity() > 0)
            {
               // Fetches the selected ExtraFacility from Store.
               final ExtraFacility selectedExtra =
                  fetchSelectedPackageExtraFacilityLite(data.getExtraCode(), packageModel.getId());
               final int quantity = data.getQuantity();
               final List<String> applicablePassengersIds =
                  PassengerUtils.getPassengersIdWithAgeRang(selectedExtra
                     .getExtraFacilityRestrictions().getMinAge(), selectedExtra
                     .getExtraFacilityRestrictions().getMaxAge(), packageModel.getPassengers());
               
               exFacSelectionComposition.put(Arrays.asList(selectedExtra),
                  applicablePassengersIds.subList(0, quantity));
            }
         }
         updateExtraFacilityService.updateExtraLite(packageModel, exFacSelectionComposition);
         updateAncilliaryQuantity(packageModel, criteria);
      }
   }
   
   /**
    * Update ancilliary quantity.
    *
    * @param packageModel the package model
    * @param criteria the criteria
    */
   private void updateAncilliaryQuantity(final BasePackage packageModel,
      final List<AncillaryCriteria> criteria)
   {
      for (final AncillaryCriteria data : criteria)
      {
         if (data.getQuantity() > 0)
         {
            updateExtraFacilityQuantity(data.getExtraCode(), packageModel);
         }
      }
   }
   
   /**
    * Validate if adult extra selected.
    *
    * @param criteria the criteria
    * @param packageId the package id
    * @return true, if successful
    */
   @SuppressWarnings(BOXING)
   private boolean validateIfAdultExtraSelected(final List<AncillaryCriteria> criteria,
      final String packageId)
   {
      
      for (final AncillaryCriteria data : criteria)
      {
         if (data.getQuantity() > 0)
         {
            // Fetches the selected ExtraFacility from Store.
            final ExtraFacility selectedExtra =
               fetchSelectedPackageExtraFacilityLite(data.getExtraCode(), packageId);
            if (selectedExtra.getPrices().get(0).getPriceType() == uk.co.tui.book.domain.lite.PriceType.ADULT)
            {
               return true;
            }
         }
      }
      return false;
   }
   
   /**
    * The updator method to update the Attraction extras.
    *
    * @param criteria the criteria
    * @return the extra options view data
    */
   @Override
   public ExtraOptionsViewData updateProductForAttractions(final List<AncillaryCriteria> criteria)
   {
      final BasePackage packageModel = getPackageModel();
      updateExtraFacilityService.removeExtraAliasSuperCategoryFromPackage(criteria.get(0)
         .getAliasSuperCategoryCode());
      if (!removalRequired(criteria))
      {
         updateAncilliary(criteria, packageModel);
      }
      saveHolidayService.resetHolidaySavedIndicator();
      final ExtraOptionsViewData viewData = new ExtraOptionsViewData();
      // Populates the ViewData.
      populateExtraOptionsAllView(packageModel, viewData);
      return viewData;
   }
   
   /**
    * Check for removal.
    *
    * @param criteria the criteria
    * @return true, if successful
    */
   private boolean removalRequired(final List<AncillaryCriteria> criteria)
   {
      boolean removeCategory = true;
      final Iterator<AncillaryCriteria> criteriaIterator = criteria.iterator();
      while (criteriaIterator.hasNext())
      {
         if (criteriaIterator.next().getQuantity() != 0)
         {
            removeCategory = false;
            break;
         }
      }
      return removeCategory;
   }
   
   /**
    * Populate extra options static content view data.
    *
    * @param extraOptionsViewData the extra options view data
    */
   @Override
   public void populateExtraOptionsStaticContentViewData(
      final ExtraOptionsViewData extraOptionsViewData)
   {
      final ExtraOptionsStaticContentViewData extraOptionsStaticContentViewData =
         new ExtraOptionsStaticContentViewData();
      extraStaticContentViewDataPopulator.populate(new Object(), extraOptionsStaticContentViewData);
      extraOptionsViewData.setExtraOptionsStaticContentViewData(extraOptionsStaticContentViewData);
   }
   
   /**
    * Populates the extra options content.
    *
    * @param extraOptionsViewData the extra options view data
    */
   private void populateExtraOptionsContentViewData(final ExtraOptionsViewData extraOptionsViewData)
   {
      final ExtraOptionsContentViewData extraContentViewData = new ExtraOptionsContentViewData();
      final ContentViewData extraContent = new ContentViewData();
      extraOptionsContentViewDataPopulator.populate(new Object(), extraContent);
      extraContentViewData.setExtraContentViewData(extraContent);
      extraOptionsViewData.setExtraOptionsContentViewData(extraContentViewData);
      
   }
   
   /**
    * Render car upgrade options.
    *
    * @param extraOptionsViewData the extra options view data
    * @param packageModel the package model
    */
   private void renderCarUpgradeOptions(final ExtraOptionsViewData extraOptionsViewData,
      final BasePackage packageModel)
   {
      if (isCarHireAvailable(packageModel))
      {
         CarHireUpgradeExtraFacilityStore carHireUpgradeExtraFacilityStore =
            getCarHireUpgradeExtraFacilityStore();
         
         if (nullCheckForCarHireUpgradeOrExtraFacility(packageModel,
            carHireUpgradeExtraFacilityStore))
         {
            CarHireSearchResponse carHireSearchResponse = null;
            try
            {
               carHireSearchResponse = carHireServiceLite.getCarHireOptions(packageModel);
            }
            catch (final BookServiceException e)
            {
               LOG.error("TUISystemException : " + e.getMessage());
               throw new TUISystemException(e.getErrorCode(), e);
            }
            
            carHireSearchResponse.setPackageId(packageModel.getId());
            carHireUpgradeExtraFacilityStore = new CarHireUpgradeExtraFacilityStore();
            carHireUpgradeExtraFacilityStore.add(carHireSearchResponse);
            sessionService.setAttribute(SessionObjectKeys.CARHIRE_UPGRADE_EXTRA_FACILITY_STORE,
               carHireUpgradeExtraFacilityStore);
         }
         
         updatePackageModelLite(packageModel,
            carHireUpgradeExtraFacilityStore.getExtraFacilityLite(packageModel.getId()));
         updatePackageIntoCart();
         final List<ExtraFacilityCategoryViewData> extraFacilityCategories =
            new ArrayList<ExtraFacilityCategoryViewData>();
         carHireUpgradeViewDataPopulatorLite.populate(
            carHireUpgradeExtraFacilityStore.getExtraFacilityLite(packageModel.getId()),
            extraFacilityCategories);
         updateSelectedCarHireToViewData(packageModel, extraFacilityCategories);
         extraOptionsViewData.getExtraFacilityViewDataContainer().setCarHireUpgrade(
            extraFacilityCategories);
         populateBasicCarType(extraOptionsViewData, extraFacilityCategories);
      }
   }
   
   /**
    * @param packageModel
    * @param extraFacilityCategories
    */
   private void updateSelectedCarHireToViewData(final BasePackage packageModel,
      final List<ExtraFacilityCategoryViewData> extraFacilityCategories)
   {
      for (final ExtraFacilityCategory eachExtraCategory : packageModel
         .getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(eachExtraCategory.getSuperCategoryCode(),
            ExtraFacilityConstants.CAR_HIRE))
         {
            selectedCarHireViewDataPopulator.populate(eachExtraCategory, extraFacilityCategories);
            break;
         }
      }
      
   }
   
   /**
    * @param packageModel
    * @param carHireUpgradeExtraFacilityStore
    * @return boolean
    */
   private boolean nullCheckForCarHireUpgradeOrExtraFacility(final BasePackage packageModel,
      final CarHireUpgradeExtraFacilityStore carHireUpgradeExtraFacilityStore)
   {
      return carHireUpgradeExtraFacilityStore == null
         || CollectionUtils.isEmpty(carHireUpgradeExtraFacilityStore
            .getExtraFacilityLite(packageModel.getId()));
   }
   
   /**
    * Populate basic car type.
    *
    * @param extraOptionsViewData the extra options view data
    * @param extraFacilityCategories the extra facility categories
    */
   private void populateBasicCarType(final ExtraOptionsViewData extraOptionsViewData,
      final List<ExtraFacilityCategoryViewData> extraFacilityCategories)
   {
      
      for (final ExtraFacilityCategoryViewData category : extraFacilityCategories)
      {
         for (final ExtraFacilityViewData carData : category.getExtraFacilityViewData())
         {
            if (StringUtils.equalsIgnoreCase(carData.getCode(),
               ExtraFacilityConstants.BASIC_CARHIRE))
            {
               extraOptionsViewData.setAlamoCarHire(true);
            }
         }
      }
   }
   
   /**
    * Populate accommodation type.
    *
    * @param packageModel the package model
    * @param extraOptionsViewData the extra options view data
    */
   private void populateAccommodationType(final BasePackage packageModel,
      final ExtraOptionsViewData extraOptionsViewData)
   {
      // REVISIT:This should be changed as part of design decision
      final Stay stay = packageComponentService.getStay(packageModel);
      if (packageModel instanceof InclusivePackage && stay.getType() != null
         && stay.getType() == StayType.VILLA)
      {
         extraOptionsViewData.setVillaAcommodation(true);
      }
   }
   
   /**
    * Update package model.
    *
    */
   private void updatePackageModelLite(final BasePackage packageModel,
      final List<ExtraFacilityCategory> extraFacilityCategoryModelList)
   {
      if (!isDefaultTransferExitsInPackageModel(packageModel))
      {
         updateDefaultTransferOptionToPakageModelLite(packageModel, extraFacilityCategoryModelList);
      }
      else
      {
         swapDefaultCarHireExtraWithRequiredDetails(packageModel);
      }
   }
   
   /**
    * Swap default car hire extra with required details.
    *
    * @param packageModel the package model
    */
   private void swapDefaultCarHireExtraWithRequiredDetails(final BasePackage packageModel)
   {
      for (final ExtraFacilityCategory extraCat : packageModel.getExtraFacilityCategories())
      {
         if (validBasicCarHire(extraCat))
         {
            final String extraFacCode = extraCat.getExtraFacilities().get(0).getExtraFacilityCode();
            final ExtraFacility swappedExtra =
               fetchSelectedCarHireExFacilityCategoryLite(extraFacCode);
            if (swappedExtra != null)
            {
               ((CarHireExtraFacility) extraCat.getExtraFacilities().get(0))
                  .setExtraFacilitySchedule(swappedExtra.getExtraFacilitySchedule());
            }
            updateDepositDetails(extraCat, swappedExtra);
            break;
            
         }
      }
   }
   
   /**
    * Method to update deposit details.
    *
    * @param extraCat
    * @param swappedExtra
    */
   private void updateDepositDetails(final ExtraFacilityCategory extraCat,
      final ExtraFacility swappedExtra)
   {
      if (isValidDepotDetails(swappedExtra))
      {
         ((CarHireExtraFacility) extraCat.getExtraFacilities().get(0))
            .setDropUpOff(((CarHireExtraFacility) swappedExtra).getDropUpOff());
         ((CarHireExtraFacility) extraCat.getExtraFacilities().get(0))
            .setPickupDepot(((CarHireExtraFacility) swappedExtra).getPickupDepot());
         ((CarHireExtraFacility) extraCat.getExtraFacilities().get(0))
            .setInventoryCode(((CarHireExtraFacility) swappedExtra).getInventoryCode());
      }
   }
   
   /**
    * @param swappedExtra
    * @return
    */
   private boolean isValidDepotDetails(final ExtraFacility swappedExtra)
   {
      return swappedExtra != null && ((CarHireExtraFacility) swappedExtra).getDropUpOff() != null
         && ((CarHireExtraFacility) swappedExtra).getPickupDepot() != null;
   }
   
   /**
    * @param extraCat
    * @return
    */
   private boolean validBasicCarHire(final ExtraFacilityCategory extraCat)
   {
      return validateExtraFacilityGroupAndCarHire(extraCat)
         && StringUtils.equalsIgnoreCase(extraCat.getCode(), "BASIC");
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.PackageExtraFacilityFacade#
    * updateProductForCarHireUpgradeOptions(java.lang.String)
    */
   @Override
   public ExtraOptionsViewData updateProductForCarHireUpgradeOptions(final String code)
   {
      final BasePackage packageModel = getPackageModel();
      
      final boolean carHireExtra = isCarHireTransfer();
      // Remove if any other transfer options are selected
      removeDefaultTransferCategories();
      // Reset transfer options in store if any
      updateStoreTransferExtrasLite(code, false);
      validateCode(code, packageModel, carHireExtra);
      saveHolidayService.resetHolidaySavedIndicator();
      final ExtraOptionsViewData extraOptionsViewData = new ExtraOptionsViewData();
      final CarHireUpgradeExtraFacilityStore carHireUpgradeExtraFacilityStore =
         getCarHireUpgradeExtraFacilityStore();
      final List<ExtraFacilityCategoryViewData> extraFacilityCategories =
         new ArrayList<ExtraFacilityCategoryViewData>();
      updatePackageIntoCart();
      if (carHireUpgradeExtraFacilityStore != null)
      {
         carHireUpgradeViewDataPopulatorLite.populate(
            carHireUpgradeExtraFacilityStore.getExtraFacilityLite(packageModel.getId()),
            extraFacilityCategories);
      }
      updateSelectedCarHireToViewData(packageModel, extraFacilityCategories);
      extraOptionsViewData.getExtraFacilityViewDataContainer().setCarHireUpgrade(
         extraFacilityCategories);
      populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      populateAccommodationType(packageModel, extraOptionsViewData);
      populateBasicCarType(extraOptionsViewData, extraFacilityCategories);
      return extraOptionsViewData;
   }
   
   /**
    * @param code
    * @param packageModel
    * @param carHireExtra
    */
   private void validateCode(final String code, final BasePackage packageModel,
      final boolean carHireExtra)
   {
      final ExtraFacility selectExFacility;
      if (ExtraFacilityConstants.NOTF.equalsIgnoreCase(code))
      {
         resetCarHireUpgrades(packageModel);
         selectExFacility = fetchSelectedPackageExtraFacilityLite(code, packageModel.getId());
         updateSelectedCarHire(packageModel, ExtraFacilityUtils.deepCopyExtra(selectExFacility));
         if (carHireExtra)
         {
            updateCarHireExtraDiscription();
         }
      }
      else
      {
         // getting the selected extra from the store
         
         selectExFacility = fetchSelectedCarHireExFacilityCategoryLite(code);
         
         if (selectExFacility != null)
         {
            updateSelectedCarHire(packageModel, ExtraFacilityUtils.deepCopyExtra(selectExFacility));
         }
      }
   }
   
   private boolean isCarHireTransfer()
   {
      final BasePackage packageModel = getPackageModel();
      for (final ExtraFacilityCategory category : packageModel.getExtraFacilityCategories())
      {
         if (category.getSuperCategoryCode().equalsIgnoreCase(ExtraFacilityConstants.CAR_HIRE))
         {
            return true;
         }
      }
      return false;
   }
   
   private void updateCarHireExtraDiscription()
   {
      final BasePackage packageModel = getPackageModel();
      for (final ExtraFacilityCategory category : packageModel.getExtraFacilityCategories())
      {
         if (category.getCode().equalsIgnoreCase(ExtraFacilityConstants.TRANSFER))
         {
            setCarHireDiscription(category);
         }
      }
   }
   
   private void setCarHireDiscription(final ExtraFacilityCategory category)
   {
      for (final ExtraFacility extra : category.getExtraFacilities())
      {
         if (extra.getExtraFacilityCode().equalsIgnoreCase(ExtraFacilityConstants.NOTF))
         {
            extra.setDescription("No CarHire");
         }
      }
   }
   
   /**
    * Reset car hire upgrades.
    *
    * @param packageModel the package model
    */
   private void resetCarHireUpgrades(final BasePackage packageModel)
   {
      final ExtraFacilityCategory existingCarHire =
         getExistingCarHireCategoryFromPackage(packageModel);
      if (existingCarHire != null)
      {
         updateExtraFacilityService.removeExtraSuperCategoryFromPackage(existingCarHire
            .getSuperCategoryCode());
      }
      final CarHireUpgradeExtraFacilityStore carHireStore = getCarHireUpgradeExtraFacilityStore();
      // Reset the car hire upgrades in Store
      for (final ExtraFacilityCategory categoryModel : carHireStore
         .getExtraFacilityLite(packageModel.getId()))
      {
         for (final ExtraFacility extra : categoryModel.getExtraFacilities())
         {
            extra.setSelected(false);
         }
      }
   }
   
   /**
    * Gets the existing car hire category from package.
    *
    * @param packageModel the package model
    * @return extraFacility Category Model
    */
   private ExtraFacilityCategory getExistingCarHireCategoryFromPackage(
      final BasePackage packageModel)
   {
      
      for (final ExtraFacilityCategory category : packageModel.getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(category.getSuperCategoryCode(),
            BookFlowConstants.CAR_HIRE))
         {
            return category;
         }
      }
      return null;
   }
   
   /**
    * Gets the existing extra from package model.
    *
    * @param selectedCategoryCode the selected category code
    * @return extraFacility Model
    */
   private ExtraFacility getExistingExtraFromPackageModel(final String selectedCategoryCode)
   {
      for (final ExtraFacilityCategory categoryModel : getPackageModel()
         .getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(selectedCategoryCode,
            categoryModel.getSuperCategoryCode()))
         {
            return categoryModel.getExtraFacilities().get(0);
            
         }
      }
      return null;
   }
   
   private ExtraFacility fetchSelectedCarHireExFacilityCategoryLite(final String exFacCode)
   {
      final CarHireUpgradeExtraFacilityStore carHireStore = getCarHireUpgradeExtraFacilityStore();
      ExtraFacility selectedExtraFacility = null;
      if (carHireStore != null)
      {
         for (final ExtraFacilityCategory categoryModel : carHireStore
            .getExtraFacilityLite(getPackageModel().getId()))
         {
            selectedExtraFacility = iterateCategoryModelForCarHireLite(exFacCode, categoryModel);
            if (SyntacticSugar.isNotNull(selectedExtraFacility))
            {
               return selectedExtraFacility;
            }
         }
         return selectedExtraFacility;
      }
      return null;
   }
   
   private ExtraFacility iterateCategoryModelForCarHireLite(final String exFacCode,
      final ExtraFacilityCategory categoryModel)
   {
      for (final ExtraFacility extra : categoryModel.getExtraFacilities())
      {
         if (StringUtils.equalsIgnoreCase(extra.getExtraFacilityCode(), exFacCode))
         {
            return extra;
         }
      }
      return null;
   }
   
   /**
    * Gets the car hire upgrade extra facility store.
    *
    * @return CarHireExtraFacilityStore
    */
   private CarHireUpgradeExtraFacilityStore getCarHireUpgradeExtraFacilityStore()
   {
      return (CarHireUpgradeExtraFacilityStore) sessionService
         .getAttribute(SessionObjectKeys.CARHIRE_UPGRADE_EXTRA_FACILITY_STORE);
   }
   
   /**
    * Update package into cart.
    */
   private void updatePackageIntoCart()
   {
      packageCartService.updateCart(getPackageModel());
   }
   
   /**
    * Checks if is car hire available.
    *
    * @param packageModel the package model
    * @return the boolean
    */
   private boolean isCarHireAvailable(final BasePackage packageModel)
   {
      final List<HighLights> highlights = packageModel.getListOfHighlights();
      return highlights != null && highlights.contains(HighLights.FREE_CAR_HIRE);
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
    * This method updates the default transfer option to package model.
    *
    * @param packageModel the package model
    * @param extraFacilityCategoryModelList the extra facility category model list
    */
   private void updateDefaultTransferOptionToPakageModelLite(final BasePackage packageModel,
      final List<ExtraFacilityCategory> extraFacilityCategoryModelList)
   {
      for (final ExtraFacilityCategory extraFacilityCategoryModel : extraFacilityCategoryModelList)
      {
         if (transferOptionExtraFacilityPresent(extraFacilityCategoryModel.getCode())
            || StringUtils.equalsIgnoreCase(extraFacilityCategoryModel.getSuperCategoryCode(),
               ExtraFacilityConstants.CAR_HIRE))
         {
            iterateExtraFacilityCategoryForDefaultTransferLite(packageModel,
               extraFacilityCategoryModel);
         }
      }
   }
   
   /**
    * @param packageModel
    * @param extraFacilityCategoryModel
    */
   private void iterateExtraFacilityCategoryForDefaultTransferLite(final BasePackage packageModel,
      final ExtraFacilityCategory extraFacilityCategoryModel)
   {
      for (final ExtraFacility extraFacilityModel : extraFacilityCategoryModel.getExtraFacilities())
      {
         if (isFreeOrIncluded(extraFacilityModel))
         {
            
            updateExtraFacilityService.updateExtraFacilityLite(packageModel.getPassengers(),
               Arrays.asList(extraFacilityModel), packageModel);
            
            extraFacilityQuantityCalculationService.updateTransferExtraFacilityQuantity(
               packageModel.getExtraFacilityCategories(), packageModel.getPassengers(),
               extraFacilityModel.getExtraFacilityCode());
            packageCartService.updateCart(packageModel);
            break;
         }
      }
   }
   
   /**
    * Is free or included extra facility
    *
    * @param extraFacilityModel
    * @return boolean
    */
   private boolean isFreeOrIncluded(final ExtraFacility extraFacilityModel)
   {
      return extraFacilityModel.isSelected()
         && (StringUtils.equalsIgnoreCase(extraFacilityModel.getSelection().toString(),
            FacilitySelectionType.FREE.toString()) || StringUtils
            .equalsIgnoreCase(extraFacilityModel.getSelection().toString(),
               FacilitySelectionType.INCLUDED.toString()));
   }
   
   /**
    * This method checks if extra facility is already exists in package Model.
    *
    * @param packageModel the package model
    * @return boolean
    */
   private boolean isDefaultTransferExitsInPackageModel(final BasePackage packageModel)
   {
      // We know that for SKY & FTW we have already Coach Transfers & for ALC
      // Taxi Transfer. No need to update again
      if (!isPreSelectTransferAvailableProduct(packageModel))
      {
         for (final ExtraFacilityCategory extraFacilityCategoryModel : packageModel
            .getExtraFacilityCategories())
         {
            if (validateExtraFacilityGroupAndCarHire(extraFacilityCategoryModel))
            {
               return true;
            }
         }
      }
      return false;
   }
   
   /**
    * @param extraFacilityCategoryModel
    * @return boolean
    */
   private boolean validateExtraFacilityGroupAndCarHire(
      final ExtraFacilityCategory extraFacilityCategoryModel)
   {
      return extraFacilityCategoryModel.getExtraFacilityGroup() == ExtraFacilityGroup.PACKAGE
         && ExtraFacilityConstants.TRANSFER_OPTION_CATEGORY_CODES
            .contains(extraFacilityCategoryModel.getCode())
         || StringUtils.equalsIgnoreCase(extraFacilityCategoryModel.getSuperCategoryCode(),
            ExtraFacilityConstants.CAR_HIRE);
   }
   
   /**
    * Checks if preselect of transfer available product type
    *
    * @param packageModel
    * @return boolean
    */
   private boolean isPreSelectTransferAvailableProduct(final BasePackage packageModel)
   {
      return packageModel.getProductType() != null
         && "SKY,ALC,FTW".contains(packageModel.getProductType());
   }
   
   /**
    * Updates the relative price difference.
    *
    * @param transfeOptions the transfer options
    */
   private void updateRelativePriceDifference(final List<ExtraFacilityViewData> transfeOptions)
   {
      final ExtraFacilityViewData selectedExtraFacilityViewData =
         (ExtraFacilityViewData) CollectionUtils.find(transfeOptions, new Predicate()
         {
            @Override
            public boolean evaluate(final Object obj)
            {
               return ((ExtraFacilityViewData) obj).isSelected();
            }
         });
      if (selectedExtraFacilityViewData != null)
      {
         if (selectedExtraFacilityViewData.isFree())
         {
            
            LOG.debug("selected extrafacilityview is free");
         }
         final BigDecimal selectedTransferPrice = selectedExtraFacilityViewData.getPrice();
         updateCurrencyAppendedRelativePrice(transfeOptions, selectedTransferPrice);
      }
   }
   
   /**
    * Updates the Currency Appended Relative Price
    *
    * @param transfeOptions
    * @param selectedTransferPrice
    */
   private void updateCurrencyAppendedRelativePrice(
      final List<ExtraFacilityViewData> transfeOptions, final BigDecimal selectedTransferPrice)
   {
      BigDecimal priceDifference = BigDecimal.ZERO;
      BigDecimal unitPrice = BigDecimal.ZERO;
      for (final ExtraFacilityViewData extraFacilityViewData : transfeOptions)
      {
         if (isRelativePriceApplicable(extraFacilityViewData))
         {
            final String currencyCode = currencyResolver.getSiteCurrency();
            priceDifference = extraFacilityViewData.getPrice().subtract(selectedTransferPrice);
            extraFacilityViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(
               priceDifference, currencyCode));
            unitPrice =
               priceDifference.divide(new BigDecimal(extraFacilityViewData.getQuantity()),
                  RoundingMode.HALF_UP);
            if (StringUtils.isEmpty(extraFacilityViewData.getCurrencyAppendedPerTaxiPrice()))
            {
               extraFacilityViewData.setCurrencyAppendedPerPersonPrice(getCurrencyAppendedPrice(
                  unitPrice, currencyCode));
            }
            else
            {
               extraFacilityViewData.setCurrencyAppendedPerTaxiPrice(getCurrencyAppendedPrice(
                  unitPrice, currencyCode));
            }
         }
      }
   }
   
   /**
    * Checks is relative price update is applicable
    *
    * @param extraFacilityViewData
    * @return boolean
    */
   private boolean isRelativePriceApplicable(final ExtraFacilityViewData extraFacilityViewData)
   {
      return !extraFacilityViewData.isSelected() && !extraFacilityViewData.isFree();
   }
   
   /**
    * Populates the room extras content after the ajax calls.
    *
    * @param viewData the view data
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
   
   /**
    * Update the selected car hire to package model by removing the existing car hire in package
    * model.
    *
    * @param packageModel
    * @param selectExFacility
    */
   private void updateSelectedCarHire(final BasePackage packageModel,
      final ExtraFacility selectExFacility)
   {
      // get the existing car hire extra facility from package model
      final ExtraFacility existingCarHire =
         getExistingExtraFromPackageModel(selectExFacility.getExtraFacilityCategory()
            .getSuperCategoryCode());
      
      // existingCarHire is null when the noNeedCarhire check box is
      // unselected from selected case, in this case package model doen't
      // have any car hire extras.
      if (existingCarHire != null)
      {
         // removing the existing car hire extra from the package model.
         updateExtraFacilityService.removeExtraSuperCategoryFromPackage(existingCarHire
            .getExtraFacilityCategory().getSuperCategoryCode());
      }
      // adding the selected car hire category to package model
      carHireServiceLite.addSelectedCarHireExtraFaciltyToPackageModel(packageModel,
         selectExFacility);
   }
   
   /**
    * @param extraFacilityViewDataContainer
    * @param packageModel
    */
   private void updateTransferOptionContainer(
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer,
      final BasePackage packageModel)
   {
      final String extraFacilityCode = getSelectedTransferExtraCode(packageModel);
      updateTransferViewDataContainer(extraFacilityCode,
         extraFacilityViewDataContainer.getTransferOptions());
      if (CollectionUtils.isNotEmpty(extraFacilityViewDataContainer.getCarHireUpgrade()))
      {
         updateCarHireViewDataContainer(extraFacilityCode,
            extraFacilityViewDataContainer.getCarHireUpgrade());
      }
   }
   
   /**
    * @param packageModel
    */
   private String getSelectedTransferExtraCode(final BasePackage packageModel)
   {
      String extraFacilityCode = StringUtils.EMPTY;
      for (final ExtraFacilityCategory extraFacilityCategory : packageModel
         .getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(extraFacilityCategory.getCode(),
            ExtraFacilityConstants.TRANSFER)
            || StringUtils.equalsIgnoreCase(extraFacilityCategory.getSuperCategoryCode(),
               ExtraFacilityConstants.CAR_HIRE))
         {
            extraFacilityCode =
               extraFacilityCategory.getExtraFacilities().get(0).getExtraFacilityCode();
            break;
         }
      }
      return extraFacilityCode;
   }
   
   /**
    * @param extraFacilityCode
    * @param carHireUpgrade
    */
   private void updateCarHireViewDataContainer(final String extraFacilityCode,
      final List<ExtraFacilityCategoryViewData> carHireUpgrade)
   {
      for (final ExtraFacilityCategoryViewData extraFacilityCategoryViewData : carHireUpgrade)
      {
         extraFacilityCategoryViewData.setSelected(false);
         for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
            .getExtraFacilityViewData())
         {
            if (StringUtils.equalsIgnoreCase(extraFacilityCode, extraFacilityViewData.getCode()))
            {
               extraFacilityViewData.setSelected(true);
               extraFacilityCategoryViewData.setSelected(true);
            }
            else
            {
               extraFacilityViewData.setSelected(false);
            }
         }
      }
      sortExtraFacilityCategoryViewList(carHireUpgrade);
      
   }
   
   /**
    * @param extraFacilityCode
    * @param transferOptions
    */
   private void updateTransferViewDataContainer(final String extraFacilityCode,
      final ExtraFacilityCategoryViewData transferOptions)
   {
      if (CollectionUtils.isNotEmpty(transferOptions.getExtraFacilityViewData()))
      {
         for (final ExtraFacilityViewData extraFacilityViewData : transferOptions
            .getExtraFacilityViewData())
         {
            if (StringUtils.equalsIgnoreCase(extraFacilityCode, extraFacilityViewData.getCode()))
            {
               extraFacilityViewData.setSelected(true);
            }
            else
            {
               extraFacilityViewData.setSelected(false);
            }
         }
      }
   }
   
   /**
    * Sort extra facility category view list.
    *
    * @param extraFacilityCategoryViewList the extra facility category view list
    */
   private void sortExtraFacilityCategoryViewList(
      final List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewList)
   {
      if (extraFacilityCategoryViewList.size() > 1)
      {
         Collections.sort(extraFacilityCategoryViewList,
            new Comparator<ExtraFacilityCategoryViewData>()
            {
               
               @Override
               public int compare(
                  final ExtraFacilityCategoryViewData firstExtraFacilityCategoryViewData,
                  final ExtraFacilityCategoryViewData secondExtraFacilityCategoryViewData)
               {
                  final boolean firstCategorySelected =
                     firstExtraFacilityCategoryViewData.isSelected();
                  final boolean secondCategorySelected =
                     secondExtraFacilityCategoryViewData.isSelected();
                  if (firstCategorySelected == secondCategorySelected)
                  {
                     return 0;
                  }
                  return firstCategorySelected ? -1 : 1;
               }
            });
      }
   }
   
   private void updateSelectionTypeLite(final ExtraFacility selectedExtraFacility,
      final boolean isCarHireAvailable)
   {
      if (isCarHireAvailable)
      {
         selectedExtraFacility.setSelection(FacilitySelectionType.SELECTABLE);
      }
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.PackageExtraFacilityFacade#renderCruiseExtras
    * (uk.co.tui.th.book.view.data. CruiseOptionsViewData)
    */
   @Override
   public void renderCruiseExtras(final CruiseOptionsViewData cruiseOptionsViewData)
   {
      // its an empty method.
   }
   
   /**
    * Gets the currency appended price.
    *
    * @param price the price
    * @param currencyCode the currency symbol
    * @return the currency appended price
    */
   private static String getCurrencyAppendedPrice(final BigDecimal price, final String currencyCode)
   {
      String formattedPrice = StringUtils.EMPTY;
      if (price.compareTo(BigDecimal.ZERO) < 0)
      {
         formattedPrice =
            StringUtil.append("-", CurrencyUtils.getCurrencySymbol(currencyCode),
               String.valueOf(price.negate()));
      }
      else
      {
         formattedPrice =
            StringUtil.append("+", CurrencyUtils.getCurrencySymbol(currencyCode),
               String.valueOf(price));
      }
      return formattedPrice;
   }
}
