/**
 *
 */
package uk.co.portaltech.tui.flights.facades.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.route.FlightRouteIndexService;
import uk.co.portaltech.tui.flights.facades.DealsSearchPanelFacade;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.pojo.DealsFlyOutData;
import uk.co.tui.flights.pojo.SearchRequestData;
import uk.co.tui.flights.service.FlightDealsService;
import uk.co.tui.flights.service.FlightSearchService;


/**
 * @author extpn2
 *
 */
public class DefaultDealsSearchPanelFacade implements DealsSearchPanelFacade
{

	@Resource
	private FlightRouteIndexService flightRouteIndexService;

	@Resource
	private FlightSearchService flightSearchService;

	@Resource
	private FlightDealsService flightDealsService;



	@Resource
	private CMSSiteService cmsSiteService;

	private static final String ERROR_CODE = "6002";


	@Override
	public DealsFlyOutData getFlyOutDurations(final String seasonEndDate)
	{
		final List<String> availableDates = flightSearchService.filterFlightDates(seasonEndDate,
				flightRouteIndexService.findAllDepartureFlightDates());

		return flightDealsService.createDealsFlyoutData(availableDates);
	}


	@Override
	public DealsFlyOutData getFlyOutDurations(final SearchRequestData searchRequestData, final String seasonEndDate)
	{
		final List<String> availableDates = flightSearchService.filterFlightDates(
				seasonEndDate,
				flightRouteIndexService.findDepartureFlightDates(searchRequestData.getFromAirports(),
						searchRequestData.getToAirports()));

		return flightDealsService.createDealsFlyoutData(availableDates);
	}


	/**
	 * This method is used to get the arrival airport list in the 'Arriving To' field in the search panel
	 */
	@Override
	public Map<String, List<AirportData>> getArrivalData(final SearchRequestData searchRequestData)
			throws SearchResultsBusinessException
	{
		Map<String, List<AirportData>> arrivalAirportGuide = null;

		try
		{
			if (checkForSearchPanelValues(searchRequestData))
			{
				arrivalAirportGuide = flightSearchService.fetchArrivalAirports(cmsSiteService.getCurrentCatalogVersion());
			}
			else
			{
				arrivalAirportGuide = flightSearchService.fetchArrivalAirports(cmsSiteService.getCurrentCatalogVersion(),
						searchRequestData.getFromAirports(), flightDealsService.getDealsSearchPanelAllValidDates(searchRequestData));
			}
		}
		catch (final FlightsServiceException e)
		{
			throw new SearchResultsBusinessException(ERROR_CODE, e);
		}

		return arrivalAirportGuide;
	}


	private boolean checkForSearchPanelValues(final SearchRequestData searchRequestData)
	{
		return (searchRequestData == null || (CollectionUtils.isEmpty(searchRequestData.getFromAirports())
				&& StringUtils.isEmpty(searchRequestData.getDays()) && StringUtils.isEmpty(searchRequestData.getMonth())));
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.co.portaltech.tui.flights.facades.DealsSearchPanelFacade#getDepartureData(uk.co.tui.flights.pojo.SearchRequestData
	 * )
	 */
	@Override
	public Map<String, List<AirportData>> getDepartureData(final SearchRequestData searchRequestData)
			throws SearchResultsBusinessException
	{
		Map<String, List<AirportData>> departureAirportGuide = null;

		try
		{
			if (checkPreSelectedValues(searchRequestData))
			{
				departureAirportGuide = flightSearchService.fetchDepartingFromAirports(cmsSiteService.getCurrentCatalogVersion());
			}
			else
			{
				departureAirportGuide = flightSearchService.fetchDepartingFromAirports(cmsSiteService.getCurrentCatalogVersion(),
						searchRequestData.getToAirports(), flightDealsService.getDealsSearchPanelAllValidDates(searchRequestData));
			}
		}
		catch (final FlightsServiceException e)
		{
			throw new SearchResultsBusinessException(ERROR_CODE, e);
		}

		return departureAirportGuide;
	}


	/**
	 * @param searchRequestData
	 * @return
	 */
	private boolean checkPreSelectedValues(final SearchRequestData searchRequestData)
	{
		return (searchRequestData == null || (CollectionUtils.isEmpty(searchRequestData.getToAirports())
				&& StringUtils.isEmpty(searchRequestData.getDays()) && StringUtils.isEmpty(searchRequestData.getMonth())));
	}

	/**
    *
    */
	@Override
	public boolean isTracsInventory()
	{
		return flightSearchService.verifyTracsInventory();

	}
}
