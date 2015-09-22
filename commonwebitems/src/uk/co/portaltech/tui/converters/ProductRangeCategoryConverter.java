/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;

/**
 * @author l.furrer
 *
 */
public class ProductRangeCategoryConverter extends
   AbstractPopulatingConverter<ProductRangeCategoryModel, ProductRangeCategoryViewData>
{

   @Override
   public ProductRangeCategoryViewData createTarget()
   {
      return new ProductRangeCategoryViewData();
   }

   @Override
   public void populate(final ProductRangeCategoryModel source,
      final ProductRangeCategoryViewData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      super.populate(source, target);

   }

}
