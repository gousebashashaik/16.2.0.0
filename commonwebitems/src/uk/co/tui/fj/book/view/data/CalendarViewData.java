/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The Class CalendarViewData.
 *
 * @author samantha.gd
 */
public class CalendarViewData
{
   /** The duration. */
   private List<DurationListViewData> duration;

   /** The availability. */
   private Map<Date, DepartureDateAvailabilityViewData> availability;

   /** The departure airport. */
   private List<AirportListViewData> departureAirport;

   /** The filter view data. */
   private List<FlightOptionsFilterViewData> filterViewData;

   /** The should display airport link. */
   private boolean shouldDisplayAirportLink;

/** The start date. */
   private Date startDate;

   /** The end date. */
   private Date endDate ;

   /**
    * Gets the duration.
    *
    * @return the duration
    */
   public List<DurationListViewData> getDuration()
   {
      return duration;
   }

   /**
    * Sets the duration.
    *
    * @param duration the new duration
    */
   public void setDuration(List<DurationListViewData> duration)
   {
      this.duration = duration;
   }

   /**
    * Gets the availability.
    *
    * @return the availability
    */
   public Map<Date, DepartureDateAvailabilityViewData> getAvailability()
   {
      return availability;
   }

   /**
    * Sets the availability.
    *
    * @param availability the availability
    */
   public void setAvailability(Map<Date, DepartureDateAvailabilityViewData> availability)
   {
      this.availability = availability;
   }

   /**
    * Gets the departure airport.
    *
    * @return the departure airport
    */
   public List<AirportListViewData> getDepartureAirport()
   {
      return departureAirport;
   }

   /**
    * Sets the departure airport.
    *
    * @param departureAirport the new departure airport
    */
   public void setDepartureAirport(List<AirportListViewData> departureAirport)
   {
      this.departureAirport = departureAirport;
   }

   /**
    * Gets the filter view data.
    *
    * @return the filterViewData
    */
   public List<FlightOptionsFilterViewData> getFilterViewData()
   {
      return filterViewData;
   }

   /**
    * Sets the filter view data.
    *
    * @param filterViewData the filterViewData to set
    */
   public void setFilterViewData(List<FlightOptionsFilterViewData> filterViewData)
   {
      this.filterViewData = filterViewData;
   }

    /**
     * Gets the start date.
     *
     * @return the startDate
     */
    public Date getStartDate() {
        return (Date) startDate.clone();
    }

    /**
     * Sets the start date.
     *
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = (Date) startDate.clone();
    }

    /**
     * Gets the end date.
     *
     * @return the endDate
     */
    public Date getEndDate() {
        return (Date) endDate.clone();
    }

    /**
     * Sets the end date.
     *
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = (Date) endDate.clone();
    }

    /**
     * Checks if is should display airport link.
     *
     * @return true, if is should display airport link
     */
    public boolean isShouldDisplayAirportLink() {
    return shouldDisplayAirportLink;
    }

    /**
     * Sets the should display airport link.
     *
     * @param shouldDisplayAirportLink
     *            the new should display airport link
     */
    public void setShouldDisplayAirportLink(boolean shouldDisplayAirportLink) {
    this.shouldDisplayAirportLink = shouldDisplayAirportLink;
    }

}
