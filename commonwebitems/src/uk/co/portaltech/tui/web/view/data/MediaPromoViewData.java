/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author kavita.na
 *
 */
public class MediaPromoViewData {
     private String componentId;
        private List<MediaViewData> galleryImages;

        /**
         * @param componentId
         * @param galleryImages
         */
        public MediaPromoViewData(String componentId, List<MediaViewData> galleryImages) {
            super();
            this.componentId = componentId;
            this.galleryImages = galleryImages;
        }
        /**
         * @return the componentId
         */
        public String getComponentId() {
            return componentId;
        }
        /**
         * @return the mediaDatas
         */
        public List<MediaViewData> getGalleryImages() {
            return galleryImages;
        }
}
