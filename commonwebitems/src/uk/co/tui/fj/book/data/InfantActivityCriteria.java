/**
 *
 */
package uk.co.tui.fj.book.data;

/**
 * @author pradeep.as
 *
 */
public class InfantActivityCriteria {

    /**
     * The ExtraFacility Code.
     */
    private String extraCode;

    /**
     * The selected extra Quantity.
     */
    private int quantity;


    private String extraCategoryCode;


    /**
     * @return the extraCode
     */
    public String getExtraCode() {
        return extraCode;
    }


    /**
     * @param extraCode the extraCode to set
     */
    public void setExtraCode(String extraCode) {
        this.extraCode = extraCode;
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
     * @return the extraCategoryCode
     */
    public String getExtraCategoryCode() {
        return extraCategoryCode;
    }


    /**
     * @param extraCategoryCode the extraCategoryCode to set
     */
    public void setExtraCategoryCode(String extraCategoryCode) {
        this.extraCategoryCode = extraCategoryCode;
    }

}
