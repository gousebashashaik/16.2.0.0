/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vivek.vk
 *
 */
public class AnalyticViewData {

    private List<WebAnalytics> analytics = new ArrayList<WebAnalytics>();

    /**
     * @return the analytics
     */
    public List<WebAnalytics> getAnalytics() {
        return analytics;
    }

    /**
     * @param analytics the analytics to set
     */
    public void setAnalytics(List<WebAnalytics> analytics) {
        this.analytics = analytics;
    }

}
