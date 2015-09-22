/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.List;

import uk.co.tui.book.domain.lite.BookingType;


/**
 * @author veena.pn
 *
 */
public class BookingHistoryViewData
{


    private BookingType bookingChangeType;

    private List<PaymentReferenceViewData> paymentReferenceViewDatas;

    private AgentViewData agentViewData;

    /**
     * @return the paymentReferenceViewDatas
     */
    public List<PaymentReferenceViewData> getPaymentReferenceViewDatas()
    {
        return paymentReferenceViewDatas;
    }

    /**
     * @param paymentReferenceViewDatas the paymentReferenceViewDatas to set
     */
    public void setPaymentReferenceViewDatas(List<PaymentReferenceViewData> paymentReferenceViewDatas)
    {
        this.paymentReferenceViewDatas = paymentReferenceViewDatas;
    }


    /**
     * @return the bookingChangeType
     */
    public BookingType getBookingChangeType()
    {
        return bookingChangeType;
    }

    /**
     * @param bookingChangeType
     *           the bookingChangeType to set
     */
    public void setBookingChangeType(final BookingType bookingChangeType)
    {
        this.bookingChangeType = bookingChangeType;
    }

    /**
     * @return the agentViewData
     */
    public AgentViewData getAgentViewData()
    {
        return agentViewData;
    }

    /**
     * @param agentViewData
     *           the agentViewData to set
     */
    public void setAgentViewData(final AgentViewData agentViewData)
    {
        this.agentViewData = agentViewData;
    }





}
