/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.travel.model.AnalyticsParamsModel;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;

/**
 *
 */
public interface AnalyticsFacade {

    List<WebAnalytics> createAnalyticsData(List<Object> analyticModel,
            List<AnalyticsParamsModel> analyticOrders, String pageId);
}
