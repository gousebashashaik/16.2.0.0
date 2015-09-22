/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;



/**
 * @author laxmibai.p
 *
 */
public class SuggestionViewData {
    private String id;
    private String name;
    private String type;
    private String inspireText;
    private String inspireImage;
    private String inspireURL;
    private List<DestinationData> children =  new ArrayList<DestinationData>();
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
     * @return the inspireText
     */
    public String getInspireText() {
        return inspireText;
    }
    /**
     * @param inspireText the inspireText to set
     */
    public void setInspireText(String inspireText) {
        this.inspireText = inspireText;
    }
    /**
     * @return the inspireImage
     */
    public String getInspireImage() {
        return inspireImage;
    }
    /**
     * @param inspireImage the inspireImage to set
     */
    public void setInspireImage(String inspireImage) {
        this.inspireImage = inspireImage;
    }
    /**
     * @return the inspireURL
     */
    public String getInspireURL() {
        return inspireURL;
    }
    /**
     * @param inspireURL the inspireURL to set
     */
    public void setInspireURL(String inspireURL) {
        this.inspireURL = inspireURL;
    }
    /**
     * @return the children
     */
    public List<DestinationData> getChildren() {
        return children;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(List<DestinationData> children) {
        this.children = children;
    }






}
