/**
 *
 */
package uk.co.portaltech.tui.services;

import java.util.List;

import uk.co.portaltech.travel.services.lowdeposit.PartyCompositionCriteria;
import uk.co.portaltech.travel.services.lowdeposit.VisionBrochureCriteria;
import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.tui.services.data.CreditCardRuleOutputData;
import uk.co.tui.services.data.RoomAvailabilityIndicatorData;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;

/**
 * @author laxmibai.p
 *
 */
public interface DroolsPriorityProviderService {

    /**
     * @return
     * Returns offer priority list from drools
     */
      List<String> getOfferPriorityList(String brandType);

      String getDurationPriorityList(String durationType,String collection);

      CreditCardRuleOutputData getCreditCardCharges();

      WorldCareFundRuleOutputData getWorldCareFundCharges();

      void checkLDNWeeks(VisionBrochureCriteria brochureCriteria);

      void checkLDPP(PartyCompositionCriteria partyCompositionCriteria);


      void applyLowAvailabilityRules(RoomAvailabilityIndicatorData roomAvailabilityIndicatorModel);

      String getDurationPriorityList(String durationType,HolidaySearchContext context);
      String getDurationPriorityList(String collection);

}
