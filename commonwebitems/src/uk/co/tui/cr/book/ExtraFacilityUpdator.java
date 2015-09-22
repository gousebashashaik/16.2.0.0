/**
 *
 */
package uk.co.tui.cr.book;

import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.ExtraFacilityType;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.LegExtraFacility;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.ExtraFacilityQuantityCalculationService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.ExtraFacilityUtils;
import uk.co.tui.cr.book.constants.BookFlowConstants;
import uk.co.tui.cr.book.populators.ExtraFacilityViewDataPopulator;
import uk.co.tui.cr.book.populators.InsuranceViewDataPopulator;
import uk.co.tui.cr.book.view.data.BaggageExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewDataContainer;
import uk.co.tui.cr.book.view.data.ExtrasToPassengerRelationViewData;
import uk.co.tui.cr.book.view.data.FlightOptionsViewData;
import uk.co.tui.cr.book.view.data.InsuranceViewData;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.PassengerViewData;
import uk.co.tui.cr.book.view.data.PaxCompositionViewData;

/**
 * @author akshay.an
 *
 */
public class ExtraFacilityUpdator
{

   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;

   @Resource
   private CurrencyResolver currencyResolver;

   @Resource
   private ExtraFacilityQuantityCalculationService extraFacilityQuantityCalculationService;

   @Resource(name = "crExtraFacilityViewDataPopulator")
   private ExtraFacilityViewDataPopulator extraFacilityViewDataPopulator;

   @Resource(name = "crInsuranceViewDataPopulator")
   private InsuranceViewDataPopulator insuranceViewDataPopulator;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   private static final String BOXING = "boxing";

   private static final TUILogUtils LOGGER = new TUILogUtils("ExtraFacilityUpdator");

   private static final int TWO = 2;

   /**
    * The method updates the PackageViewData with the selected extras.
    *
    * @param packageViewData
    * @param packageModel
    */
   public void updatePackageViewData(final BasePackage packageModel,
      final PackageViewData packageViewData)
   {
      final List<ExtraFacilityCategoryViewData> listOfCategoryViewData =
         new ArrayList<ExtraFacilityCategoryViewData>();
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         final ExtraFacilityCategoryViewData categoryViewData = new ExtraFacilityCategoryViewData();
         categoryViewData.setExtraFacilityCategoryCode(categoryModel.getCode());
         categoryViewData.setExtraFacilityGroupCode(categoryModel.getExtraFacilities().get(0)
            .getExtraFacilityGroup().toString());
         categoryViewData.setSuperCategoryCode(categoryModel.getSuperCategoryCode());
         categoryViewData.setAliasSuperCategoryCode(categoryModel.getAliasSuperCategoryCode());
         categoryViewData.setDescription(categoryModel.getDescription());
         populateExtrasViewDataFromPackageModel(categoryModel, categoryViewData);
         if (StringUtils.equalsIgnoreCase(categoryModel.getSuperCategoryCode(),
            ExtraFacilityConstants.EXCURSION)
            || StringUtils.equalsIgnoreCase(categoryModel.getSuperCategoryCode(),
               ExtraFacilityConstants.ATTRACTION))
         {
            updateTotalPriceAndTotalQuantityForExcursions(categoryModel.getExtraFacilities(),
               categoryViewData);
         }
         listOfCategoryViewData.add(categoryViewData);
      }
      packageViewData.getExtraFacilityCategoryViewData().clear();
      packageViewData.getExtraFacilityCategoryViewData().addAll(listOfCategoryViewData);
      populateDefaultTransferOption(packageModel, packageViewData);
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
    * The method fetches the package model from the cart.
    *
    * @return BasePackage
    */
   private BasePackage getBasePackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * The method Populates the ExtraFacilityViewData from ExtraFacility.
    *
    * @param categoryModel
    * @param categoryViewData
    */
   private void populateExtrasViewDataFromPackageModel(final ExtraFacilityCategory categoryModel,
      final ExtraFacilityCategoryViewData categoryViewData)
   {

      for (final ExtraFacility extraModel : categoryModel.getExtraFacilities())
      {
         if (StringUtil.equalsIgnoreCase(categoryModel.getCode(), "BAG"))
         {
            retrieveBagViewDataFromBagExtraModel(categoryViewData, extraModel);

         }
         else
         {
            retrieveOtherExtraViewDataFromExtrasModel(categoryViewData, extraModel);
         }
      }
      // Exceptional handling for World care fund at category level instead of
      // extra level
      if (StringUtils.equalsIgnoreCase(categoryModel.getCode(),
         ExtraFacilityConstants.DONATION_CATEGORY))
      {
         updateCharity(categoryModel, categoryViewData);
      }

   }

   /**
    * @param categoryModel
    * @param categoryViewData
    */
   private void updateCharity(final ExtraFacilityCategory categoryModel,
      final ExtraFacilityCategoryViewData categoryViewData)
   {
      if (InventoryType.ATCOM == packageCartService.getBasePackage().getInventory()
         .getInventoryType())
      {
         updateWorldCare(categoryModel, categoryViewData);
      }
      else
      {
         updateWorldCareFund(categoryModel, categoryViewData);
      }
   }

   /**
    * @param categoryModel
    * @param categoryViewData
    */
   private void updateWorldCare(final ExtraFacilityCategory categoryModel,
      final ExtraFacilityCategoryViewData categoryViewData)
   {
      for (final ExtraFacilityViewData extraView : categoryViewData.getExtraFacilityViewData())
      {
         BigDecimal amount = BigDecimal.ZERO;
         for (final ExtraFacility donationExtra : categoryModel.getExtraFacilities())
         {
            for (final Price price : donationExtra.getPrices())
            {
               amount = amount.add(price.getAmount().getAmount());
            }
         }
         extraView.setPrice(amount);
         extraView.setCurrencyAppendedPrice(CurrencyUtils
            .getCurrencyAppendedPriceWithDecimalPart(amount));
      }

   }

   /**
    * @param categoryModel
    * @param categoryViewData
    */
   private void updateWorldCareFund(final ExtraFacilityCategory categoryModel,
      final ExtraFacilityCategoryViewData categoryViewData)
   {
      for (final ExtraFacilityViewData extraView : categoryViewData.getExtraFacilityViewData())
      {
         BigDecimal amount = BigDecimal.ZERO;
         for (final ExtraFacility donationExtra : categoryModel.getExtraFacilities())
         {
            amount = amount.add(donationExtra.getPrices().get(0).getAmount().getAmount());
         }
         extraView.setPrice(amount);
         extraView.setCurrencyAppendedPrice(CurrencyUtils
            .getCurrencyAppendedPriceWithDecimalPart(amount));
      }

   }

   /**
    * The method populates the BaggageExtraFacilityViewData from BaggageExtraModel.
    *
    * @param categoryViewData
    * @param extraModel
    */
   private void retrieveBagViewDataFromBagExtraModel(
      final ExtraFacilityCategoryViewData categoryViewData, final ExtraFacility extraModel)
   {
      boolean isExtraPresent = false;
      final BaggageExtraFacility bagModel = (BaggageExtraFacility) extraModel;
      for (final ExtraFacilityViewData extraFacilityViewData : categoryViewData
         .getExtraFacilityViewData())
      {
         if (bagModel.getExtraFacilityCode().equals(extraFacilityViewData.getCode()))
         {
            extraFacilityViewData.setPrice(extraFacilityViewData.getPrice().add(
               bagModel.getPrice().getRate().getAmount()));
            extraFacilityViewData.setCurrencyAppendedPrice(CurrencyUtils
               .getCurrencyAppendedPrice(extraFacilityViewData.getPrice().multiply(
                  BigDecimal.valueOf(extraFacilityViewData.getPassengerExtraFacilityMapping()
                     .getPassengers().size()))));
            isExtraPresent = true;

         }
      }
      if (!isExtraPresent)
      {
         final BaggageExtraFacilityViewData bagView = new BaggageExtraFacilityViewData();
         extraFacilityViewDataPopulator.populate(bagModel, bagView);
         assignPassengerForExtra(bagView, bagModel);
         bagView.setCurrencyAppendedPrice(CurrencyUtils.getCurrencyAppendedPrice(bagView.getPrice()
            .multiply(
               BigDecimal
                  .valueOf(bagView.getPassengerExtraFacilityMapping().getPassengers().size()))));
         categoryViewData.getExtraFacilityViewData().add(bagView);
      }
   }

   /**
    * The method populates the ExtraFacilityViewData from ExtraModel.
    *
    * @param categoryViewData
    * @param extraModel
    */
   private void retrieveOtherExtraViewDataFromExtrasModel(
      final ExtraFacilityCategoryViewData categoryViewData, final ExtraFacility extraModel)
   {
      boolean isExtraPresent = false;
      for (final ExtraFacilityViewData extraFacilityViewData : categoryViewData
         .getExtraFacilityViewData())
      {
         if (extraModel.getExtraFacilityCode().equals(extraFacilityViewData.getCode()))
         {
            extraFacilityViewData.setAdultPrice(extraFacilityViewData.getAdultPrice().add(
               extraModel.getPrices().get(0).getRate().getAmount()));
            if (extraModel.getPrices().size() > 1)
            {
               extraFacilityViewData.setChildPrice(extraFacilityViewData.getChildPrice().add(
                  extraModel.getPrices().get(1).getRate().getAmount()));
            }
            else
            {
               extraFacilityViewData.setChildPrice(extraFacilityViewData.getChildPrice().add(
                  extraModel.getPrices().get(0).getRate().getAmount()));
            }
            handleSeatExtra(extraModel, extraFacilityViewData);
            handleKidsActivityExtra(extraModel, extraFacilityViewData);
            isExtraPresent = true;
         }
      }

      handleExtraAbsence(categoryViewData, extraModel, isExtraPresent);
   }

   /**
    * @param categoryViewData
    * @param extraModel
    * @param isExtraPresent
    */
   private void handleExtraAbsence(final ExtraFacilityCategoryViewData categoryViewData,
      final ExtraFacility extraModel, final boolean isExtraPresent)
   {
      if (!isExtraPresent)
      {
         if (extraModel instanceof InsuranceExtraFacility)
         {
            final InsuranceExtraFacility insuranceModel = (InsuranceExtraFacility) extraModel;
            final InsuranceViewData insViewData = new InsuranceViewData();
            insuranceViewDataPopulator.populate(insuranceModel, insViewData);
            categoryViewData.getInsuranceViewData().add(insViewData);
         }
         else if (StringUtil.isNotEquals(extraModel.getExtraFacilityCode(),
            ExtraFacilityConstants.WORLD_CARE_FUND_CHILD))
         {
            final ExtraFacilityViewData extraView = new ExtraFacilityViewData();
            populateInsuranceViewData(extraModel);
            extraFacilityViewDataPopulator.populate(extraModel, extraView);
            assignPassengerForExtra(extraView, extraModel);
            populateKidsActivityExtra(extraModel, extraView);
            setSelectionForMeal(categoryViewData, extraView);
            categoryViewData.getExtraFacilityViewData().add(extraView);
         }
         else
         {
            populateWorldCareFundViewData(categoryViewData, extraModel);
         }
      }
   }

   /**
    * @param extraModel
    */
   private void populateInsuranceViewData(final ExtraFacility extraModel)
   {
      if (extraModel instanceof InsuranceExtraFacility)
      {
         final InsuranceExtraFacility insuranceModel = (InsuranceExtraFacility) extraModel;
         final InsuranceViewData insViewData = new InsuranceViewData();
         insuranceViewDataPopulator.populate(insuranceModel, insViewData);
      }
   }

   /**
    * @param categoryViewData
    * @param extraView
    */
   private void setSelectionForMeal(final ExtraFacilityCategoryViewData categoryViewData,
      final ExtraFacilityViewData extraView)
   {
      if (StringUtils.equalsIgnoreCase(categoryViewData.getExtraFacilityCategoryCode(),
         BookFlowConstants.MEAL))
      {
         extraView.setSelection(BookFlowConstants.INCLUDED);
      }
   }

   /**
    * @param extraModel
    * @param extraView
    */
   private void populateKidsActivityExtra(final ExtraFacility extraModel,
      final ExtraFacilityViewData extraView)
   {
      if (isKidsActivityExtra(extraView) && BigDecimal.ZERO.compareTo(extraView.getPrice()) == 0)
      {
         BigDecimal amount = extraView.getPrice();
         amount =
            amount.add(extraModel.getPrices().get(getPriceIndexBasedOnExFacilityType(extraModel))
               .getAmount().getAmount());
         extraView.setPrice(amount);
         extraView.setCurrencyAppendedPrice(getCurrencyAppendedPrice(
            amount,
            Currency.getInstance(Config.getString("default.currency",
               currencyResolver.getSiteCurrency()))));
      }
   }

   /**
    * @param extraModel
    * @param extraFacilityViewData
    */
   private void handleKidsActivityExtra(final ExtraFacility extraModel,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      if (isKidsActivityExtra(extraFacilityViewData))
      {
         BigDecimal amount = extraFacilityViewData.getPrice();
         amount =
            amount.add(extraModel.getPrices().get(getPriceIndexBasedOnExFacilityType(extraModel))
               .getAmount().getAmount());
         extraFacilityViewData.setPrice(amount);
         extraFacilityViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(
            amount,
            Currency.getInstance(Config.getString("default.currency",
               currencyResolver.getSiteCurrency()))));
         extraFacilityViewData.setQuantity(extraFacilityViewData.getQuantity()
            + extraModel.getPrices().get(getPriceIndexBasedOnExFacilityType(extraModel))
               .getQuantity().intValue());
         populateDescription(extraModel, extraFacilityViewData);
         assignPassengerForExtra(extraFacilityViewData, extraModel);
      }
   }

   /**
    * Populate description.
    *
    * @param extraModel the extra model
    * @param extraFacilityViewData the extra facility view data
    */
   private void populateDescription(final ExtraFacility extraModel,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      if (StringUtil.equalsIgnoreCase(extraModel.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.SWIMACADEMY))
      {
         extraFacilityViewData.setSummaryDescription(getDescription(extraModel
            .getExtraFacilityCategory().getCode(), extraModel.getDescription(), Integer
            .toString(extraFacilityViewData.getQuantity())));
      }
   }

   /**
    * @param extraModel
    * @param extraFacilityViewData
    */
   private void handleSeatExtra(final ExtraFacility extraModel,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      if (isSeatExtra(extraFacilityViewData))
      {
         updateSeatExtraFacilityQuantity(getBasePackageModel());
         BigDecimal totalExtraViewPrice = BigDecimal.ZERO;
         final Price adultPriceModel = extraModel.getPrices().get(0);
         final Price childPriceModel = extraModel.getPrices().get(1);
         final BigDecimal totalAdultPriceAmount =
            extraFacilityViewData.getAdultPrice().multiply(
               new BigDecimal(adultPriceModel.getQuantity().intValue()));
         final BigDecimal totalChildPriceAmount =
            extraFacilityViewData.getChildPrice().multiply(
               new BigDecimal(childPriceModel.getQuantity().intValue()));

         totalExtraViewPrice =
            totalExtraViewPrice.add(totalAdultPriceAmount).add(totalChildPriceAmount);
         extraFacilityViewData.setPrice(totalExtraViewPrice);
         extraFacilityViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(
            totalExtraViewPrice.setScale(TWO, RoundingMode.HALF_UP),
            Currency.getInstance(Config.getString("default.currency",
               currencyResolver.getSiteCurrency()))));
      }
   }

   /**
    * Populate world care fund view data.
    *
    * @param categoryViewData the category view data
    * @param extraModel the extra model
    */
   private void populateWorldCareFundViewData(final ExtraFacilityCategoryViewData categoryViewData,
      final ExtraFacility extraModel)
   {
      for (final ExtraFacilityViewData extraView : categoryViewData.getExtraFacilityViewData())
      {
         if (StringUtils.equalsIgnoreCase(extraView.getCode(),
            ExtraFacilityConstants.WORLD_CARE_FUND_ADULT)
            || StringUtils.equalsIgnoreCase(extraView.getCode(),
               ExtraFacilityConstants.WORLD_CARE_FUND_CHILD))
         {
            BigDecimal amount = extraView.getPrice();
            amount = amount.add(extraModel.getPrices().get(0).getAmount().getAmount());
            extraView.setPrice(amount);
            extraView.setCurrencyAppendedPrice(CurrencyUtils
               .getCurrencyAppendedPriceWithDecimalPart(amount));
         }
      }
   }

   /**
    * @param extraFacilities
    * @param excursionCategory
    */
   @SuppressWarnings(ExtraFacilityUpdator.BOXING)
   private void updateTotalPriceAndTotalQuantityForExcursions(
      final List<ExtraFacility> extraFacilities,
      final ExtraFacilityCategoryViewData excursionCategory)
   {

      BigDecimal totalPrice = BigDecimal.ZERO;
      int totalQuantity = 0;
      for (final ExtraFacility eachExtraFacility : extraFacilities)
      {
         final int quantity = eachExtraFacility.getPrices().get(0).getQuantity().intValue();
         totalPrice =
            totalPrice.add(eachExtraFacility.getPrices().get(0).getRate().getAmount()
               .multiply(BigDecimal.valueOf(quantity)));
         totalQuantity += quantity;
      }

      excursionCategory.setSelectedQuantity(totalQuantity);
      excursionCategory.setCurrencyAppendedCategoryTotalPrice(CurrencyUtils
         .getCurrencyAppendedPriceWithDecimalPart(totalPrice));

   }

   /**
    * @param extraFacilityViewData
    * @return true if the Extra belongs to kids activity.
    */
   private boolean isKidsActivityExtra(final ExtraFacilityViewData extraFacilityViewData)
   {
      final List<String> kidsActivityCategories =
         Arrays.asList(new String[] { ExtraFacilityConstants.CRECHEACADEMY,
            ExtraFacilityConstants.SWIMACADEMY, ExtraFacilityConstants.STAGEACADEMY,
            ExtraFacilityConstants.FOOTBALLACADEMY });
      return kidsActivityCategories.contains(extraFacilityViewData.getCategoryCode());
   }

   /**
    * @param extraFacilityViewData
    * @return true if the Extra belongs to Seat extra.
    */
   private boolean isSeatExtra(final ExtraFacilityViewData extraFacilityViewData)
   {
      final List<String> seatExtraCodes =
         Arrays.asList(new String[] { ExtraFacilityConstants.STANDARD_SEATS,
            ExtraFacilityConstants.SEATS_WITH_EXTRA_SPACE, ExtraFacilityConstants.PREMIUM_SEAT,
            ExtraFacilityConstants.EXTRA_LEG_ROOM });
      return seatExtraCodes.contains(extraFacilityViewData.getCode());
   }

   /**
    * The method is used to add the selected passenger to the newly selected extra.
    *
    * @param extraView
    * @param extraModel
    */
   private void assignPassengerForExtra(final ExtraFacilityViewData extraView,
      final ExtraFacility extraModel)
   {
      if (extraModel != null && extraModel.getPassengers() != null)
      {
         for (final Passenger passenger : extraModel.getPassengers())
         {
            extraView.getPassengerExtraFacilityMapping().getPassengers().add(passenger.getId());
         }
      }
   }

   /**
    * The method updates the total cost of the seat extras.
    *
    * @param viewData
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   public void updateSeatExtrasRelativePrice(final FlightOptionsViewData viewData)
   {
      final List<ExtraFacilityViewData> extras =
         viewData.getExtraFacilityViewDataContainer().getSeatOptions().getExtraFacilityViewData();
      BigDecimal selectedSeatPrice = BigDecimal.ZERO;
      selectedSeatPrice = getSelectedSeatPrice(viewData);

      for (final ExtraFacilityViewData viewExtra : extras)
      {
         final Map<String, Integer> personCountMap = extractApplicablePersons(viewData, viewExtra);
         final int noOfApplicableChildren = personCountMap.get("childCount").intValue();
         final int noOfApplicableAdults = personCountMap.get("adultCount").intValue();
         final BigDecimal price =
            (viewExtra.getAdultPrice().multiply(BigDecimal.valueOf(noOfApplicableAdults)))
               .add(viewExtra.getChildPrice().multiply(BigDecimal.valueOf(noOfApplicableChildren)));
         /**
          * If baggage and seat extra facility has been upgraded, the premium seat price needs to be
          * updated to show an relative price on UI
          */
         if (StringUtil.equalsIgnoreCase(viewExtra.getCode(), ExtraFacilityConstants.PREMIUM_SEAT))
         {
            final BigDecimal deltaPrice = sumBaggageAndSeatExtraPrice(viewData);
            final BigDecimal premiumPrice = price.subtract(deltaPrice);
            viewExtra.setPrice(premiumPrice);
            viewExtra
               .setCurrencyAppendedPrice(CurrencyUtils.getCurrencyAppendedPrice(premiumPrice));
            viewExtra.setCurrencyAppendedPerPersonPrice(CurrencyUtils
               .getCurrencyAppendedPrice(getCalculatedPrice(
                  price,
                  new BigDecimal(PassengerUtils.getChargeablePaxCount(getPackageModel()
                     .getPassengers())))));
         }
         else
         {

            viewExtra.setPrice(price.subtract(selectedSeatPrice));
            viewExtra.setCurrencyAppendedPrice(CurrencyUtils.getCurrencyAppendedPrice(viewExtra
               .getPrice().setScale(TWO, RoundingMode.HALF_UP)));
            final int chargeablePersons = noOfApplicableAdults + noOfApplicableChildren;
            viewExtra.setCurrencyAppendedPerPersonPrice(CurrencyUtils
               .getCurrencyAppendedPrice(getCalculatedPrice(viewExtra.getPrice(),
                  BigDecimal.valueOf(chargeablePersons))));
         }
      }

   }

   /**
    * @param viewData
    * @return BigDecimal
    */
   private BigDecimal getSelectedSeatPrice(final FlightOptionsViewData viewData)
   {
      BigDecimal selectedSeatPrice = BigDecimal.ZERO;
      for (final ExtraFacilityCategoryViewData categoryViewData : viewData.getPackageViewData()
         .getExtraFacilityCategoryViewData())
      {
         if (StringUtil.equalsIgnoreCase(categoryViewData.getExtraFacilityCategoryCode(),
            ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
         {
            // the extraPrice if any seat is selected other than default.
            selectedSeatPrice = getSelectedSeatExtraPrice(categoryViewData);
         }
      }
      return selectedSeatPrice;
   }

   /**
    * This method is used to update the price of Premium seats on selection of any baggage extra and
    * a seat extra to invoke the customer so that they can upgrade the seat options to Premium Seat.
    *
    * @param viewData
    */
   private BigDecimal sumBaggageAndSeatExtraPrice(final FlightOptionsViewData viewData)
   {
      BigDecimal selectedSeatPrice = BigDecimal.ZERO;
      BigDecimal selectedBaggagePrice = BigDecimal.ZERO;

      // iterate through selected extraCategory to identify whether baggage
      // and seat extras are been selected.
      for (final ExtraFacilityCategoryViewData categoryViewData : viewData.getPackageViewData()
         .getExtraFacilityCategoryViewData())
      {
         if (StringUtil.equalsIgnoreCase(categoryViewData.getExtraFacilityCategoryCode(),
            ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
         {

            // the extraPrice if any baggage is selected other than default.
            selectedBaggagePrice = getSelectedBaggageExtraPrice(categoryViewData);
         }
         else if (StringUtil.equalsIgnoreCase(categoryViewData.getExtraFacilityCategoryCode(),
            ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
         {

            // the extraPrice if any seat is selected other than default.
            selectedSeatPrice = getSelectedSeatExtraPrice(categoryViewData);
         }
      }
      return sumSeatBagPrice(selectedSeatPrice, selectedBaggagePrice);
   }

   /**
    * @param categoryViewData
    * @return isBaggageSelected
    */
   private BigDecimal getSelectedBaggageExtraPrice(
      final ExtraFacilityCategoryViewData categoryViewData)
   {
      BigDecimal price = BigDecimal.ZERO;
      for (final ExtraFacilityViewData extra : categoryViewData.getExtraFacilityViewData())
      {
         if (!extra.isFree())
         {
            price =
               price.add(extra.getPrice().multiply(
                  BigDecimal.valueOf(extra.getPassengerExtraFacilityMapping().getPassengers()
                     .size())));
         }
      }
      return price;
   }

   /**
    * @param categoryViewData
    * @return price
    */
   private BigDecimal getSelectedSeatExtraPrice(final ExtraFacilityCategoryViewData categoryViewData)
   {
      BigDecimal price = BigDecimal.ZERO;
      for (final ExtraFacilityViewData extra : categoryViewData.getExtraFacilityViewData())
      {
         price = price.add(extra.getPrice());
      }
      return price;
   }

   /**
    * Returns the sum of selected seats and baggage price.
    *
    * @param selectedSeatPrice the selected seat price
    * @param selectedBaggagePrice the selected baggage price
    * @return the selected
    */
   private BigDecimal sumSeatBagPrice(final BigDecimal selectedSeatPrice,
      final BigDecimal selectedBaggagePrice)
   {
      return selectedBaggagePrice.add(selectedSeatPrice);
   }

   /**
    * To extract the applicable no of persons for a given extra facility view data.
    *
    * @param viewData
    * @param viewExtra
    *
    */
   @SuppressWarnings(ExtraFacilityUpdator.BOXING)
   public Map<String, Integer> extractApplicablePersons(final FlightOptionsViewData viewData,
      final ExtraFacilityViewData viewExtra)
   {
      final Map<String, Integer> personsMap = new HashMap<String, Integer>();

      final PaxCompositionViewData paxViewData = viewData.getPackageViewData().getPaxViewData();
      int noOfApplicableChildren = 0;
      int noOfApplicableAdults = paxViewData.getNoOfAdults() + paxViewData.getNoOfSeniors();
      final int minAge = viewExtra.getMinAge();
      final int maxChildAge = viewExtra.getMaxChildAge();
      for (final Integer eachChildAge : paxViewData.getChildAges())
      {
         if (isValidChildAge(minAge, maxChildAge, eachChildAge))
         {
            noOfApplicableChildren++;
         }
         else if (eachChildAge.intValue() > maxChildAge)
         {
            noOfApplicableAdults++;
         }
      }
      personsMap.put("childCount", Integer.valueOf(noOfApplicableChildren));
      personsMap.put("adultCount", Integer.valueOf(noOfApplicableAdults));

      return personsMap;
   }

   /**
    * @param minAge
    * @param maxChildAge
    * @param eachChildAge
    */
   private boolean isValidChildAge(final int minAge, final int maxChildAge,
      final Integer eachChildAge)
   {
      return eachChildAge.intValue() >= minAge && eachChildAge.intValue() <= maxChildAge;
   }

   /**
    * To populate the currency appended price for the selected seat extra facility.
    *
    * @param containerCategoryViewData
    * @param packageCategoryViewDatas
    */
   public void populatePricesForSelectedSeats(
      final ExtraFacilityCategoryViewData containerCategoryViewData,
      final List<ExtraFacilityCategoryViewData> packageCategoryViewDatas)
   {
      final List<ExtraFacilityViewData> containerExtraFacilities =
         containerCategoryViewData.getExtraFacilityViewData();
      for (final ExtraFacilityCategoryViewData packageCategoryViewData : packageCategoryViewDatas)
      {
         for (final ExtraFacilityViewData packageExtraFacility : packageCategoryViewData
            .getExtraFacilityViewData())
         {
            setPriceForContainerExtras(containerExtraFacilities, packageExtraFacility);
         }
      }
   }

   /**
    * @param containerExtraFacilities
    * @param packageExtraFacility
    */
   private void setPriceForContainerExtras(
      final List<ExtraFacilityViewData> containerExtraFacilities,
      final ExtraFacilityViewData packageExtraFacility)
   {
      for (final ExtraFacilityViewData containerExtraFacility : containerExtraFacilities)
      {
         if (areSame(packageExtraFacility, containerExtraFacility))
         {
            packageExtraFacility.setPrice(containerExtraFacility.getPrice());
            packageExtraFacility.setCurrencyAppendedPrice(containerExtraFacility
               .getCurrencyAppendedPrice());
         }
      }
   }

   /**
    * @param packageExtraFacility
    * @param containerExtraFacility
    */
   private boolean areSame(final ExtraFacilityViewData packageExtraFacility,
      final ExtraFacilityViewData containerExtraFacility)
   {
      return StringUtils.equalsIgnoreCase(containerExtraFacility.getCode(),
         packageExtraFacility.getCode());
   }

   /**
    * This method calculates the price per chargeable person and returned with zero trimmed value.
    * First divide and then trim the zero decimals.
    *
    * @param price the total price
    * @param chargeablePersons the chargeable persons
    * @return calculatedPrice
    */
   private BigDecimal getCalculatedPrice(final BigDecimal price, final BigDecimal chargeablePersons)
   {
      if (chargeablePersons.compareTo(BigDecimal.ZERO) == 0)
      {
         return BigDecimal.ZERO;
      }
      return price.divide(chargeablePersons, TWO, RoundingMode.HALF_UP);
   }

   /**
    * To sort seat options sequentially.
    *
    * @param seatOptions
    */
   public void sortSeatsByIndex(final ExtraFacilityCategoryViewData seatOptions)
   {
      Collections.sort(seatOptions.getExtraFacilityViewData(),
         new Comparator<ExtraFacilityViewData>()
         {
            @Override
            public int compare(final ExtraFacilityViewData o1, final ExtraFacilityViewData o2)
            {
               if (o1.getPositionIndex() > o2.getPositionIndex())
               {
                  return 1;
               }
               return -1;
            }
         });
   }

   /**
    * To sort Display meals based on description.
    *
    * @param mealCategoryViewData
    */
   public void sortMealAlphabetically(final ExtraFacilityCategoryViewData mealCategoryViewData)
   {
      Collections.sort(mealCategoryViewData.getExtraFacilityViewData(),
         new Comparator<ExtraFacilityViewData>()
         {
            @Override
            public int compare(final ExtraFacilityViewData o1, final ExtraFacilityViewData o2)
            {
               return o1.getDescription().compareTo(o2.getDescription());
            }
         });
   }

   /**
    * To sort Baggage based on weight.
    *
    * @param baggageCategoryViewData
    */
   public void sortBaggageBasedOnWeight(final ExtraFacilityCategoryViewData baggageCategoryViewData)
   {
      Collections.sort(baggageCategoryViewData.getExtraFacilityViewData(),
         new Comparator<ExtraFacilityViewData>()
         {
            @Override
            public int compare(final ExtraFacilityViewData o1, final ExtraFacilityViewData o2)
            {
               return Integer.parseInt(o1.getWeightCode()) < Integer.parseInt(o2.getWeightCode())
                  ? BookFlowConstants.MINUS : BookFlowConstants.ONE;
            }
         });
   }

   /**
    * to get all extrafacility which will present in all the legs as a one with summation of all
    * extrafacility leg price
    *
    * @param outboundLegList (List of leg extra facilities from outbound Leg)
    * @param inboundLegList (List of leg extra facilities from inbound Leg)
    *
    * @return the map< array list< string>, array list< extra facility Model>>
    */
   public Map<String, ArrayList<ExtraFacility>> getValidExtraFacilityModelList(
      final List<LegExtraFacility> outboundLegList, final List<LegExtraFacility> inboundLegList)
   {
      final int totalLegs = outboundLegList.size() + inboundLegList.size();
      final List<ExtraFacilityCategory> outboundCategory = new ArrayList<ExtraFacilityCategory>();
      final List<ExtraFacilityCategory> inboundCategory = new ArrayList<ExtraFacilityCategory>();
      final List<ExtraFacility> combinedExtraFacilities = new ArrayList<ExtraFacility>();
      final Map<String, ArrayList<ExtraFacility>> filteredExtraFacility =
         new HashMap<String, ArrayList<ExtraFacility>>();
      for (final LegExtraFacility outbound : outboundLegList)
      {
         outboundCategory.addAll(outbound.getExtraFacilityCategories());
      }
      for (final LegExtraFacility inbound : inboundLegList)
      {
         inboundCategory.addAll(inbound.getExtraFacilityCategories());
      }
      addCombinedExtrasFromItinerary(outboundCategory, inboundCategory, combinedExtraFacilities);
      processCombinedExtraFacilitiesFurther(totalLegs, combinedExtraFacilities,
         filteredExtraFacility);
      return filteredExtraFacility;
   }

   /**
    * @param outboundCategory
    * @param inboundCategory
    * @param combinedExtraFacilities
    */
   private void addCombinedExtrasFromItinerary(final List<ExtraFacilityCategory> outboundCategory,
      final List<ExtraFacilityCategory> inboundCategory,
      final List<ExtraFacility> combinedExtraFacilities)
   {
      for (final ExtraFacilityCategory extrasOutbound : outboundCategory)
      {
         combinedExtraFacilities.addAll(extrasOutbound.getExtraFacilities());
      }
      for (final ExtraFacilityCategory extrasInbound : inboundCategory)
      {
         combinedExtraFacilities.addAll(extrasInbound.getExtraFacilities());
      }
   }

   /**
    * @param totalLegs
    * @param combinedExtraFacilities
    * @param filteredExtraFacility
    */
   private void processCombinedExtraFacilitiesFurther(final int totalLegs,
      final List<ExtraFacility> combinedExtraFacilities,
      final Map<String, ArrayList<ExtraFacility>> filteredExtraFacility)
   {
      if (combinedExtraFacilities.size() > 1)
      {
         getGroupedFacility(combinedExtraFacilities, filteredExtraFacility, totalLegs);
      }
   }

   /**
    * get the extra facilities which are present on all the Legs
    *
    * @param combinedList (ExtraFacility from all the legs)
    * @param filteredExtraFacility (Key= ExtraFacilityCode, Value= List<ExtraFacility> related to
    *           code from all the legs)
    * @param totalLegs (Total no. of legs)
    *
    */
   private void getGroupedFacility(final List<ExtraFacility> combinedList,
      final Map<String, ArrayList<ExtraFacility>> filteredExtraFacility, final int totalLegs)
   {
      final Map<ArrayList<String>, ArrayList<ExtraFacility>> groupedExtraFacility =
         groupByExtraFacilityCode(combinedList);
      for (final Map.Entry<ArrayList<String>, ArrayList<ExtraFacility>> entry : groupedExtraFacility
         .entrySet())
      {
         if (CollectionUtils.size(entry.getValue()) == totalLegs)
         {
            filteredExtraFacility.put(entry.getKey().get(0), entry.getValue());
         }
      }
   }

   /**
    * Group by extra facility code using josql.
    *
    * @param cpoExtraFacilities the cpo extra facilities
    *
    * @return the map< array list< string>, array list< extra facility Model>>
    */
   @SuppressWarnings(BookFlowConstants.UNCHECKED)
   private Map<ArrayList<String>, ArrayList<ExtraFacility>> groupByExtraFacilityCode(
      final List<ExtraFacility> cpoExtraFacilities)
   {
      Map<ArrayList<String>, ArrayList<ExtraFacility>> extraFacilityMap =
         new HashMap<ArrayList<String>, ArrayList<ExtraFacility>>();
      try
      {
         final StringBuilder queryForParsing = new StringBuilder("SELECT * FROM ");
         queryForParsing.append(ExtraFacility.class.getName());
         queryForParsing.append(" GROUP BY extraFacilityCode ");
         final Query query = new Query();
         query.parse(queryForParsing.toString());
         final QueryResults queryResults = query.execute(cpoExtraFacilities);
         extraFacilityMap = extraFacilityMap.getClass().cast(queryResults.getGroupByResults());
      }
      catch (final QueryParseException qpe)
      {
         LOGGER.error("Caught QueryParseException", qpe);
         return Collections.emptyMap();
      }
      catch (final QueryExecutionException qee)
      {
         LOGGER.error("Caught QueryParseException", qee);
         return Collections.emptyMap();
      }

      return extraFacilityMap;
   }

   /**
    * To update the price of seat extra facility when upgraded.
    *
    * @param viewData
    * @param seatExtrasCode
    */
   public void updateSeatExtrasPrice(final FlightOptionsViewData viewData,
      final String seatExtrasCode, final boolean isPremiumUpdated)
   {
      if (!StringUtil.equalsIgnoreCase(seatExtrasCode, "DEFAULT"))
      {
         final List<ExtraFacilityViewData> extras =
            viewData.getExtraFacilityViewDataContainer().getSeatOptions()
               .getExtraFacilityViewData();
         BigDecimal selectedSeatPrice = BigDecimal.ZERO;
         for (final ExtraFacilityViewData viewExtra : extras)
         {
            if (StringUtil.equalsIgnoreCase(viewExtra.getCode(), seatExtrasCode))
            {
               selectedSeatPrice = viewExtra.getPrice();
            }
         }
         populateExtrasViewData(viewData, seatExtrasCode, isPremiumUpdated, extras,
            selectedSeatPrice);
      }
   }

   /**
    * @param viewData
    * @param seatExtrasCode
    * @param isPremiumUpdated
    * @param extras
    * @param selectedSeatPrice
    */
   private void populateExtrasViewData(final FlightOptionsViewData viewData,
      final String seatExtrasCode, final boolean isPremiumUpdated,
      final List<ExtraFacilityViewData> extras, final BigDecimal selectedSeatPrice)
   {
      for (final ExtraFacilityViewData viewExtra : extras)
      {
         if (isValidCode(seatExtrasCode, isPremiumUpdated, viewExtra))
         {
            final Map<String, Integer> personCountMap =
               extractApplicablePersons(viewData, viewExtra);
            final int noOfApplicableChildren = personCountMap.get("childCount").intValue();
            final int noOfApplicableAdults = personCountMap.get("adultCount").intValue();
            final BigDecimal price = viewExtra.getPrice().subtract(selectedSeatPrice);
            viewExtra.setPrice(price);
            viewExtra.setCurrencyAppendedPrice(CurrencyUtils.getCurrencyAppendedPrice(viewExtra
               .getPrice().setScale(TWO, RoundingMode.HALF_UP)));
            final int chargeablePersons = noOfApplicableAdults + noOfApplicableChildren;
            viewExtra.setCurrencyAppendedPerPersonPrice(CurrencyUtils
               .getCurrencyAppendedPrice(getCalculatedPrice(viewExtra.getPrice(),
                  BigDecimal.valueOf(chargeablePersons))));
         }
      }
   }

   /**
    * @param seatExtrasCode
    * @param isPremiumUpdated
    * @param viewExtra
    */
   private boolean isValidCode(final String seatExtrasCode, final boolean isPremiumUpdated,
      final ExtraFacilityViewData viewExtra)
   {
      return !(isPremiumUpdated && StringUtil.equalsIgnoreCase(viewExtra.getCode(), "PMCL"))
         && (!StringUtil.equalsIgnoreCase(viewExtra.getCode(), seatExtrasCode));
   }

   /**
    * Removes the List of passengers from the Baggage extraFacility.
    *
    * @param categoryModel
    */

   /**
    * The method is used to set the selection Flag of the seat extra facility based on the selected
    * seatExtrasCode.
    *
    * @param viewData
    * @param seatExtrasCode
    */
   public void setSelectionFlagForSeatExtra(final FlightOptionsViewData viewData,
      final String seatExtrasCode)
   {
      for (final ExtraFacilityViewData extraViewData : viewData.getExtraFacilityViewDataContainer()
         .getSeatOptions().getExtraFacilityViewData())
      {
         if (StringUtils.equalsIgnoreCase(seatExtrasCode, extraViewData.getCode()))
         {
            extraViewData.setSelected(true);
         }
         else
         {
            extraViewData.setSelected(false);
         }
      }

   }

   /**
    * Gets the currency appended price.
    *
    * @param price the price
    * @param currency the currency
    * @return the currency appended price
    */
   private String getCurrencyAppendedPrice(final BigDecimal price, final Currency currency)
   {
      if (price.intValue() >= BookFlowConstants.ONE)
      {
         return currency.getSymbol() + price;
      }
      return StringUtils.EMPTY;
   }

   /**
    * Sorts the Infant Equipments based on price
    */
   public void sortInfantEquipBasedOnPrice(final ExtraFacilityCategoryViewData infantEquipViewData)
   {
      Collections.sort(infantEquipViewData.getExtraFacilityViewData(),
         new Comparator<ExtraFacilityViewData>()
         {
            @Override
            public int compare(final ExtraFacilityViewData o1, final ExtraFacilityViewData o2)
            {
               return o1.getCurrencyAppendedPerPersonPrice().compareTo(
                  o2.getCurrencyAppendedPerPersonPrice());
            }
         });

   }

   /**
    * @param packageViewData
    * @param extraFacilityViewDataContainer
    */
   public void updateContainerViewData(final PackageViewData packageViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      for (final ExtraFacilityCategoryViewData extraFacilityCategory : packageViewData
         .getExtraFacilityCategoryViewData())
      {
         updateSeatExtras(extraFacilityCategory, extraFacilityViewDataContainer);
         updateMealExtras(extraFacilityCategory, extraFacilityViewDataContainer);
         updateBaggageExtras(extraFacilityCategory, extraFacilityViewDataContainer);
         updateInfantOptionsExtras(extraFacilityCategory, extraFacilityViewDataContainer);
         updateWorkshopExtra(extraFacilityCategory, extraFacilityViewDataContainer);
         updateLateCheckout(extraFacilityCategory, extraFacilityViewDataContainer);
         updateDonationExtras(extraFacilityCategory, extraFacilityViewDataContainer);
         updateExcursionExtras(extraFacilityCategory, extraFacilityViewDataContainer);
      }

   }

   /**
    * Update excursion extras.
    *
    * @param extraFacilityCategory the extra facility category
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateExcursionExtras(final ExtraFacilityCategoryViewData extraFacilityCategory,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      for (final ExtraFacilityCategoryViewData category : extraFacilityViewDataContainer
         .getExcursionOptions())
      {
         updateExtras(extraFacilityCategory.getExtraFacilityViewData(),
            category.getExtraFacilityViewData());
      }
   }

   /**
    * Update donation extras.
    *
    * @param extraFacilityCategoryViewData the extra facility category view data
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateDonationExtras(
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      if (StringUtils.equalsIgnoreCase(
         extraFacilityCategoryViewData.getExtraFacilityCategoryCode(),
         ExtraFacilityConstants.DONATION_CATEGORY))
      {
         updateExtras(extraFacilityCategoryViewData.getExtraFacilityViewData(),
            extraFacilityViewDataContainer.getDonationOptions().getExtraFacilityViewData());
      }
   }

   /**
    * Update late checkout.
    *
    * @param extraFacilityCategoryViewData the extra facility category view data
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateLateCheckout(
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      if (StringUtils.equalsIgnoreCase(
         extraFacilityCategoryViewData.getExtraFacilityCategoryCode(),
         ExtraFacilityConstants.ACCOMMODATION))
      {
         updateExtras(extraFacilityCategoryViewData.getExtraFacilityViewData(),
            extraFacilityViewDataContainer.getLateCheckOut().getExtraFacilityViewData());
      }
   }

   /**
    * Update infant options extras.
    *
    * @param extraFacilityCategoryViewData the extra facility category view data
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateInfantOptionsExtras(
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      if (StringUtils.equalsIgnoreCase(
         extraFacilityCategoryViewData.getExtraFacilityCategoryCode(),
         ExtraFacilityConstants.INFANT_OPTION_CATEGORY))
      {
         updateExtras(extraFacilityCategoryViewData.getExtraFacilityViewData(),
            extraFacilityViewDataContainer.getInfantOptions().getExtraFacilityViewData());
      }
   }

   private void updateWorkshopExtra(
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      if (StringUtils.equalsIgnoreCase(
         extraFacilityCategoryViewData.getExtraFacilityCategoryCode(),
         ExtraFacilityConstants.CLASS_ACT_WORKSHOP_OPTION_CATEGORY))
      {
         updateExtras(extraFacilityCategoryViewData.getExtraFacilityViewData(),
            extraFacilityViewDataContainer.getWorkshopOptions().getExtraFacilityViewData());
      }
   }

   /**
    * Update baggage extras.
    *
    * @param extraFacilityCategoryViewData the extra facility category view data
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateBaggageExtras(
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      if (StringUtils.equalsIgnoreCase(
         extraFacilityCategoryViewData.getExtraFacilityCategoryCode(), BookFlowConstants.BAG))
      {
         updateExtras(extraFacilityCategoryViewData.getExtraFacilityViewData(),
            extraFacilityViewDataContainer.getBaggageOptions().getExtraFacilityViewData());
      }
   }

   /**
    * Update meal extras.
    *
    * @param extraFacilityCategoryViewData the extra facility category view data
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateMealExtras(final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      if (StringUtils.equalsIgnoreCase(
         extraFacilityCategoryViewData.getExtraFacilityCategoryCode(), BookFlowConstants.MEAL))
      {
         updateExtras(extraFacilityCategoryViewData.getExtraFacilityViewData(),
            extraFacilityViewDataContainer.getMealOptions().getExtraFacilityViewData());
      }
   }

   /**
    * Update seat extras.
    *
    * @param extraFacilityCategoryViewData the extra facility category view data
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateSeatExtras(final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      if (StringUtils.equalsIgnoreCase(
         extraFacilityCategoryViewData.getExtraFacilityCategoryCode(), BookFlowConstants.SEAT))
      {
         updateExtras(extraFacilityCategoryViewData.getExtraFacilityViewData(),
            extraFacilityViewDataContainer.getSeatOptions().getExtraFacilityViewData());

      }

   }

   /**
    * Update extras.
    *
    * @param packageExtras the package extras
    * @param containerExtras the container extras
    */
   private void updateExtras(final List<ExtraFacilityViewData> packageExtras,
      final List<ExtraFacilityViewData> containerExtras)
   {
      for (final ExtraFacilityViewData packageExtraFacility : packageExtras)
      {
         for (final ExtraFacilityViewData containerExtraFacility : containerExtras)
         {
            if (StringUtils.equalsIgnoreCase(packageExtraFacility.getCode(),
               containerExtraFacility.getCode()))
            {
               containerExtraFacility.setSelected(packageExtraFacility.isSelected());
               containerExtraFacility.setSelectedQuantity(packageExtraFacility
                  .getSelectedQuantity());
               updateCurrencyAppendedPrice(packageExtraFacility, containerExtraFacility);
               updateBaggageTotalSelection(packageExtraFacility, containerExtraFacility);
            }
         }
      }
   }

   /**
    * @param packageExtraFacility
    * @param containerExtraFacility
    */
   private void updateCurrencyAppendedPrice(final ExtraFacilityViewData packageExtraFacility,
      final ExtraFacilityViewData containerExtraFacility)
   {
      if (StringUtils.equalsIgnoreCase(packageExtraFacility.getCategoryCode(),
         ExtraFacilityConstants.INFANT_OPTION_CATEGORY))
      {
         containerExtraFacility.setCurrencyAppendedPrice(packageExtraFacility
            .getCurrencyAppendedPrice());
      }
   }

   /**
    * @param packageExtraFacility
    * @param containerExtraFacility
    */
   private void updateBaggageTotalSelection(final ExtraFacilityViewData packageExtraFacility,
      final ExtraFacilityViewData containerExtraFacility)
   {
      if (containerExtraFacility instanceof BaggageExtraFacilityViewData)
      {
         updateTotalSelected(containerExtraFacility, packageExtraFacility);
      }
   }

   /**
    * @param containerExtraFacility
    * @param packageExtraFacility
    */
   private void updateTotalSelected(final ExtraFacilityViewData containerExtraFacility,
      final ExtraFacilityViewData packageExtraFacility)
   {
      if (packageExtraFacility.getPassengerExtraFacilityMapping().getPassengers().size() == PassengerUtils
         .getPersonTypeCountFromPassengers(packageCartService.getBasePackage().getPassengers(),
            EnumSet.of(PersonType.ADULT, PersonType.CHILD)))
      {
         ((BaggageExtraFacilityViewData) containerExtraFacility).setTotalSelected(true);
      }
   }

   /**
    * Update extra options container view.
    *
    * @param packageModel the package view data
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   public void updateExtraOptionsContainerView(final BasePackage packageModel,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      final List<String> kidsActivityCategoryList = getKidsActivityCriteriaList();
      final Map<String, ExtraFacilityCategoryViewData> categoryMap =
         getCategoryViewData(extraFacilityViewDataContainer);
      boolean charityAbsent = true;
      for (final ExtraFacilityCategory extraFacilityCategory : packageModel
         .getExtraFacilityCategories())
      {
         updateExtraFacilityCategory(extraFacilityViewDataContainer, kidsActivityCategoryList,
            categoryMap, extraFacilityCategory);

         if (StringUtil.equalsIgnoreCase(extraFacilityCategory.getCode(), "Charity"))
         {
            charityAbsent = false;
         }
      }

      if (charityAbsent)
      {
         updateCharityAbsent(extraFacilityViewDataContainer);
      }
   }

   /**
    * Update charity absent.
    *
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   private void updateCharityAbsent(
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      for (final ExtraFacilityViewData viewData : extraFacilityViewDataContainer
         .getDonationOptions().getExtraFacilityViewData())
      {
         viewData.setSelected(false);
      }

   }

   /**
    * @param extraFacilityViewDataContainer
    * @param kidsActivityCategoryList
    * @param categoryMap
    * @param extraFacilityCategory
    */
   private void updateExtraFacilityCategory(
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer,
      final List<String> kidsActivityCategoryList,
      final Map<String, ExtraFacilityCategoryViewData> categoryMap,
      final ExtraFacilityCategory extraFacilityCategory)
   {
      if (kidsActivityCategoryList.contains(extraFacilityCategory.getCode()))
      {
         updateKidsActivityExtra(categoryMap, extraFacilityCategory);
      }
      else if (isExcursion(extraFacilityViewDataContainer, extraFacilityCategory))
      {
         updateContainerExtraFacilityViewaData(extraFacilityCategory.getExtraFacilities(),
            extraFacilityViewDataContainer.getExcursionOptions());
      }
      else if (isAttration(extraFacilityViewDataContainer, extraFacilityCategory))
      {
         updateAttractionExtra(extraFacilityViewDataContainer, extraFacilityCategory);
      }
   }

   /**
    * @param categoryMap
    * @param extraFacilityCategory
    */
   private void updateKidsActivityExtra(
      final Map<String, ExtraFacilityCategoryViewData> categoryMap,
      final ExtraFacilityCategory extraFacilityCategory)
   {
      for (final ExtrasToPassengerRelationViewData relationViewData : categoryMap.get(
         extraFacilityCategory.getCode()).getExtrasToPassengerMapping())
      {
         updateContainerExtraFacilityViewaData(extraFacilityCategory.getExtraFacilities(),
            relationViewData.getExtras(), relationViewData.getPassenger());
      }
   }

   /**
    * @param extraFacilityViewDataContainer
    * @param extraFacilityCategory
    */
   private void updateAttractionExtra(
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer,
      final ExtraFacilityCategory extraFacilityCategory)
   {
      for (final List<ExtraFacilityCategoryViewData> attractionCategories : extraFacilityViewDataContainer
         .getAttractionOptions().values())
      {
         updateContainerExtraFacilityViewaData(extraFacilityCategory.getExtraFacilities(),
            attractionCategories);
      }
   }

   /**
    * @param extraFacilityViewDataContainer
    * @param extraFacilityCategory
    */
   private boolean isExcursion(final ExtraFacilityViewDataContainer extraFacilityViewDataContainer,
      final ExtraFacilityCategory extraFacilityCategory)
   {
      return StringUtils.equals(extraFacilityCategory.getSuperCategoryCode(),
         ExtraFacilityConstants.EXCURSION)
         && SyntacticSugar.isNotNull(extraFacilityViewDataContainer.getExcursionOptions());
   }

   /**
    * @param extraFacilityViewDataContainer
    * @param extraFacilityCategory
    */
   private boolean isAttration(final ExtraFacilityViewDataContainer extraFacilityViewDataContainer,
      final ExtraFacilityCategory extraFacilityCategory)
   {
      return StringUtils.equals(extraFacilityCategory.getSuperCategoryCode(),
         ExtraFacilityConstants.ATTRACTION)
         && SyntacticSugar.isNotNull(extraFacilityViewDataContainer.getExcursionOptions());
   }

   /**
    * @param extraFacilities
    * @param containerCategoryList
    */
   private void updateContainerExtraFacilityViewaData(final List<ExtraFacility> extraFacilities,
      final List<ExtraFacilityCategoryViewData> containerCategoryList)
   {

      for (final ExtraFacilityCategoryViewData categoryViewData : containerCategoryList)
      {
         updatePriceAndSelectedAttributes(extraFacilities, categoryViewData);
      }

   }

   /**
    * @param extraFacilities
    * @param categoryViewData
    */
   private void updatePriceAndSelectedAttributes(final List<ExtraFacility> extraFacilities,
      final ExtraFacilityCategoryViewData categoryViewData)
   {
      BigDecimal totalPrice = BigDecimal.ZERO;
      for (final ExtraFacility eachExtraFacility : extraFacilities)
      {
         totalPrice = updateCategory(categoryViewData, totalPrice, eachExtraFacility);
      }
   }

   /**
    * Update category.
    *
    * @param categoryViewData the category view data
    * @param totalPrice the total price
    * @param eachExtraFacility the each extra facility
    * @return the big decimal
    */
   @SuppressWarnings(ExtraFacilityUpdator.BOXING)
   private BigDecimal updateCategory(final ExtraFacilityCategoryViewData categoryViewData,
      final BigDecimal totalPrice, final ExtraFacility eachExtraFacility)
   {
      BigDecimal price = totalPrice;
      if (StringUtils.equalsIgnoreCase(categoryViewData.getExtraFacilityCategoryCode(),
         eachExtraFacility.getExtraFacilityCategory().getCode()))
      {
         for (final ExtraFacilityViewData eachExtraFacilityViewData : categoryViewData
            .getExtraFacilityViewData())
         {
            if (StringUtils.equalsIgnoreCase(eachExtraFacilityViewData.getCode(),
               eachExtraFacility.getExtraFacilityCode()))
            {
               eachExtraFacilityViewData.setSelected(eachExtraFacility.isSelected());
               eachExtraFacilityViewData.setSelectedQuantity(eachExtraFacility.getPassengers()
                  .size());
               price =
                  price.add(eachExtraFacility
                     .getPrices()
                     .get(0)
                     .getRate()
                     .getAmount()
                     .multiply(
                        BigDecimal.valueOf(eachExtraFacility.getPrices().get(0).getQuantity()
                           .intValue())));
            }
         }
         categoryViewData.setSelected(true);
         categoryViewData.setCurrencyAppendedCategoryTotalPrice(CurrencyUtils
            .getCurrencyAppendedPriceWithDecimalPart(price));
      }
      return price;
   }

   /**
    * in this method we are setting the quantity only , selection status is already updated with
    * container view data
    *
    * Update container extra facility viewa data.
    *
    * @param packageExtras the package extras
    * @param containerExtras the container extras
    */

   private void updateContainerExtraFacilityViewaData(final List<ExtraFacility> packageExtras,
      final List<ExtraFacilityViewData> containerExtras, final PassengerViewData passenger)
   {
      for (final ExtraFacility packageExtraFacility : packageExtras)
      {
         updatePriceAndQuantityForSwim(containerExtras, passenger, packageExtraFacility);
      }
   }

   /**
    * @param containerExtras
    * @param passenger
    * @param packageExtraFacility
    */
   @SuppressWarnings(ExtraFacilityUpdator.BOXING)
   private void updatePriceAndQuantityForSwim(final List<ExtraFacilityViewData> containerExtras,
      final PassengerViewData passenger, final ExtraFacility packageExtraFacility)
   {
      for (final ExtraFacilityViewData containerExtraFacility : containerExtras)
      {
         if (StringUtils.equalsIgnoreCase(packageExtraFacility.getExtraFacilityCode(),
            containerExtraFacility.getCode())
            && (!updateForSwimAcademy(passenger, packageExtraFacility, containerExtraFacility)))
         {
            containerExtraFacility.setSelectedQuantity(packageExtraFacility.getPrices()
               .get(getPriceIndexBasedOnExFacilityType(packageExtraFacility)).getQuantity()
               .intValue());

         }
      }
   }

   /**
    * @param passenger
    * @param packageExtraFacility
    * @param containerExtraFacility
    */
   @SuppressWarnings(ExtraFacilityUpdator.BOXING)
   private boolean updateForSwimAcademy(final PassengerViewData passenger,
      final ExtraFacility packageExtraFacility, final ExtraFacilityViewData containerExtraFacility)
   {

      if (StringUtil.equalsIgnoreCase(packageExtraFacility.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.SWIMACADEMY))
      {
         if (checkIsPassengerPresent(packageExtraFacility, passenger.getIdentifier()))
         {
            containerExtraFacility.setSelectedQuantity(packageExtraFacility.getPrices()
               .get(getPriceIndexBasedOnExFacilityType(packageExtraFacility)).getQuantity()
               .intValue());
            containerExtraFacility.setCurrencyAppendedTotalPrice(CurrencyUtils
               .getCurrencyAppendedPrice(packageExtraFacility.getPrices()
                  .get(getPriceIndexBasedOnExFacilityType(packageExtraFacility)).getAmount()
                  .getAmount()));

         }
         return true;
      }
      return false;
   }

   /**
    * @param packageExtraFacility
    * @param identifier
    * @return
    */
   @SuppressWarnings({ "javadoc", ExtraFacilityUpdator.BOXING })
   private boolean checkIsPassengerPresent(final ExtraFacility packageExtraFacility,
      final int identifier)
   {
      for (final Passenger passenger : packageExtraFacility.getPassengers())
      {
         if (passenger.getId().intValue() == identifier)
         {
            return true;
         }
      }
      return false;
   }

   /**
    * The Method returns the List of ExtraFacilityCategoryCodes List
    *
    * @return list of KidsActivityCategory
    */
   public List<String> getKidsActivityCriteriaList()
   {
      return Arrays.asList(new String[] { ExtraFacilityConstants.CRECHEACADEMY,
         ExtraFacilityConstants.SWIMACADEMY, ExtraFacilityConstants.STAGEACADEMY,
         ExtraFacilityConstants.FOOTBALLACADEMY });
   }

   /**
    * returns the map of categoryViewData corresponding to each extra.
    *
    * @param containerViewData
    * @return categoryMap
    */
   public Map<String, ExtraFacilityCategoryViewData> getCategoryViewData(
      final ExtraFacilityViewDataContainer containerViewData)
   {
      final Map<String, ExtraFacilityCategoryViewData> categoryMap =
         new HashMap<String, ExtraFacilityCategoryViewData>();
      categoryMap.put(ExtraFacilityConstants.SWIMACADEMY, containerViewData.getSwimOptions());
      categoryMap.put(ExtraFacilityConstants.STAGEACADEMY, containerViewData.getStageOptions());
      categoryMap.put(ExtraFacilityConstants.FOOTBALLACADEMY, containerViewData.getSoccerOptions());
      categoryMap.put(ExtraFacilityConstants.CRECHEACADEMY, containerViewData.getCrecheOptions());
      return categoryMap;
   }

   /**
    * Gets the description from content.
    *
    * @param key the key
    * @param value the value
    * @return the description
    */
   private String getDescription(final String key, final String... value)
   {
      final Map<String, String> summaryContentMap = staticContentServ.getSummaryContents();

      if (key != null)
      {
         final String summaryContent = summaryContentMap.get(key);
         if (SyntacticSugar.isNotNull(summaryContent))
         {
            if (SyntacticSugar.isEmpty(value))
            {
               return summaryContent;
            }
            return StringUtil.substitute(summaryContent, value);
         }
      }
      return StringUtils.EMPTY;
   }

   /**
    * Update extra facility quntity.
    *
    * @param packageModel the package model
    */
   private void updateSeatExtraFacilityQuantity(final BasePackage packageModel)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         for (final ExtraFacility extras : categoryModel.getExtraFacilities())
         {
            if (isSeat(extras))
            {
               extraFacilityQuantityCalculationService.updateExtraFacilitySelectedQuantityForSeats(
                  extras, packageModel.getPassengers());
            }
         }
      }
   }

   /**
    * @param extras
    */
   @SuppressWarnings(ExtraFacilityUpdator.BOXING)
   private boolean isSeat(final ExtraFacility extras)
   {
      return extras.isSelected()
         && StringUtil.equalsIgnoreCase(extras.getExtraFacilityCategory().getCode(),
            ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE);
   }

   /**
    * This method populates the default transfer/car hire option which is available as part of the
    * package.
    *
    * @param packageModel the package model
    * @param packageViewData the package view data
    */
   private void populateDefaultTransferOption(final BasePackage packageModel,
      final PackageViewData packageViewData)
   {
      final List<HighLights> highlights = packageModel.getListOfHighlights();
      if (transferOptionOrCarHireNotExits(packageModel)
         && isTransferOptionApplicable(packageModel.getProductType(), highlights))
      {
         final ExtraFacilityCategoryViewData categoryViewData = new ExtraFacilityCategoryViewData();
         categoryViewData.setExtraFacilityCategoryCode(ExtraFacilityConstants.TRANSFER);
         categoryViewData.setExtraFacilityGroupCode("PACKAGE");
         final ExtraFacilityViewData transferViewData = new ExtraFacilityViewData();
         transferViewData.setGroupCode(ExtraFacilityConstants.TRANSFER);
         transferViewData.setSelected(true);
         transferViewData.setPrice(BigDecimal.ZERO);
         categoryViewData.getExtraFacilityViewData().add(transferViewData);
         packageViewData.getExtraFacilityCategoryViewData().add(categoryViewData);
      }
   }

   /**
    * Checks if transfer option is applicable
    *
    * @param highlights
    * @return boolean
    */
   private boolean isTransferOptionApplicable(final String productType,
      final List<HighLights> highlights)
   {
      return isTransferPresent(highlights) || isPreSelectableTransferProduct(productType);
   }

   /**
    * Checks if transfer option pre-selectable product
    *
    * @param productType
    * @return boolean
    */
   private boolean isPreSelectableTransferProduct(final String productType)
   {
      final List<String> productTypes = Arrays.asList("SKY", "FTW", "ALC");
      return productTypes.contains(productType);
   }

   /**
    * @param highlights
    * @return boolean
    */
   private boolean isTransferPresent(final List<HighLights> highlights)
   {
      return CollectionUtils.isNotEmpty(highlights)
         && (highlights.contains(HighLights.FREE_CAR_HIRE) || highlights
            .contains(HighLights.COACH_TRANSFER));
   }

   /**
    * Checks if is no transfer opted.
    *
    * @param packageModel the package model
    * @return true, if is no transfer opted
    */
   private boolean transferOptionOrCarHireNotExits(final BasePackage packageModel)
   {
      for (final ExtraFacilityCategory categoryModel : packageModel.getExtraFacilityCategories())
      {
         if ((categoryModel.getExtraFacilityGroup() == ExtraFacilityGroup.PACKAGE)
            && (!checkIfTransferPresent(categoryModel)))
         {
            return false;
         }
      }
      return true;
   }

   /**
    * @param categoryModel
    * @return isTransferNotPresent
    */
   private boolean checkIfTransferPresent(final ExtraFacilityCategory categoryModel)
   {
      boolean isTransferNotPresent = true;
      for (final ExtraFacility extraFacilityModel : categoryModel.getExtraFacilities())
      {
         if (isTransferOrCarHire(categoryModel, extraFacilityModel))
         {
            isTransferNotPresent = false;
         }
      }
      return isTransferNotPresent;
   }

   /**
    * @param categoryModel
    * @param extraFacilityModel
    */
   private boolean isTransferOrCarHire(final ExtraFacilityCategory categoryModel,
      final ExtraFacility extraFacilityModel)
   {
      return BookFlowConstants.TRANSFER_OPTION_CODES.contains(extraFacilityModel
         .getExtraFacilityCode())
         || StringUtils.equalsIgnoreCase(categoryModel.getSuperCategoryCode(),
            ExtraFacilityConstants.CAR_HIRE);
   }

   /**
    * @param packageModel
    * @param viewData
    */
   public void updateBaggageDisplayIndicator(final BasePackage packageModel,
      final FlightOptionsViewData viewData)
   {
      if (ExtraFacilityUtils.isPremiumSeatSelected(packageModel.getExtraFacilityCategories()))
      {
         viewData.setBaggageSectionDisplayed(false);
      }
   }

   /**
    * Gets the price index based on ex facility type.
    *
    * @param extra the extra
    * @return the price index based on ex facility type
    */
   public int getPriceIndexBasedOnExFacilityType(final ExtraFacility extra)
   {
      int index = 1;
      if (extra.getType() == ExtraFacilityType.PACKAGE || extra.getPrices().size() == 1)
      {
         index = 0;
      }
      return index;
   }

   /**
    * Gets the price index based on ex facility type.
    *
    * @param extra the extra
    * @return the price index based on ex facility type
    */
   public int getPriceIndexBasedOnExFacilityTypeLite(final ExtraFacility extra)
   {
      int index = 1;
      if (extra.getType() == ExtraFacilityType.PACKAGE || extra.getPrices().size() == 1)
      {
         index = 0;
      }
      return index;
   }

}
