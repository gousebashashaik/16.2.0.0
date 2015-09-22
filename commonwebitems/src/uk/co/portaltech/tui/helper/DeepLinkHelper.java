/**
 *
 */
package uk.co.portaltech.tui.helper;

import static uk.co.portaltech.commons.Collections.map;
import static uk.co.portaltech.tui.web.view.data.wrapper.UnitData.toUnitData;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.Collections;
import uk.co.portaltech.commons.Collections.MapFn;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.airport.Airport;
import uk.co.portaltech.travel.model.unit.Unit;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.travel.services.ProductRangeService;
import uk.co.portaltech.travel.services.airport.AirportService;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.BeanUtil;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author laxmibai.p
 *
 */
public class DeepLinkHelper
{

   @Resource(name = "cmsSiteService")
   private CMSSiteService cmsSiteService;

   private static final MainStreamTravelLocationService MSTRAVELLOCATIONSERVICE = BeanUtil
      .getSpringBean("mainStreamTravelLocationService", MainStreamTravelLocationService.class);

   private AirportService airportsService;

   private AccommodationService accommodationService;

   private ProductRangeService productRangeService;

   @Resource(name = "sessionService")
   private SessionService sessionService;

   @Resource(name = "tuiUtilityService")
   private TuiUtilityService tuiUtilityService;

   @Resource(name = "tuiLocationService")
   private LocationService locationService;

   @Resource
   private FeatureService featureService;

   @Resource
   private BrandUtils brandUtils;

   private static final String PRODUCT_RANGE = "productrange";

   private static final TUILogUtils LOG = new TUILogUtils("DeepLinkHelper");

   /**
    *
    */
   private final MapFn<Unit, UnitData> unitToUnitDataMapper = new MapFn<Unit, UnitData>()
   {
      @Override
      public UnitData call(final Unit input)
      {
         return UnitData.toUnitData(input);

      }
   };

   private final MapFn<Airport, AirportData> airportToAirportDataMapper =
      new MapFn<Airport, AirportData>()
      {
         @Override
         public AirportData call(final Airport input)
         {
            return AirportData.toAirportData(input);
         }
      };

   private static final MapFn<Unit, UnitData> UNIT_TO_UNIT_DATA_MAPPER =
      new MapFn<Unit, UnitData>()
      {
         @Override
         public UnitData call(final Unit input)
         {
            return toUnitData(input);

         }
      };

   /**
    * @param departureCodes
    * @returns departure list of departure airportData objects for given array of airport codes
    */
   public List<AirportData> getAirportsForInventory(final String departureCodes)
   {
      final List<Airport> airportList = new ArrayList<Airport>();
      final Set<Airport> requestedAirports = new HashSet<Airport>();

      if (StringUtils.isEmpty(departureCodes))
      {
         return new ArrayList<AirportData>();
      }

      final String[] depCodes = departureCodes.split(",");

      final Collection<String> airportsCollection = new ArrayList<String>(Arrays.asList(depCodes));

      for (final String airport : airportsCollection)
      {
         airportList.addAll(airportsService.search(airport));
      }
      if (CollectionUtils.isNotEmpty(airportList))
      {
         for (final Airport airport : airportList)
         {
            for (final String depCode : depCodes)
            {
               if (StringUtils.equals(airport.code(), depCode))
               {
                  requestedAirports.add(airport);
               }
            }
         }
      }

      if (!CollectionUtils.isEmpty(requestedAirports))
      {
         return Collections.map(requestedAirports, airportToAirportDataMapper);
      }
      return new ArrayList<AirportData>();
   }

   /**
    * @param departureCodes
    * @returns departure list of departure airportData objects for given array of airport codes
    */
   public List<AirportData> getAirports(final String departureCodes)
   {
      final List<Airport> airportList = new ArrayList<Airport>();
      final Set<Airport> requestedAirports = new HashSet<Airport>();

      if (StringUtils.isEmpty(departureCodes))
      {
         return new ArrayList<AirportData>();
      }

      final String[] depCodes = departureCodes.split("\\|");

      final Collection<String> airportsCollection = new ArrayList<String>(Arrays.asList(depCodes));

      for (final String airport : airportsCollection)
      {
         airportList.addAll(airportsService.search(airport));
      }
      if (CollectionUtils.isNotEmpty(airportList))
      {
         for (final Airport airport : airportList)
         {
            for (final String depCode : depCodes)
            {
               if (StringUtils.equals(airport.code(), depCode))
               {
                  requestedAirports.add(airport);
               }
            }
         }
      }

      if (!CollectionUtils.isEmpty(requestedAirports))
      {
         return Collections.map(requestedAirports, airportToAirportDataMapper);
      }
      return new ArrayList<AirportData>();
   }

   /**
    * @param childrenAge
    * @returns List of Children ages
    */
   @SuppressWarnings("boxing")
   public List<Integer> getChildernAge(final String childrenAge)
   {

      final List<Integer> ages = new ArrayList();
      if (!StringUtils.isEmpty(childrenAge))
      {
         final String[] agesString = decode(childrenAge).split(",");
         for (final String age : agesString)
         {
            if (!StringUtils.isEmpty(age))
            {
               ages.add(Integer.parseInt(age));
            }
         }
      }
      return ages;
   }

   public String decode(final String input)
   {
      String tempInput = input;

      while (StringUtils.contains(tempInput, '%'))
      {
         LOG.debug("Decoding - " + tempInput);
         tempInput = URLDecoder.decode(tempInput);
      }
      return tempInput;
   }

   /**
    * Retrieves of UnitData objects from list of unit.
    *
    * @param units
    * @returns List<UnitData>
    */
   public List<UnitData> getUnits(final String units, final String multiSelect,
      final String brandType)
   {
      Collection<Unit> unitModel = new ArrayList<Unit>();
      final Set<Unit> unitModels = new HashSet<Unit>();
      final List<String> laplandLocation =
         Arrays.asList("LAP_COUNTRY", "LAP_REGION", "LAP_DESTINATION", "LAP_RESORT");
      if (StringUtils.isEmpty(units))
      {
         return new ArrayList<UnitData>();
      }
      LocationModel locationModel = null;
      ProductRangeModel productRangeModel = null;
      String[] listOfUnits = null;
      listOfUnits = decode(units).split("\\|");
      for (final String unitCodeType : listOfUnits)
      {

         final String[] unitCodeAndType = unitCodeType.split(":");
         if (!StringUtils.isEmpty(unitCodeAndType[0]))
         {
            if (unitCodeAndType.length > 1
               && (StringUtils
                  .contains(
                     "COUNTRY, REGION, DESTINATION, RESORT,LAP_COUNTRY,LAP_REGION,LAP_DESTINATION,LAP_RESORT",
                     unitCodeAndType[1].toUpperCase())))
            {
               locationModel = getLocationModelForCode(unitCodeAndType[0]);

            }
            else if (unitCodeAndType.length > 1
               && (StringUtils.equalsIgnoreCase("productrange", unitCodeAndType[1])))
            {
               productRangeModel = getProductRangeModel(unitCodeAndType[0]);
            }
            else if (unitCodeAndType.length > 1)
            {

               unitModel =
                  getAccomodation(unitCodeAndType[0], unitCodeAndType[1].toUpperCase(), multiSelect);
            }

            if (unitCodeAndType.length > 1 && null != locationModel && unitCodeAndType[1] != null
               && laplandLocation.contains(unitCodeAndType[1].toUpperCase()))
            {
               final Unit unit =
                  new Unit(locationModel.getCode(), locationModel.getName(),
                     unitCodeAndType[1].toUpperCase(), featureService.getFirstFeatureValueAsString(
                        "synonyms", locationModel, new Date(), null), null);
               unitModel.add(unit);
            }

            else if (null != locationModel)
            {
               final Unit unit =
                  new Unit(locationModel.getCode(), locationModel.getName(), locationModel
                     .getType().getCode(), locationModel.getSynonyms(), null);

               if (StringUtils.isNotEmpty(multiSelect))
               {
                  unit.setMultiSelect(multiSelect);
               }

               unitModel.add(unit);
            }
            if (null != productRangeModel)
            {
               final Unit unit =
                  new Unit(productRangeModel.getCode(), productRangeModel.getName(), PRODUCT_RANGE,
                     StringUtils.EMPTY, null);

               if (StringUtils.isNotEmpty(multiSelect))
               {
                  unit.setMultiSelect(multiSelect);
               }
               unitModel.add(unit);
            }
            unitModels.addAll(unitModel);
         }
      }
      return Collections.map(unitModels, unitToUnitDataMapper);
   }

   /**
    * Retrieves of UnitData objects from list of unit.
    *
    * @param units
    * @returns List<UnitData>
    */
   public List<UnitData> getUnitDataList(final String units)
   {

      return new ArrayList<UnitData>();

   }

   /**
    * Retrieves of UnitData objects from list of unit.
    *
    * @param units
    * @returns List<UnitData>
    */
   public List<UnitData> getUnitDataListForSmerch(final String units)
   {
      Collection<Unit> unitModel = new ArrayList<Unit>();
      final Set<Unit> unitModels = new HashSet<Unit>();
      if (StringUtils.isEmpty(units))
      {
         return new ArrayList<UnitData>();
      }
      LocationModel locationModel = null;
      ProductRangeModel productRangeModel = null;
      String[] listOfUnitCodes = null;
      listOfUnitCodes = units.split(",");
      for (final String unitCode : listOfUnitCodes)
      {
         if (!StringUtils.isEmpty(unitCode))
         {

            locationModel = getLocationModelForCode(unitCode);

            if (locationModel == null)
            {
               productRangeModel = getProductRangeModel(unitCode);
               if (productRangeModel == null)
               {
                  unitModel = getAccomodationByNameForUnit(unitCode);

               }
               else
               {
                  final Unit unit =
                     new Unit(productRangeModel.getCode(), productRangeModel.getName(),
                        PRODUCT_RANGE, null);
                  unitModel.add(unit);
               }

            }
            else
            {
               final Unit unit =
                  new Unit(locationModel.getCode(), locationModel.getName(), locationModel
                     .getType().getCode(), null);
               unitModel.add(unit);
            }

            unitModels.addAll(unitModel);
         }
      }
      return Collections.map(unitModels, unitToUnitDataMapper);
   }

   /**
    * @param code
    * @return Collection<Unit>
    */
   private Collection<Unit> getAccomodationByNameForUnit(final String name)
   {
      final Collection<Unit> unitModels = new ArrayList<Unit>();
      final List<String> brandPKs = tuiUtilityService.getSiteReleventBrandPks();
      final AccommodationModel accommodationModel = getAccommodationModelForName(name, brandPKs);

      if (accommodationModel != null)
      {
         unitModels.add(new Unit(accommodationModel.getCode(), accommodationModel.getName(),
            accommodationModel.getType().getCode(), tuiUtilityService.getSiteBrand(), null));
      }
      return unitModels;
   }

   // code commenting as unused--END

   /**
    * @param productCode
    * @return Collection<Unit>
    */
   public List<UnitData> getUnitForAccommodationForFlightOption(final String productCode,
      final String multiSelect)
   {
      if (StringUtils.isEmpty(productCode))
      {
         return new ArrayList<UnitData>();
      }

      final Set<Unit> unitModels = new HashSet<Unit>();

      final Collection<Unit> unitModel = getAccomodationForFlightOption(productCode, multiSelect);
      unitModels.addAll(unitModel);

      return Collections.map(unitModels, unitToUnitDataMapper);

   }

   /**
    * @param productCode
    * @param multiSelect
    * @return List
    */
   private Collection<Unit> getAccomodationForFlightOption(final String productCode,
      final String multiSelect)
   {
      final Collection<Unit> unitModels = new ArrayList<Unit>();
      final AccommodationModel accommodationModel =
         getAccommodationModelForFlightOption(productCode);

      final Unit unit =
         new Unit(accommodationModel.getCode(), accommodationModel.getName(), accommodationModel
            .getType().getCode(),
            brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()), null);
      if (StringUtils.isNotEmpty(multiSelect))
      {
         unit.setMultiSelect(multiSelect);
      }
      unitModels.add(unit);

      return unitModels;
   }

   /**
    * @param code
    * @return Collection<Unit>
    */
   private Collection<Unit> getAccomodation(final String code, final String type,
      final String multiSelect)
   {
      final Collection<Unit> unitModels = new ArrayList<Unit>();
      AccommodationModel accommodationModel = null;
      // Added for Inventory - since there is a chance of getting cross
      // feature holiday
      // brand is purposely send as null as we were not able to search FC only
      // units sold on TH
      accommodationModel = getAccommodationModel(code, null);

      final List<String> laplandHoletAndDayTrip = Arrays.asList("DAYTRIP", "LAP_HOTEL");

      if (accommodationModel != null)
      {
         if (type != null && laplandHoletAndDayTrip.contains(type))
         {
            final Unit unit =
               new Unit(accommodationModel.getCode(), accommodationModel.getName(), type,
                  brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()), null);
            if (StringUtils.isNotEmpty(multiSelect))
            {
               unit.setMultiSelect(multiSelect);
            }
            unitModels.add(unit);

         }
         else
         {
            final Unit unit =
               new Unit(accommodationModel.getCode(), accommodationModel.getName(),
                  accommodationModel.getType().getCode(),
                  brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()), null);
            if (StringUtils.isNotEmpty(multiSelect))
            {
               unit.setMultiSelect(multiSelect);
            }
            unitModels.add(unit);

         }

      }

      return unitModels;
   }

   /**
    * @param locationCode
    * @return LocationModel
    */
   public LocationModel getLocationModelForCode(final String locationCode)
   {

      LocationModel resultLocationModel = null;

      if (!StringUtils.isEmpty(locationCode))
      {

         resultLocationModel = getLocationModel(locationCode);

      }
      return resultLocationModel;
   }

   /**
    * @param locationCode
    * @return
    */
   private LocationModel getLocationModel(final String locationCode)
   {

      LocationModel location = null;

      final CatalogVersionModel catalogVersion = getCatalogVersionModel();

      if (!StringUtils.isEmpty(locationCode))
      {
         final BrandDetails brandDetails =
            sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> brandPKs = brandDetails.getRelevantBrands();
         location =
            locationService.getLocationModelForGivenBrand(locationCode, catalogVersion, brandPKs);
      }

      return location;

   }

   /**
    * @param code
    * @return AccommodationModel
    */
   public AccommodationModel getAccommodationModel(final String code, final List<String> brandPKs)
   {
      AccommodationModel accommadation = null;
      final CatalogVersionModel catalogVersion = getCatalogVersionModel();

      if (!StringUtils.isEmpty(code))
      {

         accommadation =
            accommodationService.getAccomodationByCodeAndCatalogVersion(code, catalogVersion,
               brandPKs);
      }

      return accommadation;

   }

   /**
    * @param code
    * @return AccommodationModel
    */
   public AccommodationModel getAccommodationModelForName(final String name,
      final List<String> brandPKs)
   {
      AccommodationModel accommadation = null;
      final CatalogVersionModel catalogVersion = getCatalogVersionModel();

      if (!StringUtils.isEmpty(name))
      {

         accommadation =
            accommodationService.getAccomodationByNameAndCatalogVersion(name, catalogVersion,
               brandPKs);
      }

      return accommadation;

   }

   /**
    * @param code
    * @return AccommodationModel
    */
   public AccommodationModel getAccommodationModelForFlightOption(final String code)
   {
      AccommodationModel accommadation = null;

      final CatalogVersionModel catalogVersion = getCatalogVersionModel();

      if (!StringUtils.isEmpty(code))
      {

         accommadation =
            accommodationService.getAccomodationByCodeAndCatalogVersion(code, catalogVersion, null);
      }

      return accommadation;

   }

   /**
    * @param code
    * @return AccommodationModel
    */
   public ProductRangeModel getProductRangeModel(final String code)
   {
      ProductRangeModel productRange = null;
      final CatalogVersionModel catalogVersion = getCatalogVersionModel();

      if (!StringUtils.isEmpty(code))
      {
         final BrandDetails brandDetails =
            sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> brandPKs = brandDetails.getRelevantBrands();
         productRange =
            productRangeService.getProductForProductRange(code, catalogVersion, brandPKs);
      }

      return productRange;
   }

   public List<AirportData> getMatchedAirprots(final List<String> airports)
   {
      final Set<Airport> airportList = new HashSet<Airport>();
      final List<Airport> requestedAirports = new ArrayList<Airport>();

      if (CollectionUtils.isNotEmpty(airports))
      {
         for (final String code : airports)
         {
            airportList.addAll(airportsService.search(code));
         }
      }
      if (!CollectionUtils.isEmpty(airportList))
      {
         for (final Airport airport : airportList)
         {
            for (final String depCode : airports)
            {
               if (StringUtils.equals(airport.code(), depCode))
               {
                  requestedAirports.add(airport);
               }
            }
         }
         if (!CollectionUtils.isEmpty(requestedAirports))
         {
            return Collections.map(requestedAirports, airportToAirportDataMapper);
         }

      }
      return new ArrayList<AirportData>();
   }

   public static List<UnitData> getMatchedSelectedUnits(final List<String> selectedUnits,
      final List<String> releventBrands, final String multiSelect)
   {
      final Collection<Unit> units = new ArrayList<Unit>();
      if (CollectionUtils.isNotEmpty(selectedUnits))
      {
         for (final String key : selectedUnits)
         {
            units.addAll(MSTRAVELLOCATIONSERVICE
               .searchDestination(key, releventBrands, multiSelect));
         }
      }
      if (!CollectionUtils.isEmpty(units))
      {
         return map(units, UNIT_TO_UNIT_DATA_MAPPER);
      }
      return new ArrayList<UnitData>();
   }

   /**
    * @return CatalogVersionModel
    */
   private CatalogVersionModel getCatalogVersionModel()
   {
      return cmsSiteService.getCurrentCatalogVersion();
   }

   /**
    * @param airportsService the airportsService to set
    */
   public void setAirportsService(final AirportService airportsService)
   {
      this.airportsService = airportsService;
   }

   /**
    * @param accommodationService the accommodationService to set
    */
   public void setAccommodationService(final AccommodationService accommodationService)
   {
      this.accommodationService = accommodationService;
   }

   /**
    * @param productRangeService the productRangeService to set
    */
   public void setProductRangeService(final ProductRangeService productRangeService)
   {
      this.productRangeService = productRangeService;
   }

}
