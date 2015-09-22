/**
 *
 */
package uk.co.tui.cr.book.url.builders;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.PackageComponentService;

/**
 * @author sunilkumar.sahu
 *
 */
public class BookAccomPageUrlBuilder
{
   
   @Resource
   private TUIUrlResolver<ProductModel> bookAccommodationUrlResolver;
   
   /** The Product Service . */
   @Resource
   private ProductService productService;
   
   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;
   
   @Resource
   private SessionService sessionService;
   
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;
   
   private static final String EQUALS = "=";
   
   private static final String AND = "&";
   
   private static final String FROM = "airports[]";
   
   private static final String WHERE_TO = "units[]";
   
   private static final String IS_FLEXIBLE = "flexibility";
   
   private static final String FLEXIBLE_DAYS = "flexibleDays";
   
   private static final String DURATION = "duration";
   
   private static final String ADULTS = "noOfAdults";
   
   private static final String SENIORS = "noOfSeniors";
   
   private static final String CHILDREN = "noOfChildren";
   
   private static final String CHILDREN_AGE = "childrenAge";
   
   private static final String WHEN = "when";
   
   private static final String PACKAGE_ID = "packageId";
   
   private static final String INDEX = "index";
   
   private static final String MULTI_SELECT = "multiSelect";
   
   private static final String LATESTCRITERIA = "latestCriteria";
   
   private static final String BRAND_TYPE = "brandType";
   
   private static final String FIRSTCHOICE = "F";
   
   private static final String COLON = ":";
   
   private static final String BOARBBASIS = "ChangedBoardBasis";
   
   private static final String BB = "bb";
   
   private static final TUILogUtils LOG = new TUILogUtils("BookAccomPageUrlBuilder");
   
   /**
    * To construct the book flow accommodation page url from the current cart package model.
    *
    * @return the url
    */
   public String getAccomBookFlowPageUrl(final String tab)
   {
      final BasePackage pkgModel = getPackageModel();
      final StringBuilder accomPageUrl = new StringBuilder();
      final AccommodationModel accomodationModel =
         getAccommodationModel(packageComponentService.getStay(pkgModel).getCode());
      final SearchResultsRequestData searchParameterInSession =
         sessionService.getAttribute(LATESTCRITERIA);
      
      if (!StringUtils.isEmpty(tab))
      {
         bookAccommodationUrlResolver.setOverrideSubPageType(tab);
      }
      accomPageUrl.append(bookAccommodationUrlResolver.resolve(accomodationModel));
      
      accomPageUrl
         .append(ADULTS)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.ADULT, PersonType.SENIOR))).append(AND);
      accomPageUrl
         .append(CHILDREN)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.CHILD, PersonType.INFANT))).append(AND);
      accomPageUrl.append(CHILDREN_AGE).append(EQUALS).append(checkChildrenInPassengers(pkgModel))
         .append(AND);
      accomPageUrl.append(DURATION).append(EQUALS).append(pkgModel.getDuration()).append(AND);
      
      // for departure airport code and departure date.
      final List<Leg> outBoundLegList =
         packageComponentService.getFlightItinerary(pkgModel).getOutBound();
      if (searchParameterInSession != null)
      {
         accomPageUrl.append(FROM).append(EQUALS)
            .append(getAirports(searchParameterInSession.getAirports())).append(AND);
         accomPageUrl.append(WHERE_TO).append(EQUALS)
            .append(getWhereTo(searchParameterInSession.getUnits())).append(COLON)
            .append(packageComponentService.getStay(pkgModel).getType()).append(AND);
         accomPageUrl.append(FLEXIBLE_DAYS).append(EQUALS)
            .append(searchParameterInSession.getFlexibleDays()).append(AND);
         accomPageUrl.append(IS_FLEXIBLE).append(EQUALS)
            .append(searchParameterInSession.isFlexibility()).append(AND);
         accomPageUrl.append(WHEN).append(EQUALS)
            .append(searchParameterInSession.getDepartureDate()).append(AND);
         // added by jitendrak
      }
      else
      {
         // The search parameter would not be in session for Short listed
         // pages where the source for this Build would be only package model
         accomPageUrl.append(FROM).append(EQUALS).append(getDepartureAirportCode(outBoundLegList))
            .append(AND);
         accomPageUrl.append(WHERE_TO).append(EQUALS)
            .append(packageComponentService.getStay(pkgModel).getCode());
         accomPageUrl.append(IS_FLEXIBLE).append(EQUALS).append(true).append(AND);
         accomPageUrl.append(WHEN).append(EQUALS).append(getDepartureDate(outBoundLegList))
            .append(AND);
      }
      
      accomPageUrl
         .append(SENIORS)
         .append(EQUALS)
         .append(
            PassengerUtils.getPersonTypeCountFromPassengers(pkgModel.getPassengers(),
               EnumSet.of(PersonType.SENIOR))).append(AND);
      // modified by jitendrak
      
      accomPageUrl.append(MULTI_SELECT).append(EQUALS)
         .append(String.valueOf(isMultiSelect(pkgModel))).append(AND);
      accomPageUrl.append(PACKAGE_ID).append(EQUALS).append(getPackageId(pkgModel)).append(AND);
      accomPageUrl.append(INDEX).append(EQUALS).append(1);
      
      if (pkgModel.getBrandType() == Brand.FC)
      {
         accomPageUrl.append(AND).append(BRAND_TYPE).append(EQUALS).append(FIRSTCHOICE);
      }
      // to add the changed board basis code for retainig the Boardbasis ui in
      // bookdetails page
      setBoardBasisForURL(accomPageUrl);
      return accomPageUrl.toString();
   }
   
   /**
    * @param pkgModel
    * @return
    */
   private String checkChildrenInPassengers(final BasePackage pkgModel)
   {
      if (PassengerUtils.isChildInPassengers(pkgModel.getPassengers()))
      {
         return getChildrenAgeAsString(PassengerUtils.getChildAges(pkgModel.getPassengers()));
      }
      return StringUtils.EMPTY;
   }
   
   private void setBoardBasisForURL(final StringBuilder accomURL)
   {
      final String bbCode = sessionService.getAttribute(BOARBBASIS);
      if (bbCode != null && !bbCode.isEmpty())
      {
         accomURL.append(AND).append(BB).append(EQUALS).append(bbCode);
      }
   }
   
   /*
    * To return the departure airport code.
    * 
    * @param outBoundLegList
    * 
    * @return airport code.
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
    * To set multiSelect = false for Lapland holidays
    *
    * @param pkgModel
    * @return multi selection of the product
    */
   private boolean isMultiSelect(final BasePackage pkgModel)
   {
      final List<HighLights> listOfHighlight = pkgModel.getListOfHighlights();
      if (CollectionUtils.isNotEmpty(listOfHighlight)
         && listOfHighlight.contains(HighLights.LAPLAND_DAYTRIP))
      {
         return false;
      }
      return true;
   }
   
   /**
    * To construct the package id.
    *
    * @param pkg Model
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
      flightInfoCode.append(getRoomCode(stay.getRooms()));
      
      packageId.append(stay.getCode()).append(getSellingCode(pkgModel)).append(flightInfoCode);
      LOG.debug("Package ID generated for given Accommodation");
      
      return packageId.toString();
      
   }
   
   /**
    * To get room code.
    *
    * @param rooms
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
    * To get selling code.
    *
    * @param pkgModel
    * @return
    */
   private String getSellingCode(final BasePackage pkgModel)
   {
      
      return (pkgModel.getInventory().getSupplierProductDetails()).getSellingCode();
   }
   
   /**
    * Method to return AccommodationModel from accommodation Code from PIM.
    *
    * @param accomCode
    * @return AccommodationModel
    *
    */
   private AccommodationModel getAccommodationModel(final String accomCode)
   {
      return (AccommodationModel) productService.getProductForCode(accomCode);
   }
   
   /**
    * To append the departure date and time milliseconds and arrival date and time milliseconds and
    * carrier code.
    *
    * @param flightInfoCode
    * @param outBoundLegList
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
    *
    * To return the DepartureDate.
    *
    * @param outBoundLegList
    * @return Departure Date
    */
   private String getDepartureDate(final List<Leg> outBoundLegList)
   {
      final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return dateFormat.format(outBoundLegList.get(0).getSchedule().getDepartureDate());
      }
      
      return "";
   }
   
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
    * This method gets the airport list in string format.
    *
    * @param airportsList
    * @return airports
    */
   private String getAirports(final List<AirportData> airportsList)
   {
      final StringBuilder airports = new StringBuilder("");
      String pipe = "";
      
      for (final AirportData airport : airportsList)
      {
         airports.append(pipe).append(airport.getId());
         pipe = "|";
      }
      
      return airports.toString();
   }
   
   /**
    * This method gets the where to list in string format.
    *
    * @param whereToList
    * @return whereTo
    */
   @SuppressWarnings("deprecation")
   private String getWhereTo(final List<UnitData> whereToList)
   {
      final StringBuilder whereTo = new StringBuilder("");
      String pipe = "";
      
      for (final UnitData unit : whereToList)
      {
         whereTo.append(pipe).append(unit.getId()).append(COLON).append(unit.getType());
         pipe = "|";
      }
      return whereTo.toString();
   }
   
}
