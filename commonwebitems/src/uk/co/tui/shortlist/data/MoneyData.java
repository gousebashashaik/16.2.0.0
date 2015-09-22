/**
 *
 */
package uk.co.tui.shortlist.data;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author akhileshvarma.d
 *
 */
public class MoneyData
{
   private Currency currency;

   private int precision;

   private BigDecimal value;

   /**
    * @return the currency
    */
   public Currency getCurrency()
   {
      return currency;
   }

   /**
    * @param currency the currency to set
    */
   public void setCurrency(final Currency currency)
   {
      this.currency = currency;
   }

   /**
    * @return the precision
    */
   public int getPrecision()
   {
      return precision;
   }

   /**
    * @param precision the precision to set
    */
   public void setPrecision(final int precision)
   {
      this.precision = precision;
   }

   /**
    * @return the value
    */
   public BigDecimal getValue()
   {
      return value;
   }

   /**
    * @param value the value to set
    */
   public void setValue(final BigDecimal value)
   {
      this.value = value;
   }

}
