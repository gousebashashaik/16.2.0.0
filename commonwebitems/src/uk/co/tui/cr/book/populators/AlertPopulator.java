/**
 *
 */
package uk.co.tui.cr.book.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.constants.PriceCodeConstants;
import uk.co.tui.cr.book.view.data.AlertViewData;

/**
 * @author srikanth.bs
 *
 */
public class AlertPopulator {

    /** AlertViewData Populator . */
    @Resource(name = "crAlertViewDataPopulator")
    private AlertViewDataPopulator alertViewDataPopulator;

    /**
     * Populate TotalCost Alert
     *
     * @param alertViewDataList
     * @param feedBackList
     */
    public List<AlertViewData> populateTotalCostAlert(
            final List<AlertViewData> alertViewDataList,
            final List<Feedback> feedBackList) {
        if (CollectionUtils.isNotEmpty(feedBackList)) {
            for (final Feedback feedback : feedBackList) {
                if (StringUtils.equalsIgnoreCase(feedback.getCode(),
                        PriceCodeConstants.TOTAL_COST)) {
                    final AlertViewData alertViewdata = new AlertViewData();
                    alertViewDataPopulator.populate(feedback, alertViewdata);
                    alertViewDataList.add(alertViewdata);
                    break;
                }
            }
        }
        return alertViewDataList;
    }
}
