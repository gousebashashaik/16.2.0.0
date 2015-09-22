/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.comments.model.CommentModel;
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
import uk.co.portaltech.travel.enums.FeatureType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.enums.TemporalType;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
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
public class LocationGettingAroundEditorialPopulatorTest
{

	@InjectMocks
	private final LocationGettingAroundEditorialPopulator locationGettingAroundEditorialPopulator = new LocationGettingAroundEditorialPopulator();
	@Mock
	private FeatureService featureService;

	private LocationModel sourceData;
	private LocationData targetData;
	private Map<String, List<Object>> mapValuesForFeatures;


	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		sourceData = new LocationModel();
		targetData = new LocationData();

		final CategoryModel categoryModel = new CategoryModel();

		categoryModel.setCode("002240");
		categoryModel.setCatalogVersion(getCatalogVersion());


		final List<FeatureValueSetModel> featureValueSetModelList = new ArrayList<FeatureValueSetModel>();

		final FeatureValueSetModel featureValueSetModel = new FeatureValueSetModel();

		featureValueSetModel.setCatalogVersion(getCatalogVersion());
		featureValueSetModel.setCategory(categoryModel);

		featureValueSetModelList.add(featureValueSetModel);


		final Set<FeatureDescriptorModel> descriptorModelSet = new HashSet<FeatureDescriptorModel>();

		final FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();

		descriptorModel.setCode("002240");
		descriptorModel.setTemporalType(TemporalType.TRAVELLING);
		descriptorModel.setType(FeatureType.INTEGER);

		descriptorModelSet.add(descriptorModel);

		final List<ProductFeatureModel> featureModelList = new ArrayList<ProductFeatureModel>();

		final ProductFeatureModel featureModel = new ProductFeatureModel();

		featureModel.setDescription("desc");
		featureModel.setFeaturePosition(1);
		featureModel.setValueDetails("Values Details");
		featureModel.setValuePosition(2);

		featureModelList.add(featureModel);

		final TouristBoardModel touristBoardModel = new TouristBoardModel();

		final List<CommentModel> commentModelList = new ArrayList<CommentModel>();

		final CommentModel commentModel = new CommentModel();

		commentModel.setPriority(10);
		commentModel.setSubject("Comments om Location");
		commentModel.setText("write text ");
		commentModelList.add(commentModel);

		touristBoardModel.setComments(commentModelList);

		final WeatherModel weatherModel = new WeatherModel();

		final List<WeatherTypeModel> weatherTypeModelList = new ArrayList<WeatherTypeModel>();

		final WeatherTypeModel weatherTypeModel = new WeatherTypeModel();

		weatherTypeModel.setWeatherTypeName(uk.co.portaltech.travel.enums.WeatherTypeName.RAINFALL);

		weatherTypeModelList.add(weatherTypeModel);

		weatherModel.setWeatherTypes(weatherTypeModelList);

		sourceData.setCode("002240");
		sourceData.setName("UK", Locale.UK);
		sourceData.setDescription("Good for Sight", Locale.UK);
		sourceData.setFeatureValueSets(featureValueSetModelList);
		sourceData.setFeatureDescriptors(descriptorModelSet);
		sourceData.setType(LocationType.DESTINATION);
		sourceData.setTouristBoard(touristBoardModel);
		sourceData.setWeather(weatherModel);
		sourceData.setBrands(Arrays.asList(BrandType.FC));

		final List<Object> value = new ArrayList<Object>();
		value.add(sourceData);

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
	public void testPopulate()
	{
		try
		{
			/*
			 * Mockito.when( featureService.getValuesForFeatures(Arrays.asList(new String[] { "gettingAroundOverview" }),
			 * sourceData, new Date())).thenReturn( mapValuesForFeatures);
			 */
			assertNotNull(sourceData);
			assertNotNull(targetData);

			locationGettingAroundEditorialPopulator.populate(sourceData, targetData);

			assertNotNull(targetData);

			final Map<String, List<Object>> expectedResult = targetData.getFeatureCodesAndValues();
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
					assertThat(model.getName(Locale.UK), is("UK"));
					assertThat(model.getFeatureDescriptors().size(), is(1));
					assertThat(model.getType(), is(LocationType.DESTINATION));
					assertThat(model.getFeatureValueSets().size(), is(1));
					assertThat(model.getTouristBoard().getComments().size(), is(1));
					assertThat(model.getDescription(Locale.UK), is("Good for Sight"));

				}
			}



		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}

}
