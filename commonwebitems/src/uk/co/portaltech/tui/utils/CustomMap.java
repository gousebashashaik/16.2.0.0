/**
 *
 */
package uk.co.portaltech.tui.utils;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.HashMap;

/**
 * atic
 *
 * @author EXTLP1 This class is written to match String key input to regex key in map.
 *
 */
public class CustomMap extends HashMap<String, String>
{

   /**
    *
    * @param key
    * @return String
    * @description This method takes String object as input and matches lowercase value of string
    *              with regex key in map. EX if input key value is destinationLandingPage then it
    *              will match lowerecase of this with map key as .*destination.* and return value of
    *              it. If key does not match with any map entry key then it will return default
    *              Value as empty.
    */
   @Override
   public String get(final Object key)
   {
      String value = EMPTY;
      if (isKeyNull(key))
      {
         for (final java.util.Map.Entry<String, String> keyValue : this.entrySet())
         {
            if (key.toString().toLowerCase().matches(keyValue.getKey()))
            {
               value = keyValue.getValue();
               break;
            }
         }
      }
      return value;
   }

   /**
    * @param key
    * @return
    */
   private boolean isKeyNull(final Object key)
   {
      return key == null ? false : true;
   }

}
