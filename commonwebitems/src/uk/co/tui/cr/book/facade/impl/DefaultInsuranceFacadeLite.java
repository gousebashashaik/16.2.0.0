/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityType;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.InsuranceType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.ExtraFacilityQuantityCalculationService;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.book.services.inventory.InsuranceServiceLite;
import uk.co.tui.book.validator.impl.DefaultInsuranceEnablingStrategy;
import uk.co.tui.cr.book.constants.BookFlowConstants;
import uk.co.tui.cr.book.facade.InsuranceFacade;
import uk.co.tui.cr.book.facade.PackageExtraFacilityFacade;
import uk.co.tui.cr.book.formbean.InsuranceDetailsCriteria;
import uk.co.tui.cr.book.populators.InsuranceContainerViewDataPopulatorLite;
import uk.co.tui.cr.book.store.PackageExtraFacilityStore;
import uk.co.tui.cr.book.view.data.AgeProfile;
import uk.co.tui.cr.book.view.data.ExtraOptionsViewData;
import uk.co.tui.cr.book.view.data.InsuranceContainerViewData;
import uk.co.tui.cr.book.view.data.InsuranceCriteria;
import uk.co.tui.cr.book.view.data.InsurancePassengerViewData;
import uk.co.tui.cr.book.view.data.InsuranceViewData;
import uk.co.tui.cr.exception.TUISystemException;

/**
 * The Class DefaultInsuranceFacadeLite.
 *
 * @author madhumathi.m
 */
public class DefaultInsuranceFacadeLite implements InsuranceFacade
{
   
   /** Logger for DefaultInsuranceFacade class *. */
   private static final TUILogUtils LOG = new TUILogUtils("DefaultInsuranceFacadeLite");
   
   /** BasePackage Cart Service. */
   @Resource
   private PackageCartService packageCartService;
   
   /** The insurance service lite. */
   @Resource
   private InsuranceServiceLite insuranceServiceLite;
   
   /** The session service. */
   @Resource
   private SessionService sessionService;
   
   /** The update extra facility service. */
   @Resource
   private UpdateExtraFacilityService updateExtraFacilityService;
   
   /** The extra facility quantity calculation service. */
   @Resource
   private ExtraFacilityQuantityCalculationService extraFacilityQuantityCalculationService;
   
   /** The th insurance container view data populator lite. */
   @Resource(name = "crInsuranceContainerViewDataPopulatorLite")
   private InsuranceContainerViewDataPopulatorLite thInsuranceContainerViewDataPopulatorLite;
   
   /** The package extra facility facade. */
   @Resource(name = "crPackageExtraFacilityFacade")
   private PackageExtraFacilityFacade packageExtraFacilityFacade;
   
   /** The Constant INSURANCE. */
   private static final String INSURANCE = "INS";
   
   /** The Constant CHILD. */
   private static final String CHILD = "Child";
   
   /** The Constant CHILDREN. */
   private static final String CHILDREN = " Children";
   
   /** The Constant ADULT. */
   private static final String ADULT = "Adult";
   
   /** The Constant SENIOR. */
   private static final String SENIOR = "Senior";
   
   /** The Constant SUPERSENIOR. */
   private static final String SUPERSENIOR = "SuperSenior";
   
   /** The Constant MULTI_SYMBOL. */
   private static final String MULTI_SYMBOL = " " + "\u00D7" + " ";
   
   /** The Constant S. */
   private static final String S = "s";
   
   /** The Constant SPACE. */
   private static final String SPACE = " ";
   
   /** The currency resolver. */
   @Resource
   private CurrencyResolver currencyResolver;
   
   @Resource
   private DefaultInsuranceEnablingStrategy insuranceEnablingStrategy;
   
   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.cr.book.facade.InsuranceFacade#renderInsuranceExtras()
    */
   @Override
   public void renderInsuranceExtras(final InsuranceContainerViewData insuranceContainerViewData)
      throws ParseException
   {
      final BasePackage packageModel = getPackageModel();
      if (!insuranceEnablingStrategy.insuranceSwitchedOff())
      {
         final PackageExtraFacilityStore packageExtraFacilityStore = getPackageExtraFacilityStore();
         try
         {
            final List<ExtraFacilityCategory> extraFacilityCategories =
               checkStoreStatus(packageModel, packageExtraFacilityStore);
            if (CollectionUtils.isNotEmpty(extraFacilityCategories))
            {
               thInsuranceContainerViewDataPopulatorLite.populate(extraFacilityCategories,
                  insuranceContainerViewData);
               updateInsuranceContainerViewData(packageModel, insuranceContainerViewData);
            }
         }
         catch (final BookServiceException e)
         {
            LOG.error("TUISystemException : " + e.getMessage());
            throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
         }
      }
   }
   
   /**
    * Check store status.
    *
    * @param packageModel the package model
    * @param packageExtraFacilityStore the package extra facility store
    * @return the list
    * @throws BookServiceException the book service exception
    */
   private List<ExtraFacilityCategory> checkStoreStatus(final BasePackage packageModel,
      final PackageExtraFacilityStore packageExtraFacilityStore) throws BookServiceException
   {
      List<ExtraFacilityCategory> extraFacilityCategoriesInStore = Collections.emptyList();
      List<ExtraFacilityCategory> extraFacilityCategories = Collections.emptyList();
      // 1. check if store is not empty
      if (SyntacticSugar.isNotNull(packageExtraFacilityStore))
      {
         // 2. get the extra present in store using package id
         extraFacilityCategoriesInStore =
            packageExtraFacilityStore.getExtraFacilityLite(packageModel.getId());
         final ExtraFacilityCategory insuranceExtraCategoryFromStore =
            getInsuranceExtraCategoryFromStore(extraFacilityCategoriesInStore);
         // 3. if insurance extras are not availble in store then do
         // an
         // anite search
         extraFacilityCategories =
            extractInsuranceExtras(packageModel, packageExtraFacilityStore,
               insuranceExtraCategoryFromStore);
      }
      return extraFacilityCategories;
   }
   
   /**
    * checks the insurance extras are there in store if no then send the ins req and update the
    * store with response.
    *
    * @param packageModel the package model
    * @param packageExtraFacilityStore the package extra facility store
    * @param insuranceExtraCategoryFromStore the insurance extra category from store
    * @return extraFacilityCategories
    * @throws BookServiceException the book service exception
    */
   private List<ExtraFacilityCategory> extractInsuranceExtras(final BasePackage packageModel,
      final PackageExtraFacilityStore packageExtraFacilityStore,
      final ExtraFacilityCategory insuranceExtraCategoryFromStore) throws BookServiceException
   {
      List<ExtraFacilityCategory> extraFacilityCategories;
      if (!SyntacticSugar.isNotNull(insuranceExtraCategoryFromStore))
      {
         extraFacilityCategories = insuranceServiceLite.getInsuranceExtras(packageModel);
         if (CollectionUtils.isNotEmpty(extraFacilityCategories))
         {
            packageExtraFacilityStore.getExtraFacilityLite(packageModel.getId()).addAll(
               extraFacilityCategories);
         }
      }
      else
      {
         extraFacilityCategories = new ArrayList<ExtraFacilityCategory>();
         extraFacilityCategories.add(insuranceExtraCategoryFromStore);
      }
      return extraFacilityCategories;
   }
   
   /*
    * (non-Javadoc) select the insurance based on the user selection
    * 
    * @see uk.co.tui.cr.book.facade.InsuranceFacade#selectInsurance(uk.co.tui.th
    * .book.formbean.InsuranceDetailsCriteria)
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   @Override
   public void selectInsurance(final InsuranceDetailsCriteria insuranceDetailsCriteria,
      final ExtraOptionsViewData extraOptionsViewData)
   {
      final BasePackage packageModel = getPackageModel();
      final ExtraFacility selectedPackageExtraFacility =
         fetchSelectedPackageExtraFacility(insuranceDetailsCriteria.getInsuranceCode(),
            packageModel.getId());
      final InsuranceContainerViewData insuranceContainerViewData = getInsuranceContainerViewData();
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      for (final InsuranceViewData insuranceData : insuranceContainerViewData.getInsViewData())
      {
         if (StringUtils.equalsIgnoreCase(insuranceData.getCode(),
            insuranceDetailsCriteria.getInsuranceCode()))
         {
            insuranceData.setFrmPrice(calculateTotalPriceInsurance(insuranceDetailsCriteria,
               selectedPackageExtraFacility));
            insuranceData.setCurrencyAppendedFrmPrice(CurrencyUtils
               .getCurrencySymbol(defaultCurrencyCode) + insuranceData.getFrmPrice());
            insuranceData.setPrice(insuranceData.getCurrencyAppendedFrmPrice());
            insuranceData.setPaxComposition(getPaxComposition(insuranceDetailsCriteria
               .getSelected().values()));
            if (checkInsuranceIsPresent(selectedPackageExtraFacility, insuranceData))
            {
               final BigDecimal totalCount =
                  new BigDecimal(insuranceDetailsCriteria.getSelected().values().size());
               insuranceData.getExcessWaiverViewData().setPrice(
                  (totalCount).multiply(
                     ((InsuranceExtraFacility) selectedPackageExtraFacility)
                        .getInsuranceExcessWaiver().getPrice().getRate().getAmount()).toString());
               insuranceData.getExcessWaiverViewData().setCurrencyAppendedPrice(
                  CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
                     + insuranceData.getExcessWaiverViewData().getPrice());
            }
         }
      }
      packageExtraFacilityFacade.populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      setEachPassengerPrice(insuranceDetailsCriteria, selectedPackageExtraFacility,
         insuranceContainerViewData);
      extraOptionsViewData.setInsuranceContainerViewData(insuranceContainerViewData);
   }
   
   /**
    * Sets the each passenger price.
    *
    * @param insuranceDetailsCriteria the insurance details criteria
    * @param selectedPackageExtraFacility the selected package extra facility
    * @param insuranceContainerViewData the insurance container view data
    */
   private void setEachPassengerPrice(final InsuranceDetailsCriteria insuranceDetailsCriteria,
      final ExtraFacility selectedPackageExtraFacility,
      final InsuranceContainerViewData insuranceContainerViewData)
   {
      for (final InsurancePassengerViewData passengerData : insuranceContainerViewData
         .getInsPasViewData())
      {
         for (final Entry<String, String> eachselectedEntry : insuranceDetailsCriteria
            .getSelected().entrySet())
         {
            setPassViewDataPrice(selectedPackageExtraFacility, passengerData, eachselectedEntry);
         }
      }
   }
   
   /**
    * Sets the pass view data price.
    *
    * @param selectedPackageExtraFacility the selected package extra facility
    * @param passengerData the passenger data
    * @param eachselectedEntry the eachselected entry
    */
   private void setPassViewDataPrice(final ExtraFacility selectedPackageExtraFacility,
      final InsurancePassengerViewData passengerData, final Entry<String, String> eachselectedEntry)
   {
      if (eachselectedEntry.getKey().equalsIgnoreCase(passengerData.getId()))
      {
         for (final Price eachPriceModel : selectedPackageExtraFacility.getPrices())
         {
            if (StringUtils.equalsIgnoreCase(eachPriceModel.getPriceProfile().getPersonType()
               .toString(), eachselectedEntry.getValue().trim()))
            {
               passengerData.setPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
                  .getSiteCurrency()) + eachPriceModel.getRate().getAmount());
            }
         }
      }
   }
   
   /**
    * Calculate total price insurance.
    *
    * @param formBean the form bean
    * @param extraFacility the extra facility
    * @return the big decimal
    */
   public String calculateTotalPriceInsurance(final InsuranceDetailsCriteria formBean,
      final ExtraFacility extraFacility)
   {
      final List<Price> extraPrices = extraFacility.getPrices();
      BigDecimal total = BigDecimal.ZERO;
      
      for (final String personType : formBean.getSelected().values())
      {
         for (final Price eachPriceModel : extraPrices)
         {
            if (StringUtils.equalsIgnoreCase(eachPriceModel.getPriceProfile().getPersonType()
               .toString(), personType.trim()))
            {
               total = total.add(eachPriceModel.getRate().getAmount());
               break;
            }
         }
      }
      return total.toString();
   }
   
   /**
    * Calculate total price.
    *
    * @param extraFacility the extra facility
    * @return the string
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private BigDecimal calculateTotalPriceInsuranceWithoutExcessWaiver(
      final ExtraFacility extraFacility)
   {
      final List<Price> extraPrices = extraFacility.getPrices();
      BigDecimal total = BigDecimal.ZERO;
      for (final Price eachPriceModel : extraPrices)
      {
         total =
            total.add(eachPriceModel.getRate().getAmount()
               .multiply(new BigDecimal(eachPriceModel.getQuantity().intValue())));
         
      }
      
      return total;
   }
   
   /**
    * Check insurance is present.
    *
    * @param selectedPackageExtraFacility the selected package extra facility
    * @param insuranceData the insurance data
    * @return boolean value
    */
   private boolean checkInsuranceIsPresent(final ExtraFacility selectedPackageExtraFacility,
      final InsuranceViewData insuranceData)
   {
      return ((InsuranceExtraFacility) selectedPackageExtraFacility).getInsuranceExcessWaiver() != null
         && insuranceData.getExcessWaiverViewData() != null;
   }
   
   /**
    * Apply excess waiver insurance price.
    *
    * @param insuranceExtra the insurance extra
    * @return the big decimal
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private BigDecimal applyExcessWaiverInsurancePrice(final InsuranceExtraFacility insuranceExtra)
   {
      if (isInsurancePresent(insuranceExtra))
      {
         if (isFamilyInsurance(insuranceExtra))
         {
            return insuranceExtra.getInsuranceExcessWaiver().getPrice().getRate().getAmount();
         }
         else if (insuranceExtra.getInsuranceExcessWaiver().getPrice() != null)
         {
            final BigDecimal count =
               new BigDecimal(PassengerUtils.getChargeablePaxCount(insuranceExtra.getPassengers()));
            return insuranceExtra.getInsuranceExcessWaiver().getPrice().getRate().getAmount()
               .multiply(count);
         }
      }
      return BigDecimal.ZERO;
   }
   
   /**
    * Checks if is insurance present extrafacility model.
    *
    * @param insuranceExtra the insurance extra
    * @return true if insurance is present.
    */
   private boolean isInsurancePresent(final InsuranceExtraFacility insuranceExtra)
   {
      return insuranceExtra.getInsuranceExcessWaiver() != null
         && BooleanUtils.isTrue(Boolean.valueOf(insuranceExtra.getInsuranceExcessWaiver()
            .isSelected()));
   }
   
   /**
    * Checks if is family insurance.
    *
    * @param insuranceExtra the insurance extra
    * @return true if its family insurance
    */
   private boolean isFamilyInsurance(final InsuranceExtraFacility insuranceExtra)
   {
      return insuranceExtra.getInsuranceExcessWaiver().getPrice() != null
         && insuranceExtra.getInsuranceType() == InsuranceType.FAMILY;
   }
   
   /**
    * Gets the pax composition.
    *
    * @param types the types
    * @return the pax composition
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private String getPaxComposition(final Collection<String> types)
   {
      final StringBuilder paxComposition = new StringBuilder();
      getPax(getCount(types, SENIOR) + getCount(types, SUPERSENIOR), SENIOR, paxComposition);
      getPax(getCount(types, ADULT), ADULT, paxComposition);
      getPax(getCount(types, CHILD), CHILD, paxComposition);
      return paxComposition.toString();
   }
   
   /**
    * Gets the pax count.
    *
    * @param types the types
    * @param type the type
    * @return the count
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private int getCount(final Collection<String> types, final String type)
   {
      int paxCount = 0;
      for (final String passengerType : types)
      {
         if (StringUtils.equalsIgnoreCase(passengerType.trim(), type))
         {
            paxCount++;
         }
      }
      return paxCount;
   }
   
   /**
    * Gets the pax.
    *
    * @param count the count
    * @param type the type
    * @param paxComposition the pax composition
    */
   private void getPax(final int count, final String type, final StringBuilder paxComposition)
   {
      if (count != 0)
      {
         paxComposition.append(MULTI_SYMBOL);
         constructPaxType(count, type, paxComposition);
      }
   }
   
   /**
    * @param count
    * @param type
    * @param paxComposition
    */
   private void constructPaxType(final int count, final String type,
      final StringBuilder paxComposition)
   {
      if (count > 1)
      {
         if (type.equalsIgnoreCase(CHILD))
         {
            paxComposition.append(count).append(CHILDREN);
         }
         else
         {
            paxComposition.append(count).append(SPACE).append(type).append(S);
         }
      }
      else
      {
         paxComposition.append(count).append(SPACE).append(type);
      }
   }
   
   /*
    * (non-Javadoc) adding the insurance based on the user selection
    * 
    * @see uk.co.tui.cr.book.facade.InsuranceFacade#addInsurance(uk.co.tui.cr.book
    * .formbean.InsuranceDetailsFormBean, uk.co.tui.cr.book.view.data.InsuranceContainerViewData)
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   @Override
   public void addInsurance(final InsuranceDetailsCriteria insuranceDetailsCriteria,
      final ExtraOptionsViewData extraOptionsViewData)
   {
      final BasePackage packageModel = getPackageModel();
      
      final ExtraFacility selectedPackageExtraFacility =
         fetchSelectedPackageExtraFacility(insuranceDetailsCriteria.getInsuranceCode(),
            packageModel.getId());
      
      final List<ExtraFacility> selectedExtras = new ArrayList<ExtraFacility>();
      
      selectedExtras.add(selectedPackageExtraFacility);
      
      // check if the insurance is a family insuranc based on the insurance
      // criteria.
      if (MapUtils.isEmpty(insuranceDetailsCriteria.getSelected()))
      {
         selectedExtras.get(0).setType(ExtraFacilityType.PACKAGE);
         updateExtraFacilityService.updateExtraFacilityLite(PassengerUtils.getApplicablePassengers(
            packageModel.getPassengers(), EnumSet.of(PersonType.ADULT, PersonType.SENIOR,
               PersonType.SUPERSENIOR, PersonType.CHILD)), Arrays
            .asList(selectedPackageExtraFacility), packageModel);
      }
      // if not it is assumed to be individual insurance
      else
      {
         // set as individual insurance.
         selectedExtras.get(0).setType(ExtraFacilityType.PAX);
         
         final List<String> selectedPassengersId = new ArrayList<String>();
         for (final Map.Entry<String, String> data : insuranceDetailsCriteria.getSelected()
            .entrySet())
         {
            updateExtraFacilityService.updatePassengerPersonType(data.getKey(), data.getValue(),
               packageModel.getPassengers());
            selectedPassengersId.add(data.getKey());
         }
         final Map<List<ExtraFacility>, List<String>> exFacSelectionComposition =
            new HashMap<List<ExtraFacility>, List<String>>();
         exFacSelectionComposition.put(selectedExtras, selectedPassengersId);
         updateExtraFacilityService.updateExtraLite(packageModel, exFacSelectionComposition);
      }
      updateExtraFacilityService.markExcessWaiverAsSelected(
         selectedPackageExtraFacility.getExtraFacilityCode(),
         insuranceDetailsCriteria.isExcessWaiver(), packageModel);
      updateExtraFacilityPriceModels(packageModel, insuranceDetailsCriteria.getSelected().values());
      final InsuranceContainerViewData insuranceContainerViewData = getInsuranceContainerViewData();
      updateInsuranceContainerViewData(packageModel, insuranceContainerViewData);
      packageExtraFacilityFacade.populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      extraOptionsViewData.setInsuranceContainerViewData(insuranceContainerViewData);
      resetPassengerDOB(packageModel);
   }
   
   private void resetPassengerDOB(final BasePackage packageModel)
   {
      final List<Passenger> passengers = packageModel.getPassengers();
      if (CollectionUtils.isNotEmpty(passengers))
      {
         for (final Passenger passenger : passengers)
         {
            passengerDoesNotHaveDOB(passenger);
         }
      }
      
   }
   
   /**
    * Does not have dob.
    *
    * @param eachPassenger the each passenger
    * @return true, if successful
    */
   private void passengerDoesNotHaveDOB(final Passenger eachPassenger)
   {
      if (adultPassengers(eachPassenger) && !StringUtils.isEmpty(eachPassenger.getDateOfBirth()))
      {
         eachPassenger.setDateOfBirth(null);
      }
   }
   
   /**
    * Adult passengers.
    *
    * @param eachPassenger the each passenger
    * @return true, if successful
    */
   private boolean adultPassengers(final Passenger eachPassenger)
   {
      return EnumSet.of(PersonType.ADULT, PersonType.TEEN, PersonType.SENIOR,
         PersonType.SUPERSENIOR).contains(eachPassenger.getType());
   }
   
   /*
    * (non-Javadoc) removing the insurance based on the user selection
    * 
    * @see uk.co.tui.cr.book.facade.InsuranceFacade#removeInsurance(java.lang.String ,
    * uk.co.tui.cr.book.view.data.InsuranceContainerViewData)
    */
   @Override
   public boolean removeInsurance(final String insuranceCode,
      final ExtraOptionsViewData extraOptionsViewData)
   {
      final BasePackage packageModel = getPackageModel();
      removeInsuranceByCode(INSURANCE, packageModel);
      final InsuranceContainerViewData insuranceContainerViewData = getInsuranceContainerViewData();
      updateInsuranceContainerViewData(insuranceCode, insuranceContainerViewData);
      packageExtraFacilityFacade.populateExtraOptionsAllView(packageModel, extraOptionsViewData);
      extraOptionsViewData.setInsuranceContainerViewData(insuranceContainerViewData);
      return true;
   }
   
   /**
    * Removes the insurance by code.
    *
    * @param insuranceCode the insurance code
    * @param packageModel the package model
    */
   private void removeInsuranceByCode(final String insuranceCode, final BasePackage packageModel)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(INSURANCE, categoryModel.getCode()))
         {
            updateExtraFacilityService.removeExtraCategoryFromPackage(insuranceCode);
         }
      }
   }
   
   /**
    * Insurance available in store.
    *
    * @param categoryList the category list
    * @return true, if successful
    */
   private ExtraFacilityCategory getInsuranceExtraCategoryFromStore(
      final List<ExtraFacilityCategory> categoryList)
   {
      for (final ExtraFacilityCategory categoryModel : categoryList)
      {
         if (StringUtils.equalsIgnoreCase(INSURANCE, categoryModel.getCode()))
         {
            return categoryModel;
         }
      }
      return null;
   }
   
   /**
    * Update insurance container view data.
    *
    * @param packageModel the package model
    * @param insuranceContainerViewData the insurance container view data
    */
   private void updateInsuranceContainerViewData(final BasePackage packageModel,
      final InsuranceContainerViewData insuranceContainerViewData)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(categoryModel.getCode(), INSURANCE))
         {
            updateInsuranceViewData(packageModel, insuranceContainerViewData, categoryModel);
         }
      }
   }
   
   /**
    * Update insurance view data.
    *
    * @param packageModel the package model
    * @param insuranceContainerViewData the insurance container view data
    * @param categoryModel the category model
    */
   private void updateInsuranceViewData(final BasePackage packageModel,
      final InsuranceContainerViewData insuranceContainerViewData,
      final ExtraFacilityCategory categoryModel)
   {
      for (final ExtraFacility extraFacility : categoryModel.getExtraFacilities())
      {
         updateInsuranceViewData(extraFacility, insuranceContainerViewData.getInsViewData());
         updateInsPassViewData(insuranceContainerViewData.getInsPasViewData());
         updateInsuranceCriteria(extraFacility.getPassengers(), insuranceContainerViewData);
         insuranceContainerViewData
            .setCoveredAllPassenger(extraFacility.getPassengers().size() < calculateQuantity(packageModel
               .getPassengers()) ? false : true);
      }
   }
   
   /**
    * Update insurance container view data.
    *
    * @param insuranceCode the insurance code
    * @param insuranceContainerViewData the insurance container view data
    */
   private void updateInsuranceContainerViewData(final String insuranceCode,
      final InsuranceContainerViewData insuranceContainerViewData)
   {
      updateInsuranceViewData(insuranceCode, insuranceContainerViewData.getInsViewData());
      updateInsPassViewData(insuranceContainerViewData.getInsPasViewData());
      insuranceContainerViewData.setInsCriteria(null);
   }
   
   /**
    * update the ins view data.
    *
    * @param extraFacility the extra facility
    * @param insViewData the ins view data
    */
   private void updateInsuranceViewData(final ExtraFacility extraFacility,
      final List<InsuranceViewData> insViewData)
   {
      for (final InsuranceViewData insuranceViewData : insViewData)
      {
         if (StringUtils.equalsIgnoreCase(insuranceViewData.getCode(),
            extraFacility.getExtraFacilityCode()))
         {
            insuranceViewData.setSelected(true);
            insuranceViewData.setInsRemoved(false);
            BigDecimal totalPrice = calculateTotalPriceInsuranceWithoutExcessWaiver(extraFacility);
            setInsurancePaxCompositionQuantity(extraFacility, insuranceViewData);
            if (((InsuranceExtraFacility) extraFacility).getInsuranceExcessWaiver() != null)
            {
               insuranceViewData.getExcessWaiverViewData().setSelected(
                  ((InsuranceExtraFacility) extraFacility).getInsuranceExcessWaiver().isSelected());
               totalPrice =
                  totalPrice
                     .add(applyExcessWaiverInsurancePrice((InsuranceExtraFacility) extraFacility));
            }
            insuranceViewData.setTotalPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
               .getSiteCurrency()) + totalPrice.toString());
         }
         else
         {
            insuranceViewData.setSelected(false);
            insuranceViewData.setInsRemoved(false);
            setInsuranceSelectionFlag(insuranceViewData);
         }
      }
   }
   
   /**
    * Sets the insurance pax composition quantity.
    *
    * @param extraFacility the extra facility
    * @param insuranceViewData the insurance view data
    */
   private void setInsurancePaxCompositionQuantity(final ExtraFacility extraFacility,
      final InsuranceViewData insuranceViewData)
   {
      if (extraFacility.getPassengers() != null
         && StringUtils.equalsIgnoreCase(extraFacility.getType().toString(),
            ExtraFacilityType.PAX.toString()))
      {
         insuranceViewData.setQuantity(calculateQuantity(extraFacility.getPassengers()));
         insuranceViewData.setPaxComposition(getPaxComposition(getTypes(extraFacility
            .getPassengers())));
         insuranceViewData.setDescriptionSummary(insuranceViewData.getDescriptionSummary()
            + insuranceViewData.getPaxComposition());
      }
   }
   
   /**
    * Sets the insurance selection flag.
    *
    * @param insuranceViewData the new insurance selection flag
    */
   private void setInsuranceSelectionFlag(final InsuranceViewData insuranceViewData)
   {
      if (SyntacticSugar.isNotNull(insuranceViewData.getExcessWaiverViewData()))
      {
         insuranceViewData.getExcessWaiverViewData().setSelected(false);
      }
   }
   
   /**
    * Gets the pax types.
    *
    * @param passengers the passengers
    * @return types
    */
   private Collection<String> getTypes(final Collection<Passenger> passengers)
   {
      final List<String> types = new ArrayList<String>();
      for (final Passenger passenger : passengers)
      {
         types.add(passenger.getType().toString());
      }
      return types;
   }
   
   /**
    * Calculate quantity.
    *
    * @param passengers the passengers
    * @return ChargeablePaxCount
    */
   private int calculateQuantity(final Collection<Passenger> passengers)
   {
      
      return PassengerUtils.getChargeablePaxCount(passengers);
   }
   
   /**
    * Update insurance criteria.
    *
    * @param passengers the passengers
    * @param insuranceContainerViewData the insurance container view data
    */
   private void updateInsuranceCriteria(final Collection<Passenger> passengers,
      final InsuranceContainerViewData insuranceContainerViewData)
   {
      final List<InsuranceCriteria> criteriaList = new ArrayList<InsuranceCriteria>();
      for (final Passenger passengerModel : passengers)
      {
         final InsuranceCriteria insuranceCriteria = new InsuranceCriteria();
         insuranceCriteria.setPassId(passengerModel.getId().toString());
         insuranceCriteria.setAgeCode(StringUtils.upperCase(passengerModel.getType().toString()));
         criteriaList.add(insuranceCriteria);
      }
      if (CollectionUtils.isNotEmpty(criteriaList))
      {
         insuranceContainerViewData.setInsCriteria(criteriaList);
      }
   }
   
   /**
    * Update insurance view data.
    *
    * @param insuranceCode the insurance code
    * @param insViewDataList the ins view data list
    */
   private void updateInsuranceViewData(final String insuranceCode,
      final List<InsuranceViewData> insViewDataList)
   {
      for (final InsuranceViewData insuranceViewData : insViewDataList)
      {
         if (StringUtils.equalsIgnoreCase(insuranceCode, insuranceViewData.getCode()))
         {
            insuranceViewData.setInsRemoved(true);
         }
         insuranceViewData.setSelected(false);
         insuranceViewData.setQuantity(0);
         setInsuranceSelectionFlag(insuranceViewData);
      }
   }
   
   /**
    * Update ins pass view data.
    *
    * @param insPasViewData the ins pas view data
    */
   private void updateInsPassViewData(final List<InsurancePassengerViewData> insPasViewData)
   {
      for (final InsurancePassengerViewData insPassengerViewData : insPasViewData)
      {
         final List<AgeProfile> ageProfileInfo = insPassengerViewData.getAgeProfile();
         for (final AgeProfile ageProfile : ageProfileInfo)
         {
            if ("ADULT".equals(ageProfile.getAgeCode()))
            {
               ageProfile.setSelected("selected");
            }
            else
            {
               ageProfile.setSelected(StringUtils.EMPTY);
            }
            
         }
         insPassengerViewData.setSelected(true);
         
      }
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
    * Gets the package extra facility store.
    *
    * @return the package extra facility store
    */
   private PackageExtraFacilityStore getPackageExtraFacilityStore()
   {
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService.getAttribute("PackageExtraFacilityStore");
      if (packageExtraFacilityStore != null)
      {
         return packageExtraFacilityStore;
      }
      return new PackageExtraFacilityStore();
   }
   
   /**
    * Fetch selected package extra facility.
    *
    * @param extraCode the extra code
    * @param packageId the package id
    * @return the extra facility model
    */
   private ExtraFacility fetchSelectedPackageExtraFacility(final String extraCode,
      final String packageId)
   {
      LOG.info("Fetch ExtraFacility From BasePackage Store for Extra Facilty Code " + extraCode
         + "PackageCode " + packageId);
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService.getAttribute("PackageExtraFacilityStore");
      final List<ExtraFacilityCategory> extraFacilityCategoryList =
         packageExtraFacilityStore.getExtraFacilityLite(packageId);
      ExtraFacility insuranceExtra;
      for (final ExtraFacilityCategory exFacilityCategory : extraFacilityCategoryList)
      {
         insuranceExtra = extractInsuranceExtra(extraCode, exFacilityCategory);
         if (insuranceExtra != null)
         {
            return insuranceExtra;
         }
      }
      return null;
   }
   
   /**
    * Extract insurance extra.
    *
    * @param extraCode the extra code
    * @param exFacilityCategory the ex facility category
    * @return the extra facility model
    */
   private ExtraFacility extractInsuranceExtra(final String extraCode,
      final ExtraFacilityCategory exFacilityCategory)
   {
      if (StringUtils.equalsIgnoreCase(exFacilityCategory.getCode(), INSURANCE))
      {
         for (final ExtraFacility extra : exFacilityCategory.getExtraFacilities())
         {
            if (extraCode.equalsIgnoreCase(extra.getExtraFacilityCode()))
            {
               LOG.info("ExtraFacility from package Store :-" + extraCode);
               return extra;
            }
         }
      }
      return null;
   }
   
   /**
    * Update extra facility price models.
    *
    * @param packageModel the package model
    * @param personTypes the person types
    */
   private void updateExtraFacilityPriceModels(final BasePackage packageModel,
      final Collection<String> personTypes)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(INSURANCE, categoryModel.getCode()))
         {
            for (final ExtraFacility extra : categoryModel.getExtraFacilities())
            {
               extraFacilityQuantityCalculationService.updateQuantityForInsurance(
                  (InsuranceExtraFacility) extra, personTypes);
               extra.setPrices((List<Price>) org.apache.commons.collections.CollectionUtils.select(
                  extra.getPrices(), new Predicate()
                  {
                     @Override
                     public boolean evaluate(final Object priceModel)
                     {
                        if (((Price) priceModel).getQuantity() != null)
                        {
                           return ((Price) priceModel).getQuantity().intValue() > 0;
                        }
                        return false;
                     }
                  }));
            }
         }
      }
   }
   
   /**
    * To return the InsuranceContainerViewData from session.
    *
    * @return InsuranceContainerViewData
    */
   private InsuranceContainerViewData getInsuranceContainerViewData()
   {
      return (InsuranceContainerViewData) sessionService.getAttribute("insuranceContainerViewData");
   }
}
