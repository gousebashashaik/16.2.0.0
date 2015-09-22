/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.CreditCardCharge;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.tui.services.data.CreditCardRuleOutputData;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;

/**
 * @author deepakkumar.k
 *
 */
public class BookFlowAccommodationPopulator implements
   Populator<SearchResultViewData, BookFlowAccommodationViewData>
{

   @Resource
   private DroolsPriorityProviderService droolsPriorityProviderService;

   public static final int HUNDRED = 100;

   public static final double CHILD_CHARGE = 0.5;

   private static final Logger LOG = Logger.getLogger(BookFlowAccommodationPopulator.class);

   private static final int MIN_AGE = 2;

   private static final int MAX_AGE = 12;

   /**
    * Retrieves a target(viewData) ,from source (SearchResultViewData).
    *
    * @param source
    * @param target
    */

   @Override
   public void populate(final SearchResultViewData source,
      final BookFlowAccommodationViewData target) throws ConversionException
   {

      target.setPackageData(source);
      if (source.isWorldCare())
      {
         target.setWorldCareFunds(getWorldCareFunds(target));
      }
      target.setPackagePrice(getPackagePrice(source, target));
      target.setSubTotal(getSubTotal(source));
      target.setTotal(getTotal(source));
      target.setCreditCardData(getCreditCardData(target, source));
      target.setPackageInfo(setPackageInfo(source));

   }

   /**
    * Retrieves a flag for all attribute inside in setPackageIncluded.
    *
    * @param source
    * @return BookFlowData
    */
   @SuppressWarnings("boxing")
   private Map<String, Object> setPackageInfo(final SearchResultViewData source)
   {
      final Map<String, Object> flag = new LinkedHashMap<String, Object>();

      flag.put("flight", !CollectionUtils.isEmpty(source.getItinerary().getOutbounds()));
      flag.put("coach", source.isCoachTransfer());
      flag.put("duration", source.getDuration());
      flag.put("room", source.getAccommodation().getRooms());
      flag.put("board", source.getAccommodation().getRooms().get(0).getBoardType());
      flag.put("donation", source.isWorldCare());

      // TODO whenever these data will get from endeca.
      flag.put("baggage", false);
      flag.put("protection", false);

      boolean carHire = false;
      if ((source.getOffer() != null)
         && (StringUtils.equals("FREE_CAR_HIRE", source.getOffer().getId())))
      {
         carHire = true;
         LOG.info(String.valueOf(carHire));
      }

      return flag;
   }

   /**
    * Retrieves a total ,which subtracts discount from subtotal.
    *
    * @param source
    * @return Total
    */

   private String getTotal(final SearchResultViewData source)
   {

      final String subTotal =
         Double.toString(Double.parseDouble(source.getPrice().getTotalParty())
            + Double.parseDouble(source.getPrice().getDiscount()));
      return Double.toString(Double.parseDouble(subTotal)
         - Double.parseDouble(source.getPrice().getDiscount()));
   }

   /**
    * Retrieves a sub total ,which include totalparty , worldcarefund and discount.
    *
    * @param source
    * @return subTotal
    */

   private String getSubTotal(final SearchResultViewData source)
   {

      return Double.toString(Double.parseDouble(source.getPrice().getTotalParty())
         + Double.parseDouble(source.getPrice().getDiscount()));

   }

   /**
    * Retrieves a CreditCardCharges on the basis of drools.. Added credit card cap Calculation
    *
    * @param source
    * @param target
    * @return CreditCardData
    */

   @SuppressWarnings("boxing")
   private CreditCardCharge getCreditCardData(final BookFlowAccommodationViewData target,
      final SearchResultViewData source)
   {
      double creditCardCharge = 0;

      final CreditCardCharge charge = new CreditCardCharge();

      final CreditCardRuleOutputData credit = droolsPriorityProviderService.getCreditCardCharges();

      if (StringUtils.isNotBlank(credit.getCreditCardPercentage())
         && StringUtils.isNotEmpty(credit.getCreditCardCapAmount()))
      {

         creditCardCharge =
            Double.parseDouble(target.getTotal())
               * (Double.parseDouble(credit.getCreditCardPercentage()) / HUNDRED);

         charge.setCreditCardPercentage(Double.toString(Double.parseDouble(credit
            .getCreditCardPercentage())));

         if (creditCardCharge >= Double.parseDouble(credit.getCreditCardCapAmount()))
         {
            creditCardCharge = Double.parseDouble(credit.getCreditCardCapAmount());
         }

         charge.setTotal(Double.toString(Double.parseDouble(target.getTotal()) + creditCardCharge));
      }

      // DepositAmountPP is used which holds lowdeposit or standard deposit
      // based on booking date
      if ((source.getPrice().isLowDepositExists() || source.getPrice().isDepositExists())
         && StringUtils.isNotBlank(credit.getCreditCardPercentage())
         && StringUtils.isNotEmpty(credit.getCreditCardCapAmount()))
      {

         final Double lowDeposit = Double.parseDouble(source.getPrice().getDepositAmountPP());

         creditCardCharge =
            lowDeposit * Double.parseDouble(credit.getCreditCardPercentage()) / HUNDRED;

         if (creditCardCharge >= Double.parseDouble(credit.getCreditCardCapAmount()))
         {
            creditCardCharge = Double.parseDouble(credit.getCreditCardCapAmount());
         }

         charge.setDeposit(Double.toString(lowDeposit + creditCardCharge));

      }
      return charge;
   }

   /**
    * Retrieves a WorldCareFunds on the basis of drools.
    *
    * @param target
    * @return WorldCareFunds
    */
   private double getWorldCareFunds(final BookFlowAccommodationViewData target)
   {

      final WorldCareFundRuleOutputData worldCare =
         droolsPriorityProviderService.getWorldCareFundCharges();

      if (StringUtils.isNotEmpty(worldCare.getAdultCharge())
         && StringUtils.isNotBlank(worldCare.getChildCharge()))
      {
         return (target.getSearchRequestData().getNoOfAdults() * Double.parseDouble(worldCare
            .getAdultCharge()))
            + (target.getSearchRequestData().getNoOfSeniors() * Double.parseDouble(worldCare
               .getAdultCharge()))
            + calcluateChildWorldCareFund(target.getSearchRequestData().getChildAges(),
               Double.parseDouble(worldCare.getAdultCharge()),
               Double.parseDouble(worldCare.getChildCharge()));

      }
      else
      {
         return (target.getSearchRequestData().getNoOfAdults() * 1.0)
            + (target.getSearchRequestData().getNoOfSeniors() * 1.0)
            + calcluateChildWorldCareFund(target.getSearchRequestData().getChildAges(), 1.0,
               CHILD_CHARGE);
      }

   }

   /**
    * Added as tracs considers above 12 child as Adults for world care calculation
    */

   private double calcluateChildWorldCareFund(final List<Integer> childrenAge,
      final double adultCharge, final double childCharge)
   {

      int childcount = 0;
      int adultCount = 0;

      if (childrenAge != null)
      {

         for (final int age : childrenAge)
         {
            if (age > MAX_AGE)
            {
               adultCount++;
            }
            else if (age >= MIN_AGE && age <= MAX_AGE)
            {
               childcount++;
            }
         }
      }
      return (adultCount * adultCharge) + (childcount * childCharge);
   }

   /**
    * Retrieves a getPackagePrice from SearchResultPriceViewData.
    *
    * @param source
    * @return PackagePrice
    */
   private String getPackagePrice(final SearchResultViewData source,
      final BookFlowAccommodationViewData target)
   {
      return Double.toString(Double.parseDouble(source.getPrice().getTotalParty())
         + Double.parseDouble(source.getPrice().getDiscount()) - target.getWorldCareFunds());
   }

}
