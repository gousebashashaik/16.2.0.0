/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TuiCategoryUrlResolver;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;


/**
 * @author vinodkumar.g
 * 
 */
public class InspirationMapPopulatorTest
{

	@InjectMocks
	InspirationMapPopulator inspirationMapPopulator = new InspirationMapPopulator();

	@Mock
	private TuiCategoryUrlResolver tuiCategoryUrlResolver;

	@Mock
	private FeatureService featureService;

	@Mock
	private ViewSelector selector;

	@Mock
	private LocationSubCategoriesPopulator locationSubCategoriesPopulator;

	List<LocationModel> source;
	InspirationMapViewData target;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		source = new ArrayList<LocationModel>();
		final LocationModel locationModel = new LocationModel();
		target = new InspirationMapViewData();

		source.add(locationModel);

		locationModel.setBrands(Arrays.asList(BrandType.FC));
		locationModel.setCode("fc");

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.InspirationMapPopulator#populate(java.util.List, uk.co.portaltech.tui.web.view.data.InspirationMapViewData)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{

		final AttractionModel attractionModel = new AttractionModel();
		final Collection<AttractionModel> attractionModels = new ArrayList<AttractionModel>();
		final LocationModel locationModel = new LocationModel();
		final Map<String, List<Object>> features = new HashMap<String, List<Object>>();


		attractionModels.add(attractionModel);

		final LocationModel locationModelMock = Mockito.mock(LocationModel.class);

		Mockito.when(tuiCategoryUrlResolver.resolve(locationModel)).thenReturn("www.tui.com");
		Mockito.when(locationModelMock.getAttractions()).thenReturn(attractionModels);
		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyCollection(), Mockito.any(LocationModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(features);
		Mockito.when(selector.checkIsMobile()).thenReturn(false);
		Mockito.doNothing().when(locationSubCategoriesPopulator)
				.populate(Mockito.any(LocationModel.class), Mockito.any(LocationData.class));


		inspirationMapPopulator.populate(source, target);

		assertNotNull(source);
		assertNotNull(target);
		assertEquals(1, target.getMarkupListCount());
		assertEquals("fc", target.getMarkerMapViewDataList().get(0).getCode());
	}

}
