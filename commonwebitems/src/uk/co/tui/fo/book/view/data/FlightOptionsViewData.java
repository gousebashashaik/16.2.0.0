/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.tui.book.page.response.PageResponse;

/**
 * The Class FlightOptionsViewData.
 *
 * @author munisekhar.k
 */
public class FlightOptionsViewData
{
   // identifies one way or with return
   private String routeType;

   /**
    * Holds the data specific to summary panel.
    */
   private SummaryPanelViewData summaryViewData;

   /**
    * Holds the Package Relevant View Data.
    */
   private PackageViewData packageViewData;

   /**
    * Holds the data specific to summary panel.
    */
   private ExtraFacilityViewDataContainer extraFacilityViewDataContainer;

   private List<AlertViewData> alertMessages;

   private FlightOptionsContentViewData flightOptionsContentViewData;

   private FlightOptionsStaticContentViewData flightOptionsStaticContentViewData;

   /** The session time interval. */
   private int sessionTimePeriod;

   /**
    * The Flag is used to decide whether the baggage section needs to be displayed in FO page.The
    * flag will be set false if Premium seat is selected.
    */
   private boolean baggageSectionDisplayed = true;

   private PageResponse pageResponse;

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
    * @return the routeType
    */
   public String getRouteType()
   {
      return routeType;
   }

   /**
    * @param routeType the routeType to set
    */
   public void setRouteType(final String routeType)
   {
      this.routeType = routeType;
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
    * This method prevents this class from being cloned.
    *
    * @return the object
    * @throws CloneNotSupportedException the clone not supported exception
    * @see java.lang.Object#clone()
    */

   /**
    * @return the baggageSectionDisplayed
    */
   public boolean isBaggageSectionDisplayed()
   {
      return baggageSectionDisplayed;
   }

   /**
    * @param baggageSectionDisplayed the baggageSectionDisplayed to set
    */
   public void setBaggageSectionDisplayed(final boolean baggageSectionDisplayed)
   {
      this.baggageSectionDisplayed = baggageSectionDisplayed;
   }

   /**
    * @return the alertMessages
    */
   public List<AlertViewData> getAlertMessages()
   {
      return alertMessages;
   }

   /**
    * @param alertMessages the alertMessages to set
    */
   public void setAlertMessages(final List<AlertViewData> alertMessages)
   {
      this.alertMessages = alertMessages;
   }

   /**
    * @return the sessionTimePeriod
    */
   public int getSessionTimePeriod()
   {
      return sessionTimePeriod;
   }

   /**
    * @param sessionTimePeriod the sessionTimePeriod to set
    */
   public void setSessionTimePeriod(final int sessionTimePeriod)
   {
      this.sessionTimePeriod = sessionTimePeriod;
   }

   /**
    * @return the flightOptionsContentViewData
    */
   public FlightOptionsContentViewData getFlightOptionsContentViewData()
   {
      return flightOptionsContentViewData;
   }

   /**
    * @param flightOptionsContentViewData the flightOptionsContentViewData to set
    */
   public void setFlightOptionsContentViewData(
      final FlightOptionsContentViewData flightOptionsContentViewData)
   {
      this.flightOptionsContentViewData = flightOptionsContentViewData;
   }

   /**
    * @return the flightOptionsStaticContentViewData
    */
   public FlightOptionsStaticContentViewData getFlightOptionsStaticContentViewData()
   {
      if (this.flightOptionsStaticContentViewData == null)
      {
         return new FlightOptionsStaticContentViewData();
      }
      return flightOptionsStaticContentViewData;
   }

   /**
    * @param flightOptionsStaticContentViewData the flightOptionsStaticContentViewData to set
    */
   public void setFlightOptionsStaticContentViewData(
      final FlightOptionsStaticContentViewData flightOptionsStaticContentViewData)
   {
      this.flightOptionsStaticContentViewData = flightOptionsStaticContentViewData;
   }

   /**
    * @return the pageResponse
    */
   public PageResponse getPageResponse()
   {
      return pageResponse;
   }

   /**
    * @param pageResponse the pageResponseData to set
    */
   public void setPageResponse(final PageResponse pageResponse)
   {
      this.pageResponse = pageResponse;
   }

}
