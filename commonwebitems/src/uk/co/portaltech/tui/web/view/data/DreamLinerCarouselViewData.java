/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author narendra.bm
 *
 */
public class DreamLinerCarouselViewData {
    private List<DreamLinerUspViewData> dreamLinerUsps;
    private MediaViewData galleryImage;
    /**
     * @return the dreamLinerUsps
     */
    public List<DreamLinerUspViewData> getDreamLinerUsps() {
        return dreamLinerUsps;
    }
    /**
     * @param dreamLinerUsps the dreamLinerUsps to set
     */
    public void setDreamLinerUsps(List<DreamLinerUspViewData> dreamLinerUsps) {
        this.dreamLinerUsps = dreamLinerUsps;
    }
    /**
     * @return the galleryImage
     */
    public MediaViewData getGalleryImage() {
        return galleryImage;
    }
    /**
     * @param galleryImage the galleryImage to set
     */
    public void setGalleryImage(MediaViewData galleryImage) {
        this.galleryImage = galleryImage;
    }

}
