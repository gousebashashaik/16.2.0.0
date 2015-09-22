/**
 *
 */
package uk.co.tui.fo.book.populators;

import static uk.co.tui.fo.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BaggageExtraFacility;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.config.BookFlowTUIConfigService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.fo.book.constants.BookFlowConstants;
import uk.co.tui.fo.book.view.data.BaggageExtraFacilityViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityViewData;
import uk.co.tui.fo.book.view.data.ExtraFacilityViewDataContainer;

/**
 * The Class ExtraFacilityViewDataContainerPopulator.
 *
 * @author madhumathi.m
 *
 *         The populator which populates the ExtraFacilityViewDataContainer value from
 *         InclusivePackageModel which implements the populator.
 */
public class FlightExtrasContainerPopulator implements
   Populator<List<ExtraFacility>, ExtraFacilityViewDataContainer>
{

   /** Adult weight. */
   private static final int ADULT_WEIGHT = 23;

   /** Infant weight. */
   private static final int INFANT_WEIGHT = 10;

   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;

   /** The extra facility view data populator. */
   @Resource(name = "foExtraFacilityViewDataPopulatorLite")
   private ExtraFacilityViewDataPopulatorLite extraFacilityViewDataPopulator;

   /** The infant count flag. */
   private boolean infantCountFlag;

   /** The static content utils. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private BookFlowTUIConfigService bookFlowTuiConfigService;

   /** The Constant SEATS_LA_TEXT. */
   private static final String SEATS_LA_TEXT = "Seats_LA_Text";

   /** The Constant LAI_WARNING_MESSAGE. */
   private static final String LAI_WARNING_MESSAGE = "seat_lai_warning_message";

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
         populateSeatOptions(extras, target, exFacilityCategoryCode);

         populateMealOptions(extras, target, exFacilityCategoryCode);

         populateBaggageOptions(extras, target, exFacilityCategoryCode);
      }
   }

   /**
    * Populate baggage options.
    *
    * @param extras the extras
    * @param target the target
    * @param exFacilityCategoryCode the ex facility category code
    */
   private void populateBaggageOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target, final String exFacilityCategoryCode)
   {
      if (StringUtils.equalsIgnoreCase(exFacilityCategoryCode, BookFlowConstants.BAG))
      {
         populateBaggageExtras(extras, target);
      }

   }

   /**
    * Populate meal options.
    *
    * @param extras the extras
    * @param target the target
    * @param exFacilityCategoryCode the ex facility category code
    */
   private void populateMealOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target, final String exFacilityCategoryCode)
   {
      if (StringUtils.equalsIgnoreCase(exFacilityCategoryCode, BookFlowConstants.MEAL))
      {
         target.setMealOptions(populateExtras(extras, target.getMealOptions(),
            BookFlowConstants.FLIGHT));
      }

   }

   /**
    * Populate seat options.
    *
    * @param extras the extras
    * @param target the target
    * @param exFacilityCategoryCode the ex facility category code
    */
   private void populateSeatOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target, final String exFacilityCategoryCode)
   {
      if (StringUtils.equalsIgnoreCase(exFacilityCategoryCode, BookFlowConstants.SEAT))
      {
         target.setSeatOptions(populateExtras(extras, target.getSeatOptions(),
            BookFlowConstants.FLIGHT));
      }

   }

   /**
    * Populate extras.
    *
    * @param source the source
    * @param categoryViewData the category view data
    * @param extraFacilityGroupCode the extra facility group code
    * @return the extra facility category view data
    */
   @SuppressWarnings(BOXING)
   private ExtraFacilityCategoryViewData populateExtras(final ExtraFacility source,
      final ExtraFacilityCategoryViewData categoryViewData, final String extraFacilityGroupCode)
   {
      categoryViewData.setExtraFacilityCategoryCode(source.getExtraFacilityCategory().getCode());
      categoryViewData.setExtraFacilityGroupCode(extraFacilityGroupCode);

      for (final ExtraFacilityViewData extraFacilityViewData : categoryViewData
         .getExtraFacilityViewData())
      {
         if (source.getExtraFacilityCode().equals(extraFacilityViewData.getCode()))
         {
            extraFacilityViewData.setAdultPrice(extraFacilityViewData.getAdultPrice().add(
               source.getPrices().get(0).getRate().getAmount()));
            extraFacilityViewData.setChildPrice(extraFacilityViewData.getChildPrice().add(
               source.getPrices().get(1).getRate().getAmount()));
            return categoryViewData;
         }
      }

      final ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
      extraFacilityViewDataPopulator.populate(source, extraViewData);
      // populate SEAT Extras LAI
      if (source.getExtraFacilityCategory().getCode()
         .equalsIgnoreCase(ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
      {
         populateFlightSeatsLAI(extraViewData, source.getExtraFacilityCategory());
      }
      extraViewData.setSelected(false);
      categoryViewData.getExtraFacilityViewData().add(extraViewData);
      categoryViewData.setAvailable(true);
      return categoryViewData;
   }

   /**
    * Populate baggage extras.
    *
    * @param source the source
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateBaggageExtras(final ExtraFacility source,
      final ExtraFacilityViewDataContainer target)
   {
      final ExtraFacilityCategoryViewData bagCategoryViewData = target.getBaggageOptions();
      bagCategoryViewData.setExtraFacilityCategoryCode(source.getExtraFacilityCategory().getCode());
      bagCategoryViewData.setExtraFacilityGroupCode(BookFlowConstants.FLIGHT);
      BigDecimal baggagePrice = BigDecimal.ZERO;
      for (final ExtraFacilityViewData extraFacilityViewData : bagCategoryViewData
         .getExtraFacilityViewData())
      {
         if (source.getExtraFacilityCode().equals(extraFacilityViewData.getCode()))
         {
            baggagePrice =
               extraFacilityViewData.getPrice()
                  .add(((BaggageExtraFacility) source).getPrice().getRate().getAmount())
                  .setScale(TWO, RoundingMode.HALF_UP);
            extraFacilityViewData.setPrice(baggagePrice);
            extraFacilityViewData.setCurrencyAppendedPrice(CurrencyUtils
               .getCurrencyAppendedPrice(baggagePrice));
            return;
         }
      }

      final BaggageExtraFacilityViewData extraViewData = new BaggageExtraFacilityViewData();
      final BaggageExtraFacility bagModel = (BaggageExtraFacility) source;

      // REVISIT Need to revisit the below logic

      setTotalSelectedToTrue(source, bagCategoryViewData, extraViewData);

      extraFacilityViewDataPopulator.populate(bagModel, extraViewData);

      final int weight = bagCategoryViewData.getTotalSelectedWeight();
      if (weight == 0)
      {
         infantCountFlag = true;
      }

      populateTotalWeight(bagCategoryViewData, bagModel, weight);

      bagCategoryViewData.getExtraFacilityViewData().add(extraViewData);
      bagCategoryViewData.setAvailable(true);
      target.setBaggageOptions(bagCategoryViewData);
   }

   /**
    * Sets the total selected to true.
    *
    * @param source the source
    * @param bagCategoryViewData the bag category view data
    * @param extraViewData the extra view data
    */
   private void setTotalSelectedToTrue(final ExtraFacility source,
      final ExtraFacilityCategoryViewData bagCategoryViewData,
      final BaggageExtraFacilityViewData extraViewData)
   {
      if (StringUtils.equalsIgnoreCase(bagCategoryViewData.getTotalSelectedCode(),
         source.getExtraFacilityCode()))
      {
         extraViewData.setTotalSelected(true);
      }
   }

   /**
    * Populate total weight.
    *
    * @param bagCategoryViewData the bag category view data
    * @param bagModel the bag model
    * @param weight the weight
    */
   @SuppressWarnings(BOXING)
   private void populateTotalWeight(final ExtraFacilityCategoryViewData bagCategoryViewData,
      final BaggageExtraFacility bagModel, final int weight)
   {
      if (checkPremium())
      {
         short noOfAdult = 0;
         short noOfInfant = 0;
         int totalWeight = 0;
         final List<Passenger> passengers = packageCartService.getBasePackage().getPassengers();

         for (final Passenger passenger : passengers)
         {

            if (passenger.getAge().intValue() < TWO)
            {
               noOfInfant++;

            }
            else
            {
               noOfAdult++;
            }
         }

         totalWeight = (ADULT_WEIGHT * noOfAdult) + (INFANT_WEIGHT * noOfInfant);
         bagCategoryViewData.setTotalSelectedWeight(totalWeight);

      }
      else
      {

         bagCategoryViewData
            .setTotalSelectedWeight(calculateSelectedBaggageWeight(bagModel, weight));
      }
   }

   /**
    * To calculate the total selected baggage weight.
    *
    * @param bagModel the bag model
    * @param selectedWeight the selected weight
    * @return selectedWeight
    */
   @SuppressWarnings("boxing")
   private int calculateSelectedBaggageWeight(final BaggageExtraFacility bagModel,
      final int selectedWeight)
   {

      int newSelectedWeight = selectedWeight;

      final List<Passenger> passengers = packageCartService.getBasePackage().getPassengers();
      short infantCount = 0;

      for (final Passenger passenger : passengers)
      {
         infantCount = getInfantCount(infantCount, passenger);
         newSelectedWeight = getSelectedWeight(bagModel, newSelectedWeight, passenger);
      }

      if (infantCountFlag)
      {
         while (infantCount > 0)
         {
            newSelectedWeight += bagModel.getBaggageExtraFacilityRestrictions().getInfantWeight();
            infantCount--;
         }
         infantCountFlag = false;
      }

      return newSelectedWeight;

   }

   /**
    * Gets the selected weight.
    *
    * @param bagModel the bag model
    * @param selectedWeight the selected weight
    * @param passenger the passenger
    * @return the selected weight
    */
   @SuppressWarnings(BOXING)
   private int getSelectedWeight(final BaggageExtraFacility bagModel, final int selectedWeight,
      final Passenger passenger)
   {
      int newSelectedWeight = selectedWeight;
      if (CollectionUtils.isNotEmpty(passenger.getExtraFacilities()))
      {
         for (final ExtraFacility extraFacilityModel : passenger.getExtraFacilities())
         {

            if (isBaggageCodeMatches(bagModel, extraFacilityModel))
            {

               newSelectedWeight += bagModel.getBaggageWeight();
               break;

            }

         }
      }
      return newSelectedWeight;
   }

   /**
    * Checks if is baggage code matches.
    *
    * @param bagModel the bag model
    * @param extraFacilityModel the extra facility model
    * @return true, if is baggage code matches
    */
   private boolean isBaggageCodeMatches(final BaggageExtraFacility bagModel,
      final ExtraFacility extraFacilityModel)
   {
      return bagModel.getExtraFacilityCode().equals(extraFacilityModel.getExtraFacilityCode());
   }

   /**
    * Gets the infant count.
    *
    * @param infantCount the infant count
    * @param passenger the passenger
    * @return the infant count
    */
   private short getInfantCount(final short infantCount, final Passenger passenger)
   {
      short count = infantCount;
      if (StringUtils.equals(passenger.getType().toString(), PersonType.INFANT.toString()))
      {
         count++;
      }
      return count;
   }

   /**
    * To check baggage allowance for premium seat.
    *
    * @return selectedWeight
    */
   private boolean checkPremium()
   {
      final Collection<Passenger> allAdults =
         PassengerUtils.getApplicablePassengers(
            packageCartService.getBasePackage().getPassengers(), EnumSet.of(PersonType.ADULT,
               PersonType.CHILD, PersonType.SENIOR, PersonType.SUPERSENIOR));
      for (final Passenger passenger : allAdults)
      {
         if (CollectionUtils.isNotEmpty(passenger.getExtraFacilities()))
         {
            return isPremiumCodeMathces(passenger);
         }
      }
      return false;
   }

   /**
    * Checks if is premium code mathces.
    *
    * @param passenger the passenger
    * @return true, if is premium code mathces
    */
   private boolean isPremiumCodeMathces(final Passenger passenger)
   {

      boolean codeMatches = false;
      for (final ExtraFacility extraFacilityModel : passenger.getExtraFacilities())
      {

         if (StringUtils.equalsIgnoreCase(extraFacilityModel.getExtraFacilityCode(), "XX3"))
         {
            codeMatches = true;
            break;
         }
      }
      return codeMatches;
   }

   /**
    * Populate lai view data for flight seats.
    *
    * @param viewData the view data
    */
   private void populateFlightSeatsLAI(final ExtraFacilityViewData viewData,
      final ExtraFacilityCategory category)
   {
      setSeatsLAI(viewData, category);
   }

   /**
    * Gets the seat threshold.
    *
    * @param seatCode the seat code
    * @return the seat threshold
    */
   private int getSeatsThreshold(final String seatCode)
   {

      if (StringUtils.equalsIgnoreCase(seatCode, ExtraFacilityConstants.PREMIUM_SEAT))
      {
         return bookFlowTuiConfigService.getPremiumSeatsThreshold();
      }
      else if (StringUtils.equalsIgnoreCase(seatCode, ExtraFacilityConstants.EXTRA_LEG_ROOM))
      {
         return bookFlowTuiConfigService.getExtraLegRoomSeatsThreshold();
      }
      else if (StringUtils
         .equalsIgnoreCase(seatCode, ExtraFacilityConstants.SEATS_WITH_EXTRA_SPACE))
      {
         return bookFlowTuiConfigService.getExtaSpaceSeatsThreshold();
      }
      return bookFlowTuiConfigService.getStandardSeatsThreshold();
   }

   /**
    * Fetch Flight static content.
    *
    * @return the map
    */
   private Map<String, String> fetchFlightStaticContent()
   {
      return staticContentServ.getFlightContents();
   }

   /**
    * Sets the seats lai.
    *
    * @param seat the seat
    */
   private void setSeatsLAI(final ExtraFacilityViewData seat, final ExtraFacilityCategory category)
   {
      final int seatThreshold = getSeatsThreshold(seat.getCode());
      final ExtraFacility extraFacility = getSeatExtraFacility(category, seat.getCode());
      final int availableQuantity = getExtraFacilityQuantity(extraFacility);

      if (availableQuantity > 0 && availableQuantity <= seatThreshold)
      {
         seat.setLimitedAvailability(true);
         seat.setLimitedAvailabilityText(fetchFlightStaticContent().get(SEATS_LA_TEXT));
         seat.setSeatWarningMessage(getSeatsLaiWarningMessage(LAI_WARNING_MESSAGE,
            String.valueOf(availableQuantity)));
      }
   }

   /**
    * Gets the extra facility quantity.
    *
    * @param extraFacility the extra facility
    * @return the extra facility quantity
    */
   private int getExtraFacilityQuantity(final ExtraFacility extraFacility)
   {
      if (extraFacility != null && nonStandardSeatOptions(extraFacility))
      {
         return extraFacility.getQuantity().intValue();
      }
      return packageComponentService.getFlightItinerary(getCartPackageModel()).getMinAvail();
   }

   /**
    * Gets the cart's package model.
    *
    * @return BasePackage
    */
   private BasePackage getCartPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * Non standard seat options.
    *
    * @param extraFacility the extra facility
    * @return true, if successful
    */
   private boolean nonStandardSeatOptions(final ExtraFacility extraFacility)
   {
      return StringUtils.equals(extraFacility.getExtraFacilityCode(),
         ExtraFacilityConstants.PREMIUM_SEAT)
         || StringUtils.equals(extraFacility.getExtraFacilityCode(),
            ExtraFacilityConstants.EXTRA_LEG_ROOM)
         || StringUtils.equals(extraFacility.getExtraFacilityCode(),
            ExtraFacilityConstants.SEATS_WITH_EXTRA_SPACE);
   }

   /**
    * Gets the seat extra facility.
    *
    * @return the seat extra facility
    */
   private ExtraFacility getSeatExtraFacility(final ExtraFacilityCategory category,
      final String seatCode)
   {
      for (final ExtraFacility extraFacility : category.getExtraFacilities().get(0)
         .getExtraFacilityCategory().getExtraFacilities())
      {
         if (StringUtils.equals(extraFacility.getExtraFacilityCode(), seatCode))
         {
            return extraFacility;
         }
      }
      return null;
   }

   /**
    * Gets the seats lai warning message.
    *
    * @param key the key
    * @param value the value
    * @return the seats lai warning message
    */
   private String getSeatsLaiWarningMessage(final String key, final String... value)
   {
      if (key != null)
      {
         final String flightExtrasContent = fetchFlightStaticContent().get(key);
         if (StringUtils.isNotEmpty(flightExtrasContent))
         {
            if (SyntacticSugar.isNull(value))
            {
               return flightExtrasContent;
            }
            return StringUtil.substitute(flightExtrasContent, value);
         }
      }
      return StringUtils.EMPTY;
   }

}
