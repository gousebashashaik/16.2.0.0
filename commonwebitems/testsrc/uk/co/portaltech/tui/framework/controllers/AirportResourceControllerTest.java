package uk.co.portaltech.tui.framework.controllers;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.co.portaltech.tui.facades.impl.AirportFacade;
import uk.co.portaltech.tui.web.view.data.AirportData;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AirportResourceControllerTest {

/*    @Mock
    private AirportFacade airportFacade;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldSearchAirports() {
        AirportData lgwAirport = new AirportData("lgw", "London Gatwick");

        when(airportFacade.find("lond", asList("123", "456"), asList("01-02-2012", "01-03-2011"))).thenReturn(asList(lgwAirport));
        AirportResourceController airportResourceController = new AirportResourceController(airportFacade);

        List<AirportData> airports = airportResourceController.search("lond", "123,456", "01-02-2012,01-03-2011");

        assertThat(airports, is(notNullValue()));
        assertThat(airports.size(), is(1));
        assertThat(airports, hasItems(lgwAirport));
    }*/
}
