/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author s.consolino
 * @author l.furrer
 *
 */
public class AttractionViewData extends ViewData implements HasFeatures
{
   private static final int SIZE_OF_SUBSTRING = 120;

   private String code;

   private String name;

   private String editorialContent;

   private List<String> usps;

   private MediaViewData thumbnail;

   private final Map<String, List<Object>> featureCodesAndValues;

   private String type;

   private String latitude;

   private String longitude;

   private List<MediaViewData> galleryImages;

   private String url;

   private List<MediaViewData> heroCarouselMedia;

   private int galleryImageVisibleItems;

   /**
    * @param featureCodesAndValues
    */
   public AttractionViewData()
   {
      this.featureCodesAndValues = new HashMap<String, List<Object>>();
      this.galleryImages = new ArrayList<MediaViewData>();
   }

   /**
    * @return the heroCarouselMedia
    */
   public List<MediaViewData> getHeroCarouselMedia()
   {
      return heroCarouselMedia;
   }

   /**
    * @param heroCarouselMedia the heroCarouselMedia to set
    */
   public void setHeroCarouselMedia(final List<MediaViewData> heroCarouselMedia)
   {
      this.heroCarouselMedia = heroCarouselMedia;
   }

   /**
     *
     */
   public String getSubStringedEditorialContent()
   {
      if (StringUtils.isNotEmpty(this.getEditorialContent())
         && this.getEditorialContent().length() > SIZE_OF_SUBSTRING)
      {
         final String str = this.getEditorialContent().substring(0, SIZE_OF_SUBSTRING);
         return str.substring(0, str.lastIndexOf(' '));
      }
      return this.getEditorialContent();
   }

   /**
    * @return the editorialContent
    */
   public String getEditorialContent()
   {
      return editorialContent;
   }

   /**
    * @param editorialContent the editorialContent to set
    */
   public void setEditorialContent(final String editorialContent)
   {
      this.editorialContent = editorialContent;
   }

   /**
    * @return the usps
    */
   public List<String> getUsps()
   {
      return usps;
   }

   /**
    * @param usps the usps to set
    */
   public void setUsps(final List<String> usps)
   {
      this.usps = usps;
   }

   public MediaViewData getThumbnail()
   {
      return thumbnail;
   }

   public void setThumbnail(final MediaViewData thumbnail)
   {
      this.thumbnail = thumbnail;
   }

   @Override
   public Map<String, List<Object>> getFeatureCodesAndValues()
   {
      return featureCodesAndValues;
   }

   @Override
   public void putFeatureCodesAndValues(final Map<String, List<Object>> featureCodesAndValues)
   {
      this.featureCodesAndValues.putAll(featureCodesAndValues);
   }

   @Override
   public void putFeatureValue(final String featureCode, final List<Object> featureValues)
   {
      featureCodesAndValues.put(featureCode, featureValues);
   }

   public void putFeatureValueNonObjectType(final String featureCode,
      final List<? extends Object> featureValues)
   {
      featureCodesAndValues.put(featureCode, (List<Object>) featureValues);
   }

   @Override
   public List<Object> getFeatureValues(final String featureCode)
   {
      return featureCodesAndValues.get(featureCode);
   }

   public String getFeatureValue(final String featureCode)
   {
      final List<Object> featurelist = featureCodesAndValues.get(featureCode);
      if (featureCodesAndValues.containsKey(featureCode) && CollectionUtils.isNotEmpty(featurelist))
      {
         return featurelist.get(0).toString();
      }
      return StringUtils.EMPTY;
   }

   public String getType()
   {
      return type;
   }

   public void setType(final String type)
   {
      this.type = type;
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
   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * @return the latitude
    */
   public String getLatitude()
   {
      return latitude;
   }

   /**
    * @param latitude the latitude to set
    */
   public void setLatitude(final String latitude)
   {
      this.latitude = latitude;
   }

   /**
    * @return the longitude
    */
   public String getLongitude()
   {
      return longitude;
   }

   /**
    * @param longitude the longitude to set
    */
   public void setLongitude(final String longitude)
   {
      this.longitude = longitude;
   }

   public List<MediaViewData> getGalleryImages()
   {
      return galleryImages;
   }

   public void setGalleryImages(final List<MediaViewData> galleryImages)
   {
      this.galleryImages = galleryImages;
   }

   /**
    * @return the url
    */
   public String getUrl()
   {
      return url;
   }

   /**
    * @param url the url to set
    */
   public void setUrl(final String url)
   {
      this.url = url;
   }

   /**
    * @return the galleryImageVisibleItems
    */
   public int getGalleryImageVisibleItems()
   {
      return galleryImageVisibleItems;
   }

   /**
    * @param galleryImageVisibleItems the galleryImageVisibleItems to set
    */
   public void setGalleryImageVisibleItems(final int galleryImageVisibleItems)
   {
      this.galleryImageVisibleItems = galleryImageVisibleItems;
   }

}
