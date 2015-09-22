/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author s.consolino
 *
 */
public class InspirationMapComponentData {
    private List<InspirationMapViewData> inspirationMapViewDataList;
    private Integer                      visibleItems;

    /**
     *
     */
    public InspirationMapComponentData() {
        super();
    }

    /**
     * @param visibleItems
     */
    public InspirationMapComponentData(Integer visibleItems) {
        this.visibleItems = visibleItems;
    }

    /**
     * @return the inspirationMapViewDataList
     */
    public List<InspirationMapViewData> getInspirationMapViewDataList() {
        return inspirationMapViewDataList;
    }

    /**
     * @param inspirationMapViewDataList
     *            the inspirationMapViewDataList to set
     */
    public void setInspirationMapViewDataList(List<InspirationMapViewData> inspirationMapViewDataList) {
        this.inspirationMapViewDataList = inspirationMapViewDataList;
    }

    /**
     * @return the visibleItems
     */
    public Integer getVisibleItems() {
        return visibleItems;
    }

    /**
     * @param visibleItems
     *            the visibleItems to set
     */
    public void setVisibleItems(Integer visibleItems) {
        this.visibleItems = visibleItems;
    }
}
