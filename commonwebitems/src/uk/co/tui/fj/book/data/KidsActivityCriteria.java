/**
 *
 */
package uk.co.tui.fj.book.data;

/**
 * The class to holds the Parameters as part of request required for the
 * updation of Kids Activity Extra.
 *
 * @author madhumathi.m
 *
 */
public class KidsActivityCriteria {

    /**
     * The Passenger Id
     */
    private String passengerId;

    /**
     * The ExtraFacility Code.
     */
    private String extraCode;

    /**
     * The selected extra Quantity.
     */
    private int quantity;

    /** The selected. */
    private boolean selected;


    private String extraCategoryCode;

    /**
     * @return the passengerId
     */
    public String getPassengerId() {
        return passengerId;
    }

    /**
     * @param passengerId the passengerId to set
     */
    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

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
