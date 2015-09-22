/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.web.view.data.NavigationBarComponentViewData;

/**
 * @author gagan
 *
 */
public class NavigationBarComponentConverter extends
   AbstractPopulatingConverter<NavigationBarComponentModel, NavigationBarComponentViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public NavigationBarComponentViewData createTarget()
   {
      return new NavigationBarComponentViewData();
   }

}
