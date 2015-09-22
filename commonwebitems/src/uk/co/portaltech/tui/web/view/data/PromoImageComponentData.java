/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author veena.pn
 *
 */
public class PromoImageComponentData {

     private List<LocationData> locationViewDataList;
     private Integer    visibleItems;
     private String promoImageTitle;
     private String promoImageDescription1;
     private String promoImageDescription2;
     private String promoImageUrl;
     private String width;
     private String position;

     public PromoImageComponentData() {
            super();
        }

    /**
     * @param visibleItems
     */
    public PromoImageComponentData(Integer visibleItems) {
         this.visibleItems = visibleItems;
    }


    /**
     * @return the locationViewDataList
     */
    public List<LocationData> getLocationViewDataList() {
        return locationViewDataList;
    }

    /**
     * @param locationViewDataList the locationViewDataList to set
     */
    public void setLocationViewDataList(List<LocationData> locationViewDataList) {
        this.locationViewDataList = locationViewDataList;
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

    /**
     * @return the promoImageTitle
     */
    public String getPromoImageTitle() {
        return promoImageTitle;
    }

    /**
     * @param promoImageTitle the promoImageTitle to set
     */
    public void setPromoImageTitle(String promoImageTitle) {
        this.promoImageTitle = promoImageTitle;
    }

    /**
     * @return the promoImageDescription1
     */
    public String getPromoImageDescription1() {
        return promoImageDescription1;
    }

    /**
     * @return the promoImageDescription1
     */
    public String getPromoImageDescription2() {
        return promoImageDescription2;
    }

    /**
     * @param promoImageDescription1 the promoImageDescription1 to set
     */
    public void setPromoImageDescription1(String promoImageDescription1) {
        this.promoImageDescription1 = promoImageDescription1;
    }

    /**
     * @param promoImageDescription2 the promoImageDescription2 to set
     */
    public void setPromoImageDescription2(String promoImageDescription2) {
        this.promoImageDescription2 = promoImageDescription2;
    }

    /**
     * @return the promoImageUrl
     */
    public String getPromoImageUrl() {
        return promoImageUrl;
    }

    /**
     * @param promoImageUrl the promoImageUrl to set
     */
    public void setPromoImageUrl(String promoImageUrl) {
        this.promoImageUrl = promoImageUrl;
    }

    /**
     * @return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }



}
