/**
 *
 */
package uk.co.portaltech.tui.populators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import uk.co.portaltech.tui.facades.impl.FlightRouteFacade;
import uk.co.portaltech.tui.utils.RouteValidator;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

/**
 * @author laxmibai.p
 *
 */
public class TravelDatesPopulator
{

   private RouteValidator routeValidator;

   private FlightRouteFacade flightRouteFacade;

   public void populateTraveleDates(DeepLinkRequestData deepLinkRequestData,
      DeepLinkRequestViewData deepLinkRequestViewData)
   {

      // FlightRouteFacade flightRouteFacade = Registry

      List<AirportData> airportDatas = deepLinkRequestData.getAirports();
      List<UnitData> unitDatas = deepLinkRequestData.getUnits();

      List<String> travelDates = new ArrayList<String>();

      List<String> unitCodeList = new ArrayList<String>();
      List<String> airportCodeList = new ArrayList<String>();

      routeValidator.getUnitsCodes(unitDatas, unitCodeList);
      routeValidator.getAirportCodes(airportDatas, airportCodeList);

      if (!CollectionUtils.isEmpty(airportCodeList) && !CollectionUtils.isEmpty(unitCodeList))
      {
         travelDates.addAll(flightRouteFacade.getFlightDates(airportCodeList, unitCodeList, null));

      }
      else if (!CollectionUtils.isEmpty(airportCodeList) && CollectionUtils.isEmpty(unitCodeList))
      {

         travelDates.addAll(flightRouteFacade.getFlightDates(airportCodeList, null, null));

      }
      else if (!CollectionUtils.isEmpty(unitCodeList) && CollectionUtils.isEmpty(airportCodeList))
      {

         travelDates.addAll(flightRouteFacade.getFlightDates(null, unitCodeList, null));
      }
      else
      {
         travelDates.addAll(flightRouteFacade.getAllFlightsDates());
      }

      deepLinkRequestViewData.setAvailableDates(travelDates);
   }

   /**
    * @return the routeValidator
    */
   public RouteValidator getRouteValidator()
   {
      return routeValidator;
   }

   /**
    * @param routeValidator the routeValidator to set
    */
   public void setRouteValidator(RouteValidator routeValidator)
   {
      this.routeValidator = routeValidator;
   }

   /**
    * @return the flightRouteFacade
    */
   public FlightRouteFacade getFlightRouteFacade()
   {
      return flightRouteFacade;
   }

   /**
    * @param flightRouteFacade the flightRouteFacade to set
    */
   public void setFlightRouteFacade(FlightRouteFacade flightRouteFacade)
   {
      this.flightRouteFacade = flightRouteFacade;
   }
}
