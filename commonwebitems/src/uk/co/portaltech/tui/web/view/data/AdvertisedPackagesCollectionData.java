/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Collection;
import java.util.List;

/**
 * @author niranjani.r
 *
 */
public class AdvertisedPackagesCollectionData {

    private String accommodationName;
    private String price;
    private List<ProductRangeViewData> productRanges;
    private String accommUrl;
    private Collection<MediaViewData> galleryImages;
    private MediaViewData thumbnail;
    /**
     * @return the accommodationName
     */
    public String getAccommodationName() {
        return accommodationName;
    }
    /**
     * @param accommodationName the accommodationName to set
     */
    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }
    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }
    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }
    /**
     * @return the productRanges
     */
    public List<ProductRangeViewData> getProductRanges() {
        return productRanges;
    }
    /**
     * @param productRanges the productRanges to set
     */
    public void setProductRanges(List<ProductRangeViewData> productRanges) {
        this.productRanges = productRanges;
    }
    /**
     * @return the accommUrl
     */
    public String getAccommUrl() {
        return accommUrl;
    }
    /**
     * @param accommUrl the accommUrl to set
     */
    public void setAccommUrl(String accommUrl) {
        this.accommUrl = accommUrl;
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
    public void setGalleryImages(Collection<MediaViewData> galleryImages) {
        this.galleryImages = galleryImages;
    }
    /**
     * @return the thumbnail
     */
    public MediaViewData getThumbnail() {
        return thumbnail;
    }
    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(MediaViewData thumbnail) {
        this.thumbnail = thumbnail;
    }


}
