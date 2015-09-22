/**
 *
 */
package uk.co.tui.stub;

import uk.co.portaltech.travel.model.results.TracsDetails;

/**
 *
 */
public class Inventory
{
   private String inventoryName;

   private String promoCode;

   private String sellingCode;

   private TracsDetails tracsDetails = new TracsDetails();

   /**
    * @return the inventoryName
    */
   public String getInventoryName()
   {
      return inventoryName;
   }

   /**
    * @param inventoryName the inventoryName to set
    */
   public void setInventoryName(final String inventoryName)
   {
      this.inventoryName = inventoryName;
   }

   /**
    * @return the promoCode
    */
   public String getPromoCode()
   {
      return promoCode;
   }

   /**
    * @param promoCode the promoCode to set
    */
   public void setPromoCode(final String promoCode)
   {
      this.promoCode = promoCode;
   }

   /**
    * @return the tracsDetails
    */
   public TracsDetails getTracsDetails()
   {
      return tracsDetails;
   }

   /**
    * @param tracsDetails the tracsDetails to set
    */
   public void setTracsDetails(final TracsDetails tracsDetails)
   {
      this.tracsDetails = tracsDetails;
   }

   /**
    * @return the sellingCode
    */
   public String getSellingCode()
   {
      return sellingCode;
   }

   /**
    * @param sellingCode the sellingCode to set
    */
   public void setSellingCode(final String sellingCode)
   {
      this.sellingCode = sellingCode;
   }

}
