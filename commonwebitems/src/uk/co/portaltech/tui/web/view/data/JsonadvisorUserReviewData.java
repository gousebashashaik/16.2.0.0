/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import org.apache.commons.lang.StringUtils;



/**
 * @author sreenivasulu.v
 *
 */
public class JsonadvisorUserReviewData
{



    private String author = StringUtils.EMPTY;
    private String authorLocation = StringUtils.EMPTY;
    private String content = StringUtils.EMPTY;
    private String contentSummary = StringUtils.EMPTY;
    private String publishedDate = StringUtils.EMPTY;
    private String ratingImage = StringUtils.EMPTY;
    private String title = StringUtils.EMPTY;
    private String authorID = StringUtils.EMPTY;


    private String reviewsUrl = StringUtils.EMPTY;

    /**
     * @return the reviewsUrl
     */
    public String getReviewsUrl()
    {
        return reviewsUrl;
    }

    /**
     * @param reviewsUrl
     *           the reviewsUrl to set
     */
    public void setReviewsUrl(final String reviewsUrl)
    {
        this.reviewsUrl = reviewsUrl;
    }

    /**
     * @return the author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * @param author
     *           the author to set
     */
    public void setAuthor(final String author)
    {
        this.author = author;
    }

    /**
     * @return the authorLocation
     */
    public String getAuthorLocation()
    {
        return authorLocation;
    }

    /**
     * @param authorLocation
     *           the authorLocation to set
     */
    public void setAuthorLocation(final String authorLocation)
    {
        this.authorLocation = authorLocation;
    }

    /**
     * @return the content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * @param content
     *           the content to set
     */
    public void setContent(final String content)
    {
        this.content = content;
    }

    /**
     * @return the contentSummary
     */
    public String getContentSummary()
    {
        return contentSummary;
    }

    /**
     * @param contentSummary
     *           the contentSummary to set
     */
    public void setContentSummary(final String contentSummary)
    {
        this.contentSummary = contentSummary;
    }

    /**
     * @return the publishedDate
     */
    public String getPublishedDate()
    {
        return publishedDate;
    }

    /**
     * @param publishedDate
     *           the publishedDate to set
     */
    public void setPublishedDate(final String publishedDate)
    {
        this.publishedDate = publishedDate;
    }

    /**
     * @return the ratingImage
     */
    public String getRatingImage()
    {
        return ratingImage;
    }

    /**
     * @param ratingImage
     *           the ratingImage to set
     */
    public void setRatingImage(final String ratingImage)
    {
        this.ratingImage = ratingImage;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *           the title to set
     */
    public void setTitle(final String title)
    {
        this.title = title;
    }

    /**
     * @return the authorID
     */
    public String getAuthorID()
    {
        return authorID;
    }

    /**
     * @param authorID
     *           the authorID to set
     */
    public void setAuthorID(final String authorID)
    {
        this.authorID = authorID;
    }




}
