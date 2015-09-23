/**
 *
 */
package uk.co.tui.flights.web.view.data;

import uk.co.portaltech.tui.web.view.data.MediaViewData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vijaya.putheti
 *
 */
public class BeforeYouFlyViewData
{

   private String displayName;

   private String introduction;

   private List<MediaViewData> galleryImages = new ArrayList<MediaViewData>();

   private List<MediaViewData> galleryVideos;

   private Map<String, String> featureLists = new HashMap<String, String>();

   private List<String> names;

   private String code;


   /**
    * @return the galleryImages
    */
   public List<MediaViewData> getGalleryImages()
   {
      return galleryImages;
   }

   /**
    * @param galleryImages the galleryImages to set
    */
   public void setGalleryImages(List<MediaViewData> galleryImages)
   {
      this.galleryImages = galleryImages;
   }

   /**
    * @return the galleryVideos
    */
   public List<MediaViewData> getGalleryVideos()
   {
      return galleryVideos;
   }

   /**
    * @param galleryVideos the galleryVideos to set
    */
   public void setGalleryVideos(List<MediaViewData> galleryVideos)
   {
      this.galleryVideos = galleryVideos;
   }

   /**
    * @return the displayName
    */
   public String getDisplayName()
   {
      return displayName;
   }

   /**
    * @param displayName
    *           the displayName to set
    */
   public void setDisplayName(final String displayName)
   {
      this.displayName = displayName;
   }

   /**
    * @return the featureLists
    */
   public Map<String, String> getFeatureLists()
   {
      return featureLists;
   }

   /**
    * @param featureLists the featureLists to set
    */
   public void setFeatureLists(Map<String, String> featureLists)
   {
      this.featureLists = featureLists;
   }

   /**
    * @return the names
    */
   public List<String> getNames()
   {
      return names;
   }

   /**
    * @param names the names to set
    */
   public void setNames(List<String> names)
   {
      this.names = names;
   }

   /**
    * @return the code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * @param code the code to set
    */
   public void setCode(String code)
   {
      this.code = code;
   }

   /**
    * @return the introduction
    */
   public String getIntroduction()
   {
      return introduction;
   }

   /**
    * @param introduction the introduction to set
    */
   public void setIntroduction(String introduction)
   {
      this.introduction = introduction;
   }



}
