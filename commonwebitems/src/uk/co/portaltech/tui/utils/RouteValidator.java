/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.DateRangeProviderUtil;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.impl.AirportFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.AirportSearchResult;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.ErrorData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;




/**
 * @author laxmibai.p
 *
 */

public class RouteValidator
{

    @Resource
    private TuiUtilityService tuiUtilityService;

    private AirportFacade airportFacade;

    private LocationFacade locationFacade;

    private static final String NO_ROUTE_FOUND_ON = "NO_ROUTE_FOUND_ON";
    private static final String NO_ROUTE_FOUND_TO = "NO_ROUTE_FOUND_TO";


    private static final String INVALID_WHEN = "INVALID_WHEN";
    private static final String INVALID_WHEN_FORMAT = "INVALID_WHEN_FORMAT";
    private static final String INVALID_WHEN_DATE = "INVALID_WHEN_DATE";



    public RouteValidator()
    {
        //Default constructor
    }

    public List<ErrorData> validateRoutes( DeepLinkRequestData deepLinkRequestData,  List<ErrorData> errors)
    {
        List<String> dates = new ArrayList<String>();
         List<String> unitCodes = new ArrayList<String>();
         List<String> airports = new ArrayList<String>();
        if (StringUtils.isNotEmpty(deepLinkRequestData.getWhen()))
        {
            if (deepLinkRequestData.isFlexibility())
            {
                dates = DateRangeProviderUtil.getAllvaliddate(deepLinkRequestData.getWhen(), deepLinkRequestData.getFlexibleDays());
            }
            else
            {
                dates = new ArrayList<String>();
                 boolean error = getWhenErrors(errors);
                if (!error)
                {
                    dates.add(deepLinkRequestData.getWhen());
                }
            }
        }
        getUnitsCodes(deepLinkRequestData.getUnits(), unitCodes);
        getAirportCodes(deepLinkRequestData.getAirports(), airports);
        validateAirportsRoutes(deepLinkRequestData, errors, dates, unitCodes);
        return errors;
    }


    private boolean getWhenErrors( List<ErrorData> errors)
    {
        for ( ErrorData errorData : errors)
        {
            if (INVALID_WHEN.equals(errorData.getCode()) || INVALID_WHEN_FORMAT.equals(errorData.getCode())
                    || INVALID_WHEN_DATE.equals(errorData.getCode()))
            {
                return true;
            }
        }
        return false;
    }

    private void validateAirportsRoutes( DeepLinkRequestData deepLinkRequestData,  List<ErrorData> errors,
             List<String> dates,  List<String> unitCodes)
    {
        for ( AirportData airport : deepLinkRequestData.getAirports())
        {
             AirportSearchResult searchResult = airportFacade.find(airport.getId(), unitCodes, dates,
                    tuiUtilityService.getFlightRouteReleventBrands());
            if (searchResult.getError() != null)
            {
                if (StringUtils.equals(searchResult.getError().getCode(), NO_ROUTE_FOUND_ON))
                {
                    addToErrors(ValidationErrorMessage.NO_ROUTE_FOUND_ON_FROM_AIRPORT, errors);
                }
                else if (StringUtils.equals(searchResult.getError().getCode(), NO_ROUTE_FOUND_TO))
                {
                    addToErrors(ValidationErrorMessage.NO_ROUTE_FOUND_TO_FROM_AIRPORT, errors);
                }

            }
            setValidStatusToAirportData(deepLinkRequestData, searchResult);

        }

    }


    private void setValidStatusToAirportData( DeepLinkRequestData deepLinkRequestData,  AirportSearchResult searchResult)
    {

         List<AirportData> airports = searchResult.getAirports();
        for ( AirportData airport : deepLinkRequestData.getAirports())
        {
             boolean available = findAvailability(airport, airports);
            airport.setAvailable(available);
        }
    }

    private boolean findAvailability( AirportData requestedAirport,  List<AirportData> airports)
    {
        for ( AirportData airportData : airports)
        {
            if (airportData.getId().equals(requestedAirport.getId()))
            {
                return true;
            }


        }
        return false;
    }

    public void getUnitsCodes( List<UnitData> unitDatas,  List<String> unitCodes)
    {

        for ( UnitData unit : unitDatas)
        {
            unitCodes.add(unit.getId());
        }
    }

    public void getAirportCodes( List<AirportData> airportDatas,  List<String> airports)
    {
        for ( AirportData airport : airportDatas)
        {
            airports.add(airport.getId());
        }
    }

    public void addToErrors( ValidationErrorMessage errorType,  List<ErrorData> errors)
    {
         ErrorData errorData = new ErrorData();
        errorData.setCode(errorType.getCode());
        errorData.setDescription(errorType.getDescription());
        errors.add(errorData);
    }

    /**
     * @return the locationFacade
     */
    public LocationFacade getLocationFacade()
    {
        return locationFacade;
    }

    /**
     * @param locationFacade
     *           the locationFacade to set
     */
    public void setLocationFacade( LocationFacade locationFacade)
    {
        this.locationFacade = locationFacade;
    }

    /**
     * @return the airportFacade
     */
    public AirportFacade getAirportFacade()
    {
        return airportFacade;
    }

    /**
     * @param airportFacade
     *           the airportFacade to set
     */
    public void setAirportFacade(final AirportFacade airportFacade)
    {
        this.airportFacade = airportFacade;
    }
}
