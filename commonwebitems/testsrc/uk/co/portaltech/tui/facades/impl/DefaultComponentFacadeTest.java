/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.components.model.AccommodationsResultComponentModel;
import uk.co.portaltech.tui.components.model.BookingComponentModel;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.DealsComponentModel;
import uk.co.portaltech.tui.components.model.HeroCarouselComponentModel;
import uk.co.portaltech.tui.components.model.HighlightsComponentModel;
import uk.co.portaltech.tui.components.model.InteractiveMapComponentModel;
import uk.co.portaltech.tui.components.model.MediaPromoComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselModel;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TabbedAccommodationCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TouristBoardBannerComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ABTestFacade;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.CarouselFacade;
import uk.co.portaltech.tui.facades.ExcursionFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.ProductRangeFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.facades.impl.DefaultComponentFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.mediafinder.MediaFinder;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.search.EnrichmentService;
import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.HasFeatures;
import uk.co.portaltech.tui.web.view.data.HeroCarouselViewData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MediaPromoViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.PlacesToStayWrapper;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.ThingsToDoWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationFilterDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.DestinationQuickSearchDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.ProductCrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.web.common.enums.CarouselLookupType;


/**
 * @author sunil.bd
 * 
 */
@SuppressWarnings("deprecation")
public class DefaultComponentFacadeTest
{

	@InjectMocks
	private final DefaultComponentFacade Facade = new DefaultComponentFacade();

	@Mock
	private AccommodationCarouselViewData accommodationCarouselViewData;

	@Mock
	private TabbedAccommodationCarouselComponentModel tabbedAccommodationCarouselComponentModel;

	@Mock
	private CMSComponentService cmsComponentService;

	@Mock
	private AttractionService attractionService;

	@Mock
	private AccommodationCarouselModel accommodationCarouselModel;

	@Mock
	private AccommodationViewData accommodationViewData;

	@Mock
	private CarouselFacade carouselFacade;

	@Mock
	private ExcursionFacade excursionFacade;

	@Mock
	private LocationFacade locationFacade;

	@Mock
	List<AccommodationCarouselViewData> accommodationCarouselViewDataList;

	@Mock
	List<AccommodationViewData> accommodationViewDataList;

	@Mock
	List<AccommodationCarouselModel> accommodationCarouselModelList;

	@Mock
	private HeroCarouselViewData heroCarouselViewData;

	@Mock
	private HeroCarouselComponentModel heroCarouselComponentModel;

	@Mock
	private MediaViewData mediaViewData;

	@Mock
	List<MediaViewData> mediaViewDataList;

	@Mock
	private MediaPromoComponentModel mediaPromoComponentModel;

	@Mock
	private MediaPromoViewData mediaPromoViewData;

	@Mock
	private LocationService locationService;

	@Mock
	private CMSSiteService siteService;

	@Mock
	private ItemModel itemModel;

	@Mock
	private LocationModel locationModel;

	@Mock
	private ThingsToDoCarouselComponentModel tCarouselComponentModel;

	@Mock
	private AbstractCMSComponentModel abstractCMSComponentModel;

	@Mock
	private ThingsToDoWrapper tDoWrapper;

	@Mock
	private TouristBoardBannerComponentModel tBoardBannerComponentModel;

	@Mock
	private PriceAndAvailabilityWrapper priceAndAvailabilityWrapper;

	@Mock
	private CatalogVersionModel catalogVersionModel;

	@Mock
	private CrossSellCarouselComponentModel cSellCarouselComponentModel;

	@Mock
	private CrossSellWrapper crossSellWrapper;

	@Mock
	private LocationData locationViewData;

	@Mock
	private DealsComponentModel dealsComponentModel;

	@Mock
	private SearchFacade searchFacade;

	@Mock
	private AccommodationFacade accommodationFacade;

	@Mock
	private ProductRangeFacade productRangeFacade;

	@Mock
	private HighlightsComponentModel highlightsComponentModel;

	@Mock
	private TouristBoardBannerComponentModel tBannerComponentModel;

	@Mock
	private ProductRangeViewData productRangeViewData;

	@Mock
	private Map<String, MediaFinder> mediaFinders;

	@Mock
	private MediaFinder finder;

	@Mock
	private SearchRequestData searchRequestData;

	@Mock
	private HasFeatures heroData;

	@Mock
	private ProductCrossSellCarouselComponentModel pComponentModel;

	@Mock
	private ProductCrossSellWrapper productCrossSellWrapper;

	@Mock
	private DestinationQuickSearchDataWrapper dSearchDataWrapper;

	@Mock
	private TopPlacesToStayCarouselComponentModel tModel;

	@Mock
	private TopPlacesWrapper topPlacesWrapper;

	@Mock
	private PlacesToStayCarouselComponentModel pCarouselComponentModel;

	@Mock
	private PlacesToStayCarouselModel placesToStayCarouselModel;

	@Mock
	private List<PlacesToStayCarouselModel> placesToStayCarouselModelList;

	@Mock
	private List<LocationData> locationDataList;

	@Mock
	private PlacesToStayWrapper placesToStayWrapper;

	@Mock
	private BookingComponentModel bookingComponentModel;

	@Mock
	private AccommodationsResultComponentModel accommodationsResultComponentModel;

	@Mock
	private AccommodationFilterDataWrapper aDataWrapper;

	@Mock
	private InteractiveMapComponentModel interactiveMapComponentModel;

	@Mock
	private MapDataWrapper mapDataWrapper;

	@Mock
	private HolidayViewData holidayViewData;

	@Mock
	private EnrichmentService enrichmentService;

	@Mock
	private SearchPanelComponentModel searchPanelComponentModel;

	@Mock
	private SessionService sessionService;

	@Mock
	private SearchResultsRequestData searchResultsRequestData;

	@Mock
	private SearchResultsViewData searchResultsViewData;

	@Mock
	private ABTestFacade abTestFacade;

	@Mock
	private ViewSelector viewSelector;

	@Mock
	private TuiUtilityService tuiUtilityService;

	private final static String COMPUID = "WF_COM_007-2";
	private final static String lOCATION_CODE = "ESP";
	private final static int VARIANT_PERCENTAGE = 0;
	private final static int INDEX = 5;
	private final Integer SIZE = Integer.valueOf(50);
	private CarouselLookupType carouselLookupType;

	// DefaultComponentFacade defaultComponentFacade
	

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		final AccommodationViewData accommodationViewData = new AccommodationViewData();
		accommodationViewData.setReviewRating("5");
		accommodationViewData.setPriceFrom("1000");
		accommodationViewData.setRoomOccupancy("4");
		accommodationViewData.setStayPeriod("7");
		accommodationViewData.setLocationMapUrl("www.google.com");

		accommodationViewDataList = new ArrayList<AccommodationViewData>();
		accommodationViewDataList.add(accommodationViewData);

		accommodationCarouselViewData = new AccommodationCarouselViewData();
		accommodationCarouselViewData.setAccomodationDatas(accommodationViewDataList);

		accommodationCarouselViewDataList = new ArrayList<AccommodationCarouselViewData>();
		accommodationCarouselViewDataList.add(accommodationCarouselViewData);

		accommodationCarouselModelList = new ArrayList<AccommodationCarouselModel>();
		accommodationCarouselModelList.add(accommodationCarouselModel);

	}



	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultComponentFacade#getAllAccomodationCarouselsData(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAllAccomodationCarouselsData()
	{

		final AbstractCMSComponentModel abstractCMSComponentModel = new AbstractCMSComponentModel();
		abstractCMSComponentModel.setUid("uid");
		abstractCMSComponentModel.setName("name");
		final DefaultComponentFacade defaultComponentFacade = Mockito.mock(DefaultComponentFacade.class);

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(tabbedAccommodationCarouselComponentModel);
		}
		catch (final CMSItemNotFoundException e1)
		{
			e1.printStackTrace();
		}
		Mockito.when(tabbedAccommodationCarouselComponentModel.getAccommodationCarousels()).thenReturn(
				accommodationCarouselModelList);

		Mockito.when(carouselFacade.getAccommodationViewData(accommodationCarouselModel, lOCATION_CODE, null, null, 0, -1))
				.thenReturn(accommodationViewDataList);

		try
		{
			Mockito.when(defaultComponentFacade.getAllAccomodationCarouselsData(COMPUID, lOCATION_CODE)).thenReturn(
					accommodationCarouselViewDataList);
			final List<AccommodationCarouselViewData> expectedResult = accommodationCarouselViewDataList;
			assertThat(expectedResult, is(notNullValue()));
			assertThat(expectedResult.get(0).getAccomodationDatas(), is(accommodationViewDataList));
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	private HeroCarouselViewData getDummyDataForHeroCarouselData()
	{

		final MediaViewData mediaViewData1 = new MediaViewData();
		mediaViewData1.setCode("code");
		mediaViewData1.setDescription("description");
		mediaViewData1.setSize("size");
		final MediaViewData mediaViewData2 = new MediaViewData();
		mediaViewData2.setCode("code");
		mediaViewData2.setDescription("description");
		mediaViewData2.setSize("size");

		mediaViewDataList.add(mediaViewData1);
		mediaViewDataList.add(mediaViewData2);

		return new HeroCarouselViewData(COMPUID, mediaViewDataList);

	}

	@Test
	public void testGetHeroCarouselData()
	{

		final HeroCarouselViewData heroCarouselViewData = getDummyDataForHeroCarouselData();

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(heroCarouselComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}

		Mockito.when(carouselFacade.getHeroCarouselMediaData(heroCarouselComponentModel, 5, 50)).thenReturn(mediaViewDataList);

		try
		{
			final HeroCarouselViewData expectedResult = Facade.getHeroCarouselData(COMPUID, INDEX, SIZE);
			assertThat(expectedResult.getComponentId(), is(heroCarouselViewData.getComponentId()));
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	@Test(expected = NullPointerException.class)
	public void testGetHeroCarouselDataForInvalidData()
	{
		try
		{
			Facade.getHeroCarouselData(null, 0, null);
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	private MediaPromoViewData getDummyDataForMediaCarouselData()
	{

		final MediaViewData mediaViewData1 = new MediaViewData();
		mediaViewData1.setCode("code");
		mediaViewData1.setDescription("description");
		mediaViewData1.setSize("size");
		final MediaViewData mediaViewData2 = new MediaViewData();
		mediaViewData2.setCode("code");
		mediaViewData2.setDescription("description");
		mediaViewData2.setSize("size");

		mediaViewDataList.add(mediaViewData1);
		mediaViewDataList.add(mediaViewData2);

		return new MediaPromoViewData(COMPUID, mediaViewDataList);
	}

	@Test
	public void testGetMediaCarouselData()
	{

		final MediaPromoViewData mediaPromoViewData = getDummyDataForMediaCarouselData();

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(mediaPromoComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}

		Mockito.when(carouselFacade.getMediaCarouselMediaData(mediaPromoComponentModel, 5, 50)).thenReturn(mediaViewDataList);
		try
		{
			final MediaPromoViewData viewData = Facade.getMediaCarouselData(COMPUID, INDEX, SIZE);
			assertThat(viewData.getComponentId(), is(mediaPromoViewData.getComponentId()));
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	@Test(expected = NullPointerException.class)
	public void testGetMediaCarouselDataForInvalidData()
	{
		try
		{
			Facade.getMediaCarouselData(null, INDEX, null);
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void testGetThingsToDoCarousels()
	{

		final DefaultComponentFacade defaultComponentFacade = Mockito.mock(DefaultComponentFacade.class);

		Mockito.when(locationService.getLocationForItem(itemModel, null)).thenReturn(locationModel);

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(tCarouselComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(carouselFacade.getThingsToDoViewData(lOCATION_CODE, "pageType", "seoPageType", tCarouselComponentModel))
				.thenReturn(tDoWrapper);

		try
		{
			Mockito.when(
					defaultComponentFacade
							.getThingsToDoCarousels(COMPUID, lOCATION_CODE, "productCode", "pageType", "seoPageType", -1)).thenReturn(
					tDoWrapper);
			final ThingsToDoWrapper expectedResult = tDoWrapper;
			assertThat(expectedResult.getHeading(), is(tDoWrapper.getHeading()));
			assertThat(expectedResult.getExcursions(), is(tDoWrapper.getExcursions()));
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetThingsToDoCarouselsforTouristBoard()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(tBoardBannerComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(
				carouselFacade.getThingsToDoForTouristBoardViewData(lOCATION_CODE, "pageType", "seoPageType",
						tBoardBannerComponentModel)).thenReturn(tDoWrapper);

		try
		{
			final ThingsToDoWrapper expectedResult = Facade.getThingsToDoCarouselsforTouristBoard(COMPUID, lOCATION_CODE,
					"productCode", "pageType", "seoPageType", -1);
			assertThat(expectedResult, is(tDoWrapper));
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void testGetThingsToDoCarouselsforTouristBoardForNull()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(tBoardBannerComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(
				carouselFacade.getThingsToDoForTouristBoardViewData(lOCATION_CODE, "pageType", "seoPageType",
						tBoardBannerComponentModel)).thenReturn(null);

		try
		{
			final ThingsToDoWrapper expectedResult = Facade.getThingsToDoCarouselsforTouristBoard(null, null, null, null, null, 0);
			assertNull(expectedResult);
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void testGetExcursionViewDataForPriceAndAvailability()
	{

		final BrandDetails brandDetails = new BrandDetails();
		final List<String> brandPks = new ArrayList<String>();
		brandDetails.setRelevantBrands(brandPks);

		Mockito.when(sessionService.getAttribute(Mockito.anyString())).thenReturn(brandDetails);

		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		Mockito.when(attractionService.getAttractionForCode("productCode", catalogVersionModel)).thenReturn(itemModel);
		Mockito.when(locationService.getLocationForItem(itemModel, null)).thenReturn(locationModel);
		Mockito.when(excursionFacade.getLowsetPriceExcursionForLocation(locationModel, itemModel)).thenReturn(
				priceAndAvailabilityWrapper);
		try
		{
			final PriceAndAvailabilityWrapper expectedResult1 = Facade.getExcursionViewDataForPriceAndAvailability("productCode");
			final PriceAndAvailabilityWrapper expectedResult2 = Facade.getExcursionViewDataForPriceAndAvailability(null);
			assertNull(expectedResult1);
			assertNull(expectedResult2);
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetCrossSellCarousels()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(cSellCarouselComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}

		Mockito
				.when(carouselFacade.getCrossSellCarouselsData(null, "productCode", "pageType", "seoPageType",
						cSellCarouselComponentModel)).thenReturn(crossSellWrapper);
		Mockito
				.when(carouselFacade.getCrossSellCarouselsData(lOCATION_CODE, null, "pageType", "seoPageType",
						cSellCarouselComponentModel)).thenReturn(crossSellWrapper);
		try
		{
			final CrossSellWrapper expectedResult1 = Facade.getCrossSellCarousels(COMPUID, lOCATION_CODE, "productCode", "pageType",
					"seoPageType");
			final CrossSellWrapper expectedResult2 = Facade.getCrossSellCarousels(null, null, null, null, null);
			assertThat(expectedResult1, is(crossSellWrapper));
			assertNull(expectedResult2);

		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAccommodationCarouselData()
	{
		final DefaultComponentFacade defaultComponentFacade = Mockito.mock(DefaultComponentFacade.class);
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(tabbedAccommodationCarouselComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(tabbedAccommodationCarouselComponentModel.getAccommodationCarousels()).thenReturn(
				accommodationCarouselModelList);

		Mockito.when(locationFacade.getLocationData(lOCATION_CODE)).thenReturn(locationViewData);

		Mockito.when(defaultComponentFacade.getAccommodationCarouselData(lOCATION_CODE, "pageType", "seoType", COMPUID))
				.thenReturn(locationViewData);

		final LocationData expectedResult = locationViewData;
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult, is(locationViewData));
	}

	@Test(expected = NullPointerException.class)
	public void testGetDealsData()
	{

		try
		{
			Facade.getDealsData(lOCATION_CODE, "productCode", "pageType", "seoPageType", COMPUID);
			Facade.getDealsData(null, null, null, null, null);
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetLocationHighlights()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(highlightsComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(highlightsComponentModel.getHighlightsNumber()).thenReturn(5);
		Mockito.when(locationFacade.getLocationHighlights(lOCATION_CODE, 5)).thenReturn(locationViewData);
		final LocationData expectedResult = Facade.getLocationHighlights(COMPUID, lOCATION_CODE);
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult, is(locationViewData));
		assertThat(expectedResult.getCount(), is(locationViewData.getCount()));
	}

	@Test(expected = NullPointerException.class)
	public void testGetLocationHighlightsForNullAndBlank()
	{
		Facade.getLocationHighlights("", "");
		Facade.getLocationHighlights(null, null);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetLocationAtAGlance()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(tBannerComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(tBannerComponentModel.getAtAGlanceNumber()).thenReturn(5);
		Mockito.when(locationFacade.getLocationHighlights(lOCATION_CODE, 5)).thenReturn(locationViewData);
		final LocationData expectedResult = Facade.getLocationAtAGlance(COMPUID, lOCATION_CODE);
		assertThat(expectedResult, is(locationViewData));
		assertThat(expectedResult, is(notNullValue()));
	}

	@Test(expected = NullPointerException.class)
	public void testGetLocationAtAGlanceForNullAndBlank()
	{
		Facade.getLocationAtAGlance("", "");
		Facade.getLocationAtAGlance(null, null);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetAccommodationHighlights()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(highlightsComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(highlightsComponentModel.getHighlightsNumber()).thenReturn(5);
		Mockito.when(accommodationFacade.getAccommodationHighlights("accCode", 5)).thenReturn(accommodationViewData);

		final AccommodationViewData expectedResult = Facade.getAccommodationHighlights(COMPUID, "accCode");
		assertThat(expectedResult, is(accommodationViewData));
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getAccommodationType(), is(accommodationViewData.getAccommodationType()));
	}

	@Test(expected = NullPointerException.class)
	public void testGetAccommodationHighlightsForNullAndBlank()
	{

		Facade.getAccommodationHighlights("", "");
		Facade.getAccommodationHighlights(null, null);

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetProductRangeHighlights()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(highlightsComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(highlightsComponentModel.getHighlightsNumber()).thenReturn(5);
		Mockito.when(productRangeFacade.getProductRangeHighlights("productRangeCode", 5)).thenReturn(productRangeViewData);

		final ProductRangeViewData expectedResult = Facade.getProductRangeHighlights(COMPUID, "productRangeCode");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult, is(productRangeViewData));
	}

	@Test(expected = NullPointerException.class)
	public void testGetProductRangeHighlightsForNullAndBlank()
	{

		Facade.getProductRangeHighlights("", "");
		Facade.getProductRangeHighlights(null, null);

	}

	public SearchRequestData setDummySearchRequestData()
	{
		searchRequestData.setPageSize(5);
		searchRequestData.setOffset(0);
		searchRequestData.setRelevantItem(heroCarouselComponentModel);

		return searchRequestData;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetHeroCarouselViewData()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(heroCarouselComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(heroCarouselComponentModel.getLookupType()).thenReturn(carouselLookupType);
		Mockito.when(mediaFinders.get("mediaFinder_" + carouselLookupType)).thenReturn(finder);

		final SearchRequestData data = setDummySearchRequestData();
		if (CarouselLookupType.AUTOMATIC.equals(heroCarouselComponentModel.getLookupType()))
		{
			Mockito.when(finder.searchAutomatic(data, "code", "type")).thenReturn(heroData);
			final HasFeatures expectedResult1 = Facade.getHeroCarouselViewData(COMPUID, "code", "AUTOMATIC");
			assertThat(expectedResult1, is(notNullValue()));
			assertThat(expectedResult1, is(heroData));
		}
		else if (CarouselLookupType.MANUAL.equals(heroCarouselComponentModel.getLookupType()))
		{
			Mockito.when(finder.searchManual(data, "code", "type")).thenReturn(heroData);
			final HasFeatures expectedResult2 = Facade.getHeroCarouselViewData(COMPUID, "code", "MANUAL");
			assertThat(expectedResult2, is(notNullValue()));
			assertThat(expectedResult2, is(heroData));
		}
	}

	@Test(expected = NullPointerException.class)
	public void testGetHeroCarouselViewDataForNullAndBlank()
	{
		Facade.getHeroCarouselViewData("", "", "");
		Facade.getHeroCarouselViewData(null, null, null);
	}

	public SearchRequestData setDummySearchRequestDataForMediaCarousel()
	{
		searchRequestData.setPageSize(5);
		searchRequestData.setOffset(0);
		searchRequestData.setRelevantItem(mediaPromoComponentModel);

		return searchRequestData;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetMediaCarouselViewData()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(mediaPromoComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(mediaPromoComponentModel.getLookupType()).thenReturn(carouselLookupType);
		Mockito.when(mediaFinders.get("mediaFinder_" + carouselLookupType)).thenReturn(finder);
		final SearchRequestData data = setDummySearchRequestDataForMediaCarousel();

		if (CarouselLookupType.AUTOMATIC.equals(mediaPromoComponentModel.getLookupType()))
		{
			Mockito.when(finder.searchAutomatic(data, "code", "type")).thenReturn(heroData);
			final HasFeatures expectedResult1 = Facade.getMediaCarouselViewData(COMPUID, "code", "AUTOMATIC");
			assertThat(expectedResult1, is(notNullValue()));
			assertThat(expectedResult1, is(heroData));
		}

		else if (CarouselLookupType.MANUAL.equals(mediaPromoComponentModel.getLookupType()))
		{
			Mockito.when(finder.searchManual(data, "code", "type")).thenReturn(heroData);
			final HasFeatures expectedResult2 = Facade.getMediaCarouselViewData(COMPUID, "code", "MANUAL");
			assertThat(expectedResult2, is(notNullValue()));
			assertThat(expectedResult2, is(heroData));
		}

		final HasFeatures expectedResult3 = Facade.getMediaCarouselViewData(COMPUID, "code", "");
		assertNull(expectedResult3);
	}

	public SearchRequestData setDummySearchRequestDataForImageCarousel()
	{
		searchRequestData.setPageSize(5);
		searchRequestData.setOffset(0);
		searchRequestData.setRelevantItem(tBoardBannerComponentModel);

		return searchRequestData;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetImageCarouselViewData()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(tBoardBannerComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(tBoardBannerComponentModel.getLookupType()).thenReturn(carouselLookupType);
		Mockito.when(mediaFinders.get("mediaFinder_" + carouselLookupType)).thenReturn(finder);
		final SearchRequestData data = setDummySearchRequestDataForImageCarousel();

		if (CarouselLookupType.AUTOMATIC.equals(tBoardBannerComponentModel.getLookupType()))
		{
			Mockito.when(finder.searchAutomatic(data, "code", "type")).thenReturn(heroData);
			final HasFeatures expectedResult1 = Facade.getImageCarouselViewData(COMPUID, "code", "AUTOMATIC");
			assertThat(expectedResult1, is(notNullValue()));
			assertThat(expectedResult1, is(heroData));
		}

		else if (CarouselLookupType.MANUAL.equals(mediaPromoComponentModel.getLookupType()))
		{
			Mockito.when(finder.searchManual(data, "code", "type")).thenReturn(heroData);
			final HasFeatures expectedResult2 = Facade.getImageCarouselViewData(COMPUID, "code", "MANUAL");
			assertThat(expectedResult2, is(notNullValue()));
			assertThat(expectedResult2, is(heroData));
		}

		final HasFeatures expectedResult3 = Facade.getImageCarouselViewData(COMPUID, "code", "");
		assertNull(expectedResult3);
	}

	/*
	 * @Test public void testGetProductCrossSellCarouselData() { try {
	 * Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)) .thenReturn(pComponentModel); } catch
	 * (CMSItemNotFoundException e) { e.printStackTrace(); } Mockito.when(
	 * carouselFacade.getProductCrossSellCarouselData(lOCATION_CODE, "pageType", "seoType", pComponentModel)).thenReturn(
	 * productCrossSellWrapper);
	 * 
	 * try { final ProductCrossSellWrapper expectedResult = Facade .getProductCrossSellCarouselData(COMPUID, "pageType",
	 * "seoType", lOCATION_CODE); assertThat(expectedResult, is(notNullValue())); } catch (NoSuchComponentException e) {
	 * e.printStackTrace(); } }
	 * 
	 * @Test public void testGetProductCrossSellCarouselDataForNullAndBlank() {
	 * 
	 * try { final ProductCrossSellWrapper expectedResult1 = Facade .getProductCrossSellCarouselData("", "", "", "");
	 * final ProductCrossSellWrapper expectedResult2 = Facade .getProductCrossSellCarouselData(null, null, null, null);
	 * assertNull(expectedResult1); assertNull(expectedResult2); } catch (NoSuchComponentException e) {
	 * e.printStackTrace(); } }
	 */
	@Test
	public void testGetQuickSearchResultData()
	{
		Mockito.when(searchFacade.getQuickSearchResultData("searchParameter")).thenReturn(dSearchDataWrapper);
		try
		{
			final DestinationQuickSearchDataWrapper expectedResult = Facade.getQuickSearchResultData("searchParameter");
			final DestinationQuickSearchDataWrapper expectedResult1 = Facade.getQuickSearchResultData("");
			final DestinationQuickSearchDataWrapper expectedResult2 = Facade.getQuickSearchResultData(null);
			assertThat(expectedResult, is(notNullValue()));
			assertThat(expectedResult, is(dSearchDataWrapper));
			assertNull(expectedResult1);
			assertNull(expectedResult2);
		}
		catch (final NoSuchComponentException e)
		{
			Log.error("The component doesn't exists");
		}
	}

	@Test
	public void testGetTopPlacesToStayData()
	{
		Mockito.when(carouselFacade.getTopPlacesToStayData(tModel, lOCATION_CODE, "pageType", "seoPageType", 0)).thenReturn(
				topPlacesWrapper);

		final TopPlacesWrapper expectedResult = Facade.getTopPlacesToStayData(lOCATION_CODE, "pageType",
				"RECOMMENDED_HOLIDAYS_PAGE", tModel);
		assertNull(expectedResult);
	}

	@SuppressWarnings("boxing")
	public LocationData getDataForPlacesToStayCarouselData()
	{
		placesToStayCarouselModel = new PlacesToStayCarouselModel();
		placesToStayCarouselModel.setName("model");
		placesToStayCarouselModel.setVisibleItems(3);
		placesToStayCarouselModel.setUid("itemModel");

		placesToStayCarouselModelList = new ArrayList<PlacesToStayCarouselModel>();
		placesToStayCarouselModelList.add(placesToStayCarouselModel);

		final LocationData data1 = new LocationData();
		data1.setThingstodoMapUrl("www.google.com");
		data1.setPriceFrom("priceFrom");
		locationDataList = new ArrayList<LocationData>();
		locationDataList.add(data1);

		placesToStayWrapper.setTopLocationName("Name");

		return data1;
	}

	@Test
	public void testGetPlacesToStayCarouselData()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(pCarouselComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}

		final LocationData locationData = getDataForPlacesToStayCarouselData();

		Mockito.when(pCarouselComponentModel.getPlacesToStayCarousels()).thenReturn(placesToStayCarouselModelList);
		Mockito.when(searchFacade.getPlacesToStayCarouselData(lOCATION_CODE, "pageType", "seoType", placesToStayCarouselModel))
				.thenReturn(locationDataList);

		Mockito.when(locationFacade.getLocationData(lOCATION_CODE)).thenReturn(locationData);
		final PlacesToStayWrapper expectedResult = Facade
				.getPlacesToStayCarouselData(lOCATION_CODE, "pageType", "seoType", COMPUID);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getTopLocationName(), is(placesToStayWrapper.getTopLocationName()));
	}

	@Test(expected = NullPointerException.class)
	public void testGetPlacesToStayCarouselDataForNullAndBlank()
	{

		Facade.getPlacesToStayCarouselData("", "", "", "");
		Facade.getPlacesToStayCarouselData(null, null, null, null);
	}

	@Test
	public void testGetAccommodationPrice()
	{

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(bookingComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}

		Mockito.when(searchFacade.getAccommodationPriceData("accommodationCode", bookingComponentModel)).thenReturn(
				accommodationViewData);
		final AccommodationViewData expectedResult = Facade.getAccommodationPrice(COMPUID, "accommodationCode");
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getAccommodationType(), is(accommodationViewData.getAccommodationType()));
	}

	@Test
	public void testGetAccommodationPriceForNullAndBlank()
	{

		final AccommodationViewData expectedResult1 = Facade.getAccommodationPrice("", "");
		final AccommodationViewData expectedResult2 = Facade.getAccommodationPrice(null, null);
		assertNull(expectedResult1);
		assertNull(expectedResult2);
	}

	private List<String> getDummyFacetOptionNames()
	{
		final List<String> names = new ArrayList<String>();
		names.add("name1");
		names.add("name2");
		return names;
	}

	private List<String> getDummyFilterParams()
	{
		final List<String> params = new ArrayList<String>();
		params.add("param1");
		params.add("param2");

		return params;
	}

	@Test
	public void testGetAccommodationResultAndFilterData()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(accommodationsResultComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		final List<String> facetOptionNames = getDummyFacetOptionNames();
		final List<String> filterParams = getDummyFilterParams();
		Mockito.when(
				searchFacade.getAccommodationResultAndFilterData("categoryCode", "pageType", "seoType", facetOptionNames,
						filterParams, accommodationsResultComponentModel)).thenReturn(aDataWrapper);

		try
		{
			final AccommodationFilterDataWrapper expectedResult = Facade.getAccommodationResultAndFilterData(COMPUID, "pageType",
					"seoType", "categoryCode", facetOptionNames, filterParams);
			assertThat(expectedResult, is(notNullValue()));
			assertThat(expectedResult.getAccommodations(), is(aDataWrapper.getAccommodations()));
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAccommodationResultAndFilterDataForNullAndBlank()
	{

		AccommodationFilterDataWrapper expectedResult;
		try
		{
			expectedResult = Facade.getAccommodationResultAndFilterData("", "", "", "", null, null);
			assertNull(expectedResult);
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void testGetInteractiveMapData()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(COMPUID)).thenReturn(interactiveMapComponentModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		Mockito.when(searchFacade.getInteractiveMapData("categoryCode", "pageType", "seoPageType", interactiveMapComponentModel))
				.thenReturn(mapDataWrapper);
		try
		{
			final MapDataWrapper expectedResult = Facade.getInteractiveMapData("categoryCode", "pageType", "seoPageType", COMPUID);
			assertThat(expectedResult, is(notNullValue()));
			assertThat(expectedResult.getTopLocationName(), is(mapDataWrapper.getTopLocationName()));
			assertThat(expectedResult.getLocationType(), is(mapDataWrapper.getLocationType()));

		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetInteractiveMapDataForNullAndBlank()
	{

		try
		{
			final MapDataWrapper expectedResult1 = Facade.getInteractiveMapData("", "", "", "");
			final MapDataWrapper expectedResult2 = Facade.getInteractiveMapData(null, null, null, null);
			assertNull(expectedResult1);
			assertNull(expectedResult2);
		}
		catch (final NoSuchComponentException e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void testGetHolidayPackagesResultData()
	{
		enrichmentService.enrich(searchPanelComponentModel, searchResultsRequestData);

		try
		{
			Mockito.when(searchFacade.getHolidayPackagesResultData(searchResultsRequestData, tuiUtilityService.getSiteBrand()))
					.thenReturn(searchResultsViewData);
			holidayViewData.setSearchResult(searchResultsViewData);
			sessionService.setAttribute("latestCriteria", searchResultsRequestData);

			final HolidayViewData expectedResult1 = Facade.getHolidayPackagesResultData(searchResultsRequestData,
					tuiUtilityService.getSiteBrand());
			assertNotNull(expectedResult1);
		}
		catch (final SearchResultsBusinessException e)
		{
			e.printStackTrace();
		}
	}

	@Test(expected = NullPointerException.class)
	public void testGetHolidayPackagesResultDataForNull()
	{
		try
		{
			Facade.getHolidayPackagesResultData(null, null);
		}
		catch (final SearchResultsBusinessException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetVariantPercentageByCode()
	{
		Mockito.when(abTestFacade.getVariantPercentageByCode("variantCode")).thenReturn(VARIANT_PERCENTAGE);
		final int expectedResult = Facade.getVariantPercentageByCode("iscape");
		assertEquals(expectedResult, 0);

	}
	
	@Test
	public void testGetReccommendedHolidayPackageData()
	{
		enrichmentService.enrich(searchPanelComponentModel, searchResultsRequestData);

		try
		{
			Mockito.when(searchFacade.getHolidayPackagesResultData(searchResultsRequestData, tuiUtilityService.getSiteBrand()))
					.thenReturn(searchResultsViewData);
			holidayViewData.setSearchResult(searchResultsViewData);
			sessionService.setAttribute("latestCriteria", searchResultsRequestData);

			final HolidayViewData expectedResult1 = Facade.getHolidayPackagesResultData(searchResultsRequestData,
					tuiUtilityService.getSiteBrand());
			assertNotNull(expectedResult1);
		}
		catch (final SearchResultsBusinessException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRecommendedAccomPriceInfo(){
		
		List<AccommodationViewData> accommodationViewDataList=new ArrayList<AccommodationViewData>() ;
		List<String> productCodes=new ArrayList<String> ();
		productCodes.add("002638");
		Mockito.when(searchFacade.getRecommendedAccomPriceInfo(productCodes)).thenReturn(accommodationViewDataList);
		
	}
}
