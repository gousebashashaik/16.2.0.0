/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.testframework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.AccommodationConverter;
import uk.co.portaltech.tui.converters.AccommodationOption;
import uk.co.portaltech.tui.facades.impl.DefaultAccomodationFacade;
import uk.co.portaltech.tui.populators.MediaDataPopulator;
import uk.co.portaltech.tui.populators.TripAdvisorPopulator;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.web.view.data.AccommodationRoomViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.ArticleViewData;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SustainableTourismComponentViewData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author kavita.na
 * 
 */
public class DefaultAccommodationFacadeTest
{

	@InjectMocks
	private final DefaultAccomodationFacade facade = new DefaultAccomodationFacade();

	@Mock
	private ConfigurablePopulator<AccommodationModel, AccommodationViewData, AccommodationOption> accommodationConfiguredPopulator;

	@Mock
	private ConfigurablePopulator<ItemModel, AccommodationViewData, AccommodationOption> accommodationAttractionExcursionConfiguredPopulator;

	@Mock
	private Converter<ItemModel, AccommodationViewData> accommodationAttractionExcursionConverter;

	@Mock
	private AccommodationService accommodationService;

	@Mock
	private AccommodationViewData viewData;

	@Mock
	private Converter<BenefitModel, BenefitViewData> defaultBenefitConverter;

	@Mock
	private CMSSiteService siteService;

	@Mock
	private MediaDataPopulator mediaDataPopulator;

	@Mock
	private CategoryService categoryService;

	@Mock
	private AccommodationConverter accommodationConverter;

	@Mock
	private Converter<ArticleModel, ArticleViewData> articleConverter;

	@Mock
	private ArticleViewData articleViewData;

	@Mock
	private ArticleModel articleModel;

	@Mock
	private Populator<ArticleModel, ArticleViewData> articlePopulator;

	@Mock
	private Populator<AccommodationModel, AccommodationViewData> accommodationInfoPopulator;

	@Mock
	private TripAdvisorPopulator tripAdvisorPopulator;

	@Mock
	private MediaModel mediaModel;

	@Mock
	private List<MediaContainerModel> containerModelList;

	@Mock
	private List<MediaModel> mediaModelList;

	@Mock
	private SessionService sessionService;

	@Mock
	private CacheWrapper<String, AccommodationViewData> accomDataCache;

	@Mock
	private CacheWrapper<String, List<AccommodationViewData>> accomListCache;

	@Mock
	private CacheUtil cacheUtil;

	@Mock
	private CacheWrapper<String, AccommodationViewData> cacheWrapper;

	@Mock
	private SiteAwareKeyGenerator keyGenerator;

	@Mock
	private List<AccommodationViewData> listOfAccommodations;

	private BenefitModel benefitModel;

	private AccommodationModel accommodationModel, accommodationModel1, accommodationModel2;
	private AccommodationViewData accommodationViewData, accommodationViewData1, accommodationViewData2, accommodationViewData3,
			accommodationViewData4;
	private List<AccommodationRoomViewData> roomData;
	private List<BenefitModel> benefitsForAccommodation;
	private List<BenefitViewData> listOfAccommodationBenefits;
	private List<AccommodationModel> accommodationsForLocation;
	private List<AccommodationViewData> viewDataList;
	private ItemModel itemModel;
	private LocationModel locationModel;
	private SustainableTourismComponentViewData sustainableTourismViewData;
	private List<ResultData> ResultDataList, resultDataList1;
	private List<FacilityViewData> facilityList;
	private SearchResultData<ResultData> searchResultData;
	private List<ArticleModel> articles;

	public static final String TEST_COMPONENT_UID = "WF_COM_42-1";
	private final static String ACCOMCODE = "039425";
	private final static String TYPE = "ROOMS";

	private List<String> brandPks = null;
	@Mock
	private TuiUtilityService tuiUtilityService;

	@Before
	public void setUp() throws Exception
	{

		

		MockitoAnnotations.initMocks(this);

		final BrandDetails brandDetails = new BrandDetails();
		brandDetails.setSiteBrand("firstchoice");
		brandDetails.setSiteName("Firstchoice");
		brandDetails.setSiteUid("1122445");
		final List<String> relevantBrandIds = new ArrayList<String>();
		relevantBrandIds.add("AAA");
		relevantBrandIds.add("BBB");
		relevantBrandIds.add("CCC");

		brandDetails.setRelevantBrandIds(relevantBrandIds);
		brandPks = brandDetails.getRelevantBrands();
		Mockito.when(sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS)).thenReturn(brandDetails);
		brandPks = brandDetails.getRelevantBrands();
		itemModel = new ItemModel();

		articles = new ArrayList<ArticleModel>();

		containerModelList = new ArrayList<MediaContainerModel>();

		mediaModelList = new ArrayList<MediaModel>();

		facilityList = new ArrayList<FacilityViewData>();

		final FacilityViewData facilities = new FacilityViewData();
		facilities.setName("Flamingo Benidorm");
		facilities.setFacilityType("Type");
		facilityList.add(facilities);

		accommodationModel = new AccommodationModel();
		accommodationModel.setCode("002240");
		accommodationModel.setType(AccommodationType.HOTEL);
		accommodationModel.setGalleryImages(containerModelList);
		accommodationModel.setArticles(articles);

		accommodationModel1 = new AccommodationModel();
		accommodationModel1.setCode("002238");
		accommodationModel1.setType(AccommodationType.HOTEL);

		accommodationModel2 = new AccommodationModel();
		accommodationModel2.setCode("047406");
		accommodationModel2.setType(AccommodationType.VILLA);

		roomData = new ArrayList<AccommodationRoomViewData>();

		final AccommodationRoomViewData accommodationRoomViewData = new AccommodationRoomViewData();
		accommodationRoomViewData.setCode("002240");
		accommodationRoomViewData.setDescription("Description");

		roomData.add(accommodationRoomViewData);

		accommodationViewData = new AccommodationViewData();
		accommodationViewData.setCode("002240");
		accommodationViewData.setName("Flamingo Benidorm");
		accommodationViewData.setUrl("/holiday/accommodation/overview/Benidorm/Flamingo-Benidorm-002240");
		accommodationViewData.setLocationMapUrl("/holiday/accommodation/location/Benidorm/Flamingo-Benidorm-002240");
		accommodationViewData.setRoomsData(roomData);
		accommodationViewData.setFacilities(facilityList);

		accommodationViewData1 = new AccommodationViewData();
		accommodationViewData1.setCode("002238");
		accommodationViewData1.setName("Flamingo Playa Apartments");
		accommodationViewData1.setLocationMapUrl("/holiday/accommodation/location/Benidorm/Flamingo-Playa-Apartments-002238");
		accommodationViewData1.setUrl("/holiday/accommodation/overview/Benidorm/Flamingo-Playa-Apartments-002238");

		accommodationViewData2 = new AccommodationViewData();

		accommodationViewData2.setCode("047406");
		accommodationViewData2.setName("Holiday Village Kos");
		accommodationViewData2.setUrl("" + "/holiday/accommodation/overview/Marmari/Holiday-Village-Kos-047406");

		accommodationViewData3 = new AccommodationViewData();

		accommodationViewData3.setCode("030164");

		accommodationViewData4 = new AccommodationViewData();
		accommodationViewData4.setCode("002558");

		benefitModel = new BenefitModel();
		benefitModel.setCode("002240");
		benefitsForAccommodation = new ArrayList<BenefitModel>();
		benefitsForAccommodation.add(benefitModel);

		locationModel = new LocationModel();

		locationModel.setCode("000338");
		locationModel.setCommercialPriority(Integer.valueOf(50));
		locationModel.setType(LocationType.RESORT);

		accommodationsForLocation = new ArrayList<AccommodationModel>();

		ResultDataList = new ArrayList<ResultData>();

		final ResultData resultData = new ResultData();
		resultData.setCode("002240");
		resultData.setName("Flamingo Benidorm");

		final ResultData resultData1 = new ResultData();
		resultData1.setCode("990791");
		resultData1.setName("Siam Park");
		resultData1.setPriceFrom("33.00");
		resultData1.setSearchResultSubtype("EXR");
		resultData1.setDeparturePoint("India");
		resultData1.setRoomOccupancy("Occupency");
		resultData1.setStayPeriod("30");
		resultData1.settRating("8/10");

		ResultDataList.add(resultData);

		resultDataList1 = new ArrayList<ResultData>();
		resultDataList1.add(resultData1);

		sustainableTourismViewData = new SustainableTourismComponentViewData();
		sustainableTourismViewData.setDescription("");
		sustainableTourismViewData.setTitle("");

		searchResultData = new SearchResultData<ResultData>();
		searchResultData.setResults(resultDataList1);
	}

	@SuppressWarnings("boxing")
	private AccommodationModel getDummyComponentModel()
	{
		final AccommodationModel accom1 = new AccommodationModel();
		accom1.setCode(ACCOMCODE);
		accom1.setType(AccommodationType.HOTEL);

		return accom1;

	}

	@SuppressWarnings("boxing")
	private AccommodationViewData GetAccommodationInfoData()
	{

		final AccommodationViewData promoImageComponentData = new AccommodationViewData();

		promoImageComponentData.setCode(ACCOMCODE);
		promoImageComponentData
				.setDescription("Apartments here are sleek and modern, with cream walls, cool tiled floors and compact kitchenettes");
		return promoImageComponentData;
	}

	private CatalogVersionModel getCatalogVersion()
	{

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;
	}

	@Test
	public void testingAccommodationEditorialInfo()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		viewData = GetAccommodationInfoData();
		accommodationModel = getDummyComponentModel();

		Mockito.when(keyGenerator.generate("getAccommodationEditorialInfo", ACCOMCODE)).thenReturn("key");
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(ACCOMCODE, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(viewData);
		accommodationConfiguredPopulator.populate(accommodationModel, viewData, Arrays.asList(AccommodationOption.BASIC,
				AccommodationOption.PRIMARY_IMAGE, AccommodationOption.EDITORIAL_INFO, AccommodationOption.EDITORIAL_INTRO));

		final AccommodationViewData expectedResult = facade.getAccommodationEditorialInfo(ACCOMCODE, TYPE);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(viewData.getCode()));
		assertThat(expectedResult.getAccommodationType(), is(viewData.getAccommodationType()));
		assertThat(expectedResult.getDescription(), is(viewData.getDescription()));
	}

	@Test
	public void testGetAccommodationNameComponentData()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);

		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.NAME));

		final AccommodationViewData expectedResult = facade.getAccommodationNameComponentData(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	}

	@Test
	public void testGetAccommodationNameComponentDataForNull()
	{

		final CatalogVersionModel catalog = null;
		final String accommodationCode = null;

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null, Arrays.asList(AccommodationOption.BASIC, AccommodationOption.NAME));

		final AccommodationViewData expectedResult = facade.getAccommodationNameComponentData(accommodationCode);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationLocationPanelInfo()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel1.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel1);
		Mockito.when(accommodationConverter.convert(accommodationModel1)).thenReturn(accommodationViewData1);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel1, accommodationViewData1,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));

		final AccommodationViewData expectedResult = facade.getAccommodationLocationPanelInfo(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData1.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData1.getName()));
		assertThat(expectedResult.getLocationMapUrl(), is(accommodationViewData1.getLocationMapUrl()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData1.getUrl()));
	}

	@Test
	public void testGetAccommodationLocationPanelInfoForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(
				accommodationModel1);
		Mockito.when(accommodationConverter.convert(accommodationModel1)).thenReturn(accommodationViewData1);
		accommodationConfiguredPopulator.populate(accommodationModel1, accommodationViewData1,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		final AccommodationViewData expectedResult = facade.getAccommodationLocationPanelInfo(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationLocationInfo()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);

		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);

		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));

		final AccommodationViewData expectedResult = facade.getAccommodationLocationInfo(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getLocationMapUrl(), is(accommodationViewData.getLocationMapUrl()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	}

	@Test
	public void testGetAccommodationLocationInfoForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(
				accommodationModel);

		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO));

		final AccommodationViewData expectedResult = facade.getAccommodationLocationInfo(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationEditorialIntroduction()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.EDITORIAL_INTRO));
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.EDITORIAL_INTRO));

		final AccommodationViewData expectedResult = facade.getRequiredAccommodationEditorialIntroduction(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	}

	@Test
	public void testGetAccommodationEditorialIntroductionForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);

		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.EDITORIAL_INTRO));

		final AccommodationViewData expectedResult = facade.getRequiredAccommodationEditorialIntroduction(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationDisclaimer()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.DISCLAIMER));

		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.DISCLAIMER));

		final AccommodationViewData expectedResult = facade.getAccommodationDisclaimer(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));

		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	}

	@Test
	public void testGetAccommodationDisclaimerForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.DISCLAIMER));
		final AccommodationViewData expectedResult = facade.getAccommodationDisclaimer(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetHealthAndSafetyEditorial()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.HEALTHANDSAFETY));

		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.HEALTHANDSAFETY));

		final AccommodationViewData expectedResult = facade.getHealthAndSafetyEditorial(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));

		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	}

	@Test
	public void testGetHealthAndSafetyEditorialForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.HEALTHANDSAFETY));

		final AccommodationViewData expectedResult = facade.getHealthAndSafetyEditorial(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationByCode()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC));

		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC));

		final AccommodationViewData expectedResult = facade.getAccommodationByCode(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
	}

	@Test
	public void testGetAccommodationByCodeForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null, Arrays.asList(AccommodationOption.BASIC));

		final AccommodationViewData expectedResult = facade.getAccommodationByCode(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetRoomsForAccommodation()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ROOMS));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ROOMS));
		roomData = accommodationViewData.getRoomsData();
		accommodationViewData.setRoomsData(roomData);

		final AccommodationViewData expectedResult = facade.getRoomsForAccommodation(accommodationCode, 2);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	}

	@Test(expected = NullPointerException.class)
	public void testGetRoomsForAccommodationForNull()
	{
		facade.getRoomsForAccommodation(null, 2);
	}

	@Test
	public void testGetBenefitsForAccomodation()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();
		listOfAccommodationBenefits = new ArrayList<BenefitViewData>();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getBenefitsForAccommodation(accommodationCode, catalog, brandPks)).thenReturn(
				benefitsForAccommodation);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		listOfAccommodationBenefits.add(defaultBenefitConverter.convert(benefitModel));
		accommodationViewData.setAccommodationBenefits(listOfAccommodationBenefits);

		final AccommodationViewData expectedResult = facade.getBenefitsForAccomodation(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		Assert.assertNotEquals(expectedResult.getCode(), is(accommodationViewData.getCode()));
		Assert.assertNotEquals(expectedResult.getName(), is(accommodationViewData.getName()));
	}

	@Test(expected = NullPointerException.class)
	public void testGetBenefitsForAccomodationForNull()
	{
		listOfAccommodationBenefits = new ArrayList<BenefitViewData>();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getBenefitsForAccommodation(null, null, brandPks)).thenReturn(null);
		facade.getBenefitsForAccomodation(null);
	}

	@Test
	public void testGetPassportAndVisasEditorial()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PASSPORTANDVISA));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PASSPORTANDVISA));

		final AccommodationViewData expectedResult = facade.getPassportAndVisasEditorial(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
	}

	@Test
	public void testGetPassportAndVisasEditorialForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PASSPORTANDVISA));
		final AccommodationViewData expectedResult = facade.getPassportAndVisasEditorial(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationAllInclusive()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel1.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel1);
		Mockito.when(accommodationConverter.convert(accommodationModel1)).thenReturn(accommodationViewData1);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel1, accommodationViewData1,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ALL_INCLUSIVE));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel1, accommodationViewData1,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ALL_INCLUSIVE));

		final AccommodationViewData expectedResult = facade.getAccommodationAllInclusive(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData1.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData1.getName()));
		assertThat(expectedResult.getArticles(), is(accommodationViewData1.getArticles()));
		assertThat(expectedResult.getAvailableFrom(), is(accommodationViewData1.getAvailableFrom()));
	}

	@Test
	public void testGetAccommodationAllInclusiveForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ALL_INCLUSIVE));

		final AccommodationViewData expectedResult = facade.getAccommodationAllInclusive(null);

		assertNull("ExpectedResult is Null", expectedResult);
	}

	@Test
	public void testGetAccommodationProductRanges()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel2.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel2);
		Mockito.when(accommodationConverter.convert(accommodationModel2)).thenReturn(accommodationViewData2);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel2, accommodationViewData2,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PRODUCTRANGE));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel2, accommodationViewData2,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PRODUCTRANGE));

		final AccommodationViewData expectedResult = facade.getAccommodationProductRanges(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData2.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData2.getName()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData2.getUrl()));
	}

	public void testGetAccommodationProductRangesForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PRODUCTRANGE));
		final AccommodationViewData expectedResult = facade.getAccommodationProductRanges(null);
		assertNull("Expected Result is Null", expectedResult);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetAccommodationAtAGlance()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.AT_A_GLANCE));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.AT_A_GLANCE));
		final AccommodationViewData expectedResult = facade.getAccommodationAtAGlance(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));

		assertNotNull(expectedResult.getFeatureCodesAndValues());
	}

	@Test
	public void testGetAccommodationAtAGlanceForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.AT_A_GLANCE));

		final AccommodationViewData expectedResult = facade.getAccommodationAtAGlance(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationWhenToGoEditorial()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel2.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel2);
		Mockito.when(accommodationConverter.convert(accommodationModel2)).thenReturn(accommodationViewData2);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel2, accommodationViewData2,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.WHEN_TO_GO));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel2, accommodationViewData2,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.WHEN_TO_GO));

		final AccommodationViewData expectedResult = facade.getAccommodationWhenToGoEditorial(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData2.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData2.getName()));
	}

	@Test
	public void testGetAccommodationWhenToGoEditorialForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(null, null, brandPks)).thenReturn(null);
		Mockito.when(accommodationConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(null, null,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.WHEN_TO_GO));

		final AccommodationViewData expectedResult = facade.getAccommodationWhenToGoEditorial(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationEntertainment()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ENTERTAINMENT, AccommodationOption.GALLERY));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.ENTERTAINMENT, AccommodationOption.GALLERY));

		final AccommodationViewData expectedResult = facade.getAccommodationEntertainment(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getGalleryImages(), is(accommodationViewData.getGalleryImages()));

	}

	@Test(expected = NullPointerException.class)
	public void testGetAccommodationEntertainmentForNull()
	{
		facade.getAccommodationEntertainment(null);
	}

	@Test
	public void testGetAccommodationThumbnailMapData()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccommodationAttractionExcursionbyCode(accommodationCode, catalog)).thenReturn(
				itemModel);
		Mockito.when(accommodationAttractionExcursionConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationAttractionExcursionConfiguredPopulator.populate(itemModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.THUMBNAILMAP));
		Mockito.verify(accommodationAttractionExcursionConfiguredPopulator).populate(itemModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.THUMBNAILMAP));
		final AccommodationViewData expectedResult = facade.getAccommodationThumbnailMapData(accommodationCode);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationThumbnailMapDataForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccommodationAttractionExcursionbyCode(null, null)).thenReturn(null);
		Mockito.when(accommodationAttractionExcursionConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		final AccommodationViewData expectedResult = facade.getAccommodationThumbnailMapData(null);

		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationKeyFactsData()
	{

		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_KEYFACTS));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_KEYFACTS));

		final AccommodationViewData expectedResult = facade.getAccommodationKeyFactsData(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
		assertThat(expectedResult.getLocationMapUrl(), is(accommodationViewData.getLocationMapUrl()));
		assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	}

	@Test
	public void testGetAccommodationKeyFactsDataForNull()
	{

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(null);
		Mockito.when(accommodationService.getAccommodationAttractionExcursionbyCode(null, null)).thenReturn(null);
		Mockito.when(accommodationAttractionExcursionConverter.convert(null)).thenReturn(null);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		final AccommodationViewData expectedResult = facade.getAccommodationKeyFactsData(null);

		assertNull(expectedResult);
	}

	/*
	 * @Test public void testGetArticlesForAccommodation(){ final CatalogVersionModel catalog = getCatalogVersion();
	 * String accommodationCode=accommodationModel.getCode();
	 * 
	 * Mockito.when(siteService.getCurrentCatalogVersion()) .thenReturn(catalog);
	 * Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion( accommodationCode,
	 * catalog)).thenReturn(accommodationModel); Mockito.when(accommodationConverter.convert(
	 * accommodationModel)).thenReturn(accommodationViewData);
	 * 
	 * articleList=accommodationModel.getArticles();
	 * 
	 * Mockito.when(articleConverter.convert(articleModel)).thenReturn( articleViewData);
	 * 
	 * articlePopulator.populate(articleModel, articleViewData);
	 * 
	 * Mockito.verify(articlePopulator).populate(articleModel, articleViewData);
	 * 
	 * final AccommodationViewData expectedResult=facade.getArticlesForAccommodation(accommodationCode);
	 * 
	 * assertThat(expectedResult, is(notNullValue())); assertThat(expectedResult.getCode(),
	 * is(accommodationViewData.getCode())); assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
	 * assertThat(expectedResult.getLocationMapUrl (),is(accommodationViewData.getLocationMapUrl()));
	 * assertThat(expectedResult.getUrl(), is(accommodationViewData.getUrl()));
	 * System.out.println(expectedResult.getArticles()); assertNotNull(expectedResult.getArticles());
	 * 
	 * 
	 * 
	 * }
	 */

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorSummaryData()
	{
		

		final String accomodationCode = "030164";
		final Integer maxUserReview = 0;

		Mockito.when(tripAdvisorPopulator.getTripAdvisorSummaryData(accomodationCode, maxUserReview)).thenReturn(
				accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorSummaryData(accomodationCode, maxUserReview);
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData3.getCode()));

		assertNull(expectedResult.getTripadvisorData());
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorSummaryDataForNull()
	{
		final String accomodationCode = null;
		final Integer maxUserReview = 0;
		Mockito.when(tripAdvisorPopulator.getTripAdvisorSummaryData(accomodationCode, maxUserReview)).thenReturn(
				accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorSummaryData(accomodationCode, maxUserReview);
		assertNull(expectedResult);

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorSummaryDataForEmpty()
	{
		final String accomodationCode = "";
		final Integer maxUserReview = 1;
		Mockito.when(tripAdvisorPopulator.getTripAdvisorSummaryData(accomodationCode, maxUserReview)).thenReturn(
				accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorSummaryData(accomodationCode, maxUserReview);
		assertNull(expectedResult);

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorSummaryDataForNegative()
	{
		final String accomodationCode = "";
		final Integer maxUserReview = -1;
		Mockito.when(tripAdvisorPopulator.getTripAdvisorSummaryData(accomodationCode, maxUserReview)).thenReturn(
				accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorSummaryData(accomodationCode, maxUserReview);
		assertNull(expectedResult);

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorReviewData()
	{
		final String accomodationCode = "030164";
		final Integer maxUserReview = 5;

		Mockito.when(tripAdvisorPopulator.getTripAdvisorReviews(accomodationCode, maxUserReview))
				.thenReturn(accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorReviewData(accomodationCode, maxUserReview);
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData3.getCode()));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorReviewDataForNull()
	{
		final Integer maxUserReview = 5;
		final String accomodationCode = null;

		Mockito.when(tripAdvisorPopulator.getTripAdvisorReviews(accomodationCode, maxUserReview))
				.thenReturn(accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorReviewData(accomodationCode, maxUserReview);
		assertNull(expectedResult);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorReviewDataForEmpty()
	{
		final Integer maxUserReview = 5;
		final String accomodationCode = "";

		Mockito.when(tripAdvisorPopulator.getTripAdvisorReviews(accomodationCode, maxUserReview))
				.thenReturn(accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorReviewData(accomodationCode, maxUserReview);
		assertNull(expectedResult);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorReviewDataForNegative()
	{
		final Integer maxUserReview = -5;
		final String accomodationCode = "";

		Mockito.when(tripAdvisorPopulator.getTripAdvisorReviews(accomodationCode, maxUserReview))
				.thenReturn(accommodationViewData3);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getTripAdvisorReviewData(accomodationCode, maxUserReview);
		assertNull(expectedResult);
	}

	@Test
	public void testGetAccommodationsByResort()
	{
		final String locationCode = locationModel.getCode();
		final int visibleItems = -1;

		Mockito.when(categoryService.getCategoryForCode(locationCode)).thenReturn(locationModel);
		Mockito.when(cacheUtil.getAccommodationListCacheWrapper()).thenReturn(accomListCache);
		Mockito.when(accomListCache.get("key")).thenReturn(listOfAccommodations);
		Mockito.when(tuiUtilityService.getSiteReleventBrands()).thenReturn(brandPks);
		viewDataList = facade.getAccommodations(locationCode, visibleItems);
		Mockito.when(facade.getAccommodations(locationCode, visibleItems)).thenReturn(viewDataList);
		final List<AccommodationViewData> expectedResult = facade.getAccommodationsByResort(locationCode, visibleItems);

		assertThat(expectedResult, is(notNullValue()));
		/*
		 * System.out.println("dgjgdgajdgaj"+expectedResult); assertThat("000338", is(expectedResult.get(0).getCode()));
		 * assertThat("90", is(expectedResult.get(0).getCommercialPriority())); assertThat("RESORT",
		 * is(expectedResult.get(0).getResort().getName()));
		 */
	}

	@Test
	public void testGetAccommodationsByResortForNull()
	{
		final String locationCode = null;
		final int visibleItems = -1;

		Mockito.when(categoryService.getCategoryForCode(locationCode)).thenReturn(locationModel);
		Mockito.when(cacheUtil.getAccommodationListCacheWrapper()).thenReturn(accomListCache);
		Mockito.when(accomListCache.get("key")).thenReturn(listOfAccommodations);

		viewDataList = facade.getAccommodations(locationCode, visibleItems);
		Mockito.when(facade.getAccommodations(locationCode, visibleItems)).thenReturn(viewDataList);
		final List<AccommodationViewData> expectedResult = facade.getAccommodationsByResort(locationCode, visibleItems);
		assertNotNull(expectedResult);
	}

	@Test
	public void testGetAccommodationsByResortForEmpty()
	{
		final String locationCode = "";
		final int visibleItems = -1;

		Mockito.when(categoryService.getCategoryForCode(locationCode)).thenReturn(locationModel);
		Mockito.when(cacheUtil.getAccommodationListCacheWrapper()).thenReturn(accomListCache);
		Mockito.when(accomListCache.get("key")).thenReturn(listOfAccommodations);

		viewDataList = facade.getAccommodations(locationCode, visibleItems);
		Mockito.when(facade.getAccommodations(locationCode, visibleItems)).thenReturn(viewDataList);
		final List<AccommodationViewData> expectedResult = facade.getAccommodationsByResort(locationCode, visibleItems);
		assertNotNull(expectedResult);
	}

	@Test
	public void testGetAccommodationsByResortForPossitive()
	{
		final String locationCode = locationModel.getCode();
		final int visibleItems = 1;

		Mockito.when(categoryService.getCategoryForCode(locationCode)).thenReturn(locationModel);
		Mockito.when(cacheUtil.getAccommodationListCacheWrapper()).thenReturn(accomListCache);
		Mockito.when(accomListCache.get("key")).thenReturn(listOfAccommodations);

		viewDataList = facade.getAccommodations(locationCode, visibleItems);
		Mockito.when(facade.getAccommodations(locationCode, visibleItems)).thenReturn(viewDataList);
		final List<AccommodationViewData> expectedResult = facade.getAccommodationsByResort(locationCode, visibleItems);
		assertNotNull(expectedResult);
	}

	@Test
	public void testGetAccommodations()
	{

		final String locationCode = "000338";
		final int visibleItems = -1;
		/*
		 * Mockito.when( accommodationService.getAccommodationsForLocation(locationCode))
		 * .thenReturn(accommodationsForLocation);
		 */
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData4);
		Mockito.when(cacheUtil.getAccommodationListCacheWrapper()).thenReturn(accomListCache);
		Mockito.when(accomListCache.get("key")).thenReturn(listOfAccommodations);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData4,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO, AccommodationOption.T_RATING));
		accommodationInfoPopulator.populate(accommodationModel, accommodationViewData4);

		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData4,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO, AccommodationOption.T_RATING));
		Mockito.verify(accommodationInfoPopulator).populate(accommodationModel, accommodationViewData4);

		final List<AccommodationViewData> expectedResult = facade.getAccommodations(locationCode, visibleItems);

		assertThat(expectedResult, is(notNullValue()));

	}

	@Test
	public void testGetAccommodations1()
	{

		final String locationCode = "000338";
		final int visibleItems = -1;
		/*
		 * Mockito.when( accommodationService.getAccommodationsForLocation(locationCode))
		 * .thenReturn(accommodationsForLocation);
		 */
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData4);
		Mockito.when(cacheUtil.getAccommodationListCacheWrapper()).thenReturn(accomListCache);
		Mockito.when(accomListCache.get("key")).thenReturn(listOfAccommodations);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData4,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO, AccommodationOption.T_RATING));
		accommodationInfoPopulator.populate(accommodationModel, accommodationViewData4);

		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData4,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.LOCATION_INFO, AccommodationOption.T_RATING));
		Mockito.verify(accommodationInfoPopulator).populate(accommodationModel, accommodationViewData4);

		final List<AccommodationViewData> expectedResult = facade.getAccommodations(locationCode, visibleItems);

		assertThat(expectedResult, is(notNullValue()));

	}

	@Test
	public void testgetAccommodationDealsData()
	{

		final List<AccommodationViewData> expectedResult = facade.getAccommodationDealsData(ResultDataList);

		assertNotNull(expectedResult);
		final Iterator<AccommodationViewData> iterator = expectedResult.iterator();
		while (iterator.hasNext())
		{
			final AccommodationViewData data = iterator.next();

			assertThat("000338", is(data.getCode()));
			assertThat("Flamingo Benidorm", is(data.getName()));
		}
	}

	@Test
	public void testGetAccommodationsWithEndecaData()
	{
		final List<AccommodationViewData> expectedResult = facade.getAccommodationsWithEndecaData(ResultDataList);

		assertThat(expectedResult, is(notNullValue()));

	}

	@Test
	public void testGetAccommodationTravelLifeAwardInfo()
	{
		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		final Map<String, List<Object>> viewData = accommodationViewData.getFeatureCodesAndValues();

		final SustainableTourismComponentViewData expectedResult = facade.getAccommodationTravelLifeAwardInfo(accommodationCode);
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getDescription(), is(""));
		assertThat(expectedResult.getTitle(), is(""));
	}

	@Test
	public void testGetAccommodationTravelLifeAwardInfoForNull()
	{
		final CatalogVersionModel catalog = null;
		final String accommodationCode = null;

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		final SustainableTourismComponentViewData expectedResult = facade.getAccommodationTravelLifeAwardInfo(accommodationCode);
		assertNotNull(expectedResult);

	}

	@Test
	public void testGetAccommodationTravelLifeAwardInfoForEmpty()
	{
		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = "";
		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		final SustainableTourismComponentViewData expectedResult = facade.getAccommodationTravelLifeAwardInfo(accommodationCode);
		assertNotNull(expectedResult);
	}

	@Test(expected = NullPointerException.class)
	public void testGetAccommodationHighlights()
	{
		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();
		final Integer highlightsNumber = 1;

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.HIGHLIGHTS));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.HIGHLIGHTS));

		final AccommodationViewData expectedResult = facade.getAccommodationHighlights(accommodationCode, highlightsNumber);
		assertNotNull(expectedResult);
	}

	@SuppressWarnings("boxing")
	@Test(expected = NullPointerException.class)
	public void testGetAccommodationHighlightsForNull()
	{

		final String accommodationCode = null;
		final Integer highlightsNumber = 5;

		facade.getAccommodationHighlights(accommodationCode, highlightsNumber);
	}

	@SuppressWarnings("boxing")
	@Test(expected = NullPointerException.class)
	public void testGetAccommodationHighlightsForEmpty()
	{

		final String accommodationCode = "";
		final Integer highlightsNumber = 9;

		facade.getAccommodationHighlights(accommodationCode, highlightsNumber);
	}

	@SuppressWarnings("boxing")
	@Test(expected = NullPointerException.class)
	public void testGetAccommodationHighlightsForNegative()
	{
		final String accommodationCode = accommodationModel.getCode();
		final Integer highlightsNumber = -1;

		facade.getAccommodationHighlights(accommodationCode, highlightsNumber);
	}

	@Test
	public void testGetAccommodationFacilities()
	{
		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.FACILITIES));

		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.FACILITIES));
		final List<FacilityViewData> failityList = accommodationViewData.getFacilities();
		assertNotNull(failityList);

		final AccommodationViewData expectedResult = facade.getAccommodationFacilities(accommodationCode);
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationViewData.getCode()));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));

	}

	@Test(expected = NullPointerException.class)
	public void testGetAccommodationFacilitiesForNull()
	{
		final String accommodationCode = null;

		facade.getAccommodationFacilities(accommodationCode);

	}

	@Test(expected = NullPointerException.class)
	public void testGetAccommodationFacilitiesForEmpty()
	{
		final String accommodationCode = "";

		facade.getAccommodationFacilities(accommodationCode);
	}

	@Test
	public void testGetGettingAroundEditorial()
	{
		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = accommodationModel.getCode();

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(accommodationConverter.convert(accommodationModel)).thenReturn(accommodationViewData);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		accommodationConfiguredPopulator.populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.GETTINGAROUND));
		Mockito.verify(accommodationConfiguredPopulator).populate(accommodationModel, accommodationViewData,
				Arrays.asList(AccommodationOption.BASIC, AccommodationOption.GETTINGAROUND));
		final AccommodationViewData expectedResult = facade.getGettingAroundEditorial(accommodationCode);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(accommodationCode));
		assertThat(expectedResult.getName(), is(accommodationViewData.getName()));
	}

	@Test
	public void testGetGettingAroundEditorialForNull()
	{
		final CatalogVersionModel catalog = null;
		final String accommodationCode = null;

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);
		final AccommodationViewData expectedResult = facade.getGettingAroundEditorial(accommodationCode);
		assertNull(expectedResult);
	}

	@Test
	public void testGetGettingAroundEditorialForEmpty()
	{
		final CatalogVersionModel catalog = getCatalogVersion();
		final String accommodationCode = "";

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, brandPks)).thenReturn(
				accommodationModel);
		Mockito.when(cacheUtil.getAccommodationDataCacheWrapper()).thenReturn(accomDataCache);

		final AccommodationViewData expectedResult = facade.getGettingAroundEditorial(accommodationCode);

		assertNull(expectedResult);
	}

	@Test(expected = NullPointerException.class)
	public void testGetAccommodationPriceDataFromEndeca()
	{

		facade.getAccommodationPriceDataFromEndeca(searchResultData);

	}

	@Test
	public void testGetAccommodationPriceDataFromEndecaForNull()
	{

		final AccommodationViewData expectedResult = facade.getAccommodationPriceDataFromEndeca(null);

		assertNotNull(expectedResult);

	}
}
