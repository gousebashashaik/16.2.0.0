/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thyagaraju.e
 *
 */
public class TermsAndConditionsViewData {

    /** The terms and conditions map. */
    private Map<Object, Object> specialNeedsMap = new HashMap<Object,Object>();

    /** The privacy policy map. */
    private Map<Object, Object> privacyPolicyMap = new HashMap<Object,Object>();

    /** The identifier. */
    private String identifier;

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the specialNeedsMap
     */
    public Map<Object, Object> getSpecialNeedsMap() {
        return specialNeedsMap;
    }

    /**
     * @param specialNeedsMap the specialNeedsMap to set
     */
    public void setSpecialNeedsMap(Map<Object, Object> specialNeedsMap) {
        this.specialNeedsMap = specialNeedsMap;
    }

    /**
     * @return the privacyPolicyMap
     */
    public Map<Object, Object> getPrivacyPolicyMap() {
        return privacyPolicyMap;
    }

    /**
     * @param privacyPolicyMap the privacyPolicyMap to set
     */
    public void setPrivacyPolicyMap(Map<Object, Object> privacyPolicyMap) {
        this.privacyPolicyMap = privacyPolicyMap;
    }

}
