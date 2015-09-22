/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Collection;

import uk.co.tui.web.common.enums.NavigationViewMode;

/**
 * @author
 *
 */
public class NavigationComponentViewData
{

   private Collection<NavigationBarComponentViewData> navigationBarComponents;

   private NavigationViewMode navigationViewMode;

   private String componentStyle;

   /**
    * @return the navigationBarComponents
    */
   public Collection<NavigationBarComponentViewData> getNavigationBarComponents()
   {
      return navigationBarComponents;
   }

   /**
    * @param navigationBarComponents the navigationBarComponents to set
    */
   public void setNavigationBarComponents(
      final Collection<NavigationBarComponentViewData> navigationBarComponents)
   {
      this.navigationBarComponents = navigationBarComponents;
   }

   /**
    * @return the navigationViewMode
    */
   public NavigationViewMode getNavigationViewMode()
   {
      return navigationViewMode;
   }

   /**
    * @param navigationViewMode the navigationViewMode to set
    */
   public void setNavigationViewMode(final NavigationViewMode navigationViewMode)
   {
      this.navigationViewMode = navigationViewMode;
   }

   /**
    * @return the componentStyle
    */
   public String getComponentStyle()
   {
      return componentStyle;
   }

   /**
    * @param componentStyle the componentStyle to set
    */
   public void setComponentStyle(final String componentStyle)
   {
      this.componentStyle = componentStyle;
   }
}
