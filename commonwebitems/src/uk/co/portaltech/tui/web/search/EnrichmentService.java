/**
 *
 */
package uk.co.portaltech.tui.web.search;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author shravan.m
 *
 */
public interface EnrichmentService  <S, T>{

    void enrich(S source, T target);


    /**
     * @param component
     * @param searchParameter
     */
    void enrichForHolidayFinder(HolidayFinderComponentModel component,
            SearchResultsRequestData searchParameter);
}
