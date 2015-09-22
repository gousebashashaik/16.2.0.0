/**
 *
 */
package uk.co.tui.th.book.formbean.annotationimp;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.th.book.formbean.PassengerInfoFormBean;
import uk.co.tui.th.book.formbean.annotation.ValidatePassengerType;
import uk.co.tui.th.book.formbean.annotation.constants.PassengerDetailConstants;

/**
 * @author pradeep.as
 *
 *class to validate passenger title and gender.
 */
public class PassengerTypeValidator implements ConstraintValidator<ValidatePassengerType, PassengerInfoFormBean>  {


    /** The male title list. */
    private List<String> maleTitleList = Arrays.asList("MR","MSTR","DR","REV","PROF");

    /** The female title list. */
    private List<String> femaleTitleList = Arrays.asList("MRS","MISS","MS","DR","REV","PROF");

    /** The title list. */
    private List<String>  titleList = Arrays.asList("MR","MSTR","MRS","MISS","MS","DR","REV","PROF");

    private List<String> genderList = Arrays.asList("MALE","FEMALE");
    /* initialization method for future use.
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(ValidatePassengerType validatePaxType) {
        //future use
    }

    /* validating pax title and pax gender.
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final PassengerInfoFormBean passengerDetails,
            final ConstraintValidatorContext context) {

        boolean result = true;
        String title = passengerDetails.getTitle();
        String gender = passengerDetails.getGender();
        if (!titleList.contains(title)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    PassengerDetailConstants.TITLE_MESS_CODE).addNode("title")
                    .addConstraintViolation();
            result = false;
        }else if (!genderList.contains(gender))
            {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    PassengerDetailConstants.GENDER_MESS_CODE).addNode("gender")
                    .addConstraintViolation();
            result = false;
            }else if (checkGenderCondition(title, gender)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    PassengerDetailConstants.WRONG_GENDER_MESS_CODE).addNode("gender")
                    .addConstraintViolation();
            result = false;
        }
        return result;
    }

    /**
     * @param title
     * @param gender
     * @return gender condition
     */
    public boolean checkGenderCondition(String title, String gender) {
        return !((StringUtils.equalsIgnoreCase(gender, "MALE") && maleTitleList
                .contains(title)) || (StringUtils.equalsIgnoreCase(gender,
                "FEMALE") && femaleTitleList.contains(title)));
    }

}
