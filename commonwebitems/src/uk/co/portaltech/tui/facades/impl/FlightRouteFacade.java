/**
 *
 */
package uk.co.portaltech.tui.facades.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;

import uk.co.portaltech.travel.services.route.FlightRouteIndexService;
import uk.co.portaltech.tui.services.TuiUtilityService;


/**
 * @author gaurav.b
 *
 */
public class FlightRouteFacade
{

    @Resource
    private FlightRouteIndexService flightRouteIndexService;
    @Resource
    private TuiUtilityService tuiUtilityService;

    public FlightRouteFacade()
    {

    }

    public FlightRouteFacade(final FlightRouteIndexService flightRouteIndexService)
    {
        this.flightRouteIndexService = flightRouteIndexService;
    }

    public List<String> getFlightDates(final List<String> airportCodeList, final List<String> unitCodes, final String multiSelect)
    {
        List<String> unitCodeList = unitCodes;
        if (!CollectionUtils.isEmpty(unitCodeList))
        {
            final List<String> codeList = new ArrayList<String>();
            for (final String unit : unitCodeList)
            {
                final StringTokenizer unitCode = new StringTokenizer(unit, ":");
                codeList.add(unitCode.nextToken().toString());
            }
            unitCodeList = codeList;
        }
        return flightRouteIndexService.findFlightDates(airportCodeList, unitCodeList,
                tuiUtilityService.getFlightRouteReleventBrands(), multiSelect);
    }

    public List<String> getAllFlightsDates()
    {
        return flightRouteIndexService.findAllFlightDates(tuiUtilityService.getFlightRouteReleventBrands());
    }
}
