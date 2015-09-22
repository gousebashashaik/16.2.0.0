/**
 *
 */
package uk.co.tui.th.book.view.data;



/**
 * The Class MediaContents.
 *
 * @author amaresh.d
 */
public class MediaContents {

    private String mediaQualifier;
    private String mediaCode;
    private MediaTypeFormat mediaFormatType;
    private String mediaUrl;
    /**
     * @return the mediaQualifier
     */
    public String getMediaQualifier() {
        return mediaQualifier;
    }
    /**
     * @param mediaQualifier the mediaQualifier to set
     */
    public void setMediaQualifier(String mediaQualifier) {
        this.mediaQualifier = mediaQualifier;
    }
    /**
     * @return the mediaCode
     */
    public String getMediaCode() {
        return mediaCode;
    }
    /**
     * @param mediaCode the mediaCode to set
     */
    public void setMediaCode(String mediaCode) {
        this.mediaCode = mediaCode;
    }
    /**
     * @return the mediaFormatType
     */
    public MediaTypeFormat getMediaFormatType() {
        return mediaFormatType;
    }
    /**
     * @param mediaFormatType the mediaFormatType to set
     */
    public void setMediaFormatType(MediaTypeFormat mediaFormatType) {
        this.mediaFormatType = mediaFormatType;
    }
    /**
     * @return the mediaUrl
     */
    public String getMediaUrl() {
        return mediaUrl;
    }
    /**
     * @param mediaUrl the mediaUrl to set
     */
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    /**
     * The Enum MediaTypeFormat.
     */
    public enum MediaTypeFormat {

        SMALL("small"),
        MEDIUM("medium"),
        LARGE("large"),
        XTRALARGE("xlarge"),
        UNKNOWN("unknown");

        /** The code. */
        private final String code;

        /**
         * Instantiates a new media type format.
         *
         * @param code the code
         */
        private MediaTypeFormat(final String code) {
            this.code = code;
        }

        /**
         * Gets the code.
         *
         * @return the code
         */
        public String getCode() {
            return this.code;
        }

        /**
         * Find by code.
         *
         * @param code the code
         * @return the media type format
         */
        public static MediaTypeFormat findByCode(final String code) {
            for (MediaTypeFormat format : values()) {
                if (format.getCode().equals(code)) {
                    return format;
                }
            }
            return MediaTypeFormat.UNKNOWN;
        }
    }

}
