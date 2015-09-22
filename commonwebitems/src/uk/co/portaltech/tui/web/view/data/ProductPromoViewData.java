/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author narendra.bm
 *
 */
public class ProductPromoViewData
{
   private String productName;

   private String productDescription;

   private String productURL;

   private MediaViewData image;

   private String code;

   private String productURLForMobile;

   public String getCode()
   {
      return code;
   }

   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @return the productName
    */
   public String getProductName()
   {
      return productName;
   }

   /**
    * @param productName the productName to set
    */
   public void setProductName(final String productName)
   {
      this.productName = productName;
   }

   /**
    * @return the productDescription
    */
   public String getProductDescription()
   {
      return productDescription;
   }

   /**
    * @param productDescription the productDescription to set
    */
   public void setProductDescription(final String productDescription)
   {
      this.productDescription = productDescription;
   }

   /**
    * @return the productURL
    */
   public String getProductURL()
   {
      return productURL;
   }

   /**
    * @param productURL the productURL to set
    */
   public void setProductURL(final String productURL)
   {
      this.productURL = productURL;
   }

   /**
    * @return the image
    */
   public MediaViewData getImage()
   {
      return image;
   }

   /**
    * @param image the image to set
    */
   public void setImage(final MediaViewData image)
   {
      this.image = image;
   }

   /**
    * @return the productURLForMobile
    */
   public String getProductURLForMobile()
   {
      return productURLForMobile;
   }

   /**
    * @param productURLForMobile the productURLForMobile to set
    */
   public void setProductURLForMobile(final String productURLForMobile)
   {
      this.productURLForMobile = productURLForMobile;
   }
}
