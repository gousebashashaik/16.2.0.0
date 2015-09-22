/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.commercefacades.product.data.ImageData;

import java.util.List;

public class ProductRangeCollectionViewData {
    private String productName;
    private String productDescription;
    private String productURL;
    private List<ImageData> imageDataList;
    private List<MediaViewData> galleryImages;

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }
    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    /**
     * @return the productDescription
     */
    public String getProductDescription() {
        return productDescription;
    }
    /**
     * @param productDescription the productDescription to set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    /**
     * @return the productURL
     */
    public String getProductURL() {
        return productURL;
    }
    /**
     * @param productURL the productURL to set
     */
    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }
    /**
     * @return the imageDataList
     */
    public List<ImageData> getImageDataList() {
        return imageDataList;
    }
    /**
     * @param imageDataList the imageDataList to set
     */
    public void setImageDataList(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }
    /**
     * @return the mediaData
     */
    /**
     * @return the galleryImages
     */
    public List<MediaViewData> getGalleryImages() {
        return galleryImages;
    }
    /**
     * @param galleryImages the galleryImages to set
     */
    public void setGalleryImages(List<MediaViewData> galleryImages) {
        this.galleryImages = galleryImages;
    }


}
