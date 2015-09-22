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
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.BookingDetails;
import uk.co.tui.book.domain.lite.BookingHistory;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
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

/**
 * @author sreevidhya.r
 *
 */
public class FESUKThirdPartyBasePackagePopulator implements Populator<String, BasePackage>
{

   private static final int ONE = 1;

   private static final int TWO = 2;

   private static final int THIRTY = 30;

   private static final int ONE_THOUSAND_TWO_FORTY_FIVE = 1245;

   private static final int SEVEN = 7;

   private static final String ABR = "ABR";

   private static final int ONE_HUNDRED_AND_EIGHTY = 180;

   @Override
   public void populate(final String source, final BasePackage basePackage)
      throws ConversionException
   {
      basePackage.setPackageType(PackageType.FO);
      ((PackageHoliday) basePackage).setItinerary(populateItinerary());

      basePackage.setBrandType(Brand.FO);
      basePackage.setDuration(Integer.valueOf(SEVEN));

      basePackage.setInventory(populateInventory());
      basePackage.setPassengers(populatePassengers());
      basePackage.setPrice(populatePrice());
      basePackage.setBookingDetails(populateBookingDetails());

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
   private List<Price> populateLegLevelPrices()
   {
      final Price price = new Price();
      price.setCode(PriceCodeConstants.FLT);
      final Money money = new Money();
      money.setAmount(new BigDecimal(ONE_HUNDRED_AND_EIGHTY));
      price.setAmount(money);
      return Collections.emptyList();
   }

   private BookingDetails populateBookingDetails()
   {
      final BookingDetails bookingDetails = new BookingDetails();
      bookingDetails.setBookingHistory(populateBookingHistory());
      return bookingDetails;
   }

   /**
    * @return bookingHistoryList
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
   private Money populateRateMoney()
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
    * @return paxList
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
    * @return inventory
    */
   private Inventory populateInventory()
   {
      final Inventory inventory = new Inventory();

      inventory.setInventoryType(InventoryType.ATCOM);
      inventory.setSupplierProductDetails(populateSupplierProductDetails());
      return inventory;
   }

   /**
    * @return supplierProductDetails
    */
   private SupplierProductDetails populateSupplierProductDetails()
   {
      final SupplierProductDetails supplierProductDetails = new SupplierProductDetails();
      supplierProductDetails.setProductCode("BTFO");
      supplierProductDetails.setPromoCode("BTFO");
      return supplierProductDetails;
   }

   private Itinerary populateItinerary()
   {
      final Itinerary itinerary = new Itinerary();
      final List<Leg> outboundLegs = new ArrayList<Leg>();

      itinerary.setOutBound(outboundLegs);

      outboundLegs.add(populateOutBoundLeg());

      return itinerary;
   }

   /**
    * @return leg
    */
   private Leg populateOutBoundLeg()
   {
      final FlightLeg leg = new FlightLeg();
      leg.setArrivalAirport(populateArrivalAirport());
      leg.setCarrier(populateOutboundCarrier());
      leg.setDepartureAirport(populateDepartureAirport());
      leg.setExternalInventory(false);

      leg.setInventoryId("3969287");
      leg.setJourneyDuration("03:25");
      final DateTime dt = new DateTime("2015-05-01T16:10:00+01:00");
      leg.setCycDate(dt.toDate());

      leg.setRouteCode("CFUMAN5BMANCFU");

      leg.setSchedule(populateOutBoundSchedule());
      leg.setPriceList(populateLegLevelPrices());
      return leg;
   }

   /**
    * @return port
    */
   private Port populateDepartureAirport()
   {
      final Port port = new Port();
      port.setCode("MAN");
      port.setName("Mancher");
      return port;
   }

   /**
    * @return port
    */
   private Port populateArrivalAirport()
   {
      final Port port = new Port();
      port.setCode("CFU");
      port.setName("Cfu");
      return port;
   }

   /**
    * @return schedule
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
    * @return carrier
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
    * @return carrierInformation
    */
   private CarrierInformation populateCarrierInformation()
   {
      final CarrierInformation carrierInformation = new CarrierInformation();
      carrierInformation.setMarketingAirlineCode(ABR);
      carrierInformation.setMarketingAirlineName("Airpost");
      carrierInformation.setOperatingAirlineCode(ABR);
      carrierInformation.setOperatingAirlineName(ABR);
      carrierInformation.setTourOperationFlightId(ABR);
      return carrierInformation;
   }

}
