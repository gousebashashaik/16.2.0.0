/**
 *
 */
package uk.co.tui.cr.book.formbean.annotationimp;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.enums.PersonType;
import uk.co.tui.cr.book.formbean.PassengerInfoFormBean;
import uk.co.tui.cr.book.formbean.annotation.ValidateDateOfBirth;
import uk.co.tui.cr.book.formbean.annotation.constants.PassengerDetailConstants;

/**
 * @author uday.g
 *
 */
public class DateOfBirthValidator
        implements
            ConstraintValidator<ValidateDateOfBirth, PassengerInfoFormBean> {

    private String bindingAttribute;

    @Override
    public void initialize(final ValidateDateOfBirth constraintAnnotation) {
        bindingAttribute = constraintAnnotation.resultbindingAttribute();
    }

    /*
     * validating passenger date of birth.
     *
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
     * javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final PassengerInfoFormBean passengerInfoFormBean,
            final ConstraintValidatorContext context) {
        if (!isInfantOrChild(passengerInfoFormBean)) {
            return true;
        }
        boolean result = true;
        final Date dateOfBirth = DateUtils.toUtilDate(
                passengerInfoFormBean.getDay(),
                passengerInfoFormBean.getMonth(),
                passengerInfoFormBean.getYear());
        if (dateOfBirth == null) {
            buildConstraintViolation(context);
            return false;
        }
        result = validateFutureDate(dateOfBirth, context,
                passengerInfoFormBean.isInfantNotYetBornFlag());
        return result;
    }

    /**
     * To check if the passenger is Child or Infant
     *
     * @param passengerInfoFormBean
     * @return boolean
     */
    private boolean isInfantOrChild(
            final PassengerInfoFormBean passengerInfoFormBean) {
        return StringUtils.equalsIgnoreCase(PersonType.CHILD.getCode(),
                passengerInfoFormBean.getPersonType())
                || StringUtils.equalsIgnoreCase(PersonType.INFANT.getCode(),
                        passengerInfoFormBean.getPersonType());
    }

    /**
     * To check if the passenger date is in future.
     */
    private boolean validateFutureDate(final Date dateOfBirth,
            final ConstraintValidatorContext context,
            final boolean infantNotYetBornFlag) {
        final Date currentDate = new Date();

        if (infantNotYetBornFlag) {
            return true;
        }

        if (dateOfBirth.after(currentDate)) {
            buildConstraintViolation(context);
            return false;
        }
        return true;
    }

    /**
     * To set an error message to the binding attribute.
     *
     * @param context
     */
    private void buildConstraintViolation(
            final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                PassengerDetailConstants.INVALID_DOB_CODE)
                .addNode(bindingAttribute).addConstraintViolation();

    }

}
