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
 * $RCSfile: IntrasiteNavigation.java$
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

/**
 * @author EXTAP2 Basic data required to get IntrasiteNavigation of a website. Each website specific
 *         Navigation POJO will extend this POJO
 */
public class IntrasiteNavigation
{

   /**
    * Site brand/id
    */
   private String site;

   /**
    * @return the site
    */
   public String getSite()
   {
      return site;
   }

   /**
    * @param site the site to set
    */
   public void setSite(final String site)
   {
      this.site = site;
   }
}
