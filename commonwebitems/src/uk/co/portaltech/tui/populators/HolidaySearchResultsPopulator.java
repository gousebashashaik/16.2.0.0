/**
 *
 */
package uk.co.portaltech.tui.populators;

import static uk.co.portaltech.commons.DateUtils.format;
import static uk.co.portaltech.commons.DateUtils.newDateFormat;
import static uk.co.portaltech.commons.DateUtils.subtractDates;
import static uk.co.portaltech.commons.DateUtils.toDateTimeFormat;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.Offers;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;
import uk.co.portaltech.tui.enums.BoardBasis;
import uk.co.portaltech.tui.enums.CarrierName;
import uk.co.portaltech.tui.services.DurationHaulTypeService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;

public class HolidaySearchResultsPopulator implements Populator<Holiday, SearchResultViewData>
{
   
   private static final int TWO = 2;
   
   private static final int INDEX_ZERO = 0;
   
   private static final int INDEX_TWO = 2;
   
   private static final int INDEX_THREE = 3;
   
   @Resource
   private DurationHaulTypeService durationHaulTypeService;
   
   @Resource
   private AirportService airportService;
   
   @Resource
   private ViewSelector viewSelector;
   
   /**
    * This method populates holiday object required for view of serach results
    *
    * @param target - holiday object
    */
   @Override
   public void populate(final Holiday holiday, final SearchResultViewData target)
      throws ConversionException
   {
      
      target.setPackageId(holiday.getPackageId());
      target.setTracsUnitCode(holiday.getTracs());
      target.setSellingCode(holiday.getInventoryInfo().getSellingCode());
      target.setTracsPackageId(holiday.getInventoryInfo().getTracsPackageId());
      target.setDuration(holiday.getDuration());
      target.setProductCode(holiday.getInventoryInfo().getProductCode());
      
      if (holiday.getInventoryInfo().getTracsPackageId() != null
         && StringUtils.isNotEmpty(holiday.getInventoryInfo().getTracsPackageId()))
      {
         target.setSubProductCode(holiday.getInventoryInfo().getSubProductCode());
      }
      else if (holiday.getInventoryInfo().getAtcomId() != null
         && StringUtils.isNotEmpty(holiday.getInventoryInfo().getAtcomId()))
      {
         target.setSubProductCode(holiday.getInventoryInfo().getProm());
      }
      
      target.getAccommodation().getRatings().setTripAdvisorRating(holiday.getTripAdvisorRating());
      target.setBrandType(holiday.getAccomodation().getBrandType());
      populateAccommodationData(holiday, target);
      populateFlightData(holiday, target);
      populatePriceData(holiday, target);
      populateCoachTranferandWorldCare(holiday, target);
      if (checkAccomDetails(holiday))
      {
         populateAlternateBoardBasisList(holiday, target);
      }
   }
   
   /**
    * @param holiday
    * @return boolean
    */
   private boolean checkAccomDetails(final Holiday holiday)
   {
      return holiday.getAccomodation() != null
         && CollectionUtils.isNotEmpty(holiday.getAccomodation().getRoomDetails())
         && StringUtils.isNotEmpty(holiday.getAccomodation().getRoomDetails().get(0)
            .getBoardBasisCode());
   }
   
   /**
    * Populating board Basis for accommodation including default.
    *
    * @param holiday
    * @param target
    */
   private void populateAlternateBoardBasisList(final Holiday holiday,
      final SearchResultViewData target)
   {
      final List<BoardBasisType> allBoard = new ArrayList<BoardBasisType>();
      final BoardBasisType defaultBoard = new BoardBasisType();
      
      defaultBoard.setTotalPrice(holiday.getTpp().setScale(0, RoundingMode.HALF_UP).toString());
      defaultBoard.setTotalPricePP(holiday.getPpPrice().setScale(0, RoundingMode.HALF_UP)
         .toString());
      defaultBoard.setBoardbasisCode(holiday.getAccomodation().getRoomDetails().get(0)
         .getBoardBasisCode());
      
      if (StringUtils.isNotEmpty(holiday.getAccomodation().getRoomDetails().get(0)
         .getBoardBasisCode()))
      {
         final String boardCode =
            StringUtils.deleteWhitespace(holiday.getAccomodation().getRoomDetails().get(0)
               .getBoardBasisCode());
         defaultBoard.setName(BoardBasis.valueOf(StringUtils.chomp(boardCode, "+")).getValue());
      }
      defaultBoard.setDefaultBoardBasis(true);
      allBoard.add(defaultBoard);
      
      if (CollectionUtils.isNotEmpty(holiday.getBoardBasisData()))
      {
         for (final BoardBasisDataResponse boardBasisType : holiday.getBoardBasisData())
         {
            final BoardBasisType alternateBoard = new BoardBasisType();
            final String boardBasisPriceBeforeRounding = boardBasisType.getPrice();
            alternateBoard.setBoardbasisCode(boardBasisType.getBoardbasisCode());
            if (StringUtils.isNotEmpty(boardBasisType.getBoardbasisCode()))
            {
               final String boardCode =
                  StringUtils.deleteWhitespace(boardBasisType.getBoardbasisCode());
               alternateBoard.setName(BoardBasis.valueOf(StringUtils.chomp(boardCode, "+"))
                  .getValue());
            }
            
            alternateBoard.setTotalPrice(BigDecimal
               .valueOf(Double.parseDouble(boardBasisPriceBeforeRounding))
               .setScale(0, RoundingMode.HALF_UP).toString());
            int noOfPerson = 0;
            for (final RoomDetails room : holiday.getAccomodation().getRoomDetails())
            {
               final int person =
                  room.getOccupancy().getAdults() + room.getOccupancy().getChildren();
               noOfPerson = noOfPerson + person;
            }
            
            double priceDiff =
               Double.parseDouble(boardBasisPriceBeforeRounding)
                  - Double.parseDouble(holiday.getTpp().toString());
            priceDiff = new BigDecimal(priceDiff).setScale(TWO, RoundingMode.HALF_UP).doubleValue();
            
            if (priceDiff > 0)
            {
               
               final StringBuilder priceDifferance = new StringBuilder("+");
               priceDifferance.append(Double.toString(priceDiff));
               
               alternateBoard.setPriceDiffrence(removeTraillingZero(priceDifferance.toString()));
               final BigDecimal bda = new BigDecimal(priceDiff).setScale(TWO, RoundingMode.HALF_UP);
               
               alternateBoard.setAccomdboardpriceDiffrence(bda.toString());
               
            }
            else
            {
               alternateBoard.setPriceDiffrence(removeTraillingZero(Double.toString(priceDiff)));
               final BigDecimal bda = new BigDecimal(priceDiff).setScale(TWO, RoundingMode.HALF_UP);
               alternateBoard.setAccomdboardpriceDiffrence(bda.toString());
            }
            
            if (viewSelector.checkIsMobile())
            {
               final String priceDifference = Integer.toString((int) priceDiff);
               if (Double.parseDouble(priceDifference) >= 0)
               {
                  final StringBuilder priceDifferance = new StringBuilder("+");
                  priceDifferance.append(priceDifference);
                  alternateBoard.setPriceDiffrence(removeTraillingZero(priceDifferance.toString()));
               }
               else
               {
                  alternateBoard.setPriceDiffrence(removeTraillingZero(priceDifference));
               }
            }
            
            updateBoardBasisPPBasedOnFreeKid(alternateBoard, boardBasisType, noOfPerson,
               holiday.getOffer());
            allBoard.add(alternateBoard);
            
         }
      }
      target.setAlternateBoard(allBoard);
   }
   
   /**
    * @param alternateBoard
    * @param boardBasisType
    * @param noOfPerson
    * @param offer
    */
   private void updateBoardBasisPPBasedOnFreeKid(final BoardBasisType alternateBoard,
      final BoardBasisDataResponse boardBasisType, final int noOfPerson, final Offers offer)
   {
      int paxCount = noOfPerson;
      if (StringUtils.equalsIgnoreCase(offer.getFreeKids(), "Y"))
      {
         paxCount--;
      }
      alternateBoard.setTotalPricePP(BigDecimal
         .valueOf(Double.parseDouble(boardBasisType.getPrice()) / paxCount)
         .setScale(0, RoundingMode.HALF_UP).toString());
   }
   
   /**
    * @param priceDiff
    * @return String
    */
   private String removeTraillingZero(final String priceDiff)
   {
      return priceDiff.indexOf('.') < 0 ? priceDiff : priceDiff.replaceAll("0*$", "").replaceAll(
         "\\.$", "");
   }
   
   /**
    * @param holiday
    * @param target
    */
   private void populateCoachTranferandWorldCare(final Holiday holiday,
      final SearchResultViewData target)
   {
      target.setCoachTransfer(holiday.isCoachTransfer());
      target.setWorldCare(holiday.isWorldCare());
   }
   
   /**
    * Method to populate Flight related data
    *
    * @param holiday
    * @param resultsview
    */
   private void populateFlightData(final Holiday holiday, final SearchResultViewData resultsview)
   {
      
      final SearchResultFlightViewData flight = resultsview.getItinerary();
      
      final Leg outbound = holiday.getItinerary().getOutbound().get(0);
      
      if (outbound.getSchedule().getDepartureDate() != null)
      {
         flight.setDepartureDate(format(outbound.getSchedule().getDepartureDate()));
         if (viewSelector.checkIsMobile())
         {
            flight.setDepartureDateForMobile(newDateFormat(outbound.getSchedule()
               .getDepartureDate()));
         }
      }
      
      if (outbound.getDeparture().getCode() != null)
      {
         flight.setDepartureAirport(getAirportName(outbound.getDeparture().getCode()));
      }
      
      flight.getOutbounds().addAll(
         populateLegInfo(holiday.getItinerary().getOutbound(), null, holiday.getItinerary()
            .getInbound().size()
            + holiday.getItinerary().getOutbound().size(), false));
      
      flight.getInbounds().addAll(
         populateLegInfo(holiday.getItinerary().getInbound(), flight.getOutbounds(), holiday
            .getItinerary().getInbound().size()
            + holiday.getItinerary().getOutbound().size(), true));
      
      flight.setDreamlinerLogo(isDreamlinerPackage(flight));
   }
   
   /**
    *
    * @param legs
    * @param outboundSectors
    * @param totalNoOfSectors
    * @param isInboundSector
    * @return
    */
   @SuppressWarnings("javadoc")
   public List<SearchResultFlightDetailViewData> populateLegInfo(final List<Leg> legs,
      final List<SearchResultFlightDetailViewData> outboundSectors, final int totalNoOfSectors,
      final boolean isInboundSector)
   {
      final List<SearchResultFlightDetailViewData> listOfFlightDetails =
         new ArrayList<SearchResultFlightDetailViewData>();
      for (final Leg leg : legs)
      {
         final SearchResultFlightDetailViewData flightviewsData =
            new SearchResultFlightDetailViewData();
         populateAirportInfo(totalNoOfSectors, outboundSectors, leg, flightviewsData,
            isInboundSector);
         flightviewsData.getCarrier().setCode(getCarrierNumber(leg.getCarrier().getCode()));
         populateCarrierCode(leg, flightviewsData);
         final String carrierName =
            StringUtils.trim(StringUtils.upperCase(getCarrierName(leg.getCarrier().getCode())));
         if (StringUtils.isNotBlank(carrierName))
         {
            flightviewsData.getCarrier().setName(CarrierName.findByCode(carrierName));
         }
         flightviewsData.setDreamLinerIndicator(leg.getCarrier().isDreamLine());
         populateFlightSchedule(leg, flightviewsData);
         flightviewsData.setExternalInventory(leg.isExternalInventory());
         listOfFlightDetails.add(flightviewsData);
      }
      return listOfFlightDetails;
   }
   
   /**
    * @param leg
    * @param flightviewsData
    */
   private void populateCarrierCode(final Leg leg,
      final SearchResultFlightDetailViewData flightviewsData)
   {
      if (leg.getCarrier().getCarrier() != null)
      {
         flightviewsData.getCarrier().setCarrierCode(leg.getCarrier().getCarrier());
      }
      else
      {
         flightviewsData.getCarrier().setCarrierCode(getCarrier(leg.getCarrier().getCode()));
         
      }
   }
   
   /*
    * The previous way of taking carrier code was wrong. It was strictly assuming that the carrier
    * name consists of 3 chars. but that is wrong and implemented as below now.
    */
   private static String getCarrierNumber(final String code)
   {
      if (StringUtils.isNumeric(String.valueOf(code.charAt(INDEX_TWO))))
      {
         return code.substring(INDEX_TWO);
      }
      return code.substring(INDEX_THREE);
   }
   
   private static String getCarrier(final String code)
   {
      return getCarrierName(code);
   }
   
   private static String getCarrierName(final String code)
   {
      if (StringUtils.isNumeric(String.valueOf(code.charAt(INDEX_TWO))))
      {
         return code.substring(INDEX_ZERO, INDEX_TWO).trim();
      }
      return code.substring(INDEX_ZERO, INDEX_THREE).trim();
   }
   
   /**
    * @param leg
    * @param flightviewsData
    */
   private void populateAirportInfo(final int totalNoOfSectors,
      final List<SearchResultFlightDetailViewData> outboundSectors, final Leg leg,
      final SearchResultFlightDetailViewData flightviewsData, final boolean isInboundSector)
   {
      
      flightviewsData.setDepartureAirportCode(leg.getDeparture().getCode());
      flightviewsData.setArrivalAirportCode(leg.getArrival().getCode());
      
      /**
       * if both outbound and inbound has only one leg airport name can be mapped in the reverse
       * order & haul type would be same.
       */
      if (totalNoOfSectors == TWO && isInboundSector)
      {
         flightviewsData.setDepartureAirport(outboundSectors.get(0).getArrivalAirport());
         flightviewsData.setArrivalAirport(outboundSectors.get(0).getDepartureAirport());
         flightviewsData.setHaulType(outboundSectors.get(0).getHaulType());
      }
      else
      {
         flightviewsData.setDepartureAirport(getAirportName(leg.getDeparture().getCode()));
         flightviewsData.setArrivalAirport(getAirportName(leg.getArrival().getCode()));
         flightviewsData.setHaulType(getHaulType(leg.getDeparture().getCode(), leg.getArrival()
            .getCode()));
      }
   }
   
   /**
    * @param legDetails
    * @param flightviewsData
    */
   private void populateFlightSchedule(final Leg legDetails,
      final SearchResultFlightDetailViewData flightviewsData)
   {
      
      final DateTime departureDate = legDetails.getSchedule().getDepartureDate();
      final DateTime arrivalDate = legDetails.getSchedule().getArrivalDate();
      
      flightviewsData.getSchedule().setDepartureTime(legDetails.getSchedule().getDepTime());
      flightviewsData.getSchedule().setArrivalTime(legDetails.getSchedule().getArrTime());
      
      if ((departureDate != null) && (arrivalDate != null)
         && StringUtils.isNotEmpty(legDetails.getSchedule().getDepTime())
         && StringUtils.isNotEmpty(legDetails.getSchedule().getArrTime()))
      {
         // populate arrival date and departure date in milli seconds
         
         flightviewsData.getSchedule().setArrivalDateTimeInMilli(
            (toDateTimeFormat(format(arrivalDate), legDetails.getSchedule().getArrTime()))
               .getMillis());
         flightviewsData.getSchedule().setDepartureDateTimeInMilli(
            (toDateTimeFormat(format(departureDate), legDetails.getSchedule().getDepTime()))
               .getMillis());
         flightviewsData.getSchedule().setDepartureDate(format(departureDate));
         flightviewsData.getSchedule().setArrivalDate(format(arrivalDate));
         if (viewSelector.checkIsMobile())
         {
            flightviewsData.getSchedule().setDepartureDateForMobile(newDateFormat(departureDate));
            flightviewsData.getSchedule().setArrivalDateForMobile(newDateFormat(arrivalDate));
         }
         flightviewsData.getSchedule().setOverlapDay(subtractDates(departureDate, arrivalDate) < 0);
      }
   }
   
   /**
    * Populate the dreamliner logo - for display of dreamliner usp in UI
    *
    * @param flight
    * @return boolean
    */
   public boolean isDreamlinerPackage(final SearchResultFlightViewData flight)
   {
      
      return isEachSectorDreamLiner(flight.getOutbounds())
         && isEachSectorDreamLiner(flight.getInbounds());
   }
   
   /**
    * get Dreamliner for each sector
    *
    * @param outboundFlight
    * @return String
    */
   private boolean isEachSectorDreamLiner(
      final List<SearchResultFlightDetailViewData> outboundFlight)
   {
      
      for (final SearchResultFlightDetailViewData legs : outboundFlight)
      {
         if (!legs.isDreamLinerIndicator())
         {
            return false;
         }
      }
      return true;
   }
   
   /**
    * Method to getAirport Name from PIM
    *
    * @param airportCode
    * @return String- airportName
    */
   public String getAirportName(final String airportCode)
   {
      if (StringUtils.isNotEmpty(airportCode))
      {
         return airportService.getAirportByCode(airportCode).getName();
      }
      return null;
   }
   
   /**
    * Method to populate Holiday Price
    *
    * @param holiday
    * @param resultsview
    */
   private void populatePriceData(final Holiday holiday, final SearchResultViewData resultsview)
   {
      
      final SearchResultPriceViewData price = resultsview.getPrice();
      
      String perPersonPrice = holiday.getPpPrice().toString();
      
      String childPrice = holiday.getChildPrice().toString();
      
      if (viewSelector.checkIsMobile())
      {
         
         perPersonPrice = String.valueOf(Math.round(Float.parseFloat(perPersonPrice)));
         childPrice = String.valueOf(Math.round(Float.parseFloat(childPrice)));
      }
      
      price.setPerPerson(perPersonPrice);
      price.setTotalParty(holiday.getTpp().toString());
      price.setDiscount(holiday.getTotalDiscount().toString());
      price.setDiscountPP(holiday.getPerPersonDiscount().toString());
      // Deposit Amount calculated from Lowdepositpopulator
      
      // This below method willbe called to set Zero to hide the Discount for 3PF flights.
      setDiscountFor3PF(resultsview, price);
      
      price.setPromotionalOffer(holiday.getPromotionalOffer());
      // setting child price
      price.setChildPrice(childPrice);
   }
   
   /**
    * This method is written to set the Discount as 0 for 3PF flights. to hide ..
    *
    *
    * @param resultsview
    * @param price
    *
    */
   private void setDiscountFor3PF(final SearchResultViewData resultsview,
      final SearchResultPriceViewData price)
   {
      
      final SearchResultFlightViewData flight = resultsview.getItinerary();
      
      final List<SearchResultFlightDetailViewData> searchResultFlightDetailViewDataList =
         new ArrayList<SearchResultFlightDetailViewData>();
      
      searchResultFlightDetailViewDataList.addAll(flight.getInbounds());
      searchResultFlightDetailViewDataList.addAll(flight.getOutbounds());
      
      for (final SearchResultFlightDetailViewData searchResultFlightDetailViewData : searchResultFlightDetailViewDataList)
      {
         if (searchResultFlightDetailViewData != null
            && searchResultFlightDetailViewData.getExternalInventory() != null
            && BooleanUtils.isTrue(searchResultFlightDetailViewData.getExternalInventory()))
         {
            price.setDiscountPP("0");
            price.setDiscount("0");
            break;
         }
         
      }
   }
   
   /**
    * Method to populate Accommodation Data
    *
    * @param holiday
    * @param resultsview
    */
   private void populateAccommodationData(final Holiday holiday,
      final SearchResultViewData resultsview)
   {
      
      final SearchResultAccomodationViewData accommodation = resultsview.getAccommodation();
      accommodation.setCode(holiday.getAccomodation().getAccomCode());
      if (holiday.getAccomodation().getCommercialPriority() != null)
      {
         accommodation.setCommercialPriority(Integer.parseInt(holiday.getAccomodation()
            .getCommercialPriority().toString()));
      }
      accommodation.getRatings().setOfficialRating(holiday.getOfficialRating());
   }
   
   /**
    * gets haul type for departure airport and arrival airport
    */
   private String getHaulType(final String departureAirport, final String arrivalAirport)
   {
      return durationHaulTypeService.findHaultypeForAirports(departureAirport, arrivalAirport);
   }
}
