/**
 *
 */
package uk.co.tui.th.book.view.data;

/**
 * The Class DurationListViewData.
 *
 * @author samantha.gd
 */
public class DurationListViewData
{

   /** The duration. */
   private String duration;

   /** The selected. */
   private boolean selected;

   /** The display order. */
   private int displayOrder;

   /** The should display. */
   private boolean shouldDisplay;

   /**
    * Gets the duration.
    *
    * @return the duration
    */
   public String getDuration()
   {
      return duration;
   }

   /**
    * Sets the duration.
    *
    * @param duration the new duration
    */
   public void setDuration(String duration)
   {
      this.duration = duration;
   }

   /**
    * Checks if is selected.
    *
    * @return true, if is selected
    */
   public boolean isSelected()
   {
      return selected;
   }

   /**
    * Sets the selected.
    *
    * @param selected the new selected
    */
   public void setSelected(boolean selected)
   {
      this.selected = selected;
   }

   /**
    * Gets the display order.
    *
    * @return the display order
    */
   public int getDisplayOrder()
   {
      return displayOrder;
   }

   /**
    * Sets the display order.
    *
    * @param displayOrder the new display order
    */
   public void setDisplayOrder(int displayOrder)
   {
      this.displayOrder = displayOrder;
   }

   /**
    * Checks if is should display.
    *
    * @return true, if is should display
    */
   public boolean isShouldDisplay()
   {
      return shouldDisplay;
   }

   /**
    * Sets the should display.
    *
    * @param shouldDisplay the new should display
    */
   public void setShouldDisplay(boolean shouldDisplay)
   {
      this.shouldDisplay = shouldDisplay;
   }
}
