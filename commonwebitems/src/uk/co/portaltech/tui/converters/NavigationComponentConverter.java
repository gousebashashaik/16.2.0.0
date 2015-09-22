/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.components.model.NavigationComponentModel;
import uk.co.portaltech.tui.web.view.data.NavigationComponentViewData;

/**
 * @author gagan
 *
 */
public class NavigationComponentConverter extends
   AbstractPopulatingConverter<NavigationComponentModel, NavigationComponentViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public NavigationComponentViewData createTarget()
   {
      return new NavigationComponentViewData();
   }
}
