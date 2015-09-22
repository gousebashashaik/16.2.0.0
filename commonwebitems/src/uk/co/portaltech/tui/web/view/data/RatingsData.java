/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import org.apache.commons.lang.StringUtils;


public class RatingsData {

    private String officialRating = StringUtils.EMPTY;
    private String tRatingCss;
    private String tripAdvisorRating = StringUtils.EMPTY;
    /**
     * @return the officialRating
     */
    public String getOfficialRating() {
        return officialRating;
    }
    /**
     * @return the tripAdvisorRating
     */
    public String getTripAdvisorRating() {
        return tripAdvisorRating;
    }
    /**
     * @param officialRating the officialRating to set
     */
    public void setOfficialRating(String officialRating) {
        this.officialRating = officialRating;
    }
    /**
     * @param tripAdvisorRating the tripAdvisorRating to set
     */
    public void setTripAdvisorRating(String tripAdvisorRating) {
        this.tripAdvisorRating = tripAdvisorRating;
    }

    /**
     * @return the tRatingCss
     */
    public String gettRatingCss()
    {
        return tRatingCss;
    }

    /**
     * @param tRatingCss the tRatingCss to set
     */
    public void settRatingCss(String tRatingCss)
    {
        this.tRatingCss = tRatingCss;
    }

}
