/**
 *
 */
package uk.co.portaltech.tui.flights.facades;

import java.util.List;
import java.util.Map;

import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.pojo.DealsFlyOutData;
import uk.co.tui.flights.pojo.SearchRequestData;


/**
 * @author extpn2
 *
 */
public interface DealsSearchPanelFacade
{

	/**
	 *
	 * @param searchRequestData
	 * @param seasonEndDate
	 * @return flyOutData
	 */
	DealsFlyOutData getFlyOutDurations(SearchRequestData searchRequestData, String seasonEndDate);

	/**
	 *
	 * @param seasonEndDate
	 * @return flyOutData
	 */
	DealsFlyOutData getFlyOutDurations(String seasonEndDate);

	/**
	 * This method returns all the list of destination airports based on the deals selection criteria.
	 *
	 * @param searchRequestData
	 * @return destinationAirports
	 * @throws SearchResultsBusinessException
	 */
	Map<String, List<AirportData>> getArrivalData(SearchRequestData searchRequestData) throws SearchResultsBusinessException;

	/**
	 * @param searchRequestData
	 * @return
	 */
	Map<String, List<AirportData>> getDepartureData(SearchRequestData searchRequestData) throws SearchResultsBusinessException;

	boolean isTracsInventory();

}
