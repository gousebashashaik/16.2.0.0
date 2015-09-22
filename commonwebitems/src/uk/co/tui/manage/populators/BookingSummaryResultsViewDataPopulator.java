/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import uk.co.portaltech.travel.enums.PersonType;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.HighLevelBookingType;
import uk.co.tui.book.domain.lite.Memo;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.services.ISOCountryCodeService;
import uk.co.tui.book.utils.ExtraFacilityUtils;
import uk.co.tui.common.DateUtils;
import uk.co.tui.manage.security.services.ManageSessionService;
import uk.co.tui.manage.viewdata.BaggageExtraFacilityViewData;
import uk.co.tui.manage.viewdata.ErrataViewData;
import uk.co.tui.manage.viewdata.ExtraFacilityCategoryViewData;
import uk.co.tui.manage.viewdata.ExtraFacilityViewData;
import uk.co.tui.manage.viewdata.HighLevelBookingViewData;
import uk.co.tui.manage.viewdata.PackageViewData;
import uk.co.tui.manage.viewdata.PassengerAddressViewData;
import uk.co.tui.manage.viewdata.PassengerViewData;
import uk.co.tui.manage.viewdata.PaxCompositionViewData;

/**
 * @author lakshmipathi.d
 *
 */
public class BookingSummaryResultsViewDataPopulator implements
   Populator<PackageHoliday, PackageViewData>
{

   @Resource
   private ManageSessionService manageSessionService;

   @Resource
   private Populator<Stay, PackageViewData> availAccommViewDataPopulator;

   @Resource
   private Populator<PackageHoliday, PackageViewData> extraFacilityCategoryViewDataPopulator;

   @Resource
   private Populator<PackageHoliday, PackageViewData> packagePriceViewDataPopulator;

   @Resource
   private Populator<FlightItinerary, PackageViewData> manageFlightViewDataPopulator;

   @Resource
   private ISOCountryCodeService iSOCountryCodeService;

   private static final Logger LOGGER = Logger
      .getLogger(BookingSummaryResultsViewDataPopulator.class);

   private static final String MEAL = "MEAL";

   private static final String BAG = "BAG";

   private static final String SEAT = "SEAT";

   private static final String DR = "DR";

   private static final String DOCT = "DOCT";

   private static final String INFANT = "INFA";

   private static final String REV = "REV";

   private boolean readOnly;

   private static final int TWO = 2;

   @Override
   public void populate(final PackageHoliday source, final PackageViewData target)
      throws ConversionException
   {
      readOnly = false;
      if (source.getInventory() != null)
      {
         target.setInventoryType(source.getInventory().getInventoryType().name());
      }
      populateHighLevelBookingData(source.getHighLevelBookingType(), target);

      target.setErrataViewData(populateErrataMemoViewData(source.getMemos()));

      target.setBookingRefNum(source.getBookingRefNum());

      populatePassengerData(source.getPassengers(), target);

      manageFlightViewDataPopulator.populate((FlightItinerary) source.getItinerary(), target);
      target.getFlightViewData().setWelfare(readOnly);

      if (source.getStay() != null)
      {
         availAccommViewDataPopulator.populate(source.getStay(), target);
      }

      extraFacilityCategoryViewDataPopulator.populate(source, target);
      target.setPremiumSeat(ExtraFacilityUtils.isPremiumSeatSelected(source
         .getExtraFacilityCategories()));
      target.setTotalBagAllowance(calculateTotalBaggageAllowance(source
         .getExtraFacilityCategories()));
      createExtrafacilityPaxRelation(target.getExtraFacilityCategoryViewData(),
         target.getPassenger());

      packagePriceViewDataPopulator.populate(source, target);

      // Revisit this method for due amount population
      checkForMakePayment(target);
   }

   private void createExtrafacilityPaxRelation(
      final List<ExtraFacilityCategoryViewData> extraFacilityCategories,
      final List<PassengerViewData> passenger)
   {
      for (final ExtraFacilityCategoryViewData extraFacilityCategoryViewData : extraFacilityCategories)
      {
         if (extraFacilityCategoryViewData.getExtraFacilityGroup().equalsIgnoreCase(
            ExtraFacilityGroup.FLIGHT.toString()))
         {
            for (final ExtraFacilityViewData extraViewData : extraFacilityCategoryViewData
               .getExtraFacilityViewData())
            {
               mapPaxToExtra(extraViewData, passenger);
            }
         }
      }
   }

   /**
    * @param extraViewData
    * @param passenger
    */
   private void mapPaxToExtra(final ExtraFacilityViewData extraViewData,
      final List<PassengerViewData> passenger)
   {
      for (final PassengerViewData passengerData : passenger)
      {
         for (final PassengerViewData pax : extraViewData.getPassengerViewData())
         {
            if (pax.getIdentifier().equals(passengerData.getIdentifier()))
            {
               if (MEAL.equalsIgnoreCase(extraViewData.getCategoryCode()))
               {
                  pax.getPaxExtraMappingData().setMealDescription(extraViewData.getDescription());
               }
               if (SEAT.equalsIgnoreCase(extraViewData.getCategoryCode()))
               {
                  pax.getPaxExtraMappingData().setSeatDescription(extraViewData.getDescription());
               }
               if (BAG.equalsIgnoreCase(extraViewData.getCategoryCode()))
               {
                  pax.getPaxExtraMappingData().setBaggageWeight(
                     ((BaggageExtraFacilityViewData) extraViewData).getBaggageWeight());
               }
            }
         }
      }
   }

   /**
    * @param extraFacilityCategories
    * @return integer
    */
   private int calculateTotalBaggageAllowance(
      final List<ExtraFacilityCategory> extraFacilityCategories)
   {
      for (final ExtraFacilityCategory extraFacilityCategory : extraFacilityCategories)
      {
         if (extraFacilityCategory.getExtraFacilityGroup().equals(ExtraFacilityGroup.FLIGHT))
         {
            return getTotalBaggageAllowance(extraFacilityCategory.getExtraFacilities());
         }
      }
      return 0;
   }

   /**
    * @param extraFacilities
    * @return sum
    */
   private int getTotalBaggageAllowance(final List<ExtraFacility> extraFacilities)
   {
      int sum = 0;
      for (final ExtraFacility extraFacility : extraFacilities)
      {
         if (extraFacility instanceof BaggageExtraFacility)
         {
            final BaggageExtraFacility baggageExtraFacility = (BaggageExtraFacility) extraFacility;
            sum =
               sum
                  + (baggageExtraFacility.getBaggageWeight() * baggageExtraFacility.getPassengers()
                     .size());
         }
      }

      /* Total baggage allowance for a leg */
      return sum / TWO;
   }

   /**
    * @param target
    */
   // Revisit this method for due amount population
   private void checkForMakePayment(final PackageViewData target)
   {
      final Date currentDate = new Date();

      try
      {
         final DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy");
         final String currDate = df.format(currentDate);
         final Date date = df.parse(currDate);
         final String deptDate =
            target.getFlightViewData().getOutboundSectors().get(0).getSchedule().getDepartureDate();
         Date deptDateForamtted;
         deptDateForamtted = df.parse(deptDate);
         final Money dueAmt = target.getBookingDetails().getAmountDue();
         BigDecimal dueAmount = BigDecimal.ZERO;
         if (dueAmt != null)
         {
            dueAmount = dueAmt.getAmount();
         }
         boolean flag = true;
         if (date.equals(deptDateForamtted) || date.after(deptDateForamtted)
            || (dueAmount != null && BigDecimal.ZERO.compareTo(dueAmount) >= 0))
         {

            flag = false;
         }

         target.setMakePaymentButtonStatus(flag);
      }
      catch (final ParseException e)
      {
         LOGGER.info("Pase exceptionw while formating date");
      }

   }

   /**
    * @param source
    * @param target
    */
   private void populateHighLevelBookingData(final HighLevelBookingType source,
      final PackageViewData target)
   {
      if (source != null)
      {
         final HighLevelBookingViewData bookingViewData = new HighLevelBookingViewData();
         bookingViewData.setAtolProtType(source.isAtolProtType());

         target.setHighLevelbookingViewData(bookingViewData);
      }
   }

   private List<ErrataViewData> populateErrataMemoViewData(final List<Memo> source)
   {
      final List<Memo> packageLevelErrata = source;
      final List<ErrataViewData> errataList = new ArrayList<ErrataViewData>();
      if (CollectionUtils.isNotEmpty(source))
      {
         for (final Memo memo : packageLevelErrata)
         {
            final ErrataViewData errViewData = new ErrataViewData();
            errViewData.setCode(memo.getCode());
            errViewData.setName(memo.getName());
            errViewData.setDescription(memo.getDescription());
            errViewData.setMemoType(memo.getMemoType());
            errataList.add(errViewData);
         }
      }
      return errataList;
   }

   /**
    * @param passengers
    * @param target
    */
   private void populatePassengerData(final List<Passenger> passengers, final PackageViewData target)
   {
      boolean readOnlyTest = false;
      final List<Integer> childAges = new ArrayList<Integer>();
      final List<PassengerViewData> passengerList = new ArrayList<PassengerViewData>();

      // Setting PaxViewData
      final PaxCompositionViewData paxCompositionViewData = new PaxCompositionViewData();
      target.setPaxViewData(paxCompositionViewData);

      for (final Passenger pax : passengers)
      {
         final PassengerViewData passenger = new PassengerViewData();
         if (readOnlyTest)
         {
            passenger.setWelfare(readOnly);
            readOnlyTest = true;
         }

         passenger.setAge(pax.getAge());
         passenger.setIdentifier(pax.getId());
         passenger.setTitle(WordUtils.capitalize(pax.getTitle().toLowerCase()));

         if (DR.equalsIgnoreCase(passenger.getTitle()) || DR.equalsIgnoreCase(passenger.getTitle()))
         {
            passenger.setTitle(DOCT);
         }

         else if (REV.equalsIgnoreCase(passenger.getTitle()))
         {
            passenger.setTitle(INFANT);
         }

         passenger.setLead(pax.isLead());
         if ((pax.getType().toString() != null && pax.getType().toString()
            .equalsIgnoreCase(PersonType.CHILD.getCode()))
            || (pax.getType().toString() != null && pax.getType().toString()
               .equalsIgnoreCase(PersonType.INFANT.getCode())))
         {
            childAges.add(pax.getAge());
         }
         if (pax.getType().toString() != null)
         {
            passenger.setType(pax.getType().toString());

            processAdultsCount(pax.getType().toString(), target);
         }
         // This needs to be refactored once domain is refactored
         populatePassengerNames(pax, passenger, target);

         passengerList.add(passenger);

      }
      target.getPaxViewData().setChildAges(childAges);
      final String paxComposition = getPaxComposition(target.getPaxViewData());
      target.getPaxViewData().setPaxComposition(paxComposition);
      target.setPassenger(passengerList);
   }

   /**
    * This method creates a string representing all the passengers.
    *
    * @param paxData the pax data
    * @return paxComposition
    */
   private String getPaxComposition(final PaxCompositionViewData paxData)
   {
      final StringBuilder paxComposition = new StringBuilder("");
      final int noOfAdult = paxData.getNoOfAdults();
      final int noOfSenior = paxData.getNoOfSeniors();
      final int noOfChildren = paxData.getNoOfChildren() + paxData.getNoOfInfants();

      final PaxCompositionViewData tempPaxCompData = new PaxCompositionViewData();
      tempPaxCompData.setNoOfAdults(noOfAdult);
      tempPaxCompData.setNoOfChildren(noOfChildren);
      tempPaxCompData.setNoOfSeniors(noOfSenior);
      tempPaxCompData.setChildAges(paxData.getChildAges());

      if (noOfAdult > 0)
      {
         appendAdults(paxComposition, tempPaxCompData);
      }

      if (noOfSenior > 0)
      {
         appendSeniors(paxComposition, tempPaxCompData);
      }
      if (noOfChildren > 0)
      {
         appendChildren(paxComposition, tempPaxCompData);
      }
      return paxComposition.toString();
   }

   /**
    * Appends the Seniors to the party composition.
    *
    * @param paxComposition the pax composition
    * @param paxData the pax data
    */
   private void appendSeniors(final StringBuilder paxComposition,
      final PaxCompositionViewData paxData)
   {
      final String comma = ", ";
      final int noOfSenior = paxData.getNoOfSeniors();
      final int noOfChildren = paxData.getNoOfChildren();

      if (noOfSenior > 1)
      {
         paxComposition.append(noOfSenior).append(" Seniors");
      }
      else
      {
         paxComposition.append(noOfSenior).append(" Senior");
      }
      if (noOfChildren > 0)
      {
         paxComposition.append(comma);
      }

   }

   /**
    * Appends the adults to the party composition.
    *
    * @param paxComposition the pax composition
    * @param paxData the pax data
    */
   private void appendAdults(final StringBuilder paxComposition,
      final PaxCompositionViewData paxData)
   {
      final String comma = ", ";
      final int noOfAdult = paxData.getNoOfAdults();
      final int noOfSenior = paxData.getNoOfSeniors();
      final int noOfChildren = paxData.getNoOfChildren();

      if (noOfAdult > 1)
      {
         paxComposition.append(noOfAdult).append(" Adults");
      }
      else
      {
         paxComposition.append(noOfAdult).append(" Adult");
      }
      if (noOfSenior > 0 || noOfChildren > 0)
      {
         paxComposition.append(comma);
      }
   }

   /**
    * Appends the children to the party composition.
    *
    * @param paxComposition the pax composition
    * @param paxData the pax data
    */
   @SuppressWarnings("boxing")
   private void appendChildren(final StringBuilder paxComposition,
      final PaxCompositionViewData paxData)
   {
      final String comma = ", ";
      final int noOfChildren = paxData.getNoOfChildren();
      final List<Integer> childAges = paxData.getChildAges();

      appendChildText(paxComposition, noOfChildren);
      int count = 0;
      for (final Integer integer : childAges)
      {
         paxComposition.append(integer);
         if (integer > 1)
         {
            paxComposition.append(" yrs");
         }
         else
         {
            paxComposition.append(" yr");
         }
         if (count < childAges.size() - 1)
         {
            paxComposition.append(comma);
         }
         count++;
      }
      paxComposition.append(")");
   }

   /**
    * @param paxComposition
    * @param noOfChildren
    */
   private void appendChildText(final StringBuilder paxComposition, final int noOfChildren)
   {
      if (noOfChildren > 1)
      {
         paxComposition.append(noOfChildren).append(" Children (");
      }
      else
      {
         paxComposition.append(noOfChildren).append(" Child (");
      }
   }

   private void processAdultsCount(final String type, final PackageViewData target)
   {

      if (type != null
         && (type.equalsIgnoreCase(PersonType.ADULT.getCode()) || type
            .equalsIgnoreCase(PersonType.SENIOR.getCode())))
      {
         target.getPaxViewData().setNoOfAdults(target.getPaxViewData().getNoOfAdults() + 1);
      }
      else if (type != null && type.equalsIgnoreCase(PersonType.CHILD.getCode()))
      {
         target.getPaxViewData().setNoOfChildren(target.getPaxViewData().getNoOfChildren() + 1);
      }
      else
      {
         target.getPaxViewData().setNoOfInfants(target.getPaxViewData().getNoOfInfants() + 1);
      }

   }

   /**
    * @param pax
    * @param passenger
    */
   private void populatePassengerNames(final Passenger pax, final PassengerViewData passenger,
      final PackageViewData target)
   {

      final PassengerAddressViewData passengerAddress = new PassengerAddressViewData();
      passengerAddress.setFirstName(pax.getFirstname());
      passengerAddress.setGender(pax.getGender().getCode());
      passengerAddress.setLastName(pax.getLastname());
      if (pax.getDateOfBirth() != null)
      {
         passengerAddress.setDateofbirth(DateUtils.amendnCancelDateFormat(new DateTime(pax
            .getDateOfBirth())));
      }

      if (CollectionUtils.isNotEmpty(pax.getAddresses())
         && manageSessionService.isLoggedIn(target.getBookingRefNum()))
      {
         final Address address = pax.getAddresses().get(0);
         target.setShowLeadPaxAddress(true);
         populatePassengerAddress(address, passengerAddress);

      }
      passenger.setAddress(passengerAddress);

   }

   private void populatePassengerAddress(final Address address,
      final PassengerAddressViewData passengerAddress)
   {
      passengerAddress.setCity(address.getTown());

      /**
       * checkCountryCode(address, passengerAddress);
       */
      if (iSOCountryCodeService.getCountryByAlpha2Code(address.getCountry()) != null)
      {
         passengerAddress.setCountry(iSOCountryCodeService.getCountryByAlpha2Code(
            address.getCountry()).getShortName());
      }
      passengerAddress.setEmail(address.getEmail());
      passengerAddress.setHouseNumber(address.getHouseNumber());
      passengerAddress.setPhone(address.getPhone1());
      passengerAddress.setPostalcode(address.getPostalcode());
      passengerAddress.setPostbox(address.getPobox());
      passengerAddress.setStreetName(address.getStreetname());
      passengerAddress.setRegion(address.getCounty());
   }

}
