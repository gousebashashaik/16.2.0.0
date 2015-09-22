/**
 *
 */
package uk.co.tui.book.view.data.teasers;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * The Class DepartureDateTeaserViewData.
 *
 * @author samantha.gd
 */
public class DepartureDateTeaserViewData
{

   /** The date. */
   private String date;

   /** The display date. */
   private String displayDate = StringUtils.EMPTY;

   /** The per person price. */
   private BigDecimal perPersonPrice;

   /** The total price. */
   private BigDecimal totalPrice;

   /** The package id. */
   private String packageId = StringUtils.EMPTY;

   /** The difference per person price. */
   private String diffPerPersonPrice = StringUtils.EMPTY;

   /** The difference total price. */
   private String diffTotalPrice = StringUtils.EMPTY;

   /**
    * Gets the date.
    *
    * @return the date
    */
   public String getDate()
   {
      return date;
   }

   /**
    * Sets the date.
    *
    * @param date the new date
    */
   public void setDate(String date)
   {
      this.date = date;
   }

   /**
    * Gets the per person price.
    *
    * @return the per person price
    */
   public BigDecimal getPerPersonPrice()
   {
      return perPersonPrice;
   }

   /**
    * Sets the per person price.
    *
    * @param perPersonPrice the new per person price
    */
   public void setPerPersonPrice(BigDecimal perPersonPrice)
   {
      this.perPersonPrice = perPersonPrice;
   }

   /**
    * Gets the total price.
    *
    * @return the total price
    */
   public BigDecimal getTotalPrice()
   {
      return totalPrice;
   }

   /**
    * Sets the total price.
    *
    * @param totalPrice the new total price
    */
   public void setTotalPrice(BigDecimal totalPrice)
   {
      this.totalPrice = totalPrice;
   }

   /**
    * Gets the display date.
    *
    * @return the display date
    */
   public String getDisplayDate()
   {
      return displayDate;
   }

   /**
    * Sets the display date.
    *
    * @param displayDate the new display date
    */
   public void setDisplayDate(String displayDate)
   {
      this.displayDate = displayDate;
   }

   /**
    * Gets the package id.
    *
    * @return the package id
    */
   public String getPackageId()
   {
      return packageId;
   }

   /**
    * Sets the package id.
    *
    * @param packageId the new package id
    */
   public void setPackageId(String packageId)
   {
      this.packageId = packageId;
   }

   /**
    * Gets the diff per person price.
    *
    * @return the diff per person price
    */
   public String getDiffPerPersonPrice()
   {
      return diffPerPersonPrice;
   }

   /**
    * Sets the diff per person price.
    *
    * @param diffPerPersonPrice the new diff per person price
    */
   public void setDiffPerPersonPrice(String diffPerPersonPrice)
   {
      this.diffPerPersonPrice = diffPerPersonPrice;
   }

   /**
    * Gets the diff total price.
    *
    * @return the diff total price
    */
   public String getDiffTotalPrice()
   {
      return diffTotalPrice;
   }

   /**
    * Sets the diff total price.
    *
    * @param diffTotalPrice the new diff total price
    */
   public void setDiffTotalPrice(String diffTotalPrice)
   {
      this.diffTotalPrice = diffTotalPrice;
   }

}
