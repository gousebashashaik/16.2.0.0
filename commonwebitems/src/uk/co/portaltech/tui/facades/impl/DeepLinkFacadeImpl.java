/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.MultiValueFilterComponentModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.SmerchConfiguration;
import uk.co.portaltech.travel.services.FacilityService;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.converters.FlightOptionsRequestDataConverter;
import uk.co.portaltech.tui.converters.HttpRequestToDeepLinkRequestConverter;
import uk.co.portaltech.tui.converters.SearchResultsRequestDataConverter;
import uk.co.portaltech.tui.converters.SmerchConfigurationToDeepLinkRequestConverter;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.DeepLinkFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.populators.DeepLinkRequestViewDataPopulator;
import uk.co.portaltech.tui.populators.TravelDatesPopulator;
import uk.co.portaltech.tui.services.FilterSectionService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.RouteValidator;
import uk.co.portaltech.tui.utils.SearchCriteriaValidator;
import uk.co.portaltech.tui.utils.ValidationErrorMessage;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.CommonFilterData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;
import uk.co.portaltech.tui.web.view.data.ErrorData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.util.CatalogUtil;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsBusinessException;


/**
 * @author laxmibai.p
 *
 */
public class DeepLinkFacadeImpl implements DeepLinkFacade
{
    /**
     *
     */
    private static final String ON = "ON";

    /**
     *
     */
    private static final String OFF = "OFF";

    /**
     *
     */
    private static final String ROUTE_VALIDATION = "route.validation";

    @Resource
    private HttpRequestToDeepLinkRequestConverter deepLinkRequestConverter;

    @Resource
    private SmerchConfigurationToDeepLinkRequestConverter smerchConfigurationConverter;

    @Resource
    private SearchResultsRequestDataConverter searchResultsRequestDataConverter;

    @Resource
    private FlightOptionsRequestDataConverter flightOptionsRequestDataConverter;

    @Resource(name = "deepLinkRequestViewDataPopulator")
    private DeepLinkRequestViewDataPopulator deepLinkRequestViewDataPopulator;

    @Resource
    private ViewSelector viewSelector;
    @Resource
    private CatalogUtil catalogUtil;
    @Resource
    private FacilityService facilityService;
    @Resource
    private SessionService sessionService;

    @Resource
    private ComponentFacade componentFacade;

    @Resource
    private SearchFacade searchFacade;
    @Resource
    private TuiUtilityService tuiUtilityService;

    @Resource
    private FilterSectionService filterSectionService;

    @Resource(name = "tuiConfigService")
    private TUIConfigService tuiConfigService;


    @Resource
    private CatalogVersionService catalogVersionService;

    private RouteValidator routeValidator;

    private TravelDatesPopulator travelDatesPopulator;

    private static final String MULTI_VALUE_FILTER_UID = "featuresFilter";
    private static final TUILogUtils LOGGER = new TUILogUtils("DeepLinkFacadeImpl");

    private static final String BACKTOSEARCHLATESTCRITERIA = "backlatestCriteria";

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getDeepLinkRequestViewData(uk.co.portaltech.tui.web.view.data.
     * DeepLinkRequestData) This method converts DeepLinkRequestData to DeepLinkRequestViewData
     */
    @Override
    public DeepLinkRequestViewData getDeepLinkRequestViewData(final DeepLinkRequestData deepLinkRequestData,
            final SearchPanelComponentModel seachPanelComp)
    {


        final DeepLinkRequestViewData deepLinkrequestViewData = new DeepLinkRequestViewData();
        final SearchCriteriaValidator validator = new SearchCriteriaValidator();
        validator.validate(deepLinkRequestData, deepLinkrequestViewData.getErrors(), seachPanelComp);

        deepLinkRequestViewDataPopulator.populate(deepLinkRequestData, deepLinkrequestViewData);


        if (!deepLinkRequestData.isAppRequest()
                && StringUtils.equalsIgnoreCase(tuiConfigService.getConfigValue(ROUTE_VALIDATION, OFF), ON)
                && !validator.isRequiredFieldSet())
        {
            routeValidator.validateRoutes(deepLinkRequestData, deepLinkrequestViewData.getErrors());
            travelDatesPopulator.populateTraveleDates(deepLinkRequestData, deepLinkrequestViewData);

        }

        return deepLinkrequestViewData;
    }



    @Override
    public DeepLinkRequestViewData getDeepLinkRequestViewDataForNewSearch(final DeepLinkRequestData deepLinkRequestData,
            final NewSearchPanelComponentModel newSearchPanelComp)
    {


        final DeepLinkRequestViewData deepLinkrequestViewData = new DeepLinkRequestViewData();
        final SearchCriteriaValidator validator = new SearchCriteriaValidator();
        validator.validateForNewSearch(deepLinkRequestData, deepLinkrequestViewData.getErrors(), newSearchPanelComp);

        deepLinkRequestViewDataPopulator.populate(deepLinkRequestData, deepLinkrequestViewData);


        if (!deepLinkRequestData.isAppRequest()
                && StringUtils.equalsIgnoreCase(tuiConfigService.getConfigValue(ROUTE_VALIDATION, OFF), ON)
                && !validator.isRequiredFieldSet())
        {
            routeValidator.validateRoutes(deepLinkRequestData, deepLinkrequestViewData.getErrors());
            travelDatesPopulator.populateTraveleDates(deepLinkRequestData, deepLinkrequestViewData);

        }

        return deepLinkrequestViewData;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getDeepLinkRequestViewDataForSmerch(DeepLinkRequestData,
     * uk.co.portaltech.travel.model.SearchPanelComponentModel)
     */
    @Override
    public DeepLinkRequestViewData getDeepLinkRequestViewDataForSmerch(final DeepLinkRequestData deepLinkRequestData,
            final SearchPanelComponentModel seachPanelComp)
    {
        final DeepLinkRequestViewData deepLinkrequestViewData = new DeepLinkRequestViewData();
        final SearchCriteriaValidator validator = new SearchCriteriaValidator();
        validator.validateForSmerch(deepLinkRequestData, deepLinkrequestViewData.getErrors(), seachPanelComp);
        deepLinkRequestViewDataPopulator.populate(deepLinkRequestData, deepLinkrequestViewData);
        if (!deepLinkRequestData.isAppRequest())
        {
            final RouteValidator rValidator = new RouteValidator();
            if (StringUtils.equalsIgnoreCase(tuiConfigService.getConfigValue(ROUTE_VALIDATION, OFF), ON)
                    && !validator.isRequiredFieldSet())
            {
                rValidator.validateRoutes(deepLinkRequestData, deepLinkrequestViewData.getErrors());
            }
            travelDatesPopulator.populateTraveleDates(deepLinkRequestData, deepLinkrequestViewData);
        }
        else
        {
            travelDatesPopulator.populateTraveleDates(deepLinkRequestData, deepLinkrequestViewData);
        }
        return deepLinkrequestViewData;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getDeepLinkRequestViewDataForSmerch(DeepLinkRequestData,
     * uk.co.portaltech.travel.model.HolidayFinderComponentModel)
     */
    @Override
    public DeepLinkRequestViewData getDeepLinkRequestViewDataForSmerchMobile(final DeepLinkRequestData deepLinkRequestData,
            final HolidayFinderComponentModel holidayFinderComp)
    {
        final DeepLinkRequestViewData deepLinkrequestViewData = new DeepLinkRequestViewData();
        final SearchCriteriaValidator validator = new SearchCriteriaValidator();
        validator.validateForSmerchMobile(deepLinkRequestData, deepLinkrequestViewData.getErrors(), holidayFinderComp);
        deepLinkRequestViewDataPopulator.populate(deepLinkRequestData, deepLinkrequestViewData);
        if (!deepLinkRequestData.isAppRequest())
        {
            final RouteValidator newRouteValidator = new RouteValidator();
            if ((StringUtils.equalsIgnoreCase(tuiConfigService.getConfigValue(ROUTE_VALIDATION, OFF), ON))
                    && (!validator.isRequiredFieldSet()))
            {
                newRouteValidator.validateRoutes(deepLinkRequestData, deepLinkrequestViewData.getErrors());
            }
            travelDatesPopulator.populateTraveleDates(deepLinkRequestData, deepLinkrequestViewData);
        }
        travelDatesPopulator.populateTraveleDates(deepLinkRequestData, deepLinkrequestViewData);
        return deepLinkrequestViewData;
    }

    @Override
    public DeepLinkRequestViewData getDeepLinkRequestViewDataForHolidayFinder(final DeepLinkRequestData deepLinkRequestData,
            final HolidayFinderComponentModel holidayFinderComp)
    {

        final DeepLinkRequestViewData deepLinkrequestViewData = new DeepLinkRequestViewData();
        final SearchCriteriaValidator validator = new SearchCriteriaValidator();
        validator.validateForHolidayFinder(deepLinkRequestData, deepLinkrequestViewData.getErrors(), holidayFinderComp);
        deepLinkRequestViewDataPopulator.populate(deepLinkRequestData, deepLinkrequestViewData);
        if (!deepLinkRequestData.isAppRequest()
                && StringUtils.equalsIgnoreCase(tuiConfigService.getConfigValue(ROUTE_VALIDATION, OFF), ON)
                && !validator.isRequiredFieldSet())
        {

            routeValidator.validateRoutes(deepLinkRequestData, deepLinkrequestViewData.getErrors());
            travelDatesPopulator.populateTraveleDates(deepLinkRequestData, deepLinkrequestViewData);
        }

        return deepLinkrequestViewData;
    }


    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getSearchResultsRequestData(uk.co.portaltech.tui.web.view.data.
     * DeepLinkRequestViewData) This method converts DeepLinkRequestViewData into searchResultsRequestData
     */
    @Override
    public SearchResultsRequestData getSearchResultsRequestData(final DeepLinkRequestViewData deepLinkRequestViewData)
    {
        SearchResultsRequestData searchResultsRequestData = new SearchResultsRequestData();
        searchResultsRequestData = searchResultsRequestDataConverter.convert(deepLinkRequestViewData, searchResultsRequestData);

        return searchResultsRequestData;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getSearchResultsRequestData(uk.co.portaltech.tui.web.view.data.
     * DeepLinkRequestViewData) This method converts DeepLinkRequestViewData into searchResultsRequestData
     */
    @Override
    public SearchResultsRequestData getFlightOptionsDeepLinkViewData(final DeepLinkRequestViewData deepLinkRequestViewData)
    {
        SearchResultsRequestData searchResultsRequestData = new SearchResultsRequestData();
        searchResultsRequestData = flightOptionsRequestDataConverter.convert(deepLinkRequestViewData, searchResultsRequestData);

        return searchResultsRequestData;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getDeepLinkRequestData(javax.servlet.http.HttpServletRequest)
     * This method converts http Request object into DeepLink request Data
     */
    @Override
    public DeepLinkRequestData getDeepLinkRequestData(final HttpServletRequest request)
    {
        DeepLinkRequestData deepLinkRequestData = new DeepLinkRequestData();
        deepLinkRequestData = deepLinkRequestConverter.convert(request, deepLinkRequestData);
        return deepLinkRequestData;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getDeepLinkRequestData(uk.co.portaltech.travel.model.results.
     * SmerchConfiguration)
     */
    @Override
    public DeepLinkRequestData getDeepLinkRequestData(final SmerchConfiguration results)
    {
        DeepLinkRequestData deepLinkRequestData = new DeepLinkRequestData();
        deepLinkRequestData = smerchConfigurationConverter.convert(results, deepLinkRequestData);
        return deepLinkRequestData;
    }

    /**
     * @param errors
     * @return
     */
    @Override
    public boolean tamperData(final List<ErrorData> errors)
    {
        if (CollectionUtils.isNotEmpty(errors))
        {
            for (final ErrorData error : errors)
            {
                if (StringUtils.equalsIgnoreCase(error.getCode(), ValidationErrorMessage.TAMPER_DATA_FOUND.getCode()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the routeValidator
     */
    public RouteValidator getRouteValidator()
    {
        return routeValidator;
    }

    /**
     * @param routeValidator
     *           the routeValidator to set
     */
    public void setRouteValidator(final RouteValidator routeValidator)
    {
        this.routeValidator = routeValidator;
    }

    /**
     * @return the travelDatesPopulator
     */
    public TravelDatesPopulator getTravelDatesPopulator()
    {
        return travelDatesPopulator;
    }

    /**
     * @param travelDatesPopulator
     *           the travelDatesPopulator to set
     */
    public void setTravelDatesPopulator(final TravelDatesPopulator travelDatesPopulator)
    {
        this.travelDatesPopulator = travelDatesPopulator;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.DeepLinkFacade#getDeepLinkRequestViewData(javax.servlet.http.HttpServletRequest,
     * uk.co.portaltech.travel.model.SearchPanelComponentModel)
     */
    @Override
    public Object getSearchResultsViewData(final HttpServletRequest request, final SearchPanelComponentModel searchPanel)
            throws SearchResultsBusinessException
    {

        DeepLinkRequestViewData deepLinkRequestViewData = null;
        final String siteBrand = tuiUtilityService.getSiteBrand();
        final DeepLinkRequestData deepLinkRequestData = getDeepLinkRequestData(request);
        final SearchResultsRequestData searchResultsRequestData;

        if (viewSelector.checkIsMobile())
        {
            deepLinkRequestViewData = getDeepLinkRequestViewDataForHolidayFinder(deepLinkRequestData,
                    componentFacade.getHolidayFinderComponent());
        }

        else
        {
            deepLinkRequestViewData = getDeepLinkRequestViewData(deepLinkRequestData, componentFacade.getNewSearchPanelComponent());

        }

        if (deepLinkRequestViewData != null && CollectionUtils.isNotEmpty(deepLinkRequestViewData.getErrors()))
        {
            deepLinkRequestViewData.setSiteBrand(siteBrand);
            if (tamperData(deepLinkRequestViewData.getErrors()))
            {
                throw new SearchResultsBusinessException("6003");
            }

            return deepLinkRequestViewData;
        }

        searchResultsRequestData = getSearchResultsRequestData(deepLinkRequestViewData);
        searchFacade.getBackToSearchURL(searchResultsRequestData);
        sessionService.setAttribute(BACKTOSEARCHLATESTCRITERIA, searchResultsRequestData);
        final HolidayViewData viewData = componentFacade.getHolidayPackagesResultData(searchResultsRequestData, siteBrand);
        if (!viewSelector.checkIsMobile() && !searchResultsRequestData.isSingleAccomSearch())
        {
            setFeaturePrority(viewData);
        }
        viewData.setSiteBrand(siteBrand);
        filterSectionService.createSearchFilterMap(viewData);
        return viewData;
    }

    private void setFeaturePrority(final HolidayViewData viewData)
    {

        final List<String> featureList = new ArrayList<String>();

        try
        {
            final MultiValueFilterComponentModel filterComponent = (MultiValueFilterComponentModel) componentFacade
                    .getComponent(MULTI_VALUE_FILTER_UID);
            final List<FacilityModel> priorityFeatureList = getFacilityList(filterComponent);
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
            LOGGER.error("Componet with uid" + MULTI_VALUE_FILTER_UID + " not Found", e);
        }




    }

    private List<FacilityModel> getFacilityList(final MultiValueFilterComponentModel filterComponent)
    {

        final List<String> list = Collections.emptyList();

        final List<FacilityModel> facilityList = new ArrayList<FacilityModel>();
        if (filterComponent.getFeatures() != null)
        {
            for (final String codes : filterComponent.getFeatures())
            {
                facilityList.add(facilityService.getFacilityForCode(codes, getActiveCatalogversion(), list));
            }
        }

        return facilityList;

    }

    private CatalogVersionModel getActiveCatalogversion()
    {
        final CatalogModel catalogModel = catalogUtil.getDefaultProductCatalogForCMSSiteId(tuiUtilityService.getSiteBrand());
        return catalogVersionService.getCatalogVersion(catalogModel.getId(), "Online");
    }

}
