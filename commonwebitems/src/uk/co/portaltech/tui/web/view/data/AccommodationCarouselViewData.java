/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author omonikhide
 *
 */
public class AccommodationCarouselViewData
{
    private String name;
    private String code;
    private List<AccommodationViewData> accomodationDatas;
    private String lookUpTypeCode;
    private Integer visibleItems;


    /**
     * @param name
     * @param code
     * @param accomodationDatas
     * @param lookUpTypeCode
     * @param visibleItems
     */
    public AccommodationCarouselViewData( List<AccommodationViewData> accomodationDatas, Integer visibleItems) {
        super();
        this.accomodationDatas = accomodationDatas;
        this.visibleItems = visibleItems;
    }
    /**
     * @param name
     * @param code
     * @param accomodationDatas
     * @param lookUpTypeCode
     * @param visibleItems
     */
    public AccommodationCarouselViewData(String name, String code, List<AccommodationViewData> accomodationDatas, String lookUpTypeCode, Integer visibleItems) {
        super();
        this.name = name;
        this.code = code;
        this.accomodationDatas = accomodationDatas;
        this.lookUpTypeCode = lookUpTypeCode;
        this.visibleItems = visibleItems;
    }
    public AccommodationCarouselViewData(String name, String code, List<AccommodationViewData> accomodationDatas) {
        super();
        this.name = name;
        this.code = code;
        this.accomodationDatas = accomodationDatas;
    }

    public AccommodationCarouselViewData() {}

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @return the accomodationDatas
     */
    public List<AccommodationViewData> getAccomodationDatas() {
        return accomodationDatas;
    }
    /**
     * @param accomodationDatas the accomodationDatas to set
     */
    public void setAccomodationDatas(List<AccommodationViewData> accomodationDatas) {
        this.accomodationDatas = accomodationDatas;
    }
    /**
     * @return the lookUpType
     */
    public String getLookUpTypeCode() {
        return lookUpTypeCode;
    }
    /**
     * @return the visibleItems
     */
    public Integer getVisibleItems() {
        return visibleItems;
    }





}
