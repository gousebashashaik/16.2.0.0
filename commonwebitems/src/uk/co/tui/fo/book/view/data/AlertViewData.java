/**
 *
 */
package uk.co.tui.fo.book.view.data;

/**
 * @author pradeep.as
 *
 */
public class AlertViewData {

    private String messageHeader;

    private String messageText;

    private String alertLevel;

    /**
     * @return the messageHeader
     */
    public String getMessageHeader() {
        return messageHeader;
    }

    /**
     * @param messageHeader
     *            the messageHeader to set
     */
    public void setMessageHeader(final String messageHeader) {
        this.messageHeader = messageHeader;
    }

    /**
     * @return the messageText
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * @param messageText
     *            the messageText to set
     */
    public void setMessageText(final String messageText) {
        this.messageText = messageText;
    }

    /**
     * @return the alertLevel
     */
    public String getAlertLevel() {
        return alertLevel;
    }

    /**
     * @param alertLevel
     *            the alertLevel to set
     */
    public void setAlertLevel(final String alertLevel) {
        this.alertLevel = alertLevel;
    }

}
