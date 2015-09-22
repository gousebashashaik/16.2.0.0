package uk.co.portaltech.tui.services.productfinder;

/*
 * Originating Unit: Portal Technology Systems Ltd
 * http://www.portaltech.co.uk
 *
 * Copyright Portal Technology Systems Ltd.
 *
 * $Id: $
 */

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.components.model.AccommodationCarouselModel;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.CarouselLookupType;


public final class ExplicitProductFinder implements ProductFinder
{

    @Resource
    private FlexibleSearchService flexibleSearchService;


    private static final TUILogUtils LOG = new TUILogUtils("ExplicitProductFinder");

    @Override
    public SearchResultData<AccommodationModel> search(final SearchRequestData request)
    {
        LOG.debug("Searching explicit products.");
        final SearchResultData<AccommodationModel> result = new SearchResultData<AccommodationModel>();
        final AccommodationCarouselModel carouselModel = (AccommodationCarouselModel) request.getRelevantItem();
        final int fromIndex = request.getOffset();
        final int toIndex = request.getOffset() + request.getPageSize();
        if (request.getRelevantItem() == null)
        {
            return (SearchResultData<AccommodationModel>) Collections.<AccommodationModel> emptyList();
        }
        if (request.getPageSize() == -1)
        {
            result.setResults(getAllAccommodationsForCarousel(carouselModel.getPk().toString()));
            return result;
        }
        LOG.debug("fromIndex = " + fromIndex + " and toIndex = " + toIndex);
        result.setResults(getAccommodationsForCarousel(carouselModel.getPk().toString(), fromIndex, toIndex));

        return result;
    }

    private List<AccommodationModel> getAccommodationsForCarousel(final String carouselPk, final int fromIndex, final int toIndex)
    {
        // Query is been made using the flexible search query paging functionality
        final String queryString = "SELECT {target} FROM {AccomodationForAccommodationCarousel} WHERE SOURCEPK = ?carouselPK ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("carouselPK", carouselPk);
        query.setCount(toIndex);
        query.setNeedTotal(true);
        query.setStart(fromIndex);

        return flexibleSearchService.<AccommodationModel> search(query).getResult();
    }


    private List<AccommodationModel> getAllAccommodationsForCarousel(final String carouselPk)
    {
        // Query is been made using the flexible search query paging functionality
        final String queryString = "SELECT {target} FROM {AccomodationForAccommodationCarousel} WHERE SOURCEPK = ?carouselPK ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("carouselPK", carouselPk);

        return flexibleSearchService.<AccommodationModel> search(query).getResult();
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

    /* (non-Javadoc)
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
     */
    @Override
    public SearchResultData<? extends Object> searchRecPack(SearchResultsRequestData request)
    {

        return null;
    }
}
