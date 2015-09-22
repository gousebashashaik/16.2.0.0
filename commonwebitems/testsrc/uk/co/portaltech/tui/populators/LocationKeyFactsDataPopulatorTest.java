/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.web.view.data.LocationData;


/**
 * @author vinodkumar.g
 * 
 */
public class LocationKeyFactsDataPopulatorTest
{

	@InjectMocks
	LocationKeyFactsDataPopulator locationKeyFactsDataPopulator = new LocationKeyFactsDataPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private SessionService sessionService;

	@Mock
	private LocationService tuiLocationService;

	LocationModel locationModel;
	LocationData locationData;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		locationModel = new LocationModel();
		locationData = new LocationData();
		final List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
		final CategoryModel categoryModel = new LocationModel();
		categoryModelList.add(categoryModel);

		locationModel.setBrands(Arrays.asList(BrandType.FC));
		locationModel.setSupercategories(categoryModelList);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.LocationKeyFactsDataPopulator#populate(LocationModel, LocationData)}.
	 */
	@Test
	public void testPopulate()
	{

		final Map<String, List<Object>> valuesForFeatures = new HashMap<String, List<Object>>();
		final List<Object> features = new ArrayList<Object>();
		final List<String> relevantBrands = new ArrayList<String>();
		relevantBrands.add("relevantBrands");
		final BrandDetails brandDetails = new BrandDetails();
		brandDetails.setRelevantBrands(relevantBrands);
		features.add(locationModel);
		valuesForFeatures.put("FLT_DURATION", features);
		valuesForFeatures.put("LANGUAGE", features);
		valuesForFeatures.put("TIME_ZONE", features);


		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyCollection(), Mockito.any(ItemModel.class), Mockito.any(Date.class),
						Mockito.anyString())).thenReturn(valuesForFeatures);
		Mockito.when(
				featureService.getFeatureValues(Mockito.anyString(), Mockito.any(ItemModel.class), Mockito.any(Date.class),
						Mockito.anyString())).thenReturn(features);
		Mockito.when(sessionService.getAttribute(Mockito.anyString())).thenReturn(brandDetails);
		Mockito.when(
				tuiLocationService.getLocationForItem(Mockito.any(ItemModel.class), Mockito.any(LocationType.class),
						Mockito.anyList())).thenReturn(locationModel);

		locationKeyFactsDataPopulator.populate(locationModel, locationData);

		assertNotNull(locationModel);
		assertNotNull(locationData);


		assertEquals(1, locationData.getFeatureValues("FLT_DURATION").size());
		assertEquals(1, locationData.getFeatureValues("LANGUAGE").size());
		assertEquals(1, locationData.getFeatureValues("TIME_ZONE").size());
		assertEquals(locationModel, locationData.getFeatureValues("FLT_DURATION").get(0));
		assertEquals(locationModel, locationData.getFeatureValues("LANGUAGE").get(0));
		assertEquals(locationModel, locationData.getFeatureValues("TIME_ZONE").get(0));
	}

}
