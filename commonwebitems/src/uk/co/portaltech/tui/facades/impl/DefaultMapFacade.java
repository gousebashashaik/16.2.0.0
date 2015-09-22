package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.tui.components.model.InspirationMapComponentModel;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.facades.MapFacade;
import uk.co.portaltech.tui.model.InspirationMapTabModel;
import uk.co.portaltech.tui.populators.AccommodationInteractiveMapPopulator;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.InspirationMapComponentData;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MarkerMapViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author s.consolino
 *
 */
public class DefaultMapFacade implements MapFacade
{
   @Resource
   private Converter<List<LocationModel>, InspirationMapViewData> inspirationMapConverter;

   private Populator<List<LocationModel>, InspirationMapViewData> inspirationMapPopulator;

   @Resource
   private Converter<List<AccommodationModel>, InspirationMapViewData> inspirationMapConverterForAccom;

   private Populator<List<AccommodationModel>, InspirationMapViewData> inspirationMapPopulatorForAccom;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private AccommodationInteractiveMapPopulator accommodationInteractiveMapPopulator;

   @Resource
   private CategoryService categoryService;

   @Resource
   private LocationConverter locationConverter;

   @Resource
   private ConfigurablePopulator<LocationModel, LocationData, LocationOption> defaultLocationConfiguredPopulator;

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource(name = "siteAwareKeyGenerator")
   private SiteAwareKeyGenerator keyGenerator;

   private static final int TWO = 2;

   @Required
   public void setInspirationMapPopulator(
      final Populator<List<LocationModel>, InspirationMapViewData> inspirationMapPopulator)
   {
      this.inspirationMapPopulator = inspirationMapPopulator;
   }

   @Required
   public void setInspirationMapPopulatorForAccom(
      final Populator<List<AccommodationModel>, InspirationMapViewData> inspirationMapPopulatorForAccom)
   {
      this.inspirationMapPopulatorForAccom = inspirationMapPopulatorForAccom;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.MapFacade#getInspirationMapData()
    */
   @Override
   public InspirationMapComponentData getInspirationMap(
      final InspirationMapComponentModel inspirationMapComponentModel)
   {
      final InspirationMapComponentData inspirationMapComponentData =
         new InspirationMapComponentData(inspirationMapComponentModel.getVisibleItems());
      final int visibleItems =
         inspirationMapComponentModel.getVisibleItems() != null ? inspirationMapComponentModel
            .getVisibleItems().intValue() : 0;
      final List<InspirationMapViewData> inspirationMapViewDataList =
         new ArrayList<InspirationMapViewData>();
      final List<InspirationMapTabModel> inspirationMapTabModelList =
         inspirationMapComponentModel.getTabs();

      for (final InspirationMapTabModel inspirationMapTabModel : inspirationMapTabModelList)
      {
         final List<LocationModel> locationModelList = inspirationMapTabModel.getLocations();
         final List<AccommodationModel> accomLocationList =
            inspirationMapTabModel.getAccomodations();

         InspirationMapViewData inspirationMapViewDataLocation = new InspirationMapViewData();
         InspirationMapViewData inspirationMapViewDataAccom = new InspirationMapViewData();
         if (CollectionUtils.isNotEmpty(accomLocationList))
         {
            inspirationMapViewDataAccom =
               inspirationMapConverterForAccom.convert(accomLocationList);
            inspirationMapPopulatorForAccom
               .populate(accomLocationList, inspirationMapViewDataAccom);
         }
         if (CollectionUtils.isNotEmpty(locationModelList))
         {
            inspirationMapViewDataLocation = inspirationMapConverter.convert(locationModelList);
            inspirationMapPopulator.populate(locationModelList, inspirationMapViewDataLocation);
         }

         final InspirationMapViewData inspirationMapViewData = new InspirationMapViewData();
         mergeInspirationMapData(inspirationMapViewDataLocation, inspirationMapViewDataAccom,
            inspirationMapViewData);

         // sorting logic
         final Map<String, MarkerMapViewData> markerMap = new HashMap<String, MarkerMapViewData>();
         final List<MarkerMapViewData> tempMarkerMap =
            inspirationMapViewData.getMarkerMapViewDataList();
         for (int i = 0; i < tempMarkerMap.size(); i++)
         {
            markerMap.put(tempMarkerMap.get(i).getCode(), tempMarkerMap.get(i));
         }
         List<MarkerMapViewData> inspirationMapOderedList = new ArrayList<MarkerMapViewData>();
         final Iterator<String> itr = inspirationMapTabModel.getLocationCodes().iterator();
         while (itr.hasNext())
         {
            String code = itr.next().toString();
            if (StringUtils.contains(code, "A-"))
            {
               code = code.substring(TWO);
            }
            if (markerMap.get(code) != null)
            {
               inspirationMapOderedList.add(markerMap.get(code));
            }
         }
         // end of sorting logic

         if (visibleItems < inspirationMapOderedList.size())
         {
            inspirationMapOderedList = inspirationMapOderedList.subList(0, visibleItems);
         }
         inspirationMapViewData.setMarkerMapViewDataList(inspirationMapOderedList);

         inspirationMapViewData.setTitle(inspirationMapTabModel.getTitle());
         if (inspirationMapTabModel.getPictureURL() != null)
         {
            inspirationMapViewData.setPictureUrl(inspirationMapTabModel.getPictureURL());
         }

         inspirationMapViewDataList.add(inspirationMapViewData);
      }
      inspirationMapComponentData.setInspirationMapViewDataList(inspirationMapViewDataList);
      return inspirationMapComponentData;
   }

   /**
    * @param inspirationMapViewDataLocation
    * @param inspirationMapViewDataAccom
    * @param inspirationMapViewData
    */
   private void mergeInspirationMapData(
      final InspirationMapViewData inspirationMapViewDataLocation,
      final InspirationMapViewData inspirationMapViewDataAccom,
      final InspirationMapViewData inspirationMapViewData)
   {
      if (CollectionUtils.isNotEmpty(inspirationMapViewDataAccom.getMarkerMapViewDataList())
         && CollectionUtils.isNotEmpty(inspirationMapViewDataLocation.getMarkerMapViewDataList()))
      {
         final List<MarkerMapViewData> inspirationMergedList =
            (List<MarkerMapViewData>) CollectionUtils.union(
               inspirationMapViewDataAccom.getMarkerMapViewDataList(),
               inspirationMapViewDataLocation.getMarkerMapViewDataList());
         inspirationMapViewData.setMarkerMapViewDataList(inspirationMergedList);
         inspirationMapViewData.setMarkupListCount(inspirationMergedList.size());
      }
      else
      {
         if (CollectionUtils.isNotEmpty(inspirationMapViewDataAccom.getMarkerMapViewDataList()))
         {
            inspirationMapViewData.setMarkerMapViewDataList(inspirationMapViewDataAccom
               .getMarkerMapViewDataList());
            inspirationMapViewData.setMarkupListCount(inspirationMapViewDataAccom
               .getMarkerMapViewDataList().size());
         }
         else if (CollectionUtils.isNotEmpty(inspirationMapViewDataLocation
            .getMarkerMapViewDataList()))
         {
            inspirationMapViewData.setMarkerMapViewDataList(inspirationMapViewDataLocation
               .getMarkerMapViewDataList());
            inspirationMapViewData.setMarkupListCount(inspirationMapViewDataLocation
               .getMarkerMapViewDataList().size());
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.MapFacade#getInteractiveMapForAccommodation(java.lang.String)
    */
   @Override
   public MapDataWrapper getInteractiveMapForAccommodation(final String accommodationCode)
   {
      final String key =
         keyGenerator.generate("getInteractiveMapForAccommodation", accommodationCode);
      final CacheWrapper<String, MapDataWrapper> mapDataCache =
         cacheUtil.getInteractiveMapCacheWrapper();
      // get from cache
      MapDataWrapper mapData = mapDataCache.get(key);
      if (mapData == null)
      {
         mapData = new MapDataWrapper();

         final AccommodationModel accommodationModel =
            accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode,
               cmsSiteService.getCurrentCatalogVersion(), null);

         accommodationInteractiveMapPopulator.populate(accommodationModel, mapData);
         mapData.setLocationType("accommodation");
         // put in cache
         mapDataCache.put(key, mapData);
      }

      return mapData;
   }

   @Override
   public MapDataWrapper getInteractiveMapForBookAccommodation(final String accommodationCode)
   {
      final String key =
         keyGenerator.generate("getInteractiveMapForBookAccommodation", accommodationCode);
      final CacheWrapper<String, MapDataWrapper> mapDataCache =
         cacheUtil.getInteractiveMapCacheWrapper();
      // get from cache
      MapDataWrapper mapData = mapDataCache.get(key);

      if (mapData == null)
      {
         mapData = getInteractiveMapForAccommodation(accommodationCode);
         String acccomCode = null;

         for (int i = 0; i < mapData.getHotels().size(); i++)
         {
            acccomCode = mapData.getHotels().get(i).getCode();
            if ((accommodationCode).equals(acccomCode))
            {
               AccommodationViewData temp;

               temp = mapData.getHotels().get(i);
               final List<AccommodationViewData> accom = new ArrayList<AccommodationViewData>();
               accom.add(temp);
               mapData.setHotels(accom);
               mapData.getExcursions().clear();
               break;
            }
         }

         for (int i = 0; i < mapData.getVillas().size(); i++)
         {
            acccomCode = mapData.getVillas().get(i).getCode();
            if ((accommodationCode).equals(acccomCode))
            {
               AccommodationViewData temp;

               temp = mapData.getVillas().get(i);
               final List<AccommodationViewData> accom = new ArrayList<AccommodationViewData>();
               accom.add(temp);
               mapData.setVillas(accom);
               mapData.getExcursions().clear();
               break;
            }
         }
         // put in cache
         mapDataCache.put(key, mapData);
      }

      return mapData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.MapFacade#getInteractiveMapForLocations(java.lang.String)
    */
   @Override
   public MapDataWrapper getInteractiveMapForLocations(final String locationCode)
   {
      final String key = keyGenerator.generate("getInteractiveMapForLocations", locationCode);
      final CacheWrapper<String, MapDataWrapper> mapDataCache =
         cacheUtil.getInteractiveMapCacheWrapper();
      // get from cache
      MapDataWrapper mapData = mapDataCache.get(key);

      if (mapData == null)
      {
         mapData = new MapDataWrapper();
         final CategoryModel category = categoryService.getCategoryForCode(locationCode);
         if (category instanceof LocationModel)
         {
            final LocationModel locationModel = (LocationModel) category;
            final LocationData locationData = locationConverter.convert(locationModel);
            defaultLocationConfiguredPopulator.populate(locationModel, locationData,
               Arrays.asList(LocationOption.SUBCATEGORIES));

            // What is make the location subcategoires populator generic so that it can be reused
            // when sublocations are
            // needed for a location.
            // I then get the list of sublocations and attach it to the Map data to be sent back to
            // view.
            if (locationData.getSubLocations() != null
               && !Collections.isEmpty(locationData.getSubLocations()))
            {
               mapData.setLocations(locationData.getSubLocations());
            }
            mapData.setTopLocationName(locationData.getName());
            mapData.setSuperCategoryNames(locationData.getSuperCategoryNames());
            mapData.setLocationType(locationModel.getType().getCode().toLowerCase());
         }
         // put in cache
         mapDataCache.put(key, mapData);
      }

      return mapData;
   }

   /**
    *
    */
   @Override
   public List<LocationData> getSubLocations(final String categoryCode)
   {
      final List<LocationData> cruiseCategoriesList = new ArrayList<LocationData>();
      for (final CategoryModel cruiseCategory : categoryService.getCategoryForCode(categoryCode)
         .getCategories())
      {
         final LocationData locationData =
            locationConverter.convert((LocationModel) cruiseCategory);
         defaultLocationConfiguredPopulator.populate((LocationModel) cruiseCategory, locationData,
            Arrays.asList(LocationOption.SUBCATEGORIES));
         for (final LocationData locationDatas : locationData.getSubLocations())
         {
            if (BrandUtils.isValidBrandCode(
               categoryService.getCategoryForCode(locationDatas.getCode()).getBrands(),
               BrandType.CR.toString()))
            {
               cruiseCategoriesList.add(locationDatas);
            }
         }
      }
      return cruiseCategoriesList;
   }

   /**
    *
    */
   @Override
   public List<LocationData> checkIsPort(final MapDataWrapper mapDataWrapper)
   {
      final List<LocationData> portLocation = mapDataWrapper.getLocations();
      final List<LocationData> locationWithNoPort = new ArrayList<LocationData>();
      for (final LocationData location : portLocation)
      {
         final LocationModel locationModel =
            (LocationModel) categoryService.getCategoryForCode(location.getCode());
         if (!locationModel.getType().equals(LocationType.PORT))
         {
            locationWithNoPort.add(location);
         }
      }
      return locationWithNoPort;
   }

   @Override
   public boolean getLocationUrl(final String categoryCode)
   {
      final LocationModel locationModel =
         (LocationModel) categoryService.getCategoryForCode(categoryCode);
      return locationModel.getIsPOC().booleanValue();
   }

}
