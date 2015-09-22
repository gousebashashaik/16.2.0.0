/**
 * 
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.quicklive.travel.model.ExcursionContactDetailsModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.PositionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.ExcursionURLResolverForLegacySystems;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author gopinath.n
 * 
 */
public class ExcursionBasicPopulatorTest {
	
	@InjectMocks
	private final ExcursionBasicPopulator excursionBasicPopulator = new ExcursionBasicPopulator();

	private ExcursionModel sourceModel;


	private PositionModel positionModel;
	private ExcursionContactDetailsModel excursionContactDetailsModel;

	private List<Object> description;

	@Mock
	private ExcursionViewData targetData;
	
	@Mock
	private TypeService typeService;

	@Mock
	private Map<String, List<Object>> features;

	@Mock
	private FeatureService featureService;

	@Mock
	private ExcursionURLResolverForLegacySystems excursionURLResolverForLegacySystems;

	private static final String EDITORIAL_CONTENT = "editorialContent";
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		sourceModel = new ExcursionModel();
		positionModel = new PositionModel();
		excursionContactDetailsModel = new ExcursionContactDetailsModel();
		targetData = new ExcursionViewData();

		positionModel.setLatitude(14.4);
		positionModel.setLongitude(24.4);

		excursionContactDetailsModel.setCode("excursioin code");
		excursionContactDetailsModel.setEmail("xxx@gmail.com");
		excursionContactDetailsModel.setFax("14003610356");
		excursionContactDetailsModel.setName("Excursion");
		excursionContactDetailsModel.setPhone("9256326994");
		sourceModel.setPosition(positionModel);
		sourceModel.setContactDetails(excursionContactDetailsModel);

		targetData.setName("Get Price");
		targetData.setBookingUrl("bookingUrl");
		targetData.setUrl("");
		targetData.setChildAgeMax("10");
		targetData.setChildAgeMin("5");
		targetData.setChildPrice("377");
		targetData.setDescription("description");
		targetData.setFromPrice("255");
		targetData.setImageUrl("imageUrl");

	}

	@Test
	public void testPopulate() {
		
		final List<String> featureDescriptorList = new ArrayList(
				Arrays.asList(new String[] { "name", "suitableFor",
						EDITORIAL_CONTENT, "duration", "childAgeMax",
						"childAgeMin" }));
		Mockito.when(
				featureService.getValuesForFeatures(featureDescriptorList,
						sourceModel, new Date(), "")).thenReturn(features);
		Mockito.when(features.get(EDITORIAL_CONTENT)).thenReturn(description);
		Mockito.when(features.remove(EDITORIAL_CONTENT))
				.thenReturn(description);
		targetData = getLatitudeAndLongitude(targetData);

		Assert.assertNotNull(sourceModel);
		Assert.assertNotNull(targetData);
		Assert.assertEquals(targetData.getChildAgeMax(), "10");
	}

	private ExcursionViewData getLatitudeAndLongitude(
			final ExcursionViewData targetData) {
		return targetData;
	}

}
