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
 * $RCSfile: THglobalNavigationPopulator.java$
 * 
 * $Revision: $
 * 
 * $Date: 15 Apr 2015$
 * 
 * Author: EXTLP1
 * 
 * 
 * $Log: $
 */
package uk.co.portaltech.tui.populators;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;

import javax.annotation.Resource;

import uk.co.portaltech.tui.components.model.MegaMenuComponentModel;
import uk.co.portaltech.tui.constants.GlobalNavigationConstant;
import uk.co.portaltech.tui.facades.NonGeoItemFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.ProductPageComponentViewData;

/**
 * @author EXTLP1
 * 
 */
public class TuiGlobalHeaderNavigationPopulator implements
   Populator<NavigationComponent, GlobalNavigationComponentViewData>
{

   @Resource
   private NonGeoItemFacade nonGeoItemFacade;

   @Resource
   private TuiUtilityService tuiUtilityService;

   private static final String COLLECTION_HEADING = "collectionHeading";

   private static final String COLLECTION_HEADING_TITILE = "collectionHeadingLinkTitle";

   private static final String COLLECTION_HEADING_URL = "collectionHeadingLinkURL";

   /**
    * @param source
    * @param target
    * @throws ConversionException
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    *      java.lang.Object)
    */
   @Override
   public void populate(final NavigationComponent source,
      final GlobalNavigationComponentViewData target) throws ConversionException
   {
      final MegaMenuComponentModel megaMenu = (MegaMenuComponentModel) source.getComponent();
      final Map<String, Object> navMap =
         target.getTuiGlobalNavigationViewData().getTuiHolidayNavigationViewData();
      collectionData(megaMenu, navMap);
      bestforData(megaMenu, navMap);
      locationData(megaMenu, navMap);
      dealsData(megaMenu, navMap);
   }

   /**
    * @param megaMenu
    * @param navMap
    */
   private void collectionData(final MegaMenuComponentModel megaMenu,
      final Map<String, Object> navMap)
   {
      populateTHNavigationData(megaMenu.getCollectionCode1(), megaMenu.getCollectionLink1(),
         navMap, GlobalNavigationConstant.COLLECTION1);
      populateTHNavigationData(megaMenu.getCollectionCode2(), megaMenu.getCollectionLink2(),
         navMap, GlobalNavigationConstant.COLLECTION2);
      populateTHNavigationData(megaMenu.getCollectionCode3(), megaMenu.getCollectionLink3(),
         navMap, GlobalNavigationConstant.COLLECTION3);
      populateTHNavigationData(megaMenu.getCollectionCode4(), megaMenu.getCollectionLink4(),
         navMap, GlobalNavigationConstant.COLLECTION4);
      populateTHNavigationData(megaMenu.getCollectionCode5(), megaMenu.getCollectionLink5(),
         navMap, GlobalNavigationConstant.COLLECTION5);
      populateTHNavigationData(megaMenu.getCollectionCode6(), megaMenu.getCollectionLink6(),
         navMap, GlobalNavigationConstant.COLLECTION6);

      navMap.put(COLLECTION_HEADING, megaMenu.getCollectionHeading());
      navMap.put(COLLECTION_HEADING_TITILE, megaMenu.getBestForHeadingLinkTitle());
      navMap.put(COLLECTION_HEADING_URL, megaMenu.getCollectionHeadingLinkURL());
   }

   /**
    * @param megaMenu
    * @param navMap
    */
   private void dealsData(final MegaMenuComponentModel megaMenu, final Map<String, Object> navMap)
   {
      navMap.put("dealsHeading", megaMenu.getDealsHeading());
      navMap.put("dealsHeadingLinkTitle", megaMenu.getDealsHeadingLinkTitle());
      navMap.put("dealsHeadingLinkURL", megaMenu.getDealsHeadingLinkURL());
      navMap.put("deal1", megaMenu.getDeals1());
      navMap.put("dealLink1", megaMenu.getDealsLink1());
      navMap.put("deal2", megaMenu.getDeals2());
      navMap.put("dealLink2", megaMenu.getDealsLink2());
      navMap.put("deal3", megaMenu.getDeals3());
      navMap.put("dealLink3", megaMenu.getDealsLink3());
   }

   /**
    * @param megaMenu
    * @param navMap
    */
   private void locationData(final MegaMenuComponentModel megaMenu, final Map<String, Object> navMap)
   {
      navMap.put("locationHeading", megaMenu.getLocationHeading());
      navMap.put("locationHeadingLinkTitle", megaMenu.getLocationHeadingLinkTitle());
      navMap.put("locationHeadingLinkURL", megaMenu.getLocationHeadingLinkURL());
      navMap.put("location1", megaMenu.getLocation1());
      navMap.put("locationLink1", megaMenu.getLocationLink1());
      navMap.put("location2", megaMenu.getLocation2());
      navMap.put("locationLink2", megaMenu.getLocationLink2());
      navMap.put("location3", megaMenu.getLocation3());
      navMap.put("locationLink3", megaMenu.getLocationLink3());
      navMap.put("location4", megaMenu.getLocation4());
      navMap.put("locationLink4", megaMenu.getLocationLink4());
   }

   /**
    * @param megaMenu
    * @param navMap
    */
   private void bestforData(final MegaMenuComponentModel megaMenu, final Map<String, Object> navMap)
   {
      navMap.put("bestForHeading", megaMenu.getBestForHeading());
      navMap.put("bestForHeadingLinkTitle", megaMenu.getBestForHeadingLinkTitle());
      navMap.put("bestForHeadingLinkURL", megaMenu.getBestForHeadingLinkURL());
      navMap.put("bestForTitle1", megaMenu.getBestForTitle1());
      navMap.put("bestForLink1", megaMenu.getBestForLink1());
      navMap.put("bestForTitle2", megaMenu.getBestForTitle2());
      navMap.put("bestForLink2", megaMenu.getBestForLink2());
      navMap.put("bestForTitle3", megaMenu.getBestForTitle3());
      navMap.put("bestForLink3", megaMenu.getBestForLink3());
      navMap.put("bestForTitle4", megaMenu.getBestForTitle4());
      navMap.put("bestForLink4", megaMenu.getBestForLink4());
      navMap.put("bestForTitle5", megaMenu.getBestForTitle5());
      navMap.put("bestForLink5", megaMenu.getBestForLink5());
      navMap.put("bestForTitle6", megaMenu.getBestForTitle6());
      navMap.put("bestForLink6", megaMenu.getBestForLink6());
      navMap.put("bestForTitle7", megaMenu.getBestForTitle7());
      navMap.put("bestForLink7", megaMenu.getBestForLink7());
      navMap.put("bestForTitle8", megaMenu.getBestForTitle8());
      navMap.put("bestForLink8", megaMenu.getBestForLink8());
      navMap.put("bestForTitle9", megaMenu.getBestForTitle9());
      navMap.put("bestForLink9", megaMenu.getBestForLink9());
   }

   private void populateTHNavigationData(final String collectionCode, final String collectionLink,
      final Map navigationDataMap, final String key)
   {
      if (isNotEmpty(collectionCode) && isNotBlank(collectionCode))
      {
         final ProductPageComponentViewData holidayNavigationData =
            nonGeoItemFacade.getCollectionDiffProdData(
               tuiUtilityService.getValueMap(collectionCode), "");
         holidayNavigationData.setCollectionLinkUrl(collectionLink);
         navigationDataMap.put(key, holidayNavigationData);
      }
   }

}
