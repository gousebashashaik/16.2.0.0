/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FlightCarrier;
import uk.co.portaltech.tui.web.view.data.FlightOptionRequestData;
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

/**
 * @author deepakkumar.k
 *
 */
@UnitTest
public class FlightOptionRequestPopulatorTest {

	@InjectMocks
	private FlightOptionRequestPopulator populator = new FlightOptionRequestPopulator();
	@Mock
	private ConfigurationService configService;
	@Mock
	private Configuration configuration;

	private BookFlowAccommodationViewData source;
	private FlightOptionRequestData  target;
	 private SearchResultViewData searchResultData;
	 private SearchResultsRequestData searchRequestData;

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
	   private static final int  FOUR=4;
	   private static final double  RATING=4.0;
	   private static final int  FIVE=5;
	   private static final int  SIX=6;
	   private static final int  SEVEN=7;
	   private static final int  NINE=9;
	   private static final int  TEN=10;
	   private static final int  SEVENTEEN=17;
	   private static final int SELLING_OUT=2;
	   private static final int ZERO=0;
	   private static final int TWO=2;

	   
	   /**
	    * Setting the  data before testing.
	    */
	   @SuppressWarnings("boxing")
	@Before
		public void setUp()
		{ 
	       MockitoAnnotations.initMocks(this);
		   
		   target= new FlightOptionRequestData();   
	       searchResultData =new SearchResultViewData();

		
		setUpPriceData();
		setUpFlightData();
		setUpAccommodation();
		 
		 searchResultData.setCoachTransfer(true);
		 searchResultData.setPackageId("06012");
		 searchResultData.setTracsUnitCode("123345469");
		
		 
		 
		 searchRequestData= new SearchResultsRequestData();
		 searchRequestData.setNoOfAdults(FOUR);
		 searchRequestData.setNoOfChildren(FOUR);
		 searchRequestData.setNoOfSeniors(2);
		 searchRequestData.setInfantCount(2);
		 searchRequestData.setChildCount(2);
		 List<Integer>childrenAge =new ArrayList<Integer>();
		 childrenAge.add(FOUR);
		 childrenAge.add(FIVE);
		 childrenAge.add(SIX);
		 searchRequestData.setChildrenAge(childrenAge);
		 searchRequestData.setWhen("28-02-2013");
		 searchRequestData.setDuration(NINE);
		 
		 
		 source=new BookFlowAccommodationViewData();
		  source.setPackageData(searchResultData);
		  source.setSearchRequestData(searchRequestData);
	      source.setFinPos("2");
	   
		}
	   /**
	    * Setting the Accommodation data.
	    */
	   public void setUpAccommodation() {
	   	accommodation=new SearchResultAccomodationViewData();
	   	 accommodation.setCode("06012");
	   	 accommodation.setCommercialPriority(FOUR);
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
	 * Test method for {@link uk.co.portaltech.tui.populators.FlightOptionRequestPopulator#populate(uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData, uk.co.portaltech.tui.web.view.data.FlightOptionRequestData)}.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {
		
		Mockito.when(configService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getInt(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
		
		populator.populate(source, target);
		
		
		 assertThat(target, is(notNullValue()));
		 assertThat(target.getAct(), is(ZERO));
		 assertThat(target.getAttrstr(), is("||||||||null|null"));
		 assertThat(target.getBc(), is(SEVENTEEN));
		 assertThat(target.getBoardBasisPackageNumber(), is("06012null00212232231233231"));
		 assertThat(target.getDay(), is("28"));
		 assertThat(target.getDepm(), is(SEVEN));
		 assertThat(target.getDess(), is("true"));
		 assertThat(target.getDtx(), is(0));
		 assertThat(target.getDur(), is(ZERO));
		 assertThat(target.getDurT(), is("0/w"));
		 assertThat(target.getDxsel(), is(0));
		 assertThat(target.getLoct(), is(0));
		 assertThat(target.getMargindt(), is(SEVEN));
		 assertThat(target.getMnth(), is("02"));
		 assertThat(target.getMps(), is(TWO));
		 assertThat(target.getMthyr(), is("02/2013"));
		 assertThat(target.getNumr(), is("1"));
		 assertThat(target.getPconfig(), is("1|4|2|4|2|4-5-6-/"));
		 assertThat(target.getPid(), is("06012null00212232231233231"));
		 assertThat(target.getSd(), is("28/02/2013"));
		 assertThat(target.getSda(), is("true"));
		 assertThat(target.getTadt(), is(FOUR));
		 assertThat(target.getTinf(), is(2)); 
		 assertThat(target.getTsnr(), is(2));
		 assertThat(target.getTuidesc(), is("123"));
		 assertThat(target.getYear(), is("2013"));
		 assertThat(target.getDta(), is(false));
		 assertThat(target.getJsen(), is(true));
         assertThat(target.getLs(), is(false));
	
		
		
		
	
	}

}
