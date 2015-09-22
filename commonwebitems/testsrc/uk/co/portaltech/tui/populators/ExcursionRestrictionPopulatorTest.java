/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.type.TypeService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author sunil.bd
 * 
 */
public class ExcursionRestrictionPopulatorTest {

	@InjectMocks
	private ExcursionRestrictionPopulator restrictionPopulator = new ExcursionRestrictionPopulator();
	
	@Mock
	private FeatureService featureService;
	
	@Mock
	private TypeService typeService;

	private Map<String, List<Object>> mapValuesForFeatures;
	private ExcursionModel sourceData;
	private ExcursionViewData targetData;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		sourceData = new ExcursionModel();

		targetData = new ExcursionViewData();

		Set<ExcursionPriceModel> excursionPriceModelSet = new HashSet<ExcursionPriceModel>();

		ExcursionPriceModel priceModel = new ExcursionPriceModel();
		priceModel.setCode("002240");
		priceModel.setChildPrice(new BigDecimal(33.00));
		priceModel.setCurrency("Dollar$");
		priceModel.setTicketName("Normal");

		excursionPriceModelSet.add(priceModel);

		Collection<CMSProductRestrictionModel> cmsProductRestrictionModelList = new ArrayList<CMSProductRestrictionModel>();
		CMSProductRestrictionModel restrictionModel = new CMSProductRestrictionModel();
		Collection<ProductModel> productModelList = new ArrayList<ProductModel>();

		ProductModel productModel = new ProductModel();

		productModel.setCode("002240");
		productModel.setEan("EAN");
		productModel.setManufacturerAID("Manufacturer AID");
		productModel.setManufacturerName("Manufacturer Name");
		productModel.setOrder(32);
		productModel.setSummary("Good for summer", Locale.FRANCE);

		productModelList.add(productModel);

		List<FeatureValueSetModel> valueSetModelList = new ArrayList<FeatureValueSetModel>();
		FeatureValueSetModel valueSetModel = new FeatureValueSetModel();
		valueSetModel.setCode("002240");
		valueSetModel.setProduct(productModel);
		valueSetModelList.add(valueSetModel);

		restrictionModel.setUid("RW12ST");
		restrictionModel.setProducts(productModelList);

		cmsProductRestrictionModelList.add(restrictionModel);

		sourceData.setCode("002240");
		sourceData.setRestrictions(cmsProductRestrictionModelList);
		sourceData.setExcursionPrices(excursionPriceModelSet);
		sourceData.setOrder(32);
		sourceData.setFeatureValueSets(valueSetModelList);
		sourceData.setName("FRANCE", Locale.FRANCE);
		sourceData.setEan("EAN");
		sourceData.setDescription("FRANCE Desc", Locale.FRANCE);
		sourceData.setManufacturerName("MANUFACTURER NAME");
		sourceData.setManufacturerAID("RW12ST");
		sourceData.setMaxOrderQuantity(56);
		sourceData.setMinOrderQuantity(12);
		sourceData.setRemarks("Remarks", Locale.FRANCE);
		sourceData.setPriceQuantity(44.00);
		sourceData.setSummary("Summary", Locale.FRANCE);
		sourceData.setSegment("Segment", Locale.FRANCE);
		

		List<Object> value = new ArrayList<Object>();
		value.add(sourceData);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {
		Mockito.when(
				featureService.getValuesForFeatures(
						Arrays.asList(new String[] { "restrictionInfo" }),
						sourceData, new Date(), "")).thenReturn(
				mapValuesForFeatures);

		assertNotNull(sourceData);
		assertNotNull(targetData);

		restrictionPopulator.populate(sourceData, targetData);

		assertNotNull(targetData);

		Map<String, List<Object>> expectedResult = targetData
				.getFeatureCodesAndValues();

		Set set = expectedResult.entrySet();

		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, List<Object>> keyValues = (Map.Entry<String, List<Object>>) iterator
					.next();

			List<Object> value = keyValues.getValue();

			Iterator valueIterator = value.iterator();
			while (valueIterator.hasNext()) {
				Object object = valueIterator.next();

				ExcursionModel model = (ExcursionModel) object;

				assertThat(model.getCode(), is("002240"));
				assertThat(model.getName(Locale.FRANCE), is("FRANCE"));
				assertThat(model.getExcursionPrices().size(), is(1));
				assertThat(model.getRestrictions().size(), is(1));
				assertThat(model.getFeatureValueSets().size(), is(1));
				assertThat(model.getOrder(), is(32));
				assertThat(model.getSummary(Locale.FRANCE), is("Summary"));
				assertThat(model.getPriceQuantity(), is(44.00));
				assertThat(model.getEan(), is("EAN"));
				assertThat(model.getManufacturerAID(), is("RW12ST"));
				assertThat(model.getManufacturerName(), is("MANUFACTURER NAME"));
				assertThat(model.getMaxOrderQuantity(), is(56));
				assertThat(model.getMinOrderQuantity(), is(12));
				assertThat(model.getSegment(Locale.FRANCE), is("Segment"));
				assertThat(model.getDescription(Locale.FRANCE),
						is("FRANCE Desc"));
				assertThat(model.getRemarks(Locale.FRANCE), is("Remarks"));

			}
		}
	}
}
