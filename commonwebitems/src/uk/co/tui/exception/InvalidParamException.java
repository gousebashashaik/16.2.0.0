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
 * $RCSfile: AbstractComplexPopulator.java$
 *
 * $Revision: $
 *
 * $Date: Mar 19, 2014$
 *
 * Author: abir.p
 *
 *
 * $Log: $
 */
package uk.co.tui.exception;

import uk.co.tui.exception.TUIBusinessException;

/**
 * @author abir.p
 * @description Exception Class For Invalid Search Result Param
 */
public class InvalidParamException extends TUIBusinessException
{

   public InvalidParamException(final String message)
   {
      super(message);
   }

   public InvalidParamException(final String code, final Throwable throwable)
   {
      super(code, throwable);
   }
}
