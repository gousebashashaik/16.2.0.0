/**
 *
 */
package uk.co.tui.book.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.constants.PriceCodeConstants;
import uk.co.tui.book.view.data.AlertViewData;

/**
 * @author srikanth.bs
 *
 */
public class AlertPopulator
{

   /** AlertViewData Populator . */
   @Resource
   private AlertViewDataPopulator alertViewDataPopulator;

   @Resource(name = "thirdPartyFlightAlertViewDataPopulatorForTui")
   private ThirdPartyFlightAlertViewDataPopulator thirdPartyFlightAlertViewDataPopulator;

   /**
    * Populate TotalCost Alert
    *
    * @param alertViewDataList
    * @param feedBackList
    */
   public List<AlertViewData> populateTotalCostAlert(final List<AlertViewData> alertViewDataList,
      final List<Feedback> feedBackList)
   {
      if (CollectionUtils.isNotEmpty(feedBackList))
      {
         for (final Feedback feedback : feedBackList)
         {
            populateAlertData(alertViewDataList, feedback);
         }
      }
      return alertViewDataList;
   }

   /**
    * @param alertViewDataList
    * @param feedback
    */
   private void populateAlertData(final List<AlertViewData> alertViewDataList,
      final Feedback feedback)
   {
      if (StringUtils.equalsIgnoreCase(feedback.getCode(), PriceCodeConstants.TOTAL_COST))
      {
         final AlertViewData alertViewdata = new AlertViewData();
         alertViewDataPopulator.populate(feedback, alertViewdata);
         alertViewDataList.add(alertViewdata);
      }

      if (StringUtils.equalsIgnoreCase(feedback.getCode(),
         PriceCodeConstants.THIRDPARTY_FLIGHT_COST))
      {
         final AlertViewData alertViewdata = new AlertViewData();
         thirdPartyFlightAlertViewDataPopulator.populate(feedback, alertViewdata);
         alertViewDataList.add(alertViewdata);
      }
   }

}
