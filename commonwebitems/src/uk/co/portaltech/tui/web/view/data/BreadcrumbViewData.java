/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author omonikhide
 *
 */
public class BreadcrumbViewData {

    private Map<String, String> breadcrumbNodes = new LinkedHashMap<String, String>();
    private Boolean             showCatalog;
    private String              breadcrumbTrailBrandsText;
    private String              breadcrumbTrailBrandsPageLabel;

    public Map<String, String> getBreadcrumbNodes() {
        return breadcrumbNodes;
    }

    public void setBreadcrumbNodes(Map<String, String> breadcrumbNodes) {
        this.breadcrumbNodes = breadcrumbNodes;
    }

    public void addBreadcrumb(String one, String two){
        breadcrumbNodes.put(one, two);
    }

    public Boolean getShowCatalog() {
        return showCatalog;
    }

    public void setShowCatalog(Boolean showCatalog) {
        this.showCatalog = showCatalog;
    }

    public String getBreadcrumbTrailBrandsText() {
        return breadcrumbTrailBrandsText;
    }

    public void setBreadcrumbTrailBrandsText(String breadcrumbTrailBrandsText) {
        this.breadcrumbTrailBrandsText = breadcrumbTrailBrandsText;
    }

    public String getBreadcrumbTrailBrandsPageLabel() {
        return breadcrumbTrailBrandsPageLabel;
    }

    public void setBreadcrumbTrailBrandsPageLabel(String breadcrumbTrailBrandsPageLabel) {
        this.breadcrumbTrailBrandsPageLabel = breadcrumbTrailBrandsPageLabel;
    }

}
