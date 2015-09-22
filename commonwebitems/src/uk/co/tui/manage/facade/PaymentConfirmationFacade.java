/**
 *
 */
package uk.co.tui.manage.facade;

import uk.co.tui.manage.viewdata.PaymentConfirmationViewData;


/**
 * @author veena.pn
 *
 */
public interface PaymentConfirmationFacade
{
    PaymentConfirmationViewData confirmPartPayment();

    /**
     * @param viewData
     */
    void populateConfirmationStaticContentViewData(PaymentConfirmationViewData viewData);


}
