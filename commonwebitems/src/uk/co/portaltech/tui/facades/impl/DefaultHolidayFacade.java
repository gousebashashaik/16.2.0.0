/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static uk.co.portaltech.tui.constants.NumberConstants.TIME_OUT;
import static uk.co.portaltech.tui.constants.NumberConstants.TWELVE;
import static uk.co.portaltech.tui.constants.NumberConstants.TWO;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.enums.AniteUnitBrand;
import uk.co.portaltech.tui.enums.IscapeBrandLookUp;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.HolidayFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.populators.IscapeShortlistHolidayPopulator;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;
import uk.co.tui.shortlist.data.ShortlistHolidayData;

import com.tui.uk.shortlist.service.ShortlistResourceClient;
import com.tui.uk.shortlist.shortlistedholiday.Accommodation;
import com.tui.uk.shortlist.shortlistedholiday.BrandType;
import com.tui.uk.shortlist.shortlistedholiday.Flight;
import com.tui.uk.shortlist.shortlistedholiday.HolidayDetails;
import com.tui.uk.shortlist.shortlistedholiday.PackageAttributes;
import com.tui.uk.shortlist.shortlistedholiday.Price;
import com.tui.uk.shortlist.shortlistedholiday.Pricing;
import com.tui.uk.shortlist.shortlistedholiday.Room;
import com.tui.uk.shortlist.shortlistedholiday.ShortlistedHoliday;

/**
 *
 */
public class DefaultHolidayFacade implements HolidayFacade
{

   /**
     *
     */
   private static final String ISCAPE_SHORTLIST_TIMEOUT = "iscape.shortlist.timeout";

   /**
    * business context id
    */
   private static final String ISCAPE_BUSINESS_CONTEXT_ID = ".iscapeBusinessContextId";

   /**
     *
     */
   private static final String ZERO = "0";

   private static final int MIN_CHILD_AGE = 2;

   private static final int TIME_OUT_NUMBER = 5000;

   private static final int MAX_CHILD_AGE = 17;

   public static final double CHILD_CHARGE = 0.5;

   /**
     *
     */
   private static final String WISHLIST_MAP = "wishListMap";

   @Resource
   private ShortlistResourceClient iscapeShortlistClient;

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private TUIConfigService tuiConfigService;

   @Resource
   private SessionService sessionService;

   @Resource
   private SearchFacade searchFacade;

   @Resource
   private ComponentFacade componentFacade;

   @Resource
   private DroolsPriorityProviderService droolsPriorityProviderService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private IscapeShortlistHolidayPopulator iscapeShortlistHolidayPopulator;

   private static final String DATE_FORMAT = "dd-MM-yyyy";

   private static final TUILogUtils LOG = new TUILogUtils("DefaultHolidayFacade");

   private static final String ISCAPE_SERVER_URL = ".iscape.server.url";

   private static final String TRUE = "true";

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.HolidayFacade#addtoShortlist(uk.co.portaltech
    * .tui.web.view.data.SearchRequestData, java.lang.String, java.lang.String)
    */
   @Override
   public boolean addtoShortlist(final SearchResultsRequestData searchCriteria,
      final String packageId, final String customerId, final String brandType)
      throws SearchResultsBusinessException
   {

      // get the holiday to be short listed
      final BookFlowAccommodationViewData bookFlowAccommodationViewData =
         getHolidayFromSession(searchCriteria, packageId);

      String status = "";
      if (null != bookFlowAccommodationViewData)
      {
         final int num = TIME_OUT_NUMBER;
         iscapeShortlistClient.setConnectionTimeOut(Config.getInt(ISCAPE_SHORTLIST_TIMEOUT, num));

         status =
            iscapeShortlistClient
               .addToShortlist(
                  populateShortlistedHoliday(searchCriteria, bookFlowAccommodationViewData,
                     brandType), customerId, getIscapeServerUrl(brandType));

         LOG.debug(status);
      }

      return TRUE.equals(status);

   }

   private BookFlowAccommodationViewData getHolidayFromSession(
      final SearchResultsRequestData searchCriteria, final String packageId)
   {
      final Map map = (Map) sessionService.getAttribute("holidayResult");
      final Map<String, String> wishListMap =
         (Map<String, String>) sessionService.getAttribute("wishListMap");
      final Map<String, String> urlMap =
         (Map<String, String>) sessionService.getAttribute("urlMap");
      final SearchResultViewData sViewData = new SearchResultViewData();
      final ShortlistHolidayData sHolidayData = (ShortlistHolidayData) map.get(packageId);
      final String iscapePackageid = StringUtils.EMPTY;
      if (sHolidayData != null)
      {
         iscapeShortlistHolidayPopulator.populate(sHolidayData, sViewData);
         if (wishListMap != null)
         {
            populateWishListID(packageId, wishListMap, sViewData, iscapePackageid);
         }
         if (urlMap != null)
         {
            final String url = urlMap.get(sHolidayData.getPackageId());
            sViewData.getAccommodation().setUrl(url);
         }
         final BookFlowAccommodationViewData bflowAccommodationViewData =
            new BookFlowAccommodationViewData();
         bflowAccommodationViewData.setPackageData(sViewData);
         bflowAccommodationViewData.setSearchRequestData(searchCriteria);
         return bflowAccommodationViewData;
      }
      return null;
   }

   /**
    * @param packageId
    * @param wishListMap
    * @param sViewData
    * @param iscapePackageid
    */
   private void populateWishListID(final String packageId, final Map<String, String> wishListMap,
      final SearchResultViewData sViewData, String iscapePackageid)
   {
      for (final Map.Entry<String, String> entry : wishListMap.entrySet())
      {
         if (StringUtils.equalsIgnoreCase(entry.getValue(), packageId))
         {
            iscapePackageid = entry.getKey();
            break;
         }
      }

      sViewData.setWishListId(iscapePackageid);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.HolidayFacade#removeShortlist(java.lang. String,
    * java.lang.String)
    */
   @Override
   public boolean removeShortlist(final String packageId, final String customerId,
      final String brandType)
   {
      String iscapePackageid = StringUtils.EMPTY;
      if (sessionService.getAttribute(WISHLIST_MAP) != null)
      {
         final Map<String, String> wishListMap =
            (Map<String, String>) sessionService.getAttribute(WISHLIST_MAP);

         for (final Map.Entry<String, String> entry : wishListMap.entrySet())
         {
            if (StringUtils.equalsIgnoreCase(entry.getValue(), packageId))
            {
               iscapePackageid = entry.getKey();
               break;
            }
         }
         iscapeShortlistClient.setConnectionTimeOut(Config.getInt(ISCAPE_SHORTLIST_TIMEOUT,
            TIME_OUT));
         final String status =
            iscapeShortlistClient.removeShortlistedHoliday(customerId, iscapePackageid,
               getIscapeServerUrl(brandType));

         LOG.debug("Remove short list status: " + status);

      }

      return true;
   }

   /*
    * (non-Javadoc)This method ensures all user shortlisted units are available back to the user.
    * 
    * @see uk.co.portaltech.tui.facades.HolidayFacade#getUserShortlist(java.lang .String)
    */
   @Override
   public List getUserShortlist(final String customerId, final String brandType)
   {

      final List<String> phoenixHolidays = new ArrayList<String>();
      iscapeShortlistClient.setConnectionTimeOut(Config.getInt(ISCAPE_SHORTLIST_TIMEOUT, TIME_OUT));
      final List<String> userShortlist =
         iscapeShortlistClient.getShortlistedHolidays(customerId, getIscapeServerUrl(brandType));
      if (userShortlist != null)
      {
         final List<String> tempUserShortlist = new ArrayList<String>(userShortlist);
         checkWishListSession(phoenixHolidays, userShortlist, tempUserShortlist);
         if (CollectionUtils.isEmpty(phoenixHolidays))
         {
            return tempUserShortlist;
         }
         // There are shortlisted Holidays but not for this search
         // criteria...
         if (CollectionUtils.size(userShortlist) != CollectionUtils.size(phoenixHolidays))
         {
            tempUserShortlist.addAll(phoenixHolidays);
            return tempUserShortlist;
         }
      }
      return phoenixHolidays;
   }

   /**
    * @param phoenixHolidays
    * @param userShortlist
    * @param tempUserShortlist
    */
   private void checkWishListSession(final List<String> phoenixHolidays,
      final List<String> userShortlist, final List<String> tempUserShortlist)
   {
      if (sessionService.getAttribute(WISHLIST_MAP) != null)
      {
         removeTempUserShortList(phoenixHolidays, userShortlist, tempUserShortlist);
      }
   }

   /**
    * @param phoenixHolidays
    * @param userShortlist
    * @param tempUserShortlist
    */
   private void removeTempUserShortList(final List<String> phoenixHolidays,
      final List<String> userShortlist, final List<String> tempUserShortlist)
   {
      final Map<String, String> wishListMap =
         (Map<String, String>) sessionService.getAttribute(WISHLIST_MAP);
      for (final String wishListId : userShortlist)
      {
         if (StringUtils.isNotEmpty(wishListId) && wishListMap.get(wishListId) != null)
         {
            phoenixHolidays.add(wishListMap.get(wishListId));
            tempUserShortlist.remove(wishListId);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.HolidayFacade#getShortlistCount()
    */
   @Override
   public int getShortlistCount(final String customerId, final String brandType)
   {
      return iscapeShortlistClient.getShortlistCount(customerId, getIscapeServerUrl(brandType));
   }

   /**
    * Populates iScape ShortlistedHoliday object required for iScape ShortList functionality
    *
    * @param searchRequest
    * @param bookFlowAccommodationViewData
    * @return ShortlistedHoliday
    */
   public ShortlistedHoliday populateShortlistedHoliday(
      final SearchResultsRequestData searchRequest,
      final BookFlowAccommodationViewData bookFlowAccommodationViewData, final String brandType)
   {
      final SearchResultViewData result = bookFlowAccommodationViewData.getPackageData();
      final ShortlistedHoliday shortlistedHoliday = new ShortlistedHoliday();

      // check FC unit sold on TH
      final boolean fcUnitSoldOnTH = checkFCUnitsSoldOnTH(brandType, result.getBrandType());

      shortlistedHoliday.setUniqueHolidayID(result.getWishListId());
      shortlistedHoliday.setBrand(getIscapeBrand(brandType));
      shortlistedHoliday.setDuration(result.getDuration());
      shortlistedHoliday
         .setBusinessContextID(getIscapeBusinessContextId(brandType, fcUnitSoldOnTH));
      shortlistedHoliday.setDayTripHoliday(false);
      shortlistedHoliday.setExclusiveToThomson(false);
      shortlistedHoliday.setFreeChildTransfer(false);
      shortlistedHoliday.setHandPicked(false);

      shortlistedHoliday.setSoldOut(false);

      String derivedProductCode = "SH";
      if (!CollectionUtils.isEmpty(result.getItinerary().getOutbounds())
         && !StringUtils.isEmpty(result.getItinerary().getOutbounds().get(0).getHaulType()))
      {
         derivedProductCode = result.getItinerary().getOutbounds().get(0).getHaulType();
      }

      shortlistedHoliday.setHolidayDetails(populateHolidayDetails(result.getProductCode(),
         derivedProductCode, result.getAccommodation().getAccomType(), result.getSubProductCode()));
      shortlistedHoliday.setBookmarkURL(result.getAccommodation().getUrl());
      shortlistedHoliday.setDepartureDate(formatDate(result.getItinerary().getDepartureDate()));
      shortlistedHoliday.setAccommodation(populateAccommodation(result.getAccommodation(),
         brandType, fcUnitSoldOnTH));
      shortlistedHoliday.setFlight(populateFlight(result.getItinerary()));
      shortlistedHoliday.setPricing(populatePrice(result.getPrice(),
         getWorldCareFunds(bookFlowAccommodationViewData)));
      if (result.getAccommodation().isDifferentiatedProduct())
      {
         shortlistedHoliday.setAccomBrandCodes(result.getAccommodation().getDifferentiatedCode());
      }

      shortlistedHoliday.setRooms(populateRoom(result.getAccommodation().getRooms()));

      return shortlistedHoliday;
   }

   /**
    * @param brandTypeParam
    * @return IscapeBusinessContextId
    */
   private int getIscapeBusinessContextId(final String brandTypeParam, final boolean fcUnitSoldOnTH)
   {
      String brandType = brandTypeParam;
      if (fcUnitSoldOnTH)
      {
         // overriding the brand in case of FC units sold on TH
         brandType = uk.co.portaltech.travel.enums.BrandType.FC.getCode();
      }

      return Integer.parseInt(tuiConfigService.getConfigValue(brandType
         + ISCAPE_BUSINESS_CONTEXT_ID));
   }

   /**
    * @param brandType
    * @param unitBrandType
    * @return true or false
    */
   private boolean checkFCUnitsSoldOnTH(final String brandType, final String unitBrandType)
   {
      return StringUtils.equalsIgnoreCase(unitBrandType, AniteUnitBrand.F.toString())
         && StringUtils.equalsIgnoreCase(brandType,
            uk.co.portaltech.travel.enums.BrandType.TH.getCode());
   }

   /**
    * @param brandType
    * @return IscapeBrand
    */
   private BrandType getIscapeBrand(final String brandType)
   {
      return BrandType.valueOf(IscapeBrandLookUp.valueOf(brandType).getBrand());
   }

   /**
    * Populates iScape short list Accommodation data from search result
    *
    * @param accomData
    * @return Accommodation
    */
   private Accommodation populateAccommodation(final SearchResultAccomodationViewData accomData,
      final String brand, final boolean fcUnitSoldOnTH)
   {
      final Accommodation accommodation = new Accommodation();
      accommodation.setUnitCode(accomData.getCode());
      accommodation.setAccommodationName(accomData.getName());

      setDestinationCode(accommodation, accomData);
      if (CollectionUtils.isNotEmpty(accomData.getRooms()))
      {
         final SearchResultRoomsData room = accomData.getRooms().get(0);
         accommodation.setBoardBasisCode(room.getBoardBasisCode());
         accommodation.setBoardBasisName(room.getBoardType());
         accommodation.setLimitedAvailabilityCount(room.getSellingout());
      }

      accommodation.setProductCode(accomData.getCode());
      setResortCode(accommodation, accomData);
      accommodation.setSourceSystem(getSourceSystemId(brand, fcUnitSoldOnTH));

      return accommodation;
   }

   /**
    * @param brandParam
    * @return sourceSytemId
    */
   private String getSourceSystemId(final String brandParam, final boolean fcUnitSoldOnTH)
   {
      String brand = brandParam;
      if (fcUnitSoldOnTH)
      {
         // overriding the brand in case of FC units sold on TH
         brand = uk.co.portaltech.travel.enums.BrandType.FC.getCode();
      }
      return tuiConfigService.getConfigValue(brand + ".sourceSytemId");
   }

   private void setDestinationCode(final Accommodation accommodation,
      final SearchResultAccomodationViewData accomData)
   {
      final String code = getCode(accomData);
      if (StringUtils.isNotEmpty(code))
      {
         accommodation.setDestinationCode(code);
      }
      else if (accomData.getLocation().getCountry() != null
         && StringUtils.isNotEmpty(accomData.getLocation().getCountry().getCode()))
      {
         accommodation.setDestinationCode(accomData.getLocation().getCountry().getCode());
      }
   }

   private void setResortCode(final Accommodation accommodation,
      final SearchResultAccomodationViewData accomData)
   {
      if (accomData.getLocation().getResort() != null
         && StringUtils.isNotEmpty(accomData.getLocation().getResort().getCode()))
      {
         accommodation.setResortCode(accomData.getLocation().getResort().getCode());
      }
      else
      {

         final String code = getCode(accomData);
         if (StringUtils.isNotEmpty(code))
         {
            accommodation.setResortCode(code);
         }
      }

   }

   private String getCode(final SearchResultAccomodationViewData accomData)
   {

      if (accomData.getLocation().getDestination() != null
         && StringUtils.isNotEmpty(accomData.getLocation().getDestination().getCode()))
      {
         return accomData.getLocation().getDestination().getCode();
      }

      else if (accomData.getLocation().getRegion() != null)
      {
         getRegionCode(accomData);
      }
      return null;
   }

   /**
    * @param accomData
    */
   private String getRegionCode(final SearchResultAccomodationViewData accomData)
   {
      if (StringUtils.isNotEmpty(accomData.getLocation().getRegion().getCode()))
      {
         return accomData.getLocation().getRegion().getCode();
      }
      return null;

   }

   /**
    * Populate iScape Flight Data from search result
    *
    * @param flightData
    * @return Flight
    */

   private Flight populateFlight(final SearchResultFlightViewData flightData)
   {
      final Flight flight = new Flight();
      flight.setAirportCode(flightData.getInbounds().get(0).getArrivalAirportCode());
      flight.setDepartureAirportName(flightData.getDepartureAirport());
      return flight;
   }

   /**
    * Populates iScape short list Price data from search result
    *
    * @param priceData
    * @return Pricing
    */
   private Pricing populatePrice(final SearchResultPriceViewData priceData,
      final double worldCareFund)
   {
      final Pricing pricing = new Pricing();
      Price price = new Price();
      if (StringUtils.isEmpty(priceData.getLowDeposit()))
      {
         priceData.setLowDeposit(ZERO);
      }
      price.setAmount(new BigDecimal(priceData.getLowDeposit()));
      pricing.setDeposit(price);

      price = new Price();
      BigDecimal perPerson = new BigDecimal(priceData.getPerPerson());
      if (isFC())
      {
         perPerson = perPerson.add(new BigDecimal(worldCareFund));
      }
      price.setAmount(perPerson);

      pricing.setFromPricePerPerson(price);
      pricing.setPricePerPerson(price);
      pricing.setToPricePerPerson(price);

      price = new Price();
      BigDecimal totalParty = new BigDecimal(priceData.getTotalParty());
      if (isFC())
      {
         totalParty = totalParty.add(new BigDecimal(worldCareFund));
      }
      final BigDecimal roundedTotalParty = totalParty.setScale(0, BigDecimal.ROUND_HALF_DOWN);
      price.setAmount(roundedTotalParty);
      pricing.setFromTotalPartyPrice(price);
      pricing.setToTotalPartyPrice(price);

      price = new Price();
      price.setAmount(new BigDecimal(priceData.getDiscount()));
      pricing.setSavings(price);

      return pricing;
   }

   /**
    * Removed the seniors calculation .. made same as population logic of iscape -- refer
    * ShortlistRoomBuilder.java of iscape Populate iScape Short list Room Data from search result
    *
    * @param roomsData
    * @return List<Room>
    */
   @SuppressWarnings("boxing")
   private List<Room> populateRoom(final List<SearchResultRoomsData> roomsData)
   {
      final List<Room> rooms = new ArrayList<Room>();
      Room iscapeRoom = null;

      final int maxChildAge = getMaxChildAge();
      final int minChilAge = getMinChildAge();

      for (final SearchResultRoomsData room : roomsData)
      {
         iscapeRoom = new Room();
         final List<Integer> childrenAge = new ArrayList<Integer>();

         for (final PaxDetail pax : room.getOccupancy().getPaxDetail())
         {
            if (pax.getAge() >= minChilAge && pax.getAge() <= maxChildAge)
            {
               childrenAge.add(Integer.valueOf(pax.getAge()));
            }
         }

         iscapeRoom.setAdults(room.getOccupancy().getAdults());
         iscapeRoom.setChildAges(childrenAge);
         iscapeRoom.setChildren(room.getOccupancy().getChildren());
         iscapeRoom.setInfants(room.getOccupancy().getInfant());
         iscapeRoom.setSeniors(room.getOccupancy().getSeniors());
         rooms.add(iscapeRoom);
      }

      return rooms;
   }

   private NewSearchPanelComponentModel getNewSearchPanelComponentModel()
   {

      try
      {
         return componentFacade.getNewSearchPanelComponent();
      }
      catch (final SearchResultsBusinessException e)
      {
         LOG.error("Search Results Business Exception ", e);
         return null;
      }
   }

   @SuppressWarnings("boxing")
   private int getMaxChildAge()
   {
      final NewSearchPanelComponentModel newsearchPanelcomponent =
         getNewSearchPanelComponentModel();

      if (newsearchPanelcomponent != null && newsearchPanelcomponent.getMaxChildAge() != null)
      {
         return newsearchPanelcomponent.getMaxChildAge();
      }
      return MAX_CHILD_AGE;
   }

   @SuppressWarnings("boxing")
   private int getMinChildAge()
   {

      final NewSearchPanelComponentModel newsearchPanelcomponent =
         getNewSearchPanelComponentModel();

      if (newsearchPanelcomponent != null && newsearchPanelcomponent.getMaxChildAge() != null)
      {
         return newsearchPanelcomponent.getMinChildAge();
      }
      return MIN_CHILD_AGE;
   }

   /**
    * Populates iscape Holiday Details from Search Result
    *
    * @param productCode
    * @param derivedProductCode
    * @param productType
    * @param subProductCode
    * @return HolidayDetails
    */
   private HolidayDetails populateHolidayDetails(final String productCode,
      final String derivedProductCode, final String productType, final String subProductCode)
   {

      final HolidayDetails holidayDetails = new HolidayDetails();
      final PackageAttributes packageAttributes = new PackageAttributes();
      packageAttributes.setProductCode(productCode);
      packageAttributes.setDerivedProductCode(derivedProductCode);
      packageAttributes.setProductType(productType);
      packageAttributes.setSubProductCode(subProductCode);
      holidayDetails.setPackageDetails(packageAttributes);
      return holidayDetails;
   }

   /**
    * Converts Date String to Date object
    *
    * @param date
    * @return Date
    */
   private Date formatDate(final String date)
   {
      final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
      try
      {
         return sdf.parse(date);
      }
      catch (final ParseException e)
      {
         LOG.error("Error while parsing the date ", e);
      }
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.HolidayFacade#getHoliday(uk.co.portaltech
    * .tui.web.view.data.SearchResultsRequestData)
    */
   @Override
   public BookFlowAccommodationViewData getHoliday(final SearchResultsRequestData searchParameter,
      final String packageId, final String brandType) throws SearchResultsBusinessException
   {

      final BookFlowAccommodationViewData accom =
         searchFacade.getPackageSummaryAccommodationData(searchParameter, packageId, ZERO, "",
            brandType);
      if (accom != null)
      {
         return accom;
      }
      return null;
   }

   /**
    * This needs to be removed once we start getting the world care fund indicator from Endeca.
    * Retrieves a WorldCareFunds on the basis of drools.
    *
    * @param target
    * @return WorldCareFunds
    */
   private double getWorldCareFunds(final BookFlowAccommodationViewData target)
   {
      final WorldCareFundRuleOutputData worldCare =
         droolsPriorityProviderService.getWorldCareFundCharges();

      if (StringUtils.isNotEmpty(worldCare.getAdultCharge())
         && StringUtils.isNotBlank(worldCare.getChildCharge()))
      {
         return (target.getSearchRequestData().getNoOfAdults() * Double.parseDouble(worldCare
            .getAdultCharge()))
            + (target.getSearchRequestData().getNoOfSeniors() * Double.parseDouble(worldCare
               .getAdultCharge()))
            + calcluateChildWorldCareFund(target.getSearchRequestData().getChildAges(),
               Double.parseDouble(worldCare.getAdultCharge()),
               Double.parseDouble(worldCare.getChildCharge()));

      }
      else
      {
         return (target.getSearchRequestData().getNoOfAdults() * 1.0)
            + (target.getSearchRequestData().getNoOfSeniors() * 1.0)
            + calcluateChildWorldCareFund(target.getSearchRequestData().getChildAges(), 1.0,
               CHILD_CHARGE);
      }

   }

   /**
    * Added as tracs considers above 12 child as Adults for world care calculation
    */

   private double calcluateChildWorldCareFund(final List<Integer> childrenAge,
      final double adultCharge, final double childCharge)
   {

      int childcount = 0;
      int adultCount = 0;

      if (childrenAge != null)
      {

         for (final int age : childrenAge)
         {
            if (age > TWELVE)
            {
               adultCount++;
            }
            else if (age >= TWO && age <= TWELVE)
            {
               childcount++;
            }
         }
      }
      return (adultCount * adultCharge) + (childcount * childCharge);
   }

   /**
    * @param brandType
    * @return IscapeServerUrl
    */
   private String getIscapeServerUrl(final String brandType)
   {
      return configurationService.getConfiguration().getString(brandType + ISCAPE_SERVER_URL);
   }

   private boolean isFC()
   {
      final String brand = tuiUtilityService.getSiteBrand();
      return StringUtils.isNotBlank(brand)
         && StringUtils.equalsIgnoreCase(uk.co.portaltech.travel.enums.BrandType.FC.getCode(),
            brand);
   }

   /**
    * @param iscapeShortlistClient the iscapeShortlistClient to set
    */
   public void setIscapeShortlistClient(final ShortlistResourceClient iscapeShortlistClient)
   {
      this.iscapeShortlistClient = iscapeShortlistClient;
   }

   /**
    * @param configurationService the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }

   /**
    * @param tuiConfigService the tuiConfigService to set
    */
   public void setTuiConfigService(final TUIConfigService tuiConfigService)
   {
      this.tuiConfigService = tuiConfigService;
   }

   /**
    * @param sessionService the sessionService to set
    */
   public void setSessionService(final SessionService sessionService)
   {
      this.sessionService = sessionService;
   }

   /**
    * @param searchFacade the searchFacade to set
    */
   public void setSearchFacade(final SearchFacade searchFacade)
   {
      this.searchFacade = searchFacade;
   }

   /**
    * @param tuiPageService the tuiPageService to set
    */

   /**
    * @param componentFacade the componentFacade to set
    */
   public void setComponentFacade(final ComponentFacade componentFacade)
   {
      this.componentFacade = componentFacade;
   }

   /**
    * @param droolsPriorityProviderService the droolsPriorityProviderService to set
    */
   public void setDroolsPriorityProviderService(
      final DroolsPriorityProviderService droolsPriorityProviderService)
   {
      this.droolsPriorityProviderService = droolsPriorityProviderService;
   }

   /**
    * @param cmsSiteService the cmsSiteService to set
    */

   /**
    * @param tuiUtilityService the tuiUtilityService to set
    */

   public void setTuiUtilityService(final TuiUtilityService tuiUtilityService)
   {
      this.tuiUtilityService = tuiUtilityService;
   }

}
