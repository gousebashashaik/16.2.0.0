/**
 *
 */
package uk.co.tui.th.book.facade.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.FacilityTypeModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.facility.FacilityTypeService;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.th.book.facade.AllInclusiveBoardFacade;
import uk.co.tui.th.book.view.data.BoardBasisViewData;
import uk.co.tui.th.book.view.data.RoomOptionsContentViewData;

/**
 *
 * @author sunilkumar.sahu
 *
 */
public class AllInclusiveBoardFacadeImpl implements AllInclusiveBoardFacade
{

   /** The CMSSite Service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** The FacilityType Service. */
   @Resource
   private FacilityTypeService facilityType;

   /** The Feature Service. */
   @Resource
   private FeatureService featureService;

   /** The Accommodation Service. */
   @Resource
   private AccommodationService accommodationService;

   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   @Resource
   private BrandUtils brandUtils;

   private static final String FEATURE_DESCRIPTOR_ALL_INCLUSIVE_FOOD = "allInclusiveFood";

   private static final String FEATURE_DESCRIPTOR_ALL_INCLUSIVE_DRINK = "allInclusiveDrink";

   private static final String FEATURE_DESCRIPTOR_ALL_INCLUSIVE_SPORTS_ACTIVITY =
      "allInclusiveSportActivity";

   private static final String FEATURE_DESCRIPTOR_CODE_NAME = "name";

   private static final String FEATURE_DESCRIPTOR_CODE_PRIORITY = "priority";

   private static final String FEATURE_DESCRIPTOR_CODE_DESCRIPTION = "description";

   private static final String FEATURE_DESCRIPTOR_CODE_OPENING_TIMES = "openingTimes";

   private static final String SPORTS_FACILITY_TYPE_CODE = "FT000S";

   private static final String FOOD_AND_DRINK_FACILITY_TYPE_CODE = "FT000F";

   /**
    * This method returns BoardBasisViewData.
    *
    * @return boardBasisViewData
    */
   @Override
   public BoardBasisViewData getBoardBasisViewData()
   {
      final BoardBasisViewData boardBasisViewData = new BoardBasisViewData();
      final Map<String, List<Object>> featureCodeAndValues = new HashMap<String, List<Object>>();

      // Fetch Accomodation Model
      final AccommodationModel accommodationModel = getAccommodationModel();

      final Map<String, List<Object>> pageFacilities =
         featureService.getValuesForFeatures(
            Arrays.asList(new String[] { FEATURE_DESCRIPTOR_ALL_INCLUSIVE_FOOD,
               FEATURE_DESCRIPTOR_ALL_INCLUSIVE_DRINK,
               FEATURE_DESCRIPTOR_ALL_INCLUSIVE_SPORTS_ACTIVITY }), accommodationModel, new Date(),
            brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()));

      featureCodeAndValues.putAll(pageFacilities);
      populateOtherFacilities(accommodationModel, featureCodeAndValues);

      final RoomOptionsContentViewData roomOptionsContentViewData =
         new RoomOptionsContentViewData();
      boardBasisViewData.setRoomOptionsContentViewData(roomOptionsContentViewData);
      return boardBasisViewData;
   }

   /**
    * This method populates sportsActivities, foodAndDrinks like facilities from AccommodationModel
    *
    * @param accommodationModel
    * @param featureCodeAndValues
    */
   private void populateOtherFacilities(final AccommodationModel accommodationModel,
      final Map<String, List<Object>> featureCodeAndValues)
   {
      final List<Object> sportsActivities = new ArrayList<Object>();
      final List<Object> foodAndDrinks = new ArrayList<Object>();

      // Fetching Sport Facility Model using Facility Parent type as FT000S
      final List<FacilityTypeModel> sportfacilityTypeModel =
         facilityType.getFacilityType(cmsSiteService.getCurrentCatalogVersion(),
            SPORTS_FACILITY_TYPE_CODE);

      // Food And Drink Facility Model using Facility Parent type as FT000F
      final List<FacilityTypeModel> foodAndDrinkfacilityTypeModel =
         facilityType.getFacilityType(cmsSiteService.getCurrentCatalogVersion(),
            FOOD_AND_DRINK_FACILITY_TYPE_CODE);

      final List<String> sportfacilityCodes = getSportFacilityCodes(sportfacilityTypeModel);

      final List<String> foodAndDrinkfacilityCodes =
         getFoodAndDrinkfacilityCodes(foodAndDrinkfacilityTypeModel);

      final Iterator<FacilityModel> facilitiesModelItr =
         accommodationModel.getFacilities().iterator();
      while (facilitiesModelItr.hasNext())
      {
         final FacilityModel facilityModel = facilitiesModelItr.next();
         final Map<String, List<Object>> facilitiy =
            featureService.getValuesForFeatures(
               Arrays.asList(new String[] { FEATURE_DESCRIPTOR_CODE_NAME,
                  FEATURE_DESCRIPTOR_CODE_PRIORITY, FEATURE_DESCRIPTOR_CODE_DESCRIPTION,
                  FEATURE_DESCRIPTOR_CODE_OPENING_TIMES }), facilityModel, new Date(), null);
         addSportsAndFoodDrinks(sportsActivities, foodAndDrinks, facilitiy, facilityModel,
            sportfacilityCodes, foodAndDrinkfacilityCodes);
      }
      featureCodeAndValues.put("sportsActivities", sportsActivities);
      featureCodeAndValues.put("foodAndDrinks", foodAndDrinks);
   }

   /**
    * @param foodAndDrinkfacilityTypeModel
    * @return foodAndDrinkfacilityCodes
    */
   private List<String> getFoodAndDrinkfacilityCodes(
      final List<FacilityTypeModel> foodAndDrinkfacilityTypeModel)
   {
      final List<String> foodAndDrinkfacilityCodes = new ArrayList<String>();
      for (final FacilityTypeModel foodAndDrinkFacility : foodAndDrinkfacilityTypeModel)
      {
         foodAndDrinkfacilityCodes.add(foodAndDrinkFacility.getCode());
      }

      return foodAndDrinkfacilityCodes;
   }

   /**
    * @param sportfacilityTypeModel
    * @return sportfacilityCodes
    */
   private List<String> getSportFacilityCodes(final List<FacilityTypeModel> sportfacilityTypeModel)
   {
      final List<String> sportfacilityCodes = new ArrayList<String>();
      for (final FacilityTypeModel sportFacility : sportfacilityTypeModel)
      {
         sportfacilityCodes.add(sportFacility.getCode());
      }
      return sportfacilityCodes;
   }

   /**
    * @param sportsActivities
    * @param foodAndDrinks
    * @param facilitiy
    * @param foodAndDrinkfacilityCodes
    * @param sportfacilityCodes
    */
   private void addSportsAndFoodDrinks(final List<Object> sportsActivities,
      final List<Object> foodAndDrinks, final Map<String, List<Object>> facilitiy,
      final FacilityModel facilityModel, final List<String> sportfacilityCodes,
      final List<String> foodAndDrinkfacilityCodes)
   {

      if (sportfacilityCodes.contains(facilityModel.getType().getCode()))
      {
         sportsActivities.add(facilitiy);
      }
      else if (foodAndDrinkfacilityCodes.contains(facilityModel.getType().getCode()))
      {
         foodAndDrinks.add(facilitiy);
      }

   }

   /**
    * The method fetches the AccommodationModel.
    *
    * @return accommodationModel
    */
   private AccommodationModel getAccommodationModel()
   {
      return accommodationService.getAccomodationByCodeAndCatalogVersion(packageComponentService
         .getStay(getPackageModel()).getCode(), cmsSiteService.getCurrentCatalogVersion());

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
}
