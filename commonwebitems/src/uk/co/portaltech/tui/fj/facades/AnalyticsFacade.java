/**
 *
 */
package uk.co.portaltech.tui.fj.facades;

import java.util.List;

import uk.co.portaltech.travel.model.AnalyticsParamsModel;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;

/**
 *
 */
public interface AnalyticsFacade {

    List<WebAnalytics> createAnalyticsData(List<Object> analyticModel,
            List<AnalyticsParamsModel> analyticOrders, String pageId);

    List<WebAnalytics> createAnalyticsData(List<Object> analyticModel,
            List<AnalyticsParamsModel> analyticOrders, String pageId,
            String userStatus);

    List<WebAnalytics> createCustomerAnalyticsData(List<Object> analyticModel,
            List<AnalyticsParamsModel> analyticOrders, String pageId,
            String userStatus);

}
