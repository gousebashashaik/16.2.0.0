/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Collection;

/**
 * @author niranjani.r
 *
 */
public class GeoLocationPromoData {

    private String locationName;
    private String promoTitle;
    private String linkName;
    private String locationLink;
    private Collection<MediaViewData> galleryImages;
    private String position;
    private String width;


    /**
     * @return the position
     */
    public  String getPosition() {
        return position;
    }
    /**
     * @param position the position to set
     */
    public  void setPosition( final String position) {
        this.position = position;
    }
    /**
     * @return the width
     */
    public  String getWidth() {
        return width;
    }
    /**
     * @param width the width to set
     */
    public  void setWidth(final String width) {
        this.width = width;
    }
    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }
    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(final String locationName) {
        this.locationName = locationName;
    }
    /**
     * @return the promoTitle
     */
    public String getPromoTitle() {
        return promoTitle;
    }
    /**
     * @param promoTitle the promoTitle to set
     */
    public void setPromoTitle(final String promoTitle) {
        this.promoTitle = promoTitle;
    }
    /**
     * @return the linkName
     */
    public String getLinkName() {
        return linkName;
    }
    /**
     * @param linkName the linkName to set
     */
    public void setLinkName(final String linkName) {
        this.linkName = linkName;
    }
    /**
     * @return the locationLink
     */
    public String getLocationLink() {
        return locationLink;
    }
    /**
     * @param locationLink the locationLink to set
     */
    public void setLocationLink(final String locationLink) {
        this.locationLink = locationLink;
    }
    /**
     * @return the galleryImages
     */
    public Collection<MediaViewData> getGalleryImages() {
        return galleryImages;
    }
    /**
     * @param galleryImages the galleryImages to set
     */
    public void setGalleryImages(final Collection<MediaViewData> galleryImages) {
        this.galleryImages = galleryImages;
    }


}
