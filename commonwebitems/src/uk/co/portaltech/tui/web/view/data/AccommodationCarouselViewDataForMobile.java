/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author gopinath.n
 *
 */
public class AccommodationCarouselViewDataForMobile {
    private String name;
    private String code;
    private List<AccommodationViewDataForMobile> accomodationDatas;
    private String lookUpTypeCode;
    private Integer visibleItems;

    public AccommodationCarouselViewDataForMobile(String name, String code,
            List<AccommodationViewDataForMobile> accomodationDatas,
            String lookUpTypeCode, Integer visibleItems) {
        super();
        this.name = name;
        this.code = code;
        this.accomodationDatas = accomodationDatas;
        this.lookUpTypeCode = lookUpTypeCode;
        this.visibleItems = visibleItems;
    }

    public AccommodationCarouselViewDataForMobile(String name, String code,
            List<AccommodationViewDataForMobile> accomodationDatas) {
        super();
        this.name = name;
        this.code = code;
        this.accomodationDatas = accomodationDatas;
    }

    public AccommodationCarouselViewDataForMobile() {
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
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
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the accomodationDatas
     */
    public List<AccommodationViewDataForMobile> getAccomodationDatas() {
        return accomodationDatas;
    }

    /**
     * @param accomodationDatas
     *            the accomodationDatas to set
     */
    public void setAccomodationDatas(
            List<AccommodationViewDataForMobile> accomodationDatas) {
        this.accomodationDatas = accomodationDatas;
    }

    /**
     * @return the lookUpTypeCode
     */
    public String getLookUpTypeCode() {
        return lookUpTypeCode;
    }

    /**
     * @param lookUpTypeCode
     *            the lookUpTypeCode to set
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
     * @param visibleItems
     *            the visibleItems to set
     */
    public void setVisibleItems(Integer visibleItems) {
        this.visibleItems = visibleItems;
    }

}
