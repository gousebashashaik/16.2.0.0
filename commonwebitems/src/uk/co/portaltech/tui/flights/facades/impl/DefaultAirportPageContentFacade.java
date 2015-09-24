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
package uk.co.portaltech.tui.flights.facades.impl;

import javax.annotation.Resource;

import uk.co.portaltech.tui.flights.facades.AirportPageContentFacade;
import uk.co.tui.exception.FlightsBusinessException;
import uk.co.tui.flights.data.ContentViewData;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.service.AirportPageContentService;



/**
 * This class delegates call to service layer.
 */
public class DefaultAirportPageContentFacade implements AirportPageContentFacade
{
	@Resource
	private AirportPageContentService airportPageContentService;

	@Override
	public ContentViewData getAirportName(final String code) throws FlightsBusinessException
	{
		try
		{
			return airportPageContentService.getContentValue(code);
		}
		catch (final FlightsServiceException e)
		{
			throw new FlightsBusinessException("6004", e);
		}
	}

}
