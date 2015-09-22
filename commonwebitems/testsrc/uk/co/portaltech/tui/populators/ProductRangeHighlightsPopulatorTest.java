/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;


/**
 * @author sureshbabu.rn
 * 
 */
public class ProductRangeHighlightsPopulatorTest
{

	@InjectMocks
	ProductRangeHighlightsPopulator productRangeHighlightsPopulator = new ProductRangeHighlightsPopulator();

	@Mock
	FeatureService featureService;

	ProductRangeModel source;
	ProductRangeViewData target;
	Map<String, List<Object>> mapForFeatureValueAndCodes;

	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);
		source = new ProductRangeModel();
		target = new ProductRangeViewData();

		final List<FeatureValueSetModel> featureValueSetModelList = new ArrayList<FeatureValueSetModel>();

		final FeatureValueSetModel featureValueSetModel = new FeatureValueSetModel();

		featureValueSetModel.setCode("02312");
		featureValueSetModel.setCreationtime(new Date());

		featureValueSetModelList.add(featureValueSetModel);

		source.setFeatureValueSets(featureValueSetModelList);
		source.setCode("002240");
		source.setOrder(12);
		source.setBrands(Arrays.asList(BrandType.TH));

		final List<Object> value = new ArrayList<Object>();
		value.add(source);

		mapForFeatureValueAndCodes = new HashMap<String, List<Object>>();
		mapForFeatureValueAndCodes.put("01234", value);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.ProductRangeHighlightsPopulator#populate(ProductRangeModel, ProductRangeViewData)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{

		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyCollection(), Mockito.any(ProductRangeModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(mapForFeatureValueAndCodes);

		assertNotNull(source);
		assertNotNull(target);

		productRangeHighlightsPopulator.populate(source, target);

		assertNotNull(target);

		final Map<String, List<Object>> expectedResults = target.getFeatureCodesAndValues();

		final Set set = expectedResults.entrySet();
		final java.util.Iterator iterator = set.iterator();

		while (iterator.hasNext())
		{

			final Map.Entry<String, List<Object>> keyValue = (Map.Entry<String, List<Object>>) iterator.next();

			final List<Object> valueList = keyValue.getValue();

			final java.util.Iterator<Object> valueListIterator = valueList.iterator();

			while (valueListIterator.hasNext())
			{

				final Object object = valueListIterator.next();

				final ProductRangeModel productRangeModel = (ProductRangeModel) object;

				assertNotNull(productRangeModel);
				assertThat(productRangeModel.getCode(), is("002240"));
				assertThat(productRangeModel.getOrder(), is(12));
				assertThat(productRangeModel.getFeatureValueSets().size(), is(1));

			}




		}

	}

}
