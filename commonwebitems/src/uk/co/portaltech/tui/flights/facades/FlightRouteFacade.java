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

import uk.co.portaltech.travel.model.route.FlightRouteDTO;


/**
 * This interface delegates request from controller to service
 */
public interface FlightRouteFacade
{
   List<FlightRouteDTO> searchByDepartureAndArrivalAirports(final String departureCode, final String arrivalCode);
}
