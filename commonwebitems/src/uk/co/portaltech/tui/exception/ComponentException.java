package uk.co.portaltech.tui.exception;


public class ComponentException extends Exception
{

    public ComponentException(final String message)
    {
        super(message);
    }

    public ComponentException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
