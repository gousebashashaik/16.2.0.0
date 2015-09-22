/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.io.Serializable;
import java.util.Map;


/**
 * @author narendra.bm
 *
 */
public class MultiCentreData implements Serializable
{
    private String name;
    private String duration;
    private String imageUrl;
    private Map locations;
    private String tRating;
    private String type;

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *           the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return the duration
     */
    public String getDuration()
    {
        return duration;
    }

    /**
     * @param duration
     *           the duration to set
     */
    public void setDuration(final String duration)
    {
        this.duration = duration;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl()
    {
        return imageUrl;
    }

    /**
     * @param imageUrl
     *           the imageUrl to set
     */
    public void setImageUrl(final String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    /**
     * @return the locations
     */
    public Map getLocations()
    {
        return locations;
    }

    /**
     * @param locations
     *           the locations to set
     */
    public void setLocations(final Map locations)
    {
        this.locations = locations;
    }

    /**
     * @return the tRating
     */
    public String gettRating()
    {
        return tRating;
    }

    /**
     * @param tRating
     *           the tRating to set
     */
    public void settRating(final String tRating)
    {
        this.tRating = tRating;
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
