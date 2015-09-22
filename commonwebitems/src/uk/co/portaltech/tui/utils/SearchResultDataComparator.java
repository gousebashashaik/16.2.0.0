package uk.co.portaltech.tui.utils;


import java.util.Comparator;

import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.tui.web.common.enums.SortParameters;


public class SearchResultDataComparator implements Comparator<SearchResultViewData>
{

    private final SortParameters[] parameters;

    public SearchResultDataComparator(final SortParameters[] parameters)
    {
        this.parameters = parameters.clone();
    }

    @Override
    public int compare(final SearchResultViewData holidayObj1, final SearchResultViewData holidayObj2)
    {
        LocalDate dateObj1 = null, dateObj2 = null;
        double diff = 0;
        int comparison = 0;
        for (final SortParameters parameter : parameters)
        {

            switch (parameter)
            {
                case PRICE_ASCENDING:
                    diff = Double.parseDouble(holidayObj1.getPrice().getTotalParty())
                            - Double.parseDouble(holidayObj2.getPrice().getTotalParty());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case PRICE_DESCENDING:
                    diff = Double.parseDouble(holidayObj2.getPrice().getTotalParty())
                            - Double.parseDouble(holidayObj1.getPrice().getTotalParty());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case NAME_ASCENDING:
                    comparison = holidayObj1.getAccommodation().getName().compareTo(holidayObj2.getAccommodation().getName());
                    if (comparison != 0)
                    {
                        return comparison;
                    }
                    break;

                case NAME_DESCENDING:
                    comparison = holidayObj2.getAccommodation().getName().compareTo(holidayObj1.getAccommodation().getName());
                    if (comparison != 0)
                    {
                        return comparison;
                    }
                    break;

                case COMMERICAL_PRIORITY_ASCENDING:
                    comparison = holidayObj1.getAccommodation().getCommercialPriority()
                            - holidayObj2.getAccommodation().getCommercialPriority();
                    if (comparison != 0)
                    {
                        return comparison;
                    }
                    break;

                case COMMERICAL_PRIORITY_DESCENDING:
                    comparison = holidayObj2.getAccommodation().getCommercialPriority()
                            - holidayObj1.getAccommodation().getCommercialPriority();
                    if (comparison != 0)
                    {
                        return comparison;
                    }
                    break;

                case TRATING_ASCENDING:
                    diff = Double.parseDouble(holidayObj1.getAccommodation().getRatings().getOfficialRating())
                            - Double.parseDouble(holidayObj2.getAccommodation().getRatings().getOfficialRating());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case TRATING_DESCENDING:
                    diff = Double.parseDouble(holidayObj2.getAccommodation().getRatings().getOfficialRating())
                            - Double.parseDouble(holidayObj1.getAccommodation().getRatings().getOfficialRating());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case SAVINGS_AMOUNT_ASCENDING:
                    diff = Double.parseDouble(holidayObj1.getPrice().getDiscount())
                            - Double.parseDouble(holidayObj2.getPrice().getDiscount());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case SAVINGS_AMOUNT_DESCENDING:
                    diff = Double.parseDouble(holidayObj2.getPrice().getDiscount())
                            - Double.parseDouble(holidayObj1.getPrice().getDiscount());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case TRIP_ADVISOR_RATING_ASCENDING:
                    diff = Double.parseDouble(holidayObj1.getAccommodation().getRatings().getTripAdvisorRating())
                            - Double.parseDouble(holidayObj2.getAccommodation().getRatings().getTripAdvisorRating());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case TRIP_ADVISOR_RATING_DESCENDING:
                    diff = Double.parseDouble(holidayObj2.getAccommodation().getRatings().getTripAdvisorRating())
                            - Double.parseDouble(holidayObj1.getAccommodation().getRatings().getTripAdvisorRating());
                    if (diff > 0)
                    {
                        return 1;
                    }
                    if (diff < 0)
                    {
                        return -1;
                    }
                    break;

                case DEPARTURE_DATE_ASCENDING:

                    dateObj1 = DateUtils.toDate(holidayObj1.getItinerary().getDepartureDate());
                    dateObj2 = DateUtils.toDate(holidayObj2.getItinerary().getDepartureDate());
                    comparison = dateObj1.compareTo(dateObj2);
                    if (comparison != 0)
                    {
                        return comparison;
                    }
                    break;

                case DEPARTURE_DATE_DESCENDING:

                    dateObj1 = DateUtils.toDate(holidayObj2.getItinerary().getDepartureDate());
                    dateObj2 = DateUtils.toDate(holidayObj1.getItinerary().getDepartureDate());
                    comparison = dateObj1.compareTo(dateObj2);
                    if (comparison != 0)
                    {
                        return comparison;
                    }
                    break;
            }
        }
        return 0;
    }

}
