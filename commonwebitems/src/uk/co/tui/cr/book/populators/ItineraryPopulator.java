/**
 *
 */
package uk.co.tui.cr.book.populators;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.Schedule;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.tui.book.domain.lite.Carrier;
import uk.co.tui.book.domain.lite.CarrierInformation;
import uk.co.tui.book.domain.lite.EquipmentType;
import uk.co.tui.book.domain.lite.Flight;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Port;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author ramkishore.p
 *
 */
public class ItineraryPopulator implements Populator<PackageItemValue, Itinerary>
{
   
   /** The Airport Service . */
   @Resource
   private AirportService airportService;
   
   /** Offset1. */
   private static final int SCHEDULE_TIME_OFFSET1 = 4;
   
   /** Offset2. */
   private static final int SCHEDULE_TIME_OFFSET2 = 2;
   
   /** The Constant INDEX_ZERO. */
   private static final int INDEX_ZERO = 0;
   
   /** The Constant INDEX_TWO. */
   private static final int INDEX_TWO = 2;
   
   /** The Constant INDEX_THREE. */
   private static final int INDEX_THREE = 3;
   
   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final PackageItemValue packageItemValue, final Itinerary flightItinerary)
      throws ConversionException
   {
      
      final List<uk.co.tui.book.domain.lite.Leg> outBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();
      final List<uk.co.tui.book.domain.lite.Leg> inBoundLegModelList =
         new ArrayList<uk.co.tui.book.domain.lite.Leg>();
      
      final List<Leg> outboundlegs =
         packageItemValue.getPckSailingInfo().get(0).getDefaultFlight().getOutbound();
      final List<Leg> inboundlegs =
         packageItemValue.getPckSailingInfo().get(0).getDefaultFlight().getInbound();
      
      populateLegModels(outboundlegs, outBoundLegModelList);
      populateLegModels(inboundlegs, inBoundLegModelList);
      
      flightItinerary.setOutBound(outBoundLegModelList);
      flightItinerary.setInBound(inBoundLegModelList);
   }
   
   /**
    * To populate the LegModels.
    *
    * @param legs the legs
    * @param legModelList the leg model list
    */
   private void populateLegModels(final List<Leg> legs,
      final List<uk.co.tui.book.domain.lite.Leg> legModelList)
   {
      final uk.co.tui.book.domain.lite.Leg legModel = new FlightLeg();
      for (final Leg eachLeg : legs)
      {
         
         final uk.co.tui.book.domain.lite.Schedule scheduleModel =
            new uk.co.tui.book.domain.lite.Schedule();
         populateFlightSchedule(eachLeg, scheduleModel);
         
         final CarrierInformation carrierInfoModel = new CarrierInformation();
         final Carrier carrier = new Flight();
         populateCarrierInformation(eachLeg, carrierInfoModel, carrier);
         carrier.setCarrierInformation(carrierInfoModel);
         populateFlight(eachLeg, carrier);
         legModel.setSchedule(scheduleModel);
         legModel.setCarrier(carrier);
         populateExternalInventory(legModel, eachLeg);
         setAirports(eachLeg, legModel);
         populateAtcomId((FlightLeg) legModel, eachLeg);
         populateRouteCode((FlightLeg) legModel, eachLeg);
         populateCycDate((FlightLeg) legModel, eachLeg);
         populateFlightSeqCode((FlightLeg) legModel, eachLeg);
         legModelList.add(legModel);
      }
   }
   
   /**
    * To populate the FlightSchedule to the Leg object.
    *
    * @param eachLeg the each leg
    * @param scheduleModel the schedule model
    */
   private void populateFlightSchedule(final Leg eachLeg,
      final uk.co.tui.book.domain.lite.Schedule scheduleModel)
   {
      final Schedule schedule = eachLeg.getSchedule();
      if (isNotNull(schedule))
      {
         scheduleModel.setArrivalTime(new StringBuilder(StringUtils.leftPad(schedule.getArrTime(),
            SCHEDULE_TIME_OFFSET1)).insert(SCHEDULE_TIME_OFFSET2, ":").toString());
         scheduleModel.setDepartureTime(new StringBuilder(StringUtils.leftPad(
            schedule.getDepTime(), SCHEDULE_TIME_OFFSET1)).insert(SCHEDULE_TIME_OFFSET2, ":")
            .toString());
         scheduleModel.setArrivalDate(schedule.getArrivalDate().toDate());
         scheduleModel.setDepartureDate(schedule.getDepartureDate().toDate());
      }
   }
   
   /**
    * To populate the CarrierInformation to the Leg object.
    *
    * @param eachLeg the each leg
    * @param carrierInfoModel the carrier info model
    * @param carrier the carrier
    */
   private void populateCarrierInformation(final Leg eachLeg,
      final CarrierInformation carrierInfoModel, final Carrier carrier)
   {
      final uk.co.portaltech.travel.model.results.Flight flight = eachLeg.getCarrier();
      final String flightNumber = getCarrierNumber(flight.getCode());
      final String carrierCode =
         getActualCarrierCode(flight.getCarrier(), getCarrierCode(flight.getCode()));
      carrierInfoModel.setMarketingAirlineName(getAirlinesName(carrierCode));
      carrierInfoModel.setMarketingAirlineCode(carrierCode);
      carrier.setCode(carrierCode);
      carrier.setNumber(flightNumber);
   }
   
   /**
    * @param carrierCode
    * @return
    */
   private String getAirlinesName(final String carrierCode)
   {
      if (StringUtils.equalsIgnoreCase(carrierCode, "TOM"))
      {
         return "Thomson Airways";
      }
      return carrierCode;
   }
   
   /**
    * @param inventoryCarrierCode
    * @param derivedCarrierCode
    * @return
    */
   private String getActualCarrierCode(final String inventoryCarrierCode,
      final String derivedCarrierCode)
   {
      if (StringUtils.isEmpty(inventoryCarrierCode)
         || !StringUtils.equals(inventoryCarrierCode, derivedCarrierCode))
      {
         return derivedCarrierCode;
      }
      return inventoryCarrierCode;
   }
   
   /*
    * The previous way of taking carrier code was wrong. It was strictly assuming that the carrier
    * name consists of 3 chars. but that is wrong and implemented as below now.
    */
   
   /**
    * To get the FlightCode. The previous way of taking carrier code was wrong. It was strictly
    * assuming that the carrier name consists of 3 chars. but that is wrong and implemented as below
    * now.
    *
    * @param code the code
    */
   private static String getCarrierNumber(final String code)
   {
      
      if (StringUtils.isNumeric(String.valueOf(code.charAt(INDEX_TWO))))
      {
         return code.substring(INDEX_TWO);
      }
      return code.substring(INDEX_THREE);
   }
   
   /**
    * To get the CarrierName. The previous way of taking carrier code was wrong. It was strictly
    * assuming that the carrier name consists of 3 chars. but that is wrong and implemented as below
    * now.
    *
    * @param code the code
    */
   private static String getCarrierCode(final String code)
   {
      if (StringUtils.isNumeric(String.valueOf(code.charAt(INDEX_TWO))))
      {
         return code.substring(INDEX_ZERO, INDEX_TWO).trim();
      }
      return code.substring(INDEX_ZERO, INDEX_THREE).trim();
   }
   
   /**
    * To populate the Flight to the Leg object.
    *
    * @param eachLeg the each leg
    * @param carrier the flight model
    */
   private void populateFlight(final Leg eachLeg, final Carrier carrier)
   {
      if (eachLeg.getCarrier().isDreamLine())
      {
         carrier.setEquipementType(EquipmentType.DREAMLINEAR787);
      }
   }
   
   /**
    * Populate external inventory.
    *
    * @param legModel the leg model
    * @param leg the flight leg
    */
   private void populateExternalInventory(final uk.co.tui.book.domain.lite.Leg legModel,
      final Leg leg)
   {
      legModel.setExternalInventory(BooleanUtils.isTrue(leg.isExternalInventory()));
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
    * Populate airport.
    *
    * @param airportModel the airport model
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
    * Populate atcom id.
    *
    * @param legModel the leg model
    * @param eachLeg the each leg
    */
   private void populateAtcomId(final FlightLeg legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getAtcomId()))
      {
         legModel.setAtcomId(eachLeg.getAtcomId());
      }
   }
   
   /**
    * Populate route code.
    *
    * @param legModel the leg model
    * @param eachLeg the each leg
    */
   private void populateRouteCode(final FlightLeg legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getRouteCd()))
      {
         legModel.setRouteCode(eachLeg.getRouteCd());
      }
   }
   
   /**
    * Populate cyc date.
    *
    * @param legModel the leg model
    * @param eachLeg the each leg
    */
   private void populateCycDate(final FlightLeg legModel, final Leg eachLeg)
   {
      if (eachLeg.getCycDate() != null)
      {
         legModel.setCycDate(eachLeg.getCycDate().toDate());
      }
   }
   
   /**
    * Populate flight seq code.
    *
    * @param legModel the leg model
    * @param eachLeg the each leg
    */
   private void populateFlightSeqCode(final FlightLeg legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getRouteCd()))
      {
         legModel.setFlightSeqCode(StringUtils.substring(eachLeg.getRouteCd(),
            CommonwebitemsConstants.SEVEN, CommonwebitemsConstants.EIGHT));
      }
   }
   
}
