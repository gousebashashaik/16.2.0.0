/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.io.Serializable;
import java.util.List;


/**
 * @author omonikhide
 *
 */
public class TripadvisorData implements Serializable
{

    private String logo;
    private String ratingBar;
    private Double averageRating;
    private int reviewsCount;
    private List<UserReview> userReviews;
    private int userReviewsCount;
    private String reviewsUrl;
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



    public String getLogo()
    {
        return logo;
    }

    public void setLogo(final String logo)
    {
        this.logo = logo;
    }

    public String getRatingBar()
    {
        return ratingBar;
    }

    public void setRatingBar(final String ratingBar)
    {
        this.ratingBar = ratingBar;
    }

    public Double getAverageRating()
    {
        return averageRating;
    }

    public void setAverageRating(final Double averageRating)
    {
        this.averageRating = averageRating;
    }

    public int getReviewsCount()
    {
        return reviewsCount;
    }

    public void setReviewsCount(final int reviewsCount)
    {
        this.reviewsCount = reviewsCount;
    }

    public List<UserReview> getUserReviews()
    {
        return userReviews;
    }

    public void setUserReviews(final List<UserReview> userReviews)
    {
        this.userReviews = userReviews;
    }

    public int getUserReviewsCount()
    {
        return userReviewsCount;
    }

    public void setUserReviewsCount(final int userReviewsCount)
    {
        this.userReviewsCount = userReviewsCount;
    }

    public String getReviewsUrl()
    {
        return reviewsUrl;
    }

    public void setReviewsUrl(final String reviewsUrl)
    {
        this.reviewsUrl = reviewsUrl;
    }

}
