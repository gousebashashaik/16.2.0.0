/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.BaggageExtraFacilityRestrictions;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityRestrictions;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.th.book.constants.BookFlowConstants;
import uk.co.tui.th.book.view.data.BaggageExtraFacilityViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityViewData;

/**
 * @author madhumathi.m
 *
 */
public class ExtraFacilityViewDataPopulator implements
   Populator<ExtraFacility, ExtraFacilityViewData>
{

   /** The summary panel property reader. */
   @Resource
   private PropertyReader summaryPanelPropertyReader;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   @SuppressWarnings(BookFlowConstants.BOXING)
   @Override
   public void populate(final ExtraFacility source, final ExtraFacilityViewData target)
   {
      target.setCode(source.getExtraFacilityCode());
      target.setGroupCode(source.getExtraFacilityGroup().toString());
      target.setIncluded(isExtraIncluded(source));
      target.setFree(isExtraFree(source));
      target.setSelected(source.isSelected());
      target.setDescription(populateDescrption(source));
      target.setSelection(StringUtils.capitalize(source.getSelection().toString().toLowerCase()));
      target.setCategoryCode(source.getExtraFacilityCategory().getCode());
      target.setAvailableQuantity(source.getQuantity() != null ? source.getQuantity() : 0);
      target.setPositionIndex(BookFlowConstants.SEAT_POSITION_INDEX.indexOf(source
         .getExtraFacilityCode()));
      if (StringUtils.equalsIgnoreCase(source.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
      {
         populateBaggageExtraFacilityViewData(source, target);
      }
      else
      {
         populateOtherExtraFacilityViewData(source, target);
      }
      populateSummaryDescription(source, target);
   }

   /**
    * @param source
    * @param target
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void populateBaggageExtraFacilityViewData(final ExtraFacility source,
      final ExtraFacilityViewData target)
   {
      final BaggageExtraFacilityViewData bagViewData = (BaggageExtraFacilityViewData) target;
      final BaggageExtraFacility bagModel = (BaggageExtraFacility) source;
      final BaggageExtraFacilityRestrictions restrictionModel =
         bagModel.getBaggageExtraFacilityRestrictions();
      bagViewData.setPrice(bagModel.getPrice().getRate().getAmount());
      if (SyntacticSugar.isNotNull(restrictionModel))
      {
         bagViewData.setMaxPiecePerPerson(restrictionModel.getMaxPieceperPax());
         bagViewData.setMaxWeightPerPiece(restrictionModel.getMaxWeightPerPiece());
         bagViewData.setInfantWeight(restrictionModel.getInfantWeight());
         bagViewData.setBaseWeight(restrictionModel.getBaseWeight());
         bagViewData.setWeightCode(String.valueOf(bagModel.getBaggageWeight()));
         bagViewData.setInfantBaggageWeightDescription(getDescription("INFANT_BAG_DESCRIPTION"));
         bagViewData.setInfantBaggageWeightSelection("Included");
      }
   }

   /**
    * @param source
    * @param target
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void populateOtherExtraFacilityViewData(final ExtraFacility source,
      final ExtraFacilityViewData target)
   {
      final ExtraFacilityRestrictions restrictionModel = source.getExtraFacilityRestrictions();
      final Price priceModel = getValidPrices(source);
      target.setAdultPrice(source.getPrices().get(0).getRate().getAmount());
      final Integer quantity = priceModel.getQuantity();
      populatePrice(target, priceModel);
      populateAgeRestriction(target, restrictionModel);
      populatePaxType(source, target);
      if (StringUtil.isNotEquals(source.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.DONATION_CATEGORY))
      {
         final int quant = (quantity == null) ? 0 : quantity;
         target.setQuantity(quant);
         target.setSelectedQuantity(quant);
      }
      populateQuantity(source, target);
   }

   /**
    * Gets the prices which has more then 0 quantity.
    *
    * @param source the ExtraFacility
    * @return the valid Price
    */
   private Price getValidPrices(final ExtraFacility source)
   {
      Price finalPriceModel = source.getPrices().get(0);
      if (StringUtils.equalsIgnoreCase(source.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.INFANT_OPTION_CATEGORY))
      {
         for (final Price priceModel : source.getPrices())
         {
            if (getPriceModel(priceModel))
            {
               finalPriceModel = priceModel;

            }
         }
      }
      return finalPriceModel;
   }

   /**
    * Gets the price model.
    *
    * @param priceModel the price model
    * @return the price model
    */
   private boolean getPriceModel(final Price priceModel)
   {
      return SyntacticSugar.isNotNull(priceModel.getQuantity())
         && priceModel.getQuantity().intValue() != 0;
   }

   /**
    * @param source
    * @param target
    */
   private void populatePaxType(final ExtraFacility source, final ExtraFacilityViewData target)
   {
      if (StringUtils.equalsIgnoreCase(source.getExtraFacilityCategory().getSuperCategoryCode(),
         ExtraFacilityConstants.EXCURSION)
         || StringUtils.equalsIgnoreCase(source.getExtraFacilityCategory().getSuperCategoryCode(),
            ExtraFacilityConstants.ATTRACTION))
      {
         final String paxType = source.getPrices().get(0).getPriceType().toString();
         target.setPaxType(WordUtils.capitalize(StringUtils.lowerCase(paxType)));

      }
   }

   /**
    * @param source
    * @param target
    */
   @SuppressWarnings("boxing")
   private void populateQuantity(final ExtraFacility source, final ExtraFacilityViewData target)
   {
      final Price adultPriceModel = source.getPrices().get(0);
      if (source.getPrices().size() > 1)
      {
         final Price childPriceModel = source.getPrices().get(1);
         if (childPriceModel.getQuantity() != null)
         {
            target.setSelectedQuantity(target.getQuantity() + childPriceModel.getQuantity());
            target.setQuantity(target.getQuantity() + childPriceModel.getQuantity());
         }
         target.setChildPrice(childPriceModel.getRate().getAmount());
      }
      else if (adultPriceModel.getQuantity() != null)
      {
         target.setSelectedQuantity(adultPriceModel.getQuantity());
      }
      populateChildPrice(source, target);
   }

   /**
    * @param source
    * @param target
    */
   private void populateChildPrice(final ExtraFacility source, final ExtraFacilityViewData target)
   {
      if (target.getChildPrice().compareTo(BigDecimal.ZERO) == 0)
      {
         target.setChildPrice(source.getPrices().get(0).getRate().getAmount());
      }
   }

   /**
    * @param target
    * @param restrictionModel
    */
   @SuppressWarnings("boxing")
   private void populateAgeRestriction(final ExtraFacilityViewData target,
      final ExtraFacilityRestrictions restrictionModel)
   {
      target
         .setMaxChildAge(restrictionModel.getMaxAge() != null ? restrictionModel.getMaxAge() : 0);
      target.setMinAge(restrictionModel.getMinAge() != null ? restrictionModel.getMinAge() : 0);
   }

   /**
    * @param target
    * @param priceModel
    */
   private void populatePrice(final ExtraFacilityViewData target, final Price priceModel)
   {
      final Money moneyModel = priceModel.getAmount();
      if (moneyModel != null && moneyModel.getAmount() != null)
      {
         final BigDecimal totalPrice = moneyModel.getAmount();
         target.setPrice(totalPrice);
         target.setCurrencyAppendedPrice(getCurrencyAppendedPrice(
            totalPrice.setScale(TWO, RoundingMode.HALF_UP),
            CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency())));
      }
   }

   /**
    * @param source
    *
    */
   private void populateSummaryDescription(final ExtraFacility source,
      final ExtraFacilityViewData target)
   {

      boolean description = false;
      description = populateFlightExtraDescriptions(source, target, description);
      description = populateLateCheckoutDescriptions(source, target, description);
      description = populateTransferOptionDescriptions(source, target, description);
      description = populateInfantEquipmentDescriptions(source, target, description);
      description = populateKidsActivityDescriptions(source, target, description);
      if (!description)
      {
         target.setSummaryDescription(target.getDescription());
      }
   }

   /**
    * Populate infant equipment descriptions.
    *
    * @param source the source
    * @param target the target
    * @param description
    * @return description
    */
   private boolean populateInfantEquipmentDescriptions(final ExtraFacility source,
      final ExtraFacilityViewData target, final boolean description)
   {
      if (StringUtils.equalsIgnoreCase(source.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.INFANT_OPTION_CATEGORY))
      {
         target.setSummaryDescription(getDescription(source.getExtraFacilityCategory().getCode(),
            source.getDescription(), Integer.toString(target.getSelectedQuantity())));
         return true;
      }

      return description;
   }

   /**
    * Populate infant equipment descriptions.
    *
    * @param source the source
    * @param target the target
    * @param description
    * @return description
    */
   private boolean populateKidsActivityDescriptions(final ExtraFacility source,
      final ExtraFacilityViewData target, final boolean description)
   {
      if (ExtraFacilityConstants.KIDS_ACTIVITY_CATEGORY_CODES.contains(source
         .getExtraFacilityCategory().getCode()))
      {
         target.setSummaryDescription(getDescription(source.getExtraFacilityCategory().getCode(),
            source.getDescription(), Integer.toString(target.getQuantity())));
         return true;
      }

      return description;
   }

   /**
    * To set the descriptions related to TransferOptions.
    *
    * @param source
    * @param target
    * @param description
    * @return description
    */
   private boolean populateTransferOptionDescriptions(final ExtraFacility source,
      final ExtraFacilityViewData target, final boolean description)
   {
      if (BookFlowConstants.TRANSFER_OPTION_CODES.contains(source.getExtraFacilityCode())
         || StringUtils.equalsIgnoreCase(source.getExtraFacilityCategory().getSuperCategoryCode(),
            ExtraFacilityConstants.CAR_HIRE))
      {
         target.setSummaryDescription(getTransferDescription(source));
         return true;
      }
      return description;
   }

   /**
    * To set the descriptions related to LateCheckout.
    *
    * @param source
    * @param target
    * @param description
    * @return description
    */
   private boolean populateLateCheckoutDescriptions(final ExtraFacility source,
      final ExtraFacilityViewData target, final boolean description)
   {
      if (StringUtils.equalsIgnoreCase(source.getExtraFacilityCode(), ExtraFacilityConstants.LCD))
      {
         target.setSummaryDescription(getLateCheckoutDescription(source, target));
         return true;
      }
      return description;
   }

   /**
    * To set the descriptions related to FlightExtras.
    *
    * @param source
    * @param target
    * @param description
    * @return description
    */
   private boolean populateFlightExtraDescriptions(final ExtraFacility source,
      final ExtraFacilityViewData target, final boolean description)
   {
      final int countFlightExtras =
         StringUtils.countMatches(getStaticContent().get("FLIGHTEXTRAS"), source
            .getExtraFacilityGroup().toString());
      if (countFlightExtras >= 1 && CollectionUtils.isNotEmpty(source.getPassengers()))
      {
         target.setSummaryDescription(getFlightExtrasDescription(source));
         return true;
      }
      return description;
   }

   /**
    * Gets the flight extras description.
    *
    * @param source the source
    * @return the flight extras description
    */
   private String getFlightExtrasDescription(final ExtraFacility source)
   {
      if (StringUtils.equalsIgnoreCase(source.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
      {
         return getDescription(ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE,
            String.valueOf(((BaggageExtraFacility) source).getBaggageWeight()),
            Integer.toString(source.getPassengers().size()));
      }
      else
      {
         return getDescription("EXTRADESCRIPTION", source.getDescription(),
            Integer.toString(source.getPassengers().size()));
      }
   }

   /**
    * Gets the late checkout description.
    *
    * @param source the source
    * @param target the target
    * @return the late checkout description
    */
   private String getLateCheckoutDescription(final ExtraFacility source,
      final ExtraFacilityViewData target)
   {
      if (target.getSelectedQuantity() <= 1)
      {
         return getDescription(source.getExtraFacilityCode(),
            Integer.toString(target.getSelectedQuantity()));
      }
      else
      {
         return getDescription("LCDS", Integer.toString(target.getSelectedQuantity()));
      }
   }

   /**
    * Decides if the extra facility is included along with the package.
    *
    * @param extraFacilityModel
    * @return boolean
    */
   private boolean isExtraIncluded(final ExtraFacility extraFacilityModel)
   {
      return extraFacilityModel.getSelection() == FacilitySelectionType.INCLUDED;
   }

   /**
    * Decides if the extra facility is free along with the package.
    *
    * @param extraFacilityModel
    * @return boolean
    */
   private boolean isExtraFree(final ExtraFacility extraFacilityModel)
   {
      return extraFacilityModel.getSelection() == FacilitySelectionType.FREE;
   }

   /**
    * Gets the currency appended child price.
    *
    * @param price the price
    * @param currencySymbol the currency symbol
    * @return the currency appended child price
    */
   private String getCurrencyAppendedPrice(final BigDecimal price, final String currencySymbol)
   {
      if (price.intValue() >= BookFlowConstants.ONE)
      {
         return currencySymbol + price.setScale(TWO, RoundingMode.HALF_UP).toString();
      }
      return StringUtils.EMPTY;
   }

   /**
    * Populate descrption.
    *
    * @param source the source
    * @return the string
    */
   private String populateDescrption(final ExtraFacility source)
   {

      final int countCategoryCodes =
         StringUtils.countMatches(getDescriptionFromProperties("CATEGORY_CODES"), source
            .getExtraFacilityCategory().getCode());
      if (countCategoryCodes >= 1)
      {
         return getDescription(source.getExtraFacilityCategory().getCode());
      }

      return source.getDescription();
   }

   /**
    * Gets the description from property reader.
    *
    * @param key the key
    * @param value the value
    * @return the description
    */
   private String getDescriptionFromProperties(final String key, final String... value)
   {
      if (key != null)
      {
         if (SyntacticSugar.isNull(value))
         {
            return summaryPanelPropertyReader.getValue(key);
         }
         return summaryPanelPropertyReader.substitute(key, value);
      }
      return null;
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
      if (StringUtils.isNotEmpty(key))
      {
         final String summaryContent = getStaticContent().get(key);
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
    * Gets the transfer/car hire extra facility description
    *
    * @param source
    * @return description
    */
   private String getTransferDescription(final ExtraFacility source)
   {
      // TODO : This needs to be looked up
      if (source.getSelection() == FacilitySelectionType.FREE
         || source.getSelection() == FacilitySelectionType.INCLUDED)
      {
         return getDescription(source.getExtraFacilityCode());
      }
      else
      {
         if (BookFlowConstants.TRANSFER_OPTION_CODES.contains(source.getExtraFacilityCode()))
         {
            return getDescription(source.getExtraFacilityCode() + "S", source.getPrices().get(0)
               .getQuantity().toString());
         }
         else
         {
            return getDescription("CAR_HIRE", source.getDescription(), source.getPrices().get(0)
               .getQuantity().toString());
         }
      }
   }

   /**
    * Gets the static content.
    *
    * @return the static content
    */
   private Map<String, String> getStaticContent()
   {
      return staticContentServ.getSummaryContents();
   }
}
