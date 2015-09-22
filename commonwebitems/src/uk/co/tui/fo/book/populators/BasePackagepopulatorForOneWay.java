/**
 *
 */
package uk.co.tui.fo.book.populators;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.FlightOnlyPackage;
import uk.co.tui.book.domain.lite.Inventory;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.book.domain.lite.Schedule;
import uk.co.tui.book.domain.lite.SupplierProductDetails;

/**
 * @author munisekhar.k
 *
 */
public class BasePackagepopulatorForOneWay
{

   private static final int NUMBER_THIRTY = 30;

   private static final int NUMBER_SEVEN = 7;

   private static final String TOM = "TOM";

   private static final int TWO = 2;

   public FlightOnlyPackage populateBasePackage()
   {
      final FlightOnlyPackage basePackage = new FlightOnlyPackage();
      basePackage.setItinerary(populateItinerary());

      basePackage.setBrandType(Brand.TH);
      basePackage.setDuration(NUMBER_SEVEN);

      basePackage.setInventory(populateInventory());
      basePackage.setPackageType(PackageType.FO);
      basePackage.setPassengers(populatePassengers());

      return basePackage;
   }

   /**
    * @return List<Passenger>
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
    * @return Inventory
    */
   private Inventory populateInventory()
   {
      final Inventory inventory = new Inventory();

      inventory.setInventoryType(InventoryType.ATCOM);
      inventory.setSupplierProductDetails(populateSupplierProductDetails());
      return inventory;
   }

   /**
    * @return SupplierProductDetails
    */
   private SupplierProductDetails populateSupplierProductDetails()
   {
      final SupplierProductDetails supplierProductDetails = new SupplierProductDetails();
      supplierProductDetails.setProductCode("BTFO");
      supplierProductDetails.setPromoCode("BTFO");
      return null;
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
     *
     */
   private Leg populateOutBoundLeg()
   {
      final FlightLeg leg = new FlightLeg();
      leg.setArrivalAirport(populateArrivalAirport());
      leg.setCarrier(populateOutboundCarrier());
      leg.setDepartureAirport(populateDepartureAirport());
      leg.setExternalInventory(false);

      leg.setInventoryId("3556675");
      leg.setJourneyDuration("02hrs 15min");
      final DateTime dt = new DateTime("2015-06-14T10:25:00+02:00");
      leg.setCycDate(dt.toDate());

      leg.setRouteCode("PUJEMA7AEMAPUJ");

      leg.setSchedule(populateOutBoundSchedule());
      return leg;
   }

   /**
    * @return Port
    */
   private Port populateDepartureAirport()
   {
      final Port port = new Port();
      port.setCode("LGW");
      port.setName("London Gatwick");
      return port;
   }

   /**
    * @return Port
    */
   private Port populateArrivalAirport()
   {
      final Port port = new Port();
      port.setCode("PMI");
      port.setName("Palma De Majorca");
      return port;
   }

   /**
    * @return Schedule
    */
   private Schedule populateOutBoundSchedule()
   {

      final Schedule schedule = new Schedule();
      final DateTime dt = new DateTime("2015-06-06T10:25:00+02:00");
      schedule.setArrivalDate(dt.toDate());
      schedule.setArrivalTime("09:25");
      final DateTime dt1 = new DateTime("2015-06-06T10:25:00+02:00");
      schedule.setDepartureDate(dt1.toDate());
      schedule.setDepartureTime("06:10");
      return schedule;
   }

   /**
    * @return Carrier
    */
   private Carrier populateOutboundCarrier()
   {
      final Carrier carrier = new Carrier();
      carrier.setCarrierInformation(populateCarrierInformation());
      carrier.setCode(TOM);

      carrier.setNumber("118");
      carrier.setSeries(TOM);

      return carrier;
   }

   /**
    * @return CarrierInformation
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
