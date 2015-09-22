/**
 *
 */
package uk.co.tui.manage.viewdata;

import uk.co.portaltech.travel.model.ItineraryItemModel;
import uk.co.portaltech.travel.model.LocationModel;


/**
 * @author premkumar.nd
 *
 */
public class ItineraryViewData
{
    private String loctionCode;
    private Integer dayNo;
    private Integer sequenceNo;
    private String nameOfPort;
    private String description;
    private String time;
    private String tendorValue;
    private LocationModel locationModel;
    private String mapImageUrl;

    private ItineraryItemModel itineraryItemModel;

    /**
     * @return the loctionCode
     */
    public String getLoctionCode()
    {
        return loctionCode;
    }

    /**
     * @param loctionCode
     *           the loctionCode to set
     */
    public void setLoctionCode(final String loctionCode)
    {
        this.loctionCode = loctionCode;
    }

    /**
     * @return the dayNo
     */
    public Integer getDayNo()
    {
        return dayNo;
    }

    /**
     * @param dayNo
     *           the dayNo to set
     */
    public void setDayNo(final Integer dayNo)
    {
        this.dayNo = dayNo;
    }

    /**
     * @return the sequenceNo
     */
    public Integer getSequenceNo()
    {
        return sequenceNo;
    }

    /**
     * @param sequenceNo
     *           the sequenceNo to set
     */
    public void setSequenceNo(final Integer sequenceNo)
    {
        this.sequenceNo = sequenceNo;
    }

    /**
     * @return the nameOfPort
     */
    public String getNameOfPort()
    {
        return nameOfPort;
    }

    /**
     * @param nameOfPort
     *           the nameOfPort to set
     */
    public void setNameOfPort(final String nameOfPort)
    {
        this.nameOfPort = nameOfPort;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *           the description to set
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return the time
     */
    public String getTime()
    {
        return time;
    }

    /**
     * @param time
     *           the time to set
     */
    public void setTime(final String time)
    {
        this.time = time;
    }

    /**
     * @return the tendorValue
     */
    public String getTendorValue()
    {
        return tendorValue;
    }

    /**
     * @param tendorValue
     *           the tendorValue to set
     */
    public void setTendorValue(final String tendorValue)
    {
        this.tendorValue = tendorValue;
    }

    /**
     * @return the mapImageUrl
     */
    public String getMapImageUrl()
    {
        return mapImageUrl;
    }

    /**
     * @param mapImageUrl
     *           the mapImageUrl to set
     */
    public void setMapImageUrl(final String mapImageUrl)
    {
        this.mapImageUrl = mapImageUrl;
    }

    /**
     * @return the locationModel
     */
    public LocationModel getLocationModel()
    {
        return locationModel;
    }

    /**
     * @param locationModel
     *           the locationModel to set
     */
    public void setLocationModel(final LocationModel locationModel)
    {
        this.locationModel = locationModel;
    }

    /**
     * @return the itineraryItemModel
     */
    public ItineraryItemModel getItineraryItemModel()
    {
        return itineraryItemModel;
    }

    /**
     * @param itineraryItemModel
     *           the itineraryItemModel to set
     */
    public void setItineraryItemModel(final ItineraryItemModel itineraryItemModel)
    {
        this.itineraryItemModel = itineraryItemModel;
    }


}
