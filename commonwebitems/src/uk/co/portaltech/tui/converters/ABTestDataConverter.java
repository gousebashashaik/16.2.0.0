/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.web.view.data.ABTestViewData;

/**
 * @author s.consolino
 *
 */
public class ABTestDataConverter extends
   AbstractPopulatingConverter<SimpleCMSComponentModel, ABTestViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter#populate(java
    * .lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final SimpleCMSComponentModel source, final ABTestViewData target)
   {
      super.populate(source, target);
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public ABTestViewData createTarget()
   {
      return new ABTestViewData();
   }
}
