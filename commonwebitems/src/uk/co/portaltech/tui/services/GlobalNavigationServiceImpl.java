/*
 * Copyright (C)2006 TUI UK Ltd
 * 
 * TUI UK Ltd, Columbus House, Westwood Way, Westwood Business Park, Coventry, United Kingdom CV4
 * 8TT
 * 
 * Telephone - (024)76282828
 * 
 * All rights reserved - The copyright notice above does not evidence any actual or intended
 * publication of this source code.
 * 
 * $RCSfile: GlobalNavigationServiceImpl.java$
 * 
 * $Revision: $
 * 
 * $Date: 20 Apr 2015$
 * 
 * Author: EXTAP2
 * 
 * 
 * $Log: $
 */
package uk.co.portaltech.tui.services;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.tui.constants.GlobalNavigationConstant;
import uk.co.portaltech.tui.services.pojo.GlobalNavigation;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.tui.cr.components.model.GlobalNavigationComponentModel;

/**
 * @author EXTAP2
 *
 */
public class GlobalNavigationServiceImpl implements GlobalNavigationService
{

   private static final String INTRASITE_SEPARATOR = ":";

   private static final String INTERSITE_SEPERATOR = ",";

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private FlexibleSearchService flexibleSearchService;

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource(name = "siteAwareKeyGenerator")
   private SiteAwareKeyGenerator keyGenerator;

   @Resource
   private Map<String, IntrasiteNavigationService> intrasiteNavigationService;

   private static final String ALT_COMP_QUERY =
      "select {sc.pk} from {SimpleCMSComponent as sc},{CatalogVersion as cv},{Catalog as c} where {sc.catalogVersion}={cv.pk}"
         + " and {cv.version}='Online' and {cv.catalog}={c.pk} and {c.id}= ?catalogId and {sc.uid}= ?compId";

   /**
    *
    * @param siteBrand
    * @param componentModel
    * @return GlobalNavigation
    * @see uk.co.portaltech.tui.services.GlobalNavigationService#getGlobalNavigations(java.lang.String,
    *      uk.co.tui.cr.components.model.GlobalNavigationComponentModel)
    */
   @Override
   public GlobalNavigation getGlobalNavigations(final String siteBrand,
      final GlobalNavigationComponentModel componentModel)
   {

      final String key = keyGenerator.generate("globalNavigationsByBrand", componentModel.getUid());

      final CacheWrapper<String, GlobalNavigation> globalNavigationCacheWrapper =
         cacheUtil.getGlobalNavigationCacheWrapper();

      GlobalNavigation globalNavigation = globalNavigationCacheWrapper.get(key);

      if (globalNavigation == null)
      {

         // Key for getting alternate navigation data of other websites
         final String alternateNav =
            configurationService.getConfiguration().getString(
               siteBrand + GlobalNavigationConstant.DOT
                  + GlobalNavigationConstant.ALTERNATE_NAVIGATION);
         /*
          * List of all navigation data. This will have navigation header of current website and
          * other websites as well.
          */
         final List<NavigationComponent> navigations = new ArrayList<NavigationComponent>();
         /*
          * Creating navigation data for other websites which are required to be shown in current
          * website
          */
         if (isNotEmpty(alternateNav))
         {
            for (final String alternateComp : alternateNav.split(INTERSITE_SEPERATOR))
            {
               final String[] alternateData = alternateComp.split(INTRASITE_SEPARATOR);
               final NavigationComponent component = new NavigationComponent();
               // catalog id is the first element
               component.setCatalogId(alternateData[0]);
               // getting component from another content catalog , using catalog id and component
               // id.
               component.setComponent(getCMSComponentByIdNCatalog(alternateData[0],
                  alternateData[1]));
               /*
                * Header navigation detail of one website. Respective intrasite navigation service
                * is chosen dynamically using content catalog id as key
                */
               component.setIntraSiteNavigation(intrasiteNavigationService.get(alternateData[0])
                  .getIntraSiteNavigation(component.getComponent()));

               navigations.add(component);
            }
         }
         // Header navigation detail of current website.
         final NavigationComponent navigationComponent = new NavigationComponent();
         navigationComponent.setComponent(componentModel.getGlobalNavigation());
         navigationComponent.setCatalogId(componentModel.getCatalogVersion().getCatalog().getId());
         navigationComponent.setIntraSiteNavigation(intrasiteNavigationService.get(
            navigationComponent.getCatalogId()).getIntraSiteNavigation(
            navigationComponent.getComponent()));
         navigations.add(navigationComponent);
         // Setting required detail into GlobalNavigation
         globalNavigation = new GlobalNavigation();

         globalNavigation.setNavigationComponents(navigations);
         globalNavigation.setMainNavigations(componentModel.getGlobalHeaderNavigationLinks());

         globalNavigationCacheWrapper.put(key, globalNavigation);
      }
      return globalNavigation;
   }

   /**
    *
    * @param catalogId
    * @param compId
    * @return SimpleCMSComponentModel
    * @description getting component using catalog id and component id.
    */
   private SimpleCMSComponentModel getCMSComponentByIdNCatalog(final String catalogId,
      final String compId)
   {
      final FlexibleSearchQuery query = new FlexibleSearchQuery(ALT_COMP_QUERY);
      query.addQueryParameter("catalogId", catalogId);
      query.addQueryParameter("compId", compId);
      return flexibleSearchService.searchUnique(query);
   }

}
