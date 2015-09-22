/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FilterPanelFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;

/**
 *
 * Analytics Populator Class for Accommodation
 *
 */
public class AccommodationAnalyticsPopulator implements
   Populator<Object, Map<String, WebAnalytics>>
{

   @Resource
   private AnalyticsHelper analyticsHelper;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private DroolsPriorityProviderService droolsPriorityProviderService;

   private static final String NULL = "null";

   /* Adding Fields To Capture The Filter Details For Analytics */

   private static final String BUDGET_F = "BudgetF";

   private static final String RATING_F = "RatingF";

   private static final String TRIPADVISOR_F = "TARatingF";

   private static final String HOLIDAY_TYPE_F = "PackageTypeF";

   private static final String BESTFOR_F = "BestForF";

   private static final String DEPARTURE_AIRPORT_F = "DepAirF";

   private static final String DEPARTURE_TIME_GOINGOUT_F = "OutTimeF";

   private static final String DEPARTURE_TIME_REUTRN_F = "BackTimeF";

   private static final String ACCOMODATION_FEATURE_F = "FeaturesF";

   private static final String DESTINATIONS_OPTIONS_GEO_F = "GeoF";

   private static final String DESTINATIONS_OPTIONS_PRODUCT_F = "ProductF";

   private static final String FILTER_RESULT = "Results";

   private static final String TOUR_OPERATOR_FILTER = "TourOpF";

   private static final String DREAM_LINE_FILTER = "DreamF";

   private static final String ACCOMMODATION_TYPE_FILTER = "AccomTypeF";

   private static final String BOARD_FILTER = "BoardF";

   private static final String TUI_ANC_FILTER = "TuiAnc";

   private static final String BOARD = "Board";

   private static final String HOLIDAY_TYPE = "HolTypeF";

   private static final String PAX = "Pax";

   private static final String FINPOS = "FinPos";

   private static final String ORINGPOS = "OrigPos";

   private static final String DUR = "Dur";

   private static final String GREAT_DEAL = "Pers";

   private static final String GREAT_DEAL_PER = "PerDisc";

   private static final String SLIST = "SList";

   private static final String S_LIST_CHANGE = "SListChange";

   private static final String NO = "No";

   private static final String YES = "Yes";

   private static final String COLON = ":";

   private static final String PLUS = "p";

   private static final String MINUS = "m";

   /** added constructor for execution of test cases */
   public AccommodationAnalyticsPopulator()
   {

   }

   public AccommodationAnalyticsPopulator(final AnalyticsHelper analyticsHelper)
   {
      this.analyticsHelper = analyticsHelper;
   }

   /**
    * Analytics population for book flow accommodation page
    *
    * @param analyticMap
    */
   @Override
   public void populate(final Object source, final Map<String, WebAnalytics> analyticMap)
      throws ConversionException
   {

      if (source instanceof BookFlowAccommodationViewData)
      {
         final BookFlowAccommodationViewData bookFlowAccommodationViewData =
            (BookFlowAccommodationViewData) source;
         final SearchResultViewData packageData = bookFlowAccommodationViewData.getPackageData();
         final SearchResultsRequestData searchResultRequestData =
            bookFlowAccommodationViewData.getSearchRequestData();

         populateWhereTo(analyticMap, packageData);
         populateAirport(analyticMap, packageData);
         populateDepartureDate(analyticMap, packageData);
         populateDuration(analyticMap, packageData);
         populateSearchResultsCount(analyticMap, bookFlowAccommodationViewData);
         populateLimitedAvailabilityIndicator(analyticMap, packageData);
         populateBoard(analyticMap, packageData);
         populateDreamlinerIndicator(analyticMap, packageData);
         populateprice(analyticMap, packageData);
         populateOriginalPosition(analyticMap, bookFlowAccommodationViewData);
         populateFinalPosition(analyticMap, bookFlowAccommodationViewData);
         populateHolidayType(analyticMap, packageData);
         populatePartyComposition(analyticMap, bookFlowAccommodationViewData.getPackageData());
         populateSubProductCode(analyticMap, packageData.getSubProductCode());
         populateTuiAncAnalyticData(analyticMap, packageData);
         populatePaxData(analyticMap, searchResultRequestData);
         populateBoard(analyticMap, packageData);
         populateGreatDeal(analyticMap, packageData);
         populatePriceChnageForAnalytics(analyticMap, bookFlowAccommodationViewData);
      }
      else
      {
         if (source instanceof FilterPanelFlowAccommodationViewData)
         {
            final FilterPanelFlowAccommodationViewData filterPanelFlowAccommodationViewData =
               (FilterPanelFlowAccommodationViewData) source;
            populateFilterPanelAccomodationFlow(analyticMap, filterPanelFlowAccommodationViewData);
         }
      }

   }

   private void populateHolidayType(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData requestData)
   {
      if (StringUtils.isNotEmpty(requestData.getHolidayType()))
      {
         analyticMap.put(HOLIDAY_TYPE,
            new WebAnalytics(HOLIDAY_TYPE, String.valueOf(requestData.getHolidayType())));
      }
      else
      {
         analyticMap.put(HOLIDAY_TYPE, new WebAnalytics(HOLIDAY_TYPE, NULL));
      }
   }

   /**
    * Population of the total number of passengers in the package selected (exc. Infants)
    *
    * @param analyticMap
    * @param searchResultRequestData
    */
   private void populatePaxData(final Map<String, WebAnalytics> analyticMap,
      final SearchResultsRequestData searchResultRequestData)
   {
      // No of passengers excluding infants->NoOfAdults+NoOfSeniors+(NoOfChildren-NoOfInfants)
      final int noOfPassenger =
         searchResultRequestData.getNoOfAdults() + searchResultRequestData.getNoOfSeniors()
            + searchResultRequestData.getChildCount();
      analyticMap.put(PAX, new WebAnalytics(PAX, Integer.toString(noOfPassenger)));

   }

   /**
    * Duration population for Analytics
    *
    * @param analyticMap
    * @param packageData
    */
   private void populateBoard(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {

      if (packageData.getAlternateBoard() != null)
      {
         final List<BoardBasisType> boardBasisData = packageData.getAlternateBoard();
         for (final BoardBasisType boardBasisType : boardBasisData)
         {
            if (boardBasisType.getBoardbasisCode() != null && boardBasisType.isDefaultBoardBasis())
            {
               final WebAnalytics webAnalyticDur =
                  new WebAnalytics(BOARD, String.valueOf(boardBasisType.getBoardbasisCode()));
               analyticMap.put(BOARD, webAnalyticDur);
            }

         }
      }

   }

   /**
    * GreatDeal population for Analytics
    *
    * @param analyticMap
    * @param packageData
    */
   private void populateGreatDeal(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {
      String isgreatDeal = null;
      if (packageData.getGreatDealPercentage() > 0)
      {
         isgreatDeal = "Yes";
         final WebAnalytics webAnalyticDur =
            new WebAnalytics(GREAT_DEAL, String.valueOf(isgreatDeal));
         final WebAnalytics webAnalyticDurPer =
            new WebAnalytics(GREAT_DEAL_PER, String.valueOf(packageData.getGreatDealPercentage())
               .toString());
         analyticMap.put(GREAT_DEAL, webAnalyticDur);
         analyticMap.put(GREAT_DEAL_PER, webAnalyticDurPer);
      }
      else
      {
         isgreatDeal = "No";
         final WebAnalytics webAnalyticDur =
            new WebAnalytics(GREAT_DEAL, String.valueOf(isgreatDeal));
         final WebAnalytics webAnalyticDurNoPer = new WebAnalytics(GREAT_DEAL_PER, "null");
         analyticMap.put(GREAT_DEAL, webAnalyticDur);
         analyticMap.put(GREAT_DEAL_PER, webAnalyticDurNoPer);
      }

   }

   /**
    * @param analyticMap
    * @param subProductCode
    */
   private void populateSubProductCode(final Map<String, WebAnalytics> analyticMap,
      final String subProductCode)
   {
      analyticMap.put("SubProd", new WebAnalytics("SubProd", subProductCode));
   }

   /**
    * Population of Final Position of the Package
    *
    * @param analyticMap
    * @param bookFlowAccommodationViewData
    */
   private void populateFinalPosition(final Map<String, WebAnalytics> analyticMap,
      final BookFlowAccommodationViewData bookFlowAccommodationViewData)
   {

      String finPos = StringUtils.EMPTY;
      if (!bookFlowAccommodationViewData.getSearchRequestData().isSingleAccomSearch())
      {
         finPos = "DL";
         if (StringUtils.isNotEmpty(bookFlowAccommodationViewData.getFinPos()))
         {
            finPos = String.valueOf(bookFlowAccommodationViewData.getFinPos());
         }
         analyticMap.put(FINPOS, new WebAnalytics(FINPOS, finPos));
      }
      else
      {
         if (StringUtils.isNotEmpty(bookFlowAccommodationViewData.getFinPos()))
         {
            finPos = String.valueOf(bookFlowAccommodationViewData.getFinPos());
         }
         analyticMap.put(FINPOS, new WebAnalytics(FINPOS, finPos));
      }

   }

   /**
    * Population of Original Position of the Package
    *
    * @param analyticMap
    * @param bookFlowAccommodationViewData
    */
   private void populateOriginalPosition(final Map<String, WebAnalytics> analyticMap,
      final BookFlowAccommodationViewData bookFlowAccommodationViewData)
   {
      String origPos = StringUtils.EMPTY;
      if (!bookFlowAccommodationViewData.getSearchRequestData().isSingleAccomSearch())
      {
         origPos = "DL";

         if (StringUtils.isNotEmpty(bookFlowAccommodationViewData.getOrigPos()))
         {
            origPos = String.valueOf(bookFlowAccommodationViewData.getOrigPos());
         }
         analyticMap.put(ORINGPOS, new WebAnalytics(ORINGPOS, origPos));
      }
      else
      {
         if (StringUtils.isNotEmpty(bookFlowAccommodationViewData.getOrigPos()))
         {
            origPos = String.valueOf(bookFlowAccommodationViewData.getOrigPos());
         }
         analyticMap.put(ORINGPOS, new WebAnalytics(ORINGPOS, origPos));
      }

   }

   /**
    * Population of Number of Search Results
    *
    * @param analyticMap
    * @param bookFlowAccommodationViewData
    */
   private void populateSearchResultsCount(final Map<String, WebAnalytics> analyticMap,
      final BookFlowAccommodationViewData bookFlowAccommodationViewData)
   {
      analyticMap.put(
         "Results",
         new WebAnalytics("Results", String.valueOf(bookFlowAccommodationViewData
            .getEndecaSearchResultCount())));
   }

   /**
    * LimitedAvailabilityIndicator for Analytics When the scenario arises where someone has more
    * than 1 room on their booking only the 1st room to populate the LimAv parameter
    *
    * @param analyticMap
    * @param packageData
    */
   private void populateLimitedAvailabilityIndicator(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {
      String limAv = NULL;

      if (CollectionUtils.isNotEmpty(packageData.getAccommodation().getRooms()))
      {
         final SearchResultRoomsData searchResultRoomsData =
            packageData.getAccommodation().getRooms().get(0);
         if (searchResultRoomsData.isAvailability())
         {
            limAv = String.valueOf(searchResultRoomsData.getSellingout());
         }

      }
      analyticMap.put("LimAvB", new WebAnalytics("LimAvB", limAv));
   }

   /**
    * Duration population for Analytics
    *
    * @param analyticMap
    * @param packageData
    */
   private void populateDuration(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {
      if (packageData.getDuration() > 0)
      {
         analyticMap.put(DUR, new WebAnalytics(DUR, String.valueOf(packageData.getDuration())));

      }
      else
      {
         analyticMap.put(DUR, new WebAnalytics(DUR, NULL));

      }
   }

   /**
    * Date population for AccommodationAnalytics
    *
    * @param analyticMap
    * @param packageData
    */
   public void populateDepartureDate(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {

      if (StringUtils.isNotEmpty(packageData.getItinerary().getDepartureDate()))
      {
         final LocalDate departureDate =
            DateUtils.toDate(packageData.getItinerary().getDepartureDate());
         analyticsHelper.addFormattedDate(analyticMap, departureDate);
         analyticsHelper.addMonthYear(analyticMap, departureDate);
      }
      else
      {
         analyticMap.put("MonthYear", new WebAnalytics("MonthYear", ""));
         analyticMap.put("DepDate", new WebAnalytics("DepDate", ""));

      }
   }

   public void populateTuiAncAnalyticData(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {
      if (packageData.isWorldCare())
      {
         int count = 0;
         double worldcarefund = 0;
         for (final SearchResultRoomsData room : packageData.getAccommodation().getRooms())
         {
            count += room.getOccupancy().getAdults() + room.getOccupancy().getChildren();
            final WorldCareFundRuleOutputData worldCare =
               droolsPriorityProviderService.getWorldCareFundCharges();

            worldcarefund +=
               (room.getOccupancy().getAdults() * Double.parseDouble(worldCare.getAdultCharge()))
                  + (room.getOccupancy().getChildren() * Double.parseDouble(worldCare
                     .getChildCharge()));
         }

         String worldCareAnalyticsData = StringUtils.EMPTY;
         if (packageData.isCoachTransfer())
         {
            worldCareAnalyticsData = "TFJ|" + count + "|" + 0 + "-";
         }
         worldCareAnalyticsData =
            worldCareAnalyticsData.concat("WCF|" + count + "|" + Math.round(worldcarefund));
         analyticMap.put(TUI_ANC_FILTER, new WebAnalytics(TUI_ANC_FILTER, worldCareAnalyticsData));

      }
      else
      {
         analyticMap.put(TUI_ANC_FILTER, new WebAnalytics(TUI_ANC_FILTER, NULL));

      }
   }

   /**
    * Airport Data Population for for Accommodation Page Analytics
    *
    * @param analyticMap
    * @param packageData
    */
   public void populateAirport(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {
      String airport = "";

      if (CollectionUtils.isNotEmpty(packageData.getItinerary().getOutbounds()))
      {
         airport = (packageData.getItinerary().getOutbounds().get(0)).getDepartureAirportCode();
      }
      final WebAnalytics webAnalyticDepAir = new WebAnalytics("DepAir", airport);
      analyticMap.put("DepAir", webAnalyticDepAir);
   }

   /**
    * WhereTo population for Accommodation Page Analytics
    *
    * @param analyticMap
    * @param packageData
    */
   public void populateWhereTo(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {
      String accomCode = "";
      if (StringUtils.isNotEmpty(packageData.getAccommodation().getCode()))
      {
         accomCode = packageData.getAccommodation().getCode();
      }
      final WebAnalytics webAnalyticDepAir = new WebAnalytics("WhereTo", "H" + accomCode);
      analyticMap.put("WhereTo", webAnalyticDepAir);
   }

   /**
    * Population of Price Data for Analytics
    *
    * @param analyticMap
    * @param resultViewData
    */
   public void populateprice(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData resultViewData)
   {

      int sum = 0;
      int discount = 0;

      final SearchResultPriceViewData searchResultPriceViewData = resultViewData.getPrice();
      if (StringUtils.isNotEmpty(searchResultPriceViewData.getTotalParty()))
      {
         sum = analyticsHelper.getPriceRoundedValue(searchResultPriceViewData.getTotalParty());
      }
      if (StringUtils.isNotEmpty(searchResultPriceViewData.getDiscount()))
      {
         discount = analyticsHelper.getPriceRoundedValue(searchResultPriceViewData.getDiscount());
      }
      analyticMap.put("Sum", new WebAnalytics("Sum", String.valueOf(sum)));
      analyticMap.put("Disc", new WebAnalytics("Disc", String.valueOf(discount)));
   }

   /**
    * Function to Populate Dremaliner data for Analytics
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

      analyticMap.put("DreamFl", new WebAnalytics("DreamFl", dreamlinerIndicator));

   }

   /**
    * Party Composition for rooms population for Analytics
    *
    * @param analyticMap
    * @param packageData Party=R1-A2/C0/I0,R2-A2/C1/I0
    */
   private void populatePartyComposition(final Map<String, WebAnalytics> analyticMap,
      final SearchResultViewData packageData)
   {

      int count = 0;
      String party = "";
      final StringBuilder partyComp = new StringBuilder();
      for (final SearchResultRoomsData room : packageData.getAccommodation().getRooms())
      {
         count++;
         partyComp.append("R");
         partyComp.append(count);
         partyComp.append("-");
         partyComp.append("A");
         partyComp.append(room.getOccupancy().getAdults());
         partyComp.append("/");
         partyComp.append("C");
         partyComp.append(room.getOccupancy().getChildren());
         partyComp.append("/");
         partyComp.append("I");
         partyComp.append(room.getOccupancy().getInfant());
         partyComp.append(",");
      }
      if (StringUtils.isNotEmpty(partyComp.toString()))
      {
         party = partyComp.toString();
         party = StringUtils.substring(party, 0, party.length() - 1);
      }
      final WebAnalytics webAnalyticDur = new WebAnalytics("Party", party);
      analyticMap.put("Party", webAnalyticDur);

   }

   /**
    * Setting the values to analytics
    *
    * @param analyticMap
    * @param filterPanelFlowAccommodationViewData
    */
   @SuppressWarnings("boxing")
   private void populateFilterPanelAccomodationFlow(final Map<String, WebAnalytics> analyticMap,
      final FilterPanelFlowAccommodationViewData filterPanelFlowAccommodationViewData)
   {

      if (StringUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getResultCount()))
      {
         analyticMap.put(FILTER_RESULT, new WebAnalytics(FILTER_RESULT,
            filterPanelFlowAccommodationViewData.getResultCount()));
      }

      if (StringUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getMaximumBudget()))
      {

         analyticMap.put(
            BUDGET_F,
            new WebAnalytics(BUDGET_F, String.valueOf(Math.round(Double
               .valueOf(filterPanelFlowAccommodationViewData.getMaximumBudget())))));
      }
      else
      {
         analyticMap.put(BUDGET_F, new WebAnalytics(BUDGET_F, NULL));
      }

      if (StringUtils.isNotEmpty(filterPanelFlowAccommodationViewData
         .getTourOperatorProductRating()))
      {
         analyticMap.put(
            RATING_F,
            new WebAnalytics(RATING_F, filterPanelFlowAccommodationViewData
               .getTourOperatorProductRating()));
      }
      else
      {
         analyticMap.put(RATING_F, new WebAnalytics(RATING_F, NULL));
      }

      if (StringUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getTripAdvisorRating()))
      {
         analyticMap.put(TRIPADVISOR_F, new WebAnalytics(TRIPADVISOR_F,
            filterPanelFlowAccommodationViewData.getTripAdvisorRating()));
      }
      else
      {
         analyticMap.put(TRIPADVISOR_F, new WebAnalytics(TRIPADVISOR_F, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getHolidayType()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getHolidayType()))
      {
         analyticMap.put(HOLIDAY_TYPE_F, new WebAnalytics(HOLIDAY_TYPE_F,
            populateStringFromList(filterPanelFlowAccommodationViewData.getHolidayType())));
      }
      else
      {
         analyticMap.put(HOLIDAY_TYPE_F, new WebAnalytics(HOLIDAY_TYPE_F, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getBestFor()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getBestFor()))
      {
         analyticMap.put(BESTFOR_F, new WebAnalytics(BESTFOR_F,
            populateStringFromList(filterPanelFlowAccommodationViewData.getBestFor())));
      }
      else
      {
         analyticMap.put(BESTFOR_F, new WebAnalytics(BESTFOR_F, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getBoard()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getBoard()))
      {
         analyticMap.put(BOARD_FILTER, new WebAnalytics(BOARD_FILTER,
            populateStringFromList(filterPanelFlowAccommodationViewData.getBoard())));
      }
      else
      {
         analyticMap.put(BOARD_FILTER, new WebAnalytics(BOARD_FILTER, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getTourOperator()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getTourOperator()))
      {
         analyticMap.put(TOUR_OPERATOR_FILTER, new WebAnalytics(TOUR_OPERATOR_FILTER,
            populateStringFromList(filterPanelFlowAccommodationViewData.getTourOperator())));
      }
      else
      {
         analyticMap.put(TOUR_OPERATOR_FILTER, new WebAnalytics(TOUR_OPERATOR_FILTER, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getAccommodationType()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getAccommodationType()))
      {
         analyticMap.put(ACCOMMODATION_TYPE_FILTER, new WebAnalytics(ACCOMMODATION_TYPE_FILTER,
            populateStringFromList(filterPanelFlowAccommodationViewData.getAccommodationType())));
      }
      else
      {
         analyticMap.put(ACCOMMODATION_TYPE_FILTER, new WebAnalytics(ACCOMMODATION_TYPE_FILTER,
            NULL));
      }
      compareFilterPanelParameters(analyticMap, filterPanelFlowAccommodationViewData);
   }

   /**
    * @param analyticMap
    * @param filterPanelFlowAccommodationViewData
    */
   private void compareFilterPanelParameters(final Map<String, WebAnalytics> analyticMap,
      final FilterPanelFlowAccommodationViewData filterPanelFlowAccommodationViewData)
   {
      if (null != filterPanelFlowAccommodationViewData.getDepartureAirports()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getDepartureAirports()))
      {
         analyticMap.put(DEPARTURE_AIRPORT_F, new WebAnalytics(DEPARTURE_AIRPORT_F,
            populateStringFromList(filterPanelFlowAccommodationViewData.getDepartureAirports())));
      }
      else
      {
         analyticMap.put(DEPARTURE_AIRPORT_F, new WebAnalytics(DEPARTURE_AIRPORT_F, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getDepartureTimeGoingOut()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData
            .getDepartureTimeGoingOut()))
      {
         analyticMap.put(
            DEPARTURE_TIME_GOINGOUT_F,
            new WebAnalytics(DEPARTURE_TIME_GOINGOUT_F,
               populateStringFromList(filterPanelFlowAccommodationViewData
                  .getDepartureTimeGoingOut())));
      }
      else
      {
         analyticMap.put(DEPARTURE_TIME_GOINGOUT_F, new WebAnalytics(DEPARTURE_TIME_GOINGOUT_F,
            NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getDepartureTimeReturn()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData
            .getDepartureTimeReturn()))
      {
         analyticMap.put(DEPARTURE_TIME_REUTRN_F, new WebAnalytics(DEPARTURE_TIME_REUTRN_F,
            populateStringFromList(filterPanelFlowAccommodationViewData.getDepartureTimeReturn())));
      }
      else
      {
         analyticMap.put(DEPARTURE_TIME_REUTRN_F, new WebAnalytics(DEPARTURE_TIME_REUTRN_F, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getAccommodationFeatures()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData
            .getAccommodationFeatures()))
      {
         analyticMap.put(
            ACCOMODATION_FEATURE_F,
            new WebAnalytics(ACCOMODATION_FEATURE_F,
               populateStringFromList(filterPanelFlowAccommodationViewData
                  .getAccommodationFeatures())));
      }
      else
      {
         analyticMap.put(ACCOMODATION_FEATURE_F, new WebAnalytics(ACCOMODATION_FEATURE_F, NULL));
      }

      anlyticMapForDestinationOptions(analyticMap, filterPanelFlowAccommodationViewData);
      if (StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(), BrandType.TH.getCode()))
      {
         analyticMapForNewFilterPanelComponents(analyticMap, filterPanelFlowAccommodationViewData);
      }
      analyticMapForCollection(analyticMap, filterPanelFlowAccommodationViewData);
   }

   private void analyticMapForCollection(final Map<String, WebAnalytics> analyticMap,
      final FilterPanelFlowAccommodationViewData filterPanelFlowAccommodationViewData)
   {

      if (null != filterPanelFlowAccommodationViewData.getCollections()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getCollections()))
      {
         analyticMap.put(HOLIDAY_TYPE, new WebAnalytics(HOLIDAY_TYPE,
            populateStringFromList(filterPanelFlowAccommodationViewData.getCollections())));
      }
      else
      {
         analyticMap.put(HOLIDAY_TYPE, new WebAnalytics(HOLIDAY_TYPE, NULL));
      }

   }

   /**
    * @param analyticMap
    * @param filterPanelFlowAccommodationViewData
    */
   private void anlyticMapForDestinationOptions(final Map<String, WebAnalytics> analyticMap,
      final FilterPanelFlowAccommodationViewData filterPanelFlowAccommodationViewData)
   {
      if (null != filterPanelFlowAccommodationViewData.getDestinationOptionsGeo()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData
            .getDestinationOptionsGeo()))
      {
         analyticMap.put(
            DESTINATIONS_OPTIONS_GEO_F,
            new WebAnalytics(DESTINATIONS_OPTIONS_GEO_F,
               populateStringFromList(filterPanelFlowAccommodationViewData
                  .getDestinationOptionsGeo())));
      }
      else
      {
         analyticMap.put(DESTINATIONS_OPTIONS_GEO_F, new WebAnalytics(DESTINATIONS_OPTIONS_GEO_F,
            NULL));
      }

      analyticMap.put(DESTINATIONS_OPTIONS_PRODUCT_F, new WebAnalytics(
         DESTINATIONS_OPTIONS_PRODUCT_F, NULL));
   }

   // Adding the Lated Analytic filter for specific to the Thomson Start
   private void analyticMapForNewFilterPanelComponents(final Map<String, WebAnalytics> analyticMap,
      final FilterPanelFlowAccommodationViewData filterPanelFlowAccommodationViewData)
   {
      if (null != filterPanelFlowAccommodationViewData.getTourOperator()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getTourOperator()))
      {
         analyticMap.put(TOUR_OPERATOR_FILTER, new WebAnalytics(TOUR_OPERATOR_FILTER,
            populateStringFromList(filterPanelFlowAccommodationViewData.getTourOperator())));
      }
      else
      {
         analyticMap.put(TOUR_OPERATOR_FILTER, new WebAnalytics(TOUR_OPERATOR_FILTER, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getDreamLinerFlights()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getDreamLinerFlights()))
      {
         analyticMap.put(DREAM_LINE_FILTER, new WebAnalytics(DREAM_LINE_FILTER,
            populateStringFromList(filterPanelFlowAccommodationViewData.getDreamLinerFlights())));
      }
      else
      {
         analyticMap.put(DREAM_LINE_FILTER, new WebAnalytics(DREAM_LINE_FILTER, NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getAccommodationType()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getAccommodationType()))
      {
         analyticMap.put(ACCOMMODATION_TYPE_FILTER, new WebAnalytics(ACCOMMODATION_TYPE_FILTER,
            populateStringFromList(filterPanelFlowAccommodationViewData.getAccommodationType())));
      }
      else
      {
         analyticMap.put(ACCOMMODATION_TYPE_FILTER, new WebAnalytics(ACCOMMODATION_TYPE_FILTER,
            NULL));
      }

      if (null != filterPanelFlowAccommodationViewData.getBoard()
         && CollectionUtils.isNotEmpty(filterPanelFlowAccommodationViewData.getBoard()))
      {
         analyticMap.put(BOARD_FILTER, new WebAnalytics(BOARD_FILTER,
            populateStringFromList(filterPanelFlowAccommodationViewData.getBoard())));
      }
      else
      {
         analyticMap.put(BOARD_FILTER, new WebAnalytics(BOARD_FILTER, NULL));
      }

   }

   // Adding the Lated Analytic filter for specific to the Thomson End
   private String populateStringFromList(final List<String> sourceList)
   {
      final StringBuilder buf = new StringBuilder();
      for (final String s : sourceList)
      {
         buf.append(s).append("|");
      }
      final String convertedStr = buf.toString();

      return convertedStr.substring(0, convertedStr.length() - 1);
   }

   /**
    * @param analyticMap
    * @param bookFlowAccommodationViewData This Method will populate the analytics param required
    *           for shortlisted holiday
    */
   private void populatePriceChnageForAnalytics(final Map<String, WebAnalytics> analyticMap,
      final BookFlowAccommodationViewData bookFlowAccommodationViewData)
   {
      if (bookFlowAccommodationViewData.getPriceChange() != null)
      {
         if (StringUtils.equals(bookFlowAccommodationViewData.getPriceChange(), "N"))
         {
            analyticMap.put(SLIST, new WebAnalytics(SLIST, NO));
            analyticMap.put(S_LIST_CHANGE, new WebAnalytics(S_LIST_CHANGE, NULL));

         }
         else
         {
            populatePriceAnalytics(analyticMap, bookFlowAccommodationViewData);
         }
      }

   }

   /**
    * @param analyticMap
    * @param bookFlowAccommodationViewData This Method will populate the analytics param required
    *           for shortlisted holiday
    */
   private void populatePriceAnalytics(final Map<String, WebAnalytics> analyticMap,
      final BookFlowAccommodationViewData bookFlowAccommodationViewData)
   {
      analyticMap.put(SLIST, new WebAnalytics(SLIST, YES));
      final String price = bookFlowAccommodationViewData.getPriceChange();
      final String[] priceData = StringUtils.split(price, COLON);
      if (priceData.length > 0)
      {
         if (StringUtils.equals(priceData[0], MINUS))
         {
            analyticMap.put(S_LIST_CHANGE, new WebAnalytics(S_LIST_CHANGE, "-" + priceData[1]));
         }
         else if (StringUtils.equals(priceData[0], PLUS))

         {
            analyticMap.put(S_LIST_CHANGE, new WebAnalytics(S_LIST_CHANGE, priceData[1]));

         }
      }
   }
}
