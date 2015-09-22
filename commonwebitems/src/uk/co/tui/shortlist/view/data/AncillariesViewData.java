/**
 *
 */
package uk.co.tui.shortlist.view.data;

import java.math.BigDecimal;

/**
 * @author thirunavukkarasu.k
 *
 */
public class AncillariesViewData
{

   private BigDecimal price;

   private String ancillaryName;

   private int count;

   /**
    * @return the price
    */
   public BigDecimal getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final BigDecimal price)
   {
      this.price = price;
   }

   /**
    * @return the ancillaryName
    */
   public String getAncillaryName()
   {
      return ancillaryName;
   }

   /**
    * @param ancillaryName the ancillaryName to set
    */
   public void setAncillaryName(final String ancillaryName)
   {
      this.ancillaryName = ancillaryName;
   }

   /**
    * @return the count
    */
   public int getCount()
   {
      return count;
   }

   /**
    * @param count the count to set
    */
   public void setCount(final int count)
   {
      this.count = count;
   }

}
