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
 * $RCSfile: NavigationComponent.java$
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
package uk.co.portaltech.tui.services.pojo;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;

/**
 * @author EXTAP2
 * @description This POJO hold data of website of one brand
 */
public class NavigationComponent
{

   /**
    * content catalog id of a website
    */
   private String catalogId;

   /**
    * Mega menu of a website
    */
   private SimpleCMSComponentModel component;

   /**
    * Mega menu navigation detail of a website.
    */
   private IntrasiteNavigation intraSiteNavigation;

   /**
    * @return the catalogId
    */
   public String getCatalogId()
   {
      return catalogId;
   }

   /**
    * @param catalogId the catalogId to set
    */
   public void setCatalogId(final String catalogId)
   {
      this.catalogId = catalogId;
   }

   /**
    * @return the component
    */
   public SimpleCMSComponentModel getComponent()
   {
      return component;
   }

   /**
    * @param component the component to set
    */
   public void setComponent(final SimpleCMSComponentModel component)
   {
      this.component = component;
   }

   /**
    * @return the intraSiteNavigation
    */
   public IntrasiteNavigation getIntraSiteNavigation()
   {
      return intraSiteNavigation;
   }

   /**
    * @param intraSiteNavigation the intraSiteNavigation to set
    */
   public void setIntraSiteNavigation(final IntrasiteNavigation intraSiteNavigation)
   {
      this.intraSiteNavigation = intraSiteNavigation;
   }

}
