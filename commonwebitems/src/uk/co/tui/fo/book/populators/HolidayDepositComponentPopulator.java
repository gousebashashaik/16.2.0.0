/**
 *
 */
package uk.co.tui.fo.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.LowDepositModel;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.InventoryInfo;
import uk.co.portaltech.travel.services.lowdeposit.LowDepositService;
import uk.co.portaltech.travel.services.lowdeposit.PartyCompositionCriteria;
import uk.co.portaltech.travel.services.lowdeposit.VisionBrochureCriteria;
import uk.co.portaltech.tui.services.DurationHaulTypeService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Deposit;
import uk.co.tui.book.domain.lite.DepositType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.LowDeposit;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.services.rule.RuleService;

/**
 * populates deposit and low deposit in package. HolidayDepositComponentPopulator.
 *
 * @author amaresh.d
 */
final class HolidayDepositComponentPopulator implements Populator<Holiday, BasePackage>
{

   /** The low deposit service. */
   @Resource
   private LowDepositService lowDepositService;

   /** The duration hual service. */
   @Resource
   private DurationHaulTypeService durationHaulTypeService;

   /** The RuleService. */
   @Resource
   private RuleService ruleService;

   @Resource
   private CurrencyResolver currencyResolver;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /**
    * Populates the deposit component such as deposit and low deposit required for the holiday.
    *
    * @param holiday the holiday
    * @param packageModel the package model
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    *      java.lang.Object)
    */
   @Override
   public void populate(final Holiday holiday, final BasePackage packageModel)
   {
      // 1. construct Brochure Criteria
      final VisionBrochureCriteria brochureCriteria = new VisionBrochureCriteria();
      constructBrochureCriteria(brochureCriteria, holiday);

      // 2. get haul type of inbound flight
      // krus if check added for flightonly oneway
      Leg leg = null;
      final Itinerary itinerary = packageComponentService.getFlightItinerary(packageModel);
      if (itinerary.getInBound() != null)
      {
         leg = itinerary.getInBound().get(0);
      }
      else
      {
         leg = itinerary.getOutBound().get(0);
      }
      brochureCriteria.setFlightType(durationHaulTypeService.findHaultypeForAirports(leg
         .getArrivalAirport().getCode(), leg.getDepartureAirport().getCode()));
      final LowDepositModel lowDeposit = lowDepositService.getLowDeposit(brochureCriteria);
      populateLowDeposit(packageModel, lowDeposit);
   }

   /**
    * Populates low deposit.
    *
    * @param packageModel the package model
    * @param lowDeposit the low deposit
    */
   private void populateLowDeposit(final BasePackage packageModel, final LowDepositModel lowDeposit)
   {
      // offer lowdeposit if and only if it is applicable and only for TRACS
      // sourced flights.
      // donot offer lowdeposit for third party flights
      if (lowDeposit != null
         && !PackageUtilityService.isMulticomThirdPartyFlight(packageComponentService
            .getFlightItinerary(packageModel)))
      {
         final PartyCompositionCriteria partyCompositionCriteria =
            createPartyCompositionCriteria(packageModel.getPassengers());
         partyCompositionCriteria.setBrandType(packageModel.getBrandType().toString());
         ruleService.executeLowDepositPP(partyCompositionCriteria);
         final LowDeposit holidayLowDeposit = new LowDeposit();
         // 1. if low deposit is present then proceed.
         holidayLowDeposit.setAvailable(Boolean.TRUE);
         holidayLowDeposit.setApplicable(Boolean.TRUE);
         holidayLowDeposit.setDepositType(DepositType.LOW_DEPOSIT);
         holidayLowDeposit.setBalanceDueDate(lowDeposit.getBalanceCollectionDate());
         holidayLowDeposit.setDepositDueDate(new Date());
         final int eligiblePartyCount = partyCompositionCriteria.getNoOfPassengers();
         final boolean isApplied =
            applyPerPersonDeposit(holidayLowDeposit, lowDeposit, eligiblePartyCount,
               partyCompositionCriteria.isPerPersonDeposit());
         if (!isApplied)
         {
            applyFamilyLowdeposit(lowDeposit, holidayLowDeposit, eligiblePartyCount);
         }

         final List<Deposit> deposits = new ArrayList<Deposit>();
         deposits.add(holidayLowDeposit);
         packageModel.setDeposits(deposits);
      }
      else
      {
         packageModel.setDeposits(new ArrayList<Deposit>());
      }
   }

   /**
    * Apply family lowdeposit.
    *
    * @param lowDeposit the low deposit
    * @param holidayLowDeposit the holiday low deposit
    * @param noOfPax no of Passengers
    */
   private void applyFamilyLowdeposit(final LowDepositModel lowDeposit,
      final LowDeposit holidayLowDeposit, final int noOfPax)
   {

      final Money perFamilyAmount = convertDoubleToMoneyLite(lowDeposit.getPerFamily());

      final Price price = new Price();
      price.setAmount(perFamilyAmount);
      price.setQuantity(Integer.valueOf(noOfPax));

      holidayLowDeposit.setDepositAmount(price);
      holidayLowDeposit.setBaseDepositAmount(price);

      final Price outstandingBalance = new Price();
      outstandingBalance.setAmount(convertBigDecimalToMoneyLite(BigDecimal.ZERO));
      outstandingBalance.setQuantity(Integer.valueOf(noOfPax));
      holidayLowDeposit.setOutstandingBalance(outstandingBalance);
   }

   /**
    * Convert double to money lite.
    *
    * @param value the value
    * @return the money
    */
   private Money convertDoubleToMoneyLite(final Double value)
   {
      return convertStringToMoneyLite(String.valueOf(value));
   }

   /**
    * Convert string to money lite.
    *
    * @param value the value
    * @return the money
    */
   private Money convertStringToMoneyLite(final String value)
   {
      return convertBigDecimalToMoneyLite(new BigDecimal(value));
   }

   /**
    * Find and apply per person deposit.
    *
    * @param holidayLowDeposit the holiday low deposit
    * @param lowDeposit the low deposit
    * @param noOfPassengers total no of eligible passengers
    * @param perPersonDepositApplicable
    * @return true, if successful
    */
   private boolean applyPerPersonDeposit(final LowDeposit holidayLowDeposit,
      final LowDepositModel lowDeposit, final int noOfPassengers,
      final boolean perPersonDepositApplicable)
   {
      boolean applied = false;
      if (isPersonalLowDepositApplicable(perPersonDepositApplicable, lowDeposit))
      {

         final Money perPersonAmount = convertDoubleToMoneyLite(lowDeposit.getPerPerson());
         final Price perPersonPrice = new Price();
         perPersonPrice.setAmount(perPersonAmount);
         perPersonPrice.setQuantity(Integer.valueOf(noOfPassengers));

         holidayLowDeposit.setBaseDepositAmount(perPersonPrice);

         final BigDecimal calculatedAmount =
            perPersonAmount.getAmount().multiply(new BigDecimal(String.valueOf(noOfPassengers)));

         final Price totalPrice = new Price();
         totalPrice.setAmount(convertBigDecimalToMoneyLite(calculatedAmount));
         totalPrice.setQuantity(Integer.valueOf(noOfPassengers));
         holidayLowDeposit.setDepositAmount(totalPrice);
         final Price outstandingBalance = new Price();
         outstandingBalance.setAmount(convertBigDecimalToMoneyLite(BigDecimal.ZERO));
         outstandingBalance.setQuantity(Integer.valueOf(noOfPassengers));
         holidayLowDeposit.setOutstandingBalance(outstandingBalance);
         applied = true;
      }
      return applied;
   }

   /**
    * Checks if is personal low deposit applicable.
    *
    * @param personPersonPrice the person person price
    * @param lowDeposit the low deposit
    * @return true, if is person low deposit applicable
    */
   private boolean isPersonalLowDepositApplicable(final boolean personPersonPrice,
      final LowDepositModel lowDeposit)
   {
      return personPersonPrice || lowDeposit.getPerFamily() == null;
   }

   /**
    * VisionBrochureCriteria is filled with Product code, SubProduct code and Flight type.
    *
    * @param brochureCriteria the brochure criteria
    * @param holiday the holiday
    */
   private void constructBrochureCriteria(final VisionBrochureCriteria brochureCriteria,
      final Holiday holiday)
   {

      // Get product code and Sub product code from Holiday
      final InventoryInfo tracsDetails = holiday.getInventoryInfo();
      brochureCriteria.setSubProductCode(tracsDetails.getSubProductCode());
      brochureCriteria.setProductCode(tracsDetails.getProductCode());
      // current Date
      final Date bookingdate = DateUtils.getSystemDate();
      Date depdate = null;
      // krus: added if condition and else condition for Flight only one way
      if (holiday.getItinerary().getOutbound() != null)
      {
         depdate =
            holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate().toDate();
      }
      else if (holiday.getItinerary().getInbound() != null)
      {
         depdate =
            holiday.getItinerary().getInbound().get(0).getSchedule().getDepartureDate().toDate();
      }
      brochureCriteria.setDepartureDate(depdate);
      brochureCriteria.setBookingDate(bookingdate);
      brochureCriteria.setNoOfDays(bookingdate, depdate);
   }

   /**
    * Create PartyCompositionCriteria from passengers.
    *
    * @param passengers
    * @return PartyCompositionCriteria
    */
   private PartyCompositionCriteria createPartyCompositionCriteria(final List<Passenger> passengers)
   {
      final PartyCompositionCriteria partyCompositionCriteria = new PartyCompositionCriteria();

      final int noOfAdults =
         PassengerUtils.getPersonTypeCountFromPassengers(passengers, EnumSet.of(PersonType.ADULT));
      final int noOfSeniors =
         PassengerUtils.getPersonTypeCountFromPassengers(passengers, EnumSet.of(PersonType.SENIOR));
      final int noOfTeens =
         PassengerUtils.getPersonTypeCountFromPassengers(passengers, EnumSet.of(PersonType.TEEN));
      final int noOfChildren =
         PassengerUtils.getPersonTypeCountFromPassengers(passengers, EnumSet.of(PersonType.CHILD));
      final int noOfInfants =
         PassengerUtils.getPersonTypeCountFromPassengers(passengers, EnumSet.of(PersonType.INFANT));
      // infants are ignored
      partyCompositionCriteria.setNoOfPassengers(noOfAdults + noOfSeniors + noOfChildren
         + noOfTeens);
      partyCompositionCriteria.setNoOfAdults(noOfAdults);
      partyCompositionCriteria.setNoOfChildren(noOfChildren);
      partyCompositionCriteria.setInfantCount(noOfInfants);
      partyCompositionCriteria.setPerPersonDeposit(true);
      return partyCompositionCriteria;
   }

   /**
    * This method takes the value as BigDecimal and returns the MoneyModel object.
    *
    * @param value the value as BigDecimal.
    * @return MoneyModel
    */
   private Money convertBigDecimalToMoneyLite(final BigDecimal value)
   {
      final Money money = new Money();
      money.setAmount(value);
      money.setCurrency(Currency.getInstance(currencyResolver.getSiteCurrency()));
      return money;
   }

}
