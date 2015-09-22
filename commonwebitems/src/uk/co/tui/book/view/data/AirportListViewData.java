/**
 *
 */
package uk.co.tui.book.view.data;

/**
 * The Class AirportListViewData.
 *
 * @author samantha.gd
 */
public class AirportListViewData
{

   /** The airport code. */
   private String airportCode;

   /** The airport name. */
   private String airportName;

   /** The selected. */
   private boolean selected;

   /** The display order. */
   private int displayOrder;

   /** The should display. */
   private boolean shouldDisplay;

   /**
    * Gets the airport code.
    *
    * @return the airport code
    */
   public String getAirportCode()
   {
      return airportCode;
   }

   /**
    * Sets the airport code.
    *
    * @param airportCode the new airport code
    */
   public void setAirportCode(String airportCode)
   {
      this.airportCode = airportCode;
   }

   /**
    * Gets the airport name.
    *
    * @return the airport name
    */
   public String getAirportName()
   {
      return airportName;
   }

   /**
    * Sets the airport name.
    *
    * @param airportName the new airport name
    */
   public void setAirportName(String airportName)
   {
      this.airportName = airportName;
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
