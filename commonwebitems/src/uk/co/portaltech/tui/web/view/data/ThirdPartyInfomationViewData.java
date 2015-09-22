/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author veena.pn
 *
 */
public class ThirdPartyInfomationViewData
{
   /** The third content map. */
   private Map<String, String> thirdPartyContentMap = new HashMap<String, String>();

   /**
    * @return the thirdPartyContentMap
    */
   public Map<String, String> getThirdPartyContentMap()
   {
      return thirdPartyContentMap;
   }

   /**
    * @param thirdPartyContentMap the extraContentMap to set
    */
   public void setThirdPartyContentMap(final Map<String, String> thirdPartyContentMap)
   {
      this.thirdPartyContentMap = thirdPartyContentMap;
   }

}
