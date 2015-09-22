/**
 *
 */
package uk.co.tui.fj.book.view.data;

/**
 * @author siddharam.r
 *
 */
public class CommunicationPreferencesViewData {
    private boolean byPhone;

    private boolean byEmail;

    private boolean thirdPartySharing;

    private boolean receiveCommunication;

    private boolean byPost;

    /**
     * @return the byPhone
     */
    public boolean isByPhone() {
        return byPhone;
    }

    /**
     * @param byPhone the byPhone to set
     */
    public void setByPhone(boolean byPhone) {
        this.byPhone = byPhone;
    }

    /**
     * @return the byEmail
     */
    public boolean isByEmail() {
        return byEmail;
    }

    /**
     * @param byEmail the byEmail to set
     */
    public void setByEmail(boolean byEmail) {
        this.byEmail = byEmail;
    }

    /**
     * @return the thirdPartySharing
     */
    public boolean isThirdPartySharing() {
        return thirdPartySharing;
    }

    /**
     * @param thirdPartySharing the thirdPartySharing to set
     */
    public void setThirdPartySharing(boolean thirdPartySharing) {
        this.thirdPartySharing = thirdPartySharing;
    }

    /**
     * @return the receiveCommunication
     */
    public boolean isReceiveCommunication() {
        return receiveCommunication;
    }

    /**
     * @param receiveCommunication the receiveCommunication to set
     */
    public void setReceiveCommunication(boolean receiveCommunication) {
        this.receiveCommunication = receiveCommunication;
    }

    /**
     * @return the byPost
     */
    public boolean isByPost() {
        return byPost;
    }

    /**
     * @param byPost the byPost to set
     */
    public void setByPost(boolean byPost) {
        this.byPost = byPost;
    }

}
