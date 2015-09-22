/**
 *
 */
package uk.co.tui.book.data;

/**
 *
 *
 */
public class CommunicationPreferencesCriteria {

    private boolean communicateByEmail;

    private boolean communicateByPhone;

    private boolean communicateByPost;

    private boolean chkThirdPartyMarketingAllowed;

    /**
     * @return the communicateByEmail
     */
    public boolean isCommunicateByEmail() {
        return communicateByEmail;
    }

    /**
     * @param communicateByEmail the communicateByEmail to set
     */
    public void setCommunicateByEmail(boolean communicateByEmail) {
        this.communicateByEmail = communicateByEmail;
    }

    /**
     * @return the communicateByPhone
     */
    public boolean isCommunicateByPhone() {
        return communicateByPhone;
    }

    /**
     * @param communicateByPhone the communicateByPhone to set
     */
    public void setCommunicateByPhone(boolean communicateByPhone) {
        this.communicateByPhone = communicateByPhone;
    }

    /**
     * @return the communicateByPost
     */
    public boolean isCommunicateByPost() {
        return communicateByPost;
    }

    /**
     * @param communicateByPost the communicateByPost to set
     */
    public void setCommunicateByPost(boolean communicateByPost) {
        this.communicateByPost = communicateByPost;
    }

    /**
     * @return the chkThirdPartyMarketingAllowed
     */
    public boolean isChkThirdPartyMarketingAllowed() {
        return chkThirdPartyMarketingAllowed;
    }

    /**
     * @param chkThirdPartyMarketingAllowed the chkThirdPartyMarketingAllowed to set
     */
    public void setChkThirdPartyMarketingAllowed(
            boolean chkThirdPartyMarketingAllowed) {
        this.chkThirdPartyMarketingAllowed = chkThirdPartyMarketingAllowed;
    }




}
