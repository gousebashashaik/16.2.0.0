/**
 *
 */
package uk.co.portaltech.tui.populators;

import static uk.co.portaltech.commons.DateUtils.format;
import static uk.co.portaltech.commons.DateUtils.newDateFormat;
import static uk.co.portaltech.commons.DateUtils.subtractDates;
import static uk.co.portaltech.commons.DateUtils.toDateTimeFormat;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;
import uk.co.portaltech.travel.thirdparty.endeca.enums.BrandName;
import uk.co.portaltech.tui.enums.BoardBasis;
import uk.co.portaltech.tui.enums.CarrierName;
import uk.co.portaltech.tui.model.RoomAvailabilityIndicatorModel;
import uk.co.portaltech.tui.services.DurationHaulTypeService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.shortlist.data.ShortlistHolidayData;
import uk.co.tui.shortlist.data.ShortlistLegData;
import uk.co.tui.shortlist.data.ShortlistPaxDetails;
import uk.co.tui.shortlist.data.ShortlistRoomData;

/**
 * @author akhileshvarma.d
 *
 */
public class IscapeShortlistHolidayPopulator implements
   Populator<ShortlistHolidayData, SearchResultViewData>
{

   private static final String[] SPECIAL_CARRIER_CODES = { "A3", "S4", "X3", "X9" };

   private static final String CARRIER_NAME = "TBA";

   private static final int ZERO = 0;

   private static final int TWO = 2;

   private static final int THREE = 3;

   private static final String CARRIER_NUM_REG_EX = "[0-9]+$";

   @Resource
   private DurationHaulTypeService durationHaulTypeService;

   @Resource
   private AirportService airportService;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private ProductService productService;

   @Resource
   private FeatureService featureService;

   @Resource(name = "tuiUtilityService")
   private TuiUtilityService tuiUtilityService;

   @Resource
   private BrandUtils brandUtils;

   private static final TUILogUtils LOG = new TUILogUtils("IscapeShortlistHolidayPopulator");

   private static final String STRAP_LINE = "strapline";

   /**
    * This method populates holiday object required for view of serach results
    *
    * @param target - holiday object
    */
   @Override
   public void populate(final ShortlistHolidayData holiday, final SearchResultViewData target)
      throws ConversionException
   {
      final AccommodationModel accommodationModel =
         getAccommodationModel(holiday.getAccomodation().getAccomCode());
      target.setPackageId(holiday.getPackageId());
      target.setTracsUnitCode(holiday.getTracs());
      target.setSellingCode(holiday.getInventoryInfo().getSellingCode());
      target.setTracsPackageId(holiday.getInventoryInfo().getTracsPackageId());
      target.setDuration(holiday.getDuration());
      target.setProductCode(holiday.getInventoryInfo().getProductCode());
      target.setSubProductCode(holiday.getInventoryInfo().getSubProductCode());

      target.setBrandType(holiday.getAccomodation().getBrandType());
      populateAccommodationData(holiday, target, accommodationModel);
      populateFlightData(holiday, target);
      populatePriceData(holiday, target);
      populateCoachTranferandWorldCare(holiday, target);

      populateRoomsData(holiday.getAccomodation().getRoomDetails(), target.getAccommodation()
         .getRooms(), holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate(),
         accommodationModel);
      if (holiday.getAccomodation().getRoomDetails().get(0).getBoardBasisCode() != null)
      {
         populateAlternateBoardBasisList(holiday, target);
      }
   }

   private void populatePriceData(final ShortlistHolidayData holiday,
      final SearchResultViewData resultsview)
   {
      final SearchResultPriceViewData price = resultsview.getPrice();

      final String perPersonPrice = holiday.getPpPrice().toString();

      final String childPrice = holiday.getChildPrice().toString();

      price.setPerPerson(perPersonPrice);
      price.setTotalParty(holiday.getTpp().toString());
      price.setDiscount(holiday.getTotalDiscount().toString());
      price.setDiscountPP(holiday.getPpDiscount().toString());
      // Deposit Amount calculated from Lowdepositpopulator

      // This below method willbe called to set Zero to hide the Discount for 3PF flights.
      setDiscountFor3PF(resultsview, price);

      price.setPromotionalOffer(holiday.getPromotionalOffer());
      // setting child price
      price.setChildPrice(childPrice);
   }

   private void setDiscountFor3PF(final SearchResultViewData resultsview,
      final SearchResultPriceViewData price)
   {

      final SearchResultFlightViewData flight = resultsview.getItinerary();

      final List<SearchResultFlightDetailViewData> searchResultFlightDetailViewDataList =
         new ArrayList<SearchResultFlightDetailViewData>();

      searchResultFlightDetailViewDataList.addAll(flight.getInbounds());
      searchResultFlightDetailViewDataList.addAll(flight.getOutbounds());

      populateDiscountPrice(price, searchResultFlightDetailViewDataList);
   }

   /**
    * @param price
    * @param searchResultFlightDetailViewDataList
    */
   private void populateDiscountPrice(final SearchResultPriceViewData price,
      final List<SearchResultFlightDetailViewData> searchResultFlightDetailViewDataList)
   {
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
    * Populating board Basis for accommodation including default.
    *
    * @param holiday
    * @param target
    */
   private void populateAlternateBoardBasisList(final ShortlistHolidayData holiday,
      final SearchResultViewData target)
   {
      final ArrayList<BoardBasisType> allBoard = new ArrayList<BoardBasisType>();
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

      if (CollectionUtils.isNotEmpty(holiday.getAlternateBoard()))
      {
         for (final BoardBasisDataResponse boardBasisType : holiday.getAlternateBoard())
         {
            final BoardBasisType alternateBoard = new BoardBasisType();
            final String boardBasisPriceBeforeRounding = boardBasisType.getPrice();
            alternateBoard.setBoardbasisCode(boardBasisType.getBoardbasisCode());
            populateBoardName(boardBasisType, alternateBoard);

            alternateBoard.setTotalPrice(BigDecimal
               .valueOf(Double.parseDouble(boardBasisPriceBeforeRounding))
               .setScale(0, RoundingMode.HALF_UP).toString());
            final int noOfPerson = countNoofPerson(holiday);

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

            }

            if (viewSelector.checkIsMobile())
            {
               populateAlternateBoardForMobile(alternateBoard, priceDiff);
            }

            alternateBoard.setTotalPricePP(BigDecimal
               .valueOf(Double.parseDouble(boardBasisType.getPrice()) / noOfPerson)
               .setScale(0, RoundingMode.HALF_UP).toString());
            allBoard.add(alternateBoard);

         }
      }
      target.setAlternateBoard(allBoard);
   }

   /**
    * @param alternateBoard
    * @param priceDiff
    */
   private void populateAlternateBoardForMobile(final BoardBasisType alternateBoard,
      final double priceDiff)
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

   /**
    * @param holiday
    * @return
    */
   private int countNoofPerson(final ShortlistHolidayData holiday)
   {
      int noOfPerson = 0;
      for (final ShortlistRoomData room : holiday.getAccomodation().getRoomDetails())
      {
         final int person = room.getOccupancy().getAdults() + room.getOccupancy().getChildren();
         noOfPerson = noOfPerson + person;
      }
      return noOfPerson;
   }

   /**
    * @param boardBasisType
    * @param alternateBoard
    */
   private void populateBoardName(final BoardBasisDataResponse boardBasisType,
      final BoardBasisType alternateBoard)
   {
      if (StringUtils.isNotEmpty(boardBasisType.getBoardbasisCode()))
      {
         final String boardCode = StringUtils.deleteWhitespace(boardBasisType.getBoardbasisCode());
         alternateBoard.setName(BoardBasis.valueOf(StringUtils.chomp(boardCode, "+")).getValue());
      }
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
   private void populateCoachTranferandWorldCare(final ShortlistHolidayData holiday,
      final SearchResultViewData target)
   {

      target.setWorldCare(holiday.isWorldCare());
   }

   /**
    * Population of room Details and Low Availability Indicator for rooms
    *
    * @param roomDetails
    * @param searchResultRooms
    * @param dateTime
    */

   public void populateRoomsData(final List<ShortlistRoomData> roomDetails,
      final List<SearchResultRoomsData> searchResultRooms, final DateTime dateTime,
      final AccommodationModel accommodationModel)
   {

      final Map<String, RoomModel> roomMap = new HashMap<String, RoomModel>();

      populateRoomsMap(accommodationModel, roomMap);

      if (CollectionUtils.isNotEmpty(roomDetails))
      {
         for (final ShortlistRoomData rooms : roomDetails)
         {

            final RoomAvailabilityIndicatorModel roomAvailabilityIndicatorModel =
               new RoomAvailabilityIndicatorModel();

            roomAvailabilityIndicatorModel.setInventoryCount(rooms.getNoOfRooms());

            final SearchResultRoomsData searchResultRoomsData = new SearchResultRoomsData();

            searchResultRoomsData.setRoomCode(rooms.getRoomCode());
            searchResultRoomsData.setSellingout(rooms.getNoOfRooms());
            searchResultRoomsData.setBoardBasisCode(rooms.getBoardBasisCode());
            searchResultRoomsData.setBoardType(BoardBasis.valueOf(rooms.getBoardBasisCode())
               .getValue());

            if (rooms.getOccupancy() != null)
            {
               searchResultRoomsData.getOccupancy().setAdults(rooms.getOccupancy().getAdults());
               searchResultRoomsData.getOccupancy().setChildren(rooms.getOccupancy().getChildren());
               searchResultRoomsData.getOccupancy().setInfant(rooms.getOccupancy().getInfant());
               populatePaxDetail(rooms.getOccupancy().getPaxDetail(), searchResultRoomsData);

            }
            searchResultRooms.add(searchResultRoomsData);
         }

      }

   }

   /**
    * @param accommodationModel
    * @param roomMap
    */
   private void populateRoomsMap(final AccommodationModel accommodationModel,
      final Map<String, RoomModel> roomMap)
   {
      for (final RoomModel roomModel : accommodationModel.getRooms())
      {
         roomMap.put(roomModel.getRoomTypeCode(), roomModel);
      }
   }

   /**
    * @param paxDetail
    * @param searchResultRoomsData
    */
   private void populatePaxDetail(final List<ShortlistPaxDetails> paxDetail,
      final SearchResultRoomsData searchResultRoomsData)
   {
      final List<PaxDetail> paxDetails = new ArrayList<PaxDetail>();
      for (final ShortlistPaxDetails pax : paxDetail)
      {
         final PaxDetail paxDetail1 = new PaxDetail();
         paxDetail1.setId(pax.getId());
         paxDetail1.setAge(pax.getAge());
         paxDetails.add(paxDetail1);
      }

      searchResultRoomsData.getOccupancy().setPaxDetail(paxDetails);

   }

   /**
    * Method to populate Flight related data
    *
    * @param holiday
    * @param resultsview
    */
   private void populateFlightData(final ShortlistHolidayData holiday,
      final SearchResultViewData resultsview)
   {

      final SearchResultFlightViewData flight = resultsview.getItinerary();

      final ShortlistLegData outbound = holiday.getItinerary().getOutbound().get(0);

      if (outbound.getSchedule().getDepartureDate() != null)
      {
         flight.setDepartureDate(format(outbound.getSchedule().getDepartureDate()));
         if (viewSelector.checkIsMobile())
         {
            flight.setDepartureDateForMobile(newDateFormat(outbound.getSchedule()
               .getDepartureDate()));
         }
      }

      populateDeptAirport(flight, outbound);

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
    * @param flight
    * @param outbound
    */
   private void populateDeptAirport(final SearchResultFlightViewData flight,
      final ShortlistLegData outbound)
   {
      if (outbound.getDepartureAirport().getCode() != null)
      {
         flight.setDepartureAirport(getAirportName(outbound.getDepartureAirport().getCode()));
      }
   }

   /**
    *
    * @param list
    * @param outboundSectors
    * @param totalNoOfSectors
    * @param isInboundSector
    * @return
    */
   @SuppressWarnings("javadoc")
   public List<SearchResultFlightDetailViewData> populateLegInfo(final List<ShortlistLegData> list,
      final List<SearchResultFlightDetailViewData> outboundSectors, final int totalNoOfSectors,
      final boolean isInboundSector)
   {

      final List<SearchResultFlightDetailViewData> listOfFlightDetails =
         new ArrayList<SearchResultFlightDetailViewData>();
      for (final ShortlistLegData leg : list)
      {
         final SearchResultFlightDetailViewData flightviewsData =
            new SearchResultFlightDetailViewData();

         populateAirportInfo(totalNoOfSectors, outboundSectors, leg, flightviewsData,
            isInboundSector);

         flightviewsData.getCarrier().setCode(getCarrierCode(leg.getCarrier().getCode()));

         final String carrierName =
            StringUtils.upperCase(getCarrierName(leg.getCarrier().getCode()));

         if (StringUtils.isNotBlank(carrierName))
         {
            try
            {
               flightviewsData.getCarrier().setName(CarrierName.valueOf(carrierName).getValue());
            }
            catch (final IllegalArgumentException ex)
            {
               flightviewsData.getCarrier().setName("");
               LOG.error("ConversionException", ex);
            }
         }

         flightviewsData.setDreamLinerIndicator(leg.getCarrier().isDreamLine());

         populateFlightSchedule(leg, flightviewsData);

         flightviewsData.setExternalInventory(leg.getExternalInventory());

         listOfFlightDetails.add(flightviewsData);
      }
      return listOfFlightDetails;
   }

   /*
    * The previous way of taking carrier code was wrong. It was strictly assuming that the carrier
    * name consists of 3 chars. but that is wrong and implemented as below now.
    */
   private static String getCarrierCode(final String code)
   {
      if (StringUtils.startsWithIgnoreCase(code, CARRIER_NAME))
      {
         return code.substring(THREE).trim();
      }
      else if (ArrayUtils.contains(SPECIAL_CARRIER_CODES, code.substring(ZERO, TWO)))
      {
         return code.substring(TWO).trim();
      }
      else
      {
         // Ex: if code is TOM123 return 123
         return code.replaceAll(code.replaceAll(CARRIER_NUM_REG_EX, StringUtils.EMPTY),
            StringUtils.EMPTY);
      }
   }

   private static String getCarrierName(final String code)
   {
      if (StringUtils.startsWithIgnoreCase(code, CARRIER_NAME))
      {
         return code.substring(ZERO, THREE).trim();
      }
      else if (ArrayUtils.contains(SPECIAL_CARRIER_CODES, code.substring(ZERO, TWO)))
      {
         return code.substring(ZERO, TWO).trim();
      }
      else
      {
         return code.replaceAll(CARRIER_NUM_REG_EX, StringUtils.EMPTY);
      }
   }

   /**
    * @param leg
    * @param flightviewsData
    */
   private void populateAirportInfo(final int totalNoOfSectors,
      final List<SearchResultFlightDetailViewData> outboundSectors, final ShortlistLegData leg,
      final SearchResultFlightDetailViewData flightviewsData, final boolean isInboundSector)
   {

      flightviewsData.setDepartureAirportCode(leg.getDepartureAirport().getCode());
      flightviewsData.setArrivalAirportCode(leg.getArrivalAirport().getCode());

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
         flightviewsData.setDepartureAirport(getAirportName(leg.getDepartureAirport().getCode()));
         flightviewsData.setArrivalAirport(getAirportName(leg.getArrivalAirport().getCode()));
         flightviewsData.setHaulType(getHaulType(leg.getDepartureAirport().getCode(), leg
            .getArrivalAirport().getCode()));
      }
   }

   /**
    * @param legDetails
    * @param flightviewsData
    */
   private void populateFlightSchedule(final ShortlistLegData legDetails,
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
    * Method to populate Accommodation Data
    *
    * @param holiday
    * @param resultsview
    */
   private void populateAccommodationData(final ShortlistHolidayData holiday,
      final SearchResultViewData resultsview, final AccommodationModel accommodationModel)
   {

      final SearchResultAccomodationViewData accommodation = resultsview.getAccommodation();
      accommodation.setCode(holiday.getAccomodation().getAccomCode());
      accommodation.setAccomType(accommodationModel.getType().getCode());
      if (holiday.getAccomodation().getCommercialPriority() != null)
      {
         accommodation.setCommercialPriority(Integer.parseInt(holiday.getAccomodation()
            .getCommercialPriority().toString()));
      }

      final Map locations = new LinkedHashMap<String, String>();

      populateCategoryContent(resultsview, accommodationModel.getSupercategories(), locations);
      resultsview.getAccommodation().setLocationMap(locations);
   }

   /**
    * gets haul type for departure airport and arrival airport
    */
   private String getHaulType(final String departureAirport, final String arrivalAirport)
   {
      return durationHaulTypeService.findHaultypeForAirports(departureAirport, arrivalAirport);
   }

   public AccommodationModel getAccommodationModel(final String code)
   {
      return (AccommodationModel) fetchProductModel(code);
   }

   private ProductModel fetchProductModel(final String code)
   {
      ProductModel product = null;
      try
      {
         product = productService.getProductForCode(code);
         Validate.notNull(product);
      }
      catch (final ModelNotFoundException exc)
      {
         LOG.error("THe Product model with code " + code + " does not exist in PIM.", exc);
      }
      catch (final UnknownIdentifierException e)
      {
         LOG.error(
            "Endeca has product code and PIM doesnt have" + code + " does not exist in PIM.", e);
      }
      return product;
   }

   public void populateCategoryContent(final SearchResultViewData target,
      final Collection<CategoryModel> productCategories, final Map locations)
   {
      if (CollectionUtils.isNotEmpty(productCategories))
      {
         for (final CategoryModel category : productCategories)
         {
            processCategory(target, category, locations);
         }
      }
   }

   /**
    * @param target
    * @param category
    */
   private void processCategory(final SearchResultViewData target, final CategoryModel category,
      final Map locations)
   {
      if (category instanceof LocationModel)
      {
         if (locations.size() < TWO)
         {
            addLocation(target, category, locations);
         }

      }
      else if (category instanceof ProductRangeModel)
      {
         addProductRange(target, category);
      }
   }

   /**
    * @param target
    * @param category
    */
   private void addLocation(final SearchResultViewData target, final CategoryModel category,
      final Map locations)
   {

      final LocationModel locModel = (LocationModel) category;
      locations.put(locModel.getType().getCode(), locModel.getName());
      populateCategoryContent(target, locModel.getSupercategories(), locations);

      /** added to fix bug 112463 */
      if (locModel.getType().equals(LocationType.DESTINATION))
      {
         target.getAccommodation().getLocation().getDestination().setCode(category.getCode());

      }
      else if (locModel.getType().equals(LocationType.RESORT))
      {

         target.getAccommodation().getLocation().getResort().setCode(category.getCode());

      }
      else if (locModel.getType().equals(LocationType.COUNTRY))
      {

         target.getAccommodation().getLocation().getCountry().setCode(category.getCode());

      }
      else if (locModel.getType().equals(LocationType.REGION))
      {

         target.getAccommodation().getLocation().getRegion().setCode(category.getCode());

      }
   }

   /**
    * @param target
    * @param category
    */
   private void addProductRange(final SearchResultViewData target, final CategoryModel category)
   {

      final ProductRangeModel prodRange = (ProductRangeModel) category;

      final String siteBrand = tuiUtilityService.getSiteBrand();
      final String productRangeBrand = brandUtils.getFeatureServiceBrand(prodRange.getBrands());
      final String holidayBrand = target.getBrandType();

      final String aniteBrandEnum = BrandName.valueOf(siteBrand).getBrandName();

      if (StringUtils.equalsIgnoreCase(holidayBrand, aniteBrandEnum)
         && StringUtils.equalsIgnoreCase(siteBrand, productRangeBrand))
      {
         target.getAccommodation().setDifferentiatedType(category.getName());
         target.getAccommodation().setDifferentiatedCode(category.getCode());
         target.getAccommodation().setDifferentiatedProduct(true);

         Map<String, List<Object>> featureCodeMap = null;
         try
         {
            featureCodeMap =
               featureService.getOptimizedValuesForFeatures(
                  Arrays.asList(new String[] { STRAP_LINE }), prodRange, new Date(), null);
            target.getAccommodation().putFeatureCodesAndValues(featureCodeMap);
         }
         catch (final UnknownIdentifierException uke)
         {
            LOG.error("Error loading strapline from Product with Code:" + prodRange.getCode(), uke);
         }
      }

   }
}
