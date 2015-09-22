/**
 *
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uk.co.portaltech.travel.model.results.Accomodation;
import uk.co.portaltech.travel.model.results.Airport;
import uk.co.portaltech.travel.model.results.Flight;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.Itinerary;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.Schedule;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightScheduleData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@UnitTest
public class HolidaySearchResultsPopulatorTest {


	private HolidaySearchResultsPopulator populator;
	private Holiday holiday;
	private SearchResultViewData resultsview;

	@Before
	public void setUp() {
		resultsview = new SearchResultViewData();
		populator = new HolidaySearchResultsPopulator();
		populateHoliday();

	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulateFlightData() {

	}
	/**
	 * data population
	 */
	private void populateHoliday() {
		holiday = new Holiday();

		populatePrice(holiday);
		holiday.setPackageId("X201");
		holiday.setOfficialRating("5");
		Accomodation accomodation = new Accomodation();
		populateAccommodation(accomodation);

		holiday.setAccomodation(accomodation);
		Itinerary itinerary = new Itinerary();
		populateItineary(itinerary);
		holiday.setItinerary(itinerary);

	}

	@SuppressWarnings("boxing")
	public SearchResultFlightScheduleData popluatetScheduleData(String departureDate,
			String departureTime, String arrivalDate, String arrivalTime,
			Boolean overlapDay) {
		SearchResultFlightScheduleData schedule = new SearchResultFlightScheduleData();
		schedule.setDepartureDate(departureDate);
		schedule.setDepartureTime(departureTime);
		schedule.setArrivalDate(arrivalDate);
		schedule.setArrivalTime(arrivalTime);
		schedule.setOverlapDay(overlapDay);
		return schedule;
	}

	/**
	 *
	 */
	private void populateItineary(Itinerary itinerary) {
		List<Leg> inbound = new ArrayList<Leg>();
		List<Leg> outbound = new ArrayList<Leg>();

		itinerary.setDepartureAirport("London Gatwick");
		itinerary.setDepartureTime(new DateTime(2013, 1, 2, 16, 16, 2, 2));
		outbound.add(populateDepartLegHop1());
		outbound.add(populateDepartLegHop2());
		inbound.add(populatArrivalLegHop1());
		inbound.add(populateArrivalLegHop2());
		itinerary.setOutbound(outbound);
		itinerary.setInbound(inbound);

	}

	private Leg populateDepartLegHop1() {
		Leg legs = new Leg();

		legs.setDeparture(new Airport("sfb", "Florida"));
		legs.setArrival(new Airport("lgw", "London Gatwick"));
		legs.setCarrier(populateCarrier("Thomson Airways", "TOM0123"));
		return legs;
	}

	private Leg populateDepartLegHop2() {
		Leg legs = new Leg();

		legs.setDeparture(new Airport("lgw", "London Gatwick"));
		legs.setArrival(new Airport("man", "Manchester"));
		legs.setCarrier(populateCarrier("Thomson Airways", "TOM0124"));

		return legs;
	}

	private Leg populatArrivalLegHop1() {
		Leg legs = new Leg();

		legs.setDeparture(new Airport("man", "Manchester"));
		legs.setArrival(new Airport("sfb", "Florida"));
		legs.setCarrier(populateCarrier("Thomson Airways", "TOM0321"));

		return legs;
	}

	private Leg populateArrivalLegHop2() {
		Leg legs = new Leg();

		legs.setDeparture(new Airport("sfb", "Florida"));
		legs.setArrival(new Airport("lgw", "London Gatwick"));
		legs.setCarrier(populateCarrier("Thomson Airways", "TOM0323"));

		return legs;
	}

	private	Flight populateCarrier(String carrierName, String code) {
		Flight carrier = new Flight();
		carrier.setCarrier(carrierName);
		carrier.setCode(code);
		return carrier;

	}

	/**
	 * @param holiday
	 */
	private void populatePrice(Holiday holiday) {
		holiday.setDepositAmount(new BigDecimal(5000));
		holiday.setDiscount("200");
		holiday.setOfficialRating("4");
		holiday.setPerPersonPrice("100");
		holiday.setPrice("2500");
		holiday.setPromotionalOffer("20");
		holiday.setTotalPrice("50000");
	}

	/**
	 *
	 */
	private void populateAccommodation(Accomodation accomodation) {

		accomodation.setAccomCode("016211");
		accomodation.setAccomodationName("Riu Tikida Garden");

	}

}
