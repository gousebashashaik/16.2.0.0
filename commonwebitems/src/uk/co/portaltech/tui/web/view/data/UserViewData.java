/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UserViewData {

    private List<String> shortlistedPackages = new ArrayList<String>();

    /**
     * @return the shortlistedPackages
     */
    public List<String> getShortlistedPackages() {
        return shortlistedPackages;
    }

    /**
     * @param shortlistedPackages the shortlistedPackages to set
     */
    public void setShortlistedPackages(List<String> shortlistedPackages) {
        this.shortlistedPackages = shortlistedPackages;
    }

}
