/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.FeatureType;
import uk.co.portaltech.travel.enums.TemporalType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author sunil.bd
 * 
 */
public class AccommodationAllInclusivePopulatorTest {

	@InjectMocks
	private AccommodationAllInclusivePopulator accommodationAllInclusivePopulator = new AccommodationAllInclusivePopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private BrandUtils brandUtils;
	
	private AccommodationModel sourceModel = new AccommodationModel();

	@Mock
	private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;
	
	private Map<String, List<Object>> mapValuesForFeatures;

	private AccommodationViewData targetData;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		targetData = new AccommodationViewData();

		CategoryModel categoryModel = new CategoryModel();

		categoryModel.setCode("002240");
		categoryModel.setCatalogVersion(getCatalogVersion());

		List<ProductFeatureModel> featureModelList = new ArrayList<ProductFeatureModel>();

		ProductFeatureModel featureModel = new ProductFeatureModel();

		featureModel.setDescription("desc");
		featureModel.setFeaturePosition(1);
		featureModel.setValueDetails("Values Details");
		featureModel.setValuePosition(2);

		featureModelList.add(featureModel);

		Set<FeatureDescriptorModel> descriptorModelSet = new HashSet<FeatureDescriptorModel>();

		FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();

		descriptorModel.setCode("002240");
		descriptorModel.setTemporalType(TemporalType.TRAVELLING);
		descriptorModel.setType(FeatureType.INTEGER);

		descriptorModelSet.add(descriptorModel);

		List<FeatureValueSetModel> valueSetModelList = new ArrayList<FeatureValueSetModel>();

		FeatureValueSetModel valueSetModel = new FeatureValueSetModel();

		valueSetModel.setCode("002240");
		valueSetModel.setCatalogVersion(getCatalogVersion());
		valueSetModel.setCategory(categoryModel);

		valueSetModelList.add(valueSetModel);

		sourceModel.setFeatureValueSets(valueSetModelList);
		sourceModel.setCode("002240");
		sourceModel.setFeatureDescriptors(descriptorModelSet);
		sourceModel.setReviewsCount(2);
		sourceModel.setReviewRating(10.00);
		sourceModel.setType(AccommodationType.HOTEL);
		sourceModel.setFeatures(featureModelList);
		sourceModel.setBrands(Arrays.asList(BrandType.FC));

		List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);

		tuiProductUrlResolver.urlSafe("URL");
		tuiProductUrlResolver.setDefaultSubPageType("pageType");
		tuiProductUrlResolver.setPattern("pattern");

	}

	private CatalogVersionModel getCatalogVersion() {

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.AccommodationAllInclusivePopulator#populate(uk.co.portaltech.travel.model.AccommodationModel, uk.co.portaltech.tui.web.view.data.AccommodationViewData)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {
		Mockito.when(
				featureService.getValuesForFeatures(
						Arrays.asList(new String[] { "allInclusiveFood",
								"allInclusiveDrink",
								"allInclusiveSportActivity" }), sourceModel,
						new Date(), brandUtils.getFeatureServiceBrand(sourceModel.getBrands()))).thenReturn(mapValuesForFeatures);
		assertNotNull(sourceModel);
		assertNotNull(targetData);

		accommodationAllInclusivePopulator.populate(sourceModel, targetData);

		assertNotNull(targetData);

		Map<String, List<Object>> expectedResult = targetData
				.getFeatureCodesAndValues();

		Set set = expectedResult.entrySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator
					.next();

			List<Object> featureList = keyVal.getValue();

			Iterator featureListIterator = featureList.iterator();

			while (featureListIterator.hasNext()) {

				Object object = featureListIterator.next();
				AccommodationModel model = (AccommodationModel) object;

				assertThat(model.getCode(), is("002240"));
				assertThat(model.getFeatureDescriptors().size(), is(1));
				assertThat(model.getType(), is(AccommodationType.HOTEL));
				assertThat(model.getFeatureValueSets().size(), is(1));
				assertThat(model.getFeatures().size(), is(1));

			}
		}

		tuiProductUrlResolver.setOverrideSubPageType("facilities");

		Mockito.when(tuiProductUrlResolver.resolve(sourceModel)).thenReturn(
				"url");

	}
}
