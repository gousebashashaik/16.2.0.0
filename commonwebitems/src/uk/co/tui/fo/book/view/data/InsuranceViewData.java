/**
 *
 */
package uk.co.tui.fo.book.view.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author akshay.an
 *
 */
public class InsuranceViewData {

    /** The code. */
    private String code = StringUtils.EMPTY;

    /** The description. */
    private String description = StringUtils.EMPTY;

    /** The from adult price. */
    private String frmAdtPrice = StringUtils.EMPTY;

    /** The from child price. */
    private String frmChdPrice = StringUtils.EMPTY;

    /** The price. */
    private String price;

    /** The frm price. */
    private String frmPrice;

    /** The price with ew. */
    private String priceWithEW;

    /** The insurance type. */
    private boolean tandcAccepted;

    /** The excess waiver view data. */
    private ExcessWaiverViewData excessWaiverViewData;

    /** The selected. */
    private boolean selected;

    /** The family ins present. */
    private boolean familyInsPresent;

    /** The currency appendedFrm price. */
    private String currencyAppendedFrmPrice = StringUtils.EMPTY;

    /** The pax composition. */
    private String paxComposition = StringUtils.EMPTY;

    /** The currency frm price. */
    private String currencyFrmPrice = StringUtils.EMPTY;

    /** The ins removed. */
    private boolean insRemoved;

    /** The quantity. */
    private int quantity;

    private String totalPrice;

    /** The description. */
    private String descriptionSummary = StringUtils.EMPTY;

    /**
     * @return the totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the frmAdtPrice
     */
    public String getFrmAdtPrice() {
        return frmAdtPrice;
    }

    /**
     * @param frmAdtPrice the frmAdtPrice to set
     */
    public void setFrmAdtPrice(String frmAdtPrice) {
        this.frmAdtPrice = frmAdtPrice;
    }

    /**
     * @return the frmChdPrice
     */
    public String getFrmChdPrice() {
        return frmChdPrice;
    }

    /**
     * @param frmChdPrice the frmChdPrice to set
     */
    public void setFrmChdPrice(String frmChdPrice) {
        this.frmChdPrice = frmChdPrice;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return the frmPrice
     */
    public String getFrmPrice() {
        return frmPrice;
    }

    /**
     * @param frmPrice the frmPrice to set
     */
    public void setFrmPrice(String frmPrice) {
        this.frmPrice = frmPrice;
    }

    /**
     * @return the priceWithEW
     */
    public String getPriceWithEW() {
        return priceWithEW;
    }

    /**
     * @param priceWithEW the priceWithEW to set
     */
    public void setPriceWithEW(String priceWithEW) {
        this.priceWithEW = priceWithEW;
    }

    /**
     * @return the tandcAccepted
     */
    public boolean isTandcAccepted() {
        return tandcAccepted;
    }

    /**
     * @param tandcAccepted the tandcAccepted to set
     */
    public void setTandcAccepted(boolean tandcAccepted) {
        this.tandcAccepted = tandcAccepted;
    }

    /**
     * @return the excessWaiverViewData
     */
    public ExcessWaiverViewData getExcessWaiverViewData() {
        return excessWaiverViewData;
    }

    /**
     * @param excessWaiverViewData the excessWaiverViewData to set
     */
    public void setExcessWaiverViewData(ExcessWaiverViewData excessWaiverViewData) {
        this.excessWaiverViewData = excessWaiverViewData;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the familyInsPresent
     */
    public boolean isFamilyInsPresent() {
        return familyInsPresent;
    }

    /**
     * @param familyInsPresent the familyInsPresent to set
     */
    public void setFamilyInsPresent(boolean familyInsPresent) {
        this.familyInsPresent = familyInsPresent;
    }

    /**
     * @return the currencyAppendedFrmPrice
     */
    public String getCurrencyAppendedFrmPrice() {
        return currencyAppendedFrmPrice;
    }

    /**
     * @param currencyAppendedFrmPrice the currencyAppendedFrmPrice to set
     */
    public void setCurrencyAppendedFrmPrice(String currencyAppendedFrmPrice) {
        this.currencyAppendedFrmPrice = currencyAppendedFrmPrice;
    }

    /**
     * @return the paxComposition
     */
    public String getPaxComposition() {
        return paxComposition;
    }

    /**
     * @param paxComposition the paxComposition to set
     */
    public void setPaxComposition(String paxComposition) {
        this.paxComposition = paxComposition;
    }

    /**
     * @return the currencyFrmPrice
     */
    public String getCurrencyFrmPrice() {
        return currencyFrmPrice;
    }

    /**
     * @param currencyFrmPrice the currencyFrmPrice to set
     */
    public void setCurrencyFrmPrice(String currencyFrmPrice) {
        this.currencyFrmPrice = currencyFrmPrice;
    }

    /**
     * @return the insRemoved
     */
    public boolean isInsRemoved() {
        return insRemoved;
    }

    /**
     * @param insRemoved the insRemoved to set
     */
    public void setInsRemoved(boolean insRemoved) {
        this.insRemoved = insRemoved;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the descriptionSummary
     */
    public String getDescriptionSummary() {
        return descriptionSummary;
    }

    /**
     * @param descriptionSummary the descriptionSummary to set
     */
    public void setDescriptionSummary(String descriptionSummary) {
        this.descriptionSummary = descriptionSummary;
    }
}
