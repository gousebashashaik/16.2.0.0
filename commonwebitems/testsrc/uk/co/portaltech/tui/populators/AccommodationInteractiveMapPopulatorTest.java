/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertNotNull;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.AttractionType;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.converters.AttractionConverter;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MediaViewData;


/**
 * @author sureshbabu.rn
 * 
 */
public class AccommodationInteractiveMapPopulatorTest
{

	@InjectMocks
	AccommodationInteractiveMapPopulator accommodationInteractiveMapPopulator = new AccommodationInteractiveMapPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private AttractionConverter attractionConverter;

	@Mock
	private MediaDataPopulator mediaDataPopulator;

	@Mock
	private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

	@Mock
	private TUIUrlResolver<AttractionModel> attractionUrlResolver;

	@Mock
	private AccommodationProductRangePopulator accommodationProductRangePopulator;

	@Mock
	private SessionService sessionService;

	@Mock
	LocationModel locationModel;

	@Mock
	CategoryModel categoryModel1;

	@Mock
	ProductModel productModel1;

	AccommodationModel source;
	MapDataWrapper target;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		source = new AccommodationModel();
		target = new MapDataWrapper();
		final CategoryModel categoryModel = new CategoryModel();
		final Collection<CategoryModel> categoryModels = new ArrayList<CategoryModel>();


		categoryModels.add(categoryModel);

		source.setSupercategories(categoryModels);


	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.AccommodationInteractiveMapPopulator#populate(uk.co.portaltech.travel.model.AccommodationModel, uk.co.portaltech.tui.web.view.data.MapDataWrapper)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{

		final AttractionModel attractionModel = new AttractionModel();
		attractionModel.setAttractionType(AttractionType.BEACH);
		final Collection<AttractionModel> attractionModels = new ArrayList<AttractionModel>();
		attractionModels.add(attractionModel);
		final Map<String, List<Object>> map = new HashMap<String, List<Object>>();

		Mockito.when(locationModel.getAttractions()).thenReturn(attractionModels);
		Mockito.when(locationModel.getBrands()).thenReturn(Arrays.asList(BrandType.FC));
		Mockito.when(
				featureService.getFirstFeatureValueAsString(Mockito.anyString(), Mockito.any(LocationModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn("goa");
		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyList(), Mockito.any(AttractionModel.class), Mockito.any(Date.class),
						Mockito.anyString())).thenReturn(map);
		Mockito.when(attractionUrlResolver.resolve(Mockito.any(AttractionModel.class))).thenReturn("www.tui.com");
		Mockito.doNothing().when(mediaDataPopulator).populate(Mockito.any(MediaModel.class), Mockito.any(MediaViewData.class));
		/*
		 * Mockito.when(categoryModel1 instanceof LocationModel).thenReturn(true); Mockito.when(productModel1 instanceof
		 * AccommodationModel).thenReturn(true);
		 */

		assertNotNull(source);
		assertNotNull(target);

		accommodationInteractiveMapPopulator.populate(source, target);

		assertNotNull(target);

	}

}
