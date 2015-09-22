/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author laxmibai.p
 *
 */
public class DestinationGuideViewData {

    private List<SuggestionViewData>   suggestions;

    private List<DestinationData> destinationlist;

    /**
     * @return the suggestions
     */

    public DestinationGuideViewData()
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
     * @return the destinationlist
     */
    public List<DestinationData> getDestinationlist() {
        return destinationlist;
    }

    /**
     * @param destinationlist the destinationlist to set
     */
    public void setDestinationlist(List<DestinationData> destinationlist) {
        this.destinationlist = destinationlist;
    }
}
