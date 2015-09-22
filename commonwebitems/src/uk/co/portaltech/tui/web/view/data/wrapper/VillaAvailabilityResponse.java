/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.tui.web.view.data.MonthYearViewData;

/**
 * @author arya.ap
 *
 */
public class VillaAvailabilityResponse {

    private SearchParams searchParams;

    private List<VillaAvailabilityViewDataWrapper> villaAvailabilityData;

    private List<MonthYearViewData> seasonRange = new ArrayList<MonthYearViewData>();

    private String firstAvailableDate;

    private String lastAvailableDate;

    /**
     * @return the villaAvailabilityData
     */
    public List<VillaAvailabilityViewDataWrapper> getVillaAvailabilityData() {
        return villaAvailabilityData;
    }

    /**
     * @param villaAvailabilityData the villaAvailabilityData to set
     */
    public void setVillaAvailabilityData(
            List<VillaAvailabilityViewDataWrapper> villaAvailabilityData) {
        this.villaAvailabilityData = villaAvailabilityData;
    }

    /**
     * @return the searchParams
     */
    public SearchParams getSearchParams() {
        return searchParams;
    }

    /**
     * @param searchParams the searchParams to set
     */
    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    /**
     * @return the seasonRange
     */
    public List<MonthYearViewData> getSeasonRange() {
        return seasonRange;
    }

    /**
     * @param seasonRange the seasonRange to set
     */
    public void setSeasonRange(List<MonthYearViewData> seasonRange) {
        this.seasonRange = seasonRange;
    }

    /**
     * @return the firstAvailableDate
     */
    public String getFirstAvailableDate() {
        return firstAvailableDate;
    }

    /**
     * @param firstAvailableDate the firstAvailableDate to set
     */
    public void setFirstAvailableDate(String firstAvailableDate) {
        this.firstAvailableDate = firstAvailableDate;
    }

    /**
     * @return the lastAvailableDate
     */
    public String getLastAvailableDate() {
        return lastAvailableDate;
    }

    /**
     * @param lastAvailableDate the lastAvailableDate to set
     */
    public void setLastAvailableDate(String lastAvailableDate) {
        this.lastAvailableDate = lastAvailableDate;
    }


}
