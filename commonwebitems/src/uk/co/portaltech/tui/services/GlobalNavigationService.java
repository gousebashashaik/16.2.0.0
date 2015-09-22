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
 * $RCSfile: GlobalNavigationService.java$
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

import uk.co.portaltech.tui.services.pojo.GlobalNavigation;
import uk.co.tui.cr.components.model.GlobalNavigationComponentModel;

/**
 * @author EXTAP2
 * 
 */
public interface GlobalNavigationService
{

   /**
    * 
    * @param siteBrand
    * @param globalComp
    * @return GlobalNavigation
    * @description This method take current site brand and current site global navigation component
    *              as input. GlobalNavigation will have header navigation of all thomson websites
    *              (TH,CR etc).
    */
   GlobalNavigation getGlobalNavigations(String siteBrand, GlobalNavigationComponentModel globalComp);
}
