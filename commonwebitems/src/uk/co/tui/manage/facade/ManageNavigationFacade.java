/**
 *
 */
package uk.co.tui.manage.facade;

import uk.co.portaltech.tui.exception.TUIBusinessException;


/**
 * The Interface NavigationFacade.
 *
 * @author samantha.gd
 */
public interface ManageNavigationFacade
{

    /**
     * Fetch next page.
     *
     * @param step
     *           the step
     * @return the string
     */
    String fetchNextPage(String step);


    /**
     * @throws TUIBusinessException
     */
    void navigate() throws TUIBusinessException;

    void exitBooking();


    /**
     * When the user navigates from payment page back to any relevant RnP
     * pages, the generated payment token is purged and the payment request will
     * be removed from the session.
     */
    void removePaymentRequest();

}
