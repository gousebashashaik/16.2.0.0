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
 * $RCSfile: IntrasiteNavigationService.java$
 * 
 * $Revision: $
 * 
 * $Date: Apr 20, 2015$
 * 
 * Author: abir.p
 * 
 * 
 * $Log: $
 */
package uk.co.portaltech.tui.services;

import uk.co.portaltech.tui.services.pojo.IntrasiteNavigation;

/**
 * @author abir.p
 * @description Interface with method that can take variable number of argument as parameter and
 *              return one target.
 */
public interface IntrasiteNavigationService<S>
{
   /**
    * 
    * @param sources
    * @return {@link IntrasiteNavigation}
    * @description Any new website's IntrasiteNavigation detail will be populated by implementation
    *              of this service. This method create IntrasiteNavigation detail of any website.
    */
   IntrasiteNavigation getIntraSiteNavigation(S... sources);
}
