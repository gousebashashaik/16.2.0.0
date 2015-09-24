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
import java.util.Map;

import uk.co.tui.exception.FlightsBusinessException;
import uk.co.tui.flights.data.AirportData;
import uk.co.tui.flights.data.WhereWeFlyData;
import uk.co.tui.flights.exception.FlightsServiceException;



/**
 * This class is used to delegate map related request to service from controller
 */
public interface FlightsMapFacade
{


   Map<String, List<AirportData>> getDepartingFromAirports() throws FlightsBusinessException;


   /**
    * @return
    * @throws FlightsServiceException
    */
   WhereWeFlyData getWhereWeFlyData() throws FlightsServiceException;

}
