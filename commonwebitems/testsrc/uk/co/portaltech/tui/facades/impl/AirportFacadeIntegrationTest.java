package uk.co.portaltech.tui.facades.impl;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static uk.co.portaltech.commons.Collections.filter;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.commons.Collections;
import uk.co.portaltech.tui.facades.impl.AirportFacade;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.tui.feeds.model.cronjobs.DataFeedCronJobModel;
import uk.co.tui.feeds.services.jobs.FlightRoutesImportJobPerformable;


@IntegrationTest
public class AirportFacadeIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource
	private AirportFacade airportFacade;
	private List<String> brandList = null;
	@Resource
	private FlightRoutesImportJobPerformable flightRoutesImportJobPerformable;

	@Before
	public void setUp()
	{
		final DataFeedCronJobModel cronJobModel = new DataFeedCronJobModel();
		cronJobModel.setRootDir("/feed/incoming");
		cronJobModel.setOutputDir("/feed/processed");
		flightRoutesImportJobPerformable.perform(cronJobModel);
		brandList = new ArrayList<String>();
		brandList.add("TH");
		brandList.add("TH_FC");
	}

	@Test
	public void shouldInitializeAStableSpringContext()
	{
		assertThat(airportFacade, is(notNullValue()));
	}

	@Test
	public void shouldSearchForAirportsByAirportCode()
	{
		final List<AirportData> airports = airportFacade.find("ALC", null, null, brandList).getAirports();
		assertThat(airports, is(notNullValue()));
		assertThat(airports.size(), is(1));

	}

	@Test
	public void shouldFindAirportsByName()
	{
		final List<AirportData> airports = airportFacade.find("BRISTOL", null, null, brandList).getAirports();
		assertThat(airports, is(notNullValue()));
		assertThat(airports.size(), is(1));
	}

	@Test
	public void shouldIndexAndFindAirportWithChildren()
	{
		final List<AirportData> airports = airportFacade.find("any london", null, null, brandList).getAirports();
		assertThat(airports, hasItem(new AirportData("LN", "Any London")));
		final List<AirportData> anyLondons = filter(airports, new Collections.FilterFn<AirportData>()
		{
			@Override
			public boolean call(final AirportData input)
			{
				return input.getId().equals("LN");
			}
		});
		assertThat(anyLondons, is(notNullValue()));
		assertThat(anyLondons, hasItem(new AirportData("LN", "AnyLondon")));
		final AirportData anyLondon = anyLondons.get(0);
		assertThat(anyLondon.getChildren(), hasItem("LGW"));
	}

	@Test
	public void shouldIndexAndFindAirportWithParents()
	{
		final List<AirportData> airports = airportFacade.find("london gatwick", null, null, brandList).getAirports();

		assertThat(airports, hasItem(new AirportData("LGW", "london gatwick")));
		final AirportData lgw = filter(airports, new Collections.FilterFn<uk.co.portaltech.tui.web.view.data.AirportData>()
		{
			public boolean call(final AirportData input)
			{
				return input.getId().equals("LGW");
			}
		}).get(0);
		assertThat(lgw.getGroup(), hasItem("LN"));

	}


}
