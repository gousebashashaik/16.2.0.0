/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import static uk.co.portaltech.tui.constants.GlobalNavigationConstant.HOLIDAYS;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EXTLP1
 *
 */
public class TuiGlobalNavigationViewData
{
   // This is used by both TH & FC
   private final Map<String, Object> tuiHolidayNavigationViewData = new HashMap<String, Object>();

   private boolean active;

   private String activeStyle;

   /**
    * @return the thHolidayNavigationViewData
    */
   public Map<String, Object> getTuiHolidayNavigationViewData()
   {
      return tuiHolidayNavigationViewData;
   }

   /**
    * @return the active
    */
   public boolean isActive()
   {
      return active;
   }

   /**
    * @param active the active to set
    */
   public void setActive( String activeTab)
   {
      this.active = activeTab.equals(HOLIDAYS);
   }

   /**
    * @return the activeStyle
    */
   public String getActiveStyle()
   {
      return activeStyle;
   }

   /**
    * @param activeStyle the activeStyle to set
    */
   public void setActiveStyle( String activeStyle)
   {
      this.activeStyle = activeStyle;
   }

}
