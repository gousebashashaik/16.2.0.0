/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import static uk.co.portaltech.tui.constants.GlobalNavigationConstant.CRUISES;

/**
 * @author EXTLP1
 *
 */
public class CRGlobalNavigationViewData
{
   private CruiseDestinationViewData cruiseDestViewData;

   private TopPortOfCallViewData topPortOfCallViewData;

   private NavigationComponentViewData navComViewData;

   private boolean active;

   private String activeStyle;

   public CruiseDestinationViewData getCruiseDestViewData()
   {
      return cruiseDestViewData;
   }

   public void setCruiseDestViewData(final CruiseDestinationViewData cruiseDestViewData)
   {
      this.cruiseDestViewData = cruiseDestViewData;
   }

   public TopPortOfCallViewData getTopPortOfCallViewData()
   {
      return topPortOfCallViewData;
   }

   public void setTopPortOfCallViewData(final TopPortOfCallViewData topPortOfCallViewData)
   {
      this.topPortOfCallViewData = topPortOfCallViewData;
   }

   public NavigationComponentViewData getNavComViewData()
   {
      return navComViewData;
   }

   public void setNavComViewData(final NavigationComponentViewData navComViewData)
   {
      this.navComViewData = navComViewData;
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
   public void setActive(final String activeTab)
   {
      this.active = activeTab.equals(CRUISES);
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
   public void setActiveStyle(final String activeStyle)
   {
      this.activeStyle = activeStyle;
   }

}
