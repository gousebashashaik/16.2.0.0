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
package uk.co.tui.validator;

import uk.co.tui.exception.InvalidParamException;

/**
 * @author abir.p
 * @param <S>
 * @description Interface For Validation
 */
public interface Validator<S>
{
   void validate(S... sources) throws InvalidParamException;
}
