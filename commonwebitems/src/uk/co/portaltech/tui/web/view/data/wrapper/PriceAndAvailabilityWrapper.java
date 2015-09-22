/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author pts
 *
 */
public class PriceAndAvailabilityWrapper {
    private ExcursionViewData excursion;
    private AttractionViewData attraction;
    /**
     * @return the excursion
     */
    public ExcursionViewData getExcursion() {
        return excursion;
    }
    /**
     * @param excursion the excursion to set
     */
    public void setExcursion(ExcursionViewData excursion) {
        this.excursion = excursion;
    }
    /**
     * @return the attraction
     */
    public AttractionViewData getAttraction() {
        return attraction;
    }
    /**
     * @param attraction the attraction to set
     */
    public void setAttraction(AttractionViewData attraction) {
        this.attraction = attraction;
    }




}
