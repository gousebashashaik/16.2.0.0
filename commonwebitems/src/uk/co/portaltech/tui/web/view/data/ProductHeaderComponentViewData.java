/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author venkataharish.k
 *
 */
public class ProductHeaderComponentViewData {

    private String  featureTitle;
     private String  name;
    private String  imageUrl;
    private String  description;
    private String videoUrl;
    /**
     * @return the videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }
    /**
     * @param videoUrl the videoUrl to set
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    /**
     * @return the featureTitle
     */
    public String getFeatureTitle() {
        return featureTitle;
    }
    /**
     * @param featureTitle the featureTitle to set
     */
    public void setFeatureTitle(String featureTitle) {
        this.featureTitle = featureTitle;
    }
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
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }
    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

}
