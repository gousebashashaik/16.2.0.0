/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Set;

import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultDurationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.SearchResultDateSelectionViewData;


/**
 * @author arun.y
 *
 */
public class BackToSearchSortByPopulator implements Populator<HolidayViewData, SearchResultsRequestData>
{
    @Override
    public void populate(final HolidayViewData source, final SearchResultsRequestData target) throws ConversionException
    {
        //Changes for Sort By
        final String backToSearchSortBy = target.getSortBy();

        if (backToSearchSortBy != null && !backToSearchSortBy.isEmpty())
        {
            source.getSearchRequest().setSortBy(backToSearchSortBy);
        }

        //Changes for Retaining Duration
        final Set<Integer> defaultDisplay = target.getDurationSelection().getDefaultDisplay();
        final int activeDuration = target.getDuration();
        final Set<Integer> moreChoices = target.getDurationSelection().getMoreChoices();
        final SearchResultDurationViewData durationSelection = source.getSearchRequest().getDurationSelection();

        setActiveDuration(source, defaultDisplay, activeDuration, moreChoices, durationSelection);

        //Changes for Retaining Date Slider
        final List<String> availableValues = target.getAvailableDates().getAvailableValues();
        final String when = target.getWhen();
        final String until = target.getUntil();


        final String depatureDate = target.getAvailableDates().getDepatureDate();
        final int flexibleDays = target.getAvailableDates().getFlexibleDays();
        final SearchResultDateSelectionViewData availableDates = source.getSearchRequest().getAvailableDates();

        if (availableDates != null)
        {
            if (availableValues != null)
            {
                availableDates.setAvailableValues(availableValues);
            }
            if (verifyWhenTrueOrFalse(when, until))
            {
                availableDates.setMaxValue(when);
                availableDates.setMinValue(when);
            }
            if (depatureDate != null)
            {
                availableDates.setDepatureDate(depatureDate);
            }
            availableDates.setFlexibleDays(flexibleDays);
            source.getSearchResult().setAvailableDates(availableDates);
        }
    }

    /**
     * @param when
     * @param until
     * @return boolean
     */
    private boolean verifyWhenTrueOrFalse(final String when, final String until)
    {
        return when != null && ((!when.isEmpty() && until.isEmpty()) || when.equals(until));
    }

    /**
     * @param source
     * @param defaultDisplay
     * @param activeDuration
     * @param moreChoices
     * @param duration
     */
    private void setActiveDuration(final HolidayViewData source, final Set<Integer> defaultDisplay, final int activeDuration,
            final Set<Integer> moreChoices, final SearchResultDurationViewData duration)
    {
        final SearchResultDurationViewData durationSelection = duration;
        if (durationSelection != null)
        {
            if (defaultDisplay != null)
            {
                durationSelection.setDefaultDisplay(defaultDisplay);
            }
            if (moreChoices != null)
            {
                durationSelection.setMoreChoices(moreChoices);
            }
            durationSelection.setActiveDuration(Integer.valueOf(activeDuration));
            source.getSearchResult().setDurationSelection(durationSelection);
        }
    }
}
