/**
 *
 */
package uk.co.tui.book.view.data.teasers;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * The Class DurationTeaserViewData.
 *
 * @author samantha.gd
 */
public class DurationTeaserViewData
{

   /** The duration. */
   private int duration;

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
    * Gets the duration.
    *
    * @return the duration
    */
   public int getDuration()
   {
      return duration;
   }

   /**
    * Sets the duration.
    *
    * @param duration the new duration
    */
   public void setDuration(int duration)
   {
      this.duration = duration;
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
    * Gets the difference per person price.
    *
    * @return the difference per person price
    */
   public String getDiffPerPersonPrice()
   {
      return diffPerPersonPrice;
   }

   /**
    * Sets the difference per person price.
    *
    * @param diffPerPersonPrice the new difference per person price
    */
   public void setDiffPerPersonPrice(String diffPerPersonPrice)
   {
      this.diffPerPersonPrice = diffPerPersonPrice;
   }

   /**
    * Gets the difference total price.
    *
    * @return the difference total price
    */
   public String getDiffTotalPrice()
   {
      return diffTotalPrice;
   }

   /**
    * Sets the difference total price.
    *
    * @param diffTotalPrice the new difference total price
    */
   public void setDiffTotalPrice(String diffTotalPrice)
   {
      this.diffTotalPrice = diffTotalPrice;
   }
}
