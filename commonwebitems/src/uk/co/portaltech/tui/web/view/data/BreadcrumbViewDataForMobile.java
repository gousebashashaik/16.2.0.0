/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.portaltech.tui.breadcrumb.BreadCrumb;


/**
 * @author gopinath.n
 *
 */
public class BreadcrumbViewDataForMobile
{

    private List<BreadCrumb> breadCrumbData;

    /**
     * @return the breadCrumbData
     */
    public List<BreadCrumb> getBreadCrumbData()
    {
        return breadCrumbData;
    }

    /**
     * @param breadCrumbData
     *           the breadCrumbData to set
     */
    public void setBreadCrumbData(final List<BreadCrumb> breadCrumbData)
    {
        this.breadCrumbData = breadCrumbData;
    }
}
