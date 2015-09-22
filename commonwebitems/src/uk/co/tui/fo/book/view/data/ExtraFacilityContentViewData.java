/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.CollectionUtils;

import uk.co.portaltech.tui.web.view.data.MediaViewData;


/**
 * @author thyagaraju.e
 *
 */
public class ExtraFacilityContentViewData {

    private Map<String, String>    extraFacilityContent ;

    private  List<MediaViewData>           galleryImages;

    /**
     * @return the extraFacilityContent
     */
    public Map<String, String> getExtraFacilityContent() {
        return extraFacilityContent;
    }

    /**
     * Puts all the content.
     */
    public void putExtraFacilityContent(Map<String, String> extraFacilityContent) {
        if (CollectionUtils.isEmpty(this.extraFacilityContent)) {
            this.extraFacilityContent =  new ConcurrentHashMap<String, String>();
        }
        this.extraFacilityContent.putAll(extraFacilityContent);
    }

    /**
     * @return the galleryImages
     */
    public List<MediaViewData> getGalleryImages() {

        if (CollectionUtils.isEmpty(this.galleryImages)) {
            this.galleryImages = new ArrayList<MediaViewData>();
        }
        return galleryImages;

    }

    /**
     * @param galleryImages the galleryImages to set
     */
    public void setGalleryImages(List<MediaViewData> galleryImages) {
        this.galleryImages = galleryImages;
    }

}
