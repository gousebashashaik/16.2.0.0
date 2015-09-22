/**
 * 
 */
package uk.co.portaltech.tui.services;


import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.unit.Unit;
import uk.co.portaltech.travel.model.unit.Units;
import uk.co.portaltech.travel.services.route.FlightRouteIndexService;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
/**
 * @author laxmibai.p
 *
 */
public class HolidayTypeServiceTest {

	@Mock
	private FlightRouteIndexService flightRouteIndexService;
	 private SearchPanelComponentModel searchPanelComponentModel;
	
	@InjectMocks
	private HolidayTypeService holidayTypeService = new HolidayTypeService();
	private List<DestinationData> destinations;
	private Collection<Unit> unitsWithoutRoute;
	private Units unitsForRoute;
	private List<String> airports = new ArrayList<String>();
	private List<String> dates = new ArrayList<String>();
	private Collection<Unit> emptyUnits = new ArrayList<Unit>();
	private static final int  ZERO = 0;
	private static final int  TWO = 2;
	private static final int  FOUR = 4;
	private static final Integer MAX_PRODUCT_RANGE = Integer.valueOf(4);
	private List<String> brandList = null;
	@Before
	public void setUp()
	{
		initMocks(this);
	    destinations = HolidayTypeServiceTestData.getDestinationDataList();
	    unitsWithoutRoute = HolidayTypeServiceTestData.getUnitsWithOutRoute();
	    unitsForRoute=HolidayTypeServiceTestData.getUnitsForRoute();
	    searchPanelComponentModel = new SearchPanelComponentModel();
	      searchPanelComponentModel.setMaxProdRangeViewable(MAX_PRODUCT_RANGE);	
	      brandList = new ArrayList<String>();
			brandList.add("TH");
			brandList.add("TH_FC");
	}
	
	
	
	@SuppressWarnings("boxing")
	@Test
	public void setHotelsForHolidayTypeWhenNoCriteria()
	{
		String productRangeType = "Premier Families";
		
		when( flightRouteIndexService
				.findAllProductsForType(productRangeType,brandList)).thenReturn(unitsWithoutRoute);
		holidayTypeService.getHolidaysForProductRange(destinations, airports, dates,"true");
		for (DestinationData destiantionData : destinations) {
			if(destiantionData.getName().equals(productRangeType))
			{
				assertThat(destiantionData.getHotels(), notNullValue());
				assertThat(destiantionData.getName(), is(productRangeType));
				assertThat(destiantionData.getHotels().size(), is(FOUR));
			}
		}
		
		
	}
	
	
	@SuppressWarnings("boxing")
	@Test
	public void setHotelsForHolidayTypeWhenCriteria()
	{
		String productRangeType = "Premier";
		List<String> airportsList = new ArrayList<String>();
		List<LocalDate> dateList = new ArrayList<LocalDate>();
		airportsList.add("LGW");
		
		when( flightRouteIndexService.findProductsWithRoute(productRangeType,airportsList,dateList,brandList)).thenReturn(unitsForRoute);
		holidayTypeService.getHolidaysForProductRange(destinations, airportsList, dates,"true");
		for (DestinationData destiantionData : destinations) {
			if(destiantionData.getName().equals(productRangeType))
			{
				assertThat(destiantionData.getHotels(), notNullValue());
				assertThat(destiantionData.getName(), is(productRangeType));
				assertThat(destiantionData.getHotels().size(), is(FOUR));
			}
		}
	}
	
	@SuppressWarnings("boxing")
	@Test
	public void setEmptyHotelsIfNoProductsFound()
	{
		String productRangeType = "Splash World";
		when( flightRouteIndexService
				.findAllProductsForType(productRangeType,brandList)).thenReturn(emptyUnits);
		holidayTypeService.getHolidaysForProductRange(destinations, airports, dates,"true");
		for (DestinationData destiantionData : destinations) {
			if(destiantionData.getName().equals(productRangeType))
			{
				assertThat(destiantionData.getHotels().size(), is(ZERO));
			}
		}
	}
	
}
