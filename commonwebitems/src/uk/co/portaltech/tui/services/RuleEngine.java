/**
 *
 */
package uk.co.portaltech.tui.services;

import java.util.Date;

import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.exception.DurationPriorityRuleException;
import uk.co.tui.exception.TimebasedCrossFeatRuleException;

/**
 * @author gaurav.b
 *
 */
public interface RuleEngine {

    /**
     *
     * @param context
     *            the HolidaySearchContext
     * @param requestData
     *            the SearchResultsRequestData
     */
    void applyDurationPriorityRules(SearchResultsRequestData requestData, HolidaySearchContext context);

    void applyDurationPriorityRulesOnRequest(SearchResultsRequestData requestData, HolidaySearchContext context) throws DurationPriorityRuleException;

    /**
     * This method executes time based cross featuring rules and return cross feature on or off string.
     * @param startDate
     * @param endDate
     * @Param brandCode TH/FC
     * @return searchDoNotCrossFeature/searchCrossFeature
     * @throws TimebasedCrossFeatRuleException
     */
    String applyTimeBasedCrossFeaturingRules(Date startDate, Date endDate, String brandCode) throws TimebasedCrossFeatRuleException;

}
