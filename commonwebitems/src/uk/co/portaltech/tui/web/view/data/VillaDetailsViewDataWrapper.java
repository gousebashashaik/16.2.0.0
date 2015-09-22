/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 * @author ext
 *
 */
public class VillaDetailsViewDataWrapper
{
    private AccommodationViewData accommodationData;

    private List<FacilityViewData> villaFacilities;

    private List<FacilityViewData> otherFacilities;

    /**
     * @return the accommodationData
     */
    public AccommodationViewData getAccommodationData() {
        return accommodationData;
    }

    /**
     * @param accommodationData the accommodationData to set
     */
    public void setAccommodationData(AccommodationViewData accommodationData) {
        this.accommodationData = accommodationData;
    }

    /**
     * @return the villaFacilities
     */
    public List<FacilityViewData> getVillaFacilities() {
        return villaFacilities;
    }

    /**
     * @param villaFacilities the villaFacilities to set
     */
    public void setVillaFacilities(List<FacilityViewData> villaFacilities) {
        this.villaFacilities = villaFacilities;
    }

    /**
     * @return the otherFacilities
     */
    public List<FacilityViewData> getOtherFacilities() {
        return otherFacilities;
    }

    /**
     * @param otherFacilities the otherFacilities to set
     */
    public void setOtherFacilities(List<FacilityViewData> otherFacilities) {
        this.otherFacilities = otherFacilities;
    }
}
