/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author omonikhide
 *
 */
public class LocationCarouselViewData
{
    private String name;
    private String code;
    private List<LocationData> locationDatas;
    private String lookUpTypeCode;
    private Integer visibleItems;
    /**
     * @param name
     * @param code
     * @param locationDatas
     * @param lookUpTypeCode
     * @param visibleItems
     */
    public LocationCarouselViewData(String name, String code, List<LocationData> locationDatas, String lookUpTypeCode, Integer visibleItems) {
        super();
        this.name = name;
        this.code = code;
        this.locationDatas = locationDatas;
        this.lookUpTypeCode = lookUpTypeCode;
        this.visibleItems = visibleItems;
    }
    /**
     *
     * @param name
     * @param code
     * @param locationDatas
     */
    public LocationCarouselViewData(String name, String code, List<LocationData> locationDatas) {
        super();
        this.name = name;
        this.code = code;
        this.locationDatas = locationDatas;
    }

    /**
     *
     */
    public LocationCarouselViewData() {

    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the locationDatas
     */
    public List<LocationData> getLocationDatas() {
        return locationDatas;
    }
    /**
     * @param locationDatas the locationDatas to set
     */
    public void setLocationDatas(List<LocationData> locationDatas) {
        this.locationDatas = locationDatas;
    }
    /**
     * @return the lookUpTypeCode
     */
    public String getLookUpTypeCode() {
        return lookUpTypeCode;
    }
    /**
     * @param lookUpTypeCode the lookUpType to set
     */
    public void setLookUpTypeCode(String lookUpTypeCode) {
        this.lookUpTypeCode = lookUpTypeCode;
    }
    /**
     * @return the visibleItems
     */
    public Integer getVisibleItems() {
        return visibleItems;
    }
    /**
     * @param visibleItems the visibleItems to set
     */
    public void setVisibleItems(Integer visibleItems) {
        this.visibleItems = visibleItems;
    }
}
