/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author vinodkumar.g
 *
 */
public class ContinentAndCountryHierarchyViewData
{

   private List<DestinationData> allContinentHierarchy;

   /**
    * @return the allContinentHierarchy
    */
   public List<DestinationData> getAllContinentHierarchy()
   {
      return allContinentHierarchy;
   }

   /**
    * @param allContinentHierarchy the allContinentHierarchy to set
    */
   public void setAllContinentHierarchy(final List<DestinationData> allContinentHierarchy)
   {
      this.allContinentHierarchy = allContinentHierarchy;
   }

}
