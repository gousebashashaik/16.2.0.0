/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;


/**
 * @author arun.y
 *
 */
public class SocialMediaLinksComponentData
{

    private List<SocialMediaViewData> socialMediaViewDataList;
    private String title;
    private String description;

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
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *           the description to set
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return the socialMediaViewDataList
     */
    public List<SocialMediaViewData> getSocialMediaViewDataList()
    {
        return socialMediaViewDataList;
    }

    /**
     * @param socialMediaViewDataList
     *           the socialMediaViewDataList to set
     */
    public void setSocialMediaViewDataList(final List<SocialMediaViewData> socialMediaViewDataList)
    {
        this.socialMediaViewDataList = socialMediaViewDataList;
    }

}
