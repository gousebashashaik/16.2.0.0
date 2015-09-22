/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Map;

/**
 *
 */
public class HolidayViewData
{

   private SearchResultsViewData searchResult = new SearchResultsViewData();

   private UserViewData userData = new UserViewData();

   private SearchResultsRequestData searchRequest = new SearchResultsRequestData();

   private String siteName;

   private String siteBrand;

   private ThirdPartyInfomationViewData thirdPartyInfoData;

   private Map<String, Map<String, String>> thirdPartyInfoMap;

   private boolean shortlistEnabled;

   /**
    * @return the siteBrand
    */
   public String getSiteBrand()
   {
      return siteBrand;
   }

   /**
    * @param siteBrand the siteBrand to set
    */
   public void setSiteBrand(final String siteBrand)
   {
      this.siteBrand = siteBrand;
   }

   /**
    * @return the siteBrand
    */
   public String getSiteName()
   {
      return siteName;
   }

   /**
    * @param siteBrand the siteBrand to set
    */
   public void setSiteName(final String siteBrand)
   {
      this.siteName = siteBrand;
   }

   /**
    * @return the searchResult
    */
   public SearchResultsViewData getSearchResult()
   {
      return searchResult;
   }

   /**
    * @param searchResult the searchResult to set
    */
   public void setSearchResult(final SearchResultsViewData searchResult)
   {
      this.searchResult = searchResult;
   }

   /**
    * @return the userData
    */
   public UserViewData getUserData()
   {
      return userData;
   }

   /**
    * @param userData the userData to set
    */
   public void setUserData(final UserViewData userData)
   {
      this.userData = userData;
   }

   /**
    * @return the searchRequest
    */
   public SearchResultsRequestData getSearchRequest()
   {
      return searchRequest;
   }

   /**
    * @param searchRequest the searchRequest to set
    */
   public void setSearchRequest(final SearchResultsRequestData searchRequest)
   {
      this.searchRequest = searchRequest;
   }

   /**
    * @return the thirdPartyInfoData
    */
   public ThirdPartyInfomationViewData getThirdPartyInfoData()
   {
      return thirdPartyInfoData;
   }

   /**
    * @param thirdPartyInfoData the thirdPartyInfoData to set
    */
   public void setThirdPartyInfoData(final ThirdPartyInfomationViewData thirdPartyInfoData)
   {
      this.thirdPartyInfoData = thirdPartyInfoData;
   }

   /**
    * @return the thirdPartyInfoMap
    */
   public Map<String, Map<String, String>> getThirdPartyInfoMap()
   {
      return thirdPartyInfoMap;
   }

   /**
    * @param thirdPartyInfoMap the thirdPartyInfoMap to set
    */
   public void setThirdPartyInfoMap(final Map<String, Map<String, String>> thirdPartyInfoMap)
   {
      this.thirdPartyInfoMap = thirdPartyInfoMap;
   }

   public boolean isShortlistEnabled()
   {
      return shortlistEnabled;
   }

   public void setShortlistEnabled(final boolean shortlistEnabled)
   {
      this.shortlistEnabled = shortlistEnabled;
   }

}
