/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;
import java.util.Map;

import uk.co.tui.book.page.response.PageResponse;

/**
 * @author deepakkumar.k
 *
 */
public class BookFlowAccommodationViewData
{
   private SearchResultViewData packageData = new SearchResultViewData();

   private SearchResultsRequestData searchRequestData = new SearchResultsRequestData();

   private String packagePrice;

   private String total;

   private String subTotal;

   private double worldCareFunds;

   private int endecaSearchResultCount;

   private String origPos;

   private String finPos;

   private String nextPageUrl;

   private boolean packageSoldOut;

   private Map<String, Object> packageInfo;

   private CreditCardCharge creditCardData;

   private List<AlternateBoardBasisPriceViewData> alternateBoardPrice;

   private boolean aniteSwitch;

   private String siteName;

   private PageResponse pageResponse;

   private String priceDisplay;

   private String priceChange;

   private boolean shortlistEnabled;

   /**
    * @return the priceChange
    */
   public String getPriceChange()
   {
      return priceChange;
   }

   /**
    * @param priceChange the priceChange to set
    */
   public void setPriceChange(final String priceChange)
   {
      this.priceChange = priceChange;
   }

   /**
    * @return the priceDisplay
    */
   public String getPriceDisplay()
   {
      return priceDisplay;
   }

   /**
    * @param priceDisplay the priceDisplay to set
    */
   public void setPriceDisplay(final String priceDisplay)
   {
      this.priceDisplay = priceDisplay;
   }

   /**
    * @return the siteName
    */
   public String getSiteName()
   {
      return siteName;
   }

   /**
    * @param siteName the siteName to set
    */
   public void setSiteName(final String siteName)
   {
      this.siteName = siteName;
   }

   /**
    * @return the alternateBoardPrice
    */
   public List<AlternateBoardBasisPriceViewData> getAlternateBoardPrice()
   {
      return alternateBoardPrice;
   }

   /**
    * @param alternateBoardPrice the alternateBoardPrice to set
    */
   public void setAlternateBoardPrice(
      final List<AlternateBoardBasisPriceViewData> alternateBoardPrice)
   {
      this.alternateBoardPrice = alternateBoardPrice;
   }

   /**
    * @return the creditCardData
    */
   public CreditCardCharge getCreditCardData()
   {
      return creditCardData;
   }

   /**
    * @param creditCardData the creditCardData to set
    */
   public void setCreditCardData(final CreditCardCharge creditCardData)
   {
      this.creditCardData = creditCardData;
   }

   /**
    * @return the packagePrice
    */
   public String getPackagePrice()
   {
      return packagePrice;
   }

   /**
    * @param packagePrice the packagePrice to set
    */
   public void setPackagePrice(final String packagePrice)
   {
      this.packagePrice = packagePrice;
   }

   public boolean isPackageSoldOut()
   {
      return packageSoldOut;
   }

   /**
    * @param packageSoldOut the isPackageSoldOut to set
    */
   public void setPackageSoldOut(final boolean packageSoldOut)
   {
      this.packageSoldOut = packageSoldOut;
   }

   /**
    * @return the total
    */
   public String getTotal()
   {
      return total;
   }

   /**
    * @return the nextPageUrl
    */
   public String getNextPageUrl()
   {
      return nextPageUrl;
   }

   /**
    * @param nextPageUrl the nextPageUrl to set
    */
   public void setNextPageUrl(final String nextPageUrl)
   {
      this.nextPageUrl = nextPageUrl;
   }

   /**
    * @param total the total to set
    */
   public void setTotal(final String total)
   {
      this.total = total;
   }

   /**
    * @return the subTotal
    */
   public String getSubTotal()
   {
      return subTotal;
   }

   /**
    * @param subTotal the subTotal to set
    */
   public void setSubTotal(final String subTotal)
   {
      this.subTotal = subTotal;
   }

   /**
    * @return the worldCareFunds
    */
   public double getWorldCareFunds()
   {
      return worldCareFunds;
   }

   /**
    * @param worldCareFunds the worldCareFunds to set
    */
   public void setWorldCareFunds(final double worldCareFunds)
   {
      this.worldCareFunds = worldCareFunds;
   }

   /**
    * @return the packageData
    */
   public SearchResultViewData getPackageData()
   {
      return packageData;
   }

   /**
    * @param packageData the packageData to set
    */
   public void setPackageData(final SearchResultViewData packageData)
   {
      this.packageData = packageData;
   }

   /**
    * @return the searchRequestData
    */
   public SearchResultsRequestData getSearchRequestData()
   {
      return searchRequestData;
   }

   /**
    * @param searchRequestData the searchRequestData to set
    */
   public void setSearchRequestData(final SearchResultsRequestData searchRequestData)
   {
      this.searchRequestData = searchRequestData;
   }

   /**
    * @return the endecaSearchResultCount
    */
   public int getEndecaSearchResultCount()
   {
      return endecaSearchResultCount;
   }

   /**
    * @param endecaSearchResultCount the endecaSearchResultCount to set
    */
   public void setEndecaSearchResultCount(final int endecaSearchResultCount)
   {
      this.endecaSearchResultCount = endecaSearchResultCount;
   }

   /**
    * @return the origPos
    */
   public String getOrigPos()
   {
      return origPos;
   }

   /**
    * @param origPos the origPos to set
    */
   public void setOrigPos(final String origPos)
   {
      this.origPos = origPos;
   }

   /**
    * @return the finPos
    */
   public String getFinPos()
   {
      return finPos;
   }

   /**
    * @param finPos the finPos to set
    */
   public void setFinPos(final String finPos)
   {
      this.finPos = finPos;
   }

   /**
    * @return the packageInfo
    */
   public Map<String, Object> getPackageInfo()
   {
      return packageInfo;
   }

   /**
    * @param packageInfo the packageInfo to set
    */
   public void setPackageInfo(final Map<String, Object> packageInfo)
   {
      this.packageInfo = packageInfo;
   }

   /**
    * @return the aniteSwitch
    */
   public boolean isAniteSwitch()
   {
      return aniteSwitch;
   }

   /**
    * @param aniteSwitch the aniteSwitch to set
    */
   public void setAniteSwitch(final boolean aniteSwitch)
   {
      this.aniteSwitch = aniteSwitch;
   }

   /**
    * @return the pageResponse
    */
   public PageResponse getPageResponse()
   {
      return pageResponse;
   }

   /**
    * @param pageResponse the pageResponse to set
    */
   public void setPageResponse(final PageResponse pageResponse)
   {
      this.pageResponse = pageResponse;
   }

   /**
    * @return the shortlistEnabled
    */
   public boolean isShortlistEnabled()
   {
      return shortlistEnabled;
   }

   /**
    * @param shortlistEnabled the shortlistEnabled to set
    */
   public void setShortlistEnabled(final boolean shortlistEnabled)
   {
      this.shortlistEnabled = shortlistEnabled;
   }

   /**
    * @return the bookFlowPageUrl
    */

}
