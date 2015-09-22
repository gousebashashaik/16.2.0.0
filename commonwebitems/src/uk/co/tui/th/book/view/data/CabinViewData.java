/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.List;

/**
 * The Class CabinViewData.
 *
 * @author anithamani.s
 */
public class CabinViewData extends RoomViewData {

    /** The deck details. */
    private List<DeckViewData> decksViewData;

    /**
     * Gets the decks view data.
     *
     * @return the decksViewData
     */
    public List<DeckViewData> getDecksViewData() {
        return decksViewData;
    }

    /**
     * Sets the decks view data.
     *
     * @param decksViewData
     *            the decksViewData to set
     */
    public void setDecksViewData(final List<DeckViewData> decksViewData) {
        this.decksViewData = decksViewData;
    }

}
