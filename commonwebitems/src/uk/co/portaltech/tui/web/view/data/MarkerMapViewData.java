/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author s.consolino
 *
 */
public class MarkerMapViewData
{

   private String code;

   private String name;

   private String price;

   private String thumbnailUrl;

   private String count;

   private String latitude;

   private String longitude;

   private String classification;

   private String url;

   private LocationData location;

   private List<LocationData> locations;

   private List<AttractionViewData> events;

   private List<AttractionViewData> sights;

   private MediaViewData thumbnail;

   private String flightDutation;

   private String introText;

   private String brandType;

   /**
    * @param locations
    * @param events
    * @param sights
    */
   public MarkerMapViewData()
   {
      super();
      this.locations = new ArrayList<LocationData>();
      this.events = new ArrayList<AttractionViewData>();
      this.sights = new ArrayList<AttractionViewData>();
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
    * @return the flightDutation
    */
   public String getFlightDutation()
   {
      return flightDutation;
   }

   /**
    * @param flightDutation the flightDutation to set
    */
   public void setFlightDutation(final String flightDutation)
   {
      this.flightDutation = flightDutation;
   }

   /**
    * @return the introText
    */
   public String getIntroText()
   {
      return introText;
   }

   /**
    * @param introText the introText to set
    */
   public void setIntroText(final String introText)
   {
      this.introText = introText;
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
    * @return the price
    */
   public String getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final String price)
   {
      this.price = price;
   }

   /**
    * @return the thumbnailUrl
    */
   public String getThumbnailUrl()
   {
      return thumbnailUrl;
   }

   /**
    * @param thumbnailUrl the thumbnailUrl to set
    */
   public void setThumbnailUrl(final String thumbnailUrl)
   {
      this.thumbnailUrl = thumbnailUrl;
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

   /**
    * @return the classification
    */
   public String getClassification()
   {
      return classification;
   }

   /**
    * @param classification the classification to set
    */
   public void setClassification(final String classification)
   {
      this.classification = classification;
   }

   /**
    * @return the overviewUrl
    */
   public String getUrl()
   {
      return url;
   }

   /**
    * @param overviewUrl the overviewUrl to set
    */
   public void setUrl(final String overviewUrl)
   {
      this.url = overviewUrl;
   }

   public List<LocationData> getLocations()
   {
      return locations;
   }

   public void setLocations(final List<LocationData> locations)
   {
      this.locations = locations;
   }

   public List<AttractionViewData> getEvents()
   {
      return events;
   }

   public void setEvents(final List<AttractionViewData> events)
   {
      this.events = events;
   }

   public List<AttractionViewData> getSights()
   {
      return sights;
   }

   public void setSights(final List<AttractionViewData> sights)
   {
      this.sights = sights;
   }

   public MediaViewData getThumbnail()
   {
      return thumbnail;
   }

   public void setThumbnail(final MediaViewData thumbnail)
   {
      this.thumbnail = thumbnail;
   }

   public LocationData getLocation()
   {
      return location;
   }

   public void setLocation(final LocationData location)
   {
      this.location = location;
   }

}
