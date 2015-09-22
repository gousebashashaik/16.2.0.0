/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static uk.co.portaltech.tui.utils.GlobalHeaderCommonUtil.getActiveTab;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;

import javax.annotation.Resource;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.tui.constants.GlobalNavigationConstant;
import uk.co.portaltech.tui.converters.GlobalNavigationOption;
import uk.co.portaltech.tui.facades.GlobalNavigationFacade;
import uk.co.portaltech.tui.services.GlobalNavigationService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.pojo.GlobalNavigation;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.tui.cr.components.model.GlobalNavigationComponentModel;

/**
 * @author EXTLP1
 *
 */
public class GlobalNavigationFacadeImpl implements GlobalNavigationFacade
{

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private GlobalNavigationService globalNavigationService;

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource(name = "siteAwareKeyGenerator")
   private SiteAwareKeyGenerator keyGenerator;

   @Resource
   private ConfigurablePopulator<NavigationComponent, GlobalNavigationComponentViewData, GlobalNavigationOption> defaultCrGlobalNavigationPopulator;

   /**
    *
    * @param globalNavigationComponent
    * @param pageId
    * @return GlobalNavigationComponentViewData
    * @see uk.co.portaltech.tui.facades.GlobalNavigationFacade#getGlobalNavigationViewData(uk.co.tui.cr.components.model.GlobalNavigationComponentModel,
    *      java.lang.String)
    */
   @Override
   public GlobalNavigationComponentViewData getGlobalNavigationViewData(
      final GlobalNavigationComponentModel globalNavigationComponent, final String pageId)
   {
      final String brand = tuiUtilityService.getSiteBrand();

      final String key =
         keyGenerator.generate("GetGlobalNavigationByBrandAndPage", pageId,
            globalNavigationComponent.getUid());

      final CacheWrapper<String, GlobalNavigationComponentViewData> globalNavigationComponentViewDataCacheWrapper =
         cacheUtil.getGlobalNavigationComponentViewDataCacheWrapper();

      GlobalNavigationComponentViewData globalNavigationComponentViewData =
         globalNavigationComponentViewDataCacheWrapper.get(key);

      if (globalNavigationComponentViewData == null)
      {
         globalNavigationComponentViewData = new GlobalNavigationComponentViewData();

         // Get GlobalNavigation which have required data for all website header.
         final GlobalNavigation globalNavigation =
            globalNavigationService.getGlobalNavigations(brand, globalNavigationComponent);

         // Populate each website navigation view data.
         for (final NavigationComponent component : globalNavigation.getNavigationComponents())
         {
            defaultCrGlobalNavigationPopulator.populate(component,
               globalNavigationComponentViewData, GlobalNavigationConstant.POPULATOR_MAP
                  .get(component.getIntraSiteNavigation().getSite()));
         }

         // Global links for all thomson domain website header.
         globalNavigationComponentViewData.setGlobalLinks(globalNavigation.getMainNavigations());

         // Current website brand setting
         globalNavigationComponentViewData.setSiteBrand(brand);

         // Set current website active tab
         globalNavigationComponentViewData.setActiveTabs(getActiveTab(pageId, brand));

         globalNavigationComponentViewDataCacheWrapper.put(key, globalNavigationComponentViewData);
      }
      return globalNavigationComponentViewData;
   }

}
