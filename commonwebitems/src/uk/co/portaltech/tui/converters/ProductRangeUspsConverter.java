/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.util.List;

import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author s.consolino
 *
 */
public class ProductRangeUspsConverter extends
   AbstractPopulatingConverter<List<ProductUspModel>, ProductRangeViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter#populate(java
    * .lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final List<ProductUspModel> source, final ProductRangeViewData target)
   {
      super.populate(source, target);
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public ProductRangeViewData createTarget()
   {
      return new ProductRangeViewData();
   }
}
