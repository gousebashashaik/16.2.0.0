/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;


/**
 * @author ramkishore.p
 *
 */
public class SummaryCabinViewData extends SummaryRoomViewData
{
    /** The deckViewData. */
    private List<DeckViewData> deckViewData;

    /**
     * Gets the DeckViewData.
     *
     * @return the DeckViewData
     */
    public List<DeckViewData> getDeckViewData()
    {
        return deckViewData;
    }

    /**
     * Sets the deckViewData.
     *
     * @param deckViewData
     *           the deckViewData to set
     */
    public void setDeckViewData(final List<DeckViewData> deckViewData)
    {
        this.deckViewData = deckViewData;
    }
}
