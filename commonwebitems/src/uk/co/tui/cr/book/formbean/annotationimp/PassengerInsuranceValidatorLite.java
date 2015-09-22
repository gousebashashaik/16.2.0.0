/**
 *
 */
package uk.co.tui.cr.book.formbean.annotationimp;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.InsuranceExtraFacility;
import uk.co.tui.book.domain.lite.InsuranceType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.cr.book.formbean.PassengerDetailsFormBean;
import uk.co.tui.cr.book.formbean.PassengerInfoFormBean;


/**
 * @author srikanth.bs
 *
 *         Class to validate passenger age based on flight arrival date.
 */

/**
 * SONAR FIX-Utility classes should not have a public or default constructor and class should be final.
 */
public final class PassengerInsuranceValidatorLite
{

    private static final String ERROR_MESSAGE = "Insurances are not valid.";

    private static final String FORM_NAME = "passengerDetailsFormBean";

    private static final String FIELD = "";

    private static final int ZERO = 0;

    private static final String INSURANCE = "INSURANCE";


    /**
     * SONAR FIX-Utility classes should not have a public or default constructor.
     */
    private PassengerInsuranceValidatorLite()
    {

    }

    /**
     * @param formBean
     * @param packageModel
     * @param result
     */
    public static void validateInsurance(final PassengerDetailsFormBean formBean, final BasePackage packageModel,
            final BindingResult result)
    {
        final List<Passenger> passengersList = packageModel.getPassengers();

        final Passenger passenger = passengersList.get(0);
        final boolean isFamilyInsurance = isFamilyINSPresent(passenger);

        if (!isFamilyInsurance)
        {
            validateIndividualINS(passengersList, formBean, result);
        }
    }

    /**
     * Validate individual ins.
     *
     * @param passengersList
     *           the passengers list
     * @param formBean
     *           the form bean
     * @param result
     *           the result
     */
    private static void validateIndividualINS(final List<Passenger> passengersList, final PassengerDetailsFormBean formBean,
            final BindingResult result)
    {

        int formBeanChildInsSelected = ZERO;
        int formBeanAdultInsSelected = ZERO;
        int formBeanSeniorInsSelected = ZERO;
        int formBeanSuperSeniorInsSelected = ZERO;

        for (final PassengerInfoFormBean passengerInfo : formBean.getPaxInfoFormBean())
        {
            formBeanChildInsSelected = getApplicableChildINSCount(passengerInfo, formBeanChildInsSelected);
            formBeanAdultInsSelected = getApplicableAdultINSCount(passengerInfo, formBeanAdultInsSelected);
            formBeanSeniorInsSelected = getApplicableSeniorINSCount(passengerInfo, formBeanSeniorInsSelected);
            formBeanSuperSeniorInsSelected = getApplicableSuperSeniorINSCount(passengerInfo, formBeanSuperSeniorInsSelected);
        }
        if (!isInsuranceValid(passengersList, formBeanChildInsSelected, formBeanAdultInsSelected, formBeanSeniorInsSelected,
                formBeanSuperSeniorInsSelected))
        {
            final FieldError error = new FieldError(FORM_NAME, FIELD, ERROR_MESSAGE);
            result.addError(error);
        }
    }

    private static boolean isInsuranceValid(final List<Passenger> passengersList, final int formBeanChildInsSelected,
            final int formBeanAdultInsSelected, final int formBeanSeniorInsSelected, final int formBeanSuperSeniorInsSelected)
    {
        final int packageChildInsSelected = getInsuranceSelectedCount(passengersList, PersonType.CHILD);
        final int packageAdultInsSelected = getInsuranceSelectedCount(passengersList, PersonType.ADULT);
        final int packageSeniorInsSelected = getInsuranceSelectedCount(passengersList, PersonType.SENIOR);
        final int packageSuperSeniorInsSelected = getInsuranceSelectedCount(passengersList, PersonType.SUPERSENIOR);
        return packageChildInsSelected == formBeanChildInsSelected && packageAdultInsSelected == formBeanAdultInsSelected
                && packageSeniorInsSelected == formBeanSeniorInsSelected
                && packageSuperSeniorInsSelected == formBeanSuperSeniorInsSelected;
    }

    /**
     * Checks if is family ins present.
     *
     * @param passenger
     *           the passenger
     * @return true, if is family ins present
     */
    private static boolean isFamilyINSPresent(final Passenger passenger)
    {
        for (final ExtraFacility extraFacility : passenger.getExtraFacilities())
        {
            if ((extraFacility.getExtraFacilityGroup().toString()).equals(INSURANCE))
            {
                final InsuranceExtraFacility insuranceExtraFacilityModel = (InsuranceExtraFacility) extraFacility;
                if (isFamilyInsurance(insuranceExtraFacilityModel))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isFamilyInsurance(final InsuranceExtraFacility insuranceExtraFacilityModel)
    {
        return insuranceExtraFacilityModel.getInsuranceType() == InsuranceType.FAMILY;
    }

    /**
     * Gets the applicable child ins count.
     *
     * @param passengerInfo
     *           the passenger info
     * @param formBeanChildInsSelected
     *           the form bean child ins selected
     * @return the applicable child ins count
     */
    private static int getApplicableChildINSCount(final PassengerInfoFormBean passengerInfo, final int formBeanChildInsSelected)
    {
        if (("CHILD").equals(passengerInfo.getInsurancePersonType()))
        {
            return formBeanChildInsSelected + 1;
        }
        return formBeanChildInsSelected;
    }

    /**
     * Gets the applicable adult ins count.
     *
     * @param passengerInfo
     *           the passenger info
     * @param formBeanAdultInsSelected
     *           the form bean adult ins selected
     * @return the applicable adult ins count
     */
    private static int getApplicableAdultINSCount(final PassengerInfoFormBean passengerInfo, final int formBeanAdultInsSelected)
    {
        if (("ADULT").equals(passengerInfo.getInsurancePersonType()))
        {
            return formBeanAdultInsSelected + 1;
        }
        return formBeanAdultInsSelected;
    }

    /**
     * Gets the applicable senior ins count.
     *
     * @param passengerInfo
     *           the passenger info
     * @param formBeanSeniorInsSelected
     *           the form bean senior ins selected
     * @return the applicable senior ins count
     */
    private static int getApplicableSeniorINSCount(final PassengerInfoFormBean passengerInfo, final int formBeanSeniorInsSelected)
    {
        if (("SENIOR").equals(passengerInfo.getInsurancePersonType()))
        {
            return formBeanSeniorInsSelected + 1;
        }
        return formBeanSeniorInsSelected;
    }

    /**
     * Gets the applicable super senior ins count.
     *
     * @param passengerInfo
     *           the passenger info
     * @param formBeanSuperSeniorInsSelected
     *           the form bean super senior ins selected
     * @return the applicable super senior ins count
     */
    private static int getApplicableSuperSeniorINSCount(final PassengerInfoFormBean passengerInfo,
            final int formBeanSuperSeniorInsSelected)
    {
        if (("SUPERSENIOR").equals(passengerInfo.getInsurancePersonType()))
        {
            return formBeanSuperSeniorInsSelected + 1;
        }
        return formBeanSuperSeniorInsSelected;
    }

    /**
     * @param passengersList
     * @param personType
     * @return count
     */
    private static int getInsuranceSelectedCount(final List<Passenger> passengersList, final PersonType personType)
    {
        int count = ZERO;
        for (final Passenger passenger : passengersList)
        {
            if (passenger.getType().equals(personType) && isPassengerHasInsurance(passenger))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if is passenger has an insurance.
     *
     * @param passenger
     *           the passenger
     * @return true, if is passenger has an insurance
     */
    private static boolean isPassengerHasInsurance(final Passenger passenger)
    {
        for (final ExtraFacility extraFacility : passenger.getExtraFacilities())
        {
            if (extraFacility.getExtraFacilityGroup().toString().equals(INSURANCE))
            {
                return true;
            }
        }
        return false;
    }
}
