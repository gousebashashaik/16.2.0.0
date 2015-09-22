/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.Collection;

import uk.co.tui.book.domain.lite.Price;


/**
 * @author sunil.bd
 *
 */
public class SelectedRoomViewData
{

    private String roomCode;

    private Price price;

    private Boolean freeKids;

    private Boolean defaultRoom = Boolean.FALSE;

    private Integer quantity;

    private Collection<PassengerViewData> passgengers;

    /**
     * @return the roomCode
     */
    public String getRoomCode()
    {
        return roomCode;
    }

    /**
     * @param roomCode
     *           the roomCode to set
     */
    public void setRoomCode(final String roomCode)
    {
        this.roomCode = roomCode;
    }

    /**
     * @return the price
     */
    public Price getPrice()
    {
        return price;
    }

    /**
     * @param price
     *           the price to set
     */
    public void setPrice(final Price price)
    {
        this.price = price;
    }

    /**
     * @return the freeKids
     */
    public Boolean getFreeKids()
    {
        return freeKids;
    }

    /**
     * @param freeKids
     *           the freeKids to set
     */
    public void setFreeKids(final Boolean freeKids)
    {
        this.freeKids = freeKids;
    }

    /**
     * @return the defaultRoom
     */
    public Boolean getDefaultRoom()
    {
        return defaultRoom;
    }

    /**
     * @param defaultRoom
     *           the defaultRoom to set
     */
    public void setDefaultRoom(final Boolean defaultRoom)
    {
        this.defaultRoom = defaultRoom;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity()
    {
        return quantity;
    }

    /**
     * @param quantity
     *           the quantity to set
     */
    public void setQuantity(final Integer quantity)
    {
        this.quantity = quantity;
    }

    /**
     * @return the passgengers
     */
    public Collection<PassengerViewData> getPassgengers()
    {
        return passgengers;
    }

    /**
     * @param passgengers
     *           the passgengers to set
     */
    public void setPassgengers(final Collection<PassengerViewData> passgengers)
    {
        this.passgengers = passgengers;
    }



}
