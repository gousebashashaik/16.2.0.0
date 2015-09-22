/**
 *
 */
package uk.co.tui.fo.book.formbean;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.tui.fo.book.formbean.annotation.CompareEmail;
import uk.co.tui.fo.book.formbean.annotation.ValidateFormat;
import uk.co.tui.fo.book.formbean.annotation.constants.PassengerDetailConstants;

/**
 * This class holds all the form data entered by the user in the passenger details page.
 *
 * @author madhumathi.m
 *
 */
@CompareEmail(regEx = "(^([a-z0-9_\\.\\-])+\\@(([a-z0-9\\-])+\\.)+([a-z0-9]{2,4})+$)", resultbindingAttribute = "confirmationEmail")
public final class PassengerDetailsFormBean
{

   private static final int ADDRESS1_MAX = 25;

   private static final int ADDRESS2_MAX = 25;

   private static final int TOWN_MAX = 25;

   private static final int COUNTY_MAX = 16;

   private static final int HOUSE_NO = 20;

   /** Customer's address1. */
   @ValidateFormat(regEx = "([a-zA-Z0-9]+[-,&\\/\\s*]*[a-zA-Z0-9 \\s*]*)+", max = ADDRESS1_MAX, emptyCheck = true, message = PassengerDetailConstants.ADDRESS_MESS)
   private String address1;

   /** Customer's address2. */
   @ValidateFormat(regEx = "([a-zA-Z0-9]+[-,&\\/\\s*]*[a-zA-Z0-9 \\s*]*)+", max = ADDRESS2_MAX, emptyCheck = false, message = PassengerDetailConstants.ADDRESS_MESS)
   private String address2;

   /** Customer's housenumber. */
   @ValidateFormat(regEx = "([a-zA-Z0-9]+[-,&\\/\\s*]*[a-zA-Z0-9 \\s*]*)+", max = HOUSE_NO, emptyCheck = true, message = PassengerDetailConstants.ADDRESS_MESS)
   private String houseNum;

   /** Customer's town/city. */
   @ValidateFormat(regEx = "[a-zA-Z\\'\\., \\-\\s*]+", max = TOWN_MAX, emptyCheck = true, message = PassengerDetailConstants.TOWN_MESS)
   private String town;

   /** Customer's county. */
   @ValidateFormat(regEx = "[a-zA-Z0-9\\'\\., \\-\\s*]+", max = COUNTY_MAX, emptyCheck = true, message = PassengerDetailConstants.COUNTY_MESS)
   private String county;

   /** The country. */
   private String country;

   /** Customer's postCode. */
   // @ValidateFormat(regEx =
   // "^([A-Pa-pR-UWYZr-uwyz0-9][A-Ha-hK-Yk-y0-9][AEHMNPRTUVXYaehmnprtuvxy0-9]?[ABEHMNPRVWXYabehmnprvwxy0-9]?[ \\s]{0,1}[0-9][ABD-HJLN-UW-Zabd-hjln-uw-z]{2}|GIR 0AA)$",
   // max = POST_CODE_MAX, emptyCheck = false, message =
   // PassengerDetailConstants.POSTCODE_MESS)
   private String postCode;

   /** Customer's telephone number. */
   // @ValidateFormat(regEx =
   // "^(0([1-9]{1}[0-4,6-9]{1}[0-9]{1}[0-9]{3,7})|0([1-9]{1}[5]{1}[0-2,4-9]{1}[0-9]{3,7})|[1-9]{1}[0-4,6-9]{1}[0-9]{1}[0-9]{3,7}|[1-9]{1}[5]{1}[0-2,4-9]{1}[0-9]{3,7})$",
   // emptyCheck = true, max = TELEPHONE_NO_MAX, message =
   // PassengerDetailConstants.TELEPHONE_MESS)
   private String telephoneNum;

   /** Customer's email. */
   @ValidateFormat(regEx = "(^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$)", emptyCheck = true, message = PassengerDetailConstants.EMAIL_MESS)
   private String email;

   /** Customer's confirmation email. */
   @ValidateFormat(emptyCheck = true, message = PassengerDetailConstants.CONFIRM_EMAIL_MESS)
   private String confirmationEmail;

   /** Boolean to hold important information check status. */
   private boolean importantInformationChecked;

   /**
    * Collection holding passenger details.
    */
   @Valid
   private List<PassengerInfoFormBean> paxInfoFormBean = new ArrayList<PassengerInfoFormBean>();

   /**
    * @return the address1
    */
   public String getAddress1()
   {
      return this.address1;
   }

   /**
    * @param address1 the address1 to set
    */
   public void setAddress1(final String address1)
   {
      this.address1 = address1;
   }

   /**
    * @return the address2
    */
   public String getAddress2()
   {
      return this.address2;
   }

   /**
    * @param address2 the address2 to set
    */
   public void setAddress2(final String address2)
   {
      this.address2 = address2;
   }

   /**
    * @return the county
    */
   public String getCounty()
   {
      return this.county;
   }

   /**
    * @param county the county to set
    */
   public void setCounty(final String county)
   {
      this.county = county;
   }

   /**
    * @return the country
    */
   public String getCountry()
   {
      return country;
   }

   /**
    * @param country the country to set
    */
   public void setCountry(final String country)
   {
      this.country = country;
   }

   /**
    * @return the postCode
    */

   public String getPostCode()
   {
      return this.postCode;
   }

   /**
    * @param postCode the postCode to set
    */

   public void setPostCode(final String postCode)
   {
      this.postCode = postCode;
   }

   /**
    * @return the telephoneNum
    */
   public String getTelephoneNum()
   {
      return this.telephoneNum;
   }

   /**
    * @param telephoneNum the telephoneNum to set
    */
   public void setTelephoneNum(final String telephoneNum)
   {
      this.telephoneNum = telephoneNum;
   }

   /**
    * @return the email
    */
   public String getEmail()
   {
      return this.email;
   }

   /**
    * @param email the email to set
    */
   public void setEmail(final String email)
   {
      this.email = email;
   }

   /**
    * @return the confirmationEmail
    */
   public String getConfirmationEmail()
   {
      return this.confirmationEmail;
   }

   /**
    * @param confirmationEmail the confirmationEmail to set
    */
   public void setConfirmationEmail(final String confirmationEmail)
   {
      this.confirmationEmail = confirmationEmail;
   }

   /**
    * @return the houseNum
    */
   public String getHouseNum()
   {
      return houseNum;
   }

   /**
    * @param houseNum the houseNum to set
    */
   public void setHouseNum(final String houseNum)
   {
      this.houseNum = houseNum;
   }

   /**
    * @return the importantInformationChecked
    */
   public boolean isImportantInformationChecked()
   {
      return this.importantInformationChecked;
   }

   /**
    * @param importantInformationChecked the importantInformationChecked to set
    */
   public void setImportantInformationChecked(final boolean importantInformationChecked)
   {
      this.importantInformationChecked = importantInformationChecked;
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
    * Gets the pax info form bean.
    *
    * @return the paxInfoFormBean
    */
   public List<PassengerInfoFormBean> getPaxInfoFormBean()
   {
      return paxInfoFormBean;
   }

   /**
    * Sets the pax info form bean.
    *
    * @param paxInfoFormBean the paxInfoFormBean to set
    */
   public void setPaxInfoFormBean(final List<PassengerInfoFormBean> paxInfoFormBean)
   {
      this.paxInfoFormBean = paxInfoFormBean;
   }

   /**
    * @return the town
    */
   public String getTown()
   {
      return this.town;
   }

   /**
    * @param town the town to set
    */
   public void setTown(final String town)
   {
      this.town = town;
   }

}
