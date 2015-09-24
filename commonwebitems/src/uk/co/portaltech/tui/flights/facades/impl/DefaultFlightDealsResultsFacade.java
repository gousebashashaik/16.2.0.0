/**
 *
 */
package uk.co.portaltech.tui.flights.facades.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.comparators.sort.FlightDealsSortComparator;
import uk.co.portaltech.tui.flights.facades.FlightDealsResultsFacade;
import uk.co.tui.book.domain.lite.FlightSearchPackage;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.pojo.SearchRequestData;
import uk.co.tui.flights.service.FlightDealsService;
import uk.co.tui.flights.service.FlightSearchService;
import uk.co.tui.flights.web.view.data.ItineraryViewData;


/**
 * @author naveenkumar.m
 *
 *         This class used for flight deals results page
 *
 */
public class DefaultFlightDealsResultsFacade implements FlightDealsResultsFacade
{

	@Resource
	private FlightDealsService flightDealsService;

	@Resource
	private FlightSearchService flightSearchService;

	@Resource
	private Populator<List<Itinerary>, ItineraryViewData> itineraryViewDataPopulator;

	@Resource
	private Populator<FlightSearchPackage, ItineraryViewData> flightSearchPackageDataPopulator;

	@Resource
	private Populator<SearchRequestData, FlightSearchCriteria> flightDealsRequestPopulator;

	@Resource
	private SessionService sessionService;

	@Resource
	private ConfigurationService configurationService;

	@Resource
	private CMSSiteService cmsSiteService;

	private static final String ITINERARY_VIEW_DATA = "itineraryViewData";

	private static final String FLIGHT_SEARCH_PACKAGE = "flightSearchPackage";

	private static final String PER_PERSON_PRICE_ASCENDING = "PER_PERSON_PRICE_ASCENDING";

	private static final String DESTINATION_NAME_ASCENDING = "DESTINATION_NAME_ASCENDING";

	private static final String FLIGHT_SEARCH_CRITERIA = "flightsearchcriteria";

	private static final String FLIGHT_DEALS_SEARCH_CRITERIA = "flightDealsSearchCriteria";

	private static final int ZERO = 0;

	private static final int PAGE_SIZE = 20;



	/**
	 * This method fetches the grouped deals results.
	 *
	 * @param flightSearchCriteria
	 * @return ItineraryViewData
	 * @throws SearchResultsBusinessException
	 */
	@Override
	public ItineraryViewData getGroupedDealsResults(final FlightSearchCriteria flightSearchCriteria)
			throws SearchResultsBusinessException
	{
		final ItineraryViewData itineraryViewData = new ItineraryViewData();
		FlightSearchPackage flightSearchPackage = new FlightSearchPackage();

		try
		{
			flightSearchPackage = flightDealsService.getGroupedDealsResults(flightSearchCriteria);

			final List<Itinerary> itinerayList = flightSearchPackage.getItineraryList();
			itineraryViewData.setItineraryCount(itinerayList.size());
			Collections.sort(itinerayList,
					FlightDealsSortComparator.getComparator(getFlightDealsComparatorList(PER_PERSON_PRICE_ASCENDING)));


			flightSearchPackage.setItineraryList(itinerayList);
			itineraryViewData.setFlightSearchCriteria(flightSearchCriteria);
			itineraryViewData.setSearchType(flightSearchCriteria.getSearchCriteriaType());

			itineraryViewData.setDepartureFlightsInfo(flightDealsService.getDepartureFlightInformation(flightSearchCriteria));
			itineraryViewData.setArrivalFLightsInfo(flightDealsService.getArrivalFlightInformation(flightSearchCriteria));

			populateItineraryPaginatedData(itineraryViewData, itinerayList, ZERO);



			sessionService.setAttribute(FLIGHT_SEARCH_PACKAGE, flightSearchPackage);
			flightSearchPackageDataPopulator.populate(flightSearchPackage, itineraryViewData);
		}
		catch (final FlightsServiceException e)
		{
			throw new SearchResultsBusinessException("6002", e);
		}

		return itineraryViewData;
	}


	/**
	 * This method fetches the lazy data for deals results.
	 *
	 * @param searchRequestData
	 * @return ItineraryViewData
	 */
	@Override
	public ItineraryViewData getDealsResultsLazyData(final SearchRequestData searchRequestData)
	{
		final ItineraryViewData itineraryViewData = new ItineraryViewData();
		List<Itinerary> itinerayList = null;
		final FlightSearchPackage flightSearchPackage = sessionService.getAttribute(FLIGHT_SEARCH_PACKAGE);

		if (flightSearchPackage != null && CollectionUtils.isNotEmpty(flightSearchPackage.getItineraryList()))
		{
			itinerayList = flightSearchPackage.getItineraryList();
			itineraryViewData.setSearchType(searchRequestData.getSearchType());
			itineraryViewDataPopulator.populate(getItineraryLazyData(itinerayList, searchRequestData), itineraryViewData);
			flightSearchPackageDataPopulator.populate(flightSearchPackage, itineraryViewData);
		}

		return itineraryViewData;
	}


	/**
	 * This method fetches the individual deals results.
	 *
	 * @param searchRequestData
	 * @return itineraryList
	 */
	@Override
	public ItineraryViewData getIndividualDealsResults(final SearchRequestData searchRequestData)
			throws SearchResultsBusinessException
	{
		final ItineraryViewData itineraryViewData = new ItineraryViewData();

		try
		{
			final FlightSearchCriteria flightSearchCriteria = new FlightSearchCriteria();
			flightDealsRequestPopulator.populate(searchRequestData, flightSearchCriteria);

			if (StringUtils.equalsIgnoreCase("paginate", flightSearchCriteria.getSearchCriteriaType()))
			{
				return getIndividualPaginatedItineraryViewData(flightSearchCriteria, itineraryViewData);
			}

			final FlightSearchPackage flightSearchPackage = flightDealsService.getIndividualDealsResults(flightSearchCriteria);
			final List<Itinerary> itinerayList = flightSearchPackage.getItineraryList();
			if (CollectionUtils.isNotEmpty(itinerayList))
			{
				Collections.sort(itinerayList,
						FlightDealsSortComparator.getComparator(getFlightDealsComparatorList(PER_PERSON_PRICE_ASCENDING)));
			}
			sessionService.setAttribute(ITINERARY_VIEW_DATA, itinerayList);
			final FlightSearchCriteria dealsParameterInSession = sessionService.getAttribute(FLIGHT_DEALS_SEARCH_CRITERIA);
			flightSearchCriteria.setBackDealsIdentity(dealsParameterInSession.getBackDealsIdentity());
			sessionService.setAttribute(FLIGHT_SEARCH_CRITERIA, flightSearchCriteria);
			itineraryViewData.setSearchType(searchRequestData.getSearchType());
			populateItineraryPaginatedData(itineraryViewData, itinerayList, ZERO);

		}
		catch (final FlightsServiceException e)
		{
			throw new SearchResultsBusinessException("6002", e);
		}

		return itineraryViewData;
	}


	/**
	 * @param flightSearchCriteria
	 * @param itineraryViewData
	 * @return itineraryViewData
	 */
	private ItineraryViewData getIndividualPaginatedItineraryViewData(final FlightSearchCriteria flightSearchCriteria,
			final ItineraryViewData itineraryViewData)
	{
		final List<Itinerary> itinerayList = sessionService.getAttribute(ITINERARY_VIEW_DATA);
		if (CollectionUtils.isNotEmpty(itinerayList))
		{
			final int startNumber = StringUtils.isNotEmpty(flightSearchCriteria.getPageCount()) ? Integer
					.parseInt(flightSearchCriteria.getPageCount()) : PAGE_SIZE;
			itineraryViewData.setSearchType(flightSearchCriteria.getSearchCriteriaType());
			populateItineraryPaginatedData(itineraryViewData, itinerayList, startNumber);
		}
		return itineraryViewData;
	}

	/**
	 *
	 * @param searchRequestData
	 * @return ItineraryViewData
	 */
	@Override
	public ItineraryViewData getItinerarySortBy(final SearchRequestData searchRequestData)
	{
		final ItineraryViewData itineraryViewData = new ItineraryViewData();

		final FlightSearchPackage flightSearchPackage = sessionService.getAttribute(FLIGHT_SEARCH_PACKAGE);
		if (flightSearchPackage != null && CollectionUtils.isNotEmpty(flightSearchPackage.getItineraryList()))
		{
			final List<Itinerary> itinerayList = flightSearchPackage.getItineraryList();
			if (StringUtils.equalsIgnoreCase("price", searchRequestData.getSearchType()))
			{
				Collections.sort(itinerayList,
						FlightDealsSortComparator.getComparator(getFlightDealsComparatorList(PER_PERSON_PRICE_ASCENDING)));
			}
			else
			{
				Collections.sort(itinerayList,
						FlightDealsSortComparator.getComparator(getFlightDealsComparatorList(DESTINATION_NAME_ASCENDING)));
			}

			flightSearchPackage.setItineraryList(itinerayList);
			sessionService.setAttribute(FLIGHT_SEARCH_PACKAGE, flightSearchPackage);

			itineraryViewData.setSearchType(searchRequestData.getSearchType());
			populateItineraryPaginatedData(itineraryViewData, itinerayList, ZERO);

			flightSearchPackageDataPopulator.populate(flightSearchPackage, itineraryViewData);
		}

		return itineraryViewData;
	}


	/**
	 * @param itineraryViewData
	 * @param itinerayList
	 */
	private void populateItineraryPaginatedData(final ItineraryViewData itineraryViewData, final List<Itinerary> itinerayList,
			final int startNumber)
	{
		List<Itinerary> itineraysubList = null;
		final int itinerayListSize = itinerayList.size();

		final int dealsPageSize = getDealsPageSize(itineraryViewData);

		if (itinerayListSize > dealsPageSize)
		{
			int endNumber = startNumber + dealsPageSize;
			if (endNumber > itinerayListSize)
			{
				endNumber = itinerayListSize;
			}
			itineraysubList = flightDealsService.getPaginatedItineraryList(itinerayList, startNumber, endNumber);
			itineraryViewDataPopulator.populate(itineraysubList, itineraryViewData);
		}
		else
		{
			itineraryViewDataPopulator.populate(itinerayList, itineraryViewData);

		}
	}


	/**
	 * @param itineraryViewData
	 * @return dealsPageSize
	 */
	private int getDealsPageSize(final ItineraryViewData itineraryViewData)
	{
		int dealsPageSize;

		if (StringUtils.equalsIgnoreCase("paginate", itineraryViewData.getSearchType())
				|| StringUtils.equalsIgnoreCase("expanded", itineraryViewData.getSearchType()))
		{
			dealsPageSize = Integer.parseInt(configurationService.getConfiguration().getString("flight.expanded.deals.pagesize",
					"20"));
		}
		else
		{
			dealsPageSize = Integer.parseInt(configurationService.getConfiguration()
					.getString("flight.grouped.deals.pagesize", "15"));
		}
		return dealsPageSize;
	}

	/**
	 *
	 * @param comperatorCode
	 * @return List<FlightDealsSortandPagination>
	 */
	private List<FlightDealsSortComparator> getFlightDealsComparatorList(final String comperatorCode)
	{
		final List<FlightDealsSortComparator> comparators = new ArrayList<FlightDealsSortComparator>();
		comparators.add(FlightDealsSortComparator.valueOf(comperatorCode));
		return comparators;
	}


	/**
	 *
	 * @param itinerayList
	 * @param searchRequestData
	 * @return List<Itinerary>
	 */
	@Override
	public List<Itinerary> getItineraryLazyData(final List<Itinerary> itinerayList, final SearchRequestData searchRequestData)
	{
		return flightDealsService.getLazyloadedItineraray(itinerayList, searchRequestData);
	}

	@Override
	public boolean isSelectedFlightAvailable(final FlightSearchCriteria flightSearchCriteria, final String slectedFlight)
			throws SearchResultsBusinessException
	{
		return flightDealsService.checkAvaliablityOfSelectedFlight(flightSearchCriteria, slectedFlight);
	}


	@Override
	public AirportData getAirportDataForCode(final String airportCode, final CatalogVersionModel catalogVersion)
			throws FlightsServiceException
	{
		return flightSearchService.getAirportDataForCode(airportCode, cmsSiteService.getCurrentCatalogVersion());
	}

}
