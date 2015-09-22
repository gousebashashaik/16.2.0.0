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

import uk.co.tui.book.formbean.annotationimp.DateOfBirthValidator;

/**
 * @author pushparaja.g
 * Custom annotation to validate passenger date of birth.
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateOfBirthValidator.class )
@ReportAsSingleViolation
public @interface ValidateDateOfBirth {

    /**
     * @return {@code String}
     */
    String resultbindingAttribute();

    /**
     * @return {@code String}
     */
    String message() default "{uk.co.tui.book.formbean.customvalidators.ValidateDateOfBirth.year.message}";

    /**
     * @return {@code Class<?>[]}
     */
    Class<?>[] groups() default {};

    /**
     * @return {@code Class<? extends Payload>[]}
     */
    Class<? extends Payload>[] payload() default {};
}
