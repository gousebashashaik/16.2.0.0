/**
 *
 */
package uk.co.tui.fo.book.formbean;

import de.hybris.platform.validation.annotations.NotEmpty;

import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.tui.fo.book.formbean.annotation.ValidateDateOfBirth;
import uk.co.tui.fo.book.formbean.annotation.ValidateFormat;
import uk.co.tui.fo.book.formbean.annotation.ValidatePassengerType;
import uk.co.tui.fo.book.formbean.annotation.constants.PassengerDetailConstants;

/**
 * This holds all the form data entered by the user in the passenger details page.
 *
 * @author madhumathi.m
 *
 */
@ValidatePassengerType
@ValidateDateOfBirth(resultbindingAttribute = "year")
public class PassengerInfoFormBean
{
   /** first name max value */
   private static final int FIRST_NAME_MAX = 15;

   /** last name max value */
   private static final int LAST_NAME_MAX = 15;

   /** Customer's title. */
   private String title;

   /** Customer's gender. */
   private String gender;

   /** Customer's first name. */
   @ValidateFormat(regEx = "^[a-zA-Z- \\s]*$", max = FIRST_NAME_MAX, emptyCheck = true, message = PassengerDetailConstants.FIRSTNAME_MESS)
   private String firstName;

   /** Customer's lastName. */
   @ValidateFormat(regEx = "^[a-zA-Z- \\s]*$", max = LAST_NAME_MAX, emptyCheck = true, message = PassengerDetailConstants.SURENAME_MESS)
   private String lastName;

   /** Customer's lead passenger status. */
   private boolean leadPassenger;

   /** Customer's checkBox status. */
   private String checkBox;

   /** Customer type. */
   @NotEmpty
   private String personType;

   /** The age. */
   private String age;

   /** The day. */
   private String day;

   /** The month. */
   private String month;

   /** The year. */
   private String year;

   /**
    * Infant not yet born case flag
    */
   private boolean infantNotYetBornFlag;

   /** Is Insurance is Selected or not */
   private boolean insuranceSelected;

   /** If insurance is selected it holds the insurance PersonType */
   private String insurancePersonType;

   /**
    * @return the infantNotYetBornFlag
    */
   public boolean isInfantNotYetBornFlag()
   {
      return infantNotYetBornFlag;
   }

   /**
    * @param infantNotYetBornFlag the infantNotYetBornFlag to set
    */
   public void setInfantNotYetBornFlag(final boolean infantNotYetBornFlag)
   {
      this.infantNotYetBornFlag = infantNotYetBornFlag;
   }

   /**
    * Gets the title.
    *
    * @return the title
    */
   public String getTitle()
   {
      return this.title;
   }

   /**
    * Sets the title.
    *
    * @param title the title to set
    */
   public void setTitle(final String title)
   {
      this.title = title;
   }

   /**
    * Gets the gender.
    *
    * @return the gender
    */
   public String getGender()
   {
      return this.gender;
   }

   /**
    * Sets the gender.
    *
    * @param gender the gender to set
    */
   public void setGender(final String gender)
   {
      this.gender = gender;
   }

   /**
    * Gets the first name.
    *
    * @return the firstName
    */
   public String getFirstName()
   {
      return this.firstName;
   }

   /**
    * Sets the first name.
    *
    * @param firstName the firstName to set
    */
   public void setFirstName(final String firstName)
   {
      this.firstName = firstName;
   }

   /**
    * Gets the last name.
    *
    * @return the surName
    */
   public String getLastName()
   {
      return this.lastName;
   }

   /**
    * Sets the last name.
    *
    * @param surName the surName to set
    */
   public void setLastName(final String surName)
   {
      this.lastName = surName;
   }

   /**
    * Checks if is lead passenger.
    *
    * @return the leadPassenger
    */
   public boolean isLeadPassenger()
   {
      return this.leadPassenger;
   }

   /**
    * Sets the lead passenger.
    *
    * @param leadPassenger the leadPassenger to set
    */
   public void setLeadPassenger(final boolean leadPassenger)
   {
      this.leadPassenger = leadPassenger;
   }

   /**
    * Gets the person type.
    *
    * @return the personType
    */
   public String getPersonType()
   {
      return personType;
   }

   /**
    * Sets the person type.
    *
    * @param personType the personType to set
    */
   public void setPersonType(final String personType)
   {
      this.personType = personType;
   }

   /**
    * Return the string representation of the object.
    *
    * @return the string representation of the object.
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return ToStringBuilder.reflectionToString(this);
   }

   /**
    * Gets the age.
    *
    * @return the age
    */
   public String getAge()
   {
      return age;
   }

   /**
    * Sets the age.
    *
    * @param age the age to set
    */
   public void setAge(final String age)
   {
      this.age = age;
   }

   /**
    * @return the day
    */
   public String getDay()
   {
      return day;
   }

   /**
    * @param day the day to set
    */
   public void setDay(final String day)
   {
      this.day = day;
   }

   /**
    * @return the month
    */
   public String getMonth()
   {
      return month;
   }

   /**
    * @param month the month to set
    */
   public void setMonth(final String month)
   {
      this.month = month;
   }

   /**
    * @return the year
    */
   public String getYear()
   {
      return year;
   }

   /**
    * @param year the year to set
    */
   public void setYear(final String year)
   {
      this.year = year;
   }

   /**
    * @return the insuranceSelected
    */
   public boolean isInsuranceSelected()
   {
      return insuranceSelected;
   }

   /**
    * @param insuranceSelected the insuranceSelected to set
    */
   public void setInsuranceSelected(final boolean insuranceSelected)
   {
      this.insuranceSelected = insuranceSelected;
   }

   /**
    * @return the insurancePersonType
    */
   public String getInsurancePersonType()
   {
      return insurancePersonType;
   }

   /**
    * @param insurancePersonType the insurancePersonType to set
    */
   public void setInsurancePersonType(final String insurancePersonType)
   {
      this.insurancePersonType = insurancePersonType;
   }

   /**
    * @return the checkBox
    */
   public String getCheckBox()
   {
      return checkBox;
   }

   /**
    * @param checkBox the checkBox to set
    */
   public void setCheckBox(final String checkBox)
   {
      this.checkBox = checkBox;
   }

}
