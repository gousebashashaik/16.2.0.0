/**
 *
 */
package uk.co.tui.manage.viewdata;

/**
 * @author sumit.ks
 *
 */
public class ErrorViewData
{
    private boolean error;
    private String errorCode;
    private String errorMessage;

    /**
     * @return the error
     */
    public boolean isError()
    {
        return error;
    }

    /**
     * @param error
     *           the error to set
     */
    public void setError(final boolean error)
    {
        this.error = error;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode()
    {
        return errorCode;
    }

    /**
     * @param errorCode
     *           the errorCode to set
     */
    public void setErrorCode(final String errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * @param errorMessage
     *           the errorMessage to set
     */
    public void setErrorMessage(final String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


}
