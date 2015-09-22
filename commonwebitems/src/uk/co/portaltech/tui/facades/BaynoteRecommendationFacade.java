/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.RecommendationsData;

/**
 * @author veena.pn
 *
 */
public interface BaynoteRecommendationFacade
{

   /**
    * @param cspSorting
    * @param brandtype
    * @param pageType
    * @param urlmap
    * @param siteBrand
    * @return
    * @throws SearchResultsBusinessException
    */

   /**
    * @param pAGE_TYPE
    * @param brandtype
    * @param urlMap
    * @param componentUid
    * @return
    */

   /**
    * @param productCodes
    * @return
    */
   @SuppressWarnings("javadoc")
   RecommendationsData getProductProductRanges(List<String> productCodes, String siteBrand);

   /**
    * @param productCodes
    * @param cspSorting
    * @return
    */
   @SuppressWarnings("javadoc")
   RecommendationsData getRecommendedAccomPriceInfoBrowse(List<String> productCodes,
      Boolean cspSorting, String siteBrand);

   /**
    * @param brandtype
    * @param productCodes
    * @param cspSorting
    * @return
    */
   @SuppressWarnings("javadoc")
   RecommendationsData getReccommendedHolidayPackageDataBook(String brandtype,
      List<String> productCodes, Boolean cspSorting);

   String baynoteStubStatus();

   /**
    * @param productCodes
    * @param siteBrand
    * @param recommendationsData
    * @return
    */
   RecommendationsData getProductProductRanges(List<String> productCodes, String siteBrand,
      RecommendationsData recommendationsData);

   /**
    * @param productCodes
    * @param cspSorting
    * @param siteBrand
    * @param recommendationsData
    * @return
    */
   RecommendationsData getRecommendedAccomPriceInfoBrowse(List<String> productCodes,
      Boolean cspSorting, String siteBrand, RecommendationsData recommendationsData);

   /**
    * @param siteBrand
    * @param productCodes
    * @param cspSorting
    * @param recommendationsData
    * @return
    */
   RecommendationsData getReccommendedHolidayPackageDataBook(String siteBrand,
      List<String> productCodes, Boolean cspSorting, RecommendationsData recommendationsData);

}
