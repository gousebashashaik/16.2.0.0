/**
 *
 */
package uk.co.portaltech.tui.services.productfinder;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.components.model.ProductCrossSellCarouselComponentModel;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.web.common.enums.CarouselLookupType;

/**
 * @author narendra.bm
 *
 */
public class ProductCrossSellCarouselFinder extends AbstractEndecaFinder implements ProductFinder
{

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.
    * view.data.SearchRequestData )
    */
   @Override
   // @Cacheable(cacheName = "endecaBrowseCache")
   public SearchResultData<ResultData> search(final SearchRequestData request)
   {

      final ProductCrossSellCarouselComponentModel component =
         (ProductCrossSellCarouselComponentModel) request.getRelevantItem();
      return (SearchResultData) processRequest(request, component.getLookupType().toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.productfinder.ProductFinder#accomSearch(uk.co.portaltech.tui
    * .web.view.data. SearchRequestData, uk.co.portaltech.tui.enums.CarouselLookupType)
    */
   @Override
   public SearchResultData<? extends Object> accomSearch(final SearchRequestData searchRequestData,
      final CarouselLookupType carouselLookupType)
   {

      return null;
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
