/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.Date;
import java.util.Map;

import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.PriceType;


/**
 * @author sunil.bd
 *
 */
public class PriceViewData
{

    private String code;

    private String description;

    private Money rate;

    private Money amount;

    private Integer quantity;

    private PriceType priceType;

    private Map<String, String> keyValuePair;



    private String codeType;
    private Date date;



    /**
     * @return the codeType
     */
    public String getCodeType()
    {
        return codeType;
    }

    /**
     * @param codeType
     *           the codeType to set
     */
    public void setCodeType(final String codeType)
    {
        this.codeType = codeType;
    }

    /**
     * @return the date
     */
    public Date getDate()
    {
        if(this.date!=null){
            return new Date(this.date.getTime());
        }
        return null;
    }

    /**
     * @param date
     *           the date to set
     */
    public void setDate(final Date date) {
        if (date != null) {
            this.date = new Date(this.date.getTime());
        }
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *           the code to set
     */
    public void setCode(final String code)
    {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *           the description to set
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return the rate
     */
    public Money getRate()
    {
        return rate;
    }

    /**
     * @param rate
     *           the rate to set
     */
    public void setRate(final Money rate)
    {
        this.rate = rate;
    }

    /**
     * @return the amount
     */
    public Money getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *           the amount to set
     */
    public void setAmount(final Money amount)
    {
        this.amount = amount;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity()
    {
        return quantity;
    }

    /**
     * @param quantity
     *           the quantity to set
     */
    public void setQuantity(final Integer quantity)
    {
        this.quantity = quantity;
    }

    /**
     * @return the priceType
     */
    public PriceType getPriceType()
    {
        return priceType;
    }

    /**
     * @param priceType
     *           the priceType to set
     */
    public void setPriceType(final PriceType priceType)
    {
        this.priceType = priceType;
    }

    /**
     * @return the keyValuePair
     */
    public Map<String, String> getKeyValuePair()
    {
        return keyValuePair;
    }

    /**
     * @param keyValuePair
     *           the keyValuePair to set
     */
    public void setKeyValuePair(final Map<String, String> keyValuePair)
    {
        this.keyValuePair = keyValuePair;
    }



}
