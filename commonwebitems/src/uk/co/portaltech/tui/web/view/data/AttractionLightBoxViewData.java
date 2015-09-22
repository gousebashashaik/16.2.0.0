/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;

/**
 * @author
 *
 */
public class AttractionLightBoxViewData {


    private HasFeatures viewData;

    private HasFeatures carousel;

    private HasFeatures editorial;

    private PriceAndAvailabilityWrapper usps;

    private HasFeatures thumbnailMap;

    /**
     * @return the viewData
     */
    public HasFeatures getViewData() {
        return viewData;
    }
    /**
     * @param viewData the viewData to set
     */
    public void setViewData(HasFeatures viewData) {
        this.viewData = viewData;
    }
    /**
     * @return the carousel
     */
    public HasFeatures getCarousel() {
        return carousel;
    }
    /**
     * @param carousel the carousel to set
     */
    public void setCarousel(HasFeatures carousel) {
        this.carousel = carousel;
    }
    /**
     * @return the editorial
     */
    public HasFeatures getEditorial() {
        return editorial;
    }
    /**
     * @param editorial the editorial to set
     */
    public void setEditorial(HasFeatures editorial) {
        this.editorial = editorial;
    }
    /**
     * @return the usps
     */
    public PriceAndAvailabilityWrapper getUsps() {
        return usps;
    }
    /**
     * @param usps the usps to set
     */
    public void setUsps(PriceAndAvailabilityWrapper usps) {
        this.usps = usps;
    }
    /**
     * @return the thumbnailMap
     */
    public HasFeatures getThumbnailMap() {
        return thumbnailMap;
    }
    /**
     * @param thumbnailMap the thumbnailMap to set
     */
    public void setThumbnailMap(HasFeatures thumbnailMap) {
        this.thumbnailMap = thumbnailMap;
    }

}
