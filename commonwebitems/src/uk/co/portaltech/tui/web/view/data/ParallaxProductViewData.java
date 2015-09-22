/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author venkatasuresh.t
 *
 */
public class ParallaxProductViewData
{
   String title;

   String description;

   List<ParallaxProductCollectionData> imageData = new ArrayList<ParallaxProductCollectionData>();

   int noofimages;

   /**
    * @return the imageData
    */
   public List<ParallaxProductCollectionData> getImageData()
   {
      return imageData;
   }

   /**
    * @param imageData the imageData to set
    */
   public void setImageData(final List<ParallaxProductCollectionData> imageData)
   {
      this.imageData = imageData;
   }

   /**
    * @return the noofimages
    */
   public int getNoofimages()
   {
      return noofimages;
   }

   /**
    * @param noofimages the noofimages to set
    */
   public void setNoofimages(final int noofimages)
   {
      this.noofimages = noofimages;
   }

   /**
    * @return the title
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * @param title the title to set
    */
   public void setTitle(final String title)
   {
      this.title = title;
   }

   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(final String description)
   {
      this.description = description;
   }

}
