/**
 *
 */
package uk.co.portaltech.tui.populators;

import static uk.co.portaltech.commons.SyntacticSugar.isNotNull;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.results.Accomodation;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.InventoryInfo;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.Money;
import uk.co.portaltech.travel.model.results.Occupancy;
import uk.co.portaltech.travel.model.results.Offers;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.model.results.Schedule;
import uk.co.tui.shortlist.data.MoneyData;
import uk.co.tui.shortlist.data.ScheduleData;
import uk.co.tui.shortlist.data.ShortlistAccomData;
import uk.co.tui.shortlist.data.ShortlistFlightData;
import uk.co.tui.shortlist.data.ShortlistHolidayData;
import uk.co.tui.shortlist.data.ShortlistLegData;
import uk.co.tui.shortlist.data.ShortlistOccupancy;
import uk.co.tui.shortlist.data.ShortlistOffersData;
import uk.co.tui.shortlist.data.ShortlistPaxDetails;
import uk.co.tui.shortlist.data.ShortlistRoomData;
import uk.co.tui.shortlist.view.data.AirportViewData;

/**
 * @author akhileshvarma.d
 * 
 */
public class ShortlistHolidayPopulator implements
   Populator<List<Holiday>, List<ShortlistHolidayData>>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final List<Holiday> holidays,
      final List<ShortlistHolidayData> shortlistHolidays) throws ConversionException
   {
      for (final Holiday holiday : holidays)
      {
         final ShortlistHolidayData sHoliday = new ShortlistHolidayData();
         populateAccomData(holiday, sHoliday);
         populateItinery(holiday, sHoliday);
         populateInventoryInfo(holiday, sHoliday);
         sHoliday.setAlternateBoard(holiday.getBoardBasisData());
         sHoliday.setDepositAmount(holiday.getDepositAmount());
         sHoliday.setDuration(holiday.getDuration());
         sHoliday.setPackageId(holiday.getPackageId());
         sHoliday.setPerPersonDiscount(holiday.getPerPersonDiscount());
         sHoliday.setPerPersonPrice(holiday.getPerPersonPrice());
         sHoliday.setPrice(holiday.getPrice());
         sHoliday.setTotalDiscount(holiday.getTotalDiscount());
         sHoliday.setTotalPrice(holiday.getTotalPrice());
         sHoliday.setTracs(holiday.getTracs());
         sHoliday.setWorldCare(holiday.isWorldCare());
         sHoliday.setChildPrice(holiday.getChildPrice());
         sHoliday.setPpDiscount(holiday.getPpDiscount());
         sHoliday.setPpPrice(holiday.getPpPrice());
         sHoliday.setTpp(holiday.getTpp());
         sHoliday.setPromotionalOffer(holiday.getPromotionalOffer());
         shortlistHolidays.add(sHoliday);
      }

   }

   /**
    * @param holiday
    * @param sHoliday
    */
   private void populateInventoryInfo(final Holiday holiday, final ShortlistHolidayData sHoliday)
   {
      final uk.co.tui.shortlist.data.InventoryInfo inventory =
         new uk.co.tui.shortlist.data.InventoryInfo();
      final InventoryInfo info = holiday.getInventoryInfo();
      inventory.setAtcomId(info.getAtcomId());
      inventory.setPrimeSubProductCode(info.getPrimeSubProductCode());
      inventory.setProductCode(info.getProductCode());
      inventory.setProm(info.getProm());
      inventory.setSellingCode(info.getSellingCode());
      inventory.setSubProductCode(info.getSubProductCode());
      inventory.setTracsPackageId(info.getTracsPackageId());

      sHoliday.setInventoryInfo(inventory);
   }

   /**
    * @param holiday
    * @param sHoliday
    */
   private void populateItinery(final Holiday holiday, final ShortlistHolidayData sHoliday)
   {
      final List<Leg> outboundlegs = holiday.getItinerary().getOutbound();
      final List<Leg> inboundlegs = holiday.getItinerary().getInbound();

      final List<ShortlistLegData> outbounds = new ArrayList<ShortlistLegData>();
      final List<ShortlistLegData> inbounds = new ArrayList<ShortlistLegData>();

      populateLegData(outboundlegs, outbounds);
      populateLegData(inboundlegs, inbounds);

      sHoliday.getItinerary().setInbound(inbounds);
      sHoliday.getItinerary().setOutbound(outbounds);
   }

   /**
    * @param holiday
    * @param sHoliday
    */
   private void populateAccomData(final Holiday holiday, final ShortlistHolidayData sHoliday)
   {
      final Accomodation accomodation = holiday.getAccomodation();
      final ShortlistAccomData accomData = new ShortlistAccomData();
      accomData.setAccomCode(accomodation.getAccomCode());
      accomData.setAccomodationName(accomodation.getAccomodationName());
      accomData.setAccomStartDate(accomodation.getAccomStartDate());
      populateRoomDetails(accomodation.getRoomDetails(), accomData);
      accomData.setBrandType(accomodation.getBrandType());
      accomData.setCommercialPriority(accomodation.getCommercialPriority());
      populatePrice(accomodation, accomData);
      accomData.setOfficialRating(holiday.getOfficialRating());
      sHoliday.setAccomodation(accomData);

   }

   /**
    * @param accomodation
    * @param accomData
    */
   private void populatePrice(final Accomodation accomodation, final ShortlistAccomData accomData)
   {
      final Money money = accomodation.getPrice();
      final MoneyData mData = new MoneyData();

      mData.setCurrency(money.getCurrency());
      mData.setPrecision(money.getPrecision());
      mData.setValue(money.getValue());

      accomData.setPrice(mData);
   }

   private void populateRoomDetails(final List<RoomDetails> roomDetails,
      final ShortlistAccomData accomData)
   {
      final List<ShortlistRoomData> sRoomData = new ArrayList<ShortlistRoomData>();
      for (final RoomDetails room : roomDetails)
      {
         final ShortlistRoomData roomData = new ShortlistRoomData();

         roomData.setBoardBasisCode(room.getBoardBasisCode());
         roomData.setChargeablePaxCount(room.getChargeablePaxCount());
         roomData.setDiscount(room.getDiscount());
         roomData.setDiscountPerPerson(room.getDiscountPerPerson());
         roomData.setNoOfRooms(room.getNoOfRooms());
         roomData.setPrice(room.getPrice());
         roomData.setPricePerPerson(room.getPricePerPerson());
         roomData.setRoomCode(room.getRoomCode());
         roomData.setRoomType(room.getRoomType());

         populateOccupancy(room.getOccupancy(), roomData);
         populateOffers(room.getOffer(), roomData);

         sRoomData.add(roomData);

      }

      accomData.setRoomDetails(sRoomData);
   }

   private void populateOccupancy(final Occupancy occupancy, final ShortlistRoomData sRoomData)
   {
      final ShortlistOccupancy sOccupancy = new ShortlistOccupancy();
      sOccupancy.setAdults(occupancy.getAdults());
      sOccupancy.setChildren(occupancy.getChildren());
      sOccupancy.setInfant(occupancy.getInfant());
      sOccupancy.setSeniors(occupancy.getSeniors());
      final List<ShortlistPaxDetails> sPaxDetails = new ArrayList<ShortlistPaxDetails>();
      for (final PaxDetail paxDetail : occupancy.getPaxDetail())
      {
         final ShortlistPaxDetails details = new ShortlistPaxDetails();
         details.setAge(paxDetail.getAge());
         details.setId(paxDetail.getId());
         sPaxDetails.add(details);
      }
      sOccupancy.setPaxDetail(sPaxDetails);

      sRoomData.setOccupancy(sOccupancy);

   }

   private void populateOffers(final Offers offers, final ShortlistRoomData sRoomData)
   {
      final ShortlistOffersData sOffers = new ShortlistOffersData();
      sOffers.setCoachTransfer(offers.getCoachTransfer());
      sOffers.setFreeCarHire(offers.getFreeCarHire());
      sOffers.setFreeKids(offers.getFreeKids());
      sRoomData.setOffer(sOffers);
   }

   private void populateLegData(final List<Leg> legs, final List<ShortlistLegData> legDataList)
   {
      for (final Leg eachLeg : legs)
      {
         final ShortlistLegData legData = new ShortlistLegData();
         final ScheduleData scheduleData = new ScheduleData();
         populateFlightSchedule(eachLeg, scheduleData);

         final ShortlistFlightData carrier = new ShortlistFlightData();
         populateCarrierInformation(eachLeg, carrier);

         legData.setSchedule(scheduleData);
         legData.setCarrier(carrier);
         populateAtcomId(legData, eachLeg);
         populateRouteCode(legData, eachLeg);
         populateCycDate(legData, eachLeg);
         populateExternalInventory(legData, eachLeg);
         setAirports(eachLeg, legData);
         legDataList.add(legData);
      }
   }

   /**
    * @param eachLeg
    * @param scheduleData
    */
   private void populateFlightSchedule(final Leg eachLeg, final ScheduleData scheduleData)
   {
      final Schedule schedule = eachLeg.getSchedule();
      if (isNotNull(schedule))
      {
         scheduleData.setArrivalDate(schedule.getArrivalDate());
         scheduleData.setDepartureDate(schedule.getDepartureDate());
         scheduleData.setArrTime(schedule.getArrTime());
         scheduleData.setDepTime(schedule.getDepTime());
      }

   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateCycDate(final ShortlistLegData legModel, final Leg eachLeg)
   {
      if (eachLeg.getCycDate() != null)
      {
         legModel.setCycDate(eachLeg.getCycDate());
      }
   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateRouteCode(final ShortlistLegData legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getRouteCd()))
      {
         legModel.setRouteCd(eachLeg.getRouteCd());
      }
   }

   /**
    * @param legModel
    * @param eachLeg
    */
   private void populateAtcomId(final ShortlistLegData legModel, final Leg eachLeg)
   {
      if (StringUtils.isNotBlank(eachLeg.getAtcomId()))
      {
         legModel.setAtcomId(eachLeg.getAtcomId());
      }
   }

   /**
    * Populate external inventory.
    * 
    * @param legModel the leg model
    * @param leg the flight leg
    */
   private void populateExternalInventory(final ShortlistLegData legModel, final Leg leg)
   {
      legModel.setExternalInventory(leg.isExternalInventory());
   }

   /**
    * To populate the Arrival Airport and Departure Airport to the Leg object.
    * 
    * @param eachLeg the each leg
    * @param legModel the leg model
    */
   private void setAirports(final Leg eachLeg, final ShortlistLegData legModel)
   {
      final AirportViewData arrivalAirport = new AirportViewData();
      arrivalAirport.setCode(eachLeg.getArrivalAirport().getCode());
      legModel.setArrivalAirport(arrivalAirport);
      final AirportViewData departureAirport = new AirportViewData();
      departureAirport.setCode(eachLeg.getDepartureAirport().getCode());
      legModel.setDepartureAirport(departureAirport);
   }

   private void populateCarrierInformation(final Leg eachLeg, final ShortlistFlightData carrier)
   {
      final uk.co.portaltech.travel.model.results.Flight flight = eachLeg.getCarrier();
      carrier.setCode(flight.getCode());
      carrier.setCarrier(flight.getCarrier());
      carrier.setDreamLine(flight.isDreamLine());

   }

}
