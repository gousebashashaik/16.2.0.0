/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;

/**
 * @author sunilkumar.sahu
 *
 */
public class PassengerConfigPopulator implements Populator<SearchResultsRequestData, BasePackage>
{

   private List<Integer> childAges;

   private static final int TWO = 2;

   /**
    * Method to populate passengers from SearchResultsRequestData to InclusivePackageModel.
    *
    * @param source - SearchResultsRequestData object
    * @param target - InclusivePackageModel object
    */
   @SuppressWarnings("boxing")
   @Override
   public void populate(final SearchResultsRequestData source, final BasePackage target)
      throws ConversionException
   {
      final List<Passenger> passengerList = new ArrayList<Passenger>();
      final int passengerCount =
         source.getNoOfAdults() + source.getNoOfSeniors() + source.getNoOfChildren();
      final Map<Integer, PersonType> passengerMap = getPassengerMap(source);
      this.childAges = source.getChildAges();
      for (int i = 1; i <= passengerCount; i++)
      {
         final Passenger passengerModel = new Passenger();
         passengerModel.setId(i);
         passengerModel.setType(passengerMap.get(i));

         passengerModel.setName("Passenger" + i);
         if (passengerModel.getType() == PersonType.CHILD
            || passengerModel.getType() == PersonType.INFANT)
         {
            setAgeOfPassenger(passengerModel);
         }
         passengerList.add(passengerModel);
      }
      target.setPassengers(passengerList);
   }

   /**
    * @param passengerModel
    */
   @SuppressWarnings("boxing")
   private void setAgeOfPassenger(final Passenger passengerModel)
   {
      for (final Integer age : this.childAges)
      {
         if (checkIfPersonTypeIsChild(passengerModel, age))
         {
            passengerModel.setAge(age);
            this.childAges.remove(age);
            break;
         }
         else if (checkIfPersonTypeIsInfant(passengerModel, age))
         {
            passengerModel.setAge(age);
            this.childAges.remove(age);
            break;
         }
      }
   }

   /**
    * @param passengerModel
    * @param age
    * @return
    */
   private boolean checkIfPersonTypeIsInfant(final Passenger passengerModel, final Integer age)
   {
      return passengerModel.getType() == PersonType.INFANT && age < TWO;
   }

   /**
    * @param passengerModel
    * @param age
    * @return
    */
   private boolean checkIfPersonTypeIsChild(final Passenger passengerModel, final Integer age)
   {
      return passengerModel.getType() == PersonType.CHILD && age >= TWO;
   }

   /**
    * Method to create a map of passenger id with PassengerType.
    *
    * @param source - SearchResultsRequestData object
    * @return passengerMap
    */
   @SuppressWarnings("boxing")
   private Map<Integer, PersonType> getPassengerMap(final SearchResultsRequestData source)
   {

      final int adultCount = source.getNoOfAdults();
      final int seniorCount = source.getNoOfSeniors();
      final int infantCount = source.getInfantCount();
      final int childCount = source.getNoOfChildren() - infantCount;
      final int totalCount = adultCount + seniorCount + infantCount + childCount;
      final Map<Integer, PersonType> passengerMap = new HashMap<Integer, PersonType>();
      for (int id = 1; id <= totalCount; id++)
      {
         putIdsAccordinglyPassengerType(adultCount, seniorCount, childCount, passengerMap, id);
      }
      return passengerMap;
   }

   /**
    * @param adultCount
    * @param seniorCount
    * @param childCount
    * @param passengerMap
    * @param id
    */
   private void putIdsAccordinglyPassengerType(final int adultCount, final int seniorCount,
      final int childCount, final Map<Integer, PersonType> passengerMap, final int id)
   {
      if (id <= adultCount)
      {
         passengerMap.put(id, PersonType.ADULT);
      }
      else if (isIdUnderSeniorCategory(adultCount, seniorCount, id))
      {
         passengerMap.put(id, PersonType.SENIOR);
      }
      else if (isIdUnderChildCategory(adultCount, seniorCount, childCount, id))
      {
         passengerMap.put(id, PersonType.CHILD);
      }
      else
      {
         passengerMap.put(id, PersonType.INFANT);
      }
   }

   /**
    * @param adultCount
    * @param seniorCount
    * @param childCount
    * @param id
    * @return
    */
   private boolean isIdUnderChildCategory(final int adultCount, final int seniorCount,
      final int childCount, final int id)
   {
      return id > (adultCount + seniorCount) && id <= (adultCount + seniorCount + childCount);
   }

   /**
    * @param adultCount
    * @param seniorCount
    * @param id
    * @return
    */
   private boolean isIdUnderSeniorCategory(final int adultCount, final int seniorCount, final int id)
   {
      return id > adultCount && id <= (adultCount + seniorCount);
   }
}
