/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author gagan
 *
 */
public class CMSNavigationNodeViewData {

    private boolean visible;
    private List<CMSNavigationEntryViewData> entries;
    private List<CMSNavigationNodeViewData> children;
    private String title;
    private CMSNavigationNodeViewData parent;
    private List<CMSLinkComponentViewData> links;
    /**
     * @return the links
     */
    public List<CMSLinkComponentViewData> getLinks() {
        return links;
    }
    /**
     * @param links the links to set
     */
    public void setLinks(List<CMSLinkComponentViewData> links) {
        this.links = links;
    }
    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }
    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    /**
     * @return the entries
     */
    public List<CMSNavigationEntryViewData> getEntries() {
        return entries;
    }
    /**
     * @param entries the entries to set
     */
    public void setEntries(List<CMSNavigationEntryViewData> entries) {
        this.entries = entries;
    }
    /**
     * @return the children
     */
    public List<CMSNavigationNodeViewData> getChildren() {
        return children;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(List<CMSNavigationNodeViewData> children) {
        this.children = children;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the parent
     */
    public CMSNavigationNodeViewData getParent() {
        return parent;
    }
    /**
     * @param parent the parent to set
     */
    public void setParent(CMSNavigationNodeViewData parent) {
        this.parent = parent;
    }

}
