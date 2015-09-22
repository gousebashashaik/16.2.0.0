/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.constants.BookFlowConstants;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.manage.viewdata.BaggageExtraFacilityViewData;
import uk.co.tui.manage.viewdata.ExtraFacilityCategoryViewData;
import uk.co.tui.manage.viewdata.ExtraFacilityViewData;
import uk.co.tui.manage.viewdata.PackageViewData;
import uk.co.tui.manage.viewdata.PassengerViewData;
import uk.co.tui.manage.viewdata.PaxCompositionViewData;

/**
 * @author premkumar.nd
 *
 */
public class ExtraFacilityCategoryViewDataPopulator implements
   Populator<PackageHoliday, PackageViewData>
{
   @Resource
   private CurrencyResolver currencyResolver;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The summary panel property reader. */
   @Resource
   private PropertyReader summaryPanelPropertyReader;

   private static final int TWO = 2;

   @Override
   public void populate(final PackageHoliday source, final PackageViewData target)
      throws ConversionException
   {
      populateIntegratedExtras(source, target);
   }

   public void populateIntegratedExtras(final PackageHoliday source, final PackageViewData target)
   {
      final List<PassengerViewData> pkgPassengers = target.getPassenger();

      final List<ExtraFacilityCategoryViewData> categoryViewDatas =
         new ArrayList<ExtraFacilityCategoryViewData>();
      for (final ExtraFacilityCategory extraFacilityCategory : source.getExtraFacilityCategories())
      {
         final ExtraFacilityCategoryViewData extraFacilityCategoryViewData =
            new ExtraFacilityCategoryViewData();
         extraFacilityCategoryViewData.setExtraFacilityGroup(extraFacilityCategory
            .getExtraFacilityGroup().toString());
         extraFacilityCategoryViewData.setAliasSuperCategoryCode(extraFacilityCategory
            .getAliasSuperCategoryCode());
         extraFacilityCategoryViewData.setDescription(extraFacilityCategory.getDescription());
         extraFacilityCategoryViewData
            .setExtraFacilityCategoryCode(extraFacilityCategory.getCode());
         extraFacilityCategoryViewData.setInventoryCode(extraFacilityCategory.getInventoryCode());
         extraFacilityCategoryViewData.setSuperCategoryCode(extraFacilityCategory
            .getSuperCategoryCode());
         populateExtraFacilityViewDataFromModel(extraFacilityCategory, pkgPassengers,
            extraFacilityCategoryViewData);
         categoryViewDatas.add(extraFacilityCategoryViewData);
      }
      target.setExtraFacilityCategoryViewData(categoryViewDatas);
   }

   /**
    * @param extraFacilityCategory
    * @param pkgPassengers
    * @param extraFacilityCategoryViewData
    */
   private void populateExtraFacilityViewDataFromModel(
      final ExtraFacilityCategory extraFacilityCategory,
      final List<PassengerViewData> pkgPassengers,
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData)
   {
      final List<ExtraFacility> extraFacilitiesDom = extraFacilityCategory.getExtraFacilities();
      if (CollectionUtils.isNotEmpty(extraFacilitiesDom))
      {
         for (final ExtraFacility extraFacility : extraFacilitiesDom)
         {
            getExtraFacilityViewDataFromModel(extraFacility, extraFacilityCategoryViewData,
               pkgPassengers);
         }
      }

   }

   /**
    * populates extraFacilityViewData and updates the price if extra facility of the same code is
    * already present
    *
    * @param extraFacility
    * @param extraFacilityCategoryViewData
    * @param pkgPassengers
    */
   private void getExtraFacilityViewDataFromModel(final ExtraFacility extraFacility,
      final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
      final List<PassengerViewData> pkgPassengers)
   {
      boolean isExtraPresent = false;
      for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
         .getExtraFacilityViewData())
      {
         if (isExtraWithSameCodePresent(extraFacility, extraFacilityViewData))
         {
            final BigDecimal totPrc =
               extraFacilityViewData.getPrice().add(getTotalPrice(extraFacility.getPrices()));
            extraFacilityViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(totPrc,
               CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency())));
            isExtraPresent = true;
         }
      }
      if (!isExtraPresent)
      {
         extraFacilityCategoryViewData.getExtraFacilityViewData().add(
            getExtraFacilityViewData(extraFacility, pkgPassengers));
      }

   }

   /**
    * @param extraFacility
    * @param extraFacilityViewData
    * @return boolean
    */
   private boolean isExtraWithSameCodePresent(final ExtraFacility extraFacility,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      return extraFacility.getExtraFacilityCode().equals(extraFacilityViewData.getCode());
   }

   /**
    * @param extraFacilitySDom
    */
   private ExtraFacilityViewData getExtraFacilityViewData(final ExtraFacility extraFacilitySDom,
      final List<PassengerViewData> passengers)
   {

      final ExtraFacilityViewData extraFacilityViewData;
      if (StringUtils.equalsIgnoreCase(extraFacilitySDom.getExtraFacilityCategory().getCode(),
         ExtraFacilityConstants.BAGGAGE_EXTRA_CATEGORY_CODE))
      {
         extraFacilityViewData = new BaggageExtraFacilityViewData();
         populateBaggageExtraFacilityViewData(extraFacilitySDom, extraFacilityViewData);
      }
      else
      {
         extraFacilityViewData = new ExtraFacilityViewData();
         populateOtherExtraFacilityViewData(extraFacilitySDom, extraFacilityViewData);
      }
      extraFacilityViewData.setCode(extraFacilitySDom.getExtraFacilityCode());
      extraFacilityViewData.setCategoryCode(extraFacilitySDom.getExtraFacilityCategory().getCode());
      extraFacilityViewData.setDescription(populateDescription(extraFacilitySDom));
      extraFacilityViewData.setInventoryCode(extraFacilitySDom.getInventoryCode());
      extraFacilityViewData.setInventoryId(extraFacilitySDom.getInventoryId());
      populateExtraPrice(extraFacilitySDom, extraFacilityViewData);
      extraFacilityViewData.setSelection(StringUtils.capitalize(extraFacilitySDom.getSelection()
         .toString().toLowerCase()));
      final List<PassengerViewData> paxViewDataList =
         getPassengerList(extraFacilitySDom.getPassengers(), passengers);
      extraFacilityViewData.setPassengerViewData(paxViewDataList);
      populateSummaryDescription(extraFacilitySDom, extraFacilityViewData);
      return extraFacilityViewData;
   }

   /**
    * @param extraFacilitySDom
    */
   private void populateBaggageExtraFacilityViewData(final ExtraFacility extraFacilitySDom,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      final BaggageExtraFacilityViewData bagexFacilityViewData =
         (BaggageExtraFacilityViewData) extraFacilityViewData;
      final BaggageExtraFacility baggageFacility = (BaggageExtraFacility) extraFacilitySDom;
      bagexFacilityViewData.setBaggageWeight(Integer.valueOf(baggageFacility.getBaggageWeight()));
   }

   /**
    * @param extraFacilitySDom
    * @param extraFacilityViewData
    */
   private void populateOtherExtraFacilityViewData(final ExtraFacility extraFacilitySDom,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      extraFacilityViewData.setQuantity(getTotalQuantity(extraFacilitySDom.getPrices()));
      extraFacilityViewData
         .setPaxCompositionViewData(populatePaxCompositionForExtraFacility(extraFacilitySDom));
   }

   /**
    * @param prices
    * @return totalQuantity
    */
   private Integer getTotalQuantity(final List<Price> prices)
   {
      int totalQuantity = 0;
      for (final Price price : prices)
      {
         totalQuantity = totalQuantity + price.getQuantity();
      }
      return totalQuantity;
   }

   /**
    * @param extraFacilitySDom
    * @return paxCompositionViewData
    */
   private PaxCompositionViewData populatePaxCompositionForExtraFacility(
      final ExtraFacility extraFacilitySDom)
   {
      final PaxCompositionViewData paxCompositionViewData = new PaxCompositionViewData();
      paxCompositionViewData.setNoOfAdults(PassengerUtils.getPersonTypeCountFromPassengers(
         extraFacilitySDom.getPassengers(), EnumSet.of(PersonType.ADULT)));
      paxCompositionViewData.setNoOfChildren(PassengerUtils.getPersonTypeCountFromPassengers(
         extraFacilitySDom.getPassengers(), EnumSet.of(PersonType.CHILD)));
      paxCompositionViewData.setNoOfInfants(PassengerUtils.getPersonTypeCountFromPassengers(
         extraFacilitySDom.getPassengers(), EnumSet.of(PersonType.INFANT)));
      paxCompositionViewData.setNoOfSeniors(PassengerUtils.getPersonTypeCountFromPassengers(
         extraFacilitySDom.getPassengers(), EnumSet.of(PersonType.SENIOR)));
      return paxCompositionViewData;
   }

   /**
    * @param passengers
    * @param passengersViewList
    * @return paxViewDataList
    */
   private List<PassengerViewData> getPassengerList(final List<Passenger> passengers,
      final List<PassengerViewData> passengersViewList)
   {
      final List<PassengerViewData> paxViewDataList = new ArrayList<PassengerViewData>();
      for (final Passenger pax : passengers)
      {
         for (final PassengerViewData paxView : passengersViewList)
         {
            if (pax.getId().equals(paxView.getIdentifier()))
            {
               paxViewDataList.add(paxView);
               break;
            }
         }
      }
      return paxViewDataList;

   }

   /**
    * @param extraFacilitySDom
    * @param extraFacilityViewData
    */
   private void populateExtraPrice(final ExtraFacility extraFacilitySDom,
      final ExtraFacilityViewData extraFacilityViewData)
   {
      final BigDecimal totPrc = getTotalPrice(extraFacilitySDom.getPrices());
      extraFacilityViewData.setPrice(totPrc);
      extraFacilityViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(totPrc,
         CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency())));
   }

   /**
    * Gets the currency appended price.
    *
    * @param price the price
    * @param currencySymbol the currencySymbol
    * @return the currency appended price
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
    * @param prices
    * @return totPrc
    */
   private BigDecimal getTotalPrice(final List<Price> prices)
   {
      BigDecimal totPrc = BigDecimal.ZERO;
      if (null != prices)
      {
         for (final Price price : prices)
         {
            totPrc = totPrc.add(price.getAmount().getAmount());
         }
      }
      return totPrc;
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
      description = populateWorldCareFundDescription(source, target, description);
      if (!description)
      {
         target.setSummaryDescription(getCommonSummaryDescription(target.getDescription(),
            target.getQuantity()));
      }
   }

   /**
    * @param source
    * @param target
    * @param description
    * @return boolean
    */
   private boolean populateWorldCareFundDescription(final ExtraFacility source,
      final ExtraFacilityViewData target, final boolean description)
   {
      if (StringUtils.equalsIgnoreCase(source.getExtraFacilityCode(),
         ExtraFacilityConstants.WORLD_CARE_FUND_ADULT))
      {
         target.setSummaryDescription(populateDescription(source));
         return true;
      }
      return description;
   }

   /**
    * @param description
    * @param quantity
    * @return string
    */
   private String getCommonSummaryDescription(final String description, final Integer quantity)
   {
      if (quantity > 0)
      {
         return description + " x " + quantity;
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
            source.getDescription(), Integer.toString(target.getQuantity())));
         return true;
      }

      return description;
   }

   /**
    * To set the descriptions related to LateCheckout.
    *
    * @param source
    * @param target
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
    * To set the descriptions related to TransferOption.
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
    * To set the descriptions related to Flight Extras.
    *
    * @param source
    * @param target
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
      if (target.getQuantity() <= 1)
      {
         return getDescription(source.getExtraFacilityCode(),
            Integer.toString(target.getQuantity()));
      }
      return getDescription("LCDS", Integer.toString(target.getQuantity()));
   }

   /**
    * Gets the description from content csv.
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
            return getDescription(source.getExtraFacilityCode() + "S",
               getTotalQuantity(source.getPrices()).toString());
         }
         else
         {
            return getDescription("CAR_HIRE", source.getDescription(),
               getTotalQuantity(source.getPrices()).toString());
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

   /**
    * Populate descrption.
    *
    * @param source the source
    * @return the string
    */
   private String populateDescription(final ExtraFacility source)
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

}