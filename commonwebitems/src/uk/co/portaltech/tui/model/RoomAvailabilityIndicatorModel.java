/**
 *
 */
package uk.co.portaltech.tui.model;




public class RoomAvailabilityIndicatorModel
{
    private int daysToDeparture;

    private int inventoryCount;

    private String brandName;

    private String channel;

    private boolean lowAvailabilityIndicator;

    private String accomodationType;

    /**
     * @return the daysToDeparture
     */
    public int getDaysToDeparture()
    {
        return daysToDeparture;
    }

    /**
     * @param daysToDeparture
     *           the daysToDeparture to set
     */
    public void setDaysToDeparture(final int daysToDeparture)
    {
        this.daysToDeparture = daysToDeparture;
    }

    /**
     * @return the inventoryCount
     */
    public int getInventoryCount()
    {
        return inventoryCount;
    }

    /**
     * @param inventoryCount
     *           the inventoryCount to set
     */
    public void setInventoryCount(final int inventoryCount)
    {
        this.inventoryCount = inventoryCount;
    }

    /**
     * @return the lowAvailabilityIndicator
     */
    public boolean isLowAvailabilityIndicator()
    {
        return lowAvailabilityIndicator;
    }

    /**
     * @param lowAvailabilityIndicator
     *           the lowAvailabilityIndicator to set
     */
    public void setLowAvailabilityIndicator(final boolean lowAvailabilityIndicator)
    {
        this.lowAvailabilityIndicator = lowAvailabilityIndicator;
    }

    /**
     * @return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }


    /**
     * @return the accomodationType
     */
    public String getAccomodationType() {
        return accomodationType;
    }

    /**
     * @param accomodationType the accomodationType to set
     */
    public void setAccomodationType(String accomodationType) {
        this.accomodationType = accomodationType;
    }

}
