/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.facades.WeatherComponentFacade;
import uk.co.portaltech.tui.web.view.data.WeatherViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author gagan
 *
 */
public class DefaultWeatherComponentFacade implements WeatherComponentFacade
{

   private static final TUILogUtils LOG = new TUILogUtils("DefaultWeatherComponentFacade");

   @Resource
   private CategoryService categoryService;

   @Resource
   private Converter<WeatherModel, WeatherViewData> weatherViewDataConverter;

   @Resource
   private Populator<WeatherModel, WeatherViewData> weatherViewDataPopulator;

   @Resource
   private ProductService productService;

   @Resource
   private FeatureService featureService;

   @Resource
   private LocationService tuiLocationService;

   @Resource
   private SessionService sessionService;

   @Resource
   private BrandUtils brandUtils;

   private static final int CURRENT_LEVEL = 2;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.WeatherComponentFacade#getWeatherData(java.lang.String)
    */
   @Override
   public WeatherViewData getWeatherData(final LocationModel locationModel)
   {

      WeatherViewData weatherViewData = null;
      final List<Object> features =
         featureService.getFeatureValues("name", locationModel, new Date(),
            brandUtils.getFeatureServiceBrand(locationModel.getBrands()));
      if (CollectionUtils.isNotEmpty(features))
      {
         final WeatherModel weatherModel = locationModel.getWeather();
         if (weatherModel != null && weatherModel.getWeatherTypes() != null
            && !weatherModel.getWeatherTypes().isEmpty())
         {
            weatherViewData = weatherViewDataConverter.convert(weatherModel);
            weatherViewData.setLocationName(features.get(0).toString());
            weatherViewDataPopulator.populate(weatherModel, weatherViewData);
         }
      }
      else
      {
         LOG.warn("Not able to find the location feature value [name] for "
            + locationModel.getCode());
      }

      return weatherViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.WeatherComponentFacade#getCurrentLocationDetails(java.lang.String
    * )
    */
   @Override
   public LocationModel getCurrentLocationDetails(final String categoryCode, final String prodCode)
   {
      LocationModel location = null;
      if (!StringUtils.isEmpty(categoryCode))
      {
         final CategoryModel categoryForCode = categoryService.getCategoryForCode(categoryCode);
         if (categoryForCode instanceof LocationModel)
         {
            location = (LocationModel) categoryForCode;
            final BrandDetails brandDetails =
               sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
            final List<String> brands = brandDetails.getRelevantBrandIds();
            tuiLocationService.getCategoriesFilteredByBrand(location, brands);
         }
      }
      else if (StringUtils.isNotEmpty(prodCode))
      {
         final AccommodationModel accommodation =
            (AccommodationModel) productService.getProductForCode(prodCode);
         final BrandDetails brandDetails =
            sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> brandTypePKS = brandDetails.getRelevantBrands();
         final LocationModel prodLocation =
            tuiLocationService.getLocationForItem(accommodation, brandTypePKS);

         location =
            tuiLocationService.getLocationForItem(prodLocation, LocationType.RESORT, brandTypePKS);
         if (location == null)
         {
            location =
               tuiLocationService.getLocationForItem(prodLocation, LocationType.DESTINATION,
                  brandTypePKS);
         }
         if (location == null)
         {
            location =
               tuiLocationService.getLocationForItem(prodLocation, LocationType.REGION,
                  brandTypePKS);
         }
         if (location == null)
         {
            location =
               tuiLocationService.getLocationForItem(prodLocation, LocationType.COUNTRY,
                  brandTypePKS);
         }
      }
      return location;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.WeatherComponentFacade#getDestinationsForCountryOrRegion(int)
    */
   @Override
   public LocationModel getDestinationsForCountryOrRegion(final LocationModel locationModel,
      final int depthLevel)
   {

      return findFirstDestination(locationModel, CURRENT_LEVEL, depthLevel);
   }

   /**
    * @param locationModel
    * @param currentLevel
    * @param depthLevel
    */
   private LocationModel findFirstDestination(final LocationModel location, final int currentLevel,
      final int depthLevel)
   {
      LocationModel locationModel = location;
      final List<CategoryModel> subCategories = locationModel.getCategories();
      if (subCategories != null && !subCategories.isEmpty())
      {
         for (final CategoryModel subCategoryModel : subCategories)
         {
            if (subCategoryModel instanceof LocationModel)
            {
               final LocationModel subLocation = (LocationModel) subCategoryModel;
               if (LocationType.DESTINATION.getCode().equals(subLocation.getType().getCode()))
               {
                  return subLocation;
               }
               if (depthLevel >= (currentLevel + 1))
               {
                  locationModel = findFirstDestination(subLocation, currentLevel + 1, depthLevel);
               }
            }
         }
      }
      return locationModel;
   }

   /**
    * @param locationModel
    */
   @Override
   public List<LocationModel> getDestinations(final LocationModel locationModel)
   {
      final List<LocationModel> locModel = new ArrayList<LocationModel>();
      final List<CategoryModel> subCategories = locationModel.getCategories();
      if (subCategories != null && !subCategories.isEmpty())
      {
         for (final CategoryModel subCategoryModel : subCategories)
         {
            if (subCategoryModel instanceof LocationModel)
            {
               final LocationModel subLocation = (LocationModel) subCategoryModel;
               locModel.add(subLocation);
               /*
                * Need to revisit...
                */

            }
         }
      }
      return locModel;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.WeatherComponentFacade#getDestinationsForResort(uk.co.portaltech
    * .travel.model. LocationModel)
    */
   @Override
   public LocationModel getDestinationsForResort(final LocationModel resortModel)
   {
      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandTypePKS = brandDetails.getRelevantBrands();
      LocationModel location =
         tuiLocationService.getLocationForItem(resortModel, LocationType.DESTINATION, brandTypePKS);
      if (location == null)
      {
         location =
            tuiLocationService.getLocationForItem(resortModel, LocationType.REGION, brandTypePKS);
      }
      if (location == null)
      {
         location =
            tuiLocationService.getLocationForItem(resortModel, LocationType.COUNTRY, brandTypePKS);
      }
      if (location == null)
      {
         return resortModel;
      }
      return location;
   }

   @Override
   public List<WeatherViewData> getCountryWeatherData(final LocationModel locationModel)
   {
      /*
       * Getting the Country/Region Weather data. If data is not available then getting the data for
       * sub categories.
       */
      final List<WeatherViewData> weatherDatas = new ArrayList<WeatherViewData>();
      final WeatherViewData weatherData = getWeatherData(locationModel);
      if (weatherData != null)
      {
         weatherDatas.add(weatherData);
      }
      else
      {
         getLocationCategoriesWeatherData(locationModel, weatherDatas);
      }
      return weatherDatas;
   }

   /**
    * @param locationModel
    * @param weatherDatas
    */
   private void getLocationCategoriesWeatherData(final LocationModel locationModel,
      final List<WeatherViewData> weatherDatas)
   {
      WeatherViewData weatherData;
      final List<CategoryModel> categories = locationModel.getCategories();
      for (final CategoryModel category : categories)
      {
         if (category instanceof LocationModel)
         {
            weatherData = getWeatherData((LocationModel) category);
            if (weatherData != null)
            {
               weatherDatas.add(weatherData);
            }
         }
      }
   }

   @Override
   public List<WeatherViewData> getSpainWeatherData(final LocationModel locationModel)
   {
      final List<WeatherViewData> weatherDatas = new ArrayList<WeatherViewData>();
      final List<CategoryModel> categories = locationModel.getCategories();
      for (final CategoryModel category : categories)
      {
         if (category instanceof LocationModel)
         {
            final WeatherViewData weatherData = getWeatherData((LocationModel) category);
            if (weatherData != null)
            {
               weatherDatas.add(weatherData);
            }
            else
            {
               weatherDatas.addAll(getWeatherDataList(category.getCategories()));
            }
         }
      }
      return weatherDatas;
   }

   private List<WeatherViewData> getWeatherDataList(final List<CategoryModel> categories)
   {
      final List<WeatherViewData> weatherDatas = new ArrayList<WeatherViewData>();
      for (final CategoryModel category : categories)
      {
         if (category instanceof LocationModel)
         {
            final WeatherViewData weatherData = getWeatherData((LocationModel) category);
            if (weatherData != null)
            {
               weatherDatas.add(weatherData);
            }
         }
      }
      return weatherDatas;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.WeatherComponentFacade#getWeatherDataForLocation(uk.co.portaltech
    * .travel.model. LocationModel)
    */
   @Override
   public WeatherViewData getWeatherDataForLocation(final LocationModel locationModel)
   {

      WeatherViewData weatherViewData = null;
      // get super category weather data
      weatherViewData = getSuperCategoryWeatherData(locationModel);
      if (weatherViewData != null && CollectionUtils.isNotEmpty(weatherViewData.getWeatherTypes()))
      {
         return weatherViewData;

      }
      // if not get sub-category weather data
      return getSubCategoryWeatherData(locationModel);
   }

   /**
    * get sub-category weather data
    *
    * @param locationModel
    *
    * @return WeatherViewData
    */
   private WeatherViewData getSubCategoryWeatherData(final LocationModel locationModel)
   {
      final Collection<CategoryModel> allSubCategories = locationModel.getAllSubcategories();
      for (final CategoryModel category : allSubCategories)
      {
         if (category instanceof LocationModel)
         {
            final WeatherViewData weatherData = getWeatherData((LocationModel) category);
            if (weatherData != null)
            {
               return weatherData;
            }
            else
            {
               return getSubCategoryWeatherData((LocationModel) category);
            }
         }
      }
      return null;
   }

   /**
    * get super category weather data
    *
    * @param locationModel
    *
    * @return WeatherViewData
    */
   private WeatherViewData getSuperCategoryWeatherData(final LocationModel locationModel)
   {
      final List<CategoryModel> categories = locationModel.getSupercategories();
      for (final CategoryModel category : categories)
      {
         if (category instanceof LocationModel)
         {
            final WeatherViewData weatherData = getWeatherData((LocationModel) category);
            if (weatherData != null)
            {
               return weatherData;
            }
            else
            {
               return getSuperCategoryWeatherData((LocationModel) category);
            }
         }
      }
      return null;
   }

}
