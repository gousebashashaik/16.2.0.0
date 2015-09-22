/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.web.view.data.AlternateBoardBasisPriceViewData;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.CreditCardCharge;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightScheduleData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.tui.services.data.CreditCardRuleOutputData;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;

/**
 * @author deepakkumar.k
 *
 */
public class ThBookFlowAccommodationPopulator implements
   Populator<SearchResultViewData, BookFlowAccommodationViewData>
{

   @Resource
   private DroolsPriorityProviderService droolsPriorityProviderService;

   public static final int HUNDRED = 100;

   public static final double CHILD_CHARGE = 0.5;

   private static final int AGE_TWO = 2;

   private static final int AGE_TWELVE = 12;

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

      target.setPackageInfo(setPackageInfo(source));
      target.setAlternateBoardPrice(getAlternateBoardPrice(source, target));

   }

   /**
    * @param source
    * @return AlternateBoardPrice This method populate all boardbasis for a accommodation includes
    *         default.
    */
   private List<AlternateBoardBasisPriceViewData> getAlternateBoardPrice(
      final SearchResultViewData source, final BookFlowAccommodationViewData target)
   {

      final List<AlternateBoardBasisPriceViewData> alternateBoardBasis =
         new ArrayList<AlternateBoardBasisPriceViewData>();
      for (final BoardBasisType board : source.getAlternateBoard())
      {

         // Fixed DE4238 : Total party price was getting updated with other board basis price.
         // It should be set only with default board basis price.
         final boolean defaultBoardBasis = board.isDefaultBoardBasis();
         if (defaultBoardBasis)
         {
            source.getPrice().setTotalParty(board.getTotalPrice());
         }
         final AlternateBoardBasisPriceViewData alternateBoard =
            new AlternateBoardBasisPriceViewData();
         alternateBoard.setBoardbasisCode(board.getBoardbasisCode());
         alternateBoard.setDefaultBoardBasis(defaultBoardBasis);
         alternateBoard.setName(board.getName());
         alternateBoard.setTotalPricePP(board.getTotalPricePP());
         alternateBoard.setPackagePrice(getPackagePrice(board.getTotalPrice(), source.getPrice()
            .getDiscount(), target.getWorldCareFunds()));
         alternateBoard.setSubTotal(getSubTotal(board.getTotalPrice(), source.getPrice()
            .getDiscount()));
         alternateBoard.setTotal(getTotal(board.getTotalPrice(), source.getPrice().getDiscount()));
         alternateBoard.setCreditCardData(getCreditCardData(source, board.getTotalPrice(), source
            .getPrice().getDiscount()));
         alternateBoard.setAccomdboardpriceDiffrence(board.getAccomdboardpriceDiffrence());
         alternateBoardBasis.add(alternateBoard);
      }

      return alternateBoardBasis;
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
      boolean carHire = false;
      if ((source.getOffer() != null)
         && (StringUtils.equals("FREE_CAR_HIRE", source.getOffer().getId())))
      {
         carHire = true;
      }
      flag.put("freeCareHire", carHire);
      /**
       * this value is hardcoded for Alcarte package, As presently anite doesn't return any
       * indicator for private Taxi This need to be changed once Anite fixes it
       */
      checkCoachAndALC(source, flag);

      flag.put("duration", source.getDuration());
      flag.put("room", source.getAccommodation().getRooms());
      flag.put("board", source.getAccommodation().getRooms().get(0).getBoardType());
      flag.put("donation", source.isWorldCare());

      flag.put("baggage", false);
      flag.put("protection", false);

      flag.put("outBoundDuration", getFlightDuration(source.getItinerary().getOutbounds()));
      flag.put("inBoundDuration", getFlightDuration(source.getItinerary().getInbounds()));

      return flag;
   }

   /**
    * @param source
    * @param flag
    */
   private void checkCoachAndALC(final SearchResultViewData source, final Map<String, Object> flag)
   {
      boolean privateTaxi = false;
      if (source.isCoachTransfer()
         && StringUtils.equalsIgnoreCase(source.getAccommodation().getDifferentiatedCode(), "ALC"))
      {
         privateTaxi = true;
         flag.put("coach", false);
      }
      flag.put("privateTaxi", privateTaxi);
   }

   /**
    *
    * @param flightWay
    * @return duration
    */
   private String getFlightDuration(final List<SearchResultFlightDetailViewData> flightWay)
   {

      final SearchResultFlightScheduleData schedule = flightWay.get(0).getSchedule();
      return DateUtils.substractTimes(schedule.getDepartureDate(), schedule.getArrivalDate(),
         schedule.getDepartureTime(), schedule.getArrivalTime());

   }

   /**
    * Retrieves a total ,which subtracts discount from subtotal.
    *
    * @param discount
    * @return Total
    */

   private String getTotal(final String boardPrice, final String discount)
   {

      final String subTotal =
         Double.toString(Double.parseDouble(boardPrice) + Double.parseDouble(discount));
      return Double.toString(Double.parseDouble(subTotal) - Double.parseDouble(discount));
   }

   /**
    * Retrieves a sub total ,which include totalparty , worldcarefund and discount.
    *
    * @param discount
    * @return subTotal
    */

   private String getSubTotal(final String boardPrice, final String discount)
   {

      return Double.toString(Double.parseDouble(boardPrice) + Double.parseDouble(discount));

   }

   /**
    * Retrieves a CreditCardCharges on the basis of drools.. Added credit card cap Calculation
    *
    * @param source
    * @return CreditCardData
    */

   @SuppressWarnings("boxing")
   private CreditCardCharge getCreditCardData(final SearchResultViewData source,
      final String boardPrice, final String discount)
   {
      double creditCardCharge = 0;

      final CreditCardCharge charge = new CreditCardCharge();

      final CreditCardRuleOutputData credit = droolsPriorityProviderService.getCreditCardCharges();

      if (StringUtils.isNotBlank(credit.getCreditCardPercentage())
         && StringUtils.isNotEmpty(credit.getCreditCardCapAmount()))
      {

         creditCardCharge =
            Double.parseDouble(getTotal(boardPrice, discount))
               * (Double.parseDouble(credit.getCreditCardPercentage()) / HUNDRED);

         charge.setCreditCardPercentage(Double.toString(Double.parseDouble(credit
            .getCreditCardPercentage())));

         if (creditCardCharge >= Double.parseDouble(credit.getCreditCardCapAmount()))
         {
            creditCardCharge = Double.parseDouble(credit.getCreditCardCapAmount());
         }

         charge.setTotal(Double.toString(Double.parseDouble(getTotal(boardPrice, discount))
            + creditCardCharge));
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
            if (age > AGE_TWELVE)
            {
               adultCount++;
            }
            else if (age >= AGE_TWO && age <= AGE_TWELVE)
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
    * @param boardPrice
    * @param discount
    * @return PackagePrice
    */
   private String getPackagePrice(final String boardPrice, final String discount,
      final Double worldcareFund)
   {
      return Double.toString(Double.parseDouble(boardPrice) + Double.parseDouble(discount)
         - worldcareFund);
   }

}
