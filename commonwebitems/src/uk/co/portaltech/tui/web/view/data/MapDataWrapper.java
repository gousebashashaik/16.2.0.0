/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author omonikhide
 *
 */
public class MapDataWrapper
{

   private String topLocationName;

   private List<AttractionViewData> events;

   private List<AttractionViewData> sights;

   private List<AccommodationViewData> hotels;

   private List<AccommodationViewData> villas;

   private List<AccommodationViewData> accommodations;

   private List<LocationData> locations;

   private List<ExcursionViewData> excursions;

   private List<String> superCategoryNames;

   private String locationType;

   private String siteBrand;

   private String aniteBrand;

   public MapDataWrapper()
   {
      events = new ArrayList<AttractionViewData>();
      sights = new ArrayList<AttractionViewData>();
      hotels = new ArrayList<AccommodationViewData>();
      villas = new ArrayList<AccommodationViewData>();

      accommodations = new ArrayList<AccommodationViewData>();
      locations = new ArrayList<LocationData>();
      excursions = new ArrayList<ExcursionViewData>();
   }

   /**
    * @return the aniteBrand
    */
   public String getAniteBrand()
   {
      return aniteBrand;
   }

   /**
    * @param aniteBrand the aniteBrand to set
    */
   public void setAniteBrand(final String aniteBrand)
   {
      this.aniteBrand = aniteBrand;
   }

   /**
    * @return the hotels
    */
   public List<AccommodationViewData> getHotels()
   {
      return hotels;
   }

   /**
    * @param hotels the hotels to set
    */
   public void setHotels(final List<AccommodationViewData> hotels)
   {
      this.hotels = hotels;
   }

   /**
    * @return the villas
    */
   public List<AccommodationViewData> getVillas()
   {
      return villas;
   }

   /**
    * @param villas the villas to set
    */
   public void setVillas(final List<AccommodationViewData> villas)
   {
      this.villas = villas;
   }

   public List<AccommodationViewData> getAccommodations()
   {
      return accommodations;
   }

   public void setAccommodations(final List<AccommodationViewData> accommodations)
   {
      this.accommodations = accommodations;
   }

   public List<LocationData> getLocations()
   {
      return locations;
   }

   public void setLocations(final List<LocationData> locations)
   {
      this.locations = locations;
   }

   public List<String> getSuperCategoryNames()
   {
      return superCategoryNames;
   }

   public void setSuperCategoryNames(final List<String> superCategoryNames)
   {
      this.superCategoryNames = superCategoryNames;
   }

   public List<ExcursionViewData> getExcursions()
   {
      return excursions;
   }

   public void setExcursions(final List<ExcursionViewData> excursions)
   {
      this.excursions = excursions;
   }

   /**
    * @return the events
    */
   public List<AttractionViewData> getEvents()
   {
      return events;
   }

   /**
    * @param events the events to set
    */
   public void setEvents(final List<AttractionViewData> events)
   {
      this.events = events;
   }

   /**
    * @return the sights
    */
   public List<AttractionViewData> getSights()
   {
      return sights;
   }

   /**
    * @param sights the sights to set
    */
   public void setSights(final List<AttractionViewData> sights)
   {
      this.sights = sights;
   }

   public String getTopLocationName()
   {
      return topLocationName;
   }

   public void setTopLocationName(final String topLocationName)
   {
      this.topLocationName = topLocationName;
   }

   /**
    * @return the locationType
    */
   public String getLocationType()
   {
      return locationType;
   }

   /**
    * @param locationType the locationType to set
    */
   public void setLocationType(final String locationType)
   {
      this.locationType = locationType;
   }

   /**
    * @return the siteBrand
    */
   public String getSiteBrand()
   {
      return siteBrand;
   }

   /**
    * @param siteBrand the siteBrand to set
    */
   public void setSiteBrand(final String siteBrand)
   {
      this.siteBrand = siteBrand;
   }

}
