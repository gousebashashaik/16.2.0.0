/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.enums.LocationType;

/**
 * @author gagan
 *
 */
public class GeographicalNavigationComponentViewData
{

   private List<TUICategoryData> categories;

   private Integer navigationLevelDepth;

   private LocationType locationType;

   private List<String> validSubLocationsForCurrentLocation;

   private Map<String, List<AccommodationViewData>> accommodations;

   private String placesToGoUrl;

   private String locationName;

   private Map<String, List<TUICategoryData>> categoriesByAccomType;

   /**
    * @return the locationName
    */
   public String getLocationName()
   {
      return locationName;
   }

   /**
    * @param locationName the locationName to set
    */
   public void setLocationName(final String locationName)
   {
      this.locationName = locationName;
   }

   /**
    * @return the placesToGoUrl
    */
   public String getPlacesToGoUrl()
   {
      return placesToGoUrl;
   }

   /**
    * @param placesToGoUrl the placesToGoUrl to set
    */
   public void setPlacesToGoUrl(final String placesToGoUrl)
   {
      this.placesToGoUrl = placesToGoUrl;
   }

   /**
    * @return the validSubLocationsForCurrentLocation
    */
   public List<String> getValidSubLocationsForCurrentLocation()
   {
      return validSubLocationsForCurrentLocation;
   }

   /**
    * @param validSubLocationsForCurrentLocation the validSubLocationsForCurrentLocation to set
    */
   public void setValidSubLocationsForCurrentLocation(
      final List<String> validSubLocationsForCurrentLocation)
   {
      this.validSubLocationsForCurrentLocation = validSubLocationsForCurrentLocation;
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
    * @return the navigationLevelDepth
    */
   public Integer getNavigationLevelDepth()
   {
      return navigationLevelDepth;
   }

   /**
    * @param navigationLevelDepth the navigationLevelDepth to set
    */
   public void setNavigationLevelDepth(final Integer navigationLevelDepth)
   {
      this.navigationLevelDepth = navigationLevelDepth;
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

   /**
    * @return the accommodations
    */
   public Map<String, List<AccommodationViewData>> getAccommodations()
   {
      return accommodations;
   }

   /**
    * @param accommodations the accommodations to set
    */
   public void setAccommodations(final Map<String, List<AccommodationViewData>> accommodations)
   {
      this.accommodations = accommodations;
   }

   /**
    * @return the categoriesByAccomType
    */
   public Map<String, List<TUICategoryData>> getCategoriesByAccomType()
   {
      return categoriesByAccomType;
   }

   /**
    * @param categoriesByAccomType the categoriesByAccomType to set
    */
   public void setCategoriesByAccomType(
      final Map<String, List<TUICategoryData>> categoriesByAccomType)
   {
      this.categoriesByAccomType = categoriesByAccomType;
   }

}
