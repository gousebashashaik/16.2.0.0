/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author abi
 *
 */
public class ProductCrossSellWrapper {
    private List<ProductRangeViewData> crossSellData;
    private List<AccommodationCarouselViewData> accommodationCarousels;
    private String heading;

    /**
     *
     */
    public ProductCrossSellWrapper() {
        accommodationCarousels = new ArrayList<AccommodationCarouselViewData>();
    }

    /**
     * @return the crossSellData
     */
    public List<ProductRangeViewData> getCrossSellData() {
        return crossSellData;
    }

    /**
     * @param crossSellData the crossSellData to set
     */
    public void setCrossSellData(List<ProductRangeViewData> crossSellData) {
        this.crossSellData = crossSellData;
    }

    /**
     * @return the accommodationCarousels
     */
    public List<AccommodationCarouselViewData> getAccommodationCarousels() {
        return accommodationCarousels;
    }

    /**
     * @param accommodationCarousels the accommodationCarousels to set
     */
    public void setAccommodationCarousels(
            List<AccommodationCarouselViewData> accommodationCarousels) {
        this.accommodationCarousels = accommodationCarousels;
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
