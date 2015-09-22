/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niranjani.r
 *
 */
public class CollectionGroupViewData {
    private String id;
    private String name;
    private String type;
    private List<CollectionViewData> children =  new ArrayList<CollectionViewData>();
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
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the children
     */
    public List<CollectionViewData> getChildren() {
        return children;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(List<CollectionViewData> children) {
        this.children = children;
    }

}
