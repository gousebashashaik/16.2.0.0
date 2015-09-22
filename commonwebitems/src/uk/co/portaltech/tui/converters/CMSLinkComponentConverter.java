/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;

/**
 * @author gagan
 *
 */
public class CMSLinkComponentConverter extends
   AbstractPopulatingConverter<CMSLinkComponentModel, CMSLinkComponentViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public CMSLinkComponentViewData createTarget()
   {
      return new CMSLinkComponentViewData();
   }

}
