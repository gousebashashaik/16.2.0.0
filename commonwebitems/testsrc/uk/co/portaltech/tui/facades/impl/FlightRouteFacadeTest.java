/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.services.route.*;
import uk.co.portaltech.tui.facades.impl.FlightRouteFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author gaurav.b
 */
@UnitTest
public class FlightRouteFacadeTest {

	@InjectMocks
	private FlightRouteFacade flightRouteFacade = new FlightRouteFacade();

	private List<String> airportCodeList;
	private List<String> unitCodeList;
	@Mock
	private TuiUtilityService tuiUtilityService;
	@Mock
	private FlightRouteIndexService flightRouteIndexService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	@SuppressWarnings("boxing")
	@Test
	public void shouldGetFlightDatesByWhereFromAndWhereTo() {
		airportCodeList = new ArrayList<String>();
		unitCodeList = new ArrayList<String>();
		airportCodeList.add("MBA");
		airportCodeList.add("STN");
		unitCodeList.add("11");
		unitCodeList.add("12");

		Mockito.when(tuiUtilityService.getSiteReleventBrands()).thenReturn(
				airportCodeList);
		Mockito.when(
				flightRouteIndexService.findFlightDates(Mockito.anyList(),
						Mockito.anyList(), Mockito.anyList(),null)).thenReturn(
				airportCodeList);

		final Collection<String> dates = flightRouteFacade.getFlightDates(
				airportCodeList, unitCodeList,null);
		assertThat(dates, is(notNullValue()));
		assertThat(dates.size(), is(2));
		assertThat(dates, hasItems("MBA"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void shouldGetFlightDatesByWhereFrom() {
		unitCodeList = new ArrayList<String>();
		airportCodeList = new ArrayList<String>();
		airportCodeList.add("LGW");
		airportCodeList.add("STN");

		Mockito.when(tuiUtilityService.getSiteReleventBrands()).thenReturn(
				airportCodeList);
		Mockito.when(
				flightRouteIndexService.findFlightDates(Mockito.anyList(),
						Mockito.anyList(), Mockito.anyList(),null)).thenReturn(
				airportCodeList);

		final Collection<String> dates = flightRouteFacade.getFlightDates(
				airportCodeList, unitCodeList,null);
		assertThat(dates, is(notNullValue()));
		assertThat(dates.size(), is(2));
		assertThat(dates, hasItems("LGW"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void shouldGetFlightDatesByWhereTo() {
		airportCodeList = null;
		unitCodeList = new ArrayList<String>();
		unitCodeList.add("11");
		unitCodeList.add("12");

		Mockito.when(tuiUtilityService.getSiteReleventBrands()).thenReturn(
				airportCodeList);
		Mockito.when(
				flightRouteIndexService.findFlightDates(Mockito.anyList(),
						Mockito.anyList(), Mockito.anyList(),null)).thenReturn(
				unitCodeList);

		final Collection<String> dates = flightRouteFacade.getFlightDates(
				airportCodeList, unitCodeList,null);
		assertThat(dates, is(notNullValue()));
		assertThat(dates.size(), is(2));
		assertThat(dates, hasItems("11"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void shouldGetFlightDatesWithoutWhereFromAndWhereTo() {
		airportCodeList = new ArrayList<String>();
		airportCodeList.add("LGW");
		airportCodeList.add("STN");

		Mockito.when(tuiUtilityService.getSiteReleventBrands()).thenReturn(
				airportCodeList);
		Mockito.when(
				flightRouteIndexService.findAllFlightDates(Mockito.anyList()))
				.thenReturn(airportCodeList);

		final Collection<String> dates = flightRouteFacade.getAllFlightsDates();
		assertThat(dates, is(notNullValue()));
		assertThat(dates.size(), is(2));
		assertThat(dates, hasItems("LGW"));
	}

}
