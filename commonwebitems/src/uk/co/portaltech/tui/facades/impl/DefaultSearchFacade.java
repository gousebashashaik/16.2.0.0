/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static uk.co.portaltech.commons.DateUtils.currentdateTime;
import static uk.co.portaltech.commons.DateUtils.subtractDates;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.media.services.ProductMediaService;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AniteRoomModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.HowLongDurationModel;
import uk.co.portaltech.travel.model.InlineHeaderComponentModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.MultiValueFilterComponentModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.Offers;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.services.FacilityService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.travel.services.lowdeposit.PartyCompositionCriteria;
import uk.co.portaltech.travel.services.room.RoomsService;
import uk.co.portaltech.travel.thirdparty.endeca.FacetValue;
import uk.co.portaltech.travel.thirdparty.endeca.QuickSearchAutoSuggestItem;
import uk.co.portaltech.travel.thirdparty.endeca.QuickSearchGroup;
import uk.co.portaltech.travel.thirdparty.endeca.QuickSearchValue;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.comparators.sort.SearchResultViewDataComparator;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.components.model.AccommodationStandardComponentModel;
import uk.co.portaltech.tui.components.model.AccommodationsResultComponentModel;
import uk.co.portaltech.tui.components.model.AttractionResultPaneComponentModel;
import uk.co.portaltech.tui.components.model.AvailableFlightsComponentModel;
import uk.co.portaltech.tui.components.model.BookingComponentModel;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.DealsComponentModel;
import uk.co.portaltech.tui.components.model.InteractiveMapComponentModel;
import uk.co.portaltech.tui.components.model.LatestVillaAvailabilityDateComponentModel;
import uk.co.portaltech.tui.components.model.PlacesToStayCarouselModel;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.ProductLeadPriceComponentModel;
import uk.co.portaltech.tui.components.model.ThingsToDoCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesCarouselModel;
import uk.co.portaltech.tui.components.model.UpsellToHolidayComponentModel;
import uk.co.portaltech.tui.components.model.VillaAvailabilityComponentModel;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.enums.BoardBasis;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.AttractionFacade;
import uk.co.portaltech.tui.facades.ExcursionFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.helper.Pagination;
import uk.co.portaltech.tui.helper.VillaAvailabilityHelper;
import uk.co.portaltech.tui.model.SortCriteriaModel;
import uk.co.portaltech.tui.populators.BookFlowAccommodationPopulator;
import uk.co.portaltech.tui.populators.DateSelectorResultsPopulator;
import uk.co.portaltech.tui.populators.DurationSearchResultsPopulator;
import uk.co.portaltech.tui.populators.FilterSelectorPopulator;
import uk.co.portaltech.tui.populators.HolidaySearchResultsPopulator;
import uk.co.portaltech.tui.populators.MediaPopulatorLite;
import uk.co.portaltech.tui.populators.SearchResultAccomPopulator;
import uk.co.portaltech.tui.populators.SearchResultLowDepositPopulator;
import uk.co.portaltech.tui.populators.ShortlistHolidayPopulator;
import uk.co.portaltech.tui.populators.SingleAccomSearchResultsPopulator;
import uk.co.portaltech.tui.populators.ThBookFlowAccommodationPopulator;
import uk.co.portaltech.tui.resolvers.BackToSearchResultsUrlResolver;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.services.DurationHowLongService;
import uk.co.portaltech.tui.services.NonCoreProductsService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.productfinder.ProductFinder;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.utils.PackageIdGenerator;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.search.EnrichmentService;
import uk.co.portaltech.tui.web.url.builders.BookFlowAccomPageUrlBuilder;
import uk.co.portaltech.tui.web.url.builders.FlightOptionRequestBuilder;
import uk.co.portaltech.tui.web.url.builders.RequestBuilder;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FlightAvailabilityViewData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.Offer;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.SingleAccomSearchResultViewData;
import uk.co.portaltech.tui.web.view.data.VillaAvailabilitySearchRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationFilterDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.AttractionResultDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.DestinationQuickSearchDataWrapper;
import uk.co.portaltech.tui.web.view.data.wrapper.SearchResultDateSelectionViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.ViewWrapperPopulator;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityRequest;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityResponse;
import uk.co.portaltech.util.CatalogUtil;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.page.enums.PageType;
import uk.co.tui.book.page.request.PageRequest;
import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.book.services.NavigationPageResolver;
import uk.co.tui.exception.BookFlowAccommadationException;
import uk.co.tui.exception.EndecaSearchException;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.services.data.RoomAvailabilityIndicatorData;
import uk.co.tui.shortlist.data.ShortlistHolidayData;
import uk.co.tui.util.HolidayComparator;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;
import uk.co.tui.web.common.enums.CarouselLookupType;
import uk.co.tui.web.common.enums.SortParameters;

import com.googlecode.ehcache.annotations.Cacheable;


/**
 * @author Abi
 *
 */
/**
 * @author shyamaprasada.vs
 *
 */
public class DefaultSearchFacade implements SearchFacade
{

   /**
     *
     */
   private static final String FJ = "FJ";

   /**
     *
     */
   private static final String TH = "TH";

   private static final int NUMBER_ONE = 1;

   private static final double NUMBER_HALF = 0.5;

   private static final double NUMBER_ONE_TENTHOUSANDTH = 0.00001;

   private static final String USPS = "usps";

   private static final String SPECIAL_OFFER = "specialOffer";

   private static final String HOTEL = "HOTEL";

   private static final String TYPE_GROUP = "typeGroup";

   private static final String ROOM_TITLE = "roomTitle";

   private static final String HOLIDAY_INDEX = "holidayIndex";

   private static final String ENABLE_SHORTLIST_KEY = "%s.enable.shortlist";

   private static final TUIConfigService TUI_CONFIG_SERVICE = (TUIConfigService) Registry.getApplicationContext().getBean(
         "tuiConfigService");

   private static final String AVAILABLEROOMS_DISPLAY_LAI_THRESHOLD = ".display_LAI_threshold";

   @Autowired
   private Map<String, ProductFinder> finders;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private AccommodationFacade accomodationFacade;

   @Resource
   private LocationFacade locationFacade;

   @Resource
   private ExcursionFacade excursionFacade;

   @Resource
   private AttractionFacade attractionFacade;

   @Resource
   private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   @Resource
   private CategoryService categoryService;

   @Resource
   private ProductService productService;

   @Resource
   private CatalogUtil catalogUtil;

   @Resource
   private SessionService sessionService;

   @Resource
   private TUIConfigService tuiConfigService;

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private ViewWrapperPopulator viewWrapperPopulator;

   @Resource
   private RoomsService roomsService;

   @Resource
   private VillaAvailabilityHelper villaAvailabilityHelper;

   @Resource
   private HolidaySearchResultsPopulator holidaySearchResultsPopulator;

   @Resource
   private SingleAccomSearchResultsPopulator singleAccomSearchResultsPopulator;

   @Resource
   private SearchResultLowDepositPopulator searchResultLowDepositPopulator;

   @Resource
   private SearchResultAccomPopulator searchResultsAccomPopulator;

   @Resource
   private DateSelectorResultsPopulator dateSelectorResultsPopulator;

   @Resource
   private BookFlowAccommodationPopulator bookFlowAccommodationPopulator;

   @Resource
   private ThBookFlowAccommodationPopulator thBookFlowAccommodationPopulator;

   @Resource
   private FeatureService featureService;

   @Resource
   private Pagination pagination;

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private RequestBuilder bookFlowFlightOptionUrlBuilder;

   @Resource
   private DurationSearchResultsPopulator durationSearchResultsPopulator;

   @Resource
   private BookFlowAccomPageUrlBuilder bookFlowAccomPageUrlBuilder;

   @Resource
   private FilterSelectorPopulator filterPopulator;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private NonCoreProductsService nonCoreProductsService;

   @Resource
   private DroolsPriorityProviderService droolsPriorityProviderService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private BackToSearchResultsUrlResolver backToSearchResultsUrlResolver;

   @Resource
   private NavigationPageResolver navigationPageResolver;

   private ConfigurationService configurationService;

   @Resource
   private LocationConverter locationConverter;

   @Resource
   private ConfigurablePopulator<LocationModel, LocationData, LocationOption> defaultLocationConfiguredPopulator;

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource
   private ShortlistHolidayPopulator shortlistHolidayPopulator;

   private static final String LOG_MSG = "productFinder_";

   private static final String LOG_MSG_ONE = "No product finder for lookup type ";

   private static final String LOG_MSG_TWO = " found. Check the lookupType field or create a Finder for the lookupType.";

   private static final String COMPONENT_ID = "WF_COM_306";

   private static final String SEARCH_PANEL_COMPONENT_ID = "WF_COM_300";

   private static final String SEARCHRESULTS = "searchResults";

   private static final String LATESTCRITERIA = "latestCriteria";

   private static final String COACH_TRANSFER = "COACH_TRANSFER";

   private static final String FREE_KIDS = "FREE_KIDS";

   private static final String FREE_CAR_HIRE = "FREE_CAR_HIRE";

   private static final String FREE_CHILD_PLACE = "1 free child place";

   private static final String COACH_TRANSFER_DESCRIPTION = "coach transfer";

   private static final String FREE_CAR_HIRE_DESCRIPTION = "free car hire";

   private static final String BOXING = "boxing";

   private static final String HOLIDAY_FINDER_COMPONENT_ID = "WF_COM_301";

   private static final String FALCON_ACCOM_USPS = "falcon_usp_override";

   private static final String FALCON_PRODUCT_OVERRIDE = "falcon_product_override";

   private final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
   { ROOM_TITLE, TYPE_GROUP, USPS, FALCON_ACCOM_USPS, FALCON_PRODUCT_OVERRIDE, "tRating", "latitude", "longitude", "name",
         "strapline", "holiday_type", "special_offer_promotion", SPECIAL_OFFER }));

   @Resource
   private EnrichmentService enrichmentService;

   private static final String BACKTOSEARCH = "backToSearch";

   @Resource(name = "holidayOptionsPageUrlBuilder")
   private RequestBuilder holidayOptionsPageUrl;

   @Resource(name = "iscapeflightOptionsUrlBuilder")
   private FlightOptionRequestBuilder flightOptionRequestBuilder;

   private static final TUILogUtils LOG = new TUILogUtils("DefaultSearchFacade");

   @Resource(name = "siteAwareKeyGenerator")
   private SiteAwareKeyGenerator keyGenerator;

   @Resource
   private FacilityService facilityService;

   @Resource
   private DurationHowLongService durationHowLongService;

   private static final int TWO = 2;

   @Resource
   private ProductMediaService productMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   @Override
   public List<AccommodationViewData> getDealsData(final String locationCode, final String pageType, final String seoPageType,
         final DealsComponentModel item)
   {
      if (item == null)
      {
         return Collections.<AccommodationViewData> emptyList();
      }

      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }

      List<AccommodationViewData> result = null;

      // Generate a SearchRequest object to pass to our ProductFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoPageType);
      final SearchResultData<? extends Object> dealsSearchResult = finder.search(searchRequest);
      if (dealsSearchResult != null)
      {
         final List<ResultData> searchResult = (List<ResultData>) dealsSearchResult.getResults();
         result = accomodationFacade.getAccommodationDealsData(searchResult);
         iterateAccomViewData(result);
      }
      return result;
   }

   /**
    * @param result
    */
   private void iterateAccomViewData(final List<AccommodationViewData> result)
   {
      for (final AccommodationViewData accommodationViewData : result)
      {
         if (accommodationViewData.getCategories() != null)
         {
            for (final CategoryData category : accommodationViewData.getCategories())
            {
               final LocationData locationData = locationFacade.getLocationData(category.getCode());
               if (locationData != null)
               {
                  setLocationType(accommodationViewData, locationData);
               }
            }
         }
      }
   }

   /**
    * @param accommodationViewData
    * @param locationData
    */
   private void setLocationType(final AccommodationViewData accommodationViewData, final LocationData locationData)
   {
      if (LocationType.RESORT.equals(locationData.getLocationType()))
      {
         accommodationViewData.setResort(locationData);

      }
      else if (LocationType.DESTINATION.equals(locationData.getLocationType())
            || LocationType.REGION.equals(locationData.getLocationType()))
      {
         accommodationViewData.setDestination(locationData);
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getPlacesToStayData(java.lang .String,
    * uk.co.portaltech.tui.components.model.AccommodationCarouselModel)
    */
   @Override
   public List<AccommodationViewData> getPlacesToStayData(final String locationCode, final String pageType,
         final String seoPageType, final AccommodationCarouselModel item)
   {
      if (item == null)
      {
         return Collections.<AccommodationViewData> emptyList();
      }

      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }

      List<AccommodationViewData> result = null;

      // Generate a SearchRequest object to pass to our ProductFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoPageType);
      verifyAccomTypeContext(item, searchRequest);
      final SearchResultData placesToStaySearchResult = finder.search(searchRequest);

      if (placesToStaySearchResult != null)
      {
         final List<ResultData> searchResult = placesToStaySearchResult.getResults();
         result = accomodationFacade.getAccommodationsWithEndecaData(searchResult);
      }

      return result;
   }

   /**
    * @param item
    * @param searchRequest
    */
   private void verifyAccomTypeContext(final AccommodationCarouselModel item, final SearchRequestData searchRequest)
   {
      if (item.getAccommodationType() != null)
      {
         searchRequest.setAccommodationTypeContext(item.getAccommodationType().getCode());
      }
      else
      {
         LOG.warn("AccommodationTypeContext was null");
      }
   }

   @Override
   public SearchResultData getProductCrossSellCarouselData(final String locationCode, final String pageType,
         final String seoType, final PlacesToStayCarouselModel item, final String productCode)
   {

      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      if (finder == null)
      {
         throw new RuntimeException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }

      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setProductCode(productCode);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoType);
      if (StringUtils.isNotBlank(item.getNonCoreHolidayType()))
      {
         searchRequest.setNonCoreHolidayType(item.getNonCoreHolidayType());
      }

      return finder.search(searchRequest);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getThingsToDoViewData(java. lang.String,
    * uk.co.portaltech.tui.components.model.AccommodationCarouselModel)
    */
   @Override
   public SearchResultData getThingsToDoViewData(final String locationCode, final String pageType, final String seoPageType,
         final ThingsToDoCarouselModel item)
   {

      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      // Generate a SearchRequest object to pass to our ProductFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoPageType);
      return finder.search(searchRequest);

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getProductCrossSellData(java .lang.String,
    * uk.co.portaltech.tui.components.model.ProductCrossSellCarouselModel)
    */
   @Override
   public SearchResultData getProductCrossSellData(final String locationCode, final String pageType, final String seoType,
         final ProductCrossSellCarouselComponentModel item, final String productCode)
   {
      if (item == null)
      {
         throw new IllegalArgumentException("No carousel component to work with");
      }
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setProductCode(productCode);
      searchRequest.setTabbedResult(true);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoType);
      return finder.search(searchRequest);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getQuickSearchResultData(java .lang.String)
    */
   @Override
   public DestinationQuickSearchDataWrapper getQuickSearchResultData(final String searchParameter)
   {
      final ProductFinder finder = finders.get("productFinder_ENDECA_DESTINATION_QUICK_SEARCH");

      if (finder == null)
      {
         throw new IllegalArgumentException("No product finder for lookup type productFinder_ENDECA_DESTINATION_QUICK_SEARCH"
               + LOG_MSG_TWO);
      }

      QuickSearchAutoSuggestItem quickSearchAutoSuggestItem = null;
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setQuickSearchParameter(searchParameter);
      final SearchResultData quickSearchResult = finder.search(searchRequest);
      if (quickSearchResult != null)
      {
         if ("0".equals(quickSearchResult.getQuickSearchAutoSuggestItem().getTotalNumResults()))
         {
            return null;
         }
         quickSearchAutoSuggestItem = quickSearchResult.getQuickSearchAutoSuggestItem();
      }

      return createDestinationQuickSearchDataWrapper(quickSearchAutoSuggestItem);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getAccommodationPriceData(java .lang.String,
    * uk.co.portaltech.tui.components.model.BookingComponentModel)
    */
   @Override
   public AccommodationViewData getAccommodationPriceData(final String accommodationCode,
         final BookingComponentModel bookingComponentModel)
   {
      if (bookingComponentModel == null)
      {
         return null;
      }
      final ProductFinder productFinder = finders.get(LOG_MSG + bookingComponentModel.getLookupType());
      if (productFinder == null)
      {
         throw new RuntimeException(LOG_MSG_ONE + bookingComponentModel.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequestData = new SearchRequestData();
      searchRequestData.setRelevantItem(bookingComponentModel);
      searchRequestData.setProductCode(accommodationCode);
      final AccommodationViewData accommodationViewData = accomodationFacade
            .getAccommodationPriceDataFromEndeca((SearchResultData<ResultData>) productFinder.search(searchRequestData));
      accommodationViewData.setCode(accommodationCode);

      if (accommodationViewData.getRoomOccupancy() != null)
      {
         accommodationViewData.setRoomOccupancy(getRoomOccupancy(accommodationViewData.getRoomOccupancy()).toString());
      }

      return accommodationViewData;
   }

   /**
    * @param roomOccupancy
    */
   private StringBuilder getRoomOccupancy(final String roomOccupancy)
   {
      final String[] roomOcc = roomOccupancy.split("/");
      final StringBuilder sb = new StringBuilder();
      final int adultCount = Integer.parseInt(StringUtils.substring(roomOcc[0], 0, 1));
      final int childCount = Integer.parseInt(StringUtils.substring(roomOcc[1], 0, 1));
      final int infantCount = Integer.parseInt(StringUtils.substring(roomOcc[TWO], 0, 1));

      sb.append(adultCount);

      if (adultCount == 1)
      {
         sb.append(" adult");
      }
      else
      {
         sb.append(" adults");
      }
      addChildToStringBuffer(sb, childCount);
      addInfantToStringBuilder(sb, infantCount);
      return sb;
   }

   /**
    * @param sb
    * @param childCount
    */
   private void addChildToStringBuffer(final StringBuilder sb, final int childCount)
   {
      if (childCount > 0)
      {
         sb.append(", ").append(childCount);

         if (childCount == 1)
         {
            sb.append(" child");
         }
         else
         {
            sb.append(" children");
         }

      }
   }

   /**
    * @param sb
    * @param infantCount
    */
   private void addInfantToStringBuilder(final StringBuilder sb, final int infantCount)
   {
      if (infantCount > 0)
      {
         sb.append(", ").append(infantCount);

         if (infantCount == 1)
         {
            sb.append(" infant");
         }
         else
         {
            sb.append(" infants");
         }
      }
   }

   /**
    * @param quickSearchAutoSuggestItem
    */
   private DestinationQuickSearchDataWrapper createDestinationQuickSearchDataWrapper(
         final QuickSearchAutoSuggestItem quickSearchAutoSuggestItem)
   {
      DestinationQuickSearchDataWrapper destinationQuickSearchDataWrapper = null;
      if (quickSearchAutoSuggestItem != null)
      {
         destinationQuickSearchDataWrapper = new DestinationQuickSearchDataWrapper();
         final List<AccommodationViewData> accommodations = new ArrayList<AccommodationViewData>();
         final List<LocationData> locations = new ArrayList<LocationData>();
         destinationQuickSearchDataWrapper.setAccommodations(accommodations);
         destinationQuickSearchDataWrapper.setLocations(locations);
         final List<QuickSearchGroup> quickSearchGroups = quickSearchAutoSuggestItem.getQuickSearchGroups();
         if (quickSearchGroups != null && !quickSearchGroups.isEmpty())
         {
            for (final QuickSearchGroup quickSearchGroup : quickSearchGroups)
            {
               final String quickSearchGroupName = quickSearchGroup.getQuickSearchGroupName();
               final List<QuickSearchValue> quickSearchValues = quickSearchGroup.getQuickSearchValues();
               if (quickSearchValues != null && !quickSearchValues.isEmpty())
               {
                  for (final QuickSearchValue quickSearchValue : quickSearchValues)
                  {
                     final String code = quickSearchValue.getCode();
                     final boolean isCategory = quickSearchValue.isCategory();
                     if (isCategory)
                     {
                        setLocationData(locations, quickSearchGroupName, quickSearchValue, code);
                     }
                     else
                     {
                        final AccommodationViewData accommodationViewData = new AccommodationViewData();
                        accommodations.add(accommodationViewData);
                        if (!StringUtils.isEmpty(code))
                        {
                           resolveProductUrl(quickSearchValue, code);
                        }
                        setAccommodationData(accommodationViewData, quickSearchValue, quickSearchGroupName);
                     }
                  }
               }
            }
         }
      }

      return destinationQuickSearchDataWrapper;
   }

   /**
    * @param quickSearchValue
    * @param code
    */
   private void resolveProductUrl(final QuickSearchValue quickSearchValue, final String code)
   {
      try
      {
         final ProductModel productForCode = productService.getProductForCode(code);
         if (productForCode != null)
         {
            quickSearchValue.setUrl(tuiProductUrlResolver.resolve(productForCode));
         }
      }
      catch (final UnknownIdentifierException uie)
      {
         LOG.error("Product code present in Endeca and not in PIM. Systems not in sync", uie);
      }
   }

   /**
    * @param locations
    * @param quickSearchGroupName
    * @param quickSearchValue
    * @param code
    */
   private void setLocationData(final List<LocationData> locations, final String quickSearchGroupName,
         final QuickSearchValue quickSearchValue, final String code)
   {
      final LocationData locationData = new LocationData();
      locations.add(locationData);
      if (!StringUtils.isEmpty(code))
      {
         resolveCategoryUrl(quickSearchValue, code);
      }
      setLocationData(locationData, quickSearchValue, quickSearchGroupName);
   }

   /**
    * @param quickSearchValue
    * @param code
    */
   private void resolveCategoryUrl(final QuickSearchValue quickSearchValue, final String code)
   {
      // Handling Category if not found in PIM.
      CategoryModel categoryForCode = null;
      try
      {
         categoryForCode = categoryService.getCategoryForCode(code);
      }
      catch (final UnknownIdentifierException ue)
      {
         LOG.error("Category Service was unable to find the Location with code: " + code, ue);
      }
      if (categoryForCode != null)
      {
         // set to blank, some times its not
         // blank in case if there are any
         // previously
         // set.
         tuiCategoryModelUrlResolver.setOverrideSubPageType("");
         quickSearchValue.setUrl(tuiCategoryModelUrlResolver.resolve(categoryForCode));
      }
   }

   /**
    * @param accommodationViewData
    * @param quickSearchValue
    */
   private void setAccommodationData(final AccommodationViewData accommodationViewData, final QuickSearchValue quickSearchValue,
         final String quickSearchGroupName)
   {
      accommodationViewData.setCode(quickSearchValue.getCode());
      accommodationViewData.setUrl(quickSearchValue.getUrl());
      accommodationViewData.setName(quickSearchValue.getLabel());
      accommodationViewData.setCount(quickSearchValue.getCount());
      accommodationViewData.setDisplayOrder(quickSearchValue.getDisplayOrder());
      if (viewSelector.checkIsMobile() && TH.equalsIgnoreCase(tuiUtilityService.getSiteBrand()))
      {
         final String siteBrand = tuiUtilityService.getSiteBrand();
         accommodationViewData.setMultiSelect(Boolean.valueOf(quickSearchValue.isMultiSelect()).booleanValue());
         final AccommodationViewData accomdata = accomodationFacade.getAccommodationByCode(quickSearchValue.getCode());
         setAccommodationType(accommodationViewData, quickSearchValue, quickSearchGroupName, siteBrand, accomdata);
      }

      else
      {
         accommodationViewData.setAccommodationType(quickSearchGroupName);
      }
   }

   /**
    * @param accommodationViewData
    * @param quickSearchValue
    * @param quickSearchGroupName
    * @param siteBrand
    * @param accomdata
    */
   private void setAccommodationType(final AccommodationViewData accommodationViewData, final QuickSearchValue quickSearchValue,
         final String quickSearchGroupName, final String siteBrand, final AccommodationViewData accomdata)
   {
      if (accomdata != null)
      {
         accommodationViewData.setAccommodationType(accomdata.getAccommodationType());
         if ((TH.equalsIgnoreCase(siteBrand) || FJ.equalsIgnoreCase(siteBrand))
               && "HOTEL".equalsIgnoreCase(accomdata.getAccommodationType()) || accomTypeCheck(accomdata))
         {
            addHotels(quickSearchValue, accommodationViewData);
         }
      }
      else
      {
         accommodationViewData.setAccommodationType(quickSearchGroupName);
      }
   }

   private boolean accomTypeCheck(final AccommodationViewData accomdata)
   {
      return "APARTMENT_HOTEL".equalsIgnoreCase(accomdata.getAccommodationType())
            || "VILLA".equalsIgnoreCase(accomdata.getAccommodationType())
            || "ship".equalsIgnoreCase(accomdata.getAccommodationType())
            || "SELF_CATERED".equalsIgnoreCase(accomdata.getAccommodationType())
            || "TOUR".equalsIgnoreCase(accomdata.getAccommodationType());
   }

   /**
    * @param locationData
    * @param quickSearchValue
    */
   private void setLocationData(final LocationData locationData, final QuickSearchValue quickSearchValue,
         final String quickSearchGroupName)
   {
      locationData.setCode(quickSearchValue.getCode());
      locationData.setUrl(quickSearchValue.getUrl());
      locationData.setName(quickSearchValue.getLabel());
      locationData.setCount(quickSearchValue.getCount());
      locationData.setDisplayOrder(quickSearchValue.getDisplayOrder());

      setLocationType(locationData, quickSearchGroupName);
      if (viewSelector.checkIsMobile() && TH.equalsIgnoreCase(tuiUtilityService.getSiteBrand()))
      {
         final String siteBrand = tuiUtilityService.getSiteBrand();

         locationData.setMultiSelect(quickSearchValue.isMultiSelect());
         final CategoryModel source = categoryService.getCategoryForCode(quickSearchValue.getCode());
         final LocationData location = locationFacade.getLocationData(quickSearchValue.getCode());
         locationData.setLocationType(location.getLocationType());

         final Collection<CategoryModel> parents = source.getSupercategories();
         if (verifyCondition(quickSearchGroupName, siteBrand))
         {
            addDestinations(quickSearchValue, quickSearchGroupName, locationData, parents);
         }
         else
         {
            if (verifyBrandAndCountry(quickSearchGroupName, siteBrand))
            {
               addCountry(quickSearchValue, quickSearchGroupName, locationData);
            }
         }

      }

   }

   /**
    * @param quickSearchGroupName
    * @param siteBrand
    * @return boolean
    */
   private boolean verifyBrandAndCountry(final String quickSearchGroupName, final String siteBrand)
   {
      return (TH.equalsIgnoreCase(siteBrand) || FJ.equalsIgnoreCase(siteBrand))
            && ("COUNTRY".equalsIgnoreCase(quickSearchGroupName));
   }

   /**
    * @param quickSearchGroupName
    * @param siteBrand
    * @return boolean
    */
   private boolean verifyCondition(final String quickSearchGroupName, final String siteBrand)
   {
      return (TH.equalsIgnoreCase(siteBrand) || FJ.equalsIgnoreCase(siteBrand))
            && ("DESTINATION".equalsIgnoreCase(quickSearchGroupName) || "RESORT".equalsIgnoreCase(quickSearchGroupName) || "REGION"
                  .equalsIgnoreCase(quickSearchGroupName));
   }

   /**
    * @param locationData
    * @param quickSearchGroupName
    */
   private void setLocationType(final LocationData locationData, final String quickSearchGroupName)
   {
      final LocationType[] locationTypes = LocationType.values();
      for (final LocationType locationType : locationTypes)
      {
         if (locationType.getCode().equalsIgnoreCase(quickSearchGroupName))
         {
            locationData.setLocationType(locationType);
            break;
         }
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getPlacesToStayCarouselData (java.lang.String)
    */
   @Override
   public List<LocationData> getPlacesToStayCarouselData(final String locationCode, final String pageType, final String seoType,
         final PlacesToStayCarouselModel item)
   {
      if (item == null)
      {
         return Collections.<LocationData> emptyList();
      }
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      final SearchRequestData searchRequest = new SearchRequestData(locationCode, pageType, seoType, item);

      setAccomTypeContext(item, searchRequest);

      if (StringUtils.isNotBlank(item.getNonCoreHolidayType()))
      {
         searchRequest.setNonCoreHolidayType(item.getNonCoreHolidayType());
      }

      final SearchResultData searchResult = finder.search(searchRequest);
      final List<ResultData> resultDataList = searchResult.getResults();
      final List<LocationData> locationDatas = new ArrayList<LocationData>();

      if (resultDataList != null && !resultDataList.isEmpty())
      {
         final boolean fetchAccommodation = false;
         final List<ResultData> accommodationsLst = new ArrayList<ResultData>();
         getLocationdata(resultDataList, locationDatas, fetchAccommodation, accommodationsLst);
      }
      return locationDatas;
   }

   /**
    * @param resultDataList
    * @param locationDatas
    * @param fetchAccommodationParam
    * @param accommodationsLst
    */
   private void getLocationdata(final List<ResultData> resultDataList, final List<LocationData> locationDatas,
         final boolean fetchAccommodationParam, final List<ResultData> accommodationsLst)
   {
      boolean fetchAccommodation = fetchAccommodationParam;
      for (final ResultData resultData : resultDataList)
      {
         fetchAccommodation = setLocationOrAccommodation(locationDatas, accommodationsLst, resultData);
      }
      if (fetchAccommodation && !accommodationsLst.isEmpty())
      {
         final LocationData locationData = new LocationData();
         locationData.setAccommodations(accomodationFacade.getAccommodationsWithEndecaData(accommodationsLst));
         locationDatas.add(locationData);
      }
   }

   /**
    * @param item
    * @param searchRequest
    */
   private void setAccomTypeContext(final PlacesToStayCarouselModel item, final SearchRequestData searchRequest)
   {
      if (item.getAccommodationType() != null)
      {
         searchRequest.setAccommodationTypeContext(item.getAccommodationType().getCode());
      }
      else
      {
         LOG.warn("AccommodationType was null");
      }
   }

   /**
    * @param locationDatas
    * @param accommodationsLst
    * @param resultData
    * @return setLocationOrAccommodation
    */
   private boolean setLocationOrAccommodation(final List<LocationData> locationDatas, final List<ResultData> accommodationsLst,
         final ResultData resultData)
   {
      if (resultData.getParentFacetOption() != null && resultData.getParentFacetOption().getFacetValues() != null
            && !resultData.getParentFacetOption().getFacetValues().isEmpty())
      {
         final List<FacetValue> locations = resultData.getParentFacetOption().getFacetValues();
         iterateLocations(locationDatas, resultData, locations);
      }
      else if (SearchResultType.ACCOMMODATION.equals(resultData.getType()))
      {
         accommodationsLst.add(resultData);
         return true;
      }
      return false;
   }

   /**
    * @param locationDatas
    * @param resultData
    * @param locations
    */
   private void iterateLocations(final List<LocationData> locationDatas, final ResultData resultData,
         final List<FacetValue> locations)
   {
      if (locations != null)
      {
         for (final FacetValue locationDimention : locations)
         {
            setLocationData(locationDatas, resultData, locationDimention);
         }

      }
   }

   /**
    * @param locationDatas
    * @param resultData
    * @param locationDimention
    */
   private void setLocationData(final List<LocationData> locationDatas, final ResultData resultData,
         final FacetValue locationDimention)
   {
      final LocationData locationData = locationFacade.getLocationData(locationDimention.getCode());
      if (resultData.getFacetOption() != null)
      {
         final List<FacetValue> subLocations = resultData.getFacetOption().getFacetValues();
         iterateSubLocations(locationData, subLocations);
      }
      if (resultData.getChildren() != null)
      {
         final List<ResultData> accommodations = new ArrayList<ResultData>();
         iterateResultData(resultData, accommodations);
         if (!accommodations.isEmpty())
         {
            locationData.setAccommodations(accomodationFacade.getAccommodationsWithEndecaData(accommodations));
         }
      }
      locationDatas.add(locationData);
   }

   /**
    * @param resultData
    * @param accommodations
    */
   private void iterateResultData(final ResultData resultData, final List<ResultData> accommodations)
   {
      for (final ResultData child : resultData.getChildren())
      {
         if (SearchResultType.ACCOMMODATION.equals(child.getType()))
         {
            accommodations.add(child);
         }
      }
   }

   /**
    * @param locationData
    * @param subLocations
    */
   private void iterateSubLocations(final LocationData locationData, final List<FacetValue> subLocations)
   {
      if (subLocations != null && !subLocations.isEmpty())
      {
         for (final FacetValue dimensionData : subLocations)
         {
            final LocationData subLocation = locationFacade.getLocationData(dimensionData.getCode());
            // this condition is put because we are caching the DTO's, so to
            // avoid duplicate additions.
            if (!locationData.getSubLocations().contains(subLocation))
            {
               locationData.getSubLocations().add(subLocation);
            }
         }
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getTopPlacesToStayData(java .lang.String,
    * uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel )
    */
   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getTopPlacesToStayData(java .lang.String,
    * uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel )
    */
   @Override
   public SearchResultData getTopPlacesToStayData(final String locationCode, final String pageType, final String seoPageType,
         final TopPlacesCarouselModel carouselModel)
   {

      final ProductFinder finder = finders.get(LOG_MSG + carouselModel.getLookupType());
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + carouselModel.getLookupType() + LOG_MSG_TWO);
      }
      // Generate a SearchRequest object to pass to our ProductFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(carouselModel);
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoPageType);

      return finder.search(searchRequest);
   }

   @Override
   public AccommodationFilterDataWrapper getAccommodationResultAndFilterData(final String categoryCode, final String pageType,
         final String seoType, final List<String> facetOptionNames, final List<String> filterParams,
         final AccommodationsResultComponentModel item)
   {
      // get accommodation results.
      ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(categoryCode);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoType);
      searchRequest.setFilterParams(filterParams);
      searchRequest.setFacetOptionNames(facetOptionNames);
      final SearchResultData accommodationsSearchResult = finder.search(searchRequest);
      List<ResultData> results = accommodationsSearchResult.getResults();
      final AccommodationFilterDataWrapper accommodationFilterDataWrapper = new AccommodationFilterDataWrapper();

      accommodationFilterDataWrapper.setPaginationData(accommodationsSearchResult.getPagination());
      accommodationFilterDataWrapper.setAccommodations(viewWrapperPopulator.getAccommodationList(results));

      // get accommodation filter data
      finder = finders.get(LOG_MSG + CarouselLookupType.ENDECA_ACCOMMODATIONS_FILTER);
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }

      searchRequest.setRelevantItem(null);
      searchRequest.setLookUpType(CarouselLookupType.ENDECA_ACCOMMODATIONS_FILTER.getCode());
      searchRequest.setBreadCrumbRequired(true);

      final SearchResultData<? extends Object> accommodationsFilterResult = finder.search(searchRequest);
      results = (List<ResultData>) accommodationsFilterResult.getResults();
      accommodationFilterDataWrapper.setFacetOptions(viewWrapperPopulator.getFacetOptions(results));
      accommodationFilterDataWrapper.setSelectedFacetOptions(viewWrapperPopulator.getSelectedFacetOptions(results));
      return accommodationFilterDataWrapper;

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getAttractionResultPaneData (java.lang.String, java.lang.String,
    * java.lang.String, uk.co.portaltech.tui.components.model.AttractionResultPaneComponentModel)
    */
   @Override
   @Cacheable(cacheName = "attractionResultPaneCache")
   public AttractionResultDataWrapper getAttractionResultPaneData(final String categoryCode, final String pageType,
         final String seoType, final String sortBy, final AttractionResultPaneComponentModel item)
   {
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequest = new SearchRequestData(categoryCode, pageType, seoType, item);
      searchRequest.setSortBy(sortBy);
      final SearchResultData attractionSearchResult = finder.search(searchRequest);
      final List<ResultData> results = attractionSearchResult.getResults();

      final AttractionResultDataWrapper attractionResultDataWrapper = new AttractionResultDataWrapper();
      if (results != null && !results.isEmpty())
      {
         final List<ResultData> sights = new ArrayList<ResultData>();
         final List<ResultData> events = new ArrayList<ResultData>();
         final List<ResultData> excursions = new ArrayList<ResultData>();

         verifyAndSetData(results, attractionResultDataWrapper, sights, events, excursions);
      }
      attractionResultDataWrapper.setSortBy(attractionSearchResult.getSortOptions());
      final LocationData locationData = locationFacade.getLocationData(categoryCode);
      attractionResultDataWrapper.setTopLocationName(locationData.getName());
      return attractionResultDataWrapper;
   }

   /**
    * @param results
    * @param attractionResultDataWrapper
    * @param sights
    * @param events
    * @param excursions
    */
   private void verifyAndSetData(final List<ResultData> results, final AttractionResultDataWrapper attractionResultDataWrapper,
         final List<ResultData> sights, final List<ResultData> events, final List<ResultData> excursions)
   {
      for (final ResultData result : results)
      {
         setSearchResultTypeForAttractions(sights, events, excursions, result);
      }
      if (!excursions.isEmpty())
      {
         attractionResultDataWrapper.setExcursions(excursionFacade.getExcursionsWithEndecaData(excursions));
      }

      if (!sights.isEmpty())
      {
         attractionResultDataWrapper.setSights(attractionFacade.getAttractionEnrichedData(sights));
      }
      if (!events.isEmpty())
      {
         attractionResultDataWrapper.setEvents(attractionFacade.getAttractionEnrichedData(events));
      }
   }

   /**
    * @param sights
    * @param events
    * @param excursions
    * @param result
    */
   private void setSearchResultTypeForAttractions(final List<ResultData> sights, final List<ResultData> events,
         final List<ResultData> excursions, final ResultData result)
   {
      if (SearchResultType.EXCURSION.equals(result.getType()))
      {
         excursions.add(result);
      }
      else if (SearchResultType.ATTRACTION.equals(result.getType()))
      {
         if ("SIGHT".equalsIgnoreCase(result.getSearchResultSubtype()))
         {
            sights.add(result);
         }
         else if ("EVENT".equalsIgnoreCase(result.getSearchResultSubtype()))
         {
            events.add(result);
         }
         else
         {
            // If it is any other type such as 'OTHER' treat it as a
            // sight.
            sights.add(result);
         }
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getInteractiveMapData(java. lang.String, java.lang.String,
    * java.lang.String, uk.co.portaltech.tui.components.model.InteractiveMapComponentModel)
    */
   @Override
   public MapDataWrapper getInteractiveMapData(final String categoryCode, final String pageType, final String seoPageType,
         final InteractiveMapComponentModel item)
   {
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequest = new SearchRequestData(categoryCode, pageType, seoPageType, item);
      // Auto-generated
      // method
      // stub
      MapDataWrapper mapDataWrapper = new MapDataWrapper();
      final SearchResultData attractionSearchResult = finder.search(searchRequest);
      final List<ResultData> results = attractionSearchResult.getResults();

      // loop through the result - and break down the list according to record
      // type - move this to appropriate helper/
      // method

      if (results == null || results.isEmpty())
      {
         LOG.error("No data returned from endeca for Interactive Map component");
      }
      else
      {
         final List<ResultData> accommodations = new ArrayList<ResultData>();
         final List<ResultData> hotels = new ArrayList<ResultData>();
         final List<ResultData> villas = new ArrayList<ResultData>();
         final List<ResultData> sights = new ArrayList<ResultData>();
         final List<ResultData> events = new ArrayList<ResultData>();
         final List<ResultData> excursions = new ArrayList<ResultData>();
         for (final ResultData result : results)
         {
            setSearchResultTypeForInteractiveMap(accommodations, hotels, villas, sights, events, excursions, result);

         }
         addAccomDataToMap(mapDataWrapper, accommodations, hotels, excursions);
         setAccomDataToMap(mapDataWrapper, villas, sights, events);
         mapDataWrapper = getInteractiveMapWrapper(events, sights, excursions, hotels, villas, mapDataWrapper);
      }
      final LocationData locationdata = locationFacade.getLocationData(categoryCode);
      mapDataWrapper.setTopLocationName(locationdata.getName());
      mapDataWrapper.setTopLocationName(categoryService.getCategoryForCode(categoryCode).getName());
      mapDataWrapper.setLocationType("resort");
      mapDataWrapper.setSiteBrand(tuiUtilityService.getSiteBrand());
      return mapDataWrapper;
   }

   /**
    * @param mapDataWrapper
    * @param villas
    * @param sights
    * @param events
    */
   private void setAccomDataToMap(final MapDataWrapper mapDataWrapper, final List<ResultData> villas,
         final List<ResultData> sights, final List<ResultData> events)
   {
      if (!villas.isEmpty())
      {
         mapDataWrapper.setVillas(accomodationFacade.getAccommodationsWithEndecaData(villas));
      }

      if (!sights.isEmpty())
      {
         mapDataWrapper.setSights(attractionFacade.getAttractionEnrichedData(sights));
      }
      if (!events.isEmpty())
      {
         mapDataWrapper.setEvents(attractionFacade.getAttractionEnrichedData(events));
      }
   }

   /**
    * @param mapDataWrapper
    * @param accommodations
    * @param hotels
    * @param excursions
    */
   private void addAccomDataToMap(final MapDataWrapper mapDataWrapper, final List<ResultData> accommodations,
         final List<ResultData> hotels, final List<ResultData> excursions)
   {
      if (!excursions.isEmpty())
      {
         mapDataWrapper.setExcursions(excursionFacade.getExcursionsWithEndecaData(excursions));
      }

      if (!accommodations.isEmpty())
      {
         mapDataWrapper.setAccommodations(accomodationFacade.getAccommodationsWithEndecaData(accommodations));
      }

      if (!hotels.isEmpty())
      {
         mapDataWrapper.setHotels(accomodationFacade.getAccommodationsWithEndecaData(hotels));
      }
   }

   /**
    * @param hotels
    * @param villas
    * @param sights
    * @param events
    * @param excursions
    * @param result
    */
   private void setSearchResultTypeForInteractiveMap(final List<ResultData> accommodations, final List<ResultData> hotels,
         final List<ResultData> villas, final List<ResultData> sights, final List<ResultData> events,
         final List<ResultData> excursions, final ResultData result)
   {
      final BrandDetails brandDetails = (BrandDetails) sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);

      if (SearchResultType.ACCOMMODATION.equals(result.getType()))
      {
         verifyBrandAndSetAccomType(accommodations, hotels, villas, result, brandDetails);
      }
      else if (SearchResultType.EXCURSION.equals(result.getType()))
      {
         excursions.add(result);
      }
      else if (SearchResultType.ATTRACTION.equals(result.getType()))
      {
         verifySightAndEvent(sights, events, result);
      }
   }

   /**
    * @param sights
    * @param events
    * @param result
    */
   private void verifySightAndEvent(final List<ResultData> sights, final List<ResultData> events, final ResultData result)
   {
      if ("SIGHT".equals(result.getSearchResultSubtype()))
      {
         sights.add(result);
      }
      else if ("EVENT".equals(result.getSearchResultSubtype()))
      {
         events.add(result);
      }
      else
      {
         // If it is any other type such as 'OTHER' treat it as a
         // sight.
         sights.add(result);
      }
   }

   /**
    * @param accommodations
    * @param hotels
    * @param villas
    * @param result
    * @param brandDetails
    */
   private void verifyBrandAndSetAccomType(final List<ResultData> accommodations, final List<ResultData> hotels,
         final List<ResultData> villas, final ResultData result, final BrandDetails brandDetails)
   {
      if ("thomson".equals(brandDetails.getSiteName()))
      {
         if (AccommodationType.VILLA.toString().equalsIgnoreCase(result.getAccommodationType()))
         {
            villas.add(result);
         }
         else
         {
            hotels.add(result);
         }
      }
      else if ("firstchoice".equals(brandDetails.getSiteName()))
      {
         accommodations.add(result);
      }
      else if ("falcon".equals(brandDetails.getSiteName()))
      {
         hotels.add(result);
      }
      else if ("cruise".equals(brandDetails.getSiteName()))
      {
         hotels.add(result);
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getCrossSellCarouselData(java .lang.String, java.lang.String,
    * java.lang.String, uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel)
    */
   @Override
   public SearchResultData getCrossSellCarouselData(final String locationCode, final String productCode, final String pageType,
         final String seoType, final CrossSellCarouselComponentModel item)
   {
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequest = new SearchRequestData(locationCode, pageType, seoType, item);
      searchRequest.setProductCode(productCode);
      searchRequest.setTabbedResult(true);
      return finder.search(searchRequest);

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getVillaAvailability(
    * uk.co.portaltech.tui.components.model.VillaAvailabilityComponentModel, VillaAvailabilityRequest, DateTime)
    */
   @Override
   public VillaAvailabilityResponse getVillaAvailability(final VillaAvailabilityComponentModel villaAvailabilityModel,
         final VillaAvailabilityRequest villaAvailabilityRequest)
   {

      final ProductFinder finder = finders.get(LOG_MSG + villaAvailabilityModel.getLookupType());
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + villaAvailabilityModel.getLookupType() + LOG_MSG_TWO);
      }

      // 1) Setup Endeca request parameters
      final VillaAvailabilitySearchRequestData searchRequest = createEndecaQuery(villaAvailabilityRequest.getCode(),
            villaAvailabilityModel, villaAvailabilityRequest);

      // 2) get Endeca response
      final SearchResultData<ResultData> villaAvailability = (SearchResultData<ResultData>) finder.search(searchRequest);

      // 3) sort and arrive at first available date...as Endeca component
      // integration is pending
      return villaAvailabilityHelper.createResponse(villaAvailabilityRequest, searchRequest, villaAvailability);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getFirstAvailableDateForVilla
    * (uk.co.portaltech.tui.components.model. LatestVillaAvailabilityDateComponentModel)
    */
   @Override
   public DateTime getFirstAvailableDateForVilla(
         final LatestVillaAvailabilityDateComponentModel latestVillaAvailabilityComponentModel)
   {
      final ProductFinder finder = finders.get(LOG_MSG + latestVillaAvailabilityComponentModel.getLookupType());
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + latestVillaAvailabilityComponentModel.getLookupType() + LOG_MSG_TWO);
      }
      return new DateTime();
   }

   /**
    * @param productCode
    * @param villaAvailabilityModel
    * @param villaAvailabilityRequest
    */
   private VillaAvailabilitySearchRequestData createEndecaQuery(final String productCode,
         final VillaAvailabilityComponentModel villaAvailabilityModel, final VillaAvailabilityRequest villaAvailabilityRequest)
   {

      final VillaAvailabilitySearchRequestData searchRequest = new VillaAvailabilitySearchRequestData();
      final int adults = updateAdultCount(villaAvailabilityModel, villaAvailabilityRequest);

      final int children = updateChildrenCount(villaAvailabilityModel, villaAvailabilityRequest);
      final DateTime start = villaAvailabilityRequest.getStartDate();
      searchRequest.setProductCode(productCode);
      if (villaAvailabilityHelper.scrollingBack(villaAvailabilityRequest.getDirection()))
      {
         searchRequest.setStartDate(DateUtils.formatForDb(start.minusDays(CommonwebitemsConstants.FORTY_NINE)));
         searchRequest.setEndDate(DateUtils.formatForDb(start));
      }
      else
      {
         searchRequest.setStartDate(DateUtils.formatForDb(start));
         searchRequest.setEndDate(DateUtils.formatForDb(start.plusDays(CommonwebitemsConstants.FORTY_NINE)));
      }
      searchRequest.setStay(CommonwebitemsConstants.SEVEN);
      searchRequest.setAdults(adults);
      searchRequest.setChildren(children);
      searchRequest.setRelevantItem(villaAvailabilityModel);
      return searchRequest;
   }

   /**
    * @param villaAvailabilityModel
    * @param villaAvailabilityRequest
    * @return childrenCount
    */
   private int updateChildrenCount(final VillaAvailabilityComponentModel villaAvailabilityModel,
         final VillaAvailabilityRequest villaAvailabilityRequest)
   {
      int children = 0;
      if (StringUtils.isNotEmpty(villaAvailabilityRequest.getNoOfChildren()))
      {
         children = Integer.parseInt(villaAvailabilityRequest.getNoOfChildren());
      }
      else
      {
         children = villaAvailabilityModel.getDefaultChildCount().intValue();
      }
      return children;
   }

   /**
    * @param villaAvailabilityModel
    * @param villaAvailabilityRequest
    * @return adultCount
    */
   private int updateAdultCount(final VillaAvailabilityComponentModel villaAvailabilityModel,
         final VillaAvailabilityRequest villaAvailabilityRequest)
   {
      int adults = 0;

      if (StringUtils.isNotEmpty(villaAvailabilityRequest.getNoOfAdults()))
      {
         adults = Integer.parseInt(villaAvailabilityRequest.getNoOfAdults());
      }
      else
      {
         adults = villaAvailabilityModel.getDefaultAdultCount().intValue();
      }
      return adults;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getCrossSellCarouselData(java .lang.String, java.lang.String,
    * java.lang.String, java.lang.String, uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel,
    * java.lang.String)
    */
   @Override
   public SearchResultData getCrossSellCarouselData(final String locationCode, final String productCode, final String pageType,
         final String seoType, final CrossSellCarouselComponentModel item, final String acommodationTypeContext)
   {
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());
      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequest = new SearchRequestData(locationCode, pageType, seoType, item);
      searchRequest.setProductCode(productCode);
      searchRequest.setTabbedResult(true);
      searchRequest.setAccommodationTypeContext(acommodationTypeContext);
      return finder.search(searchRequest);

   }

   @Override
   public SearchResultData getUpSellCarouselData(final String locationCode, final String productCode, final String pageType,
         final String seoType, final UpsellToHolidayComponentModel item, final String acommodationTypeContext)
   {
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());
      if (finder == null)
      {
         throw new RuntimeException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequest = new SearchRequestData(locationCode, pageType, seoType, item);
      searchRequest.setProductCode(productCode);
      searchRequest.setTabbedResult(true);
      searchRequest.setAccommodationTypeContext(acommodationTypeContext);
      return finder.search(searchRequest);
   }

   /**
    * @param event
    * @param sight
    * @param excursion
    * @param hotel
    * @param villa
    * @param mapDataWrapper
    * @return MapDataWrapper
    *
    *         This method gets the InteractiveMap Wrapper.
    */
   private MapDataWrapper getInteractiveMapWrapper(final List<ResultData> event, final List<ResultData> sight,
         final List<ResultData> excursion, final List<ResultData> hotel, final List<ResultData> villa,
         final MapDataWrapper mapDataWrapper)
   {
      final List<ResultData> sights = sight;
      final List<ResultData> events = event;
      final List<ResultData> excursions = excursion;
      final List<ResultData> hotels = hotel;
      final List<ResultData> villas = villa;

      if (!excursions.isEmpty())
      {
         mapDataWrapper.setExcursions(excursionFacade.getExcursionsWithEndecaData(excursions));
      }
      if (!hotels.isEmpty())
      {
         mapDataWrapper.setHotels(accomodationFacade.getAccommodationsWithEndecaData(hotels));
      }
      setAccomDataToMap(mapDataWrapper, villas, sights, events);
      return mapDataWrapper;

   }

   @Override
   public BookFlowAccommodationViewData getPackageForShortlist(final SearchResultsRequestData resultRequestData,
         final String packageid, final String brandType) throws SearchResultsBusinessException
   {
      final long start = System.currentTimeMillis();

      final BookFlowAccommodationViewData viewData = new BookFlowAccommodationViewData();

      if (resultRequestData != null)
      {
         final SearchResultViewData selectedUnit = getHolidayFromResults(resultRequestData, packageid, viewData, brandType);

         viewData.setSearchRequestData(resultRequestData);

         verifyBrandAndPopulateViewData(brandType, viewData, selectedUnit);
         if (selectedUnit == null)
         {
            viewData.setPackageSoldOut(true);
         }

      }
      else
      {
         return null;
      }

      LOG.debug("> Time taken to search Accommodation for shortlist in results : " + (System.currentTimeMillis() - start)
            + " milli sec");

      return viewData;
   }

   /**
    * @param brandType
    * @param viewData
    * @param selectedUnit
    */
   private void verifyBrandAndPopulateViewData(final String brandType, final BookFlowAccommodationViewData viewData,
         final SearchResultViewData selectedUnit)
   {
      if (selectedUnit != null && "FC".equalsIgnoreCase(brandType))
      {
         bookFlowAccommodationPopulator.populate(selectedUnit, viewData);
      }
      else if (selectedUnit != null && (TH.equalsIgnoreCase(brandType) || FJ.equalsIgnoreCase(brandType)))
      {
         thBookFlowAccommodationPopulator.populate(selectedUnit, viewData);

      }
   }

   /**
    * Retrieve the information of particular accommodation on the basis of packageId.
    *
    * @return viewData
    * @param packageid
    * @throws SearchResultsBusinessException
    *
    */

   @Override
   public BookFlowAccommodationViewData getPackageSummaryAccommodationData(final SearchResultsRequestData resultRequestData,
         final String packageid, final String finPos, final String index, final String brandType)
         throws SearchResultsBusinessException
   {
      final BookFlowAccommodationViewData viewData = new BookFlowAccommodationViewData();

      final long start = System.currentTimeMillis();

      if (resultRequestData != null)
      {
         if (viewSelector.checkIsMobile())
         {
            enrichmentService.enrichForHolidayFinder(getHolidayFinderComponent(), resultRequestData);
         }
         else
         {
            enrichmentService.enrich(getSearchPanelComponent(), resultRequestData);
         }
         final SearchResultViewData selectedUnit = getHolidayFromResults(resultRequestData, packageid, viewData, brandType);

         if (CollectionUtils.isNotEmpty(resultRequestData.getPrimaryUnits()) || resultRequestData.isAnyWhereSearch())
         {
            // overriding units because we would loose the user's search
            // critera and the same time we should do single accom search in
            // book flow acoom details page

            resultRequestData.setUnits(resultRequestData.getPrimaryUnits());
            resultRequestData.setSingleAccomSearch(false);
         }

         final SearchResultsRequestData resultRequestDataInSession = (SearchResultsRequestData) sessionService
               .getAttribute(LATESTCRITERIA);
         if (resultRequestDataInSession != null)
         {
            if (resultRequestDataInSession.getShortlistOffset() > 0 && resultRequestDataInSession.getShortlistFirst() > 0)
            {
               resultRequestData.setShortlistFirst(resultRequestDataInSession.getShortlistFirst());
               resultRequestData.setShortlistOffset(resultRequestDataInSession.getShortlistOffset());
            }
            else
            {
               if (resultRequestDataInSession.getFirst() < 1)
               {
                  resultRequestDataInSession.setFirst(NUMBER_ONE);
               }
               resultRequestData.setShortlistFirst(resultRequestDataInSession.getFirst());
               resultRequestData.setShortlistOffset(resultRequestDataInSession.getOffset()
                     * resultRequestDataInSession.getFirst());

            }
            resultRequestData.setFilters(resultRequestDataInSession.getFilters());
            resultRequestData.setDuration(resultRequestDataInSession.getDuration());
            resultRequestData.setSearchRequestType(resultRequestDataInSession.getSearchRequestType());
         }
         sessionService.setAttribute(LATESTCRITERIA, resultRequestData);
         // this is purposely set
         // here after overriding
         // units in above line.
         // Couple of things to
         // remember here.. one put
         // search criteria into
         // session only after
         // enrichment and second do
         // not put criteria into
         // session when you do
         // pagination/sort.

         if (StringUtils.isNotEmpty(resultRequestData.getDepartureDate()))
         {
            resultRequestData.setWhen(resultRequestData.getDepartureDate());
         }

         viewData.setSearchRequestData(resultRequestData);

         verifyBrandAndPopulateViewData(brandType, viewData, selectedUnit);

         if (sessionService.getAttribute(HOLIDAY_INDEX) != null && selectedUnit != null)
         {

            final Map<String, Integer> holidayIndex = (Map<String, Integer>) sessionService.getAttribute(HOLIDAY_INDEX);
            final Integer origPosVal = holidayIndex.get(selectedUnit.getPackageId()) != null ? holidayIndex.get(selectedUnit
                  .getPackageId()) : Integer.valueOf(0);
            viewData.setOrigPos(origPosVal.toString());

            if (StringUtils.isNotEmpty(finPos))
            {
               viewData.setFinPos(finPos);
            }
            if ("FC".equalsIgnoreCase(brandType))
            {

               if (viewSelector.checkIsMobile())
               {
                  viewData.setNextPageUrl(flightOptionRequestBuilder.builder(viewData, getHolidayFinderComponent(),
                        resultRequestData));
               }
               else
               {
                  viewData.setNextPageUrl(flightOptionRequestBuilder.builder(viewData, getSearchPanelComponent(),
                        resultRequestData));
               }
            }
            else if (TH.equalsIgnoreCase(brandType) || FJ.equalsIgnoreCase(brandType))
            {
               if (resultRequestData.isSingleAccomSearch() && !resultRequestData.isAccomDetails())
               {
                  // Add Iscape travel option page URl
                  if (viewSelector.checkIsMobile())
                  {
                     viewData.setNextPageUrl(holidayOptionsPageUrl.builder(viewData, getHolidayFinderComponent(),
                           resultRequestData));
                  }
                  else
                  {
                     viewData.setNextPageUrl(holidayOptionsPageUrl
                           .builder(viewData, getSearchPanelComponent(), resultRequestData));
                  }
               }
               else
               {
                  viewData.setNextPageUrl(bookFlowFlightOptionUrlBuilder.builder(viewData, getSearchPanelComponent()));
               }
            }
         }
         else
         {
            viewData.setPackageSoldOut(true);
         }

         viewData.setAniteSwitch(isAniteInventory(resultRequestData.getDepartureDate()));
         setShortlist(brandType, viewData);
      }
      LOG.debug("> Time taken to search Accommodation in results : " + (System.currentTimeMillis() - start) + " milli sec");

      final String siteName = cmsSiteService.getCurrentSite().getName();
      viewData.setSiteName(siteName);
      return viewData;
   }

   /**
    * @param brandType
    * @param viewData
    */
   private void setShortlist(final String brandType, final BookFlowAccommodationViewData viewData)
   {
      if (!StringUtils.equalsIgnoreCase("FJ", brandType))
      {
         viewData.setShortlistEnabled(Boolean.valueOf(
               tuiConfigService.getConfigValue(String.format(ENABLE_SHORTLIST_KEY, brandType))).booleanValue());
      }
   }

   /**
    * @param departureDate
    */
   private boolean isAniteInventory(final String departureDate)
   {
      final String siteBrand = tuiUtilityService.getSiteBrand();
      final String aniteStartDate = Config.getString(siteBrand + CommonwebitemsConstants.DOT
            + CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015");

      if (StringUtils.isNotBlank(departureDate) && DateUtils.isEqualOrAfter(departureDate, aniteStartDate))
      {
         return true;
      }
      return false;
   }

   /**
    * @param resultRequestData
    * @param packageid
    * @param viewData
    * @throws SearchResultsBusinessException
    */
   private SearchResultViewData getHolidayFromResults(final SearchResultsRequestData resultRequestData, final String packageid,
         final BookFlowAccommodationViewData viewData, final String siteBrandId) throws SearchResultsBusinessException
   {

      final List<Holiday> holidays = new ArrayList<Holiday>();
      SearchResultViewData selectedUnit = null;
      boolean holidayPackageFlag = false;
      // Keep search Type empty and offset value high to get all units as
      // we cannot apply any paging here

      final SearchResultsViewData searchResultsViewData = new SearchResultsViewData();
      EndecaSearchResult searchResult = null;
      if (facetSearchResultsInSession() && StringUtils.isBlank(resultRequestData.getSelectedBoardBasis()))
      {
         searchResult = (EndecaSearchResult) sessionService.getAttribute(SEARCHRESULTS);
      }
      else
      {
         searchResult = getPackagesFromEndecaCache(resultRequestData);
      }

      holidayPackageFlag = checkHolidayPackageFromSearchResults(packageid, searchResult);
      if (!holidayPackageFlag)
      {
         searchResult = getPackagesFromEndecaCache(resultRequestData);
         sessionService.setAttribute(SEARCHRESULTS, searchResult);
      }
      // Checking Phoenix Package Id or Iscape Package Id
      if (!resultRequestData.isIscapeRequest())
      {
         LOG.debug("> Phoenix Accommodation Search...... packageid -:" + packageid);

         for (final Holiday holiday : searchResult.getHolidays())
         {
            if (StringUtils.equals(packageid, holiday.getPackageId()))
            {
               holidays.add(holiday);
               /*
                * Below piece of code would put the selected holiday into session for the flight options page to use.
                */
               sessionService.setAttribute("selectedHoliday", holiday);
               break;
            }
         }
         populateResultViewData(resultRequestData, holidays, searchResultsViewData, siteBrandId);
         viewData.setEndecaSearchResultCount(searchResult.getHolidays().size());
         selectedUnit = CollectionUtils.isNotEmpty(searchResultsViewData.getHolidays()) ? searchResultsViewData.getHolidays()
               .get(0) : null;

      }
      else
      {
         LOG.debug("> Iscape Accommodation Search........ packageid -:" + packageid);

         populateResultViewData(resultRequestData, searchResult.getHolidays(), searchResultsViewData, siteBrandId);

         for (final SearchResultViewData sViewData : searchResultsViewData.getHolidays())
         {

            if (packageid.equalsIgnoreCase(PackageIdGenerator.generateIscapePackageId(sViewData)))
            {
               selectedUnit = sViewData;
               break;
            }
         }
         viewData.setEndecaSearchResultCount(searchResult.getHolidays().size());
      }

      return selectedUnit;
   }

   /**
    * @param packageid
    * @param searchResult
    * @throws SearchResultsBusinessException
    */
   @SuppressWarnings(BOXING)
   private boolean checkHolidayPackageFromSearchResults(final String packageid, final EndecaSearchResult searchResult)
         throws SearchResultsBusinessException
   {
      boolean holidayPackageFlag = false;
      for (final Holiday holiday : searchResult.getHolidays())
      {
         if (StringUtils.equals(packageid, holiday.getPackageId()))
         {
            holidayPackageFlag = true;
            break;
         }
      }
      return holidayPackageFlag;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getQuickSearchResultData(java .lang.String)
    */
   // Commented as part of performance fix.
   // @Cacheable(cacheName = ENDECA_VIEW_DATA_CACHE, keyGenerator =
   // @KeyGenerator(name = KEY_GENERATOR))
   @SuppressWarnings(BOXING)
   @Override
   public SearchResultsViewData getHolidayPackagesResultData(final SearchResultsRequestData searchParameter,
         final String siteBrand) throws SearchResultsBusinessException
   {

      final EndecaSearchResult searchResult = getPackagesFromEndecaCache(searchParameter);

      // When user does "sorting" on basic search
      // Paginate Results
      // Removing previously searched facetResults from user session when
      // again doing main Search
      sessionService.removeAttribute(SEARCHRESULTS);

      final SearchResultsViewData searchResultsViewData = new SearchResultsViewData();

      setEndecaResultsCount(searchResult, searchResultsViewData);

      populateSearchResultViewData(searchParameter, searchResult, searchResultsViewData);

      return populateResultViewData(searchParameter, searchResult.getHolidays(), searchResultsViewData, siteBrand);
   }

   @SuppressWarnings("boxing")
   /**
    * Added for for analytics - For search filters - when endeca returns new results for filters
    * those new results has to be appended to the exiting Holiday_index session attribute to get the correct original position
    * in case of analytics
    * @param searchParameter
    * @param searchResult
    */
   private void setHolidayIndexFromSearchResult(final EndecaSearchResult searchResult)
   {

      Map<String, Integer> holidayIndex = new HashMap<String, Integer>();
      if (sessionService.getAttribute(HOLIDAY_INDEX) == null)
      {
         // this will get excuted only first time of Endeca call
         holidayIndex.put(null, Integer.valueOf(0));
      }
      else
      {
         holidayIndex = new HashMap<String, Integer>((Map<String, Integer>) sessionService.getAttribute(HOLIDAY_INDEX));
      }
      if (!(holidayIndex.isEmpty()))
      {

         int index = Collections.max(holidayIndex.values()).intValue();
         for (final Holiday holiday : searchResult.getHolidays())
         {

            if (!holidayIndex.containsKey(holiday.getPackageId()))
            {
               index++;
               if (holidayIndex.get(holiday.getPackageId()) == null)
               {
                  holidayIndex.put(holiday.getPackageId(), Integer.valueOf(index));
               }
            }
         }
         sessionService.setAttribute(HOLIDAY_INDEX, holidayIndex);
      }

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getQuickSearchResultData(java .lang.String)
    */
   // Commented as part of performance fix.
   // @Cacheable(cacheName = ENDECA_VIEW_DATA_CACHE, keyGenerator =
   // @KeyGenerator(name = KEY_GENERATOR))
   @Override
   public SearchResultsViewData getHolidayPackagesResultDataForSingleAccom(final SearchResultsRequestData searchParameter,
         final String siteBrand) throws SearchResultsBusinessException
   {

      final EndecaSearchResult searchResult = getPackagesFromEndecaCache(searchParameter);

      final SearchResultsViewData searchResultsViewData = new SearchResultsViewData();
      if (!isEndecaResultEmpty(searchResult))
      {
         // this is commented bcoz mains
         // search results shouldn't be put in session as its very heavy.
         setEndecaResultsCount(searchResult, searchResultsViewData);
         populateSearchResultViewData(searchParameter, searchResult, searchResultsViewData);

         populateResultViewData(searchParameter, searchResult.getHolidays(), searchResultsViewData, siteBrand);
      }

      return searchResultsViewData;
   }

   /**
    * @param searchResult
    * @param searchResultsViewData
    */
   private void setEndecaResultsCount(final EndecaSearchResult searchResult, final SearchResultsViewData searchResultsViewData)
   {
      if (StringUtils.isNotBlank(searchResult.getTotalNumRecs()))
      {
         searchResultsViewData.setEndecaResultsCount(Integer.parseInt(searchResult.getTotalNumRecs()));
      }
      else
      {
         // this is add beacuse in single accom search we don't get
         // totalnumrecs and UI is dependent on this number.
         searchResultsViewData.setEndecaResultsCount(searchResult.getHolidays().size());
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#
    * getRecommendedHolidayPackagesResultData(uk.co.portaltech.tui.web.view .data.SearchResultsRequestData,
    * java.lang.String)
    */
   @Override
   public SearchResultsViewData getRecommendedHolidayPackagesResultData(final SearchResultsRequestData searchParameter,
         final String siteBrandID) throws SearchResultsBusinessException
   {
      final EndecaSearchResult searchResult = getRecommendedPackagesFromEndecaCache(searchParameter);

      final SearchResultsViewData searchResultsViewData = new SearchResultsViewData();
      if (!isEndecaResultEmpty(searchResult))
      {
         searchResultsViewData.setRecsEngineSearch(true);
         populateResultViewData(searchParameter, searchResult.getHolidays(), searchResultsViewData, siteBrandID);
      }

      return searchResultsViewData;
   }

   /**
    * @param searchParameter
    * @param searchResult
    * @param searchResultsViewData
    */
   private void populateSearchResultViewData(final SearchResultsRequestData searchParameter,
         final EndecaSearchResult searchResult, final SearchResultsViewData searchResultsViewData)
   {

      if (searchResult != null)
      {
         searchResultsViewData.setPrioritizedDuration(searchResult.getPrioritizedDuration());
      }
      // Filters are not required in flight option page so below filter code
      // will be executed only if flight options is false
      if (!searchParameter.isFlightOptions())
      {
         if ((searchParameter.getFilters() != null) && (searchParameter.getFilters().getBudgettotal() != null)
               && StringUtils.isNotEmpty(searchParameter.getFilters().getBudgettotal().getMax())
               && StringUtils.isNotEmpty(searchParameter.getFilters().getBudgettotal().getMaxValue()))
         {
            double max = 0;
            double maxValue = 0;

            max = Double.parseDouble(searchParameter.getFilters().getBudgettotal().getMax());
            maxValue = Double.parseDouble(searchParameter.getFilters().getBudgettotal().getMaxValue());

            searchResult.setRepaintFilter(max == maxValue);
            final double diffBetweenMaxValues = maxValue - max;
            /*
             * Sonar fix: Dodgy - Test for floating point equality Changing this condition from
             * "Math.abs(diffBetweenMaxValues) == NUMBER_HALF" to Math.abs(diffBetweenMaxValues) - NUMBER_HALF <
             * 0.00001. The meaning of the two conditions is the same. Ex: if a==b then a-b=0. or if a==b then
             * a-b<0.00001.
             */
            if (Math.abs(Math.abs(diffBetweenMaxValues) - NUMBER_HALF) < NUMBER_ONE_TENTHOUSANDTH)
            {
               searchResult.setRepaintFilter(true);
            }
         }
         if (viewSelector.checkIsMobile())
         {
            filterPopulator.populateMobileFilterData(searchResult, searchResultsViewData);
         }
         else
         {
            filterPopulator.populate(searchResult, searchResultsViewData);
         }
      }
      if (searchResult != null)
      {
         durationSearchResultsPopulator.populate(searchResult, searchResultsViewData);
      }

      final Integer activeDuration = searchResultsViewData.getDurationSelection().getActiveDuration();
      if (activeDuration == null || activeDuration.intValue() == 0)
      {
         searchResultsViewData.getDurationSelection().setActiveDuration(Integer.valueOf(searchParameter.getDuration()));
      }
      if (viewSelector.checkIsMobile())
      {
         searchResultsViewData.setDuration(searchResult.getDuration());
      }
      if ((searchParameter.isFlexibility()) && (CollectionUtils.isNotEmpty(searchResult.getDateSelectorDates())))
      {

         dateSelectorResultsPopulator.populate(searchResult, searchResultsViewData);

         final SearchResultDateSelectionViewData dateSelectionViewData = searchResultsViewData.getAvailableDates();
         dateSelectionViewData.setMaxValue(searchParameter.getUntil());
         dateSelectionViewData.setMinValue(searchParameter.getWhen());
         dateSelectionViewData.setDepatureDate(StringUtils.isNotBlank(searchParameter.getDepartureDate()) ? searchParameter
               .getDepartureDate() : searchParameter.getWhen());
         dateSelectionViewData.setFlexibleDays(searchParameter.getFlexibleDays());
      }
   }

   /**
    * @param searchParameter
    * @param searchResultsViewData
    * @param searchResults
    * @return SearchResultsViewData
    * @throws SearchResultsBusinessException
    */
   @Override
   public SearchResultsViewData populatePaginatedViewData(final SearchResultsRequestData searchParameter,
         final SearchResultsViewData searchResultsViewData, final EndecaSearchResult searchResults, final String siteBrand)
         throws SearchResultsBusinessException
   {
      populateSearchResultViewData(searchParameter, searchResults, searchResultsViewData);

      return populateResultViewData(searchParameter, searchResults.getHolidays(), searchResultsViewData, siteBrand);
   }

   /**
    * Sort and Paginate on these results
    *
    * @param searchParameter
    * @param searchResult
    */
   @Override
   public void sort(final SearchResultsRequestData searchParameter, final EndecaSearchResult searchResult)
   {

      if (!isEndecaResultEmpty(searchResult) && !searchResult.isSingleAccomSearch()
            && StringUtils.isNotEmpty(searchParameter.getSortBy()))
      {
         // Apply sorting here
         Collections.sort(searchResult.getHolidays(),
               HolidayComparator.getComparator(getHolidayComparatorList(searchParameter.getSortBy())));
      }
   }

   /**
    * DateOnly search is when No Results page is displayed For Which UI display has to show -- if widen Search criteria
    * may yield any result or not
    */
   @Override
   public SearchResultsViewData getDatesOnlySearchResult(
         @SuppressWarnings("unused") final SearchResultsRequestData searchParameter) throws SearchResultsBusinessException
   {

      final EndecaSearchResult endecaSearchResult = getPackagesFromEndecaCache(searchParameter);

      final SearchResultsViewData searchResultsViewData = new SearchResultsViewData();

      populateSearchResultViewData(searchParameter, endecaSearchResult, searchResultsViewData);
      return searchResultsViewData;
   }

   /**
    * This method is used for facet search
    *
    */
   @SuppressWarnings("deprecation")
   @Override
   public SearchResultsViewData getHolidayPackagesResultDataForFacets(
         @SuppressWarnings("unused") final SearchResultsRequestData searchParameter, final String siteBrand)
         throws SearchResultsBusinessException
   {

      SearchResultsViewData searchResultsViewData = new SearchResultsViewData();
      final EndecaSearchResult searchResult = getPackagesFromEndecaCache(searchParameter);

      if (!isEndecaResultEmpty(searchResult))
      {

         // setting search type when facet search is invoked.
         searchResult.setSearchRequestType(searchParameter.getSearchRequestType());

         setHolidayIndexFromSearchResult(searchResult);

         setEndecaResultsCount(searchResult, searchResultsViewData);

         populateSearchResultViewData(searchParameter, searchResult, searchResultsViewData);

         searchResultsViewData = populateResultViewData(searchParameter, searchResult.getHolidays(), searchResultsViewData,
               siteBrand);
      }
      return searchResultsViewData;
   }

   /**
    * This method is used for facet search
    *
    */
   @SuppressWarnings("deprecation")
   @Override
   public SearchResultsViewData getHolidayPackagesResultDataForSingleAccomFacets(
         @SuppressWarnings("unused") final SearchResultsRequestData searchParameter, final String siteBrand)
         throws SearchResultsBusinessException
   {

      SearchResultsViewData searchResultsViewData = new SearchResultsViewData();
      final EndecaSearchResult searchResult = getPackagesFromEndecaCache(searchParameter);

      if (!isEndecaResultEmpty(searchResult))
      {
         setEndecaResultsCount(searchResult, searchResultsViewData);
         populateSearchResultViewData(searchParameter, searchResult, searchResultsViewData);

         searchResultsViewData = populateResultViewData(searchParameter, searchResult.getHolidays(), searchResultsViewData,
               siteBrand);

      }

      return searchResultsViewData;

   }

   /**
    * This method returns search results from Endeca raw cache or from Endeca if the cache is expired for the given
    * search criteria
    *
    * @param searchParameter
    * @return EndecaSearchResult
    * @throws SearchResultsBusinessException
    */
   @Override
   public EndecaSearchResult getPackagesFromEndecaCache(final SearchResultsRequestData searchParameter)
         throws SearchResultsBusinessException
   {
      final ProductFinder finder = finders.get("productFinder_ENDECA_HOLIDAY_PACKAGES");

      if (finder == null)
      {
         LOG.error("No product finder for lookup type productFinder_ENDECA_HOLIDAY_PACKAGES" + LOG_MSG_TWO);
         throw new SearchResultsBusinessException("3002");
      }
      EndecaSearchResult searchResult = null;
      try
      {
         searchResult = finder.search(searchParameter).getSearchResult();
         setHolidayIndexFromSearchResult(searchResult);
      }
      catch (final EndecaSearchException ex)
      {
         throw new SearchResultsBusinessException("3005", ex);
      }

      if (CollectionUtils.isNotEmpty(searchResult.getHolidays()))
      {
         LOG.info("> searchResult Size -: " + searchResult.getHolidays().size());
      }
      return searchResult;
   }

   @Override
   public EndecaSearchResult getRecommendedPackagesFromEndecaCache(final SearchResultsRequestData searchParameter)
         throws SearchResultsBusinessException
   {
      final ProductFinder finder = finders.get("productFinder_ENDECA_HOLIDAY_PACKAGES");

      if (finder == null)
      {
         LOG.error("No product finder for lookup type productFinder_ENDECA_HOLIDAY_PACKAGES" + LOG_MSG_TWO);
         throw new SearchResultsBusinessException("3002");
      }
      EndecaSearchResult searchResult = null;
      try
      {
         searchResult = finder.search(searchParameter).getSearchResult();
         setHolidayIndexFromSearchResult(searchResult);
      }
      catch (final EndecaSearchException ex)
      {
         throw new SearchResultsBusinessException("3005", ex);
      }

      if (CollectionUtils.isNotEmpty(searchResult.getHolidays()))
      {
         LOG.info("> searchResult Size -: " + searchResult.getHolidays().size());
      }
      return searchResult;
   }

   public AccommodationModel getAccommodationModel(final String code)
   {
      return (AccommodationModel) fetchProductModel(code);
   }

   /**
    * This method populates searchResultsViewData
    *
    * @param searchParameter
    * @return SearchResultsViewData
    * @throws SearchResultsBusinessException
    */

   private SearchResultsViewData populateResultViewData(final SearchResultsRequestData searchParameter,
         final List<Holiday> paginatedHolidays, final SearchResultsViewData searchResultsViewData, final String siteBrand)
         throws SearchResultsBusinessException

   {

      final CMSSiteModel currentSiteModel = cmsSiteService.getCurrentSite();
      String brandType = null;

      if (sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS) != null && currentSiteModel != null)
      {
         // first session request

         final BrandDetails brandDetails = sessionService.getCurrentSession().getAttribute("brandDetails");
         brandType = brandDetails.getSiteUid();
      }
      searchResultsViewData.setSiteBrand(siteBrand);
      boolean singleAccomFlag = searchParameter.isSingleAccomSearch();
      final PartyCompositionCriteria partyCompositionCriteria = createPartyCompositionCriteria(searchParameter);
      // By default set Low deposit to Per person
      partyCompositionCriteria.setPerPersonDeposit(true);

      partyCompositionCriteria.setBrandType(siteBrand);
      droolsPriorityProviderService.checkLDPP(partyCompositionCriteria);

      final RoomAvailabilityIndicatorData roomAvailabilityIndicatorModel = new RoomAvailabilityIndicatorData();

      roomAvailabilityIndicatorModel.setChannel(configurationService.getConfiguration().getString("channel", StringUtils.EMPTY));
      roomAvailabilityIndicatorModel.setBrandName(siteBrand);

      if (CollectionUtils.isNotEmpty(paginatedHolidays))
      {
         AccommodationModel accommodationModel = null;

         final Set<DateTime> singleAccomDates = new TreeSet<DateTime>();
         final Map<String, String> wishListMap = new HashMap<String, String>();
         // get aLL unique HOLIDAYS
         for (final Holiday holiday : paginatedHolidays)
         {
            // this is required for flight options page
            if (identifyDuplicateUnits(searchParameter, holiday.getPackageId()))

            {
               final String accomCode = holiday.getAccomodation().getAccomCode();
               if (checkIfOneModelRequired(searchParameter, singleAccomFlag, holiday))
               {
                  accommodationModel = getAccommodationModel(accomCode);
                  singleAccomFlag = false;
               }
               else if (!searchParameter.isSingleAccomSearch())
               {
                  accommodationModel = getAccommodationModel(accomCode);
               }

               if (accommodationModel != null)
               {
                  final SearchResultViewData resultsview = new SearchResultViewData(holiday.getIndex());

                  holidaySearchResultsPopulator.populate(holiday, resultsview);

                  populateFeaturesForAccom(accommodationModel, resultsview);

                  // added to support Anite room types...
                  if (isAniteInventory(searchParameter.getDepartureDate()))
                  {
                     populateAniteRoomData(holiday.getAccomodation().getRoomDetails(), resultsview.getAccommodation().getRooms(),
                           holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate(), holiday
                                 .getInventoryInfo().getSellingCode(), roomAvailabilityIndicatorModel, accommodationModel
                                 .getType().getCode(), holiday.getInventoryInfo().getAtcomId());
                  }
                  else
                  {
                     populateRoomsData(holiday.getAccomodation().getRoomDetails(), resultsview.getAccommodation().getRooms(),
                           holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate(), accommodationModel,
                           roomAvailabilityIndicatorModel, holiday.getInventoryInfo().getAtcomId());
                  }

                  searchResultsAccomPopulator.populate(accommodationModel, resultsview);
                  holiday.setOfficialRating(resultsview.getAccommodation().getRatings().getOfficialRating());

                  if (checkTripAdvisorRating(searchParameter, accommodationModel, resultsview))
                  {
                     resultsview.getAccommodation().getRatings()
                           .setTripAdvisorRating(accommodationModel.getReviewRating().toString());
                  }

                  if (TH.equalsIgnoreCase(brandType) || FJ.equalsIgnoreCase(brandType))
                  {
                     nonCoreProductsService.checkingNonCoreProducts(accommodationModel, resultsview, siteBrand);

                     if (resultsview.getNonCoreProduct() != null)
                     {
                        restrictionForNonCoreItem(resultsview);
                     }
                  }

                  if (FJ.equalsIgnoreCase(brandType))
                  {
                     for (final BrandType accombrandType : accommodationModel.getBrands())
                     {
                        if (StringUtils.equalsIgnoreCase(brandType, accombrandType.getCode()))
                        {
                           resultsview.getAccommodation().getRatings().settRatingCss("falcon");
                           break;
                        }
                        resultsview.getAccommodation().getRatings().settRatingCss("");
                     }
                  }

                  // US7776 - externalInventory - for 3pf changes
                  final boolean outbound = BooleanUtils.toBoolean(holiday.getItinerary().getOutbound().get(0)
                        .isExternalInventory());
                  final boolean inbound = BooleanUtils
                        .toBoolean(holiday.getItinerary().getInbound().get(0).isExternalInventory());

                  if ((!outbound) && (!inbound) && holiday.getDepositAmount() != null)
                  {
                     searchResultLowDepositPopulator.populateLowDeposit(holiday, resultsview, partyCompositionCriteria);
                  }

                  try
                  {

                     if (!searchParameter.isFlightOptions())
                     {
                        bookFlowAccomPageUrlBuilder.build(searchParameter, resultsview, accommodationModel,
                              getSearchPanelComponent());
                     }
                     else
                     {
                        if (viewSelector.checkIsMobile())
                        {
                           holidayOptionsPageUrl.builder(resultsview, getHolidayFinderComponent(), searchParameter);
                        }
                        else
                        {
                           holidayOptionsPageUrl.builder(resultsview, getSearchPanelComponent(), searchParameter);
                        }

                     }
                  }
                  catch (final BookFlowAccommadationException e)
                  {
                     throw new SearchResultsBusinessException("3005", e);
                  }

                  final List<String> offerPriorities = droolsPriorityProviderService.getOfferPriorityList(siteBrand);
                  if (CollectionUtils.isNotEmpty(offerPriorities))
                  {
                     populatePriorityOffer(holiday, resultsview, offerPriorities);
                  }

                  /**
                   * this value is hardcoded for Alcarte package ,as agreed with business, As presently anite doesn't
                   * return any indicator for private Taxi This need to be changed once Anite fixes it
                   */
                  if (StringUtils.equalsIgnoreCase(resultsview.getAccommodation().getDifferentiatedCode(), "ALC"))
                  {
                     populateofferForAlcarte(resultsview);
                  }

                  if (viewSelector.checkIsMobile())
                  {
                     populateGalleryImagesForMobile(accommodationModel, resultsview);
                  }

                  singleAccomDateSelectorPopulation(searchParameter, singleAccomDates, holiday);
                  resultsview.setWishListId(PackageIdGenerator.getIscapePackageId(resultsview, brandType, true));
                  wishListMap.put(resultsview.getWishListId(), resultsview.getPackageId());
                  searchResultsViewData.getHolidays().add(resultsview);
               }

            }
         }
         createWishListMap(wishListMap);
         if (searchParameter.isSingleAccomSearch())
         {
            singleAccomPopulation(searchResultsViewData, singleAccomDates);
            searchResultsViewData.getAvailableDates().setDepatureDate(
                  StringUtils.isNotBlank(searchParameter.getDepartureDate()) ? searchParameter.getDepartureDate()
                        : searchParameter.getWhen());
            if (searchParameter.isFlexibility())
            {
               searchResultsViewData.getAvailableDates().setFlexibleDays(searchParameter.getFlexibleDays());
            }
         }
      }
      storeIntoSession(paginatedHolidays);
      return searchResultsViewData;
   }

   private void storeIntoSession(final List<Holiday> holidays)
   {
      final List<ShortlistHolidayData> shortlistData = new ArrayList<ShortlistHolidayData>();
      final Map hashMap = (Map) sessionService.getAttribute("holidayResult");
      shortlistHolidayPopulator.populate(holidays, shortlistData);
      if (hashMap != null)
      {
         final Map dataMap = new HashMap(hashMap);
         for (final ShortlistHolidayData sData : shortlistData)
         {
            dataMap.put(sData.getPackageId(), sData);
         }
         sessionService.setAttribute("holidayResult", dataMap);
      }
      else
      {
         final Map dataMap = new HashMap();
         for (final ShortlistHolidayData sData : shortlistData)
         {
            dataMap.put(sData.getPackageId(), sData);
         }
         sessionService.setAttribute("holidayResult", dataMap);
      }

   }

   /**
    * Sets gallery images for mobile
    *
    * @param accommodationModel
    * @param resultsview
    */
   private void populateGalleryImagesForMobile(final AccommodationModel accommodationModel, final SearchResultViewData resultsview)
   {
      final ArrayList<MediaViewData> mediaDatalist = new ArrayList<MediaViewData>();
      mediaPopulatorLite.populate(productMediaService.getImageMedias(accommodationModel), mediaDatalist);
      resultsview.getAccommodation().setGalleryImages(mediaDatalist);
   }

   /**
    * @param roomDetails
    * @param rooms
    * @param departureDate
    * @param sellingCode
    * @param roomAvailabilityIndicatorModel
    */
   private void populateAniteRoomData(final List<RoomDetails> roomDetails, final List<SearchResultRoomsData> rooms,
         final DateTime departureDate, final String sellingCode,
         final RoomAvailabilityIndicatorData roomAvailabilityIndicatorModel, final String accommodationType, final String atcomId)
   {

      if (CollectionUtils.isNotEmpty(roomDetails))
      {
         for (final RoomDetails room : roomDetails)
         {

            final SearchResultRoomsData searchResultRoomsData = new SearchResultRoomsData();

            final AniteRoomModel aniteRoom = roomsService.getAniteRoomForCodeAndAccomCode(room.getRoomCode(), sellingCode,
                  catalogUtil.getActiveCatalogVersion());

            setRoomData(searchResultRoomsData, aniteRoom);

            if (room.getOccupancy() != null)
            {
               searchResultRoomsData.getOccupancy().setAdults(room.getOccupancy().getAdults());
               searchResultRoomsData.getOccupancy().setChildren(room.getOccupancy().getChildren());
               searchResultRoomsData.getOccupancy().setInfant(room.getOccupancy().getInfant());
               searchResultRoomsData.getOccupancy().setPaxDetail(room.getOccupancy().getPaxDetail());
            }

            setAvailabilityAndBoardBasis(departureDate, roomAvailabilityIndicatorModel, accommodationType, room,
                  searchResultRoomsData, atcomId);

            rooms.add(searchResultRoomsData);
         }

      }

   }

   /**
    * @param searchResultRoomsData
    * @param aniteRoom
    */
   private void setRoomData(final SearchResultRoomsData searchResultRoomsData, final AniteRoomModel aniteRoom)
   {
      if (aniteRoom != null)
      {
         if (StringUtils.isNotBlank(aniteRoom.getShortRoomTitle()))
         {
            searchResultRoomsData.setRoomType(aniteRoom.getShortRoomTitle());
         }
         else
         {
            searchResultRoomsData.setRoomType("");
         }

         searchResultRoomsData.setRoomTypeGroup("");

      }
      else
      {
         searchResultRoomsData.setRoomType("");
         searchResultRoomsData.setRoomTypeGroup("");
      }
   }

   /**
    * @param departureDate
    * @param roomAvailabilityIndicatorModel
    * @param accommodationType
    * @param room
    */
   private void setAvailabilityAndBoardBasis(final DateTime departureDate,
         final RoomAvailabilityIndicatorData roomAvailabilityIndicatorModel, final String accommodationType,
         final RoomDetails room, final SearchResultRoomsData searchResultRoomsData, final String atcomId)
   {
      roomAvailabilityIndicatorModel.setDaysToDeparture(getDaysToDeparture(departureDate));
      roomAvailabilityIndicatorModel.setInventoryCount(room.getNoOfRooms());
      roomAvailabilityIndicatorModel.setAccomodationType(accommodationType);
      droolsPriorityProviderService.applyLowAvailabilityRules(roomAvailabilityIndicatorModel);

      final String inventoryType = StringUtils.isNotEmpty(atcomId) ? "ATCOM" : "TRACS";

      final boolean limitedAvailabilityThreshold = Boolean.valueOf(
            TUI_CONFIG_SERVICE.getConfigValue(cmsSiteService.getCurrentSite().getUid() + "_" + inventoryType + "_ROOM"
                  + AVAILABLEROOMS_DISPLAY_LAI_THRESHOLD)).booleanValue();

      searchResultRoomsData.setLimitedAvailabilityThreshold(limitedAvailabilityThreshold);
      searchResultRoomsData.setAvailability(roomAvailabilityIndicatorModel.isLowAvailabilityIndicator());
      searchResultRoomsData.setRoomCode(room.getRoomCode());
      searchResultRoomsData.setSellingout(room.getNoOfRooms());
      if (StringUtils.isNotEmpty(room.getBoardBasisCode()))
      {
         final String boardCode = StringUtils.deleteWhitespace(room.getBoardBasisCode());
         searchResultRoomsData.setBoardType(BoardBasis.valueOf(StringUtils.chomp(boardCode, "+")).getValue());
      }
      searchResultRoomsData.setBoardBasisCode(room.getBoardBasisCode());
   }

   /**
    * this value is hardcoded for Alcarte package ,as agreed with business, As presently anite doesn't return any
    * indicator for private Taxi This need to be changed once Anite fixes it
    */
   private void populateofferForAlcarte(final SearchResultViewData resultsview)
   {

      final Offer alOffer = new Offer();
      alOffer.setId("privateTaxi");
      alOffer.setDescription("Private Taxi Transfer");
      resultsview.setOffer(alOffer);

   }

   private Map<String, List<Object>> getFeaturesForAccom(final AccommodationModel accommodationModel)
   {
      final String key = keyGenerator.generate("getFeaturesForAccom", accommodationModel.getCode());
      final CacheWrapper<String, Map<String, List<Object>>> featuresForAccomCache = cacheUtil
            .getAccomFeaturesInSearchCacheWrapper();

      // get from cache
      Map<String, List<Object>> featuresForAccom = featuresForAccomCache.get(key);
      if (MapUtils.isEmpty(featuresForAccom))
      {
         featuresForAccom = featureService.getOptimizedValuesForFeatures(featureDescriptorList, accommodationModel, new Date(),
               brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()));
         if (StringUtils.equalsIgnoreCase("FJ", tuiUtilityService.getSiteBrand()))
         {
            final List<Object> featureValues = featuresForAccom.get(FALCON_ACCOM_USPS);
            if (CollectionUtils.isNotEmpty(featureValues))
            {
               featuresForAccom.put("usps", featureValues);
            }
         }
         // put in cache
         featuresForAccomCache.put(key, featuresForAccom);
      }
      return featuresForAccom;
   }

   /**
    * @param accommodationModel
    * @param resultsview
    */
   private void populateFeaturesForAccom(final AccommodationModel accommodationModel, final SearchResultViewData resultsview)
   {
      resultsview.getAccommodation().putFeatureCodesAndValues(getFeaturesForAccom(accommodationModel));
      // Since this is not working for Falcon, the below code is commented for
      // now. It will be looked into and uncommented later
      customSpecialOfferPromotions(resultsview.getAccommodation());
      modifyFeatureCodeAndValuesForMobile(resultsview.getAccommodation());
   }

   /**
    * This method is used to remove superscript for mobile as django doesnt support <sup></sup> tags
    *
    * @param accomodationViewData
    */
   private void modifyFeatureCodeAndValuesForMobile(final SearchResultAccomodationViewData accomodationViewData)
   {
      final List<Object> featureCodes = new ArrayList<Object>();
      if (accomodationViewData.getFeatureCodesAndValues().get(USPS) != null)
      {
         for (final Object featureCode : accomodationViewData.getFeatureCodesAndValues().get(USPS))
         {
            String updateFeatureCode = "";
            updateFeatureCode = featureCode.toString().replace("<sup>", "");
            updateFeatureCode = updateFeatureCode.replace("</sup>", "");
            featureCodes.add(updateFeatureCode);
         }
         accomodationViewData.getFeatureCodesAndValues().put(USPS, featureCodes);
      }
   }

   @SuppressWarnings("boxing")
   private void customSpecialOfferPromotions(final SearchResultAccomodationViewData accomodationViewData)
   {

      final Map<String, List<Object>> specialOfferMapData = accomodationViewData.getFeatureCodesAndValues();
      final List<String> specialOfferPromotions = new ArrayList<String>();
      if (specialOfferMapData.containsKey("special_offer_promotion"))
      {
         final List<Object> specialOfferPromotion = specialOfferMapData.get("special_offer_promotion");

         // this condition check special offer exists or not

         if (CollectionUtils.isNotEmpty(specialOfferPromotion))
         {
            final String specialOfferPromotionData = (String) specialOfferPromotion.get(0);
            if ("yes".equals(specialOfferPromotionData.toLowerCase().trim()))
            {

               verifyOffers(specialOfferMapData, specialOfferPromotions);

               accomodationViewData.setSpecialOfferPromotions(specialOfferPromotions);
            }
         }
      }
   }

   /**
    * @param specialOfferMapData
    * @param specialOfferPromotions
    */
   private void verifyOffers(final Map<String, List<Object>> specialOfferMapData, final List<String> specialOfferPromotions)
   {
      if (specialOfferMapData.containsKey(SPECIAL_OFFER) && !specialOfferMapData.get(SPECIAL_OFFER).isEmpty())
      {

         final List<Object> specialOfferValue = specialOfferMapData.get(SPECIAL_OFFER);

         final String specialOfferData = (String) specialOfferValue.get(0);

         String[] specialOfferSplitData = StringUtils.splitByWholeSeparator(specialOfferData, "<h");

         specialOfferSplitData = StringUtils.splitByWholeSeparator(specialOfferData, "<h");

         splitOffers(specialOfferSplitData, specialOfferPromotions);

      }
   }

   /**
    * @param specialOfferSplitData
    */
   private void splitOffers(final String[] specialOfferSplitData, final List<String> specialOfferPromotions)
   {

      String specialOfferTitle = null;
      for (final String offer : specialOfferSplitData)
      {
         if (offer.startsWith("><strong>") || offer.startsWith("2><strong>"))
         {
            specialOfferTitle = StringUtils.substringBetween(offer, "<strong>", "</");
            specialOfferPromotions.add(specialOfferTitle);
         }
         else
         {
            specialOfferTitle = StringUtils.substringBetween(offer, ">", "</");
            specialOfferPromotions.add(specialOfferTitle);

         }
      }

   }

   /**
    *
    * @param requestData
    * @param holidayPackageId
    * @return identifyDuplicateUnits
    */
   private boolean identifyDuplicateUnits(final SearchResultsRequestData requestData, final String holidayPackageId)
   {

      return requestData.isFlightOptions() ? !(requestData.isPosibilityOfDuplicateUnit() && StringUtils.equalsIgnoreCase(
            requestData.getPackageId(), holidayPackageId)) : true;
   }

   /**
    * @param searchParameter
    * @param singleAccomFlag
    * @param holiday
    * @return checkIfOneModelRequired
    */
   private boolean checkIfOneModelRequired(final SearchResultsRequestData searchParameter, final boolean singleAccomFlag,
         final Holiday holiday)
   {
      return singleAccomFlag
            && !(searchParameter.isPosibilityOfDuplicateUnit() && removeDuplicateUnits(searchParameter.getPackageId(),
                  holiday.getPackageId()));
   }

   /**
    * @param requestPackageId
    * @param holidayPackageId
    *
    * @return true or false
    */
   private boolean removeDuplicateUnits(final String requestPackageId, final String holidayPackageId)
   {
      return StringUtils.equalsIgnoreCase(requestPackageId, holidayPackageId);
   }

   /**
    * In case of nonCoreProduct we no need to display Trating and trip advisor rating.
    *
    * @param resultViewData
    */
   private void restrictionForNonCoreItem(final SearchResultViewData resultViewData)
   {

      if (resultViewData.getNonCoreProduct().isMultipleStay())
      {
         resultViewData.getAccommodation().getRatings().setOfficialRating(StringUtils.EMPTY);
         resultViewData.getAccommodation().getRatings().setTripAdvisorRating(StringUtils.EMPTY);
      }
      else
      {
         resultViewData.getAccommodation().getRatings().setTripAdvisorRating(StringUtils.EMPTY);
      }

   }

   /**
    * This method is introduced for finding Iscape Package Id and corresponding Phoenix package Id. Single accom search
    * yields duplicates for this mapping.
    *
    * @param wishListMap
    */
   private void createWishListMap(final Map<String, String> wishListMap)
   {
      if (sessionService.getAttribute("wishListMap") != null)
      {
         final Map<String, String> mapFromSession = (Map<String, String>) sessionService.getAttribute("wishListMap");
         wishListMap.putAll(mapFromSession);
      }
      sessionService.setAttribute("wishListMap", wishListMap);
   }

   /**
    * @param searchParameter
    * @param singleAccomDates
    * @param holiday
    */
   private void singleAccomDateSelectorPopulation(final SearchResultsRequestData searchParameter,
         final Collection<DateTime> singleAccomDates, final Holiday holiday)
   {
      final boolean dateSelector = verifyIsDateSelector(searchParameter, holiday);

      if (dateSelector
            && !singleAccomDates.contains(holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate()))
      {
         singleAccomDates.add(holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate());
      }
   }

   /**
    * @param searchParameter
    * @param holiday
    * @return boolean
    */
   private boolean verifyIsDateSelector(final SearchResultsRequestData searchParameter, final Holiday holiday)
   {
      return searchParameter.isSingleAccomSearch() && searchParameter.isFlexibility()
            && (holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate() != null);
   }

   /**
    * @param searchResultsViewData
    * @param singleAccomDates
    */
   private void singleAccomPopulation(final SearchResultsViewData searchResultsViewData, final Set<DateTime> singleAccomDates)
   {
      final SingleAccomSearchResultViewData singleAccomViewData = new SingleAccomSearchResultViewData();
      singleAccomSearchResultsPopulator.populate(searchResultsViewData, singleAccomViewData);
      if (CollectionUtils.isNotEmpty(singleAccomDates))
      {
         searchResultsViewData.getAvailableDates().setMinValue(DateUtils.format(((TreeSet<DateTime>) singleAccomDates).first()));
         searchResultsViewData.getAvailableDates().setMaxValue(DateUtils.format(((TreeSet<DateTime>) singleAccomDates).last()));
         for (final DateTime departureDate : singleAccomDates)
         {
            final String formattedDate = DateUtils.format(departureDate);
            searchResultsViewData.getAvailableDates().getAvailableValues().add(formattedDate);
            if (viewSelector.checkIsMobile())
            {
               final Date formattedDateForMobile = new Date(DateUtils.newDateFormat(departureDate));
               searchResultsViewData.getAvailableDates().getAvailableValuesForMobile().add(formattedDateForMobile);
            }

         }
      }
   }

   /**
    * @param searchParameter
    * @param accommodationModel
    * @param resultsview
    * @return checkTripAdvisorRating
    */
   private boolean checkTripAdvisorRating(final SearchResultsRequestData searchParameter,
         final AccommodationModel accommodationModel, final SearchResultViewData resultsview)
   {
      return searchParameter.isSingleAccomSearch() && resultsview.getAccommodation().getRatings().getTripAdvisorRating() == null
            && accommodationModel.getReviewRating() != null;
   }

   /**
    * Population of room Details and Low Availability Indicator for rooms
    *
    * @param roomDetails
    * @param searchResultRooms
    * @param dateTime
    * @param roomAvailabilityIndicatorModel
    */
   public void populateRoomsData(final List<RoomDetails> roomDetails, final List<SearchResultRoomsData> searchResultRooms,
         final DateTime dateTime, final AccommodationModel accommodationModel,
         final RoomAvailabilityIndicatorData roomAvailabilityIndicatorModel, final String atcomId)
   {

      if (CollectionUtils.isNotEmpty(roomDetails))
      {
         for (final RoomDetails rooms : roomDetails)
         {
            final SearchResultRoomsData searchResultRoomsData = populateRoomDetails(accommodationModel, rooms);

            setAvailabilityAndBoardBasis(dateTime, roomAvailabilityIndicatorModel, accommodationModel.getType().getCode(), rooms,
                  searchResultRoomsData, atcomId);
            populateRoomDetails(accommodationModel, rooms);
            searchResultRooms.add(searchResultRoomsData);
         }

      }

   }

   /**
    * @param accommodationModel
    * @param room
    */
   private SearchResultRoomsData populateRoomDetails(final AccommodationModel accommodationModel, final RoomDetails room)
   {

      final String key = keyGenerator.generate("populateRoomDetails", accommodationModel.getCode(), room.getRoomCode(),
            room.getPricePerPerson());
      final CacheWrapper<String, SearchResultRoomsData> roomDetailsCache = cacheUtil.getSearchResultsRoomsDetailsCacheWrapper();
      // get from cache
      SearchResultRoomsData searchResultRoomsData = roomDetailsCache.get(key);

      if (searchResultRoomsData == null)
      {
         searchResultRoomsData = new SearchResultRoomsData();

         final RoomModel roomModel = roomsService.getRoomForAccommodation(room.getRoomCode(), accommodationModel);

         if (roomModel != null)
         {

            final Map<String, List<Object>> roomsTitle = featureService.getOptimizedValuesForFeatures(Arrays.asList(new String[]
            { ROOM_TITLE }), roomModel, new Date(), null);
            final Map<String, List<Object>> roomsGroup = featureService.getOptimizedValuesForFeatures(Arrays.asList(new String[]
            { TYPE_GROUP }), roomModel, new Date(), null);

            // Added in order to remove junk character like
            // <sup>&#174;</sup>
            String roomTitle = Normalizer.normalize(roomsTitle.get(ROOM_TITLE).get(0).toString(), Normalizer.Form.NFD)
                  .replaceAll("[^\\p{ASCII}]", "");
            if (roomTitle.contains("<sup></sup>"))
            {
               roomTitle = roomTitle.substring(0, roomTitle.indexOf("<sup>"))
                     + roomTitle.substring(roomTitle.indexOf("</sup>") + CommonwebitemsConstants.SIX, roomTitle.length());
            }
            searchResultRoomsData.setRoomType(roomTitle);
            if (roomsGroup.containsKey(TYPE_GROUP) && roomsGroup.get(TYPE_GROUP) != null
                  && CollectionUtils.isNotEmpty(roomsGroup.get(TYPE_GROUP)))
            {
               searchResultRoomsData.setRoomTypeGroup(roomsGroup.get(TYPE_GROUP).get(0).toString());
            }

         }
         else
         {
            searchResultRoomsData.setRoomType("");
            searchResultRoomsData.setRoomTypeGroup("");
         }
         if (room.getOccupancy() != null)
         {
            searchResultRoomsData.getOccupancy().setAdults(room.getOccupancy().getAdults());
            searchResultRoomsData.getOccupancy().setChildren(room.getOccupancy().getChildren());
            searchResultRoomsData.getOccupancy().setInfant(room.getOccupancy().getInfant());
            searchResultRoomsData.getOccupancy().setPaxDetail(room.getOccupancy().getPaxDetail());
         }

         roomDetailsCache.put(key, searchResultRoomsData);
      }
      return searchResultRoomsData;
   }

   /**
    * Get the No of days to Departure
    *
    * @paramdepartureDate
    */
   private int getDaysToDeparture(final DateTime departureDate)
   {

      if (departureDate != null)
      {
         return subtractDates(departureDate, currentdateTime());
      }
      return 0;
   }

   /**
    * @param code
    * @return product
    */
   private ProductModel fetchProductModel(final String code)
   {
      ProductModel product = null;
      try
      {
         product = productService.getProductForCode(code);
         Validate.notNull(product);
      }
      catch (final ModelNotFoundException exc)
      {
         LOG.error("THe Product model with code " + code + " does not exist in PIM.", exc);
      }
      catch (final UnknownIdentifierException e)
      {
         LOG.error("Endeca has product code and PIM doesnt have" + code + " does not exist in PIM.", e);
      }
      return product;
   }

   private PartyCompositionCriteria createPartyCompositionCriteria(final SearchResultsRequestData searchParameter)
   {
      final PartyCompositionCriteria partyCompositionCriteria = new PartyCompositionCriteria();
      // infants are ignored
      partyCompositionCriteria.setNoOfPassengers(searchParameter.getNoOfAdults() + searchParameter.getNoOfSeniors()
            + searchParameter.getNoOfChildren());
      partyCompositionCriteria.setNoOfAdults(searchParameter.getNoOfAdults());
      partyCompositionCriteria.setNoOfChildren(searchParameter.getNoOfChildren() - searchParameter.getInfantCount());
      partyCompositionCriteria.setInfantCount(searchParameter.getInfantCount());
      partyCompositionCriteria.setPerPersonDeposit(true);
      return partyCompositionCriteria;
   }

   /**
    * This method will return the SearchResultViewData in sorted order
    *
    * @param searchParameter
    * @return sorted SearchResultsViewData
    */
   @Override
   public SearchResultsViewData getSortedHolidayPackagesResultData(final SearchResultsRequestData searchParameter,
         final String siteBrandID) throws SearchResultsBusinessException
   {
      final EndecaSearchResult searchResult = getPackagesFromEndecaCache(searchParameter);
      sort(searchParameter, searchResult);
      final SearchResultsViewData searchResultsViewData = new SearchResultsViewData();

      populateSearchResultViewData(searchParameter, searchResult, searchResultsViewData);

      final List<Holiday> paginatedHolidays = pagination.paginateResults(searchResult, searchParameter);

      return populateResultViewData(searchParameter, paginatedHolidays, searchResultsViewData, siteBrandID);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#sortSearchResultsViewData
    * (uk.co.portaltech.tui.web.view.data.SearchResultsViewData,
    * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
    */
   @Override
   public SearchResultsViewData sortSearchResultsViewData(final SearchResultsViewData searchResultViewData,
         final SearchResultsRequestData searchParameter)
   {

      Collections.sort(searchResultViewData.getHolidays(),
            SearchResultViewDataComparator.getComparator(getSearchResultViewDataComparatorList(searchParameter.getSortBy())));
      return searchResultViewData;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getSortParameters(java.lang .String)
    */
   @Override
   public SortParameters[] getSortParameters(final String sortCriteriaCode)
   {
      SortParameters[] sortParameters = null;
      final InlineHeaderComponentModel component = (InlineHeaderComponentModel) getComponent(COMPONENT_ID);

      List<SortParameters> listofParams = null;

      if (null != component)
      {
         for (final SortCriteriaModel sortCriteria : component.getSortCriteria())
         {
            if (sortCriteria.getCode().equalsIgnoreCase(sortCriteriaCode))
            {
               listofParams = sortCriteria.getSortParameters();
               sortParameters = listofParams.toArray(new SortParameters[listofParams.size()]);
               return sortParameters;
            }
         }
      }

      return sortParameters;
   }

   /**
    * This method returns list of Comparators for sorting the SearchResultViewData based on the defined Sort Criteria
    *
    * @param sortCriteriaCode
    * @return List<SearchResultViewDataComparator>
    */

   private List<HolidayComparator> getHolidayComparatorList(final String sortCriteriaCode)
   {

      final List<HolidayComparator> comparators = new ArrayList<HolidayComparator>();

      for (final SortParameters parameter : getSortParameters(sortCriteriaCode))
      {

         comparators.add(HolidayComparator.valueOf(parameter.getCode()));
      }
      return comparators;
   }

   /**
    * This method returns list of Comparators for sorting the SearchResultViewData based on the defined Sort Criteria
    *
    * @param sortCriteriaCode
    * @return List<SearchResultViewDataComparator>
    */

   private List<SearchResultViewDataComparator> getSearchResultViewDataComparatorList(final String sortCriteriaCode)
   {

      final List<SearchResultViewDataComparator> comparators = new ArrayList<SearchResultViewDataComparator>();

      for (final SortParameters parameter : getSortParameters(sortCriteriaCode))
      {

         comparators.add(SearchResultViewDataComparator.valueOf(parameter.getCode()));
      }

      return comparators;
   }

   /**
    * This method returns the AbstractCMSComponent model for the given component id
    *
    * @return AbstractCMSComponentModel
    */
   private AbstractCMSComponentModel getComponent(final String id)
   {
      try
      {
         return cmsComponentService.getAbstractCMSComponent(id);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOG.error("Component not found", e);

         return null;
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#isPackageFound(uk.co.portaltech
    * .tui.web.view.data.SearchResultsViewData, java.lang.String)
    */
   @Override
   public boolean isPackageFound(final SearchResultViewData searchResultViewData, final String packageId)
   {
      return packageId.equals(searchResultViewData.getPackageId());
   }

   private SearchPanelComponentModel getSearchPanelComponent()
   {
      return (SearchPanelComponentModel) getComponent(SEARCH_PANEL_COMPONENT_ID);
   }

   private HolidayFinderComponentModel getHolidayFinderComponent()
   {
      return (HolidayFinderComponentModel) getComponent(HOLIDAY_FINDER_COMPONENT_ID);
   }

   /**
    * This method checks if the Result returned by endeca is empty or not
    *
    * @param endecaSearchResult
    * @return boolean
    */
   private boolean isEndecaResultEmpty(final EndecaSearchResult endecaSearchResult)
   {
      return !((null != endecaSearchResult) && (CollectionUtils.isNotEmpty(endecaSearchResult.getHolidays())));

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getTouristBoardThingsToDoViewData (java.lang.String,
    * java.lang.String, java.lang.String, uk.co.portaltech.tui.components.model.ThingsToDoCarouselModel)
    */
   @Override
   public SearchResultData getTouristBoardThingsToDoViewData(final String locationCode, final String pageType,
         final String seoPageType, final ThingsToDoCarouselModel item)
   {

      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      if (finder == null)
      {
         throw new IllegalArgumentException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      // Generate a SearchRequest object to pass to our ProductFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setRelevantItem(item);
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoPageType);
      return finder.search(searchRequest);
   }

   @Override
   public boolean userCriteriaInSession()
   {
      return sessionService.getAttribute(LATESTCRITERIA) != null;
   }

   @Override
   public boolean facetSearchResultsInSession()
   {
      return sessionService.getAttribute(SEARCHRESULTS) != null;
   }

   /**
    * @param holiday
    * @param target
    * @param offerPriorities
    *           This method populates offer with highest priority to holiday
    */
   private void populatePriorityOffer(final Holiday holiday, final SearchResultViewData target, final List<String> offerPriorities)
   {
      final Offers offers = holiday.getOffer();
      boolean offerFound = false;
      Offer priorityOffer = null;
      for (final String offer : offerPriorities)
      {
         if (verifyCoachTransferOffer(offers, offerFound, offer))
         {
            priorityOffer = new Offer();
            priorityOffer.setId(offer);
            priorityOffer.setDescription(COACH_TRANSFER_DESCRIPTION);
            offerFound = true;
         }
         else if (verifyFreeKidsOffer(offers, offerFound, offer))
         {
            priorityOffer = new Offer();
            priorityOffer.setId(offer);
            setFreeChildDescription(holiday, priorityOffer);
            offerFound = true;
         }
         else if (verifyFreeCarHireOffer(offers, offerFound, offer))
         {
            priorityOffer = new Offer();
            priorityOffer.setId(offer);
            priorityOffer.setDescription(FREE_CAR_HIRE_DESCRIPTION);
            offerFound = true;
         }
      }
      target.setOffer(priorityOffer);
      offerFound = false;
   }

   /**
    * @param offers
    * @param offerFound
    * @param offer
    * @return boolean
    */
   private boolean verifyFreeCarHireOffer(final Offers offers, final boolean offerFound, final String offer)
   {
      return !offerFound && StringUtils.isNotEmpty(offers.getFreeCarHire()) && StringUtils.equals(offers.getFreeCarHire(), "Y")
            && StringUtils.equalsIgnoreCase(offer, FREE_CAR_HIRE);
   }

   /**
    * @param offers
    * @param offerFound
    * @param offer
    * @return boolean
    */
   private boolean verifyFreeKidsOffer(final Offers offers, final boolean offerFound, final String offer)
   {
      return !offerFound && StringUtils.isNotEmpty(offers.getFreeKids()) && StringUtils.equals(offers.getFreeKids(), "Y")
            && StringUtils.equalsIgnoreCase(offer, FREE_KIDS);
   }

   /**
    * @param offers
    * @param offerFound
    * @param offer
    * @return boolean
    */
   private boolean verifyCoachTransferOffer(final Offers offers, final boolean offerFound, final String offer)
   {
      return !offerFound && StringUtils.isNotEmpty(offers.getCoachTransfer())
            && StringUtils.equals(offers.getCoachTransfer(), "Y") && StringUtils.equalsIgnoreCase(offer, COACH_TRANSFER);
   }

   /**
    * @param holiday
    * @param priorityOffer
    */
   private void setFreeChildDescription(final Holiday holiday, final Offer priorityOffer)
   {
      int noOfFreeChildren = 0;
      for (final RoomDetails room : holiday.getAccomodation().getRoomDetails())
      {
         if (StringUtils.equalsIgnoreCase(room.getOffer().getFreeKids(), "Y"))
         {
            noOfFreeChildren++;
            break;
         }
      }
      // Reverting the freechild places change from application.

      if (noOfFreeChildren == 1)
      {
         priorityOffer.setDescription(FREE_CHILD_PLACE);
      }
      else
      {
         priorityOffer.setDescription(StringUtils.EMPTY);
      }
   }

   /**
    * @return the configurationService
    */
   public ConfigurationService getConfigurationService()
   {
      return configurationService;
   }

   /**
    * @param configurationService
    *           the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getFlightAvailability(java. lang.String, java.lang.String,
    * java.lang.String, uk.co.portaltech.tui.components.model.AvailableFlightsComponentModel)
    */
   @Override
   public Map<String, List<FlightAvailabilityViewData>> getFlightAvailability(final String productCode,
         final AvailableFlightsComponentModel item) throws SearchResultsBusinessException
   {

      if (item == null)
      {
         return null;
      }
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());

      final SearchRequestData requestData = new SearchRequestData();
      requestData.setLookUpType(item.getLookupType().getCode());
      requestData.setProductCode(productCode);
      final EndecaSearchResult endecaSearchResult = finder.searchHolidays(requestData);

      final List<Holiday> holidays = endecaSearchResult.getHolidays();

      final Map<DateTime, List<FlightAvailabilityViewData>> flightAvailabilityMap = new TreeMap<DateTime, List<FlightAvailabilityViewData>>();
      final Map<String, List<FlightAvailabilityViewData>> flightAvailability = new LinkedHashMap<String, List<FlightAvailabilityViewData>>();

      AccommodationModel accommodationModel = null;
      boolean flag = true;
      for (final Holiday holiday : holidays)
      {
         if (flag)
         {
            final String accomCode = holiday.getAccomodation().getAccomCode();
            accommodationModel = getAccommodationModel(accomCode);
            flag = false;
         }
         if (accommodationModel != null)
         {
            final SearchResultViewData resultsview = new SearchResultViewData(holiday.getIndex());
            holidaySearchResultsPopulator.populate(holiday, resultsview);
            populateFlightAvailabilityMap(flightAvailabilityMap, resultsview, holiday.getItinerary().getOutbound().get(0)
                  .getSchedule().getDepartureDate());
         }

      }
      convertMapKeysToString(flightAvailabilityMap, flightAvailability);
      return flightAvailability;
   }

   /**
    * @param flightAvailabilityMap
    * @param flightAvailability
    */
   private void convertMapKeysToString(final Map<DateTime, List<FlightAvailabilityViewData>> flightAvailabilityMap,
         final Map<String, List<FlightAvailabilityViewData>> flightAvailability)
   {
      for (final Map.Entry<DateTime, List<FlightAvailabilityViewData>> entry : flightAvailabilityMap.entrySet())
      {
         flightAvailability.put(DateUtils.format(entry.getKey()), entry.getValue());
      }
   }

   /**
    *
    * @param flightAvailabilityMap
    * @param resultsview
    * @param depDate
    */
   private void populateFlightAvailabilityMap(final Map<DateTime, List<FlightAvailabilityViewData>> flightAvailabilityMap,
         final SearchResultViewData resultsview, final DateTime depDate)
   {
      final FlightAvailabilityViewData viewData = new FlightAvailabilityViewData();
      viewData.setDepAirportName(resultsview.getItinerary().getDepartureAirport());
      viewData.setDepAirportCode(resultsview.getItinerary().getOutbounds().get(0).getDepartureAirportCode());
      viewData.setPpPrice(roundingPriceValue(resultsview.getPrice().getPerPerson()));
      // setting child price
      viewData.setChildPrice(roundingPriceValue(resultsview.getPrice().getChildPrice()));
      if (flightAvailabilityMap.containsKey(depDate))
      {
         flightAvailabilityMap.get(depDate).add(viewData);
      }
      else
      {
         final List<FlightAvailabilityViewData> flightAvailabilityList = new ArrayList<FlightAvailabilityViewData>();
         flightAvailabilityList.add(viewData);
         flightAvailabilityMap.put(depDate, flightAvailabilityList);
      }
   }

   private String roundingPriceValue(final String s)
   {
      if (s != null && s.length() > 0)
      {
         final BigDecimal bd = new BigDecimal(s).setScale(0, RoundingMode.HALF_UP);
         return bd.toString();
      }
      else
      {
         return "";
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getAccommodationStandardData (java.lang.String, java.lang.String,
    * java.lang.String, uk.co.portaltech.tui .components.model.AccommodationStandardComponentModel, java.lang.String)
    */
   @Override
   public SearchResultData getAccommodationStandardData(final String locationCode, final String pageType,
         final String seoPageType, final AccommodationStandardComponentModel item)
   {
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());
      if (finder == null)
      {
         throw new RuntimeException(LOG_MSG_ONE + item.getLookupType() + LOG_MSG_TWO);
      }
      // Generate a SearchRequest object to pass to our ProductFinder
      final SearchRequestData searchRequest = new SearchRequestData();
      searchRequest.setCategoryCode(locationCode);
      searchRequest.setRelevantItem(item);
      searchRequest.setPageType(pageType);
      searchRequest.setSeoPageType(seoPageType);
      return finder.search(searchRequest);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getProductLeadPriceData(java .lang.String,
    * uk.co.portaltech.tui.components.model.ProductLeadPriceComponentModel)
    */
   @Override
   public AccommodationViewData getProductLeadPriceData(final String locationCode,
         final ProductLeadPriceComponentModel productLeadPriceComponentModel)
   {
      if (productLeadPriceComponentModel == null)
      {
         return null;
      }
      final ProductFinder productFinder = finders.get(LOG_MSG + productLeadPriceComponentModel.getLookupType());
      if (productFinder == null)
      {
         throw new RuntimeException(LOG_MSG_ONE + productLeadPriceComponentModel.getLookupType() + LOG_MSG_TWO);
      }
      final SearchRequestData searchRequestData = new SearchRequestData();
      searchRequestData.setRelevantItem(productLeadPriceComponentModel);
      searchRequestData.setCategoryCode(locationCode);
      final AccommodationViewData accommodationViewData = accomodationFacade
            .getAccommodationPriceDataFromEndeca((SearchResultData<ResultData>) productFinder.search(searchRequestData));

      if (accommodationViewData.getRoomOccupancy() != null)
      {
         populatePartyComposition(accommodationViewData);
      }

      return accommodationViewData;
   }

   /**
    * @param accommodationViewData
    */
   private void populatePartyComposition(final AccommodationViewData accommodationViewData)
   {
      final String[] roomOcc = accommodationViewData.getRoomOccupancy().split("/");
      final StringBuilder sb = new StringBuilder();
      final int adultCount = Integer.parseInt(StringUtils.substring(roomOcc[0], 0, 1));
      final int childCount = Integer.parseInt(StringUtils.substring(roomOcc[1], 0, 1));
      final int infantCount = Integer.parseInt(StringUtils.substring(roomOcc[TWO], 0, 1));

      sb.append(adultCount);

      if (adultCount == 1)
      {
         sb.append(" adult");
      }
      else
      {
         sb.append(" adults");
      }
      addChildToStringBuffer(sb, childCount);
      addInfantToStringBuilder(sb, infantCount);

      accommodationViewData.setRoomOccupancy(sb.toString());
   }

   private void addHotels(final QuickSearchValue quickSearchValue, final AccommodationViewData accommodationViewData)
   {
      if (!quickSearchValue.isMultiSelect())
      {

         accommodationViewData.setAccommodationType("lap" + "_" + accommodationViewData.getAccommodationType());

      }
   }

   private void addCountry(final QuickSearchValue quickSearchValue, final String quickSearchGroupName,
         final LocationData locationData)
   {
      if (!quickSearchValue.isMultiSelect())
      {
         locationData.setMobileLocationType("lap" + "_" + quickSearchGroupName.toUpperCase());
      }
   }

   private void addDestinations(final QuickSearchValue quickSearchValue, final String quickSearchGroupName,
         final LocationData locationData, final Collection<CategoryModel> parents)
   {
      final String laplandCode = tuiConfigService.getConfigValue("country.lapland");
      if (parents.contains(laplandCode))
      {
         quickSearchValue.setMultiSelect(false);
         locationData.setMobileLocationType("lap" + "_" + quickSearchGroupName.toUpperCase());
      }
   }

   /**
    *
    * @param resultRequestData
    * @return EndecaSearchResult
    * @throws SearchResultsBusinessException
    */
   private EndecaSearchResult searchEndeca(final SearchResultsRequestData resultRequestData)
         throws SearchResultsBusinessException
   {
      if (resultRequestData != null)
      {
         enrichmentService.enrich(getSearchPanelComponent(), resultRequestData);
      }
      return getPackagesFromEndecaCache(resultRequestData);
   }

   /**
    *
    * @return EndecaSearchResult
    */
   private EndecaSearchResult getSessionCachedEndecaResults()
   {
      if (facetSearchResultsInSession())
      {
         return (EndecaSearchResult) sessionService.getAttribute(SEARCHRESULTS);
      }
      return null;
   }

   @Override
   public Holiday getSelectedPackage(final SearchResultsRequestData resultRequestData, final String selectedPackageID)
         throws SearchResultsBusinessException
   {
      final EndecaSearchResult sessionCachedEndecaResult = getSessionCachedEndecaResults();

      Holiday selectedHoliday = null;

      // Check the session cached results for the selected package.
      if (sessionCachedEndecaResult != null && CollectionUtils.isNotEmpty(sessionCachedEndecaResult.getHolidays()))
      {
         selectedHoliday = searchHolidayByPackageID(selectedPackageID, sessionCachedEndecaResult);
      }

      // If session doesnt have the selected holiday, search endeca.
      if (selectedHoliday == null)
      {
         selectedHoliday = searchHolidayByPackageID(selectedPackageID, searchEndeca(resultRequestData));
      }

      return selectedHoliday;
   }

   /**
    * @param selectedPackageID
    * @param sessionCachedEndecaResult
    */
   private Holiday searchHolidayByPackageID(final String selectedPackageID, final EndecaSearchResult sessionCachedEndecaResult)
   {
      for (final Holiday holiday : sessionCachedEndecaResult.getHolidays())
      {
         if (StringUtils.equals(selectedPackageID, holiday.getPackageId()))
         {
            return holiday;
         }
      }
      return null;
   }

   /**
    * This method checks if the holiday is upgraded with alternate board, if yes then set the new BB to session to be
    * used in the bookflow page. else do nothing.
    */
   @Override
   public boolean isBoardBasisUpgrade(final String boardBasisCode)
   {
      return StringUtils.equalsIgnoreCase(boardBasisCode,
            ((Holiday) sessionService.getAttribute(uk.co.tui.book.constants.SessionObjectKeys.SELECTEDHOLIDAY)).getAccomodation()
                  .getRoomDetails().get(0).getBoardBasisCode());
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getRecommendedAccomPriceInfo (java.util.List)
    */
   @Override
   public List<AccommodationViewData> getRecommendedAccomPriceInfo(final List<String> productCodes)
   {
      final ProductFinder productFinder = finders.get("productFinder_ENDECA_GET_PRICE");

      final SearchRequestData requestData = new SearchRequestData();

      requestData.setProductCodes(productCodes);

      return accomodationFacade.getRecommendedAccomPriceInfo(
            (SearchResultData<ResultData>) productFinder.priceForRecSearch(requestData), productCodes);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.SearchFacade#getBackToSearchURL(uk.co.portaltech .tui.web.view .data.
    * SearchResultsRequestData)
    */
   @Override
   public String getBackToSearchURL(final SearchResultsRequestData searchParameter)
   {
      final String backToSearch = backToSearchResultsUrlResolver.resolve(searchParameter);
      sessionService.setAttribute(BACKTOSEARCH, backToSearch);
      return backToSearch;
   }

   public void filterThirdPartyFlights(final EndecaSearchResult searchResult)
   {

      final ListIterator<Holiday> it = searchResult.getHolidays().listIterator();

      while (it.hasNext())
      {
         final Holiday holiday = it.next();
         if (holiday.getItinerary() != null)
         {
            if ((holiday.getItinerary().getOutbound().get(0).isExternalInventory().booleanValue())
                  || (holiday.getItinerary().getInbound().get(0).isExternalInventory().booleanValue()))
            {
               it.remove();
            }
         }
      }

   }

   @Override
   public List<FacilityModel> getFacilityList(final MultiValueFilterComponentModel filterComponent)
   {

      final List<String> list = Collections.emptyList();

      final List<FacilityModel> facilityList = new ArrayList<FacilityModel>();
      if (filterComponent.getFeatures() != null)
      {
         for (final String codes : filterComponent.getFeatures())
         {
            facilityList.add(facilityService.getFacilityForCode(codes, catalogUtil.getActiveCatalogVersion(), list));
         }
      }

      return facilityList;

   }

   /**
    * @param holidayViewData
    * @return durationMap
    */
   @Override
   public Map<String, String> populateDurationMap(final HolidayViewData holidayViewData)
   {
      final List<HowLongDurationModel> durations = durationHowLongService.getDurations(TH);
      final Map<String, String> durationMap = new HashMap<String, String>();
      for (final HowLongDurationModel duration : durations)
      {
         if (Integer.parseInt(duration.getValue()) == (holidayViewData.getSearchRequest().getDuration()))
         {
            durationMap.put("description", duration.getDescription());
            durationMap.put("value", duration.getValue());
            durationMap.put("code", duration.getCode());
            durationMap.put("stay", duration.getStay());
            durationMap.put("range", duration.getDurationRange());
         }
      }
      return durationMap;
   }

   @Override
   public PageResponse getPageResponse()
   {
      final PageRequest pageRequest = new PageRequest();
      final String site = cmsSiteService.getCurrentSite().getUid();
      pageRequest.setPackageType(PackageType.INCLUSIVE);
      pageRequest.setCurrentPage(PageType.ACCOMMODATION_DETAILS);
      if ("TH".equals(site))
      {
         pageRequest.setBrand(Brand.TH);
      }
      else if ("FC".equals(site))
      {
         pageRequest.setBrand(Brand.FC);
      }
      else if ("FJ".equals(site))
      {
         pageRequest.setBrand(Brand.FJ);
      }
      else
      {
         pageRequest.setBrand(Brand.CR);
      }
      return navigationPageResolver.resolveNextPage(pageRequest);
   }

   @Override
   public List<LocationData> getPlacesToStayCarouselDataCruise(final String locationCode, final String pageType,
         final String seoType, final PlacesToStayCarouselModel item)
   {
      if (item == null)
      {
         return Collections.<LocationData> emptyList();
      }
      final ProductFinder finder = finders.get(LOG_MSG + item.getLookupType());
      final List<CategoryModel> cruiseCategories = new ArrayList<CategoryModel>();
      final SearchRequestData searchRequest = new SearchRequestData(locationCode, pageType, seoType, item);

      final LocationModel locationModel = (LocationModel) categoryService.getCategoryForCode(locationCode);

      final LocationData locationData = locationConverter.convert(locationModel);
      defaultLocationConfiguredPopulator.populate(locationModel, locationData, Arrays.asList(LocationOption.SUBCATEGORIES));
      checkCruiseCategories(cruiseCategories, locationData);

      setAccomTypeContext(item, searchRequest);

      addAccomHolidayType(item, cruiseCategories, searchRequest);

      final SearchResultData searchResult = finder.search(searchRequest);
      final List<ResultData> resultDataList = searchResult.getResults();
      final List<LocationData> locationDatas = new ArrayList<LocationData>();

      if (CollectionUtils.isNotEmpty(resultDataList))
      {
         final boolean fetchAccommodation = false;
         final List<ResultData> accommodationsLst = new ArrayList<ResultData>();
         getLocationdata(resultDataList, locationDatas, fetchAccommodation, accommodationsLst);
      }
      return locationDatas;
   }

   /**
    * @param cruiseCategories
    * @param locationData
    */
   private void checkCruiseCategories(final List<CategoryModel> cruiseCategories, final LocationData locationData)
   {
      for (final CategoryData categoryData : locationData.getSubLocations())
      {

         final CategoryModel location = categoryService.getCategoryForCode(categoryData.getCode());
         final LocationModel locationModels = (LocationModel) location;

         if (BrandUtils.isValidBrandCode(location.getBrands(), BrandType.CR.toString())
               && !locationModels.getIsPOC().booleanValue())
         {
            cruiseCategories.add(location);
         }
      }
   }

   /**
    * @param item
    * @param cruiseCategories
    * @param searchRequest
    */
   private void addAccomHolidayType(final PlacesToStayCarouselModel item, final List<CategoryModel> cruiseCategories,
         final SearchRequestData searchRequest)
   {
      if (!CollectionUtils.isNotEmpty(cruiseCategories))
      {
         searchRequest.setAccommodationTypeContext(HOTEL);
      }

      if (StringUtils.isNotBlank(item.getNonCoreHolidayType()))
      {
         searchRequest.setNonCoreHolidayType(item.getNonCoreHolidayType());
      }
   }

}
