/**
 *
 */
package uk.co.tui.fo.book.formbean.annotationimp;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.fo.book.formbean.PassengerDetailsFormBean;
import uk.co.tui.fo.book.formbean.annotation.CompareEmail;

/**
 * The Class StringComparator.
 *
 * @author pradeep.as
 */
public class EmailComparator implements ConstraintValidator<CompareEmail, PassengerDetailsFormBean>
{

   private String regEx;

   private String bindingAttribute;

   /*
    * (non-Javadoc)
    * 
    * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
    */
   @Override
   public void initialize(final CompareEmail constraintAnnotation)
   {

      regEx = constraintAnnotation.regEx();
      bindingAttribute = constraintAnnotation.resultbindingAttribute();
   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
    * javax.validation.ConstraintValidatorContext)
    */
   @Override
   public boolean isValid(final PassengerDetailsFormBean passengerForm,
      final ConstraintValidatorContext context)
   {
      boolean result = true;
      final String email = passengerForm.getEmail();
      final String confirmationEmail = passengerForm.getConfirmationEmail();

      if (isValidEmail(email) && isValidEmail(confirmationEmail)
         && StringUtil.isNotEquals(email, confirmationEmail))
      {
         context
            .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
            .addNode(bindingAttribute).addConstraintViolation().disableDefaultConstraintViolation();
         result = false;
      }

      return result;
   }

   private boolean isValidEmail(final String email)
   {
      return StringUtils.isNotEmpty(email) && email.matches(regEx);
   }

}
