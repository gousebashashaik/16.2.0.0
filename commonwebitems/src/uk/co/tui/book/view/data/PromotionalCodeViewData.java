/**
 *
 */
package uk.co.tui.book.view.data;

import java.math.BigDecimal;

/**
 * @author naresh.gls
 *
 */
public class PromotionalCodeViewData {

    /**
     * promotionalCode entered.
     */
    private String promotionalCode;

    /**
     * indicates if promotional code is applicable for this package or not.
     */
    private boolean applicable;

    /**
     * Holds the promotionalDiscount applied.
     */
    private BigDecimal promotionalDiscount = BigDecimal.ZERO;

    /**
     * Holds the totalPrice of package.
     */
    private BigDecimal totalPrice = BigDecimal.ZERO;

    private String promotionalCodeFailueMessage;

    /**
     * @return the promotionalCode
     */
    public String getPromotionalCode() {
        return promotionalCode;
    }

    /**
     * @param promotionalCode
     *            the promotionalCode to set
     */
    public void setPromotionalCode(final String promotionalCode) {
        this.promotionalCode = promotionalCode;
    }

    /**
     * @return the applicable
     */
    public boolean isApplicable() {
        return applicable;
    }

    /**
     * @param applicable
     *            the applicable to set
     */
    public void setApplicable(final boolean applicable) {
        this.applicable = applicable;
    }

    /**
     * @return the promotionalDiscount
     */
    public BigDecimal getPromotionalDiscount() {
        return promotionalDiscount;
    }

    /**
     * @param promotionalDiscount
     *            the promotionalDiscount to set
     */
    public void setPromotionalDiscount(final BigDecimal promotionalDiscount) {
        this.promotionalDiscount = promotionalDiscount;
    }

    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice
     *            the totalPrice to set
     */
    public void setTotalPrice(final BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the promotionalCodeFailueMessage
     */
    public String getPromotionalCodeFailueMessage() {
        return promotionalCodeFailueMessage;
    }

    /**
     * @param promotionalCodeFailueMessage
     *            the promotionalCodeFailueMessage to set
     */
    public void setPromotionalCodeFailueMessage(
            final String promotionalCodeFailueMessage) {
        this.promotionalCodeFailueMessage = promotionalCodeFailueMessage;
    }
}
