/**
 *
 */
package uk.co.portaltech.tui.comparators.sort;

import java.util.Comparator;
import java.util.List;

import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.pojo.FlightStatusAutoSuggestData;
import uk.co.tui.services.flightstatus.pojo.FlightDetails;
import uk.co.tui.services.flightstatus.pojo.FlightStatusDetails;


/**
 * @author perlagangi.r
 *
 */
public enum FlightStatusAttributesComparator implements Comparator<Object>
{

	FLIGHT_NUMBER_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final FlightDetails flightDetails1 = (FlightDetails) object1;
			final FlightDetails flightDetails2 = (FlightDetails) object2;
			final int flinghtnumber1 = Integer.parseInt(flightDetails1.getFlightshortno());
			final int flinghtnumber2 = Integer.parseInt(flightDetails2.getFlightshortno());
			final int diff = flinghtnumber1 - flinghtnumber2;
			if (diff > 0)
			{
				return 1;
			}
			if (diff < 0)
			{
				return -1;
			}
			return 0;
		}
	},

	ARRIVAL_DATE_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final FlightStatusDetails flightStatusDetails1 = (FlightStatusDetails) object1;
			final FlightStatusDetails flightStatusDetails2 = (FlightStatusDetails) object2;
			return flightStatusDetails1.getScharrtm().compareTo(flightStatusDetails2.getScharrtm());
		}
	},

	DEPARTURE_DATE_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final FlightStatusDetails flightStatusDetails1 = (FlightStatusDetails) object1;
			final FlightStatusDetails flightStatusDetails2 = (FlightStatusDetails) object2;
			return flightStatusDetails1.getSchdeptm().compareTo(flightStatusDetails2.getSchdeptm());
		}
	},

	DEPARTURE_AIRPORTINFO_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final FlightStatusAutoSuggestData flightStatusAutoSuggestData1 = (FlightStatusAutoSuggestData) object1;
			final FlightStatusAutoSuggestData flightStatusAutoSuggestData2 = (FlightStatusAutoSuggestData) object2;
			return flightStatusAutoSuggestData1.getDepAirPortInfo().compareTo(flightStatusAutoSuggestData2.getDepAirPortInfo());
		}
	},

	ARRIVAL_AIRPORTINFO_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final FlightStatusAutoSuggestData flightStatusAutoSuggestData1 = (FlightStatusAutoSuggestData) object1;
			final FlightStatusAutoSuggestData flightStatusAutoSuggestData2 = (FlightStatusAutoSuggestData) object2;
			return flightStatusAutoSuggestData1.getArrAirPortInfo().compareTo(flightStatusAutoSuggestData2.getArrAirPortInfo());
		}
	},

	AIRPORTNAME_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final AirportData airportData1 = (AirportData) object1;
			final AirportData airportData2 = (AirportData) object2;
			return airportData1.getName().compareTo(airportData2.getName());
		}
	};


	public static Comparator<Object> getComparator(final List<FlightStatusAttributesComparator> sortComparators)
	{
		return new Comparator<Object>()
		{
			@Override
			public int compare(final Object o1, final Object o2)
			{
				return compareSort(sortComparators, o1, o2);
			}
		};
	}

	/**
	 * @param sortComparators
	 * @param o1
	 * @param o2
	 * @return integer
	 */
	private static int compareSort(final List<FlightStatusAttributesComparator> sortComparators, final Object o1, final Object o2)
	{
		for (final FlightStatusAttributesComparator sortComparator : sortComparators)
		{
			final int result = sortComparator.compare(o1, o2);
			if (result != 0)
			{
				return result;
			}
		}
		return 0;
	}
}
