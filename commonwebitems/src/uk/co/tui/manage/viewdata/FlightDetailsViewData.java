/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.List;



/**
 * @author sunil.bd
 *
 */
public class FlightDetailsViewData
{

    private int duration;

    private PriceViewData price;

    private boolean isWelfare;

    private String inventory;

    private List<LegViewData> inboundSectors;

    private List<LegViewData> outboundSectors;

    /**
     * @return the inboundSectors
     */
    public List<LegViewData> getInboundSectors()
    {
        return inboundSectors;
    }

    /**
     * @param inboundSectors
     *           the inboundSectors to set
     */
    public void setInboundSectors(final List<LegViewData> inboundSectors)
    {
        this.inboundSectors = inboundSectors;
    }

    /**
     * @return the outboundSectors
     */
    public List<LegViewData> getOutboundSectors()
    {
        return outboundSectors;
    }

    /**
     * @param outboundSectors
     *           the outboundSectors to set
     */
    public void setOutboundSectors(final List<LegViewData> outboundSectors)
    {
        this.outboundSectors = outboundSectors;
    }

    /**
     * @return the isWelfare
     */
    public boolean isWelfare()
    {
        return isWelfare;
    }

    /**
     * @param isWelfare
     *           the isWelfare to set
     */
    public void setWelfare(final boolean isWelfare)
    {
        this.isWelfare = isWelfare;
    }

    /**
     * @return the price
     */
    public PriceViewData getPrice()
    {
        return price;
    }

    /**
     * @param price
     *           the price to set
     */
    public void setPrice(final PriceViewData price)
    {
        this.price = price;
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

    /**
     * @return the duration
     */
    public int getDuration()
    {
        return duration;
    }

    /**
     * @param duration
     *           the duration to set
     */
    public void setDuration(final int duration)
    {
        this.duration = duration;
    }



}
