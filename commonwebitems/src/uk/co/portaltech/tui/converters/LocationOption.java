/**
 *
 */
package uk.co.portaltech.tui.converters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author omonikhide
 *
 */
public class LocationOption
{

   public static final LocationOption BASIC = new LocationOption("BASIC");

   public static final LocationOption GALLERY = new LocationOption("GALLERY");

   public static final LocationOption KEY_FACTS = new LocationOption("PRICE");

   public static final LocationOption EDITORIAL = new LocationOption("EDITORIAL");

   public static final LocationOption HEALTHANDSAFETY = new LocationOption("HEALTHANDSAFETY");

   public static final LocationOption PASSPORTANDVISA = new LocationOption("PASSPORTANDVISA");

   public static final LocationOption WHERE_TO_GO = new LocationOption("WHERE_TO_GO");

   public static final LocationOption WHENTOGO = new LocationOption("WHENTOGO");

   public static final LocationOption GETTINGAROUND = new LocationOption("GETTINGAROUND");

   public static final LocationOption THUMBNAILMAP = new LocationOption("THUMBNAILMAP");

   public static final LocationOption CONTENT = new LocationOption("CONTENT");

   public static final LocationOption HIGHLIGHTS = new LocationOption("HIGHLIGHTS");

   public static final LocationOption SUBCATEGORIES = new LocationOption("SUBCATEGORIES");

   public static final LocationOption THINGSTOSEEANDDO = new LocationOption("THINGSTOSEEANDDO");

   // Added for Hybris Content Services
   public static final LocationOption WEATHER = new LocationOption("WEATHER");

   public static final LocationOption FULLDATA = new LocationOption("FULLDATA");

   private static List<LocationOption> internalValues = Arrays.asList(BASIC, GALLERY, KEY_FACTS,
      EDITORIAL, WHERE_TO_GO, CONTENT, HIGHLIGHTS);

   private final String code;

   protected LocationOption(final String code)
   {
      this.code = code;
   }

   public static LocationOption valueOf(final String option)
   {
      final LocationOption possibleOption = new LocationOption(option);

      final int internalValuesIndex = internalValues.indexOf(possibleOption);
      if (internalValuesIndex != -1)
      {
         return internalValues.get(internalValuesIndex);
         // just to always return one instance not many the same (equal)
      }
      else
      {
         throw new IllegalArgumentException("Cannot parse into an element of "
            + LocationOption.class.getSimpleName() + ": '" + option + "'");
      }
   }

   @Override
   public String toString()
   {
      return code;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (obj instanceof LocationOption)
      {
         return code.equals(((LocationOption) obj).code);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return code.hashCode();
   }

   public static Map<String, LocationOption> getAllPopulators()
   {
      final Map<String, LocationOption> populatorsMap = new HashMap<String, LocationOption>();
      populatorsMap.put("BASIC", BASIC);
      populatorsMap.put("GALLERY", GALLERY);
      populatorsMap.put("KEY_FACTS", KEY_FACTS);
      populatorsMap.put("EDITORIAL", EDITORIAL);
      populatorsMap.put("HEALTHANDSAFETY", HEALTHANDSAFETY);
      populatorsMap.put("PASSPORTANDVISA", PASSPORTANDVISA);
      populatorsMap.put("GETTINGAROUND", GETTINGAROUND);
      populatorsMap.put("WHENTOGO", WHENTOGO);
      populatorsMap.put("THUMBNAILMAP", THUMBNAILMAP);
      populatorsMap.put("CONTENT", CONTENT);
      populatorsMap.put("HIGHLIGHTS", HIGHLIGHTS);
      populatorsMap.put("HIGHLIGHTS", HIGHLIGHTS);
      populatorsMap.put("SUBCATEGORIES", SUBCATEGORIES);
      populatorsMap.put("WEATHER", WEATHER);
      populatorsMap.put("FULLDATA", FULLDATA);

      return populatorsMap;
   }

}
