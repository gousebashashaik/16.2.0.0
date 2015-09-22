/**
 *
 */
package uk.co.tui.fo.book.view.data;

/**
 * @author prashant.godi
 *
 */
public class WebAnalyticsData
{

   /** Used for alternative flight options data capturing . */
   private WebAnalyticsALTFLTData webAnalyticsALTFLTData;

   private String limAvS;

   /**
    * @return the webAnalyticsALTFLTData
    */
   public WebAnalyticsALTFLTData getWebAnalyticsALTFLTData()
   {
      return webAnalyticsALTFLTData;
   }

   /**
    * @param webAnalyticsALTFLTData the webAnalyticsALTFLTData to set
    */
   public void setWebAnalyticsALTFLTData(final WebAnalyticsALTFLTData webAnalyticsALTFLTData)
   {
      this.webAnalyticsALTFLTData = webAnalyticsALTFLTData;
   }

   /**
    * @return the limAvS
    */
   public String getLimAvS()
   {
      return limAvS;
   }

   /**
    * @param limAvS the limAvS to set
    */
   public void setLimAvS(final String limAvS)
   {
      this.limAvS = limAvS;
   }

}
