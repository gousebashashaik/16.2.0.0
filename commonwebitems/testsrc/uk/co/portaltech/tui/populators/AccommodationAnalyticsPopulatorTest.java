/**
 *
 */
package uk.co.portaltech.tui.populators;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Test;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FlightCarrier;
import uk.co.portaltech.tui.web.view.data.RatingsData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightScheduleData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsDestinationData;
import uk.co.portaltech.tui.web.view.data.SearchResultsLocationData;
import uk.co.portaltech.tui.web.view.data.SearchResultsResortData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

@IntegrationTest
public class AccommodationAnalyticsPopulatorTest  extends ServicelayerTransactionalTest {

	@Resource
	private AccommodationAnalyticsPopulator accommodationAnalyticsPopulator;

	@Resource
	private AnalyticsHelper analyticsHelper;

	private Map<String, WebAnalytics> analyticMap;



	@Test
	public void testPopulateSearchRequestAnalyticsData() {

		accommodationAnalyticsPopulator = new AccommodationAnalyticsPopulator(analyticsHelper);

		BookFlowAccommodationViewData srd = new BookFlowAccommodationViewData();

		analyticMap = new HashMap<String, WebAnalytics>();

		accommodationAnalyticsPopulator.populate(populateBookFlowAccommodationViewData(srd), analyticMap);


		  assertThat((analyticMap.get("WhereTo")).getKey(), is("WhereTo"));
		  assertThat((analyticMap.get("WhereTo")).getValue(), is("H06012"));

		  assertThat((analyticMap.get("DepAir")).getKey(), is("DepAir"));
		  assertThat((analyticMap.get("DepAir")).getValue(), is("LUT"));

		  assertThat((analyticMap.get("DepDate")).getKey(), is("DepDate"));
		  assertThat((analyticMap.get("DepDate")).getValue(), is("01"));
		  assertThat((analyticMap.get("MonthYear")).getKey(), is("MonthYear"));
		  assertThat((analyticMap.get("MonthYear")).getValue(), is("03/2013"));

		  assertThat((analyticMap.get("Dur")).getKey(), is("Dur"));
		  assertThat((analyticMap.get("Dur")).getValue(), is("7"));

		  assertThat((analyticMap.get("LimAv")).getKey(), is("LimAv"));
		  assertThat((analyticMap.get("LimAv")).getValue(), is("4"));

		  assertThat((analyticMap.get("Sum")).getKey(), is("Sum"));
		  assertThat((analyticMap.get("Sum")).getValue(), is("1600"));

		  assertThat((analyticMap.get("Disc")).getKey(), is("Disc"));
		  assertThat((analyticMap.get("Disc")).getValue(), is("20"));

		  assertThat((analyticMap.get("Results")).getKey(), is("Results"));
		  assertThat((analyticMap.get("Results")).getValue(), is("20"));

		  assertThat((analyticMap.get("OrigPos")).getKey(), is("OrigPos"));
		  assertThat((analyticMap.get("OrigPos")).getValue(), is("5"));

		  assertThat((analyticMap.get("FinPos")).getKey(), is("FinPos"));
		  assertThat((analyticMap.get("FinPos")).getValue(), is("7"));

	}



	private BookFlowAccommodationViewData populateBookFlowAccommodationViewData(
			BookFlowAccommodationViewData srd) {
		srd.setEndecaSearchResultCount(20);
		srd.setOrigPos("5");
		srd.setFinPos("7");
		SearchResultViewData packageData = srd.getPackageData();
		SearchResultsRequestData searchRequestData = srd.getSearchRequestData();
		packageData.setDuration(7);
		populatePackageData(packageData);
		populateSearchRequestData(searchRequestData);
		return srd;
	}




	private void populateSearchRequestData(SearchResultsRequestData srd) {
		List<AirportData> airports = poupluateAirportData();
		List<UnitData> units = populateUnitData();
		String latestDepartureDate = "01-10-2012";
		int noOfAdults = 2;
		int noOfSeniors = 2;
		int noOfChildren = 2;
		List<Integer> age = new ArrayList<Integer>();
		age.add(Integer.valueOf(1));
		age.add(Integer.valueOf(10));


		srd.setAirports(airports);
		srd.setUnits(units);
		srd.setFlexibleDays(7);
		srd.setFlexibility(false);
		srd.setWhen(latestDepartureDate);
		srd.setNoOfAdults(noOfAdults);
		srd.setNoOfChildren(noOfChildren);
		srd.setInfantCount(1);
		srd.setChildCount(1);
		srd.setNoOfSeniors(noOfSeniors);
		srd.setChildrenAge(age);
		srd.setDuration(5);


	}

	List<AirportData> poupluateAirportData() {
		Collection<String> group = new ArrayList<String>();
		group.add(new String("SE"));
		group.add(new String("LN"));

		AirportData ad = new AirportData("LGW", "LONDON GATWICK");

		List<AirportData> lists = new ArrayList();

		group = new ArrayList<String>();
		group.add(new String("LUT"));
		group.add(new String("STN"));

		AirportData ad2 = new AirportData("LN", "ANY LONDON");

		lists.add((ad));
		lists.add((ad2));
		return (lists);

	}

	List<UnitData> populateUnitData() {

		UnitData ud1 = new UnitData("ESP", "Spain", "COUNTRY");
		UnitData ud2 = new UnitData("004965", "Eurosalou Hotel  Spa", "HOTEL");

		List<UnitData> lists = new ArrayList();
		lists.add((ud1));
		lists.add((ud2));
		return (lists);

	}

	/**
	 * @param packageData
	 */
	private void populatePackageData(SearchResultViewData packageData) {
		// price data
		SearchResultPriceViewData priceData = packageData.getPrice() ;
		priceData.setDepositAmount("120");
		priceData.setDiscount("20");
		priceData.setPerPerson("200");
		priceData.setTotalParty("1600");
		priceData.setLowDeposit("100");
		priceData.setLowDepositExists(true);
		priceData.setPromotionalOffer("PromotionalOffer");


		// flight data
		SearchResultFlightViewData itinerary = packageData.getItinerary();
		itinerary.setDepartureAirport("London Luton");
		itinerary.setDepartureDate("01-03-2013");
		itinerary.setFlightText("flight text");

		// outbound
		List<SearchResultFlightDetailViewData>flightsData = itinerary.getOutbounds();

		SearchResultFlightDetailViewData flightDetails  = new SearchResultFlightDetailViewData();
		flightDetails.setArrivalAirport("Glassow");
		flightDetails.setArrivalAirportCode("GLA");
		FlightCarrier   carrier = new FlightCarrier();
		carrier.setCode("0001");
		carrier.setName("taxi");
		flightDetails.setCarrier(carrier);
		flightDetails.setDepartureAirport("London Luton");
		flightDetails.setDepartureAirportCode("LUT");
		flightDetails.setHaulType("SH");
		flightDetails.setDreamLinerIndicator(false);
		SearchResultFlightScheduleData schedule = flightDetails.getSchedule();
		schedule.setArrivalDate("02-03-2013");
		schedule.setArrivalDateTimeInMilli(123323);
		schedule.setArrivalTime("22:20");
		schedule.setDepartureDate("01-03-2013");
		schedule.setDepartureDateTimeInMilli(123);
		schedule.setDepartureTime("12:30");
		schedule.setOverlapDay(true);

		flightsData.add(flightDetails);


		// inbound
		flightsData = itinerary.getInbounds();

		flightDetails = new SearchResultFlightDetailViewData();
		flightDetails.setArrivalAirport("Glassow");
		flightDetails.setArrivalAirportCode("GLA");
	    carrier = new FlightCarrier();
	    carrier.setCode("0002");
	    carrier.setName("bus");
		flightDetails.setCarrier(carrier);
		flightDetails.setDepartureAirport("London GatWick");
		flightDetails.setDepartureAirportCode("LGW");
		flightDetails.setHaulType("LH");
		flightDetails.setDreamLinerIndicator(true);
		schedule = flightDetails.getSchedule();
		schedule.setArrivalDate("02-03-2013");
		schedule.setArrivalDateTimeInMilli(123);
		schedule.setArrivalTime("21:20");
		schedule.setDepartureDate("01-03-2013");
		schedule.setDepartureDateTimeInMilli(1233);
		schedule.setDepartureTime("12:10");
		schedule.setOverlapDay(false);

		flightsData.add(flightDetails);


		// accommodation
		SearchResultAccomodationViewData accommodation = packageData.getAccommodation();
		accommodation.setCode("06012");
		accommodation.setCommercialPriority(4);
		accommodation.setDifferentiatedCode("0012");
		accommodation.setDifferentiatedProduct(false);
		accommodation.setDifferentiatedType("differentiatedType");
		accommodation.setImageUrl("imageUrl");

		SearchResultsLocationData location   = accommodation.getLocation();

		SearchResultsDestinationData destination   = location.getDestination();
		destination.setCode("123");
		destination.setName("Marrakech");

		SearchResultsResortData resort = location.getResort();
		resort.setCode("124");
		resort.setName("resort");
		location.setResort(resort);
		location.setDestination(destination);
		accommodation.setLocation(location);
		accommodation.setName("Marrakech");

		RatingsData ratings  = accommodation.getRatings();
		ratings.setOfficialRating("4");
		ratings.setTripAdvisorRating("3");
		accommodation.setRatings(ratings);

		SearchResultRoomsData roomData1  = new SearchResultRoomsData();
		roomData1.setAvailability(true);
		roomData1.setRoomType("doubleRoom");
		roomData1.setSellingout(4);
		roomData1.setBoardType("All Inclusive");
		roomData1.setRoomCode("156");

		List<SearchResultRoomsData> rooms = accommodation.getRooms();
		rooms.add(roomData1);

		accommodation.setRooms(rooms);
		accommodation.setStrapline("strapline");
		accommodation.setTripAdvisorReviewCount(2);
		accommodation.setUrl("url");
	}

}
