/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Comparator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author abhijit.s
 *
 */
public class FacilityViewDataPriorityComparator implements Comparator<FacilityViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
   @Override
   public int compare(final FacilityViewData o1, final FacilityViewData o2)
   {
      int priority1 = 0;
      int priority2 = 0;
      final String key = "priority";
      if (o1.getFeatureCodesAndValues().containsKey(key)
         && CollectionUtils.isNotEmpty(o1.getFeatureCodesAndValues().get(key))
         && StringUtils.isNotEmpty((String) o1.getFeatureCodesAndValues().get(key).get(0)))
      {
         priority1 = Integer.parseInt((String) o1.getFeatureCodesAndValues().get(key).get(0));
      }
      if (o2.getFeatureCodesAndValues().containsKey(key)
         && CollectionUtils.isNotEmpty(o2.getFeatureCodesAndValues().get(key))
         && StringUtils.isNotEmpty((String) o2.getFeatureCodesAndValues().get(key).get(0)))
      {
         priority2 = Integer.parseInt((String) o2.getFeatureCodesAndValues().get(key).get(0));
      }
      return priority1 - priority2;
   }

}
