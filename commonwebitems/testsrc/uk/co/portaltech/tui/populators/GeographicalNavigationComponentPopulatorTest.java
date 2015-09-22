/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.components.model.GeographicalNavigationComponentModel;
import uk.co.portaltech.tui.web.view.data.GeographicalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.TUICategoryData;


/**
 * @author vinodkumar.g
 * 
 */
public class GeographicalNavigationComponentPopulatorTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@InjectMocks
	GeographicalNavigationComponentPopulator geographicalNavigationComponentPopulator = new GeographicalNavigationComponentPopulator();

	@Mock
	private CategoryService categoryService;
	@Mock
	private SessionService sessionService;
	@Mock
	private BrandDetails brandDetailsMock;
	@Mock
	private LocationService locationService;
	@Mock
	private Converter<CategoryModel, TUICategoryData> tuiCategoryDataConverter;
	@Mock
	private RecursivePopulator<CategoryModel, TUICategoryData> tuiCategoryDataPopulatorWithDepth;
	@Mock
	private TUICategoryData tuiCategoryDataMock;
	@Mock
	private List<TUICategoryData> tuiCategoryDataListMock;

	GeographicalNavigationComponentModel geographicalNavigationComponentModel;
	GeographicalNavigationComponentViewData geographicalNavigationComponentViewData;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		geographicalNavigationComponentModel = new GeographicalNavigationComponentModel();
		geographicalNavigationComponentViewData = new GeographicalNavigationComponentViewData();
		final Collection<String> categoryCodes = new ArrayList<String>();
		categoryCodes.add("staged");

		geographicalNavigationComponentModel.setCategoryCodes(categoryCodes);
		geographicalNavigationComponentModel.setNavigationLevelDepth(1);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.GeographicalNavigationComponentPopulator#populate(uk.co.portaltech.tui.components.model.GeographicalNavigationComponentModel, uk.co.portaltech.tui.web.view.data.GeographicalNavigationComponentViewData)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{

		final CategoryModel categoryModel = new LocationModel();
		categoryModel.setCode("123");
		final BrandDetails brandDetails = new BrandDetails();
		final List<String> relevantBrands = new ArrayList<String>();
		relevantBrands.add("tui");
		final CategoryModel locationModel = new LocationModel();
		final List<LocationModel> locationModelList = new ArrayList<LocationModel>();
		locationModel.setCode("location");
		locationModelList.add((LocationModel) locationModel);
		final TUICategoryData tuiCategoryData = new TUICategoryData();
		tuiCategoryData.setName("tuicategory");

		Mockito.when(categoryService.getCategoryForCode(Mockito.anyString())).thenReturn(categoryModel);
		Mockito.when(sessionService.getAttribute(Mockito.anyString())).thenReturn(brandDetails);
		Mockito.when(brandDetailsMock.getRelevantBrands()).thenReturn(relevantBrands);
		Mockito.when(locationService.getLocationsFilteredByBrand(Mockito.anyList(), Mockito.anyList())).thenReturn(
				locationModelList);
		Mockito.when(tuiCategoryDataConverter.convert(Mockito.any(CategoryModel.class))).thenReturn(tuiCategoryData);
		Mockito.doNothing().when(tuiCategoryDataPopulatorWithDepth)
				.populate(Mockito.any(CategoryModel.class), Mockito.any(TUICategoryData.class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.when(tuiCategoryDataMock.getName()).thenReturn("tuiCategory");
		Mockito.when(tuiCategoryDataListMock.get(0)).thenReturn(tuiCategoryData);

		geographicalNavigationComponentPopulator.populate(geographicalNavigationComponentModel,
				geographicalNavigationComponentViewData);


		assertNotNull(geographicalNavigationComponentModel);
		assertNotNull(geographicalNavigationComponentViewData);

		assertEquals((int) geographicalNavigationComponentViewData.getNavigationLevelDepth(), 1);
		assertEquals(geographicalNavigationComponentViewData.getLocationName(), "tuicategory");



	}


}
