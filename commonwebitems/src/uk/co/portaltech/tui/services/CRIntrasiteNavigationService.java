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
 * $RCSfile: CRIntrasiteNavigationService.java$
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

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.cruise.services.CruiseAreaService;
import uk.co.portaltech.travel.cruise.services.ItineraryService;
import uk.co.portaltech.travel.cruise.services.location.MainStreamLocationService;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.constants.GlobalNavigationConstant;
import uk.co.portaltech.tui.services.pojo.CruiseNavigation;
import uk.co.portaltech.tui.services.pojo.IntrasiteNavigation;
import uk.co.tui.cr.components.model.CruiseHeaderNavigationComponentModel;

/**
 * @author EXTAP2
 * @description Cruise specific implementation of IntrasiteNavigationService.
 */
public class CRIntrasiteNavigationService implements IntrasiteNavigationService
{

   @Resource
   private MainStreamLocationService mainStreamLocationService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private TUIConfigService tuiConfigService;

   @Resource
   private ItineraryService itineraryService;

   @Resource
   private CruiseAreaService cruiseAreaService;

   /**
    * 
    * @param sources
    * @return CruiseNavigation
    * @see uk.co.portaltech.tui.services.IntrasiteNavigationService#getIntraSiteNavigation(java.lang.Object[])
    */
   @Override
   public IntrasiteNavigation getIntraSiteNavigation(final Object... sources)
   {
      final CruiseNavigation cruiseNavigation = new CruiseNavigation();
      final CruiseHeaderNavigationComponentModel componentModel =
         (CruiseHeaderNavigationComponentModel) sources[0];
      cruiseNavigation.setMegaMenuConfigValues(tuiConfigService
         .getConfigValuesMap(GlobalNavigationConstant.ALL_MEGA_MENU_CONFIG_KEYS));
      final CatalogVersionModel catalogVersion = cmsSiteService.getCurrentCatalogVersion();
      final List<String> validPocCodes =
         fetchAllValidPorts(componentModel.getPocCodes(), catalogVersion);
      cruiseNavigation
         .setCruiseAreaMap(cruiseAreaService.getAllCRAreaWithCountries(catalogVersion));
      cruiseNavigation.setLocationModels(mainStreamLocationService.getAllPOCByCodes(validPocCodes,
         catalogVersion));
      cruiseNavigation.setSite(BrandType.CR.toString());
      return cruiseNavigation;
   }

   /**
    * 
    * @param pocCodes
    * @param catalogVersion
    * @return List<String>
    * @description This method returns list of valid poc's among the poc's passed as an argument
    */
   private List<String> fetchAllValidPorts(final List<String> pocCodes,
      final CatalogVersionModel catalogVersion)
   {
      final List<String> validPocCodes = new ArrayList<String>();

      for (final String locationCode : pocCodes)
      {
         if (itineraryService.isValidPortOfCall(locationCode, catalogVersion))
         {
            validPocCodes.add(locationCode);
         }
      }
      return validPocCodes;
   }

}
