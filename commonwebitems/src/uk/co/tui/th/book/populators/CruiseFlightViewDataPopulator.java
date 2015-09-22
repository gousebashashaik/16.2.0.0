package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.th.book.view.data.Airport;
import uk.co.tui.th.book.view.data.FlightViewData;
import uk.co.tui.th.book.view.data.PackageViewData;
import uk.co.tui.th.book.view.data.Schedule;

/**
 * The Class CruiseFlightViewDataPopulator is used to populate cruise flight
 * details. This class has been introduced to avoid unnecessary conditional
 * coding checking eg. following 1.whether it inclusive package or
 * flyNcruisePackage 2.whether leg are null or not etc
 */
public class CruiseFlightViewDataPopulator
        implements
            Populator<BasePackage, PackageViewData> {

    /** The Constant CUSTOM_DATE_FORMAT. */
    private static final String CUSTOM_DATE_FORMAT = "MMM dd, yyyy hh:mm:ss a";
    /* Day in seconds */
    /** The Constant DAYS_IN_SEC. */
    private static final int DAYS_IN_SEC = 1000 * 60 * 60 * 24;

    /** The static page content service. */
    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /** The package component service. */
    @Resource
    private PackageComponentService packageComponentService;

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
        FlightViewData flightViewData = new FlightViewData();
        List<FlightViewData> flightViewDataList = new ArrayList<FlightViewData>();
        populate(source, flightViewData);
        flightViewDataList.add(flightViewData);
        target.setFlightViewData(flightViewDataList);
    }

    /**
     * Populate.
     *
     * @param source
     *            the source
     * @param target
     *            the target
     * @throws ConversionException
     *             the conversion exception
     */
    @SuppressWarnings("boxing")
    public void populate(final BasePackage source, final FlightViewData target)
            throws ConversionException {
        target.setDuration(source.getDuration().intValue());
        target.setDurationSummary(getDescription("DURATION",
                Integer.toString(target.getDuration())));
        final Itinerary flight = packageComponentService
                .getFlightItinerary(source);
        populateLegs(flight.getInBound(), target.getInboundSectors());
        populateLegs(flight.getOutBound(), target.getOutboundSectors());
    }

    /**
     * Populate legs.
     *
     * @param legs
     *            the legs
     */
    private void populateLegs(final List<Leg> legs,
            final List<uk.co.tui.th.book.view.data.Leg> legsViewData) {
        legsViewData.clear();
        if (CollectionUtils.isNotEmpty(legs)) {
            for (final Leg eachLeg : legs) {
                final uk.co.tui.th.book.view.data.Leg eachLegViewData = new uk.co.tui.th.book.view.data.Leg();
                eachLegViewData.setCarrierCode(eachLeg.getCarrier()
                        .getCarrierInformation().getMarketingAirlineCode());
                eachLegViewData.setCarrierName(eachLeg.getCarrier()
                        .getCarrierInformation().getMarketingAirlineName());
                eachLegViewData.setEqmtDescription(getEqmtDescription(eachLeg));
                eachLegViewData.setSchedule(populateScheduleData(
                        eachLeg.getSchedule(), eachLegViewData.getSchedule()));
                eachLegViewData.setJnrDuration(DateUtils.calculateTimeOfFlight(
                        eachLeg.getSchedule().getArrivalTime(), eachLeg
                                .getSchedule().getDepartureTime()));
                eachLegViewData.setArrivalAirport(populateAirportDetails(
                        eachLeg.getArrivalAirport(),
                        eachLegViewData.getArrivalAirport()));
                eachLegViewData.setDepartureAirport(populateAirportDetails(
                        eachLeg.getDepartureAirport(),
                        eachLegViewData.getDepartureAirport()));
                legsViewData.add(eachLegViewData);
            }
        }
    }

    /**
     * Gets the eqmt description.
     *
     * @param leg
     *            the leg
     * @return the eqmt description
     */
    private String getEqmtDescription(final Leg leg) {
        if (leg.getCarrier().getEquipementType() != null) {
            return leg.getCarrier().getEquipementType().toString();
        }
        return null;
    }

    /**
     * This method will return the days between departure date and arrival data.
     *
     * @param flightSchedule
     *            the flight schedule
     * @return int number of days
     */
    @SuppressWarnings("deprecation")
    int getflightOffsetDays(
            final uk.co.tui.book.domain.lite.Schedule flightSchedule) {
        final Date departDate = flightSchedule.getDepartureDate();
        final Date arrivalDate = flightSchedule.getArrivalDate();
        if (!org.apache.commons.lang.time.DateUtils.isSameDay(departDate,
                arrivalDate)) {
            final int days = (int) (arrivalDate.getTime() - departDate
                    .getTime()) / DAYS_IN_SEC;
            if (days < 1) {
                return 1;
            }
            return days;
        }
        return 0;
    }

    /**
     * Populate arrival airport details.
     *
     * @param airport
     *            the airport
     * @param airportViewData
     *            the arr airport
     * @return the airport
     */
    private Airport populateAirportDetails(
            final uk.co.tui.book.domain.lite.Port airport,
            final Airport airportViewData) {
        airportViewData.setCode(airport.getCode());
        airportViewData.setName(airport.getName());
        return airportViewData;
    }

    /**
     * Populate schedule data.
     *
     * @param flightSchedule
     *            the flight schedule
     * @param schedule
     *            the schedule
     * @return the schedule
     */
    @SuppressWarnings("deprecation")
    private Schedule populateScheduleData(
            final uk.co.tui.book.domain.lite.Schedule flightSchedule,
            final Schedule schedule) {
        schedule.setArrivalDate(DateUtils.customFormatFlightDate(flightSchedule
                .getArrivalDate()));
        schedule.setArrivalDateInMiliSeconds(DateUtils.getDateInStringFormat(
                flightSchedule.getArrivalDate(), CUSTOM_DATE_FORMAT));
        schedule.setArrTime(flightSchedule.getArrivalTime());
        schedule.setDepartureDate(DateUtils
                .customFormatFlightDate(flightSchedule.getDepartureDate()));
        schedule.setDepartureDateInMiliSeconds(DateUtils.getDateInStringFormat(
                flightSchedule.getDepartureDate(), CUSTOM_DATE_FORMAT));
        schedule.setDepTime(flightSchedule.getDepartureTime());
        schedule.setFlightOffsetDays(getflightOffsetDays(flightSchedule));
        schedule.setFlightOffsetDaysSummary(getDescription("FLIGHTOFFSETDAYS",
                Integer.toString(schedule.getFlightOffsetDays())));
        schedule.setSlot();
        schedule.setTimeOfFlight(DateUtils.calculateTimeOfFlight(
                schedule.getArrTime(), schedule.getDepTime()));
        return schedule;
    }

    /**
     * Gets the description from content.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the description
     */
    private String getDescription(final String key, final String... value) {
        final Map<String, String> summaryContentMap = staticContentServ
                .getSummaryContents();

        if (StringUtils.isNotEmpty(key)) {
            final String summaryContent = summaryContentMap.get(key);
            if (SyntacticSugar.isNotNull(summaryContent)) {
                if (SyntacticSugar.isEmpty(value)) {
                    return summaryContent;
                }
                return StringUtil.substitute(summaryContent, value);
            }
        }
        return StringUtils.EMPTY;
    }

}
