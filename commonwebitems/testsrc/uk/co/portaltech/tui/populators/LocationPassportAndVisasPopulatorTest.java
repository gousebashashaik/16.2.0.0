/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.FeatureType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.enums.TemporalType;
import uk.co.portaltech.travel.enums.WeatherTypeName;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author sunil.bd
 * 
 */
public class LocationPassportAndVisasPopulatorTest
{

	@InjectMocks
	private final LocationPassportAndVisasPopulator populator = new LocationPassportAndVisasPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private BrandUtils brandUtils;

	private LocationModel sourceModel;
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

		sourceModel = new LocationModel();
		locationData = new LocationData();

		final List<FeatureValueSetModel> featureValueSetModelList = new ArrayList<FeatureValueSetModel>();

		final FeatureValueSetModel featureValueSetModel = new FeatureValueSetModel();
		featureValueSetModel.setDefault(true);
		featureValueSetModelList.add(featureValueSetModel);

		final Set<FeatureDescriptorModel> descriptorModelSet = new HashSet<FeatureDescriptorModel>();

		final FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();

		descriptorModel.setTemporalType(TemporalType.TRAVELLING);
		descriptorModel.setType(FeatureType.INTEGER);
		descriptorModelSet.add(descriptorModel);

		final List<WeatherTypeModel> weatherTypeModelList = new ArrayList<WeatherTypeModel>();

		final WeatherTypeModel typeModel = new WeatherTypeModel();
		typeModel.setWeatherTypeName(WeatherTypeName.SUNSHINE);
		weatherTypeModelList.add(typeModel);

		final WeatherModel weatherModel = new WeatherModel();
		weatherModel.setWeatherTypes(weatherTypeModelList);

		sourceModel.setCode("002240");
		sourceModel.setName("England", Locale.UK);
		sourceModel.setOrder(32);
		sourceModel.setType(LocationType.DESTINATION);
		sourceModel.setSynonyms("synonyms");
		sourceModel.setFeatureDescriptors(descriptorModelSet);
		sourceModel.setFeatureValueSets(featureValueSetModelList);
		sourceModel.setWeather(weatherModel);
		sourceModel.setBrands(Arrays.asList(BrandType.FC));

		final List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{

		try
		{

			Mockito.when(brandUtils.getFeatureServiceBrand(sourceModel.getBrands())).thenReturn("TH");

			Mockito.when(featureService.getValuesForFeatures(Arrays.asList(new String[]
			{ "passportAndVisaInfo" }), sourceModel, new Date(), brandUtils.getFeatureServiceBrand(sourceModel.getBrands())))
					.thenReturn(mapValuesForFeatures);

			assertNotNull(sourceModel);
			assertNotNull(locationData);

			populator.populate(sourceModel, locationData);

			assertNotNull(locationData);

			final Map<String, List<Object>> expectedResult = locationData.getFeatureCodesAndValues();

			final Set aSet = expectedResult.entrySet();

			final Iterator iterator = aSet.iterator();

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
					assertThat(model.getName(Locale.UK), is("England"));
					assertThat(model.getWeather().getWeatherTypes().size(), is(1));
					assertThat(model.getFeatureValueSets().size(), is(1));
					assertThat(model.getType(), is(LocationType.DESTINATION));
					assertThat(model.getOrder(), is(32));
				}
			}
		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}
}
