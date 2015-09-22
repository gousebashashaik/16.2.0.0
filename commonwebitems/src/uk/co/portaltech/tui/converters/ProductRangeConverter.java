/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author gagan
 *
 */
public class ProductRangeConverter extends
   AbstractPopulatingConverter<ProductRangeModel, ProductRangeViewData>
{
   @Resource
   private TUIUrlResolver<ProductRangeModel> tuiCategoryModelUrlResolver;

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter#populate(java
    * .lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final ProductRangeModel source, final ProductRangeViewData target)
   {
      target.setCode(source.getCode());
      target.setUrl(tuiCategoryModelUrlResolver.resolve(source));
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
