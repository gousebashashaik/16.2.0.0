/**
 *
 */
package uk.co.tui.fj.book.view.data;

/**
 * The Class FlightOptionsFilterViewData.
 *
 * @author samantha.gd
 */
public class FlightOptionsFilterViewData
{

   /** The filter code. */
   private String filterCode;

   /** The filter name. */
   private String filterName;

   /** The selected. */
   private boolean selected;

   /**
    * Gets the filter code.
    *
    * @return the filter code
    */
   public String getFilterCode()
   {
      return filterCode;
   }

   /**
    * Sets the filter code.
    *
    * @param filterCode the new filter code
    */
   public void setFilterCode(String filterCode)
   {
      this.filterCode = filterCode;
   }

   /**
    * Gets the filter name.
    *
    * @return the filter name
    */
   public String getFilterName()
   {
      return filterName;
   }

   /**
    * Sets the filter name.
    *
    * @param filterName the new filter name
    */
   public void setFilterName(String filterName)
   {
      this.filterName = filterName;
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
}
