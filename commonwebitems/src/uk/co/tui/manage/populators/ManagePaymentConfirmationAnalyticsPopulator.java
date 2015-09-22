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
import uk.co.tui.manage.viewdata.PaymentConfirmationViewData;


/**
 * @author sunil.bd
 *
 */
public class ManagePaymentConfirmationAnalyticsPopulator implements Populator<Object, Map<String, WebAnalytics>>
{

    @Resource
    private AnalyticsHelper analyticsHelper;

    private static final String PAX_TYPE = "PayType";

    private static final String SRCBKGSYS = "SrcBkgSys";

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final Object source, final Map<String, WebAnalytics> analyticMap) throws ConversionException
    {
        if (source instanceof PaymentConfirmationViewData)
        {
            final PaymentConfirmationViewData paymentConfirmationViewData = (PaymentConfirmationViewData) source;

            populateSrcBkgRef(analyticMap, paymentConfirmationViewData);
            populateSrcBkgSys(analyticMap, paymentConfirmationViewData);
            populateDepDate(analyticMap, paymentConfirmationViewData);
            populateBookingAmount(analyticMap, paymentConfirmationViewData);
            populatePaymentType(analyticMap, paymentConfirmationViewData);
            populateTransactionIdentifier(analyticMap, paymentConfirmationViewData);
        }

    }

    /**
     * @param analyticMap
     * @param paymentConfirmationViewData
     */
    private void populatePaymentType(final Map<String, WebAnalytics> analyticMap,
            final PaymentConfirmationViewData paymentConfirmationViewData)
    {
        if (StringUtils.isNotEmpty(paymentConfirmationViewData.getCardType()))
        {
            analyticMap.put(PAX_TYPE, new WebAnalytics(PAX_TYPE, paymentConfirmationViewData.getCardType()));
        }
        else
        {
            analyticMap.put(PAX_TYPE, new WebAnalytics(PAX_TYPE, ""));
        }
    }

    /**
     * @param analyticMap
     * @param paymentConfirmationViewData
     */
    private void populateSrcBkgSys(final Map<String, WebAnalytics> analyticMap,
            final PaymentConfirmationViewData paymentConfirmationViewData)
    {
        if (StringUtils.isNotEmpty(paymentConfirmationViewData.getInventoryType()))
        {
            final WebAnalytics webAnalyticSrcBkgSys = new WebAnalytics(SRCBKGSYS, paymentConfirmationViewData.getInventoryType());
            analyticMap.put(SRCBKGSYS, webAnalyticSrcBkgSys);
        }
        else
        {
            analyticMap.put(SRCBKGSYS, new WebAnalytics(SRCBKGSYS, ""));
        }

    }

    /**
     * @param analyticMap
     * @param paymentConfirmationViewData
     */
    private void populateTransactionIdentifier(final Map<String, WebAnalytics> analyticMap,
            final PaymentConfirmationViewData paymentConfirmationViewData)
    {
        final String rp = "-RP-";
        final String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        final LocalDate todaysDate = DateUtils.toDate(formattedDate);
        final String date = DateUtils.formattedDate(todaysDate);
        final StringBuilder yearWithMonth = new StringBuilder();
        yearWithMonth.append(DateUtils.formattedMonth(todaysDate));
        yearWithMonth.append(todaysDate.getYear());
        final StringBuilder dateWithMonthYear = new StringBuilder(date);
        dateWithMonthYear.append(yearWithMonth);
        final String transactionIdentifier = paymentConfirmationViewData.getBookingRef() + rp + dateWithMonthYear;

        analyticMap.put("RPRef", new WebAnalytics("RPRef", transactionIdentifier));
    }

    /**
     * @param analyticMap
     * @param paymentConfirmationViewData
     */
    private void populateBookingAmount(final Map<String, WebAnalytics> analyticMap,
            final PaymentConfirmationViewData paymentConfirmationViewData)
    {
        int totalAmount = 0;
        int amountDue = 0;
        int transactionAmount = 0;

        if (StringUtils.isNotEmpty(paymentConfirmationViewData.getTotalPriceExclusive().toString()))
        {
            totalAmount = analyticsHelper.getPriceRoundedValue(paymentConfirmationViewData.getTotalPriceExclusive().toString());
        }
        if (StringUtils.isNotEmpty(paymentConfirmationViewData.getDueAmount().toString()))
        {

            amountDue = analyticsHelper.getPriceRoundedValue(paymentConfirmationViewData.getDueAmount().toString());
        }
        if (StringUtils.isNotEmpty(paymentConfirmationViewData.getTransactionAmount().toString()))
        {
            transactionAmount = analyticsHelper.getPriceRoundedValue(paymentConfirmationViewData.getTransactionAmount().toString());
        }

        analyticMap.put("Amount", new WebAnalytics("Amount", String.valueOf(totalAmount)));
        analyticMap.put("AmountOutstanding", new WebAnalytics("AmountOutstanding", String.valueOf(amountDue)));
        analyticMap.put("TransactionValue", new WebAnalytics("TransactionValue", String.valueOf(transactionAmount)));

    }

    /**
     * @param analyticMap
     * @param paymentConfirmationViewData
     */
    private void populateDepDate(final Map<String, WebAnalytics> analyticMap,
            final PaymentConfirmationViewData paymentConfirmationViewData)
    {
        if (paymentConfirmationViewData.getDepDate() != null)
        {
            final SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy");
            final String newFormatDate = newformat.format(paymentConfirmationViewData.getDepDate());
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
     * @param paymentConfirmationViewData
     */
    private void populateSrcBkgRef(final Map<String, WebAnalytics> analyticMap,
            final PaymentConfirmationViewData paymentConfirmationViewData)
    {
        final String bookinfRefNum = paymentConfirmationViewData.getBookingRef();
        final WebAnalytics webAnalyticSrcBkgRef = new WebAnalytics("SrcBkgRef", bookinfRefNum);
        analyticMap.put("SrcBkgRef", webAnalyticSrcBkgRef);
    }

}
