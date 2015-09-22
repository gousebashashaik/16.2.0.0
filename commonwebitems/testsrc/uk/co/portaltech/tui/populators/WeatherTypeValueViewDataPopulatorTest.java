/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.enums.Month;
import uk.co.portaltech.travel.enums.WeatherUnit;
import uk.co.portaltech.travel.model.WeatherTypeValueModel;
import uk.co.portaltech.tui.web.view.data.WeatherTypeValueViewData;

/**
 * @author sureshbabu.rn
 *
 */
public class WeatherTypeValueViewDataPopulatorTest {

	private WeatherTypeValueViewDataPopulator weatherTypeValueViewDataPopulator=new WeatherTypeValueViewDataPopulator();
	private WeatherTypeValueModel source;
	private WeatherTypeValueViewData target;
	
	@SuppressWarnings({ "boxing", "static-access" })
	@Before
	public void setUp() throws Exception {
		
		source=new WeatherTypeValueModel();
		target=new WeatherTypeValueViewData();
		Month month = null;
		WeatherUnit weatherUnit = null;
		
		source.setAverage(55.26);
		source.setMin(5.25);
		source.setMax(95.45);
		source.setMonthType(month.JUN);
		source.setWeatherUnitType(weatherUnit.HR);
	}
 
	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.WeatherTypeValueViewDataPopulator#populate(uk.co.portaltech.travel.model.WeatherTypeValueModel, uk.co.portaltech.tui.web.view.data.WeatherTypeValueViewData)}.
	 */
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testPopulate() {
		assertNotNull(source);
		assertNotNull(target);
		weatherTypeValueViewDataPopulator.populate(source, target);		
		
		double expectedAverage=55.26, expectedMin=5.25, expectedMax=95.45;
		double actualAverage=target.getAverage(), actualMin=target.getMin(), actualMax=target.getMax();
		
		assertNotNull(target);
		assertEquals(expectedAverage, actualAverage, 0.26);
		
	}

}
