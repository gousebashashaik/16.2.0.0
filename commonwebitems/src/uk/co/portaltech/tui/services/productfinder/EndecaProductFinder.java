package uk.co.portaltech.tui.services.productfinder;

/*
 * Originating Unit: Portal Technology Systems Ltd http://www.portaltech.co.uk
 * 
 * Copyright Portal Technology Systems Ltd.
 * 
 * $Id: $
 */

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.CarouselLookupType;

public final class EndecaProductFinder extends AbstractEndecaFinder implements ProductFinder
{

   private static final TUILogUtils LOG = new TUILogUtils("EndecaProductFinder");

   @Override
   // @Cacheable(cacheName = "endecaBrowseCache")
   public SearchResultData<AccommodationModel> search(final SearchRequestData request)
   {
      LOG.debug("Searching endeca Places to stay result.");
      final AccommodationCarouselModel carouselModel =
         (AccommodationCarouselModel) request.getRelevantItem();
      return (SearchResultData) processRequest(request, carouselModel.getLookupType().toString());

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
