/**
 *
 */
package uk.co.tui.fj.book.facade;

import java.util.List;

import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.fj.book.view.data.FlightOptionsViewData;
import uk.co.tui.fj.book.view.data.WebAnalyticsData;

/**
 * The Interface PackageSwapFacade.
 *
 * @author pradeep.as
 */
public interface PackageSwapFacade {

    /**
     * to get package from the store and call swapPackage method with
     * packageModel parameter.
     *
     * @param packageId
     *            the package id
     * @return FlightOptionsViewData
     */
    FlightOptionsViewData swapPackage(String packageId);

    /**
     * @param selectedPackage
     * @param unAvailableExtras2
     * @return
     */
    List<ExtraFacilityCategory> swapPackage(
            BasePackage selectedPackage,
            List<Feedback> unAvailableExtras2,
            List<Feedback> latestPackageFromInventory);

    /**
     * It sets the web analytics data of alternative flight in WebAnalyticsData
     *
     * @return
     */
    WebAnalyticsData setALTFLT();

    /**
     * @param selectedPackage
     * @return price difference feedBack after info book
     */
    void postSwapUpdation(BasePackage selectedPackage);

    /**
     * @param swapFrom
     * @param swapTo
     * @return
     */
    List<ExtraFacilityCategory> getExtraFacilityCategory(
            BasePackage swapFrom, BasePackage swapTo);

}
