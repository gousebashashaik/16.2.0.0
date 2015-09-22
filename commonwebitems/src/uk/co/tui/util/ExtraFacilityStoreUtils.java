/**
 *
 */
package uk.co.tui.util;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;

import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.tui.book.domain.CarHireSearchResponse;
import uk.co.tui.book.domain.FlightExtraFacilityResponse;
import uk.co.tui.book.domain.PackageExtraFacilityResponse;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.constants.SessionObjectKeys;
import uk.co.tui.book.store.CarHireUpgradeExtraFacilityStore;
import uk.co.tui.book.store.FlightExtraFacilityStore;
import uk.co.tui.book.store.PackageExtraFacilityStore;

/**
 * @author pradeep.as
 *
 */
public final class ExtraFacilityStoreUtils {

    @Resource
    private SessionService sessionService;

    public List<ExtraFacilityCategory> getPackageExtrasFromStoreLite(
            final String packageId) {
        final PackageExtraFacilityStore packageExtrasStore = getPackageExtrafacilityStore();
        if (SyntacticSugar.isNotNull(packageExtrasStore)) {
            return packageExtrasStore.getExtraFacilityLite(packageId);
        }
        return Collections.emptyList();
    }

    /**
     * Gets the flight extras from store.
     *
     * @param packageId
     *            the package id
     * @return the flight extras from store
     */
    public Map<String, List<ExtraFacility>> getFlightExtrasFromStoreForLite(
            final String packageId, final String cabCls) {
        final FlightExtraFacilityStore flightExtrasStore = getFlightExtraFacilityStore();

        if (SyntacticSugar.isNotNull(flightExtrasStore)) {
            return flightExtrasStore.getExtraFacilityFromAllLegsBasedOnCabinClass(packageId,cabCls);
        }
        return MapUtils.EMPTY_MAP;
    }

    /**
     * Update flight extras to store.
     *
     * @param flightExtraFacilityResponse
     *            the flight extra facility response
     */
    public void updateFlightExtrasToStore(
            final FlightExtraFacilityResponse flightExtraFacilityResponse) {
        FlightExtraFacilityStore flightExtraFacilityStore = getFlightExtraFacilityStore();
        if (SyntacticSugar.isNull(flightExtraFacilityStore)) {
            flightExtraFacilityStore = new FlightExtraFacilityStore();
        }

        flightExtraFacilityStore
                .addFlightExtraFacilityResponseToStore(flightExtraFacilityResponse);
        sessionService.setAttribute(
                SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE,
                flightExtraFacilityStore);

    }

    /**
     * Update package extras to store.
     *
     * @param packageExtraResponse
     *            the package extra response
     */
    public void updatePackageExtrasToStore(
            final PackageExtraFacilityResponse packageExtraResponse) {
        PackageExtraFacilityStore packageExtrasStore = getPackageExtrafacilityStore();
        if (SyntacticSugar.isNull(packageExtrasStore)) {
            packageExtrasStore = new PackageExtraFacilityStore();
        }
        packageExtrasStore.add(packageExtraResponse);
        sessionService.setAttribute(
                SessionObjectKeys.PACKAGE_EXTRA_FACILITY_STORE,
                packageExtrasStore);

    }

    /**
     * Update car hire extras to store.
     *
     * @param carHireExtraResponse the car hire extra response
     */
    public void updateCarHireExtrasToStore(
            final CarHireSearchResponse carHireExtraResponse) {
        CarHireUpgradeExtraFacilityStore carHireExtrasStore = getCarHireExtraFacilityStore();
        if (SyntacticSugar.isNull(carHireExtrasStore)) {
            carHireExtrasStore = new CarHireUpgradeExtraFacilityStore();
        }
        carHireExtrasStore.add(carHireExtraResponse);
        sessionService.setAttribute(
                SessionObjectKeys.CARHIRE_UPGRADE_EXTRA_FACILITY_STORE,
                carHireExtrasStore);

    }

    /**
     * Gets the flight extra facility store.
     *
     * @return the flight extra facility store
     */
    private FlightExtraFacilityStore getFlightExtraFacilityStore() {
        return (FlightExtraFacilityStore) sessionService
                .getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
    }

    /**
     * Gets the package extrafacility store.
     *
     * @return the package extrafacility store
     */
    private PackageExtraFacilityStore getPackageExtrafacilityStore() {
        return (PackageExtraFacilityStore) sessionService
                .getAttribute(SessionObjectKeys.PACKAGE_EXTRA_FACILITY_STORE);
    }

    /**
     * Gets the car hire extra facility store.
     *
     * @return the car hire extra facility store
     */
    private CarHireUpgradeExtraFacilityStore getCarHireExtraFacilityStore() {
        return (CarHireUpgradeExtraFacilityStore) sessionService
                .getAttribute(SessionObjectKeys.CARHIRE_UPGRADE_EXTRA_FACILITY_STORE);

    }

    /**
     * Gets the car hire upgrade extras from store lite.
     *
     * @param packageId
     *            the package id
     * @return the car hire upgrade extras from store lite
     */
    public List<ExtraFacilityCategory> getCarHireUpgradeExtrasFromStoreLite(
            final String packageId) {
        final CarHireUpgradeExtraFacilityStore carHireExtrasStore = getCarHireExtraFacilityStore();

        if (SyntacticSugar.isNotNull(carHireExtrasStore)) {
            return carHireExtrasStore.getExtraFacilityLite(packageId);
        }

        return Collections.emptyList();
    }

}
