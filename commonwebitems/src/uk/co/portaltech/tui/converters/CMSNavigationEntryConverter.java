/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.web.view.data.CMSNavigationEntryViewData;

/**
 * @author gagan
 *
 */
public class CMSNavigationEntryConverter extends
   AbstractPopulatingConverter<CMSNavigationEntryModel, CMSNavigationEntryViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public CMSNavigationEntryViewData createTarget()
   {
      return new CMSNavigationEntryViewData();
   }

}
