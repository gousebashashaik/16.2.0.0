/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.fj.book.view.data.teasers.AirportTeaserViewData;
import uk.co.tui.fj.book.view.data.teasers.DepartureDateTeaserViewData;
import uk.co.tui.fj.book.view.data.teasers.DurationTeaserViewData;

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

   /** The duration teaser data. */
   private DurationTeaserViewData durationTeaserViewData;

   /** The date teaser view data. */
   private DepartureDateTeaserViewData dateTeaserViewData;

   /** The airport teaser view data. */
   private AirportTeaserViewData airportTeaserViewData;

   /** The calendar view data. */
   private CalendarViewData calendarViewData;

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

   /** The render teasers. */
   private boolean renderTeasers = true;

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
    * Gets the duration teaser view data.
    *
    * @return the duration teaser view data
    */
   public DurationTeaserViewData getDurationTeaserViewData()
   {
      return durationTeaserViewData;
   }

   /**
    * Sets the duration teaser view data.
    *
    * @param durationTeaserViewData the new duration teaser view data
    */
   public void setDurationTeaserViewData(final DurationTeaserViewData durationTeaserViewData)
   {
      this.durationTeaserViewData = durationTeaserViewData;
   }

   /**
    * Gets the date teaser view data.
    *
    * @return the date teaser view data
    */
   public DepartureDateTeaserViewData getDateTeaserViewData()
   {
      return dateTeaserViewData;
   }

   /**
    * Sets the date teaser view data.
    *
    * @param dateTeaserViewData the new date teaser view data
    */
   public void setDateTeaserViewData(final DepartureDateTeaserViewData dateTeaserViewData)
   {
      this.dateTeaserViewData = dateTeaserViewData;
   }

   /**
    * Gets the airport teaser view data.
    *
    * @return the airport teaser view data
    */
   public AirportTeaserViewData getAirportTeaserViewData()
   {
      return airportTeaserViewData;
   }

   /**
    * Sets the airport teaser view data.
    *
    * @param airportTeaserViewData the new airport teaser view data
    */
   public void setAirportTeaserViewData(final AirportTeaserViewData airportTeaserViewData)
   {
      this.airportTeaserViewData = airportTeaserViewData;
   }

   /**
    * Gets the calendar view data.
    *
    * @return the calendar view data
    */
   public CalendarViewData getCalendarViewData()
   {
      return calendarViewData;
   }

   /**
    * Sets the calendar view data.
    *
    * @param calendarViewData the new calendar view data
    */
   public void setCalendarViewData(final CalendarViewData calendarViewData)
   {
      this.calendarViewData = calendarViewData;
   }

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
    * Checks if is render teasers.
    *
    * @return true, if is render teasers
    */
   public boolean isRenderTeasers()
   {
      return renderTeasers;
   }

   /**
    * Sets the render teasers.
    *
    * @param renderTeasers the new render teasers
    */
   public void setRenderTeasers(final boolean renderTeasers)
   {
      this.renderTeasers = renderTeasers;
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
