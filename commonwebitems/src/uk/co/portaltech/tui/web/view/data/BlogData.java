/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author s.consolino
 *
 */
public class BlogData
{
    /**
     *
     */
    private String title;

    /**
     *
     */
    private String link;

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
     * @return the link
     */
    public String getLink()
    {
        return link;
    }

    /**
     * @param link
     *           the link to set
     */
    public void setLink(final String link)
    {
        this.link = link;
    }

    @Override
    public String toString()
    {
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("title: ");
        stringBuffer.append(title);
        stringBuffer.append("; link: ");
        stringBuffer.append(link);
        return stringBuffer.toString();
    }
}
