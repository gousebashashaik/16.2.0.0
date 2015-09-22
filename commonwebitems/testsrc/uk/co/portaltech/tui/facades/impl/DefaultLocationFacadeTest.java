/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static uk.co.portaltech.commons.Collections.map;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.travel.enums.ChangeType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.unit.Units;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.DestinationConverter;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.converters.ProductToDestinationConverter;
import uk.co.portaltech.tui.facades.impl.DefaultLocationFacade;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.DestinationGuideViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;

/**
 * @author sureshbabu.rn
 * 
 */
public class DefaultLocationFacadeTest {
	@InjectMocks
	private DefaultLocationFacade defaultLocationFacade = new DefaultLocationFacade();
	@Mock
	private SiteAwareKeyGenerator keyGenerator;
	@Mock
	private CacheWrapper<String, LocationData> locationDataCache;
	@Mock
	CacheWrapper<String, List<LocationData>> locationDataCache1;
	@Mock
	private CacheUtil cacheUtil;
	@Mock
	private CategoryService categoryService;
	@Mock
	private LocationConverter locationConverter;
	@Mock
	private ConfigurablePopulator<LocationModel, LocationData, LocationOption> defaultLocationConfiguredPopulator;
	@Mock
	private LocationService locationService;
	@Mock
	private ProductService productService;
	@Mock
	private List<CategoryModel> childLocationsItr;

	private SearchPanelComponentModel searchPanelComponent;

	private List<String> airports;

	private List<String> dates;
	@Mock
	private MainStreamTravelLocationService mstravelLocationService;
	@Mock
	private Units unitResult;
	@Mock
	private DestinationConverter destinationConverter;
	@Mock
	private ProductToDestinationConverter prodToDestConverter;
	@Mock
	private SessionService sessionService;

	
	private ProductRangeModel productRangeModel;

	private LocationModel locationModel, locationModel1;
	private LocationData locationData, locationData1;
	private static final String key = "getPassportAndVisaEditorialESP8796093056040fc_catalog/Online(8796093121113)fc_content/Online(8796093219417)fc_catalog/Online(8796093121113)8796093055008->en8796125855777->GBP";
	private static final String key2 = "getGettingAroundEditorial";
	private static final String key3 = "getWhereToGoData";
	private static final String key4 = "GetThumbnailMapData";
	private static final String key5 = "getLocationKeyFactsData";
	private static final String key6 = "getThingsToDoAndSeeEditorial";
	private static final String key7 = "getLocationInteractiveMapData";
	private static final String key8 = "getLocationForAccommodation";
	private static final String key9 = "getWhenToGoEditorial";
	private static final String key10 = "getArticlesForTheLocation";
	private static final String key11 = "getLocationData";
	private static final String key12 = "getLocationHighlights";
	private static final String key13 = "getChildLocationsData";

	private List<LocationModel> popularDestinations;
	private List<ProductRangeModel> besHolidayTypes;
	private DestinationData destData1, deData2;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		/*
		 * List<FeatureValueSetModel> featureValueSetModelList=new
		 * ArrayList<FeatureValueSetModel>();
		 * 
		 * FeatureValueSetModel value=new FeatureValueSetModel();
		 * 
		 * value.setCode(value)
		 */
		locationData = new LocationData();
		locationModel = new LocationModel();
		locationModel.setCode("ESP");

		Map<String, List<Object>> featureCodesAndValues = new HashMap<String, List<Object>>();
		List<Object> list = new ArrayList<Object>();
		list.add("British citizens don’t need a visa to enter Spain but must have a valid passport. For the most up-to-date passport and visa info, visit <a target='_blank' href='https://www.gov.uk/foreign-travel-advice/spain/entry-requirements'>www.gov.uk/foreign-travel-advice/spain/entry-requirements</a>");
		list.add("Spain");
		list.add("Sun-soaked Costas, Moorish cities, and even moreish paella");
		featureCodesAndValues.put("passportAndVisaInfo", list);
		featureCodesAndValues.put("name", list);
		featureCodesAndValues.put("strapline", list);

		locationData.putFeatureCodesAndValues(featureCodesAndValues);
		locationData1 = new LocationData();

		Map<String, List<Object>> featureCodesAndValues1 = new HashMap<String, List<Object>>();
		List<Object> list1 = new ArrayList<Object>();
		list1.add("You should take out comprehensive travel and medical insurance to cover you while you’re away. It’s also a good idea to get a European Health Insurance Card – or EHIC – before leaving the UK. Though it’s not a substitute for travel insurance, it entitles you to emergency medical treatment on the same terms as Spanish nationals. It won’t, however, cover you for medical repatriation, ongoing medical treatment or non-urgent treatment. For the latest health and safety info, visit <a href='http://www.gov.uk/foreign-travel-advice/spain/health/a'>www.gov.uk/foreign-travel-advice/spain/health</a>");
		list1.add("Spain");
		list1.add("Sun-soaked Costas, Moorish cities, and even moreish paella");
		featureCodesAndValues1.put("healthAndSafety", list1);
		featureCodesAndValues1.put("name", list1);
		featureCodesAndValues1.put("strapline", list1);

		locationData1.putFeatureCodesAndValues(featureCodesAndValues1);
		/*
		 * List<Object> value=new ArrayList<Object>(); value.add(new
		 * Integer(10)); locationData1.putFeatureValue("usps",value );
		 */
		locationModel1 = new LocationModel();
		locationModel1.setCode("ESP");
		locationModel1.setCommercialPriority(807);
		List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

		CategoryModel categoryModel = new CategoryModel();

		categoryModel.setCode("002501");
		categoryModelList.add(categoryModel);
		locationModel1.setCategories(categoryModelList);
		locationModel1.setType(LocationType.COUNTRY);
		locationModel1.setSynonyms("ALC");

		

		

		popularDestinations = new ArrayList<LocationModel>();
		Collection<LocationModel> locationModels = new ArrayList<LocationModel>();
		LocationModel locationMode = new LocationModel();
		locationMode.setCode("ESP");
		locationMode.setOrder(4);
		locationMode.setType(LocationType.DESTINATION);
		locationModels.add(locationMode);

		Collection<AirportModel> airportModels = new ArrayList<AirportModel>();

		AirportModel value = new AirportModel();
		value.setCode("ESP");
		value.setCountry("Spain");
		value.setDestination(locationModels);
		airportModels.add(value);

		LocationModel model = new LocationModel();

		model.setCode("002240");
		model.setAirports(airportModels);

		popularDestinations.add(model);

		besHolidayTypes = new ArrayList<ProductRangeModel>();
		ProductRangeModel productRangeModel = new ProductRangeModel();
		productRangeModel.setCode("002240");
		besHolidayTypes.add(productRangeModel);

		searchPanelComponent = new SearchPanelComponentModel();
		searchPanelComponent.setFirst(0);
		searchPanelComponent.setFlexibleDays(7);
		searchPanelComponent.setMaxAirportsSelectable(7);
		searchPanelComponent.setMaxChildAge(17);
		searchPanelComponent.setMaxCountriesViewable(2);
		searchPanelComponent.setMaxDestinationsViewable(4);
		searchPanelComponent.setMaxHotelsViewable(4);
		searchPanelComponent.setMaxNoOfAdult(9);
		searchPanelComponent.setMaxNoOfChild(8);
		searchPanelComponent.setMaxNoOfSeniors(9);
		searchPanelComponent.setMaxPaxConfig(9);
		searchPanelComponent.setMaxProdRangeViewable(4);
		searchPanelComponent.setMinChildAge(0);
		searchPanelComponent.setMaxNoOfAdult(2);
		searchPanelComponent.setMinNoOfChild(0);
		searchPanelComponent.setName("Search Panel Component");
		searchPanelComponent.setOffset(10);
		searchPanelComponent.setPersistedSearchPeriod(30);

		airports = new ArrayList<String>();

		airports.add("DSA");
		dates = new ArrayList<String>();

		dates.add("17-10-2013");
		dates.add("18-10-2013");
		dates.add("19-10-2013");

		productRangeModel = new ProductRangeModel();

		productRangeModel.setCode("002244");
		productRangeModel.setName("Flemingo", Locale.ENGLISH);
		productRangeModel.setOrder(5);
		deData2 = new DestinationData();
		deData2.setId(productRangeModel.getCode());
		deData2.setName(productRangeModel.getName(Locale.ENGLISH));

		destData1 = new DestinationData();
		destData1.setName("INdia");

		destData1.setId(locationModel1.getCode());
		destData1.setType(locationModel1.getType().getCode());
		destData1.setName(locationModel1.getName(Locale.UK));

	}

	/*
	 * private List<LocalDate> toLocalDates(List<String> dates) { return
	 * map(dates, locateDateMapper); }
	 */

	@SuppressWarnings("boxing")
	@Test
	public void testGetPassportAndVisaEditorial() {

		LocationData data = null;
		Mockito.when(keyGenerator.generate("getWhenToGoEditorial", "ESP"))
				.thenReturn(key);
		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		Mockito.when(locationDataCache.get(key)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData, Arrays.asList(LocationOption.PASSPORTANDVISA));

		final LocationData expectedResult = defaultLocationFacade
				.getPassportAndVisaEditorial("ESP");
		assertThat(expectedResult.getFeatureCodesAndValues(),
				is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetHealthAndSafetyEditorial() {
		Mockito.when(
				keyGenerator.generate("getHealthAndSafetyEditorial", "ESP"))
				.thenReturn(key);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData1);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData1, Arrays.asList(LocationOption.HEALTHANDSAFETY));

		final LocationData expectedResult = defaultLocationFacade
				.getHealthAndSafetyEditorial("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetGettingAroundEditorial() {
		Mockito.when(keyGenerator.generate("getGettingAroundEditorial", "ESP"))
				.thenReturn(key2);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key2)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData1);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData1, Arrays.asList(LocationOption.GETTINGAROUND));

		final LocationData expectedResult = defaultLocationFacade
				.getGettingAroundEditorial("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetWhereToGoData() {
		Mockito.when(keyGenerator.generate("getWhereToGoData", "ESP"))
				.thenReturn(key3);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key3)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData, Arrays.asList(LocationOption.CONTENT));

		final LocationData expectedResult = defaultLocationFacade
				.getWhereToGoData("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetThumbnailMapData() {
		Mockito.when(keyGenerator.generate("getThumbnailMapData", "ESP"))
				.thenReturn(key4);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key4)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData, Arrays.asList(LocationOption.THUMBNAILMAP));

		final LocationData expectedResult = defaultLocationFacade
				.getThumbnailMapData("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetLocationKeyFactsData() {
		Mockito.when(keyGenerator.generate("getLocationKeyFactsData", "ESP"))
				.thenReturn(key5);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key5)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData, Arrays.asList(LocationOption.KEY_FACTS));

		final LocationData expectedResult = defaultLocationFacade
				.getLocationKeyFactsData("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetThingsToDoAndSeeEditorial() {
		Mockito.when(
				keyGenerator.generate("getThingsToDoAndSeeEditorial", "ESP"))
				.thenReturn(key6);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key6)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData, Arrays.asList(LocationOption.CONTENT));

		final LocationData expectedResult = defaultLocationFacade
				.getThingsToDoAndSeeEditorial("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetLocationInteractiveMapData() {
		Mockito.when(
				keyGenerator.generate("getLocationInteractiveMapData", "ESP"))
				.thenReturn(key7);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key7)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData, Arrays.asList(LocationOption.SUBCATEGORIES));

		final LocationData expectedResult = defaultLocationFacade
				.getLocationInteractiveMapData("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetLocationForAccommodation() {

		AccommodationModel model = new AccommodationModel();
		model.setCode("004965");
		LocationModel locmodel = new LocationModel();
		locmodel.setCode("000374");

		List<String> relevantBrands = new ArrayList<String>();

		BrandDetails brandDetails = new BrandDetails();
		brandDetails.setRelevantBrands(relevantBrands);

		Collection<AttractionModel> attractionModelList = new ArrayList<AttractionModel>();
		AttractionModel value = new AttractionModel();
		value.setCode("81673");
		attractionModelList.add(value);
		locmodel.setAttractions(attractionModelList);

		LocationData locData = new LocationData();

		Map<String, List<Object>> featureCodesAndValues = new HashMap<String, List<Object>>();
		List<Object> list1 = new ArrayList<Object>();
		list1.add("Day trips to Tarragona and Barcelona");

		featureCodesAndValues.put("healthAndSafety", list1);

		locData.putFeatureCodesAndValues(featureCodesAndValues);

		Mockito.when(
				keyGenerator.generate("getLocationForAccommodation", "ESP"))
				.thenReturn(key8);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key8)).thenReturn(data);
		Mockito.when(productService.getProductForCode("004965")).thenReturn(
				model);
		Mockito.when(
				locationService.getLocationForItem(new AccommodationModel(),
						LocationType.RESORT, relevantBrands)).thenReturn(
				locmodel);
		Mockito.when(locationConverter.convert(locmodel)).thenReturn(locData);
		Mockito.when(sessionService.getAttribute(Mockito.anyString()))
				.thenReturn(brandDetails);
		defaultLocationConfiguredPopulator.populate(locmodel, locData,
				Arrays.asList(LocationOption.BASIC));

		final LocationData expectedResult = defaultLocationFacade
				.getLocationForAccommodation("004965");
		assertNull(expectedResult);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetWhenToGoEditorial() {

		List<String> relevantBrands = new ArrayList<String>();

		BrandDetails brandDetails = new BrandDetails();
		brandDetails.setRelevantBrands(relevantBrands);
		Mockito.when(keyGenerator.generate("getWhenToGoEditorial", "ESP"))
				.thenReturn(key9);

		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key9)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		/*
		 * Mockito.when(locationService.getLocationForItem(locationModel,
		 * LocationType.COUNTRY)).thenReturn(locationModel);
		 */
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		Mockito.when(sessionService.getAttribute(Mockito.anyString()))
				.thenReturn(brandDetails);

		defaultLocationConfiguredPopulator.populate(locationModel,
				locationData, Arrays.asList(LocationOption.WHENTOGO));

		final LocationData expectedResult = defaultLocationFacade
				.getWhenToGoEditorial("ESP");
		assertNull(expectedResult);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetArticlesForTheLocation() {
		Mockito.when(keyGenerator.generate("getArticlesForTheLocation", "ESP"))
				.thenReturn(key10);
		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);

		LocationData data = null;
		Mockito.when(locationDataCache.get(key10)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);

		final LocationData expectedResult = defaultLocationFacade
				.getArticlesForTheLocation("ESP");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetLocationData() {
		Mockito.when(keyGenerator.generate("getLocationData", "052555"))
				.thenReturn(key11);
		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);
		Mockito.when(locationDataCache.get(key11)).thenReturn(locationData);

		final LocationData expectedResult = defaultLocationFacade
				.getLocationData("052555");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetLocationHighlights() {
		Mockito.when(
				keyGenerator.generate("getLocationHighlights", "052555", 1))
				.thenReturn(key12);
		Mockito.when(cacheUtil.getLocationDataCacheWrapper()).thenReturn(
				locationDataCache);
		LocationData data = null;
		Mockito.when(locationDataCache.get(key10)).thenReturn(data);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(categoryService.getCategoryForCode("ESP")).thenReturn(
				locationModel);
		Mockito.when(locationConverter.convert(locationModel)).thenReturn(
				locationData);
		final LocationData expectedResult = defaultLocationFacade
				.getLocationHighlights("ESP", 1);
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getFeatureCodesAndValues().size(), is(3));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetChildLocationsData() {
		Mockito.when(keyGenerator.generate("getChildLocationsData", "ESP", 30))
				.thenReturn(key13);
		Mockito.when(cacheUtil.getLocationListCacheWrapper()).thenReturn(
				locationDataCache1);
		List<LocationData> data = null;
		Mockito.when(locationDataCache1.get(key13)).thenReturn(data);

		final List<LocationData> expectedResult = defaultLocationFacade
				.getChildLocationsData("ESP", 30);
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.size(), is(0));
	}

	/*
	 * @SuppressWarnings("boxing")
	 * @Test public void testFetchDestinationGuide(){
	 * @Test public void testGetCountries(){
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testCreateDestinationList() {
		LocationModel locationModel1 = new LocationModel();
		DestinationData destinationData = new DestinationData();
		locationModel1.setCode("ESP");
		locationModel1.setType(LocationType.COUNTRY);
		locationModel1.setName("Flemingo", Locale.UK);
		locationModel1.setCatalogVersion(getCatalogVersion());

		List<LocationModel> bestforbeaches = new ArrayList<LocationModel>();
		bestforbeaches.add(locationModel1);
		when(destinationConverter.convert(locationModel1, destinationData))
				.thenReturn(destData1);

		/*
		 * final List<DestinationData>
		 * expectedResult=defaultLocationFacade.createDestinationList
		 * (bestforbeaches); assertNotNull(expectedResult);
		 */
	}

	private CatalogVersionModel getCatalogVersion() {

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;
	}

	@Test
	public void testCreateDestListForProduct() {
		DestinationData destinationData = new DestinationData();
		Mockito.when(
				prodToDestConverter.convert(productRangeModel, destinationData))
				.thenReturn(deData2);

		List<ProductRangeModel> productRanges = new ArrayList<ProductRangeModel>();
		productRanges.add(productRangeModel);

		/*
		 * final List<DestinationData>
		 * expectedResult=defaultLocationFacade.createDestListForProduct
		 * (productRanges); assertNotNull(expectedResult);
		 */
	}
}
