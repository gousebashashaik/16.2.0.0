/**
 *
 */
package uk.co.portaltech.tui.flights.facades;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.pojo.SearchRequestData;
import uk.co.tui.flights.web.view.data.ItineraryViewData;


/**
 * @author naveenkumar.m
 *
 */
public interface FlightDealsResultsFacade
{


	/**
	 * This method fetches the deals grouped search results.
	 *
	 * @param flightSearchCriteria
	 * @return ItineraryViewData
	 * @throws SearchResultsBusinessException
	 */
	ItineraryViewData getGroupedDealsResults(FlightSearchCriteria flightSearchCriteria) throws SearchResultsBusinessException;

	/**
	 * This method fetches the deals individual search results.
	 *
	 * @param searchRequestData
	 * @return ItineraryViewData
	 * @throws SearchResultsBusinessException
	 */
	ItineraryViewData getIndividualDealsResults(final SearchRequestData searchRequestData) throws SearchResultsBusinessException;

	/**
	 *
	 * @param searchRequestData
	 * @param itinerayList
	 * @return List<Itinerary>
	 */
	List<Itinerary> getItineraryLazyData(List<Itinerary> itinerayList, final SearchRequestData searchRequestData);

	/**
	 * @param searchRequestData
	 * @return ItineraryViewData
	 */
	ItineraryViewData getItinerarySortBy(SearchRequestData searchRequestData);


	/**
	 *
	 * @param searchRequestData
	 * @return ItineraryViewData
	 */
	ItineraryViewData getDealsResultsLazyData(final SearchRequestData searchRequestData);

	/**
	 * @param flightSearchCriteria
	 * @param selectedFlight
	 * @return boolean
	 * @throws SearchResultsBusinessException
	 */
	boolean isSelectedFlightAvailable(FlightSearchCriteria flightSearchCriteria, String selectedFlight)
			throws SearchResultsBusinessException;


	/**
	 *
	 * @param airportCode
	 * @param catalogVersion
	 * @return AirportData
	 * @throws FlightsServiceException
	 */
	AirportData getAirportDataForCode(final String airportCode, final CatalogVersionModel catalogVersion)
			throws FlightsServiceException;

}
