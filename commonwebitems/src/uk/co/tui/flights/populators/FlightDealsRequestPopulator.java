/**
 *
 */
package uk.co.tui.flights.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.pojo.SearchRequestData;


/**
 * @author arunkumar.v
 *
 */
public class FlightDealsRequestPopulator implements Populator<SearchRequestData, FlightSearchCriteria>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SearchRequestData source, final FlightSearchCriteria target) throws ConversionException
	{

		setFlyOutData(source, target);

		target.setDepartureAirportCode(source.getFromAirports());
		if (CollectionUtils.isNotEmpty(source.getDestinationCode()))
		{
			target.setArrivalAirportCode(source.getDestinationCode());
		}
		else
		{
			target.setArrivalAirportCode(source.getToAirports());
		}

		target.setOneWay(source.isOneWay());
		target.setFlexible(source.isFlexible());
		target.setSearchCriteriaType(source.getSearchType());
		target.setResponseType(source.getResponseType());
		target.setOutboundSlots(source.getOutboundSlots());
		target.setPageCount(source.getPageCount());

		setPaxDetails(source, target);

		setDurationFilterData(source, target);

		target.setMaxPrice(source.getMaxPrice());

	}

	/**
	 * @param source
	 * @param target
	 */
	private void setPaxDetails(final SearchRequestData source, final FlightSearchCriteria target)
	{
		target.setAdultCount(StringUtils.isNotEmpty(source.getAdultCount()) ? Integer.parseInt(source.getAdultCount()) : 0);
		target.setChildCount(StringUtils.isNotEmpty(source.getChildCount()) ? Integer.parseInt(source.getChildCount()) : 0);
		target.setChildages(source.getChildages());
	}

	/**
	 * @param source
	 * @param target
	 */
	private void setDurationFilterData(final SearchRequestData source, final FlightSearchCriteria target)
	{
		target.setStartDuration(StringUtils.isNotEmpty(source.getStartDuration()) ? Integer.parseInt(source.getStartDuration()) : 0);
		target.setEndDuration(StringUtils.isNotEmpty(source.getEndDuration()) ? Integer.parseInt(source.getEndDuration()) : 0);
	}

	/**
	 * @param source
	 * @param target
	 */
	private void setFlyOutData(final SearchRequestData source, final FlightSearchCriteria target)
	{
		target.setDays(StringUtils.isNotEmpty(source.getDays()) ? Integer.parseInt(source.getDays()) : 0);
		target.setMonth(StringUtils.isNotEmpty(source.getMonth()) ? Integer.parseInt(source.getMonth()) : 0);
		target.setYear(StringUtils.isNotEmpty(source.getYear()) ? Integer.parseInt(source.getYear()) : 0);
	}

}
