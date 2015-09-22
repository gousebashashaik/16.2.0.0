/**
 *
 */
package uk.co.portaltech.tui.flights.facades.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AnalyticsParamsModel;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.flights.facades.AnalyticsFacade;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;

/**
 * @author vijaya.putheti
 *
 */
public class DefaultAnalyticFacade implements AnalyticsFacade
{
   @Resource
   private Populator<Object, Map<String, WebAnalytics>> searchResultsAnalyticsPopulator;

   @Resource
   private TUIConfigService tuiConfigService;

   @Resource
   private Populator<Object, Map<String, WebAnalytics>> foBookFlowAnalyticsPopulator;

   private static final String SEARCH_RESULTS_PAGE_LABEL = "flightSearchResultsPage";

   private static final String NO_RESULTS_PAGE_LABEL = "noResults";

   private static final String ANALYTICS_PARAM = "analyticsparams";

   private static final String FLIGHT_ANALYTICS = "flightOptionsPage";

   private static final String EXTRA_ANALYTICS = "extraOptionsPage";

   private static final String PASSENGERS_ANALYTICS = "passengerDetailsPage";

   private static final String CONFIRMATION_ANALYTICS = "confirmationpage";

   private List<WebAnalytics> createAnalyticsDatas(final List<Object> analyticModel,
      final List<AnalyticsParamsModel> analyticOrders, final String page)
   {
      final List<WebAnalytics> analytics = new ArrayList<WebAnalytics>();
      final Map<String, WebAnalytics> analyticMap = new HashMap<String, WebAnalytics>();
      populateSearchAnalytics(analyticModel, page, analyticMap);

      setupAnalyticsOrder(analyticOrders, analytics, analyticMap);

      if (tuiConfigService.getConfigValue(ANALYTICS_PARAM) != null)
      {
         final String[] analyticParams =
            tuiConfigService.getConfigValue(ANALYTICS_PARAM).split(",");
         for (final String param : analyticParams)
         {
            analytics.add(analyticMap.get(param));
         }

      }

      return analytics;
   }

   /**
    * @param analyticOrders
    * @param analytics
    * @param analyticMap
    */
   private void setupAnalyticsOrder(final List<AnalyticsParamsModel> analyticOrders,
      final List<WebAnalytics> analytics, final Map<String, WebAnalytics> analyticMap)
   {
      if (!analyticOrders.isEmpty())
      {
         for (final AnalyticsParamsModel apm : analyticOrders)
         {
            analytics.add(analyticMap.get(apm.getCode()));
         }
      }
   }

   /**
    * @param analyticModel
    * @param page
    * @param analyticMap
    */
   private void populateSearchAnalytics(final List<Object> analyticModel, final String page,
      final Map<String, WebAnalytics> analyticMap)
   {
      if (CollectionUtils.isNotEmpty(analyticModel))
      {

         populateAnalytics(analyticModel, page, analyticMap);
      }
   }

   /**
    * @param analyticModel
    * @param page
    * @param analyticMap
    */
   private void populateAnalytics(final List<Object> analyticModel, final String page,
      final Map<String, WebAnalytics> analyticMap)
   {
      for (final Object model : analyticModel)
      {
         if (isResultsPage(page))
         {
            searchResultsAnalyticsPopulator.populate(model, analyticMap);
         }
         else if (isFCBookFlowPage(page))
         {
            foBookFlowAnalyticsPopulator.populate(model, analyticMap);
         }

      }
   }

   /**
    * Comparing weather is this is a book flow page or not.
    *
    * @return true if pageuid is from bookflow pages, else false
    */
   private boolean isFCBookFlowPage(final String pageUid)
   {
      return isAnalyticsCheck(pageUid)
         || StringUtils.equalsIgnoreCase(pageUid, PASSENGERS_ANALYTICS)
         || StringUtils.equalsIgnoreCase(pageUid, CONFIRMATION_ANALYTICS);
   }

   private boolean isAnalyticsCheck(final String pageUid)
   {

      return StringUtils.equalsIgnoreCase(pageUid, FLIGHT_ANALYTICS)
         || StringUtils.equalsIgnoreCase(pageUid, EXTRA_ANALYTICS);
   }

   @Override
   public List<WebAnalytics> createAnalyticsData(final List<Object> analyticModel,
      final List<AnalyticsParamsModel> analyticOrders, final String page)
   {
      return createAnalyticsDatas(analyticModel, analyticOrders, page);
   }

   /**
    * @return:String
    */
   private boolean isResultsPage(final String pageUid)
   {
      return StringUtils.equalsIgnoreCase(pageUid, SEARCH_RESULTS_PAGE_LABEL)
         || StringUtils.equalsIgnoreCase(pageUid, NO_RESULTS_PAGE_LABEL);
   }

}
