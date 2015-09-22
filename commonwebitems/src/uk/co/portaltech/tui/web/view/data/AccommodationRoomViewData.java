/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gagan
 *
 */
public class AccommodationRoomViewData implements HasFeatures
{

   private String code;

   private String roomType;

   private String roomTitle;

   private Map<String, Integer> occupancy;

   private String[] usps = new String[] {};

   private String description;

   private String shortDescription;

   private String[] upgrade;

   private String accommodationRoomsUrl;

   private final Map<String, List<Object>> featureCodesAndValues;

   private List<String> sharedUsps;

   private List<String> varyingUsps;

   private String roomPlanImage;

   // Added property for hybris ContentServices
   private String roomtypeCode;

   private Collection<MediaViewData> galleryImages;

   public AccommodationRoomViewData()
   {
      this.featureCodesAndValues = new HashMap<String, List<Object>>();
      this.sharedUsps = new ArrayList<String>();
      this.varyingUsps = new ArrayList<String>();
   }

   public AccommodationRoomViewData(final String code, final String roomType,
                                    final String roomTitle, final Map<String, Integer> occupancy,
                                    final String[] usps, final String description,
                                    final String shortDescription, final String[] upgrade,
                                    final List<MediaViewData> mediaData)
   {
      final String[] usp = usps.clone();
      final String[] upgrades = upgrade.clone();
      this.code = code;
      this.roomType = roomType;
      this.setRoomTitle(roomTitle);
      this.occupancy = occupancy;
      this.usps = usp;
      this.description = description;
      this.shortDescription = shortDescription;
      this.upgrade = upgrades;
      this.galleryImages = mediaData;
      this.featureCodesAndValues = new HashMap<String, List<Object>>();

   }

   /**
    * @return the roomPlanImage
    */
   public String getRoomPlanImage()
   {
      return roomPlanImage;
   }

   /**
    * @param roomPlanImage the roomPlanImage to set
    */
   public void setRoomPlanImage(final String roomPlanImage)
   {
      this.roomPlanImage = roomPlanImage;
   }

   /**
    * @return the accommodationRoomsUrl
    */
   public String getAccommodationRoomsUrl()
   {
      return accommodationRoomsUrl;
   }

   /**
    * @param accommodationRoomsUrl the accommodationRoomsUrl to set
    */
   public void setAccommodationRoomsUrl(final String accommodationRoomsUrl)
   {
      this.accommodationRoomsUrl = accommodationRoomsUrl;
   }

   public List<String> getSharedUsps()
   {
      return sharedUsps;
   }

   public void setSharedUsps(final List<String> sharedUsps)
   {
      this.sharedUsps = sharedUsps;
   }

   public List<String> getVaryingUsps()
   {
      return varyingUsps;
   }

   public void setVaryingUsps(final List<String> varyingUsps)
   {
      this.varyingUsps = varyingUsps;
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
    * @return the roomType
    */
   public String getRoomType()
   {
      return roomType;
   }

   /**
    * @param roomType the roomType to set
    */
   public void setRoomType(final String roomType)
   {
      this.roomType = roomType;
   }

   /**
    * @return the occupancy
    */
   public Map<String, Integer> getOccupancy()
   {
      return occupancy;
   }

   public Integer getOccupancyValue(final String key)
   {

      if (occupancy != null && occupancy.get(key) != null)
      {
         return occupancy.get(key);
      }
      return Integer.valueOf(0);
   }

   /**
    * @param occupancy the occupancy to set
    */
   public void setOccupancy(final Map<String, Integer> occupancy)
   {
      this.occupancy = occupancy;
   }

   public void setOccupancyValue(final String key, final Integer value)
   {
      occupancy.put(key, value);
   }

   /**
    * @return the usps
    */
   public String[] getUsps()
   {
      if (usps == null)
      {
         return new String[0];
      }
      return Arrays.copyOf(usps, this.usps.length);
   }

   /**
    * @param usps the usps to set
    */
   public void setUsps(final String[] usps)
   {
      final String[] usp = usps.clone();
      this.usps = usp;
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

   /**
    * @return the shortDescription
    */
   public String getShortDescription()
   {
      return shortDescription;
   }

   /**
    * @param shortDescription the shortDescription to set
    */
   public void setShortDescription(final String shortDescription)
   {
      this.shortDescription = shortDescription;
   }

   /**
    * @return the upgrade
    */
   public String[] getUpgrade()
   {
      if (upgrade == null)
      {
         return new String[0];
      }
      return Arrays.copyOf(upgrade, upgrade.length);
   }

   /**
    * @param upgrade the upgrade to set
    */
   public void setUpgrade(final String[] upgrade)
   {
      final String[] upgrades = upgrade.clone();
      this.upgrade = upgrades;
   }

   /**
    * @return the roomTitle
    */
   public String getRoomTitle()
   {
      return roomTitle;
   }

   /**
    * @param roomTitle the roomTitle to set
    */
   public final void setRoomTitle(final String roomTitle)
   {
      this.roomTitle = roomTitle;
   }

   /**
    * @return the galleryImages
    */
   public Collection<MediaViewData> getGalleryImages()
   {
      return galleryImages;
   }

   /**
    * @param galleryImages the galleryImages to set
    */
   public void setGalleryImages(final Collection<MediaViewData> galleryImages)
   {
      this.galleryImages = galleryImages;
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

   /**
    * @return the roomtypeCode
    */
   public String getRoomtypeCode()
   {
      return roomtypeCode;
   }

   /**
    * @param roomtypeCode the roomtypeCode to set
    */
   public void setRoomtypeCode(final String roomtypeCode)
   {
      this.roomtypeCode = roomtypeCode;
   }

}
