/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * @author sreenivasulu.v
 *
 */
public class JsonadvisorUserRatingData
{

    private String logo = StringUtils.EMPTY;

    private String ratingBar = StringUtils.EMPTY;
    private Double averageRating;
    private int reviewsCount;
    private int userReviewsCount;
    private List<JsonadvisorUserReviewData> userReviews;
    private String ratingReviewsUrl;

    /**
     * @return the ratingReviewsUrl
     */
    public String getRatingReviewsUrl()
    {
        return ratingReviewsUrl;
    }

    /**
     * @param ratingReviewsUrl
     *           the ratingReviewsUrl to set
     */
    public void setRatingReviewsUrl(final String ratingReviewsUrl)
    {
        this.ratingReviewsUrl = ratingReviewsUrl;
    }

    /**
     * @return the userReviews
     */
    public List<JsonadvisorUserReviewData> getUserReviews()
    {
        return userReviews;
    }

    /**
     * @param userReviews
     *           the userReviews to set
     */
    public void setUserReviews(final List<JsonadvisorUserReviewData> userReviews)
    {
        this.userReviews = userReviews;
    }

    /**
     * @return the logo
     */
    public String getLogo()
    {
        return logo;
    }

    /**
     * @param logo
     *           the logo to set
     */
    public void setLogo(final String logo)
    {
        this.logo = logo;
    }

    /**
     * @return the ratingBar
     */
    public String getRatingBar()
    {
        return ratingBar;
    }

    /**
     * @param ratingBar
     *           the ratingBar to set
     */
    public void setRatingBar(final String ratingBar)
    {
        this.ratingBar = ratingBar;
    }

    /**
     * @return the averageRating
     */
    public Double getAverageRating()
    {
        return averageRating;
    }

    /**
     * @param averageRating
     *           the averageRating to set
     */
    public void setAverageRating(final Double averageRating)
    {
        this.averageRating = averageRating;
    }

    /**
     * @return the reviewsCount
     */
    public int getReviewsCount()
    {
        return reviewsCount;
    }

    /**
     * @param reviewsCount
     *           the reviewsCount to set
     */
    public void setReviewsCount(final int reviewsCount)
    {
        this.reviewsCount = reviewsCount;
    }

    /**
     * @return the userReviewsCount
     */
    public int getUserReviewsCount()
    {
        return userReviewsCount;
    }

    /**
     * @param userReviewsCount
     *           the userReviewsCount to set
     */
    public void setUserReviewsCount(final int userReviewsCount)
    {
        this.userReviewsCount = userReviewsCount;
    }



}
