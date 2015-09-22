package uk.co.portaltech.tui.cr.facades.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AnalyticsParamsModel;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.cr.facades.AnalyticsFacade;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;


/**
 *
 */
public class DefaultAnalyticFacade implements AnalyticsFacade
{

    @Resource
    private Populator<Object, Map<String, WebAnalytics>> searchResultsAnalyticsPopulator;

    @Resource
    private Populator<Object, Map<String, WebAnalytics>> flightOptionsAnalyticsPopulator;

    @Resource
    private Populator<Object, Map<String, WebAnalytics>> accommodationAnalyticsPopulator;


    @Resource
    private Populator<Object, Map<String, WebAnalytics>> crBookFlowAnalyticsPopulator;

    private static final String ACCOMODATION_PAGE = "bookFlowAccommodationOverviewPage";

    private static final String ACCOMODATION_PAGE_MOBILE = "bookFlowAccommodationOverviewPage_Mobile";

    private static final String SEARCH_RESULTS_PAGE_LABEL = "searchresultspage";

    private static final String SEARCH_RESULTS_PAGE_LABEL_MOBILE = "MobileSearchresultspage";

    private static final String SINGLE_ACCOM_RESULTS_PAGE_LABEL = "singleaccomsearchresultspage";

    private static final String NO_RESULTS_PAGE_LABEL = "noresultspage";

    private static final String NO_RESULTS_PAGE_LABEL_MOBILE = "noResultsMobile";

    private static final String FLIGHT_OPTION_PAGE_ANALYTICS = "bookFlowFlightOptionPage";

    private static final String VILLA_ACCOMODATION_PAGE = "bookFlowAccommodationOverviewPage_Villa";

    private static final String CRUISE_ACCOMODATION_PAGE = "bookFlowAccommodationOverviewPage_Cruise";

    private static final String VILLA_ACCOMODATION_PAGE_MOBILE = "bookFlowVillaOverviewPage_Mobile";

    private static final String FLIGHT_ANALYTICS = "flightOptionsPage";

    private static final String ROOM_ANALYTICS = "cruiseOptionsPage";

    private static final String EXTRA_ANALYTICS = "extraOptionsPage";

    private static final String PASSENGERS_ANALYTICS = "passengerDetailsPage";

    private String smerchSession;

    private static final String CONFIRMATION_ANALYTICS = "confirmationpage";

    @Resource
    private SessionService sessionService;

    @Resource
    private TUIConfigService tuiConfigService;

    private static final String ANALYTICS_PARAM = "analyticsparams";

    private static final String SINGLE_ACCOM_RESULTS_PAGE_LABEL_MOBILE = "MobileSingleAccomSearchresultspage";

    private List<WebAnalytics> createAnalyticsDatas(final List<Object> analyticModel,
            final List<AnalyticsParamsModel> analyticOrders, final String page)
    {
        final List<WebAnalytics> analytics = new ArrayList<WebAnalytics>();
        final Map<String, WebAnalytics> analyticMap = new HashMap<String, WebAnalytics>();
        if (CollectionUtils.isNotEmpty(analyticModel))
        {

            for (final Object model : analyticModel)
            {
                if (isResultsPage(page))
                {
                    searchResultsAnalyticsPopulator.populate(model, analyticMap);
                }
                else if (accomodationPageCheck(page) || StringUtils.equalsIgnoreCase(page, VILLA_ACCOMODATION_PAGE_MOBILE)
                        || StringUtils.equalsIgnoreCase(page, CRUISE_ACCOMODATION_PAGE))
                {
                    accommodationAnalyticsPopulator.populate(model, analyticMap);
                }
                else if (StringUtils.equalsIgnoreCase(page, FLIGHT_OPTION_PAGE_ANALYTICS))
                {
                    flightOptionsAnalyticsPopulator.populate(model, analyticMap);
                }

                else if (isFCBookFlowPage(page))
                {
                    crBookFlowAnalyticsPopulator.populate(model, analyticMap);
                }
            }
        }

        if (!analyticOrders.isEmpty())
        {
            for (final AnalyticsParamsModel apm : analyticOrders)
            {
                analytics.add(analyticMap.get(apm.getCode()));
            }
        }

        if (tuiConfigService.getConfigValue(ANALYTICS_PARAM) != null)
        {
            final String[] analyticParams = tuiConfigService.getConfigValue(ANALYTICS_PARAM).split(",");
            for (final String param : analyticParams)
            {
                analytics.add(analyticMap.get(param));
            }

        }
        if (sessionService != null)
        {
            sessionService.removeAttribute("smerchresultspage");
        }
        return analytics;
    }

    private boolean accomodationPageCheck(final String page)
    {
        return StringUtils.equalsIgnoreCase(page, ACCOMODATION_PAGE)
                || StringUtils.equalsIgnoreCase(page, ACCOMODATION_PAGE_MOBILE)
                || StringUtils.equalsIgnoreCase(page, VILLA_ACCOMODATION_PAGE);
    }

    @Override
    public List<WebAnalytics> createAnalyticsData(final List<Object> analyticModel,
            final List<AnalyticsParamsModel> analyticOrders, final String page)
    {
        return createAnalyticsDatas(analyticModel, analyticOrders, page);
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.AnalyticsFacade#createAnalyticsData(java .util.List)
     */
    @Override
    public List<WebAnalytics> createAnalyticsData(final List<Object> analyticModel,
            final List<AnalyticsParamsModel> analyticOrders, final String page, final String userStatus)
    {
        return createAnalyticsDatas(analyticModel, analyticOrders, page);
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.AnalyticsFacade#createCustomerAnalyticsData (java.util.List, java.util.List,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<WebAnalytics> createCustomerAnalyticsData(final List<Object> analyticModel,
            final List<AnalyticsParamsModel> analyticOrders, final String page, final String userStatus)
    {
        final List<WebAnalytics> analytics = new ArrayList<WebAnalytics>();
        final Map<String, WebAnalytics> analyticMap = new HashMap<String, WebAnalytics>();

        if (!analyticOrders.isEmpty())
        {
            for (final AnalyticsParamsModel apm : analyticOrders)
            {
                analytics.add(analyticMap.get(apm.getCode()));
            }
        }
        return analytics;
    }

    /**
     * @return:String
     */
    private boolean isResultsPage(final String pageUid)
    {
        if (sessionService != null)
        {
            smerchSession = sessionService.getAttribute("smerchresultspage");
        }
        return pageLabelCheck(pageUid) || StringUtils.equalsIgnoreCase(pageUid, SEARCH_RESULTS_PAGE_LABEL_MOBILE)
                || StringUtils.equalsIgnoreCase(pageUid, NO_RESULTS_PAGE_LABEL_MOBILE)
                || StringUtils.equalsIgnoreCase(pageUid, SINGLE_ACCOM_RESULTS_PAGE_LABEL_MOBILE)
                || StringUtils.equalsIgnoreCase(smerchSession, "smerchresultspage");
    }

    private boolean pageLabelCheck(final String pageUid)
    {
        return StringUtils.equalsIgnoreCase(pageUid, SEARCH_RESULTS_PAGE_LABEL)
                || StringUtils.equalsIgnoreCase(pageUid, SINGLE_ACCOM_RESULTS_PAGE_LABEL)
                || StringUtils.equalsIgnoreCase(pageUid, NO_RESULTS_PAGE_LABEL);
    }

    /**
     * Comparing weather is this is a book flow page or not.
     *
     * @return true if pageuid is from bookflow pages, else false
     */
    private boolean isFCBookFlowPage(final String pageUid)
    {
        return isAnalyticsCheck(pageUid) || StringUtils.equalsIgnoreCase(pageUid, PASSENGERS_ANALYTICS)
                || StringUtils.equalsIgnoreCase(pageUid, CONFIRMATION_ANALYTICS);
    }

    private boolean isAnalyticsCheck(final String pageUid)
    {

        return StringUtils.equalsIgnoreCase(pageUid, FLIGHT_ANALYTICS) || StringUtils.equalsIgnoreCase(pageUid, ROOM_ANALYTICS)
                || StringUtils.equalsIgnoreCase(pageUid, EXTRA_ANALYTICS);
    }
}
