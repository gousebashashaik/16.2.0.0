/**
 *
 */
package uk.co.portaltech.tui.services;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.portaltech.tui.model.DurationHaulTypeModel;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.tui.exception.DurationPriorityRuleException;
import uk.co.tui.exception.TimebasedCrossFeatRuleException;
import uk.co.tui.services.data.CrossFeaturingRuleInputData;
import uk.co.tui.services.data.CrossFeaturingRuleOutPutData;
import uk.co.tui.services.rule.RuleService;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author gaurav.b
 *
 */
public class RuleEngineFacade implements RuleEngine {

    private static final String DEFAULT = "Default";

    private static final String LH = "LH";

    private static final String SH = "SH";

    private static final String LONGHAUL = "LongHaul";
    private static final String LAPLAND = "Lapland";
    private static final String LAPLAND_DAYTRIP = "DAYTRIP";

    private static final String SHORTHAUL = "ShortHaul";

    private static final String DURATION_ORDER_FOR_PRIORITIZE_COUNTRIES = "DurationForPrioritizeCountries";

    private static final String PRIORITIZE_COUNTRIES = "PrioritizeCountries";

    @Resource
    private DurationHaulTypeService durationHaulTypeService;


    @Resource
    private DroolsPriorityProviderService droolsPriorityProviderService;

    @Resource
    private RuleService ruleService;

    /**
     * This method calls the service method responsible to sets the duration and
     * durationPriorityOrder in the context object.
     */
    @Override
    public void applyDurationPriorityRules(
            final SearchResultsRequestData requestData,
            final HolidaySearchContext context)
            throws DurationPriorityRuleException {
       /**
        * Need to check
        *
        */

    }


    /**
     * This method calls the service method responsible to sets the duration and
     * durationPriorityOrder in the context object.
     */
    @Override
    public void applyDurationPriorityRulesOnRequest(
            final SearchResultsRequestData requestData,
            final HolidaySearchContext context)
            throws DurationPriorityRuleException {

        int longHauls = 0;
        int shortHauls = 0;

        final boolean lapland = searchForLaplandType(requestData, context);

        if (!lapland) {

            final List<String> countries = new ArrayList<String>();

            for (final String airportData : context.getAirports()) {
                for (final UnitData unit : requestData.getUnits()) {
                    final DurationHaulTypeModel durationHaulTypeModel = durationHaulTypeService
                            .getDurationHaulType(airportData, unit.getId(),
                                    unit.getType());
                    if (durationHaulTypeModel != null) {
                        if (StringUtils.equals(SH,
                                durationHaulTypeModel.getHaulType())) {
                            shortHauls++;
                        } else if (StringUtils.equals(LH,
                                durationHaulTypeModel.getHaulType())) {
                            longHauls++;
                        }
                        countries.add(durationHaulTypeModel.getCountry());
                    }
                }
            }

            setDurationPriorityOrder(context, longHauls, shortHauls, countries);

        }
    }

    /**
     * @param requestData
     * @param context
     * @return count
     */
    private boolean searchForLaplandType(
            final SearchResultsRequestData requestData,
            final HolidaySearchContext context) {
        int laplandDayTrip = 0;
        int laplandCollection = 0;
        final List<String> laplandLocation = asList("LAP_COUNTRY",
                "LAP_REGION", "LAP_DESTINATION", "LAP_RESORT", "LAP_HOTEL");
        for (final UnitData unit : requestData.getUnits()) {

            if (StringUtils.equals(LAPLAND_DAYTRIP, unit.getType())) {

                laplandDayTrip++;
            }
            if (laplandLocation.contains(unit.getType())) {

                laplandCollection++;
            }
        }
        if (laplandDayTrip == 0 && laplandCollection == 0) {
            return false;
        } else {
            setDurationPriorityOrderForLapland(context, laplandDayTrip,
                    laplandCollection);
        }

        return true;
    }

    /**
     * @param context
     * @param laplandCollection
     * @param laplandDayTrip
     */
    public void setDurationPriorityOrderForLapland(
            final HolidaySearchContext context, final int laplandDayTrip,
            final int laplandCollection) {

        if (laplandCollection >= 1) {

            final String durOrderForLapland = droolsPriorityProviderService
                    .getDurationPriorityList(LAPLAND);

            if (StringUtils.isNotBlank(durOrderForLapland)) {

                context.setDurationPriorityOrder(durOrderForLapland);
                context.setDuration(Integer.parseInt(StringUtils.substring(
                        durOrderForLapland, 0,
                        StringUtils.indexOf(durOrderForLapland, ","))));

            }

        }
        if (laplandDayTrip >= 1 && laplandCollection == 0) {
            context.setDuration(1);
            context.setDurationPriorityOrder("1");
        }

    }

    /**
     * @param context
     * @param longHauls
     * @param shortHauls
     * @param countries
     */
    private void setDurationPriorityOrder(final HolidaySearchContext context,
            final int longHauls, final int shortHauls,
            final List<String> countries) {

        final String prioritizeCountries = droolsPriorityProviderService
                .getDurationPriorityList(PRIORITIZE_COUNTRIES, context);
        String durOrderForPrioritizeCountries;
        boolean flag = false;
        // Checking for the Countries

        if (StringUtils.isNotBlank(prioritizeCountries)
                && CollectionUtils.containsAny(countries,
                        Arrays.asList(prioritizeCountries.split(",")))) {

            final String durationPriorityOrder = droolsPriorityProviderService
                    .getDurationPriorityList(
                            DURATION_ORDER_FOR_PRIORITIZE_COUNTRIES, context);

            context.setDurationPriorityOrder(durationPriorityOrder);
            context.setDuration(Integer.parseInt(StringUtils.substring(
                    durationPriorityOrder, 0,
                    StringUtils.indexOf(durationPriorityOrder, ","))));
            flag = true;

        } else if (!flag && !(longHauls + shortHauls == 0)) {
            // Long Haul / Short Haul Check
            if (shortHauls > longHauls) {

                durOrderForPrioritizeCountries = droolsPriorityProviderService
                        .getDurationPriorityList(SHORTHAUL, context);

                if (StringUtils.isNotBlank(durOrderForPrioritizeCountries)) {

                    context.setDurationPriorityOrder(durOrderForPrioritizeCountries);
                    context.setDuration(Integer.parseInt(StringUtils.substring(
                            durOrderForPrioritizeCountries, 0, StringUtils
                                    .indexOf(durOrderForPrioritizeCountries,
                                            ","))));

                }

            } else {

                durOrderForPrioritizeCountries = droolsPriorityProviderService
                        .getDurationPriorityList(LONGHAUL, context);

                if (StringUtils.isNotBlank(durOrderForPrioritizeCountries)) {

                    context.setDurationPriorityOrder(durOrderForPrioritizeCountries);
                    context.setDuration(Integer.parseInt(StringUtils.substring(
                            durOrderForPrioritizeCountries, 0, StringUtils
                                    .indexOf(durOrderForPrioritizeCountries,
                                            ","))));

                }

            }

        } else {

            // Default
            final String defaultDurationPriOrder = droolsPriorityProviderService
                    .getDurationPriorityList(DEFAULT, context);
            if (StringUtils.isNotBlank(defaultDurationPriOrder)) {
                context.setDurationPriorityOrder(defaultDurationPriOrder);
                context.setDuration(Integer.parseInt(StringUtils.substring(
                        defaultDurationPriOrder, 0,
                        StringUtils.indexOf(defaultDurationPriOrder, ","))));
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.services.RuleEngine#applyTimeBasedCrossFeaturingRules
     * (java.util.Date, java.util.Date)
     */
    @Override
    public String applyTimeBasedCrossFeaturingRules(final Date startDate,
            final Date endDate, final String brandCode)
            throws TimebasedCrossFeatRuleException {
        final CrossFeaturingRuleInputData data = new CrossFeaturingRuleInputData();
        data.setStartDate(startDate);
        data.setEndDate(endDate);
        data.setBrandCode(brandCode);
        final CrossFeaturingRuleOutPutData outputData = ruleService
                .executeCrossFeaturingRule(data);
        if (outputData.isCrossFeature()) {
            return CommonwebitemsConstants.CROSS_FEATUR_ON;
        }
        return CommonwebitemsConstants.CROSS_FEATUR_OFF;
    }

}
