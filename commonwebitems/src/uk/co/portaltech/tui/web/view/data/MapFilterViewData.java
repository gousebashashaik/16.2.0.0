/**
 *
 */
package uk.co.portaltech.tui.web.view.data;


/**
 * This class is used to provide filter data for accommodation location
 * page in order to display filter panel for maps.
 * @author niranjankumar.d
 *
 */
public class MapFilterViewData {

    private String                      name;
    private String                      id;
    private String                      icon;
    private boolean                     appear;
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }
    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
    /**
     * @return the appear
     */
    public boolean isAppear() {
        return appear;
    }
    /**
     * @param appear the appear to set
     */
    public void setAppear(boolean appear) {
        this.appear = appear;
    }




}
