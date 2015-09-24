/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package uk.co.portaltech.tui.flights.facades;

import java.util.List;

import uk.co.tui.flights.data.ContentMediaViewData;
import uk.co.tui.flights.data.ContentViewData;
import uk.co.tui.flights.web.view.data.BeforeYouFlyViewData;



/**
 * @author sivaramya.t
 * @description -This interface contains all the method definitions.
 */
public interface ContentFacade
{
   /**
    * This method is used to get epic content with given parameters
    *
    * @param code
    * @param names
    * @return {@link BeforeYouFlyViewData}
    *
    */
   BeforeYouFlyViewData getBeforeYouFlyViewData(final String code, final List<String> names);

   /**
    *
    *
    * @param code
    * @return {@link ContentViewData}
    */
   ContentViewData getContentValue(final String code, final String name);

   /**
    * This method is used to get epic image with given code
    *
    * @param code
    * @return List<ContentMediaViewData>
    */
   List<ContentMediaViewData> getContentItemMedia(final String code);

   /**
    * This method is used to get epic image with given parameters
    *
    * @param code
    * @return List<ContentMediaViewData>
    */
   List<ContentMediaViewData> getCarouselImages(final String code);

}
