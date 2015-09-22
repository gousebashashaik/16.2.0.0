/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static com.google.common.collect.Collections2.filter;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.ComponentNotFoundException;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.BoardBasisContentModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.MultiValueFilterComponentModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.services.BoardBasisService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.comparators.sort.SearchResultViewDataComparator;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.components.model.AccommodationStandardComponentModel;
import uk.co.portaltech.tui.components.model.AccommodationsResultComponentModel;
import uk.co.portaltech.tui.components.model.AttractionLightBoxComponentModel;
import uk.co.portaltech.tui.components.model.AvailableFlightsComponentModel;
import uk.co.portaltech.tui.components.model.BookingComponentModel;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.DaytripsPromoComponentModel;
import uk.co.portaltech.tui.components.model.DealsComponentModel;
import uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel;
import uk.co.portaltech.tui.components.model.HeroCarouselComponentModel;
import uk.co.portaltech.tui.components.model.HighlightsComponentModel;
import uk.co.portaltech.tui.components.model.InteractiveMapComponentModel;
import uk.co.portaltech.tui.components.model.MediaPromoComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselModel;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ProductLeadPriceComponentModel;
import uk.co.portaltech.tui.components.model.TabbedAccommodationCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.components.model.TouristBoardBannerComponentModel;
import uk.co.portaltech.tui.components.model.UpsellToHolidayComponentModel;
import uk.co.portaltech.tui.components.model.VillaAvailabilityComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ABTestFacade;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.AttractionFacade;
import uk.co.portaltech.tui.facades.CarouselFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.ExcursionFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.ProductRangeFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.populators.BoardBasisDataPopulator;
import uk.co.portaltech.tui.populators.SearchPanelDataPopulator;
import uk.co.portaltech.tui.populators.ThingToSeeAndDoDataPopulator;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.mediafinder.MediaFinder;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.search.EnrichmentService;
import uk.co.portaltech.tui.web.view.data.ABTestViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.BoardBasisData;
import uk.co.portaltech.tui.web.view.data.CommonFilterData;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.FeatureListViewData;
import uk.co.portaltech.tui.web.view.data.HasFeatures;
import uk.co.portaltech.tui.web.view.data.HeroCarouselViewData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.LocationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MediaPromoViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.PlacesOfInterestViewData;
import uk.co.portaltech.tui.web.view.data.PlacesToStayWrapper;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.SearchPanelViewData;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.ThingsToDoWrapper;
import uk.co.portaltech.tui.web.view.data.ThingsToSeeAndDoEditorialData;
import uk.co.portaltech.tui.web.view.data.UpSellCarouselWrapper;
import uk.co.portaltech.tui.web.view.data.WidenSearchCriteriaData;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationFilterDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationStandardWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.DestinationQuickSearchDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.FlightAvailabilityViewDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.ProductCrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityRequest;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityResponse;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.components.model.ConfPageHeroCarouselComponentModel;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.exception.TUISystemException;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;
import uk.co.tui.web.common.enums.CarouselLookupType;

import com.google.common.base.Predicate;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;


/**
 * @author omonikhide
 *
 */
public class DefaultComponentFacade implements ComponentFacade
{
	/**
     *
     */
	private static final String GENERIC_KEY_GENERATOR = "uk.co.portaltech.tui.utils.GenericKeyGenerator";

	private static final String CROSS_SELL_KEY_GENERATOR = "uk.co.portaltech.tui.utils.CrossSellCacheKeyGenerator";

	private static final TUILogUtils LOG = new TUILogUtils("DefaultComponentFacade");

	private static final String ENABLE_SHORTLIST_KEY = "%s.enable.shortlist";

	@Resource
	private CMSComponentService cmsComponentService;

	@Resource
	private TUIConfigService tuiConfigService;

	@Resource
	private LocationFacade locationFacade;

	@Resource
	private SearchFacade searchFacade;

	@Autowired
	private Map<String, MediaFinder> mediaFinders;

	@Resource
	private CarouselFacade carouselFacade;

	@Resource
	private ABTestFacade abTestFacade;

	@Resource
	private AccommodationFacade accomodationFacade;

	@Resource
	private ProductRangeFacade productRangeFacade;

	@Resource(name = "tuiLocationService")
	private LocationService locationService;

	@Resource
	private AccommodationService accommodationService;

	@Resource
	private CMSSiteService cmsSiteService;

	@Resource
	private AttractionService attractionService;

	@Resource
	private ProductService productService;

	@Resource
	private ExcursionFacade excursionFacade;

	@Resource
	private SessionService sessionService;

	@Resource
	private ViewSelector viewSelector;

	@Resource
	private SearchPanelDataPopulator searchPanelDataPopulator;

	@Resource
	private CategoryService categoryService;

	// for avoiding the duplication of literals
	private static final String LOG_MSG = "No component with uid ";

	@Resource
	private EnrichmentService enrichmentService;

	@Resource
	private AccommodationStandardFacade accommodationStandardFacade;

	@Resource
	private AttractionFacade attractionFacade;

	@Resource
	private MainStreamTravelLocationService mainStreamTravelLocationService;

	@Resource
	private TuiUtilityService tuiUtilityService;

	@Resource
	private ThingToSeeAndDoDataPopulator thingToSeeAndDoDataPopulator;

	@Resource
	private BrandUtils brandUtils;

	private static final String SEARCH_PANELCOMPONENT_ID = "WF_COM_300";

	private static final String NEW_SEARCH_PANELCOMPONENT_ID = "WF_COM_300B";

	private static final String KEY_GENERATOR = "uk.co.portaltech.tui.utils.TopPlacesCacheKeyGenerator";

	private static final String SORT = "sort";

	private static final String FILTER = "Filter";

	private static final String PAGINATE = "paginate";

	private static final String DATESLIDER = "dateslider";

	private static final String DURATION = "duration";

	private static final String ROOMS = "rooms";

	private static final String INS = "ins";

	private static final String LATESTCRITERIA = "latestCriteria";

	private static final String BACKTOSEARCHLATESTCRITERIA = "backlatestCriteria";

	private static final String SEARCHRESULTS = "searchResults";

	private static final String VILLA = "VILLA";

	private static final String MEDIAFINDER = "mediaFinder_";

	private static final String NO_MEDIA_FOUNDER_FOR_LOOKUP_TYPE = "No mediaFinder for lookup type ";

	private static final String CHECK_LOOKUP_TYPE_FIELD_OR_CREATE_FINDER_FOR_LOOKUP_TYPE = " found. Check the lookupType field or create a Finder for the lookupType.";

	private static final String NO_COMPONENT_FOUND_WITH_ID = "No component found with id: ";

	private static final List<String> NORMAL_BOARD_NAMES = Arrays.asList("roomonly", "selfcatering", "bed&breakfast",
			"asperbrochure", "bedandbreakfast");

	private static final String HOLIDAY_FINDER_COMPONENT_ID = "WF_COM_301";

	private static final String LOCATION = "location";

	// Board Basis starting
	@Resource
	private BoardBasisService boardBasisService;

	@Resource
	private BoardBasisDataPopulator boardBasisDataPopulator;

	private static final String MULTI_VALUE_FILTER_UID = "featuresFilter";

	private static final String CROSSSELLCAROUSELCACHE = "crossSellCarouselCache";

	private static final String PLACESTOSTAYCAROUSELCACHE = "placesToStayCarouselCache";

	private static final String NO_COMPONENT_FOUND = " No component found with the id ";

	private static final String NO_SEARCH_PANEL_COMPONENT = "No search panel component";

	private static final String NO_LOCATION_CODE = "Could not get location for the code: ";

	private static final String HOTEL = "HOTEL";

	private static final int FORTY = 40;

	// Flight Option Page
	@Override
	public HolidayViewData getSingleAccommSearchData(final SearchResultsRequestData requestData)
			throws SearchResultsBusinessException
	{
		final HolidayViewData holidayViewData = new HolidayViewData();
		holidayViewData.setSearchRequest(cloneSearchCriteria(requestData));
		enrichmentService.enrich(getSearchPanelComponent(), requestData);
		if (!requestData.isSingleAccomSearch())
		{
			throw new SearchResultsBusinessException("Only Single Accommodation Search Can Happen In Flight Option Page!!!");
		}
		sessionService.setAttribute(LATESTCRITERIA, requestData);
		holidayViewData.setSearchResult(searchFacade.getHolidayPackagesResultDataForFacets(requestData, null));

		return holidayViewData;
	}

	// BoardBasis starting
	/**
	 * @param accommCode
	 * @return boardMap
	 * @description This method takes accommodationCode as argument and return the board basis datas available for
	 *              accommodation with particular accommodationCode. Before returning the boardbasis data splits the data
	 *              in two types of board list a)normal accommodation b)complex accommodation
	 */
	@Override
	@Cacheable(cacheName = "accomBoardBasisCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public Map<String, List<BoardBasisData>> getAccommodationBoardBasisComponentData(final String accommCode)
	{

		final List<BoardBasisData> boardBasisDatas = new ArrayList<BoardBasisData>();
		final List<BoardBasisContentModel> boardBasisModels = boardBasisService.getBoardBasisDatasForAccommodation(accommCode,
				cmsSiteService.getCurrentCatalogVersion(), null);
		Map<String, List<BoardBasisData>> boardMap = new HashMap<String, List<BoardBasisData>>();

		if (!boardBasisModels.isEmpty())
		{
			for (final BoardBasisContentModel boardBasisModel : boardBasisModels)
			{
				final BoardBasisData boardBasisData = new BoardBasisData();
				boardBasisDataPopulator.populate(boardBasisModel, boardBasisData);
				boardBasisDatas.add(boardBasisData);
			}
			boardMap = filterBoardBasisData(boardBasisDatas);
		}
		return boardMap;
	}

	/**
	 * @param boardBasisDatas
	 * @return boardMap
	 * @description This method splits the board basis data in two types of board list a)normal accommodation b)complex
	 *              accommodation
	 */
	private Map<String, List<BoardBasisData>> filterBoardBasisData(final List<BoardBasisData> boardBasisDatas)
	{
		final Map<String, List<BoardBasisData>> boardMap = new HashMap<String, List<BoardBasisData>>();

		final List<BoardBasisData> normalBoardBasisDatas = new ArrayList<BoardBasisData>();
		final List<BoardBasisData> complexBoardBasisDatas = new ArrayList<BoardBasisData>();

		for (final BoardBasisData basisData : boardBasisDatas)
		{
			if (NORMAL_BOARD_NAMES.contains(basisData.getName().replace(" ", "").toLowerCase()))
			{
				normalBoardBasisDatas.add(basisData);
			}
			else
			{
				complexBoardBasisDatas.add(basisData);
			}
		}
		boardMap.put(CommonwebitemsConstants.NORMAL_BOARD_LIST, normalBoardBasisDatas);
		boardMap.put(CommonwebitemsConstants.COMPLEX_BOARD_LIST, complexBoardBasisDatas);
		return boardMap;
	}

	// BoardBasis End

	@Override
	@Cacheable(cacheName = "villaDetailsCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public AccommodationViewData getVillaDetailsComponentData(final String villaCode)
	{
		return accomodationFacade.getAccomWithClassifiedFacilityList(villaCode);
	}

	@Override
	public <T extends AbstractCMSComponentModel> T getComponent(final String componentUid) throws NoSuchComponentException
	{
		try
		{
			return cmsComponentService.getAbstractCMSComponent(componentUid);
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error("No such component exception for component id" + componentUid, e);
			throw new NoSuchComponentException(componentUid, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getAllAccomodationCarouselsData (java.lang.String)
	 */
	// Commented as part of performance fix.
	// @Cacheable(cacheName = "allAccomodationCarouselsCache", keyGenerator = @KeyGenerator(name =
	// GENERIC_KEY_GENERATOR))
	@Override
	public List<AccommodationCarouselViewData> getAllAccomodationCarouselsData(final String componentUid, final String locationCode)
			throws NoSuchComponentException
	{
		final List<AccommodationCarouselViewData> list = new ArrayList<AccommodationCarouselViewData>();
		final TabbedAccommodationCarouselComponentModel tabbedAccommodationCarouselModel = (TabbedAccommodationCarouselComponentModel) getComponent(componentUid);
		final List<AccommodationCarouselModel> accommodationCarousels = tabbedAccommodationCarouselModel
				.getAccommodationCarousels();

		for (int x = 0; x < accommodationCarousels.size(); x++)
		{
			final AccommodationCarouselModel carouselModel = accommodationCarousels.get(x);
			list.add(new AccommodationCarouselViewData(carouselModel.getTitle(), carouselModel.getUid(), carouselFacade
					.getAccommodationViewData(carouselModel, locationCode, null, null, 0, -1),
					carouselModel.getLookupType().getCode(), carouselModel.getVisibleItems()));
		}
		return list;
	}

	@Override
	@Cacheable(cacheName = "heroCarouselDataCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public HeroCarouselViewData getHeroCarouselData(final String componentUid, final int index, final Integer size)
			throws NoSuchComponentException
	{
		Integer pageSize = size;
		final HeroCarouselComponentModel heroComponentModel = (HeroCarouselComponentModel) getComponent(componentUid);
		if (pageSize == null)
		{
			pageSize = heroComponentModel.getVisibleItems();
		}
		final List<MediaViewData> imageCarouselData = carouselFacade.getHeroCarouselMediaData(heroComponentModel, index,
				pageSize.intValue());
		return new HeroCarouselViewData(componentUid, imageCarouselData);

	}

	@Override
	@Cacheable(cacheName = "thingsToDoCarouselCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public ThingsToDoWrapper getThingsToDoCarousels(final String componentUid, final String code, final String productCode,
			final String pageType, final String seoPageType, final int visibleItems) throws NoSuchComponentException
	{
		String locationCode = code;
		if (StringUtils.isEmpty(locationCode) && StringUtils.isNotEmpty(productCode))
		{
			final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
			final List<String> brandPks = brandDetails.getRelevantBrands();
			final LocationModel locationForItem = locationService.getLocationForItem(accommodationService
					.getAccomodationByCodeAndCatalogVersion(productCode, cmsSiteService.getCurrentCatalogVersion(), null), brandPks);
			if (locationForItem != null)
			{
				locationCode = locationForItem.getCode();
			}
			else
			{
				return null;
			}

		}
		final ThingsToDoCarouselComponentModel thingsToDoCarouselComponentModel = (ThingsToDoCarouselComponentModel) getComponent(componentUid);
		return carouselFacade.getThingsToDoViewData(locationCode, pageType, seoPageType, thingsToDoCarouselComponentModel);
	}

	@Override
	@Cacheable(cacheName = "thingsToDoCarouselCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public ThingsToDoWrapper getThingsToDoCarouselsforTouristBoard(final String componentUid, final String code,
			final String productCode, final String pageType, final String seoPageType, final int visibleItems)
			throws NoSuchComponentException
	{
		String locationCode = code;
		if (StringUtils.isEmpty(locationCode) && StringUtils.isNotEmpty(productCode))
		{
			final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
			final List<String> brandPks = brandDetails.getRelevantBrands();
			final LocationModel locationForItem = locationService.getLocationForItem(accommodationService
					.getAccomodationByCodeAndCatalogVersion(productCode, cmsSiteService.getCurrentCatalogVersion(), null), brandPks);
			locationCode = locationForItem.getCode();
		}
		final TouristBoardBannerComponentModel touristBoardBannerComponentModel = (TouristBoardBannerComponentModel) getComponent(componentUid);
		return carouselFacade.getThingsToDoForTouristBoardViewData(locationCode, pageType, seoPageType,
				touristBoardBannerComponentModel);
	}

	@Override
	@Cacheable(cacheName = "excursionViewDataPriceCache")
	public PriceAndAvailabilityWrapper getExcursionViewDataForPriceAndAvailability(final String productCode)
			throws NoSuchComponentException
	{
		PriceAndAvailabilityWrapper priceAndAvailabilityWrapper = null;
		if (StringUtils.isNotEmpty(productCode))
		{
			final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
			final List<String> brandPks = brandDetails.getRelevantBrands();
			final ItemModel attraction = attractionService.getAttractionForCode(productCode,
					cmsSiteService.getCurrentCatalogVersion());
			final LocationModel location = locationService.getLocationForItem(attraction, brandPks);

			if (location != null)
			{
				priceAndAvailabilityWrapper = excursionFacade.getLowsetPriceExcursionForLocation(location, attraction);
			}
		}
		return priceAndAvailabilityWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getCrossSellCarousels(java .lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CROSSSELLCAROUSELCACHE, keyGenerator = @KeyGenerator(name = CROSS_SELL_KEY_GENERATOR))
	public CrossSellWrapper getCrossSellCarousels(final String componentUid, final String locationCode, final String productCode,
			final String pageType, final String seoPageType, final String acommodationTypeContext) throws NoSuchComponentException
	{

		final CrossSellCarouselComponentModel crossSellCarouselComponentModel = (CrossSellCarouselComponentModel) getComponent(componentUid);

		if (StringUtils.isNotBlank(productCode))
		{
			// This condition is added since -Incase of Attraction crossell --
			// the attarction code value comes in productCode ,
			// due to which the call to product service throws an expection as
			// attraction is not a product , its temporal item

			String accomType = null;
			final ProductModel productModel = productService.getProductForCode(cmsSiteService.getCurrentCatalogVersion(),
					productCode);
			if (productModel instanceof AccommodationModel)
			{
				accomType = ((AccommodationModel) productModel).getType().toString();
			}
			return carouselFacade.getCrossSellCarouselsData(null, productCode, pageType, seoPageType,
					crossSellCarouselComponentModel, accomType);

		}
		else
		{
			return carouselFacade.getCrossSellCarouselsData(locationCode, null, pageType, seoPageType,
					crossSellCarouselComponentModel, acommodationTypeContext);

		}

	}

	@Override
	// Below code is commented as part of performance fix
	// @Cacheable(cacheName = CROSSSELLCAROUSELCACHE, keyGenerator = @KeyGenerator(name =
	// GENERIC_KEY_GENERATOR))
	public UpSellCarouselWrapper getUpsellHolidayCarousels(final String componentUid, final String locationCode,
			final String productCode, final String pageType, final String seoPageType, final String acommodationTypeContext)
			throws NoSuchComponentException
	{
		final UpsellToHolidayComponentModel crossSellCarouselComponentModel = (UpsellToHolidayComponentModel) getComponent(componentUid);
		if (StringUtils.isNotBlank(productCode))
		{
			// This condition is added since -In case of Attraction cross sell
			// --
			// the attraction code value comes in productCode ,
			// due to which the call to product service throws an exception as
			// attraction is not a product , its temporal item

			String accomType = null;
			final ProductModel productModel = productService.getProductForCode(cmsSiteService.getCurrentCatalogVersion(),
					productCode);
			if (productModel instanceof AccommodationModel)
			{
				accomType = ((AccommodationModel) productModel).getType().toString();
			}
			return carouselFacade.getUpSellCarouselsData(null, productCode, pageType, seoPageType, crossSellCarouselComponentModel,
					accomType);

		}
		else
		{
			return carouselFacade.getUpSellCarouselsData(locationCode, null, pageType, seoPageType, crossSellCarouselComponentModel,
					acommodationTypeContext);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getVillaAvailability(java .lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "villaAvailabilityCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public VillaAvailabilityResponse getVillaAvailability(final String componentUid,
			final VillaAvailabilityRequest villaAvailabilityRequest) throws NoSuchComponentException
	{

		final VillaAvailabilityComponentModel villaAvailabilityComponentModel = (VillaAvailabilityComponentModel) getComponent(componentUid);

		if (StringUtils.isNotBlank(villaAvailabilityRequest.getCode()))
		{
			return searchFacade.getVillaAvailability(villaAvailabilityComponentModel, villaAvailabilityRequest);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getCrossSellCarousels(java .lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CROSSSELLCAROUSELCACHE, keyGenerator = @KeyGenerator(name = CROSS_SELL_KEY_GENERATOR))
	public CrossSellWrapper getCrossSellCarousels(final String componentUid, final String locationCode, final String productCode,
			final String pageType, final String seoPageType) throws NoSuchComponentException
	{

		final CrossSellCarouselComponentModel crossSellCarouselComponentModel = (CrossSellCarouselComponentModel) getComponent(componentUid);

		if (StringUtils.isNotBlank(productCode))
		{
			return carouselFacade.getCrossSellCarouselsData(null, productCode, pageType, seoPageType,
					crossSellCarouselComponentModel);
		}
		else
		{
			return carouselFacade.getCrossSellCarouselsData(locationCode, null, pageType, seoPageType,
					crossSellCarouselComponentModel);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getABTestData(java.lang. String)
	 */
	@Override
	public ABTestViewData getABTestData(final String componentUid)
	{
		return abTestFacade.getABTestData(componentUid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getABTestData(java.lang. String, java.lang.String)
	 */
	@Override
	public ABTestViewData getABTestData(final String componentUid, final String variantCode)
	{
		return abTestFacade.getABTestData(componentUid, variantCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getVariantForNewUser()
	 */
	@Override
	public String getVariantForNewUser(final String testCOde)
	{
		return abTestFacade.getVariantForNewUser(testCOde);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getVariant(java.lang.String, java.util.Set)
	 */
	@Override
	public ABTestViewData getVariant(final String componentUid, final Set<String> testNames)
	{
		return abTestFacade.getVariant(componentUid, testNames);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getThingsToDoAndSee(java .lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "thingsToDoAndSeeCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public LocationData getThingsToDoAndSeeEditorialData(final String componentUid, final String locationCode)
			throws NoSuchComponentException
	{
		return locationFacade.getThingsToDoAndSeeEditorial(locationCode);

	}

	/*
	 * This Methods get the feature codes for the component populates the view data
	 */
	@Override
	public LocationData getThingsToSeeAndDoEditorialData(final String componentUid, final String locationCode)
			throws NoSuchComponentException
	{
		final LocationData data = locationFacade.getThingsToSeeAndDoEditorial(locationCode);
		final List<ThingsToSeeAndDoEditorialData> tabData = new ArrayList<ThingsToSeeAndDoEditorialData>();
		thingToSeeAndDoDataPopulator.populate(data, tabData);
		data.setThingsToSeeAndDoData(tabData);
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.LocationFacade#getCarouselData(java.lang .String)
	 */
	@Override
	@Cacheable(cacheName = "accomodationCarouselDataCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public LocationData getAccommodationCarouselData(final String locationCode, final String pageType, final String seoType,
			final String componentUid)
	{
		final List<AccommodationCarouselViewData> accmList = new ArrayList<AccommodationCarouselViewData>();
		final List<AccommodationCarouselViewData> hotelList = new ArrayList<AccommodationCarouselViewData>();
		final List<AccommodationCarouselViewData> villaList = new ArrayList<AccommodationCarouselViewData>();

		final LocationData locationViewData = new LocationData();
		TabbedAccommodationCarouselComponentModel tabbedAccommodationCarouselModel = null;
		try
		{
			tabbedAccommodationCarouselModel = (TabbedAccommodationCarouselComponentModel) getComponent(componentUid);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error(NO_COMPONENT_FOUND + componentUid + ".Error caused while getting accommodation carousel data ", e);
		}
		final List<AccommodationCarouselModel> accommodationCarousels = tabbedAccommodationCarouselModel
				.getAccommodationCarousels();

		for (int x = 0; x < accommodationCarousels.size(); x++)
		{

			final AccommodationCarouselModel carouselModel = accommodationCarousels.get(x);

			final List<AccommodationViewData> accommodationViewDatas = carouselFacade.getAccommodationViewData(carouselModel,
					locationCode, pageType, seoType, 0, carouselModel.getVisibleItems().intValue());
			final List<AccommodationViewData> hotelViewDatas = new ArrayList<AccommodationViewData>();
			final List<AccommodationViewData> villaViewDatas = new ArrayList<AccommodationViewData>();

			for (final AccommodationViewData accommodationViewData : accommodationViewDatas)
			{
				final boolean accomTypeEquality = VILLA.equalsIgnoreCase(accommodationViewData.getAccommodationType());
				if (accomTypeEquality && !villaViewDatas.contains(accommodationViewData))
				{
					villaViewDatas.add(accommodationViewData);
				}
				else if (!accomTypeEquality && !hotelViewDatas.contains(accommodationViewData))
				{
					hotelViewDatas.add(accommodationViewData);
				}
			}

			accmList.add(new AccommodationCarouselViewData(carouselModel.getTitle(), carouselModel.getUid(), accommodationViewDatas,
					carouselModel.getLookupType().getCode(), carouselModel.getVisibleItems()));
			if (CollectionUtils.isNotEmpty(hotelViewDatas))
			{
				hotelList.add(new AccommodationCarouselViewData(carouselModel.getTitle(), carouselModel.getUid(), hotelViewDatas,
						carouselModel.getLookupType().getCode(), carouselModel.getVisibleItems()));
			}
			if (CollectionUtils.isNotEmpty(villaViewDatas))
			{
				villaList.add(new AccommodationCarouselViewData(carouselModel.getTitle(), carouselModel.getUid(), villaViewDatas,
						carouselModel.getLookupType().getCode(), carouselModel.getVisibleItems()));
			}
		}

		locationViewData.setCarouselViewData(accmList);

		if (CollectionUtils.isNotEmpty(hotelList) && CollectionUtils.isNotEmpty(hotelList.get(0).getAccomodationDatas()))
		{
			locationViewData.setHotelCarouselViewData(hotelList);
		}
		if (CollectionUtils.isNotEmpty(villaList) && CollectionUtils.isNotEmpty(villaList.get(0).getAccomodationDatas()))
		{
			locationViewData.setVillaCarouselViewData(villaList);
		}

		final LocationData currentLocation = locationFacade.getLocationData(locationCode);
		locationViewData.setName(currentLocation.getName());
		return locationViewData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getDealsData(java.lang.String , java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "latestDealsCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public List<AccommodationViewData> getDealsData(final String code, final String productCode, final String pageType,
			final String seoPageType, final String componentUid) throws NoSuchComponentException
	{
		String locationCode = code;
		final DealsComponentModel item = (DealsComponentModel) getComponent(componentUid);
		// If the component is requested by a product page, the location's code
		// associated to the product will be used
		// instead of the product code
		if (locationCode == null)
		{
			final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
			final List<String> brandPks = brandDetails.getRelevantBrands();
			final LocationModel location = locationService.getLocationForItem(
					attractionService.getAttractionForCode(productCode, cmsSiteService.getCurrentCatalogVersion()), brandPks);
			locationCode = location.getCode();
		}
		return searchFacade.getDealsData(locationCode, pageType, seoPageType, item);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getLocationHighlights(java .lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "locationHighlightsCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public LocationData getLocationHighlights(final String componentUid, final String locationCode)
	{
		LocationData locationData = new LocationData();
		try
		{
			final HighlightsComponentModel highlightsComponentModel = (HighlightsComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			locationData = locationFacade.getLocationHighlights(locationCode, highlightsComponentModel.getHighlightsNumber());
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(LOG_MSG + componentUid + "]", e);
		}
		return locationData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getLocationHighlights(java .lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "locationAtAGlanceCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public LocationData getLocationAtAGlance(final String componentUid, final String locationCode)
	{
		LocationData locationData = new LocationData();
		try
		{
			final TouristBoardBannerComponentModel componentModel = (TouristBoardBannerComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			locationData = locationFacade.getLocationHighlights(locationCode, componentModel.getAtAGlanceNumber());
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(LOG_MSG + componentUid + "]", e);
		}
		return locationData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getAccommodationHighlights (java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "accomHilightsCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public AccommodationViewData getAccommodationHighlights(final String componentUid, final String accommodationCode)
	{
		AccommodationViewData accommodationViewData = new AccommodationViewData();
		try
		{
			final HighlightsComponentModel highlightsComponentModel = (HighlightsComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			accommodationViewData = accomodationFacade.getAccommodationHighlights(accommodationCode,
					highlightsComponentModel.getHighlightsNumber());
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(LOG_MSG + componentUid + "]", e);
		}

		return accommodationViewData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getProductRangeHighlights (java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "productHilightsCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public ProductRangeViewData getProductRangeHighlights(final String componentUid, final String productRangeCode)
	{
		ProductRangeViewData productRangeViewData = new ProductRangeViewData();
		try
		{
			final HighlightsComponentModel highlightsComponentModel = (HighlightsComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			productRangeViewData = productRangeFacade.getProductRangeHighlights(productRangeCode,
					highlightsComponentModel.getHighlightsNumber());
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(LOG_MSG + componentUid + "]", e);
		}
		return productRangeViewData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getHeroCarouselViewData( java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "heroCarouselViewDataCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public HasFeatures getHeroCarouselViewData(final String componentUid, final String code, final String type)
	{
		try
		{
			final HeroCarouselComponentModel componentModel = (HeroCarouselComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			final MediaFinder finder = mediaFinders.get(MEDIAFINDER + componentModel.getLookupType());

			if (finder == null)
			{
				LOG.error(NO_MEDIA_FOUNDER_FOR_LOOKUP_TYPE + componentModel.getLookupType()
						+ CHECK_LOOKUP_TYPE_FIELD_OR_CREATE_FINDER_FOR_LOOKUP_TYPE);
			}

			final SearchRequestData searchRequest = new SearchRequestData();
			searchRequest.setPageSize(componentModel.getVisibleItems().intValue());
			searchRequest.setOffset(0);
			searchRequest.setRelevantItem(componentModel);
			HasFeatures heroData = null;

			// The method implemented from the Media finder interface varies
			// amongst the finders
			// Here a check is done to know what exact finder has been called so
			// a decision can be made on what method
			// to pass the data to.
			// The return type for MANUAL and ATTRACTION is a list of
			// MediaModels and they use the same populator to
			// convert that data into
			// a MediaViewData while the return type of AUTOMATIC finder is a
			// list of MediaContainerModels and a
			// different populator is used
			// to covert the data into a MediaViewData also "type" parameter is
			// passed in this is used to know what
			// exact service should be called in the finder, either a location
			// or an accommodation

			if (CarouselLookupType.AUTOMATIC.equals(componentModel.getLookupType()))
			{
				heroData = finder.searchAutomatic(searchRequest, code, type);
				return heroData;
			}
			else if (CarouselLookupType.MANUAL.equals(componentModel.getLookupType()))
			{
				if (finder != null)
				{
					heroData = finder.searchManual(searchRequest, code, type);
				}
				return heroData;
			}
			// This mode to implement later

			return null;

		}
		catch (final CMSItemNotFoundException e)
		{

			LOG.error(NO_COMPONENT_FOUND_WITH_ID + componentUid, e);
		}
		return null;
	}

	@Override
	public HasFeatures getConfPageHeroCarouselViewData(final String componentUid, final String code, final String type)
	{
		try
		{
			final ConfPageHeroCarouselComponentModel componentModel = (ConfPageHeroCarouselComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			final MediaFinder finder = mediaFinders.get("mediaFinder_" + componentModel.getLookupType());

			if (finder == null)
			{
				LOG.error("No mediaFinder for lookup type " + componentModel.getLookupType()
						+ " found. Check the lookupType field or create a Finder for the lookupType.");
			}

			final SearchRequestData searchRequest = new SearchRequestData();
			searchRequest.setPageSize(componentModel.getVisibleItems().intValue());
			searchRequest.setOffset(0);
			searchRequest.setRelevantItem(componentModel);

			HasFeatures heroData = null;

			// The method implemented from the Media finder interface varies
			// amongst the finders
			// Here a check is done to know what exact finder has been called so
			// a decision can be made on what method
			// to pass the data to.
			// The return type for MANUAL and ATTRACTION is a list of
			// MediaModels and they use the same populator to
			// convert that data into
			// a MediaViewData while the return type of AUTOMATIC finder is a
			// list of MediaContainerModels and a
			// different populator is used
			// to covert the data into a MediaViewData also "type" parameter is
			// passed in this is used to know what
			// exact service should be called in the finder, either a location
			// or an accommodation

			if (CarouselLookupType.AUTOMATIC.equals(componentModel.getLookupType()))
			{
				heroData = finder.searchAutomatic(searchRequest, code, type);
				return heroData;
			}
			else if (CarouselLookupType.MANUAL.equals(componentModel.getLookupType()))
			{
				heroData = finder.searchManual(searchRequest, code, type);
				return heroData;
			}
			// This mode to implement later

			return null;

		}
		catch (final CMSItemNotFoundException e)
		{

			LOG.error("No component found with id: " + componentUid, e);
		}
		return null;
	}

	@Override
	@Cacheable(cacheName = "searchResultsMediaCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public HasFeatures getMediaSearchResultsViewData(final String lookuptype, final String code, final String type)
	{
		try
		{
			final MediaFinder finder = mediaFinders.get("mediaFinder_AUTOMATIC");

			if (finder == null)
			{
				LOG.error(NO_MEDIA_FOUNDER_FOR_LOOKUP_TYPE + "mediaFinder_AUTOMATIC"
						+ CHECK_LOOKUP_TYPE_FIELD_OR_CREATE_FINDER_FOR_LOOKUP_TYPE);
			}

			final SearchRequestData searchRequest = new SearchRequestData();
			searchRequest.setPageSize(FORTY);
			searchRequest.setOffset(0);

			HasFeatures heroData = null;

			// The method implemented from the Media finder interface varies
			// amongst the finders
			// Here a check is done to know what exact finder has been called so
			// a decision can be made on what method
			// to pass the data to.
			// The return type for MANUAL and ATTRACTION is a list of
			// MediaModels and they use the same populator to
			// convert that data into
			// a MediaViewData while the return type of AUTOMATIC finder is a
			// list of MediaContainerModels and a
			// different populator is used
			// to covert the data into a MediaViewData also "type" parameter is
			// passed in this is used to know what
			// exact service should be called in the finder, either a location
			// or an accommodation
			if (finder != null)
			{
				heroData = finder.searchAutomatic(searchRequest, code, type);
			}
			return heroData;
			// This mode to implement later

		}
		catch (final ComponentNotFoundException e)
		{
			LOG.error(NO_COMPONENT_FOUND_WITH_ID, e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getHeroCarouselViewData( java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "attractionsHeroCarouselCache")
	public HasFeatures getHeroCarouselViewDataForAttractionLightBox(final String componentUid, final String code, final String type)
	{
		try
		{
			final AttractionLightBoxComponentModel componentModel = (AttractionLightBoxComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			final MediaFinder finder = mediaFinders.get(MEDIAFINDER + componentModel.getLookupType());

			if (finder == null)
			{
				throw new TUISystemException(NO_MEDIA_FOUNDER_FOR_LOOKUP_TYPE + componentModel.getLookupType()
						+ CHECK_LOOKUP_TYPE_FIELD_OR_CREATE_FINDER_FOR_LOOKUP_TYPE);
			}

			final SearchRequestData searchRequest = new SearchRequestData();
			searchRequest.setPageSize(componentModel.getVisibleItems().intValue());
			searchRequest.setOffset(0);
			searchRequest.setRelevantItem(componentModel);

			HasFeatures heroData = null;

			// The method implemented from the Media finder interface varies
			// amongst the finders
			// Here a check is done to know what exact finder has been called so
			// a decision can be made on what method
			// to pass the data to.
			// The return type for MANUAL and ATTRACTION is a list of
			// MediaModels and they use the same populator to
			// convert that data into
			// a MediaViewData while the return type of AUTOMATIC finder is a
			// list of MediaContainerModels and a
			// different populator is used
			// to covert the data into a MediaViewData also "type" parameter is
			// passed in this is used to know what
			// exact service should be called in the finder, either a location
			// or an accommodation

			if (CarouselLookupType.AUTOMATIC.equals(componentModel.getLookupType()))
			{
				heroData = finder.searchAutomatic(searchRequest, code, type);
				return heroData;
			}
			else if (CarouselLookupType.MANUAL.equals(componentModel.getLookupType()))
			{
				heroData = finder.searchManual(searchRequest, code, type);
				return heroData;
			}
			// This mode to implement later

			return null;

		}
		catch (final CMSItemNotFoundException e)
		{

			LOG.error(NO_COMPONENT_FOUND_WITH_ID + componentUid, e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getHeroCarouselViewData( java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "imageCarouselCache")
	public HasFeatures getImageCarouselViewData(final String componentUid, final String code, final String type)
	{
		try
		{
			final TouristBoardBannerComponentModel componentModel = (TouristBoardBannerComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			final MediaFinder finder = mediaFinders.get(MEDIAFINDER + componentModel.getLookupType());

			if (finder == null)
			{
				throw new TUISystemException(NO_MEDIA_FOUNDER_FOR_LOOKUP_TYPE + componentModel.getLookupType()
						+ CHECK_LOOKUP_TYPE_FIELD_OR_CREATE_FINDER_FOR_LOOKUP_TYPE);
			}

			final SearchRequestData searchRequest = new SearchRequestData();
			searchRequest.setPageSize(componentModel.getVisibleItems().intValue());
			searchRequest.setOffset(0);
			searchRequest.setRelevantItem(componentModel);
			HasFeatures imageData = null;

			// The method implemented from the Media finder interface varies
			// amongst the finders
			// Here a check is done to know what exact finder has been called so
			// a decision can be made on what method
			// to pass the data to.
			// The return type for MANUAL and ATTRACTION is a list of
			// MediaModels and they use the same populator to
			// convert that data into
			// a MediaViewData while the return type of AUTOMATIC finder is a
			// list of MediaContainerModels and a
			// different populator is used
			// to covert the data into a MediaViewData also "type" parameter is
			// passed in this is used to know what
			// exact service should be called in the finder, either a location
			// or an accommodation

			if (CarouselLookupType.AUTOMATIC.equals(componentModel.getLookupType()))
			{
				imageData = finder.searchAutomatic(searchRequest, code, type);
				return imageData;
			}
			else if (CarouselLookupType.MANUAL.equals(componentModel.getLookupType()))
			{
				imageData = finder.searchManual(searchRequest, code, type);
				return imageData;
			}
			// This mode to implement later

			return null;

		}
		catch (final CMSItemNotFoundException e)
		{

			LOG.error(NO_COMPONENT_FOUND_WITH_ID + componentUid, e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getProductCrossSellCarouselData (java.lang.String,
	 * java.lang.String)
	 */
	@Override
	// Below code is commented as part of performance fix
	// @Cacheable(cacheName = CROSSSELLCAROUSELCACHE, keyGenerator = @KeyGenerator(name =
	// GENERIC_KEY_GENERATOR))
	public ProductCrossSellWrapper getProductCrossSellCarouselData(final String componentUid, final String pageType,
			final String seoType, final String locationCode, final String productCode) throws NoSuchComponentException
	{
		final ProductCrossSellCarouselComponentModel productCrossSellCarouselModel = (ProductCrossSellCarouselComponentModel) getComponent(componentUid);
		return carouselFacade.getProductCrossSellCarouselData(locationCode, pageType, seoType, productCrossSellCarouselModel,
				productCode);
	}

	@Override
	// Below code is commented as part of performance fix
	// @Cacheable(cacheName = CROSSSELLCAROUSELCACHE, keyGenerator = @KeyGenerator(name =
	// GENERIC_KEY_GENERATOR))
	public ProductCrossSellWrapper getProductCrossSellCarouselData(final String componentUid, final String pageType,
			final String seoType, final String locationCode, final String productCode,
			final List<PlacesToStayCarouselModel> placesToStayCarousels) throws NoSuchComponentException
	{

		if (CollectionUtils.isNotEmpty(placesToStayCarousels))
		{

			return carouselFacade.getProductCrossSellCarouselHolidaysData(locationCode, pageType, seoType,
					placesToStayCarousels.get(0), productCode);
		}

		return new ProductCrossSellWrapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getQuickSearchResultData (java.lang.String, java.lang.String)
	 */
	@Override
	public DestinationQuickSearchDataWrapper getQuickSearchResultData(final String searchParameter)
			throws NoSuchComponentException
	{
		if (StringUtils.isBlank(searchParameter))
		{
			return null;
		}
		return searchFacade.getQuickSearchResultData(searchParameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getTopPlacesToStayData(java .lang.String,
	 * uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel )
	 */
	@Override
	@Cacheable(cacheName = "topXPlacesCache", keyGenerator = @KeyGenerator(name = KEY_GENERATOR))
	public TopPlacesWrapper getTopPlacesToStayData(final String locationCode, final String pageType, final String seoPageType,
			final TopPlacesToStayCarouselComponentModel component)
	{

		return carouselFacade.getTopPlacesToStayData(component, locationCode, pageType, seoPageType, 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getTopPlacesToStayData(java .lang.String,
	 * uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel )
	 */
	@Override
	@Cacheable(cacheName = "topXPlacesCache", keyGenerator = @KeyGenerator(name = KEY_GENERATOR))
	public TopPlacesWrapper getTopPlacesForDestQS(final String locationCode, final String pageType, final String seoPageType,
			final DestinationQSAndTopPlacesComponentModel component)
	{

		return carouselFacade.getTopPlacesForDestQS(component, locationCode, pageType, seoPageType, 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getPlacesToStayCarouselData (java.lang.String,
	 * uk.co.portaltech.tui.components.model.PlacesToStayCarouselComponentModel)
	 */
	@Override
	@Cacheable(cacheName = PLACESTOSTAYCAROUSELCACHE, keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public PlacesToStayWrapper getPlacesToStayCarouselData(final String locationCode, final String pageType, final String seoType,
			final String componentId)
	{

		PlacesToStayCarouselComponentModel component;
		try
		{
			if (!LocationType.CONTINENT.equals(((LocationModel) categoryService.getCategoryForCode(locationCode)).getType()))
			{
				component = (PlacesToStayCarouselComponentModel) getComponent(componentId);

				return getPlacestoStayWrapper(locationCode, pageType, seoType, component.getPlacesToStayCarousels());
			}
		}
		catch (final NoSuchComponentException e1)
		{
			LOG.error(NO_COMPONENT_FOUND + componentId + ".Error caused while getting placestostay carousel data ", e1);
		}
		return new PlacesToStayWrapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getPlacesToStayCarouselData (java.lang.String,
	 * uk.co.portaltech.tui.components.model.PlacesToStayCarouselComponentModel)
	 */
	@Override
	@Cacheable(cacheName = PLACESTOSTAYCAROUSELCACHE, keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public PlacesToStayWrapper getDayTripPlacesToStayCarouselData(final String locationCode, final String pageType,
			final String seoType, final String componentId)
	{

		try
		{
			final DaytripsPromoComponentModel component = (DaytripsPromoComponentModel) getComponent(componentId);

			return getPlacestoStayWrapper(locationCode, pageType, seoType, component.getPlacesToStayCarousels());
		}
		catch (final NoSuchComponentException e1)
		{
			LOG.error(NO_COMPONENT_FOUND + componentId + ".Error caused while getting placestostay carousel data ", e1);
		}
		return new PlacesToStayWrapper();
	}

	/**
	 * @param locationCode
	 * @param pageType
	 * @param seoType
	 * @return PlacesToStayWrapper
	 */
	private PlacesToStayWrapper getPlacestoStayWrapper(final String locationCode, final String pageType, final String seoType,
			final List<PlacesToStayCarouselModel> placesToStayCarousels)
	{
		PlacesToStayWrapper placesToStayWrapper = new PlacesToStayWrapper();
		List<LocationData> placesToStayCarouselData = null;
		final List<LocationData> locationPlace = new ArrayList<LocationData>();
		final BrandDetails brandDetails = sessionService.getAttribute("brandDetails");
		final List<LocationData> hotelLocations = new ArrayList<LocationData>();
		final List<LocationData> villaLocations = new ArrayList<LocationData>();
		if (!StringUtils.equalsIgnoreCase(brandDetails.getSiteUid(), BrandType.CR.toString()))
		{

			for (final PlacesToStayCarouselModel carouselModel : placesToStayCarousels)
			{
				placesToStayCarouselData = searchFacade.getPlacesToStayCarouselData(locationCode, pageType, seoType, carouselModel);
				placesToStayWrapper.setLocations(placesToStayCarouselData);
				placesToStayWrapper = splitLocationsInTab(placesToStayCarouselData, placesToStayWrapper, hotelLocations,
						villaLocations);
			}

		}
		else
		{
			for (final PlacesToStayCarouselModel carouselModel : placesToStayCarousels)
			{
				placesToStayCarouselData = searchFacade.getPlacesToStayCarouselDataCruise(locationCode, pageType, seoType,
						carouselModel);
				placesToStayWrapper.setLocations(placesToStayCarouselData);
				placesToStayWrapper = splitLocationsInTab(placesToStayCarouselData, placesToStayWrapper, hotelLocations,
						villaLocations);
			}
			locationBrandVerification(placesToStayCarouselData, locationPlace);
			getSubLocationsOfnonCruiseLocation(placesToStayCarouselData, locationPlace);

		}
		final LocationData currentLocation = locationFacade.getLocationData(locationCode);
		placesToStayWrapper.setTopLocationName(currentLocation.getName());

		return placesToStayWrapper;
	}

	/**
	 * @param placesToStayCarouselData
	 * @param locationPlace
	 */
	private void getSubLocationsOfnonCruiseLocation(final List<LocationData> placesToStayCarouselData,
			final List<LocationData> locationPlace)
	{
		if (CollectionUtils.isNotEmpty(locationPlace))
		{
			placesToStayCarouselData.removeAll(locationPlace);
			for (final LocationData location : locationPlace)
			{

				cruiseBrandVerification(placesToStayCarouselData, location);

			}
		}
	}

	/**
	 * @param placesToStayCarouselData
	 * @param location
	 */
	private void cruiseBrandVerification(final List<LocationData> placesToStayCarouselData, final LocationData location)
	{
		for (final LocationData loc : location.getSubLocations())
		{
			if (BrandUtils.isValidBrandCode(categoryService.getCategoryForCode(loc.getCode()).getBrands(), BrandType.CR.toString()))
			{
				placesToStayCarouselData.add(loc);
			}
			break;
		}
	}

	/**
	 * @param placesToStayCarouselData
	 * @param locationPlace
	 */
	private void locationBrandVerification(final List<LocationData> placesToStayCarouselData,
			final List<LocationData> locationPlace)
	{
		for (final LocationData locationData : placesToStayCarouselData)
		{
			if (!BrandUtils.isValidBrandCode(categoryService.getCategoryForCode(locationData.getCode()).getBrands(),
					BrandType.CR.toString()))
			{
				locationPlace.add(locationData);
			}
		}
	}

	/**
	 * @param placesToStayCarouselData
	 * @param placesToStayWrapper
	 * @return placesToStayWrapper
	 * @description This method split places to stay location according to the availability of villa and hotel.
	 *
	 */
	private PlacesToStayWrapper splitLocationsInTab(final List<LocationData> placesToStayCarouselData,
			final PlacesToStayWrapper placesToStayWrapper, final List<LocationData> hotelLocations,
			final List<LocationData> villaLocations)
	{

		for (final LocationData location : placesToStayCarouselData)
		{

			if (CollectionUtils.isNotEmpty(location.getLocationAccomTypes()))
			{

				if (location.getLocationAccomTypes().contains(VILLA) && !villaLocations.contains(location))
				{
					villaLocations.add(getFilteredLocationDatas(VILLA, location));
				}
				// the below if is written for a purpose as we need to show pill
				// for
				// hotels and villas
				if (location.getLocationAccomTypes().contains(HOTEL) && !hotelLocations.contains(location))
				{
					hotelLocations.add(getFilteredLocationDatas(HOTEL, location));
				}
			}
			else
			{
				if (!villaLocations.contains(location))
				{
					villaLocations.add(getFilteredLocationDatas(VILLA, location));
				}
				// the below if is written for a purpose as we need to show pill
				// for
				// hotels and villas
				if (!hotelLocations.contains(location))
				{
					hotelLocations.add(getFilteredLocationDatas(HOTEL, location));
				}
			}
		}
		placesToStayWrapper.setHotelLocations(hotelLocations);
		placesToStayWrapper.setVillaLocations(villaLocations);

		return placesToStayWrapper;
	}

	/**
	 * @param type
	 * @param locationData
	 * @return locationData
	 * @description This method checks the sublocation and accommodations inside the location and then filter them
	 *              according to the type supplied.
	 */

	private LocationData getFilteredLocationDatas(final String type, final LocationData locationData)
	{
		final LocationData filteredLocationData = cloneLocationData(locationData);
		if (locationData.getAccommodations() != null && !locationData.getAccommodations().isEmpty())
		{
			final List<AccommodationViewData> accommodationViewDatas = locationData.getAccommodations();
			final List<AccommodationViewData> filteredAcmViewDatas = new ArrayList<AccommodationViewData>();
			filteredAcmViewDatas.addAll(filter(accommodationViewDatas, new Predicate<AccommodationViewData>()
			{
				@Override
				public boolean apply(final AccommodationViewData viewData)
				{

					if (viewData.getAccommodationType().equalsIgnoreCase(type))
					{
						return true;
					}
					return false;
				}
			}));
			filteredLocationData.setAccommodations(filteredAcmViewDatas);
		}
		if (locationData.getSubLocations() != null && !locationData.getSubLocations().isEmpty())
		{
			final List<LocationData> subLocationDatas = locationData.getSubLocations();
			final List<LocationData> subLocations = new ArrayList<LocationData>();
			subLocations.addAll(filter(subLocationDatas, new Predicate<LocationData>()
			{
				@Override
				public boolean apply(final LocationData subData)
				{
					final List<String> accomTypeList = locationService.getLocationAccomTypes(subData.getCode());
					if (accomTypeList.contains(type))
					{
						return true;
					}
					return false;
				}
			}));

			filteredLocationData.setSubLocations(subLocations);
		}
		return filteredLocationData;
	}

	/**
	 * @param locationData
	 * @return cloned LocationData
	 */
	private LocationData cloneLocationData(final LocationData locationData)
	{
		LocationData filteredLocationData = new LocationData();
		try
		{
			filteredLocationData = (LocationData) locationData.clone();
		}
		catch (final CloneNotSupportedException e)
		{
			LOG.error("Clone not supported exception", e);
		}
		return filteredLocationData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getAccommodationPrice(java .lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "browsePriceDataCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public AccommodationViewData getAccommodationPrice(final String componentUid, final String accommodationCode)
	{
		// Performance Fix Done by creating AccommodationViewData with required attributes
		final AccommodationViewData requiredAccommodationViewData = new AccommodationViewData();

		try
		{
			final AccommodationViewData accommodationViewData = searchFacade.getAccommodationPriceData(accommodationCode,
					(BookingComponentModel) getComponent(componentUid));
			requiredAccommodationViewData.setPriceFrom(accommodationViewData.getPriceFrom());
			requiredAccommodationViewData.setAvailableFrom(accommodationViewData.getAvailableFrom());
			requiredAccommodationViewData.setStayPeriod(accommodationViewData.getStayPeriod());
			requiredAccommodationViewData.setDeparturePoint(accommodationViewData.getDeparturePoint());
			requiredAccommodationViewData.setRoomOccupancy(accommodationViewData.getRoomOccupancy());
			return requiredAccommodationViewData;

		}
		catch (final NoSuchComponentException e)
		{
			LOG.error(LOG_MSG + componentUid + "]", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade# getAccommodationResultAndFilterData(java.lang.String)
	 */
	@Override
	public AccommodationFilterDataWrapper getAccommodationResultAndFilterData(final String componentUid, final String pageType,
			final String seoType, final String categoryCode, final List<String> facetOptionNames, final List<String> filterParams)
			throws NoSuchComponentException
	{
		final AccommodationsResultComponentModel item = (AccommodationsResultComponentModel) getComponent(componentUid);
		return searchFacade.getAccommodationResultAndFilterData(categoryCode, pageType, seoType, facetOptionNames, filterParams,
				item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getInteractiveMapData(java .lang.String, java.lang.String,
	 * java.lang.String, uk.co.portaltech.tui.components.model.InteractiveMapComponentModel)
	 */
	@Override
	@Cacheable(cacheName = "interactiveMapDataCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public MapDataWrapper getInteractiveMapData(final String categoryCode, final String pageType, final String seoPageType,
			final String componentUid) throws NoSuchComponentException
	{
		final InteractiveMapComponentModel item = (InteractiveMapComponentModel) getComponent(componentUid);
		return searchFacade.getInteractiveMapData(categoryCode, pageType, seoPageType, item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getMediaCarouselData(java .lang.String, int, java.lang.Integer)
	 */
	@Override
	public MediaPromoViewData getMediaCarouselData(final String componentUid, final int index, final Integer size)
			throws NoSuchComponentException
	{
		Integer pageSize = size;
		final MediaPromoComponentModel heroComponentModel = (MediaPromoComponentModel) getComponent(componentUid);

		if (pageSize == null)
		{
			pageSize = heroComponentModel.getVisibleItems();
		}
		final List<MediaViewData> imageCarouselData = carouselFacade.getMediaCarouselMediaData(heroComponentModel, index,
				pageSize.intValue());

		return new MediaPromoViewData(componentUid, imageCarouselData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getMediaCarouselViewData (java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public HasFeatures getMediaCarouselViewData(final String componentUid, final String code, final String type)
	{
		try
		{
			final MediaPromoComponentModel componentModel = (MediaPromoComponentModel) cmsComponentService
					.getAbstractCMSComponent(componentUid);
			final MediaFinder finder = mediaFinders.get(MEDIAFINDER + componentModel.getLookupType());

			if (finder == null)
			{

				LOG.error(NO_MEDIA_FOUNDER_FOR_LOOKUP_TYPE + componentModel.getLookupType()
						+ CHECK_LOOKUP_TYPE_FIELD_OR_CREATE_FINDER_FOR_LOOKUP_TYPE);

			}

			final SearchRequestData searchRequest = new SearchRequestData();
			searchRequest.setPageSize(componentModel.getVisibleItems().intValue());
			searchRequest.setOffset(0);
			searchRequest.setRelevantItem(componentModel);
			HasFeatures mediaPromoData = null;

			// The method implemented from the Media finder interface varies
			// amongst the finders
			// Here a check is done to know what exact finder has been called so
			// a decision can be made on what method
			// to pass the data to.
			// The return type for MANUAL and ATTRACTION is a list of
			// MediaModels and they use the same populator to
			// convert that data into
			// a MediaViewData while the return type of AUTOMATIC finder is a
			// list of MediaContainerModels and a
			// different populator is used
			// to covert the data into a MediaViewData also "type" parameter is
			// passed in this is used to know what
			// exact service should be called in the finder, either a location
			// or an accommodation

			if (CarouselLookupType.AUTOMATIC.equals(componentModel.getLookupType()))
			{
				mediaPromoData = finder.searchAutomatic(searchRequest, code, type);
				return mediaPromoData;
			}
			else if (CarouselLookupType.MANUAL.equals(componentModel.getLookupType()))
			{
				mediaPromoData = finder.searchManual(searchRequest, code, type);
				return mediaPromoData;
			}

			return null;

		}
		catch (final CMSItemNotFoundException e)
		{

			LOG.error(NO_COMPONENT_FOUND_WITH_ID + componentUid, e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getQuickSearchResultData (java.lang.String, java.lang.String)
	 */

	@Override
	public HolidayViewData getHolidayPackagesResultData(final SearchResultsRequestData searchParameter, final String siteBrand)
			throws SearchResultsBusinessException

	{
		final HolidayViewData holidayViewData = new HolidayViewData();
		holidayViewData.setSearchRequest(cloneSearchCriteria(searchParameter));
		setShortList(siteBrand, holidayViewData);
		if (viewSelector.checkIsMobile())
		{
			enrichmentService.enrichForHolidayFinder(getHolidayFinderComponent(), searchParameter);
		}
		else
		{
			enrichmentService.enrich(getSearchPanelComponent(), searchParameter);
		}
		boolean aniteSwitch = false;

		final String aniteInvStartDate = Config.getString(siteBrand + CommonwebitemsConstants.DOT
				+ CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015");
		if (StringUtils.isNotBlank(searchParameter.getDepartureDate())
				&& DateUtils.isEqualOrAfter(searchParameter.getDepartureDate(), aniteInvStartDate))
		{
			aniteSwitch = true;
		}
		if (StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), PAGINATE)
				|| StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), SORT))
		{

			holidayViewData.setSearchResult(populateSearchResultsViewData(searchParameter, siteBrand));
			holidayViewData.getSearchResult().setAniteSwitch(aniteSwitch);
			holidayViewData.getSearchResult().setShortlistEnabled(
					Boolean.valueOf(tuiConfigService.getConfigValue(String.format(ENABLE_SHORTLIST_KEY, siteBrand))));
			return holidayViewData;
		}

		// single accommodation search
		if (searchParameter.isSingleAccomSearch())
		{
			if ("FJ".equalsIgnoreCase(siteBrand))
			{
				final String productCode = searchParameter.getUnits().get(0).getId();
				if (StringUtils.isNotEmpty(productCode))
				{
					final AccommodationModel accommodationModel = accommodationService.getAccomodationByCodeAndCatalogVersion(
							productCode, cmsSiteService.getCurrentCatalogVersion(), null);

					final String brand = brandUtils.getFeatureServiceBrand(accommodationModel.getBrands());
					final String brandType = "FJ".equalsIgnoreCase(brand) ? "E" : "D";
					searchParameter.setBrandType(brandType);

				}
			}
			// latest search criteria is added because if we do any room change and go to accom
			// details page we take latest criteria from session to get the holiday.
			sessionService.setAttribute(BACKTOSEARCHLATESTCRITERIA, searchParameter);

			// latest search criteria is added because if we do any room change
			// and go to accom
			// details page we take latest criteria from session to get the
			// holiday.
			sessionService.setAttribute(LATESTCRITERIA, searchParameter);
			holidayViewData.setSearchResult(getSingleAccomHolidayPackagesResultData(searchParameter, siteBrand));

			// This map is populated when wishList map is not in seesion and
			// holiday packages are returned from cache.
			populateWishListMap(holidayViewData);
			if (holidayViewData.getSearchResult() != null)
			{
				final List<SearchResultViewData> searchResultViewDatas = sortFlightByDate(holidayViewData.getSearchResult()
						.getHolidays());
				holidayViewData.getSearchResult().setHolidays(searchResultViewDatas);
			}
			holidayViewData.getSearchResult().setAniteSwitch(aniteSwitch);
			holidayViewData.getSearchResult().setShortlistEnabled(
					Boolean.valueOf(tuiConfigService.getConfigValue(String.format(ENABLE_SHORTLIST_KEY, siteBrand))));
			return holidayViewData;
		}

		if (isFollowOnSearch(searchParameter))
		{
			// latest search criteria is added because if we do any room change
			// and go to accom
			// details page we take latest criteria from session to get the
			// holiday.
			sessionService.setAttribute(LATESTCRITERIA, searchParameter);
			sessionService.setAttribute(BACKTOSEARCHLATESTCRITERIA, searchParameter);

			holidayViewData.setSearchResult(searchFacade.getHolidayPackagesResultDataForFacets(searchParameter, siteBrand));
			holidayViewData.getSearchResult().setAniteSwitch(aniteSwitch);
			return holidayViewData;
		}

		sessionService.setAttribute(LATESTCRITERIA, searchParameter);
		sessionService.setAttribute(BACKTOSEARCHLATESTCRITERIA, searchParameter);

		holidayViewData.setSearchResult(searchFacade.getHolidayPackagesResultData(searchParameter, siteBrand));

		// This map is populated when wishList map is not in seesion and holiday
		// packages are returned from cache.
		populateWishListMap(holidayViewData);
		holidayViewData.getSearchResult().setAniteSwitch(aniteSwitch);
		return holidayViewData;
	}

	/**
	 * @param siteBrand
	 * @param holidayViewData
	 */
	private void setShortList(final String siteBrand, final HolidayViewData holidayViewData)
	{
		if (!StringUtils.equalsIgnoreCase("FJ", siteBrand))
		{
			holidayViewData.setShortlistEnabled(Boolean.valueOf(tuiConfigService.getConfigValue(String.format(ENABLE_SHORTLIST_KEY,
					siteBrand))));
		}
	}

	/**
	 *
	 * @param searchResultViewDatas
	 * @return searchResultViewDatas
	 * @description This method sorts list of SearchResultViewData by Ascending Order Of Date.
	 */
	private List<SearchResultViewData> sortFlightByDate(final List<SearchResultViewData> searchResultViewDatas)
	{
		if (CollectionUtils.isNotEmpty(searchResultViewDatas))
		{
			Collections.sort(searchResultViewDatas,
					SearchResultViewDataComparator.getComparator(getSearchResultViewDataComparatorList("DEPARTURE_DATE_ASCENDING")));
		}
		return searchResultViewDatas;
	}

	/**
	 *
	 * @param comperatorCode
	 * @return comparators
	 */
	private List<SearchResultViewDataComparator> getSearchResultViewDataComparatorList(final String comperatorCode)
	{

		final List<SearchResultViewDataComparator> comparators = new ArrayList<SearchResultViewDataComparator>();
		comparators.add(SearchResultViewDataComparator.valueOf(comperatorCode));
		return comparators;
	}

	/**
	 * @param holidayViewData
	 */
	protected void populateWishListMap(final HolidayViewData holidayViewData)
	{
		if (sessionService.getAttribute("wishListMap") == null)
		{
			final Map<String, String> wishListMap = new HashMap<String, String>();

			for (final SearchResultViewData resultsview : holidayViewData.getSearchResult().getHolidays())
			{
				wishListMap.put(resultsview.getWishListId(), resultsview.getPackageId());
			}

			sessionService.setAttribute("wishListMap", wishListMap);
		}
	}

	private SearchResultsViewData populateSearchResultsViewData(final SearchResultsRequestData searchParameter,
			final String siteBrand) throws SearchResultsBusinessException
	{

		LOG.info("This search Criteria is ignored except for page no's and sort criteria. First: " + searchParameter.getFirst()
				+ " Offset: " + searchParameter.getOffset() + "Sort By: " + searchParameter.getSortBy());
		final SearchResultsViewData searchResultsViewData = new SearchResultsViewData();

		final EndecaSearchResult endecaSearchResults = sessionService.getAttribute(SEARCHRESULTS);

		// checking previous search is Main Search / Follow On Search
		if (checkIsFollowSearchResult(endecaSearchResults))
		{

			if (StringUtils.equalsIgnoreCase(SORT, searchParameter.getSearchRequestType()))
			{

				LOG.info("sorting is applied on raw results");
			}

			// paginate and populate the vew data.
			return searchFacade.populatePaginatedViewData(searchParameter, searchResultsViewData, endecaSearchResults, siteBrand);
		}
		else
		{
			SearchResultsRequestData searchParameterInSession = sessionService.getAttribute(LATESTCRITERIA);

			if (searchParameterInSession != null)
			{
				searchParameterInSession.setWhen(searchParameter.getWhen());
				searchParameterInSession.setUntil(searchParameter.getUntil());

				if (StringUtils.equalsIgnoreCase(PAGINATE, searchParameter.getSearchRequestType()))
				{
					// to update the new page
					searchParameterInSession.setFirst(searchParameter.getFirst());
					searchParameterInSession.setOffset(searchParameter.getOffset());
					// If its an initial search pagination...use the View cache route and If sort is not
					// happened on previous results

					// pagination on main search results with out sorting from View Cache
					return searchFacade.getHolidayPackagesResultData(searchParameterInSession, siteBrand);
				}
				else if (StringUtils.equalsIgnoreCase(SORT, searchParameter.getSearchRequestType()))
				{
					// to update the current search Criteria with any new sort
					// parameter
					searchParameterInSession.setSortBy(searchParameter.getSortBy());
					searchParameterInSession.setOffset(searchParameter.getOffset() * searchParameter.getFirst());
					// this is done if its a sort request post pagination request. it works for all other
					// scenarios as well.
					searchParameterInSession.setFirst(1);
				}
			}

			// temporary fix to handle the pagination for holiday context from mobile
			if (searchParameterInSession == null && viewSelector.checkIsMobile())
			{
				searchParameterInSession = searchParameter;
			}
			// getting results from Raw Cache / Endeca
			final EndecaSearchResult endecaSearchResult = searchFacade.getPackagesFromEndecaCache(searchParameterInSession);

			if (StringUtils.isNotBlank(endecaSearchResult.getTotalNumRecs()))
			{
				searchResultsViewData.setEndecaResultsCount(Integer.parseInt(endecaSearchResult.getTotalNumRecs()));
			}

			// paginate and populate the vew data.
			return searchFacade.populatePaginatedViewData(searchParameter, searchResultsViewData, endecaSearchResult, siteBrand);
		}
	}

	/**
	 * @param endecaSearchResults
	 * @return boolean
	 */
	protected boolean checkIsFollowSearchResult(final EndecaSearchResult endecaSearchResults)
	{
		return endecaSearchResults != null && StringUtils.isNotBlank(endecaSearchResults.getSearchRequestType())
				&& isFollowOnSearchResult(endecaSearchResults);
	}

	/**
	 * @param searchResults
	 * @return boolean
	 */

	private boolean isFollowOnSearchResult(final EndecaSearchResult searchResults)
	{

		return StringUtils.equalsIgnoreCase(searchResults.getSearchRequestType(), FILTER)
				|| StringUtils.equalsIgnoreCase(searchResults.getSearchRequestType(), DATESLIDER)
				|| StringUtils.equalsIgnoreCase(searchResults.getSearchRequestType(), DURATION)
				|| StringUtils.equalsIgnoreCase(searchResults.getSearchRequestType(), ROOMS);
	}

	/**
	 * @param searchParameter
	 *
	 * @return SearchResultsViewData
	 */
	protected SearchResultsViewData getSingleAccomHolidayPackagesResultData(final SearchResultsRequestData searchParameter,
			final String siteBrand) throws SearchResultsBusinessException
	{
		if (isFollowOnSearch(searchParameter))
		{
			// single accommodation facet search - Currently hitting endeca
			// but needs to be refactored. As per design the filtering has
			// to be done at hybris end.
			return searchFacade.getHolidayPackagesResultDataForSingleAccomFacets(searchParameter, siteBrand);
		}
		else
		{
			// single accommodation search uses endeca raw cahce
			return searchFacade.getHolidayPackagesResultDataForSingleAccom(searchParameter, siteBrand);
		}
	}

	/**
	 * @param searchParameter
	 *
	 * @return boolean
	 */
	protected boolean isFollowOnSearch(final SearchResultsRequestData searchParameter)
	{
		boolean followOnSearch = isFollowOnSearchOne(searchParameter);
		followOnSearch = followOnSearch || isFollowOnSearchTwo(searchParameter);
		return followOnSearch;
	}

	/**
	 * Returns first part of boolean checks on search request type for follow on search.
	 *
	 * @param searchParameter
	 *           search parameter
	 * @return boolean
	 */
	protected boolean isFollowOnSearchOne(final SearchResultsRequestData searchParameter)
	{
		return StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), FILTER)
				|| StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), DATESLIDER)
				|| StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), DURATION);
	}

	/**
	 * Returns second part of boolean checks on search request type for follow on search.
	 *
	 * @param searchParameter
	 *           search parameter
	 * @return boolean
	 */
	protected boolean isFollowOnSearchTwo(final SearchResultsRequestData searchParameter)
	{
		return StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), ROOMS)
				|| StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), INS);
	}

	/**
	 * @return SearchResultsRequestData
	 */
	protected SearchResultsRequestData cloneSearchCriteria(final SearchResultsRequestData searchParameter)
	{
		SearchResultsRequestData searchCriteriaClone = new SearchResultsRequestData();
		try
		{
			searchCriteriaClone = (SearchResultsRequestData) BeanUtils.cloneBean(searchParameter);
		}
		catch (final Exception e)
		{
			LOG.error("Unable to Clone Search Criteria", e);
		}
		return searchCriteriaClone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getSearchPanelComponennt()
	 */
	@Override
	public SearchPanelComponentModel getSearchPanelComponent() throws SearchResultsBusinessException
	{
		SearchPanelComponentModel component = null;
		try
		{
			component = (SearchPanelComponentModel) getComponent(SEARCH_PANELCOMPONENT_ID);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error(NO_SEARCH_PANEL_COMPONENT, e);
			throw new SearchResultsBusinessException("3001", e);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getSearchPanelComponennt()
	 */
	@Override
	public NewSearchPanelComponentModel getNewSearchPanelComponent() throws SearchResultsBusinessException
	{
		NewSearchPanelComponentModel component = null;
		try
		{
			component = (NewSearchPanelComponentModel) getComponent(NEW_SEARCH_PANELCOMPONENT_ID);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error(NO_SEARCH_PANEL_COMPONENT, e);
			throw new SearchResultsBusinessException("3001", e);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getVariantPercentageByCode (java.lang.String)
	 */
	@Override
	public int getVariantPercentageByCode(final String variantCode)
	{
		return abTestFacade.getVariantPercentageByCode(variantCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getSearchPanelComponennt()
	 */
	@Override
	public HolidayFinderComponentModel getHolidayFinderComponent() throws SearchResultsBusinessException
	{
		HolidayFinderComponentModel component = null;
		try
		{
			component = (HolidayFinderComponentModel) getComponent(HOLIDAY_FINDER_COMPONENT_ID);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No Holiday Finder component", e);
		}
		return component;
	}

	/**
	 * WidenSearchCriteriaData
	 */
	@Override
	public SearchResultsViewData datesOnlySearch(final WidenSearchCriteriaData widenSearchRequest)
	{

		try
		{
			return searchFacade.getDatesOnlySearchResult(widenSearchRequest.getSearchRequest());
		}
		catch (final SearchResultsBusinessException e)
		{
			LOG.error("Exception in dates Only Search for no results widening search", e);

		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getFlightAvailability()
	 */
	@Override
	public FlightAvailabilityViewDataWrapper getFlightAvailability(final String productCode, final String componentId)
			throws SearchResultsBusinessException
	{

		AvailableFlightsComponentModel availableFlightsComponentModel = null;
		try
		{
			availableFlightsComponentModel = (AvailableFlightsComponentModel) getComponent(componentId);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("component with id: " + componentId + " is not available", e);
		}
		final FlightAvailabilityViewDataWrapper wrapper = new FlightAvailabilityViewDataWrapper();
		wrapper.setFlightAvailabilityMap(searchFacade.getFlightAvailability(productCode, availableFlightsComponentModel));
		return wrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getSearchPanelData()
	 */
	@Override
	public SearchPanelViewData getSearchPanelData() throws SearchResultsBusinessException
	{

		SearchPanelComponentModel component = null;
		final SearchPanelViewData searchPaneldata = new SearchPanelViewData();
		try
		{
			component = (SearchPanelComponentModel) getComponent(SEARCH_PANELCOMPONENT_ID);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error(NO_SEARCH_PANEL_COMPONENT, e);
			throw new SearchResultsBusinessException("3001", e);
		}

		searchPanelDataPopulator.populate(component, searchPaneldata);

		return searchPaneldata;

	}

	// Commented as part of performance fix.
	// @Cacheable(cacheName = "accomStandardWrapperCache", keyGenerator = @KeyGenerator(name =
	// GENERIC_KEY_GENERATOR))
	@Override
	public AccommodationStandardWrapper getAccommodationStandard(final String componentUid, final String locationCode,
			final String pageType, final String seoPageType) throws NoSuchComponentException
	{

		final AccommodationStandardComponentModel accommodationStandardComponentModel = getComponent(componentUid);

		return accommodationStandardFacade.getAccommodationStandardData(locationCode, pageType, seoPageType,
				accommodationStandardComponentModel);
	}

	@Override
	public FeatureListViewData getFeatureListViewData(final String locationCode, final String noncoreHodidayType)
	{
		LocationModel locationModel = null;
		final FeatureListViewData viewData = new FeatureListViewData();

		if (StringUtils.isNotEmpty(locationCode))
		{
			try
			{
				locationModel = mainStreamTravelLocationService.getLocationModel(locationCode, LOCATION,
						tuiUtilityService.getSiteReleventBrandPks());
			}
			catch (final BusinessException e)
			{
				LOG.error(NO_LOCATION_CODE + locationCode + " and type: " + LOCATION, e);
			}
			final List<AccommodationViewData> accommodations = accomodationFacade.getAllAccommodationsByLocationFDCodeAndAccomType(
					locationModel, noncoreHodidayType);
			viewData.setAccommodationViewData(accommodations);
		}
		return viewData;
	}

	@Override
	@Cacheable(cacheName = PLACESTOSTAYCAROUSELCACHE, keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public PlacesOfInterestViewData getPlacesOfInterestViewData(final String locationCode)
	{
		LocationModel locationModel = null;
		PlacesOfInterestViewData viewData = null;
		final List<AttractionViewData> attractions = new ArrayList<AttractionViewData>();
		if (StringUtils.isNotEmpty(locationCode))
		{
			try
			{
				locationModel = mainStreamTravelLocationService.getLocationModel(locationCode, LOCATION,
						tuiUtilityService.getSiteReleventBrandPks());
			}
			catch (final BusinessException e)
			{
				LOG.error(NO_LOCATION_CODE + locationCode + " and type: " + LOCATION, e);
			}
			Collection<AttractionModel> attractionsModels = Collections.emptyList();
			if (locationModel != null)
			{
				attractionsModels = locationModel.getAttractions();
			}
			if (CollectionUtils.isNotEmpty(attractionsModels))
			{
				viewData = new PlacesOfInterestViewData();
				for (final AttractionModel attractionModel : attractionsModels)
				{
					final AttractionViewData attractionViewData = attractionFacade.getAttractionData(attractionModel.getCode());
					if (attractionViewData != null)
					{
						attractions.add(attractionViewData);
					}
				}
			}
			if (viewData != null)
			{
				viewData.setAttractionViewData(attractions);
			}
		}
		return viewData;
	}

	@Override
	@Cacheable(cacheName = PLACESTOSTAYCAROUSELCACHE, keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public ItineraryViewData getNonBookableAttractions(final String locationCode)
	{
		LocationModel locationModel = null;

		if (StringUtils.isNotEmpty(locationCode))
		{
			try
			{
				locationModel = mainStreamTravelLocationService.getLocationModel(locationCode, LOCATION,
						tuiUtilityService.getSiteReleventBrandPks());
			}
			catch (final BusinessException e)
			{
				LOG.error(NO_LOCATION_CODE + locationCode + " and type: " + LOCATION, e);
			}
			return attractionFacade.getNonBookableAttractions(locationModel);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getLeadPrice(java.lang.String , java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = "browseLeadPriceCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
	public AccommodationViewData getLeadPrice(final String componentUid, final String locationCode)
	{
		try
		{
			if (locationCode != null)
			{
				return searchFacade
						.getProductLeadPriceData(locationCode, (ProductLeadPriceComponentModel) getComponent(componentUid));
			}
			return null;
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error(LOG_MSG + componentUid + "]", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getReccommendedHolidayPackageData(uk.co.portaltech
	 * .tui.web.view.data .SearchResultsRequestData, java.lang.String)
	 */
	@Override
	public HolidayViewData getReccommendedHolidayPackageData(final SearchResultsRequestData searchParameter, final String siteBrand)
			throws SearchResultsBusinessException
	{
		// Fixing issue DE20303 . child ages used in bookflow is resent wrongly in enrich method to
		// prevent below line is added
		searchParameter.setChildrenAge(searchParameter.getChildAges());

		final HolidayViewData holidayViewData = new HolidayViewData();
		holidayViewData.setSearchResult(searchFacade.getRecommendedHolidayPackagesResultData(searchParameter, siteBrand));

		return holidayViewData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.portaltech.tui.facades.ComponentFacade#getRecommendedAccomPriceInfo (java.util.List, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<AccommodationViewData> getRecommendedAccomPriceInfo(final List<String> productCodes, final String pageType,
			final String componentUid)
	{

		return searchFacade.getRecommendedAccomPriceInfo(productCodes);
	}

	/*
     *
     * */
	@Override
	public void setFeaturePrority(final HolidayViewData viewData)
	{

		final List<String> featureList = new ArrayList<String>();

		try
		{
			final MultiValueFilterComponentModel filterComponent = (MultiValueFilterComponentModel) getComponent(MULTI_VALUE_FILTER_UID);
			final List<FacilityModel> priorityFeatureList = searchFacade.getFacilityList(filterComponent);
			for (final FacilityModel model : priorityFeatureList)
			{
				if (model != null)
				{
					featureList.add("FT_" + model.getType().getCode());
				}
			}
			if (viewData.getSearchResult().getFilterPanel() != null
					&& viewData.getSearchResult().getFilterPanel().getAccommodationOptions() != null)
			{
				final List<?> priorityList = viewData.getSearchResult().getFilterPanel().getAccommodationOptions().getFilters();
				final ListIterator<?> iterator = priorityList.listIterator();
				while (iterator.hasNext())
				{
					final Object data = iterator.next();
					if (data instanceof CommonFilterData && "features".equalsIgnoreCase(((CommonFilterData) data).getId()))
					{
						((CommonFilterData) data).setFeaturePriorityList(featureList);
					}
				}
			}
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("Componet with uid" + MULTI_VALUE_FILTER_UID + " not Found", e);
		}

	}

	@Override
	public CrossSellWrapper checkForCruiseLocation(final CrossSellWrapper crossSellWrapper)
	{
		final List<LocationCarouselViewData> carouselLocation = crossSellWrapper.getLocationCarousels();

		for (final LocationCarouselViewData locationCarouselViewData : carouselLocation)
		{
			final List<LocationData> location = locationCarouselViewData.getLocationDatas();

			checkForCruiseBrand(location.iterator());

		}
		return crossSellWrapper;

	}

	/**
   *
   */
	private void checkForCruiseBrand(final Iterator<LocationData> iterator)
	{
		while (iterator.hasNext())
		{
			final LocationData loc = iterator.next();

			if (!BrandUtils.isValidBrandCode(categoryService.getCategoryForCode(loc.getCode()).getBrands(), BrandType.CR.toString()))
			{
				iterator.remove();
			}
		}
	}

}
