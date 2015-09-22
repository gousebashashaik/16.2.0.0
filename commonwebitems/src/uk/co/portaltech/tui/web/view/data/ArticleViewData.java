/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


/**
 * @author gagan
 *
 */
public class ArticleViewData implements Serializable
{

    private String content;
    private String source;
    private String url;
    private Date publishDate;
    private String title;
    private Collection<MediaViewData> articleImages;
    private String type;

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
     * @return the source
     */
    public String getSource()
    {
        return source;
    }

    /**
     * @param source
     *           the source to set
     */
    public void setSource(final String source)
    {
        this.source = source;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url
     *           the url to set
     */
    public void setUrl(final String url)
    {
        this.url = url;
    }

    /**
     * @return the publishDate
     */
    public Date getPublishDate() {
        if (this.publishDate != null) {
            return new Date(publishDate.getTime());
        }
        return null;
    }

    /**
     * @param publishDate
     *           the publishDate to set
     */
    public void setPublishDate(final Date publishDate)
    {
        if (publishDate != null) {
            this.publishDate = new Date(publishDate.getTime());
        }
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
     * @return the articleImage
     */
    public Collection<MediaViewData> getArticleImage()
    {
        return articleImages;
    }

    /**
     * @param articleImage
     *           the articleImage to set
     */
    public void setArticleImage(final Collection<MediaViewData> articleImages)
    {
        this.articleImages = articleImages;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *           the type to set
     */
    public void setType(final String type)
    {
        this.type = type;
    }

}
