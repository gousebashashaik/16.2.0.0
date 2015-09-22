/**
 * 
 */
package uk.co.portaltech.tui.populators;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;


/**
 * @author venkatasuresh.t
 * 
 */
public class ProductRangeCategoryBasicPopulatorTest
{
	@InjectMocks
	ProductRangeCategoryBasicPopulator productRangeCategoryBasicPopulator = new ProductRangeCategoryBasicPopulator();

	@Mock
	private ProductRangeCategoryModel sourceModel;

	@Mock
	private FeatureService featureService;

	private final Map<String, List<Object>> results = new HashMap<String, List<Object>>();
	ProductRangeCategoryModel sourceModels;
	ProductRangeCategoryViewData targetData;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		targetData = new ProductRangeCategoryViewData();

	}

	@Test
	public void test()
	{
		Mockito.when(sourceModel.getName()).thenReturn("");
		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyCollection(), Mockito.any(ProductRangeCategoryModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(results);
		Mockito.when(sourceModel.getBrands()).thenReturn(Arrays.asList(BrandType.FC));
		productRangeCategoryBasicPopulator.populate(sourceModel, targetData);

		Assert.assertNotNull(sourceModel);
		Assert.assertNotNull(targetData);
	}

}
