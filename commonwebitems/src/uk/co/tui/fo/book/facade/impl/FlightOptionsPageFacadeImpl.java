/**
 *
 */
package uk.co.tui.fo.book.facade.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.FlightExtraFacilityResponse;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.services.ServiceLocator;
import uk.co.tui.book.services.inventory.FlightExtrasService;
import uk.co.tui.book.services.inventory.PackageExtrasService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.fo.book.ExtraFacilityUpdator;
import uk.co.tui.fo.book.constants.BookFlowConstants;
import uk.co.tui.fo.book.constants.SessionObjectKeys;
import uk.co.tui.fo.book.facade.FlightOptionsPageFacade;
import uk.co.tui.fo.book.populators.FlightExtrasContainerPopulator;
import uk.co.tui.fo.book.populators.SummaryPanelUrlPopulator;
import uk.co.tui.fo.book.store.FlightExtraFacilityStore;
import uk.co.tui.fo.book.view.data.ContentViewData;
import uk.co.tui.fo.book.view.data.FlightOptionsContentViewData;
import uk.co.tui.fo.book.view.data.FlightOptionsStaticContentViewData;
import uk.co.tui.fo.book.view.data.FlightOptionsViewData;
import uk.co.tui.fo.exception.TUIBusinessException;

/**
 * The Class FlightOptionsPageFacadeImpl.
 *
 * @author sunilkumar.sahu
 */
public class FlightOptionsPageFacadeImpl implements FlightOptionsPageFacade {

    /** The logger to be used. */
    private static final TUILogUtils LOG = new TUILogUtils(
            "FlightOptionsPageFacadeImpl");

    /** The session service. */
    @Resource
    private SessionService sessionService;

    /** The package extras service. */
    @Resource
    private PackageExtrasService packageExtrasServiceLite;

    /** The flight extras service. */
    @Resource
    private FlightExtrasService flightExtrasServiceLite;

    /** The extra facility updator. */
    @Resource(name = "foExtraFacilityUpdator")
    private ExtraFacilityUpdator extraFacilityUpdator;

    /** The summary panel url populator. */
    @Resource(name = "foSummaryPanelUrlPopulator")
    private SummaryPanelUrlPopulator summaryPanelUrlPopulator;

    @Resource(name = "foFlightExtrasContainerPopulator")
    private FlightExtrasContainerPopulator flightExtrasContainerPopulator;

    /** The flight static content view data populator. */
    @Resource(name = "foFlightStaticContentViewDataPopulator")
    private Populator<Object, FlightOptionsStaticContentViewData> flightStaticContentViewDataPopulator;

    /** The flight options content view data populator. */
    @Resource(name = "foFlightOptionsContentViewDataPopulator")
    private Populator<Object, ContentViewData> flightOptionsContentViewDataPopulator;

    /** The package cart service. */
    @Resource
    private PackageCartService packageCartService;

    public static final int TWO = 2;

    /** Package ViewData Populator Service Locator */
    @Resource
    private ServiceLocator<Populator> packageViewDataPopulatorServiceLocator;

    /**
     * This method facilitates the rendering of Alternate Flights Component in
     * Flight options page.
     *
     * @return the flight options view data
     * @throws TUIBusinessException
     *             the tUI business exception
     */
    @Override
    public FlightOptionsViewData renderFlightOptions()
            throws TUIBusinessException {
        final BasePackage packageModel = getBasePackageFromCart();
        getFlightExtras();
        final FlightOptionsViewData viewData = new FlightOptionsViewData();
        populateFlightOptionsPageViewData(packageModel, viewData);
        populateFlightOptionsStaticContentViewData(viewData);
        populateFlightOptionsContentViewData(viewData);
        return viewData;
    }

    /**
     * The method hits anite and fetches the flightExtraFacility response and
     * validates the Extras , populates into model and finally populates into
     * the viewData.
     *
     * @throws TUIBusinessException
     */
    private void getFlightExtras() throws TUIBusinessException {

        // get extra facility response for the selected package
        FlightExtraFacilityResponse flightExtraFacilityResponse;
        try {
            flightExtraFacilityResponse = flightExtrasServiceLite
                    .getFlightExtra();
        } catch (final BookServiceException e) {
            LOG.error("TUIBusinessException : " + e.getErrorCode());
            throw new TUIBusinessException(e.getErrorCode(),
                    e.getCustomMessage(), e);
        }
        // update package model with default extra facility
        final FlightExtraFacilityStore flightExtraStore = new FlightExtraFacilityStore();
        updateFlightExtraFacilityStore(flightExtraFacilityResponse,
                flightExtraStore);
        packageExtrasServiceLite
                .updateDefaultExtraFacilitiesToPackage(flightExtraStore
                        .getExtraFacilityFromAllLegsBasedOnCabinClass(
                                flightExtraFacilityResponse.getPackageId(),
                                getCabinClassFromPackage()));

    }

    /**
     * gets the base package model from cart.
     *
     * @return the package from cart
     */
    private BasePackage getBasePackageFromCart() {
        return packageCartService.getBasePackage();
    }

    /**
     * Updates flight extra facility store.
     *
     * @param flightExtraFacilityResponse
     *            the flight extra facility response
     * @param flightExtraStore
     *            the flight extra store
     */
    private void updateFlightExtraFacilityStore(
            final FlightExtraFacilityResponse flightExtraFacilityResponse,
            final FlightExtraFacilityStore flightExtraStore) {

        flightExtraStore
                .addFlightExtraFacilityResponseToStore(flightExtraFacilityResponse);
        sessionService
                .setAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE,
                        flightExtraStore);
    }


    /**
     * The method is used to populate the List of ExtraFacility which are
     * present in all the Legs as part of Flight response.
     *
     * @param packageModel
     *            the BasePackage
     * @param viewData
     *            the FlightOptionsViewData
     */
    private void populateFlightOptionsPageViewData(
            final BasePackage packageModel, final FlightOptionsViewData viewData) {
        populatePackageView(packageModel, viewData);
        populateSummaryPanelUrlsViewData(viewData);
        viewData.setPackageType(packageModel.getPackageType().toString());
    }

    /**
     * The method is used to populate the List of ExtraFacility which are
     * present in all the Legs as part of Flight response.
     *
     * @param packageModel
     *            the BasePackage
     * @param viewData
     *            the FlightOptionsViewData
     */
    @Override
    public void populatePackageView(final BasePackage packageModel,
            final FlightOptionsViewData viewData) {

        // REVISIT:NEED to remove down cast
        populateContainerView(viewData, packageModel.getId());
        (packageViewDataPopulatorServiceLocator
                .locateByPackageType(packageModel.getPackageType().toString()))
                .populate(packageModel, viewData.getPackageViewData());
        extraFacilityUpdator.updatePackageViewData(packageModel,
                viewData.getPackageViewData());
        extraFacilityUpdator.updateSeatExtrasRelativePrice(viewData);
        extraFacilityUpdator.updateBaggageDisplayIndicator(packageModel,
                viewData);
        extraFacilityUpdator.updateContainerViewData(
                viewData.getPackageViewData(),
                viewData.getExtraFacilityViewDataContainer());

    }

    /**
     * The method is used to populate the List of ExtraFacility which are
     * present in all the Legs as part of Flight response.
     *
     * @param viewData
     *            the FlightOptionsViewData
     */
    private void populateSummaryPanelUrlsViewData(
            final FlightOptionsViewData viewData) {
        summaryPanelUrlPopulator.populate(BookFlowConstants.FLIGHTOPTIONS,
                viewData.getSummaryViewData());
    }

    /**
     * The method is used to populate the List of ExtraFacility which are
     * present in all the Legs as part of Flight response.
     *
     * @param viewData
     *            the FlightOptionsViewData
     * @param packageCode
     *            the package code
     */
    private void populateContainerView(final FlightOptionsViewData viewData,
            final String packageCode) {
        final FlightExtraFacilityStore flightExtraStore = sessionService
                .getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);

        final Map<String, List<ExtraFacility>> validExtraFacilitiesMap = flightExtraStore
                .getExtraFacilityFromAllLegsBasedOnCabinClass(packageCode,
                        getCabinClassFromPackage());

        for (final List<ExtraFacility> extraFacilityModels : validExtraFacilitiesMap
                .values()) {
            flightExtrasContainerPopulator.populate(extraFacilityModels,
                    viewData.getExtraFacilityViewDataContainer());
        }

        extraFacilityUpdator.sortSeatsByIndex(viewData
                .getExtraFacilityViewDataContainer().getSeatOptions());
        extraFacilityUpdator.sortMealAlphabetically(viewData
                .getExtraFacilityViewDataContainer().getMealOptions());
        extraFacilityUpdator.sortBaggageBasedOnWeight(viewData
                .getExtraFacilityViewDataContainer().getBaggageOptions());
    }

    /**
     * Populate flight options static content view data.
     *
     * @param viewData
     *            the view data
     */
    @Override
    public void populateFlightOptionsStaticContentViewData(
            final FlightOptionsViewData viewData) {
        final FlightOptionsStaticContentViewData flightOptionsStaticContentViewData = new FlightOptionsStaticContentViewData();
        flightStaticContentViewDataPopulator.populate(new Object(),
                flightOptionsStaticContentViewData);
        viewData.setFlightOptionsStaticContentViewData(flightOptionsStaticContentViewData);

    }

    /**
     * Populates the flight options content view data
     */
    private void populateFlightOptionsContentViewData(
            final FlightOptionsViewData viewData) {
        final FlightOptionsContentViewData flightOptionsContentViewData = new FlightOptionsContentViewData();
        final ContentViewData contentViewData = new ContentViewData();
        flightOptionsContentViewDataPopulator.populate(new Object(),
                contentViewData);
        flightOptionsContentViewData.setFlightContentViewData(contentViewData);
        viewData.setFlightOptionsContentViewData(flightOptionsContentViewData);

    }

    @Override
    public void updatePackageIntoCart() {
        packageCartService.updateCart(getBasePackageFromCart());
    }

    private String getCabinClassFromPackage() {
        return PackageUtilityService.getCabinClass(getBasePackageFromCart());
    }

}
