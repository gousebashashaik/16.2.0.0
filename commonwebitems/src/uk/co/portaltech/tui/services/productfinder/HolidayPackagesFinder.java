package uk.co.portaltech.tui.services.productfinder;

/*
 *
 *
 *
 *
 *
 */

import static uk.co.portaltech.commons.Collections.toList;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.HowLongDurationModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.travel.thirdparty.endeca.Facet;
import uk.co.portaltech.travel.thirdparty.endeca.HolidayPackageItem;
import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.travel.thirdparty.endeca.RoomAllocation;
import uk.co.portaltech.travel.thirdparty.endeca.enums.SortParameter;
import uk.co.portaltech.travel.thirdparty.endeca.services.HolidayService;
import uk.co.portaltech.tui.services.DurationHowLongService;
import uk.co.portaltech.tui.services.RuleEngineFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.FilterRequest;
import uk.co.portaltech.tui.web.view.data.MainFilterRequest;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SliderRequest;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.EndecaSearchException;
import uk.co.tui.exception.TimebasedCrossFeatRuleException;
import uk.co.tui.feeds.excursion.convertor.FeedsConverterUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;
import uk.co.tui.web.common.enums.CarouselLookupType;

public final class HolidayPackagesFinder implements ProductFinder
{
   /**
    * Endeca sort parameter.
    */
   private static final String SORT_PARAMETER = "Ns";

   /**
    * Endeca offset parameter. Record no. Used instead of page no.
    */
   private static final String OFFSET_PARAMETER = "No";

   /**
    * Endeca records per page parameter also called offset.
    */
   private static final String RECORDS_PER_PAGE_PARAMETER = "Nrpp";

   private static final String DEFAULT = "default";

   /**
     *
     */
   private static final String DEFAULTPRIORITY = "7,10,11,14";

   /**
     *
     */

   /**
     *
     */
   private static final String THOUSANDTEN = "1010";

   /**
     *
     */
   private static final String THOUSANDELEVEN = "1011";

   private static final String UK_COUNTRY = "GBR";

   private static final String IRL_COUNTRY = "IRL";

   private static final String FILTER = "Filter";

   private static final String DATESLIDER = "dateslider";

   private static final String DURATION = "duration";

   private static final String ROOMS = "rooms";

   private static final String DATESONLY = "datesonly";

   private static final String SMERCH = "smerch";

   private static final TUILogUtils LOG = new TUILogUtils("HolidayPackagesFinder");

   @Resource
   private HolidayService holidaysearchService;

   @Resource
   private RuleEngineFacade ruleEngineFacade;

   @Resource
   private AirportService airportsService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private DurationHowLongService durationHowLongService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private ConfigurationService configurationService;

   // added for test case execution
   public HolidayPackagesFinder()
   {
      // added for test case execution
   }

   public HolidayPackagesFinder(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;

   }

   /**
    * @param request SearchRequestData
    * @throws EndecaSearchException
    * @throws EndecaSearchException
    */
   @Override
   public SearchResultData<? extends Object> search(final SearchRequestData request)
      throws EndecaSearchException
   {
      final HolidaySearchContext context = new HolidaySearchContext();
      SearchResultsRequestData requestData = null;
      if (null != request)
      {
         if (request instanceof SearchResultsRequestData)
         {
            requestData = (SearchResultsRequestData) request;
         }
         else
         {
            LOG.error("Unexpected Type cannot cast to SearchResultsRequestData");
         }

         try
         {
            processData(context, requestData);
         }
         catch (final TimebasedCrossFeatRuleException e)
         {
            throw new EndecaSearchException(THOUSANDELEVEN, e);
         }
         catch (final ParseException e)
         {
            throw new EndecaSearchException(THOUSANDTEN, e);
         }
      }
      return updateEndecaResponse(context, requestData);
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.
    * view.data. SearchResultsRequestData)
    * 
    * FOR Recommendations Engine - by ganga
    */
   @Override
   public SearchResultData<? extends Object> searchRecPack(
      final SearchResultsRequestData requestData)
   {
      final HolidaySearchContext context = new HolidaySearchContext();

      try
      {
         processData(context, requestData);
      }
      catch (final TimebasedCrossFeatRuleException e)
      {
         throw new EndecaSearchException(THOUSANDELEVEN, e);
      }
      catch (final ParseException e)
      {
         throw new EndecaSearchException(THOUSANDTEN, e);
      }

      return updateEndecaResponse(context, requestData);
   }

   /**
    * @param context
    * @param requestData
    * @throws EndecaSearchException
    */
   private SearchResultData<HolidayPackageItem> updateEndecaResponse(
      final HolidaySearchContext context, final SearchResultsRequestData requestData)
      throws EndecaSearchException
   {
      final SearchResultData<HolidayPackageItem> result =
         new SearchResultData<HolidayPackageItem>();

      if (viewSelector.checkIsMobile())
      {
         context.setMobile(true);
      }

      if (isFollowOnSearch(context))
      {
         final EndecaSearchResult endecaSearchResult =
            holidaysearchService.getPackagesSearchDataForFacets(context);

         if (endecaSearchResult != null && requestData != null && requestData.getDuration() <= 0)
         {
            endecaSearchResult.setPrioritizedDuration(context.getDuration());
         }

         result.setSearchResult(endecaSearchResult);

      }
      else
      {

         final EndecaSearchResult endecaSearchResult =
            holidaysearchService.getPackagesSearchData(context);

         if (endecaSearchResult != null && requestData != null && requestData.getDuration() <= 0)
         {
            endecaSearchResult.setPrioritizedDuration(context.getDuration());
         }
         result.setSearchResult(endecaSearchResult);
      }
      if (viewSelector.checkIsMobile())
      {
         result.getSearchResult().setDuration(context.getDuration());
      }

      return result;
   }

   /**
    * @param context
    *
    * @return boolean
    */
   private boolean isFollowOnSearch(final HolidaySearchContext context)
   {

      final boolean durationOrRoomSearch =
         StringUtils.equalsIgnoreCase(context.getSearchType(), DURATION)
            || StringUtils.equalsIgnoreCase(context.getSearchType(), ROOMS);

      return StringUtils.equalsIgnoreCase(context.getSearchType(), FILTER)
         || StringUtils.equalsIgnoreCase(context.getSearchType(), DATESLIDER)
         || StringUtils.equalsIgnoreCase(context.getSearchType(), DATESONLY)
         || durationOrRoomSearch;

   }

   /**
    * @param context HolidaySearchContext
    * @param requestData SearchResultsRequestData
    * @throws ParseException
    * @throws TimebasedCrossFeatRuleException
    * @throws ParseException
    *
    */
   @SuppressWarnings("unused")
   public void processData(final HolidaySearchContext context,
      final SearchResultsRequestData requestData) throws ParseException,
      TimebasedCrossFeatRuleException
   {
      final Set<String> airports = new HashSet<String>();
      final List<String> destinations = new ArrayList<String>();
      // added for n value calculation of product range --Destination filter
      final Map<String, String> productRange = new HashMap<String, String>();
      boolean isProductRange = false;

      /* If user has not selected any Airports consider all GBR airports */

      if (requestData.getAirportList() != null && !requestData.getAirportList().isEmpty())
      {
         context.setAirports(requestData.getAirportList());
      }
      else if (requestData.getAirports().isEmpty())
      {
         // fetch all UK airports
         context.setAirports(getAllairports());
      }
      else
      {

         for (final AirportData airport : requestData.getAirports())
         {
            if (CollectionUtils.isNotEmpty(airport.getChildren()))
            {
               addChildren(airports, airport.getChildren());
            }
            else
            {
               airports.add(airport.getId());
            }
         }
         final List<String> airportList = toList(airports);
         context.setAirports(airportList);
      }

      uk.co.portaltech.travel.thirdparty.endeca.FilterRequest destinationContext = null;

      if (requestData.getRecomData() != null && !requestData.getRecomData().isEmpty())
      {
         final Map<String, String> map = requestData.getRecomData();

         for (final Map.Entry<String, String> entry : map.entrySet())
         {
            destinationContext = new uk.co.portaltech.travel.thirdparty.endeca.FilterRequest();

            final String destinationCode = entry.getKey();
            final String destinationName = entry.getValue();
            destinations.add(destinationName);

            destinationContext.setId(destinationCode);
            destinationContext.setName(destinationName);

            productRange.put(destinationCode, destinationName);

            context.getSelectedDestinations().add(destinationContext);

         }
      }
      else
      {
         for (final UnitData destination : requestData.getUnits())
         {
            destinations.add(destination.getName());

            destinationContext = new uk.co.portaltech.travel.thirdparty.endeca.FilterRequest();
            destinationContext.setId(destination.getId());
            destinationContext.setName(destination.getName());
            destinationContext.setFilterType(destination.getType());

            // added for product range -- for n value calculation of destination option
            if (StringUtils.equalsIgnoreCase(destination.getType(), "productrange"))
            {
               isProductRange = true;
               productRange.put(destination.getId(), destination.getName());
            }

            context.getSelectedDestinations().add(destinationContext);

         }
      }

      context.setLocations(destinations);
      if (!requestData.isFlexibility())
      {
         context.setEarlierDepartureDate(requestData.getWhen());
         context.setLatestDepartureDate(requestData.getUntil());
         context.setWhen(requestData.getWhen());
         context.setUntil(requestData.getUntil());
      }
      else
      {
         if (requestData.isAccomDetails() && StringUtils.isNotBlank(requestData.getPdd()))
         {
            context.setAccomDetails(requestData.isAccomDetails());
            context.setPdd(requestData.getPdd());
         }
         populateAtcomSwitchDate(context, requestData);
         context.setEarlierDepartureDate(context.getWhen());
         context.setLatestDepartureDate(context.getUntil());

      }
      context.setLatestDepartureDate(requestData.getUntil());
      context.setFlexible(requestData.isFlexibility());
      context.setSearchType(requestData.getSearchRequestType());
      if (StringUtils.isNotEmpty(requestData.getSearchType())
         && StringUtils.equalsIgnoreCase(requestData.getSearchType(), SMERCH)
         && StringUtils.isNotEmpty(requestData.getSmerchDuration()))
      {
         if (requestData.getSmerchDuration().contains(","))
         {
            final String[] durationrange = requestData.getSmerchDuration().split(",");
            final String duration = durationrange[0];
            context.setDuration(Integer.parseInt(duration));
            context.setDurationPriorityOrder(requestData.getSmerchDuration());
         }
         else
         {
            context.setDuration(Integer.parseInt(requestData.getSmerchDuration()));
            context.setDurationPriorityOrder(requestData.getSmerchDuration());
         }
      }

      else
      {
         setHowLongDuration(context, requestData, tuiUtilityService.getSiteBrand());
      }
      /* sets the duration in context object */
      if (viewSelector.checkIsMobile())
      {
         context.setMobile(true);
      }

      final Date startDate =
         FeedsConverterUtils.parseToDate(context.getWhen(), CommonwebitemsConstants.DD_MM_YYYY);
      final Date endDate =
         FeedsConverterUtils.parseToDate(context.getUntil(), CommonwebitemsConstants.DD_MM_YYYY);
      /* set the party composition */
      context.setNoOfAdults(requestData.getNoOfAdults());
      context.setInfantCount(requestData.getInfantCount());
      context.setNoOfSeniors(requestData.getNoOfSeniors());
      context.getChildrenAge().addAll(requestData.getChildAges());
      context.setChildCount(requestData.getChildCount());

      context.setSingleAccomSearch(requestData.isSingleAccomSearch());
      // Set filter infomation

      setFilters(context, requestData, productRange, isProductRange);
      // for cross featuring setting brandtype from endeca results for flight option page .
      // modified for inventory page
      if (StringUtils.isNotEmpty(requestData.getBrandType()))
      {
         if ("T".equalsIgnoreCase(requestData.getBrandType()))
         {

            context.setBrand("TH");

         }
         else if ("F".equalsIgnoreCase(requestData.getBrandType()))
         {

            context.setBrand("FC");
         }
         else if ("E".equalsIgnoreCase(requestData.getBrandType()))
         {

            context.setBrand("FJ");
            context.setCrossBrand("FJ");
         }
         else if ("D".equalsIgnoreCase(requestData.getBrandType()))
         {
            context.setBrand("FJ");
            context.setCrossBrand("TI");
         }
      }
      else
      {
         // setting brand details
         context.setBrand(tuiUtilityService.getSiteBrand());
      }

      context.setCrossFeaturingFlag(ruleEngineFacade.applyTimeBasedCrossFeaturingRules(startDate,
         endDate, tuiUtilityService.getSiteBrand()));
      context.setFlightOptions(requestData.isFlightOptions());
      context.setSelectedBoardBasis(requestData.getSelectedBoardBasis());

      // pagination and sorting applies only to normal results and not single accom
      if (!requestData.isSingleAccomSearch())
      {
         // set additional parameters for Sorting and pagination
         setAdditionalParams(context, requestData);
      }

   }

   private void setAdditionalParams(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      final Map<String, String> additionalParams = new HashMap<String, String>();
      setDefaultSortByAndOffset(requestData);
      // set off set
      additionalParams.put(RECORDS_PER_PAGE_PARAMETER, Integer.toString(requestData.getOffset()));
      // set record no.
      additionalParams.put(OFFSET_PARAMETER,
         Integer.toString(getEndecaRecordNo(requestData.getOffset(), requestData.getFirst())));

      if (StringUtils.isNotBlank(requestData.getSortBy()))
      {
         // set Endeca sort parameter from sort parameter given by UI
         additionalParams.put(SORT_PARAMETER, SortParameter.valueOf(requestData.getSortBy())
            .getSortParameter());
      }

      context.setAdditionalParams(additionalParams);
   }

   /**
    * @param requestData
    */
   private void setDefaultSortByAndOffset(final SearchResultsRequestData requestData)
   {
      if (StringUtils.isBlank(requestData.getSortBy())
         || StringUtils.equals(DEFAULT, requestData.getSortBy()))
      {
         requestData.setSortBy(StringUtils.EMPTY);
      }
      if (requestData.getFirst() == 0)
      {
         requestData.setFirst(1);
      }
   }

   private int getEndecaRecordNo(final int offset, final int first)
   {
      // finding the record no from page number
      return offset * (first - 1) + 1;
   }

   /**
    * Conditon for population
    *
    * if difference between departure date and switch date falls between flexible days the when and
    * until has to be restricted
    *
    * @param context
    *
    * @param searchParameter
    */
   public void populateAtcomSwitchDate(final HolidaySearchContext context,
      final SearchResultsRequestData searchParameter)
   {

      final LocalDate departureDate = DateUtils.toDate(searchParameter.getDepartureDate());
      final String siteBrand = tuiUtilityService.getSiteBrand();

      final LocalDate switchDate =
         DateUtils.toDate(configurationService.getConfiguration().getString(
            siteBrand + CommonwebitemsConstants.DOT
               + CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015"));

      if (isAfterAtcom(searchParameter, departureDate, switchDate))
      {
         context.setWhen(DateUtils.format(switchDate));
         context.setUntil(searchParameter.getUntil());
      }
      else if (isBeforeAtcom(searchParameter, departureDate, switchDate))
      {
         context.setWhen(searchParameter.getWhen());
         context.setUntil(DateUtils.subtractDays(switchDate, 1));

      }
      else
      {
         context.setWhen(searchParameter.getWhen());
         context.setUntil(searchParameter.getUntil());
      }

   }

   /**
    * @param searchParameter
    * @param departureDate
    * @param switchDate
    * @return
    */
   private boolean isBeforeAtcom(final SearchResultsRequestData searchParameter,
      final LocalDate departureDate, final LocalDate switchDate)
   {
      return (DateUtils.isBefore(departureDate, switchDate))
         && (DateUtils.subtractDates(switchDate, departureDate) <= searchParameter
            .getFlexibleDays());
   }

   /**
    * @param searchParameter
    * @param departureDate
    * @param switchDate
    * @return
    */
   private boolean isAfterAtcom(final SearchResultsRequestData searchParameter,
      final LocalDate departureDate, final LocalDate switchDate)
   {
      return (DateUtils.isEqualOrAfter(departureDate, switchDate))
         && (DateUtils.subtractDates(departureDate, switchDate) <= searchParameter
            .getFlexibleDays());
   }

   /**
    * This method sets the duration in context object. First it checks for user selected duration,
    * if it finds it will set this duration in the context object otherwise will call the
    * ruleEngineFacade to determine and set the duration and durationPriorityOrder.
    * DurationPriorityOrder is only required when user has not selected any duration.
    *
    * @param context the HolidaySearchContext object
    * @param requestData the SearchResultsRequestData object
    */
   private void setHowLongDuration(final HolidaySearchContext context,
      final SearchResultsRequestData requestData, final String brandType)
   {
      context.setDuration(requestData.getDuration());
      context.setDurationPriorityOrder(Integer.toString(requestData.getDuration()));

      if (((StringUtils.equalsIgnoreCase(requestData.getSearchRequestType(), "ins")) || (StringUtils
         .equalsIgnoreCase(requestData.getSearchRequestType(), "datesonly")))
         && (requestData.getDuration() > 0) && (!requestData.isFlightOptions()))
      {

         if (!requestData.isIscapeRequest())
         {

            final List<HowLongDurationModel> list =
               durationHowLongService.getDurationForCode(requestData.getDuration(), brandType);
            if (list != null && !list.isEmpty())
            {
               context.setDuration(Integer.parseInt(list.get(0).getStay()));

               context.setDurationPriorityOrder(getOrderbasedOnDefault(list.get(0)
                  .getDurationRange(), list.get(0).getStay()));

            }
         }
         else
         {
            context.setDurationPriorityOrder(DEFAULTPRIORITY);
         }

      }
   }

   /**
    * This method fetches all UK airports
    *
    * @return airports
    */
   private List<String> getAllairports()
   {
      final List<String> airports = new ArrayList<String>();
      List<AirportModel> allAirports = null;

      if (tuiUtilityService.getSiteBrand() != null
         && "FJ".equalsIgnoreCase(tuiUtilityService.getSiteBrand()))
      {
         allAirports =
            airportsService.getAirports(cmsSiteService.getCurrentCatalogVersion(), IRL_COUNTRY);
      }
      else
      {
         allAirports =
            airportsService.getAirports(cmsSiteService.getCurrentCatalogVersion(), UK_COUNTRY);
      }

      for (final AirportModel airport : allAirports)
      {
         airports.add(airport.getCode());
      }
      return airports;
   }

   private void addChildren(final Set airports, final Collection<String> airportIds)
   {
      for (final String airport : airportIds)
      {
         airports.add(airport);
      }
   }

   /**
    *
    * @param context
    * @param requestData
    */
   private void setFilters(final HolidaySearchContext context,
      final SearchResultsRequestData requestData, final Map productRange,
      final boolean isProductRange)
   {

      // Send the Room allocation details
      setRoomAllocation(context, requestData);

      if (requestData.getFilters() != null)
      {
         final MainFilterRequest filters = requestData.getFilters();
         // Send the budget details
         final SliderRequest budgettotal = filters.getBudgettotal();
         if (budgettotal != null
            && (budgettotal.isChanged() || (filters.getBudgetpp() != null && filters.getBudgetpp()
               .isChanged())))
         {
            final uk.co.portaltech.travel.thirdparty.endeca.SliderRequest budgetTotal =
               context.getBudget();

            budgetTotal.setId(budgettotal.getCode());
            budgetTotal.setMax(budgettotal.getMax());
            budgetTotal.setMin(budgettotal.getMin());
            budgetTotal.setName(budgettotal.getName());
            budgetTotal.setMaxValue(budgettotal.getMaxValue());
         }
         // modified for calling different request for per person budget
         if (context.isMobile() && (filters.getBudgetpp() != null)
            && (filters.getBudgetpp().isChanged()))
         {
            final uk.co.portaltech.travel.thirdparty.endeca.SliderRequest budgetPP =
               context.getBudget();

            budgetPP.setId(filters.getBudgetpp().getCode());
            budgetPP.setMax(filters.getBudgetpp().getMax());
            budgetPP.setMin(filters.getBudgetpp().getMin());
            budgetPP.setName(filters.getBudgetpp().getName());
            budgetPP.setMaxValue(filters.getBudgetpp().getMaxValue());
         }

         // Send the ratings
         setRatings(context, requestData);

         // Send the temperature datails
         final SliderRequest temperature = filters.getTemperature();
         if (temperature != null && temperature.isChanged())
         {
            final uk.co.portaltech.travel.thirdparty.endeca.SliderRequest temperatureReq =
               context.getTemperature();
            temperatureReq.setId(temperature.getCode());
            temperatureReq.setMax(temperature.getMax());
            temperatureReq.setMin(temperature.getMin());
            temperatureReq.setName(temperature.getName());
         }

         // Set departure points
         setDeparturePoins(context, requestData);
         // InBound timings
         setInbound(context, requestData);
         // OutBound timings
         setOutbound(context, requestData);
         // Pass the Best For values
         setBestFor(context, requestData);

         // Accomm Type Code --End
         // Pass the Holiday type values
         setHolidayType(context, requestData);

         // Set the featues
         setFeatures(context, requestData);
         // Set the destination options
         setDestinationOptions(context, requestData, productRange, isProductRange);

         // setting BoardBasis
         setBoardBasis(context, requestData);
         setHolidayOperator(context, requestData);

         // Setting Accommodation Type
         setAccommodationType(context, requestData);

         // Setting Collections Data
         setCollectionsType(context, requestData);
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setCollectionsType(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      Facet collectionsTypeFacet = null;
      final List<String> collectionsTypeValues = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getCollections()))
      {
         collectionsTypeFacet = new Facet();

         for (final FilterRequest collectionsType : requestData.getFilters().getCollections())
         {
            if (collectionsType.isSelected())
            {
               collectionsTypeValues.add(collectionsType.getValue());
            }
         }
         collectionsTypeFacet.setValues(collectionsTypeValues);
         context.setCollections(collectionsTypeFacet);
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setAccommodationType(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      Facet accommodationTypeFacet = null;
      final List<String> accommodationTypeValues = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getAccommodationType()))
      {
         accommodationTypeFacet = new Facet();

         for (final FilterRequest accommodationType : requestData.getFilters()
            .getAccommodationType())
         {
            if (accommodationType.isSelected())
            {
               accommodationTypeValues.add(accommodationType.getValue());
            }

         }
         accommodationTypeFacet.setValues(accommodationTypeValues);
         context.setAccommodationType(accommodationTypeFacet);
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setBoardBasis(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      Facet boardBasisFacet = null;
      final List<String> boardBasisValues = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getBoardBasis()))
      {
         boardBasisFacet = new Facet();

         for (final FilterRequest boardBasis : requestData.getFilters().getBoardBasis())
         {
            if (boardBasis.isSelected())
            {
               boardBasisValues.add(boardBasis.getValue());
            }
         }
         boardBasisFacet.setValues(boardBasisValues);
         context.setBoardBasis(boardBasisFacet);
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setHolidayOperator(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      Facet holidayOperatorFacet = null;
      final List<String> holidayOperatorValue = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getHolidayOperator()))
      {
         holidayOperatorFacet = new Facet();

         for (final FilterRequest holidayOperator : requestData.getFilters().getHolidayOperator())
         {
            if (holidayOperator.isSelected())
            {
               holidayOperatorValue.add(holidayOperator.getValue());
            }
         }
         holidayOperatorFacet.setValues(holidayOperatorValue);
         context.setHolidayOperator(holidayOperatorFacet);
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setRoomAllocation(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      RoomAllocation roomAllocation = null;
      if (CollectionUtils.isNotEmpty(requestData.getRooms()))
      {
         for (final uk.co.portaltech.tui.web.view.data.RoomAllocation room : requestData.getRooms())
         {
            roomAllocation = new RoomAllocation();
            roomAllocation.setRoomId(room.getId());
            roomAllocation.setNoOfAdults(room.getNoOfAdults());
            roomAllocation.setNoOfChildren(room.getNoOfChildren());
            roomAllocation.setNoOfSeniors(room.getNoOfSeniors());
            roomAllocation.setChildrenAge(room.getChildrenAge());
            roomAllocation.setInfantCount(room.getInfantCount());
            context.getRoomAllocation().add(roomAllocation);
         }

      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setRatings(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      // Send the Trip advisor rating
      final SliderRequest tARating = requestData.getFilters().getTripadvisorrating();
      if (tARating != null && tARating.isChanged())
      {
         context.gettARating().setId(tARating.getCode());
         context.gettARating().setMax(ceilValue(tARating.getMax()));
         context.gettARating().setMin(ceilValue(tARating.getMin()));
         context.gettARating().setName(tARating.getName());
      }
      // Send the First choice rating
      final SliderRequest fcRating = requestData.getFilters().getFcRating();
      if (fcRating != null && fcRating.isChanged())
      {
         context.getFcRating().setId(fcRating.getCode());

         if ("5".equals(fcRating.getMin()) && "6".equals(fcRating.getMax()))
         {
            context.getFcRating().setMax("6");
         }
         else
         {
            context.getFcRating().setMax(fcRating.getMax());
         }
         context.getFcRating().setMin(fcRating.getMin());
         context.getFcRating().setName(fcRating.getName());
      }
   }

   /**
    * @param value
    *
    * @return ceiled value.
    */
   @SuppressWarnings("boxing")
   private String ceilValue(final String value)
   {
      if (StringUtils.isNotEmpty(value))
      {
         return Integer.toString((Double.valueOf(Math.ceil(Float.parseFloat(value)))).intValue());
      }
      else
      {
         return StringUtils.EMPTY;
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setDeparturePoins(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      List<String> departurePoints = null;
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getDeparturePoints())
         && (StringUtils.equalsIgnoreCase(requestData.getSearchRequestType(), "Filter") || StringUtils
            .equalsIgnoreCase(requestData.getSearchRequestType(), "Dateslider")))
      {
         departurePoints = new ArrayList<String>();
         for (final FilterRequest departurePoint : requestData.getFilters().getDeparturePoints())
         {
            if (departurePoint.isSelected())
            {
               departurePoints.add(departurePoint.getValue());
            }
         }
         if (CollectionUtils.isNotEmpty(departurePoints))
         {
            context.setAirports(departurePoints);
         }
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setInbound(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getInslots()))
      {
         for (final FilterRequest inbound : requestData.getFilters().getInslots())
         {
            if (inbound.isSelected())
            {
               // indicator to differentiate slots is removed
               context.getInBound().add(inbound.getValue().substring(0, 1));
            }
         }

      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setOutbound(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getOutslots()))
      {
         for (final FilterRequest outbound : requestData.getFilters().getOutslots())
         {
            if (outbound.isSelected())
            {
               // indicator to differentiate slots is removed
               context.getOutBound().add(outbound.getValue().substring(0, 1));
            }
         }
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setDestinationOptions(final HolidaySearchContext context,
      final SearchResultsRequestData requestData, final Map productRange,
      final boolean isProductRange)
   {
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getDestinations()))
      {
         uk.co.portaltech.travel.thirdparty.endeca.FilterRequest destinationContext = null;
         for (final FilterRequest destination : requestData.getFilters().getDestinations())
         {
            if (destination.isSelected())
            {
               destinationContext = new uk.co.portaltech.travel.thirdparty.endeca.FilterRequest();
               if (destination.getParent() != null)
               {

                  destinationContext.setId(destination.getValue());
                  destinationContext.setName(getDestinationoptionName(destination.getValue(),
                     destination.getName(), productRange, isProductRange));
                  context.getDestinationOptions().add(destinationContext);
               }
            }
         }
      }
   }

   /**
    * This method is added for generation of dimension key, for fetching from endeca map incase of
    * destination option filter for collection ex-key for collection search in destionation option
    * filter should be ex -ThomsonGold|Cyprus_GLD|CYP where as normal flow it should be
    * ex-hotelbeachhouse_013696|ESP
    */

   private String getDestinationoptionName(final String value, final String name,
      final Map productRange, final boolean isProductRange)
   {

      if (isProductRange && StringUtils.isNotBlank(value))
      {
         final String productRangeCode = StringUtils.split(value, "|")[0];
         if (productRange.containsKey(productRangeCode))
         {
            return productRange.get(productRangeCode).toString() + "|" + name;
         }
      }

      return name;
   }

   /**
    * @param context
    * @param requestData
    */
   private void setBestFor(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      Facet bestFor = null;
      final List<String> bestforValues = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getBestfor()))
      {
         bestFor = new Facet();

         for (final FilterRequest best : requestData.getFilters().getBestfor())
         {
            if (best.isSelected())
            {
               bestforValues.add(best.getValue());
            }

         }
         bestFor.setValues(bestforValues);
         context.setBestFor(bestFor);
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setHolidayType(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      Facet holidayType = null;
      final List<String> holidayTypeValues = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(requestData.getFilters().getHolidayType()))
      {
         holidayType = new Facet();

         for (final FilterRequest holidaytype : requestData.getFilters().getHolidayType())
         {
            if (holidaytype.isSelected())
            {
               holidayTypeValues.add(holidaytype.getValue());
            }
         }
         holidayType.setValues(holidayTypeValues);
         context.setHolidayType(holidayType);
      }
   }

   /**
    * @param context
    * @param requestData
    */
   private void setFeatures(final HolidaySearchContext context,
      final SearchResultsRequestData requestData)
   {
      final Map<String, List<String>> featuremap = new HashMap<String, List<String>>();

      Facet featureSelected = null;
      final List<String> featureValues = new ArrayList<String>();

      if (CollectionUtils.isNotEmpty(requestData.getFilters().getFeatures()))
      {
         featureSelected = new Facet();
         for (final FilterRequest feature : requestData.getFilters().getFeatures())
         {
            if (feature.isSelected())
            {
               if (featuremap.containsKey(feature.getCategoryCode()))
               {
                  final List<String> entries = featuremap.get(feature.getCategoryCode());
                  entries.add(feature.getValue());
               }
               else
               {
                  final List<String> entries = new ArrayList<String>();
                  entries.add(feature.getValue());
                  if (feature.getCategoryCode() != null)
                  {
                     featuremap.put(feature.getCategoryCode(), entries);
                  }
               }

               // this is for features' N values need to pass the names
               featureValues.add(feature.getName());
            }

         }
         featureSelected.setValues(featureValues);
         context.setAccomFeatures(featureSelected);
      }
      context.setFeatures(featuremap);

   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#getAccomListFromEndeca(uk.co.portaltech
    * .tui.web.view .data.SearchRequestData)
    */
   @Override
   public SearchResultData<? extends Object> accomSearch(final SearchRequestData searchRequestData,
      final CarouselLookupType carouselLookupType)
   {

      return null;
   }

   private String getOrderbasedOnDefault(final String durationRange, final String stay)
   {
      String durationRangetemp = durationRange;
      final String[] durationorder = durationRange.split(",");
      if (!durationorder[0].equals(stay))
      {

         durationRangetemp = durationRange.replace("," + stay, "");
         durationRangetemp = stay + ',' + durationRangetemp;
      }
      return durationRangetemp;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#searchHolidays(uk.co.portaltech.
    * tui.web.view.data. SearchRequestData)
    */
   @Override
   public EndecaSearchResult searchHolidays(final SearchRequestData request)
   {

      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#priceForRecSearch(uk.co.portaltech
    * .tui.web.view.data .SearchRequestData)
    */
   @Override
   public SearchResultData<ResultData> priceForRecSearch(final SearchRequestData requestData)
   {

      return null;
   }

}