/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.components.model.GeographicalNavigationComponentModel;
import uk.co.portaltech.tui.web.view.data.GeographicalNavigationComponentViewData;

/**
 * @author gagan
 *
 */
public class GeographicalNavigationComponentConverter
   extends
   AbstractPopulatingConverter<GeographicalNavigationComponentModel, GeographicalNavigationComponentViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public GeographicalNavigationComponentViewData createTarget()
   {
      return new GeographicalNavigationComponentViewData();
   }

}
