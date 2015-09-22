/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.math.BigDecimal;
import java.util.Date;

import uk.co.tui.book.anite.request.PaymentInfo;
import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.th.book.view.data.ConfirmationContentViewData;
import uk.co.tui.th.book.view.data.ConfirmationStaticContentViewData;

/**
 * @author veena.pn
 *
 */
public class PaymentConfirmationViewData {

    private String bookingRef;

    private String inventoryType;

    private String currencyAppendedAmtPaid;

    private String currencyAppendedRoundUpTotalCost;

    private BigDecimal transactionAmount;

    private String currencyAppendedTotalPriceExclusive;

    private BigDecimal totalPriceExclusive;

    private BigDecimal roundUpTotalCost;

    private BigDecimal dueAmount;

    private String dueDate;

    private BigDecimal creditCardCharges;

    private String currencyAppendedDueAmount;

    /** The package type. */
    private String packageType;

    private boolean paymentFailure;

    /** The payment information. */
    private PaymentInfo paymentInfo;
    private String redirectUrl;
    /** The confirmation static content view data. */
    private ConfirmationStaticContentViewData confirmationStaticContentViewData;

    /** The confirmation content view data. */
    private ConfirmationContentViewData confirmationContentViewData;

    private Date depDate;
    private PageResponse pageResponse;

    private String cardType;

    /**
     * @return the bookingRef
     */
    public String getBookingRef() {
        return bookingRef;
    }

    /**
     * @param bookingRef
     *            the bookingRef to set
     */
    public void setBookingRef(final String bookingRef) {
        this.bookingRef = bookingRef;
    }

    /**
     * @return the currencyAppendedAmtPaid
     */
    public String getCurrencyAppendedAmtPaid() {
        return currencyAppendedAmtPaid;
    }

    /**
     * @param currencyAppendedAmtPaid
     *            the currencyAppendedAmtPaid to set
     */
    public void setCurrencyAppendedAmtPaid(final String currencyAppendedAmtPaid) {
        this.currencyAppendedAmtPaid = currencyAppendedAmtPaid;
    }

    /**
     * @return the paymentFailure
     */
    public boolean isPaymentFailure() {
        return paymentFailure;
    }

    /**
     * @param paymentFailure
     *            the paymentFailure to set
     */
    public void setPaymentFailure(final boolean paymentFailure) {
        this.paymentFailure = paymentFailure;
    }

    /**
     * @return the confirmationStaticContentViewData
     */
    public ConfirmationStaticContentViewData getConfirmationStaticContentViewData() {
        return confirmationStaticContentViewData;
    }

    /**
     * @param confirmationStaticContentViewData
     *            the confirmationStaticContentViewData to set
     */
    public void setConfirmationStaticContentViewData(
            final ConfirmationStaticContentViewData confirmationStaticContentViewData) {
        this.confirmationStaticContentViewData = confirmationStaticContentViewData;
    }

    /**
     * @return the confirmationContentViewData
     */
    public ConfirmationContentViewData getConfirmationContentViewData() {
        return confirmationContentViewData;
    }

    /**
     * @param confirmationContentViewData
     *            the confirmationContentViewData to set
     */
    public void setConfirmationContentViewData(
            final ConfirmationContentViewData confirmationContentViewData) {
        this.confirmationContentViewData = confirmationContentViewData;
    }

    /**
     * @return the packageType
     */
    public String getPackageType() {
        return packageType;
    }

    /**
     * @param packageType
     *            the packageType to set
     */
    public void setPackageType(final String packageType) {
        this.packageType = packageType;
    }

    /**
     * @return the currencyAppendedRoundUpTotalCost
     */
    public String getCurrencyAppendedRoundUpTotalCost() {
        return currencyAppendedRoundUpTotalCost;
    }

    /**
     * @param currencyAppendedRoundUpTotalCost
     *            the currencyAppendedRoundUpTotalCost to set
     */
    public void setCurrencyAppendedRoundUpTotalCost(
            final String currencyAppendedRoundUpTotalCost) {
        this.currencyAppendedRoundUpTotalCost = currencyAppendedRoundUpTotalCost;
    }

    /**
     * @return the roundUpTotalCost
     */
    public BigDecimal getRoundUpTotalCost() {
        return roundUpTotalCost;
    }

    /**
     * @param roundUpTotalCost
     *            the roundUpTotalCost to set
     */
    public void setRoundUpTotalCost(final BigDecimal roundUpTotalCost) {
        this.roundUpTotalCost = roundUpTotalCost;
    }

    /**
     * @return the dueAmount
     */
    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    /**
     * @param dueAmount
     *            the dueAmount to set
     */
    public void setDueAmount(final BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    /**
     * @return the dueDate
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate
     *            the dueDate to set
     */
    public void setDueDate(final String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the currencyAppendedDueAmount
     */
    public String getCurrencyAppendedDueAmount() {
        return currencyAppendedDueAmount;
    }

    /**
     * @param currencyAppendedDueAmount
     *            the currencyAppendedDueAmount to set
     */
    public void setCurrencyAppendedDueAmount(
            final String currencyAppendedDueAmount) {
        this.currencyAppendedDueAmount = currencyAppendedDueAmount;
    }

    /**
     * @return the redirectUrl
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * @param redirectUrl
     *            the redirectUrl to set
     */
    public void setRedirectUrl(final String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * @return the pageResponse
     */
    public PageResponse getPageResponse() {
        return pageResponse;
    }

    /**
     * @param pageResponse
     *            the pageResponse to set
     */
    public void setPageResponse(final PageResponse pageResponse) {
        this.pageResponse = pageResponse;
    }

    /**
     * @return the paymentInfo
     */
    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * @param paymentInfo
     *            the paymentInfo to set
     */
    public void setPaymentInfo(final PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * @return the creditCardCharges
     */
    public BigDecimal getCreditCardCharges() {
        return creditCardCharges;
    }

    /**
     * @param creditCardCharges
     *            the creditCardCharges to set
     */
    public void setCreditCardCharges(final BigDecimal creditCardCharges) {
        this.creditCardCharges = creditCardCharges;
    }

    /**
     * @return the cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * @param cardType
     *            the cardType to set
     */
    public void setCardType(final String cardType) {
        this.cardType = cardType;
    }

    /**
     * @return the transactionAmount
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount
     *            the transactionAmount to set
     */
    public void setTransactionAmount(final BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return the currencyAppendedTotalPriceExclusive
     */
    public String getCurrencyAppendedTotalPriceExclusive() {
        return currencyAppendedTotalPriceExclusive;
    }

    /**
     * @param currencyAppendedTotalPriceExclusive
     *            the currencyAppendedTotalPriceExclusive to set
     */
    public void setCurrencyAppendedTotalPriceExclusive(
            final String currencyAppendedTotalPriceExclusive) {
        this.currencyAppendedTotalPriceExclusive = currencyAppendedTotalPriceExclusive;
    }

    /**
     * @return the totalPriceExclusive
     */
    public BigDecimal getTotalPriceExclusive() {
        return totalPriceExclusive;
    }

    /**
     * @param totalPriceExclusive
     *            the totalPriceExclusive to set
     */
    public void setTotalPriceExclusive(final BigDecimal totalPriceExclusive) {
        this.totalPriceExclusive = totalPriceExclusive;
    }

    /**
     * @return the depDate
     */
    public Date getDepDate() {
        if (this.depDate != null) {
            return new Date(depDate.getTime());
        }
        return null;
    }

    /**
     * @param depDate
     *            the depDate to set
     */
    public void setDepDate(final Date depDate) {
        if (depDate != null) {
            this.depDate = new Date(depDate.getTime());
        }
    }

    /**
     * @return the inventoryType
     */
    public String getInventoryType() {
        return inventoryType;
    }

    /**
     * @param inventoryType
     *            the inventoryType to set
     */
    public void setInventoryType(final String inventoryType) {
        this.inventoryType = inventoryType;
    }
}
