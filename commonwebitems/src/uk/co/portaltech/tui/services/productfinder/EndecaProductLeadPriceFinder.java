/**
 *
 */
package uk.co.portaltech.tui.services.productfinder;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.components.model.ProductLeadPriceComponentModel;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.CarouselLookupType;

/**
 * @author
 *
 */
public class EndecaProductLeadPriceFinder extends AbstractEndecaFinder implements ProductFinder
{

   private static final TUILogUtils LOG = new TUILogUtils("EndecaProductLeadPriceFinder");

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.
    * view.data.SearchRequestData )
    */
   @Override
   // @Cacheable(cacheName = "endecaBrowseCache")
   public SearchResultData<ResultData> search(final SearchRequestData searchRequestData)
   {
      LOG.debug("Searching endeca product price.");
      SearchResultData<ResultData> searchResultData = new SearchResultData<ResultData>();
      final ProductLeadPriceComponentModel productLeadPriceComponentModel =
         (ProductLeadPriceComponentModel) searchRequestData.getRelevantItem();
      if (productLeadPriceComponentModel != null
         && productLeadPriceComponentModel.getLookupType() != null)
      {
         searchRequestData.setNonCoreHolidayType(StringUtils.remove(
            productLeadPriceComponentModel.getNonCoreHolidayType(), " "));
         searchResultData =
            (SearchResultData) processRequest(searchRequestData, productLeadPriceComponentModel
               .getLookupType().toString());
      }
      return searchResultData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#accomSearch(uk.co.portaltech.tui
    * .web.view.data. SearchRequestData, uk.co.portaltech.tui.enums.CarouselLookupType)
    */
   @Override
   // @Cacheable(cacheName = "endecaBrowseCache")
   public SearchResultData<ResultData> accomSearch(final SearchRequestData searchRequestData,
      final CarouselLookupType carouselLookupType)
   {

      LOG.debug("Searching endeca product result.");
      SearchResultData<ResultData> searchResultData = new SearchResultData<ResultData>();
      final AccommodationModel accommodationModel =
         (AccommodationModel) searchRequestData.getRelevantItem();
      if (accommodationModel != null)
      {
         searchResultData =
            (SearchResultData) processRequest(searchRequestData, carouselLookupType.toString());
      }
      return searchResultData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#searchHolidays(uk.co.portaltech.
    * tui.web.view.data. SearchRequestData)
    */
   @Override
   public EndecaSearchResult searchHolidays(final SearchRequestData request)
   {

      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#priceForRecSearch(uk.co.portaltech
    * .tui.web.view.data .SearchRequestData)
    */
   @Override
   public SearchResultData<ResultData> priceForRecSearch(final SearchRequestData requestData)
   {

      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.
    * view.data. SearchResultsRequestData)
    */
   @Override
   public SearchResultData<? extends Object> searchRecPack(final SearchResultsRequestData request)
   {

      return null;
   }

}
