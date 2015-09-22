/**
 *
 */
package uk.co.portaltech.tui.services.productfinder;


import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.web.common.enums.CarouselLookupType;



/**
 * @author laxmibai.p
 *
 */
public class AvailableDateFinder implements ProductFinder
{

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.view.data.SearchRequestData
     * )
     */
    @Override
    public SearchResultData<? extends Object> search(final SearchRequestData request)
    {

        return null;
    }


    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#searchHolidays(uk.co.portaltech.tui.web.view.data.
     * SearchRequestData)
     */
    @Override
    public EndecaSearchResult searchHolidays(final SearchRequestData request)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#accomSearch(uk.co.portaltech.tui.web.view.data.
     * SearchRequestData, uk.co.tui.web.common.enums.CarouselLookupType)
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
     * uk.co.portaltech.tui.services.productfinder.ProductFinder#priceForRecSearch(uk.co.portaltech.tui.web.view.data
     * .SearchRequestData)
     */
    @Override
    public SearchResultData<? extends Object> priceForRecSearch(final SearchRequestData requestData)
    {

        return null;
    }


    /* (non-Javadoc)
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
     */
    @Override
    public SearchResultData<? extends Object> searchRecPack(SearchResultsRequestData request)
    {

        return null;
    }

}
