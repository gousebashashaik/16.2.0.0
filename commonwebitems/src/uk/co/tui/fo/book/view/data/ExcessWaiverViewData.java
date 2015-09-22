/**
 *
 */
package uk.co.tui.fo.book.view.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author akshay.an
 *
 */
public class ExcessWaiverViewData {

    /** The code. */
    private String code = StringUtils.EMPTY;

    /** The selected. */
    private boolean selected;

    /** The description. */
    private String description = StringUtils.EMPTY;

    /** The price. */
    private String price = StringUtils.EMPTY;

    /** The currency appended price. */
    private String currencyAppendedPrice = StringUtils.EMPTY;

    /** The description summary. */
    private String descriptionSummary = StringUtils.EMPTY;

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
     * @return the currencyAppendedPrice
     */
    public String getCurrencyAppendedPrice() {
        return currencyAppendedPrice;
    }

    /**
     * @param currencyAppendedPrice the currencyAppendedPrice to set
     */
    public void setCurrencyAppendedPrice(String currencyAppendedPrice) {
        this.currencyAppendedPrice = currencyAppendedPrice;
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
