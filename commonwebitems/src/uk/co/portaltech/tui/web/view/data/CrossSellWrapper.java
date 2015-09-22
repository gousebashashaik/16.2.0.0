/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abi
 *
 */
public class CrossSellWrapper {

    private List<LocationCarouselViewData>      locationCarousels;
    private List<AccommodationCarouselViewData> accommodationCarousels;
    private List<AttractionCarouselViewData>    attractionCarousels;
    private List<ExcursionCarouselViewData>     excursionCarousels;
    private String                              heading;

    public CrossSellWrapper() {
        locationCarousels = new ArrayList<LocationCarouselViewData>();
        accommodationCarousels = new ArrayList<AccommodationCarouselViewData>();
        attractionCarousels = new ArrayList<AttractionCarouselViewData>();
        excursionCarousels = new ArrayList<ExcursionCarouselViewData>();

    }

    /**
     * @return the locationCarousels
     */
    public List<LocationCarouselViewData> getLocationCarousels() {
        return locationCarousels;
    }

    /**
     * @param locationCarousels
     *            the locationCarousels to set
     */
    public void setLocationCarousels(List<LocationCarouselViewData> locationCarousels) {
        this.locationCarousels = locationCarousels;
    }

    /**
     * @return the accommodationCarousels
     */
    public List<AccommodationCarouselViewData> getAccommodationCarousels() {
        return accommodationCarousels;
    }

    /**
     * @param accommodationCarousels
     *            the accommodationCarousels to set
     */
    public void setAccommodationCarousels(List<AccommodationCarouselViewData> accommodationCarousels) {
        this.accommodationCarousels = accommodationCarousels;
    }

    /**
     * @return the attractionCarousels
     */
    public List<AttractionCarouselViewData> getAttractionCarousels() {
        return attractionCarousels;
    }

    /**
     * @param attractionCarousels
     *            the attractionCarousels to set
     */
    public void setAttractionCarousels(List<AttractionCarouselViewData> attractionCarousels) {
        this.attractionCarousels = attractionCarousels;
    }

    /**
     * @return the excursionCarousels
     */
    public List<ExcursionCarouselViewData> getExcursionCarousels() {
        return excursionCarousels;
    }

    /**
     * @param excursionCarousels
     *            the excursionCarousels to set
     */
    public void setExcursionCarousels(List<ExcursionCarouselViewData> excursionCarousels) {
        this.excursionCarousels = excursionCarousels;
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
