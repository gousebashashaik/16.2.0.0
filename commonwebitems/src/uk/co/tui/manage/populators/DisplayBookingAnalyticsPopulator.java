/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.helper.AnalyticsHelper;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.manage.viewdata.bookingsummary.BookingSummaryPageViewData;


/**
 * @author sunil.bd
 *
 */
public class DisplayBookingAnalyticsPopulator implements Populator<Object, Map<String, WebAnalytics>>
{

    @Resource
    private AnalyticsHelper analyticsHelper;



    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final Object source, final Map<String, WebAnalytics> analyticMap) throws ConversionException
    {
        if (source instanceof BookingSummaryPageViewData)
        {
            final BookingSummaryPageViewData bookingSummaryPageViewData = (BookingSummaryPageViewData) source;

            populateSrcBkgRef(analyticMap, bookingSummaryPageViewData);
            populateSrcBkgSys(analyticMap, bookingSummaryPageViewData);
            populateDepDate(analyticMap, bookingSummaryPageViewData);
            populateBookingAmount(analyticMap, bookingSummaryPageViewData);

        }


    }


    /**
     * @param analyticMap
     * @param bookingSummaryPageViewData
     */
    private void populateBookingAmount(final Map<String, WebAnalytics> analyticMap,
            final BookingSummaryPageViewData bookingSummaryPageViewData)
    {
        int totalAmount = 0;
        int amountDue = 0;
        if (bookingSummaryPageViewData.getPackageViewData().getRoundUpTotalCost() != null)
        {
            totalAmount = analyticsHelper.getPriceRoundedValue(bookingSummaryPageViewData.getPackageViewData().getRoundUpTotalCost()
                    .toString());
        }
        if (StringUtils.isNotEmpty(bookingSummaryPageViewData.getPackageViewData().getBookingDetails().getAmountDue().toString()))
        {

            amountDue = analyticsHelper.getPriceRoundedValue(bookingSummaryPageViewData.getPackageViewData().getBookingDetails()
                    .getAmountDue().getAmount().toString());
        }
        analyticMap.put("Amount", new WebAnalytics("Amount", String.valueOf(totalAmount)));
        analyticMap.put("AmountOutstanding", new WebAnalytics("AmountOutstanding", String.valueOf(amountDue)));

    }

    /**
     * @param analyticMap
     * @param bookingSummaryPageViewData
     */
    private void populateDepDate(final Map<String, WebAnalytics> analyticMap,
            final BookingSummaryPageViewData bookingSummaryPageViewData)
    {
        if (StringUtils.isNotEmpty(bookingSummaryPageViewData.getPackageViewData().getFlightViewData().getOutboundSectors().get(0)
                .getSchedule().getDepartureDate()))
        {
            final String depDate = bookingSummaryPageViewData.getPackageViewData().getFlightViewData().getOutboundSectors().get(0)
                    .getSchedule().getDepartureDate();
            final SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy");
            final String newFormatDate = newformat.format(new Date(depDate));
            final LocalDate departureDate = DateUtils.toDate(newFormatDate);
            analyticsHelper.addFormattedDate(analyticMap, departureDate);
            analyticsHelper.addMonthYear(analyticMap, departureDate);
        }
        else
        {
            analyticMap.put("MonthYear", new WebAnalytics("MonthYear", ""));
            analyticMap.put("DepDate", new WebAnalytics("DepDate", ""));
        }

    }

    /**
     * @param analyticMap
     * @param bookingSummaryPageViewData
     */
    private void populateSrcBkgSys(final Map<String, WebAnalytics> analyticMap,
            final BookingSummaryPageViewData bookingSummaryPageViewData)
    {
        final String srcBkgSys = "SrcBkgSys";
        if (StringUtils.isNotEmpty(bookingSummaryPageViewData.getPackageViewData().getInventoryType()))
        {
                final WebAnalytics webAnalyticSrcBkgSys = new WebAnalytics(srcBkgSys, bookingSummaryPageViewData.getPackageViewData().getInventoryType());
                analyticMap.put(srcBkgSys, webAnalyticSrcBkgSys);
        }
        else
        {
            analyticMap.put(srcBkgSys, new WebAnalytics(srcBkgSys, ""));
        }
    }

    /**
     * @param analyticMap
     * @param bookingSummaryPageViewData
     */
    private void populateSrcBkgRef(final Map<String, WebAnalytics> analyticMap,
            final BookingSummaryPageViewData bookingSummaryPageViewData)
    {
        final String bookinfRefNum = bookingSummaryPageViewData.getPackageViewData().getBookingRefNum();
        final WebAnalytics webAnalyticSrcBkgRef = new WebAnalytics("SrcBkgRef", bookinfRefNum);
        analyticMap.put("SrcBkgRef", webAnalyticSrcBkgRef);
    }

}
