/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class holds the Price breakdown view data.
 *
 * @author madhumathi.m
 *
 */
public class PriceBreakDownViewData
{

   /**
    * Holds the description of the facility, if one sent.
    */
   private String description = StringUtils.EMPTY;

   /**
    * Holds the price of extra.
    */
   private BigDecimal price;

   /** The currency appended price. */
   private String currencyAppendedPrice = StringUtils.EMPTY;

   /**
    * @return the description
    */
   public String getDescription()
   {
      return this.description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(final String description)
   {
      this.description = description;
   }

   /**
    * @return the price
    */
   public BigDecimal getPrice()
   {
      return this.price;
   }

   /**
    * @param currencyAppendedPrice the currencyAppendedPrice to set
    */
   public void setCurrencyAppendedPrice(final String currencyAppendedPrice)
   {
      this.currencyAppendedPrice = currencyAppendedPrice;
   }

   /**
    * @return the price
    */
   public String getCurrencyAppendedPrice()
   {
      return this.currencyAppendedPrice;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final BigDecimal price)
   {
      this.price = price;
   }

   /**
    * Return the string representation of the object.
    *
    * @return the string representation of the object.
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return ToStringBuilder.reflectionToString(this);
   }
}
