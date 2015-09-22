/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import static uk.co.portaltech.commons.StringUtil.notEmptyAndNotBlank;

import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Deposit;
import uk.co.tui.book.domain.lite.DepositType;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.InsuranceType;
import uk.co.tui.book.domain.lite.LowDeposit;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.UpdateExtraFacilityService;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.cr.book.constants.BookFlowConstants;
import uk.co.tui.cr.book.constants.SessionObjectKeys;
import uk.co.tui.cr.book.facade.PaymentDetailsFacade;
import uk.co.tui.cr.book.formbean.PassengerDetailsFormBean;
import uk.co.tui.cr.book.formbean.PassengerInfoFormBean;
import uk.co.tui.cr.book.view.data.SummaryPanelViewData;

/**
 * This class is used to perform all the operations required to for payment page.
 *
 * @author madhumathi.m
 *
 */
public class PaymentDetailsFacadeImpl implements PaymentDetailsFacade
{

   /** The Constant ONE. */
   private static final int ONE = 1;

   private static final int ZERO = 0;

   /**
    * The TWO
    */
   private static final int TWO = 2;

   /** The default low deposit duration. */
   private static final String DEFAULT_LOWDEPOSIT_DURATION = "56";

   /** The default low deposit duration key. */
   private static final String DEFAULT_LOWDEPOSIT_DURATION_KEY = "default.lowdeposit.duration";

   /** The model service. */
   @Resource
   private ModelService packageModelService;

   @Resource
   private UserService userService;

   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;

   @Resource
   private UpdateExtraFacilityService updateExtraFacilityService;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The book flow urls. */
   @Resource
   private PropertyReader bookFlowUrls;

   @Resource
   private CurrencyResolver currencyResolver;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /**
    * @return the userService
    */
   public UserService getUserService()
   {
      return userService;
   }

   /**
    * @param userService the userService to set
    */
   public void setUserService(final UserService userService)
   {
      this.userService = userService;
   }

   /**
    * This method is used to populate the Package data from form bean and viewData.
    *
    * @param formBean the form bean
    * @param packageModel the inclusive package
    * @return the inclusive package model
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   @Override
   public BasePackage populatePackageData(final PassengerDetailsFormBean formBean,
      final BasePackage packageModel)
   {
      final List<Passenger> paxconfigList = new ArrayList<Passenger>();
      final boolean isFamilyInsurance =
         isFamilyInsuranceInPackage(packageModel.getExtraFacilityCategories());

      for (final PassengerInfoFormBean passengerInfo : formBean.getPaxInfoFormBean())
      {

         for (final Passenger passenger : packageModel.getPassengers())
         {
            if (passenger.getId()
               .equals(formBean.getPaxInfoFormBean().indexOf(passengerInfo) + ONE))
            {
               updatePaxPersonType(passengerInfo, passenger);

               checkFamilyInsurance(isFamilyInsurance, passengerInfo, passenger);
               passenger.setName(passengerInfo.getFirstName() + " " + passengerInfo.getLastName());
               passenger.setTitle(passengerInfo.getTitle());
               updateAgeForPax(passengerInfo, passenger);
               calculateAge(passengerInfo, packageModel, passenger);
               passenger.setLead(passengerInfo.isLeadPassenger());
               // Setting the Address model to every passenger
               setAddressModel(passenger, passengerInfo, formBean);
               paxconfigList.add(passenger);
            }
         }
      }
      packageModel.setPassengers(paxconfigList);
      populatePassengers(packageModel);
      updateExtraFacilityService.resetPassengersWithExtrafacilities();
      packageModelService.save(packageModel);
      return packageModel;
   }

   /**
    * Populate passengers to second cruise itinerary for BTB packages.
    *
    * @param packageModel
    */
   private void populatePassengers(final BasePackage packageModel)
   {
      final Stay cruiseItineraryTwo = packageComponentService.getCruise(packageModel, TWO);
      if (cruiseItineraryTwo != null)
      {
         for (final Room room : cruiseItineraryTwo.getRooms())
         {
            setPassengers(room, packageModel.getPassengers());
         }
      }
   }

   /**
    * Set passengers.
    *
    * @param room
    * @param passengers
    */
   private void setPassengers(final Room room, final List<Passenger> passengers)
   {
      final List<Passenger> allocatedPassengers = new ArrayList<Passenger>();
      for (final Passenger passenger : room.getPassgengers())
      {
         allocatedPassengers.add(getPassenger(passenger.getId(), passengers));
      }
      room.setPassgengers(allocatedPassengers);
   }

   /**
    * Get passenger.
    *
    * @param passengerId
    * @param passengers
    * @return passenger.
    */
   private Passenger getPassenger(final Integer passengerId, final List<Passenger> passengers)
   {
      for (final Passenger pax : passengers)
      {
         if (pax.getId() == passengerId)
         {
            return pax;
         }
      }
      return null;
   }

   private void checkFamilyInsurance(final boolean isFamilyInsurance,
      final PassengerInfoFormBean passengerInfo, final Passenger passenger)
   {
      if (!isFamilyInsurance)
      {
         updateInsuranceToPassenger(passenger, passengerInfo);
      }
   }

   /**
    * To updateAgeForPax
    *
    * @param passengerInfo PassengerInfoFormBean
    * @param passenger Passenger
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void updateAgeForPax(final PassengerInfoFormBean passengerInfo, final Passenger passenger)
   {
      if (childOrInfant(passengerInfo.getPersonType()))
      {
         passenger.setAge(NumberUtils.toInt(passengerInfo.getAge()));
      }
   }

   /**
    * To updatePaxPersonType
    *
    * @param passengerInfo PassengerInfoFormBean
    * @param passenger Passenger
    */
   private void updatePaxPersonType(final PassengerInfoFormBean passengerInfo,
      final Passenger passenger)
   {
      if (StringUtils.isEmpty(passengerInfo.getInsurancePersonType()))
      {
         passenger.setType(PersonType.valueOf(passengerInfo.getPersonType()));
      }
      else
      {
         passenger.setType(PersonType.valueOf(passengerInfo.getInsurancePersonType()));
      }
   }

   /**
    * @param extraFacilityCategories
    *
    */
   private boolean isFamilyInsuranceInPackage(
      final List<ExtraFacilityCategory> extraFacilityCategories)
   {
      for (final ExtraFacilityCategory categoryModel : extraFacilityCategories)
      {
         for (final ExtraFacility extra : categoryModel.getExtraFacilities())
         {
            if (extra instanceof InsuranceExtraFacility)
            {
               return ((InsuranceExtraFacility) extra).getInsuranceType() == InsuranceType.FAMILY;
            }
         }
      }
      return false;
   }

   /**
    * determins if the passenger is a Child or infant.
    *
    * @param personType the person type
    * @return true, if successful
    */
   private boolean childOrInfant(final String personType)
   {
      return StringUtils.equalsIgnoreCase(personType, PersonType.CHILD.toString())
         || StringUtils.equalsIgnoreCase(personType, PersonType.INFANT.toString());

   }

   /**
    * This method is used to populate the URL required for breadcrumb in payment page.
    *
    * @param viewData the view data
    * @param hostPrefix the host
    * @return Map<String, String>
    */
   @Override
   public Map<String, String> populateBreadcrumbUrls(final SummaryPanelViewData viewData,
      final String hostPrefix)
   {
      final String brand = packageCartService.getBasePackage().getBrandType().toString();
      final String httpBaseURL =
         Config.getString(StringUtil.append(brand, ".web.http.base.url"),
            "http://www.thomson.co.uk");

      final List<String> pageNames =
         getPageNamesFromConfig(StringUtil.append(packageCartService.getBasePackage()
            .getPackageType().toString(), ".pageOrder"));
      final Map<String, String> breadCrumbUrlMap = new LinkedHashMap<String, String>();
      for (final String pageName : pageNames)
      {
         mapPageURL(brand, httpBaseURL, breadCrumbUrlMap, pageName);
      }
      return breadCrumbUrlMap;
   }

   /**
    * @param brand
    * @param httpBaseURL
    * @param breadCrumbUrlMap
    * @param pageName
    */
   private void mapPageURL(final String brand, final String httpBaseURL,
      final Map<String, String> breadCrumbUrlMap, final String pageName)
   {
      String url =
         StringUtil.append(httpBaseURL, bookFlowUrls.getValue(brand + "." + pageName + ".url"));
      if ("IO".equals(pageName))
      {
         url =
            StringUtil.append(httpBaseURL,
               (String) sessionService.getAttribute(SessionObjectKeys.ITINERARY_PAGE_URL));
      }
      else if ("HO".equals(pageName))
      {
         url =
            StringUtil.append(httpBaseURL,
               (String) sessionService.getAttribute(SessionObjectKeys.ACCOMOPTIONS_URL));
      }
      else if ("HR".equals(pageName))
      {
         final String hotelResults = getHotelResultsPageURL();
         url = StringUtil.append(httpBaseURL, hotelResults);
      }
      breadCrumbUrlMap.put(bookFlowUrls.getValue(brand + "." + pageName + ".name"), url);
   }

   /**
    * @return hotelResults
    */
   private String getHotelResultsPageURL()
   {
      final Object hotelResultsUrl =
         sessionService.getAttribute(SessionObjectKeys.HOTEL_RESULTS_URL);
      String hotelResults = null;
      if (hotelResultsUrl != null)
      {
         hotelResults = (String) hotelResultsUrl;
      }
      return hotelResults;
   }

   /**
    * Gets the page names.
    *
    * @return the page names
    */
   private List<String> getPageNamesFromConfig(final String value)
   {
      final String pageOrder = bookFlowUrls.getValue(value);
      final List<String> pageNames = new ArrayList<String>();
      final String[] pages = StringUtils.split(pageOrder, ',');
      for (final String page : pages)
      {
         pageNames.add(page);
      }
      return pageNames;
   }

   /**
    * Update deposit component with outstanding amount.
    *
    * @param packageModel the package model
    */
   @Override
   public void updateDepositComponents(final BasePackage packageModel)
   {
      updateLowDepositOutStandingBalance(packageModel);
      updateDepositOutStandingBalance(packageModel);
   }

   /**
    * Update low deposit out standing balance.
    *
    * @param packageModel the package model
    */
   private void updateLowDepositOutStandingBalance(final BasePackage packageModel)
   {
      final List<Deposit> deposits = packageModel.getDeposits();
      final Set<DepositType> lowDepositType = EnumSet.of(DepositType.LOW_DEPOSIT);
      final Set<DepositType> depositType = EnumSet.of(DepositType.STANDARD_DEPOSIT);
      LowDeposit lowDepositModel = null;
      Deposit standardDepositModel = null;

      if (deposits != null)
      {
         for (final Deposit model : deposits)
         {
            lowDepositModel = updtLowDepositModel(lowDepositType, lowDepositModel, model);
            standardDepositModel = updtStandardDepoModel(depositType, standardDepositModel, model);
         }
      }

      updateDepositModel(lowDepositModel, standardDepositModel);
   }

   /**
    * To updtStandardDepoModel
    *
    * @param depositType Set<DepositType>
    * @param standardDepositModel Deposit
    * @param model Deposit
    * @return Deposit
    */
   private Deposit updtStandardDepoModel(final Set<DepositType> depositType,
      final Deposit standardDepositModel, final Deposit model)
   {
      Deposit stdModel = standardDepositModel;
      if (depositType.contains(model.getDepositType())
         && BooleanUtils.toBoolean(model.getApplicable()))
      {
         stdModel = model;
      }
      return stdModel;
   }

   /**
    * To updtLowDepositModel
    *
    * @param lowDepositType Set<DepositType>
    * @param lowDepositModel HolidayLowDeposit
    * @param model Deposit
    * @return HolidayLowDeposit
    */
   private LowDeposit updtLowDepositModel(final Set<DepositType> lowDepositType,
      final LowDeposit lowDepositModel, final Deposit model)
   {

      LowDeposit holidayLowDepModel = lowDepositModel;
      if (lowDepositType.contains(model.getDepositType())
         && BooleanUtils.toBoolean(model.getApplicable()))
      {
         holidayLowDepModel = LowDeposit.class.cast(model);
      }
      return holidayLowDepModel;
   }

   /**
    * To updateDepositModel
    *
    * @param lowDepositModel
    * @param standardDepositModel
    */
   private void updateDepositModel(final LowDeposit lowDepositModel,
      final Deposit standardDepositModel)
   {
      if (lowDepositModel != null && standardDepositModel != null)
      {
         final BigDecimal outStandingBalance =
            standardDepositModel.getDepositAmount().getAmount().getAmount()
               .subtract(lowDepositModel.getDepositAmount().getAmount().getAmount());
         final Price price = new Price();
         price.setAmount(convertBigDecimalToMoneyLite(outStandingBalance));
         lowDepositModel.setOutstandingBalance(price);
         lowDepositModel.setBalanceDueDate(getLowDepositBalanceDueDate(standardDepositModel
            .getBalanceDueDate()));
      }
   }

   /**
    * This method takes the value as BigDecimal and returns the MoneyModel object.
    *
    * @param value the value as BigDecimal.
    * @return MoneyModel
    */
   private Money convertBigDecimalToMoneyLite(final BigDecimal value)
   {
      final Money money = new Money();
      money.setAmount(value);
      money.setCurrency(Currency.getInstance(currencyResolver.getSiteCurrency()));
      return money;
   }

   /**
    * Gets the low deposit balance due date. if the calculated low deposit balance due date is
    * greater than deposit's balance due date then make low deposit's balanace due date and
    * deposit's due date same.
    *
    * @param standardDepBalanceDueDate the standard dep balance due date
    * @return the low deposit balance due date
    */
   private Date getLowDepositBalanceDueDate(final Date standardDepBalanceDueDate)
   {
      Date ldCollectionDate = getCollectionDate();
      if (ldCollectionDate.compareTo(standardDepBalanceDueDate) > 0)
      {
         ldCollectionDate = standardDepBalanceDueDate;
      }
      return ldCollectionDate;
   }

   /**
    * Update deposit out standing balance.
    *
    * @param packageModel the package model
    */
   private void updateDepositOutStandingBalance(final BasePackage packageModel)
   {
      final List<Deposit> deposits = packageModel.getDeposits();
      final Set<DepositType> depositType = EnumSet.of(DepositType.STANDARD_DEPOSIT);
      Deposit depositModel = null;
      depositModel = retrieveDepositModel(deposits, depositType, depositModel);
      if (depositModel != null)
      {
         final BigDecimal outStandingBalance =
            packageModel.getPrice().getAmount().getAmount()
               .subtract(depositModel.getDepositAmount().getAmount().getAmount());
         final Price price = new Price();
         price.setAmount(convertBigDecimalToMoneyLite(outStandingBalance));
         depositModel.setOutstandingBalance(price);
      }
   }

   /**
    * To retrieveDepositModel
    *
    * @param deposits List<Deposit>
    * @param depositType Set<DepositType>
    * @param depositModel Deposit
    * @return Deposit
    */
   private Deposit retrieveDepositModel(final List<Deposit> deposits,
      final Set<DepositType> depositType, final Deposit depositModel)
   {

      Deposit depModel = depositModel;

      if (deposits != null)
      {
         for (final Deposit model : deposits)
         {
            depModel = updtStandardDepoModel(depositType, depositModel, model);
         }
      }
      return depModel;
   }

   /**
    * Update the insurance to the passenger
    *
    * @param passenger
    * @param passengerInfo
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void updateInsuranceToPassenger(final Passenger passenger,
      final PassengerInfoFormBean passengerInfo)
   {
      final boolean isInsuranceSelected = isInsuranceSelected(passenger);
      // If for particular passenger Insurance PersonType is Adult, Child etc
      // ... Then enters into this loop.
      if (StringUtils.isNotEmpty(passengerInfo.getInsurancePersonType()) && !isInsuranceSelected)
      {
         addInsurance(passenger);
      }
      // If for a particluar passenger, insurance is not there then it enters
      // into this loop.
      else if (isInsuranceRemovable(passengerInfo, isInsuranceSelected))
      {
         removeInsurance(passenger);

      }
   }

   /**
    * Checks if is insurance removable.
    *
    * @param passengerInfo the passenger info
    * @param isInsuranceSelected the is insurance selected
    * @return true, if is insurance removable
    */
   private boolean isInsuranceRemovable(final PassengerInfoFormBean passengerInfo,
      final boolean isInsuranceSelected)
   {
      return isInsuranceSelected && StringUtils.isEmpty(passengerInfo.getInsurancePersonType());
   }

   /**
    * To update Pax With Insurance
    *
    * @param passenger Passenger
    *
    */
   private void addInsurance(final Passenger passenger)
   {
      final BasePackage packageModel = getPackageFromCart();
      for (final ExtraFacilityCategory extraFacilityCategory : packageModel
         .getExtraFacilityCategories())
      {
         if (extraFacilityCategory.getExtraFacilityGroup() == ExtraFacilityGroup.INSURANCE)
         {
            extraFacilityCategory.getExtraFacilities().get(ZERO).getPassengers().add(passenger);
         }
      }
   }

   /**
    *
    * @param passenger Passenger
    */
   @SuppressWarnings(BookFlowConstants.BOXING)
   private void removeInsurance(final Passenger passenger)
   {
      if (passenger.getExtraFacilities() != null)
      {
         removeInsuranceForNotReqPax(passenger);
      }

   }

   /**
    *
    * @param passenger Passenger
    * @return boolean
    */
   private boolean isInsuranceSelected(final Passenger passenger)
   {
      if (CollectionUtils.isNotEmpty(passenger.getExtraFacilities()))
      {
         for (final ExtraFacility extraFacility : passenger.getExtraFacilities())
         {
            if (extraFacility.getExtraFacilityGroup() == ExtraFacilityGroup.INSURANCE)
            {
               return true;
            }
         }
      }
      return false;
   }

   /**
    *
    * @param passenger
    */
   private void removeInsuranceForNotReqPax(final Passenger passenger)
   {
      for (final ExtraFacility extraFacility : passenger.getExtraFacilities())
      {
         obtainExtraFacilities(passenger, extraFacility);
      }
   }

   /**
    * @param passenger
    * @param extraFacility
    */
   private void obtainExtraFacilities(final Passenger passenger, final ExtraFacility extraFacility)
   {
      if (extraFacility.getExtraFacilityGroup() == ExtraFacilityGroup.INSURANCE)
      {
         for (final Iterator<Passenger> passengerIterator =
            extraFacility.getPassengers().iterator(); passengerIterator.hasNext();)
         {
            final Passenger passengerModel = passengerIterator.next();

            if (passenger.getId().equals(passengerModel.getId()))
            {
               passengerIterator.remove();
               break;
            }
         }
      }
   }

   /**
    * Sets the Address Model
    *
    * @param passenger
    * @param passengerInfo
    * @param formBean
    */
   private void setAddressModel(final Passenger passenger,
      final PassengerInfoFormBean passengerInfo, final PassengerDetailsFormBean formBean)
   {

      final List<Address> addressList = new ArrayList<Address>();
      final Address addressDetails = new Address();
      // Populating from Form Bean to Address Model

      passenger.setFirstname(passengerInfo.getFirstName());
      passenger.setLastname(passengerInfo.getLastName());
      passenger.setTitle(passengerInfo.getTitle());
      passenger.setGender(Gender.valueOf(passengerInfo.getGender()));
      updtDob(passengerInfo, passenger);
      updtAddressLine1(formBean, addressDetails);
      updtAddresLine2(formBean, addressDetails);

      updtTown(formBean, addressDetails);
      updtCounty(formBean, addressDetails);
      updtCountry(formBean, addressDetails);
      updtPhoneNumber(formBean, addressDetails);
      updtPostalCode(formBean, addressDetails);
      updtHouseNumber(formBean, addressDetails);
      updtEmail(formBean, addressDetails);
      addressList.add(addressDetails);
      passenger.setAddresses(addressList);
   }

   /**
    * To update DOB
    *
    * @param passengerInfo PassengerInfoFormBean
    * @param passenger Passenger
    */

   private void updtDob(final PassengerInfoFormBean passengerInfo, final Passenger passenger)
   {
      if (notEmptyAndNotBlank(passengerInfo.getYear())
         && notEmptyAndNotBlank(passengerInfo.getMonth())
         && notEmptyAndNotBlank(passengerInfo.getDay()))
      {

         @SuppressWarnings("deprecation")
         final Calendar cal = Utilities.getDefaultCalendar();
         cal.set(NumberUtils.toInt(passengerInfo.getYear()),
            NumberUtils.toInt(passengerInfo.getMonth()) - 1,
            NumberUtils.toInt(passengerInfo.getDay()));
         final String formatedDateOfBirth =
            cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
               + cal.get(Calendar.YEAR);
         passenger.setDateOfBirth(formatedDateOfBirth);
      }
   }

   /**
    * Update email
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtEmail(final PassengerDetailsFormBean formBean, final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getEmail()))
      {
         addressDetails.setEmail(formBean.getEmail());
      }
   }

   /**
    * To update house number
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtHouseNumber(final PassengerDetailsFormBean formBean,
      final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getHouseNum()))
      {
         addressDetails.setHouseNumber(formBean.getHouseNum());
      }
   }

   /**
    * To update postal code
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtPostalCode(final PassengerDetailsFormBean formBean, final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getPostCode()))
      {
         addressDetails.setPostalcode(formBean.getPostCode());
      }
   }

   /**
    * To update phone number
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtPhoneNumber(final PassengerDetailsFormBean formBean,
      final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getTelephoneNum()))
      {
         addressDetails.setPhone1(formBean.getTelephoneNum());
      }
   }

   /**
    * To update country
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtCounty(final PassengerDetailsFormBean formBean, final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getCounty()))
      {
         addressDetails.setCounty(formBean.getCounty());
      }
   }

   /**
    * Update country for falcon.
    *
    * @param formBean the form bean
    * @param addressDetails the address details
    */
   private void updtCountry(final PassengerDetailsFormBean formBean, final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getCountry()))
      {
         addressDetails.setCountry(formBean.getCountry());
      }
   }

   /**
    * To update town
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtTown(final PassengerDetailsFormBean formBean, final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getTown()))
      {
         addressDetails.setTown(formBean.getTown());
      }
   }

   /**
    * To update address line 1
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtAddresLine2(final PassengerDetailsFormBean formBean,
      final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getAddress2()))
      {
         addressDetails.setLine2(formBean.getAddress2());
      }
   }

   /**
    * To update addressline1
    *
    * @param formBean PassengerDetailsFormBean
    * @param addressDetails Address
    */
   private void updtAddressLine1(final PassengerDetailsFormBean formBean,
      final Address addressDetails)
   {
      if (notEmptyAndNotBlank(formBean.getAddress1()))
      {
         addressDetails.setLine1(formBean.getAddress1());
      }
   }

   /**
    * gets the package model from cart.
    *
    * @return the package from cart
    */
   private BasePackage getPackageFromCart()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * This is method is used to get the balance collection date.
    *
    * @return the collection date.
    */
   private Date getCollectionDate()
   {
      final String lowdepositDuration =
         Config.getString(DEFAULT_LOWDEPOSIT_DURATION_KEY, DEFAULT_LOWDEPOSIT_DURATION);
      return getBalanceCollectionDate(lowdepositDuration);
   }

   /**
    * This method is used to get the balance collection date.
    *
    * @param lowdepositDuration the deposit switch days which is configured in the configuration
    *           file.
    *
    * @return the balanceCollectiondate.
    */
   public Date getBalanceCollectionDate(final String lowdepositDuration)
   {
      @SuppressWarnings("boxing")
      final int balanceDueOffset = Integer.valueOf(lowdepositDuration);
      return DateUtils.stripTimePart(DateUtils.calculateEarlierORLaterDate(
         DateUtils.getSystemDate(), balanceDueOffset, false));
   }

   /**
    * Adult or senior or super senior.
    *
    * @param type the type
    * @return true, if adult or senior or super senior
    */
   private boolean adultOrSeniorOrSuperSenior(final PersonType type)
   {
      return EnumSet.of(PersonType.ADULT, PersonType.SENIOR, PersonType.SUPERSENIOR).contains(type);
   }

   /**
    * Calculate age.
    *
    * @param passengerInfo the passenger info
    * @param packageModel the package model
    * @param passenger the passenger
    */

   private void calculateAge(final PassengerInfoFormBean passengerInfo,
      final BasePackage packageModel, final Passenger passenger)
   {
      final String year = passengerInfo.getYear();
      final String month = passengerInfo.getMonth();
      final String day = passengerInfo.getDay();

      if (validDayMonthYear(day, month, year) && adultOrSeniorOrSuperSenior(passenger.getType()))
      {
         final Date flightDepartureDate =
            (packageComponentService.getFlightItinerary(packageModel)).getInBound().get(0)
               .getSchedule().getDepartureDate();
         final DateTime departureDate = new DateTime(flightDepartureDate);
         final LocalDate dateOfBirth =
            new LocalDate(NumberUtils.toInt(year), NumberUtils.toInt(month), NumberUtils.toInt(day));
         final LocalDate flightReturnDate =
            new LocalDate(departureDate.getYear(), departureDate.getMonthOfYear(),
               departureDate.getDayOfMonth());
         final Period period = new Period(dateOfBirth, flightReturnDate, PeriodType.yearMonthDay());
         passenger.setAge(Integer.valueOf(period.getYears()));
      }
   }

   /**
    * Valid day month year.
    *
    * @param day the day
    * @param month the month
    * @param year the year
    * @return true, if valid day month year
    */
   private boolean validDayMonthYear(final String day, final String month, final String year)
   {
      return StringUtils.isNotEmpty(day) && StringUtils.isNotEmpty(month)
         && StringUtils.isNotEmpty(year);
   }
}