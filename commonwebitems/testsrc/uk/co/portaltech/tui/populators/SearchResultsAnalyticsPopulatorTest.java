/**
 *
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Test;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.web.view.data.ABTestViewData;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import static org.hamcrest.CoreMatchers.is;

@IntegrationTest
public class SearchResultsAnalyticsPopulatorTest extends ServicelayerTransactionalTest {

        @Resource
		private SearchResultsAnalyticsPopulator searchResultsAnalyticsPopulator;

        private List<WebAnalytics> target;
		private Map<String, WebAnalytics> analyticMap ;

		@Resource
		private AnalyticsHelper analyticsHelper;

	@Test
	public void testPopulateSearchResultsAnalyticsData() {

		  SearchResultsViewData source = new SearchResultsViewData();
		  source.setEndecaResultsCount(10);

		  searchResultsAnalyticsPopulator = new SearchResultsAnalyticsPopulator();
		  analyticMap  = new HashMap<String,WebAnalytics>();
		  searchResultsAnalyticsPopulator.populate(source, analyticMap);
		  	assertThat((analyticMap.get("SchScope")).getKey(), is("SchScope"));
			assertThat((analyticMap.get("SchScope")).getValue(), is("Standard"));

			assertThat((analyticMap.get("Results")).getKey(), is("Results"));
			assertThat((analyticMap.get("Results")).getValue(), is("10")) ;

	}

	@Test
	public void testPopulateSearchRequestAnalyticsData() {

		  searchResultsAnalyticsPopulator = new SearchResultsAnalyticsPopulator(analyticsHelper);
			SearchResultsRequestData srd = new SearchResultsRequestData();
		  target  = new ArrayList();
		  analyticMap  = new HashMap<String,WebAnalytics>();
		  searchResultsAnalyticsPopulator.populate(populateSearchRequest(srd), analyticMap);

		  assertThat((analyticMap.get("WhereTo")).getKey(), is("WhereTo"));
		  assertThat((analyticMap.get("WhereTo")).getValue(), is("LESP|H004965"));
		  assertThat((analyticMap.get("DepAir")).getKey(), is("DepAir"));
		  assertThat((analyticMap.get("DepAir")).getValue(), is("LGW|LN"));

		  assertThat((analyticMap.get("DepDate")).getKey(), is("DepDate"));
		  assertThat((analyticMap.get("DepDate")).getValue(), is("01"));
		  assertThat((analyticMap.get("MonthYear")).getKey(), is("MonthYear"));
		  assertThat((analyticMap.get("MonthYear")).getValue(), is("10/2012"));
		  assertThat((analyticMap.get("FlexDate")).getKey(), is("FlexDate"));
		  assertThat((analyticMap.get("FlexDate")).getValue(), is("No"));
		  assertThat((analyticMap.get("Adults")).getKey(), is("Adults"));
		  assertThat((analyticMap.get("Adults")).getValue(), is("4"));

		  assertThat((analyticMap.get("Children")).getKey(), is("Children"));
		  assertThat((analyticMap.get("Children")).getValue(), is("1"));

		  assertThat((analyticMap.get("Infants")).getKey(), is("Infants"));
		  assertThat((analyticMap.get("Infants")).getValue(), is("1"));

	
	

		  assertThat((analyticMap.get("Dur")).getKey(), is("Dur"));
		  assertThat((analyticMap.get("Dur")).getValue(),is("5"));


	}


	@Test
	public void testPopulateSearchABTAnalytics() {
		  searchResultsAnalyticsPopulator = new SearchResultsAnalyticsPopulator();
		  analyticMap  = new HashMap<String,WebAnalytics>();
		  searchResultsAnalyticsPopulator.populate(populateABTestViewData(), analyticMap);

		  assertThat((analyticMap.get("TestName")).getKey(), is("TestName"));
	

	}

	/** search request Data */

	ABTestViewData populateABTestViewData() {

		ABTestViewData aBTestViewData = new ABTestViewData();
		aBTestViewData.setVariantCode("test");
		return aBTestViewData;

	}

	SearchResultsRequestData populateSearchRequest(SearchResultsRequestData srd) {

		List<AirportData> airports = poupluateAirportData();
		List<UnitData> units = populateUnitData();
		String latestDepartureDate = "01-10-2012";
		int noOfAdults = 2;
		int noOfSeniors = 2;
		int noOfChildren = 2;
		List<Integer> age = new ArrayList<Integer>();
		age.add(Integer.valueOf(1));
		age.add(Integer.valueOf(10));


		srd.setAirports(airports);
		srd.setUnits(units);
		srd.setFlexibleDays(7);
		srd.setFlexibility(false);
		srd.setWhen(latestDepartureDate);
		srd.setNoOfAdults(noOfAdults);
		srd.setNoOfChildren(noOfChildren);
		srd.setInfantCount(1);
		srd.setChildCount(1);
		srd.setNoOfSeniors(noOfSeniors);
		srd.setChildrenAge(age);
		srd.setDuration(5);

		return (srd);

	}

	List<AirportData> poupluateAirportData() {
		Collection<String> group = new ArrayList<String>();
		group.add(new String("SE"));
		group.add(new String("LN"));

		AirportData ad = new AirportData("LGW", "LONDON GATWICK");

		List<AirportData> lists = new ArrayList();

		group = new ArrayList<String>();
		group.add(new String("LUT"));
		group.add(new String("STN"));

		AirportData ad2 = new AirportData("LN", "ANY LONDON");

		lists.add((ad));
		lists.add((ad2));
		return (lists);

	}

	List<UnitData> populateUnitData() {

		UnitData ud1 = new UnitData("ESP", "Spain", "COUNTRY");
		UnitData ud2 = new UnitData("004965", "Eurosalou Hotel  Spa", "HOTEL");

		List<UnitData> lists = new ArrayList();
		lists.add((ud1));
		lists.add((ud2));
		return (lists);

	}

}
