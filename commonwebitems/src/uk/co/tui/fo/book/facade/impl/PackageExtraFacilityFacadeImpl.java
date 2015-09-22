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
package uk.co.tui.fo.book.facade.impl;

import static uk.co.tui.th.book.constants.BookFlowConstants.EXTRAOPTIONS;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.PackageExtraFacilityResponse;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.ExtraFacilityQuantityCalculationService;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.SaveHolidayService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.UpdateDonationExtraFacilityService;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.book.services.inventory.PackageExtrasService;
import uk.co.tui.book.utils.ExtraFacilityUtils;
import uk.co.tui.exception.TUISystemException;
import uk.co.tui.fo.book.ExtraFacilityUpdator;
import uk.co.tui.fo.book.constants.BookFlowConstants;
import uk.co.tui.fo.book.constants.SessionObjectKeys;
import uk.co.tui.fo.book.facade.PackageExtraFacilityFacade;
import uk.co.tui.fo.book.populators.PackageExtrasContainerPopulator;
import uk.co.tui.fo.book.populators.SummaryPanelUrlPopulator;
import uk.co.tui.fo.book.store.PackageExtraFacilityStore;
import uk.co.tui.fo.book.view.data.ContentViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityViewDataContainer;
import uk.co.tui.fo.book.view.data.ExtraOptionsContentViewData;
import uk.co.tui.fo.book.view.data.ExtraOptionsStaticContentViewData;
import uk.co.tui.fo.book.view.data.ExtraOptionsViewData;
import uk.co.tui.fo.book.view.data.InsuranceContainerViewData;
import uk.co.tui.fo.util.ExtraFacilityStoreUtils;

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

   @Resource(name = "foPackageExtrasContainerPopulator")
   private PackageExtrasContainerPopulator packageExtrasContainerPopulator;

   /** The summary panel url populator. */
   @Resource(name = "foSummaryPanelUrlPopulator")
   private SummaryPanelUrlPopulator summaryPanelUrlPopulator;

   /** The extra facility updator. */
   @Resource(name = "foExtraFacilityUpdator")
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

   /** The update donation ex facility service locator. */
   @Resource(name = "updateDonationExtraLocator")
   private ServiceLocator<UpdateDonationExtraFacilityService> serviceLocator;

   /** The extra static content view data populator. */
   @Resource(name = "foExtraStaticContentViewDataPopulator")
   private Populator<Object, ExtraOptionsStaticContentViewData> extraStaticContentViewDataPopulator;

   /** The extra options content view data populator. */
   @Resource(name = "foExtraOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> extraOptionsContentViewDataPopulator;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The save holiday service. */
   @Resource
   private SaveHolidayService saveHolidayService;

   @Resource(name = "foExtraFacilityStroreUtil")
   private ExtraFacilityStoreUtils extraFacilityStroreUtil;

   private static final String BOXING = "boxing";

   /** Package ViewData Populator Service Locator */
   @Resource
   private ServiceLocator<Populator> packageViewDataPopulatorServiceLocator;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private CurrencyResolver currencyResolver;

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
      populateExtraOptionsAllView(packageModel, extraOptionsViewData);
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
      final String transferCode, final boolean isExtraSelected)
   {
      final BasePackage packageModel = getPackageModel();
      // Reset the already selected transfer option
      removeDefaultTransferCategories();
      ExtraFacility selectedExtraFacility;
      final ExtraOptionsViewData extraOptionsViewData = new ExtraOptionsViewData();
      updateStoreTransferExtrasLite(transferCode, false);
      if (isExtraSelected)
      {
         selectedExtraFacility =
            fetchSelectedPackageExtraFacilityLite(transferCode, getPackageModel().getId());

         final List<ExtraFacility> extraFacilityModelList = new ArrayList<ExtraFacility>();
         extraFacilityModelList.add(selectedExtraFacility);
         updateExtraFacilityService.updateExtraFacilityLite(
            PassengerUtils.getApplicablePassengers(packageModel.getPassengers(),
               EnumSet.of(PersonType.SENIOR, PersonType.ADULT, PersonType.TEEN, PersonType.CHILD)),
            Arrays.asList(selectedExtraFacility), packageModel);
         extraFacilityQuantityCalculationService.updateTransferExtraFacilityQuantity(
            packageModel.getExtraFacilityCategories(), packageModel.getPassengers(),
            selectedExtraFacility.getExtraFacilityCode());
      }
      saveHolidayService.resetHolidaySavedIndicator();
      // Populates the ViewData.
      populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      return extraOptionsViewData;
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
      final List<ExtraFacilityViewData> transfeOptions =
         extraOptionsViewData.getExtraFacilityViewDataContainer().getTransferOptions()
            .getExtraFacilityViewData();
      updateTransferOptionContainer(extraOptionsViewData.getExtraFacilityViewDataContainer(),
         packageModel);
      updateRelativePriceDifference(transfeOptions);
      // To handle the scenario when default transfer is not
      // available.
      handleNoTransferOption(extraOptionsViewData);
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

      getCategoryModelFromStore(packageExtraFacilityStore, containerViewData);

      if (!containerViewData.getTransferOptions().getExtraFacilityViewData().isEmpty())
      {
         // To handle the scenario when default transfer is not available.
         handleNoTransferOption(extraOptionsViewData);
      }
      if (extraOptionsViewData.getInsuranceContainerViewData() == null)
      {
         extraOptionsViewData
            .setInsuranceContainerViewData((InsuranceContainerViewData) sessionService
               .getAttribute("insuranceContainerViewData"));
      }
   }

   /**
    * @param packageExtraFacilityStore
    * @param containerViewData
    */
   private void getCategoryModelFromStore(
      final PackageExtraFacilityStore packageExtraFacilityStore,
      final ExtraFacilityViewDataContainer containerViewData)
   {
      for (final ExtraFacilityCategory extraFacilityCategoryModel : packageExtraFacilityStore
         .getExtraFacilityLite(getPackageModel().getId()))
      {
         packageExtrasContainerPopulator.populate(extraFacilityCategoryModel.getExtraFacilities(),
            containerViewData);
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
    * Update package into cart.
    */
   private void updatePackageIntoCart()
   {
      packageCartService.updateCart(getPackageModel());
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
            ExtraFacilityConstants.TRANSFER))
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
