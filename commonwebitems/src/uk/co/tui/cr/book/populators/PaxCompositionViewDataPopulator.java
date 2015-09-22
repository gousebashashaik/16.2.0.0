/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.cr.book.view.data.BaggageExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.PassengerViewData;
import uk.co.tui.cr.book.view.data.PaxCompositionViewData;

/**
 * The Class PaxCompositionViewDataPopulator.
 *
 * @author samantha.gd
 */
public class PaxCompositionViewDataPopulator
        implements
            Populator<BasePackage, PackageViewData> {

    private short adultCount;
    private short childCount;
    private short infantCount;

    /** The extra facility view data populator. */
    @Resource(name = "crExtraFacilityViewDataPopulator")
    private ExtraFacilityViewDataPopulator extraFacilityViewDataPopulator;

    /** Sonar Fix Creating String Constants to hold repetitive string literals */
    private static final String ADULT = "ADULT";

    private static final String SENIOR = "SENIOR";

    private static final String CHILD = "CHILD";

    private static final String INFANT = "INFANT";

    private static final String SUPERSENIOR = "SUPERSENIOR";

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final BasePackage source, final PackageViewData target)
            throws ConversionException {
        populatePaxCompositionViewData(source, target);
    }

    /**
     * Populate passenger view data.
     *
     * @param source
     *            the source
     * @param target
     *            the target
     */
    private void populatePaxCompositionViewData(final BasePackage source,
            final PackageViewData target) {
        final PaxCompositionViewData paxData = new PaxCompositionViewData();
        paxData.setNoOfAdults(getPersonTypeCountFromPassengers(
                source.getPassengers(), PersonType.ADULT));
        paxData.setNoOfSeniors(getPersonTypeCountFromPassengers(
                source.getPassengers(), PersonType.SENIOR)
                + getPersonTypeCountFromPassengers(source.getPassengers(),
                        PersonType.SUPERSENIOR));
        paxData.setNoOfChildren(getPersonTypeCountFromPassengers(
                source.getPassengers(), PersonType.CHILD));
        paxData.setNoOfInfants(getPersonTypeCountFromPassengers(
                source.getPassengers(), PersonType.INFANT));
        paxData.setChildAges(PassengerUtils.getChildAges(source.getPassengers()));
        final String paxComposition = getPaxComposition(paxData);
        paxData.setPaxComposition(paxComposition);
        target.setPaxViewData(paxData);

        target.setPassenger(populatePassengerViewData(source));
        for (final PassengerViewData passenger : target.getPassenger()) {
            setAdultPassengerCount(paxData, passenger);
            if (StringUtil.equalsIgnoreCase(passenger.getType(), CHILD)) {
                passenger.setPassengerCount(passenger.getIdentifier()
                        - paxData.getNoOfAdults() - paxData.getNoOfSeniors());
            }
            if (StringUtil.equalsIgnoreCase(passenger.getType(), INFANT)) {
                passenger.setPassengerCount(passenger.getIdentifier()
                        - paxData.getNoOfAdults() - paxData.getNoOfSeniors()
                        - paxData.getNoOfChildren());
            }
        }
    }

    /**
     * @param paxData
     * @param passenger
     */
    private void setAdultPassengerCount(final PaxCompositionViewData paxData,
            final PassengerViewData passenger) {
        if (StringUtil.equalsIgnoreCase(passenger.getType(), ADULT)) {
            passenger.setPassengerCount(passenger.getIdentifier());
        }
        if (StringUtil.equalsIgnoreCase(passenger.getType(), SENIOR)) {
            passenger.setPassengerCount(passenger.getIdentifier()
                    - paxData.getNoOfAdults());
        }
    }

    /**
     * This method creates a string representing all the passengers.
     *
     * @param paxData
     *            the pax data
     * @return paxComposition
     */
    private String getPaxComposition(final PaxCompositionViewData paxData) {
        final StringBuilder paxComposition = new StringBuilder("");
        final int noOfAdult = paxData.getNoOfAdults();
        final int noOfSenior = paxData.getNoOfSeniors();
        final int noOfChildren = paxData.getNoOfChildren()
                + paxData.getNoOfInfants();
        if (noOfAdult > 0) {
            appendAdults(paxComposition, paxData);
        }
        if (noOfSenior > 0) {
            appendSeniors(paxComposition, paxData);
        }
        if (noOfChildren > 0) {
            appendChildren(paxComposition, paxData);
        }
        return paxComposition.toString();
    }

    /**
     * Appends the children to the party composition.
     *
     * @param paxComposition
     *            the pax composition
     * @param paxData
     *            the pax data
     */
    @SuppressWarnings("boxing")
    private void appendChildren(final StringBuilder paxComposition,
            final PaxCompositionViewData paxData) {
        final String comma = ", ";
        final int noOfChildren = paxData.getNoOfChildren()
                + paxData.getNoOfInfants();
        final List<Integer> childAges = paxData.getChildAges();

        appendChildText(paxComposition, noOfChildren);
        int count = 0;
        for (final Integer integer : childAges) {
            paxComposition.append(integer);
            if (integer > 1) {
                paxComposition.append(" yrs");
            } else {
                paxComposition.append(" yr");
            }
            if (count < childAges.size() - 1) {
                paxComposition.append(comma);
            }
            count++;
        }
        paxComposition.append(")");
    }

    /**
     * @param paxComposition
     * @param noOfChildren
     */
    private void appendChildText(final StringBuilder paxComposition,
            final int noOfChildren) {
        if (noOfChildren > 1) {
            paxComposition.append(noOfChildren).append(" Children (");
        } else {
            paxComposition.append(noOfChildren).append(" Child (");
        }
    }

    /**
     * Appends the Seniors to the party composition.
     *
     * @param paxComposition
     *            the pax composition
     * @param paxData
     *            the pax data
     */
    private void appendSeniors(final StringBuilder paxComposition,
            final PaxCompositionViewData paxData) {
        final String comma = ", ";
        final int noOfSenior = paxData.getNoOfSeniors();
        final int noOfChildren = paxData.getNoOfChildren()
                + paxData.getNoOfInfants();

        if (noOfSenior > 1) {
            paxComposition.append(noOfSenior).append(" Seniors");
        } else {
            paxComposition.append(noOfSenior).append(" Senior");
        }
        if (noOfChildren > 0) {
            paxComposition.append(comma);
        }

    }

    /**
     * Appends the adults to the party composition.
     *
     * @param paxComposition
     *            the pax composition
     * @param paxData
     *            the pax data
     */
    private void appendAdults(final StringBuilder paxComposition,
            final PaxCompositionViewData paxData) {
        final String comma = ", ";
        final int noOfAdult = paxData.getNoOfAdults();
        final int noOfSenior = paxData.getNoOfSeniors();
        final int noOfChildren = paxData.getNoOfChildren()
                + paxData.getNoOfInfants();

        if (noOfAdult > 1) {
            paxComposition.append(noOfAdult).append(" Adults");
        } else {
            paxComposition.append(noOfAdult).append(" Adult");
        }
        if (noOfSenior > 0 || noOfChildren > 0) {
            paxComposition.append(comma);
        }
    }

    /**
     * Populate passenger view data.
     *
     * @param source
     *            the source
     * @return the list
     */
    @SuppressWarnings("boxing")
    private List<PassengerViewData> populatePassengerViewData(
            final BasePackage source) {
        final List<PassengerViewData> passengerViewData = new ArrayList<PassengerViewData>();

        adultCount = 0;
        childCount = 0;
        infantCount = 0;

        for (final Passenger passenger : source.getPassengers()) {
            final PassengerViewData paxData = new PassengerViewData();
            paxData.setIdentifier(passenger.getId());
            String type;
            if (isAdultType(passenger)) {
                type = ADULT;
            } else {
                type = passenger.getType().toString();
            }
            paxData.setType(type);
            createPassengerLabel(passenger, paxData);
            if (passenger.getAge() != null) {
                paxData.setAge(passenger.getAge());
            }
            populateBaggageAndMealExtraFacilitiesForEachPax(passenger, paxData);
            passengerViewData.add(paxData);
        }
        return passengerViewData;
    }

    /**
     * @param passenger
     * @param paxData
     */
    private void populateBaggageAndMealExtraFacilitiesForEachPax(
            final Passenger passenger, final PassengerViewData paxData) {
        if (CollectionUtils.isNotEmpty(passenger.getExtraFacilities())) {
            populateExFacilityForEachPax(passenger, paxData);
        }
    }

    /**
     * @param passenger
     * @param paxData
     */
    private void populateExFacilityForEachPax(final Passenger passenger,
            final PassengerViewData paxData) {
        for (final ExtraFacility extra : passenger.getExtraFacilities()) {
            if (StringUtils.equalsIgnoreCase(extra.getExtraFacilityCategory()
                    .getCode(), "BAG")) {
                final BaggageExtraFacilityViewData bagViewData = new BaggageExtraFacilityViewData();
                extraFacilityViewDataPopulator.populate(extra, bagViewData);
                populateSelectedBaggageOption(paxData, bagViewData);
            } else if (StringUtils.equalsIgnoreCase(extra
                    .getExtraFacilityCategory().getCode(), "MEAL")) {
                final ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
                extraFacilityViewDataPopulator.populate(extra, extraViewData);
                populateSelectedMealOption(paxData, extraViewData);

            }
        }
    }

    /**
     * @param paxData
     * @param extraViewData
     */
    private void populateSelectedMealOption(final PassengerViewData paxData,
            final ExtraFacilityViewData extraViewData) {
        if (paxData.getSelectedMealOption() == null) {
            paxData.setSelectedMealOption(extraViewData);
        } else {
            paxData.getSelectedMealOption().setAdultPrice(
                    paxData.getSelectedMealOption().getAdultPrice()
                            .add(extraViewData.getAdultPrice()));
            paxData.getSelectedMealOption().setChildPrice(
                    paxData.getSelectedMealOption().getChildPrice()
                            .add(extraViewData.getChildPrice()));
        }
    }

    /**
     * @param paxData
     * @param bagViewData
     */
    private void populateSelectedBaggageOption(final PassengerViewData paxData,
            final BaggageExtraFacilityViewData bagViewData) {
        if (paxData.getSelectedBaggageOption() == null) {
            paxData.setSelectedBaggageOption(bagViewData);
        } else {
            paxData.getSelectedBaggageOption().setPrice(
                    paxData.getSelectedBaggageOption().getPrice()
                            .add(bagViewData.getPrice()));
        }
    }

    /**
     * This method returns count of person type for given collection of
     * Passengers.
     *
     * @param passengers
     *            the passengers
     * @param type
     *            the type
     * @return int
     */
    private int getPersonTypeCountFromPassengers(
            final Collection<Passenger> passengers, final PersonType type) {
        final Set<PersonType> adultType = EnumSet.of(type);
        return PassengerUtils.getPersonTypeCountFromPassengers(passengers,
                adultType);
    }

    /**
     * This method calculates the type of passengers and create the labels
     * accordingly.
     *
     * @param passenger
     * @param paxViewData
     *
     */

    private void createPassengerLabel(final Passenger passenger,
            final PassengerViewData paxViewData) {

        if (isAdultType(passenger)) {

            paxViewData.setPassengerLabel("Adult" + " " + (++adultCount));

        } else if (PersonType.CHILD.toString().equals(
                passenger.getType().toString())) {

            paxViewData.setPassengerLabel("Child" + " " + (++childCount));

        } else if (PersonType.INFANT.toString().equals(
                passenger.getType().toString())) {

            paxViewData.setPassengerLabel("Infant" + " " + (++infantCount));
        }

    }

    /**
     * @param passenger
     * @return
     */
    private boolean isAdultType(final Passenger passenger) {
        return (ADULT).equals(passenger.getType().toString())
                || (SENIOR).equals(passenger.getType().toString())
                || (SUPERSENIOR).equals(passenger.getType().toString());
    }
}
