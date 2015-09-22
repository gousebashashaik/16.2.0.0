/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author niranjani.r
 *
 */
public class ThingsToSeeAndDoEditorialData
{
   /**
    * Tab Name.
    */
   private String tabName;

   /**
    * Set of Tab Data.
    *
    */
   private List<TabCarouselData> tabData;

   /**
    * @return the tabName
    */
   public String getTabName()
   {
      return tabName;
   }

   /**
    * @param tabName the tabName to set
    */
   public void setTabName(final String tabName)
   {
      this.tabName = tabName;
   }

   /**
    * @return the tabData
    */
   public List<TabCarouselData> getTabData()
   {
      return tabData;
   }

   /**
    * @param tabData the tabData to set
    */
   public void setTabData(final List<TabCarouselData> tabData)
   {
      this.tabData = tabData;
   }

}
