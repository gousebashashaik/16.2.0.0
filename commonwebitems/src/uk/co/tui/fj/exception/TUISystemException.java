/**
 *
 */
package uk.co.tui.fj.exception;


/**
 * @author raghavendra.dm
 *
 *Class used to handle Tui related system exceptions
 */
public class TUISystemException extends RuntimeException
{

    /** The error code. */
    private final String errorCode;

    /** The custom message. */
    private final String customMessage;

    /** The nested cause. */
    private final Throwable nestedCause;

    /**
     * Constructor used to Instantiate a new TUI system exception with Throwable cause and error code in case of TUI
     * related system failures.
     *
     * @param errorCode
     *           the error code
     * @param errorDescription
     *           the error description
     * @param cause
     *           the cause
     */
    public TUISystemException(final String errorCode, final String errorDescription, final Throwable cause)
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
