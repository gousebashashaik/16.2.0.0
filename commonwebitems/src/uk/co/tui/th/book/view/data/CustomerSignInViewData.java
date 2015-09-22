/**
 *
 */
package uk.co.tui.th.book.view.data;




/**
 * The Class CustomerSignInViewData.
 *
 * @author samantha.gd
 */
class CustomerSignInViewData
{

    /** The customer logged in. */
    private boolean customerLoggedIn;

    /** The email id. */
    private String emailId;

    /** The alert. */
    private AlertViewData alert;

    /** Flag indicates if the passenger undergone sign in flow */
    private boolean signInAttempted;

    /**
     * Checks if is customer logged in.
     *
     * @return the customerLoggedIn
     */
    public boolean isCustomerLoggedIn()
    {
        return customerLoggedIn;
    }

    /**
     * Sets the customer logged in.
     *
     * @param customerLoggedIn
     *           the customerLoggedIn to set
     */
    public void setCustomerLoggedIn(final boolean customerLoggedIn)
    {
        this.customerLoggedIn = customerLoggedIn;
    }

    /**
     * Gets the email id.
     *
     * @return the emailId
     */
    public String getEmailId()
    {
        return emailId;
    }

    /**
     * Sets the email id.
     *
     * @param emailId
     *           the emailId to set
     */
    public void setEmailId(final String emailId)
    {
        this.emailId = emailId;
    }


    /**
     * Gets the alert.
     *
     * @return the alert
     */
    public AlertViewData getAlert()
    {
        return alert;
    }


    /**
     * Sets the alert.
     *
     * @param alert
     *           the new alert
     */
    public void setAlert(final AlertViewData alert)
    {
        this.alert = alert;
    }

    /**
     * @return the signInAttempted
     */
    public boolean isSignInAttempted()
    {
        return signInAttempted;
    }

    /**
     * @param signInAttempted
     *           the signInAttempted to set
     */
    public void setSignInAttempted(final boolean signInAttempted)
    {
        this.signInAttempted = signInAttempted;
    }

}
