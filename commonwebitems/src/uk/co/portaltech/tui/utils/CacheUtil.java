/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.tui.services.pojo.GlobalNavigation;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MostPopularDestinationdata;
import uk.co.portaltech.tui.web.view.data.SearchPanelCollectionViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.tui.book.domain.lite.BasePackage;

/**
 * @author ext
 *
 */
public class CacheUtil
{
   // location caches
   private CacheWrapper<String, LocationData> locationDataCacheWrapper;

   private CacheWrapper<String, List<LocationData>> locationListCacheWrapper;

   // JSP fragment cache
   private CacheWrapper<String, String> fragmentCacheWrapper;

   private CacheWrapper<String, AccommodationViewData> accommodationDataCacheWrapper;

   private CacheWrapper<String, List<AccommodationViewData>> accommodationListCacheWrapper;

   private CacheWrapper<String, DestinationData> destGuideLocationsCacheWrapper;

   private CacheWrapper<String, MapDataWrapper> interactiveMapCacheWrapper;

   private CacheWrapper<String, SearchResultRoomsData> searchResultsRoomsDetailsCacheWrapper;

   private CacheWrapper<String, Map<String, List<Object>>> accomFeaturesInSearchCacheWrapper;

   private CacheWrapper<String, String> accomImageUrlInSearchCacheWrapper;

   private CacheWrapper<String, String> itinearyImageUrlCacheWrapper;

   private CacheWrapper<String, Map<String, List<String>>> accomImageUrlMapInSearchCacheWrapper;

   private CacheWrapper<String, GlobalNavigationComponentViewData> globalNavigationComponentViewDataCacheWrapper;

   private CacheWrapper<String, GlobalNavigation> globalNavigationCacheWrapper;

   private CacheWrapper<String, List<DestinationData>> continentAndCountryHierarchyCacheWrapper;

   private CacheWrapper<String, SearchPanelCollectionViewData> destinationGuideCollectionDataCacheWrapper;

   private CacheWrapper<String, List<MostPopularDestinationdata>> mostPopularDestinationdataCacheWrapper;

   private CacheWrapper<String, DestinationData> regionDestinationDataCacheWrapper;

   private CacheWrapper<String, Map<String, BasePackage>> addShortlistCacheWrapper;

   private CacheWrapper<String, Map<String, BasePackage>> existShortlistCacheWrapper;

   private CacheWrapper<String, Set<String>> removeShortlistCacheWrapper;

   /**
    * @return the removeShortlistCacheWrapper
    */
   public CacheWrapper<String, Set<String>> getRemoveShortlistCacheWrapper()
   {
      return removeShortlistCacheWrapper;
   }

   /**
    * @param removeShortlistCacheWrapper the removeShortlistCacheWrapper to set
    */
   public void setRemoveShortlistCacheWrapper(
      final CacheWrapper<String, Set<String>> removeShortlistCacheWrapper)
   {
      this.removeShortlistCacheWrapper = removeShortlistCacheWrapper;
   }

   /**
    * @return the regionDestinationDataCacheWrapper
    */
   public final CacheWrapper<String, DestinationData> getRegionDestinationDataCacheWrapper()
   {
      return regionDestinationDataCacheWrapper;
   }

   /**
    * @param regionDestinationDataCacheWrapper the regionDestinationDataCacheWrapper to set
    */
   public final void setRegionDestinationDataCacheWrapper(
      final CacheWrapper<String, DestinationData> regionDestinationDataCacheWrapper)
   {
      this.regionDestinationDataCacheWrapper = regionDestinationDataCacheWrapper;
   }

   /**
    * @return the mostPopularDestinationdataCacheWrapper
    */
   public final CacheWrapper<String, List<MostPopularDestinationdata>> getMostPopularDestinationdataCacheWrapper()
   {
      return mostPopularDestinationdataCacheWrapper;
   }

   /**
    * @param mostPopularDestinationdataCacheWrapper the mostPopularDestinationdataCacheWrapper to
    *           set
    */
   public final void setMostPopularDestinationdataCacheWrapper(
      final CacheWrapper<String, List<MostPopularDestinationdata>> mostPopularDestinationdataCacheWrapper)
   {
      this.mostPopularDestinationdataCacheWrapper = mostPopularDestinationdataCacheWrapper;
   }

   /**
    * @return the locationDataCacheWrapper
    */
   public CacheWrapper<String, LocationData> getLocationDataCacheWrapper()
   {
      return locationDataCacheWrapper;
   }

   /**
    * @return the continentAndCountryHierarchyCacheWrapper
    */
   public CacheWrapper<String, List<DestinationData>> getContinentAndCountryHierarchyCacheWrapper()
   {
      return continentAndCountryHierarchyCacheWrapper;
   }

   /**
    * @return the destinationGuideCollectionDataCacheWrapper
    */
   public CacheWrapper<String, SearchPanelCollectionViewData> getDestinationGuideCollectionDataCacheWrapper()
   {
      return destinationGuideCollectionDataCacheWrapper;
   }

   /**
    * @param destinationGuideCollectionDataCacheWrapper the
    *           destinationGuideCollectionDataCacheWrapper to set
    */
   public void setDestinationGuideCollectionDataCacheWrapper(
      final CacheWrapper<String, SearchPanelCollectionViewData> destinationGuideCollectionDataCacheWrapper)
   {
      this.destinationGuideCollectionDataCacheWrapper = destinationGuideCollectionDataCacheWrapper;
   }

   /**
    * @param continentAndCountryHierarchyCacheWrapper the continentAndCountryHierarchyCacheWrapper
    *           to set
    */
   public void setContinentAndCountryHierarchyCacheWrapper(
      final CacheWrapper<String, List<DestinationData>> continentAndCountryHierarchyCacheWrapper)
   {
      this.continentAndCountryHierarchyCacheWrapper = continentAndCountryHierarchyCacheWrapper;
   }

   /**
    * @param locationDataCacheWrapper the locationDataCacheWrapper to set
    */
   public void setLocationDataCacheWrapper(
      final CacheWrapper<String, LocationData> locationDataCacheWrapper)
   {
      this.locationDataCacheWrapper = locationDataCacheWrapper;
   }

   /**
    * @return the locationListCacheWrapper
    */
   public CacheWrapper<String, List<LocationData>> getLocationListCacheWrapper()
   {
      return locationListCacheWrapper;
   }

   /**
    * @param locationListCacheWrapper the locationListCacheWrapper to set
    */
   public void setLocationListCacheWrapper(
      final CacheWrapper<String, List<LocationData>> locationListCacheWrapper)
   {
      this.locationListCacheWrapper = locationListCacheWrapper;
   }

   /**
    * @return the fragmentCacheWrapper
    */
   public CacheWrapper<String, String> getFragmentCacheWrapper()
   {
      return fragmentCacheWrapper;
   }

   /**
    * @param fragmentCacheWrapper the fragmentCacheWrappe to set
    */
   public void setFragmentCacheWrapper(final CacheWrapper<String, String> fragmentCacheWrapper)
   {
      this.fragmentCacheWrapper = fragmentCacheWrapper;
   }

   /**
    * @return the accommodationDataCacheWrapper
    */
   public CacheWrapper<String, AccommodationViewData> getAccommodationDataCacheWrapper()
   {
      return accommodationDataCacheWrapper;
   }

   /**
    * @param accommodationDataCacheWrapper the accommodationDataCacheWrapper to set
    */
   public void setAccommodationDataCacheWrapper(
      final CacheWrapper<String, AccommodationViewData> accommodationDataCacheWrapper)
   {
      this.accommodationDataCacheWrapper = accommodationDataCacheWrapper;
   }

   /**
    * @return the accommodationListCacheWrapper
    */
   public CacheWrapper<String, List<AccommodationViewData>> getAccommodationListCacheWrapper()
   {
      return accommodationListCacheWrapper;
   }

   /**
    * @param accommodationListCacheWrapper the accommodationListCacheWrapper to set
    */
   public void setAccommodationListCacheWrapper(
      final CacheWrapper<String, List<AccommodationViewData>> accommodationListCacheWrapper)
   {
      this.accommodationListCacheWrapper = accommodationListCacheWrapper;
   }

   /**
    * @return the defaultDestGuideLocationsCacheWrapper
    */
   public CacheWrapper<String, DestinationData> getDestGuideLocationsCacheWrapper()
   {
      return destGuideLocationsCacheWrapper;
   }

   /**
    * @param destGuideLocationsCacheWrapper the defaultDestGuideLocationsCacheWrapper to set
    */
   public void setDestGuideLocationsCacheWrapper(
      final CacheWrapper<String, DestinationData> destGuideLocationsCacheWrapper)
   {
      this.destGuideLocationsCacheWrapper = destGuideLocationsCacheWrapper;
   }

   /**
    * @return the searchResultsRoomsDetailsCache
    */
   public CacheWrapper<String, SearchResultRoomsData> getSearchResultsRoomsDetailsCacheWrapper()
   {
      return searchResultsRoomsDetailsCacheWrapper;
   }

   /**
    * @param searchResultsRoomsDetailsCacheWrapper the searchResultsRoomsDetailsCache to set
    */
   public void setSearchResultsRoomsDetailsCacheWrapper(
      final CacheWrapper<String, SearchResultRoomsData> searchResultsRoomsDetailsCacheWrapper)
   {
      this.searchResultsRoomsDetailsCacheWrapper = searchResultsRoomsDetailsCacheWrapper;
   }

   /**
    * @return the accomFeaturesInSearchCacheWrapper
    */
   public CacheWrapper<String, Map<String, List<Object>>> getAccomFeaturesInSearchCacheWrapper()
   {
      return accomFeaturesInSearchCacheWrapper;
   }

   /**
    * @param accomFeaturesInSearchCacheWrapper the accomFeaturesInSearchCacheWrapper to set
    */
   public void setAccomFeaturesInSearchCacheWrapper(
      final CacheWrapper<String, Map<String, List<Object>>> accomFeaturesInSearchCacheWrapper)
   {
      this.accomFeaturesInSearchCacheWrapper = accomFeaturesInSearchCacheWrapper;
   }

   /**
    * @return the accomImageUrlInSearchCacheWrapper
    */
   public CacheWrapper<String, String> getAccomImageUrlInSearchCacheWrapper()
   {
      return accomImageUrlInSearchCacheWrapper;
   }

   /**
    * @param accomImageUrlInSearchCacheWrapper the accomImageUrlInSearchCacheWrapper to set
    */
   public void setAccomImageUrlInSearchCacheWrapper(
      final CacheWrapper<String, String> accomImageUrlInSearchCacheWrapper)
   {
      this.accomImageUrlInSearchCacheWrapper = accomImageUrlInSearchCacheWrapper;
   }

   /**
    * @return the accomImageUrlMapInSearchCacheWrapper
    */
   public CacheWrapper<String, Map<String, List<String>>> getAccomImageUrlMapInSearchCacheWrapper()
   {
      return accomImageUrlMapInSearchCacheWrapper;
   }

   /**
    * @param accomImageUrlMapInSearchCacheWrapper the accomImageUrlMapInSearchCacheWrapper to set
    */
   public void setAccomImageUrlMapInSearchCacheWrapper(
      final CacheWrapper<String, Map<String, List<String>>> accomImageUrlMapInSearchCacheWrapper)
   {
      this.accomImageUrlMapInSearchCacheWrapper = accomImageUrlMapInSearchCacheWrapper;
   }

   /**
    * @return the interactiveMapCacheWrapper
    */
   public CacheWrapper<String, MapDataWrapper> getInteractiveMapCacheWrapper()
   {
      return interactiveMapCacheWrapper;
   }

   /**
    * @param interactiveMapCacheWrapper the interactiveMapCacheWrapper to set
    */
   public void setInteractiveMapCacheWrapper(
      final CacheWrapper<String, MapDataWrapper> interactiveMapCacheWrapper)
   {
      this.interactiveMapCacheWrapper = interactiveMapCacheWrapper;
   }

   /**
    * @return the addShortlistCacheWrapper
    */
   public CacheWrapper<String, Map<String, BasePackage>> getAddShortlistCacheWrapper()
   {
      return addShortlistCacheWrapper;
   }

   /**
    * @param addShortlistCacheWrapper the addShortlistCacheWrapper to set
    */
   public void setAddShortlistCacheWrapper(
      final CacheWrapper<String, Map<String, BasePackage>> addShortlistCacheWrapper)
   {
      this.addShortlistCacheWrapper = addShortlistCacheWrapper;
   }

   /**
    * @return the existShortlistCacheWrapper
    */
   public CacheWrapper<String, Map<String, BasePackage>> getExistShortlistCacheWrapper()
   {
      return existShortlistCacheWrapper;
   }

   /**
    * @param existShortlistCacheWrapper the existShortlistCacheWrapper to set
    */
   public void setExistShortlistCacheWrapper(
      final CacheWrapper<String, Map<String, BasePackage>> existShortlistCacheWrapper)
   {
      this.existShortlistCacheWrapper = existShortlistCacheWrapper;
   }

   /**
    * @return the itinearyImageUrlCacheWrapper
    */
   public CacheWrapper<String, String> getItinearyImageUrlCacheWrapper()
   {
      return itinearyImageUrlCacheWrapper;
   }

   /**
    * @param itinearyImageUrlCacheWrapper the itinearyImageUrlCacheWrapper to set
    */
   public void setItinearyImageUrlCacheWrapper(
      final CacheWrapper<String, String> itinearyImageUrlCacheWrapper)
   {
      this.itinearyImageUrlCacheWrapper = itinearyImageUrlCacheWrapper;
   }

   /**
    * @return the globalNavigationComponentViewDataCacheWrapper
    */
   public CacheWrapper<String, GlobalNavigationComponentViewData> getGlobalNavigationComponentViewDataCacheWrapper()
   {
      return globalNavigationComponentViewDataCacheWrapper;
   }

   /**
    * @param globalNavigationComponentViewDataCacheWrapper the
    *           globalNavigationComponentViewDataCacheWrapper to set
    */
   public void setGlobalNavigationComponentViewDataCacheWrapper(
      final CacheWrapper<String, GlobalNavigationComponentViewData> globalNavigationComponentViewDataCacheWrapper)
   {
      this.globalNavigationComponentViewDataCacheWrapper =
         globalNavigationComponentViewDataCacheWrapper;
   }

   /**
    * @return the globalNavigationCacheWrapper
    */
   public CacheWrapper<String, GlobalNavigation> getGlobalNavigationCacheWrapper()
   {
      return globalNavigationCacheWrapper;
   }

   /**
    * @param globalNavigationCacheWrapper the globalNavigationCacheWrapper to set
    */
   public void setGlobalNavigationCacheWrapper(
      final CacheWrapper<String, GlobalNavigation> globalNavigationCacheWrapper)
   {
      this.globalNavigationCacheWrapper = globalNavigationCacheWrapper;
   }
}