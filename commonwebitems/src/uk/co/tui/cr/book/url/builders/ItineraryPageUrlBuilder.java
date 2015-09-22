/**
 *
 */
package uk.co.tui.cr.book.url.builders;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.DateUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.Hotel;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.cruise.search.data.DestinationData;

/**
 * @author uday.g
 *
 */
public abstract class ItineraryPageUrlBuilder
{
   
   private static final String CRUISE_STAY = "CS";
   
   private static final String STAY_CRUISE = "SC";
   
   private static final String PIPE = "|";
   
   private static final String COLON = ":";
   
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;
   
   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;
   
   public abstract String build();
   
   /**
    * @param packageType
    */
   protected String getStayType(final PackageType packageType)
   {
      if (packageType == PackageType.CNS)
      {
         return CRUISE_STAY;
      }
      else if (packageType == PackageType.SNC)
      {
         return STAY_CRUISE;
      }
      return null;
   }
   
   /**
    * Checks if is stay before.
    *
    * @param packageType the package type
    * @return true, if is stay before
    */
   protected boolean isStayBefore(final PackageType packageType)
   {
      boolean searchVariant = false;
      if (packageType == PackageType.CNS)
      {
         searchVariant = false;
      }
      else if (packageType == PackageType.SNC)
      {
         searchVariant = true;
      }
      return searchVariant;
   }
   
   /**
    * @param pkgModel
    */
   protected String getCruiseDuration(final BasePackage pkgModel)
   {
      return String.valueOf(packageComponentService.getCruise(pkgModel).getDuration());
      
   }
   
   /**
    * @param pkgModel
    */
   protected String getOutBoundAtComId(final BasePackage pkgModel)
   {
      final Itinerary flightItinerary = packageComponentService.getFlightItinerary(pkgModel);
      return ((FlightLeg) flightItinerary.getOutBound().get(0)).getAtcomId();
      
   }
   
   /**
    * Gets the in bound at com id.
    *
    * @param pkgModel the pkg model
    * @return the in bound at com id
    */
   protected String getInBoundAtComId(final BasePackage pkgModel)
   {
      final Itinerary flightItinerary = packageComponentService.getFlightItinerary(pkgModel);
      return ((FlightLeg) flightItinerary.getInBound().get(0)).getAtcomId();
      
   }
   
   /**
    * Gets the stay duration.
    *
    * @param pkgModel the pkg model
    * @return the stay duration
    */
   protected String getStayDuration(final BasePackage pkgModel)
   {
      Stay hotelAccom = null;
      if (packageComponentService.getHotel(pkgModel) != null)
      {
         hotelAccom = packageComponentService.getHotel(pkgModel);
         return String.valueOf(hotelAccom.getDuration());
      }
      return "0";
   }
   
   /**
    * @param destinationData
    * @return to[]
    */
   protected String getTODatas(final List<DestinationData> destinationData)
   {
      final StringBuilder to = new StringBuilder("");
      for (final DestinationData destinationDatum : destinationData)
      {
         to.append(destinationDatum.getId()).append(COLON).append(destinationDatum.getType())
            .append(PIPE);
      }
      return to.toString();
   }
   
   /**
    *
    */
   protected Object getFromDatas(final List<String> ukAirports, final List<String> ukPOCs)
   {
      final StringBuilder from = new StringBuilder("");
      String pipe = "";
      for (final String airport : ukAirports)
      {
         from.append(pipe).append(airport).append(COLON).append("Airport");
         pipe = PIPE;
      }
      for (final String port : ukPOCs)
      {
         from.append(pipe).append(port).append(COLON).append("PORT");
         pipe = PIPE;
      }
      return from.toString();
   }
   
   /**
    * Gets the departure airport code.
    *
    * @param outBoundLegList the out bound leg list
    * @return the departure airport code
    */
   protected String getDepartureAirportCode(final List<Leg> outBoundLegList)
   {
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return outBoundLegList.get(0).getDepartureAirport().getCode();
      }
      return "";
   }
   
   /**
    * To return the DepartureDate.
    *
    * @param outBoundLegList the out bound leg list
    * @return Departure Date
    */
   protected String getDepartureDate(final List<Leg> outBoundLegList)
   {
      final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy");
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return dateFormat.format(outBoundLegList.get(0).getSchedule().getDepartureDate());
      }
      
      return "";
   }
   
   /**
    * To return the DepartureDate.
    *
    * @param outBoundLegList the out bound leg list
    * @return Departure Date
    */
   protected String getSailingTime(final List<Leg> outBoundLegList)
   {
      if (CollectionUtils.isNotEmpty(outBoundLegList) && null != outBoundLegList.get(0))
      {
         return String.valueOf(outBoundLegList.get(0).getSchedule().getDepartureDate().getTime());
      }
      return "";
   }
   
   /**
    * Gets the children age as string.
    *
    * @param childrenAge the children age
    * @return the children age as string
    */
   protected String getChildrenAgeAsString(final List<Integer> childrenAge)
   {
      final StringBuilder ages = new StringBuilder("");
      String comma = "";
      for (final Integer age : childrenAge)
      {
         ages.append(comma).append(age.intValue());
         comma = ",";
      }
      return ages.toString();
   }
   
   /**
    * The method fetches the package model from the cart.
    *
    * @return BasePackage
    */
   protected BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }
   
   /**
    * To get room code.
    *
    * @param rooms the rooms
    * @return roomCode
    */
   protected Object getRoomCode(final Collection<Room> rooms)
   {
      final StringBuilder roomCode = new StringBuilder("");
      for (final Room selectedRoomModel : rooms)
      {
         roomCode.append(selectedRoomModel.getCode());
      }
      return roomCode.toString();
   }
   
   /**
    * To append the departure date and time milliseconds and arrival date and time milliseconds and
    * carrier code.
    *
    * @param flightInfoCode the flight info code
    * @param outBoundLegList the out bound leg list
    */
   protected void addSectorInformation(final StringBuilder flightInfoCode,
      final List<Leg> outBoundLegList)
   {
      
      for (final Leg legModel : outBoundLegList)
      {
         
         if (null != legModel.getSchedule())
         {
            flightInfoCode.append(
               String.valueOf(getDate(legModel.getSchedule().getDepartureDate(), legModel
                  .getSchedule().getDepartureTime()))).append(
               String.valueOf(getDate(legModel.getSchedule().getArrivalDate(), legModel
                  .getSchedule().getArrivalTime())));
            final String flightNum = legModel.getCarrier().getNumber();
            if (StringUtils.isNotEmpty(flightNum))
            {
               final StringBuilder flightCode = new StringBuilder("");
               flightCode.append(legModel.getCarrier().getCode()).append(
                  legModel.getCarrier().getNumber());
               flightInfoCode.append(flightCode);
            }
            
         }
         
      }
   }
   
   /**
    * @param packageId
    * @param pkgModel
    */
   protected void getStayInfoForMC(final StringBuilder packageId, final BasePackage pkgModel)
   {
      for (final Stay stay : packageComponentService.getAllStays(pkgModel))
      {
         if (stay.getType().equals(StayType.SHIP))
         {
            packageId.append(((Cruise) stay).getJourneyCode());
         }
         else
         {
            packageId.append(((Hotel) stay).getCode());
         }
      }
      
   }
   
   private long getDate(final Date date, final String time)
   {
      return DateUtils.toDateTimeFormat(DateUtils.formatForDate(date, DateUtils.DATE_FORMAT), time,
         DateUtils.DATE_FORMAT_9).getMillis();
   }
}
