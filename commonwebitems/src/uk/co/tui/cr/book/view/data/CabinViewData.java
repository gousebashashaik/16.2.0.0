/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;


/**
 * @author ramkishore.p
 *
 */
public class CabinViewData extends RoomViewData
{

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   /** The isPremierServiceIncluded. */
   private boolean isPremierServiceIncluded;

   /** The deckViewData. */
   private List<DeckViewData> deckViewData;


   /** The deck title. */
   private String deckTitle;

   /**
    * Gets the PremierService.
    *
    * @return the PremierService
    */
   public boolean isPremierServiceIncluded()
   {
      return isPremierServiceIncluded;
   }

   /**
    * Sets the isPremierServiceIncluded.
    *
    * @param isPremierServiceIncluded
    *           the isPremierServiceIncluded to set
    */
   public void setPremierServiceIncluded(final boolean isPremierServiceIncluded)
   {
      this.isPremierServiceIncluded = isPremierServiceIncluded;
   }

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


   /**
    * Gets the deck title.
    *
    * @return the deck title
    */
   public String getDeckTitle()
   {
      return deckTitle;
   }

   /**
    * Sets the deck title.
    *
    * @param deckTitle the new deck title
    */
   public void setDeckTitle(final String deckTitle)
   {
      this.deckTitle = deckTitle;
   }

   /**
    * Clone.
    *
    * @return the object
    * @throws CloneNotSupportedException
    *            the clone not supported exception
    */
   @Override
   public Object clone() throws CloneNotSupportedException
   {
      return super.clone();

   }

}
