/**
 *
 */
package uk.co.portaltech.tui.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.tui.converters.GlobalNavigationOption;

/**
 * @author EXTLP1
 * 
 */
public final class GlobalNavigationConstant
{
   public static final String COMMA_SEPERATOR = ", ";

   public static final String MEGA_MENU_POC_LINK_TITLE = "CR.megamenu.port.of.call.link.title";

   public static final String MEGA_MENU_POC_LINK_URL = "CR.megamenu.port.of.call.link.url";

   public static final String MEGA_MENU_POC_TITLE = "CR.megamenu.port.of.call.title";

   public static final String MEGA_MENU_DEST_TITLE = "CR.megamenu.cruise.destination.title";

   public static final String MEGA_MENU_DEST_LINK_TITLE =
      "CR.megamenu.cruise.destination.link.title";

   public static final String MEGA_MENU_DEST_LINK_URL = "CR.megamenu.cruise.destination.link.url";

   public static final String MEGA_MENU_DEST_NILE_CRUISE_TITLE =
      "CR.megamenu.cruise.destination.nile.cruise.title";

   public static final String MEGA_MENU_DEST_NILE_CRUISE_URL =
      "CR.megamenu.cruise.destination.nile.cruise.url";

   public static final String OVERVIEW = "overview";

   public static final String GLOBALNAVCOMPONENT = "GlobalNavigationComponent";

   public static final String COMPONENT = "component";

   public static final String VIEWDATA = "viewData";

   public static final List<String> ALL_MEGA_MENU_CONFIG_KEYS = Arrays.asList(MEGA_MENU_POC_TITLE,
      MEGA_MENU_POC_LINK_TITLE, MEGA_MENU_POC_LINK_URL, MEGA_MENU_DEST_TITLE,
      MEGA_MENU_DEST_LINK_TITLE, MEGA_MENU_DEST_LINK_URL, MEGA_MENU_DEST_NILE_CRUISE_TITLE,
      MEGA_MENU_DEST_NILE_CRUISE_URL);

   public static final int ZERO_VALUE = 0;

   public static final int THREE_VALUE = 3;

   public static final String COLLECTION1 = "collection1";

   public static final String COLLECTION2 = "collection2";

   public static final String COLLECTION3 = "collection3";

   public static final String COLLECTION4 = "collection4";

   public static final String COLLECTION5 = "collection5";

   public static final String COLLECTION6 = "collection6";

   public static final String CRUISES = "cruises";

   public static final String DESTINATIONS = "destinations";

   public static final String HOLIDAYS = "holidays";

   public static final List<String> GLOBAL_HEADER_APPPLIED_BRANDLIST = Arrays.asList("TH", "CR");

   public static final String ALTERNATE_NAVIGATION = "alternateNavigationComp";

   public static final String DOT = ".";

   public static final Map<String, List<GlobalNavigationOption>> POPULATOR_MAP =
      new HashMap<String, List<GlobalNavigationOption>>()
      {
         {
            put(BrandType.TH.toString(), Arrays.asList(GlobalNavigationOption.TH_NAVIGATION_LINKS));
            put(BrandType.CR.toString(), Arrays.asList(
               GlobalNavigationOption.DESTINATION_NAVIGATION,
               GlobalNavigationOption.PORTOFCALL_NAVIGATION,
               GlobalNavigationOption.CR_NAVIGATION_LINKS));
            put(BrandType.FC.toString(), Arrays.asList(GlobalNavigationOption.TH_NAVIGATION_LINKS));
         }
      };

   private GlobalNavigationConstant()
   {

   }

}
