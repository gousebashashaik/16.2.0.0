/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TuiProductUrlResolver;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;


/**
 * @author sureshbabu.rn
 * 
 */
public class AccommodationInspirationMapPopulatorTest
{

	@InjectMocks
	AccommodationInspirationMapPopulator accommodationInspirationMapPopulator = new AccommodationInspirationMapPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private MediaDataPopulator mediaDataPopulator;

	@Mock
	private AccommodationSubCategoriesPopulator accomSubCategoriesPopulator;

	@Mock
	private TuiProductUrlResolver tuiProductUrlResolver;

	@Mock
	private AccommodationProductRangePopulator accommodationProductRangePopulator;

	@Mock
	private ViewSelector selector;

	@Mock
	private TuiUtilityService tuiUtilityService;

	AccommodationModel accommodationModel;
	InspirationMapViewData target;
	List<AccommodationModel> source;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		accommodationModel = new AccommodationModel();
		target = new InspirationMapViewData();
		source = new ArrayList<AccommodationModel>();
		final MediaModel mediaModel = new MediaModel();
		final Collection<CategoryModel> categoryModels = new ArrayList<CategoryModel>();

		accommodationModel.setBrands(Arrays.asList(BrandType.FC));
		accommodationModel.setThumbnail(mediaModel);
		accommodationModel.setSupercategories(categoryModels);


		source.add(accommodationModel);



	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.AccommodationInspirationMapPopulator#populate(java.util.List, uk.co.portaltech.tui.web.view.data.InspirationMapViewData)}
	 * .
	 */
	@Test
	public void testPopulate()
	{

		assertNotNull(source);
		assertNotNull(target);

		Mockito.when(tuiProductUrlResolver.resolve(accommodationModel)).thenReturn("www.tui.com");
		Mockito.when(
				featureService.getFirstFeatureValueAsString(Mockito.anyString(), Mockito.any(AccommodationModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn("marker");
		Mockito.doNothing().when(accomSubCategoriesPopulator)
				.populate(Mockito.any(AccommodationModel.class), Mockito.any(LocationData.class));
		Mockito.doNothing().when(accommodationProductRangePopulator)
				.populate(Mockito.any(AccommodationModel.class), Mockito.any(AccommodationViewData.class));
		Mockito.doNothing().when(mediaDataPopulator).populate(Mockito.any(MediaModel.class), Mockito.any(MediaViewData.class));

		accommodationInspirationMapPopulator.populate(source, target);


		assertNotNull(target);
		assertEquals(1, target.getMarkerMapViewDataList().size());


	}

}
