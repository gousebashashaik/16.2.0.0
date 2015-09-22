/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author veena.pn
 *
 */
public class SearchBDestinationGuideViewData {

    private List<SuggestionViewData>   suggestions;

    private List<DestinationData> mostPopularDestinations;

        private List<DestinationData> countryList;

    private CountryViewData accommadationsList;

    /**
     */

    public SearchBDestinationGuideViewData()
    {

    }
    public List<SuggestionViewData> getSuggestions() {
        return suggestions;
    }

    /**
     * @param suggestions the suggestions to set
     */
    public void setSuggestions(List<SuggestionViewData> suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * @return the countryList
     */
    public List<DestinationData> getCountryList() {
        return countryList;
    }

    /**
     * @param countryList the countryList to set
     */
    public void setCountryList(List<DestinationData> countryList) {
        this.countryList = countryList;
    }

    /**
     * @return the accommadationsList
     */
    public CountryViewData getAccommadationsList() {
        return accommadationsList;
    }
    /**
     * @param accommadationsList the accommadationsList to set
     */
    public void setAccommadationsList(CountryViewData accommadationsList) {
        this.accommadationsList = accommadationsList;
    }

    /**
     * @return the mostPopularDestinations
     */
    public List<DestinationData> getMostPopularDestinations()
    {
        return mostPopularDestinations;
    }
    /**
     * @param mostPopularDestinations the mostPopularDestinations to set
     */
    public void setMostPopularDestinations(List<DestinationData> mostPopularDestinations)
    {
        this.mostPopularDestinations = mostPopularDestinations;
    }


}
