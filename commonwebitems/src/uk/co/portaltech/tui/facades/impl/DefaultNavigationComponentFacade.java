/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import uk.co.portaltech.tui.components.model.NavigationComponentModel;
import uk.co.portaltech.tui.facades.NavigationComponentFacade;
import uk.co.portaltech.tui.web.view.data.NavigationComponentViewData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author gagan
 *
 */
public class DefaultNavigationComponentFacade implements NavigationComponentFacade
{

   private static final TUILogUtils LOG = new TUILogUtils("DefaultNavigationComponentFacade");

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private Populator<NavigationComponentModel, NavigationComponentViewData> navigationComponentPopulator;

   @Resource
   private Converter<NavigationComponentModel, NavigationComponentViewData> navigationComponentConverter;

   @Resource
   private TypeService typeService;

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.NavigationComponentFacade#getNavigationComponentViewData(java
    * .lang.String)
    */
   @Override
   public NavigationComponentViewData getNavigationComponentViewData(final String componentUID)
   {
      NavigationComponentViewData navigationComponentViewData = null;
      try
      {
         final NavigationComponentModel navigationComponentModel =
            (NavigationComponentModel) cmsComponentService.getSimpleCMSComponent(componentUID);
         navigationComponentViewData =
            navigationComponentConverter.convert(navigationComponentModel);
         navigationComponentPopulator.populate(navigationComponentModel,
            navigationComponentViewData);
         if (navigationComponentModel.getComponentStyle() != null)
         {
            final EnumerationValueModel enumerationValue =
               typeService.getEnumerationValue(navigationComponentModel.getComponentStyle());
            navigationComponentViewData.setComponentStyle(enumerationValue.getName());
         }
      }
      catch (final CMSItemNotFoundException e)
      {
         LOG.error(" CMSItemNotFoundException has been raised" + e.getMessage(), e);
      }
      return navigationComponentViewData;

   }
}
