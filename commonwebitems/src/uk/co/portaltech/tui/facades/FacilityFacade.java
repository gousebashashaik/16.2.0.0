/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 *
 * Interface for the Facility facade.
 *
 * @author l.furrer
 *
 */
public interface FacilityFacade {

    /**
     *
     * Gets a facility according to its code.
     *
     * @param code
     *            the code of the facility to be returned
     * @return the Facility data object
     */
    FacilityViewData getFacility(String code);

    /**
     * gets the facilities belonging to the specified accommodation and the specified facility type
     *
     */
    List<FacilityViewData> getFacilitiesByAccommodationAndType(String accommodationCode, String facilityTypeCode);
}
