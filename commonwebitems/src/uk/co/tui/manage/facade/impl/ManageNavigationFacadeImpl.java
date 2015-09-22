/**
 *
 */
package uk.co.tui.manage.facade.impl;


import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import uk.co.portaltech.tui.exception.TUIBusinessException;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.SessionObjectKeys;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.exception.PackageNotInCartException;
import uk.co.tui.book.payment.request.PaymentRequest;
import uk.co.tui.book.services.payment.PackagePaymentService;
import uk.co.tui.manage.facade.ManageNavigationFacade;
import uk.co.tui.manage.security.services.ManageSessionService;
import uk.co.tui.manage.services.exception.SessionTimeOutException;


/**
 * @author premkumar.nd
 *
 */
public class ManageNavigationFacadeImpl implements ManageNavigationFacade
{

    /**
     *
     */
    private static final String PACKAGE_NOT_IN_CART_EXCEPTION = "PackageNotInCartException";

    private static final String SESSION_BOOKINGSEARCHCRITERIA = "bookingsearchcriteria";

    @Resource
    private ManageSessionService manageSessionService;

    @Resource
    private PackageCartService packageCartService;

    @Resource
    private SessionService sessionService;

    @Resource
    private PackagePaymentService managePackagePaymentService;


    private static final Logger LOG=Logger.getLogger(ManageNavigationFacade.class);

    /*
     * (non-Javadoc)
     *
     * @see uk.co.tui.manage.facade.NavigationFacade#fetchNextPage(java.lang.String)
     */
    @Override
    public String fetchNextPage(final String step)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.tui.manage.facade.NavigationFacade#navigate()
     */
    @Override
    public void navigate() throws TUIBusinessException
    {
        checkModelExistence();

    }

    /**
     * Check cart existence.
     */
    private void checkModelExistence()
    {
        try
        {
            if (packageCartService.getBasePackage() == null)
            {
                throw new SessionTimeOutException("140003", "Session is expired!");
            }
        }

        catch (final PackageNotInCartException pncart)
        {
            LOG.error(PACKAGE_NOT_IN_CART_EXCEPTION, pncart);
            throw new SessionTimeOutException("140003", "Session is expired!");

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.tui.manage.facade.ManageNavigationFacade#exitBooking()
     */
    @Override
    public void exitBooking()
    {
        try
        {
            final BasePackage packageholiday = packageCartService.getBasePackage();
            if (packageholiday != null)
            {
                manageSessionService.removeBookingReferenceFromSession(packageholiday.getBookingRefNum());
                sessionService.removeAttribute(SESSION_BOOKINGSEARCHCRITERIA);
                packageCartService.remove();
            }
        }
        catch (final PackageNotInCartException pnc)
        {
            LOG.error(PACKAGE_NOT_IN_CART_EXCEPTION,pnc);
            throw new SessionTimeOutException("140003", "Package Not in Cart!");

        }
    }


    /**
     * When the user navigates from payment page back to any relevant RnP
     * pages, the generated payment token is purged and the payment request will
     * be removed from the session.
     */
    @Override
    public void removePaymentRequest() {
        if (sessionService.getAttribute(SessionObjectKeys.PAYMENTREQUEST) != null) {
            try {
                managePackagePaymentService
                        .purgePaymentToken((PaymentRequest) sessionService
                                .getAttribute(SessionObjectKeys.PAYMENTREQUEST));
            } catch (final uk.co.tui.exception.TUIBusinessException e) {
                LOG.error("Remove Payment Request Exception", e);
            }
            sessionService.removeAttribute(SessionObjectKeys.PAYMENTREQUEST);
        }
    }


}
