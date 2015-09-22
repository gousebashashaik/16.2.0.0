/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.io.Serializable;

/**
 * The Class RoomImage.
 * 
 * @author samantha.gd
 */
public class RoomImage implements Serializable {

    /** The url. */
    private String url;

    /** The main image. */
    private boolean mainImage;

    /** The alt text. */
    private String altText;

    /** The code. */
    private String code;

    /** The size. */
    private String size;

    /** The description. */
    private String description;

    /**
     * Gets the image url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the image url.
     * 
     * @param url
     *            the new url
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * Checks if is main image.
     * 
     * @return true, if is main image
     */
    public boolean isMainImage() {
        return mainImage;
    }

    /**
     * Sets the main image.
     * 
     * @param mainImage
     *            the new main image
     */
    public void setMainImage(final boolean mainImage) {
        this.mainImage = mainImage;
    }

    /**
     * @return the altText
     */
    public String getAltText() {
        return altText;
    }

    /**
     * @param altText
     *            the altText to set when image not available
     */
    public void setAltText(final String altText) {
        this.altText = altText;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(final String size) {
        this.size = size;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

}
