/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.CountryViewData;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MostPopularDestinationdata;
import uk.co.portaltech.tui.web.view.data.wrapper.DestinationSearchResult;

/**
 * @author omonikhide
 *
 */
public interface LocationFacade
{
   /**
    * retrieves health and safety editorial for a given location
    *
    * @param locationCode The code for the location(category code)
    * @return The locationData object with a map containing health and safety feature code and its
    *         value.
    */
   LocationData getHealthAndSafetyEditorial(String locationCode);

   /**
    * retrieves passport and visas editorial for a given location
    *
    * @param locationCode The code for the location(category code)
    * @return The locationData object with a map containing passport and visa feature code and its
    *         value.
    */
   LocationData getPassportAndVisaEditorial(String locationCode);

   /**
    * retrieves where to go data for a given location
    *
    * @param locationCode The code for the location(category code)
    * @return The locationData object with a map containing where to go key and its value.
    */
   LocationData getWhereToGoData(String locationCode);

   /**
    * retrieves when to go editorial for a location
    *
    * @param locationCode The code for the location(category code)
    * @return The location view data with a map containing when to go feature and its value.
    */
   LocationData getWhenToGoEditorial(String locationCode);

   /**
    * retrieves getting around editorial for a location
    *
    * @param locationCode The code for the location(category code)
    * @return The location view data with a map containing getting around feature and its value.
    */
   LocationData getGettingAroundEditorial(String locationCode);

   /**
    * retrieves thumbail mapa data for a location
    *
    * @param locationCode The code for the location
    * @return The location view data with a map containing longitude and latitude features and their
    *         respective values
    */
   LocationData getThumbnailMapData(String locationCode);

   /**
    * retrieves editorial data for a location
    *
    * @param locationCode The code for the location
    * @return The location view data with with keyfacts data(language, currency, flight duration,
    *         transfer time, timezones) populated in the featureCodesAndValues map.
    */
   LocationData getLocationKeyFactsData(String locationCode);

   /**
    * retrieves articles associated with location
    *
    * @param locationCode The code for the location
    * @return The location view data containing a list of articles
    */
   LocationData getArticlesForTheLocation(String locationCode);

   /**
    * retrieves articles associated with location
    *
    * @param locationCode The code for the location
    * @return The location view data containing a things to do and see editorial
    */
   LocationData getThingsToDoAndSeeEditorial(String locationCode);

   /**
    * retrieves the location for given code
    *
    * @param locationCode The code for the location
    * @return The location view data containing a things to do and see editorial
    */
   LocationData getLocationData(String locationCode);

   /**
    * Retrieves highlights (USPs) associated with a location
    *
    * @param locationCode The code for the location
    * @param highlightsNumber Nomber of highlights to retrieves
    * @return The {@link LocationData} object with a map containing USPs features and their
    *         respective values
    */
   LocationData getLocationHighlights(String locationCode, Integer highlightsNumber);

   /**
    * Gets a list of {@link LocationData} that contains the location data for the child locations of
    * the provided location.
    *
    * @param locationCode The code of the parent location.
    * @param visibleItems
    * @return A list containing the childlocations for the location provided
    */
   List<LocationData> getChildLocationsData(String locationCode, int visibleItems,
      String accommodationTypeContext);

   /**
    * Gets a list of {@link LocationData} that contains the location data for the child locations of
    * the provided location.
    *
    * @param locationCode The code of the parent location.
    * @param visibleItems
    * @return A list containing the childlocations for the location provided
    */
   List<LocationData> getChildLocationsData(String locationCode, int visibleItems);

   /**
    * same as {@link #areProductsAvailableInLocation(LocationModel)} but with the codeof the
    * location as parameter instead of the model.
    */
   boolean areProductsAvailableInLocation(String locationCode);

   /**
    * Navigates the hierarchy of subcategories for the given location and checks if there is at
    * least one accommodation available (i.e., in an approved status). <br/>
    * The assumption is that only resorts can have accommodations attached.
    *
    * @param locationModel the model ofthe location which we want to be the root. If it is not a
    *           resort the method will look into subcategories. If it is a resort the method will
    *           look directly into the attached accommodations.
    * @return true: if at least one accommodation is found.<br/>
    *         false: if not
    */
   boolean areProductsAvailableInLocation(LocationModel locationModel);

   /**
    * This returns a list of subcategories associated with a particular location
    *
    * @param locationCode The code of the location
    * @return A location data and with a list of location data with subcategories data
    */
   LocationData getLocationInteractiveMapData(String locationCode);

   /**
    * This method will populate the data required for top places to stay carousel
    *
    * @param resortsResultData
    * @return
    */
   List<LocationData> getEndecaResortViewData(List<ResultData> resortsResultData);

   /**
    * This method returns the location related to an accommodation
    *
    * @param accommodationCode the accommodation code
    * @return the location related to the accommodation
    */
   LocationData getLocationForAccommodation(String accommodationCode);

   /**
    * This method returns the list of countries
    *
    * @return the list of countries
    */
   List<DestinationData> getAllCountries(List<String> brandList, String multiSelect);

   /**
    * This method returns the hierarchy of locations of a given country
    *
    * @return the of hierarchy of locations
    */
   CountryViewData getChildCategories(final String key, final List<String> brandList);

   /**
    * This method returns the list of destinations
    *
    * @return the list of destinations
    */
   DestinationSearchResult getDestination(List<String> airports, List<String> units, String key,
      List<String> dates, List<String> releventBrands, String siteBRand, String multiSelect);

   /**
    * @param location
    * @param airports
    * @param dates
    * @param releventBrands
    */
   void getAvailableLocation(DestinationData location, List<String> airports, List<String> dates,
      List<String> releventBrands, String multiSelect);

   /**
    * This method returns the list of countries
    *
    * @return the list of countries
    */
   List<DestinationData> getCountries(List<String> airports, List<String> dates,
      List<String> brandPks, String multiSelect, List<String> flightRouteBrands);

   /**
    *
    * @param locationCode
    * @return locationData
    */
   LocationData getBasicLocationDetails(String locationCode);

   /**
    * @param key
    * @param airports
    * @param dates
    * @param releventBrands
    * @param releventBrandPks
    * @return
    */
   CountryViewData getChildCategoriesForNewSearchPanel(String key, List<String> airports,
      List<String> dates, List<String> releventBrands, List<String> releventBrandPks,
      String multiSelect);

   /**
    * @param locationCode
    * @return
    */
   LocationData getThingsToSeeAndDoEditorial(String locationCode);

   /**
    * @param data
    * @param airports
    * @param dates
    * @param releventBrands
    * @param releventBrandpks
    * @param multiSelect
    * @return list of most popular data
    */
   List<MostPopularDestinationdata> getMostPopularDestinationData(List<String> data,
      List<String> airports, List<String> dates, List<String> releventBrands,
      List<String> releventBrandpks, String multiSelect);

   /**
    * @param countryCode
    * @param airports
    * @param dates
    * @param brands
    * @param brandPks
    * @param multiSel
    * @return DestinationData
    */
   DestinationData getRegionAndDestinationDataForCountry(String countryCode, List<String> airports,
      List<String> dates, List<String> brands, List<String> brandPks, String multiSel);

}
