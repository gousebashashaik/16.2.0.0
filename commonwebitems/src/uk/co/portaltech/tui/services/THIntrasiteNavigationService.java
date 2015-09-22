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
 * $RCSfile: THIntrasiteNavigationService.java$
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

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.tui.services.pojo.IntrasiteNavigation;

/**
 * @author EXTAP2
 * @description Thomson beach specific implementation of IntrasiteNavigationService.
 */
public class THIntrasiteNavigationService implements IntrasiteNavigationService
{

   /**
    * @param sources
    * @return IntrasiteNavigation
    * @see uk.co.portaltech.tui.services.IntrasiteNavigationService#getIntraSiteNavigation(java.lang.Object[])
    */
   @Override
   public IntrasiteNavigation getIntraSiteNavigation(final Object... sources)
   {
      final IntrasiteNavigation intrasiteNavigation = new IntrasiteNavigation();
      intrasiteNavigation.setSite(BrandType.TH.toString());
      return intrasiteNavigation;
   }

}
