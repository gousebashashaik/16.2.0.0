/**
 *
 */
package uk.co.tui.th.book.data;

/**
 * CruiseExtraCriteria to hold ajax parameters
 */
public class CruiseExtraCriteria
{

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
    public String getExtraCode()
    {
        return extraCode;
    }


    /**
     * @param extraCode
     *           the extraCode to set
     */
    public void setExtraCode(final String extraCode)
    {
        this.extraCode = extraCode;
    }


    /**
     * @return the quantity
     */
    public int getQuantity()
    {
        return quantity;
    }


    /**
     * @param quantity
     *           the quantity to set
     */
    public void setQuantity(final int quantity)
    {
        this.quantity = quantity;
    }


    /**
     * @return the extraCategoryCode
     */
    public String getExtraCategoryCode()
    {
        return extraCategoryCode;
    }


    /**
     * @param extraCategoryCode
     *           the extraCategoryCode to set
     */
    public void setExtraCategoryCode(final String extraCategoryCode)
    {
        this.extraCategoryCode = extraCategoryCode;
    }

}
