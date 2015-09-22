/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with hybris.
 */
package uk.co.tui.constants;

/**
 *
 */
public final class RegexPatterns
{

   public static final String PATTERN_FOR_COLON_SEPERATED = "([a-zA-Z0-9// |:_-])*";

   public static final String PATTERN_FOR_WHEN = "^[\\d]{2}-[\\d]{2}-[\\d]{4}$";

   public static final String PATTERN_FOR_WHEN_1 = "^[a-zA-Z0-9]*$";

   public static final String PATTERN_FOR_BOOLEAN = "(true|false|TRUE|FALSE)*";

   public static final String PATTERN_FOR_CHILDREN_AGE = "([0-9]+([,][0-9]+)*)*";

   public static final String PATTERN_FOR_DURATION = "([0-9]+([-][0-9]+)*)*";

   public static final String PATTERN_FOR_NUMBERS = "[0-9]*";

   public static final String PATTERN_FOR_ALPHABETS = "[a-zA-Z]*";

   public static final String PATTERN_FOR_SEARCH_VARIANT = "([a-zA-Z_])*";

   public static final String UTF_8 = "UTF-8";

   private RegexPatterns()
   {
      // private constructor
   }

}
