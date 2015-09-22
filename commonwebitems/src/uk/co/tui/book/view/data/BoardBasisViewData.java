/**
 *
 */
package uk.co.tui.book.view.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class BoardBasisViewData.
 *
 * @author samantha.gd
 */
public class BoardBasisViewData implements Serializable {
    /** The board basis code. */
    private String boardBasisCode;

    /** The board basis name. */
    private String boardBasisName;

    /** The difference price. */
    private BigDecimal differencePrice;

    /** The per person price. */
    private BigDecimal perPersonPrice;

    /** The currency appended difference price. */
    private String currencyAppendedDifferencePrice;

    /** The currency appended per person price. */
    private String currencyAppendedPerPersonPrice;

    /** The board basis tool tip. */
    private String boardBasisToolTip;

    /** The selected. */
    private boolean selected;

    /** The default board basis. */
    private boolean defaultBoardBasis;

    /** The RoomOptionsContentViewData. */
    private RoomOptionsContentViewData roomOptionsContentViewData;

    /** The featureCodesAndValues. */
    private Map<String, List<Object>> featureCodesAndValues = new HashMap<String, List<Object>>();

    /**
     * @return the featureCodesAndValues
     */
    public Map<String, List<Object>> getFeatureCodesAndValues() {
        return featureCodesAndValues;
    }

    /**
     * @param featureCodesAndValues
     *            the featureCodesAndValues to set
     */
    public void setFeatureCodesAndValues(
            final Map<String, List<Object>> featureCodesAndValues) {
        this.featureCodesAndValues = featureCodesAndValues;
    }

    /**
     * Gets the board basis code.
     *
     * @return the board basis code
     */
    public String getBoardBasisCode() {
        return boardBasisCode;
    }

    /**
     * Sets the board basis code.
     *
     * @param boardBasisCode
     *            the new board basis code
     */
    public void setBoardBasisCode(final String boardBasisCode) {
        this.boardBasisCode = boardBasisCode;
    }

    /**
     * Gets the board basis name.
     *
     * @return the board basis name
     */
    public String getBoardBasisName() {
        return boardBasisName;
    }

    /**
     * Sets the board basis name.
     *
     * @param boardBasisName
     *            the new board basis name
     */
    public void setBoardBasisName(final String boardBasisName) {
        this.boardBasisName = boardBasisName;
    }

    /**
     * Gets the difference price.
     *
     * @return the difference price
     */
    public BigDecimal getDifferencePrice() {
        return differencePrice;
    }

    /**
     * Sets the difference price.
     *
     * @param differencePrice
     *            the new difference price
     */
    public void setDifferencePrice(final BigDecimal differencePrice) {
        this.differencePrice = differencePrice;
    }

    /**
     * Gets the per person price.
     *
     * @return the per person price
     */
    public BigDecimal getPerPersonPrice() {
        return perPersonPrice;
    }

    /**
     * Sets the per person price.
     *
     * @param perPersonPrice
     *            the new per person price
     */
    public void setPerPersonPrice(final BigDecimal perPersonPrice) {
        this.perPersonPrice = perPersonPrice;
    }

    /**
     * Gets the currency appended difference price.
     *
     * @return the currency appended difference price
     */
    public String getCurrencyAppendedDifferencePrice() {
        return currencyAppendedDifferencePrice;
    }

    /**
     * Sets the currency appended difference price.
     *
     * @param currencyAppendedDifferencePrice
     *            the new currency appended difference price
     */
    public void setCurrencyAppendedDifferencePrice(
            final String currencyAppendedDifferencePrice) {
        this.currencyAppendedDifferencePrice = currencyAppendedDifferencePrice;
    }

    /**
     * Gets the currency appended per person price.
     *
     * @return the currency appended per person price
     */
    public String getCurrencyAppendedPerPersonPrice() {
        return currencyAppendedPerPersonPrice;
    }

    /**
     * Sets the currency appended per person price.
     *
     * @param currencyAppendedPerPersonPrice
     *            the new currency appended per person price
     */
    public void setCurrencyAppendedPerPersonPrice(
            final String currencyAppendedPerPersonPrice) {
        this.currencyAppendedPerPersonPrice = currencyAppendedPerPersonPrice;
    }

    /**
     * Checks if is selected.
     *
     * @return true, if is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected.
     *
     * @param selected
     *            the new selected
     */
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    /**
     * Checks if is default board basis.
     *
     * @return true, if is default board basis
     */
    public boolean isDefaultBoardBasis() {
        return defaultBoardBasis;
    }

    /**
     * Sets the default board basis.
     *
     * @param defaultBoardBasis
     *            the new default board basis
     */
    public void setDefaultBoardBasis(final boolean defaultBoardBasis) {
        this.defaultBoardBasis = defaultBoardBasis;
    }

    public RoomOptionsContentViewData getRoomOptionsContentViewData() {
        return roomOptionsContentViewData;
    }

    public void setRoomOptionsContentViewData(
            final RoomOptionsContentViewData roomOptionsContentViewData) {
        this.roomOptionsContentViewData = roomOptionsContentViewData;
    }

    /**
     * Gets the board basis tool tip.
     *
     * @return the boardBasisToolTip
     */
    public String getBoardBasisToolTip() {
        return boardBasisToolTip;
    }

    /**
     * Sets the board basis tool tip.
     *
     * @param boardBasisToolTip
     *            the boardBasisToolTip to set
     */
    public void setBoardBasisToolTip(final String boardBasisToolTip) {
        this.boardBasisToolTip = boardBasisToolTip;
    }

}
