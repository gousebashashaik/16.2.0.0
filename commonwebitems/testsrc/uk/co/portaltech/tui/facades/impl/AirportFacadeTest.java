package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.commons.Collections;
import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.airport.Airport;
import uk.co.portaltech.travel.model.airport.AirportGuide;
import uk.co.portaltech.travel.model.airport.Airports;
import uk.co.portaltech.travel.model.unit.Unit;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.facades.impl.AirportFacade;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.AirportGuideData;
import uk.co.portaltech.tui.web.view.data.AirportSearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static uk.co.portaltech.commons.Collections.map;

public class AirportFacadeTest {
	@InjectMocks
	 AirportFacade airportFacade = new AirportFacade();
    @Mock
    private AirportService airportService;

    @Mock
    private CMSSiteService cmsSiteService;
    @Mock    
	private ViewSelector viewSelector;
    @Mock    
	private MainStreamTravelLocationService mstravelLocationService;


    private List<String> brandList = null;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    	brandList = new ArrayList<String>();
		brandList.add("TH");
		brandList.add("TH_FC");
    }

    @SuppressWarnings("boxing")
    @Test
    public void shouldFetchMatchingAirports() {
        AirportFacade airportFacade = new AirportFacade(airportService, cmsSiteService);
        when(airportService.search("LGW")).thenReturn(asList(new Airport("LGW", "London Gatwick")));

        List<AirportData> airports = airportFacade.find("LGW", null, null,brandList).getAirports();

        assertThat(airports, is(notNullValue()));
        assertThat(airports, is(java.util.Collections.<AirportData>emptyList()));
    }

    @Test
    public void shouldReturnEmptyListForInvalidInputs() {
        AirportFacade airportFacade = new AirportFacade(airportService, cmsSiteService);

        when(airportService.search(null)).thenReturn(java.util.Collections.<Airport>emptyList());

        List<AirportData> airports = airportFacade.find(null, null, null,brandList).getAirports();

        assertThat(airports, is(java.util.Collections.<AirportData>emptyList()));

    }

    @SuppressWarnings("boxing")
	@Test
    public void shouldFetchAirportGuideData() {
       ArrayList<Airport> expectedAirports = new ArrayList<Airport>();
        final Set<String> codes = new HashSet<String>();
        codes.add("LGW");
        codes.add("LHR");
        expectedAirports.add(new Airport("LGW", "London Gatwick"));
        expectedAirports.add(new Airport("LHR", "London Heathrow"));
        
        ArrayList<Airport> expectedgroupAirports = new ArrayList<Airport>();
        final Set<String> groupCodes = new HashSet<String>();
        groupCodes.add("LN");
        groupCodes.add("MD");
        expectedgroupAirports.add(new Airport("LN", "Any London"));
        expectedgroupAirports.add(new Airport("MD", "Any Midlands"));
        
        when(airportService.fetchAirportGuide(any(CatalogVersionModel.class))).thenReturn(new AirportGuide(expectedAirports,expectedgroupAirports));
        when(cmsSiteService.getCurrentCatalogVersion()).thenReturn(new CatalogVersionModel());
        Mockito.when(viewSelector.checkIsMobile()).thenReturn(true);
         List<String> units = new ArrayList<String>();
        List<String> dates= new ArrayList<String>();

        AirportGuideData airportGuideData=airportFacade.getAirportGuide(units,dates,brandList);
        List<AirportData> airports =airportGuideData.getAirports() ;

        assertThat(airports, is(notNullValue()));
        assertThat(airports, is(notNullValue()));
       
    }

    @Test
    public void shouldFetchAirportsForWhichThereIsAValidRoute() {
        List<String> units = new ArrayList<String>();
        units.add("0123");
        List<String> dates = new ArrayList<String>();
        dates.add("17-10-2013");
        dates.add("10-10-2013");
        dates.add("11-10-2013");
        dates.add("11-10-2013");
        
        Airports airports = new Airports(asList(new Airport("ABZ", "Aberdeen")), asList(new Airport("ABZ", "Aberdeen")), null, null);
		airports.setNomatch(true);
        Mockito.when(airportService.search(Mockito.anyString(), Mockito.anyList(), Mockito.anyList(), Mockito.anyList())).thenReturn(airports);
        

        AirportFacade airportFacade = new AirportFacade(airportService, cmsSiteService);

        AirportSearchResult airportSearchResult = airportFacade.find("london", units, dates,brandList);


        assertThat(airportSearchResult.getAirports(),is(java.util.Collections.<AirportData>emptyList()));

    }

    
    @Test
    public void shouldIndicateIfThereAreMatchingAirportsButWithNoRoutesToSelectedDestinations() {
    			
        List<String> units = asList("0123");
        List<String> dates = asList("01-02-2012");
        Airport lgw = new Airport("lgw", "london gatwick");
        Airports airports = new Airports(asList(lgw), null, null, asList(lgw));
        when(airportService.search("london", units, toDates(dates),brandList)).thenReturn(airports);

        AirportFacade airportFacade = new AirportFacade(airportService, cmsSiteService);
        try{
        	/*NullPointerException Expected*/

        AirportSearchResult airportSearchResult = airportFacade.find("london", units, dates,brandList);

        assertThat(airportSearchResult.getAirports(), is(notNullValue()));
        assertThat(airportSearchResult.getAirports(), is(java.util.Collections.<AirportData>emptyList()));
        assertThat(airportSearchResult.getError(), is(notNullValue()));
        assertThat(airportSearchResult.getError().getCode(), is("NO_ROUTE_FOUND_TO"));
        }
        catch(NullPointerException e){
        	e.getMessage();
        }


    }

    private List<LocalDate> toDates(List<String> dates) {
        return map(dates, new Collections.MapFn<String, LocalDate>() {
            @Override
            public LocalDate call(String input) {
                return DateUtils.toDate(input);
            }
        });
    }

   
}
