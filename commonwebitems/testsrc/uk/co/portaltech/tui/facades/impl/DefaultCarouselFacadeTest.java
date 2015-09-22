/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.thirdparty.endeca.FacetOption;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.travel.thirdparty.endeca.TabResult;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel;
import uk.co.portaltech.tui.components.model.HeroCarouselComponentModel;
import uk.co.portaltech.tui.components.model.MediaPromoComponentModel;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TouristBoardBannerComponentModel;
import uk.co.portaltech.tui.converters.AccommodationOption;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.facades.AttractionFacade;
import uk.co.portaltech.tui.facades.ExcursionFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.ProductRangeFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.facades.impl.DefaultCarouselFacade;
import uk.co.portaltech.tui.populators.CarouselDataPopulator;
import uk.co.portaltech.tui.services.locationfinder.LocationFinder;
import uk.co.portaltech.tui.services.mediafinder.MediaFinder;
import uk.co.portaltech.tui.services.productfinder.ProductFinder;
import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.ThingsToDoWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;
import uk.co.tui.web.common.enums.CarouselLookupType;


/**
 * @author sunil.bd
 * 
 */
public class DefaultCarouselFacadeTest
{

	@InjectMocks
	private final DefaultCarouselFacade Facade = new DefaultCarouselFacade();

	@Mock
	private AccommodationViewData accommodationViewData;

	@Mock
	List<AccommodationViewData> accommodationViewDataList;

	@Mock
	private SearchRequestData searchRequestData;

	@Mock
	private AccommodationCarouselModel accommodationCarouselModel;

	@Mock
	private HeroCarouselComponentModel heroCarouselComponentModel;

	@Mock
	private ProductFinder productFinder;

	@Mock
	private MediaFinder finder;

	@Mock
	private Map<String, ProductFinder> finders;

	@Mock
	private Map<String, MediaFinder> mediaFinders;

	@Mock
	private Map<String, LocationFinder> locationFinders;

	@Mock
	private AccommodationModel accommodationModel;

	@Mock
	private List<AccommodationModel> accommodationModels;

	@Mock
	private SearchResultData<? extends Object> searchResultData;

	@Mock
	private MediaModel mediaModel;

	@Mock
	private List<MediaModel> mediaModelList;

	@Mock
	private CarouselDataPopulator carouselDataPopulator;

	@Mock
	private MediaViewData mediaViewData;

	@Mock
	private List<MediaViewData> mediaViewDataList;

	@Mock
	private SearchResultData<MediaModel> resultData;

	@Mock
	private MediaPromoComponentModel mediaPromoComponentModel;

	@Mock
	private CMSSiteService siteService;

	@Mock
	private CatalogVersionModel catalogVersionModel;

	@Mock
	private AccommodationService accommodationService;

	@Mock
	private Converter<AccommodationModel, AccommodationViewData> accommodationConverter;

	@Mock
	private AccommodationOption accommodationOption;

	@Mock
	private ConfigurablePopulator<AccommodationModel, AccommodationViewData, AccommodationOption> accommodationConfiguredPopulator;

	@Mock
	private ThingsToDoWrapper thingsToDoWrapper;

	@Mock
	private ThingsToDoCarouselModel thingsToDoCarouselModel;

	@Mock
	private List<ThingsToDoCarouselModel> thingsToDoCarouselModelList;

	@Mock
	private ThingsToDoCarouselComponentModel thingsToDoCarouselComponentModel;

	@Mock
	private SearchFacade searchFacade;

	@Mock
	private SearchResultData data;

	@Mock
	private ResultData resData;

	@Mock
	private List<ResultData> resultDataList;

	@Mock
	private ExcursionFacade excursionFacade;

	@Mock
	private ExcursionViewData excursionViewData;

	@Mock
	private List<ExcursionViewData> excursionViewDataList;

	@Mock
	private AttractionFacade attractionFacade;

	@Mock
	private AttractionViewData attractionViewData;

	@Mock
	private List<AttractionViewData> attractionViewDataList;

	@Mock
	private TouristBoardBannerComponentModel tBoardBannerComponentModel;

	@Mock
	private CrossSellCarouselComponentModel cSellCarouselComponentModel;

	@Mock
	private LocationModel locationModel;

	@Mock
	private List<LocationModel> locationModelList;

	@Mock
	private Converter<LocationModel, LocationData> locationConvertor;

	@Mock
	private SearchResultData<LocationModel> searchResultData2;

	@Mock
	private LocationData locationData;

	@Mock
	private ConfigurablePopulator<LocationModel, LocationData, LocationOption> locationConfiguredPopulator;

	@Mock
	private FacetOption facetOption;

	@Mock
	private uk.co.portaltech.travel.thirdparty.endeca.FacetValue facetValue;

	@Mock
	List<uk.co.portaltech.travel.thirdparty.endeca.FacetValue> facetValueList;

	@Mock
	private ProductCrossSellCarouselComponentModel pCarouselComponentModel;

	@Mock
	private ProductRangeFacade productRangeFacade;

	@Mock
	private ProductRangeViewData productRangeViewData;

	@Mock
	private TopPlacesToStayCarouselComponentModel tStayCarouselComponentModel;

	@Mock
	private TopPlacesCarouselModel topPlacesCarouselModel;

	@Mock
	private List<TopPlacesCarouselModel> tPlacesCarouselModelList;

	@Mock
	private TopPlacesWrapper topPlacesWrapper;

	@Mock
	private LocationFacade locationFacade;

	@Mock
	private DestinationQSAndTopPlacesComponentModel qsAndTopPlacesComponentModel;

	@Mock
	private TabResult tabResult;

	@Mock
	private SessionService sessionService;

	@Mock
	private List<TabResult> tabResultList;

	private final static String LOCATION_CODE = "HRV";
	private final static String PAGE_TYPE = "overview";
	private final static String SEO_PAGE_TYPE = "none";
	private CarouselLookupType carouselLookupType;
	private List<String> brandPks;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{


		brandPks = new ArrayList<String>();
		brandPks.add("tui");

		MockitoAnnotations.initMocks(this);

		final AccommodationViewData accommodationViewData = new AccommodationViewData();
		accommodationViewData.setReviewRating("5");
		accommodationViewData.setPriceFrom("1000");
		accommodationViewData.setRoomOccupancy("4");
		accommodationViewData.setStayPeriod("7");
		accommodationViewData.setLocationMapUrl("www.google.com");

		accommodationViewDataList = new ArrayList<AccommodationViewData>();
		accommodationViewDataList.add(accommodationViewData);

		heroCarouselComponentModel.setVisibleItems(3);
		heroCarouselComponentModel.setUid("uid");
		heroCarouselComponentModel.setName("name");
		heroCarouselComponentModel.setMedia(mediaModelList);
		heroCarouselComponentModel.setLookupType(carouselLookupType);

	}

	public SearchRequestData setDummySearchRequestData()
	{
		searchRequestData.setPageSize(5);
		searchRequestData.setOffset(0);
		searchRequestData.setCategoryCode(LOCATION_CODE);

		return searchRequestData;
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultCarouselFacade#getAccommodationViewData(uk.co.portaltech.tui.components.model.AccommodationCarouselModel, java.lang.String, java.lang.String, java.lang.String, int, int)}
	 * .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testGetAccommodationViewData()
	{

		final SearchRequestData searchRequestData = setDummySearchRequestData();

		final AccommodationCarouselModel model = new AccommodationCarouselModel();
		model.setName("name");
		model.setUid("uid");
		model.setVisible(true);
		model.setVisibleItems(5);

		searchRequestData.setRelevantItem(accommodationCarouselModel);

		Mockito.when(accommodationCarouselModel.getLookupType()).thenReturn(carouselLookupType);
		Mockito.when(finders.get("productFinder_" + carouselLookupType)).thenReturn(productFinder);

		final List<AccommodationViewData> expectedResult = Facade.getAccommodationViewData(model, LOCATION_CODE, PAGE_TYPE,
				SEO_PAGE_TYPE, 1, 50);

		assertThat(expectedResult, is(notNullValue()));
	}

	@Test
	public void testGetAccommodationViewDataforNullandBlank()
	{

		final SearchRequestData searchRequestData = setDummySearchRequestData();
		searchRequestData.setRelevantItem(accommodationCarouselModel);
		Mockito.when(accommodationCarouselModel.getLookupType()).thenReturn(carouselLookupType);
		Mockito.when(finders.get("productFinder_" + carouselLookupType)).thenReturn(productFinder);

		final List<AccommodationViewData> expectedResult = Facade.getAccommodationViewData(null, null, null, null, 1, 50);
		final List<AccommodationViewData> expectedResult1 = Facade.getAccommodationViewData(null, "", "", "", 1, 50);

		assertTrue(expectedResult.isEmpty());
		assertTrue(expectedResult1.isEmpty());
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetHeroCarouselMediaData()
	{
		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);
		final MediaFinder finder = Mockito.mock(MediaFinder.class);

		final HeroCarouselComponentModel model = new HeroCarouselComponentModel();

		model.setVisibleItems(3);
		model.setUid("uid");
		model.setName("name");
		model.setMedia(mediaModelList);
		model.setLookupType(carouselLookupType);

		Mockito.when(heroCarouselComponentModel.getLookupType()).thenReturn(carouselLookupType);

		Mockito.when(mediaFinders.get("mediaFinder_" + carouselLookupType)).thenReturn(finder);

		final SearchRequestData searchRequestData = setDummySearchRequestData();
		searchRequestData.setRelevantItem(heroCarouselComponentModel);

		Mockito.when(finder.search(searchRequestData)).thenReturn(resultData);
		Mockito.when(resultData.getResults()).thenReturn(mediaModelList);
		Mockito.when(carouselDataPopulator.convertAllMedia(mediaModelList)).thenReturn(mediaViewDataList);

		final List<MediaViewData> expectedResult = Facade.getHeroCarouselMediaData(model, 1, 1);

		assertThat(expectedResult, is(notNullValue()));
	}

	@Test
	public void testGetHeroCarouselMediaDataforNull()
	{
		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);
		final MediaFinder finder = Mockito.mock(MediaFinder.class);
		Mockito.when(heroCarouselComponentModel.getLookupType()).thenReturn(carouselLookupType);

		Mockito.when(mediaFinders.get("mediaFinder_" + carouselLookupType)).thenReturn(finder);

		final SearchRequestData searchRequestData = setDummySearchRequestData();
		searchRequestData.setRelevantItem(heroCarouselComponentModel);

		Mockito.when(finder.search(searchRequestData)).thenReturn(resultData);
		Mockito.when(resultData.getResults()).thenReturn(mediaModelList);
		Mockito.when(carouselDataPopulator.convertAllMedia(mediaModelList)).thenReturn(mediaViewDataList);

		final List<MediaViewData> expectedResult = Facade.getHeroCarouselMediaData(heroCarouselComponentModel, 1, 1);

		assertTrue(expectedResult.isEmpty());
	}

	@Test
	public void testGetMediaCarouselMediaData()
	{

		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);

		final MediaFinder finder = Mockito.mock(MediaFinder.class);
		Mockito.when(mediaPromoComponentModel.getLookupType()).thenReturn(carouselLookupType);
		Mockito.when(mediaFinders.get("mediaFinder_" + carouselLookupType)).thenReturn(finder);

		final SearchRequestData searchRequestData = setDummySearchRequestData();
		searchRequestData.setRelevantItem(mediaPromoComponentModel);

		Mockito.when(finder.search(searchRequestData)).thenReturn(resultData);
		Mockito.when(resultData.getResults()).thenReturn(mediaModelList);
		Mockito.when(carouselDataPopulator.convertAllMedia(mediaModelList)).thenReturn(mediaViewDataList);

		final List<MediaViewData> expectedResult = Facade.getMediaCarouselMediaData(mediaPromoComponentModel, 1, 1);

		assertThat(expectedResult, is(notNullValue()));
	}

	@Test
	public void testGetMediaCarouselMediaDataForNull()
	{

		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);

		final MediaFinder finder = Mockito.mock(MediaFinder.class);
		Mockito.when(mediaPromoComponentModel.getLookupType()).thenReturn(carouselLookupType);
		Mockito.when(mediaFinders.get("mediaFinder_" + carouselLookupType)).thenReturn(finder);

		final SearchRequestData searchRequestData = setDummySearchRequestData();

		searchRequestData.setRelevantItem(mediaPromoComponentModel);
		Mockito.when(finder.search(searchRequestData)).thenReturn(resultData);
		Mockito.when(resultData.getResults()).thenReturn(mediaModelList);
		Mockito.when(carouselDataPopulator.convertAllMedia(mediaModelList)).thenReturn(mediaViewDataList);

		final List<MediaViewData> expectedResult = Facade.getMediaCarouselMediaData(mediaPromoComponentModel, 1, 1);

		assertTrue(expectedResult.isEmpty());
	}

	@Test
	public void testGetHeroCarouselMediaDataByProductCode()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion("productCode", catalogVersionModel, brandPks))
				.thenReturn(accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.GALLERY_IMAGE));

		final AccommodationViewData expectedResult = Facade.getHeroCarouselMediaDataByProductCode("productCode", 1, 5);
		final AccommodationViewData expectedResult1 = Facade.getHeroCarouselMediaDataByProductCode("", 1, 5);
		final AccommodationViewData expectedResult2 = Facade.getHeroCarouselMediaDataByProductCode(null, 1, 5);

		/*
		 * assertNotNull(expectedResult); assertThat(expectedResult, is(accommodationViewData));
		 */
		assertNull(expectedResult1);
		assertNull(expectedResult2);

	}

	@Test
	public void testGetThingsToDoViewData()
	{

		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);

		Mockito.when(thingsToDoCarouselComponentModel.getThingsToDoCarousels()).thenReturn(thingsToDoCarouselModelList);
		Mockito.when(searchFacade.getThingsToDoViewData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, thingsToDoCarouselModel))
				.thenReturn(data);
		Mockito.when(data.getHeading()).thenReturn("heading");
		thingsToDoWrapper.setHeading("heading");
		Mockito.when(data.getResults()).thenReturn(resultDataList);
		Mockito.when(excursionFacade.getExcursionsWithEndecaData(resultDataList)).thenReturn(excursionViewDataList);
		Mockito.when(attractionFacade.getAttractionEnrichedData(resultDataList)).thenReturn(attractionViewDataList);

		final ThingsToDoWrapper expectedResult = Facade.getThingsToDoViewData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE,
				thingsToDoCarouselComponentModel);

		assertNull(expectedResult);
	}

	@Test
	public void testGetThingsToDoForTouristBoardViewData()
	{

		Mockito.when(
				searchFacade.getTouristBoardThingsToDoViewData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, thingsToDoCarouselModel))
				.thenReturn(data);
		Mockito.when(data.getHeading()).thenReturn("heading");
		thingsToDoWrapper.setHeading("heading");
		Mockito.when(data.getResults()).thenReturn(resultDataList);
		Mockito.when(excursionFacade.getExcursionsWithEndecaData(resultDataList)).thenReturn(excursionViewDataList);
		Mockito.when(attractionFacade.getAttractionEnrichedData(resultDataList)).thenReturn(attractionViewDataList);

		final ThingsToDoWrapper expectedResult = Facade.getThingsToDoForTouristBoardViewData(LOCATION_CODE, PAGE_TYPE,
				SEO_PAGE_TYPE, tBoardBannerComponentModel);

		final ThingsToDoWrapper expectedResult1 = Facade.getThingsToDoForTouristBoardViewData("", "", "",
				tBoardBannerComponentModel);

		assertNotNull(expectedResult);
		assertThat(expectedResult, is(ThingsToDoWrapper.class));
		assertNotNull(expectedResult1);
		assertThat(expectedResult1, is(ThingsToDoWrapper.class));

	}

	@Test(expected = NullPointerException.class)
	public void testGetThingsToDoForTouristBoardViewDataForNull()
	{

		Facade.getThingsToDoForTouristBoardViewData(null, null, null, null);

	}

	@Test
	public void testGetLocationViewData()
	{

		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);
		final LocationFinder finder = Mockito.mock(LocationFinder.class);

		Mockito.when(locationFinders.get("locationFinder_" + carouselLookupType)).thenReturn(finder);

		final List<LocationData> crossSellViewData = new ArrayList<LocationData>();
		Mockito.when(cSellCarouselComponentModel.getLookupType()).thenReturn(carouselLookupType);

		final SearchRequestData searchRequest = new SearchRequestData();
		searchRequest.setRelevantItem(cSellCarouselComponentModel);
		searchRequest.setCategoryCode(LOCATION_CODE);

		Mockito.when(finder.search(searchRequest)).thenReturn(searchResultData2);
		Mockito.when(searchResultData2.getResults()).thenReturn(locationModelList);
		Mockito.when(locationConvertor.convert(locationModel)).thenReturn(locationData);
		locationConfiguredPopulator.populate(locationModel, locationData,
				Arrays.asList(LocationOption.BASIC, LocationOption.GALLERY));
		crossSellViewData.add(locationData);

		final List<LocationData> expectedResult = Facade.getLocationViewData(LOCATION_CODE, cSellCarouselComponentModel);

		final List<LocationData> expectedResult1 = Facade.getLocationViewData(Mockito.anyString(),
				Mockito.any(CrossSellCarouselComponentModel.class));

		assertNotNull(expectedResult);
		assertTrue(expectedResult1.isEmpty());
	}

	/*
	 * @Test public void testGetProductCrossSellCarouselData() {
	 * 
	 * DefaultCarouselFacade Facade = Mockito .mock(DefaultCarouselFacade.class);
	 * 
	 * ProductCrossSellCarouselComponentModel model = new ProductCrossSellCarouselComponentModel(); model.setUid("uid");
	 * model.setName("name");
	 * 
	 * Mockito.when( searchFacade.getProductCrossSellData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE,
	 * pCarouselComponentModel)).thenReturn( data); ProductCrossSellWrapper productCrossSellWrapper = new
	 * ProductCrossSellWrapper(); Mockito.when(data.getResults()).thenReturn(resultDataList); List<ProductRangeViewData>
	 * viewData = new ArrayList<ProductRangeViewData>(); Mockito.when(resData.getFacetOption()).thenReturn(facetOption);
	 * Mockito.when(facetOption.getFacetValues()).thenReturn(facetValueList);
	 * Mockito.when(facetValue.getCode()).thenReturn("code");
	 * Mockito.when(productRangeFacade.getProductRangeByCode("code")) .thenReturn(productRangeViewData);
	 * viewData.add(productRangeViewData); productCrossSellWrapper.setCrossSellData(viewData);
	 * 
	 * final ProductCrossSellWrapper expectedResult = Facade .getProductCrossSellCarouselData(LOCATION_CODE, PAGE_TYPE,
	 * SEO_PAGE_TYPE, model);
	 * 
	 * assertNull(expectedResult);
	 * 
	 * }
	 */
	@Test
	public void testGetTopPlacesToStayData()
	{

		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);

		Mockito.when(tStayCarouselComponentModel.getTopPlacesCarousels()).thenReturn(tPlacesCarouselModelList);
		Mockito.when(searchFacade.getTopPlacesToStayData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, topPlacesCarouselModel))
				.thenReturn(data);
		Mockito.when(data.getResults()).thenReturn(resultDataList);
		final AccommodationCarouselViewData accm = new AccommodationCarouselViewData();
		accm.setAccomodationDatas(Collections.EMPTY_LIST);

		Mockito.when(locationFacade.getLocationData(LOCATION_CODE)).thenReturn(locationData);
		topPlacesWrapper.setLocation(locationData);

		final TopPlacesWrapper expectedResult = Facade.getTopPlacesToStayData(tStayCarouselComponentModel, LOCATION_CODE,
				PAGE_TYPE, SEO_PAGE_TYPE, 5);

		assertNull(expectedResult);
	}

	@Test
	public void testGetTopPlacesForDestQS()
	{
		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);

		Mockito.when(qsAndTopPlacesComponentModel.getTopPlacesCarousels()).thenReturn(tPlacesCarouselModelList);

		Mockito.when(searchFacade.getTopPlacesToStayData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, topPlacesCarouselModel))
				.thenReturn(data);
		Mockito.when(data.getResults()).thenReturn(resultDataList);
		Mockito.when(locationFacade.getLocationData(LOCATION_CODE)).thenReturn(locationData);
		topPlacesWrapper.setLocation(locationData);

		final TopPlacesWrapper expectedResult = Facade.getTopPlacesForDestQS(qsAndTopPlacesComponentModel, LOCATION_CODE,
				PAGE_TYPE, SEO_PAGE_TYPE, 3);

		assertNull(expectedResult);
	}

	@Test
	public void testGetCrossSellCarouselsData()
	{

		final DefaultCarouselFacade Facade = Mockito.mock(DefaultCarouselFacade.class);

		final CatalogVersionModel catalogVersionModel = new CatalogVersionModel();
		catalogVersionModel.setCategorySystemID("csId");
		catalogVersionModel.setVersion("5");
		catalogVersionModel.setGeneratorInfo("info");
		final CrossSellCarouselComponentModel item = new CrossSellCarouselComponentModel();
		item.setCatalogVersion(catalogVersionModel);
		item.setName("name");
		item.setUid("uid");

		Mockito.when(
				searchFacade.getCrossSellCarouselData(LOCATION_CODE, "productCode", PAGE_TYPE, SEO_PAGE_TYPE,
						cSellCarouselComponentModel)).thenReturn(data);
		Mockito.when(data.getHeading()).thenReturn("heading");
		final CrossSellWrapper crossSellWrapper = new CrossSellWrapper();
		crossSellWrapper.setHeading("heading");
		Mockito.when(data.getTabResults()).thenReturn(tabResultList);

		final CrossSellWrapper expectedResult = Facade.getCrossSellCarouselsData(LOCATION_CODE, null, PAGE_TYPE, SEO_PAGE_TYPE,
				item);

		assertNull(expectedResult);
	}
}
