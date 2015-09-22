/**
 *
 */
package uk.co.portaltech.tui.impex.util;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * This class handles the front office geography data csv, supporting the impex job
 * tui_geography.impex.
 *
 * @author James Johnstone
 *
 */
public class GeographyImportContext
{

   private static final TUILogUtils LOG = new TUILogUtils("GeographyImportContext");

   final CategoryService categoryService = Registry.getApplicationContext().getBean(
      CategoryService.class);

   // for avoiding Magic Number
   private static final int NUMBER_THREE = 3;

   private static final int NUMBER_FIVE = 5;

   private static final int NUMBER_SIX = 6;

   private static final int NUMBER_EIGHT = 8;

   private static final int NUMBER_NINE = 9;

   private static final int NUMBER_ONEONE = 11;

   private static final int NUMBER_ONETWO = 12;

   private static final int NUMBER_ONEFOUR = 14;

   private static final int NUMBER_ONEFIVE = 15;

   private static final int NUMBER_THREETHREEZERO = 330;

   private static final int NUMBER_FOURZEROZERO = 400;

   private static final int NUMBER_FOURONEZERO = 410;

   private static final int NUMBER_FOURTWOZERO = 420;

   private static final int NUMBER_SIXHUNDRED = 600;

   private static final int NUMBER_SIXHUNDREDANDTEN = 610;

   private static final int NUMBER_SIXHUNDREDANDTWENTY = 620;

   private static final int NUMBER_SIXHUNDREDANDTHIRTY = 630;

   private static final int NUMBER_SEVENHUNDRED = 700;

   private static final int NUMBER_ONE = 1;

   public void prepareContinents(final Map<Integer, String> line, final String brand)
   {
      line.put(Integer.valueOf(NUMBER_FOURZEROZERO), line.get(Integer.valueOf(NUMBER_ONE))
         .toLowerCase());
      line.put(Integer.valueOf(NUMBER_SIXHUNDRED),
         getBrand(line.get(Integer.valueOf(NUMBER_THREE)), brand));

      LOG.debug("Adding continent with code " + line.get(Integer.valueOf(NUMBER_ONE)).toLowerCase());
   }

   public void prepareCountries(final Map<Integer, String> line, final String brand)
   {
      line.put(Integer.valueOf(NUMBER_FOURZEROZERO),
         "countries," + line.get(Integer.valueOf(NUMBER_ONE)).toLowerCase());
      line.put(Integer.valueOf(NUMBER_SIXHUNDRED),
         getBrand(line.get(Integer.valueOf(NUMBER_THREE)), brand));
      LOG.debug("Adding country with code " + line.get(Integer.valueOf(NUMBER_ONE)).toLowerCase());
   }

   public void prepareRegions(final Map<Integer, String> line, final ImpExImportReader reader,
      final String brand)
   {
      if (StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_FIVE)))
         || StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_SIX))))
      {
         reader.discardNextLine();
      }
      else
      {
         final StringBuilder superCategories =
            new StringBuilder("regions," + line.get(Integer.valueOf(NUMBER_THREE)));
         // Region level assigning of Inventory Group items:
         if (!StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_ONEFIVE))))
         {
            superCategories.append("," + line.get(Integer.valueOf(NUMBER_ONEFIVE)));
         }

         line.put(Integer.valueOf(NUMBER_SIXHUNDREDANDTEN),
            getBrand(line.get(Integer.valueOf(NUMBER_THREE)), brand));
         LOG.debug("Adding region " + line.get(Integer.valueOf(NUMBER_FIVE))
            + " with supercategories " + superCategories);

         line.put(Integer.valueOf(NUMBER_FOURONEZERO), superCategories.toString());
         LOG.debug("Adding region " + line.get(Integer.valueOf(NUMBER_FIVE))
            + " with supercategories " + superCategories);

      }
   }

   public void prepareDestinations(final Map<Integer, String> line, final ImpExImportReader reader,
      final String brand)
   {
      // Check that the destination code or name are not blank. If they are,
      // ignore this line.
      if (StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_EIGHT)))
         || StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_NINE))))
      {
         reader.discardNextLine();
      }
      // If the region is blank:
      else
      {
         final StringBuilder superCategories = new StringBuilder("destinations");
         if (StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_SIX))))
         {
            superCategories.append("," + line.get(Integer.valueOf(NUMBER_THREE)));

         }
         else
         {
            superCategories.append("," + line.get(Integer.valueOf(NUMBER_SIX)));
         }
         LOG.debug("Adding destination " + line.get(Integer.valueOf(NUMBER_EIGHT))
            + " with supercategories " + superCategories);
         line.put(Integer.valueOf(NUMBER_FOURTWOZERO), superCategories.toString());
         line.put(Integer.valueOf(NUMBER_SIXHUNDREDANDTWENTY),
            getBrand(line.get(Integer.valueOf(NUMBER_THREE)), brand));
      }
   }

   public void prepareResorts(final Map<Integer, String> line, final ImpExImportReader reader,
      final String brand)
   {
      if (StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_ONEONE)))
         || StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_ONETWO))))
      {
         reader.discardNextLine();
      }
      else
      {
         final StringBuilder superCategories = new StringBuilder("resorts");
         if (StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_NINE))))
         {
            if (StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_SIX))))
            {
               // If the destination and the region are both blank:
               superCategories.append("," + line.get(Integer.valueOf(NUMBER_THREE)));
            }
            else
            {
               // If just the destination is blank:
               superCategories.append("," + line.get(Integer.valueOf(NUMBER_SIX)));
            }
         }
         else
         {
            if (line.get(Integer.valueOf(NUMBER_NINE)).equals(
               line.get(Integer.valueOf(NUMBER_ONEONE))))
            {
               LOG.warn("Destination " + line.get(Integer.valueOf(NUMBER_NINE))
                  + " has the same code as Region " + line.get(Integer.valueOf(NUMBER_SIX))
                  + ". Ignoring this resort.");
               reader.discardNextLine();
            }
            else
            {
               superCategories.append("," + line.get(Integer.valueOf(NUMBER_NINE)));

            }
         }
         LOG.debug("Adding resort " + line.get(Integer.valueOf(NUMBER_ONETWO))
            + " with supercategories '" + superCategories + "'");
         line.put(Integer.valueOf(NUMBER_THREETHREEZERO), superCategories.toString());
         line.put(Integer.valueOf(NUMBER_SIXHUNDREDANDTHIRTY),
            getBrand(line.get(Integer.valueOf(NUMBER_THREE)), brand));
      }
   }

   public void prepareInventoryGroups(final Map<Integer, String> line,
      final ImpExImportReader reader, final String brand)
   {
      if (StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_ONEFIVE)))
         || StringUtils.isBlank(line.get(Integer.valueOf(NUMBER_ONEFOUR))))
      {
         reader.discardNextLine();
      }
      line.put(Integer.valueOf(NUMBER_SEVENHUNDRED),
         getBrand(line.get(Integer.valueOf(NUMBER_THREE)), brand));
   }

   private CatalogVersionModel getActiveCatalogVersion()
   {
      final ConfigurationService configService =
         Registry.getApplicationContext().getBean(ConfigurationService.class);
      final CatalogVersionService catalogVersionService =
         Registry.getApplicationContext().getBean(CatalogVersionService.class);
      final String catalog = configService.getConfiguration().getString("import_catalog");
      final String catalogVersion = configService.getConfiguration().getString("active_catalog");
      return catalogVersionService.getCatalogVersion(catalog, catalogVersion);
   }

   private String getBrand(final String locationCode, final String brand)
   {
      CategoryModel category = null;

      category = categoryService.getCategoryForCode(getActiveCatalogVersion(), locationCode);

      // Start : for brandtype re factor
      String locationBrand = "TH,FC";
      if (category instanceof LocationModel)
      {
         final LocationModel location = (LocationModel) category;

         if (location.getBrands().isEmpty() || location.getBrands().contains(brand))
         {
            locationBrand = brand;
         }
      }
      // end : for brandtype re factor

      return locationBrand;

   }

}
