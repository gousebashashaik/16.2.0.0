/**
 *
 */
package uk.co.portaltech.tui.web.search.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.services.DurationHaulTypeService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.search.CriteriaWidenService;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.WidenSearchCriteriaData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.tui.async.logging.TUILogUtils;

public class CriteriaWidenServiceImpl implements CriteriaWidenService
{

   // @Autowired

   // @Autowired

   @Resource
   private ProductService productService;

   @Resource
   private CategoryService categoryService;

   @Resource
   private DurationHaulTypeService durationHaulTypeService;

   private ConfigurationService configurationService;

   @Resource
   private TypeService typeService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   private static final String LONG_HAUL = "LH";

   private static final String SHORT_HAUL = "SH";

   private static final String ANY_SHORT_HAUL_COUNTRY = "Any Short Haul Country";

   private static final String ANY_LONG_HAUL_COUNTRY = "Any Long Haul Country";

   private static final String ANY_DESTINATION = "Any Destination";

   private static final String ANY_UK_AIRPORT = "Any UK airport";

   private static final String ANY_ROI_AIRPORT = "Any ROI airport";

   private static final int DEFAULT_FLEXIBLE_DAYS = 10;

   private static final int DEFAULT_ACCOM_FLEXIBLE_DAYS = 15;

   private static final TUILogUtils LOG = new TUILogUtils("CriteriaWidenServiceImpl");

   @Override
   public WidenSearchCriteriaData widenSearchCriteria(final SearchResultsRequestData searchRequest,
      final String widenType)
   {

      if (StringUtils.equalsIgnoreCase(widenType, "WHEN"))
      {
         return widenDate(searchRequest);
      }
      else if (StringUtils.equalsIgnoreCase(widenType, "WHEREFROM"))
      {
         return widenFrom(searchRequest);
      }
      else if (StringUtils.equalsIgnoreCase(widenType, "WHERETO"))
      {
         return widenTo(searchRequest);
      }

      return null;

   }

   /**
    * Widening of Date - ie the flexible days will be modified to Maximum flexibility Date will be
    * widened in all three types of widening
    *
    * @param searchRequest
    * @return WidenSearchCriteriaData
    */
   private WidenSearchCriteriaData widenDate(final SearchResultsRequestData searchRequest)
   {

      final WidenSearchCriteriaData widenSearchCriteriaData = new WidenSearchCriteriaData();
      widenSearchCriteriaData.setSearchRequest(searchRequest);
      populateFlexibleDaysAndDuration(widenSearchCriteriaData, searchRequest);
      return widenSearchCriteriaData;

   }

   /**
    * Widen Where To
    *
    * @param searchRequest
    * @return WidenSearchCriteriaData
    */
   private WidenSearchCriteriaData widenTo(final SearchResultsRequestData searchRequest)
   {

      final WidenSearchCriteriaData widenSearchCriteriaData = new WidenSearchCriteriaData();

      if (CollectionUtils.isNotEmpty(searchRequest.getUnits()))
      {
         widenSearchCriteriaData.setSearchRequest(searchRequest);
         populateFlexibleDaysAndDuration(widenSearchCriteriaData, searchRequest);

         return getWidenUnits(searchRequest, widenSearchCriteriaData);
      }
      return null;
   }

   /**
    * Widen Where From
    *
    * @param searchRequest
    * @return WidenSearchCriteriaData
    */
   private WidenSearchCriteriaData widenFrom(final SearchResultsRequestData searchRequest)
   {

      final String siteBrand = tuiUtilityService.getSiteBrand();
      WidenSearchCriteriaData widenSearchCriteriaData = null;
      if (CollectionUtils.isNotEmpty(searchRequest.getAirports()))
      {
         widenSearchCriteriaData = new WidenSearchCriteriaData();
         if ("FJ".equalsIgnoreCase(siteBrand))
         {
            widenSearchCriteriaData.setDisplayName(ANY_ROI_AIRPORT);
         }
         else
         {
            widenSearchCriteriaData.setDisplayName(ANY_UK_AIRPORT);
         }
         widenSearchCriteriaData.setSearchRequest(searchRequest);
         widenSearchCriteriaData.getSearchRequest().setAirports(new ArrayList<AirportData>());
         populateFlexibleDaysAndDuration(widenSearchCriteriaData, searchRequest);

      }
      return widenSearchCriteriaData;

   }

   /**
    * Method to Get widened data based on criteria
    *
    * @param widenSearchCriteriaData
    * @param searchRequest
    */
   private WidenSearchCriteriaData getWidenUnits(final SearchResultsRequestData searchRequest,
      final WidenSearchCriteriaData widenSearchCriteriaData)
   {

      final UnitData unitData = searchRequest.getUnits().get(0);

      List<UnitData> listOfUnitData = new ArrayList<UnitData>();

      if ((StringUtils.equalsIgnoreCase(unitData.getType(), "COUNTRY"))
         || (StringUtils.equalsIgnoreCase(unitData.getType(), "LAP_COUNTRY")))
      {
         listOfUnitData =
            widenCountry(unitData.getId(), searchRequest.getAirports(), widenSearchCriteriaData);
         if (CollectionUtils.isEmpty(listOfUnitData))
         {
            return null;
         }
      }
      else if ((StringUtils.equalsIgnoreCase(unitData.getType(), "REGION"))
         || (StringUtils.equalsIgnoreCase(unitData.getType(), "LAP_REGION")))
      {
         listOfUnitData = widenRegion(unitData.getId());
         widenSearchCriteriaData.setDisplayName(listOfUnitData.get(0).getName());
      }
      else if ((StringUtils.equalsIgnoreCase(unitData.getType(), "DESTINATION"))
         || (StringUtils.equalsIgnoreCase(unitData.getType(), "LAP_DESTINATION")))
      {
         listOfUnitData = widenDestination(unitData.getId());
         widenSearchCriteriaData.setDisplayName(listOfUnitData.get(0).getName());
      }
      else if ((StringUtils.equalsIgnoreCase(unitData.getType(), "RESORT"))
         || (StringUtils.equalsIgnoreCase(unitData.getType(), "LAP_RESORT")))
      {
         listOfUnitData = widenResort(unitData.getId());
         widenSearchCriteriaData.setDisplayName(listOfUnitData.get(0).getName());
      }
      else if (checkIfAccomType(unitData.getType()))
      {
         listOfUnitData = widenHotel(unitData.getId());
         if (CollectionUtils.isEmpty(listOfUnitData))
         {
            return null;
         }
         widenSearchCriteriaData.setDisplayName(listOfUnitData.get(0).getName());
      }
      else
      {
         listOfUnitData = new ArrayList<UnitData>();

         widenSearchCriteriaData.setDisplayName(ANY_DESTINATION);
         if (CollectionUtils.isEmpty(searchRequest.getAirports()))
         {
            return null;
         }
      }

      widenSearchCriteriaData.getSearchRequest().setUnits(listOfUnitData);
      return widenSearchCriteriaData;

   }

   /**
    * checking if the request is either LAP_HOTEL ,DayTrip,Hotel,self_catered,villa,Apartment Hotel.
    */
   private boolean checkIfAccomType(final String value)
   {

      if ("DayTrip".equalsIgnoreCase(value) || "LAP_HOTEL".equalsIgnoreCase(value))
      {
         return true;
      }

      final Iterator<ItemModel> itr =
         typeService.getEnumerationTypeForCode("AccommodationType").getValues().iterator();

      while (itr.hasNext())
      {
         final EnumerationValueModel model = (EnumerationValueModel) itr.next();
         if (model.getCode().equalsIgnoreCase(value))
         {
            return true;
         }

      }

      return false;
   }

   /**
    * Function to Widen Country - country has any Long\short Haul country
    *
    * @param countryCode
    * @param airports
    * @return UnitData
    */
   private List<UnitData> widenCountry(final String countryCode, final List<AirportData> airports,
      final WidenSearchCriteriaData widenSearchCriteriaData)
   {

      String haulType = null;
      List<UnitData> unitData = null;
      final Set<String> departAirportCode = getDepartureAirport(airports);
      if (CollectionUtils.isNotEmpty(departAirportCode))
      {

         final SearchResult<List<Object>> searchresult =
            durationHaulTypeService.getDurationHaulType(departAirportCode, countryCode);
         if (searchresult != null && searchresult.getCount() > 0)
         {
            haulType = findMaxHaulType(searchresult);
         }
         else
         {
            LOG.warn("Error while fetching haul Type for");
            throw new IllegalStateException("Error!Error while fetching haul Type for");
         }

         final List<String> listCountryCode =
            durationHaulTypeService.getHaulCountry(haulType, departAirportCode);
         unitData = populateUnitData(listCountryCode);
         if (StringUtils.equalsIgnoreCase(haulType, SHORT_HAUL))
         {
            widenSearchCriteriaData.setDisplayName(ANY_SHORT_HAUL_COUNTRY);
         }
         else
         {
            widenSearchCriteriaData.setDisplayName(ANY_LONG_HAUL_COUNTRY);
         }
      }
      return unitData;
   }

   /**
    * Method to Extract all departure AirportID to a list
    *
    * @param airports
    * @return List - containing departure airportcode
    */
   private Set<String> getDepartureAirport(final List<AirportData> airports)
   {

      final Set<String> departAirportCode = new HashSet<String>();

      if (CollectionUtils.isNotEmpty(airports))
      {
         for (final AirportData airporData : airports)
         {
            if (CollectionUtils.isNotEmpty(airporData.getChildren()))
            {
               for (final String childAirport : airporData.getChildren())
               {
                  departAirportCode.add(childAirport);
               }
            }
            else
            {
               departAirportCode.add(airporData.getId());
            }
         }
      }
      return departAirportCode;
   }

   /**
    * Get unit data from PIM based on country code
    *
    * @param listCountryCode
    * @return UnitData
    */
   private List<UnitData> populateUnitData(final List<String> listCountryCode)
   {
      final List<UnitData> unitData = new ArrayList<UnitData>();

      if (CollectionUtils.isNotEmpty(listCountryCode))
      {
         for (final String countryCode : listCountryCode)
         {
            final LocationModel countryModel = getLocationModelForCode(countryCode);
            if (countryModel != null)
            {
               unitData.add(new UnitData(countryModel.getCode(), countryModel.getName(),
                  countryModel.getType().toString()));
            }
         }
      }
      return unitData;
   }

   /**
    * widen Region to Country
    *
    * @param id
    * @return List<UnitData>
    */
   private List<UnitData> widenRegion(final String id)
   {

      final List<UnitData> unitData = new ArrayList<UnitData>();
      final LocationModel locationModel = getLocationModelForCode(id);
      final LocationModel superCategory = getSuperCategoriesCode(locationModel);

      if (superCategory != null && superCategory.getType().equals(LocationType.COUNTRY))
      {
         unitData.add(new UnitData(superCategory.getCode(), superCategory.getName(), superCategory
            .getType().toString()));
      }
      return unitData;
   }

   /**
    * widen Destination to Country
    *
    * @param id
    * @return List<UnitData>
    */
   private List<UnitData> widenDestination(final String id)
   {

      final List<UnitData> unitData = new ArrayList<UnitData>();
      final LocationModel locationModel = getLocationModelForCode(id);
      final LocationModel superCategory = getSuperCategoriesCode(locationModel);

      if (superCategory != null && superCategory.getType().equals(LocationType.COUNTRY))
      {
         unitData.add(new UnitData(superCategory.getCode(), superCategory.getName(), superCategory
            .getType().toString()));
         return unitData;
      }
      else if (superCategory != null && superCategory.getType().equals(LocationType.REGION))
      {
         final LocationModel regionsuperCategory = getSuperCategoriesCode(superCategory);
         if (regionsuperCategory != null
            && regionsuperCategory.getType().equals(LocationType.COUNTRY))
         {
            unitData.add(new UnitData(regionsuperCategory.getCode(), regionsuperCategory.getName(),
               regionsuperCategory.getType().toString()));
            return unitData;
         }
      }
      return unitData;
   }

   /**
    * widen to one level up the geo-hierarchy
    *
    * @param id
    * @return List<UnitData>
    */
   private List<UnitData> widenResort(final String id)
   {
      final LocationModel locationModel = getLocationModelForCode(id);
      final LocationModel superCategory = getSuperCategoriesCode(locationModel);
      final List<UnitData> unitData = new ArrayList<UnitData>();

      if (superCategory != null)
      {
         unitData.add(new UnitData(superCategory.getCode(), superCategory.getName(), superCategory
            .getType().toString()));
      }
      return unitData;
   }

   /**
    * Widen the hotel to two levels up the geo-hierarchy
    *
    * @param id
    * @return List<UnitData>
    */
   private List<UnitData> widenHotel(final String id)
   {
      final ProductModel accommodationModel = fetchProductModel(id);
      final LocationModel superCategory = getAccommodationSuperCategory(accommodationModel);
      final LocationModel secondLevelSuperCategory = getSuperCategoriesCode(superCategory);
      final List<UnitData> unitData = new ArrayList<UnitData>();

      if (secondLevelSuperCategory != null)
      {
         unitData.add(new UnitData(secondLevelSuperCategory.getCode(), secondLevelSuperCategory
            .getName(), secondLevelSuperCategory.getType().toString()));
      }
      return unitData;
   }

   /**
    * This method will populate the flexible day to max configured flexibility
    *
    * @param widenSearchCriteriaData
    */
   private void populateFlexibleDaysAndDuration(
      final WidenSearchCriteriaData widenSearchCriteriaData,
      final SearchResultsRequestData requestData)
   {

      int flexibleDays = 0;

      if (CollectionUtils.size(requestData.getUnits()) == 1
         && ((StringUtils.equalsIgnoreCase("HOTEL", requestData.getUnits().get(0).getType())) || (StringUtils
            .equalsIgnoreCase("APARTMENT_HOTEL", requestData.getUnits().get(0).getType()))))
      {
         flexibleDays =
            configurationService.getConfiguration().getInt(
               "holiday.criteriaWiden.maxSingleAccommodationFlexibility",
               DEFAULT_ACCOM_FLEXIBLE_DAYS);

      }
      else
      {
         flexibleDays =
            configurationService.getConfiguration().getInt(
               "holiday.criteriaWiden.maximumFlexibility", DEFAULT_FLEXIBLE_DAYS);
      }

      widenSearchCriteriaData.getSearchRequest().setFlexibility(true);
      widenSearchCriteriaData.getSearchRequest().setFlexibleDays(flexibleDays);
      // commented this since its stay

   }

   /**
    * Find the first level parent of Accommodation
    *
    * @param accommodationModel
    * @return LocationModel
    */
   private LocationModel getAccommodationSuperCategory(final ProductModel accommodationModel)
   {

      LocationModel locationModel = null;
      if (accommodationModel != null)
      {

         for (final CategoryModel category : accommodationModel.getSupercategories())
         {
            if (category instanceof LocationModel)
            {
               locationModel = (LocationModel) category;
            }
         }
      }

      return locationModel;

   }

   /**
    * Find the location model based on location code
    *
    * @param locationCode
    * @return LocationModel
    */
   private LocationModel getLocationModelForCode(final String locationCode)
   {

      LocationModel locationModel = null;
      try
      {
         final CategoryModel category = categoryService.getCategoryForCode(locationCode);
         if (category instanceof LocationModel)
         {
            locationModel = (LocationModel) category;
         }

      }
      catch (final UnknownIdentifierException notFound)
      {
         LOG.warn("NO Location not for code = " + locationCode, notFound);
      }

      return locationModel;

   }

   /**
    * Finding super category of a location model
    *
    * @param locationModel
    * @return LocationModel
    */
   private LocationModel getSuperCategoriesCode(final LocationModel locationModel)
   {

      if (locationModel != null)
      {
         for (final CategoryModel categories : locationModel.getSupercategories())
         {
            if (categories instanceof LocationModel)
            {
               return (LocationModel) categories;
            }
         }
      }
      return null;
   }

   /**
    * Fetch Accommodation(Hotel) model from product service
    *
    * @param accomCode
    * @return product
    */
   private ProductModel fetchProductModel(final String accomCode)
   {
      ProductModel product = null;
      try
      {
         product = productService.getProductForCode(accomCode);
         Validate.notNull(product);
      }
      catch (final ModelNotFoundException exc)
      {
         LOG.error("THe Product model with code " + accomCode + " does not exist in PIM.", exc);
      }
      return product;
   }

   /**
    * Method to --Calculate if the country has more than one haul type, then system must choose the
    * predominant haul type
    *
    * @param result
    * @return String -- LH/SH
    */
   private String findMaxHaulType(final SearchResult<List<Object>> result)
   {
      int shortHaulCount = 0;
      int longHaulCount = 0;

      for (final List<Object> resultRow : result.getResult())
      {

         if (StringUtils.equalsIgnoreCase((String) resultRow.get(1), LONG_HAUL))
         {
            longHaulCount = longHaulCount + 1;
         }
         else if (StringUtils.equalsIgnoreCase((String) resultRow.get(1), SHORT_HAUL))
         {
            shortHaulCount = shortHaulCount + 1;
         }
      }

      if (shortHaulCount >= longHaulCount)
      {
         return SHORT_HAUL;
      }
      return LONG_HAUL;

   }

   /**
    * @return the configurationService
    */
   public ConfigurationService getConfigurationService()
   {
      return configurationService;
   }

   /**
    * @param configurationService the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }

}
