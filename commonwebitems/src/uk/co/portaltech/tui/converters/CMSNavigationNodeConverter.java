/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.web.view.data.CMSNavigationNodeViewData;

/**
 * @author gagan
 *
 */
public class CMSNavigationNodeConverter extends
   AbstractPopulatingConverter<CMSNavigationNodeModel, CMSNavigationNodeViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public CMSNavigationNodeViewData createTarget()
   {
      return new CMSNavigationNodeViewData();
   }

}
