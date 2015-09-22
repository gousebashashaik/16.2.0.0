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
 * $RCSfile: CruiseNavigation.java$
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
import java.util.Set;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.tui.cruise.mainstream.data.CruiseAreaData;
import uk.co.tui.cruise.mainstream.data.LocationData;

/**
 * @author EXTAP2
 * @description cruise specific intrasite mega menu navigation
 */
public class CruiseNavigation extends IntrasiteNavigation
{
   /**
    * Detail of each cruise area available in cruise
    */
   private Map<CruiseAreaData, Set<LocationData>> cruiseAreaMap;

   /**
    * configured values required for cruise mega menu.
    */
   private Map<String, String> megaMenuConfigValues;

   /**
    * Top port of calls of cruise mega menu.
    */
   private List<LocationModel> locationModels;

   /**
    * @return the cruiseAreaMap
    */
   public Map<CruiseAreaData, Set<LocationData>> getCruiseAreaMap()
   {
      return cruiseAreaMap;
   }

   /**
    * @param cruiseAreaMap the cruiseAreaMap to set
    */
   public void setCruiseAreaMap(final Map<CruiseAreaData, Set<LocationData>> cruiseAreaMap)
   {
      this.cruiseAreaMap = cruiseAreaMap;
   }

   /**
    * @return the megaMenuConfigValues
    */
   public Map<String, String> getMegaMenuConfigValues()
   {
      return megaMenuConfigValues;
   }

   /**
    * @param megaMenuConfigValues the megaMenuConfigValues to set
    */
   public void setMegaMenuConfigValues(final Map<String, String> megaMenuConfigValues)
   {
      this.megaMenuConfigValues = megaMenuConfigValues;
   }

   /**
    * @return the locationModels
    */
   public List<LocationModel> getLocationModels()
   {
      return locationModels;
   }

   /**
    * @param locationModels the locationModels to set
    */
   public void setLocationModels(final List<LocationModel> locationModels)
   {
      this.locationModels = locationModels;
   }

}
