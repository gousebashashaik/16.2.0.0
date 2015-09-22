/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;
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

import com.thoughtworks.xstream.converters.ConversionException;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FacilityModel;

import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author sunil.bd
 * 
 */
public class AccommodationNamePopulatorTest {

	@InjectMocks
	private AccommodationNamePopulator namePopulator = new AccommodationNamePopulator();

	@Mock
	private FeatureService featureService;
	
	@Mock
	private TypeService typeService;

	private AccommodationModel sourceModel;

	private AccommodationViewData targetData;
	private Map<String, List<Object>> mapValuesForFeatures;

	/**
	 * @throws java.lang.Exception
	 */
	
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		targetData = new AccommodationViewData();

		List<ProductListComponentModel> productListComponentModelList = new ArrayList<ProductListComponentModel>();

		FacilityModel facilityModel = new FacilityModel();
		facilityModel.setCode("002240");

		ProductListComponentModel componentModel = new ProductListComponentModel();
		componentModel.setName("Product Name");
		productListComponentModelList.add(componentModel);
		
		sourceModel = new AccommodationModel();
		sourceModel.setCode("002240");
		sourceModel.setReviewRating(new Double(20.00));
		sourceModel.setReviewsCount(23);
		sourceModel.setProductListComponents(productListComponentModelList);
      sourceModel.setBrands(Arrays.asList( BrandType.FC));
		
		List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {

		try {
			List<String> featureDescriptorList = new ArrayList(
					Arrays.asList(new String[] { "tRating", "productName" }));
			
			Mockito.when(
					featureService.getValuesForFeatures(featureDescriptorList,
							sourceModel, new Date(), "")).thenReturn(
					mapValuesForFeatures);
			
			namePopulator.populate(sourceModel, targetData);

			assertNotNull(targetData);

			Map<String, List<Object>> expexteResult = targetData
					.getFeatureCodesAndValues();

			Set set = expexteResult.entrySet();
			Iterator setIterator = set.iterator();

			while (setIterator.hasNext()) {
				Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) setIterator
						.next();

				List<Object> featureList = keyVal.getValue();

				Iterator featureListIterator = featureList.iterator();
				while (featureListIterator.hasNext()) {

					Object object = featureListIterator.next();
					AccommodationModel model = (AccommodationModel) object;
					
					assertThat(model.getCode(), is("002240"));
					assertThat(model.getReviewRating(), is(20.00));
					assertThat(model.getReviewsCount(), is(23));
					assertThat(model.getProductListComponents().size(), is(1));
				}
			}
		} catch (ConversionException conversionException) {
			conversionException.printStackTrace();
		}
	}
}
