/**
 *
 */
package uk.co.tui.fo.book.populators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.FlightOnlyPackage;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.SalesChannel;
import uk.co.tui.book.domain.lite.SalesChannelType;
import uk.co.tui.book.domain.lite.Schedule;
import uk.co.tui.book.domain.lite.SupplierProductDetails;

/**
 * @author munisekhar.k
 *
 */
public class BasePackagePopulator
{

   private static final int NUMBER_THIRTY = 30;

   private static final int ONE_THOUSAND_TWO_FORTY_FIVE = 1245;

   private static final int NUMBER_SEVEN = 7;

   private static final String TOM = "TOM";

   private static final int TWO = 2;

   /**
    * Populate base package.
    *
    * @return the flight only package
    */
   public FlightOnlyPackage populateBasePackage()
   {
      final FlightOnlyPackage basePackage = new FlightOnlyPackage();
      basePackage.setItinerary(populateItinerary());
      basePackage.setAgtNum("H1079");
      basePackage.setBrandType(Brand.TH);
      basePackage.setDuration(NUMBER_SEVEN);
      basePackage.setInventory(populateInventory());
      basePackage.setPackageType(PackageType.FO);
      basePackage.setPassengers(populatePassengers());
      basePackage.setPrice(populatePrice());
      basePackage.setBookingDetails(populateBookingDetails());
      return basePackage;
   }

   /**
    * @return BookingDetails
    */
   private BookingDetails populateBookingDetails()
   {
      final BookingDetails bookingDetails = new BookingDetails();
      bookingDetails.setBookingHistory(populateBookingHistory());
      return bookingDetails;
   }

   /**
    * @return List<BookingHistory>
    */
   private List<BookingHistory> populateBookingHistory()
   {
      final List<BookingHistory> bookingHistoryList = new ArrayList<BookingHistory>();
      final BookingHistory bookingHistory = new BookingHistory();
      final SalesChannel salesChannel = new SalesChannel();
      salesChannel.setSalesChannelType(SalesChannelType.WEB);
      bookingHistory.setSalesChannel(salesChannel);
      bookingHistoryList.add(bookingHistory);
      return bookingHistoryList;
   }

   /**
    * @return Price
    */
   private Price populatePrice()
   {
      final Price price = new Price();
      price.setAmount(populateMoney());
      price.setRate(populateMoney());
      return price;
   }

   /**
    * @return Money
    */
   private Money populateMoney()
   {
      final Money money = new Money();
      money.setAmount(new BigDecimal(ONE_THOUSAND_TWO_FORTY_FIVE));
      return money;
   }

   /**
    * Populate passengers.
    *
    * @return the list
    */
   private List<Passenger> populatePassengers()
   {
      final List<Passenger> paxList = new ArrayList<Passenger>();
      final Passenger pax = new Passenger();
      final Passenger pax1 = new Passenger();
      pax.setAge(NUMBER_THIRTY);
      pax.setType(PersonType.ADULT);
      pax.setId(1);
      pax.setLead(true);
      pax1.setAge(NUMBER_THIRTY);
      pax1.setType(PersonType.ADULT);
      pax1.setId(TWO);
      pax.setLead(false);
      paxList.add(pax);
      paxList.add(pax1);
      return paxList;
   }

   /**
    * Populate inventory.
    *
    * @return the inventory
    */
   private Inventory populateInventory()
   {
      final Inventory inventory = new Inventory();
      inventory.setInventoryType(InventoryType.ATCOM);
      inventory.setSupplierProductDetails(populateSupplierProductDetails());
      return inventory;
   }

   /**
    * Populate supplier product details.
    *
    * @return the supplier product details
    */
   private SupplierProductDetails populateSupplierProductDetails()
   {
      final SupplierProductDetails supplierProductDetails = new SupplierProductDetails();
      supplierProductDetails.setProductCode("BTFO");
      supplierProductDetails.setPromoCode("BTFO");
      return supplierProductDetails;
   }

   /**
    * Populate itinerary.
    *
    * @return the itinerary
    */
   private Itinerary populateItinerary()
   {
      final Itinerary itinerary = new Itinerary();
      final List<Leg> outboundLegs = new ArrayList<Leg>();
      final List<Leg> inboundLegs = new ArrayList<Leg>();
      itinerary.setOutBound(outboundLegs);
      itinerary.setInBound(inboundLegs);
      outboundLegs.add(populateOutBoundLeg());
      inboundLegs.add(populateInBoundLeg());
      return itinerary;
   }

   /**
    * Populate out bound leg.
    *
    * @return the leg
    */
   private Leg populateOutBoundLeg()
   {
      final FlightLeg leg = new FlightLeg();
      leg.setArrivalAirport(populateArrivalAirport());
      leg.setCarrier(populateOutboundCarrier());
      leg.setDepartureAirport(populateDepartureAirport());
      leg.setExternalInventory(false);
      final DateTime dt = new DateTime("2015-05-04T11:15:00+01:00");
      leg.setCycDate(dt.toDate());
      leg.setRouteCode("CUNLGW1ACUNLGW");
      leg.setSchedule(populateOutBoundSchedule());
      return leg;
   }

   /**
    * Populate departure airport.
    *
    * @return the port
    */
   private Port populateDepartureAirport()
   {
      final Port port = new Port();
      port.setCode("CUN");
      port.setName("LondonGatwick");
      return port;
   }

   /**
    * Populate arrival airport.
    *
    * @return the port
    */
   private Port populateArrivalAirport()
   {
      final Port port = new Port();
      port.setCode("LGW");
      port.setName("LondonGatwick");
      return port;
   }

   private Leg populateInBoundLeg()
   {
      final FlightLeg leg = new FlightLeg();
      leg.setArrivalAirport(populateDepartureAirport());
      leg.setCarrier(populateInBoundCarrier());
      leg.setDepartureAirport(populateArrivalAirport());
      leg.setExternalInventory(false);

      final DateTime dt = new DateTime("2015-05-11T11:15:00+01:00");
      leg.setCycDate(dt.toDate());
      leg.setRouteCode("CUNLGW1ALGWCUN");
      leg.setSchedule(populateInBoundSchedule());
      return leg;
   }

   /**
    * Populate out bound schedule.
    *
    * @return the schedule
    */
   private Schedule populateOutBoundSchedule()
   {
      final Schedule schedule = new Schedule();
      final DateTime dt = new DateTime("2015-05-05T08:15:00+00:40");
      schedule.setArrivalDate(dt.toDate());
      schedule.setArrivalTime("08:15");
      final DateTime dt1 = new DateTime("2015-05-04T11:15:00+01:00");
      schedule.setDepartureDate(dt1.toDate());
      schedule.setDepartureTime("11:15");
      return schedule;
   }

   /**
    * Populate in bound schedule.
    *
    * @return the schedule
    */
   private Schedule populateInBoundSchedule()
   {
      final Schedule schedule = new Schedule();
      final DateTime dt = new DateTime("2015-05-12T08:15:00+00:40");
      schedule.setArrivalDate(dt.toDate());
      schedule.setArrivalTime("08:15");
      final DateTime dt1 = new DateTime("2015-05-11T11:15:00+01:00");
      schedule.setDepartureDate(dt1.toDate());
      schedule.setDepartureTime("11:15");
      return schedule;
   }

   /**
    * Populate outbound carrier.
    *
    * @return the carrier
    */
   private Carrier populateOutboundCarrier()
   {
      final Carrier carrier = new Carrier();
      carrier.setCarrierInformation(populateCarrierInformation());
      carrier.setCode(TOM);
      carrier.setSeries(TOM);
      return carrier;
   }

   /**
    * Populate in bound carrier.
    *
    * @return the carrier
    */
   private Carrier populateInBoundCarrier()
   {
      final Carrier carrier = new Carrier();
      carrier.setCarrierInformation(populateCarrierInformation());
      carrier.setCode(TOM);
      carrier.setSeries(TOM);
      return carrier;
   }

   /**
    * Populate carrier information.
    *
    * @return the carrier information
    */
   private CarrierInformation populateCarrierInformation()
   {
      final CarrierInformation carrierInformation = new CarrierInformation();
      carrierInformation.setMarketingAirlineCode(TOM);
      carrierInformation.setMarketingAirlineName(TOM);
      carrierInformation.setOperatingAirlineCode(TOM);
      carrierInformation.setOperatingAirlineName(TOM);
      carrierInformation.setTourOperationFlightId(TOM);
      return carrierInformation;
   }

}
