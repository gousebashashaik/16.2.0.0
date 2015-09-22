/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.enums.TemporalType;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.TouristBoardModel;
import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.LocationData;


/**
 * @author sureshbabu.rn
 * 
 */
public class LocationContentPopulatorTest
{
	@InjectMocks
	private final LocationContentPopulator locationContentPopulator = new LocationContentPopulator();

	@Mock
	private FeatureService featureService;

	private LocationModel locationModel;
	private LocationData locationData;
	private Map<String, List<Object>> mapValuesForFeatures;



	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		locationData = new LocationData();

		locationModel = new LocationModel();

		final List<FeatureValueSetModel> featureValueSetModelList = new ArrayList<FeatureValueSetModel>();

		final Set<FeatureDescriptorModel> descriptorModelSet = new HashSet<FeatureDescriptorModel>();

		final FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();

		descriptorModel.setTemporalType(TemporalType.TRAVELLING);
		descriptorModelSet.add(descriptorModel);

		final FeatureValueSetModel featureValueSetModel = new FeatureValueSetModel();

		final List<FeatureValueModel> featureValueModelList = new ArrayList<FeatureValueModel>();

		final FeatureValueModel valueModel = new FeatureValueModel();

		valueModel.setCatalogVersion(getCatalogVersion());
		valueModel.setValue(locationModel);
		valueModel.setCode("002240");

		featureValueSetModel.setCatalogVersion(getCatalogVersion());
		featureValueSetModel.setFeatureValues(featureValueModelList);
		featureValueSetModel.setFeatureDescriptor(descriptorModel);
		featureValueSetModel.setFeatureValues(featureValueModelList);

		valueModel.setFeatureValueSet(featureValueSetModel);
		featureValueModelList.add(valueModel);
		featureValueSetModelList.add(featureValueSetModel);

		locationModel.setCode("002240");
		locationModel.setType(LocationType.DESTINATION);
		locationModel.setFeatureValueSets(featureValueSetModelList);
		locationModel.setFeatureDescriptors(descriptorModelSet);

		final List<LocationModel> locationModelList = new ArrayList<LocationModel>();

		final List<AirportModel> airportModelList = new ArrayList<AirportModel>();
		final AirportModel airportModel = new AirportModel();

		airportModel.setDestination(locationModelList);
		locationModel.setAirports(airportModelList);
		airportModelList.add(airportModel);
		locationModelList.add(locationModel);
		locationModel.setCatalogVersion(getCatalogVersion());

		final TouristBoardModel boardModel = new TouristBoardModel();

		boardModel.setCode("002240");
		boardModel.setRelevantLocations(locationModelList);

		boardModel.setCatalogVersion(getCatalogVersion());
		locationModel.setTouristBoard(boardModel);
		locationModel.setOrder(Integer.valueOf(4));

		final Set<LocationModel> locationModelSet = new HashSet<LocationModel>();
		locationModelSet.add(locationModel);
		final WeatherModel weatherModel = new WeatherModel();

		weatherModel.setLocation(locationModelSet);

		final List<WeatherTypeModel> typeModelList = new ArrayList<WeatherTypeModel>();

		final WeatherTypeModel typeModel = new WeatherTypeModel();

		typeModel.setWeather(weatherModel);
		typeModelList.add(typeModel);

		weatherModel.setWeatherTypes(typeModelList);
		locationModel.setWeather(weatherModel);

		locationModel.setName("UK", Locale.UK);
		locationModel.setOrder(4);
		locationModel.setType(LocationType.COUNTRY);
		locationModel.setBrands(Arrays.asList(BrandType.FC));

		final List<Object> value = new ArrayList<Object>();
		value.add(locationModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);
	}




	private CatalogVersionModel getCatalogVersion()
	{

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;

	}



	@SuppressWarnings("boxing")
	@Test
	public void test()
	{
		try
		{

			assertNotNull(locationModel);
			assertNotNull(locationData);
			/*
			 * Mockito.when( featureService.getValuesForFeatures(Arrays.asList(new String[] {"thingsToSeeAndDoEditorial",
			 * "whereToGo" }), locationModel, new Date())).thenReturn( mapValuesForFeatures);
			 */


			locationContentPopulator.populate(locationModel, locationData);

			assertNotNull(locationData);

			final Map<String, List<Object>> expectedResult = locationData.getFeatureCodesAndValues();

			final Set set = expectedResult.entrySet();

			final Iterator iterator = set.iterator();

			while (iterator.hasNext())
			{
				final Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator.next();

				final List<Object> featureList = keyVal.getValue();

				final Iterator featureListIterator = featureList.iterator();
				while (featureListIterator.hasNext())
				{

					final Object object = featureListIterator.next();
					final LocationModel model = (LocationModel) object;

					assertThat(model.getCode(), is("002240"));
					assertThat(model.getFeatureDescriptors().size(), is(1));
					assertThat(model.getFeatureValueSets().size(), is(1));
					assertThat(model.getType(), is(LocationType.COUNTRY));
					assertThat(model.getAirports().size(), is(1));
					assertThat(model.getTouristBoard().getCode(), is("002240"));
					assertThat(model.getOrder(), is(4));
				}
			}
		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}

}
