/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.tui.components.model.InspirationMapComponentModel;
import uk.co.portaltech.tui.web.view.data.InspirationMapComponentData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;

/**
 * @author s.consolino
 * 
 */
public interface MapFacade
{

   /**
    * @param componentPk
    * @return
    */
   InspirationMapComponentData getInspirationMap(
      InspirationMapComponentModel inspirationMapComponentModel);

   /**
    * Retries data for interactive map for accommodations page
    * 
    * @param accommodationCode The accommodation Code
    * @return MapData object with a list of accommodations in the same resort as the accommodation
    *         in question and a list of attractions that are in the resort the accommodation is in.
    */
   MapDataWrapper getInteractiveMapForAccommodation(String accommodationCode);

   /**
    * Retrieves data for interactive map for locations
    * 
    * @param locationCode The location code
    * @return
    * 
    */
   MapDataWrapper getInteractiveMapForLocations(String locationCode);

   /**
    * 
    * Retrieves data for interactive map for locations
    * 
    * @param accommodationCode
    * @return
    */
   MapDataWrapper getInteractiveMapForBookAccommodation(String accommodationCode);

   /**
    * @param categoryCode
    * @param mapDataWrapper
    */
   List<LocationData> getSubLocations(String categoryCode);

   /**
    * @param mapDataWrapper
    */
   List<LocationData> checkIsPort(MapDataWrapper mapDataWrapper);

   /**
    * @param categoryCode
    * @return
    */
   boolean getLocationUrl(String categoryCode);
}
