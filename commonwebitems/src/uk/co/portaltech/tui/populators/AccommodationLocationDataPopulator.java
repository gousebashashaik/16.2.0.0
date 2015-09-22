/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author narendra.bm
 */
public class AccommodationLocationDataPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   private static final int TWO = 2;

   @Override
   public void populate(final AccommodationModel sourceModel, final AccommodationViewData targetData)
      throws ConversionException
   {

      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");
      final Map locations = new LinkedHashMap<String, String>();
      Collection<CategoryModel> productCategories = sourceModel.getSupercategories();
      while (CollectionUtils.isNotEmpty(productCategories) && locations.size() < TWO)
      {
         for (final CategoryModel category : productCategories)
         {
            if (category instanceof LocationModel && locations.size() < TWO)
            {
               final LocationModel locModel = (LocationModel) category;
               locations.put(locModel.getType().getCode(), locModel.getName());
               productCategories = locModel.getSupercategories();
            }
         }
      }
      targetData.setLocations(locations);
   }
}
