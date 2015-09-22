/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.tui.web.view.data.VillaAvailabilityViewData;

/**
 * @author arya.ap
 *
 */
public class VillaAvailabilityViewDataWrapper

{

    private VillaEndecaInfo monthYear;

    private List<VillaAvailabilityViewData> days = new ArrayList<VillaAvailabilityViewData>();

    /**
     * @return the villaEndecaInfo
     */

    /**
     * @return the monthYear
     */
    public VillaEndecaInfo getMonthYear() {
        return monthYear;
    }

    /**
     * @param monthYear
     *            the monthYear to set
     */
    public void setMonthYear(VillaEndecaInfo monthYear) {
        this.monthYear = monthYear;
    }

    /**
     * @return the days
     */
    public List<VillaAvailabilityViewData> getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(List<VillaAvailabilityViewData> days) {
        this.days = days;
    }

}
