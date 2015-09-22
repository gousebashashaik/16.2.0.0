package uk.co.portaltech.tui.exception;

/**
 *
 */
public class NoSuchComponentException extends ComponentException
{

    public NoSuchComponentException(final String componentId)
    {
        super("Could not find component with id: " + componentId);
    }

    public NoSuchComponentException(final String componentId, final Throwable cause)
    {
        super("Could not find component with id: " + componentId, cause);
    }
}
