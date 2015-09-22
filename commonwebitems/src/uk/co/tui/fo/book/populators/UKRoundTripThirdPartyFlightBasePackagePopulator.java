/**
 *
 */
package uk.co.tui.fo.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import org.joda.time.DateTime;

import uk.co.tui.book.constants.PriceCodeConstants;
import uk.co.tui.book.domain.lite.AgentType;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.BookingType;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.Discount;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PaymentReference;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.SalesChannel;
import uk.co.tui.book.domain.lite.SalesChannelType;
import uk.co.tui.book.domain.lite.Schedule;
import uk.co.tui.book.domain.lite.SupplierProductDetails;
import uk.co.tui.book.domain.lite.WebAgent;

/**
 * @author sreevidhya.r
 *
 */
public class UKRoundTripThirdPartyFlightBasePackagePopulator implements
   Populator<String, BasePackage>
{
   private static final int ONE = 1;

   private static final int TWO = 2;

   private static final int THIRTY = 30;

   private static final int ONE_THOUSAND_TWO_FORTY_FIVE = 1245;

   private static final int ONE_HUNDRED = 100;

   private static final int SEVEN = 7;

   private static final String ABR = "ABR";

   private static final int ONE_HUNDRED_AND_EIGHTY = 180;

   /**
    * Populate base package.
    */
   @Override
   public void populate(final String source, final BasePackage basePackage)
      throws ConversionException
   {
      basePackage.setPackageType(PackageType.FO);
      ((PackageHoliday) basePackage).setItinerary(populateItinerary());

      basePackage.setBrandType(Brand.FO);
      basePackage.setSiteBrand("FO");
      basePackage.setDuration(Integer.valueOf(SEVEN));

      basePackage.setInventory(populateInventory());
      basePackage.setPassengers(populatePassengers());
      basePackage.setExtraFacilityCategories(new ArrayList<ExtraFacilityCategory>());
      basePackage.setPrice(populatePrice());
      basePackage.setBookingDetails(populateBookingDetails());
      basePackage.setDiscount(populateDiscount());

   }

   /**
    * @return Discount
    */
   private Discount populateDiscount()
   {
      final Discount discountModel = new Discount();

      final Price price = new Price();
      price.setCode("SAV");
      price.setAmount(populateDMoney());
      price.setRate(populateDRateMoney());
      discountModel.setPrice(price);
      return discountModel;
   }

   /**
    * @return Money
    */
   private Money populateDRateMoney()
   {
      final Money money = new Money();
      money.setAmount(new BigDecimal(ONE_HUNDRED));
      money.setCurrency(populateCurrancy());
      return money;
   }

   /**
    * @return money
    */
   private Money populateDMoney()
   {
      final Money money = new Money();
      money.setAmount(new BigDecimal(ONE_HUNDRED));
      money.setCurrency(populateCurrancy());
      return money;
   }

   private BookingDetails populateBookingDetails()
   {
      final BookingDetails bookingDetails = new BookingDetails();
      final BookingHistory bookingHistory = new BookingHistory();
      final List<BookingHistory> bookingHistorylist = new ArrayList<BookingHistory>();
      final List<PaymentReference> paymentReferenceList = new ArrayList<PaymentReference>();
      final WebAgent webagent = new WebAgent();
      webagent.setAgentType(AgentType.WEB);
      final SalesChannel salesChannel = new SalesChannel();
      salesChannel.setSalesChannelType(SalesChannelType.WEB);
      bookingHistory.setBookingType(BookingType.NEW);
      bookingHistory.setSalesChannel(salesChannel);
      bookingHistory.setAgent(webagent);
      bookingHistory.setPaymentReferences(paymentReferenceList);
      bookingHistorylist.add(bookingHistory);
      bookingDetails.setBookingHistory(bookingHistorylist);
      return bookingDetails;
   }

   /**
    * @return price
    */
   private Price populatePrice()
   {
      final Price price = new Price();
      price.setAmount(populateMoney());
      price.setRate(populateRateMoney());
      return price;
   }

   /**
    * @return
    */
   private Money populateRateMoney()
   {
      final Money money = new Money();
      money.setAmount(new BigDecimal(ONE_THOUSAND_TWO_FORTY_FIVE));
      money.setCurrency(populateCurrancy());
      return money;
   }

   /**
    * @return money
    */
   private Money populateMoney()
   {
      final Money money = new Money();
      money.setAmount(new BigDecimal(ONE_THOUSAND_TWO_FORTY_FIVE));
      money.setCurrency(populateCurrancy());
      return money;
   }

   /**
    * @return
    */
   private Currency populateCurrancy()
   {
      return Currency.getInstance("GBP");
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
      pax.setAge(Integer.valueOf(THIRTY));
      pax.setType(PersonType.ADULT);
      pax.setId(Integer.valueOf(ONE));
      pax.setLead(true);
      pax1.setAge(Integer.valueOf(THIRTY));
      pax1.setType(PersonType.ADULT);
      pax1.setId(Integer.valueOf(TWO));
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
      leg.setArrivalAirport(populateOutBoundArrivalAirport());
      leg.setCarrier(populateOutboundCarrier());
      leg.setDepartureAirport(populateOutBoundDepartureAirport());
      leg.setExternalInventory(false);

      final DateTime dt = new DateTime("2015-05-01T16:10:00+01:00");
      leg.setCycDate(dt.toDate());

      leg.setRouteCode("CFUMAN5BMANCFU");

      leg.setPriceList(populateLegLevelPrices());
      leg.setSchedule(populateOutBoundSchedule());
      return leg;
   }

   /**
    * Populate departure airport.
    *
    * @return the port
    */
   private Port populateOutBoundDepartureAirport()
   {
      final Port port = new Port();
      port.setCode("MAN");
      port.setName("Manchester");
      return port;
   }

   /**
    * Populate arrival airport.
    *
    * @return the port
    */
   private Port populateOutBoundArrivalAirport()
   {
      final Port port = new Port();
      port.setCode("CFU");
      port.setName("Cancun");
      return port;
   }

   /**
    * Populate departure airport.
    *
    * @return the port
    */
   private Port populateInBoundDepartureAirport()
   {
      final Port port = new Port();
      port.setCode("CFU");
      port.setName("Cancun");
      return port;
   }

   /**
    * Populate arrival airport.
    *
    * @return the port
    */
   private Port populateInBoundArrivalAirport()
   {
      final Port port = new Port();
      port.setCode("MAN");
      port.setName("Manchester");
      return port;
   }

   private Leg populateInBoundLeg()
   {
      final FlightLeg leg = new FlightLeg();
      leg.setArrivalAirport(populateInBoundArrivalAirport());
      leg.setCarrier(populateInBoundCarrier());
      leg.setDepartureAirport(populateInBoundDepartureAirport());
      leg.setExternalInventory(false);
      final DateTime dt = new DateTime("2015-05-08T22:05:00+03:00");
      leg.setCycDate(dt.toDate());
      leg.setRouteCode("CFUMAN5BCFUMAN");

      leg.setPriceList(populateLegLevelPrices());
      leg.setSchedule(populateInBoundSchedule());
      return leg;
   }

   /**
    * @return
    */
   private List<Price> populateLegLevelPrices()
   {
      final Price price = new Price();
      price.setCode(PriceCodeConstants.FLT);
      final Money money = new Money();
      money.setAmount(new BigDecimal(ONE_HUNDRED_AND_EIGHTY));
      price.setAmount(money);
      return Collections.emptyList();
   }

   /**
    * Populate out bound schedule.
    *
    * @return the schedule
    */
   private Schedule populateOutBoundSchedule()
   {
      final Schedule schedule = new Schedule();
      final DateTime dt = new DateTime("2015-05-01T21:35:00+03:00");
      schedule.setArrivalDate(dt.toDate());
      schedule.setArrivalTime("21:35");
      final DateTime dt1 = new DateTime("2015-05-01T16:10:00+01:00");
      schedule.setDepartureDate(dt1.toDate());
      schedule.setDepartureTime("16:10");
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
      final DateTime dt = new DateTime("2015-05-08T23:30:00+01:00");
      schedule.setArrivalDate(dt.toDate());
      schedule.setArrivalTime("23:30");
      final DateTime dt1 = new DateTime("2015-05-08T22:05:00+03:00");
      schedule.setDepartureDate(dt1.toDate());
      schedule.setDepartureTime("22:05");
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
      carrier.setCode(ABR);
      carrier.setNumber("9572");
      carrier.setSeries(ABR);

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
      carrier.setCode(ABR);
      carrier.setNumber("9573");
      carrier.setSeries(ABR);
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
      carrierInformation.setMarketingAirlineCode(ABR);
      carrierInformation.setMarketingAirlineName(ABR);
      carrierInformation.setOperatingAirlineCode(ABR);
      carrierInformation.setOperatingAirlineName(ABR);
      carrierInformation.setTourOperationFlightId(ABR);
      return carrierInformation;
   }

}