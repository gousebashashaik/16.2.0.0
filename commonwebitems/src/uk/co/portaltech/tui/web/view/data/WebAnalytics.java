/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 *
 */
public class WebAnalytics {

    private String key;

    private String value;

    /**
     *
     */
    public WebAnalytics(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
