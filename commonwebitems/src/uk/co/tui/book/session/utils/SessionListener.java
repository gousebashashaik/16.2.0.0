/**
 *
 */
package uk.co.tui.book.session.utils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import uk.co.tui.async.logging.TUILogUtils;


/**
 * The listener interface for receiving session events. The class that is interested in processing a session event
 * implements this interface, and the object created with that class is registered with a component using the
 * component's <code>addSessionListener<code> method. When
 * the session event occurs, that object's appropriate
 * method is invoked.
 *
 * @author samantha.gd
 *
 *         This listener class will be responsible for cleaning up stale packages from database when session gets
 *         invalidated; which have been saved to database through cart/order management during book flow
 */
public class SessionListener implements HttpSessionListener
{

    /** The Constant LOG. */

    private static final TUILogUtils LOG = new TUILogUtils("SessionListener");

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http .HttpSessionEvent)
     */
    @Override
    public void sessionCreated(final HttpSessionEvent arg0)
    {
        LOG.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<< Session created >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet .http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(final HttpSessionEvent arg0)
    {
        LOG.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<< Session destroyed >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }
}
