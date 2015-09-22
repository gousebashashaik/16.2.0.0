/**
 *
 */
package uk.co.tui.th.book.view.data;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class contains Booking relevant data required for UI.
 *
 * @author thyagaraju.e
 *
 */
public class BookingDetailsViewData
{

   /**
    * The title.
    */
   private String title;

   /**
    * Passenger Name.
    */
   private String passengerName;

   /**
    * The email id.
    */
   private String emailId;

   /**
    * Represents Booking Reference Number.
    */
   private String bookingReference;

   /**
    * Represents number of days left to be in the destination.
    */
   private int noOfDaysLeft;

   /**
    * The currency appended total cost.
    */
   private String currencyAppendedTotalCost;

   /**
    * The currency appended total paid.
    */
   private String currencyAppendedTotalPaid;

   /**
    * Site URL
    */
   private String siteUrl;

   /**
    * List of Deposits
    */
   private DepositData depositsDetails;

   /**
    * Payment Options URL
    */
   private String paymentOptionsUrl;

   /**
    * My Account URL
    */
   private String myAccountUrl;

   private String eticketEmailDate;

   /**
    * Destination guide first destination name.
    */
   private String destOneName;

   /**
    * Destination guide second destination name.
    */
   private String destTwoName;

   /**
    * Destination guide first destination url.
    */
   private String destOneUrl;

   /**
    * Destination guide second destination url.
    */
   private String destTwoUrl;

   /** To set the message if fulfillment fails. */
   private String bookingFailureMessage = StringUtils.EMPTY;

   /** CreditCardCharge in percentage */
   private String percentageCreditCardCharge;

   /**
    * Flag to indicate the credit card payment
    */
   private boolean creditCardPayment;

   /**
    * multicom booking reference number.
    */
   private String multicomBookingReferenceNumber;

   /**
    * Gets the title.
    *
    * @return the title
    */
   public String getTitle()
   {
      return title;
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
    * Gets the passenger name.
    *
    * @return the passengerName
    */
   public String getPassengerName()
   {
      return passengerName;
   }

   /**
    * Sets the passenger name.
    *
    * @param passengerName the passengerName to set
    */
   public void setPassengerName(final String passengerName)
   {
      this.passengerName = passengerName;
   }

   /**
    * Gets the email id.
    *
    * @return the emailId
    */
   public String getEmailId()
   {
      return emailId;
   }

   /**
    * Sets the email id.
    *
    * @param emailId the emailId to set
    */
   public void setEmailId(final String emailId)
   {
      this.emailId = emailId;
   }

   /**
    * Gets the booking reference.
    *
    * @return the bookingReference
    */
   public String getBookingReference()
   {
      return bookingReference;
   }

   /**
    * Sets the booking reference.
    *
    * @param bookingReference the bookingReference to set
    */
   public void setBookingReference(final String bookingReference)
   {
      this.bookingReference = bookingReference;
   }

   /**
    * Gets the no of days left.
    *
    * @return the noOfDaysLeft
    */
   public int getNoOfDaysLeft()
   {
      return noOfDaysLeft;
   }

   /**
    * Gets the currency appended total cost
    *
    * @return the currencyAppendedTotalCost
    */
   public String getCurrencyAppendedTotalCost()
   {
      return currencyAppendedTotalCost;
   }

   /**
    * Sets the currency appended total cost.
    *
    * @param currencyAppendedTotalCost the currencyAppendedTotalCost to set
    */
   public void setCurrencyAppendedTotalCost(final String currencyAppendedTotalCost)
   {
      this.currencyAppendedTotalCost = currencyAppendedTotalCost;
   }

   /**
    * Sets the no of days left.
    *
    * @param noOfDaysLeft the noOfDaysLeft to set
    */
   public void setNoOfDaysLeft(final int noOfDaysLeft)
   {
      this.noOfDaysLeft = noOfDaysLeft;
   }

   /**
    * Gets the currency appended total paid.
    *
    * @return the currencyAppendedtotalPaid
    */
   public String getCurrencyAppendedTotalPaid()
   {
      return currencyAppendedTotalPaid;
   }

   /**
    * Sets the currency appended total paid.
    *
    * @param currencyAppendedTotalPaid the new currency appended total paid
    */
   public void setCurrencyAppendedTotalPaid(final String currencyAppendedTotalPaid)
   {
      this.currencyAppendedTotalPaid = currencyAppendedTotalPaid;
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
    * @return the siteUrl
    */
   public String getSiteUrl()
   {
      return siteUrl;
   }

   /**
    * @param siteUrl the siteUrl to set
    */
   public void setSiteUrl(final String siteUrl)
   {
      this.siteUrl = siteUrl;
   }

   /**
    * @return the depositsDetails
    */
   public DepositData getDepositsDetails()
   {
      return depositsDetails;
   }

   /**
    * @param depositsDetails the depositsDetails to set
    */
   public void setDepositsDetails(final DepositData depositsDetails)
   {
      this.depositsDetails = depositsDetails;
   }

   /**
    * @return the paymentOptionsUrl
    */
   public String getPaymentOptionsUrl()
   {
      return paymentOptionsUrl;
   }

   /**
    * @param paymentOptionsUrl the paymentOptionsUrl to set
    */
   public void setPaymentOptionsUrl(final String paymentOptionsUrl)
   {
      this.paymentOptionsUrl = paymentOptionsUrl;
   }

   /**
    * @return the eticketEmailDate
    */
   public String getEticketEmailDate()
   {
      return eticketEmailDate;
   }

   /**
    * @param eticketEmailDate the eticketEmailDate to set
    */
   public void setEticketEmailDate(final String eticketEmailDate)
   {
      this.eticketEmailDate = eticketEmailDate;
   }

   /**
    * @return the destOneName
    */
   public String getDestOneName()
   {
      return destOneName;
   }

   /**
    * @param destOneName the destOneName to set
    */
   public void setDestOneName(final String destOneName)
   {
      this.destOneName = destOneName;
   }

   /**
    * @return the destTwoName
    */
   public String getDestTwoName()
   {
      return destTwoName;
   }

   /**
    * @param destTwoName the destTwoName to set
    */
   public void setDestTwoName(final String destTwoName)
   {
      this.destTwoName = destTwoName;
   }

   /**
    * @return the destOneUrl
    */
   public String getDestOneUrl()
   {
      return destOneUrl;
   }

   /**
    * @param destOneUrl the destOneUrl to set
    */
   public void setDestOneUrl(final String destOneUrl)
   {
      this.destOneUrl = destOneUrl;
   }

   /**
    * @return the destTwoUrl
    */
   public String getDestTwoUrl()
   {
      return destTwoUrl;
   }

   /**
    * @param destTwoUrl the destTwoUrl to set
    */
   public void setDestTwoUrl(final String destTwoUrl)
   {
      this.destTwoUrl = destTwoUrl;
   }

   /**
    * @return the bookingFailureMessage
    */
   public String getBookingFailureMessage()
   {
      return bookingFailureMessage;
   }

   /**
    * @param bookingFailureMessage the bookingFailureMessage to set
    */
   public void setBookingFailureMessage(final String bookingFailureMessage)
   {
      this.bookingFailureMessage = bookingFailureMessage;
   }

   /**
    * @return the myAccountUrl
    */
   public String getMyAccountUrl()
   {
      return myAccountUrl;
   }

   /**
    * @param myAccountUrl the myAccountUrl to set
    */
   public void setMyAccountUrl(final String myAccountUrl)
   {
      this.myAccountUrl = myAccountUrl;
   }

   /**
    * @return the percentageCreditCardCharge
    */
   public String getPercentageCreditCardCharge()
   {
      return percentageCreditCardCharge;
   }

   /**
    * @param percentageCreditCardCharge the percentageCreditCardCharge to set
    */
   public void setPercentageCreditCardCharge(final String percentageCreditCardCharge)
   {
      this.percentageCreditCardCharge = percentageCreditCardCharge;
   }

   /**
    * @return the creditCardPayment
    */
   public boolean isCreditCardPayment()
   {
      return creditCardPayment;
   }

   /**
    * @param creditCardPayment the creditCardPayment to set
    */
   public void setCreditCardPayment(final boolean creditCardPayment)
   {
      this.creditCardPayment = creditCardPayment;
   }

   /**
    * @return the multicomBookingReferenceNumber
    */
   public String getMulticomBookingReferenceNumber()
   {
      return multicomBookingReferenceNumber;
   }

   /**
    * @param multicomBookingReferenceNumber the multicomBookingReferenceNumber to set
    */
   public void setMulticomBookingReferenceNumber(final String multicomBookingReferenceNumber)
   {
      this.multicomBookingReferenceNumber = multicomBookingReferenceNumber;
   }

}
