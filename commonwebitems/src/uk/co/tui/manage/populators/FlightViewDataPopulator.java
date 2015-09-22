/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.Flight;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.manage.viewdata.AirportViewData;
import uk.co.tui.manage.viewdata.CabinClassViewData;
import uk.co.tui.manage.viewdata.CarrierInformationViewData;
import uk.co.tui.manage.viewdata.FlightDetailsViewData;
import uk.co.tui.manage.viewdata.FlightScheduleViewData;
import uk.co.tui.manage.viewdata.FlightViewData;
import uk.co.tui.manage.viewdata.LegViewData;
import uk.co.tui.manage.viewdata.PackageViewData;

/**
 * @author premkumar.nd
 *
 */
public class FlightViewDataPopulator implements Populator<FlightItinerary, PackageViewData>
{

   private final TUILogUtils logger = new TUILogUtils("FlightViewDataPopulator");

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final FlightItinerary source, final PackageViewData target)
      throws ConversionException
   {
      populateFlightsData(source, target);

   }

   private void populateFlightsData(final FlightItinerary itinerary, final PackageViewData target)
   {

      final FlightDetailsViewData flightDetailsViewData = new FlightDetailsViewData();

      if (checkForInbound(itinerary))
      {
         populateInbounds(itinerary.getInBound(), flightDetailsViewData);
      }
      if (chcekForOutBound(itinerary))
      {
         populateOutbounds(itinerary.getOutBound(), flightDetailsViewData);
      }
      target.setFlightViewData(flightDetailsViewData);

   }

   /**
    * @param itinerary
    * @return
    */
   private boolean chcekForOutBound(final FlightItinerary itinerary)
   {
      return itinerary != null && !itinerary.getOutBound().isEmpty();
   }

   /**
    * @param itinerary
    * @return
    */
   private boolean checkForInbound(final FlightItinerary itinerary)
   {
      return itinerary != null && !itinerary.getInBound().isEmpty();
   }

   private LegViewData populateInboundOutBounds(final Leg leg)
   {
      final AirportViewData arrivalAirport = new AirportViewData();
      final AirportViewData departureAirport = new AirportViewData();
      final FlightViewData flight = new FlightViewData();
      final CarrierInformationViewData carrInformation = new CarrierInformationViewData();
      final FlightScheduleViewData flightSchedule = new FlightScheduleViewData();
      final LegViewData legViewData = new LegViewData();
      final CabinClassViewData cabinClass = new CabinClassViewData();
      final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd MMM yyyy");

      dateFormatter.format(leg.getSchedule().getArrivalDate());
      final Carrier carrier = leg.getCarrier();
      cabinClass.setCabinCode(((Flight) carrier).getCabinClass().getCabinCode());
      cabinClass.setCabinName(((Flight) carrier).getCabinClass().getCabinName());
      final String eqmtDes = carrier.getEquipementDescription().toUpperCase();
      if (eqmtDes.contains("787"))
      {
         legViewData.setDreamLiner(true);
      }
      else
      {
         legViewData.setDreamLiner(false);
      }
      flight.setCabinClass(cabinClass);
      flight.setAirEquipementType(carrier.getEquipementType());
      flight.setEquipementDescription(carrier.getEquipementDescription());

      arrivalAirport.setName(leg.getArrivalAirport().getName());
      arrivalAirport.setCode(leg.getArrivalAirport().getCode());

      departureAirport.setName(leg.getDepartureAirport().getName());
      departureAirport.setCode(leg.getDepartureAirport().getCode());

      carrInformation.setFlightCode(carrier.getCode());
      carrInformation.setFlightNumber(carrier.getNumber());
      carrInformation.setFlightSeries(carrier.getSeries());
      carrInformation.setMarketingAirlineCode(carrier.getCarrierInformation()
         .getMarketingAirlineCode());
      carrInformation.setOperatingAirlineCode(carrier.getCarrierInformation()
         .getOperatingAirlineCode());
      carrInformation.setOperatingAirlineName(carrier.getCarrierInformation()
         .getOperatingAirlineName());
      carrInformation.setTourOperationFlightId(carrier.getCarrierInformation()
         .getTourOperationFlightId());

      flightSchedule.setArrivalDate(dateFormatter.format(leg.getSchedule().getArrivalDate()));
      flightSchedule.setDepartureDate(dateFormatter.format(leg.getSchedule().getDepartureDate()));
      flightSchedule.setArrTime(leg.getSchedule().getArrivalTime());
      flightSchedule.setDepTime(leg.getSchedule().getDepartureTime());
      flight.setCarrierInformation(carrInformation);

      legViewData.setInventoryId(leg.getInventoryId());
      legViewData.setCarrier(flight);
      legViewData.setJourneyDuration(getDurationFormat(leg.getJourneyDuration()));
      legViewData.setArrivalAirport(arrivalAirport);
      legViewData.setDepartureAirport(departureAirport);
      legViewData.setSchedule(flightSchedule);

      return legViewData;

   }

   private void populateInbounds(final List<Leg> inbounds,
      final FlightDetailsViewData flightDetailsViewData)
   {
      final List<LegViewData> legs = new ArrayList<LegViewData>();
      for (final Leg inbound : inbounds)
      {
         final LegViewData leg = populateInboundOutBounds(inbound);
         legs.add(leg);

         final String depDate = leg.getSchedule().getDepartureDate();
         final String arrDate = leg.getSchedule().getArrivalDate();
         boolean flag = false;
         if (!arrDate.equals(depDate))
         {
            flag = true;
            logger.debug("Flag Value is: " + flag);
         }
         leg.getSchedule().setInBoundNextDay(flag);
      }

      flightDetailsViewData.setInboundSectors(legs);
      flightDetailsViewData.setInventory("ATCOM");
   }

   private void populateOutbounds(final List<Leg> outbounds,
      final FlightDetailsViewData flightDetailsViewData)
   {

      final List<LegViewData> legs = new ArrayList<LegViewData>();
      for (final Leg outbound : outbounds)
      {
         final LegViewData leg = populateInboundOutBounds(outbound);
         legs.add(leg);

         final String depDate = leg.getSchedule().getDepartureDate();
         final String arrDate = leg.getSchedule().getArrivalDate();
         boolean flag = false;
         if (!depDate.equals(arrDate))
         {
            flag = true;
            logger.debug("Flag Value is: " + flag);
         }
         leg.getSchedule().setOutBoundNextDay(flag);
      }

      flightDetailsViewData.setInventory("ATCOM");
      flightDetailsViewData.setOutboundSectors(legs);

   }

   private String getDurationFormat(final String duration)
   {
      if (duration != null && duration.split(":") != null)
      {
         final String[] values = duration.split(":");
         if (values.length == TWO)
         {
            return values[0] + "hr" + " " + values[1] + "min";
         }
      }
      return duration;
   }
}
