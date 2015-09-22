/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import de.hybris.platform.servicelayer.type.TypeService;

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

import com.thoughtworks.xstream.converters.ConversionException;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author sunil.bd
 * 
 */
public class AccommodationEntertainmentPopulatorTest {

	@InjectMocks
	private AccommodationEntertainmentPopulator entertainmentPopulator = new AccommodationEntertainmentPopulator();

	@Mock
	private FeatureService featureService;
	
	@Mock
	private TypeService typeService;
	@Mock
	private BrandUtils brandUtils;
	
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

		Set<FeatureDescriptorModel> featureDescriptorModelList = new HashSet<FeatureDescriptorModel>();

		FeatureDescriptorModel featureDescriptorModel = new FeatureDescriptorModel();
		featureDescriptorModel.setCode("002240");

		featureDescriptorModelList.add(featureDescriptorModel);

		sourceModel = new AccommodationModel();
		sourceModel.setCode("002240");
		sourceModel.setReviewRating(3.00D);
		sourceModel.setFeatureDescriptors(featureDescriptorModelList);
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
			Mockito.when(
					featureService.getValuesForFeatures(
							Arrays.asList(new String[] {
									"familyEntertainmentOverview",
									"adultEntertainmentOverview" }),
							sourceModel, new Date(), "")).thenReturn(
					mapValuesForFeatures);

			entertainmentPopulator.populate(sourceModel, targetData);

			assertNotNull(targetData);

			Map<String, List<Object>> expexteResult = targetData
					.getFeatureCodesAndValues();

			Set set = expexteResult.entrySet();

			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator
						.next();

				List<Object> featureList = keyVal.getValue();

				Iterator iterator2 = featureList.iterator();

				while (iterator2.hasNext()) {

					Object object = iterator2.next();
					AccommodationModel model = (AccommodationModel) object;

					assertThat(model.getCode(), is("002240"));
					assertThat(model.getReviewRating(), is(3.00D));
				}
			}
		} catch (ConversionException conversionException) {
			conversionException.printStackTrace();
		}
	}
}
