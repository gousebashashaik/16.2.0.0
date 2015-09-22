/**
 *
 */
package uk.co.portaltech.tui.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateRangeProviderUtil;
import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.services.lowdeposit.PartyCompositionCriteria;
import uk.co.portaltech.travel.services.lowdeposit.VisionBrochureCriteria;
import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.tui.services.data.CreditCardRuleInputData;
import uk.co.tui.services.data.CreditCardRuleOutputData;
import uk.co.tui.services.data.DPRuleInputData;
import uk.co.tui.services.data.DPRuleOutputData;
import uk.co.tui.services.data.OPRuleInputData;
import uk.co.tui.services.data.OPRuleOutputData;
import uk.co.tui.services.data.RoomAvailabilityIndicatorData;
import uk.co.tui.services.data.WorldCareFundRuleInputData;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;
import uk.co.tui.services.rule.RuleService;

/**
 * @author laxmibai.p
 *
 */
public class DroolsPriorityProviderServiceImpl implements DroolsPriorityProviderService
{

   @Resource
   private RuleService ruleService;

   /**
    * @return Returns offer priority list from drools
    */
   @Override
   public List<String> getOfferPriorityList(final String brandType)
   {
      final OPRuleInputData data = new OPRuleInputData();
      data.setOffer("Default");
      data.setBrandType(brandType);
      final OPRuleOutputData output = ruleService.executeOfferPriorityRule(data);
      if (output != null && StringUtils.isNotEmpty(output.getPriorityList()))
      {
         return Arrays.asList(output.getPriorityList().split(","));
      }
      return Collections.emptyList();
   }

   @Override
   public CreditCardRuleOutputData getCreditCardCharges()
   {
      final CreditCardRuleInputData data = new CreditCardRuleInputData();
      data.setPercentage("CreditCardPercentage");
      data.setCapAmount("CreditCardCapAmount");
      return ruleService.executeCreditCardRule(data);
   }

   @Override
   public WorldCareFundRuleOutputData getWorldCareFundCharges()
   {
      final WorldCareFundRuleInputData data = new WorldCareFundRuleInputData();
      data.setAdultCharge("AdultCharge");
      data.setChildCharge("ChildCharge");
      return ruleService.executeWorldCareFundRule(data);

   }

   /**
    * @return Returns offer priority list from drools
    */
   @Override
   public String getDurationPriorityList(final String durationType,
      final HolidaySearchContext context)
   {

      final DPRuleInputData data = new DPRuleInputData();
      LocalDate erliest = null;
      LocalDate latest = null;

      if (null != context.getEarlierDepartureDate() && null != context.getLatestDepartureDate())
      {

         erliest =
            DateUtils.toDate(context.getEarlierDepartureDate(), DateRangeProviderUtil.DATE_FORMAT);
         latest =
            DateUtils.toDate(context.getLatestDepartureDate(), DateRangeProviderUtil.DATE_FORMAT);

      }

      data.setDurationType(durationType);
      setDates(data, erliest, latest);

      final DPRuleOutputData output = ruleService.executeDurationPriorityRule(data);

      if (output != null)
      {

         return output.getPriorityList();

      }
      return null;
   }

   /**
    * @param dataParam
    * @param erliest
    * @param latest
    */
   private void setDates(final DPRuleInputData dataParam, final LocalDate erliest,
      final LocalDate latest)
   {
      final DPRuleInputData data = dataParam;
      if (erliest != null)
      {
         data.setStartDate(erliest.toDateMidnight().toDate());
      }
      if (latest != null)
      {
         data.setEndDate(latest.toDateMidnight().toDate());
      }
   }

   /**
    * @return Returns offer priority list from drools
    */
   @Override
   public String getDurationPriorityList(final String durationType)
   {
      final DPRuleInputData data = new DPRuleInputData();

      data.setDurationType(durationType);

      final DPRuleOutputData output = ruleService.executeDurationPriorityRule(data);

      if (output != null)
      {
         return output.getPriorityList();
      }
      return null;
   }

   /**
    * @return Returns offer priority list from drools
    */
   @Override
   public String getDurationPriorityList(final String durationType, final String collectionType)
   {
      final DPRuleInputData data = new DPRuleInputData();
      data.setDurationType(durationType);
      data.setCollectionType(collectionType);
      final DPRuleOutputData output = ruleService.executeDurationPriorityRule(data);

      if (output != null)
      {
         return output.getPriorityList();
      }
      return null;
   }

   @Override
   public void checkLDNWeeks(final VisionBrochureCriteria brochureCriteria)
   {
      ruleService.executeLowDepositNWeeksRule(brochureCriteria);
   }

   @Override
   public void checkLDPP(final PartyCompositionCriteria partyCompositionCriteria)
   {
      ruleService.executeLowDepositPP(partyCompositionCriteria);
   }

   @Override
   public void applyLowAvailabilityRules(
      final RoomAvailabilityIndicatorData roomAvailabilityIndicatorModel)
   {
      ruleService.executelimitedAvailibiltyForRoom(roomAvailabilityIndicatorModel);
   }
}
