/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thyagaraju.e
 *
 */
public class ExtraOptionsStaticContentViewData {

    /** The extra content map. */
    private Map<String, String> extraContentMap = new HashMap<String, String>();

    /** The insurance static content view data. */
    private InsuranceStaticContentViewData insuranceStaticContentViewData;

    private GenericStaticContentViewData genericContentViewData;

    private NavigationStaticContentViewData navigationStaticContentViewData;

    private SummaryPanelStaticContentViewData summaryContentViewData;

    /**
     * Puts all the extra content.
     */
    public Map<String, String> getExtraContentMap() {
        return extraContentMap;
    }

    /**
     * @return the genericContentViewData
     */
    public GenericStaticContentViewData getGenericContentViewData() {
        return genericContentViewData;
    }

    /**
     * @param genericContentViewData
     *            the genericContentViewData to set
     */
    public void setGenericContentViewData(
            final GenericStaticContentViewData genericContentViewData) {
        this.genericContentViewData = genericContentViewData;
    }

    /**
     * @return the navigationStaticContentViewData
     */
    public NavigationStaticContentViewData getNavigationStaticContentViewData() {
        return navigationStaticContentViewData;
    }

    /**
     * @param navigationStaticContentViewData
     *            the navigationStaticContentViewData to set
     */
    public void setNavigationStaticContentViewData(
            final NavigationStaticContentViewData navigationStaticContentViewData) {
        this.navigationStaticContentViewData = navigationStaticContentViewData;
    }

    /**
     * @return the summaryContentViewData
     */
    public SummaryPanelStaticContentViewData getSummaryContentViewData() {
        return summaryContentViewData;
    }

    /**
     * @param extraContentMap
     *            the extraContentMap to set
     */
    public void setExtraContentMap(final Map<String, String> extraContentMap) {
        this.extraContentMap = extraContentMap;
    }

    /**
     * @param summaryContentViewData
     *            the summaryContentViewData to set
     */
    public void setSummaryContentViewData(
            final SummaryPanelStaticContentViewData summaryContentViewData) {
        this.summaryContentViewData = summaryContentViewData;
    }

    /**
     * @return the insuranceStaticContentViewData
     */
    public InsuranceStaticContentViewData getInsuranceStaticContentViewData() {
        return insuranceStaticContentViewData;
    }

    /**
     * @param insuranceStaticContentViewData
     *            the insuranceStaticContentViewData to set
     */
    public void setInsuranceStaticContentViewData(
            final InsuranceStaticContentViewData insuranceStaticContentViewData) {
        this.insuranceStaticContentViewData = insuranceStaticContentViewData;
    }
}
