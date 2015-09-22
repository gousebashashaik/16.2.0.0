/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.co.portaltech.travel.enums.LocationType;

/**
 * @author abi
 *
 */
public class LocationData extends CategoryData implements HasFeatures, Serializable, Cloneable
{

   private final Map<String, List<Object>> featureCodesAndValues;

   private Collection<ImageData> images;

   private List<AccommodationCarouselViewData> carouselViewData;

   private List<ArticleViewData> articles;

   private LocationType locationType;

   private List<MediaViewData> heroCarouselMedia;

   private MediaViewData picture;

   private List<LocationData> subLocations;

   private MediaViewData thumbnail;

   private List<AccommodationViewData> accommodations;

   private List<ResortCarouselViewData> resortCarouselData;

   private String displayOrder;

   private String count;

   private String priceFrom;

   private Collection<MediaViewData> galleryImages;

   private Collection<MediaViewData> galleryVideos;

   private List<String> superCategoryNames;

   private List<ProductRangesData> productRanges;

   private String thingstodoMapUrl;

   private int galleryImageVisibleItems;

   private String legacySystemUrl;

   private Map<String, String> locationLink = new HashMap<String, String>();

   // wf_com_50
   private List<String> locationAccomTypes;

   private List<AccommodationCarouselViewData> hotelCarouselViewData;

   private List<AccommodationCarouselViewData> villaCarouselViewData;

   private boolean multiSelect;

   private String mobileLocationType;

   private String brandType;

   private String brand;

   private boolean isPoc;

   private List<ThingsToSeeAndDoEditorialData> thingsToSeeAndDoData;

   private WeatherViewData WeatherViewData;

   public LocationData()
   {
      super();
      this.featureCodesAndValues = new HashMap<String, List<Object>>();
      this.galleryImages = new ArrayList<MediaViewData>();
      this.subLocations = new ArrayList<LocationData>();
   }

   /**
    * @return the thingsToSeeAndDoData
    */
   public List<ThingsToSeeAndDoEditorialData> getThingsToSeeAndDoData()
   {
      return thingsToSeeAndDoData;
   }

   /**
    * @param thingsToSeeAndDoData the thingsToSeeAndDoData to set
    */
   public void setThingsToSeeAndDoData(
      final List<ThingsToSeeAndDoEditorialData> thingsToSeeAndDoData)
   {
      this.thingsToSeeAndDoData = thingsToSeeAndDoData;
   }

   /**
    * @return the brand
    */
   public String getBrand()
   {
      return brand;
   }

   /**
    * @param brand the brand to set
    */
   public void setBrand(final String brand)
   {
      this.brand = brand;
   }

   /**
    * @return the brandType
    */
   public String getBrandType()
   {
      return brandType;
   }

   /**
    * @param brandType the brandType to set
    */
   public void setBrandType(final String brandType)
   {
      this.brandType = brandType;
   }

   /**
    * @return the locationAccomTypes
    */
   public List<String> getLocationAccomTypes()
   {
      return locationAccomTypes;
   }

   /**
    * @param locationAccomTypes the locationAccomTypes to set
    */
   public void setLocationAccomTypes(final List<String> locationAccomTypes)
   {
      this.locationAccomTypes = locationAccomTypes;
   }

   /**
    * @return the productRanges
    */
   public List<ProductRangesData> getProductRanges()
   {
      return productRanges;
   }

   /**
    * @param productRanges the productRanges to set
    */
   public void setProductRanges(final List<ProductRangesData> productRanges)
   {
      this.productRanges = productRanges;
   }

   /**
    * @return the subCategoryNames
    */

   /**
    * @return the locationLink
    */
   public Map<String, String> getLocationLink()
   {
      return locationLink;
   }

   /**
    * @param locationLink the locationLink to set
    */
   public void setLocationLink(final Map<String, String> locationLink)
   {
      this.locationLink = locationLink;
   }

   /**
    * @return the thingstodoMapUrl
    */
   public String getThingstodoMapUrl()
   {
      return thingstodoMapUrl;
   }

   /**
    * @param thingstodoMapUrl the thingstodoMapUrl to set
    */
   public void setThingstodoMapUrl(final String thingstodoMapUrl)
   {
      this.thingstodoMapUrl = thingstodoMapUrl;
   }

   public List<String> getSuperCategoryNames()
   {
      return superCategoryNames;
   }

   public void setSuperCategoryNames(final List<String> superCategoryNames)
   {
      this.superCategoryNames = superCategoryNames;
   }

   /**
    * @return the locationType
    */
   public LocationType getLocationType()
   {
      return locationType;
   }

   /**
    * @param locationType the locationType to set
    */
   public void setLocationType(final LocationType locationType)
   {
      this.locationType = locationType;
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

   @Override
   public List<Object> getFeatureValues(final String featureCode)
   {
      return featureCodesAndValues.get(featureCode);
   }

   /**
    * @return the images
    */
   public Collection<ImageData> getImages()
   {
      return images;
   }

   /**
    * @param images the images to set
    */
   public void setImages(final Collection<ImageData> images)
   {
      this.images = images;
   }

   public List<AccommodationCarouselViewData> getCarouselViewData()
   {
      return carouselViewData;
   }

   public void setCarouselViewData(final List<AccommodationCarouselViewData> carouselViewData)
   {
      this.carouselViewData = carouselViewData;
   }

   /**
    * @return the articles
    */
   public List<ArticleViewData> getArticles()
   {
      return articles;
   }

   /**
    * @param articles the articles to set
    */
   public void setArticles(final List<ArticleViewData> articles)
   {
      this.articles = articles;
   }

   public List<MediaViewData> getHeroCarouselMedia()
   {
      return heroCarouselMedia;
   }

   public void setHeroCarouselMedia(final List<MediaViewData> heroCarouselMedia)
   {
      this.heroCarouselMedia = heroCarouselMedia;
   }

   public MediaViewData getPicture()
   {
      return picture;
   }

   public void setPicture(final MediaViewData picture)
   {
      this.picture = picture;
   }

   public List<LocationData> getSubLocations()
   {
      return subLocations;
   }

   public void setSubLocations(final List<LocationData> subCategories)
   {
      this.subLocations = subCategories;
   }

   public MediaViewData getThumbnail()
   {
      return thumbnail;
   }

   public void setThumbnail(final MediaViewData thumbnail)
   {
      this.thumbnail = thumbnail;
   }

   public List<AccommodationViewData> getAccommodations()
   {
      return accommodations;
   }

   public void setAccommodations(final List<AccommodationViewData> accommodations)
   {
      this.accommodations = accommodations;
   }

   /**
    * @return the resortCarouselData
    */
   public List<ResortCarouselViewData> getResortCarouselData()
   {
      return resortCarouselData;
   }

   /**
    * @param resortCarouselData the resortCarouselData to set
    */
   public void setResortCarouselData(final List<ResortCarouselViewData> resortCarouselData)
   {
      this.resortCarouselData = resortCarouselData;
   }

   /**
    * @return the displayOrder
    */
   public String getDisplayOrder()
   {
      return displayOrder;
   }

   /**
    * @param displayOrder the displayOrder to set
    */
   public void setDisplayOrder(final String displayOrder)
   {
      this.displayOrder = displayOrder;
   }

   /**
    * @return the count
    */
   public String getCount()
   {
      return count;
   }

   /**
    * @param count the count to set
    */
   public void setCount(final String count)
   {
      this.count = count;
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

   public Collection<MediaViewData> getGalleryVideos()
   {
      return galleryVideos;
   }

   public void setGalleryVideos(final Collection<MediaViewData> galleryVideos)
   {
      this.galleryVideos = galleryVideos;
   }

   /**
    * @return the priceFrom
    */
   public String getPriceFrom()
   {
      return priceFrom;
   }

   /**
    * @param priceFrom the priceFrom to set
    */
   public void setPriceFrom(final String priceFrom)
   {
      this.priceFrom = priceFrom;
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

   /**
    * @return the legacySystemUrl
    */
   public String getLegacySystemUrl()
   {
      return legacySystemUrl;
   }

   /**
    * @param legacySystemUrl the legacySystemUrl to set
    */
   public void setLegacySystemUrl(final String legacySystemUrl)
   {
      this.legacySystemUrl = legacySystemUrl;
   }

   /**
    * @return the hotelCarouselViewData
    */
   public List<AccommodationCarouselViewData> getHotelCarouselViewData()
   {
      return hotelCarouselViewData;
   }

   /**
    * @param hotelCarouselViewData the hotelCarouselViewData to set
    */
   public void setHotelCarouselViewData(
      final List<AccommodationCarouselViewData> hotelCarouselViewData)
   {
      this.hotelCarouselViewData = hotelCarouselViewData;
   }

   /**
    * @return the villaCarouselViewData
    */
   public List<AccommodationCarouselViewData> getVillaCarouselViewData()
   {
      return villaCarouselViewData;
   }

   /**
    * @param villaCarouselViewData the villaCarouselViewData to set
    */
   public void setVillaCarouselViewData(
      final List<AccommodationCarouselViewData> villaCarouselViewData)
   {
      this.villaCarouselViewData = villaCarouselViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {

      if (obj == null)
      {
         return false;
      }
      if (!(obj instanceof LocationData))
      {
         return false;
      }

      return new EqualsBuilder().append(this.getCode(), ((LocationData) obj).getCode()).isEquals();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {

      return new HashCodeBuilder().append(this.getCode()).toHashCode();
   }

   @Override
   public Object clone() throws CloneNotSupportedException
   {
      return super.clone();

   }

   /**
    * @return the multiSelect
    */
   public boolean isMultiSelect()
   {
      return multiSelect;
   }

   /**
    * @param multiSelect the multiSelect to set
    */
   public void setMultiSelect(final boolean multiSelect)
   {
      this.multiSelect = multiSelect;
   }

   /**
    * @return the mobileLocationType
    */
   public String getMobileLocationType()
   {
      return mobileLocationType;
   }

   /**
    * @param mobileLocationType the mobileLocationType to set
    */
   public void setMobileLocationType(final String mobileLocationType)
   {
      this.mobileLocationType = mobileLocationType;
   }

   /**
    * @return the isPoc
    */
   public boolean isPoc()
   {
      return isPoc;
   }

   /**
    * @param isPoc the isPoc to set
    */
   public void setPoc(final boolean isPoc)
   {
      this.isPoc = isPoc;
   }

   /**
    * @return the weatherViewData
    */
   public WeatherViewData getWeatherViewData()
   {
      return WeatherViewData;
   }

   /**
    * @param weatherViewData the weatherViewData to set
    */
   public void setWeatherViewData(final WeatherViewData weatherViewData)
   {
      WeatherViewData = weatherViewData;
   }

}
