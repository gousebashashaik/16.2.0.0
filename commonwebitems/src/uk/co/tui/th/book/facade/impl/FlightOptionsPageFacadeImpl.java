/**
 *
 */
package uk.co.tui.th.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.criteria.FlightFilterCriteria;
import uk.co.tui.book.criteria.SearchCriteria;
import uk.co.tui.book.domain.Airport;
import uk.co.tui.book.domain.FlightExtraFacilityResponse;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.PackageUpdationService;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.inventory.AlternateFlightsSearchService;
import uk.co.tui.book.services.inventory.AvailableDurationSearchService;
import uk.co.tui.book.services.inventory.FlightExtrasService;
import uk.co.tui.book.services.inventory.PackageAlternateService;
import uk.co.tui.book.services.inventory.PackageExtrasService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.th.book.ExtraFacilityUpdator;
import uk.co.tui.th.book.constants.BookFlowConstants;
import uk.co.tui.th.book.constants.SessionObjectKeys;
import uk.co.tui.th.book.facade.FlightOptionsPageFacade;
import uk.co.tui.th.book.populators.CalendarViewPopulator;
import uk.co.tui.th.book.populators.FlightExtrasContainerPopulator;
import uk.co.tui.th.book.populators.PackageViewDataPopulator;
import uk.co.tui.th.book.populators.SummaryPanelUrlPopulator;
import uk.co.tui.th.book.store.AlternativeFlightStore;
import uk.co.tui.th.book.store.FlightExtraFacilityStore;
import uk.co.tui.th.book.view.data.CalendarViewData;
import uk.co.tui.th.book.view.data.ContentViewData;
import uk.co.tui.th.book.view.data.FlightOptionsContentViewData;
import uk.co.tui.th.book.view.data.FlightOptionsStaticContentViewData;
import uk.co.tui.th.book.view.data.FlightOptionsViewData;
import uk.co.tui.th.book.view.data.teasers.AirportTeaserViewData;
import uk.co.tui.th.book.view.data.teasers.DepartureDateTeaserViewData;
import uk.co.tui.th.book.view.data.teasers.DurationTeaserViewData;
import uk.co.tui.th.exception.TUIBusinessException;
import uk.co.tui.th.exception.TUISystemException;
import uk.co.tui.util.CurrencyUtil;

/**
 * The Class FlightOptionsPageFacadeImpl.
 *
 * @author sunilkumar.sahu
 */
public class FlightOptionsPageFacadeImpl implements FlightOptionsPageFacade
{

   /** The logger to be used. */
   private static final TUILogUtils LOG = new TUILogUtils("FlightOptionsPageFacadeImpl");

   private static final String TUI_SYSTEM_EXCEPTION = "TUISystemException : ";

   /** The alternate flight search service. */
   @Resource
   private PackageAlternateService packageAlternateService;

   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The alternate flights search service. */
   @Resource
   private AlternateFlightsSearchService alternateFlightsSearchService;

   /** The available duration search service. */
   @Resource
   private ServiceLocator<AvailableDurationSearchService> durationSearchServiceLocator;

   /** The drools priority provider service. */
   @Resource
   private DroolsPriorityProviderService droolsPriorityProviderService;

   /** The airport teaser populator. */
   @Resource(name = "thAirportTeaserViewDataPopulator")
   private Populator<BasePackage, AirportTeaserViewData> airportTeaserViewDataPopulator;

   /** The date time teaser populator. */
   @Resource(name = "thDateTimeTeaserViewDataPopulator")
   private Populator<BasePackage, DepartureDateTeaserViewData> dateTimeTeaserViewDataPopulator;

   /** The duration teaser populator. */
   @Resource(name = "thDurationTeaserViewDataPopulator")
   private Populator<BasePackage, DurationTeaserViewData> durationTeaserViewDataPopulator;

   /** The calendar view data creator. */
   @Resource(name = "thCalendarViewPopulator")
   private CalendarViewPopulator calendarViewPopulator;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The flight filter criteria populator. */
   @Resource(name = "thFlightFilterCriteriaPopulator")
   private Populator<BasePackage, FlightFilterCriteria> flightFilterCriteriaPopulator;

   /** The airport data to airport populator. */
   @Resource(name = "thAirportDataToAirportPopulator")
   private Populator<AirportData, Airport> airportDataToAirportPopulator;

   /** The package extras service. */
   @Resource
   private PackageExtrasService packageExtrasServiceLite;

   @Resource
   private TUIConfigService tuiConfigService;

   /** The flight extras service. */
   @Resource
   private FlightExtrasService flightExtrasServiceLite;

   /** The package view data populator. */
   @Resource(name = "thPackageViewDataPopulator")
   private PackageViewDataPopulator packageViewDataPopulator;

   /** The extra facility updator. */
   @Resource(name = "thExtraFacilityUpdator")
   private ExtraFacilityUpdator extraFacilityUpdator;

   /** The summary panel url populator. */
   @Resource(name = "thSummaryPanelUrlPopulator")
   private SummaryPanelUrlPopulator summaryPanelUrlPopulator;

   @Resource(name = "thFlightExtrasContainerPopulator")
   private FlightExtrasContainerPopulator flightExtrasContainerPopulator;

   /** The flight static content view data populator. */
   @Resource(name = "thFlightStaticContentViewDataPopulator")
   private Populator<Object, FlightOptionsStaticContentViewData> flightStaticContentViewDataPopulator;

   /** The flight options content view data populator. */
   @Resource(name = "thFlightOptionsContentViewDataPopulator")
   private Populator<Object, ContentViewData> flightOptionsContentViewDataPopulator;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The package updation service. */
   @Resource
   private PackageUpdationService packageUpdationService;

   /** The price calculation service. */
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;

   public static final int TWO = 2;

   /** Package ViewData Populator Service Locator */
   @Resource
   private ServiceLocator<Populator> packageViewDataPopulatorServiceLocator;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private CurrencyResolver currencyResolver;

   /**
    * This method facilitates the rendering of Alternate Flights Component in Flight options page.
    *
    * @return the flight options view data
    * @throws TUIBusinessException the tUI business exception
    */
   @Override
   public FlightOptionsViewData renderFlightOptions() throws TUIBusinessException
   {

      final FlightOptionsViewData viewData = new FlightOptionsViewData();

      final BasePackage packageModel = getBasePackageFromCart();

      getFlightExtras();
      // Get package model from cart

      // Temporarily adding condition to work following block of code to be
      // executed when
      // TRACS inventory as @com inventory doesn't have anite cache ready
      // Once everything ready we need remove the condtion and make the code
      // as earlier

      List<BasePackage> singleAccomResultPackages = null;
      FlightFilterCriteria filterCriteria = null;
      // Create store that holds all the alternate packages
      final AlternativeFlightStore alternativeFlightStore = new AlternativeFlightStore();
      final SearchCriteria searchCriteria = new SearchCriteria();
      if (sessionService.getAttribute(SessionObjectKeys.LATESTCRITERIA) != null)
      {
         populateSearchCriteria(getAirportFromSearchCriteria(), searchCriteria);
      }
      else
      {
         populateSearchCriteria(searchCriteria,
            (packageComponentService.getFlightItinerary(packageModel)).getOutBound());
      }
      updateSearchCriteria(searchCriteria, packageModel.getDuration(), packageModel
         .getListOfHighlights().contains(HighLights.LAPLAND_DAYTRIP));
      sessionService.setAttribute(SessionObjectKeys.SEARCH_CRITERIA, searchCriteria);
      // List of all alternates
      sessionService.setAttribute(SessionObjectKeys.ALTERNATE_FLIGHT_STORE, alternativeFlightStore);
      filterCriteria = new FlightFilterCriteria();
      // Setting the selected package details into flight filter criteria
      // to
      // get eligible candidates
      flightFilterCriteriaPopulator.populate(packageModel, filterCriteria);
      singleAccomResultPackages = fetchAlternatePackagesLite(packageModel, alternativeFlightStore);
      populateAvlQuantityForStandardSeats(packageModel);

      packageUpdationService.updateDreamLinerInfo(singleAccomResultPackages, packageModel);
      // populate view data
      populateFlightOptionsPageViewData(packageModel, viewData, singleAccomResultPackages,
         filterCriteria);

      populateFlightOptionsStaticContentViewData(viewData);
      populateFlightOptionsContentViewData(viewData);
      return viewData;
   }

   /**
    * Populate available quantity for standard seats.
    *
    */
   @Override
   @SuppressWarnings("boxing")
   public void populateAvlQuantityForStandardSeats(final BasePackage packageModel)
   {
      final FlightExtraFacilityStore flightExtraStore =
         sessionService.getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
      final AlternativeFlightStore alternativeFlightStore =
         sessionService.getAttribute(SessionObjectKeys.ALTERNATE_FLIGHT_STORE);
      final String packageId = packageModel.getId();
      final List<ExtraFacility> extraFacilitiesList =
         flightExtraStore.getExtraFacilityFromAllLegsBasedOnCabCls(packageId,
            ExtraFacilityConstants.DEFAULT_SEAT, ExtraFacilityConstants.DEFAULT_SEAT_CABIN_CLASS);
      for (final BasePackage packageHoliday : alternativeFlightStore.getPackageHoliday())
      {
         if (StringUtils.equalsIgnoreCase(packageId, packageHoliday.getId()))
         {
            for (final ExtraFacility extraFacility : extraFacilitiesList)
            {
               extraFacility.setQuantity(Integer.valueOf((packageComponentService
                  .getFlightItinerary(packageModel)).getMinAvail()));
            }
            updatePackageModel(packageModel.getExtraFacilityCategories(), packageHoliday);
         }
      }
   }

   /**
    * Update package model.
    *
    * @param categoryList the category list
    * @param packageHoliday the package holiday
    */
   private void updatePackageModel(final List<ExtraFacilityCategory> categoryList,
      final BasePackage packageHoliday)
   {
      for (final ExtraFacilityCategory category : categoryList)
      {
         if (StringUtils.equalsIgnoreCase(category.getCode(),
            ExtraFacilityConstants.SEAT_EXTRAS_CATEGORY_CODE))
         {
            for (final ExtraFacility extraFacility : category.getExtraFacilities())
            {
               updateAvlQuantityForDefaultSeat(packageHoliday, extraFacility);
            }
         }
      }
   }

   /**
    * Update available quantity for default seat.
    *
    * @param packageHoliday the package holiday
    * @param extraFacility the extra facility
    */
   @SuppressWarnings("boxing")
   private void updateAvlQuantityForDefaultSeat(final BasePackage packageHoliday,
      final ExtraFacility extraFacility)
   {
      if (StringUtils.equalsIgnoreCase(extraFacility.getExtraFacilityCode(),
         ExtraFacilityConstants.DEFAULT_SEAT))
      {
         extraFacility.setQuantity(Integer.valueOf((packageComponentService
            .getFlightItinerary(packageHoliday)).getMinAvail()));
      }
   }

   /**
    * The method hits anite and fetches the flightExtraFacility response and validates the Extras ,
    * populates into model and finally populates into the viewData.
    *
    * @throws TUIBusinessException
    */
   private void getFlightExtras() throws TUIBusinessException
   {

      // get extra facility response for the selected package
      FlightExtraFacilityResponse flightExtraFacilityResponse;
      try
      {
         flightExtraFacilityResponse = flightExtrasServiceLite.getFlightExtra();
      }
      catch (final BookServiceException e)
      {
         LOG.error("TUIBusinessException : " + e.getErrorCode());
         throw new TUIBusinessException(e.getErrorCode(), e.getCustomMessage(), e);
      }
      // update package model with default extra facility
      final FlightExtraFacilityStore flightExtraStore = new FlightExtraFacilityStore();

      updateFlightExtraFacilityStore(flightExtraFacilityResponse, flightExtraStore);
      packageExtrasServiceLite.updateDefaultExtraFacilitiesToPackage(flightExtraStore
         .getExtraFacilityFromAllLegsBasedOnCabinClass(flightExtraFacilityResponse.getPackageId(),
            getCabinClassFromPackage()));

   }

   /**
    * Fetch alternate packages.
    *
    * @param packageModel the package model
    * @param alternativeFlightStore the alternative flight store
    * @return the list
    */
   private List<BasePackage> fetchAlternatePackagesLite(final BasePackage packageModel,
      final AlternativeFlightStore alternativeFlightStore)
   {
      List<BasePackage> alternatePackages;
      try
      {
         alternatePackages =
            alternateFlightsSearchService.getAlternateFlightsLite(getSearchCriteriaFromSession(),
               packageModel,
               getDurationsToSearchAndUpdateStore(packageModel, alternativeFlightStore));
      }
      catch (final BookServiceException e)
      {
         LOG.error(TUI_SYSTEM_EXCEPTION + e.getMessage());
         throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
      }
      if (CollectionUtils.isNotEmpty(alternatePackages))
      {
         alternativeFlightStore.addAlternatePackagesHoliday(alternatePackages);
      }
      return alternatePackages;
   }

   private SearchCriteria getSearchCriteriaFromSession()
   {
      return (SearchCriteria) sessionService.getAttribute(SessionObjectKeys.SEARCH_CRITERIA);
   }

   /**
    * Gets the durations to search and update store.
    *
    * @param packageModel the package model
    * @param alternativeFlightStore the alternative flight store
    * @return List containing selected packages duration & the next highest
    */
   private List<String> getDurationsToSearchAndUpdateStore(final BasePackage packageModel,
      final AlternativeFlightStore alternativeFlightStore)
   {
      // duration from available durations
      List<String> durationsToSearch;
      try
      {
         durationsToSearch =
            durationSearchServiceLocator.locateByPackageType(
               packageModel.getPackageType().toString()).getAvailableDurations(
               getSearchCriteriaFromSession(), packageModel);
      }
      catch (final BookServiceException e)
      {
         LOG.error(TUI_SYSTEM_EXCEPTION + e.getMessage());
         throw new TUISystemException(e.getMessage(), e.getCustomMessage(), e);
      }
      // Add durations to store for future use
      alternativeFlightStore.addDurations(durationsToSearch);
      return durationsToSearch;
   }

   /**
    * Sets the teasers view data to page.
    *
    * @param flViewData the fl view data
    * @param packageModel the package model
    * @param alternatePackages the alternate packages
    * @param filterCriteria the filter criteria
    */
   private void setTeasersViewData(final FlightOptionsViewData flViewData,
      final BasePackage packageModel, final List<BasePackage> alternatePackages,
      final FlightFilterCriteria filterCriteria)
   {
      final int chargeablePaxCount =
         PassengerUtils.getChargeablePaxCount(packageModel.getPassengers());
      flViewData.setAirportTeaserViewData(buildAirportTeaserView(packageModel, alternatePackages,
         filterCriteria, chargeablePaxCount));
      flViewData.setDateTeaserViewData(buildDepartureDateTeaserView(packageModel,
         alternatePackages, filterCriteria, chargeablePaxCount));
      flViewData.setDurationTeaserViewData(getDurationTeaserView(packageModel, alternatePackages,
         filterCriteria, chargeablePaxCount));
   }

   /**
    * gets the base package model from cart.
    *
    * @return the package from cart
    */
   private BasePackage getBasePackageFromCart()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * Gets the duration teaser view data.
    *
    * @param packageModel the package model
    * @param packages the packages
    * @param filterCriteria the filter criteria
    * @param payablePax the payable pax
    * @return the duration teaser view data
    */
   private DurationTeaserViewData getDurationTeaserView(final BasePackage packageModel,
      final List<BasePackage> packages, final FlightFilterCriteria filterCriteria,
      final int payablePax)
   {
      DurationTeaserViewData durationTeaserViewData = null;

      final BasePackage durationTeaserPackage =
         packageAlternateService.findDurationTeaserPackage(packages, filterCriteria);
      if (durationTeaserPackage != null)
      {
         durationTeaserViewData = new DurationTeaserViewData();
         final BigDecimal diffTotalPrice =
            calculatePriceDifference(packageModel, durationTeaserPackage);
         durationTeaserViewData.setDiffTotalPrice(CurrencyUtils
            .getCurrencyAppendedPrice(diffTotalPrice));
         durationTeaserViewData.setDiffPerPersonPrice(calculateRate(diffTotalPrice, payablePax));
         durationTeaserViewDataPopulator.populate(durationTeaserPackage, durationTeaserViewData);
      }
      return durationTeaserViewData;
   }

   /**
    * Gets the calendar overlay view data.
    *
    * @param cartPackage the package model
    * @param packages the packages
    * @param filterCriteria the filter criteria
    * @return the calendar overlay view data
    */
   private CalendarViewData renderCalendarOverlayView(final BasePackage cartPackage,
      final List<BasePackage> packages, final FlightFilterCriteria filterCriteria)
   {
      final List<BasePackage> packageList =
         packageAlternateService.filterAlternativeFlightResults(packages, filterCriteria);
      Map<List<Date>, List<BasePackage>> availabilityCalendar = null;
      Set<HighLights> highlights = null;
      if (CollectionUtils.isNotEmpty(packageList))
      {
         availabilityCalendar = packageAlternateService.groupByDateAndOrderByPrice(packageList);
         highlights = packageAlternateService.resolvePackageExtraFacilities(packageList);
      }
      if (CollectionUtils.isEmpty(getSearchCriteriaFromSession().getAirports()))
      {
         packageAlternateService.getAirportForAnyUk(getSearchCriteriaFromSession(), cartPackage);
      }

      return calendarViewPopulator.populateCalendarViewData(availabilityCalendar,
         getSearchCriteriaFromSession(), highlights, cartPackage, filterCriteria);

   }

   /**
    * Gets the departure date teaser view data.
    *
    * @param packageModel the package model
    * @param packages the packages
    * @param filterCriteria the filter criteria
    * @param payablePax the payable pax
    * @return DepartureDateTeaserViewData
    */
   private DepartureDateTeaserViewData buildDepartureDateTeaserView(final BasePackage packageModel,
      final List<BasePackage> packages, final FlightFilterCriteria filterCriteria,
      final int payablePax)
   {

      DepartureDateTeaserViewData departureDateTeaserViewData = null;

      final BasePackage dateAndTimeTeaserPackage =
         packageAlternateService.findDateTimeTeaserPackage(packages, filterCriteria);
      if (dateAndTimeTeaserPackage != null)
      {
         departureDateTeaserViewData = new DepartureDateTeaserViewData();
         final BigDecimal diffTotalPrice =
            calculatePriceDifference(packageModel, dateAndTimeTeaserPackage);
         departureDateTeaserViewData.setDiffTotalPrice(CurrencyUtils
            .getCurrencyAppendedPrice(diffTotalPrice));
         departureDateTeaserViewData
            .setDiffPerPersonPrice(calculateRate(diffTotalPrice, payablePax));
         dateTimeTeaserViewDataPopulator.populate(dateAndTimeTeaserPackage,
            departureDateTeaserViewData);
      }
      return departureDateTeaserViewData;
   }

   /**
    * Gets the airport teaser view data.
    *
    * @param packageModel the package model
    * @param packages the packages
    * @param filterCriteria the filter criteria
    * @param payablePax the payable pax
    * @return AirportTeaserViewData
    */
   private AirportTeaserViewData buildAirportTeaserView(final BasePackage packageModel,
      final List<BasePackage> packages, final FlightFilterCriteria filterCriteria,
      final int payablePax)
   {

      AirportTeaserViewData airportTeaserViewData = null;

      final BasePackage airportTeaserPackage =
         packageAlternateService.findAirportTeaserPackage(packages, filterCriteria);
      if (airportTeaserPackage != null)
      {
         airportTeaserViewData = new AirportTeaserViewData();
         final BigDecimal diffTotalPrice =
            calculatePriceDifference(packageModel, airportTeaserPackage);
         airportTeaserViewData.setDiffTotalPrice(CurrencyUtils
            .getCurrencyAppendedPrice(diffTotalPrice));
         airportTeaserViewData.setDiffPerPersonPrice(calculateRate(diffTotalPrice, payablePax));
         airportTeaserViewDataPopulator.populate(airportTeaserPackage, airportTeaserViewData);
      }
      return airportTeaserViewData;
   }

   /**
    * Calculate price difference.
    *
    * @param packageModel the package model
    * @param teaserPackage the teaser package
    * @return diffTotalPrice
    */
   private BigDecimal calculatePriceDifference(final BasePackage packageModel,
      final BasePackage teaserPackage)
   {
      final BigDecimal pkgPrice = priceCalculationService.calculateBasicCost(packageModel);

      final BigDecimal teaserPrice =
         priceCalculationService.calculatePackagePriceExcludingDefaultExtras(
            teaserPackage.getPrice().getAmount().getAmount(), packageModel.getProductType(),
            packageModel.getExtraFacilityCategories(), teaserPackage.getListOfHighlights())
            .subtract(priceCalculationService.calculateExtraFacilitiesTotalCost(teaserPackage));

      return teaserPrice.subtract(pkgPrice).setScale(TWO, RoundingMode.HALF_UP);
   }

   /**
    * Populate search criteria.
    *
    * @param airportsData the airports data
    * @param searchCriteria the search criteria
    */
   private void populateSearchCriteria(final List<AirportData> airportsData,
      final SearchCriteria searchCriteria)
   {
      final List<Airport> airports = new ArrayList<Airport>();
      for (final AirportData airportData : airportsData)
      {
         final Airport airport = new Airport();
         airportDataToAirportPopulator.populate(airportData, airport);
         airports.add(airport);
      }
      searchCriteria.setAirports(airports);
   }

   /**
    * Populate search criteria.
    *
    * @param searchCriteria the search criteria
    * @param outBound the out bound
    */
   private void populateSearchCriteria(final SearchCriteria searchCriteria, final List<Leg> outBound)
   {
      final List<Airport> airports = new ArrayList<Airport>();
      for (final Leg leg : outBound)
      {
         final Airport airport = new Airport();
         airport.setCode(leg.getDepartureAirport().getCode());
         airports.add(airport);
      }
      searchCriteria.setAirports(airports);
   }

   /**
    * Update search criteria.
    *
    * @param searchCriteria the search criteria
    * @param duration the duration
    * @param isLaplandDayTrip the is lapland day trip
    */
   private void updateSearchCriteria(final SearchCriteria searchCriteria, final Integer duration,
      final boolean isLaplandDayTrip)
   {
      if (isLaplandDayTrip)
      {
         searchCriteria.setDurationPriorityOrder("1");
         return;
      }
      searchCriteria.setDurationPriorityOrder(droolsPriorityProviderService
         .getDurationPriorityList(String.valueOf(duration)));
   }

   /**
    * This method is used to return the calendar data to the view.
    *
    * @param filterCriteria the filter criteria
    * @return the availability calendar
    */
   @Override
   public FlightOptionsViewData renderAvailabilityCalendar(final FlightFilterCriteria filterCriteria)
   {
      final FlightOptionsViewData flvViewData =
         sessionService.getAttribute("flightOptionsViewData");

      final BasePackage selectedPackage = packageCartService.getBasePackage();

      final AlternativeFlightStore alternativeFlightStore =
         fetchAlternateFlightStore(selectedPackage);

      if (!alternativeFlightStore.isDurationAvailable(String.valueOf(filterCriteria
         .getSelectedDuration())))
      {
         // Entered new duration that is not in store
         List<BasePackage> newAlternativePkgs;
         try
         {
            newAlternativePkgs =
               alternateFlightsSearchService.getAlternateFlightsLite(
                  getSearchCriteriaFromSession(), selectedPackage,
                  Arrays.asList(String.valueOf(filterCriteria.getSelectedDuration())));
         }
         catch (final BookServiceException e)
         {
            LOG.error(TUI_SYSTEM_EXCEPTION + e.getMessage());
            throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
         }
         alternativeFlightStore.addAlternatePackagesHoliday(newAlternativePkgs);
         alternativeFlightStore.addDuration(String.valueOf(filterCriteria.getSelectedDuration()));
         sessionService.setAttribute(SessionObjectKeys.ALTERNATE_FLIGHT_STORE,
            alternativeFlightStore);
      }
      flvViewData.setCalendarViewData(renderCalendarOverlayView(selectedPackage,
         alternativeFlightStore.getPackageHoliday(), filterCriteria));
      populateThirdPartyFlightInfo(flvViewData);

      return flvViewData;
   }

   /**
    * Updates flight extra facility store.
    *
    * @param flightExtraFacilityResponse the flight extra facility response
    * @param flightExtraStore the flight extra store
    */
   private void updateFlightExtraFacilityStore(
      final FlightExtraFacilityResponse flightExtraFacilityResponse,
      final FlightExtraFacilityStore flightExtraStore)
   {
      flightExtraStore.addFlightExtraFacilityResponseToStore(flightExtraFacilityResponse);
      sessionService.setAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE, flightExtraStore);
   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param packageModel the BasePackage
    * @param viewData the FlightOptionsViewData
    * @param alternativeFlightPackges the alternative flight packges
    * @param filterCriteria the filter criteria
    */
   private void populateFlightOptionsPageViewData(final BasePackage packageModel,
      final FlightOptionsViewData viewData, final List<BasePackage> alternativeFlightPackges,
      final FlightFilterCriteria filterCriteria)
   {
      populatePackageView(packageModel, viewData);
      populateSummaryPanelUrlsViewData(viewData);
      setTeasersViewData(viewData, packageModel, alternativeFlightPackges, filterCriteria);
   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param packageModel the BasePackage
    * @param viewData the FlightOptionsViewData
    */
   @Override
   public void populatePackageView(final BasePackage packageModel,
      final FlightOptionsViewData viewData)
   {
      updatePackageIntoCart();
      populateContainerView(viewData, packageModel.getId());
      (packageViewDataPopulatorServiceLocator.locateByPackageType(packageModel.getPackageType()
         .toString())).populate(packageModel, viewData.getPackageViewData());
      extraFacilityUpdator.updatePackageViewData(packageModel, viewData.getPackageViewData());
      extraFacilityUpdator.updateSeatExtrasRelativePrice(viewData);
      extraFacilityUpdator.updateBaggageDisplayIndicator(packageModel, viewData);
      extraFacilityUpdator.updateContainerViewData(viewData.getPackageViewData(),
         viewData.getExtraFacilityViewDataContainer());

   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param viewData the FlightOptionsViewData
    */
   private void populateSummaryPanelUrlsViewData(final FlightOptionsViewData viewData)
   {
      summaryPanelUrlPopulator.populate(BookFlowConstants.FLIGHTOPTIONS,
         viewData.getSummaryViewData());
   }

   /**
    * The method is used to populate the List of ExtraFacility which are present in all the Legs as
    * part of Flight response.
    *
    * @param viewData the FlightOptionsViewData
    * @param packageCode the package code
    */
   private void populateContainerView(final FlightOptionsViewData viewData, final String packageCode)
   {
      final FlightExtraFacilityStore flightExtraStore =
         sessionService.getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);

      final Map<String, List<ExtraFacility>> validExtraFacilitiesMap =
         flightExtraStore.getExtraFacilityFromAllLegsBasedOnCabinClass(packageCode,
            getCabinClassFromPackage());

      for (final List<ExtraFacility> extraFacilityModels : validExtraFacilitiesMap.values())
      {
         flightExtrasContainerPopulator.populate(extraFacilityModels,
            viewData.getExtraFacilityViewDataContainer());
      }

      extraFacilityUpdator.sortSeatsByIndex(viewData.getExtraFacilityViewDataContainer()
         .getSeatOptions());
      extraFacilityUpdator.sortMealAlphabetically(viewData.getExtraFacilityViewDataContainer()
         .getMealOptions());
      extraFacilityUpdator.sortBaggageBasedOnWeight(viewData.getExtraFacilityViewDataContainer()
         .getBaggageOptions());
   }

   /**
    * Populate flight options static content view data.
    *
    * @param viewData the view data
    */
   @Override
   public void populateFlightOptionsStaticContentViewData(final FlightOptionsViewData viewData)
   {
      final FlightOptionsStaticContentViewData flightOptionsStaticContentViewData =
         new FlightOptionsStaticContentViewData();
      flightStaticContentViewDataPopulator.populate(new Object(),
         flightOptionsStaticContentViewData);
      viewData.setFlightOptionsStaticContentViewData(flightOptionsStaticContentViewData);

   }

   /**
    * Populates the flight options content view data
    */
   private void populateFlightOptionsContentViewData(final FlightOptionsViewData viewData)
   {
      final FlightOptionsContentViewData flightOptionsContentViewData =
         new FlightOptionsContentViewData();
      final ContentViewData contentViewData = new ContentViewData();
      flightOptionsContentViewDataPopulator.populate(new Object(), contentViewData);
      flightOptionsContentViewData.setFlightContentViewData(contentViewData);
      viewData.setFlightOptionsContentViewData(flightOptionsContentViewData);

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.FlightOptionsPageFacade#updatePackageIntoCart
    * (uk.co.tui.domain.model.BasePackage)
    */
   @Override
   public void updatePackageIntoCart()
   {
      packageCartService.updateCart(getBasePackageFromCart());
   }

   /**
    * Gets the airport from search criteria.
    *
    * @return the airport from search criteria
    */
   private List<AirportData> getAirportFromSearchCriteria()
   {
      return ((SearchResultsRequestData) sessionService
         .getAttribute(SessionObjectKeys.LATESTCRITERIA)).getAirports();
   }

   /**
    * The method to render the thirdParty flights.
    *
    * @return viewData
    */
   @Override
   public FlightOptionsViewData renderFlightOptionsForMulticomThirdPartyFlight()
   {
      final FlightOptionsViewData viewData = new FlightOptionsViewData();
      // Get package model from cart.
      final BasePackage packageModel = getBasePackageFromCart();
      // Fetch Alternate packages from session.
      final List<BasePackage> singleAccomResultPackages = getAlternateFlightPackages(packageModel);
      packageUpdationService.updateDreamLinerInfo(singleAccomResultPackages, packageModel);
      // populate view data
      populateThirdPartyFlightViewData(singleAccomResultPackages, packageModel, viewData);
      return viewData;

   }

   /**
     *
     */
   private void populateThirdPartyFlightViewData(final List<BasePackage> alternativeFlightPackges,
      final BasePackage packageModel, final FlightOptionsViewData viewData)
   {
      final FlightFilterCriteria filterCriteria = new FlightFilterCriteria();
      // Setting the selected package details into flight filter criteria to
      // get eligible candidates
      flightFilterCriteriaPopulator.populate(packageModel, filterCriteria);

      packageViewDataPopulator.populate(packageModel, viewData.getPackageViewData());
      extraFacilityUpdator.updatePackageViewData(packageModel, viewData.getPackageViewData());
      populateFlightOptionsStaticContentViewData(viewData);
      populateFlightOptionsContentViewData(viewData);
      populateSummaryPanelUrlsViewData(viewData);
      setTeasersViewData(viewData, packageModel, alternativeFlightPackges, filterCriteria);
   }

   /**
    * @param packageModel
    * @return alternateFlightPackages
    */
   private List<BasePackage> getAlternateFlightPackages(final BasePackage packageModel)
   {
      return fetchAlternateFlightStore(packageModel).getPackageHoliday();
   }

   /**
    *
    * @param packageModel
    */
   private AlternativeFlightStore fetchAlternateFlightStore(final BasePackage packageModel)
   {
      AlternativeFlightStore alternativeFlightStore =
         sessionService.getAttribute(SessionObjectKeys.ALTERNATE_FLIGHT_STORE);

      if (!(alternativeFlightStore != null && CollectionUtils.isNotEmpty(alternativeFlightStore
         .getPackageHoliday())))
      {
         alternativeFlightStore = new AlternativeFlightStore();
         sessionService.setAttribute(SessionObjectKeys.ALTERNATE_FLIGHT_STORE,
            alternativeFlightStore);
         fetchAlternatePackagesLite(packageModel, alternativeFlightStore);
      }
      return alternativeFlightStore;
   }

   private String getCabinClassFromPackage()
   {
      return PackageUtilityService.getCabinClass(getBasePackageFromCart());
   }

   /**
    * Calculate rate.
    *
    * @param totalPrice the total price
    * @param quantity the quantity
    * @return the string
    */
   private String calculateRate(final BigDecimal totalPrice, final int quantity)
   {
      return CurrencyUtil.getCurrencyAppendedPrice(
         totalPrice.divide(BigDecimal.valueOf(quantity), TWO, RoundingMode.HALF_UP),
         currencyResolver.getSiteCurrency());
   }

   @Override
   public void populateThirdPartyFlightStaticContentViewData(final FlightOptionsViewData viewData,
      final Set<String> carrierCodes)
   {
      final Map<String, Map<String, String>> thirfpartyInfoMap =
         new HashMap<String, Map<String, String>>();

      final Map<String, String> summaryContentMap = staticContentServ.getThirdPartyInfoContents();
      if (CollectionUtils.isNotEmpty(carrierCodes))
      {
         for (final String code : carrierCodes)
         {
            thirfpartyInfoMap.put(code, categorizeStaticContent(summaryContentMap, code));

         }
      }
      viewData.setThirdPartyInfoMap(thirfpartyInfoMap);

   }

   // Method to populate information regarding third party flight

   @Override
   public void populateThirdPartyFlightInfo(final FlightOptionsViewData viewData)
   {

      final String thirdPFCodes =
         tuiConfigService.getConfigValue("TH.thirdPartyFlight.carrierCodeList");
      final Set<String> carrierCodes = new HashSet<String>();
      final List<String> mixedLegCodes = new ArrayList<String>();
      final List<String> codes = splitCodes(thirdPFCodes);
      // Checks if itinerary has thirdparty flight

      checkForThirdPartyFlight(codes, carrierCodes, viewData);
      // Checks if its mixed leg

      checkForMixedLeg(viewData, mixedLegCodes, carrierCodes);
      // populate static content for the carrier codes

      populateThirdPartyFlightStaticContentViewData(viewData, carrierCodes);

   }

   // Checks if inbound and outbound are mixed legs

   private void checkForMixedLeg(final FlightOptionsViewData viewData,
      final List<String> mixedLegCodes, final Set<String> carrierCodes)
   {
      if (inboundThirdParty(viewData))
      {
         mixedLegCodes.add(viewData.getPackageViewData().getFlightViewData().get(0)
            .getInboundSectors().get(0).getCarrierCode());
         mixedLegCodes.add(viewData.getPackageViewData().getFlightViewData().get(0)
            .getOutboundSectors().get(0).getCarrierCode());
      }
      else
      {
         populateOutBoundThirdParty(viewData, mixedLegCodes);
      }
      populateMixedCarrierCodes(mixedLegCodes, carrierCodes);

   }

   /**
    * @param mixedLegCodes
    * @param carrierCodes
    */
   private void populateMixedCarrierCodes(final List<String> mixedLegCodes,
      final Set<String> carrierCodes)
   {
      if (CollectionUtils.isNotEmpty(mixedLegCodes))
      {
         carrierCodes.addAll(mixedLegCodes);
      }
   }

   /**
    * @param viewData
    * @param mixedLegCodes
    */
   private void populateOutBoundThirdParty(final FlightOptionsViewData viewData,
      final List<String> mixedLegCodes)
   {
      if (outboundThirdParty(viewData)
         && CollectionUtils.isNotEmpty(viewData.getPackageViewData().getFlightViewData()))
      {

         mixedLegCodes.add(viewData.getPackageViewData().getFlightViewData().get(0)
            .getInboundSectors().get(0).getCarrierCode());
         mixedLegCodes.add(viewData.getPackageViewData().getFlightViewData().get(0)
            .getOutboundSectors().get(0).getCarrierCode());

      }
   }

   /**
    * @param viewData
    * @return
    */
   private boolean outboundThirdParty(final FlightOptionsViewData viewData)
   {
      boolean mixedleg = false;
      if (CollectionUtils.isNotEmpty(viewData.getPackageViewData().getFlightViewData()))
      {
         mixedleg =
            !viewData.getPackageViewData().getFlightViewData().get(0).isInboundThirdParty()
               && viewData.getPackageViewData().getFlightViewData().get(0).isOutBoundThirdParty();
      }
      return mixedleg;
   }

   /**
    * @param viewData
    * @return
    */
   private boolean inboundThirdParty(final FlightOptionsViewData viewData)
   {
      return viewData.getPackageViewData().getFlightViewData().get(0).isInboundThirdParty()
         && !viewData.getPackageViewData().getFlightViewData().get(0).isOutBoundThirdParty();
   }

   /**
    * @param codes
    * @param carrierCodes
    * @param viewData
    */
   // Checks for thirdparty flight and populate the carriercodes

   private void checkForThirdPartyFlight(final List<String> codes, final Set<String> carrierCodes,
      final FlightOptionsViewData viewData)
   {
      if (CollectionUtils.isNotEmpty(viewData.getPackageViewData().getFlightViewData()))
      {
         if (codes.contains(viewData.getPackageViewData().getFlightViewData().get(0)
            .getInboundSectors().get(0).getCarrierCode()))
         {
            viewData.getPackageViewData().getFlightViewData().get(0).setInboundThirdParty(true);
            carrierCodes.add(viewData.getPackageViewData().getFlightViewData().get(0)
               .getInboundSectors().get(0).getCarrierCode());
         }
         else
         {
            viewData.getPackageViewData().getFlightViewData().get(0).setInboundThirdParty(false);
         }
         if (codes.contains(viewData.getPackageViewData().getFlightViewData().get(0)
            .getOutboundSectors().get(0).getCarrierCode()))
         {
            viewData.getPackageViewData().getFlightViewData().get(0).setOutBoundThirdParty(true);
            carrierCodes.add(viewData.getPackageViewData().getFlightViewData().get(0)
               .getOutboundSectors().get(0).getCarrierCode());

         }
         else
         {
            viewData.getPackageViewData().getFlightViewData().get(0).setOutBoundThirdParty(false);

         }
      }
   }

   private List<String> splitCodes(final String thirdPartFlightCodes)
   {
      return Arrays.asList(thirdPartFlightCodes.split(","));

   }

   private Map<String, String> categorizeStaticContent(final Map<String, String> summaryMap,
      final String carrierCode)
   {
      final Map<String, String> thirdPartyMap = new HashMap<String, String>();
      for (final Map.Entry<String, String> entry : summaryMap.entrySet())
      {
         if (entry.getKey().startsWith(carrierCode + "_"))
         {
            thirdPartyMap.put(entry.getKey(), entry.getValue());
         }
      }
      return thirdPartyMap;
   }

}
