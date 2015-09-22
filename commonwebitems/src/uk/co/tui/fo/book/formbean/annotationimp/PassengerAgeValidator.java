/**
 *
 */
package uk.co.tui.fo.book.formbean.annotationimp;

import de.hybris.platform.util.Utilities;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.travel.enums.PersonType;
import uk.co.tui.fo.book.formbean.PassengerDetailsFormBean;
import uk.co.tui.fo.book.formbean.PassengerInfoFormBean;

/**
 * @author sreevidhya.r
 *
 *         Class to validate passenger age based on flight arrival date.
 */
public final class PassengerAgeValidator
{

   private PassengerAgeValidator()
   {

   }

   /**
    * Validate the passenger age based on the flight arrival date.
    *
    * @param formBean the form bean
    * @param flightArrivalDate the flight arrival date
    */
   public static void validateAge(final PassengerDetailsFormBean formBean,
      final Date flightArrivalDate, final BindingResult result)
   {

      @SuppressWarnings("deprecation")
      final Calendar dobCalendar = Utilities.getDefaultCalendar();

      @SuppressWarnings("deprecation")
      final Calendar arrivalDateCalendar = Utilities.getDefaultCalendar();
      Date dateOfBirth = null;
      for (final PassengerInfoFormBean passengerInfoFormBean : formBean.getPaxInfoFormBean())
      {
         dateOfBirth = getDateOfBirth(passengerInfoFormBean);
         if (SyntacticSugar.isNull(dateOfBirth) || SyntacticSugar.isNull(arrivalDateCalendar))
         {
            continue;
         }
         dobCalendar.setTime(dateOfBirth);
         arrivalDateCalendar.setTime(flightArrivalDate);
      }
   }

   /**
    * Checks if is infant not yet born.
    *
    * @param passengerInfoFormBean the passenger info form bean
    * @return true, if is infant not yet born
    */
   private static boolean isInfantBorn(final PassengerInfoFormBean passengerInfoFormBean)
   {
      return !passengerInfoFormBean.isInfantNotYetBornFlag();
   }

   /**
    * Gets the date of birth.
    *
    * @param passengerInfoFormBean the passenger info form bean
    * @return the date of birth
    */
   private static Date getDateOfBirth(final PassengerInfoFormBean passengerInfoFormBean)
   {
      if (isInfantBorn(passengerInfoFormBean)
         && childOrInfant(passengerInfoFormBean.getPersonType()))
      {
         return DateUtils.toUtilDate(passengerInfoFormBean.getDay(),
            passengerInfoFormBean.getMonth(), passengerInfoFormBean.getYear());
      }
      return null;
   }

   /**
    * Child or infant.
    *
    * @param paxType the pax type
    * @return true, if successful
    */
   private static boolean childOrInfant(final String paxType)
   {
      return StringUtils.equals(PersonType.CHILD.getCode(), paxType)
         || StringUtils.equals(PersonType.INFANT.getCode(), paxType);
   }
}
