/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The Class ExtraFacilityCategoryViewData.
 *
 * @author munisekhar.k
 */
public class ExtraFacilityCategoryViewData {

    /** The extra facility category code. */
    private String extraFacilityCategoryCode = StringUtils.EMPTY;

    /** The multi select. */
    private boolean multiSelect;

    /** The extra facility view data. */
    private List<ExtraFacilityViewData> extraFacilityViewData = Collections
            .emptyList();

    /** The extra facility group code. */
    private String extraFacilityGroupCode = StringUtils.EMPTY;

    /** The disable extra facility option available. */
    private boolean disableExtraFacilityOptionAvailable;

    /** The no transfer opted. */
    private boolean noTransferOpted;

    /** The display no transfer option. */
    private boolean displayNoTransferOption;

    /** Holds the code of Selected column of extra facility of baggage option. */
    private String totalSelectedCode;

    /**
     * Holds the total weight of selected baggage.
     */
    private int totalSelectedWeight;

    /** The extras to passenger relation. */
    private List<ExtrasToPassengerRelationViewData> extrasToPassengerMapping = Collections
            .emptyList();

    /** The insurance view data. */
    private List<InsuranceViewData> insuranceViewData = new ArrayList<InsuranceViewData>();

    /** The super category code. */
    private String superCategoryCode = StringUtils.EMPTY;

    /** The alias super category code. */
    private String aliasSuperCategoryCode = StringUtils.EMPTY;

    /** The display. */
    private boolean display;

    /** The display name. */
    private String displayName = StringUtils.EMPTY;

    /** The currency appended category total price. */
    private String currencyAppendedCategoryTotalPrice;

    /** The selected quantity. */
    private int selectedQuantity;

    /** The description. */
    private String description = StringUtils.EMPTY;

    /** The car hire content map. */
    private Map<Object, Object> carHireContentMap = new HashMap<Object, Object>();

    /**
     * The flag to help UI guys to decide whether an extra category is available
     * to display or not.
     */
    private boolean available;

    /** The selected. */
    private boolean selected;

    /** The extra content. */
    private ExtraFacilityContentViewData extraContent;

    /** The min adult count required for extra. */
    private int minAdultCountRequiredForExtra = 1;
    /**
     * Gets the extra facility category code.
     *
     * @return the extraFacilityCategoryCode
     */
    public String getExtraFacilityCategoryCode() {
        return extraFacilityCategoryCode;
    }

    /**
     * Sets the extra facility category code.
     *
     * @param extraFacilityCategoryCode
     *            the extraFacilityCategoryCode to set
     */
    public void setExtraFacilityCategoryCode(
            final String extraFacilityCategoryCode) {
        this.extraFacilityCategoryCode = extraFacilityCategoryCode;
    }

    /**
     * Checks if is multi select.
     *
     * @return the multiSelect
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * Sets the multi select.
     *
     * @param multiSelect
     *            the multiSelect to set
     */
    public void setMultiSelect(final boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    /**
     * Gets the extra facility view data.
     *
     * @return the extraFacilityViewData
     */
    public List<ExtraFacilityViewData> getExtraFacilityViewData() {
        if (CollectionUtils.isEmpty(extraFacilityViewData)) {
            extraFacilityViewData = new ArrayList<ExtraFacilityViewData>();
        }
        return extraFacilityViewData;
    }

    /**
     * Gets the extra facility group code.
     *
     * @return the extraFacilityGroupCode
     */
    public String getExtraFacilityGroupCode() {
        return extraFacilityGroupCode;
    }

    /**
     * Sets the extra facility group code.
     *
     * @param extraFacilityGroupCode
     *            the extraFacilityGroupCode to set
     */
    public void setExtraFacilityGroupCode(final String extraFacilityGroupCode) {
        this.extraFacilityGroupCode = extraFacilityGroupCode;
    }

    /**
     * Sets the extra facility view data.
     *
     * @param extraFacilityViewData
     *            the extraFacilityViewData to set
     */
    public void setExtraFacilityViewData(
            final List<ExtraFacilityViewData> extraFacilityViewData) {
        this.extraFacilityViewData = extraFacilityViewData;
    }

    /**
     * Checks if an option available to disable the extra facility options.
     *
     * @return the disableExtraFacilityOptionAvailable
     */
    public boolean isDisableExtraFacilityOptionAvailable() {
        return disableExtraFacilityOptionAvailable;
    }

    /**
     * Sets an option to disable the extra facility options.
     *
     * @param disableExtraFacilityOptionAvailable
     *            the new disable extra facility option available
     */
    public void setDisableExtraFacilityOptionAvailable(
            final boolean disableExtraFacilityOptionAvailable) {
        this.disableExtraFacilityOptionAvailable = disableExtraFacilityOptionAvailable;
    }

    /**
     * Gets the extras to passenger mapping.
     *
     * @return the extrasToPassengerMapping
     */
    public List<ExtrasToPassengerRelationViewData> getExtrasToPassengerMapping() {
        if (CollectionUtils.isEmpty(extrasToPassengerMapping)) {
            extrasToPassengerMapping = new ArrayList<ExtrasToPassengerRelationViewData>();
        }
        return extrasToPassengerMapping;
    }

    /**
     * Checks if is no transfer opted.
     *
     * @return true, if is no transfer opted
     */
    public boolean isNoTransferOpted() {
        return noTransferOpted;
    }

    /**
     * Sets the no transfer opted.
     *
     * @param noTransferOpted
     *            the new no transfer opted
     */
    public void setNoTransferOpted(final boolean noTransferOpted) {
        this.noTransferOpted = noTransferOpted;
    }

    /**
     * Gets the insurance view data.
     *
     * @return the insuranceViewData
     */
    public List<InsuranceViewData> getInsuranceViewData() {
        return insuranceViewData;
    }

    /**
     * Sets the insurance view data.
     *
     * @param insuranceViewData
     *            the insuranceViewData to set
     */
    public void setInsuranceViewData(
            final List<InsuranceViewData> insuranceViewData) {
        this.insuranceViewData = insuranceViewData;
    }

    /**
     * Checks if is display.
     *
     * @return the display
     */
    public boolean isDisplay() {
        return display;
    }

    /**
     * Sets the display.
     *
     * @param display
     *            the display to set
     */
    public void setDisplay(final boolean display) {
        this.display = display;
    }

    /**
     * Gets the display name.
     *
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the currency appended category total price.
     *
     * @return the currencyAppendedCategoryTotalPrice
     */
    public String getCurrencyAppendedCategoryTotalPrice() {
        return currencyAppendedCategoryTotalPrice;
    }

    /**
     * Sets the currency appended category total price.
     *
     * @param currencyAppendedCategoryTotalPrice
     *            the currencyAppendedCategoryTotalPrice to set
     */
    public void setCurrencyAppendedCategoryTotalPrice(
            final String currencyAppendedCategoryTotalPrice) {
        this.currencyAppendedCategoryTotalPrice = currencyAppendedCategoryTotalPrice;
    }

    /**
     * Gets the super category code.
     *
     * @return the superCategoryCode
     */
    public String getSuperCategoryCode() {
        return superCategoryCode;
    }

    /**
     * Sets the super category code.
     *
     * @param superCategoryCode
     *            the superCategoryCode to set
     */
    public void setSuperCategoryCode(final String superCategoryCode) {
        this.superCategoryCode = superCategoryCode;
    }

    /**
     * Gets the alias super category code.
     *
     * @return the alias super category code
     */
    public String getAliasSuperCategoryCode() {
        return aliasSuperCategoryCode;
    }

    /**
     * Sets the alias super category code.
     *
     * @param aliasSuperCategoryCode
     *            the new alias super category code
     */
    public void setAliasSuperCategoryCode(final String aliasSuperCategoryCode) {
        this.aliasSuperCategoryCode = aliasSuperCategoryCode;
    }

    /**
     * Gets the selected quantity.
     *
     * @return the selectedQuantity
     */
    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    /**
     * Sets the selected quantity.
     *
     * @param selectedQuantity
     *            the selectedQuantity to set
     */
    public void setSelectedQuantity(final int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the min adult count required for extra.
     *
     * @return the minAdultCountRequiredForExtra
     */
    public int getMinAdultCountRequiredForExtra() {
        return minAdultCountRequiredForExtra;
    }

    /**
     * Sets the min adult count required for extra.
     *
     * @param minAdultCountRequiredForExtra
     *            the minAdultCountRequiredForExtra to set
     */
    public void setMinAdultCountRequiredForExtra(
            final int minAdultCountRequiredForExtra) {
        this.minAdultCountRequiredForExtra = minAdultCountRequiredForExtra;
    }

    /**
     * Gets the extra content.
     *
     * @return the extraContent
     */
    public ExtraFacilityContentViewData getExtraContent() {
        if (extraContent == null) {
            extraContent = new ExtraFacilityContentViewData();
        }
        return extraContent;
    }

    /**
     * Sets the extra content.
     *
     * @param extraContent
     *            the extraContent to set
     */
    public void setExtraContent(final ExtraFacilityContentViewData extraContent) {
        this.extraContent = extraContent;
    }

    /**
     * Checks if is selected.
     *
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected.
     *
     * @param selected
     *            the selected to set
     */
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    /**
     * Sets the available.
     *
     * @param available
     *            the available to set
     */
    public void setAvailable(final boolean available) {
        this.available = available;
    }

    /**
     * Checks if is available.
     *
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Gets the total selected code.
     *
     * @return the totalSelectedCode
     */
    public String getTotalSelectedCode() {
        return totalSelectedCode;
    }

    /**
     * Sets the total selected code.
     *
     * @param totalSelectedCode
     *            the totalSelectedCode to set
     */
    public void setTotalSelectedCode(final String totalSelectedCode) {
        this.totalSelectedCode = totalSelectedCode;
    }

    /**
     * Gets the total selected weight.
     *
     * @return the totalSelectedWeight
     */
    public int getTotalSelectedWeight() {
        return totalSelectedWeight;
    }

    /**
     * Sets the total selected weight.
     *
     * @param totalSelectedWeight
     *            the totalSelectedWeight to set
     */
    public void setTotalSelectedWeight(final int totalSelectedWeight) {
        this.totalSelectedWeight = totalSelectedWeight;
    }

    /**
     * Gets the car hire content map.
     *
     * @return the carHireContentMap
     */
    public Map<Object, Object> getCarHireContentMap() {
        if (MapUtils.isEmpty(carHireContentMap)) {
            carHireContentMap = new HashMap<Object, Object>();
        }
        return carHireContentMap;
    }

    /**
     * Sets the car hire content map.
     *
     * @param carHireContentMap
     *            the carHireContentMap to set
     */
    public void setCarHireContentMap(final Map<Object, Object> carHireContentMap) {
        this.carHireContentMap = carHireContentMap;
    }

    /**
     * Checks if is display no transfer option.
     *
     * @return the displayNoTransferOption
     */
    public boolean isDisplayNoTransferOption() {
        return displayNoTransferOption;
    }

    /**
     * Sets the display no transfer option.
     *
     * @param displayNoTransferOption
     *            the displayNoTransferOption to set
     */
    public void setDisplayNoTransferOption(final boolean displayNoTransferOption) {
        this.displayNoTransferOption = displayNoTransferOption;
    }

}
