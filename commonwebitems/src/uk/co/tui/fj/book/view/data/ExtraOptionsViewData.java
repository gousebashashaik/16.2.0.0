/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.tui.book.page.response.PageResponse;

/**
 * The Class ExtraOptionsViewData.
 *
 * @author munisekhar.k
 */
public class ExtraOptionsViewData
{

   /**
    * Holds the data specific to summary panel.
    */
   private SummaryPanelViewData summaryViewData;

   /**
    * Holds the Package Relevant View Data.
    */
   private PackageViewData packageViewData;

   /**
    * Container for the all the extra facility.
    */
   private ExtraFacilityViewDataContainer extraFacilityViewDataContainer;

   /** The insurance container view data. */
   private InsuranceContainerViewData insuranceContainerViewData;

   /** The session time interval. */
   private int sessionTimePeriod;

   /** The extra options static content view data. */
   private ExtraOptionsStaticContentViewData extraOptionsStaticContentViewData;

   /** The extra options content view data. */
   private ExtraOptionsContentViewData extraOptionsContentViewData;

   /** The RoomOptionsContentViewData. */
   private boolean villaAcommodation;

   /** The alert messages. */
   private List<AlertViewData> alertMessages;

   /** The Alamo car hire. */
   private boolean alamoCarHire;

   /** The package type. */
   private String packageType;

   private PageResponse pageResponse;

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
    * Gets the package view data.
    *
    * @return the packageViewData
    */
   public PackageViewData getPackageViewData()
   {
      if (this.packageViewData == null)
      {
         this.packageViewData = new PackageViewData();
      }
      return this.packageViewData;
   }

   /**
    * Sets the package view data.
    *
    * @param packageViewData the packageViewData to set
    */
   public void setPackageViewData(final PackageViewData packageViewData)
   {
      this.packageViewData = packageViewData;
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
    * Gets the summary view data.
    *
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
    * Sets the summary view data.
    *
    * @param summaryViewData the summaryViewData to set
    */
   public void setSummaryViewData(final SummaryPanelViewData summaryViewData)
   {
      this.summaryViewData = summaryViewData;
   }

   /**
    * Gets the extra facility view data container.
    *
    * @return the extraFacilityViewDataContainer
    */
   public ExtraFacilityViewDataContainer getExtraFacilityViewDataContainer()
   {
      if (this.extraFacilityViewDataContainer == null)
      {
         this.extraFacilityViewDataContainer = new ExtraFacilityViewDataContainer();
      }
      return this.extraFacilityViewDataContainer;
   }

   /**
    * Sets the extra facility view data container.
    *
    * @param extraFacilityViewDataContainer the extraFacilityViewDataContainer to set
    */
   public void setExtraFacilityViewDataContainer(
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      this.extraFacilityViewDataContainer = extraFacilityViewDataContainer;
   }

   /**
    * Gets the insurance container view data.
    *
    * @return the insuranceContainerViewData
    */
   public InsuranceContainerViewData getInsuranceContainerViewData()
   {
      return insuranceContainerViewData;
   }

   /**
    * Sets the insurance container view data.
    *
    * @param insuranceContainerViewData the insuranceContainerViewData to set
    */
   public void setInsuranceContainerViewData(
      final InsuranceContainerViewData insuranceContainerViewData)
   {
      this.insuranceContainerViewData = insuranceContainerViewData;
   }

   /**
    * Gets the session time period.
    *
    * @return the sessionTimePeriod
    */
   public int getSessionTimePeriod()
   {
      return sessionTimePeriod;
   }

   /**
    * Sets the session time period.
    *
    * @param sessionTimePeriod the sessionTimePeriod to set
    */
   public void setSessionTimePeriod(final int sessionTimePeriod)
   {
      this.sessionTimePeriod = sessionTimePeriod;
   }

   /**
    * Checks if is villa acommodation.
    *
    * @return the villaAcommodation
    */
   public boolean isVillaAcommodation()
   {
      return villaAcommodation;
   }

   /**
    * Sets the villa acommodation.
    *
    * @param villaAcommodation the villaAcommodation to set
    */
   public void setVillaAcommodation(final boolean villaAcommodation)
   {
      this.villaAcommodation = villaAcommodation;
   }

   /**
    * Gets the alert messages.
    *
    * @return the alert messages
    */
   public List<AlertViewData> getAlertMessages()
   {
      return alertMessages;
   }

   /**
    * Sets the alert messages.
    *
    * @param alertMessages the new alert messages
    */
   public void setAlertMessages(final List<AlertViewData> alertMessages)
   {
      this.alertMessages = alertMessages;
   }

   /**
    * @return the extraOptionsStaticContentViewData
    */
   public ExtraOptionsStaticContentViewData getExtraOptionsStaticContentViewData()
   {
      return extraOptionsStaticContentViewData;
   }

   /**
    * @param extraOptionsStaticContentViewData the extraOptionsStaticContentViewData to set
    */
   public void setExtraOptionsStaticContentViewData(
      final ExtraOptionsStaticContentViewData extraOptionsStaticContentViewData)
   {
      this.extraOptionsStaticContentViewData = extraOptionsStaticContentViewData;
   }

   /**
    * @return the alamoCarHire
    */
   public boolean isAlamoCarHire()
   {
      return alamoCarHire;
   }

   /**
    * @param alamoCarHire the alamoCarHire to set
    */
   public void setAlamoCarHire(final boolean alamoCarHire)
   {
      this.alamoCarHire = alamoCarHire;
   }

   /**
    * @return the extraOptionsContentViewData
    */
   public ExtraOptionsContentViewData getExtraOptionsContentViewData()
   {
      return extraOptionsContentViewData;
   }

   /**
    * @param extraOptionsContentViewData the extraOptionsContentViewData to set
    */
   public void setExtraOptionsContentViewData(
      final ExtraOptionsContentViewData extraOptionsContentViewData)
   {
      this.extraOptionsContentViewData = extraOptionsContentViewData;
   }

   /**
    * @return the pageResponse
    */
   public PageResponse getPageResponse()
   {
      return pageResponse;
   }

   /**
    * @param pageResponse the pageResponse to set
    */
   public void setPageResponse(final PageResponse pageResponse)
   {
      this.pageResponse = pageResponse;
   }

}
