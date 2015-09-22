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
 * $RCSfile: GlobalNavigation.java$
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

import java.util.List;
import java.util.Map;

/**
 * @author EXTAP2 This Pojo have data regarding global header navigation of all website
 */
public class GlobalNavigation
{

   /**
    * Each entry list is have header navigation of a website
    */
   private List<NavigationComponent> navigationComponents;

   /**
    * Global header navigation of all website. Key in the map is the name of tab. Value is the URL
    * of tab.
    */
   private Map<String, String> mainNavigations;

   /**
    * @return the mainNavigations
    */
   public Map<String, String> getMainNavigations()
   {
      return mainNavigations;
   }

   /**
    * @param mainNavigations the mainNavigations to set
    */
   public void setMainNavigations(final Map<String, String> mainNavigations)
   {
      this.mainNavigations = mainNavigations;
   }

   /**
    * @return the navigationComponents
    */
   public List<NavigationComponent> getNavigationComponents()
   {
      return navigationComponents;
   }

   /**
    * @param navigationComponents the navigationComponents to set
    */
   public void setNavigationComponents(final List<NavigationComponent> navigationComponents)
   {
      this.navigationComponents = navigationComponents;
   }

}
