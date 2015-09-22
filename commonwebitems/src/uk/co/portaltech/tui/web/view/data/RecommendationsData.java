/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author omonikhide
 *
 */
public class RecommendationsData
{
   private List<AccommodationViewData> accomodationDatas;

   private String deepLinkUrl;

   private List<String> productCodes;

   private String featuredImage;

   private String baynoteRequestUrl;

   /**
    *
    * @param accomodationDatas
    *
    *
    */
   public RecommendationsData(final List<AccommodationViewData> accomodationDatas)
   {
      super();

      this.accomodationDatas = accomodationDatas;

   }

   public RecommendationsData()
   {
   }

   /**
    * @return the accomodationDatas
    */
   public List<AccommodationViewData> getAccomodationDatas()
   {
      return accomodationDatas;
   }

   /**
    * @param accomodationDatas the accomodationDatas to set
    */
   public void setAccomodationDatas(final List<AccommodationViewData> accomodationDatas)
   {
      this.accomodationDatas = accomodationDatas;
   }

   /**
    * @return the deepLinkUrl
    */
   public String getDeepLinkUrl()
   {
      return deepLinkUrl;
   }

   /**
    * @param deepLinkUrl the deepLinkUrl to set
    */
   public void setDeepLinkUrl(final String deepLinkUrl)
   {
      this.deepLinkUrl = deepLinkUrl;
   }

   /**
    * @return the productCodes
    */
   public List<String> getProductCodes()
   {
      return productCodes;
   }

   /**
    * @param productCodes the productCodes to set
    */
   public void setProductCodes(final List<String> productCodes)
   {
      this.productCodes = productCodes;
   }

   /**
    * @return the featuredImage
    */
   public String getFeaturedImage()
   {
      return featuredImage;
   }

   /**
    * @param featuredImage the featuredImage to set
    */
   public void setFeaturedImage(final String featuredImage)
   {
      this.featuredImage = featuredImage;
   }

   /**
    * @return the baynoteRequestUrl
    */
   public String getBaynoteRequestUrl()
   {
      return baynoteRequestUrl;
   }

   /**
    * @param baynoteRequestUrl the baynoteRequestUrl to set
    */
   public void setBaynoteRequestUrl(final String baynoteRequestUrl)
   {
      this.baynoteRequestUrl = baynoteRequestUrl;
   }

}
