/**
 *
 */
package uk.co.tui.book.formbean.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;

import org.springframework.integration.annotation.Payload;

import uk.co.tui.book.formbean.annotationimp.FormatValidator;

/**
 * @author pradeep.as Custom annotation to format Formbean Fields.
 *
 */
@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FormatValidator.class)
@ReportAsSingleViolation
public @interface ValidateFormat
{

   /** Max 60} */
   public static final int MAX = 60;

   /**
    * @return The {@code int}, default 0}
    */
   int min() default 0;

   /**
    * @return The {@code int}, default 60}
    */
   int max() default MAX;

   /**
    * @return The {@code String}, default .*}
    */
   String regEx() default ".*";

   /**
    * @return The {@code boolean}, default false}
    */
   boolean emptyCheck() default false;

   /**
    * @return String The {@code String}
    */
   String message() default "{uk.co.tui.book.formbean.customvalidators.ValidateFormat.message}";

   /**
    * @return {@code Class<?>[]}
    */
   Class<?>[] groups() default {};

   /**
    * @return {@code Class<? extends Payload>[]}
    */
   Class<? extends Payload>[] payload() default {};

}
