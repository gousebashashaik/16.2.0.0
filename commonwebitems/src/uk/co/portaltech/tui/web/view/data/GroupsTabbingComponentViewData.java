/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Map;



/**
 * @author vinodkumar.g
 *
 */
public class GroupsTabbingComponentViewData
{

    private String title;
    private String description;
    private Map<String, TabSectionViewData> tabs;

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
     * @return the tabs
     */
    public Map<String, TabSectionViewData> getTabs()
    {
        return tabs;
    }

    /**
     * @param tabs
     *           the tabs to set
     */
    public void setTabs(final Map<String, TabSectionViewData> tabs)
    {
        this.tabs = tabs;
    }


}
