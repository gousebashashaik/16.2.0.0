/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.io.Serializable;

/**
 * @author ramkishore.p
 *
 */
public class DeckViewData implements Serializable {

    /** The deck no. */
    private String deckNo;

    /**
     * Gets the Deck no.
     *
     * @return the deckNo
     */
    public String getDeckNo() {
        return deckNo;
    }

    /**
     * Sets the DeckNo.
     *
     * @param deckNo
     *            the deckNo to set
     */
    public void setDeckNo(final String deckNo) {
        this.deckNo = deckNo;
    }

}
