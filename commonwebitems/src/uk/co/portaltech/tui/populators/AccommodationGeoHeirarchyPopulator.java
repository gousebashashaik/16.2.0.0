/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.fest.util.Collections;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.TUICategoryData;

/**
 * @author Kandipr
 *
 */
public class AccommodationGeoHeirarchyPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   @Resource
   LocationService tuiLocationService;

   static
   {
      final Map<String, String> levelMap = new HashMap<String, String>();
      levelMap.put(LocationType.COUNTRY.toString(), "1");
      levelMap.put(LocationType.REGION.toString(), "2");
      levelMap.put(LocationType.DESTINATION.toString(), "3");
      levelMap.put(LocationType.RESORT.toString(), "4");
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final AccommodationModel source, final AccommodationViewData target)
      throws ConversionException
   {

      final Collection<CategoryModel> superCategories = source.getSupercategories();
      List<LocationModel> locations = null;

      for (final CategoryModel category : superCategories)
      {
         if (category instanceof LocationModel)
         {
            locations =
               tuiLocationService.getAllLocationsToHierarchyPoint((LocationModel) category,
                  LocationType.COUNTRY);
         }
      }

      if (!Collections.isEmpty(locations))
      {
         final TUICategoryData locationData = new TUICategoryData();
         final List<TUICategoryData> geoHierarchy = new ArrayList<TUICategoryData>();
         for (final LocationModel location : locations)
         {

            locationData.setCode(location.getCode());
            locationData.setName(location.getName());

            geoHierarchy.add(locationData);

         }

      }
   }
}
