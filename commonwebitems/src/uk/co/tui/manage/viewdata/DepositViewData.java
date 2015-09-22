/**
 *
 */
package uk.co.tui.manage.viewdata;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.domain.lite.Price;

/**
 * @author veena.pn
 *
 */
public class DepositViewData
{

   private String balanceDueDate;

   private Price outstandingBalance;

   private Price baseDepositAmount;

   private String depositDueDate;

   private Boolean applicable;

   private Price depositAmount;

   private String depositType;

   private Boolean available;

   private Boolean selected;

   /** The currency appended price. */
   private String currencyAppendedDepositPrice = StringUtils.EMPTY;

   /**
    * @return the outstandingBalance
    */
   public Price getOutstandingBalance()
   {
      return outstandingBalance;
   }

   /**
    * @param outstandingBalance the outstandingBalance to set
    */
   public void setOutstandingBalance(final Price outstandingBalance)
   {
      this.outstandingBalance = outstandingBalance;
   }

   /**
    * @return the baseDepositAmount
    */
   public Price getBaseDepositAmount()
   {
      return baseDepositAmount;
   }

   /**
    * @param baseDepositAmount the baseDepositAmount to set
    */
   public void setBaseDepositAmount(final Price baseDepositAmount)
   {
      this.baseDepositAmount = baseDepositAmount;
   }

   /**
    * @return the applicable
    */
   public Boolean getApplicable()
   {
      return applicable;
   }

   /**
    * @param applicable the applicable to set
    */
   public void setApplicable(final Boolean applicable)
   {
      this.applicable = applicable;
   }

   /**
    * @return the depositAmount
    */
   public Price getDepositAmount()
   {
      return depositAmount;
   }

   /**
    * @param depositAmount the depositAmount to set
    */
   public void setDepositAmount(final Price depositAmount)
   {
      this.depositAmount = depositAmount;
   }

   /**
    * @return the depositType
    */
   public String getDepositType()
   {
      return depositType;
   }

   /**
    * @param depositType the depositType to set
    */
   public void setDepositType(final String depositType)
   {
      this.depositType = depositType;
   }

   /**
    * @return the available
    */
   public Boolean getAvailable()
   {
      return available;
   }

   /**
    * @param available the available to set
    */
   public void setAvailable(final Boolean available)
   {
      this.available = available;
   }

   /**
    * @return the selected
    */
   public Boolean getSelected()
   {
      return selected;
   }

   /**
    * @param selected the selected to set
    */
   public void setSelected(final Boolean selected)
   {
      this.selected = selected;
   }

   /**
    * @return the balanceDueDate
    */
   public String getBalanceDueDate()
   {
      return balanceDueDate;
   }

   /**
    * @param balanceDueDate the balanceDueDate to set
    */
   public void setBalanceDueDate(final String balanceDueDate)
   {
      this.balanceDueDate = balanceDueDate;
   }

   /**
    * @return the depositDueDate
    */
   public String getDepositDueDate()
   {
      return depositDueDate;
   }

   /**
    * @param depositDueDate the depositDueDate to set
    */
   public void setDepositDueDate(final String depositDueDate)
   {
      this.depositDueDate = depositDueDate;
   }

   /**
    * @param currencyAppendedPrice the currencyAppendedPrice to set
    */
   public void setCurrencyAppendedDepositPrice(final String currencyAppendedDepositPrice)
   {
      this.currencyAppendedDepositPrice = currencyAppendedDepositPrice;
   }

   /**
    * @return the price
    */
   public String getCurrencyAppendedDepositPrice()
   {
      return this.currencyAppendedDepositPrice;
   }

}
