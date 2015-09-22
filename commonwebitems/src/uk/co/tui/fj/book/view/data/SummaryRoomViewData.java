/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * @author akshay.an
 *
 */
public class SummaryRoomViewData {

    /** The room code. */
    private String roomCode = StringUtils.EMPTY;

    /**
     * Holds the description of the facility, if one sent.
     */
    private String description = StringUtils.EMPTY;

    /**
     * Holds the available number of room.
     */
    private int quantity;

    /**
     * Holds the price of room.
     */
    private BigDecimal price;

    /** The currency appended price. */
    private String currencyAppendedPrice = StringUtils.EMPTY;

    /**
     * Holds whether the room is included.
     */
    private boolean included;

    /**
     * @return the roomCode
     */
    public String getRoomCode() {
        return roomCode;
    }

    /**
     * @param roomCode the roomCode to set
     */
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the currencyAppendedPrice
     */
    public String getCurrencyAppendedPrice() {
        return currencyAppendedPrice;
    }

    /**
     * @param currencyAppendedPrice the currencyAppendedPrice to set
     */
    public void setCurrencyAppendedPrice(String currencyAppendedPrice) {
        this.currencyAppendedPrice = currencyAppendedPrice;
    }

    /**
     * @return the included
     */
    public boolean isIncluded() {
        return included;
    }

    /**
     * @param included the included to set
     */
    public void setIncluded(boolean included) {
        this.included = included;
    }
}
