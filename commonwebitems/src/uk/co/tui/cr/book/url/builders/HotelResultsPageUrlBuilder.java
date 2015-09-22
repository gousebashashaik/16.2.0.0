/**
 *
 */
package uk.co.tui.cr.book.url.builders;

import de.hybris.platform.servicelayer.session.SessionService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.constants.EndecasearchConstants;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.cruise.search.data.MainSearchCriteria;

/**
 * @author suniljoshi.h
 *
 */
public class HotelResultsPageUrlBuilder
{
   
   @Resource
   private PackageCartService packageCartService;
   
   /** The session service. */
   @Resource
   private SessionService sessionService;
   
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;
   
   private static final String EQUALS = "=";
   
   private static final String AND = "&";
   
   private static final String QUESTION_MARK = "?";
   
   private static final String ITINERARY_CODE_ONE = "itineraryCodeOne";
   
   private static final String ITINERARY_CODE_TWO = "itineraryCodeTwo";
   
   private static final String SHIP_CODE = "shipCode";
   
   private static final String MULTI_CENTRE = "mc";
   
   private static final String MULTI_CENTRE_TRACS = "isMCTracs";
   
   private static final String STAY_BEFORE = "isStayBefore";
   
   private static final String STAY_DURATION = "stayDuration";
   
   private static final String INVENTORY_PACKAGE_ID = "invPkgId";
   
   private static final String CHILDREN = "noOfChildren";
   
   private static final String CHILDREN_AGE = "childrenAge";
   
   private static final String DURATION = "duration";
   
   private static final String FROM = "from[]";
   
   private static final String PIPE = "|";
   
   private static final String COLON = ":";
   
   private static final String IS_FLEXIBLE = "flexibility";
   
   private static final String IS_SHIP_TAB = "isShipTab";
   
   private static final String CRUISE_DURATION = "cruiseDuration";
   
   private static final String ADD_STAY = "addAStay";
   
   private static final String CRUISE_STAY = "cs";
   
   private static final String WHEN = "when";
   
   private static final String SAILING_DATE = "sailingDate";
   
   private static final int INDEX_ZERO = 0;
   
   private static final int INDEX_ONE = 1;
   
   private static final TUILogUtils LOG = new TUILogUtils("HotelPageUrlBuilder");
   
   private static final int FLIGHT_SUPPLIER_CODE_LENGTH = 3;
   
   private static final String ADULTS = "noOfAdults";
   
   private static final String PACKAGE_ID = "packageId";
   
   private static final String INDEX = "index";
   
   private static final String BRAND_TYPE = "brandType";
   
   private static final String DEPARTURE_PORT = "dp";
   
   private static final String BOARD_BASIS = "bb";
   
   private static final String ATCOM_MC = "isAtcomMc";
   
   private static final String MC_ID = "mcId";
   
   private static final String IN_BOUND_ATCOM_ID = "tra_i";
   
   private static final String OUT_BOUND_ATCOM_ID = "tra_o";
   
   private static final String SEARCH_VARIANT = "searchVariant";
   
   /**
    * To construct the Hotels page url from the current cart package model.
    *
    * @return the url
    */
   public String getHotelResultsPageUrl()
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
      hotelPageUrl.append(STAY_BEFORE).append(EQUALS).append(false).append(AND);
      hotelPageUrl.append(STAY_DURATION).append(EQUALS)
         .append(mainSearchCriteria.getStayDuration()).append(AND);
      hotelPageUrl.append(INVENTORY_PACKAGE_ID).append(EQUALS)
         .append(getInventoryPackageId(stay, itinerary, getSellingCode(pkgModel))).append(AND);
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
      hotelPageUrl.append(DURATION).append(EQUALS).append(pkgModel.getDuration()).append(AND);
      
      hotelPageUrl.append(FROM).append(EQUALS)
         .append(getFromDatas(mainSearchCriteria.getUkAirports(), mainSearchCriteria.getUkPOCs()))
         .append(AND);
      hotelPageUrl.append(IS_FLEXIBLE).append(EQUALS).append(true).append(AND);
      hotelPageUrl.append(IS_SHIP_TAB).append(EQUALS).append(false).append(AND);
      hotelPageUrl.append(CRUISE_DURATION).append(EQUALS).append(getCruiseDuration(pkgModel))
         .append(AND);
      hotelPageUrl.append(ADD_STAY).append(EQUALS).append(getStayDuration(pkgModel)).append(COLON)
         .append(CRUISE_STAY).append(AND);
      
      hotelPageUrl.append(WHEN).append(EQUALS).append(getDepartureDate(outBoundLegList))
         .append(AND);
      hotelPageUrl.append(SAILING_DATE).append(EQUALS).append(getDepartureDate(outBoundLegList))
         .append(AND);
      
      // REVISIT: TO[] should be appended
      hotelPageUrl.append(PACKAGE_ID).append(EQUALS).append(getPackageId(pkgModel)).append(AND);
      hotelPageUrl.append(INDEX).append(EQUALS).append(INDEX_ONE).append(AND);
      hotelPageUrl.append(BRAND_TYPE).append(EQUALS).append(pkgModel.getBrandType().toString())
         .append(AND);
      hotelPageUrl.append(DEPARTURE_PORT).append(EQUALS)
         .append(getDepartureAirportCode(outBoundLegList)).append(AND);
      hotelPageUrl.append(BOARD_BASIS).append(EQUALS)
         .append(stay.getRooms().get(INDEX_ZERO).getBoardBasis().getCode()).append(AND);
      hotelPageUrl.append(ATCOM_MC).append(EQUALS).append(true).append(AND);
      hotelPageUrl.append(MC_ID).append(EQUALS).append(((Cruise) stay).getJourneyCode())
         .append(AND);
      hotelPageUrl.append(IN_BOUND_ATCOM_ID).append(EQUALS).append(getInBoundAtComId(pkgModel))
         .append(AND);
      hotelPageUrl.append(OUT_BOUND_ATCOM_ID).append(EQUALS).append(getOutBoundAtComId(pkgModel))
         .append(AND);
      hotelPageUrl.append(SEARCH_VARIANT).append(EQUALS)
         .append(getSearchVariant(pkgModel.getPackageType()));
      
      return hotelPageUrl.toString();
      
   }
   
   /**
    * @param packageType
    * @return searchVariant
    */
   private String getSearchVariant(final PackageType packageType)
   {
      String searchVariant = StringUtils.EMPTY;
      if (packageType == PackageType.CNS)
      {
         searchVariant = "CRUISE_STAY_ATCOM_HOTEL";
      }
      else if (packageType == PackageType.CNS)
      {
         searchVariant = "STAY_CRUISE_ATCOM_HOTEL";
      }
      return searchVariant;
   }
   
   /**
    * @param pkgModel
    */
   private String getOutBoundAtComId(final BasePackage pkgModel)
   {
      final Itinerary flightItinerary = packageComponentService.getFlightItinerary(pkgModel);
      return ((FlightLeg) flightItinerary.getOutBound().get(0)).getAtcomId();
      
   }
   
   /**
    * Gets the in bound at com id.
    *
    * @param pkgModel the pkg model
    * @return the in bound at com id
    */
   private String getInBoundAtComId(final BasePackage pkgModel)
   {
      final Itinerary flightItinerary = packageComponentService.getFlightItinerary(pkgModel);
      return ((FlightLeg) flightItinerary.getInBound().get(0)).getAtcomId();
      
   }
   
   /**
    * Gets the departure airport code.
    *
    * @param outBoundLegList the out bound leg list
    * @return the departure airport code
    */
   private String getDepartureAirportCode(final List<Leg> outBoundLegList)
   {
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return outBoundLegList.get(0).getDepartureAirport().getCode();
      }
      return "";
   }
   
   /**
    * To return the DepartureDate.
    *
    * @param outBoundLegList the out bound leg list
    * @return Departure Date
    */
   private String getDepartureDate(final List<Leg> outBoundLegList)
   {
      final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy");
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return dateFormat.format(outBoundLegList.get(0).getSchedule().getDepartureDate());
      }
      
      return "";
   }
   
   /**
    * Gets the stay duration.
    *
    * @param pkgModel the pkg model
    * @return the stay duration
    */
   private String getStayDuration(final BasePackage pkgModel)
   {
      final Stay hotelAccom = packageComponentService.getHotel(pkgModel);
      return String.valueOf(hotelAccom.getDuration());
   }
   
   /**
    * Gets the cruise duration
    *
    * @param pkgModel
    *
    * @return cruise duration
    */
   private String getCruiseDuration(final BasePackage pkgModel)
   {
      return String.valueOf(packageComponentService.getCruise(pkgModel).getDuration());
   }
   
   /**
    * Gets From the data
    *
    * @param Airports
    *
    * @return from
    */
   private Object getFromDatas(final List<String> ukAirports, final List<String> ukPOCs)
   {
      final StringBuilder from = new StringBuilder("");
      String pipe = "";
      for (final String airport : ukAirports)
      {
         from.append(pipe).append(airport).append(COLON).append("Airport");
         pipe = PIPE;
      }
      for (final String port : ukPOCs)
      {
         from.append(pipe).append(port).append(COLON).append("PORT");
         pipe = PIPE;
      }
      return from.toString();
   }
   
   /**
    * @param sellingCode
    *
    * @param itinerary
    *
    * @param stay
    */
   private String getInventoryPackageId(final Stay stay, final Itinerary itinerary,
      final String sellingCode)
   {
      final StringBuilder uniquePackageId = new StringBuilder();
      
      uniquePackageId.append(sellingCode);
      updateWithOutboundData(itinerary, uniquePackageId);
      updateWithInboundData(itinerary, uniquePackageId);
      // This condition is added as Single accom search is giving Packages
      // with only 'RoomType' attribute as the change.
      final List<Room> rooms = stay.getRooms();
      if (CollectionUtils.isNotEmpty(rooms))
      {
         for (final Room room : rooms)
         {
            uniquePackageId.append(room.getCode());
         }
      }
      return uniquePackageId.toString();
   }
   
   /**
    * @param itinerary
    * @param uniquePackageId
    */
   @SuppressWarnings("deprecation")
   private void updateWithOutboundData(final Itinerary itinerary,
      final StringBuilder uniquePackageId)
   {
      // Assuming flights have no hops.
      if (CollectionUtils.isNotEmpty(itinerary.getOutBound()))
      {
         uniquePackageId.append(itinerary.getOutBound().get(0).getSchedule().getDepartureDate()
            .getSeconds());
         uniquePackageId.append(itinerary.getOutBound().get(0).getSchedule().getArrivalDate()
            .getSeconds());
         if (StringUtils.isNotEmpty(itinerary.getOutBound().get(0).getCarrier().getCode()))
         {
            uniquePackageId.append(StringUtils.substring(
               itinerary.getOutBound().get(0).getCarrier().getCode(), FLIGHT_SUPPLIER_CODE_LENGTH,
               itinerary.getOutBound().get(0).getCarrier().getCode().length()).trim());
         }
      }
   }
   
   /**
    * @param itinerary
    * @param uniquePackageId
    */
   @SuppressWarnings("deprecation")
   private void updateWithInboundData(final Itinerary itinerary, final StringBuilder uniquePackageId)
   {
      if (CollectionUtils.isNotEmpty(itinerary.getInBound()))
      {
         uniquePackageId.append(itinerary.getInBound().get(0).getSchedule().getDepartureDate()
            .getSeconds());
         uniquePackageId.append(itinerary.getInBound().get(0).getSchedule().getArrivalDate()
            .getSeconds());
         if (StringUtils.isNotEmpty(itinerary.getInBound().get(0).getCarrier().getCode()))
         {
            uniquePackageId.append(StringUtils.substring(
               itinerary.getInBound().get(0).getCarrier().getCode(), FLIGHT_SUPPLIER_CODE_LENGTH,
               itinerary.getInBound().get(0).getCarrier().getCode().length()).trim());
         }
      }
   }
   
   /**
    * The method fetches the package model from the cart.
    *
    * @return BasePackage
    */
   private BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }
   
   /**
    * To construct the package id.
    *
    * @param pkgModel the pkg model
    * @return packageId
    */
   private String getPackageId(final BasePackage pkgModel)
   {
      
      final StringBuilder flightInfoCode = new StringBuilder("");
      
      final StringBuilder packageId = new StringBuilder();
      
      final Stay stay = packageComponentService.getStay(pkgModel);
      final Itinerary itinerary = packageComponentService.getFlightItinerary(pkgModel);
      final List<Leg> outBoundLegList = itinerary.getOutBound();
      final List<Leg> inBoundLegList = itinerary.getInBound();
      
      addSectorInformation(flightInfoCode, outBoundLegList);
      addSectorInformation(flightInfoCode, inBoundLegList);
      
      packageId.append(flightInfoCode).append(getSellingCode(pkgModel));
      packageId.append(getRoomCode(stay.getRooms()));
      
      LOG.debug("Package ID generated for given Accommodation");
      return packageId.toString();
      
   }
   
   /**
    * To append the departure date and time milliseconds and arrival date and time milliseconds and
    * carrier code.
    *
    * @param flightInfoCode the flight info code
    * @param outBoundLegList the out bound leg list
    */
   private void addSectorInformation(final StringBuilder flightInfoCode,
      final List<Leg> outBoundLegList)
   {
      
      for (final Leg legModel : outBoundLegList)
      {
         
         if (null != legModel.getSchedule())
         {
            flightInfoCode.append(
               String.valueOf(legModel.getSchedule().getDepartureDate().getTime())).append(
               String.valueOf(legModel.getSchedule().getArrivalDate().getTime()));
            final String flightNum = legModel.getCarrier().getNumber();
            if (StringUtils.isNotEmpty(flightNum))
            {
               final StringBuilder flightCode = new StringBuilder("");
               flightCode.append(legModel.getCarrier().getCode()).append(
                  legModel.getCarrier().getNumber());
               flightInfoCode.append(flightCode);
            }
            
         }
         
      }
   }
   
   /**
    * To get selling code.
    *
    * @param pkgModel the pkg model
    * @return selling code
    */
   private String getSellingCode(final BasePackage pkgModel)
   {
      return (pkgModel.getInventory().getSupplierProductDetails()).getProductCode();
   }
   
   /**
    * To get room code.
    *
    * @param rooms the rooms
    * @return roomCode
    */
   private Object getRoomCode(final Collection<Room> rooms)
   {
      final StringBuilder roomCode = new StringBuilder("");
      for (final Room selectedRoomModel : rooms)
      {
         roomCode.append(selectedRoomModel.getCode());
      }
      return roomCode.toString();
   }
   
   /**
    * Gets the children age as string.
    *
    * @param childrenAge the children age
    * @return the children age as string
    */
   private String getChildrenAgeAsString(final List<Integer> childrenAge)
   {
      final StringBuilder ages = new StringBuilder("");
      String comma = "";
      for (final Integer age : childrenAge)
      {
         ages.append(comma).append(age.intValue());
         comma = ",";
      }
      return ages.toString();
   }
}
