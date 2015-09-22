/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.exception.SearchResultsBusinessException;

/**
 *
 */
public interface HolidayFacade {

    boolean addtoShortlist(SearchResultsRequestData searchCriteria, String packageId, String customerId, String brandType) throws  SearchResultsBusinessException;

    boolean removeShortlist(String packageId, String customerId, String brandType);

    List getUserShortlist(String customerId, String brandType);

    int getShortlistCount(String customerId, String brandType);

    /**
     * This method returns specific holiday from the set of Results returned by Endeca based on the matching packageId
     * @param searchCriteria
     * @param packageId
     * @return BookFlowAccommodationViewData
     * @throws SearchResultsBusinessException
     */
    BookFlowAccommodationViewData getHoliday(SearchResultsRequestData searchCriteria,String packageId,String brandType) throws SearchResultsBusinessException;

}
