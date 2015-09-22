/**
 *
 */
package uk.co.tui.th.book.formbean.annotationimp;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.th.book.formbean.annotation.ValidateFormat;
import uk.co.tui.th.book.formbean.annotation.constants.PassengerDetailConstants;

/**
 * @author pradeep.as
 *Implementation class to check Not Null and regex condition.
 */
public class FormatValidator implements ConstraintValidator<ValidateFormat, String> {


    private  String  regEx;
    private  boolean emptyCheck;
    private String fieldmessCode;
    private int min;
    private int max;



    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(ValidateFormat format) {
        regEx = format.regEx();
        emptyCheck = format.emptyCheck();
        fieldmessCode = format.message();
        min=format.min();
        max=format.max();
    }

    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final String field,
            final ConstraintValidatorContext context) {
        boolean result = true;
        if (empty(field)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    createMessageCode(PassengerDetailConstants.NOT_EMPTY_CODE,fieldmessCode))
                    .addConstraintViolation();
            result = false;
        } else if (invalidContent(field)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    createMessageCode(PassengerDetailConstants.PATTERN_CODE,
                            fieldmessCode)).addConstraintViolation();
            result = false;

        }
        return result;
    }

    /**
     * @param field
     * @return
     */
    private boolean invalidContent(final String field) {
        return (StringUtils.isNotBlank(field)) && (isFieldExceedsMaxValueOrMinValue(field)
                || isFieldNotMatchesRegEx(field));
    }

    /**
     * @param field
     * @return
     */
    private boolean empty(final String field) {
        return emptyCheck && StringUtils.isBlank(field);
    }

    // SONAR MAJOR FIX- Added Method to avoid sonar boolean expression
        // complexity
    /**
     * @param field
     * @return
     */
    private boolean isFieldNotMatchesRegEx(final String field) {
        return StringUtils.isNotBlank(regEx) && !field.trim().matches(
                regEx);
    }

    // SONAR MAJOR FIX- Added Method to avoid sonar boolean expression
    // complexity
    /**
     * Checks if is field exceeds max value or min value.
     *
     * @param field the field
     * @return true, if is field exceeds max value or min value
     */
    private boolean isFieldExceedsMaxValueOrMinValue(String field) {
        return field.trim().length() > max || field
                .trim().length() < min;
    }

    /**
     * Creates the message code.
     *
     * @param errorCode the error code
     * @param fieldCode the field code
     * @return the string
     */
    public String createMessageCode(String errorCode ,String fieldCode)
    {
        return "{"+errorCode+fieldCode+"}";
    }



}
