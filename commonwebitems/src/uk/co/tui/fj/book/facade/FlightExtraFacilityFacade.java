/**
 *
 */
package uk.co.tui.fj.book.facade;

import java.util.Map;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.fj.book.view.data.FlightOptionsViewData;


/**
 * @author akshay.an
 *
 */
public interface FlightExtraFacilityFacade
{

    /**
     * @param baggageAllocationMap
     * @return FlightOptionsViewData
     */
    FlightOptionsViewData updateProductForBaggageExtras(final Map<String, String> baggageAllocationMap);

    /**
     * @param seatExtrasCode
     * @return FlightOptionsViewData
     */
    FlightOptionsViewData updateProductForSeatExtras(String seatExtrasCode);

    /**
     * @param mealAllocationMap
     * @return FlightOptionsViewData
     */
    FlightOptionsViewData updateProductForMealExtras(Map<String, String> mealAllocationMap);

    /**
     * @param packageModel
     * @param viewData
     */
    void populatePackageView(final BasePackage packageModel, final FlightOptionsViewData viewData);

    /**
     * @param viewData
     */
    void populateFlightOptionsStaticContentViewData(final FlightOptionsViewData viewData);

    /**
     * @param selectedPackage
     */
    void updateBaggageForPremiumSeat(BasePackage selectedPackage, String cabinClassCode);

}
