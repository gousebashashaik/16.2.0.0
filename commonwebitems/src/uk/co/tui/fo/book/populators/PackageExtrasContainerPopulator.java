/**
 *
 */
package uk.co.tui.fo.book.populators;

import static uk.co.tui.book.constants.ExtraFacilityConstants.TRANSFER;
import static uk.co.tui.fo.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityRestrictions;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.PriceType;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.fo.book.constants.BookFlowConstants;
import uk.co.tui.fo.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityViewDataContainer;

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
      for (final ExtraFacility extras : source)
      {
         if (extras.getExtraFacilityCategory() != null)
         {
            exFacilityCategoryCode = extras.getExtraFacilityCategory().getCode();
         }
         populateTransferOptions(extras, target);

         populateDonationExtrasOptions(extras, target, exFacilityCategoryCode);
      }

      sortExtraFacilityCategoriesByAlphabaticalOrder(target.getExcursionOptions());
      sortAttractionsByPrice(target.getAttractionOptions());
      sortTransferByDescription(target.getTransferOptions().getExtraFacilityViewData());
   }

   /**
    * Sort Transfer by description.
    *
    * @param extraFacility
    */
   private void sortTransferByDescription(final List<ExtraFacilityViewData> extraFacility)
   {
      Collections.sort(extraFacility, new Comparator<ExtraFacilityViewData>()
      {

         @Override
         public int compare(final ExtraFacilityViewData firstElement,
            final ExtraFacilityViewData secondElement)
         {

            return firstElement.getDescription()
               .compareToIgnoreCase(secondElement.getDescription());
         }

      });

   }

   /**
    * Populate donation extras options.
    *
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
      if (StringUtils.equalsIgnoreCase(extras.getExtraFacilityCategory().getCode(), TRANSFER))
      {
         populateTransfers(extras, target);
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

      extraViewData.setDescription(getShortDescription(extraFacilityModel.getDescription()));

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
    * @param description
    * @return string
    */
   private String getShortDescription(final String description)
   {
      final String[] desc = description.split("To");
      if (desc.length > 1)
      {
         return desc[1];
      }
      else
      {
         return "";
      }
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
      // when user checks and un-checks the I don't need transfer to default
      // the transfer option.
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

}
