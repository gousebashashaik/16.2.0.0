/*package uk.co.portaltech.tui.utils;

import static org.junit.Assert.*;
import uk.co.portaltech.tui.web.view.data.FlightCarrier;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightScheduleData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;



*//**
 * @author shyamaprasada.vs
 *
 *//*
public class PackageIdGeneratorTest {


	private SearchResultViewData searchResultData = new SearchResultViewData();

	*//**
	 * @throws java.lang.Exception
	 *//*
	@Before
	public void setUp() throws Exception {


  		  searchResultData.setTracsUnitCode("CBLLCF12345");
		  //set Accommodation
		  SearchResultAccomodationViewData accommodation = new SearchResultAccomodationViewData();
		  accommodation.setCode("12345");
		  searchResultData.setAccommodation(accommodation);

		  //set itinerary
		  SearchResultFlightViewData itinerary = new SearchResultFlightViewData();

		  List<SearchResultFlightDetailViewData> outbounds = new ArrayList<SearchResultFlightDetailViewData>();

		  List<SearchResultFlightDetailViewData> inbounds  = new ArrayList<SearchResultFlightDetailViewData>();

		  //Add outbound details
		  SearchResultFlightDetailViewData outbound1 = new SearchResultFlightDetailViewData();

		  SearchResultFlightScheduleData  ob1_schedule = new SearchResultFlightScheduleData();

		  ob1_schedule.setDepartureDate("15-02-2013");
		  ob1_schedule.setDepartureTime("16-02-2013 12:30");

		  ob1_schedule.setDepartureDateTimeInMilli(1360998000000L);

		  ob1_schedule.setArrivalDate("16-02-2013");
		  ob1_schedule.setArrivalTime("16-02-2013 14:30");
		  ob1_schedule.setArrivalDateTimeInMilli(1361005200000L);
		  outbound1.setSchedule(ob1_schedule);

		  FlightCarrier ob1_carrier = new FlightCarrier();
		  ob1_carrier.setCode("4246");

		  outbound1.setCarrier(ob1_carrier);
		  outbounds.add(outbound1);
		  itinerary.setOutbounds(outbounds);

		  //Add inbound details
		  SearchResultFlightDetailViewData inbound1 = new SearchResultFlightDetailViewData();

		  SearchResultFlightScheduleData  ib1_schedule = new SearchResultFlightScheduleData();

		  ib1_schedule.setDepartureDate("15-02-2013");
		  ib1_schedule.setDepartureTime("16-02-2013 12:30");
		  ib1_schedule.setDepartureDateTimeInMilli(1360998000000L);
		  ib1_schedule.setArrivalDate("16-02-2013");
		  ib1_schedule.setArrivalTime("16-02-2013 14:30");
		  ib1_schedule.setArrivalDateTimeInMilli(1361005200000L);
		  inbound1.setSchedule(ib1_schedule);

		  FlightCarrier ib1_carrier = new FlightCarrier();
		  ib1_carrier.setCode("4247");
		  inbound1.setCarrier(ib1_carrier);

		  inbounds.add(inbound1);

		  itinerary.setInbounds(inbounds);

		searchResultData.setItinerary(itinerary);

	}


	*//**
	 * Test method for {@link uk.co.portaltech.tui.utils.PackageIdGenerator#
	 * generateIscapePackageId(uk.co.portaltech.tui.web.view.data.SearchResultViewData)}.
	 *//*
	@Test
	public final void testGenerateIscapePackageId() {

		assertSame("12345LCFCBL137036460000013703772000004246137098590000013709919000004247",
				PackageIdGenerator.getInstance().generateIscapePackageId(searchResultData));

	}

	*//**
	 * Test method for {@link uk.co.portaltech.tui.utils.PackageIdGenerator# getSellingCodeFromTracsUnitCode(String)}
	 *//*
	@Test
	public final void testGetSellingCodeFromTracsUnitCode(){

		assertEquals("LCFCBL",
				PackageIdGenerator.getInstance().getSellingCodeFromTracsUnitCode("CBLLCF12345"));
	}
}
*/