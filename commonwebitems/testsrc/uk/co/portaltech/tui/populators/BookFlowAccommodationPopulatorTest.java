package uk.co.portaltech.tui.populators;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.travel.services.rules.RuleEngineUtil;
import uk.co.portaltech.tui.model.WorldCareFundModel;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FlightCarrier;
import uk.co.portaltech.tui.web.view.data.RatingsData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightScheduleData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsPriceData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsDestinationData;
import uk.co.portaltech.tui.web.view.data.SearchResultsLocationData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsResortData;

@IntegrationTest
public class BookFlowAccommodationPopulatorTest extends  ServicelayerTransactionalTest {
	
	@Resource
	private BookFlowAccommodationPopulator  bookFlowAccommodationPopulator;
	
	

	private static final int NO_OF_ADULTS=2;
	private static final int NO_OF_CHILD=3;
  
   private SearchResultViewData searchResultData;
   private BookFlowAccommodationViewData viewData;
   private SearchResultFlightViewData itinerary;
   private SearchResultFlightDetailViewData flightDetails,flightDetails1;
   private SearchResultAccomodationViewData accommodation;
   private SearchResultRoomsData  roomData1;
   private SearchResultPriceViewData priceData;
   private FlightCarrier carrier,carrier1;
   private SearchResultFlightScheduleData  schedule,schedule1;
   private SearchResultsLocationData location;
   private  SearchResultsDestinationData destination;
   private SearchResultsResortData resort;
   private RatingsData ratings;
   private SearchResultRoomsPriceData price;
   private static final int ARRIVAL_DATE_TIME_IN_MILLI=123323;
   private static final int DEPARTURE_DATE_TIME_IN_MILLI=1223223;
   private static final int  SET_COMMERCIAL_PRIORITY=4;
   private static final int SELLING_OUT=2;
   private SearchResultsRequestData searchParameter;
   /**
	 * Setting all the data to source before testing. 
	 */
	@Before
	public void setUp()
	{    searchParameter =new SearchResultsRequestData();
		 searchResultData =new SearchResultViewData();
		viewData=new BookFlowAccommodationViewData();
		
		setUpPriceData();
		setUpFlightData();
		setUpAccommodation();
		 
		 searchResultData.setCoachTransfer(true);
		 searchResultData.setPackageId("06012");
		 searchResultData.setTracsUnitCode("123345469");
		 searchParameter.setNoOfAdults(NO_OF_ADULTS);
		 searchParameter.setChildCount(NO_OF_CHILD);
		
		
	}


/**
 * Setting the Accommodation data.
 */
public void setUpAccommodation() {
	accommodation=new SearchResultAccomodationViewData();
	 accommodation.setCode("06012");
	 accommodation.setCommercialPriority(SET_COMMERCIAL_PRIORITY);
	 accommodation.setDifferentiatedCode("0012");
	 accommodation.setDifferentiatedProduct(false);
	 accommodation.setDifferentiatedType("differentiatedType");
	 accommodation.setImageUrl("imageUrl");
	 
	   location = new SearchResultsLocationData();
	  destination = new SearchResultsDestinationData();
	  destination.setCode("123");
	  destination.setName("Marrakech");
	 resort = new SearchResultsResortData();
	 resort.setCode("124");
	 resort.setName("resort");
	 location.setResort(resort);
	 location.setDestination(destination);
	 accommodation.setLocation(location );
	 accommodation.setName("Marrakech");     
	 ratings=new  RatingsData();
	 ratings.setOfficialRating("4");
	 ratings.setTripAdvisorRating("3");
	accommodation.setRatings(ratings);
	
	
	 roomData1=new SearchResultRoomsData();
	 roomData1.setAvailability(true);
	 roomData1.setRoomType("doubleRoom");
	 roomData1.setSellingout(SELLING_OUT);
	 roomData1.setBoardType("All Inclusive");
	 roomData1.setRoomCode("156");
	  price = new SearchResultRoomsPriceData();
	 price.setDiscount("20");
	 roomData1.setPrice(price);
	
	List<SearchResultRoomsData> rooms= new ArrayList<SearchResultRoomsData>();
	rooms.add(roomData1);
	 accommodation.setRooms(rooms);
	 accommodation.setStrapline("strapline");
	 accommodation.setTripAdvisorReviewCount(2);
	 accommodation.setUrl("url");
	 
	 HashMap<String,List<Object>> featureCodesAndValues =new HashMap<String,List<Object>>();
	 List<Object>value= new ArrayList<Object>();
	 Object obj= new Object();
	 value.add(obj);
	 featureCodesAndValues.put("featureCodesAndValues", value);
	 accommodation.putFeatureCodesAndValues(featureCodesAndValues);
	 searchResultData.setAccommodation(accommodation);
}


/**
 * Setting the flight data.
 */
public void setUpFlightData() {
	itinerary=new SearchResultFlightViewData();
	itinerary.setDepartureAirport("London Luton");
    itinerary.setDepartureDate("01-03-2013");
    itinerary.setFlightText("flight text");

	List<SearchResultFlightDetailViewData> viewsData = new ArrayList<SearchResultFlightDetailViewData>();
	
	flightDetails =new SearchResultFlightDetailViewData();
	flightDetails.setArrivalAirport("majoraka");
	 carrier= new FlightCarrier();
	carrier.setCode("0001");
	carrier.setName("taxi");
	flightDetails.setCarrier(carrier);
	flightDetails.setDepartureAirport("London Luton");
	flightDetails.setHaulType("SH");

	viewsData.add(flightDetails);
	schedule = new SearchResultFlightScheduleData();
	schedule.setArrivalDate("02-03-2013");
	schedule.setArrivalDateTimeInMilli(ARRIVAL_DATE_TIME_IN_MILLI);
	schedule.setArrivalTime("22:20");
	schedule.setDepartureDate("01-03-2013");
	schedule.setDepartureDateTimeInMilli(DEPARTURE_DATE_TIME_IN_MILLI);
	schedule.setDepartureTime("12:30");
	schedule.setOverlapDay(true);
	flightDetails.setSchedule(schedule);
	itinerary.setInbounds(viewsData);
	

	List<SearchResultFlightDetailViewData> viewsData1 = new ArrayList<SearchResultFlightDetailViewData>();
	
	flightDetails1 =new SearchResultFlightDetailViewData();
	flightDetails1.setArrivalAirport("spain");
	carrier1= new FlightCarrier();
	carrier1.setCode("0002");
	carrier1.setName("bus");
	flightDetails1.setCarrier(carrier1);
	flightDetails1.setDepartureAirport("London GatWick");
	flightDetails1.setHaulType("LH");
	viewsData1.add(flightDetails1);
	
	schedule1 = new SearchResultFlightScheduleData();
	schedule1.setArrivalDate("02-03-2013");
	schedule1.setArrivalDateTimeInMilli(ARRIVAL_DATE_TIME_IN_MILLI);
	schedule1.setArrivalTime("21:20");
	schedule1.setDepartureDate("01-03-2013");
	schedule1.setDepartureDateTimeInMilli(DEPARTURE_DATE_TIME_IN_MILLI);
	schedule1.setDepartureTime("12:10");
	schedule1.setOverlapDay(false);
	itinerary.setOutbounds(viewsData1);
	 searchResultData.setItinerary(itinerary);
}


/**
 * Setting the price data.
 */
public void setUpPriceData() {
	priceData= new SearchResultPriceViewData();
	priceData.setDepositAmount("120");
	priceData.setDiscount("20");
	priceData.setPerPerson("200");
	priceData.setTotalParty("1600");
	priceData.setLowDeposit("100");
	priceData.setLowDepositExists(true);
	priceData.setPromotionalOffer("PromotionalOffer");
	searchResultData.setPrice(priceData);
}

	
	

	 /**
	  * Testing the populate method.
	  */
	
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {
		bookFlowAccommodationPopulator.populate(searchResultData, viewData)	;
	       assertThat(viewData, is(notNullValue()));
	      
	       assertThat(viewData.getCreditCardData(), is(notNullValue()));
	       assertThat(viewData.getCreditCardData().getCreditCardPercentage(), is("2.5"));
	       assertThat(viewData.getCreditCardData().getDeposit(), is("102.5"));
	       assertThat(viewData.getCreditCardData().getTotal(), is("1660.5"));
	       assertThat(viewData.getPackageData(), is(searchResultData));
	       assertThat(viewData.getSubTotal(), is("1620.0"));
	       assertThat(viewData.getTotal(), is("1600.0"));
	       assertThat(viewData.getWorldCareFunds(), is(3.5));
	       
	       
	       assertThat(viewData.getPackageInfo().get("flight"),is((Object)true));
	       assertThat(viewData.getPackageInfo().get("coach"),is((Object)true));
	       assertThat(viewData.getPackageInfo().get("board"),is((Object)"All Inclusive"));
	       assertThat(viewData.getPackageInfo().get("donation"),is((Object)true));
	       assertThat(viewData.getPackageInfo().get("baggage"),is((Object)true));
	       assertThat(viewData.getPackageInfo().get("protection"),is((Object)true));
	     
	       
	        
	        
	}

}
