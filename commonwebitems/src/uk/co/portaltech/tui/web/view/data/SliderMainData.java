/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author extcs5
 *
 */
public class SliderMainData {

    private String name;

    private List filters = new ArrayList();

    /**
     * @return the filters
     */
    public List getFilters() {
        return filters;
    }

    /**
     * @param filters the filters to set
     */
    public void setFilters(List filters) {
        this.filters = filters;
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

}
