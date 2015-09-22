/**
 *
 */
package uk.co.tui.th.book.view.data.teasers;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * The Class AirportTeaserViewData.
 *
 * @author samantha.gd
 */
public class AirportTeaserViewData
{

   /** The airport name. */
   private String airportName = StringUtils.EMPTY;

   /** The airport code. */
   private String airportCode = StringUtils.EMPTY;

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
    * Gets the airport name.
    *
    * @return the airport name
    */
   public String getAirportName()
   {
      return airportName;
   }

   /**
    * Sets the airport name.
    *
    * @param airportName the new airport name
    */
   public void setAirportName(String airportName)
   {
      this.airportName = airportName;
   }

   /**
    * Gets the airport code.
    *
    * @return the airport code
    */
   public String getAirportCode()
   {
      return airportCode;
   }

   /**
    * Sets the airport code.
    *
    * @param airportCode the new airport code
    */
   public void setAirportCode(String airportCode)
   {
      this.airportCode = airportCode;
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
