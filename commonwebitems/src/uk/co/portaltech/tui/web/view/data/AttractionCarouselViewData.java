/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author Abi
 *
 */
public class AttractionCarouselViewData extends CarouselViewData {
    private List<AttractionViewData> attractions;
    private List<ExcursionViewData>  excursions;

    public AttractionCarouselViewData() {
        super();
    }

    public AttractionCarouselViewData(String name, String code, Integer visibleItem) {
        super(name, code, visibleItem);
    }

    /**
     * @return the attractions
     */
    public List<AttractionViewData> getAttractions() {
        return attractions;
    }

    /**
     * @param attractions
     *            the attractions to set
     */
    public void setAttractions(List<AttractionViewData> attractions) {
        this.attractions = attractions;
    }

    /**
     * @return the excursions
     */
    public List<ExcursionViewData> getExcursions() {
        return excursions;
    }

    /**
     * @param excursions
     *            the excursions to set
     */
    public void setExcursions(List<ExcursionViewData> excursions) {
        this.excursions = excursions;
    }

}
