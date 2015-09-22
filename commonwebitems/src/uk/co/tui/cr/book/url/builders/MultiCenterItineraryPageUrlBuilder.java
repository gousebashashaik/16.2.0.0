/**
 *
 */
package uk.co.tui.cr.book.url.builders;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.constants.EndecasearchConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.MultiCenterHolidayPackage;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.cruise.search.data.MainSearchCriteria;

/**
 * @author uday.g
 *
 */
public class MultiCenterItineraryPageUrlBuilder extends MultiCenterPageUrlBuilder
{

   /**
     *
     */
   private static final String PIPE = "|";

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   private static final String MC_ID = "mcId";

   private static final String ATCOM_MC = "isAtcomMc";

   private static final String ADD_STAY = "addAStay";

   private static final String SHIP_CODE = "shipCode";

   private static final String ITINERARY_CODE_ONE = "itineraryCodeOne";

   private static final String ITINERARY_CODE_TWO = "itineraryCodeTwo";

   private static final String IN_BOUND_ATCOM_ID = "tra_i";

   private static final String OUT_BOUND_ATCOM_ID = "tra_o";

   private static final String MULTI_CENTRE = "mc";

   private static final String MULTI_CENTRE_TRACS = "isMCTracs";

   private static final String STAY_BEFORE = "isStayBefore";

   private static final String DURATION = "duration";

   private static final String STAY_DURATION = "stayDuration";

   private static final String PACKAGE_ID = "packageId";

   private static final String INVENTORY_PACKAGE_ID = "invPkgId";

   private static final String SENIORS = "noOfSeniors";

   private static final String ADULTS = "noOfAdults";

   private static final String CHILDREN = "noOfChildren";

   private static final String CHILDREN_AGE = "childrenAge";

   private static final String FROM = "from[]";

   private static final String TO = "to[]";

   private static final String DEPARTURE_PORT = "dp";

   private static final String IS_FLEXIBLE = "flexibility";

   private static final String WHEN = "when";

   private static final String SAILING_DATE = "sailingDate";

   private static final String INDEX = "index";

   private static final String BRAND_TYPE = "brandType";

   private static final String BOARD_BASIS = "bb";

   private static final String SEARCH_VARIANT = "searchVariant";

   private static final String CRUISE_DURATION = "cruiseDuration";

   private static final String AND = "&";

   private static final String EQUALS = "=";

   private static final String COLON = ":";

   private static final String HYPHEN = "-";

   private static final String QUESTION_MARK = "?";

   private static final int INDEX_ZERO = 0;

   private static final int INDEX_ONE = 1;

   private static final String ANITE_MC = "AMC";

   /**
    * The TWO
    */
   private static final int TWO = 2;

   /**
    * To construct the book flow accommodation page url from the current cart package model.
    *
    * @return the url
    */
   @Override
   public String build()
   {
      final BasePackage pkgModel = getPackageModel();
      final Stay stay = packageComponentService.getCruise(pkgModel);
      final Stay cruiseItineraryTwo = packageComponentService.getCruise(pkgModel, TWO);

      final StringBuilder itineraryPageUrl = new StringBuilder("/cruise/bookitineraries/");
      itineraryPageUrl.append(((Cruise) stay).getJourneyName()).append(HYPHEN)
         .append(getSecondCruiseItineraryJourneyName(cruiseItineraryTwo)).append(HYPHEN)
         .append(((Cruise) stay).getJourneyCode()).append(QUESTION_MARK);
      itineraryPageUrl.append(SHIP_CODE).append(EQUALS).append(stay.getCode()).append(AND);
      itineraryPageUrl.append(ITINERARY_CODE_ONE).append(EQUALS)
         .append(((Cruise) stay).getJourneyCode()).append(AND);
      itineraryPageUrl.append(ITINERARY_CODE_TWO).append(EQUALS)
         .append(getSecondCruiseItineraryCode(cruiseItineraryTwo)).append(AND);

      itineraryPageUrl.append(MULTI_CENTRE_TRACS).append(EQUALS).append(false).append(AND);

      itineraryPageUrl.append(STAY_BEFORE).append(EQUALS)
         .append(isStayBefore(pkgModel.getPackageType())).append(AND);

      addAStay(pkgModel, itineraryPageUrl);

      itineraryPageUrl.append(ATCOM_MC).append(EQUALS).append(true).append(AND);
      itineraryPageUrl.append(MC_ID).append(EQUALS)
         .append(StringUtils.removeStart(((Cruise) stay).getJourneyCode(), "0")).append(PIPE)
         .append(pkgModel.getBrandType().getId()).append(AND);
      itineraryPageUrl.append(IN_BOUND_ATCOM_ID).append(EQUALS).append(getInBoundAtComId(pkgModel))
         .append(AND);
      itineraryPageUrl.append(OUT_BOUND_ATCOM_ID).append(EQUALS)
         .append(getOutBoundAtComId(pkgModel)).append(AND);

      itineraryPageUrl
         .append(SENIORS)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.SENIOR))).append(AND);
      itineraryPageUrl
         .append(ADULTS)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.ADULT))).append(AND);
      itineraryPageUrl
         .append(CHILDREN)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.CHILD, PersonType.INFANT))).append(AND);
      itineraryPageUrl.append(CHILDREN_AGE).append(EQUALS)
         .append(getChildrenAgeAsString(PassengerUtils.getChildAges(pkgModel.getPassengers())))
         .append(AND);

      updateUrlWithSearchCriteria(itineraryPageUrl);

      final Itinerary itinerary = packageComponentService.getFlightItinerary(pkgModel);
      final List<Leg> outBoundLegList = itinerary.getOutBound();

      itineraryPageUrl.append(SAILING_DATE).append(EQUALS)
         .append(getDepartureDate(outBoundLegList)).append(AND);

      itineraryPageUrl.append(PACKAGE_ID).append(EQUALS).append(getPackageId(pkgModel)).append(AND);
      itineraryPageUrl.append(INDEX).append(EQUALS).append(INDEX_ONE).append(AND);
      itineraryPageUrl.append(BRAND_TYPE).append(EQUALS).append(pkgModel.getBrandType().toString())
         .append(AND);
      itineraryPageUrl.append(DEPARTURE_PORT).append(EQUALS)
         .append(getDepartureAirportCode(outBoundLegList)).append(AND);
      // TODO : Identify source for this
      itineraryPageUrl.append(SEARCH_VARIANT).append(EQUALS)
         .append(getSearchVariant(pkgModel.getPackageType())).append(AND);
      itineraryPageUrl.append(INVENTORY_PACKAGE_ID).append(EQUALS).append(AND);

      itineraryPageUrl.append(BOARD_BASIS).append(EQUALS)
         .append(stay.getRooms().get(INDEX_ZERO).getBoardBasis().getCode()).append(AND);

      getAniteMCId(pkgModel, itineraryPageUrl);

      return itineraryPageUrl.toString();
   }

   /**
    * Gets the second cruise itinerary journey name.
    *
    * @param cruiseItineraryTwo
    * @return
    */
   private String getSecondCruiseItineraryJourneyName(final Stay cruiseItineraryTwo)
   {
      if (cruiseItineraryTwo != null)
      {
         return ((Cruise) cruiseItineraryTwo).getJourneyName();
      }
      return StringUtils.EMPTY;
   }

   /**
    * Gets the second cruise itinerary code.
    *
    * @param cruiseItineraryTwo
    * @return
    */
   private String getSecondCruiseItineraryCode(final Stay cruiseItineraryTwo)
   {
      if (cruiseItineraryTwo != null)
      {
         return ((Cruise) cruiseItineraryTwo).getJourneyCode();
      }
      return StringUtils.EMPTY;
   }

   /**
    * @param pkgModel
    * @param itineraryPageUrl
    */
   private void addAStay(final BasePackage pkgModel, final StringBuilder itineraryPageUrl)
   {
      if (pkgModel.getPackageType() == PackageType.BTB)
      {
         itineraryPageUrl.append(ADD_STAY).append(EQUALS).append(getStayDuration(pkgModel))
            .append(AND);
         itineraryPageUrl.append(MULTI_CENTRE).append(EQUALS).append(false).append(AND);

      }
      else
      {
         itineraryPageUrl.append(ADD_STAY).append(EQUALS).append(getStayDuration(pkgModel))
            .append(COLON).append(getStayType(pkgModel.getPackageType())).append(AND);
         itineraryPageUrl.append(MULTI_CENTRE).append(EQUALS).append(true).append(AND);
      }

   }

   private void getAniteMCId(final BasePackage pkgModel, final StringBuilder itineraryPageUrl)
   {
      if (pkgModel.getPackageType() != PackageType.BTB)
      {
         itineraryPageUrl
            .append(ANITE_MC)
            .append(EQUALS)
            .append(
               ((MultiCenterHolidayPackage) pkgModel).getSupplierProductDetails().getProductCode());
      }
   }

   /**
    * @param itineraryPageUrl
    */
   private void updateUrlWithSearchCriteria(final StringBuilder itineraryPageUrl)
   {
      final MainSearchCriteria mainSearchCriteria =
         (MainSearchCriteria) sessionService
            .getAttribute(EndecasearchConstants.MAIN_SEARCH_CRITERIA);

      itineraryPageUrl.append(WHEN).append(EQUALS).append(mainSearchCriteria.getWhen()).append(AND);
      itineraryPageUrl.append(DURATION).append(EQUALS).append(mainSearchCriteria.getDuration())
         .append(AND);
      itineraryPageUrl.append(IS_FLEXIBLE).append(EQUALS)
         .append(mainSearchCriteria.isFlexibility()).append(AND);
      // TODO : Revisit this
      itineraryPageUrl.append(FROM).append(EQUALS)
         .append(getFromDatas(mainSearchCriteria.getUkAirports(), mainSearchCriteria.getUkPOCs()))
         .append(AND);
      // TODO : Revisit this
      itineraryPageUrl.append(TO).append(EQUALS).append(getTODatas(mainSearchCriteria.getTo()))
         .append(AND);

      itineraryPageUrl.append(CRUISE_DURATION).append(EQUALS)
         .append(mainSearchCriteria.getCruiseDuration()).append(AND);

      // TODO : Identify source for this
      itineraryPageUrl.append(STAY_DURATION).append(EQUALS)
         .append(mainSearchCriteria.getStayDuration()).append(AND);

   }

}
