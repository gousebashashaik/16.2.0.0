/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

 /**
  * Pojo class for holding  widened criteria option in case of no results page
  */
public class WidenSearchCriteriaData {

    /**
     * boolean value indicating if ui has to display the widened criteria in no results page
     */
    private boolean displayWidenCriteria;

    private String displayName;

    private WidenSearchError widenSearchError = new WidenSearchError();

    private String siteBrand;

    /**
     * Widened Criteria object
     */
    private SearchResultsRequestData searchRequest = new SearchResultsRequestData();

    /**
     * @return the siteBrand
     */
    public String getSiteBrand() {
        return siteBrand;
    }

    /**
     * @param siteBrand the siteBrand to set
     */
    public void setSiteBrand(String siteBrand) {
        this.siteBrand = siteBrand;
    }

    /**
     * @return the displayWidenCriteria
     */
    public boolean isDisplayWidenCriteria() {
        return displayWidenCriteria;
    }

    /**
     * @param displayWidenCriteria the displayWidenCriteria to set
     */
    public void setDisplayWidenCriteria(boolean displayWidenCriteria) {
        this.displayWidenCriteria = displayWidenCriteria;
    }

    /**
     * @return the searchRequest
     */
    public SearchResultsRequestData getSearchRequest() {
        return searchRequest;
    }

    /**
     * @param searchRequest the searchRequest to set
     */
    public void setSearchRequest(SearchResultsRequestData searchRequest) {
        this.searchRequest = searchRequest;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the widenSearchError
     */
    public WidenSearchError getWidenSearchError() {
        return widenSearchError;
    }

    /**
     * @param widenSearchError the widenSearchError to set
     */
    public void setWidenSearchError(WidenSearchError widenSearchError) {
        this.widenSearchError = widenSearchError;
    }


}
