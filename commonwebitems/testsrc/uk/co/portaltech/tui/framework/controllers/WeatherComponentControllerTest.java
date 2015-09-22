/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.WeatherComponentFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.WeatherTypeValueViewData;
import uk.co.portaltech.tui.web.view.data.WeatherTypeViewData;
import uk.co.portaltech.tui.web.view.data.WeatherViewData;

/**
 * @author gopinath.n
 * 
 */
public class WeatherComponentControllerTest {

	@Mock
	private LocationModel currentLocationDetails;

	private LocationData locationData;

	@Mock
	private LocationFacade locationFacade;

	@Mock
	private WeatherComponentFacade weatherComponentFacade;


	private List<WeatherViewData> countryWeatherData;

	private WeatherTypeViewData weatherTypeViewData;

	private List<WeatherTypeViewData> weatherTypeViewDataList;

	private WeatherTypeValueViewData weatherTypeValueViewData;

	private List<WeatherTypeValueViewData> weatherTypeValueViewDataList;

	private WeatherViewData weatherViewData;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		locationData = new LocationData();
		weatherViewData = new WeatherViewData();
		weatherTypeViewData = new WeatherTypeViewData();
		weatherTypeValueViewData = new WeatherTypeValueViewData();
		countryWeatherData = new ArrayList<WeatherViewData>();
		weatherTypeViewDataList = new ArrayList<WeatherTypeViewData>();
	}

	public List<WeatherTypeValueViewData> sampleDataForWeatherTypeValueViewData() {
		weatherTypeValueViewData.setAverage(13.0);
		weatherTypeValueViewData.setAverage(28.0);
		weatherTypeValueViewData.setAverage(26.0);
		weatherTypeValueViewData.setAverage(28.0);

		weatherTypeValueViewData.setMonth("JAN");
		weatherTypeValueViewData.setMonth("FEB");
		weatherTypeValueViewData.setMonth("MAR");
		weatherTypeValueViewData.setMonth("APR");

		weatherTypeValueViewData.setMax(0.0);
		weatherTypeValueViewData.setMin(0.0);

		weatherTypeValueViewData.setUnit("DEGC");
		weatherTypeValueViewDataList.add(weatherTypeValueViewData);
		return weatherTypeValueViewDataList;

	}

	public List<WeatherViewData> sampleDataForWeatherViewDataList() {
		weatherViewData.setLocationName("Spain");
		weatherViewData.setWeatherTypes(weatherTypeViewDataList);
		countryWeatherData.add(weatherViewData);
		return countryWeatherData;

	}

	public WeatherViewData sampleDataForWeatherViewData() {
		weatherViewData.setLocationName("Spain");
		weatherViewData.setWeatherTypes(weatherTypeViewDataList);
		return weatherViewData;

	}

	public List<WeatherTypeViewData> sampleDataForWeatherTypeViewData() {
		weatherTypeViewData.setWeatherType("Temparature");
		weatherTypeViewData
				.setWeatherTypeValueViewDataList(weatherTypeValueViewDataList);
		weatherTypeViewDataList.add(weatherTypeViewData);
		return weatherTypeViewDataList;

	}

	public LocationData sampleDataForLocationData() {
		locationData.setPriceFrom("633");
		locationData
				.setThingstodoMapUrl("holiday/location/things-to-do/Spain-ESP");
		locationData.setLegacySystemUrl("legacySystemUrl");
		return locationData;

	}

	@Test
	public void testRenderWeatherComponent() {
		Mockito.when(
				weatherComponentFacade.getCurrentLocationDetails("ESP", "null"))
				.thenReturn(currentLocationDetails);
		Mockito.when(locationFacade.getLocationData("ESP")).thenReturn(
				sampleDataForLocationData());
		Mockito.when(
				weatherComponentFacade
						.getCountryWeatherData(currentLocationDetails))
				.thenReturn(sampleDataForWeatherViewDataList());
		Mockito.when(
				weatherComponentFacade
						.getSpainWeatherData(currentLocationDetails))
				.thenReturn(sampleDataForWeatherViewDataList());
		Mockito.when(
				weatherComponentFacade.getWeatherData(currentLocationDetails))
				.thenReturn(sampleDataForWeatherViewData());
		Assert.assertEquals("633", locationData.getPriceFrom());
		Assert.assertNotNull(countryWeatherData);
		Assert.assertEquals("Spain", weatherViewData.getLocationName());

	}

}
