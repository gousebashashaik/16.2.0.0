/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import org.joda.time.DateTime;


/**
 * @author arya.ap
 *
 */
public class VillaEndecaInfo {

   private String month;

   private String year;

   private boolean available;

   private String id;

   private DateTime date;
    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }
    /**
     * @param month the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }
    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }
    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }


    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }
    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @return the date
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(DateTime date) {
        this.date = date;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder monthAndYear = new StringBuilder();
        monthAndYear.append(this.month).append(" ").append(this.year);
        return monthAndYear.toString();
    }


}
