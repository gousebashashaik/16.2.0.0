/**
 *
 */
package uk.co.tui.cr.exception;


/**
 * @author raghavendra.dm
 *
 *         Class used to handle TUI related business exceptions
 */
public class TUIBusinessException extends Exception
{

    /** The error code. */
    private final String errorCode;

    /** The custom message. */
    private final String customMessage;

    /** The nested cause. */
    private final Throwable nestedCause;

    /**
     * This constructor is used to set the TUI business related exception with error code and throwable cause
     *
     * @param errorCode
     *           the error code
     * @param errorDescription
     *           Error Description
     * @param cause
     *           the cause
     */
    public TUIBusinessException(final String errorCode, final String errorDescription, final Throwable cause)
    {
        super(cause);
        this.errorCode = errorCode;
        this.nestedCause = cause;
        this.customMessage = errorDescription;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode()
    {
        return errorCode;
    }


    /**
     * @return the customMessage
     */
    public String getCustomMessage()
    {
        return customMessage;
    }


    /**
     * @return the nestedCause
     */
    public Throwable getNestedCause()
    {
        return nestedCause;
    }


}
