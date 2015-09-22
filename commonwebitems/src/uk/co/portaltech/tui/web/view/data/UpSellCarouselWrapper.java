/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author narendra.bm
 *
 */
public class UpSellCarouselWrapper {
    private String heading;
    private List<AccommodationViewData> accommodationViewDatas;

    /**
     *
     */
    public UpSellCarouselWrapper() {
        accommodationViewDatas = new ArrayList<AccommodationViewData>();
    }
    /**
     * @return the accommodationViewDatas
     */
    public List<AccommodationViewData> getAccommodationViewDatas() {
        return accommodationViewDatas;
    }

    /**
     * @param accommodationViewDatas
     *            the accommodationViewDatas to set
     */
    public void setAccommodationViewDatas(List<AccommodationViewData> accommodationViewDatas) {
        this.accommodationViewDatas = accommodationViewDatas;
    }

    /**
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @param heading the heading to set
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }
}
