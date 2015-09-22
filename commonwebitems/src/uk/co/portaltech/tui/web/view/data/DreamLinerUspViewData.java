/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author narendra.bm
 *
 */
public class DreamLinerUspViewData {
    private String uspName;
    private String uspDesc;
    private String code;
    private List<MediaViewData> galleryImages;

    /**
     * @return the uspName
     */
    public String getUspName() {
        return uspName;
    }

    /**
     * @param uspName the uspName to set
     */
    public void setUspName(String uspName) {
        this.uspName = uspName;
    }

    /**
     * @return the uspDesc
     */
    public String getUspDesc() {
        return uspDesc;
    }

    /**
     * @param uspDesc the uspDesc to set
     */
    public void setUspDesc(String uspDesc) {
        this.uspDesc = uspDesc;
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
