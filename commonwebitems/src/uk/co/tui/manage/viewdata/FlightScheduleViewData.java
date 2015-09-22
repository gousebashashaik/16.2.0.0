/**
 *
 */
package uk.co.tui.manage.viewdata;



/**
 * @author sunil.bd
 *
 */
public class FlightScheduleViewData extends ScheduleViewData
{
    private String checkInTime;

    private boolean isInBoundNextDay;

    private boolean isOutBoundNextDay;

    /**
     * @return the checkInTime
     */
    public String getCheckInTime()
    {
        return checkInTime;
    }

    /**
     * @param checkInTime
     *           the checkInTime to set
     */
    public void setCheckInTime(final String checkInTime)
    {
        this.checkInTime = checkInTime;
    }

    /**
     * @return the isInBoundNextDay
     */
    public boolean isInBoundNextDay()
    {
        return isInBoundNextDay;
    }

    /**
     * @param isInBoundNextDay
     *           the isInBoundNextDay to set
     */
    public void setInBoundNextDay(final boolean isInBoundNextDay)
    {
        this.isInBoundNextDay = isInBoundNextDay;
    }

    /**
     * @return the isOutBoundNextDay
     */
    public boolean isOutBoundNextDay()
    {
        return isOutBoundNextDay;
    }

    /**
     * @param isOutBoundNextDay
     *           the isOutBoundNextDay to set
     */
    public void setOutBoundNextDay(final boolean isOutBoundNextDay)
    {
        this.isOutBoundNextDay = isOutBoundNextDay;
    }
}
