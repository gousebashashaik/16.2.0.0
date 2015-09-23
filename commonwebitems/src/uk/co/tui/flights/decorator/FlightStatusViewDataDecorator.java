/**
 *
 */
package uk.co.tui.flights.decorator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.comparators.sort.FlightStatusAttributesComparator;
import uk.co.tui.flights.data.Airport;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.pojo.FlightStatusAutoSuggestData;
import uk.co.tui.flights.web.view.data.FlightItemsViewData;
import uk.co.tui.flights.web.view.data.FlightStatusViewData;
import uk.co.tui.services.flightstatus.index.FlightStatusIndexService;
import uk.co.tui.services.flightstatus.pojo.FlightDetails;
import uk.co.tui.services.flightstatus.pojo.FlightStatusDetails;
import uk.co.tui.services.flightstatus.util.FlightStatusUtil;


/**
 *
 *
 */
public class FlightStatusViewDataDecorator
{

	@Resource
	private FlightStatusIndexService flightStatusIndexService;

	private Map<String, Airport> airportData;

	private FlightStatusViewData flightStatusViewData;

	private static final String STRING_RIGHT_PARANTHESIS = ")";
	private static final String STRING_LEFT_PARANTHESIS = "(";
	private static final String STRING_COMMA = ",";
	private static final String STRING_SPACE = " ";
	private static final String ARRIVING_TO = "arriveTo";
	private static final String DEPARTURE_FROM = "departFrom";
	private static final String FLIGHT_NUMBER = "flightnumber";

	private static final String UK = "GBR";


	private static final String ARRIVAL_DATE_ASCENDING = "ARRIVAL_DATE_ASCENDING";
	private static final String DEPARTURE_DATE_ASCENDING = "DEPARTURE_DATE_ASCENDING";
	private static final String DEPARTURE_AIRPORTINFO_ASCENDING = "DEPARTURE_AIRPORTINFO_ASCENDING";
	private static final String ARRIVAL_AIRPORTINFO_ASCENDING = "ARRIVAL_AIRPORTINFO_ASCENDING";
	private static final String AIRPORTNAME_ASCENDING = "AIRPORTNAME_ASCENDING";
	private static final String FLIGHT_NUMBER_ASCENDING = "FLIGHT_NUMBER_ASCENDING";


	/**
	 * This method decorate flight search by number
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @param eventType
	 * @return FlightStatusViewData
	 * @throws ParseException
	 */

	public FlightStatusViewData decorateSearchByFlightNo(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights, final String eventType) throws ParseException
	{
		airportData = getAirportData;
		flightStatusViewData = new FlightStatusViewData();
		List<FlightStatusDetails> todaysFlightDetails = new ArrayList<FlightStatusDetails>();
		List<FlightStatusDetails> tomorrowFlightDetails = new ArrayList<FlightStatusDetails>();
		Set<FlightStatusAutoSuggestData> departureAutoSuggestData = new HashSet<FlightStatusAutoSuggestData>();
		Set<FlightStatusAutoSuggestData> arrivalAutoSuggestData = new HashSet<FlightStatusAutoSuggestData>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			todaysFlightDetails = checkTodayFlightDates(flightStatusDetails, eventType);
			tomorrowFlightDetails = checkTomorrowFlightDates(flightStatusDetails, eventType);
			departureAutoSuggestData = populateAutoSuggestData(departureAutoSuggestData, flightStatusDetails.getSchdepdt(),
					flightStatusDetails.getSchdepap(), DEPARTURE_FROM);
			arrivalAutoSuggestData = populateAutoSuggestData(arrivalAutoSuggestData, flightStatusDetails.getScharrdt(),
					flightStatusDetails.getScharrap(), ARRIVING_TO);
		}
		addListToViewData(todaysFlightDetails, tomorrowFlightDetails, eventType);
		addAutoSuggestListtoViewData(DEPARTURE_FROM, departureAutoSuggestData);
		addAutoSuggestListtoViewData(ARRIVING_TO, arrivalAutoSuggestData);
		return flightStatusViewData;
	}

	private List<FlightStatusDetails> checkTodayFlightDates(final FlightStatusDetails flightStatusDetails, final String eventType)
			throws ParseException
	{
		List<FlightStatusDetails> flightDetailsList = new ArrayList<FlightStatusDetails>();
		final Date dateTime = new SimpleDateFormat("yyMMdd").parse(flightStatusDetails.getSchdepdt());
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		final Calendar today = Calendar.getInstance();
		if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
				&& calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
		{
			flightDetailsList = populateFlightStatusData(FlightStatusUtil.getCurrentDate(), flightStatusDetails, flightDetailsList,
					eventType);
		}
		return flightDetailsList;
	}

	private List<FlightStatusDetails> checkTomorrowFlightDates(final FlightStatusDetails flightStatusDetails,
			final String eventType) throws ParseException
	{
		List<FlightStatusDetails> flightDetailsList = new ArrayList<FlightStatusDetails>();
		final Date dateTime = new SimpleDateFormat("yyMMdd").parse(flightStatusDetails.getSchdepdt());
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		final Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, 1);
		if (calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR)
				&& calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR))
		{
			flightDetailsList = populateFlightStatusData(FlightStatusUtil.getTomorrowDate(), flightStatusDetails, flightDetailsList,
					eventType);
		}
		return flightDetailsList;
	}

	/**
	 * This method decorate flight search by Airport
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @param fieldEventType
	 * @param autoSuggestEventType
	 * @return FlightStatusViewData
	 */
	public FlightStatusViewData decorateSearchByAirports(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights, final String fieldEventType, final String autoSuggestEventType)
	{
		airportData = getAirportData;
		flightStatusViewData = new FlightStatusViewData();
		List<FlightStatusDetails> todaysFlightDetails = new ArrayList<FlightStatusDetails>();
		List<FlightStatusDetails> tomorrowFlightDetails = new ArrayList<FlightStatusDetails>();
		Set<FlightStatusAutoSuggestData> autoSuggestData = new HashSet<FlightStatusAutoSuggestData>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			todaysFlightDetails = populateFlightStatusData(FlightStatusUtil.getCurrentDate(), flightStatusDetails,
					todaysFlightDetails, fieldEventType);
			tomorrowFlightDetails = populateFlightStatusData(FlightStatusUtil.getTomorrowDate(), flightStatusDetails,
					tomorrowFlightDetails, fieldEventType);
			if (autoSuggestEventType.equals(DEPARTURE_FROM))
			{
				autoSuggestData = populateAutoSuggestData(autoSuggestData, flightStatusDetails.getSchdepdt(),
						flightStatusDetails.getSchdepap(), autoSuggestEventType);
			}
			else if (autoSuggestEventType.equals(ARRIVING_TO))
			{
				autoSuggestData = populateAutoSuggestData(autoSuggestData, flightStatusDetails.getScharrdt(),
						flightStatusDetails.getScharrap(), autoSuggestEventType);
			}
		}

		addListToViewData(todaysFlightDetails, tomorrowFlightDetails, fieldEventType);
		addAutoSuggestListtoViewData(autoSuggestEventType, autoSuggestData);
		return flightStatusViewData;
	}

	/**
	 * This method decorate flight search by Airport
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @param fieldEventType
	 * @param autoSuggestEventType
	 * @return FlightStatusViewData
	 */
	public FlightItemsViewData decorateSearchByAirportsAutoSuggest(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights, final String fieldEventType, final String autoSuggestEventType)
	{
		airportData = getAirportData;
		final FlightItemsViewData itemsViewData = new FlightItemsViewData();
		List<FlightStatusDetails> todaysFlightDetails = new ArrayList<FlightStatusDetails>();
		List<FlightStatusDetails> tomorrowFlightDetails = new ArrayList<FlightStatusDetails>();
		Set<FlightStatusAutoSuggestData> autoSuggestData = new HashSet<FlightStatusAutoSuggestData>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			todaysFlightDetails = populateFlightStatusData(FlightStatusUtil.getCurrentDate(), flightStatusDetails,
					todaysFlightDetails, fieldEventType);
			tomorrowFlightDetails = populateFlightStatusData(FlightStatusUtil.getTomorrowDate(), flightStatusDetails,
					tomorrowFlightDetails, fieldEventType);
			if (autoSuggestEventType.equals(DEPARTURE_FROM))
			{
				autoSuggestData = populateAutoSuggestData(autoSuggestData, flightStatusDetails.getSchdepdt(),
						flightStatusDetails.getSchdepap(), autoSuggestEventType);
			}
			else if (autoSuggestEventType.equals(ARRIVING_TO))
			{
				autoSuggestData = populateAutoSuggestData(autoSuggestData, flightStatusDetails.getSchdepdt(),
						flightStatusDetails.getScharrap(), autoSuggestEventType);
			}
		}
		addAutoSuggestListtoViewData(autoSuggestEventType, autoSuggestData, itemsViewData);
		return itemsViewData;
	}

	/**
	 * @param autoSuggestEventType
	 * @param autoSuggestData
	 */
	private void addAutoSuggestListtoViewData(final String autoSuggestEventType,
			final Set<FlightStatusAutoSuggestData> autoSuggestData)
	{
		if (autoSuggestEventType.equals(DEPARTURE_FROM))
		{

			final List<FlightStatusAutoSuggestData> departureAutoSuggestList = new ArrayList<FlightStatusAutoSuggestData>(
					autoSuggestData);
			Collections.sort(departureAutoSuggestList,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_AIRPORTINFO_ASCENDING)));
			flightStatusViewData.setDepartureAutoSuggestData(departureAutoSuggestList);
		}
		else if (autoSuggestEventType.equals(ARRIVING_TO))
		{
			final List<FlightStatusAutoSuggestData> arrivalAutoSuggestList = new ArrayList<FlightStatusAutoSuggestData>(
					autoSuggestData);
			Collections.sort(arrivalAutoSuggestList,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_AIRPORTINFO_ASCENDING)));
			flightStatusViewData.setArrivalAutoSuggestData(arrivalAutoSuggestList);
		}

	}

	/**
	 * This method decorate flight search by all departures
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @return FlightStatusViewData
	 */

	public FlightStatusViewData decorateAllDepartures(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights)
	{
		airportData = getAirportData;
		flightStatusViewData = new FlightStatusViewData();
		List<FlightStatusDetails> todaysFlightDetails = new ArrayList<FlightStatusDetails>();
		List<FlightStatusDetails> tomorrowFlightDetails = new ArrayList<FlightStatusDetails>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			todaysFlightDetails = populateFlightStatusData(FlightStatusUtil.getCurrentDate(), flightStatusDetails,
					todaysFlightDetails, DEPARTURE_FROM);
			tomorrowFlightDetails = populateFlightStatusData(FlightStatusUtil.getTomorrowDate(), flightStatusDetails,
					tomorrowFlightDetails, DEPARTURE_FROM);
		}

		Collections.sort(todaysFlightDetails,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_DATE_ASCENDING)));
		Collections.sort(tomorrowFlightDetails,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_DATE_ASCENDING)));

		flightStatusViewData.setTodaysFlightStatusData(todaysFlightDetails);
		flightStatusViewData.setTomorrowsFlightStatusData(tomorrowFlightDetails);
		flightStatusViewData.setCurrentFeedtime(flightStatusIndexService.getUpdatedFeedTime());
		return flightStatusViewData;
	}

	/**
	 * This method decorate flight search by all Arrivals
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @return FlightStatusViewData
	 */
	public FlightStatusViewData decorateAllArrivals(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights)
	{
		airportData = getAirportData;
		flightStatusViewData = new FlightStatusViewData();

		List<FlightStatusDetails> todaysFlightDetails = new ArrayList<FlightStatusDetails>();
		List<FlightStatusDetails> tomorrowFlightDetails = new ArrayList<FlightStatusDetails>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			todaysFlightDetails = populateFlightStatusData(FlightStatusUtil.getCurrentDate(), flightStatusDetails,
					todaysFlightDetails, ARRIVING_TO);
			tomorrowFlightDetails = populateFlightStatusData(FlightStatusUtil.getTomorrowDate(), flightStatusDetails,
					tomorrowFlightDetails, ARRIVING_TO);
		}

		Collections.sort(todaysFlightDetails,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_DATE_ASCENDING)));
		Collections.sort(tomorrowFlightDetails,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_DATE_ASCENDING)));

		flightStatusViewData.setTodaysFlightStatusData(todaysFlightDetails);
		flightStatusViewData.setTomorrowsFlightStatusData(tomorrowFlightDetails);
		flightStatusViewData.setCurrentFeedtime(flightStatusIndexService.getUpdatedFeedTime());
		return flightStatusViewData;
	}

	/**
	 * This method decorate flight search by departure auto suggest
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @return FlightStatusViewData
	 */

	public FlightItemsViewData decoratedeptAutoSuggData(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights)
	{
		airportData = getAirportData;
		final FlightItemsViewData itemsViewData = new FlightItemsViewData();
		Set<FlightStatusAutoSuggestData> departureAutoSuggestData = new HashSet<FlightStatusAutoSuggestData>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			if (isValidRoute(flightStatusDetails.getSchdepap(), flightStatusDetails.getScharrap()))
			{
				departureAutoSuggestData = populateAutoSuggestData(departureAutoSuggestData, flightStatusDetails.getSchdepdt(),
						flightStatusDetails.getSchdepap(), DEPARTURE_FROM);
			}
		}
		final List<FlightStatusAutoSuggestData> departureAutoSuggestList = new ArrayList<FlightStatusAutoSuggestData>(
				departureAutoSuggestData);
		Collections.sort(departureAutoSuggestList,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_AIRPORTINFO_ASCENDING)));

		itemsViewData.setItems(departureAutoSuggestList);

		return itemsViewData;
	}

	/**
	 * This method decorate flight search by Arrivals auto suggest
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @return FlightStatusViewData
	 */
	public FlightItemsViewData decorateArrAutoSuggest(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights)
	{
		airportData = getAirportData;
		final FlightItemsViewData itemsViewData = new FlightItemsViewData();
		Set<FlightStatusAutoSuggestData> arrivalAutoSuggestData = new HashSet<FlightStatusAutoSuggestData>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			if (isValidRoute(flightStatusDetails.getSchdepap(), flightStatusDetails.getScharrap()))
			{
				arrivalAutoSuggestData = populateAutoSuggestData(arrivalAutoSuggestData, flightStatusDetails.getScharrdt(),
						flightStatusDetails.getScharrap(), ARRIVING_TO);
			}
		}
		final List<FlightStatusAutoSuggestData> arrivalAutoSuggestList = new ArrayList<FlightStatusAutoSuggestData>(
				arrivalAutoSuggestData);
		Collections.sort(arrivalAutoSuggestList,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_AIRPORTINFO_ASCENDING)));
		itemsViewData.setItems(arrivalAutoSuggestList);

		return itemsViewData;
	}

	/**
	 * This method decorate flight search by multiple fields
	 *
	 * @param getAirportData
	 * @param listOfFlights
	 * @param evntType
	 * @return FlightStatusViewData
	 */
	public FlightStatusViewData decorateSearchByMultipleFields(final Map<String, Airport> getAirportData,
			final List<FlightStatusDetails> listOfFlights, final String evntType)
	{
		airportData = getAirportData;
		flightStatusViewData = new FlightStatusViewData();
		List<FlightStatusDetails> todaysFlightDetails = new ArrayList<FlightStatusDetails>();
		List<FlightStatusDetails> tomorrowFlightDetails = new ArrayList<FlightStatusDetails>();

		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			todaysFlightDetails = populateFlightStatusData(FlightStatusUtil.getCurrentDate(), flightStatusDetails,
					todaysFlightDetails, evntType);
			tomorrowFlightDetails = populateFlightStatusData(FlightStatusUtil.getTomorrowDate(), flightStatusDetails,
					tomorrowFlightDetails, evntType);
		}

		if (evntType.equalsIgnoreCase(DEPARTURE_FROM))
		{
			Collections.sort(todaysFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_DATE_ASCENDING)));
			Collections.sort(tomorrowFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_DATE_ASCENDING)));
		}
		else if (evntType.equalsIgnoreCase(ARRIVING_TO))
		{
			Collections.sort(todaysFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_DATE_ASCENDING)));
			Collections.sort(tomorrowFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_DATE_ASCENDING)));
		}

		flightStatusViewData.setTodaysFlightStatusData(todaysFlightDetails);
		flightStatusViewData.setTomorrowsFlightStatusData(tomorrowFlightDetails);
		flightStatusViewData.setCurrentFeedtime(flightStatusIndexService.getUpdatedFeedTime());
		return flightStatusViewData;
	}


	public FlightItemsViewData decorateSearchByFlightNumbers(final List<FlightStatusDetails> listOfFlights)
	{
		final FlightItemsViewData flightItemsViewData = new FlightItemsViewData();
		final List<FlightDetails> list = new ArrayList<FlightDetails>();

		final List<String> validFlights = new ArrayList<String>();
		for (final FlightStatusDetails flightStatusDetails : listOfFlights)
		{
			final FlightDetails details = new FlightDetails();
			if (StringUtils.isNotEmpty(flightStatusDetails.getFlightshortno())
					&& !validFlights.contains(flightStatusDetails.getFlightshortno()))
			{

				validFlights.add(flightStatusDetails.getFlightshortno());
				details.setFlightnr(flightStatusDetails.getFlightnr());
				details.setFlightshortno(flightStatusDetails.getFlightshortno());
				details.setCarriercode(flightStatusDetails.getCarriercode());
				list.add(details);
			}

		}

		Collections.sort(list,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(FLIGHT_NUMBER_ASCENDING)));

		final List<FlightDetails> resultFlights = decorateFlightAutoSuggest(list);

		flightItemsViewData.setItems(resultFlights);


		return flightItemsViewData;
	}


	private List<FlightDetails> decorateFlightAutoSuggest(final List<FlightDetails> list)
	{
		final List<FlightDetails> sortedFlights = new ArrayList<FlightDetails>();


		for (final FlightDetails details : list)
		{
			final FlightDetails flightDetails = new FlightDetails();
			flightDetails.setFlightnr(details.getCarriercode() + STRING_SPACE + details.getFlightshortno());
			sortedFlights.add(flightDetails);
		}

		return sortedFlights;
	}


	/**
	 * This method checks whether departure or arrival airport codes belongs to UK. If both belongs to non-UK, will
	 * ignore it.
	 *
	 * @param schDepAp
	 * @param schArrAp
	 * @return boolean
	 */
	private boolean isValidRoute(final String schDepAp, final String schArrAp)
	{
		boolean isValidRoute = false;

		if (verifyScheduledAirports(schDepAp, schArrAp))
		{
			final Airport departAirportObj = airportData.get(schDepAp);
			final Airport arrivalAirportObj = airportData.get(schArrAp);

			if (departAirportObj.getCountryCode().equalsIgnoreCase(UK) || arrivalAirportObj.getCountryCode().equalsIgnoreCase(UK))
			{
				isValidRoute = true;
			}
		}
		return isValidRoute;
	}

	/**
	 * @param schDepAp
	 * @param schArrAp
	 * @return
	 */
	private boolean verifyScheduledAirports(final String schDepAp, final String schArrAp)
	{
		return airportData.containsKey(schDepAp) && airportData.containsKey(schArrAp);
	}

	/**
	 * This method provides concatenated airport name with airport code and country
	 *
	 * @param flightStatusAutoSuggestData
	 * @param date
	 * @param airportCode
	 * @param eventType
	 * @return Set<FlightStatusAutoSuggestData>
	 */
	private Set<FlightStatusAutoSuggestData> populateAutoSuggestData(
			final Set<FlightStatusAutoSuggestData> flightStatusAutoSuggestData, final String date, final String airportCode,
			final String eventType)
	{
		if (verifyDatesAndAirportCode(date, airportCode))
		{
			final FlightStatusAutoSuggestData autoSuggestDtls = new FlightStatusAutoSuggestData();
			final Airport airPortData = airportData.get(airportCode);
			final StringBuilder ukAirportInfo = new StringBuilder().append(airPortData.getName()).append(STRING_SPACE)
					.append(STRING_LEFT_PARANTHESIS).append(airPortData.getCode()).append(STRING_RIGHT_PARANTHESIS);

			final StringBuilder overseasAirportInfo = new StringBuilder().append(airPortData.getName()).append(STRING_SPACE)
					.append(STRING_LEFT_PARANTHESIS).append(airPortData.getCode()).append(STRING_RIGHT_PARANTHESIS)
					.append(STRING_COMMA).append(STRING_SPACE).append(airPortData.getCountryName());

			if (eventType.equalsIgnoreCase(ARRIVING_TO))
			{
				autoSuggestDtls.setArrAirpotCode(airPortData.getCode());
				checkCountryCodeForArr(autoSuggestDtls, airPortData, ukAirportInfo, overseasAirportInfo);
				autoSuggestDtls.setArrAirPortCountryCode(airPortData.getCountryCode());
			}
			else if (eventType.equalsIgnoreCase(DEPARTURE_FROM))
			{
				autoSuggestDtls.setDepAirpotCode(airPortData.getCode());
				checkCountryCodeForDep(autoSuggestDtls, airPortData, ukAirportInfo, overseasAirportInfo);
				autoSuggestDtls.setDepAirPortCountryCode(airPortData.getCountryCode());
			}
			flightStatusAutoSuggestData.add(autoSuggestDtls);
		}

		return flightStatusAutoSuggestData;
	}

	/**
	 * @param autoSuggestDtls
	 * @param airPortData
	 * @param ukAirportInfo
	 * @param overseasAirportInfo
	 */
	private void checkCountryCodeForDep(final FlightStatusAutoSuggestData autoSuggestDtls, final Airport airPortData,
			final StringBuilder ukAirportInfo, final StringBuilder overseasAirportInfo)
	{
		if (UK.equals(airPortData.getCountryCode()))
		{
			autoSuggestDtls.setDepAirPortInfo(ukAirportInfo.toString());
		}
		else
		{
			autoSuggestDtls.setDepAirPortInfo(overseasAirportInfo.toString());
		}
	}

	/**
	 * @param autoSuggestDtls
	 * @param airPortData
	 * @param ukAirportInfo
	 * @param overseasAirportInfo
	 */
	private void checkCountryCodeForArr(final FlightStatusAutoSuggestData autoSuggestDtls, final Airport airPortData,
			final StringBuilder ukAirportInfo, final StringBuilder overseasAirportInfo)
	{
		if (UK.equals(airPortData.getCountryCode()))
		{
			autoSuggestDtls.setArrAirPortInfo(ukAirportInfo.toString());
		}
		else
		{
			autoSuggestDtls.setArrAirPortInfo(overseasAirportInfo.toString());
		}
	}

	/**
	 * This method provides addListToViewData
	 *
	 * @param todaysFlightDetails
	 * @param tomorrowFlightDetails
	 * @param fieldEventType
	 */
	private void addListToViewData(final List<FlightStatusDetails> todaysFlightDetails,
			final List<FlightStatusDetails> tomorrowFlightDetails, final String fieldEventType)
	{

		if (fieldEventType.equals(DEPARTURE_FROM) || fieldEventType.equals(FLIGHT_NUMBER))
		{
			Collections.sort(todaysFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_DATE_ASCENDING)));
			Collections.sort(tomorrowFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_DATE_ASCENDING)));

			flightStatusViewData.setTodaysFlightStatusData(todaysFlightDetails);
			flightStatusViewData.setTomorrowsFlightStatusData(tomorrowFlightDetails);
		}
		else if (fieldEventType.equals(ARRIVING_TO))
		{
			Collections.sort(todaysFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_DATE_ASCENDING)));
			Collections.sort(tomorrowFlightDetails,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_DATE_ASCENDING)));

			flightStatusViewData.setTodaysFlightStatusData(todaysFlightDetails);
			flightStatusViewData.setTomorrowsFlightStatusData(tomorrowFlightDetails);
		}
		flightStatusViewData.setCurrentFeedtime(flightStatusIndexService.getUpdatedFeedTime());

	}

	/**
	 * This method used to add auto suggestion data to flightStatusViewData object
	 *
	 * @param autoSuggestEventType
	 * @param autoSuggestData
	 */
	private void addAutoSuggestListtoViewData(final String autoSuggestEventType,
			final Set<FlightStatusAutoSuggestData> autoSuggestData, final FlightItemsViewData itemsViewData)
	{
		if (autoSuggestEventType.equals(DEPARTURE_FROM))
		{
			final List<FlightStatusAutoSuggestData> departureAutoSuggestList = new ArrayList<FlightStatusAutoSuggestData>(
					autoSuggestData);
			Collections.sort(departureAutoSuggestList,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(DEPARTURE_AIRPORTINFO_ASCENDING)));

			itemsViewData.setItems(departureAutoSuggestList);
		}
		else if (autoSuggestEventType.equals(ARRIVING_TO))
		{
			final List<FlightStatusAutoSuggestData> arrivalAutoSuggestList = new ArrayList<FlightStatusAutoSuggestData>(
					autoSuggestData);
			Collections.sort(arrivalAutoSuggestList,
					FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(ARRIVAL_AIRPORTINFO_ASCENDING)));

			itemsViewData.setItems(arrivalAutoSuggestList);
		}
	}

	/**
	 *
	 * @param comperatorCode
	 * @return List<FlightDealsSortandPagination>
	 */
	private List<FlightStatusAttributesComparator> getFlightStatusComparatorList(final String comperatorCode)
	{
		final List<FlightStatusAttributesComparator> comparators = new ArrayList<FlightStatusAttributesComparator>();
		comparators.add(FlightStatusAttributesComparator.valueOf(comperatorCode));
		return comparators;
	}

	/**
	 * This method populates flight status data required for today & tomorrow
	 *
	 * @param date
	 *           or tomorrow's date as String
	 * @param extractedFlightStatusDetails
	 * @param flightDetailsList
	 * @param eventType
	 * @return List<FlightStatusDetails>
	 */
	private List<FlightStatusDetails> populateFlightStatusData(final String date,
			final FlightStatusDetails extractedFlightStatusDetails, final List<FlightStatusDetails> flightDetailsList,
			final String eventType)
	{
		if (isDeptArrivalDatesMatched(date, extractedFlightStatusDetails, eventType)
				&& isValidRoute(extractedFlightStatusDetails.getSchdepap(), extractedFlightStatusDetails.getScharrap()))
		{
			flightDetailsList.add(buildAirportInfo(extractedFlightStatusDetails));
		}
		return flightDetailsList;
	}

	/**
	 * This method assign the all Departure flight details to Flight details
	 *
	 * @param flightDtls
	 *
	 * @return FlightStatusDetails
	 *
	 */
	private FlightStatusDetails buildAirportInfo(final FlightStatusDetails flightDtls)
	{
		if (airportData.containsKey(flightDtls.getSchdepap()))
		{
			final Airport airPortData = airportData.get(flightDtls.getSchdepap());
			flightDtls.setDepAirportName(airPortData.getName());
			flightDtls.setDepAirportCode(airPortData.getCode());
			flightDtls.setDepAirCountryName(airPortData.getCountryName());
			flightDtls.setDepAirCntryCode(airPortData.getCountryCode());
		}
		if (airportData.containsKey(flightDtls.getScharrap()))
		{
			final Airport airPortData = airportData.get(flightDtls.getScharrap());
			flightDtls.setArrAirportName(airPortData.getName());
			flightDtls.setArrAirportCode(airPortData.getCode());
			flightDtls.setArrAirCountryName(airPortData.getCountryName());
			flightDtls.setArrAirCntryCode(airPortData.getCountryCode());
		}
		return flightDtls;
	}

	/**
	 * This method checks whether departure and arrival dates matched with today & tomorrow's dates
	 *
	 * @param date
	 * @param extractedFlightStatusDetails
	 * @param eventType
	 * @return boolean
	 */
	private boolean isDeptArrivalDatesMatched(final String date, final FlightStatusDetails extractedFlightStatusDetails,
			final String eventType)
	{
		boolean isDateMatched = false;
		if (checkDepartureEventType(date, extractedFlightStatusDetails, eventType))
		{
			isDateMatched = true;
		}
		else if (checkArrivingToEventType(date, extractedFlightStatusDetails, eventType))
		{
			isDateMatched = true;
		}
		else if (checkFlightNumberEventType(date, extractedFlightStatusDetails, eventType))
		{
			isDateMatched = true;
		}
		return isDateMatched;
	}

	/**
	 * @param date
	 * @param extractedFlightStatusDetails
	 * @param eventType
	 * @return
	 */
	private boolean checkArrivingToEventType(final String date, final FlightStatusDetails extractedFlightStatusDetails,
			final String eventType)
	{
		return eventType.equalsIgnoreCase(ARRIVING_TO) && date.equalsIgnoreCase(extractedFlightStatusDetails.getScharrdt());
	}

	/**
	 * @param date
	 * @param extractedFlightStatusDetails
	 * @param eventType
	 * @return
	 */
	private boolean checkFlightNumberEventType(final String date, final FlightStatusDetails extractedFlightStatusDetails,
			final String eventType)
	{
		return eventType.equalsIgnoreCase(FLIGHT_NUMBER)
				&& (date.equalsIgnoreCase(extractedFlightStatusDetails.getSchdepdt()) || date
						.equalsIgnoreCase(extractedFlightStatusDetails.getScharrdt()));
	}

	/**
	 * @param date
	 * @param extractedFlightStatusDetails
	 * @param eventType
	 * @return
	 */
	private boolean checkDepartureEventType(final String date, final FlightStatusDetails extractedFlightStatusDetails,
			final String eventType)
	{
		return eventType.equalsIgnoreCase(DEPARTURE_FROM) && date.equalsIgnoreCase(extractedFlightStatusDetails.getSchdepdt());
	}

	/**
	 * @param date
	 * @param airportCode
	 * @return
	 */
	private boolean verifyDatesAndAirportCode(final String date, final String airportCode)
	{
		return (FlightStatusUtil.getCurrentDate().equalsIgnoreCase(date) || FlightStatusUtil.getTomorrowDate().equalsIgnoreCase(
				date))
				&& airportData.containsKey(airportCode);
	}

	public Map<String, List<AirportData>> getAutoSuggestedDecorator(final List<FlightStatusDetails> listOfFlights,
			final String event, final Map<String, Airport> validAirportData)
	{

		final List<AirportData> ukAirportList = new ArrayList<AirportData>();
		final List<AirportData> osAirportList = new ArrayList<AirportData>();
		final List<String> validList = new ArrayList<String>();
		final Map<String, List<AirportData>> suggestedMap = new HashMap<String, List<AirportData>>();

		if ("departure".equals(event))
		{
			for (final FlightStatusDetails details : listOfFlights)
			{
				final AirportData ukData = new AirportData();
				final AirportData osData = new AirportData();

				validateDepartures(ukAirportList, osAirportList, validList, details, ukData, osData, validAirportData);

			}
		}
		else
		{
			for (final FlightStatusDetails details : listOfFlights)
			{
				final AirportData ukData = new AirportData();
				final AirportData osData = new AirportData();

				validateArrivals(ukAirportList, osAirportList, validList, details, ukData, osData, validAirportData);

			}
		}

		Collections.sort(ukAirportList,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(AIRPORTNAME_ASCENDING)));
		Collections.sort(osAirportList,
				FlightStatusAttributesComparator.getComparator(getFlightStatusComparatorList(AIRPORTNAME_ASCENDING)));

		suggestedMap.put("ukAirports", ukAirportList);
		suggestedMap.put("osAirports", osAirportList);

		return suggestedMap;
	}

	/**
	 * @param ukAirportList
	 * @param osAirportList
	 * @param validList
	 * @param details
	 * @param ukData
	 * @param osData
	 */
	private void validateArrivals(final List<AirportData> ukAirportList, final List<AirportData> osAirportList,
			final List<String> validList, final FlightStatusDetails details, final AirportData ukData, final AirportData osData,
			final Map<String, Airport> validAirportData)
	{
		if (!validList.contains(details.getScharrap()) && validAirportData.containsKey(details.getScharrap()))
		{
			validList.add(details.getScharrap());
			checkScharrcoutrycode(ukAirportList, osAirportList, details, ukData, osData);
		}
	}

	/**
	 * @param ukAirportList
	 * @param osAirportList
	 * @param validList
	 * @param details
	 * @param ukData
	 * @param osData
	 */
	private void validateDepartures(final List<AirportData> ukAirportList, final List<AirportData> osAirportList,
			final List<String> validList, final FlightStatusDetails details, final AirportData ukData, final AirportData osData,
			final Map<String, Airport> validAirportData)
	{
		if (!validList.contains(details.getSchdepap()) && validAirportData.containsKey(details.getSchdepap()))
		{
			validList.add(details.getSchdepap());
			checkSchdepcoutrycode(ukAirportList, osAirportList, details, ukData, osData);
		}
	}

	/**
	 * @param ukAirportList
	 * @param osAirportList
	 * @param details
	 * @param ukData
	 * @param osData
	 */
	private void checkScharrcoutrycode(final List<AirportData> ukAirportList, final List<AirportData> osAirportList,
			final FlightStatusDetails details, final AirportData ukData, final AirportData osData)
	{
		if (UK.equals(details.getScharrapcountrycode()))
		{
			ukData.setId(details.getScharrap());
			ukData.setName(details.getScharrapname());
			ukData.setCountryCode(details.getScharrapcountrycode());
			ukData.setCountryName(details.getScharrapcountry());
			ukAirportList.add(ukData);
		}
		else
		{
			osData.setId(details.getScharrap());
			osData.setName(details.getScharrapname());
			osData.setCountryCode(details.getScharrapcountrycode());
			osData.setCountryName(details.getScharrapcountry());
			osAirportList.add(osData);
		}
	}

	/**
	 * @param ukAirportList
	 * @param osAirportList
	 * @param details
	 * @param ukData
	 * @param osData
	 */
	private void checkSchdepcoutrycode(final List<AirportData> ukAirportList, final List<AirportData> osAirportList,
			final FlightStatusDetails details, final AirportData ukData, final AirportData osData)
	{
		if (UK.equals(details.getSchdepapcountrycode()))
		{
			ukData.setId(details.getSchdepap());
			ukData.setName(details.getSchdepapname());
			ukData.setCountryCode(details.getSchdepapcountrycode());
			ukData.setCountryName(details.getSchdepapcountry());
			ukAirportList.add(ukData);
		}
		else
		{
			osData.setId(details.getSchdepap());
			osData.setName(details.getSchdepapname());
			osData.setCountryCode(details.getSchdepapcountrycode());
			osData.setCountryName(details.getSchdepapcountry());
			osAirportList.add(osData);
		}
	}


}
