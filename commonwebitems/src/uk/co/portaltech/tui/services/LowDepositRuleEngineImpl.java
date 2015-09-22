/**
 *
 */
package uk.co.portaltech.tui.services;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatelessKnowledgeSession;

import uk.co.portaltech.travel.services.lowdeposit.PartyCompositionCriteria;
import uk.co.portaltech.travel.services.lowdeposit.VisionBrochureCriteria;
import uk.co.portaltech.travel.services.rules.RuleEngineUtil;

/**
 * @author sunilkumar.sahu
 *
 */
public class LowDepositRuleEngineImpl {

    private static final KnowledgeBase LOW_DEPOSIT_KNOWLEDGE_BASE = RuleEngineUtil
            .getKnowledgeBase("/tui/lowDepositRules.drl");

    public void applyLowDepositRules(
            PartyCompositionCriteria partyCompositionCriteria) {
        getStatelessKnowledgeSession().execute(partyCompositionCriteria);

    }

    public void applyNweeksRules(VisionBrochureCriteria brochureCriteria) {
        getStatelessKnowledgeSession().execute(brochureCriteria);

    }

    private StatelessKnowledgeSession getStatelessKnowledgeSession() {
        return LOW_DEPOSIT_KNOWLEDGE_BASE.newStatelessKnowledgeSession();
    }

}
