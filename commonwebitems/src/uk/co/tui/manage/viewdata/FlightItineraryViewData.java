/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.List;


/**
 * @author sunil.bd
 *
 */
public class FlightItineraryViewData
{

    private List<LegViewData> inBound;

    private List<LegViewData> outBound;

    private List<PriceViewData> priceList;

    private String inventory;

    /**
     * @return the inBound
     */
    public List<LegViewData> getInBound()
    {
        return inBound;
    }

    /**
     * @param inBound
     *           the inBound to set
     */
    public void setInBound(final List<LegViewData> inBound)
    {
        this.inBound = inBound;
    }

    /**
     * @return the outBound
     */
    public List<LegViewData> getOutBound()
    {
        return outBound;
    }

    /**
     * @param outBound
     *           the outBound to set
     */
    public void setOutBound(final List<LegViewData> outBound)
    {
        this.outBound = outBound;
    }

    /**
     * @return the priceList
     */
    public List<PriceViewData> getPriceList()
    {
        return priceList;
    }

    /**
     * @param priceList
     *           the priceList to set
     */
    public void setPriceList(final List<PriceViewData> priceList)
    {
        this.priceList = priceList;
    }

    /**
     * @return the inventory
     */
    public String getInventory()
    {
        return inventory;
    }

    /**
     * @param inventory
     *           the inventory to set
     */
    public void setInventory(final String inventory)
    {
        this.inventory = inventory;
    }



}
