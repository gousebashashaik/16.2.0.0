/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author sunil.bd
 * 
 */
public class AccommodationHighlightsPopulatorTest {

	@InjectMocks
	private AccommodationHighlightsPopulator highlightsPopulator = new AccommodationHighlightsPopulator();

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

		List<FeatureValueSetModel> valueSetModelList = new ArrayList<FeatureValueSetModel>();

		FeatureValueSetModel valueSetModel = new FeatureValueSetModel();

		valueSetModel.setCode("002240");

		valueSetModelList.add(valueSetModel);

		List<FacilityModel> facilityModelList = new ArrayList<FacilityModel>();
		FacilityModel facilityModel = new FacilityModel();

		facilityModel.setName("France", Locale.FRANCE);

		facilityModelList.add(facilityModel);

		sourceModel = new AccommodationModel();
		sourceModel.setCode("002240");
		sourceModel.setReviewRating(new Double(20.00));
		sourceModel.setReviewsCount(23);
		sourceModel.setFacilities(facilityModelList);
		sourceModel.setFeatureValueSets(valueSetModelList);
		sourceModel.setOrder(32);
		sourceModel.setBrands(Arrays.asList(BrandType.FC));

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
					Arrays.asList(new String[] { "usps" }));
			
			Mockito.when(
					featureService.getValuesForFeatures(featureDescriptorList,
							sourceModel, new Date(), "")).thenReturn(
					mapValuesForFeatures);
			assertNotNull(sourceModel);
			assertNotNull(targetData);

			highlightsPopulator.populate(sourceModel, targetData);

			assertNotNull(targetData);

			Map<String, List<Object>> expectedResult = targetData
					.getFeatureCodesAndValues();
			Set set = expectedResult.entrySet();
			Iterator iterator = set.iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, List<Object>> keyValue = (Map.Entry<String, List<Object>>) iterator
						.next();

				List<Object> valueList = keyValue.getValue();

				Iterator valueListIterator = valueList.iterator();

				while (valueListIterator.hasNext()) {
					Object object = valueListIterator.next();

					AccommodationModel model = (AccommodationModel) object;

					assertNotNull(model);
					assertThat(model.getCode(), is("002240"));
					assertThat(model.getOrder(), is(32));
					assertThat(model.getFacilities().size(), is(1));
					assertThat(model.getReviewRating(), is(20.00));
					assertThat(model.getReviewsCount(), is(23));
					
				}
			}
		} catch (ConversionException conversionException) {
			conversionException.printStackTrace();
		}
	}
}
