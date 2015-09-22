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
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.cruise.search.data.MainSearchCriteria;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiCenterHotelResultsPageUrlBuilder.
 *
 * @author suniljoshi.h
 */
public class MultiCenterHotelResultsPageUrlBuilder extends MultiCenterPageUrlBuilder
{

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The Constant EQUALS. */
   private static final String EQUALS = "=";

   /** The Constant AND. */
   private static final String AND = "&";

   /** The Constant QUESTION_MARK. */
   private static final String QUESTION_MARK = "?";

   /** The Constant ITINERARY_CODE_ONE. */
   private static final String ITINERARY_CODE_ONE = "itineraryCodeOne";

   /** The Constant ITINERARY_CODE_TWO. */
   private static final String ITINERARY_CODE_TWO = "itineraryCodeTwo";

   /** The Constant SHIP_CODE. */
   private static final String SHIP_CODE = "shipCode";

   /** The Constant MULTI_CENTRE. */
   private static final String MULTI_CENTRE = "mc";

   /** The Constant MULTI_CENTRE_TRACS. */
   private static final String MULTI_CENTRE_TRACS = "isMCTracs";

   /** The Constant STAY_BEFORE. */
   private static final String STAY_BEFORE = "isStayBefore";

   /** The Constant STAY_DURATION. */
   private static final String STAY_DURATION = "stayDuration";

   /** The Constant INVENTORY_PACKAGE_ID. */
   private static final String INVENTORY_PACKAGE_ID = "invPkgId";

   /** The Constant CHILDREN. */
   private static final String CHILDREN = "noOfChildren";

   /** The Constant CHILDREN_AGE. */
   private static final String CHILDREN_AGE = "childrenAge";

   /** The Constant DURATION. */
   private static final String DURATION = "duration";

   /** The Constant FROM. */
   private static final String FROM = "from[]";

   /** The Constant TO. */
   private static final String TO = "to[]";

   /** The Constant PIPE. */
   private static final String PIPE = "|";

   /** The Constant COLON. */
   private static final String COLON = ":";

   /** The Constant IS_FLEXIBLE. */
   private static final String IS_FLEXIBLE = "flexibility";

   /** The Constant IS_SHIP_TAB. */
   private static final String IS_SHIP_TAB = "isShipTab";

   /** The Constant CRUISE_DURATION. */
   private static final String CRUISE_DURATION = "cruiseDuration";

   /** The Constant ADD_STAY. */
   private static final String ADD_STAY = "addAStay";

   /** The Constant WHEN. */
   private static final String WHEN = "when";

   /** The Constant SAILING_DATE. */
   private static final String SAILING_DATE = "sailingDate";

   /** The Constant INDEX_ZERO. */
   private static final int INDEX_ZERO = 0;

   /** The Constant INDEX_ONE. */
   private static final int INDEX_ONE = 1;

   /** The Constant ADULTS. */
   private static final String ADULTS = "noOfAdults";

   /** The Constant PACKAGE_ID. */
   private static final String PACKAGE_ID = "packageId";

   /** The Constant INDEX. */
   private static final String INDEX = "index";

   /** The Constant BRAND_TYPE. */
   private static final String BRAND_TYPE = "brandType";

   /** The Constant DEPARTURE_PORT. */
   private static final String DEPARTURE_PORT = "dp";

   /** The Constant BOARD_BASIS. */
   private static final String BOARD_BASIS = "bb";

   /** The Constant ATCOM_MC. */
   private static final String ATCOM_MC = "isAtcomMc";

   /** The Constant MC_ID. */
   private static final String MC_ID = "mcId";

   /** The Constant IN_BOUND_ATCOM_ID. */
   private static final String IN_BOUND_ATCOM_ID = "tra_i";

   /** The Constant OUT_BOUND_ATCOM_ID. */
   private static final String OUT_BOUND_ATCOM_ID = "tra_o";

   /** The Constant SEARCH_VARIANT. */
   private static final String SEARCH_VARIANT = "searchVariant";

   /** The Constant ROOMS. */
   private static final String ROOMS = "room";

   /**
    * To construct the Hotels page url from the current cart package model.
    *
    * @return the url
    */
   @Override
   public String build()
   {

      final BasePackage pkgModel = getPackageModel();
      final Stay stay = packageComponentService.getCruise(pkgModel);
      final MainSearchCriteria mainSearchCriteria =
         (MainSearchCriteria) sessionService
            .getAttribute(EndecasearchConstants.MAIN_SEARCH_CRITERIA);
      final Itinerary itinerary = packageComponentService.getFlightItinerary(pkgModel);
      final List<Leg> outBoundLegList = itinerary.getOutBound();

      final StringBuilder hotelPageUrl = new StringBuilder("/cruise/packages/hotels");
      hotelPageUrl.append(QUESTION_MARK).append(AND);
      hotelPageUrl.append(ITINERARY_CODE_ONE).append(EQUALS)
         .append(((Cruise) stay).getJourneyCode()).append(AND);
      hotelPageUrl.append(ITINERARY_CODE_TWO).append(EQUALS).append("").append(AND);
      hotelPageUrl.append(SHIP_CODE).append(EQUALS).append(stay.getCode()).append(AND);
      hotelPageUrl.append(MULTI_CENTRE).append(EQUALS).append(true).append(AND);
      hotelPageUrl.append(MULTI_CENTRE_TRACS).append(EQUALS).append(false).append(AND);
      hotelPageUrl.append(STAY_BEFORE).append(EQUALS)
         .append(isStayBefore(pkgModel.getPackageType())).append(AND);
      hotelPageUrl.append(STAY_DURATION).append(EQUALS)
         .append(mainSearchCriteria.getStayDuration()).append(AND);
      hotelPageUrl.append(INVENTORY_PACKAGE_ID).append(EQUALS).append(getPackageId(pkgModel))
         .append(AND);
      hotelPageUrl
         .append(ADULTS)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.ADULT))).append(AND);
      hotelPageUrl
         .append(CHILDREN)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.CHILD, PersonType.INFANT))).append(AND);

      hotelPageUrl.append(CHILDREN_AGE).append(EQUALS)
         .append(getChildrenAgeAsString(PassengerUtils.getChildAges(pkgModel.getPassengers())))
         .append(AND);
      hotelPageUrl.append(DURATION).append(EQUALS).append(mainSearchCriteria.getDuration())
         .append(AND);

      hotelPageUrl.append(FROM).append(EQUALS)
         .append(getFromDatas(mainSearchCriteria.getUkAirports(), mainSearchCriteria.getUkPOCs()))
         .append(AND);
      hotelPageUrl.append(IS_FLEXIBLE).append(EQUALS).append(mainSearchCriteria.isFlexibility())
         .append(AND);
      hotelPageUrl.append(IS_SHIP_TAB).append(EQUALS).append(false).append(AND);
      hotelPageUrl.append(CRUISE_DURATION).append(EQUALS).append(getCruiseDuration(pkgModel))
         .append(AND);
      hotelPageUrl.append(ADD_STAY).append(EQUALS).append(getStayDuration(pkgModel)).append(COLON)
         .append(getStayType(pkgModel.getPackageType())).append(AND);

      hotelPageUrl.append(WHEN).append(EQUALS).append(mainSearchCriteria.getWhen()).append(AND);
      hotelPageUrl.append(SAILING_DATE).append(EQUALS).append(getDepartureDate(outBoundLegList))
         .append(AND);

      hotelPageUrl.append(TO).append(EQUALS).append(getTODatas(mainSearchCriteria.getTo()))
         .append(AND);
      hotelPageUrl.append(PACKAGE_ID).append(EQUALS).append(getPackageId(pkgModel)).append(AND);
      hotelPageUrl.append(INDEX).append(EQUALS).append(INDEX_ONE).append(AND);
      hotelPageUrl.append(BRAND_TYPE).append(EQUALS).append(pkgModel.getBrandType().toString())
         .append(AND);
      hotelPageUrl.append(ROOMS).append(EQUALS).append(getRoomParam(pkgModel)).append(AND);
      hotelPageUrl.append(DEPARTURE_PORT).append(EQUALS)
         .append(getDepartureAirportCode(outBoundLegList)).append(AND);
      hotelPageUrl.append(BOARD_BASIS).append(EQUALS)
         .append(stay.getRooms().get(INDEX_ZERO).getBoardBasis().getCode()).append(AND);
      hotelPageUrl.append(ATCOM_MC).append(EQUALS).append(true).append(AND);
      hotelPageUrl.append(MC_ID).append(EQUALS)
         .append(StringUtils.removeStart(((Cruise) stay).getJourneyCode(), "0")).append(PIPE)
         .append(pkgModel.getBrandType().getId()).append(AND);
      hotelPageUrl.append(IN_BOUND_ATCOM_ID).append(EQUALS).append(getInBoundAtComId(pkgModel))
         .append(AND);
      hotelPageUrl.append(OUT_BOUND_ATCOM_ID).append(EQUALS).append(getOutBoundAtComId(pkgModel))
         .append(AND);
      hotelPageUrl.append(SEARCH_VARIANT).append(EQUALS)
         .append(getSearchVariantHotel(pkgModel.getPackageType()));

      return hotelPageUrl.toString();
   }

}
