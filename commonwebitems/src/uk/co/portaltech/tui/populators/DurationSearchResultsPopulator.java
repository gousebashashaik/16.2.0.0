package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.tui.web.view.data.SearchResultDurationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * This class is responsible to populate durationViewData into searchResultsViewData.
 *
 * @author gaurav.b
 *
 */
public class DurationSearchResultsPopulator implements Populator<EndecaSearchResult, SearchResultsViewData>
{

    private static final TUILogUtils LOG = new TUILogUtils("DurationSearchResultsPopulator");

    private static final int MAX_ITEMS_IN_DEFAULT_DISPLAY_SLOT = 4;

    /**
     * This method delegates the job of populating durationViewData into searchResultsViewData to
     * populateSearchResultDurationViewData().
     */
    @Override
    public void populate(final EndecaSearchResult source, final SearchResultsViewData target) throws ConversionException
    {

        if (StringUtils.isNotEmpty(source.getAvailableDurations()))
        {
            populateSearchResultDurationViewData(source.getAvailableDurations(), target);
        }
        else
        {
            final SearchResultDurationViewData durationViewData = new SearchResultDurationViewData();
            target.setDurationSelection(durationViewData);
        }
    }

    /**
     * This method actually populates durationViewData into searchResultsViewData
     *
     * @param availableDurations
     * @param target
     */
    private void populateSearchResultDurationViewData(final String availableDurations, final SearchResultsViewData target)
    {
        final SearchResultDurationViewData durationViewData = new SearchResultDurationViewData();
        /* get ready durationViewData before setting it to searchResultsViewData */
        setDurationViewData(durationViewData, availableDurations);
        /* sets the durationViewData to searchResultsViewData the target */
        target.setDurationSelection(durationViewData);
    }

    /**
     * This method prepares the durationViewData after taking the availableDurations value(the comma separated values)
     * form EndecaSearchResult.
     *
     * @param availableDurations
     */
    private void setDurationViewData(final SearchResultDurationViewData durationViewData, final String availableDurations)
    {
        int counter = 0;
        final Set<Integer> defaultDisplay = new TreeSet<Integer>();
        final Set<Integer> moreChoices = new TreeSet<Integer>();
        for (final Integer val : sortAvailableDurations(durationViewData, availableDurations))
        {
            if (counter < MAX_ITEMS_IN_DEFAULT_DISPLAY_SLOT)
            {
                defaultDisplay.add(val);
                counter++;
            }
            else
            {
                moreChoices.add(val);
            }
        }
        durationViewData.setDefaultDisplay(defaultDisplay);
        durationViewData.setMoreChoices(moreChoices);
    }

    /**
     * This method is responsible to sort the available durations is ascending order.
     *
     * @param availableDurations
     * @return Set<Integer> the set of Integers.
     */
    private Set<Integer> sortAvailableDurations(final SearchResultDurationViewData durationViewData,
            final String availableDurations)
    {
        final String[] durations = availableDurations.split(",");
        String temp;
        final SortedSet<Integer> durationSet = new TreeSet<Integer>();
        for (int i = 0; i < durations.length; i++)
        {
            temp = durations[i].trim();
            if (StringUtils.isNotEmpty(temp) && StringUtils.isNumeric(temp))
            {
                durationSet.add(Integer.valueOf(temp));

                if (durationSet.size() == 1)
                {
                    durationViewData.setActiveDuration(durationSet.first());
                }
            }
            else
            {
                LOG.error("availableDurations sent by endeca contains non numeric value : " + temp);
            }
        }
        return durationSet;
    }
}
