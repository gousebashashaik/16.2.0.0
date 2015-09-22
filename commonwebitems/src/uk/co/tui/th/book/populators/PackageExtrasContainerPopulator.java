/**
 *
 */
package uk.co.tui.th.book.populators;

import static uk.co.tui.th.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.travel.enums.FacilitySelectionType;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityRestrictions;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.PriceType;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.th.book.constants.BookFlowConstants;
import uk.co.tui.th.book.content.populators.ExcursionContentViewDataPopulatorLite;
import uk.co.tui.th.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityViewDataContainer;

/**
 * The Class PackageExtrasContainerPopulator.
 *
 * @author pradeep.as
 */
public class PackageExtrasContainerPopulator implements
   Populator<List<ExtraFacility>, ExtraFacilityViewDataContainer>
{

   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;

   /** The excursion content populator. */
   @Resource(name = "thExcursionContentPopulatorLite")
   private ExcursionContentViewDataPopulatorLite excursionContentPopulatorLite;

   /** The Constant ADULT. */
   private static final String ADULT = "Adult";

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   /**
    * The populator method to populate the ExtraFacilityViewDataContainer value required for page.
    *
    * @param source the source
    * @param target the target
    */
   @Override
   public void populate(final List<ExtraFacility> source,
      final ExtraFacilityViewDataContainer target)
   {
      // for getting the max age of child world care extra.
      String exFacilityCategoryCode = null;
      String extraFacilitySuperCategoryCode = null;
      for (final ExtraFacility extras : source)
      {
         if (extras.getExtraFacilityCategory() != null)
         {
            exFacilityCategoryCode = extras.getExtraFacilityCategory().getCode();
            extraFacilitySuperCategoryCode =
               extras.getExtraFacilityCategory().getSuperCategoryCode();
         }

         populateTransferOptions(extras, target);

         populateDonationExtrasOptions(extras, target, exFacilityCategoryCode);

         populateInfantExtrasOptions(extras, target, exFacilityCategoryCode);

         populateExcursionOptions(extras, target, extraFacilitySuperCategoryCode);

         populateAttractionOptions(extras, target, extraFacilitySuperCategoryCode);
      }

      sortExtraFacilityCategoriesByAlphabaticalOrder(target.getExcursionOptions());
      sortAttractionsByPrice(target.getAttractionOptions());
   }

   /**
    * Populate attraction options.
    *
    * @param extras the extras
    * @param target the target
    * @param extraFacilitySuperCategoryCode the extra facility super category code
    */
   private void populateAttractionOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target, final String extraFacilitySuperCategoryCode)
   {
      if (StringUtils.equalsIgnoreCase(extraFacilitySuperCategoryCode,
         ExtraFacilityConstants.ATTRACTION))
      {
         populateAttractionsView(extras, target.getAttractionOptions());
      }

   }

   /**
    * Populate excursion options.
    *
    * @param extras the extras
    * @param target the target
    * @param extraFacilitySuperCategoryCode the extra facility super category code
    */
   private void populateExcursionOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target, final String extraFacilitySuperCategoryCode)
   {
      if (StringUtils.equalsIgnoreCase(extraFacilitySuperCategoryCode,
         ExtraFacilityConstants.EXCURSION))
      {
         populateAncillaryViewData(extras, target.getExcursionOptions());
      }

   }

   /**
    * Populate infant extras options.
    *
    * @param extras the extras
    * @param target the target
    * @param exFacilityCategoryCode the ex facility category code
    */
   private void populateInfantExtrasOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target, final String exFacilityCategoryCode)
   {
      if (StringUtils.equalsIgnoreCase(exFacilityCategoryCode,
         ExtraFacilityConstants.INFANT_OPTION_CATEGORY))
      {
         populateInfantExtras(extras, target);
      }

   }

   /**
    * Populate donation extras options.
    *
    * @param extras the extras
    * @param target the target
    * @param exFacilityCategoryCode the ex facility category code
    */
   private void populateDonationExtrasOptions(final ExtraFacility donationExtra,
      final ExtraFacilityViewDataContainer target, final String exFacilityCategoryCode)
   {
      if (StringUtils.equalsIgnoreCase(exFacilityCategoryCode,
         ExtraFacilityConstants.DONATION_CATEGORY))
      {
         if (StringUtils.contains(donationExtra.getInventoryCode(), '/'))
         {
            populateDonationExtra(donationExtra, target);
         }
         else
         {
            populateDonationExtras(donationExtra, target);
         }
      }

   }

   /**
    * @param donationExtra
    * @param target
    */
   private void populateDonationExtra(final ExtraFacility donationExtra,
      final ExtraFacilityViewDataContainer target)
   {
      // ATCOM flow where only one extra facility would be there with two
      // prices
      final ExtraFacilityCategoryViewData donationCategoryViewData = target.getDonationOptions();
      donationCategoryViewData.setExtraFacilityCategoryCode(donationExtra
         .getExtraFacilityCategory().getCode());
      donationCategoryViewData.setExtraFacilityGroupCode(BookFlowConstants.EXTRA);
      final ExtraFacilityRestrictions restrictionModel =
         donationExtra.getExtraFacilityRestrictions();

      final ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
      extraViewData.setGroupCode(donationExtra.getExtraFacilityGroup().toString());
      extraViewData.setIncluded(donationExtra.isSelected());
      extraViewData.setSelected(donationExtra.isSelected());
      extraViewData.setDescription(ExtraFacilityConstants.WORLD_CARE_FUND_DISCRIPTION);

      BigDecimal amount = BigDecimal.ZERO;
      for (final Price price : donationExtra.getPrices())
      {
         if (PriceType.ADULT == price.getPriceType())
         {
            // adult
            final BigDecimal rate = price.getRate().getAmount();
            extraViewData.setAdultPrice(rate);
            extraViewData
               .setCurrencyAppendedAdultPrice(getCurrencyAppendedPriceWithDecimalPart(rate));
            final int quantity =
               PassengerUtils.getPassengerCountWithAgeRang(restrictionModel.getMinAge(),
                  restrictionModel.getMaxAge(), getPackageModel().getPassengers());
            amount = rate.multiply(new BigDecimal(quantity));

         }
         else
         {
            final BigDecimal rate = price.getRate().getAmount();
            extraViewData.setChildPrice(rate);
            extraViewData
               .setCurrencyAppendedChildPrice(getCurrencyAppendedPriceWithDecimalPart(rate));
            final int quantity =
               PassengerUtils.getPassengerCountWithAgeRang(restrictionModel.getMinChildAge(),
                  restrictionModel.getmaxChildAge(), getPackageModel().getPassengers());
            amount = amount.add(rate.multiply(new BigDecimal(quantity)));
         }

      }
      extraViewData.setPrice(amount);
      extraViewData.setCurrencyAppendedPrice(getCurrencyAppendedPriceWithDecimalPart(amount));
      donationCategoryViewData
         .setCurrencyAppendedCategoryTotalPrice(getCurrencyAppendedPriceWithDecimalPart(amount));
      extraViewData.setCode(donationExtra.getExtraFacilityCode());
      donationCategoryViewData.getExtraFacilityViewData().add(0, extraViewData);
      donationCategoryViewData.setAvailable(true);
      target.setDonationOptions(donationCategoryViewData);
   }

   /**
    * Populate transfer options.
    *
    * @param extras the extras
    * @param target the target
    */
   private void populateTransferOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target)
   {
      if (BookFlowConstants.TRANSFER_OPTION_CODES.contains(extras.getExtraFacilityCode()))
      {
         populateTransfers(extras, target);
      }
   }

   /**
    * Sort attractions by price.
    *
    * @param attractionOptions the attraction options
    */
   private void sortAttractionsByPrice(
      final Map<String, List<ExtraFacilityCategoryViewData>> attractionOptions)
   {
      for (final Entry<String, List<ExtraFacilityCategoryViewData>> attractionEntry : attractionOptions
         .entrySet())
      {
         Collections.sort(attractionEntry.getValue(),
            new Comparator<ExtraFacilityCategoryViewData>()
            {

               @Override
               public int compare(final ExtraFacilityCategoryViewData firstElement,
                  final ExtraFacilityCategoryViewData secondElement)
               {

                  return firstElement.getExtraFacilityViewData().get(0).getPrice()
                     .compareTo(secondElement.getExtraFacilityViewData().get(0).getPrice());
               }

            });

      }

   }

   /**
    * Sort extra facility categories by alphabatical order.
    *
    * @param excursionOptions the excursion options
    */
   private void sortExtraFacilityCategoriesByAlphabaticalOrder(
      final List<ExtraFacilityCategoryViewData> excursionOptions)
   {
      Collections.sort(excursionOptions, new Comparator<ExtraFacilityCategoryViewData>()
      {

         @Override
         public int compare(final ExtraFacilityCategoryViewData firstElement,
            final ExtraFacilityCategoryViewData secondElement)
         {

            return firstElement.getDisplayName()
               .compareToIgnoreCase(secondElement.getDisplayName());
         }

      });

   }

   /**
    * To populate the transfer options view data.
    *
    * @param extraFacilityModel the extra facility model
    * @param extraFacilityViewDataContainer the extra facility view data container
    */
   @SuppressWarnings(BOXING)
   private void populateTransfers(final ExtraFacility extraFacilityModel,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {

      final ExtraFacilityCategoryViewData transferOptionsViewData =
         extraFacilityViewDataContainer.getTransferOptions();
      transferOptionsViewData.setExtraFacilityCategoryCode(extraFacilityModel
         .getExtraFacilityCategory().getCode());
      transferOptionsViewData.setExtraFacilityGroupCode(extraFacilityModel.getExtraFacilityGroup()
         .toString());

      final ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
      final ExtraFacilityRestrictions restrictionModel =
         extraFacilityModel.getExtraFacilityRestrictions();
      extraViewData.setCode(extraFacilityModel.getExtraFacilityCode());
      extraViewData.setGroupCode(extraFacilityModel.getExtraFacilityGroup().toString());

      extraViewData.setSelected(extraFacilityModel.isSelected());
      extraViewData.setFree(isExtraFree(extraFacilityModel));
      extraViewData.setIncluded(isExtraIncluded(extraFacilityModel));
      BigDecimal totalPrice = BigDecimal.ZERO;
      final BigDecimal unitPrice = extraFacilityModel.getPrices().get(0).getRate().getAmount();

      final int allowedNoOfCarOrTaxies =
         (restrictionModel.getAllowedNoOfCarOrTaxies() == null) ? 0 : restrictionModel
            .getAllowedNoOfCarOrTaxies();
      extraViewData.setDescription(extraFacilityModel.getDescription());

      if (allowedNoOfCarOrTaxies != 0)
      {
         extraViewData.setCurrencyAppendedPerTaxiPrice(getCurrencyWithSignAppendedPrice(
            unitPrice.setScale(TWO, RoundingMode.HALF_UP), currencyResolver.getSiteCurrency()));
         extraViewData.setMaxAllowedPassengers(restrictionModel.getMaxPaxAllowed());
         totalPrice = unitPrice.multiply(BigDecimal.valueOf(allowedNoOfCarOrTaxies));
         extraViewData.setQuantity(allowedNoOfCarOrTaxies);
      }
      else
      {
         totalPrice = unitPrice.multiply(getChargablePaxCount());
         extraViewData.setCurrencyAppendedPerPersonPrice(getCurrencyWithSignAppendedPrice(
            unitPrice.setScale(TWO, RoundingMode.HALF_UP), currencyResolver.getSiteCurrency()));
         extraViewData.setQuantity(getChargablePaxCount().intValue());
      }
      extraViewData.setPrice(totalPrice);
      extraViewData.setCurrencyAppendedPrice(getCurrencyWithSignAppendedPrice(
         totalPrice.setScale(TWO, RoundingMode.HALF_UP), currencyResolver.getSiteCurrency()));

      transferOptionsViewData.getExtraFacilityViewData().add(extraViewData);
      transferOptionsViewData.setAvailable(true);
      extraFacilityViewDataContainer.setTransferOptions(transferOptionsViewData);
   }

   /**
    * Gets the currency appended price.
    *
    * @param price the price
    * @param currencyCode the currency symbol
    * @return the currency appended price
    */
   private static String getCurrencyWithSignAppendedPrice(final BigDecimal price,
      final String currencyCode)
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

   /**
    * Decides if the extra facility is included along with the package.
    *
    * @param extraFacilityModel the extra facility model
    * @return boolean
    */
   private boolean isExtraIncluded(final ExtraFacility extraFacilityModel)
   {
      // for Sky Tours & Twentys - Coach Transfer and for Al A Carte - Taxi
      // Transfer should be included in the package. This flag is mainly used
      // when user checks and un-checks the I don't need transfer to default the transfer option.
      final List<String> coachTransferPreselectProductTypes = Arrays.asList("SKY", "FTW");
      final List<String> taxiTransferPreselectProductTypes = Arrays.asList("ALC");
      final BasePackage packageModel = getPackageModel();
      return StringUtils.equalsIgnoreCase(extraFacilityModel.getSelection().toString(),
         FacilitySelectionType.INCLUDED.toString())
         || isProdcutTypeNotNull(extraFacilityModel, coachTransferPreselectProductTypes,
            taxiTransferPreselectProductTypes, packageModel);
   }

   /**
    * @param extraFacilityModel
    * @param coachTransferPreselectProductTypes
    * @param taxiTransferPreselectProductTypes
    * @param packageModel
    * @return boolean
    */
   private boolean isProdcutTypeNotNull(final ExtraFacility extraFacilityModel,
      final List<String> coachTransferPreselectProductTypes,
      final List<String> taxiTransferPreselectProductTypes, final BasePackage packageModel)
   {
      return packageModel.getProductType() != null
         && compareTransfers(extraFacilityModel, coachTransferPreselectProductTypes,
            taxiTransferPreselectProductTypes, packageModel);
   }

   /**
    * @param extraFacilityModel
    * @param coachTransferPreselectProductTypes
    * @param taxiTransferPreselectProductTypes
    * @param packageModel
    * @return boolean
    */
   private boolean compareTransfers(final ExtraFacility extraFacilityModel,
      final List<String> coachTransferPreselectProductTypes,
      final List<String> taxiTransferPreselectProductTypes, final BasePackage packageModel)
   {
      return (coachTransferPreselectProductTypes.contains(packageModel.getProductType()) && StringUtils
         .equalsIgnoreCase(extraFacilityModel.getExtraFacilityCode(),
            ExtraFacilityConstants.COACH_TRASNFER))
         || (taxiTransferPreselectProductTypes.contains(packageModel.getProductType()) && StringUtils
            .equalsIgnoreCase(extraFacilityModel.getExtraFacilityCode(),
               ExtraFacilityConstants.TAXI_TRASNFER));
   }

   /**
    * Decides if the extra facility is free along with the package.
    *
    * @param extraFacilityModel the extra facility model
    * @return boolean
    */
   private boolean isExtraFree(final ExtraFacility extraFacilityModel)
   {
      return StringUtils.equalsIgnoreCase(extraFacilityModel.getSelection().toString(),
         FacilitySelectionType.FREE.toString());
   }

   /**
    * To get the currency appended price.
    *
    * @param price the price
    * @param currency the currency
    * @return the currency appended price
    */
   private String getCurrencyAppendedPrice(final BigDecimal price, final Currency currency)
   {
      return currency.getSymbol() + price.toString();
   }

   /**
    * To get the total passenger count.
    *
    * @return totalPaxCount
    */
   private BigDecimal getChargablePaxCount()
   {
      return BigDecimal.valueOf(PassengerUtils.getChargeablePaxCount(getPackageModel()
         .getPassengers()));
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
    * Populate donation extras.
    *
    * @param model the model
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateDonationExtras(final ExtraFacility model,
      final ExtraFacilityViewDataContainer target)
   {
      final ExtraFacilityCategoryViewData donationCategoryViewData = target.getDonationOptions();
      donationCategoryViewData.setExtraFacilityCategoryCode(model.getExtraFacilityCategory()
         .getCode());
      donationCategoryViewData.setExtraFacilityGroupCode(BookFlowConstants.EXTRA);
      final ExtraFacilityRestrictions restrictionModel = model.getExtraFacilityRestrictions();
      final BigDecimal rate = model.getPrices().get(0).getRate().getAmount();
      final Currency currency = model.getPrices().get(0).getRate().getCurrency();
      BigDecimal amount = BigDecimal.ZERO;
      if (StringUtils.equals(model.getExtraFacilityCode(),
         ExtraFacilityConstants.WORLD_CARE_FUND_ADULT))
      {
         final ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
         extraViewData.setGroupCode(model.getExtraFacilityGroup().toString());
         extraViewData.setIncluded(model.isSelected());
         extraViewData.setSelected(model.isSelected());
         extraViewData.setDescription(ExtraFacilityConstants.WORLD_CARE_FUND_DISCRIPTION);
         extraViewData.setMaxChildAge(restrictionModel.getMaxAge());
         extraViewData.setMinAge(restrictionModel.getMinAge());
         extraViewData.setAdultPrice(rate);
         extraViewData.setCurrencyAppendedAdultPrice(getCurrencyAppendedPriceWithDecimalPart(rate));
         final int quantity =
            PassengerUtils.getPassengerCountWithAgeRang(restrictionModel.getMinAge(),
               restrictionModel.getMaxAge(), getPackageModel().getPassengers());
         amount = rate.multiply(new BigDecimal(quantity));
         extraViewData.setPrice(amount);
         extraViewData.setCurrencyAppendedPrice(getCurrencyAppendedPriceWithDecimalPart(amount));
         donationCategoryViewData
            .setCurrencyAppendedCategoryTotalPrice(getCurrencyAppendedPriceWithDecimalPart(amount));
         extraViewData.setCode(model.getExtraFacilityCode());
         donationCategoryViewData.getExtraFacilityViewData().add(0, extraViewData);
         donationCategoryViewData.setAvailable(true);
         target.setDonationOptions(donationCategoryViewData);
      }
      if (StringUtils.equals(model.getExtraFacilityCode(),
         ExtraFacilityConstants.WORLD_CARE_FUND_CHILD))
      {
         final ExtraFacilityViewData childExtraViewData = new ExtraFacilityViewData();
         childExtraViewData.setCode(model.getExtraFacilityCode());
         childExtraViewData.setGroupCode(model.getExtraFacilityGroup().toString());
         childExtraViewData.setIncluded(model.isSelected());
         childExtraViewData.setSelected(model.isSelected());
         donationCategoryViewData.getExtraFacilityViewData().add(1, childExtraViewData);
         childExtraViewData.setDescription(ExtraFacilityConstants.WORLD_CARE_FUND_DISCRIPTION);
         for (final ExtraFacilityViewData extraViewData : donationCategoryViewData
            .getExtraFacilityViewData())
         {
            childExtraViewData
               .setCurrencyAppendedAdultPrice(getCurrencyAppendedPriceWithDecimalPart(rate));
            final int childrens =
               PassengerUtils.getPassengerCountWithAgeRang(restrictionModel.getMinAge(),
                  restrictionModel.getMaxAge(), getPackageModel().getPassengers());
            amount = extraViewData.getPrice().add(rate.multiply(new BigDecimal(childrens)));
            extraViewData.setPrice(amount);
            extraViewData.setCurrencyAppendedPrice(getCurrencyAppendedPriceWithDecimalPart(amount));
            donationCategoryViewData
               .setCurrencyAppendedCategoryTotalPrice(getCurrencyAppendedPriceWithDecimalPart(amount));
            extraViewData.setCurrencyAppendedChildPrice(getCurrencyAppendedChildPrice(rate,
               currency));
            extraViewData.setChildPrice(rate);
            return;
         }
      }
   }

   /**
    * Populates the Infant Extras.
    *
    * @param extraFacilityModel the extra facility model
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateInfantExtras(final ExtraFacility extraFacilityModel,
      final ExtraFacilityViewDataContainer target)
   {
      final BasePackage packageModel = getPackageModel();
      final List<Passenger> passengerList = packageModel.getPassengers();
      final int selectedQuantity = getQuantity(extraFacilityModel);
      final List<Integer> childAges = PassengerUtils.getChildAges(passengerList);
      int noOfInfants = 0;
      for (final Integer ages : childAges)
      {
         if (ages.intValue() <= BookFlowConstants.THREE)
         {
            noOfInfants = noOfInfants + 1;
         }
      }

      if (checkExtraFacility(extraFacilityModel, target, packageModel))
      {
         return;
      }

      populateInfantViewData(extraFacilityModel, target, packageModel, selectedQuantity,
         noOfInfants);
   }

   /**
    * Populate infant view data.
    *
    * @param extraFacilityModel the extra facility model
    * @param target the target
    * @param packageModel the package model
    * @param selectedQuantity the selected quantity
    * @param noOfInfants the no of infants
    */
   @SuppressWarnings(BOXING)
   private void populateInfantViewData(final ExtraFacility extraFacilityModel,
      final ExtraFacilityViewDataContainer target, final BasePackage packageModel,
      final int selectedQuantity, final int noOfInfants)
   {
      if (noOfInfants >= 1)
      {

         final int noOfNights = packageModel.getDuration();
         final int avlQuantity = extraFacilityModel.getQuantity();
         final ExtraFacilityCategoryViewData infantExtraFacilityViewData =
            target.getInfantOptions();
         infantExtraFacilityViewData.setExtraFacilityCategoryCode(extraFacilityModel
            .getExtraFacilityCategory().getCode());
         infantExtraFacilityViewData.setExtraFacilityGroupCode(extraFacilityModel
            .getExtraFacilityGroup().toString());

         final ExtraFacilityViewData extraFacilityViewData = new ExtraFacilityViewData();

         extraFacilityViewData.setCode(extraFacilityModel.getExtraFacilityCode());
         extraFacilityViewData.setGroupCode(extraFacilityModel.getExtraFacilityGroup().toString());
         if (avlQuantity <= noOfInfants)
         {
            extraFacilityViewData.setQuantity(avlQuantity);
         }
         else
         {
            extraFacilityViewData.setQuantity(noOfInfants);
         }
         extraFacilityViewData.setSelectedQuantity(selectedQuantity);
         final BigDecimal unitPrice = extraFacilityModel.getPrices().get(0).getRate().getAmount();
         final Currency currency = extraFacilityModel.getPrices().get(0).getRate().getCurrency();
         final BigDecimal noOfNightsInBigDecimal = new BigDecimal(noOfNights);
         final BigDecimal pricePerNight =
            unitPrice.divide(noOfNightsInBigDecimal, TWO, RoundingMode.CEILING);
         final BigDecimal noOfselectedQuantity = new BigDecimal(selectedQuantity);
         final BigDecimal totalPrice = unitPrice.multiply(noOfselectedQuantity);

         extraFacilityViewData.setCurrencyAppendedPerPersonPrice(getCurrencyAppendedPrice(
            unitPrice, currency));
         extraFacilityViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(totalPrice,
            currency));
         extraFacilityViewData.setCurrencyAppendedPricePerNight(getCurrencyAppendedPricePerNight(
            pricePerNight, currency));
         extraFacilityViewData.setDescription(extraFacilityModel.getDescription());
         extraFacilityViewData.setSelected(extraFacilityModel.isSelected());
         if (!extraFacilityModel.isSelected())
         {
            extraFacilityViewData.setSelectedQuantity(0);
         }

         infantExtraFacilityViewData.getExtraFacilityViewData().add(extraFacilityViewData);
         infantExtraFacilityViewData.setAvailable(true);
         target.setInfantOptions(infantExtraFacilityViewData);
      }
   }

   /**
    * Populates the Attractions.
    *
    * @param extraFacilityModel the extra facility model
    * @param ancillaryCategoryViewData the ancillary category view data
    */
   @SuppressWarnings(BOXING)
   private void populateAncillaryViewData(final ExtraFacility extraFacilityModel,
      final List<ExtraFacilityCategoryViewData> ancillaryCategoryViewData)
   {

      ExtraFacilityCategoryViewData targetAncillaryCategoryView = null;
      boolean viewDataNotPresnt = true;
      for (final ExtraFacilityCategoryViewData eachancillaryViewData : ancillaryCategoryViewData)
      {
         if (StringUtils.equals(eachancillaryViewData.getExtraFacilityCategoryCode(),
            extraFacilityModel.getExtraFacilityCategory().getCode()))
         {
            targetAncillaryCategoryView = eachancillaryViewData;
            viewDataNotPresnt = false;
            break;
         }

      }
      if (viewDataNotPresnt)
      {
         targetAncillaryCategoryView = new ExtraFacilityCategoryViewData();
         populateExcursionContentViewDataIfNotPresent(extraFacilityModel,
            targetAncillaryCategoryView);

      }

      targetAncillaryCategoryView.setAvailable(true);
      targetAncillaryCategoryView.setExtraFacilityCategoryCode(extraFacilityModel
         .getExtraFacilityCategory().getCode());
      targetAncillaryCategoryView.setExtraFacilityGroupCode(extraFacilityModel
         .getExtraFacilityGroup().toString());
      targetAncillaryCategoryView.setDisplayName(WordUtils.capitalizeFully(StringUtils
         .lowerCase(extraFacilityModel.getExtraFacilityCategory().getCode())));
      targetAncillaryCategoryView.setSuperCategoryCode(extraFacilityModel
         .getExtraFacilityCategory().getSuperCategoryCode());
      targetAncillaryCategoryView.setAliasSuperCategoryCode(extraFacilityModel
         .getExtraFacilityCategory().getAliasSuperCategoryCode());
      targetAncillaryCategoryView.setDescription(extraFacilityModel.getExtraFacilityCategory()
         .getDescription());
      final BasePackage packageModel = getPackageModel();
      final ExtraFacilityViewData extraFacilityViewData = new ExtraFacilityViewData();
      extraFacilityViewData.setCode(extraFacilityModel.getExtraFacilityCode());
      extraFacilityViewData.setGroupCode(extraFacilityModel.getExtraFacilityGroup().toString());
      final ExtraFacilityRestrictions restrictionModel =
         extraFacilityModel.getExtraFacilityRestrictions();
      extraFacilityViewData.setQuantity(PassengerUtils.getPassengerCountWithAgeRang(
         restrictionModel.getMinAge(), restrictionModel.getMaxAge(), packageModel.getPassengers()));
      final BigDecimal unitPrice = extraFacilityModel.getPrices().get(0).getRate().getAmount();
      final Currency currency = extraFacilityModel.getPrices().get(0).getRate().getCurrency();
      extraFacilityViewData.setPrice(unitPrice);
      final String paxType = extraFacilityModel.getPrices().get(0).getPriceType().toString();
      extraFacilityViewData.setPaxType(WordUtils.capitalize(StringUtils.lowerCase(paxType)));
      extraFacilityViewData
         .setMaxAge(extraFacilityModel.getExtraFacilityRestrictions().getMaxAge());
      extraFacilityViewData
         .setMinAge(extraFacilityModel.getExtraFacilityRestrictions().getMinAge());
      extraFacilityViewData.setCurrencyAppendedAdultPrice(getCurrencyAppendedPrice(unitPrice,
         currency));
      extraFacilityViewData.setCurrencyAppendedChildPrice(getCurrencyAppendedChildPrice(unitPrice,
         currency));

      extraFacilityViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(unitPrice, currency));
      extraFacilityViewData.setDescription(extraFacilityModel.getDescription());
      extraFacilityViewData.setSelected(extraFacilityModel.isSelected());
      extraFacilityViewData.setSelectedQuantity(getQuantity(extraFacilityModel));
      addExtraFacilityViewDataByPaxType(targetAncillaryCategoryView, extraFacilityViewData);

      addAncillaryCategoryViewDataIfNotPresent(ancillaryCategoryViewData,
         targetAncillaryCategoryView, viewDataNotPresnt);
   }

   /**
    * Populate excursion content view data if not present.
    *
    * @param extraFacilityModel the extra facility model
    * @param targetAncillaryCategoryView the target ancillary category view
    */
   private void populateExcursionContentViewDataIfNotPresent(
      final ExtraFacility extraFacilityModel,
      final ExtraFacilityCategoryViewData targetAncillaryCategoryView)
   {
      if (excursionOrAttraction(extraFacilityModel.getExtraFacilityCategory()
         .getSuperCategoryCode()))
      {
         excursionContentPopulatorLite.populate(extraFacilityModel, targetAncillaryCategoryView);
      }
   }

   /**
    * Excursion or attraction.
    *
    * @param superCategoryCode the super category code
    * @return true, if successful
    */
   private boolean excursionOrAttraction(final String superCategoryCode)
   {
      return StringUtils.equalsIgnoreCase(superCategoryCode, ExtraFacilityConstants.EXCURSION)
         || StringUtils.equalsIgnoreCase(superCategoryCode, ExtraFacilityConstants.ATTRACTION);
   }

   /**
    * Adds the extra facility view data by pax type.
    *
    * @param targetAncillaryCategoryView the target ancillary category view
    * @param extraFacilityViewData the extra facility view data
    */
   private void addExtraFacilityViewDataByPaxType(
      final ExtraFacilityCategoryViewData targetAncillaryCategoryView,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      if (StringUtils.equalsIgnoreCase(extraFacilityViewData.getPaxType(), ADULT))
      {
         targetAncillaryCategoryView.getExtraFacilityViewData().add(0, extraFacilityViewData);
      }
      else
      {
         targetAncillaryCategoryView.getExtraFacilityViewData().add(extraFacilityViewData);
      }
   }

   /**
    * Adds the ancillary category view data if not present.
    *
    * @param ancillaryCategoryViewData the ancillary category view data
    * @param targetAncillaryCategoryView the target ancillary category view
    * @param viewDataNotPresnt the view data not presnt
    */
   private void addAncillaryCategoryViewDataIfNotPresent(
      final List<ExtraFacilityCategoryViewData> ancillaryCategoryViewData,
      final ExtraFacilityCategoryViewData targetAncillaryCategoryView,
      final boolean viewDataNotPresnt)
   {
      if (viewDataNotPresnt)
      {
         ancillaryCategoryViewData.add(targetAncillaryCategoryView);
      }
   }

   /**
    * Populate attractions view.
    *
    * @param extraFacilityModel the extra facility model
    * @param attractionOptions the attraction options
    */
   private void populateAttractionsView(final ExtraFacility extraFacilityModel,
      final Map<String, List<ExtraFacilityCategoryViewData>> attractionOptions)
   {
      final String aliasSuperCategory =
         extraFacilityModel.getExtraFacilityCategory().getAliasSuperCategoryCode();
      if (CollectionUtils.isEmpty(attractionOptions.get(aliasSuperCategory)))
      {
         attractionOptions.put(aliasSuperCategory, new ArrayList<ExtraFacilityCategoryViewData>());
      }
      populateAncillaryViewData(extraFacilityModel, attractionOptions.get(aliasSuperCategory));
   }

   /**
    * Checks whether the extra facility is present with the same quantity in package model or not.
    * If yes it returns true otherwise false
    *
    * @param extraFacilityModel the extra facility model
    * @param target the target
    * @param packageModel the package model
    * @return true, if successful
    */
   @SuppressWarnings(BOXING)
   private boolean checkExtraFacility(final ExtraFacility extraFacilityModel,
      final ExtraFacilityViewDataContainer target, final BasePackage packageModel)
   {
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData = target.getInfantOptions();
      boolean isExtraFacilityPresent = false;
      for (final ExtraFacilityCategory extraFacilityCategoryModel : packageModel
         .getExtraFacilityCategories())
      {
         if (StringUtils.equalsIgnoreCase(extraFacilityCategoryModel.getCode(),
            extraFacilityCategoryViewData.getExtraFacilityCategoryCode()))
         {
            isExtraFacilityPresent =
               isExtraFacilityPresent(extraFacilityModel, extraFacilityCategoryViewData,
                  extraFacilityCategoryModel);
         }

      }
      return isExtraFacilityPresent;
   }

   /**
    * Checks if is extra facility present.
    *
    * @param extraFacilityModel the extra facility model
    * @param extraFacilityCategoryViewData the extra facility category view data
    * @param extraFacilityCategoryModel the extra facility category model
    * @return true, if is extra facility present
    */
   private boolean isExtraFacilityPresent(final ExtraFacility extraFacilityModel,
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final ExtraFacilityCategory extraFacilityCategoryModel)
   {
      boolean isExtraFacilityPresent = false;
      for (final ExtraFacility extra : extraFacilityCategoryModel.getExtraFacilities())
      {
         for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
            .getExtraFacilityViewData())
         {
            if (checkIfCodeAndQuantityMatches(extraFacilityModel, extra, extraFacilityViewData))
            {
               isExtraFacilityPresent = true;
            }

         }

      }
      return isExtraFacilityPresent;
   }

   /**
    * Check if code and quantity matches.
    *
    * @param extraFacilityModel the extra facility model
    * @param extra the extra
    * @param extraFacilityViewData the extra facility view data
    * @return true, if successful
    */
   @SuppressWarnings(BOXING)
   private boolean checkIfCodeAndQuantityMatches(final ExtraFacility extraFacilityModel,
      final ExtraFacility extra, final ExtraFacilityViewData extraFacilityViewData)
   {
      return StringUtils.equalsIgnoreCase(extraFacilityViewData.getCode(),
         extraFacilityModel.getExtraFacilityCode())
         && StringUtils.equalsIgnoreCase(extraFacilityViewData.getCode(),
            extra.getExtraFacilityCode())
         && extra.getPrices().get(0).getQuantity() == extraFacilityViewData.getSelectedQuantity();
   }

   /**
    * Gets the Currency Appended Price Per Night.
    *
    * @param pricePerNight the price per night
    * @param currency the currency
    * @return the currency appended price per night
    */
   private String getCurrencyAppendedPricePerNight(final BigDecimal pricePerNight,
      final Currency currency)
   {
      return currency.getSymbol() + pricePerNight;
   }

   /**
    * Gets the currency appended child price.
    *
    * @param price the price
    * @param currency the currency
    * @return the currency appended child price
    */
   private String getCurrencyAppendedChildPrice(final BigDecimal price, final Currency currency)
   {
      if (price.intValue() >= BookFlowConstants.ONE)
      {
         return currency.getSymbol() + price.intValue();
      }
      // TODO temparary fix need to revist, model giving the 0.5 instead of
      // 0.50
      final BigDecimal val = BigDecimal.valueOf(price.doubleValue());
      return String.valueOf(val.multiply(BigDecimal.valueOf(BookFlowConstants.HUNDRED)).intValue());
   }

   private int getQuantity(final ExtraFacility extraFacilityModel)
   {
      return extraFacilityModel.getPrices().get(0).getQuantity() != null ? extraFacilityModel
         .getPrices().get(0).getQuantity().intValue() : 0;
   }

   /**
    * Gets the currency appended price with decimal part.
    *
    * @param price the price
    * @return the currency appended price with decimal part
    */
   private String getCurrencyAppendedPriceWithDecimalPart(final BigDecimal price)
   {
      return CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency())
         + price.setScale(TWO, BigDecimal.ROUND_HALF_UP).toString();

   }

}
