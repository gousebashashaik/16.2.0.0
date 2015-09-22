/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author vinod kumar.g
 *
 */
public class TabSectionViewData
{

   private String tabTitle;

   private String tabIntro;

   private List<ImageSectionViewData> images;

   /**
    * @return the tabTitle
    */
   public String getTabTitle()
   {
      return tabTitle;
   }

   /**
    * @param tabTitle the tabTitle to set
    */
   public void setTabTitle(final String tabTitle)
   {
      this.tabTitle = tabTitle;
   }

   /**
    * @return the tabIntro
    */
   public String getTabIntro()
   {
      return tabIntro;
   }

   /**
    * @param tabIntro the tabIntro to set
    */
   public void setTabIntro(final String tabIntro)
   {
      this.tabIntro = tabIntro;
   }

   /**
    * @return the images
    */
   public List<ImageSectionViewData> getImages()
   {
      return images;
   }

   /**
    * @param images the images to set
    */
   public void setImages(final List<ImageSectionViewData> images)
   {
      this.images = images;
   }

}