package uk.co.tui.cr.book.populators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.impl.PriceCalculationServiceImpl;
import uk.co.tui.book.criteria.FlightFilterCriteria;
import uk.co.tui.book.criteria.SearchCriteria;
import uk.co.tui.book.domain.Airport;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.config.BookFlowTUIConfigService;
import uk.co.tui.book.utils.AlternativeFlightUtils;
import uk.co.tui.cr.book.view.data.AirportListViewData;
import uk.co.tui.cr.book.view.data.CalendarViewData;
import uk.co.tui.cr.book.view.data.DepartureDateAvailabilityViewData;
import uk.co.tui.cr.book.view.data.FlightOptionsFilterViewData;
import uk.co.tui.cr.book.view.data.FlightViewData;
import uk.co.tui.cr.book.view.data.Leg;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.Schedule;
import uk.co.tui.exception.TUIBusinessException;
import uk.co.tui.util.CurrencyUtil;

/**
 * The Class CalendarViewDataPopulator. This builds CalendarViewData from multiple sources that
 * include List<PackageModels> SearchCriteria, Set<Highights>, BasePackage and FlightFilterCriteria
 *
 * This doesn't implement the conventional Hybris Populator interface because the sources are
 * umpteen.
 *
 * Multiple iterations over heavy objects like PackageModels would lead to PerformanceIssues To
 * avoid these issues, the population and additional default-selection logic is added in the same
 * location
 *
 * This resulted in having plethora of sources for the class
 *
 * @author samantha.gd
 */

public class CalendarViewPopulator
{
   /** The Logger to Log. */
   private static final Logger LOGGER = Logger.getLogger(CalendarViewPopulator.class);

   /** The airports service. */
   @Resource
   private AirportService airportsService;

   @Resource
   private BookFlowTUIConfigService bookFlowTuiConfigService;

   /** The price calculation service. */
   @Resource
   private PriceCalculationServiceImpl priceCalculationService;

   private static final int ZERO = 0;

   private static final int FOURTEEN = 14;

   /** The constant TWO. */
   public static final int TWO = 2;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private CurrencyResolver currencyResolver;

   /** The alternative flight utils. */
   @Resource
   private AlternativeFlightUtils alternativeFlightUtils;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   /**
    * Creates the calendar view data.
    *
    * @param packages the availability calendar
    * @param airports the airports
    * @param highlights the highlights
    * @param cartPkg the package from cart
    * @param filterCriteria the filter criteria
    * @param searchCriteria
    * @return the calendar view data
    */

   public CalendarViewData populateCalendarViewData(final List<BasePackage> packages,
      final Set<HighLights> highlights, final BasePackage cartPkg,
      final FlightFilterCriteria filterCriteria, final SearchCriteria searchCriteria)
   {
      final CalendarViewData viewData = new CalendarViewData();
      viewData.setDepartureAirport(buildAirportsList(filterCriteria.getSelectedDepartureAirport(),
         packages, cartPkg, searchCriteria.getAirports()));
      final boolean passengerAgeLessThanFourteen = isPassengerAgeLessThanFourteen(cartPkg);
      viewData.setAvailability(Collections.<Date, DepartureDateAvailabilityViewData> emptyMap());
      viewData.setFilterViewData(buildFilterViewData(highlights,
         filterCriteria.getSelectedFilters(), passengerAgeLessThanFourteen));
      final Date cartPackageDate =
         packageComponentService.getFlightItinerary(cartPkg).getOutBound().get(0).getSchedule()
            .getDepartureDate();
      viewData.setStartDate(cartPackageDate);
      viewData.setEndDate(cartPackageDate);
      viewData
         .setShouldDisplayAirportLink(viewData.getDepartureAirport().size() > getMaxAirportCount()
            ? true : false);
      return viewData;
   }

   /**
    * Checks if is passenger age less than fourteen.
    *
    * @param cartPkg cartPkg
    * @return true, if is passenger age less than fourteen
    */
   private boolean isPassengerAgeLessThanFourteen(final BasePackage cartPkg)
   {
      final List<Passenger> passengers = cartPkg.getPassengers();
      final int paxCount =
         PassengerUtils.getPassengerCountWithAgeRang(Integer.valueOf(ZERO),
            Integer.valueOf(FOURTEEN), passengers);
      boolean passengerAgeLessThanFourteen = false;
      if (paxCount > 0)
      {
         passengerAgeLessThanFourteen = true;
      }
      return passengerAgeLessThanFourteen;
   }

   /**
    * Creates the filter view data.
    *
    * @param highlights the highlights
    * @param selectedList the selected list
    * @return the list
    */
   private List<FlightOptionsFilterViewData> buildFilterViewData(final Set<HighLights> highlights,
      final List<HighLights> selectedList, final boolean passengerAgeLessThanFourteen)
   {
      if (CollectionUtils.isNotEmpty(highlights))
      {
         return populateHightlights(highlights, selectedList, passengerAgeLessThanFourteen);
      }
      return Collections.emptyList();

   }

   /**
    * @param highlights
    * @param selectedList
    */

   private List<FlightOptionsFilterViewData> populateHightlights(final Set<HighLights> highlights,
      final List<HighLights> selectedList, final boolean passengerAgeLessThanFourteen)
   {
      final List<FlightOptionsFilterViewData> filterData =
         new ArrayList<FlightOptionsFilterViewData>();
      for (final HighLights highlight : highlights)
      {
         if (!isExtraSpaceSeatApplicable(highlight, passengerAgeLessThanFourteen)
            && isFilterHighlight(highlight))
         {
            final FlightOptionsFilterViewData filterViewData = new FlightOptionsFilterViewData();

            // filterViewData.setFilterName(WordUtils.capitalize(StringUtils.lowerCase(code.replaceAll("_",

            populateDreamlinerFilter(highlight, filterViewData);

            setSelectedStatus(selectedList, highlight, filterViewData);

            filterData.add(filterViewData);
         }
      }
      return filterData;
   }

   /**
    * Checks if is extra space seat applicable.
    *
    * @param highlight highlight
    * @param passengerAgeLessThanFourteen the passenger age less than fourteen
    * @return true, if is extra space seat applicable
    */
   private boolean isExtraSpaceSeatApplicable(final HighLights highlight,
      final boolean passengerAgeLessThanFourteen)
   {
      return highlight.equals(HighLights.EXTRA_SPACE_SEATS) && passengerAgeLessThanFourteen;
   }

   /**
    * @param selectedList
    * @param highlight
    * @param filterViewData
    */
   private void setSelectedStatus(final List<HighLights> selectedList, final HighLights highlight,
      final FlightOptionsFilterViewData filterViewData)
   {
      if (selectedList.contains(highlight))
      {
         filterViewData.setSelected(true);
      }
   }

   /**
    * @param highlight
    * @param filterViewData
    */
   private void populateDreamlinerFilter(final HighLights highlight,
      final FlightOptionsFilterViewData filterViewData)
   {
      if (highlight.equals(HighLights.DREAM_LINER))
      {
         filterViewData.setFilterName("787 Dreamliner");
      }
   }

   /**
    * @param highlight
    * @return true if its filter highlight
    */
   private boolean isFilterHighlight(final HighLights highlight)
   {
      return highlight.equals(HighLights.DREAM_LINER)
         || highlight.equals(HighLights.DAYTIME_FLIGHTS_ONLY)
         || highlight.equals(HighLights.PREMIUM_SEATS)
         || highlight.equals(HighLights.EXTRA_SPACE_SEATS);
   }

   /**
    * Creates the airport list. This would render the airport tabs. Creates View Data for each
    * airport in the list Holds the additional logic to make a tab selected by default Provides the
    * order in which the tabs should be displayed Contains the swapping logic for swapping of tabs
    * on click of MoreAirports Link
    *
    * @param airports the airports
    * @param airportCode the selected airport code
    * @param packages
    * @param cartPackage
    * @param listOfAirports
    * @return the list
    */
   private List<AirportListViewData> buildAirportsList(final String airportCode,
      final List<BasePackage> packages, final BasePackage cartPackage,
      final List<Airport> listOfAirports)
   {
      final List<AirportListViewData> airportData = new ArrayList<AirportListViewData>();
      try
      {
         final List<AirportModel> airportModels =
            airportsService.getAirportByCodes(alternativeFlightUtils.resolveDepartureAirport(
               listOfAirports, cartPackage.getPackageType()));

         populateAirportTabsViewData(airportCode, airportData, airportModels, packages, cartPackage);
         swapAirportsBySelection(airportCode, airportData);

      }
      catch (final TUIBusinessException exp)
      {
         LOGGER.error("Error while fetching Airports from PIM", exp);
      }
      return airportData;
   }

   /**
    * @param airportCode
    */
   // This method performs swapping of airport models if the position is
   // greater than max airports tab count
   private void swapAirportsBySelection(final String airportCode,
      final List<AirportListViewData> airportData)
   {
      final int maxAirportTabsCount = getMaxAirportCount();
      for (final AirportListViewData airportDatum : airportData)
      {
         if (StringUtils.equals(airportDatum.getAirportCode(), airportCode))
         {
            airportDatum.setShouldDisplay(true);
            if (airportDatum.getDisplayOrder() > maxAirportTabsCount)
            {
               final int tempDispOrder = airportData.get(maxAirportTabsCount - 1).getDisplayOrder();
               airportData.get(maxAirportTabsCount - 1).setDisplayOrder(
                  airportDatum.getDisplayOrder());
               airportDatum.setDisplayOrder(tempDispOrder);
               airportData.get(maxAirportTabsCount - 1).setShouldDisplay(false);

            }
         }
      }
   }

   /**
    * @param airports
    * @param airportCode
    * @param airportData
    * @param airportModels
    * @param packages
    * @param cartPkg
    */
   private void populateAirportTabsViewData(final String airportCode,
      final List<AirportListViewData> airportData, final List<AirportModel> airportModels,
      final List<BasePackage> packages, final BasePackage cartPkg)
   {
      int dispOrder = 1;
      final int maxAirportTabsCount = getMaxAirportCount();
      for (final AirportModel airportModel : airportModels)
      {
         final AirportListViewData airportDatum = new AirportListViewData();
         airportDatum.setFlightViewData(Collections.<PackageViewData> emptyList());
         updateAirportData(airportCode, packages, cartPkg, airportModel, airportDatum);
         if (dispOrder <= maxAirportTabsCount)
         {
            airportDatum.setShouldDisplay(true);
         }
         airportDatum.setAirportCode(airportModel.getCode());
         airportDatum.setAirportName(airportModel.getName());
         airportDatum.setDisplayOrder(dispOrder++);
         setSelectedAirportToTrue(airportDatum, cartPkg.getId());
         airportData.add(airportDatum);
      }

   }

   /**
    * @param airportCode
    * @param packages
    * @param cartPkg
    * @param airportModel
    * @param airportDatum
    */
   private void updateAirportData(final String airportCode, final List<BasePackage> packages,
      final BasePackage cartPkg, final AirportModel airportModel,
      final AirportListViewData airportDatum)
   {
      if (StringUtils.equals(airportModel.getCode(), airportCode))
      {
         airportDatum.setSelected(true);
         airportDatum.setShouldDisplay(true);
         airportDatum.setFlightViewData(populatePackageData(cartPkg, packages));
      }
   }

   /**
    * @param cartPkg
    * @param packages
    * @return list of populated view Data
    */
   private List<PackageViewData> populatePackageData(final BasePackage cartPkg,
      final List<BasePackage> packages)
   {
      final List<PackageViewData> flightViewData = new ArrayList<PackageViewData>();
      final BigDecimal pkgPrice = priceCalculationService.calculateBasicCost(cartPkg);
      if (CollectionUtils.isNotEmpty(packages))
      {
         for (final BasePackage pkg : packages)
         {
            final PackageViewData pkgData = createPackageViewData(pkg);

            final BigDecimal alternatePackagePrice =
               priceCalculationService.computePackagePriceExcludingDefaultExtras(
                  pkg.getPrice().getAmount().getAmount(), cartPkg.getProductType(),
                  cartPkg.getExtraFacilityCategories(), pkg.getListOfHighlights()).subtract(
                  priceCalculationService.calculateExtraFacilitiesTotalCost(pkg));
            setPriceDetails(pkgData, alternatePackagePrice, pkgPrice,
               PassengerUtils.getChargeablePaxCount(cartPkg.getPassengers()));
            pkgData.setSelected(false);
            pkgData.setId(pkg.getId());
            flightViewData.add(pkgData);
         }
      }
      return flightViewData;
   }

   private void setSelectedAirportToTrue(final AirportListViewData viewData, final String cartPkgId)
   {
      for (final PackageViewData pkgViewData : viewData.getFlightViewData())
      {

         if (StringUtils.equalsIgnoreCase(cartPkgId, pkgViewData.getId()))
         {
            pkgViewData.setSelected(true);
            break;
         }
      }

   }

   /**
    * Sets the price details. Performs the relative price calculation & relative per person price
    * calculation
    *
    * @param pkgData the pkg data
    * @param listedPkgPrice the listed pkg price
    * @param cartPkgPrice the cart pkg price
    * @param chargeablePaxCount the chargeable pax count
    */
   private void setPriceDetails(final PackageViewData pkgData, final BigDecimal listedPkgPrice,
      final BigDecimal cartPkgPrice, final int chargeablePaxCount)
   {
      final BigDecimal diffTotalPrice = listedPkgPrice.subtract(cartPkgPrice);
      pkgData.setCurrencyAppendedRoundUpTotalCost(CurrencyUtils
         .getCurrencyAppendedPrice(diffTotalPrice.setScale(TWO, RoundingMode.HALF_UP)));
      pkgData.setCurrencyAppendedPricePerPerson(StringUtil.append(
         calculateRate(diffTotalPrice.setScale(TWO, RoundingMode.HALF_UP), chargeablePaxCount),
         "pp"));
   }

   /**
    * Creates the package view data.
    *
    * @param pkg the itinerary
    * @return the package view data
    */
   private PackageViewData createPackageViewData(final BasePackage pkg)
   {
      final PackageViewData pkgViewData = new PackageViewData();
      final FlightViewData flViewData = new FlightViewData();
      final List<FlightViewData> flViewDataList = new ArrayList<FlightViewData>();

      populateSectors(packageComponentService.getFlightItinerary(pkg), flViewData);
      if (CollectionUtils.isNotEmpty(pkg.getListOfHighlights()))
      {
         if (pkg.getListOfHighlights().contains(HighLights.EXTRA_SPACE_SEATS))
         {
            flViewData.getHighlights().add("Extra space seats");
         }
         if (pkg.getListOfHighlights().contains(HighLights.PREMIUM_SEATS))
         {
            flViewData.getHighlights().add("Premium seats");
         }
      }
      flViewDataList.add(flViewData);
      pkgViewData.setFlightViewData(flViewDataList);
      return pkgViewData;
   }

   /**
    * @param flight
    * @param flViewData
    */
   private void populateSectors(final Itinerary flight, final FlightViewData flViewData)
   {
      for (final uk.co.tui.book.domain.lite.Leg legModel : flight.getOutBound())
      {
         flViewData.getOutboundSectors().add(createLeg(legModel));

      }
      for (final uk.co.tui.book.domain.lite.Leg legModel : flight.getInBound())
      {
         flViewData.getInboundSectors().add(createLeg(legModel));

      }
   }

   /**
    * This method will return the days between departure date and arrival data.
    *
    * @param leg LegModel
    * @return int number of days
    */
   @SuppressWarnings("deprecation")
   int getflightOffsetDays(final uk.co.tui.book.domain.lite.Leg leg)
   {
      int daysCount = 1;

      final Date departDate = leg.getSchedule().getDepartureDate();
      final Date arrivalDate = leg.getSchedule().getArrivalDate();
      // dep>arr
      LOGGER.debug("departDate:" + departDate + "arrivalDate:" + arrivalDate);
      final Calendar depCal =
         new GregorianCalendar(departDate.getYear(), departDate.getMonth(), departDate.getDate());
      final Calendar arrCal =
         new GregorianCalendar(arrivalDate.getYear(), arrivalDate.getMonth(), arrivalDate.getDate());

      final int diff = depCal.compareTo(arrCal);
      if (diff == 0)
      {
         daysCount = 0;
      }
      LOGGER.debug("daysCount:" + daysCount);
      return daysCount;
   }

   /**
    * Creates the leg.
    *
    * @param legModel the leg model
    * @return the leg
    */
   private Leg createLeg(final uk.co.tui.book.domain.lite.Leg legModel)
   {
      final Leg leg = new Leg();
      final Schedule schedule = new Schedule();

      schedule.setFlightOffsetDays(getflightOffsetDays(legModel));
      schedule.setArrivalDate(DateUtils.customFormatFlightDate(legModel.getSchedule()
         .getArrivalDate()));
      schedule.setTimeOfFlight(DateUtils.calculateTimeOfFlight(legModel.getSchedule()
         .getArrivalTime(), legModel.getSchedule().getDepartureTime()));
      schedule.setArrTime(legModel.getSchedule().getArrivalTime());
      schedule.setDepartureDate(DateUtils.customFormatFlightDate(legModel.getSchedule()
         .getDepartureDate()));
      schedule.setDepTime(legModel.getSchedule().getDepartureTime());
      schedule.setSlot();
      leg.setSchedule(schedule);
      leg.setEqmtDescription(legModel.getCarrier().getEquipementDescription());
      leg.setCarrierCode(legModel.getCarrier().getCarrierInformation().getMarketingAirlineCode());
      leg.setCarrierName(legModel.getCarrier().getCarrierInformation().getMarketingAirlineName());
      return leg;
   }

   /**
    * Gets the max airport count.
    *
    * @return the max airport count
    */
   private int getMaxAirportCount()
   {
      return bookFlowTuiConfigService.getAlternateFlightsMaxAirportTabs();
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

}
