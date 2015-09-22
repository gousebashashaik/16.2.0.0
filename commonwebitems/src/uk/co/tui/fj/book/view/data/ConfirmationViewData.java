/**
 *
 */
package uk.co.tui.fj.book.view.data;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class holds all the data required for the population of the view data in the Confirmation
 * page.
 *
 * @author thyagaraju.e
 */
public class ConfirmationViewData
{
   /**
    * Holds the Package Relevant View Data.
    */
   private PackageViewData packageViewData;

   /**
    * Holds the data specific to summary panel.
    */
   private SummaryPanelViewData summaryViewData;

   /**
    * Holds Booking Details.
    */
   private BookingDetailsViewData bookingDetailsViewData;

   /** The confirmation static content view data. */
   private ConfirmationStaticContentViewData confirmationStaticContentViewData;

   /** The confirmation content view data. */
   private ConfirmationContentViewData confirmationContentViewData;

   /** The flag to identify mixed Third Party Flight. */
   private boolean mixedThirdPartyFlight;

   /** The package type. */
   private String packageType;

   /**
    * @return the packageType
    */
   public String getPackageType()
   {
      return packageType;
   }

   /**
    * @param packageType the packageType to set
    */
   public void setPackageType(final String packageType)
   {
      this.packageType = packageType;
   }

   /**
    * @return the packageViewData
    */
   public PackageViewData getPackageViewData()
   {
      return packageViewData;
   }

   /**
    * @param packageViewData the packageViewData to set
    */
   public void setPackageViewData(final PackageViewData packageViewData)
   {
      this.packageViewData = packageViewData;
   }

   /**
    * @return the summaryViewData
    */
   public SummaryPanelViewData getSummaryViewData()
   {
      if (this.summaryViewData == null)
      {
         this.summaryViewData = new SummaryPanelViewData();
      }
      return this.summaryViewData;
   }

   /**
    * @param summaryViewData the summaryViewData to set
    */
   public void setSummaryViewData(final SummaryPanelViewData summaryViewData)
   {
      this.summaryViewData = summaryViewData;
   }

   /**
    * @return the bookingDetailsViewData
    */
   public BookingDetailsViewData getBookingDetailsViewData()
   {
      return bookingDetailsViewData;
   }

   /**
    * @param bookingDetailsViewData the bookingDetailsViewData to set
    */
   public void setBookingDetailsViewData(final BookingDetailsViewData bookingDetailsViewData)
   {
      this.bookingDetailsViewData = bookingDetailsViewData;
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
    * @return the confirmationStaticContentViewData
    */
   public ConfirmationStaticContentViewData getConfirmationStaticContentViewData()
   {
      return confirmationStaticContentViewData;
   }

   /**
    * @param confirmationStaticContentViewData the confirmationStaticContentViewData to set
    */
   public void setConfirmationStaticContentViewData(
      final ConfirmationStaticContentViewData confirmationStaticContentViewData)
   {
      this.confirmationStaticContentViewData = confirmationStaticContentViewData;
   }

   /**
    * @return the confirmationContentViewData
    */
   public ConfirmationContentViewData getConfirmationContentViewData()
   {
      return confirmationContentViewData;
   }

   /**
    * @param confirmationContentViewData the confirmationContentViewData to set
    */
   public void setConfirmationContentViewData(
      final ConfirmationContentViewData confirmationContentViewData)
   {
      this.confirmationContentViewData = confirmationContentViewData;
   }

   /**
    * @return the mixedThirdPartyFlight
    */
   public boolean isMixedThirdPartyFlight()
   {
      return mixedThirdPartyFlight;
   }

   /**
    * @param mixedThirdPartyFlight the mixedThirdPartyFlight to set
    */
   public void setMixedThirdPartyFlight(final boolean mixedThirdPartyFlight)
   {
      this.mixedThirdPartyFlight = mixedThirdPartyFlight;
   }

}
