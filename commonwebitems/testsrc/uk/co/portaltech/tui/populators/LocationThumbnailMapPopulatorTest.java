/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
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
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.LocationData;


/**
 * @author vinodkumar.g
 * 
 */
public class LocationThumbnailMapPopulatorTest
{

	@InjectMocks
	LocationThumbnailMapPopulator locationThumbnailMapPopulator = new LocationThumbnailMapPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private TUIUrlResolver<LocationModel> tuiCategoryModelUrlResolver;

	LocationModel locationModel;
	LocationData locationData;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		locationModel = new LocationModel();
		locationData = new LocationData();

		locationModel.setBrands(Arrays.asList(BrandType.FC));
		locationModel.setType(LocationType.COUNTRY);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.LocationThumbnailMapPopulator#populate(uk.co.portaltech.travel.model.LocationModel, uk.co.portaltech.tui.web.view.data.LocationData)}
	 * .
	 */
	@Test
	public void testPopulate()
	{

		final Map<String, List<Object>> valuesForFeatures = new HashMap<String, List<Object>>();
		final List<Object> features = new ArrayList<Object>();
		valuesForFeatures.put("feature", features);

		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyCollection(), Mockito.any(LocationModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(valuesForFeatures);
		Mockito.when(tuiCategoryModelUrlResolver.resolve(Mockito.any(LocationModel.class))).thenReturn("www.tui.com/home.html");
		Mockito.when(tuiCategoryModelUrlResolver.getTabUrl(Mockito.anyString(), Mockito.anyString())).thenReturn("www.tui.com");

		assertNotNull(locationModel);
		assertNotNull(locationData);

		locationThumbnailMapPopulator.populate(locationModel, locationData);

		assertEquals("www.tui.com", locationData.getThingstodoMapUrl());
		assertEquals(LocationType.COUNTRY, locationData.getLocationType());
	}

}
