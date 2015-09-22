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
public class CollectionViewData {
    private String id;
    private String name;
    private String type;
    private String tagLine;
    private boolean available;
    private boolean multiSelect=true;
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
     * @return the tagLine
     */
    public String getTagLine() {
        return tagLine;
    }
    /**
     * @param tagLine the tagLine to set
     */
    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }
    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
    /**
     * @return the multiSelect
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }
    /**
     * @param multiSelect the multiSelect to set
     */
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
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
