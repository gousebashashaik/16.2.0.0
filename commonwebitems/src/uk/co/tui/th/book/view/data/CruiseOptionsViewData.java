/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The Class CruiseOptionsViewData.
 */
public class CruiseOptionsViewData {

    /**
     * Holds the data specific to summary panel.
     */
    private SummaryPanelViewData summaryViewData;

    /**
     * Holds the Package Relevant View Data.
     */
    private PackageViewData packageViewData;

    /**
     * Holds the data specific to summary panel.
     */
    private ExtraFacilityViewDataContainer extraFacilityViewDataContainer;

    /** The alert messages. */
    private List<AlertViewData> alertMessages;

    /** The room options view data. */
    private List<CabinOptionsViewData> cabinOptionsViewData;

    /** The board basis view data. */
    private List<BoardBasisViewData> alternateBoardBasis;

    /** The packagetype View data */
    private String packageType;

    /**
     * @return the packageType
     */
    public String getPackageType() {
        return packageType;
    }

    /**
     * @param packageType
     *            the packageType to set
     */
    public void setPackageType(final String packageType) {
        this.packageType = packageType;
    }

    /**
     * Gets the package view data.
     *
     * @return the packageViewData
     */
    public PackageViewData getPackageViewData() {
        if (this.packageViewData == null) {
            this.packageViewData = new PackageViewData();
        }
        return this.packageViewData;
    }

    /**
     * Sets the package view data.
     *
     * @param packageViewData
     *            the packageViewData to set
     */
    public void setPackageViewData(final PackageViewData packageViewData) {
        this.packageViewData = packageViewData;
    }

    /**
     * Gets the summary view data.
     *
     * @return the summaryViewData
     */
    public SummaryPanelViewData getSummaryViewData() {
        if (this.summaryViewData == null) {
            this.summaryViewData = new SummaryPanelViewData();
        }
        return this.summaryViewData;
    }

    /**
     * Sets the summary view data.
     *
     * @param summaryViewData
     *            the summaryViewData to set
     */
    public void setSummaryViewData(final SummaryPanelViewData summaryViewData) {
        this.summaryViewData = summaryViewData;
    }

    /**
     * Gets the extra facility view data container.
     *
     * @return the extraFacilityViewDataContainer
     */
    public ExtraFacilityViewDataContainer getExtraFacilityViewDataContainer() {
        if (this.extraFacilityViewDataContainer == null) {
            this.extraFacilityViewDataContainer = new ExtraFacilityViewDataContainer();
        }
        return this.extraFacilityViewDataContainer;
    }

    /**
     * Sets the extra facility view data container.
     *
     * @param extraFacilityViewDataContainer
     *            the extraFacilityViewDataContainer to set
     */
    public void setExtraFacilityViewDataContainer(
            final ExtraFacilityViewDataContainer extraFacilityViewDataContainer) {
        this.extraFacilityViewDataContainer = extraFacilityViewDataContainer;
    }

    /**
     * Return the string representation of the object.
     *
     * @return the string representation of the object.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Gets the alert messages.
     *
     * @return the alertMessages
     */
    public List<AlertViewData> getAlertMessages() {
        return alertMessages;
    }

    /**
     * Sets the alert messages.
     *
     * @param alertMessages
     *            the alertMessages to set
     */
    public void setAlertMessages(final List<AlertViewData> alertMessages) {
        this.alertMessages = alertMessages;
    }

    /**
     * Gets the cabin options view data.
     *
     * @return the cabinOptionsViewData
     */
    public List<CabinOptionsViewData> getCabinOptionsViewData() {
        return cabinOptionsViewData;
    }

    /**
     * Sets the cabin options view data.
     *
     * @param cabinOptionsViewData
     *            the cabinOptionsViewData to set
     */
    public void setCabinOptionsViewData(
            final List<CabinOptionsViewData> cabinOptionsViewData) {
        this.cabinOptionsViewData = cabinOptionsViewData;
    }

    /**
     * Gets the alternate board basis.
     *
     * @return the alternateBoardBasis
     */
    public List<BoardBasisViewData> getAlternateBoardBasis() {
        return alternateBoardBasis;
    }

    /**
     * Sets the alternate board basis.
     *
     * @param alternateBoardBasis
     *            the alternateBoardBasis to set
     */
    public void setAlternateBoardBasis(
            final List<BoardBasisViewData> alternateBoardBasis) {
        this.alternateBoardBasis = alternateBoardBasis;
    }

}
