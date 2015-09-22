/**
 *
 */
package uk.co.tui.cr.book.populators;

import static uk.co.tui.cr.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.MultiCenterUnits;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.portaltech.travel.thirdparty.endeca.constants.EndecasearchConstants;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.cruise.search.data.MainSearchCriteria;

/**
 * @author ramkishore.p
 *
 */
public class PassengerPopulator implements Populator<PackageItemValue, BasePackage>
{

   /** The session service. */
   @Resource
   private SessionService sessionService;

   private static final String CRUISE_STAY = "CRZ";

   private static final int INDEX_TWO = 2;

   /*
    * (non-Javadoc)
    *
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final PackageItemValue packageItemValue, final BasePackage target)
      throws ConversionException
   {
      final List<Passenger> passengerList = new ArrayList<Passenger>();
      final List<RoomDetails> roomDetails =
         getAccom(packageItemValue.getPckSailingInfo().get(0).getMulitCenterUnits(), CRUISE_STAY)
            .getRoomDetails();
      final Map<Integer, PersonType> passengerMap = getPassengerMap(roomDetails);
      List<Integer> childAges = getChildAges();
      for (final RoomDetails eachRoomDetails : roomDetails)

      {

         for (final PaxDetail eachPax : eachRoomDetails.getOccupancy().getPaxDetail())

         {

            final Passenger passengerModel = new Passenger();

            passengerModel.setId(Integer.valueOf(eachPax.getId()));

            passengerModel.setType(passengerMap.get(Integer.valueOf(eachPax.getId())));

            passengerModel.setName("Passenger" + eachPax.getId());

            childAges =
               setAgeOfPassenger(passengerModel, childAges, Integer.valueOf(eachPax.getAge()));

            passengerModel.setAddresses(new ArrayList<Address>());

            passengerModel.setExtraFacilities(new ArrayList<ExtraFacility>());

            passengerList.add(passengerModel);

         }

      }
      Collections.sort(passengerList, new Comparator<Passenger>()
      {
         @Override
         public int compare(final Passenger passenger1, final Passenger passenger2)
         {
            return passenger1.getId().intValue() - passenger2.getId().intValue();
         }
      });
      target.setPassengers(passengerList);
   }

   /**
    * Gets the child ages.
    *
    * @return the child ages
    */
   @SuppressWarnings(BOXING)
   private List<Integer> getChildAges()
   {
      final MainSearchCriteria mainSearchCriteria =
         sessionService.getAttribute(EndecasearchConstants.MAIN_SEARCH_CRITERIA);
      return (mainSearchCriteria != null) ? mainSearchCriteria.getChildAges()
         : new ArrayList<Integer>();
   }

   private List<Integer> setAgeOfPassenger(final Passenger passengerModel,
      final List<Integer> childAges, final Integer eachAge)
   {
      for (final Integer age : childAges)
      {
         if (isValidInfant(passengerModel, age.intValue()))
         {
            passengerModel.setAge(age);
            childAges.remove(age);
            return childAges;
         }
         else
         {
            passengerModel.setAge(eachAge);
         }
      }
      if(childAges.isEmpty()){
         passengerModel.setAge(eachAge);
      }
      return childAges;
   }

   /**
    * Checks if is valid infant.
    *
    * @param passengerModel the passenger model
    * @param age the age
    * @return true, if is valid infant
    */
   private boolean isValidInfant(final Passenger passengerModel, final int age)
   {
      return passengerModel.getType() == PersonType.INFANT && age < INDEX_TWO;
   }

   /**
    * @param multiCentreData
    * @return Accom
    */
   private MultiCenterUnits getAccom(final List<MultiCenterUnits> multiCentreData,
      final String stayType)
   {
      MultiCenterUnits sourceAccom = null;
      for (final MultiCenterUnits multiCentreDatum : multiCentreData)
      {
         if (StringUtils.equalsIgnoreCase(stayType, multiCentreDatum.getStayType()))
         {
            sourceAccom = multiCentreDatum;
            break;
         }
      }
      return sourceAccom;
   }

   /**
    * Method to create a map of passenger id with PassengerType.
    *
    * @param rooms the rooms
    * @return passengerMap
    */
   @SuppressWarnings(BOXING)
   private Map<Integer, PersonType> getPassengerMap(final List<RoomDetails> rooms)
   {

      final int adultCount = getAdultCount(rooms);
      final int childCount = getChildCount(rooms);
      final int totalCount = adultCount + childCount + getInfantCount(rooms);
      final Map<Integer, PersonType> passengerMap = new HashMap<Integer, PersonType>();
      for (int id = 1; id <= totalCount; id++)
      {
         addPassengerToMap(adultCount, childCount, passengerMap, id);
      }
      return passengerMap;
   }

   /**
    * Gets the adult count.
    *
    * @param rooms the rooms
    * @return the adult count
    */
   private int getAdultCount(final List<RoomDetails> rooms)
   {
      int adultCount = 0;
      for (final RoomDetails room : rooms)
      {
         adultCount += room.getOccupancy().getAdults();
      }
      return adultCount;
   }

   /**
    * Gets the child count.
    *
    * @param rooms the rooms
    * @return the child count
    */
   private int getChildCount(final List<RoomDetails> rooms)
   {
      int childCount = 0;
      for (final RoomDetails room : rooms)
      {
         childCount += room.getOccupancy().getChildren();
      }
      return childCount;
   }

   /**
    * Gets the child count.
    *
    * @param rooms the rooms
    * @return the child count
    */
   private int getInfantCount(final List<RoomDetails> rooms)
   {
      int childCount = 0;
      for (final RoomDetails room : rooms)
      {
         childCount += room.getOccupancy().getInfant();
      }
      return childCount;
   }

   /**
    * Adds the passenger to map.
    *
    * @param adultCount the adult count
    * @param childCount the child count
    * @param passengerMap the passenger map
    * @param id the id
    */
   private void addPassengerToMap(final int adultCount, final int childCount,
      final Map<Integer, PersonType> passengerMap, final int id)
   {
      if (id <= adultCount)
      {
         passengerMap.put(Integer.valueOf(id), PersonType.ADULT);
      }
      else if (id > (adultCount) && id <= (adultCount + childCount))
      {
         passengerMap.put(Integer.valueOf(id), PersonType.CHILD);
      }
      else
      {
         passengerMap.put(Integer.valueOf(id), PersonType.INFANT);
      }
   }

}
