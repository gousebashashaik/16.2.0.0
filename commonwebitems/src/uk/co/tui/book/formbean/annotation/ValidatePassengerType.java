/**
 *
 */
package uk.co.tui.book.formbean.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;

import org.springframework.integration.annotation.Payload;

import uk.co.tui.book.formbean.annotationimp.PassengerTypeValidator;

/**
 * @author pradeep.as
 * custom annotation to validate passenger title and gender.
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassengerTypeValidator.class )
@ReportAsSingleViolation
public @interface ValidatePassengerType {

    /**
     * Validates input and populates message.
     *
     * @return the string message
     */
    String message() default "{uk.co.tui.book.formbean.customvalidators.ValidatePassengerType.message}";

    /**
     * Group of passenger type.
     *
     * @return the class[]
     */
    Class<?>[] groups() default {};

    /**
     * The Payload.
     *
     * @return the class<? extends payload>[]
     */
    Class<? extends Payload>[] payload() default {};
}
