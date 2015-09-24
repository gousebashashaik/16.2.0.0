/**
 *
 */
package uk.co.portaltech.tui.comparators.sort;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.Price;


/**
 * @author perlagangi.r
 *
 */
public enum FlightDealsSortComparator implements Comparator<Object>
{


	DESTINATION_NAME_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final Itinerary itinerary1 = (Itinerary) object1;
			final Itinerary itinerary2 = (Itinerary) object2;
			final String arrName1 = getArrivalAirport1(itinerary1.getOutBound());
			final String arrName2 = getArrivalAirport2(itinerary2.getOutBound());

			if (checkArrivalAirportNames(arrName1, arrName2))
			{
				return 0;
			}
			if (StringUtils.isBlank(arrName1))
			{
				return 1;
			}
			if (StringUtils.isBlank(arrName2))
			{
				return -1;
			}

			return arrName1.compareTo(arrName2);
		}

	},

	PER_PERSON_PRICE_ASCENDING
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			final Itinerary itinerary1 = (Itinerary) object1;
			final Itinerary itinerary2 = (Itinerary) object2;
			final int amount1 = getAmount1(itinerary1.getPriceList());
			final int amount2 = getAmount2(itinerary2.getPriceList());

			return getAmountDiff(amount1, amount2);
		}
	};

	public static Comparator<Object> getComparator(final List<FlightDealsSortComparator> sortComparators)
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
	private static int compareSort(final List<FlightDealsSortComparator> sortComparators, final Object o1, final Object o2)
	{
		for (final FlightDealsSortComparator sortComparator : sortComparators)
		{
			final int result = sortComparator.compare(o1, o2);
			if (result != 0)
			{
				return result;
			}
		}
		return 0;
	}


	/**
	 * @param leg1
	 */
	private static String getArrivalAirport1(final List<Leg> leg1)
	{
		String arrName1 = null;
		for (final Leg leg : leg1)
		{
			arrName1 = leg.getArrivalAirport().getName();
		}
		return arrName1;
	}

	/**
	 * @param leg2
	 */
	private static String getArrivalAirport2(final List<Leg> leg2)
	{
		String arrName1 = null;
		for (final Leg leg : leg2)
		{
			arrName1 = leg.getArrivalAirport().getName();
		}
		return arrName1;
	}

	/**
	 * @param arrName1
	 * @param arrName2
	 * @return boolean
	 */
	private static boolean checkArrivalAirportNames(final String arrName1, final String arrName2)
	{
		return StringUtils.isBlank(arrName1) && StringUtils.isBlank(arrName2);
	}

	private static int getAmount1(final List<Price> prices)
	{
		int amount1 = 0;
		for (final Price price : prices)
		{
			final BigDecimal money = price.getRate().getAmount();
			amount1 = money.intValue();
		}
		return amount1;
	}

	private static int getAmount2(final List<Price> prices)
	{
		int amount2 = 0;
		for (final Price price : prices)
		{
			final BigDecimal money = price.getRate().getAmount();
			amount2 = money.intValue();
		}
		return amount2;
	}

	/**
	 * @param amount1
	 * @param amount2
	 * @return integer
	 */
	private static int getAmountDiff(final int amount1, final int amount2)
	{
		final int diff = amount1 - amount2;

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

}
