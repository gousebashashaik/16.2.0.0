/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import uk.co.portaltech.tui.web.view.data.SearchError;

/**
 * @author laxmibai.p
 *
 */
public class DestinationSearchResult {

    private SearchError error;
    private LocationCategoryResult data;
    private boolean nomatch;
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
    /**
     * @return the data
     */
    public LocationCategoryResult getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(LocationCategoryResult data) {
        this.data = data;
    }
    /**
     * @return the nomatch
     */
    public boolean isNomatch() {
        return nomatch;
    }
    /**
     * @param nomatch the nomatch to set
     */
    public void setNomatch(boolean nomatch) {
        this.nomatch = nomatch;
    }



}
