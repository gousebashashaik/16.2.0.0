package uk.co.portaltech.tui.services.productfinder;

/*
 * Originating Unit: Portal Technology Systems Ltd
 * http://www.portaltech.co.uk
 *
 * Copyright Portal Technology Systems Ltd.
 *
 * $Id: $
 */

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.web.common.enums.CarouselLookupType;



/**
 * This class will populate a list of SearchResultData<ProductModel> based on a SearchRequestData. You will likely want
 * to implement this interface with a custom method for searching, although some are already included. (Search for
 * *ProductFinder to find out which).
 *
 * @author James Johnstone
 * @author Omon Ikhide
 */
public interface ProductFinder
{
    /**
     * Populates a SearchResultData<ProductModel> based on the SearchRequestData.
     *
     * @param request
     *           A formatted SearchRequestData object containing the variables we wish to include in our search.
     * @return A SearchResultData<ProductModel> with the relevant ProductModels.
     */
    SearchResultData<? extends Object> search(SearchRequestData request);


    SearchResultData<? extends Object> searchRecPack(SearchResultsRequestData request);

    /**
     * @param searchRequestData
     * @return
     */
    SearchResultData<? extends Object> accomSearch(SearchRequestData searchRequestData, CarouselLookupType carouselLookupType);

    EndecaSearchResult searchHolidays(SearchRequestData request);

    /**
     * @param requestData
     * @return
     */
    SearchResultData<? extends Object> priceForRecSearch(SearchRequestData requestData);

}
