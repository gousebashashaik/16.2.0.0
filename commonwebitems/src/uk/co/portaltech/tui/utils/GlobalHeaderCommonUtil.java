/**
 *
 */
package uk.co.portaltech.tui.utils;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static uk.co.portaltech.tui.constants.GlobalNavigationConstant.CRUISES;
import static uk.co.portaltech.tui.constants.GlobalNavigationConstant.DESTINATIONS;
import static uk.co.portaltech.tui.constants.GlobalNavigationConstant.HOLIDAYS;

import de.hybris.platform.category.model.CategoryModel;

import java.util.HashMap;
import java.util.Map;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;

/**
 * @author EXTLP1
 *
 */
public final class GlobalHeaderCommonUtil
{

   private static final Map<String, Map> SITE_ACTIVE_TABS = new HashMap<String, Map>();
   static
   {
      final Map<String, String> thMap = new CustomMap();
      thMap.put(".*country.*", DESTINATIONS);
      thMap.put(".*destination.*", DESTINATIONS);
      thMap.put(".*resort.*", DESTINATIONS);
      thMap.put(".*location.*", DESTINATIONS);
      thMap.put(".*accommodation.*", DESTINATIONS);
      thMap.put(".*book.*", HOLIDAYS);
      thMap.put(".*search.*", HOLIDAYS);
      thMap.put(".*home.*", HOLIDAYS);
      final Map<String, String> crMap = new CustomMap();
      crMap.put(".*", CRUISES);
      SITE_ACTIVE_TABS.put(BrandType.TH.toString(), thMap);
      SITE_ACTIVE_TABS.put(BrandType.CR.toString(), crMap);
      SITE_ACTIVE_TABS.put(BrandType.FC.toString(), thMap);

   }

   private GlobalHeaderCommonUtil()
   {

   }

   public static String getParentCountryName(final LocationModel locationModel)
   {
      return isStayCountry(locationModel) ? locationModel.getName() : getCountryName(locationModel);
   }

   private static String getCountryName(final LocationModel locationModel)
   {
      for (final CategoryModel model : locationModel.getAllSupercategories())
      {
         if (model instanceof LocationModel)
         {
            final LocationModel location = (LocationModel) model;
            if (isStayCountry(location))
            {
               return location.getName();
            }
         }
      }
      return EMPTY;
   }

   private static boolean isStayCountry(final LocationModel locationModel)
   {
      return locationModel.getType().equals(LocationType.COUNTRY);
   }

   public static String getActiveTab(final String pageId, final String brand)
   {

      return (String) SITE_ACTIVE_TABS.get(brand).get(pageId);
   }

}
