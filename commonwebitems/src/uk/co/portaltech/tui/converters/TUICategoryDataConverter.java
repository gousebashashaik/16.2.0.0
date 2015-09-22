/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.web.view.data.TUICategoryData;

/**
 * @author gagan
 *
 */
public class TUICategoryDataConverter extends
   AbstractPopulatingConverter<CategoryModel, TUICategoryData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public TUICategoryData createTarget()
   {
      return new TUICategoryData();
   }

}
