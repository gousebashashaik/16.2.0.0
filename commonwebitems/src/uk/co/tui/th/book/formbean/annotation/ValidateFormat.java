/**
 *
 */
package uk.co.tui.th.book.formbean.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;

import org.springframework.integration.annotation.Payload;

import uk.co.tui.th.book.constants.BookFlowConstants;
import uk.co.tui.th.book.formbean.annotationimp.FormatValidator;

/**
 * @author pradeep.as
 *Custom annotation to format Formbean Fields.
 *
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FormatValidator.class )
@ReportAsSingleViolation
public @interface ValidateFormat {

   /** Max 60} */
   int MAX = BookFlowConstants.SIXTY;

    /**
     * @return The {@code int}, default {@value 0}
     */
    int min() default 0;

    /**
     * @return The {@code int}, default {@value 60}
     */
    int max() default MAX;

    /**
     * @return The {@code String}, default {@value .*}
     */
    String regEx() default ".*";

    /**
     * @return The {@code boolean}, default {@value false}
     */
    boolean emptyCheck()default false;

    /**
     * @return The {@code String}
     */
    String message() default "{uk.co.tui.th.book.formbean.customvalidators.ValidateFormat.message}";

    /**
     * @return {@code Class<?>[]}
     */
    Class<?>[] groups() default {};

    /**
     * @return {@code Class<? extends Payload>[]}
     */
    Class<? extends Payload>[] payload() default {};

}
