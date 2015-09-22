/**
 *
 */
package uk.co.tui.th.book.formbean.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;

import org.springframework.integration.annotation.Payload;

import uk.co.tui.th.book.formbean.annotationimp.EmailComparator;

/**
 * @author pradeep.as
 *custom annotation to compare two strings if not blank.
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailComparator.class )
@ReportAsSingleViolation
public @interface CompareEmail {

    /**
     * @return {@code String}
     */
    String resultbindingAttribute();

    /**
     * @return {@code String}
     */
    String regEx();

    /**
     * @return {@code String}
     */
    String message() default "{uk.co.tui.th.book.formbean.customvalidators.CompareEmail.message}";

    /**
     * @return {@code Class<?>[]}
     */
    Class<?>[] groups() default {};

    /**
     * @return {@code Class<? extends Payload>[]}
     */
    Class<? extends Payload>[] payload() default {};
}
