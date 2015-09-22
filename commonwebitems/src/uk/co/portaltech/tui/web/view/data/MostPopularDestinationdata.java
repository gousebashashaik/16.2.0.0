/**
 * Added for New most popular destination data for new search panel.
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author niranjani.r
 *
 */
public class MostPopularDestinationdata
{
   /**
    * To check whether the entry has multiple locations.
    */

   private boolean multiple = true;

   /**
    * To Add the list of locations .
    */
   private List<DestinationData> locationDatas;

   /**
    * @return the multiple
    */
   public final boolean isMultiple()
   {
      return multiple;
   }

   /**
    * @param multiple the multiple to set
    */
   public final void setMultiple(final boolean multiple)
   {
      this.multiple = multiple;
   }

   /**
    * @return the locationDatas
    */
   public final List<DestinationData> getLocationDatas()
   {
      return locationDatas;
   }

   /**
    * @param locationDatas the locationDatas to set
    */
   public final void setLocationDatas(final List<DestinationData> locationDatas)
   {
      this.locationDatas = locationDatas;
   }

}
