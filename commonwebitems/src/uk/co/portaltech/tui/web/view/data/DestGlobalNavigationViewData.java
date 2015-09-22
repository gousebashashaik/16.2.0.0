/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import static uk.co.portaltech.tui.constants.GlobalNavigationConstant.DESTINATIONS;

/**
 * @author EXTLP1
 *
 */
public class DestGlobalNavigationViewData
{
   private boolean active;

   private String activeStyle;

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
   public void setActive(final String activeTab)
   {
      this.active = activeTab.equals(DESTINATIONS);
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
   public void setActiveStyle(String activeStyle)
   {
      this.activeStyle = activeStyle;
   }

}
