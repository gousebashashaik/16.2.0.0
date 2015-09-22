/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.web.view.data.CollectionViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author niranjani.r
 *
 */
public class ProductRangeToCollectionDataConverter extends
   AbstractPopulatingConverter<ProductRangeViewData, CollectionViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public void populate(final ProductRangeViewData source, final CollectionViewData target)
   {
      target.setId(source.getCode());
      target.setName(source.getFeatureCodesAndValues().get("name").get(0).toString());
      target.setTagLine(source.getFeatureCodesAndValues().get("strapline").get(0).toString());
      target.setType("ProductRange");

      super.populate(source, target);
   }

   @Override
   public CollectionViewData createTarget()
   {
      return new CollectionViewData();
   }
   //
}
