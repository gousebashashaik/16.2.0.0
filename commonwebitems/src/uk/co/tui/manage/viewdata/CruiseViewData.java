/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.ArrayList;
import java.util.List;



/**
 * @author premkumar.nd
 *
 */
public class CruiseViewData
{

    private String code;
    private String name;
    private String startDate;
    private String endDate;
    private List<ItineraryViewData> cruiseItineraryList;

    private List<ItineraryViewData> cruiseItineraryCol1List;
    private List<ItineraryViewData> cruiseItineraryCol2List;

    private String duration;
    private String locationMapUrl;
    private String portName;
    private String time;
    private String productName;
    private MediaViewData picture;

    private ShipViewData shipViewData;
    private BoardBasisViewData boardBasisViewData;
    private List<CabinViewData> cabinList = new ArrayList<CabinViewData>();






    /**
     * @param cabinList
     *           the cabinList to set
     */
    public void setCabinList(final List<CabinViewData> cabinList)
    {
        this.cabinList = cabinList;
    }

    /**
     * @return the cabinList
     */
    public List<CabinViewData> getCabinList()
    {
        return cabinList;
    }

    /**
     * @return the boardBasisViewData
     */
    public BoardBasisViewData getBoardBasisViewData()
    {
        return boardBasisViewData;
    }

    /**
     * @param boardBasisViewData
     *           the boardBasisViewData to set
     */
    public void setBoardBasisViewData(final BoardBasisViewData boardBasisViewData)
    {
        this.boardBasisViewData = boardBasisViewData;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *           the code to set
     */
    public void setCode(final String code)
    {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *           the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return the startDate
     */
    public String getStartDate()
    {
        return startDate;
    }

    /**
     * @param startDate
     *           the startDate to set
     */
    public void setStartDate(final String startDate)
    {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate()
    {
        return endDate;
    }

    /**
     * @param endDate
     *           the endDate to set
     */
    public void setEndDate(final String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * @return the cruiseItineraryList
     */
    public List<ItineraryViewData> getCruiseItineraryList()
    {
        return cruiseItineraryList;
    }

    /**
     * @param cruiseItineraryList
     *           the cruiseItineraryList to set
     */
    public void setCruiseItineraryList(final List<ItineraryViewData> cruiseItineraryList)
    {
        this.cruiseItineraryList = cruiseItineraryList;
    }

    /**
     * @return the duration
     */
    public String getDuration()
    {
        return duration;
    }

    /**
     * @param duration
     *           the duration to set
     */
    public void setDuration(final String duration)
    {
        this.duration = duration;
    }

    /**
     * @return the locationMapUrl
     */
    public String getLocationMapUrl()
    {
        return locationMapUrl;
    }

    /**
     * @param locationMapUrl
     *           the locationMapUrl to set
     */
    public void setLocationMapUrl(final String locationMapUrl)
    {
        this.locationMapUrl = locationMapUrl;
    }

    /**
     * @return the portName
     */
    public String getPortName()
    {
        return portName;
    }

    /**
     * @param portName
     *           the portName to set
     */
    public void setPortName(final String portName)
    {
        this.portName = portName;
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
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param productName
     *           the productName to set
     */
    public void setProductName(final String productName)
    {
        this.productName = productName;
    }

    /**
     * @return the picture
     */
    public MediaViewData getPicture()
    {
        return picture;
    }

    /**
     * @param picture
     *           the picture to set
     */
    public void setPicture(final MediaViewData picture)
    {
        this.picture = picture;
    }

    /**
     * @return the shipViewData
     */
    public ShipViewData getShipViewData()
    {
        return shipViewData;
    }

    /**
     * @param shipViewData
     *           the shipViewData to set
     */
    public void setShipViewData(final ShipViewData shipViewData)
    {
        this.shipViewData = shipViewData;
    }

    /**
     * @return the cruiseItineraryCol1List
     */
    public List<ItineraryViewData> getCruiseItineraryCol1List()
    {
        return cruiseItineraryCol1List;
    }

    /**
     * @param cruiseItineraryCol1List
     *           the cruiseItineraryCol1List to set
     */
    public void setCruiseItineraryCol1List(final List<ItineraryViewData> cruiseItineraryCol1List)
    {
        this.cruiseItineraryCol1List = cruiseItineraryCol1List;
    }

    /**
     * @return the cruiseItineraryCol2List
     */
    public List<ItineraryViewData> getCruiseItineraryCol2List()
    {
        return cruiseItineraryCol2List;
    }

    /**
     * @param cruiseItineraryCol2List
     *           the cruiseItineraryCol2List to set
     */
    public void setCruiseItineraryCol2List(final List<ItineraryViewData> cruiseItineraryCol2List)
    {
        this.cruiseItineraryCol2List = cruiseItineraryCol2List;
    }



}
