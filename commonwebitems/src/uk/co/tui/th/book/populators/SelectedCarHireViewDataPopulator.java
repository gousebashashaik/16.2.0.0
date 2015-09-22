/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.th.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityViewData;

/**
 * @author pradeep.as
 *
 */
public class SelectedCarHireViewDataPopulator implements
   Populator<ExtraFacilityCategory, List<ExtraFacilityCategoryViewData>>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final ExtraFacilityCategory source,
      final List<ExtraFacilityCategoryViewData> target) throws ConversionException
   {
      for (final ExtraFacilityCategoryViewData eachExtraCategory : target)
      {
         for (final ExtraFacilityViewData eachExtra : eachExtraCategory.getExtraFacilityViewData())
         {
            if (StringUtils.equalsIgnoreCase(source.getExtraFacilities().get(0)
               .getExtraFacilityCode(), eachExtra.getCode()))
            {
               eachExtra.setSelected(true);
               eachExtraCategory.setSelected(true);
               break;
            }
         }

      }
      sortExtraFacilityCategoryViewList(target);

   }

   /**
    * Sort extra facility category view list.
    *
    * @param extraFacilityCategoryViewList the extra facility category view list
    */
   private void sortExtraFacilityCategoryViewList(
      final List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewList)
   {
      if (extraFacilityCategoryViewList.size() > 1)
      {
         Collections.sort(extraFacilityCategoryViewList,
            new Comparator<ExtraFacilityCategoryViewData>()
            {

               @Override
               public int compare(
                  final ExtraFacilityCategoryViewData firstExtraFacilityCategoryViewData,
                  final ExtraFacilityCategoryViewData secondExtraFacilityCategoryViewData)
               {
                  final boolean firstCategorySelected =
                     firstExtraFacilityCategoryViewData.isSelected();
                  final boolean secondCategorySelected =
                     secondExtraFacilityCategoryViewData.isSelected();
                  if (firstCategorySelected == secondCategorySelected)
                  {
                     return 0;
                  }
                  return firstCategorySelected ? -1 : 1;
               }
            });
      }
   }
}
