/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.BookFlowConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.SupplierProductDetails;
import uk.co.tui.book.view.data.AccommodationViewData;
import uk.co.tui.book.view.data.BoardBasisViewData;
import uk.co.tui.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.book.view.data.ExtraFacilityViewData;
import uk.co.tui.book.view.data.ExtraFacilityViewDataContainer;
import uk.co.tui.book.view.data.FlightOptionsViewData;
import uk.co.tui.book.view.data.Leg;
import uk.co.tui.book.view.data.PackageViewData;
import uk.co.tui.book.view.data.PriceBreakDownViewData;
import uk.co.tui.book.view.data.RoomViewData;

/**
 *
 * @author srikanth.bs
 *
 *         Analytics Populator Class for Flight Options
 *
 */

public class FlightOptionsAnalyticsPopulator implements
   Populator<Object, Map<String, WebAnalytics>>
{

   /** Offset for monthyear calculation */
   private static final int MONTH_YEAR_OFFSET = 8;

   /** start Offset for day calculation */
   private static final int DAY_OFFSET_STRAT = 4;

   /** start end for day calculation */
   private static final int DAY_OFFSET_END = 9;

   @Resource
   private AnalyticsHelper analyticsHelper;

   @Resource
   private PackageCartService packageCartService;

   private static final String TOTAL_PRICE = "total price";

   /** Adding Fields To Capture The Filter Details For Analytics */
   private static final String MONTHYEAR_FORMAT = "MMM yyyy";

   /** The Constant for DISCOUNT */
   private static final String DISCOUNT = "Online Discount";

   /** The Constant for integer TEN */
   private static final int TEN = 10;

   /** The Constant for zero */
   private static final String ZERO = "0";

   /** The Constant for Dreamliner Flight */
   private static final String DREAM_FL = "DreamFl";

   /**
    * Analytics populator for Flight Options page
    *
    * @param source - it is a FlightOptionsViewData object contains all the data related to the
    *           flight options page
    * @param analyticMap -it is target object for the analytics
    */
   @Override
   public void populate(final Object source, final Map<String, WebAnalytics> analyticMap)
      throws ConversionException
   {

      if (source instanceof FlightOptionsViewData)
      {
         final FlightOptionsViewData flightOptionsViewData = (FlightOptionsViewData) source;
         final PackageViewData packageViewData = flightOptionsViewData.getPackageViewData();
         populateWhereTo(analyticMap, packageViewData);
         populateAirport(analyticMap, packageViewData);
         populateDepartureDate(analyticMap, packageViewData);
         populateSeason(analyticMap);
         populatePartyComposition(analyticMap, packageViewData.getAccomViewData().get(0));
         populateDuration(analyticMap, packageViewData);
         populatePrice(analyticMap, packageViewData);
         populateLimAvB(analyticMap, packageViewData.getAccomViewData().get(0));
         populateLimAvS(analyticMap, flightOptionsViewData.getExtraFacilityViewDataContainer());
         populateDreamFI(analyticMap, packageViewData);
         populatePax(analyticMap, packageViewData);
         populateBoard(analyticMap, packageViewData);
         populateSubProductCode(analyticMap);
         populateTuiAnc(analyticMap);
      }
   }

   /**
    * populates Limited Availability Indicator Parameter - Beds Limited Availability Indicator on it
    * then It will populate with the numerical value that references the number of rooms left and
    * not the number of seats no indicator then populate ‘null’
    *
    * @param analyticMap
    * @param extraFacilityViewDataContainer
    */
   private void populateLimAvS(final Map<String, WebAnalytics> analyticMap,
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      String limAvS = null;
      final ExtraFacilityCategoryViewData seatOptions =
         extraFacilityViewDataContainer.getSeatOptions();

      final List<ExtraFacilityViewData> seatExtraFaclities = seatOptions.getExtraFacilityViewData();
      if (seatExtraFaclities.get(0).isSelected())
      {
         limAvS = String.valueOf(seatExtraFaclities.get(0).getAvailableQuantity());
      }
      analyticMap.put("LimAvS", new WebAnalytics("LimAvS", limAvS));
   }

   /**
    * populates Limited Availability Indicator Parameter - Seats Limited Availability Indicator on
    * it then It will populate with the numerical value that references the number of seats left and
    * not the number of rooms no indicator then populate ‘null’
    *
    * @param analyticMap
    * @param accommodationViewData
    */
   private void populateLimAvB(final Map<String, WebAnalytics> analyticMap,
      final AccommodationViewData accommodationViewData)
   {
      int count = 0;
      String limAvB = "";
      final StringBuilder limAvBComp = new StringBuilder();
      if (accommodationViewData.getRoomViewData() != null)
      {
         for (final RoomViewData room : accommodationViewData.getRoomViewData())
         {
            count++;
            limAvBComp.append("R");
            limAvBComp.append(count);
            limAvBComp.append("-");
            limAvBComp.append(room.getQuantity());
            limAvBComp.append(",");
         }
         if (StringUtils.isNotEmpty(limAvBComp.toString()))
         {
            limAvB = limAvBComp.toString();
            limAvB = StringUtils.substring(limAvB, 0, limAvB.length() - 1);
         }
      }
      analyticMap.put("LimAvB", new WebAnalytics("LimAvB", limAvB));
   }

   /**
    * This will populate either ‘Yes’ or ‘No’ depending on whether the package selected has
    * Dreamliner flights or not
    *
    * @param analyticMap
    * @param packageViewData
    */
   private void populateDreamFI(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      String dreamFl = "No";
      String dreamFlInbound = null;
      String dreamFlOutbound = null;
      if (CollectionUtils.isNotEmpty(packageViewData.getFlightViewData().get(0)
         .getOutboundSectors()))
      {
         dreamFlOutbound =
            packageViewData.getFlightViewData().get(0).getOutboundSectors().get(0)
               .getEqmtDescription();
      }
      if (CollectionUtils
         .isNotEmpty(packageViewData.getFlightViewData().get(0).getInboundSectors()))
      {
         dreamFlInbound =
            packageViewData.getFlightViewData().get(0).getInboundSectors().get(0)
               .getEqmtDescription();
      }
      if (isDreamlinerFlight(dreamFlInbound, dreamFlOutbound))
      {
         dreamFl = "Yes";
      }
      analyticMap.put(DREAM_FL, new WebAnalytics(DREAM_FL, dreamFl));
   }

   /**
    * The method checks whether dreamliner flight.
    *
    * @param dreamFlInbound
    * @param dreamFlOutbound
    * @return true if flight is dreamliner
    */
   private boolean isDreamlinerFlight(final String dreamFlInbound, final String dreamFlOutbound)
   {
      return StringUtils.isNotEmpty(dreamFlOutbound) || StringUtils.isNotEmpty(dreamFlInbound);
   }

   /**
    * @param analyticMap
    * @param packageViewData
    */
   private void populateBoard(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      String board = null;
      final List<RoomViewData> roomViewDataList =
         packageViewData.getAccomViewData().get(0).getRoomViewData();
      if (CollectionUtils.isNotEmpty(roomViewDataList))
      {
         final List<BoardBasisViewData> boardBasisList =
            roomViewDataList.get(0).getListOfBoardBasis();
         if (CollectionUtils.isNotEmpty(boardBasisList))
         {
            board = boardBasisList.get(0).getBoardBasisCode();
         }
      }
      analyticMap.put("Board", new WebAnalytics("Board", board));
   }

   /**
    * The value passed will be the numerical value of how many passengers are in the package. e.g.
    * if the packages selected was for 2 adults, 1 child and 0 infants the value would be ‘3’
    * note: MUST exclude infants
    *
    * @param analyticMap
    * @param packageViewData
    */
   private void populatePax(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      int paxSize = 0;
      if (packageViewData.getPaxViewData() != null)
      {
         // Adults + Seniors + chidren(Exclude Infants)
         paxSize =
            packageViewData.getPaxViewData().getNoOfAdults()
               + packageViewData.getPaxViewData().getNoOfSeniors()
               + packageViewData.getPaxViewData().getNoOfChildren();

      }
      analyticMap.put("Pax", new WebAnalytics("Pax", String.valueOf(paxSize)));
   }

   /**
    * The value of the parameter will be the single character/digit TRACS sub-product code
    *
    * @param analyticMap
    */
   private void populateSubProductCode(final Map<String, WebAnalytics> analyticMap)
   {
      String subProduct = null;
      final BasePackage packageModel = packageCartService.getBasePackage();
      if (packageModel.getInventory().getSupplierProductDetails() != null)
      {
         final SupplierProductDetails supplierProductDetails =
            packageModel.getInventory().getSupplierProductDetails();
         subProduct = supplierProductDetails.getProductCode();
      }
      analyticMap.put("SubProd", new WebAnalytics("SubProd", subProduct));
   }

   /**
    * Duration population for Analytics numerical value of the number of nights for the package
    * selected
    *
    * @param analyticMap
    * @param packageViewData
    */
   private void populateDuration(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      final WebAnalytics webAnalyticDur =
         new WebAnalytics("Dur", String.valueOf(packageViewData.getFlightViewData().get(0)
            .getDuration()));
      analyticMap.put("Dur", webAnalyticDur);
   }

   /**
    * Date population for AccommodationAnalytics. It will be in DD format e.g. if a customer
    * searches for a holiday departing on 03/05/2013, DepDate=03 and not DepDate=3
    *
    * It will be in MM/YYYY format e.g. if a customer searches for 13/05/2013, MonthYear=05/2013 and
    * not 5/2013.
    *
    * @param analyticMap
    * @param packageViewData
    */
   public void populateDepartureDate(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      final String deptDate = "DepDate";
      if (CollectionUtils.isNotEmpty(packageViewData.getFlightViewData().get(0)
         .getOutboundSectors()))
      {
         final List<Leg> outbounds =
            packageViewData.getFlightViewData().get(0).getOutboundSectors();
         final String date = outbounds.get(0).getSchedule().getDepartureDate();
         final String depDate = getdayOfTheDate(date);
         final String monthYear = getMonthAndYear(date);
         final LocalDate departureDate = DateUtils.toDate(monthYear, MONTHYEAR_FORMAT);
         analyticsHelper.addMonthYear(analyticMap, departureDate);
         analyticMap.put(deptDate, new WebAnalytics(deptDate, formatDate(depDate)));
      }
      else
      {
         analyticMap.put("MonthYear", new WebAnalytics("MonthYear", ""));
         analyticMap.put(deptDate, new WebAnalytics(deptDate, ""));
      }
   }

   /**
    * Airport Data Population for for Accommodation Page Analytics. The value of the parameter must
    * be the 3 character airport code of associated with the departure airport of the package
    * selected
    *
    * @param analyticMap
    * @param packageViewData
    */
   public void populateAirport(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      String depAirportCode = "";

      if (CollectionUtils.isNotEmpty(packageViewData.getFlightViewData().get(0)
         .getOutboundSectors()))
      {
         final List<Leg> outbounds =
            packageViewData.getFlightViewData().get(0).getOutboundSectors();
         depAirportCode = (outbounds.get(0)).getDepartureAirport().getCode();
      }
      final WebAnalytics webAnalyticDepAir = new WebAnalytics("DepAir", depAirportCode);
      analyticMap.put("DepAir", webAnalyticDepAir);
   }

   /**
    * WhereTo population for Accommodation Page Analytics. The value of the parameter MUST be the
    * Epic Code of the Unit selected
    *
    * @param analyticMap
    * @param packageViewData
    */
   public void populateWhereTo(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {
      String accomCode = "";
      if (StringUtils.isNotEmpty(packageViewData.getAccomViewData().get(0).getAccomCode()))
      {
         accomCode = packageViewData.getAccomViewData().get(0).getAccomCode();
      }
      final WebAnalytics webAnalyticDepAir = new WebAnalytics("WhereTo", accomCode);
      analyticMap.put("WhereTo", webAnalyticDepAir);
   }

   /**
    * Population of Price Data for Analytics. The value is to be rounded either up or down such that
    * we have no decimal places. If the decimal value is .50 please round this up. The value passed
    * is to have no ‘£’ sign or decimal places
    *
    * @param analyticMap
    * @param packageViewData
    */
   public void populatePrice(final Map<String, WebAnalytics> analyticMap,
      final PackageViewData packageViewData)
   {

      int sum = 0;
      int discount = 0;
      if (CollectionUtils.isNotEmpty(packageViewData.getPriceBreakdownViewData()))
      {
         for (final PriceBreakDownViewData priceBreakdownViewData : packageViewData
            .getPriceBreakdownViewData())
         {
            sum = populateTotalPrice(priceBreakdownViewData);
            discount = populateDiscountPrice(priceBreakdownViewData);
            analyticMap.put("Sum", new WebAnalytics("Sum", String.valueOf(sum)));
            analyticMap.put("Disc", new WebAnalytics("Disc", String.valueOf(discount)));
         }
      }
   }

   /**
    * The method calculates the discount price.
    *
    * @param priceBreakdownViewData
    * @return discount price
    */
   private int populateDiscountPrice(final PriceBreakDownViewData priceBreakdownViewData)
   {
      if (StringUtils.equalsIgnoreCase(priceBreakdownViewData.getDescription(), DISCOUNT))
      {
         return analyticsHelper.getPriceRoundedValue(priceBreakdownViewData.getPrice().toString());
      }
      return BookFlowConstants.ZERO;
   }

   /**
    * The method calculates the totalPrice.
    *
    * @param priceBreakdownViewData
    * @return total price.
    */
   private int populateTotalPrice(final PriceBreakDownViewData priceBreakdownViewData)
   {
      if (StringUtils.equalsIgnoreCase(priceBreakdownViewData.getDescription(), TOTAL_PRICE))
      {
         return analyticsHelper.getPriceRoundedValue(priceBreakdownViewData.getPrice().toString());
      }
      return BookFlowConstants.ZERO;
   }

   /**
    * Function to Populate Dreamliner Flights data for Analytics. It will populate with either
    * ‘Yes’ or ‘No’ depending on whether the package selected has Dreamliner flights or not
    *
    * @param packageData
    */
   public void populateDreamlinerIndicator(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {

      final List<SearchResultFlightDetailViewData> outbounds =
         packageData.getItinerary().getInbounds();
      final List<SearchResultFlightDetailViewData> inbounds =
         packageData.getItinerary().getOutbounds();

      String dreamlinerIndicator = "No";

      if (analyticsHelper.isdreamliner(outbounds) || analyticsHelper.isdreamliner(inbounds))
      {
         dreamlinerIndicator = "Yes";
      }

      analyticMap.put(DREAM_FL, new WebAnalytics(DREAM_FL, dreamlinerIndicator));
   }

   /**
    * Party Composition for rooms population for Analytics
    *
    * @param analyticMap
    * @param accommodationViewData Party=R1-A2/C0/I0,R2-A2/C1/I0
    */
   private void populatePartyComposition(final Map<String, WebAnalytics> analyticMap,
      final AccommodationViewData accommodationViewData)
   {

      int count = 0;
      String party = "";
      final StringBuilder partyComp = new StringBuilder();
      if (accommodationViewData.getRoomViewData() != null)
      {
         for (final RoomViewData room : accommodationViewData.getRoomViewData())
         {
            count++;
            partyComp.append("R");
            partyComp.append(count);
            partyComp.append("-");
            partyComp.append("A");
            partyComp.append(room.getNoOfAdults());
            partyComp.append("/");
            partyComp.append("C");
            partyComp.append(room.getNoOfChildren());
            partyComp.append("/");
            partyComp.append("I");
            partyComp.append(room.getNoOfInfants());
            partyComp.append(",");
         }
         if (StringUtils.isNotEmpty(partyComp.toString()))
         {
            party = partyComp.toString();
            party = StringUtils.substring(party, 0, party.length() - 1);
         }
      }
      final WebAnalytics webAnalyticDur = new WebAnalytics("Party", party);
      analyticMap.put("Party", webAnalyticDur);
   }

   /**
    * populates Season either S or W (for Summer or Winter) and the year e.g. S13, W14
    *
    * @param analyticMap
    */
   private void populateSeason(final Map<String, WebAnalytics> analyticMap)
   {
      final String season = null;
      // To be implemented..
      analyticMap.put("Season", new WebAnalytics("Season", season));
   }

   /**
    * populates TRACS Ancillaries
    *
    * @param analyticMap
    */
   private void populateTuiAnc(final Map<String, WebAnalytics> analyticMap)
   {
      final String tuiAnc = null;
      // To be implemented..
      analyticMap.put("TuiAnc", new WebAnalytics("TuiAnc", tuiAnc));
   }

   /**
    * This will append zero if the depDate is lessThan 10
    *
    * @param depDate
    * @return depDate
    */
   private String formatDate(final String depDate)
   {
      final int date = Integer.parseInt(depDate);
      if (date < TEN)
      {
         return ZERO + depDate;
      }
      return depDate;
   }

   /**
    * Gets the Month and year from date string eg: mon 17th mar 2014 ----- mar 2014
    *
    * @param date
    * @return date
    */
   private String getMonthAndYear(final String date)
   {

      return date.substring(date.length() - MONTH_YEAR_OFFSET, date.length());
   }

   /**
    * Gets the dayDate from the date string eg: mon 17th mar 2014 ----- 17
    *
    * @param date
    */
   private String getdayOfTheDate(final String date)
   {

      String modifiedDate = date.substring(DAY_OFFSET_STRAT, date.length() - DAY_OFFSET_END);
      modifiedDate = modifiedDate.replaceAll("[A-Za-z]", "");
      return modifiedDate;
   }
}
