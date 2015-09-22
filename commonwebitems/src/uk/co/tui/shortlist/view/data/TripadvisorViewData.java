/**
 * 
 */
package uk.co.tui.shortlist.view.data;



/**
 * @author Sravani
 * 
 */
public class TripadvisorViewData {
    
    private String           logo;
    private String           ratingBar;
    private Double           averageRating;
    private int              reviewsCount;
    private int              userReviewsCount;
    private String           reviewsUrl;
    
    public String getLogo() {
        return logo;
    }
    
    public void setLogo(String logo) {
        this.logo = logo;
    }
    
    public String getRatingBar() {
        return ratingBar;
    }
    
    public void setRatingBar(String ratingBar) {
        this.ratingBar = ratingBar;
    }
    
    public Double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    
    public int getReviewsCount() {
        return reviewsCount;
    }
    
    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
   
    
    public int getUserReviewsCount() {
        return userReviewsCount;
    }
    
    public void setUserReviewsCount(int userReviewsCount) {
        this.userReviewsCount = userReviewsCount;
    }
    
    public String getReviewsUrl() {
        return reviewsUrl;
    }
    
    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }
    
}
