/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.facades.impl.DefaultLocationFacade;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.web.view.data.LocationData;

/**
 * @author kavita.na
 * 
 */
public class DefaultLocationFacadeServiceTest {

	@Mock
	private LocationData locationData;

	private static final String CODE = "IND";
	private static final String CODE1 = "MDV";
	private static final String CODE2 = "ESP";
	private static final String DESC = "This trip combines two contrasting attractions";
	private static final String DESC2 = "Our First Choice team have put together a cast of highly trained entertainers";
	private static final String NAME = "India";
	private static final String NAME2 = "Spain";
	private static final String NAME1 = "Maldives";

	@InjectMocks
	private DefaultLocationFacade locationFacde = new DefaultLocationFacade();

	@Mock
	private CategoryService categoryService;

	@Mock
	private ConfigurablePopulator<LocationModel, LocationData, LocationOption> defaultLocationConfiguredPopulator;

	@Mock
	private CMSSiteService siteService;

	@Mock
	private LocationConverter locationConverter;

	@Mock
	private CacheWrapper<String, LocationData> locationDataCache;

	@Mock
	private SiteAwareKeyGenerator keyGenerator;

	@Mock
	private CacheUtil cacheUtil;

	@Mock
	private SessionService sessionService;

	@Mock
	private LocationService locationService;

	private LocationModel locationModel1, locationModel, locationModel2;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

		locationModel1 = new LocationModel();
		locationModel1.setCode(CODE);

		locationModel1.setType(LocationType.COUNTRY);

		locationModel = new LocationModel();
		locationModel.setCode(CODE1);

		locationModel.setType(LocationType.DESTINATION);

		locationModel2 = new LocationModel();
		locationModel2.setCode(CODE2);

	}

	public LocationData getTestData() {
		LocationData data = new LocationData();
		data.setCode(CODE);
		data.setDescription(DESC);

		return data;
	}

	public LocationData getDummyData() {
		LocationData data1 = new LocationData();
		data1.setCode(CODE1);
		data1.setDescription(DESC2);

		return data1;
	}

	private CatalogVersionModel getCatalogVersion() {

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;

	}

	@Test
	public void testingGetHealthyAndSafetyEditorial() {
		final CatalogVersionModel catalog = getCatalogVersion();
		LocationData locdata = getTestData();

		Mockito.when(
				keyGenerator.generate(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("key");

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		Mockito.when(locationDataCache.get(Mockito.anyString())).thenReturn(
				null);

		Mockito.when(siteService.getCurrentCatalogVersion())
				.thenReturn(catalog);

		Mockito.when(categoryService.getCategoryForCode(Mockito.anyString()))
				.thenReturn(locationModel1);

		Mockito.when(
				locationConverter.convert(Mockito.any(LocationModel.class)))
				.thenReturn(locdata);

		Mockito.doNothing()
				.when(defaultLocationConfiguredPopulator)
				.populate(Mockito.any(LocationModel.class),
						Mockito.any(LocationData.class),
						Mockito.anyCollection());

		LocationData expectedResult = locationFacde
				.getPassportAndVisaEditorial(CODE);

		assertThat(expectedResult.getDescription(), is(DESC));
		assertThat(expectedResult.getCode(), is(CODE));

	}

	@Test
	public void testinggetPassportAndVisaEditorial() {
		final CatalogVersionModel catalog = getCatalogVersion();
		LocationData locdata = getTestData();

		Mockito.when(
				keyGenerator.generate(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("key");

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		Mockito.when(locationDataCache.get(Mockito.anyString())).thenReturn(
				null);

		Mockito.when(siteService.getCurrentCatalogVersion())
				.thenReturn(catalog);

		Mockito.when(siteService.getCurrentCatalogVersion())
				.thenReturn(catalog);

		Mockito.when(categoryService.getCategoryForCode(CODE)).thenReturn(
				locationModel);

		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locdata);

		Mockito.doNothing()
				.when(defaultLocationConfiguredPopulator)
				.populate(Mockito.any(LocationModel.class),
						Mockito.any(LocationData.class),
						Mockito.anyCollection());

		LocationData expectedResult = locationFacde
				.getPassportAndVisaEditorial(CODE);

		
		assertThat(expectedResult.getDescription(),
				is(locdata.getDescription()));
		assertThat(expectedResult.getLocationType(),
				is(locdata.getLocationType()));

	}

	@Test
	public void testingGetWhenToGoEditorial() {
		final CatalogVersionModel catalog = getCatalogVersion();
		LocationData locdata = getTestData();
		BrandDetails brandDetails = new BrandDetails();
		List<String> brandPks = new ArrayList<String>();
		brandDetails.setRelevantBrands(brandPks);

		Mockito.when(
				keyGenerator.generate(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("key");

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		Mockito.when(locationDataCache.get(Mockito.anyString())).thenReturn(
				null);
		Mockito.when(siteService.getCurrentCatalogVersion())
				.thenReturn(catalog);
		Mockito.when(categoryService.getCategoryForCode(CODE)).thenReturn(
				locationModel1);
		Mockito.when(locationConverter.convert(locationModel1)).thenReturn(
				locdata);
		Mockito.when(sessionService.getAttribute(Mockito.anyString()))
				.thenReturn(brandDetails);
		Mockito.when(
				locationService.getLocationForItem(locationModel,
						LocationType.CONTINENT, brandPks)).thenReturn(
				locationModel1);

		Mockito.doNothing()
				.when(defaultLocationConfiguredPopulator)
				.populate(Mockito.any(LocationModel.class),
						Mockito.any(LocationData.class),
						Mockito.anyCollection());
		LocationData result = locationFacde.getWhenToGoEditorial(CODE);
		assertThat(result.getCode(), is(locdata.getCode()));
		assertThat(result.getDescription(), is(locdata.getDescription()));

	}

	@Test
	public void testingGetWhereToGoData()

	{

		final CatalogVersionModel catalog = getCatalogVersion();
		LocationData locdata = getTestData();

		Mockito.when(
				keyGenerator.generate(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("key");

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		Mockito.when(locationDataCache.get(Mockito.anyString())).thenReturn(
				null);
		Mockito.when(siteService.getCurrentCatalogVersion())
				.thenReturn(catalog);
		Mockito.when(categoryService.getCategoryForCode(CODE1)).thenReturn(
				locationModel2);
		Mockito.when(
				locationConverter.convert(Mockito.any(LocationModel.class)))
				.thenReturn(locdata);
		Mockito.when(locationDataCache.get(Mockito.anyString())).thenReturn(
				locdata);
		Mockito.doNothing()
				.when(defaultLocationConfiguredPopulator)
				.populate(Mockito.any(LocationModel.class),
						Mockito.any(LocationData.class),
						Mockito.anyCollection());

		LocationData result = locationFacde.getWhereToGoData(CODE1);

		assertThat(result.getCode(), is(CODE));
		assertThat(result.getDescription(), is(DESC));

	}

}
