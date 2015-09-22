/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.SearchError;



/**
 * @author pooja.ps
 *
 */
public class LocationSearchResult {

    private List<UnitData> units;

    private SearchError error;

    public LocationSearchResult() {
    }

    /**
     * @return the units
     */
    public List<UnitData> getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(List<UnitData> units) {
        this.units = units;
    }

    /**
     * @return the error
     */
    public SearchError getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(SearchError error) {
        this.error = error;
    }

}