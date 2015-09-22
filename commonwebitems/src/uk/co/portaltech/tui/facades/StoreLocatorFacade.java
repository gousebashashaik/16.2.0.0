/**
 *
 */
package uk.co.portaltech.tui.facades;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.location.DistanceAwareLocation;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.StoreLocatorData;

/**
 * @author akhileshvarma.d
 *
 */
public interface StoreLocatorFacade
{
   List<StoreLocatorData> prepareDataForMobile(List<DistanceAwareLocation> locations, GPS refGps);

   List<StoreLocatorData> getLocationsNearBy(AddressData address);

   List<StoreLocatorData> getLocationsNearBy(String latitude, String longitude);
}
