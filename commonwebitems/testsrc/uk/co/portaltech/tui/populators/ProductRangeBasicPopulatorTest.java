/**
 * 
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.core.model.ItemModel;

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

import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author venkatasuresh.t
 * 
 */
public class ProductRangeBasicPopulatorTest {
	@InjectMocks
	ProductRangeBasicPopulator productRangeBasicPopulator = new ProductRangeBasicPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private ItemModel itemModel;

	@Mock
	private ProductRangeModel source;

	private Map<String, List<Object>> results = new HashMap<String, List<Object>>();

	ProductRangeViewData target;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		target = new ProductRangeViewData();

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.ProductRangeBasicPopulator#populate(uk.co.portaltech.travel.model.ProductRangeModel, uk.co.portaltech.tui.web.view.data.ProductRangeViewData)}
	 * .
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testPopulate() {

		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyCollection(),
						Mockito.any(ProductRangeModel.class),
						Mockito.any(Date.class), Mockito.anyString()))
				.thenReturn(results);
		Mockito.when(source.getName()).thenReturn("");
		productRangeBasicPopulator.populate(source, target);
		
		Assert.assertNotNull(source);
		Assert.assertNotNull(target);
		
	}

}
