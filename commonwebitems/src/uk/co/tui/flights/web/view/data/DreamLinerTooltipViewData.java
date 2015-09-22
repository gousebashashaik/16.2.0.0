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
 * $RCSfile: DreamLinerTooltipViewData.java$
 *
 * $Revision: $
 *
 * $Date: Jan 14, 2015$
 *
 *
 *
 * $Log: $
 */
package uk.co.tui.flights.web.view.data;

import java.util.Map;


/**
 * @author phanisekhar.n
 *
 */
public class DreamLinerTooltipViewData
{
   private String code;

   private Map<String, String> contentValue;

   /**
    * @return the code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * @param code
    *           the code to set
    */
   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @return the contentValue
    */
   public Map<String, String> getContentValue()
   {
      return contentValue;
   }

   /**
    * @param contentValue
    *           the contentValue to set
    */
   public void setContentValue(final Map<String, String> contentValue)
   {
      this.contentValue = contentValue;
   }

}
