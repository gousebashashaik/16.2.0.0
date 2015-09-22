/**
 *
 */
package uk.co.portaltech.tui.converters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author EXTLP1
 *
 */
public class GlobalNavigationOption
{
   public static final GlobalNavigationOption DESTINATION_NAVIGATION = new GlobalNavigationOption(
      "DESTINATION_NAVIGATION");

   public static final GlobalNavigationOption PORTOFCALL_NAVIGATION = new GlobalNavigationOption(
      "PORTOFCALL_NAVIGATION");

   public static final GlobalNavigationOption CR_NAVIGATION_LINKS = new GlobalNavigationOption(
      "CR_NAVIGATION_LINKS");

   public static final GlobalNavigationOption TH_NAVIGATION_LINKS = new GlobalNavigationOption(
      "TH_NAVIGATION_LINKS");

   private static List<GlobalNavigationOption> internalValues = Arrays.asList(
      DESTINATION_NAVIGATION, PORTOFCALL_NAVIGATION, CR_NAVIGATION_LINKS, TH_NAVIGATION_LINKS);

   private final String code;

   protected GlobalNavigationOption(final String code)
   {
      this.code = code;
   }

   public static GlobalNavigationOption valueOf(final String option)
   {
      final GlobalNavigationOption possibleOption = new GlobalNavigationOption(option);

      final int internalValuesIndex = internalValues.indexOf(possibleOption);
      if (internalValuesIndex != -1)
      {
         return internalValues.get(internalValuesIndex);
         // just to always return one instance not many the same (equal)
      }
      else
      {
         throw new IllegalArgumentException("Cannot parse into an element of "
            + GlobalNavigationOption.class.getSimpleName() + ": '" + option + "'");
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
      if (obj instanceof GlobalNavigationOption)
      {
         return code.equals(((GlobalNavigationOption) obj).code);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return code.hashCode();
   }

   public static Map<String, GlobalNavigationOption> getAllPopulators()
   {
      final Map<String, GlobalNavigationOption> populatorsMap =
         new HashMap<String, GlobalNavigationOption>();
      populatorsMap.put("DESTINATION_NAVIGATION", DESTINATION_NAVIGATION);
      populatorsMap.put("PORTOFCALL_NAVIGATION", PORTOFCALL_NAVIGATION);
      populatorsMap.put("CR_NAVIGATION_LINKS", CR_NAVIGATION_LINKS);
      populatorsMap.put("TH_NAVIGATION_LINKS", TH_NAVIGATION_LINKS);
      return populatorsMap;
   }

}
