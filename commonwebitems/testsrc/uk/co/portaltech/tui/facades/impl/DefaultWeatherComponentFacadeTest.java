/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade;
import uk.co.portaltech.tui.web.view.data.WeatherViewData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author sureshbabu.rn
 * 
 */
public class DefaultWeatherComponentFacadeTest
{

	@InjectMocks
	private final DefaultWeatherComponentFacade defaultWeatherComponentFacade = new DefaultWeatherComponentFacade();

	@Mock
	Converter<WeatherModel, WeatherViewData> weatherViewDataConverter;

	@Mock
	FeatureService featureService;

	@Mock
	Populator<WeatherModel, WeatherViewData> weatherViewDataPopulator;

	@Mock
	CategoryService categoryService;

	@Mock
	ProductService productService;

	@Mock
	LocationModel locationModel3;

	@Mock
	LocationService locationService;

	@Mock
	SessionService sessionService;

	@Mock
	BrandDetails brandDetails2;

	LocationModel locationModel, locationModel2, prodLocation, resortModel;
	WeatherModel weatherModel;
	WeatherViewData weatherViewData;
	WeatherTypeModel weatherTypeModel;
	List<WeatherTypeModel> weatherTypeModelList;
	FeatureValueSetModel featureValueSetModel;
	List<FeatureValueSetModel> featureValueSetModelList;
	List<Object> feature;
	CatalogVersionModel catalogVersionModel;
	FeatureDescriptorModel featureDescriptorModel;
	Set<FeatureDescriptorModel> featureDescriptorModels;
	String categoryCode, prodCode;
	CategoryModel categoryModel;
	List<CategoryModel> categoryModelList;
	ProductModel productModel;
	AccommodationModel accommodationModel;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		locationModel = new LocationModel();
		locationModel2 = new LocationModel();
		weatherModel = new WeatherModel();
		weatherViewData = new WeatherViewData();
		weatherTypeModel = new WeatherTypeModel();
		weatherTypeModelList = new ArrayList<WeatherTypeModel>();
		featureValueSetModel = new FeatureValueSetModel();
		featureValueSetModelList = new ArrayList<FeatureValueSetModel>();
		feature = Mockito.mock(List.class);
		catalogVersionModel = new CatalogVersionModel();
		featureDescriptorModel = new FeatureDescriptorModel();
		featureDescriptorModels = new HashSet<FeatureDescriptorModel>();
		categoryModel = new CategoryModel();
		accommodationModel = new AccommodationModel();
		categoryModelList = new ArrayList<CategoryModel>();

		accommodationModel.setCode("4523");

		categoryModel.setCode("012365");
		categoryModel.setCreationtime(new Date());
		categoryModel.setCatalogVersion(catalogVersionModel);
		categoryModel.setFeatureDescriptors(featureDescriptorModels);
		categoryModel.setCategories(categoryModelList);
		categoryModelList.add(categoryModel);

		featureDescriptorModel.setCatalogVersion(catalogVersionModel);
		featureDescriptorModel.setCode("descriptor");
		featureDescriptorModel.setCreationtime(new Date());
		featureDescriptorModel.setDefaultFeatureValueSets(featureValueSetModelList);

		featureDescriptorModels.add(featureDescriptorModel);

		featureValueSetModel.setCode("1234");

		weatherTypeModelList.add(weatherTypeModel);

		weatherModel.setWeatherTypes(weatherTypeModelList);

		locationModel.setCode("bng");
		locationModel.setCreationtime(new Date());
		locationModel.setWeather(weatherModel);
		locationModel.setCatalogVersion(catalogVersionModel);
		locationModel.setFeatureDescriptors(featureDescriptorModels);
		locationModel.setCategories(categoryModelList);
		locationModel.setBrands(Arrays.asList(BrandType.TH));

		locationModel2.setCode("hyd");
		locationModel2.setCreationtime(new Date());
		locationModel2.setWeather(weatherModel);
		locationModel2.setFeatureDescriptors(featureDescriptorModels);

		featureValueSetModelList.add(featureValueSetModel);
		locationModel.setFeatureValueSets(featureValueSetModelList);
		locationModel2.setFeatureValueSets(featureValueSetModelList);

		feature.add(locationModel);
		feature.add(locationModel2);
		// featureService.addFeatureValu

		weatherViewData.setLocationName("munich");


	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade#getWeatherData(uk.co.portaltech.travel.model.LocationModel)}
	 * .
	 */
	@Test
	public void testGetWeatherData()
	{

		final List<Object> feature1 = Mockito.mock(List.class);
		final LocationModel locationModelMock = Mockito.mock(LocationModel.class);
		final WeatherViewData weatherViewDataMock = Mockito.mock(WeatherViewData.class);

		Mockito.when(weatherViewDataConverter.convert(weatherModel)).thenReturn(weatherViewData);
		Mockito.when(
				featureService.getFeatureValues(Mockito.anyString(), Mockito.any(LocationModel.class), Mockito.any(Date.class),
						Mockito.anyString())).thenReturn(feature1);
		Mockito.when(feature1.get(0)).thenReturn(locationModelMock);

		Mockito.when(locationModelMock.toString()).thenReturn("munich");
		Mockito.doNothing().when(weatherViewDataPopulator).populate(weatherModel, weatherViewData);
		Mockito.doNothing().when(weatherViewDataMock).setLocationName(null);

		final String expectedResult = "munich";
		final String actualResult = defaultWeatherComponentFacade.getWeatherData(locationModel).getLocationName();

		assertNotNull(defaultWeatherComponentFacade.getWeatherData(locationModel));
		assertEquals(expectedResult, actualResult);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade#getCurrentLocationDetails(java.lang.String, java.lang.String)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testGetCurrentLocationDetails_1()
	{
		categoryCode = "01230";
		prodCode = "12453";
		prodLocation = new LocationModel();
		final BrandDetails brandDetails = new BrandDetails();
		brandDetails.setSiteUid("branddetails");
		final List<String> brands = new ArrayList<String>();
		brands.add("branddetails");
		final LocationModel locationModel = Mockito.mock(LocationModel.class);
		final CategoryModel categoryForCode = Mockito.mock(CategoryModel.class);

		Mockito.when(categoryForCode instanceof LocationModel).thenReturn(true);
		Mockito.when(categoryService.getCategoryForCode(categoryCode)).thenReturn(categoryModel);
		Mockito.when(sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS)).thenReturn(brandDetails);
		Mockito.when(brandDetails2.getRelevantBrands()).thenReturn(brands);
		Mockito.when((AccommodationModel) productService.getProductForCode(prodCode)).thenReturn(accommodationModel);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.RESORT, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.DESTINATION, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.REGION, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.COUNTRY, brands)).thenReturn(locationModel);

		assertNull(defaultWeatherComponentFacade.getCurrentLocationDetails(categoryCode, prodCode));

	}

	@Test
	public void testGetCurrentLocationDetails_2()
	{
		categoryCode = "";
		prodCode = "1245";
		prodLocation = new LocationModel();
		prodLocation.setCode("bng");
		final BrandDetails brandDetails = new BrandDetails();
		brandDetails.setSiteUid("branddetails");
		final List<String> brands = new ArrayList<String>();
		brands.add("brand");

		Mockito.when(categoryService.getCategoryForCode(categoryCode)).thenReturn(categoryModel);
		Mockito.when((AccommodationModel) productService.getProductForCode(prodCode)).thenReturn(accommodationModel);
		Mockito.when(sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS)).thenReturn(brandDetails);
		Mockito.when(brandDetails2.getRelevantBrands()).thenReturn(brands);
		Mockito.when(locationService.getLocationForItem(prodLocation, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.RESORT, brands)).thenReturn(null);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.DESTINATION, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.REGION, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(prodLocation, LocationType.COUNTRY, brands)).thenReturn(locationModel);

		final String expected = "bng";
		final String actual = prodLocation.getCode();
		assertEquals(expected, actual);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade#getDestinationsForCountryOrRegion(uk.co.portaltech.travel.model.LocationModel, int)}
	 * .
	 */
	@Test
	public void testGetDestinationsForCountryOrRegion()
	{

		assertEquals(defaultWeatherComponentFacade.getDestinationsForCountryOrRegion(locationModel, 1).getCode(), "bng");
		assertEquals(defaultWeatherComponentFacade.getDestinationsForCountryOrRegion(locationModel, 1).getWeather(), weatherModel);
		assertNotNull(defaultWeatherComponentFacade.getDestinationsForCountryOrRegion(locationModel, 1));
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade#getDestinations(uk.co.portaltech.travel.model.LocationModel)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testGetDestinations()
	{
		final CategoryModel subCategoryModel = Mockito.mock(CategoryModel.class);

		Mockito.when(subCategoryModel instanceof LocationModel).thenReturn(true);
		Mockito.when(locationModel3.getCategories()).thenReturn(categoryModelList);

		final int expected = 0, actual = defaultWeatherComponentFacade.getDestinations(locationModel).size();

		assertNotNull(defaultWeatherComponentFacade.getDestinations(locationModel));
		assertEquals(expected, actual);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade#getDestinationsForResort(uk.co.portaltech.travel.model.LocationModel)}
	 * .
	 */
	@Test
	public void testGetDestinationsForResort()
	{

		final BrandDetails brandDetails = new BrandDetails();
		brandDetails.setSiteUid("branddetails");
		final List<String> brands = new ArrayList<String>();
		brands.add("brand");
		resortModel = new LocationModel();

		Mockito.when(sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS)).thenReturn(brandDetails);
		Mockito.when(brandDetails2.getRelevantBrands()).thenReturn(brands);
		Mockito.when(locationService.getLocationForItem(resortModel, LocationType.DESTINATION, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(resortModel, LocationType.REGION, brands)).thenReturn(locationModel);
		Mockito.when(locationService.getLocationForItem(resortModel, LocationType.COUNTRY, brands)).thenReturn(locationModel);

		final String actual = defaultWeatherComponentFacade.getDestinationsForResort(locationModel).getCode();
		final String expected = "bng";
		assertNotNull(defaultWeatherComponentFacade.getDestinationsForResort(locationModel));
		assertEquals(expected, actual);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade#getCountryWeatherData(uk.co.portaltech.travel.model.LocationModel)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testGetCountryWeatherData()
	{
		final List<Object> feature1 = Mockito.mock(List.class);
		final LocationModel locationModelMock = Mockito.mock(LocationModel.class);
		final DefaultWeatherComponentFacade defaultWeatherComponentFacadeSpy = Mockito.spy(new DefaultWeatherComponentFacade());
		Mockito.when(weatherViewDataConverter.convert(weatherModel)).thenReturn(weatherViewData);
		Mockito.when(locationModelMock.getWeather()).thenReturn(weatherModel);
		Mockito.when(
				featureService.getFeatureValues(Mockito.anyString(), Mockito.any(LocationModel.class), Mockito.any(Date.class),
						Mockito.anyString())).thenReturn(feature1);
		Mockito.when(feature1.get(0)).thenReturn(locationModelMock);
		Mockito.when(locationModelMock.toString()).thenReturn("munich");
		Mockito.when(locationModelMock.getCategories()).thenReturn(categoryModelList);
		Mockito.doReturn(weatherViewData).when(defaultWeatherComponentFacadeSpy)
				.getWeatherData((LocationModel) Mockito.any(CategoryModel.class));

		final int actual = defaultWeatherComponentFacadeSpy.getCountryWeatherData(locationModel).size();
		final int expected = 0;

		assertNotNull(defaultWeatherComponentFacadeSpy.getCountryWeatherData(locationModel).size());
		assertEquals(expected, actual);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultWeatherComponentFacade#getSpainWeatherData(uk.co.portaltech.travel.model.LocationModel)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testGetSpainWeatherData()
	{

		final List<Object> feature1 = Mockito.mock(List.class);
		final LocationModel locationModelMock = Mockito.mock(LocationModel.class);


		Mockito.when(
				featureService.getFeatureValues(Mockito.anyString(), Mockito.any(LocationModel.class), Mockito.any(Date.class),
						Mockito.anyString())).thenReturn(feature1);
		Mockito.when(feature1.get(0)).thenReturn(locationModelMock);
		Mockito.when(locationModelMock.toString()).thenReturn("munich");
		Mockito.when(locationModelMock.getCategories()).thenReturn(categoryModelList);

		final int actual = defaultWeatherComponentFacade.getSpainWeatherData(locationModel).size();
		final int expected = 0;

		assertNotNull(defaultWeatherComponentFacade.getSpainWeatherData(locationModel).size());
		assertEquals(expected, actual);

	}

}
