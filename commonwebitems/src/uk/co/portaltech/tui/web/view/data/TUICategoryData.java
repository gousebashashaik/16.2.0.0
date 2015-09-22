/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author gagan
 *
 */
public class TUICategoryData
{

   private String code;

   private String name;

   private String description;

   private List<TUICategoryData> categories;

   private String url;

   private String latitude;

   private String longitude;

   private List<String> locationAccomTypes;

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
    * @return the categories
    */
   public List<TUICategoryData> getCategories()
   {
      return categories;
   }

   /**
    * @param categories the categories to set
    */
   public void setCategories(final List<TUICategoryData> categories)
   {
      this.categories = categories;
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

}
