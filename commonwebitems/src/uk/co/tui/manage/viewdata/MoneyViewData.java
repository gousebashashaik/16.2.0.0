/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * @author veena.pn
 *
 */
public class MoneyViewData
{

    private BigDecimal amount;

    private Integer precisionInteger;

    private Currency currency;

    /**
     * @return the amount
     */
    public BigDecimal getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *           the amount to set
     */
    public void setAmount(final BigDecimal amount)
    {
        this.amount = amount;
    }

    /**
     * @return the precisionInteger
     */
    public Integer getPrecisionInteger()
    {
        return precisionInteger;
    }

    /**
     * @param precisionInteger
     *           the precisionInteger to set
     */
    public void setPrecisionInteger(final Integer precisionInteger)
    {
        this.precisionInteger = precisionInteger;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency()
    {
        return currency;
    }

    /**
     * @param currency
     *           the currency to set
     */
    public void setCurrency(final Currency currency)
    {
        this.currency = currency;
    }


}
