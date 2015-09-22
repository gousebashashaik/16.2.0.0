/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.location.DistanceAwareLocation;
import de.hybris.platform.storelocator.location.LocationService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.ShopTimingsModel;
import uk.co.portaltech.tui.facades.StoreLocatorFacade;
import uk.co.portaltech.tui.web.view.data.StoreLocatorData;

/**
 * @author akhileshvarma.d
 *
 */
public class DefaultStoreLocatorFacade implements StoreLocatorFacade
{
   @Resource
   private FlexibleSearchService flexibleSearchService;

   @Resource(name = "tuiGeoServiceWrapper")
   private GeoWebServiceWrapper geoServiceWrapper;

   @Resource(name = "defaultPOSService")
   private LocationService locationService;

   @Resource
   private PointOfServiceDao pointOfServiceDao;

   private static final String SHOPTIMINGS_QUERY =
      "SELECT {pk} FROM {ShopTimings} WHERE  {openingSchedule}=?openingSchedule";

   private static final String OPENINGSCHEDULE = "openingSchedule";

   private static final double KMTOMILE = 0.621;

   private static final int FIFTY = 50;

   @Override
   public List<StoreLocatorData> getLocationsNearBy(final AddressData address)
   {

      final GPS gps = geoServiceWrapper.geocodeAddress(address);

      final List<DistanceAwareLocation> locations =
         locationService.getSortedLocationsNearby(gps, FIFTY, null);

      return prepareDataForMobile(locations, gps);

   }

   @Override
   public List<StoreLocatorData> getLocationsNearBy(final String latitude, final String longitude)
   {

      final GPS gps =
         new DefaultGPS().create(Double.parseDouble(latitude), Double.parseDouble(longitude));

      final List<DistanceAwareLocation> locations =
         locationService.getSortedLocationsNearby(gps, FIFTY, null);

      return prepareDataForMobile(locations, gps);

   }

   /**
    * @param locations
    */
   @Override
   public List<StoreLocatorData> prepareDataForMobile(final List<DistanceAwareLocation> locations,
      final GPS refGps)
   {

      final List<StoreLocatorData> storeData = new ArrayList();
      for (final DistanceAwareLocation location : locations)
      {
         final Map<String, String> openingTimes = new HashMap<String, String>();
         final StoreLocatorData data = new StoreLocatorData();
         final PointOfServiceModel pos = this.pointOfServiceDao.getPosByName(location.getName());
         data.setName(pos.getName());
         data.setBuilding(pos.getAddress().getStreetnumber());
         data.setStreet(pos.getAddress().getStreetname());
         data.setCity(pos.getAddress().getCounty());
         data.setEmail(pos.getAddress().getEmail());
         data.setPhone(pos.getAddress().getPhone1());
         data.setZip(pos.getAddress().getPostalcode());
         data.setMapIconUrl(location.getMapIconUrl());
         data.setCountryCode(location.getCountry());
         data.setTown(pos.getAddress().getTown());
         data.setDistance(location.getDistance().doubleValue() * KMTOMILE);
         data.setLatitude(pos.getLatitude().toString());
         data.setLongitude(pos.getLongitude().toString());
         data.setTown(pos.getAddress().getTown());
         if (pos.getOpeningSchedule() != null)
         {
            final List<ShopTimingsModel> timingsList = new ArrayList<ShopTimingsModel>();
            final FlexibleSearchQuery getShopTimings = new FlexibleSearchQuery(SHOPTIMINGS_QUERY);
            getShopTimings.addQueryParameter(OPENINGSCHEDULE, pos.getOpeningSchedule().getPk());
            final SearchResult<ShopTimingsModel> result =
               flexibleSearchService.search(getShopTimings);
            timingsList.addAll(result.getResult());
            for (final ShopTimingsModel timing : timingsList)
            {
               openingTimes.put(timing.getDayOfWeek().toString(), timing.getTimings());
            }
            data.setTimings(openingTimes);
         }
         storeData.add(data);
      }

      return storeData;
   }

}
