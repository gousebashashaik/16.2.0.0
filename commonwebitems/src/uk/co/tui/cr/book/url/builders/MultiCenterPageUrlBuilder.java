/**
 *
 */
package uk.co.tui.cr.book.url.builders;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.Hotel;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.MultiCenterHolidayPackage;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.PackageComponentService;

/**
 * @author uday.g
 *
 */
public abstract class MultiCenterPageUrlBuilder extends ItineraryPageUrlBuilder
{

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   private static final TUILogUtils LOG = new TUILogUtils("MultiCenterPageUrlBuilder");

   private static final String PIPE = "|";

   private static final String ROOM_SEPERATOR = ",-";

   /** The Constant ZERO. */
   private static final int ZERO = 0;

   /**
    * @param packageType
    * @return searchVariant
    */
   protected String getSearchVariant(final PackageType packageType)
   {
      String searchVariant = StringUtils.EMPTY;
      if (packageType == PackageType.CNS)
      {
         searchVariant = "CRUISE_STAY_ATCOM";
      }
      else if (packageType == PackageType.SNC)
      {
         searchVariant = "STAY_CRUISE_ATCOM";
      }
      else
      {
         searchVariant = "BACK_BACK_ATCOM";
      }
      return searchVariant;
   }

   /**
    * @param packageType
    * @return searchVariant
    */
   protected String getSearchVariantHotel(final PackageType packageType)
   {
      String searchVariant = StringUtils.EMPTY;
      if (packageType == PackageType.CNS)
      {
         searchVariant = "CRUISE_STAY_ATCOM_HOTEL";
      }
      else if (packageType == PackageType.SNC)
      {
         searchVariant = "STAY_CRUISE_ATCOM_HOTEL";
      }
      return searchVariant;
   }

   /**
    * To construct the package id.
    *
    * @param pkgModel the pkg model
    * @return packageId
    */
   protected String getPackageId(final BasePackage pkgModel)
   {

      final StringBuilder flightInfoCode = new StringBuilder("");

      final StringBuilder packageId = new StringBuilder();

      final Stay cruiseStay = packageComponentService.getCruise(pkgModel);

      final Itinerary itinerary = packageComponentService.getFlightItinerary(pkgModel);
      final List<Leg> outBoundLegList = itinerary.getOutBound();
      final List<Leg> inBoundLegList = itinerary.getInBound();

      addSectorInformation(flightInfoCode, outBoundLegList);
      addSectorInformation(flightInfoCode, inBoundLegList);
      packageId.append(flightInfoCode).append(
         StringUtils.removeStart(((Cruise) cruiseStay).getJourneyCode(), "0"));

      packageId.append(((MultiCenterHolidayPackage) pkgModel).getItinCode());
      getStayInfoForMC(packageId, pkgModel);

      packageId.append(getSailingTime(outBoundLegList));
      getRoomsFromStay(packageId, packageComponentService.getAllStays(pkgModel));

      LOG.debug("Package ID generated for given Accommodation");
      return packageId.toString();

   }

   /**
    * @param pkgModel
    * @return roomsParam
    */
   protected String getRoomParam(final BasePackage pkgModel)
   {
      final Stay stay = packageComponentService.getCruise(pkgModel);
      final List<Room> rooms = stay.getRooms();
      final StringBuilder roomsParam = new StringBuilder("");
      int index = ZERO;
      for (final Room room : rooms)
      {
         index++;
         roomsParam.append(Integer.parseInt(Integer.toString(index))).append(PIPE);
         roomsParam.append(
            PassengerUtils.getPersonTypeCountFromPassengers(room.getPassgengers(),
               EnumSet.of(PersonType.ADULT))).append(PIPE);
         roomsParam.append(
            PassengerUtils.getPersonTypeCountFromPassengers(room.getPassgengers(),
               EnumSet.of(PersonType.SENIOR))).append(PIPE);

         roomsParam.append(
            PassengerUtils.getPersonTypeCountFromPassengers(room.getPassgengers(),
               EnumSet.of(PersonType.CHILD))).append(PIPE);
         roomsParam.append(
            PassengerUtils.getPersonTypeCountFromPassengers(room.getPassgengers(),
               EnumSet.of(PersonType.INFANT))).append(PIPE);
         roomsParam
            .append(getChildrenAgeAsString(PassengerUtils.getChildAges(room.getPassgengers())));
         roomsParam.append(ROOM_SEPERATOR);

      }
      return roomsParam.toString();

   }

   /**
    * @param packageId
    * @param allStays
    */
   private void getRoomsFromStay(final StringBuilder packageId, final List<Stay> allStays)
   {
      for (final Stay stay : allStays)
      {
         if (stay.getType().equals(StayType.SHIP))
         {
            packageId.append(getRoomCode(((Cruise) stay).getRooms()));
         }
         else
         {
            packageId.append(getRoomCode(((Hotel) stay).getRooms()));
         }
      }

   }
}
