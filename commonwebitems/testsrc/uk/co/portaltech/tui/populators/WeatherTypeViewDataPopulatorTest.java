/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.WeatherTypeName;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.model.WeatherTypeValueModel;
import uk.co.portaltech.tui.web.view.data.WeatherTypeValueViewData;
import uk.co.portaltech.tui.web.view.data.WeatherTypeViewData;

/**
 * @author sureshbabu.rn
 *
 */
public class WeatherTypeViewDataPopulatorTest {

	@InjectMocks
	WeatherTypeViewDataPopulator weatherTypeViewDataPopulator=new WeatherTypeViewDataPopulator();
	
	@Mock
	Converter<WeatherTypeValueModel, WeatherTypeValueViewData>                 weatherTypeValueViewDataConverter;
	@Mock
	Populator<WeatherTypeValueModel, WeatherTypeValueViewData>                 weatherTypeValueViewDataPopulator;
	
	WeatherTypeModel source;
	WeatherTypeViewData target;
	WeatherTypeValueModel weatherTypeValueModel;
	
	@SuppressWarnings("static-access")
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		source=new WeatherTypeModel();
		target=new WeatherTypeViewData();
		WeatherTypeName weatherTypeName=null;
		weatherTypeValueModel=new WeatherTypeValueModel();
		List<WeatherTypeValueModel> weatherTypeValueModelList=new ArrayList<WeatherTypeValueModel>();
		
		weatherTypeValueModelList.add(weatherTypeValueModel);
		
		source.setWeatherTypeName(weatherTypeName.HUMIDITY);
		source.setWeatherTypeValues(weatherTypeValueModelList);
		
		
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.WeatherTypeViewDataPopulator#populate(uk.co.portaltech.travel.model.WeatherTypeModel, uk.co.portaltech.tui.web.view.data.WeatherTypeViewData)}.
	 */
	@Test
	public void testPopulate() {
		WeatherTypeValueViewData weatherTypeValueViewData=new WeatherTypeValueViewData();
		weatherTypeValueViewData.setAverage(25.00);
		weatherTypeValueViewData.setMax(50.00);
		weatherTypeValueViewData.setMin(5.00);
		weatherTypeValueViewData.setMonth("june");
		weatherTypeValueViewData.setUnit("weatherUnit");
		
		Mockito.when(weatherTypeValueViewDataConverter.convert(weatherTypeValueModel)).thenReturn(weatherTypeValueViewData);
		Mockito.doNothing().when(weatherTypeValueViewDataPopulator).populate(weatherTypeValueModel,weatherTypeValueViewData);
		
		assertNotNull(source);
		assertNotNull(target);
		
		weatherTypeViewDataPopulator.populate(source, target);
		
		assertNotNull(source);
		assertNotNull(target);
		assertEquals(1, target.getWeatherTypeValueViewDataList().size());
	
		
	}

}
