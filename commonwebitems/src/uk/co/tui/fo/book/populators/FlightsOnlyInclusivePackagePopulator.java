/**
 *
 */
package uk.co.tui.fo.book.populators;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;
import static uk.co.tui.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.AgentType;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.BookingType;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.Discount;
import uk.co.tui.book.domain.lite.EquipmentType;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.Flight;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.SalesChannel;
import uk.co.tui.book.domain.lite.SalesChannelType;
import uk.co.tui.book.domain.lite.Schedule;
import uk.co.tui.book.domain.lite.SupplierProductDetails;
import uk.co.tui.book.domain.lite.WebAgent;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author prashant.godi
 *
 */
public class FlightsOnlyInclusivePackagePopulator implements Populator<Itinerary, BasePackage>
{

   /** Offset1. */
   private static final int SCHEDULE_TIME_OFFSET1 = 4;

   /** The Airport Service . */
   @Resource
   private AirportService airportService;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The Constant ADULT_AGE. */
   private static final Integer ADULT_AGE = Integer.valueOf(30);

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   /**
    * This method populates InclusivePackage object from Holiday object.
    *
    * @param target - InclusivePackage object
    */

   @SuppressWarnings(BOXING)
   @Override
   public void populate(final Itinerary source, final BasePackage target)
   {
      final Itinerary itinerary = source;

      populateBookingDetails(target);
      target.setPackageType(PackageType.FO);
      target.setSiteBrand("FO");
      target.setDuration(Integer.valueOf(itinerary.getDuration()));
      populateItinerary(itinerary, target);
      populatePriceList(itinerary, target);
      target.setPrice(populatePrice(itinerary.getPriceList()));
      populatePaxDetails(target);
      // populate brand information
      populateBrandType(itinerary, target);
      // need to check this.
      target.setInventory(populateInventary(source.getPromo()));
      target.setDiscount(populateDiscount());
   }

   private Discount populateDiscount()
   {
      final Discount discountModel = new Discount();
      final Price price = new Price();
      final Money money = new Money();
      price.setAmount(money);
      money.setAmount(BigDecimal.ZERO);
      price.setCode("SAV");
      discountModel.setPrice(price);
      return discountModel;
   }

   /**
    * @param priceList
    * @return price
    */
   private Price populatePrice(final List<Price> priceList)
   {
      Price price = null;
      if (CollectionUtils.isNotEmpty(priceList))
      {
         price = new Price();
         price.setAmount(populateMoney(priceList.get(0).getAmount()));
         price.setRate(populateMoney(priceList.get(0).getRate()));
         price.setCode(priceList.get(0).getCode());
         price.setDescription(priceList.get(0).getDescription());
      }
      return price;
   }

   /**
    * @param promo
    * @return inventory
    */
   private Inventory populateInventary(final String promo)
   {
      final Inventory inventory = new Inventory();
      inventory.setInventoryType(InventoryType.ATCOM);
      inventory.setSupplierProductDetails(populateSupplierProductDetails(promo));
      return inventory;
   }

   /**
    * @param promo
    * @return productDeatils
    */
   private SupplierProductDetails populateSupplierProductDetails(final String promo)
   {
      final SupplierProductDetails productDeatils = new SupplierProductDetails();
      productDeatils.setPromoCode(promo);
      productDeatils.setProductCode(promo);
      return productDeatils;
   }

   /**
    * Populate brand type.
    *
    * @param itinerary the itinerary
    * @param target the target
    */
   private void populateBrandType(final Itinerary itinerary, final BasePackage target)
   {
      target.setBrandType(itinerary.getBrand());
   }

   /**
    * Populate price.
    *
    * @param target the target
    */
   private void populatePriceList(final Itinerary holiday, final BasePackage target)
   {
      target.setPrices(holiday.getPriceList());
   }

   /**
    * @param amount
    * @return money
    */
   private Money populateMoney(final Money amount)
   {
      final Money money = new Money();
      money.setAmount(amount.getAmount());
      money.setCurrency(Currency.getInstance(currencyResolver.getSiteCurrency()));
      return money;
   }

   private void populateItinerary(final Itinerary itinary, final BasePackage target)
   {
      final Itinerary flightItineraryModel = new FlightItinerary();

      final List<Leg> outBoundLegModelList = new ArrayList<Leg>();
      final List<Leg> inBoundLegModelList = new ArrayList<Leg>();

      final FlightItinerary flightitinary = (FlightItinerary) itinary;

      final List<Leg> outboundlegs = flightitinary.getOutBound();
      final List<Leg> inboundlegs = flightitinary.getInBound();

      populateLegModels(outboundlegs, outBoundLegModelList);
      if (CollectionUtils.isNotEmpty(inboundlegs))
      {
         populateLegModels(inboundlegs, inBoundLegModelList);
      }
      flightItineraryModel.setOutBound(outBoundLegModelList);
      flightItineraryModel.setInBound(inBoundLegModelList);
      flightItineraryModel.setBrand(flightitinary.getBrand());
      flightItineraryModel.setMinAvail(flightitinary.getMinAvail());
      ((PackageHoliday) target).setItinerary(flightItineraryModel);
   }

   /**
    * To populate the LegModels.
    *
    * @param legs the legs
    * @param legModelList the leg model list
    */
   private void populateLegModels(final List<Leg> legs, final List<Leg> legModelList)
   {
      final Leg legModel = new FlightLeg();
      for (final Leg eachLeg : legs)
      {

         final Schedule scheduleModel = new Schedule();
         populateFlightSchedule(eachLeg, scheduleModel);

         final CarrierInformation carrierInfoModel = new CarrierInformation();
         final Carrier carrier = new Flight();
         populateCarrierInformation(eachLeg, carrierInfoModel, carrier);

         carrier.setCarrierInformation(carrierInfoModel);
         populateFlight(eachLeg, carrier);
         legModel.setSchedule(scheduleModel);
         legModel.setCarrier(carrier);
         setAirports(eachLeg, legModel);
         populateAtcomId((FlightLeg) legModel, eachLeg);
         populateRouteCode((FlightLeg) legModel, eachLeg);
         populateCycDate((FlightLeg) legModel, eachLeg);
         populateFlightSeqCode((FlightLeg) legModel, eachLeg);
         legModelList.add(legModel);
         populateLegLevelPrice(legModel, eachLeg);
      }
   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateLegLevelPrice(final Leg legModel, final Leg eachLeg)
   {

      final List<Price> priceList = new ArrayList<Price>();
      for (final Price price : eachLeg.getPriceList())
      {
         priceList.add(populatePrices(price));
      }
      legModel.setPriceList(priceList);

   }

   /**
    * To populate the Arrival Airport and Departure Airport to the Leg object.
    *
    * @param eachLeg the each leg
    * @param legModel the leg model
    */
   private void setAirports(final Leg eachLeg, final uk.co.tui.book.domain.lite.Leg legModel)
   {
      legModel.setDepartureAirport(getAirportName(eachLeg.getDepartureAirport().getCode()));
      legModel.setArrivalAirport(getAirportName(eachLeg.getArrivalAirport().getCode()));
   }

   /**
    * Method to getAirport Name from PIM.
    *
    * @param airportCode the airport code
    * @return String- airportName
    */
   private Port getAirportName(final String airportCode)
   {
      if (isNotEmpty(airportCode))
      {
         return populateAirport(airportService.getAirportByCode(airportCode));
      }
      return null;
   }

   /**
    * @param airportModel
    * @return Airport
    */
   public uk.co.tui.book.domain.lite.Port populateAirport(final AirportModel airportModel)
   {
      final uk.co.tui.book.domain.lite.Port airport = new uk.co.tui.book.domain.lite.Port();
      airport.setCode(airportModel.getCode());
      airport.setName(airportModel.getName());
      return airport;
   }

   /**
    * @param price
    * @return prc
    */
   private Price populatePrices(final Price price)
   {
      final Price prc = new Price();
      setPriceCode(price, prc);
      prc.setDescription(price.getDescription());
      prc.setQuantity(price.getQuantity());
      if (StringUtils.equalsIgnoreCase("AP", price.getCode()))
      {
         // total amount divide by qut
         prc.setRate(populateMoney(price.getRate()));
         prc.setAmount(populateAmount(price.getAmount().getAmount()));
      }
      else
      {
         final Money money = new Money();
         money.setAmount(BigDecimal.ZERO);
         prc.setRate(populateMoney(money));
         prc.setAmount(populateAmount(BigDecimal.ZERO));
      }
      return prc;

   }

   /**
    * @param quantity
    * @param rate
    * @return money
    */
   private Money populateAmount(final BigDecimal amount)
   {
      final Money money = new Money();
      money.setAmount(amount);
      money.setCurrency(Currency.getInstance(currencyResolver.getSiteCurrency()));
      return money;

   }

   private void setPriceCode(final Price price, final Price prc)
   {
      if (StringUtils.equalsIgnoreCase("AP", price.getCode()))
      {
         prc.setCode("FA");
      }
      else if (StringUtils.equalsIgnoreCase("CP", price.getCode()))
      {
         prc.setCode("FC");
      }
      else
      {
         prc.setCode(price.getCode());
      }
   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateFlightSeqCode(final FlightLeg legModel, final Leg eachLeg)
   {
      final FlightLeg flightLeg = (FlightLeg) eachLeg;
      if (StringUtils.isNotBlank(flightLeg.getRouteCode()))
      {
         legModel.setFlightSeqCode(StringUtils.substring(flightLeg.getRouteCode(),
            CommonwebitemsConstants.SEVEN, CommonwebitemsConstants.EIGHT));
      }
   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateCycDate(final FlightLeg legModel, final Leg eachLeg)
   {
      final FlightLeg flightLeg = (FlightLeg) eachLeg;
      if (flightLeg.getCycDate() != null)
      {
         legModel.setCycDate(flightLeg.getCycDate());
      }
   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateRouteCode(final FlightLeg legModel, final Leg eachLeg)
   {
      final FlightLeg flightLeg = (FlightLeg) eachLeg;
      if (StringUtils.isNotBlank(flightLeg.getRouteCode()))
      {
         legModel.setRouteCode(flightLeg.getRouteCode());
      }
   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateAtcomId(final FlightLeg legModel, final Leg eachLeg)
   {
      final FlightLeg flightLeg = (FlightLeg) eachLeg;
      if (StringUtils.isNotBlank(flightLeg.getAtcomId()))
      {
         legModel.setAtcomId(flightLeg.getAtcomId());
      }
   }

   /**
    * To populate the Flight to the Leg object.
    *
    * @param eachLeg the each leg
    * @param carrier the flight model
    */
   private void populateFlight(final Leg eachLeg, final Carrier carrier)
   {
      if (StringUtils.equalsIgnoreCase(eachLeg.getCarrier().getEquipementDescription(),
         EquipmentType.DREAMLINEAR787.toString()))
      {
         carrier.setEquipementType(EquipmentType.DREAMLINEAR787);
      }
   }

   /**
    * To populate the CarrierInformation to the Leg object.
    *
    * @param eachLeg the each leg
    * @param carrierInfoModel the carrier info model
    * @param carrier
    */
   private void populateCarrierInformation(final Leg eachLeg,
      final CarrierInformation carrierInfoModel, final Carrier carrier)
   {
      final Carrier car = eachLeg.getCarrier();
      carrierInfoModel.setMarketingAirlineName(car.getCarrierInformation()
         .getMarketingAirlineName());
      carrierInfoModel.setMarketingAirlineCode(car.getCode());
      carrier.setCode(car.getCode());
      carrier.setNumber(car.getNumber());
      carrier.setEquipementDescription(car.getEquipementDescription());

   }

   /**
    * To populate the FlightSchedule to the Leg object.
    *
    * @param eachLeg the each leg
    * @param scheduleModel the schedule model
    */
   private void populateFlightSchedule(final Leg eachLeg, final Schedule scheduleModel)
   {
      final Schedule schedule = eachLeg.getSchedule();
      if (isNotNull(schedule))
      {
         scheduleModel.setArrivalTime(new StringBuilder(StringUtils.leftPad(
            schedule.getArrivalTime(), SCHEDULE_TIME_OFFSET1)).toString());
         scheduleModel.setDepartureTime(new StringBuilder(StringUtils.leftPad(
            schedule.getDepartureTime(), SCHEDULE_TIME_OFFSET1)).toString());
         scheduleModel.setArrivalDate(schedule.getArrivalDate());
         scheduleModel.setDepartureDate(schedule.getDepartureDate());
      }
   }

   /**
    * Populate pax details.
    *
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populatePaxDetails(final BasePackage target)
   {
      final List<Passenger> passengerList = new ArrayList<Passenger>();
      final FlightSearchCriteria flightsearchcriteria =
         sessionService.getAttribute("flightsearchcriteria");
      final int passengerCount =
         getTotalPaxCount(flightsearchcriteria.getAdultCount(),
            flightsearchcriteria.getChildCount(), flightsearchcriteria.getInfantCount());
      final Map<Integer, PersonType> passengerMap =
         getPassengerMap(flightsearchcriteria.getAdultCount(),
            flightsearchcriteria.getChildCount(), flightsearchcriteria.getInfantCount());

      List<Integer> childAges = null;

      if (null != flightsearchcriteria
         && CollectionUtils.isNotEmpty(flightsearchcriteria.getChildages()))
      {
         childAges = getChildAges(flightsearchcriteria.getChildages());
      }
      for (int i = 1; i <= passengerCount; i++)
      {
         final Passenger passengerModel = new Passenger();
         passengerModel.setId(i);
         passengerModel.setType(passengerMap.get(i));

         passengerModel.setName("Passenger" + i);
         childAges = setPassengerAgeByType(passengerModel, childAges);
         passengerModel.setAddresses(new ArrayList<Address>());
         passengerModel.setExtraFacilities(new ArrayList<ExtraFacility>());
         passengerList.add(passengerModel);
      }
      target.setPassengers(passengerList);
   }

   /**
    * Sets the passenger age by type.
    *
    * @param passengerModel the new passenger age by type
    */
   private List<Integer> setPassengerAgeByType(final Passenger passengerModel,
      final List<Integer> childAges)
   {
      if (passengerModel.getType() == PersonType.CHILD
         || passengerModel.getType() == PersonType.INFANT)
      {
         return setAgeOfPassenger(passengerModel, childAges);

      }
      if (passengerModel.getType() == PersonType.ADULT)
      {
         passengerModel.setAge(ADULT_AGE);
         return childAges;
      }
      return childAges;
   }

   /**
    * Sets the age of passenger.
    *
    * @param passengerModel the new age of passenger
    */
   private List<Integer> setAgeOfPassenger(final Passenger passengerModel,
      final List<Integer> childAges)
   {
      for (final Integer age : childAges)
      {
         if (isValidChild(passengerModel, age.intValue())
            || isValidInfant(passengerModel, age.intValue()))
         {
            passengerModel.setAge(age);
            childAges.remove(age);
            return childAges;
         }
      }

      return childAges;
   }

   /**
    * Checks if is valid infant.
    *
    * @param passengerModel the passenger model
    * @param age the age
    * @return true, if is valid infant
    */
   private boolean isValidInfant(final Passenger passengerModel, final int age)
   {
      return passengerModel.getType() == PersonType.INFANT && age < TWO;
   }

   /**
    * Checks if is valid child.
    *
    * @param passengerModel the passenger model
    * @param age the age
    * @return true, if is valid child
    */
   private boolean isValidChild(final Passenger passengerModel, final int age)
   {
      return passengerModel.getType() == PersonType.CHILD && age >= TWO;
   }

   /**
    * Gets the child ages.
    *
    * @return the child ages
    */
   @SuppressWarnings(BOXING)
   private List<Integer> getChildAges(final List<String> childAges)
   {

      final List<Integer> childAgeList = new ArrayList<Integer>();
      for (final String age : childAges)
      {
         childAgeList.add(Integer.valueOf(age));
      }
      return childAgeList;
   }

   /**
    * Method to create a map of passenger id with PassengerType.
    *
    * @return passengerMap
    */
   @SuppressWarnings(BOXING)
   private Map<Integer, PersonType> getPassengerMap(final int adultCount, final int childCount,
      final int infantCount)
   {
      final int totalCount = adultCount + childCount + infantCount;
      final Map<Integer, PersonType> passengerMap = new HashMap<Integer, PersonType>();
      for (int id = 1; id <= totalCount; id++)
      {
         addPassengerToMap(adultCount, childCount, passengerMap, id);
      }
      return passengerMap;
   }

   private int getTotalPaxCount(final int adultCount, final int childCount, final int infantCount)
   {
      return adultCount + childCount + infantCount;
   }

   /**
    * Adds the passenger to map.
    *
    * @param adultCount the adult count
    * @param childCount the child count
    * @param passengerMap the passenger map
    * @param id the id
    */
   private void addPassengerToMap(final int adultCount, final int childCount,
      final Map<Integer, PersonType> passengerMap, final int id)
   {
      if (id <= adultCount)
      {
         passengerMap.put(Integer.valueOf(id), PersonType.ADULT);
      }
      else if (id > (adultCount) && id <= (adultCount + childCount))
      {
         passengerMap.put(Integer.valueOf(id), PersonType.CHILD);
      }
      else
      {
         passengerMap.put(Integer.valueOf(id), PersonType.INFANT);
      }
   }

   private void populateBookingDetails(final BasePackage target)
   {

      final BookingDetails bookingDetails = new BookingDetails();
      final BookingHistory bookingHistory = new BookingHistory();
      final List<BookingHistory> bookingHistorylist = new ArrayList<BookingHistory>();
      final WebAgent webagent = new WebAgent();
      webagent.setAgentType(AgentType.WEB);
      final SalesChannel salesChannel = new SalesChannel();
      salesChannel.setSalesChannelType(SalesChannelType.WEB);

      bookingHistory.setBookingType(BookingType.NEW);
      bookingHistory.setSalesChannel(salesChannel);
      bookingHistory.setAgent(webagent);

      bookingHistorylist.add(bookingHistory);
      bookingDetails.setBookingHistory(bookingHistorylist);

      target.setBookingDetails(bookingDetails);

   }

}
