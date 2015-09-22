/**
 *
 */
package uk.co.tui.fo.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.fo.book.view.data.AgeProfile;
import uk.co.tui.fo.book.view.data.InsuranceContainerViewData;
import uk.co.tui.fo.book.view.data.InsurancePassengerViewData;
import uk.co.tui.fo.book.view.data.InsuranceViewData;

/**
 * @author pradeep.as
 *
 */
public class InsuranceContainerViewDataPopulatorLite implements
   Populator<List<ExtraFacilityCategory>, InsuranceContainerViewData>
{

   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;

   /** The message property reader. */
   @Resource
   private PropertyReader messagePropertyReader;

   private static final String NOT_APPLICABLE = "NOT APPLICABLE";

   private static final String EIGHTY_FIVE = "85";

   /** The insurance view data populator. */
   @Resource(name = "foInsuranceViewDataPopulatorLite")
   private InsuranceViewDataPopulatorLite insuranceViewDataPopulatorLite;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final List<ExtraFacilityCategory> source,
      final InsuranceContainerViewData target)
   {
      final List<InsuranceViewData> insuranceViewDataList = new ArrayList<InsuranceViewData>();
      final List<InsurancePassengerViewData> insPassViewDataList =
         new ArrayList<InsurancePassengerViewData>();
      for (final ExtraFacilityCategory categoryModel : source)
      {
         if (SyntacticSugar.isNotNull(categoryModel))
         {
            populateInsuranceViewData(insuranceViewDataList, insPassViewDataList, categoryModel);
         }
      }
      target.setInsViewData(insuranceViewDataList);
      target.setInsPasViewData(insPassViewDataList);
      target.setOverAgeMessage(getOverAgeMessage());
   }

   /**
    * Gets the over age message.
    *
    * @return the over age message
    */
   private String getOverAgeMessage()
   {
      return messagePropertyReader.getValue("select_over_age_alert_message_fo");
   }

   /**
    * @param insuranceViewDataList
    * @param insPassViewDataList
    * @param categoryModel
    */
   private void populateInsuranceViewData(final List<InsuranceViewData> insuranceViewDataList,
      final List<InsurancePassengerViewData> insPassViewDataList,
      final ExtraFacilityCategory categoryModel)
   {
      if (CollectionUtils.isNotEmpty(categoryModel.getExtraFacilities()))
      {
         for (final ExtraFacility extraFacility : categoryModel.getExtraFacilities())
         {
            final InsuranceViewData insuranceViewData = new InsuranceViewData();
            final InsuranceExtraFacility insExtraFacility = (InsuranceExtraFacility) extraFacility;

            insuranceViewDataPopulatorLite.populate(insExtraFacility, insuranceViewData);
            // To do :- Temporary fix
            populateInsurancePaxViewData(extraFacility, insPassViewDataList);
            insuranceViewDataList.add(insuranceViewData);
         }
      }
   }

   /**
    * Populate insurance pax view data.
    *
    * @param extraFacility the extra facility
    * @param insPassViewDataList the ins pass view data list
    */
   private void populateInsurancePaxViewData(final ExtraFacility extraFacility,
      final List<InsurancePassengerViewData> insPassViewDataList)
   {
      if ((!CollectionUtils.isEmpty(extraFacility.getPrices()))
         && extraFacility.getPrices().get(0).getPriceProfile() != null)
      {
         populateInsPassengerViewData(extraFacility.getPrices(), insPassViewDataList);
      }
   }

   /**
    * @param prices
    * @param insPassViewDataList
    */
   private void populateInsPassengerViewData(final List<Price> prices,
      final List<InsurancePassengerViewData> insPassViewDataList)
   {
      InsurancePassengerViewData insPassViewData = null;
      final int adultCount = getAdultCount();
      final BasePackage packageModel = getPackageModel();
      sortPassengerById(packageModel);

      for (final Passenger passenger : packageModel.getPassengers())
      {
         if (passenger.getType() != PersonType.INFANT)
         {
            insPassViewData = new InsurancePassengerViewData();
            insPassViewData.setId(passenger.getId().toString());
            if (passenger.getType() != PersonType.CHILD)
            {
               insPassViewData.setName("Adult " + (passenger.getId().intValue()));
            }
            else
            {
               insPassViewData.setName("Child " + (passenger.getId().intValue() - adultCount));
               insPassViewData.setChild(true);
            }
            insPassViewData.setAge(passenger.getAge() + " yrs");
            insPassViewData.setSelected(true);
            insPassViewData.setAgeProfile(getAgeProfile(prices, passenger.getType()));
            insPassViewDataList.add(insPassViewData);
         }
      }
   }

   /**
    * Sort passenger by id.
    *
    * @param packageModel the package model
    */
   private void sortPassengerById(final BasePackage packageModel)
   {
      final List<Passenger> passengers = new ArrayList<Passenger>(packageModel.getPassengers());
      Collections.sort(passengers, new Comparator<Passenger>()
      {
         @Override
         public int compare(final Passenger p1, final Passenger p2)
         {
            return p1.getId().compareTo(p2.getId());
         }
      });
      packageModel.setPassengers(passengers);
   }

   /**
    * Gets the age profile.
    *
    * @param prices the prices
    * @param personType the person type
    * @return the age profile
    */
   private List<AgeProfile> getAgeProfile(final List<Price> prices, final PersonType personType)
   {
      final List<AgeProfile> ageProfileList = new ArrayList<AgeProfile>();
      for (final Price price : prices)
      {
         if (isChildType(personType, price))
         {
            ageProfileList.add(createAgeProfile(price));
            break;
         }
         else if (isNotChildType(personType, price))
         {
            ageProfileList.add(createAgeProfile(price));
         }
      }
      notChildType(personType, ageProfileList);
      return ageProfileList;
   }

   /**
    * @param personType
    * @param ageProfileList
    */
   private void notChildType(final PersonType personType, final List<AgeProfile> ageProfileList)
   {
      if (personType != PersonType.CHILD)
      {
         ageProfileList.add(createEightyFivePlusAgeProfile());
      }
   }

   /**
    * @param personType
    * @param price
    * @return boolean
    */
   private boolean isNotChildType(final PersonType personType, final Price price)
   {
      return StringUtil.isNotEquals(price.getPriceProfile().getPersonType().toString(),
         PersonType.INFANT.toString())
         && StringUtil.isNotEquals(price.getPriceProfile().getPersonType().toString(),
            PersonType.CHILD.toString()) && personType != PersonType.CHILD;
   }

   /**
    * @param personType
    * @param price
    * @return boolean
    */
   private boolean isChildType(final PersonType personType, final Price price)
   {
      return StringUtils.equalsIgnoreCase(price.getPriceProfile().getPersonType().toString(),
         PersonType.CHILD.toString()) && personType == PersonType.CHILD;
   }

   /**
    * @return AgeProfile
    */
   private AgeProfile createEightyFivePlusAgeProfile()
   {
      final AgeProfile ageProfile = new AgeProfile();
      ageProfile.setAgeCode(NOT_APPLICABLE);
      ageProfile.setAgeRange("Age " + EIGHTY_FIVE + "+");
      return ageProfile;

   }

   /**
    * Creates the age profile.
    *
    * @param price the price
    * @return the age profile
    */
   private AgeProfile createAgeProfile(final Price price)
   {
      final AgeProfile ageProfile = new AgeProfile();
      ageProfile.setAgeCode(price.getPriceProfile().getPersonType().toString());
      ageProfile.setAgeRange("Age " + price.getPriceProfile().getMinAge() + "-"
         + price.getPriceProfile().getMaxAge());
      if (PersonType.ADULT == price.getPriceProfile().getPersonType())
      {
         ageProfile.setSelected("selected");
      }
      return ageProfile;
   }

   /**
    * The method fetches the package model from the cart.
    *
    * @return PackageModel
    */
   private BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * This method gets the adult count using the list of passengers.
    *
    * @return adultCount
    */
   private int getAdultCount()
   {
      final List<Passenger> passengers = getPackageModel().getPassengers();

      return PassengerUtils.getPersonTypeCountFromPassengers(passengers,
         EnumSet.of(PersonType.ADULT, PersonType.TEEN, PersonType.SENIOR, PersonType.SUPERSENIOR));

   }
}
