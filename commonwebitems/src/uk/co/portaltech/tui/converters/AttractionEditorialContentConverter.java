/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.ItemModel;

import uk.co.portaltech.tui.web.view.data.AttractionViewData;

/**
 * @author s.consolino
 *
 */
public class AttractionEditorialContentConverter extends
   AbstractPopulatingConverter<ItemModel, AttractionViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter#populate(java
    * .lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final ItemModel source, final AttractionViewData target)
   {
      super.populate(source, target);
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public AttractionViewData createTarget()
   {
      return new AttractionViewData();
   }
}
