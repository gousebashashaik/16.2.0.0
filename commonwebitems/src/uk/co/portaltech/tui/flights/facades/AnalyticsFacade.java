/**
 *
 */
package uk.co.portaltech.tui.flights.facades;

import java.util.List;

import uk.co.portaltech.travel.model.AnalyticsParamsModel;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;


/**
 * @author vijaya.putheti
 *
 */
public interface AnalyticsFacade
{
   List<WebAnalytics> createAnalyticsData(List<Object> analyticModel, List<AnalyticsParamsModel> analyticOrders, String pageId);

}
