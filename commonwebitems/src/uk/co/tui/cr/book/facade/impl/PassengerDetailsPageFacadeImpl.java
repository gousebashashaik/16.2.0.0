/**
 *
 */
package uk.co.tui.cr.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.CommunicationPreference;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.FlightLeg;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.InsuranceType;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.PassengerDetailsPageService;
import uk.co.tui.book.services.impl.UpdateExtraFacilityServiceImpl;
import uk.co.tui.book.services.inventory.CheckPriceAvailabilityService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.cr.book.ExtraFacilityUpdator;
import uk.co.tui.cr.book.constants.BookFlowConstants;
import uk.co.tui.cr.book.data.CommunicationPreferencesCriteria;
import uk.co.tui.cr.book.facade.PassengerDetailsPageFacade;
import uk.co.tui.cr.book.populators.AlertViewDataPopulator;
import uk.co.tui.cr.book.populators.PackageViewDataPopulator;
import uk.co.tui.cr.book.store.PackageExtraFacilityStore;
import uk.co.tui.cr.book.view.data.AlertViewData;
import uk.co.tui.cr.book.view.data.CommunicationPreferencesViewData;
import uk.co.tui.cr.book.view.data.DataProtectionViewData;
import uk.co.tui.cr.book.view.data.InfantNotYetBornViewData;
import uk.co.tui.cr.book.view.data.MemoViewData;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.PassengerData;
import uk.co.tui.cr.book.view.data.PassengerDetailsStaticContentViewData;
import uk.co.tui.cr.book.view.data.PassengerDetailsViewData;
import uk.co.tui.cr.book.view.data.PassengerInsuranceViewData;
import uk.co.tui.cr.book.view.data.PromotionalCodeViewData;
import uk.co.tui.cr.exception.TUIBusinessException;
import uk.co.tui.cr.exception.TUISystemException;


/**
 * @author uday.g
 *
 */
public class PassengerDetailsPageFacadeImpl implements PassengerDetailsPageFacade
{
	private static final TUILogUtils LOG = new TUILogUtils("PassengerDetailsPageFacadeImpl");

	/** Constant for Total cost */
	private static final String TOTAL_COST = "TC";

	private static final int ZERO = 0;

	private static final String DISPLAYTEXT = "Single trip - Age ";

	/** The Constant INSURANCE. */
	private static final String INSURANCE = "INS";

	private static final String INFOBOOK_SUCCESS = "0";

	private static final String REDIRECT_EXTRAS = "1";

	private static final String REDIRECT_ACCOM = "2";

	private static final int CHILD_AGE_LIMIT = 17;

	private static final int INFANT_AGE_LIMIT = 2;

	private static final int SENIOR_AGE_LIMIT = 65;

	private static final int SUPER_SENIOR_AGE_LIMIT = 75;

	private static final int ABOVE_SUPER_SENIOR_AGE_LIMIT = 86;

	@Resource(name = "crPackageViewDataPopulator")
	private PackageViewDataPopulator packageViewDataPopulator;

	/** The extra facility updator. */
	@Resource(name = "crExtraFacilityUpdator")
	private ExtraFacilityUpdator extraFacilityUpdator;

	@Resource
	private PassengerDetailsPageService passengerDetailsPageService;

	@Resource(name = "crAlertViewDataPopulator")
	private AlertViewDataPopulator alertViewDataPopulator;

	/** The passenger static content view data populator. */
	@Resource(name = "crPassengerStaticContentViewDataPopulator")
	private Populator<Object, PassengerDetailsStaticContentViewData> passengerStaticContentViewDataPopulator;

	@Resource(name = "crPromotionalDiscountViewDataPopulator")
	private Populator<BasePackage, PromotionalCodeViewData> promotionalDiscountViewDataPopulator;

	@Resource
	private CheckPriceAvailabilityService checkPriceAvailabilityService;

	@Resource(name = "crDataProtectionViewDataPopulator")
	private Populator<Object, DataProtectionViewData> dataProtectionViewDataPopulator;

	/** The model service. */
	@Resource
	private ModelService packageModelService;

	@Resource
	private PackageCartService packageCartService;

	@Resource
	private UpdateExtraFacilityServiceImpl updateExtraFacilityService;

	@Resource
	private SessionService sessionService;

	/** The package component service. */
	@Resource
	private PackageComponentService packageComponentService;

	/** The Constant MAX_PROMOCODE_LENGTH. */
	public static final String MAX_PROMOCODE_KEY = "max.%s.promocode.length";

	/** The Constant MAX_PERSONAL_PROMOCODE_LENGTH. */
	private static final int MAX_PERSONAL_PROMOCODE_LENGTH = 21;

	/** The Constant TUI_EXCEPTION. */
	private static final String TUI_EXCEPTION = "TUISystemException : ";

	private static final String MULTICOMERROR_PREFIX = "MC";

	private static final int SIXTEEN = 16;

	private static final int EIGHT = 8;

	private static final int TWO = 2;

	/**
	 * Render package view data.
	 *
	 */
	@Override
	public PackageViewData renderPackageViewData(final PassengerDetailsViewData paxViewData)
	{
		updatePackageIntoCart();
		final BasePackage inclPkgModel = getPackageModel();
		packageViewDataPopulator.populate(inclPkgModel, paxViewData.getPackageViewData());
		extraFacilityUpdator.updatePackageViewData(getPackageModel(), paxViewData.getPackageViewData());
		if (SyntacticSugar.isNotNull(inclPkgModel.getPromotionalDiscount()))
		{
			promotionalDiscountViewDataPopulator.populate(inclPkgModel, paxViewData.getPromotionalCodeViewData());
		}
		paxViewData.setInventoryType(packageCartService.getBasePackage().getInventory().getInventoryType().toString());
		paxViewData.setMinPromoCodeLength(Config.getInt("min.promocode.length", 1));
		paxViewData.setMaxPromoCodeLength(Config.getInt(
				String.format(MAX_PROMOCODE_KEY, getPackageModel().getInventory().getInventoryType().toString().toLowerCase()),
				getDefaultMaxPromoCodeLength()));
		paxViewData.setPersonalPromoCodeLength(Config.getInt("max.personal.promocode.length", MAX_PERSONAL_PROMOCODE_LENGTH));

		populatePassengerStaticContentViewData(paxViewData);

		// Checks if basic car hire is opted by the user and adds a errata about
		// the insurance
		if (inclPkgModel.getListOfHighlights().contains(HighLights.FREE_CAR_HIRE) && isBasicFloridaCarHireOpted(inclPkgModel))
		{
			addCarHireErrataInformation(paxViewData);
		}

		return paxViewData.getPackageViewData();
	}

	/**
	 * Gets the default max promo code length.
	 *
	 * @return the default max promo code length
	 */
	private int getDefaultMaxPromoCodeLength()
	{

		if (StringUtils.equalsIgnoreCase(getPackageModel().getInventory().getInventoryType().toString(),
				InventoryType.ATCOM.toString()))
		{
			return SIXTEEN;
		}
		return EIGHT;
	}

	/**
	 * To update for infant not yet born yet case
	 */

	@Override
	public InfantNotYetBornViewData updateInfantNotYetBornViewData(final String alertInfantNotBornMessages)
	{
		final InfantNotYetBornViewData infantNotYetBornViewData = new InfantNotYetBornViewData();
		final BasePackage basePackage = getPackageModel();
		final Itinerary flightItineraryModel = packageComponentService.getFlightItinerary(basePackage);
		final Leg flightLeg = flightItineraryModel.getOutBound().get(0);
		Date deptDate = flightLeg.getSchedule().getDepartureDate();
		if (basePackage.getInventory().getInventoryType() == InventoryType.ATCOM)
		{
			deptDate = ((FlightLeg) flightLeg).getCycDate();
		}
		infantNotYetBornViewData.setFirstName("TBC");
		infantNotYetBornViewData.setSurName("TBC");
		infantNotYetBornViewData.setTitle("MSTR");
		infantNotYetBornViewData.setGender("MALE");
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(deptDate);
		infantNotYetBornViewData.setDay(String.valueOf(calendar.get(Calendar.DATE)));
		infantNotYetBornViewData.setMonth(String.valueOf(calendar.get(Calendar.MONTH) + 1));
		infantNotYetBornViewData.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
		infantNotYetBornViewData.setAlertInfantNotYetBorn(alertInfantNotBornMessages);
		return infantNotYetBornViewData;
	}

	/**
	 * The method fetches the package model from the cart.
	 *
	 * @return BasePackage
	 */
	private BasePackage getPackageModel()
	{
		return packageCartService.getBasePackage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.tui.th.book.facade.FlightOptionsPageFacade#updatePackageIntoCart (uk.co.tui.domain.model.BasePackage)
	 */
	private void updatePackageIntoCart()
	{
		packageCartService.updateCart(getPackageModel());
	}

	/**
	 * Gets the passenger details from the package Model and returns it.
	 *
	 * @return passengerDataList
	 */
	@Override
	public PassengerInsuranceViewData getPassengerDetails()
	{
		final PassengerInsuranceViewData passengerInsuranceViewData = new PassengerInsuranceViewData();
		final BasePackage packageModel = getPackageModel();
		boolean isInsuranceSelected = false;

		isInsuranceSelected = isInsSelectedCatGrp(packageModel);

		// Is any insurance is selected then it enter into this loop
		if (isInsuranceSelected)
		{
			passengerInsuranceViewData.setFamilyInsPresent(getFamilyIns(packageModel.getExtraFacilityCategories()));
			// If it is not a family insurance then enters into this loop
			if (!passengerInsuranceViewData.isFamilyInsPresent())
			{
				familyInsNotPresent(packageModel, passengerInsuranceViewData);
			}
		}

		return passengerInsuranceViewData;
	}

	/**
	 * To find insurance selected from extrFacility category group
	 *
	 * @param packageModel
	 *           BasePackage
	 * @return boolean
	 */
	private boolean isInsSelectedCatGrp(final BasePackage packageModel)
	{
		for (final ExtraFacilityCategory extraFaciltyCategory : packageModel.getExtraFacilityCategories())
		{
			if (ExtraFacilityGroup.INSURANCE.equals(extraFaciltyCategory.getExtraFacilityGroup()))
			{
				return true;
			}
		}
		return false;
	}

	private void familyInsNotPresent(final BasePackage packageModel, final PassengerInsuranceViewData passengerInsuranceViewData)
	{

		final List<PassengerData> passengerDataList = new ArrayList<PassengerData>();
		final List<Passenger> passengersList = packageModel.getPassengers();
		final int infantCount = getCount(passengersList, PersonType.INFANT);
		final int childCount = getCount(passengersList, PersonType.CHILD);
		final int adultCount = getCount(passengersList, PersonType.ADULT);
		final int seniorCount = getCount(passengersList, PersonType.SENIOR);
		final int superSeniorCount = getCount(passengersList, PersonType.SUPERSENIOR);

		final int infantInsuranceCount = getInsuranceSelectedCount(passengersList, PersonType.INFANT);
		final int childInsuranceCount = getInsuranceSelectedCount(passengersList, PersonType.CHILD);
		final int adultInsuranceCount = getInsuranceSelectedCount(passengersList, PersonType.ADULT);
		final int seniorInsuranceCount = getInsuranceSelectedCount(passengersList, PersonType.SENIOR);
		final int superSeniorInsuranceCount = getInsuranceSelectedCount(passengersList, PersonType.SUPERSENIOR);

		final PersonType leadPassenger = getLeadPassengerPersonType(passengersList);
		// If personType count is not equal to zero and insurance
		// personType count is not equal to zero then it enters into the
		// below loops
		updtInfantPassData(infantCount, infantInsuranceCount, passengerDataList, leadPassenger);
		updtChildPassData(childCount, childInsuranceCount, passengerDataList, leadPassenger);
		updtAdultPassData(adultCount, adultInsuranceCount, passengerDataList, leadPassenger);
		updtSrPassData(seniorCount, seniorInsuranceCount, passengerDataList, leadPassenger);
		updtSuperSrPassData(superSeniorCount, superSeniorInsuranceCount, passengerDataList, leadPassenger);
		passengerInsuranceViewData.setPassengerDataList(passengerDataList);

	}

	/**
	 * update pax data for Infant
	 *
	 * @param infantCount
	 * @param infantInsuranceCount
	 * @param passengerDataList
	 * @param leadPassenger
	 */
	private void updtInfantPassData(final int infantCount, final int infantInsuranceCount,
			final List<PassengerData> passengerDataList, final PersonType leadPassenger)
	{
		if (infantCount != ZERO && infantInsuranceCount != ZERO)
		{
			passengerDataList.add(setPassengerData(infantCount, infantInsuranceCount, PersonType.INFANT, leadPassenger));
		}
	}

	/**
	 * update pax data for Child
	 *
	 * @param childCount
	 * @param childInsuranceCount
	 * @param passengerDataList
	 * @param leadPassenger
	 */
	private void updtChildPassData(final int childCount, final int childInsuranceCount,
			final List<PassengerData> passengerDataList, final PersonType leadPassenger)
	{
		if (childCount != ZERO && childInsuranceCount != ZERO)
		{
			passengerDataList.add(setPassengerData(childCount, childInsuranceCount, PersonType.CHILD, leadPassenger));
		}
	}

	/**
	 * Update pax data for adult
	 *
	 * @param adultCount
	 * @param adultInsuranceCount
	 * @param passengerDataList
	 * @param leadPassenger
	 */
	private void updtAdultPassData(final int adultCount, final int adultInsuranceCount,
			final List<PassengerData> passengerDataList, final PersonType leadPassenger)
	{
		if (adultCount != ZERO && adultInsuranceCount != ZERO)
		{
			passengerDataList.add(setPassengerData(adultCount, adultInsuranceCount, PersonType.ADULT, leadPassenger));
		}
	}

	/**
	 * Update senior pasenger data
	 *
	 * @param seniorCount
	 * @param seniorInsuranceCount
	 * @param passengerDataList
	 * @param leadPassenger
	 */
	private void updtSrPassData(final int seniorCount, final int seniorInsuranceCount,
			final List<PassengerData> passengerDataList, final PersonType leadPassenger)
	{
		if (seniorCount != ZERO && seniorInsuranceCount != ZERO)
		{
			passengerDataList.add(setPassengerData(seniorCount, seniorInsuranceCount, PersonType.SENIOR, leadPassenger));
		}
	}

	/**
	 * Update Super senior
	 *
	 * @param superSeniorCount
	 * @param superSeniorInsuranceCount
	 * @param passengerDataList
	 * @param leadPassenger
	 */
	private void updtSuperSrPassData(final int superSeniorCount, final int superSeniorInsuranceCount,
			final List<PassengerData> passengerDataList, final PersonType leadPassenger)
	{

		if (superSeniorCount != ZERO && superSeniorInsuranceCount != ZERO)
		{
			passengerDataList.add(setPassengerData(superSeniorCount, superSeniorInsuranceCount, PersonType.SUPERSENIOR,
					leadPassenger));
		}
	}

	/**
	 * Gets the boolean value of Family Insurance
	 *
	 * @param extraFacilityCategorylist
	 *           List<ExtraFacilityCategory>
	 * @return boolean
	 */
	@SuppressWarnings(BookFlowConstants.BOXING)
	private boolean getFamilyIns(final List<ExtraFacilityCategory> extraFacilityCategorylist)
	{
		boolean familyInsPresent = false;
		if (extraFacilityCategorylist != null)
		{
			for (final ExtraFacilityCategory extraFacilityCategoryModel : extraFacilityCategorylist)
			{
				if (ExtraFacilityGroup.INSURANCE.equals(extraFacilityCategoryModel.getExtraFacilityGroup()))
				{
					final InsuranceExtraFacility insuranceExtraFacilityModel = (InsuranceExtraFacility) (extraFacilityCategoryModel
							.getExtraFacilities().get(0));
					familyInsPresent = isFamilyPresent(insuranceExtraFacilityModel);
				}
			}
		}
		return familyInsPresent;
	}

	private boolean isFamilyPresent(final InsuranceExtraFacility insuranceExtraFacilityModel)
	{
		return insuranceExtraFacilityModel.getInsuranceType() == InsuranceType.FAMILY;
	}

	/**
	 * Gets the count of the number of personType in the passengerList
	 *
	 * @param passengersList
	 * @return count
	 */
	private int getCount(final List<Passenger> passengersList, final PersonType personType)
	{
		int count = ZERO;
		for (final Passenger passenger : passengersList)
		{

			if (passenger.getType() != null && passenger.getType().equals(personType))
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * @param count
	 * @param leadPassenger
	 */
	private PassengerData setPassengerData(final int count, final int insuranceCount, final PersonType personType,
			final PersonType leadPassenger)
	{
		final PassengerData passengerData = new PassengerData();
		passengerData.setAgeCode(String.valueOf(personType));
		passengerData.setCountOfPaxs(String.valueOf(count));
		if (leadPassenger != null && leadPassenger.equals(personType))
		{
			passengerData.setLead(true);
		}
		// Temporary fix, need to revisit for min and max age of the person type

		final String ageRange = passengerDetailsPageService.getAgeCode(personType);
		passengerData.setMinAge(getMinAge(ageRange));
		passengerData.setMaxAge(getMaxAge(ageRange));
		passengerData.setNoOfPaxsInsSelected(String.valueOf(insuranceCount));
		passengerData.setDisplayText(DISPLAYTEXT.concat(ageRange));
		return passengerData;

	}

	/**
	 * @param ageRange
	 * @return String
	 */
	private String getMaxAge(final String ageRange)
	{
		return ageRange.substring(ageRange.indexOf('-') + 1, ageRange.length());
	}

	/**
	 * @param ageRange
	 * @return String
	 */
	private String getMinAge(final String ageRange)
	{
		return ageRange.substring(0, ageRange.indexOf('-'));
	}

	/**
	 * @param passengersList
	 * @param personType
	 * @return int
	 */
	private int getInsuranceSelectedCount(final List<Passenger> passengersList, final PersonType personType)
	{
		boolean insuranceFalg;
		int count = ZERO;
		for (final Passenger passenger : passengersList)
		{
			if (passenger.getType().equals(personType))
			{
				insuranceFalg = false;
				insuranceFalg = insuranceSelectionCheck(passenger);
				if (insuranceFalg)
				{
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Finding insurance selected or not
	 *
	 * @param passenger
	 */
	private boolean insuranceSelectionCheck(final Passenger passenger)
	{
		if (CollectionUtils.isNotEmpty(passenger.getExtraFacilities()))
		{
			for (final ExtraFacility extraFacility : passenger.getExtraFacilities())
			{
				if ((extraFacility.getExtraFacilityGroup()).equals(ExtraFacilityGroup.INSURANCE))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param passengersList
	 * @return PersonType
	 */
	private PersonType getLeadPassengerPersonType(final List<Passenger> passengersList)
	{
		PersonType personType = null;
		for (final Passenger passenger : passengersList)
		{
			if (passenger.getId().equals(Integer.valueOf(1)))
			{
				personType = passenger.getType();
			}
		}
		return personType;
	}

	/**
	 * @param dobCalendar
	 * @param arrivalDateCalendar
	 * @return int age
	 */
	private static int getAgeInYears(final Calendar dobCalendar, final Calendar arrivalDateCalendar)
	{
		int ageInYears = 0;
		// counting age
		while (true)
		{
			dobCalendar.add(Calendar.YEAR, 1);
			if (dobCalendar.after(arrivalDateCalendar))
			{
				break;
			}
			// if dob before arrival date increasing count by one.
			ageInYears++;
		}
		return ageInYears;
	}

	/**
	 * To calculate age from date of birth
	 *
	 * @param dob
	 * @return int
	 */
	private int getAge(final String dob)
	{
		int ageCalculated = 0;

		@SuppressWarnings("deprecation")
		final Calendar dobCalendar = Utilities.getDefaultCalendar();

		@SuppressWarnings("deprecation")
		final Calendar arrivalDateCalendar = Utilities.getDefaultCalendar();
		Date dateOfBirth = null;
		final String[] ddmmyy = StringUtils.split(dob, "/");
		dateOfBirth = DateUtils.toUtilDate(ddmmyy[0], ddmmyy[1], ddmmyy[TWO]);
		dobCalendar.setTime(dateOfBirth);
		final BasePackage packageModel = getPackageModel();
		final Date flightArrivalDate = (packageComponentService.getFlightItinerary(packageModel)).getOutBound().get(0)
				.getSchedule().getArrivalDate();
		arrivalDateCalendar.setTime(flightArrivalDate);
		ageCalculated = getAgeInYears(dobCalendar, arrivalDateCalendar);
		return ageCalculated;
	}

	/**
	 * Updates passenger model
	 *
	 * @param passModelLst
	 *           List<Passenger>
	 * @param id
	 *           int
	 * @param calculatedAge
	 *           int
	 * @return initage int
	 */
	@SuppressWarnings(BookFlowConstants.BOXING)
	private int updatePassModel(final List<Passenger> passModelLst, final int id, final int calculatedAge)
	{
		int initage = -1;

		for (final Passenger passModel : passModelLst)
		{
			if (passModel.getId().intValue() == id)
			{
				/* 18 64 :Adult 65 74 :Sr 75 85:Super Sr */
				initage = passModel.getAge().intValue();
				updtPassAgeAndType(passModel, calculatedAge);
				break;
			}
		}

		return initage;
	}

	/**
	 * Updating passenger age and Type base don the date of birth
	 *
	 * @param passModel
	 *           Passenger
	 * @param calculatedAge
	 *           int
	 */
	@SuppressWarnings(BookFlowConstants.BOXING)
	private void updtPassAgeAndType(final Passenger passModel, final int calculatedAge)
	{
		passModel.setType(PersonType.CHILD);
		if (calculatedAge > CHILD_AGE_LIMIT)
		{
			/* child becomes adult */
			updateSrAndAdultPassModel(passModel, calculatedAge);

		}
		else if (calculatedAge < INFANT_AGE_LIMIT)
		{
			/* become infant */
			passModel.setType(PersonType.INFANT);
		}

		passModel.setAge(Integer.valueOf(calculatedAge));
	}

	/**
	 * Update the passenger model for adult, senior ,supersenior
	 *
	 * @param passModel
	 *           Passenger
	 * @param calculatedAge
	 *           int
	 */
	private void updateSrAndAdultPassModel(final Passenger passModel, final int calculatedAge)
	{

		boolean ageChangeFlag = false;
		ageChangeFlag = setAdultType(passModel, calculatedAge, ageChangeFlag);
		ageChangeFlag = setSeniorType(passModel, calculatedAge, ageChangeFlag);
		ageChangeFlag = setSuperSeniorType(passModel, calculatedAge, ageChangeFlag);

	}

	/**
	 * Set adult type
	 *
	 * @param passModel
	 *           Passenger
	 * @param calculatedAge
	 *           int
	 * @param ageChangeFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean setAdultType(final Passenger passModel, final int calculatedAge, final boolean ageChangeFlag)
	{
		if (ageChangeFlag)
		{
			return true;
		}

		if (calculatedAge > CHILD_AGE_LIMIT && calculatedAge < SENIOR_AGE_LIMIT)
		{
			passModel.setType(PersonType.ADULT);
			return true;
		}
		return false;
	}

	/**
	 * Set Sr type
	 *
	 * @param passModel
	 *           Passenger
	 * @param calculatedAge
	 *           int
	 * @param ageChangeFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean setSeniorType(final Passenger passModel, final int calculatedAge, final boolean ageChangeFlag)
	{
		if (ageChangeFlag)
		{
			return true;
		}

		if (calculatedAge > (SENIOR_AGE_LIMIT - 1) && calculatedAge < SUPER_SENIOR_AGE_LIMIT)
		{
			passModel.setType(PersonType.SENIOR);
			return true;
		}
		return false;
	}

	/**
	 * Set Super Sr type
	 *
	 * @param passModel
	 *           Passenger
	 * @param calculatedAge
	 *           int
	 * @param ageChangeFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean setSuperSeniorType(final Passenger passModel, final int calculatedAge, final boolean ageChangeFlag)
	{
		if (ageChangeFlag)
		{
			return true;
		}

		if (calculatedAge > (SUPER_SENIOR_AGE_LIMIT - 1) && calculatedAge < ABOVE_SUPER_SENIOR_AGE_LIMIT)
		{
			passModel.setType(PersonType.SUPERSENIOR);
			return true;
		}
		return false;
	}

	/**
	 * Updating the alert list
	 *
	 * @param feedBackList
	 * @return List<AlertViewData> alertViewDataList
	 */
	private List<AlertViewData> updateAlertList(final List<Feedback> feedBackList)
	{
		final List<AlertViewData> alertViewDataList = new ArrayList<AlertViewData>();
		if (feedBackList != null)
		{
			for (final Feedback feedback : feedBackList)
			{
				if (StringUtils.equalsIgnoreCase(feedback.getCode(), TOTAL_COST))
				{
					final AlertViewData alertViewdata = new AlertViewData();
					alertViewDataPopulator.populate(feedback, alertViewdata);
					alertViewDataList.add(alertViewdata);
				}
			}
		}
		return alertViewDataList;
	}

	/**
	 * This trigger info book request and check party can be fit
	 *
	 * @param id
	 *           int id of pax
	 * @param dateofbirth
	 *           String date of birth of child/infant entered
	 * @throws TUIBusinessException
	 */
	@SuppressWarnings(BookFlowConstants.BOXING)
	@Override
	public List<AlertViewData> partyAgeValCheck(final int id, final String dateofbirth, final PassengerDetailsViewData paxViewdata)
			throws TUIBusinessException
	{

		int initage = 0;
		boolean insuranceSelFlag = false;
		final BasePackage pkg = getPackageModel();
		List<Feedback> feedBackList = Collections.emptyList();
		List<AlertViewData> alertViewDataList = Collections.emptyList();
		final List<Passenger> passModelLst = pkg.getPassengers();

		// Updating the passenger type when child becomes adult
		final int calculatedAge = getAge(dateofbirth);
		initage = updatePassModel(passModelLst, id, calculatedAge);

		updatePackageModel(pkg, passModelLst);
		feedBackList = performInfoBook(pkg, paxViewdata);

		if (paxViewdata.getValStatusFlag() == REDIRECT_ACCOM)
		{
			return alertViewDataList;
		}
		alertViewDataList = updateAlertList(feedBackList);
		insuranceSelFlag = isInsuranceSelected(pkg);
		cartUpdtn(pkg, paxViewdata, initage, calculatedAge, insuranceSelFlag);
		LOG.info("package code in cart : " + pkg.getId());
		return alertViewDataList;
	}

	@Override
	public String getUpdatedAge(final String dateofbirth)
	{
		return Integer.toString(getAge(dateofbirth));
	}

	/**
	 * Save package and update in cart
	 *
	 * @param pkg
	 * @param passModelLst
	 */
	private void updatePackageModel(final BasePackage pkg, final List<Passenger> passModelLst)
	{
		pkg.setPassengers(passModelLst);
		packageModelService.save(pkg);
		packageCartService.replace(pkg);
	}

	/**
	 * To check weather insurance is selected
	 *
	 * @param pkg
	 * @return boolean
	 */
	private boolean isInsuranceSelected(final BasePackage pkg)
	{
		for (final ExtraFacilityCategory categoryModel : pkg.getExtraFacilityCategories())
		{
			if (StringUtils.equalsIgnoreCase(INSURANCE, categoryModel.getCode()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Performing info-book request
	 *
	 * @param pkg
	 * @param paxViewdata
	 * @return List<Feedback> feedBackList
	 */
	private List<Feedback> performInfoBook(final BasePackage pkg, final PassengerDetailsViewData paxViewdata)
	{
		List<Feedback> feedBackList = null;
		try
		{
			feedBackList = checkPriceAvailabilityService.updatePriceAndAvailability(pkg);
			paxViewdata.setValStatusFlag(INFOBOOK_SUCCESS);
		}
		catch (final BookServiceException e)
		{
			paxViewdata.setValStatusFlag(REDIRECT_ACCOM);
			LOG.error("TUISystemException : ", e);
			return feedBackList;
		}
		return feedBackList;
	}

	/**
	 * Perform info book.
	 *
	 * @param pkg
	 *           the pkg
	 * @return the list
	 * @throws TUIBusinessException
	 *            the tUI business exception
	 * @throws FlightSoldOutException
	 *            the flight sold out exception
	 */
	@Override
	public List<Feedback> performInfoBook(final BasePackage pkg) throws TUIBusinessException, FlightSoldOutException
	{
		try
		{
			return checkPriceAvailabilityService.updatePriceAndAvailability(pkg);
		}
		catch (final BookServiceException e)
		{
			final List<String> multicomErrorCodes = Arrays
					.asList(StringUtils.split(Config.getParameter("multicom_error_codes"), ','));
			final String errorCode = StringUtils.trim(e.getErrorCode());
			if (isMulticomErrorCodes(multicomErrorCodes, errorCode))
			{
				throw new FlightSoldOutException(e.getErrorCode(), e);
			}
			LOG.error(TUI_EXCEPTION + e.getMessage());
			final List<String> soldOutErrorCodes = Arrays.asList(StringUtils.split(Config.getParameter("soldOutErrorCodes"), ','));
			if (isSoldoutError(e.getErrorCode(), soldOutErrorCodes))
			{
				throw new TUIBusinessException(e.getErrorCode(), e.getCustomMessage(), e);
			}
			else
			{
				throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
			}
		}
	}

	/**
	 * @param multicomErrorCodes
	 * @param errorCode
	 * @return
	 */
	private boolean isMulticomErrorCodes(final List<String> multicomErrorCodes, final String errorCode)
	{
		return multicomErrorCodes.contains(errorCode) || StringUtils.startsWithIgnoreCase(errorCode, MULTICOMERROR_PREFIX);
	}

	/**
	 * Checks if is soldout error.
	 *
	 * @param errorCode
	 *           the error code
	 * @param soldOutErrorCodes
	 * @return true if the error is of type soldout error
	 */
	private boolean isSoldoutError(final String errorCode, final List<String> soldOutErrorCodes)
	{
		// Comparison logic has been moved to TibcoSoapFaultHandler
		return PackageUtilityService.isSoldOutErrorMatches(errorCode, soldOutErrorCodes);
	}

	/**
	 *
	 * @param pkg
	 * @param paxViewdata
	 * @param initage
	 * @param calculatedAge
	 * @param insuranceSelFlag
	 */
	private void cartUpdtn(final BasePackage pkg, final PassengerDetailsViewData paxViewdata, final int initage,
			final int calculatedAge, final boolean insuranceSelFlag)
	{

		if (insuranceSelFlag)
		{
			boolean ageTranFlag = false;
			ageTranFlag = infantToChild(pkg, paxViewdata, initage, calculatedAge, ageTranFlag);
			ageTranFlag = childToInfant(pkg, paxViewdata, initage, calculatedAge, ageTranFlag);
			ageTranFlag = infantToAdult(pkg, paxViewdata, initage, calculatedAge, ageTranFlag);
			ageTranFlag = adultToInfant(pkg, paxViewdata, initage, calculatedAge, ageTranFlag);
			ageTranFlag = childToAdult(pkg, paxViewdata, initage, calculatedAge, ageTranFlag);
			ageTranFlag = adultToChild(pkg, paxViewdata, initage, calculatedAge, ageTranFlag);

		}
		else
		{
			removeInsuranceFromPkgStore();
		}

	}

	/**
	 * InfantToChild updation
	 *
	 * @param pkg
	 *           BasePackage
	 * @param paxViewdata
	 *           PassengerDetailsViewData
	 * @param initage
	 *           int
	 * @param calculatedAge
	 *           int
	 * @param ageTranFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean infantToChild(final BasePackage pkg, final PassengerDetailsViewData paxViewdata, final int initage,
			final int calculatedAge, final boolean ageTranFlag)
	{
		if (ageTranFlag)
		{
			return true;
		}

		if (initage < INFANT_AGE_LIMIT && calculatedAge >= INFANT_AGE_LIMIT)
		{
			// infant ->child
			handlePartyAgeChg(paxViewdata, pkg);
			return true;
		}
		return false;
	}

	/**
	 * ChildToInfant updation
	 *
	 * @param pkg
	 *           BasePackage
	 * @param paxViewdata
	 *           PassengerDetailsViewData
	 * @param initage
	 *           int
	 * @param calculatedAge
	 *           int
	 * @param ageTranFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean childToInfant(final BasePackage pkg, final PassengerDetailsViewData paxViewdata, final int initage,
			final int calculatedAge, final boolean ageTranFlag)
	{
		if (ageTranFlag)
		{
			return true;
		}
		if (initage >= INFANT_AGE_LIMIT && calculatedAge < INFANT_AGE_LIMIT)
		{
			// Child ->infant
			handlePartyAgeChg(paxViewdata, pkg);
			return true;
		}
		return false;
	}

	/**
	 * InfantToAdult updation
	 *
	 * @param pkg
	 *           BasePackage
	 * @param paxViewdata
	 *           PassengerDetailsViewData
	 * @param initage
	 *           int
	 * @param calculatedAge
	 *           int
	 * @param ageTranFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean infantToAdult(final BasePackage pkg, final PassengerDetailsViewData paxViewdata, final int initage,
			final int calculatedAge, final boolean ageTranFlag)
	{

		if (ageTranFlag)
		{
			return true;
		}
		if (initage < INFANT_AGE_LIMIT && calculatedAge > CHILD_AGE_LIMIT)
		{
			// infant ->Adult
			handlePartyAgeChg(paxViewdata, pkg);
			return true;
		}
		return false;
	}

	/**
	 * AdultToInfant updation
	 *
	 * @param pkg
	 *           BasePackage
	 * @param paxViewdata
	 *           PassengerDetailsViewData
	 * @param initage
	 *           int
	 * @param calculatedAge
	 *           int
	 * @param ageTranFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean adultToInfant(final BasePackage pkg, final PassengerDetailsViewData paxViewdata, final int initage,
			final int calculatedAge, final boolean ageTranFlag)
	{

		if (ageTranFlag)
		{
			return true;
		}

		if (initage > CHILD_AGE_LIMIT && calculatedAge < INFANT_AGE_LIMIT)
		{
			// Adult ->infant
			handlePartyAgeChg(paxViewdata, pkg);
			return true;
		}
		return false;
	}

	/**
	 * ChildToAdult updation
	 *
	 * @param pkg
	 *           BasePackage
	 * @param paxViewdata
	 *           PassengerDetailsViewData
	 * @param initage
	 *           int
	 * @param calculatedAge
	 *           int
	 * @param ageTranFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean childToAdult(final BasePackage pkg, final PassengerDetailsViewData paxViewdata, final int initage,
			final int calculatedAge, final boolean ageTranFlag)
	{
		if (ageTranFlag)
		{
			return true;
		}

		if (initage >= INFANT_AGE_LIMIT && calculatedAge > CHILD_AGE_LIMIT)
		{
			// Child ->ADULT
			handlePartyAgeChg(paxViewdata, pkg);
			return true;
		}
		return false;
	}

	/**
	 * AdultToChild updation
	 *
	 * @param pkg
	 *           BasePackage
	 * @param paxViewdata
	 *           PassengerDetailsViewData
	 * @param initage
	 *           int
	 * @param calculatedAge
	 *           int
	 * @param ageTranFlag
	 *           boolean
	 * @return boolean
	 */
	private boolean adultToChild(final BasePackage pkg, final PassengerDetailsViewData paxViewdata, final int initage,
			final int calculatedAge, final boolean ageTranFlag)
	{

		if (ageTranFlag)
		{
			return true;
		}

		if (initage > CHILD_AGE_LIMIT && calculatedAge >= INFANT_AGE_LIMIT)
		{
			// Adult ->Child
			handlePartyAgeChg(paxViewdata, pkg);
			return true;
		}
		return false;
	}

	/**
	 * To handle PartyAgeChange
	 *
	 * @param paxViewdata
	 * @param pkg
	 */
	private void handlePartyAgeChg(final PassengerDetailsViewData paxViewdata, final BasePackage pkg)
	{
		paxViewdata.setValStatusFlag(REDIRECT_EXTRAS);
		updateExtraFacilityService.resetExtraFacility(pkg, INSURANCE);
		removeInsuranceFromPkgStore();
		packageCartService.replace(pkg);
	}

	/**
	 * Removes the insurance from pkg store.
	 */
	private void removeInsuranceFromPkgStore()
	{

		final BasePackage packageModel = getPackageModel();
		final PackageExtraFacilityStore packageExtraFacilityStore = getPackageExtraFacilityStore();
		List<ExtraFacilityCategory> extraFacilityCategoriesInStore = Collections.emptyList();
		if (SyntacticSugar.isNotNull(packageExtraFacilityStore))
		{
			extraFacilityCategoriesInStore = packageExtraFacilityStore.getExtraFacilityLite(packageModel.getId());
			final Iterator<ExtraFacilityCategory> categoryIterator = extraFacilityCategoriesInStore.iterator();
			while (categoryIterator.hasNext())
			{
				final ExtraFacilityCategory categoryModel = categoryIterator.next();
				if (StringUtils.equalsIgnoreCase(INSURANCE, categoryModel.getCode()))
				{
					categoryIterator.remove();
				}
			}
		}
	}

	/**
	 * Populate passenger static content view data.
	 *
	 * @param viewData
	 *           the view data
	 */
	@Override
	public void populatePassengerStaticContentViewData(final PassengerDetailsViewData viewData)
	{
		final PassengerDetailsStaticContentViewData passengerDetailsStaticContentViewData = new PassengerDetailsStaticContentViewData();
		passengerStaticContentViewDataPopulator.populate(new Object(), passengerDetailsStaticContentViewData);
		viewData.setPassengerDetailsStaticContentViewData(passengerDetailsStaticContentViewData);
	}

	/**
	 * Populates the data protection content
	 */
	@Override
	public void populateDataProtectionContent(final PassengerDetailsViewData viewData)
	{
		final DataProtectionViewData dataProtectionViewData = new DataProtectionViewData();
		dataProtectionViewDataPopulator.populate(new Object(), dataProtectionViewData);
		viewData.setDataProtectionViewData(dataProtectionViewData);
	}

	/**
	 * Adds Errata when basic car hire selected for Florida destination.
	 *
	 * @param paxViewData
	 */
	private void addCarHireErrataInformation(final PassengerDetailsViewData paxViewData)
	{
		MemoViewData memoViewData = paxViewData.getPackageViewData().getMemoViewData();
		if (memoViewData == null)
		{
			memoViewData = new MemoViewData();
		}
		memoViewData.setAvailable(true);
		memoViewData.getDescription().add(
				paxViewData.getPassengerDetailsStaticContentViewData().getPassengerContentMap().get("CAR_HIRE_ERRATA_MSG"));
	}

	/**
	 * Checks if basic car hire is opted by the user for Florida destination
	 *
	 * @param packageModel
	 * @return boolean
	 */
	private boolean isBasicFloridaCarHireOpted(final BasePackage packageModel)
	{
		for (final ExtraFacilityCategory extraFacilityCategoryModel : packageModel.getExtraFacilityCategories())
		{
			if ((StringUtils.equalsIgnoreCase(extraFacilityCategoryModel.getSuperCategoryCode(), ExtraFacilityConstants.CAR_HIRE))
					&& (isBasicCarHireOpted(extraFacilityCategoryModel)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * To check whether basic car hire opted or not
	 *
	 * @param extraFacilityCategoryModel
	 * @return boolean
	 */
	private boolean isBasicCarHireOpted(final ExtraFacilityCategory extraFacilityCategoryModel)
	{
		for (final ExtraFacility extraFacilityModel : extraFacilityCategoryModel.getExtraFacilities())
		{
			if (StringUtils.equalsIgnoreCase(extraFacilityModel.getExtraFacilityCode(), ExtraFacilityConstants.BASIC_CARHIRE))
			{
				return true;
			}
		}
		return false;

	}

	/**
	 * Creates the communication preferences.
	 */
	@Override
	public void createCommunicationPreferences()
	{
		final BasePackage packageModel = getPackageModel();
		CommunicationPreference communicationPreferenceModel = null;
		if (SyntacticSugar.isNull(packageModel.getCommunicationPreference()))
		{
			communicationPreferenceModel = new CommunicationPreference();
			populateDefaultValues(communicationPreferenceModel);
			packageModel.setCommunicationPreference(communicationPreferenceModel);
			packageModelService.save(packageModel);
		}
	}

	/**
	 * Populate default values.
	 *
	 * @param communicationPreferenceModel
	 *           the communication preference model
	 */
	private void populateDefaultValues(final CommunicationPreference communicationPreferenceModel)
	{
		communicationPreferenceModel.setByEmail(false);
		communicationPreferenceModel.setByPhone(false);
		communicationPreferenceModel.setByPost(false);
		communicationPreferenceModel.setThirdPartySharing(false);
	}

	/**
	 * To update communication preferences in packageModel
	 *
	 * @param communicationPreferencesCriteria
	 */

	@Override
	public void updateCommunicationPreferences(final CommunicationPreferencesCriteria communicationPreferencesCriteria)
	{
		final BasePackage packageModel = getPackageModel();
		CommunicationPreference communicationPreferenceModel = null;
		if (SyntacticSugar.isNotNull(packageModel.getCommunicationPreference()))
		{
			communicationPreferenceModel = packageModel.getCommunicationPreference();
			updatePreferences(communicationPreferencesCriteria, communicationPreferenceModel);
			packageModel.setCommunicationPreference(communicationPreferenceModel);
			packageModelService.save(packageModel);
		}
	}

	/**
	 * @param communicationPreferencesCriteria
	 * @param packageModel
	 * @param communicationPreferenceModel
	 */
	private void updatePreferences(final CommunicationPreferencesCriteria communicationPreferencesCriteria,
			final CommunicationPreference communicationPreferenceModel)
	{
		communicationPreferenceModel.setByEmail(communicationPreferencesCriteria.isCommunicateByEmail());
		communicationPreferenceModel.setByPhone(communicationPreferencesCriteria.isCommunicateByPhone());
		communicationPreferenceModel.setByPost(communicationPreferencesCriteria.isCommunicateByPost());
		communicationPreferenceModel.setThirdPartySharing(communicationPreferencesCriteria.isChkThirdPartyMarketingAllowed());
		communicationPreferenceModel.setReceiveCommunication(isSelectedAnyMedium(communicationPreferencesCriteria));
	}

	/**
	 * @param communicationPreferencesCriteria
	 * @return
	 */
	@SuppressWarnings("boxing")
	private boolean isSelectedAnyMedium(final CommunicationPreferencesCriteria communicationPreferencesCriteria)
	{
		return communicationPreferencesCriteria.isCommunicateByEmail() || communicationPreferencesCriteria.isCommunicateByPhone()
				|| communicationPreferencesCriteria.isCommunicateByPost();
	}

	/**
	 * To get communication preferences from packageModel
	 *
	 * @return communicationPreferencesData
	 */
	@Override
	public CommunicationPreferencesViewData getCommunicationPreferences()
	{
		final BasePackage packageModel = getPackageModel();
		final CommunicationPreferencesViewData communicationPreferencesViewData = new CommunicationPreferencesViewData();
		if (packageModel.getCommunicationPreference() != null)
		{
			final CommunicationPreference comPreferenceModel = packageModel.getCommunicationPreference();
			communicationPreferencesViewData.setByEmail(comPreferenceModel.isByEmail());
			communicationPreferencesViewData.setByPhone(comPreferenceModel.isByPhone());
			communicationPreferencesViewData.setByPost(comPreferenceModel.isByPost());
			communicationPreferencesViewData.setReceiveCommunication(comPreferenceModel.isReceiveCommunication());
			communicationPreferencesViewData.setThirdPartySharing(comPreferenceModel.isThirdPartySharing());
		}
		return communicationPreferencesViewData;
	}

	/**
	 * Gets the package extra facility store.
	 *
	 * @return the package extra facility store
	 */
	private PackageExtraFacilityStore getPackageExtraFacilityStore()
	{
		final PackageExtraFacilityStore packageExtraFacilityStore = (PackageExtraFacilityStore) sessionService
				.getAttribute("PackageExtraFacilityStore");
		if (packageExtraFacilityStore != null)
		{
			return packageExtraFacilityStore;
		}
		return new PackageExtraFacilityStore();
	}
}
