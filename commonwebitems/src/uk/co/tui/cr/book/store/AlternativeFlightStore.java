/**
 *
 */
package uk.co.tui.cr.book.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.tui.book.domain.lite.BackToBackCruisePackage;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.CruiseAndStayPackage;
import uk.co.tui.book.domain.lite.FlyCruiseHolidayPackage;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.StayAndCruisePackage;

/**
 * The Class AlternativeFlightStore.
 *
 * @author samantha.gd
 */
public class AlternativeFlightStore {

    /** The package holidays. */
    private Map<String, List<BasePackage>> packageHolidays = new HashMap<String, List<BasePackage>>();

    private final List<String> airports = new ArrayList<String>();

    /**
     * Flushes the data stored in the store.
     */
    public void flush() {
        this.packageHolidays = null;
    }

    /**
     * Gets the packages.
     *
     * @return the packages
     */
    public List<BasePackage> getAlternates(final String airportCode) {
        return packageHolidays.get(airportCode);
    }

    /**
     * Adds the alternate packages.
     *
     * @param altPackages
     *            the alt packages
     */
    public void addAlternatePackagesHoliday(final List<BasePackage> altPackages) {
        for (final BasePackage altPackage : altPackages) {
            final String airport = getAirport(altPackage);
            if (!packageHolidays.keySet().contains(airport)) {
                packageHolidays.put(airport, new ArrayList<BasePackage>());
            }
            airports.add(airport);
            packageHolidays.get(airport).add(altPackage);

        }
    }

    /**
     * Gets the airport.
     *
     * @param altPackage
     *            the alt package
     * @return the airport
     */
    private String getAirport(final BasePackage altPackage) {
        // Taking itinerary directly without using package component service
        // because it can't be injected to store
        if (altPackage.getPackageType() == PackageType.FNC) {
            return ((FlyCruiseHolidayPackage) altPackage).getItinerary()
                    .getOutBound().get(0).getDepartureAirport().getCode();
        } else if (altPackage.getPackageType() == PackageType.CNS) {
            return ((CruiseAndStayPackage) altPackage).getItinerary()
                    .getOutBound().get(0).getDepartureAirport().getCode();
        } else if (altPackage.getPackageType() == PackageType.SNC) {
            return ((StayAndCruisePackage) altPackage).getItinerary()
                    .getOutBound().get(0).getDepartureAirport().getCode();
        } else {
            return ((BackToBackCruisePackage) altPackage).getItinerary()
                    .getOutBound().get(0).getDepartureAirport().getCode();
        }
    }
    /**
     * Gets the package.
     *
     * @param packageId
     *            the package id
     * @return the package
     */
    public BasePackage getPackageHoliday(final String packageId) {
        for (final List<BasePackage> packages : this.packageHolidays.values()) {
            for (final BasePackage altPackage : packages) {
                if (altPackage.getId().equals(packageId)) {
                    return altPackage;
                }
            }
        }
        return null;
    }

    /**
     * Gets the airports.
     *
     * @return the airports
     */
    public List<String> getAirports() {
        return airports;
    }

    public List<BasePackage> getPackageHoliday() {
        final List<BasePackage> packages = new ArrayList<BasePackage>();
        for (final List<BasePackage> altPackages : packageHolidays.values()) {
            packages.addAll(altPackages);
        }
        return packages;
    }

}
