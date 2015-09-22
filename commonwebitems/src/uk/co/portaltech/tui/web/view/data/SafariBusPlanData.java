/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author siddalingana.g
 *
 */
public class SafariBusPlanData {

    private String name;
    private String description;
    private MediaViewData galleryImage;
    private List<String> content = new ArrayList<String>();


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
    /**
     * @return the content
     */
    public List<String> getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(List<String> content) {
        this.content = content;
    }


}
