/**
 *
 */
package uk.co.portaltech.tui.services.productfinder;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.QuickSearchAutoSuggestItem;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.travel.thirdparty.endeca.SearchContext;
import uk.co.portaltech.travel.thirdparty.endeca.SearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.services.SearchService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.web.common.enums.CarouselLookupType;


/**
 * @author gagan
 *
 */
public class EndecaQuickSearchResultDataFinder implements ProductFinder
{

    @Resource
    private SearchService searchService;

    @Resource
    private TuiUtilityService tuiUtilityService;

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
        final SearchContext context = new SearchContext();
        context.setQuickSearchParameter(request.getQuickSearchParameter());
        context.setBrand(tuiUtilityService.getSiteBrand());
        context.setSiteId(tuiUtilityService.getSiteBrand());
        final SearchResult quickSearchData = searchService.getQuickSearchData(context);
        final SearchResultData<QuickSearchAutoSuggestItem> result = new SearchResultData<QuickSearchAutoSuggestItem>();
        result.setQuickSearchAutoSuggestItem(quickSearchData.getQuickSearchAutoSuggestItem());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#accomSearch(uk.co.portaltech.tui.web.view.data.
     * SearchRequestData, uk.co.portaltech.tui.enums.CarouselLookupType)
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
     * @see
     * uk.co.portaltech.tui.services.productfinder.ProductFinder#priceForRecSearch(uk.co.portaltech.tui.web.view.data
     * .SearchRequestData)
     */
    @Override
    public SearchResultData<ResultData> priceForRecSearch(final SearchRequestData requestData)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.view.data.
     * SearchResultsRequestData)
     */
    @Override
    public SearchResultData<? extends Object> searchRecPack(final SearchResultsRequestData request)
    {

        return null;
    }



}
