/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author laxmibai.p
 *
 */
public class ProductToDestinationConverter extends
   AbstractPopulatingConverter<ProductRangeModel, DestinationData>
{

   @Resource
   private Converter<ProductRangeModel, ProductRangeViewData> productRangeConverter;

   @Resource
   private Populator<ProductRangeModel, ProductRangeViewData> productRangeBasicPopulator;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public DestinationData createTarget()
   {

      return new DestinationData();
   }

   @Override
   public void populate(final ProductRangeModel source, final DestinationData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");
      final ProductRangeViewData productRangeViewData = productRangeConverter.convert(source);

      productRangeBasicPopulator.populate(source, productRangeViewData);
      target.setId(source.getCode());
      target.setName(source.getName());
      target.setType("ProductRange");
      target.setInspireText(productRangeViewData.getFeatureCodesAndValues().get("strapline").get(0)
         .toString());

   }
}
