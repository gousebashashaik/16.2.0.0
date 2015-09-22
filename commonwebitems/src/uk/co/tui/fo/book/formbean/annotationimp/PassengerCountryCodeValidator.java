/**
 *
 */
package uk.co.tui.fo.book.formbean.annotationimp;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import uk.co.tui.fo.book.formbean.PassengerDetailsFormBean;

/**
 * @author sreevidhya.r
 *
 */
public class PassengerCountryCodeValidator
{

   /** The Constant FORM_NAME. */
   private static final String FORM_NAME = "passengerDetailsFormBean";

   /** The Constant TELEPHONE_MESS. */
   private static final String TELEPHONE_MESS =
      "This doesn't appear to be a valid telephone number";

   /** The Constant POSTCODE_MESS. */
   private static final String POSTCODE_MESS = "This doesn't appear to be a valid postcode";

   /** The POSTCODE_REGEX. */
   private static final String POSTCODE_REGEX = "^([A-Pa-pR-UWYZr-uwyz0-9][A-Ha-hK-Yk-y0-9]"
      + "[AEHMNPRTUVXYaehmnprtuvxy0-9]?[ABEHMNPRVWXYabehmnprvwxy0-9]?"
      + "[ \\s]{0,1}[0-9][ABD-HJLN-UW-Zabd-hjln-uw-z]{2}|GIR 0AA)$";

   /** The COUNTRYCODE_REGEX. */
   private static final String PHONE_NUMBER_REGEX =
      "^(0([4]{1}[0-3,5-9]{1}[0-9]{1}[0-9]{7}|[1-3,5-9]{1}[0-9]{1}[0-9]{8})|[1-9]{1}[0-9]{9})$";

   /** The Constant PHONE_NUMBER_REGEX_FOR_IE. */
   private static final String PHONE_NUMBER_REGEX_FOR_NUK = "^([0-9]{11})$";

   /** The PHONE_NO_MAX_LENGTH. */
   private static final int PHONE_NO_MAX_LENGTH = 10;

   /** The PHONE_NO_MAX_LENGTH_WITH_ZERO. */
   private static final int PHONE_NO_MAX_LENGTH_WITH_ZERO = 11;

   /** The POST_CODE_MAX. */
   private static final int POST_CODE_MAX_LENGTH = 8;

   /** The ZERO. */
   private static final String ZERO = "0";

   /** The Constant PHONE_NUMBER_REGEX_FOR_IE. */
   private static final String POSTCODE_REGEX_FOR_IE = "^[a-zA-Z0-9]+(?:\\s[a-zA-Z0-9]+)*$";

   /**
    * Validate post code.
    *
    * @param formBean the form bean
    * @param result the result
    */
   public static void validatePostCode(final PassengerDetailsFormBean formBean,
      final BindingResult result)
   {
      String postCodeRegx = POSTCODE_REGEX_FOR_IE;
      if (StringUtils.equalsIgnoreCase(formBean.getCountry(), "GB"))
      {
         postCodeRegx = POSTCODE_REGEX;
      }

      if (isNotValidField(formBean.getPostCode(), postCodeRegx, POST_CODE_MAX_LENGTH))
      {
         final FieldError error = new FieldError(FORM_NAME, "postCode", POSTCODE_MESS);
         result.addError(error);
      }
   }

   /**
    * Validate phone no.
    *
    * @param formBean the form bean
    * @param result the result
    */
   public static void validatePhoneNo(final PassengerDetailsFormBean formBean,
      final BindingResult result)
   {

      String phoneNoRegx = PHONE_NUMBER_REGEX_FOR_NUK;
      int phoneNoMaxLength = PHONE_NO_MAX_LENGTH;
      final String phoneNo = formBean.getTelephoneNum();
      if (StringUtils.equalsIgnoreCase(formBean.getCountry(), "GB"))
      {
         phoneNoRegx = PHONE_NUMBER_REGEX;
         if (StringUtils.startsWith(phoneNo, ZERO))
         {
            phoneNoMaxLength = PHONE_NO_MAX_LENGTH_WITH_ZERO;
         }
      }
      else
      {
         phoneNoMaxLength = PHONE_NO_MAX_LENGTH_WITH_ZERO;
      }

      if (isNotValidField(phoneNo, phoneNoRegx, phoneNoMaxLength))
      {
         final FieldError error = new FieldError(FORM_NAME, "telephoneNum", TELEPHONE_MESS);
         result.addError(error);
      }
   }

   /**
    * Validate field.
    *
    * @param field
    * @param regEx
    * @param nameMax
    */
   private static boolean isNotValidField(final String field, final String regEx, final int nameMax)
   {
      if (Pattern.matches(regEx, field) && StringUtils.length(field) <= nameMax)
      {
         return false;
      }
      return true;
   }

}
