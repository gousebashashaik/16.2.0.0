/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * The Class DeckViewData.
 *
 * @author anithamani.s
 */
class DeckViewData implements Serializable {

    /** The deck number. */
    private String deckNumber;

    /** is deck selected. */
    private boolean isDeckSelected;

    /** The per person price. */
    private BigDecimal perPersonPrice;

    /** The currency appended per person price. */
    private String currencyAppendedPerPersonPrice;

    /** The difference price. */
    private static final String DIFFERENCEPRICE = StringUtils.EMPTY;

    /**
     * Gets the deck number.
     *
     * @return the deckNumber
     */
    public String getDeckNumber() {
        return deckNumber;
    }

    /**
     * Sets the deck number.
     *
     * @param deckNumber
     *            the deckNumber to set
     */
    public void setDeckNumber(final String deckNumber) {
        this.deckNumber = deckNumber;
    }

    /**
     * Checks if deck is selected.
     *
     * @return the isDeckSelected
     */
    public boolean isDeckSelected() {
        return isDeckSelected;
    }

    /**
     * Sets the deck selected.
     *
     * @param isDeckSelected
     *            the isDeckSelected to set
     */
    public void setDeckSelected(final boolean isDeckSelected) {
        this.isDeckSelected = isDeckSelected;
    }

    /**
     * Gets the per person price.
     *
     * @return the perPersonPrice
     */
    public BigDecimal getPerPersonPrice() {
        return perPersonPrice;
    }

    /**
     * Sets the per person price.
     *
     * @param perPersonPrice
     *            the perPersonPrice to set
     */
    public void setPerPersonPrice(final BigDecimal perPersonPrice) {
        this.perPersonPrice = perPersonPrice;
    }

    /**
     * Gets the currency appended per person price.
     *
     * @return the currencyAppendedPerPersonPrice
     */
    public String getCurrencyAppendedPerPersonPrice() {
        return currencyAppendedPerPersonPrice;
    }

    /**
     * Sets the currency appended per person price.
     *
     * @param currencyAppendedPerPersonPrice
     *            the currencyAppendedPerPersonPrice to set
     */
    public void setCurrencyAppendedPerPersonPrice(
            final String currencyAppendedPerPersonPrice) {
        this.currencyAppendedPerPersonPrice = currencyAppendedPerPersonPrice;
    }

    /**
     * Gets the difference price.
     *
     * @return the differencePrice
     */
    public String getDifferencePrice() {
        return DIFFERENCEPRICE;
    }

}
