/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.InsuranceType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.fj.book.constants.BookFlowConstants;
import uk.co.tui.fj.book.view.data.ExcessWaiverViewData;
import uk.co.tui.fj.book.view.data.InsuranceViewData;

/**
 * @author pradeep.as
 *
 */
public class InsuranceViewDataPopulatorLite implements
   Populator<InsuranceExtraFacility, InsuranceViewData>
{

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

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

   private static final int TWO = 2;

   @Resource
   private CurrencyResolver currencyResolver;

   @SuppressWarnings(BookFlowConstants.BOXING)
   @Override
   public void populate(final InsuranceExtraFacility extraFacility,
      final InsuranceViewData insuranceViewData) throws ConversionException
   {
      insuranceViewData.setCode(extraFacility.getExtraFacilityCode());
      insuranceViewData.setDescription(extraFacility.getDescription());
      insuranceViewData.setSelected(extraFacility.isSelected());
      insuranceViewData.setFamilyInsPresent(isFamilyPresent(extraFacility));
      updateQuantity(extraFacility, insuranceViewData);
      insuranceViewData.setTandcAccepted(false);
      BigDecimal frmPrice = BigDecimal.ZERO;
      for (final Price price : extraFacility.getPrices())
      {
         updateCurrencyAppendedPrice(insuranceViewData, price);
         frmPrice = frmPrice.add(price.getRate().getAmount());
      }
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      insuranceViewData.setTotalPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + calculateTotalPrice(extraFacility).toString());
      insuranceViewData.setPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + calculateTotalPrice(extraFacility).toString());
      insuranceViewData.setFrmPrice(frmPrice.toString());
      if (!extraFacility.isSelected())
      {
         insuranceViewData.setPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
            + frmPrice.toString());
      }
      else
      {
         insuranceViewData.setPaxComposition(getPaxComposition(getTypes(extraFacility
            .getPassengers())));
      }
      insuranceViewData.setDescriptionSummary(insuranceViewData.getDescription()
         + insuranceViewData.getPaxComposition());
      insuranceViewData.setCurrencyFrmPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
         + frmPrice.toString());
      populateExWaiverViewData(extraFacility, insuranceViewData);
   }

   private boolean isFamilyPresent(final InsuranceExtraFacility extraFacility)
   {
      return extraFacility.getInsuranceType() == InsuranceType.FAMILY;
   }

   /**
    * This method updates the currency appended price for child and adults
    *
    * @param insuranceViewData
    * @param price boolean
    */
   private void updateCurrencyAppendedPrice(final InsuranceViewData insuranceViewData,
      final Price price)
   {
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      if (price.getPriceProfile() != null)
      {
         if (isAdultPriceUpdate(insuranceViewData, price))
         {
            insuranceViewData.setFrmAdtPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
               + price.getRate().getAmount().toString());
         }
         else if (StringUtils.equalsIgnoreCase(price.getPriceProfile().getPersonType().toString(),
            PersonType.CHILD.toString()))
         {
            insuranceViewData.setFrmChdPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
               + price.getRate().getAmount().toString());
         }
      }
   }

   /**
    * Checks if adult price update required
    *
    * @param insuranceViewData
    * @param price
    * @return boolean
    */
   private boolean isAdultPriceUpdate(final InsuranceViewData insuranceViewData, final Price price)
   {
      return StringUtils.equalsIgnoreCase(price.getPriceProfile().getPersonType().toString(),
         PersonType.ADULT.toString()) && StringUtils.isEmpty(insuranceViewData.getFrmAdtPrice());
   }

   /**
    * @param extraFacility
    * @param insuranceViewData
    */
   private void updateQuantity(final InsuranceExtraFacility extraFacility,
      final InsuranceViewData insuranceViewData)
   {
      int insuranceQuantity = 0;
      if (isFamilyPresent(extraFacility)
         && CollectionUtils.isNotEmpty(extraFacility.getPassengers()))
      {
         insuranceQuantity = 1;
      }
      else if (CollectionUtils.isNotEmpty(extraFacility.getPassengers()))
      {
         insuranceQuantity = extraFacility.getPassengers().size();
      }
      insuranceViewData.setQuantity(insuranceQuantity);
   }

   /**
    * Populate ex waiver view data.
    *
    * @param extraFacility the extra facility
    * @param insuranceViewData the insurance view data
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void populateExWaiverViewData(final InsuranceExtraFacility extraFacility,
      final InsuranceViewData insuranceViewData)
   {
      if (extraFacility.getInsuranceExcessWaiver() != null)
      {
         final ExcessWaiverViewData excessWaiverViewData = new ExcessWaiverViewData();
         excessWaiverViewData.setCode(extraFacility.getInsuranceExcessWaiver()
            .getExtraFacilityCode());
         excessWaiverViewData.setDescription(extraFacility.getInsuranceExcessWaiver()
            .getDescription());
         excessWaiverViewData.setSelected(BooleanUtils
            .toBoolean(isExcessWaiverOpted(extraFacility)));
         updateInsurancePrice(extraFacility, insuranceViewData, excessWaiverViewData);
         excessWaiverViewData.setDescriptionSummary(getDescription("EXTRADESCRIPTION",
            excessWaiverViewData.getDescription(),
            Integer.toString(insuranceViewData.getQuantity())));
         insuranceViewData.setExcessWaiverViewData(excessWaiverViewData);
      }
      else
      {
         insuranceViewData.setExcessWaiverViewData(new ExcessWaiverViewData());
      }
   }

   /**
    * This method updates the excessWaiver price and total price of insurance
    *
    * @param extraFacility
    * @param insuranceViewData
    * @param excessWaiverViewData
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void updateInsurancePrice(final InsuranceExtraFacility extraFacility,
      final InsuranceViewData insuranceViewData, final ExcessWaiverViewData excessWaiverViewData)
   {
      if (extraFacility.getInsuranceExcessWaiver().getPrice() != null)
      {
         final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
         if (isNonFamilyExcessWaiverOpted(extraFacility))
         {
            insuranceViewData.setTotalPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
               + calculateTotalPrice(extraFacility).add(
                  extraFacility.getInsuranceExcessWaiver().getPrice().getAmount().getAmount())
                  .toString());
            excessWaiverViewData.setPrice(extraFacility.getInsuranceExcessWaiver().getPrice()
               .getAmount().getAmount().toString());
         }
         else if (isExcessWaiverOpted(extraFacility))
         {
            excessWaiverViewData.setPrice(extraFacility.getInsuranceExcessWaiver().getPrice()
               .getRate().getAmount().toString());
            insuranceViewData.setTotalPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
               + calculateTotalPrice(extraFacility).add(
                  extraFacility.getInsuranceExcessWaiver().getPrice().getRate().getAmount())
                  .toString());
         }
         else
         {
            excessWaiverViewData.setPrice(extraFacility.getInsuranceExcessWaiver().getPrice()
               .getRate().getAmount().toString());
            insuranceViewData.setTotalPrice(CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
               + calculateTotalPrice(extraFacility).toString());
         }
         excessWaiverViewData.setCurrencyAppendedPrice(CurrencyUtils
            .getCurrencySymbol(currencyResolver.getSiteCurrency())
            + (new BigDecimal(excessWaiverViewData.getPrice()).setScale(TWO,
               BigDecimal.ROUND_HALF_UP).toString()));
      }
   }

   /**
    * Checks if non family ExcessWaiver is Opted by the user
    *
    * @param extraFacility
    * @return boolean
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private boolean isNonFamilyExcessWaiverOpted(final InsuranceExtraFacility extraFacility)
   {
      return isExcessWaiverOpted(extraFacility) && !isFamilyPresent(extraFacility)
         && SyntacticSugar.isNotNull(extraFacility.getPassengers());
   }

   /**
    * Checks ExcessWaiver is Opted by the user
    *
    * @param extraFacility
    * @return boolean
    */
   private boolean isExcessWaiverOpted(final InsuranceExtraFacility extraFacility)
   {
      return extraFacility.getInsuranceExcessWaiver().isSelected();
   }

   /**
    * Calculate total price.
    *
    * @param extraFacility the extra facility
    * @return the string
    */
   private BigDecimal calculateTotalPrice(final InsuranceExtraFacility extraFacility)
   {
      final List<Price> extraPrices = extraFacility.getPrices();
      BigDecimal total = BigDecimal.ZERO;
      for (final Price eachPriceModel : extraPrices)
      {
         if (eachPriceModel.getAmount() != null)
         {
            total = total.add(eachPriceModel.getAmount().getAmount());
         }
      }
      return total;
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
   private Integer getCount(final Collection<String> types, final String type)
   {
      Integer paxCount = NumberUtils.INTEGER_ZERO;
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
    *
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

   /**
    * Gets the description from content csv.
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

}
