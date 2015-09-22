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
public class ExcursionCarouselViewData extends CarouselViewData {
    private List<ExcursionViewData> excursions;


    public ExcursionCarouselViewData() {
        excursions = new ArrayList<ExcursionViewData>();
    }
    public ExcursionCarouselViewData(String name, String code, List<ExcursionViewData> excursionsData,Integer visibleItems){
        super(name,code,visibleItems);
        this.excursions = excursionsData;

    }

    /**
     * @return the excursions
     */
    public List<ExcursionViewData> getExcursions() {
        return excursions;
    }


    /**
     * @param excursions the excursions to set
     */
    public void setExcursions(List<ExcursionViewData> excursions) {
        this.excursions = excursions;
    }


}
