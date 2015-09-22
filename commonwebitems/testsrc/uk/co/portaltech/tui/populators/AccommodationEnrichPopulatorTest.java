/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author sunil.bd
 *
 */
public class AccommodationEnrichPopulatorTest {
	

	@InjectMocks
	private AccommodationEnrichPopulator accommodationEnrichPopulator = new AccommodationEnrichPopulator();
	
	private ResultData sourceData;
	
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
		sourceData = new ResultData();
		
		date = new java.util.Date(2013, 8, 7);

		sourceData.setPriceFrom("33.00");
		sourceData.setAvailableFrom(date);
		sourceData.setDeparturePoint("India");
		sourceData.setRoomOccupancy("2 adults/1 children/1 infants");
		
	}

	@Test
	public void testPopulate() {
		accommodationEnrichPopulator.populate(sourceData, targetData);
		
		assertNotNull(sourceData);
		assertNotNull(targetData);
		
	}

}
