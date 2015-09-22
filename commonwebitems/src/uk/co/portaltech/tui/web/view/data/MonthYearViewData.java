/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author
 *
 */
public class MonthYearViewData {

    private String date;

    private String monthYear;

    /**
     *
     */
    public MonthYearViewData(String date, String monthYear) {
        this.date = date;
        this.monthYear = monthYear;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the monthYear
     */
    public String getMonthYear() {
        return monthYear;
    }

    /**
     * @param monthYear the monthYear to set
     */
    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

}
