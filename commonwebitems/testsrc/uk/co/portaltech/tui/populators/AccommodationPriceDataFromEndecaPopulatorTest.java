/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;

/**
 * @author sunil.bd
 * 
 */
public class AccommodationPriceDataFromEndecaPopulatorTest {

	@InjectMocks
	private AccommodationPriceDataFromEndecaPopulator accommodationPriceDataFromEndecaPopulator = new AccommodationPriceDataFromEndecaPopulator();

	private SearchResultData<ResultData> sourceData;
	private AccommodationViewData targetData;
	private Date date;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		targetData = new AccommodationViewData();
		sourceData = new SearchResultData<ResultData>();

		List<ResultData> results = new ArrayList<ResultData>();

		ResultData resultData = new ResultData();

		date = new java.util.Date(2013, 8, 7);

		resultData.setPriceFrom("33.00");
		resultData.setAvailableFrom(date);
		resultData.setDeparturePoint("India");
		resultData.setRoomOccupancy("2 adults/1 children/1 infants");

		results.add(resultData);

		sourceData.setResults(results);
	}

	@Test
	public void testPopulate() {
		accommodationPriceDataFromEndecaPopulator.populate(sourceData,
				targetData);

		assertNotNull(sourceData);
		assertNotNull(targetData);

		assertThat(targetData.getPriceFrom(), is("33.00"));
		assertThat(targetData.getAvailableFrom(), is(date));
		assertThat(targetData.getDeparturePoint(), is("India"));
		assertThat(targetData.getRoomOccupancy(),
				is("2 adults, 1 children, 1 infants"));

	}
}
