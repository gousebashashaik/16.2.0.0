/**
 *
 */
package uk.co.tui.manage.viewdata;

import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PaymentType;


/**
 * @author veena.pn
 *
 */
public class PaymentReferenceViewData
{

    private String paymentDate;

    private PaymentType paymentType;

    private Money amountPaid;

    private String currencyAppendedAmtPaid;


    /**
     * @return the paymentType
     */
    public PaymentType getPaymentType()
    {
        return paymentType;
    }

    /**
     * @param paymentType
     *           the paymentType to set
     */
    public void setPaymentType(final PaymentType paymentType)
    {
        this.paymentType = paymentType;
    }

    /**
     * @return the amountPaid
     */
    public Money getAmountPaid()
    {
        return amountPaid;
    }

    /**
     * @param amountPaid
     *           the amountPaid to set
     */
    public void setAmountPaid(final Money amountPaid)
    {
        this.amountPaid = amountPaid;
    }

    /**
     * @return the paymentDate
     */
    public String getPaymentDate()
    {
        return paymentDate;
    }

    /**
     * @param paymentDate
     *           the paymentDate to set
     */
    public void setPaymentDate(final String paymentDate)
    {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the currencyAppendedAmtPaid
     */
    public String getCurrencyAppendedAmtPaid()
    {
        return currencyAppendedAmtPaid;
    }

    /**
     * @param currencyAppendedAmtPaid the currencyAppendedAmtPaid to set
     */
    public void setCurrencyAppendedAmtPaid(String currencyAppendedAmtPaid)
    {
        this.currencyAppendedAmtPaid = currencyAppendedAmtPaid;
    }

}
