package uk.co.tui.th.book.store;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.tui.book.domain.CarHireSearchResponse;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;

/**
 * The Store to hold all Car Hire ExtraFacilities.
 *
 * @author munisekhar.k
 *
 */
public class CarHireUpgradeExtraFacilityStore {

    /** The car hire search response. */
    private Map<String, CarHireSearchResponse> carHireSearchResponse = new HashMap<String, CarHireSearchResponse>();

    /**
     *
     */
    public void flush() {
        this.carHireSearchResponse = null;
    }

    /**
     * Gets the car hire search response.
     *
     * @return the carHireSearchResponse
     */
    public Map<String, CarHireSearchResponse> getCarHireSearchResponse() {
        return carHireSearchResponse;
    }

    /**
     * Sets the car hire search response.
     *
     * @param carHireSearchResponse the carHireSearchResponse to set
     */
    public void setCarHireSearchResponse(
            Map<String, CarHireSearchResponse> carHireSearchResponse) {
        this.carHireSearchResponse = carHireSearchResponse;
    }



    /**
     * Returns the extra facility category list.
     *
     * @param packageCode the package code
     * @return the extra facility
     */
    public List<ExtraFacilityCategory> getExtraFacilityLite(String packageCode) {
        List<ExtraFacilityCategory>  categoryList = Collections.emptyList();
        if(getCarHireSearchResponse().get(packageCode) != null){
            return getCarHireSearchResponse().get(packageCode)
                    .getExtraFacilityCategoryList();
        }
        return categoryList;
    }

    /**
     * Populates the carHireSearchResponse Map.
     *
     * @param carHireSearchResponse the car hire search response
     */
    public void add(CarHireSearchResponse carHireSearchResponse)
    {
        getCarHireSearchResponse().put(carHireSearchResponse.getPackageId(), carHireSearchResponse);
    }


}
