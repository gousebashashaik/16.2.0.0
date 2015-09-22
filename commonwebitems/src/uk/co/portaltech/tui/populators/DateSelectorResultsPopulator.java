
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.DateSelectionValue;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.SearchResultDateSelectionViewData;

/**
 * @author laxmibai.p
 *
 */

public class DateSelectorResultsPopulator implements
        Populator<EndecaSearchResult, SearchResultsViewData> {


    @Resource
    private ViewSelector viewSelector;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     *
     * This method populates endeca available results to SearchResultsViewData
     */
    @Override
    public void populate(EndecaSearchResult source, SearchResultsViewData target)
            throws ConversionException {

        final SearchResultDateSelectionViewData dateSelectionViewData = new SearchResultDateSelectionViewData();
        dateSelectionViewData.setAvailableValues(getDateSelectorDates(source.getDateSelectorDates()));
        if (viewSelector.checkIsMobile())
        {
            dateSelectionViewData.setAvailableValuesForMobile(getDateSelectorDatesForMobile(source.getDateSelectorDates()));
        }
        target.setAvailableDates(dateSelectionViewData);

    }


    /**
     * @param dateSelectorDates
     * @return
     */
    @SuppressWarnings("deprecation")
    private List<Date> getDateSelectorDatesForMobile(
            List<DateSelectionValue> endecaDates) {
        List<Date> availableValues = new ArrayList<Date>();
        for (DateSelectionValue dateSelectionValue : endecaDates) {
            if (StringUtils.equals("Y", dateSelectionValue.getAvailable())) {
                DateTime date = dateSelectionValue.getDate();
                if (date != null) {
                    Date formattedDate = new Date(DateUtils.newDateFormat(date));
                    availableValues.add(formattedDate);
                }
            }
        }

        return availableValues;
    }

    /**
     * @param endecaDates
     * @return This method provides available dates from endeca
     */
    private List<String> getDateSelectorDates(
            List<DateSelectionValue> endecaDates) {
        List<String> availableValues = new ArrayList<String>();
        for (DateSelectionValue dateSelectionValue : endecaDates) {
            if (StringUtils.equals("Y", dateSelectionValue.getAvailable())) {
                DateTime date = dateSelectionValue.getDate();
                if (date != null) {
                    availableValues.add(DateUtils.format(date));
                }
            }
        }

        return availableValues;
    }

}
