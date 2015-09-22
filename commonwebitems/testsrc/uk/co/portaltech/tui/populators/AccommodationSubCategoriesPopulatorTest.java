/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.LocationData;


/**
 * @author sunil.bd
 * 
 */
public class AccommodationSubCategoriesPopulatorTest
{

	@InjectMocks
	private final AccommodationSubCategoriesPopulator categoriesPopulator = new AccommodationSubCategoriesPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private MediaDataPopulator mediaDataPopoulator;

	@Mock
	private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;

	@Mock
	private LocationService locationService;

	@Mock
	private Map<String, List<Object>> aMap;

	@Mock
	private TypeService typeService;

	private AccommodationModel sourceData;
	private LocationData targetData;
	private Map<String, List<Object>> mapValuesForFeatures;

	/**
	 * @throws java.lang.Exception
	 */

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		targetData = new LocationData();

		final MediaModel mediaModel = new MediaModel();
		mediaModel.setCode("002240");
		mediaModel.setMime("type");
		mediaModel.setUrl("www.re.com");
		mediaModel.setUrl2("www.hdhh.com");

		final List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

		final CategoryModel categoryModel = new CategoryModel();

		final List<MediaModel> mediaModelList = new ArrayList<MediaModel>();

		mediaModelList.add(mediaModel);

		categoryModel.setCode("002240");
		categoryModelList.add(categoryModel);

		sourceData = new AccommodationModel();

		sourceData.setCode("002240");
		sourceData.setSupercategories(categoryModelList);
		sourceData.setThumbnail(mediaModel);
		sourceData.setThumbnails(mediaModelList);
		sourceData.setBrands(Arrays.asList(BrandType.FC));

		final List<Object> value = new ArrayList<Object>();
		value.add(sourceData);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);

	}

	@Test
	public void testPopulate()
	{
		final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
		{ "longitude", "latitude", "capitalCity", "fromPrice", "tRating", "name", "strapline" }));

		Mockito.when(featureService.getValuesForFeatures(featureDescriptorList, sourceData, new Date(), "")).thenReturn(
				mapValuesForFeatures);

		try
		{
			categoriesPopulator.populate(sourceData, targetData);

			assertThat(targetData, is(notNullValue()));

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
					final AccommodationModel model = (AccommodationModel) featureListIterator.next();

					assertThat(model.getCode(), is("002240"));

				}
			}
		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}
}
