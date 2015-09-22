/**
 *
 */
package uk.co.tui.manage.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.travel.domain.manage.request.UpdatePaymentRequest;
import uk.co.travel.domain.manage.response.UpdatePaymentResponse;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.exception.TUIBusinessException;
import uk.co.tui.manage.facade.PaymentConfirmationFacade;
import uk.co.tui.manage.services.exception.AmendNCancelServiceException;
import uk.co.tui.manage.services.inventory.UpdatePaymentService;
import uk.co.tui.manage.viewdata.PaymentConfirmationViewData;
import uk.co.tui.th.book.view.data.ConfirmationStaticContentViewData;


/**
 * @author veena.pn
 *
 */
public class PaymentConfirmationFacadeImpl implements PaymentConfirmationFacade
{

    @Resource
    private UpdatePaymentService updatePaymentService;

    /** The Constant LOGGER. */
    private static final TUILogUtils LOGGER = new TUILogUtils("PaymentConfirmationFacadeImpl");

    @Resource
    private Populator<UpdatePaymentResponse, PaymentConfirmationViewData> paymentConfirmationPopulator;

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /*
     * (non-Javadoc)
     *
     * @see uk.co.tui.manage.facade.PaymentConfirmationFacade#confirmPartPayment()
     */
    @Override
    public PaymentConfirmationViewData confirmPartPayment()
    {
        final UpdatePaymentRequest updatePaymentRequest = new UpdatePaymentRequest();
        final PaymentConfirmationViewData paymentConfirmationViewData = new PaymentConfirmationViewData();
        try
        {
            final UpdatePaymentResponse response = updatePaymentService.updatePayment(updatePaymentRequest);
            paymentConfirmationViewData.setPaymentInfo(updatePaymentRequest.getPaymentInfo());

            paymentConfirmationPopulator.populate(response, paymentConfirmationViewData);
        }

        catch (final AmendNCancelServiceException e)
        {
            LOGGER.debug("AmendNCancelServiceException in  PaymentConfirmationFacade- " + e.getMessage(), e);


            paymentConfirmationViewData.setPaymentFailure(true);

        }

        catch (final TUIBusinessException e)
        {
            LOGGER.debug("TUIBusinessException in PaymentConfirmationFacade- " + e.getMessage(), e);

            paymentConfirmationViewData.setPaymentFailure(true);

        }
        return paymentConfirmationViewData;
    }

    /**
     * Populate confirmation static content view data.
     *
     * @param viewData
     *           the view data
     */
    @Override
    public void populateConfirmationStaticContentViewData(final PaymentConfirmationViewData viewData)
    {
        final ConfirmationStaticContentViewData confirmationStaticContentViewData = new ConfirmationStaticContentViewData();
        confirmationStaticContentViewData.setConfirmationContentMap(staticContentServ.getManagePaymentConfirmationPageContents());
        viewData.setConfirmationStaticContentViewData(confirmationStaticContentViewData);
    }

}
