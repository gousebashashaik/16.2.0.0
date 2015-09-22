/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.IntegrationTest;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.RatingsData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

/**
 * @author shyamaprasada.vs
 *
 */
@IntegrationTest
public class DefaultSearchFacadeTest  extends ServicelayerTransactionalTest{

	private static Logger LOG = Logger.getLogger(DefaultSearchFacadeTest.class);

    @Resource
	private SearchFacade searchFacade;

    @Resource
    private CatalogService  catalogService;

	private SearchResultsRequestData request;

	private SearchResultsViewData  searchResultsViewData;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	 initMocks(this);
	 importCsv("/testing/content_csv_for_sort.csv","utf-8");
	 Set<CatalogVersionModel> catalogs = new HashSet<CatalogVersionModel>();
	 catalogs.addAll(catalogService.getCatalog("fc_content").getCatalogVersions());
	 catalogs.addAll(catalogService.getCatalog("fc_catalog").getCatalogVersions());
	 this.catalogService.setSessionCatalogVersions(catalogs);

	 request = new SearchResultsRequestData();

     List<AirportData> airports = new ArrayList<AirportData>();
     request.setAirports(airports);
	 List<UnitData> units = new ArrayList<UnitData>();
	 request.setUnits(units);

	 searchResultsViewData = new SearchResultsViewData();

	 final List<SearchResultViewData> holidays = new ArrayList<SearchResultViewData>();

	 //data1
	 SearchResultViewData data1 = new SearchResultViewData();

	 SearchResultAccomodationViewData accomodation1 = new SearchResultAccomodationViewData();
	 accomodation1.setCode("1");
	 accomodation1.setName("Costa blanca");
	 accomodation1.setCommercialPriority(123);

	 RatingsData ratings1 = new RatingsData();
	 ratings1.setOfficialRating("4");
	 ratings1.setTripAdvisorRating("50");
	 accomodation1.setRatings(ratings1);
	 data1.setAccommodation(accomodation1);


	 SearchResultFlightViewData itinerary1 = new SearchResultFlightViewData();
	 itinerary1.setDepartureDate("24-01-2013");
	 data1.setItinerary(itinerary1);

	 SearchResultPriceViewData price1 = new SearchResultPriceViewData();
	 price1.setDiscount("20.5");
	 price1.setTotalParty("90.12");
	 data1.setPrice(price1);
	 holidays.add(data1);


	 //data2
	 SearchResultViewData data2 = new SearchResultViewData();

	 SearchResultAccomodationViewData accomodation2 = new SearchResultAccomodationViewData();
	 accomodation2.setCode("2");
	 accomodation2.setName("Atlantica Oasis");
	 accomodation2.setCommercialPriority(100);

	 RatingsData ratings2 = new RatingsData();
	 ratings2.setOfficialRating("4.5");
	 ratings2.setTripAdvisorRating("15");
	 accomodation2.setRatings(ratings2);
	 data2.setAccommodation(accomodation2);


	 SearchResultFlightViewData itinerary2 = new SearchResultFlightViewData();
	 itinerary2.setDepartureDate("24-01-2013");
	 data2.setItinerary(itinerary2);

	 SearchResultPriceViewData price2 = new SearchResultPriceViewData();
	 price2.setDiscount("23.5");
	 price2.setTotalParty("90.12");
	 data2.setPrice(price2);

	 holidays.add(data2);

	 //data3
	 SearchResultViewData data3 = new SearchResultViewData();

	 SearchResultAccomodationViewData accomodation3 = new SearchResultAccomodationViewData();
	 accomodation3.setCode("3");
	 accomodation3.setName("Colossus Beach");
	 accomodation3.setCommercialPriority(323);

	 RatingsData ratings3 = new RatingsData();
	 ratings3.setOfficialRating("5");
	 ratings3.setTripAdvisorRating("25");
	 accomodation3.setRatings(ratings3);
	 data3.setAccommodation(accomodation3);


	 SearchResultFlightViewData itinerary3 = new SearchResultFlightViewData();
	 itinerary3.setDepartureDate("24-03-2013");
	 data3.setItinerary(itinerary3);

	 SearchResultPriceViewData price3 = new SearchResultPriceViewData();
	 price3.setDiscount("10.5");
	 price3.setTotalParty("90.32");
	 data3.setPrice(price3);

	 holidays.add(data3);


     //data4

	 SearchResultViewData data4 = new SearchResultViewData();

	 SearchResultAccomodationViewData accomodation4 = new SearchResultAccomodationViewData();
	 accomodation4.setCode("4");
	 accomodation4.setName("Oasis");
	 accomodation4.setCommercialPriority(23);

	 RatingsData ratings4 = new RatingsData();
	 ratings4.setOfficialRating("3.5");
	 ratings4.setTripAdvisorRating("0");
	 accomodation4.setRatings(ratings4);
	 data4.setAccommodation(accomodation4);

	 SearchResultFlightViewData itinerary4 = new SearchResultFlightViewData();
	 itinerary4.setDepartureDate("24-04-2013");
	 data4.setItinerary(itinerary4);

	 SearchResultPriceViewData price4 = new SearchResultPriceViewData();
	 price4.setDiscount("20.5");
	 price4.setTotalParty("190.42");
	 data4.setPrice(price4);

	 holidays.add(data4);

	 searchResultsViewData.setHolidays(holidays);

	}

	@Test
	public void isSortByOptionNull()  {
		request.setSortBy("tRatingDesc");
		assertEquals(null,request.getSortBy());

	}

	@Test
	public void isSortBySetToRecommended() {
		request.setSortBy("default");
		assertSame("default",request.getSortBy());
	}


	@Test
	public void isSortByParametersEmpty() {
		request.setSortBy("tRatingDesc");
		assertSame(null,searchFacade.getSortParameters(request.getSortBy()));
	}


	@Test
	public void isSortByParametersConfiguredForTrating() {
		request.setSortBy("tRatingDesc");
		assertSame("TRATING_DESCENDING",searchFacade.getSortParameters(request.getSortBy())[0].getCode());
	}

	@Test
	public void isSearchResultDataEmpty() {
		 assertEquals(Integer.parseInt("0"),searchResultsViewData.getHolidays().size());
	}

	@Test
	public void shouldSortByRecommended() {
		request.setSortBy("default");
		searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
		 assertEquals("1",searchResultsViewData.getHolidays().get(0).getAccommodation().getCode());
	}

	@Test
	public void shouldSortByTRatingDesc() {
         request.setSortBy("tRatingDesc");
         searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
         assertSame("5",searchResultsViewData.getHolidays().get(0).getAccommodation().getRatings().getOfficialRating());
	}

	@Test
	public void shouldSortByTripAdvisorRatingDesc() {
         request.setSortBy("tripAdvisorRatingDesc");
         searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
		assertEquals("1",searchResultsViewData.getHolidays().get(0).getAccommodation().getCode());
	}

	@Test
	public void shouldSortBytPriceAsc() {
		request.setSortBy("priceAsc");
		searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
		assertSame("90.12",searchResultsViewData.getHolidays().get(0).getPrice().getTotalParty());
		assertSame("Atlantica Oasis",searchResultsViewData.getHolidays().get(0).getAccommodation().getName());
	}

	@Test
	public void shouldSortBytPriceDesc() {
		request.setSortBy("priceDesc");
		 searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
		 assertSame("190.42",searchResultsViewData.getHolidays().get(0).getPrice().getTotalParty());
	}


	@Test
	public void shouldSortByHotelName() {
		request.setSortBy("hotelName");
		 searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
		 assertSame("Atlantica Oasis",searchResultsViewData.getHolidays().get(0).getAccommodation().getName());
	}


	@Test
	public void shouldSortByDiscountDesc() {
		 request.setSortBy("discount");
		 searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
		 assertSame("23.5",searchResultsViewData.getHolidays().get(0).getPrice().getDiscount());
	}

	@Test
	public void shouldSortByDepartureDateAsc() throws ParseException {
		 request.setSortBy("departureDate");
		 searchResultsViewData = searchFacade.sortSearchResultsViewData(searchResultsViewData,request);
		 assertSame("24-01-2013",searchResultsViewData.getHolidays().get(3).getItinerary().getDepartureDate());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		LOG.info(" HolidayPackagesFinder Test Case  is Completed !!!");
	}

}
