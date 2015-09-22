/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jfree.util.Log;

import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.RecommendationsFacade;
import uk.co.portaltech.tui.populators.RecommendationsPopulator;
import uk.co.portaltech.tui.services.RecommendationsService;
import uk.co.portaltech.tui.utils.CSPSorting;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.RecommendationsData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsBusinessException;

/**
 * @author Raja Rao.R
 *
 */
public class RecommendationsFacadeImpl implements RecommendationsFacade {

    /**
     *
     */
    private static final String ON = "on";

    /**
     *
     */
    private static final String HOME_REC = "HomeRec";

    /**
     *
     */
    private static final String ACCOMODATION_REC = "AccomodationRec";

    /**
     *
     */
    private static final String LATEST_CRITERIA = "latestCriteria";

    /**
     * number 1
     */
    private static final int NUMBER_ONE = 1;

    private static final TUILogUtils LOGGER = new TUILogUtils(
            "RecommendationsFacadeImpl");

    @Resource
    private RecommendationsService recommendationsService;

    @Resource
    private AccommodationFacade accomodationFacade;

    @Resource
    private ComponentFacade componentFacade;

    @Resource
    private RecommendationsPopulator recommendationsPopulator;

    @Resource
    private SessionService sessionService;

    /** The configuration service. */
    @Resource
    private ConfigurationService configurationService;

    /**
     * @return the configurationService
     */
    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    /**
     * @param configurationService
     *            the configurationService to set
     */
    public void setConfigurationService(
            final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * Clone search criteria.
     *
     * @param searchParameter
     *            the search parameter
     * @return the search results request data
     */
    protected SearchResultsRequestData cloneSearchCriteria(
            final SearchResultsRequestData searchParameter) {
        SearchResultsRequestData searchCriteriaClone = new SearchResultsRequestData();
        try {
            searchCriteriaClone = (SearchResultsRequestData) BeanUtils
                    .cloneBean(searchParameter);
        } catch (final Exception e) {
            Log.error("Unable to Clone Search Criteria", e);
        }
        return searchCriteriaClone;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.facades.RecommendationsFacade#getProductProductRanges
     * (java.util.ArrayList)
     */
    @Override
    public RecommendationsData getProductProductRanges(
            final List<String> productCodes, final String siteBrand) {
        LOGGER.debug("getProductProductRanges is calling");

        RecommendationsData recommendationsData = new RecommendationsData();
        final List<AccommodationViewData> accomViewDataList = new ArrayList<AccommodationViewData>();

        if (ON.equalsIgnoreCase(baynoteStubStatus())) {
            recommendationsData = recommendationsService.getRecommendations(
                    HOME_REC, siteBrand);
        } else {
            handleAccomProductRanges(productCodes, recommendationsData,
                    accomViewDataList);
        }
        return recommendationsData;

    }

    /**
     * @param productCodes
     * @param recommendationsData
     * @param accomViewDataList
     */
    private void handleAccomProductRanges(final List<String> productCodes,
            final RecommendationsData recommendationsData,
            final List<AccommodationViewData> accomViewDataList) {
        try {
            for (final String productCode : productCodes) {
                final AccommodationViewData accomViewData = accomodationFacade
                        .getAccommodationProductRanges(productCode);
                if (accomViewData != null) {
                    accomViewDataList.add(accomViewData);
                }

            }
            recommendationsData.setAccomodationDatas(accomViewDataList);
            LOGGER.debug("Recommendations Size: "
                    + recommendationsData.getAccomodationDatas().size());
        } catch (final Exception exp) {
            LOGGER.info("Exception while getting AccommodationProductRanges: ",
                    exp);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.RecommendationsFacade#
     * getRecommendedAccomPriceInfoBrowse(java.util.ArrayList,
     * java.lang.Boolean)
     */
    @Override
    public RecommendationsData getRecommendedAccomPriceInfoBrowse(
            final List<String> productCodes, final Boolean cspSorting,
            final String siteBrand) {
        LOGGER.debug("getRecommendedAccomPriceInfoBrowse");
        List<AccommodationViewData> endecaAccomViewData;
        RecommendationsData browseFlowRecomData = new RecommendationsData();

        if (ON.equalsIgnoreCase(baynoteStubStatus())) {
            browseFlowRecomData = recommendationsService.getRecommendations(
                    ACCOMODATION_REC, siteBrand);
        } else {
            endecaAccomViewData = componentFacade.getRecommendedAccomPriceInfo(
                    productCodes, null, null);

            endecaAccomViewData = handleAccomPriceInfoBrowse(productCodes,
                    cspSorting, siteBrand, endecaAccomViewData);

            browseFlowRecomData.setAccomodationDatas(endecaAccomViewData);

            LOGGER.debug("Recommendations Size: "
                    + browseFlowRecomData.getAccomodationDatas().size());
        }
        return browseFlowRecomData;
    }

    /**
     * @param productCodes
     * @param cspSorting
     * @param siteBrand
     * @param endecaAccomViewData
     * @return
     */
    @SuppressWarnings("javadoc")
    private List<AccommodationViewData> handleAccomPriceInfoBrowse(
            final List<String> productCodes, final Boolean cspSorting,
            final String siteBrand,
            List<AccommodationViewData> endecaAccomViewData) {
        final RecommendationsData recommendationsData = getProductProductRanges(
                productCodes, siteBrand);

        if (CollectionUtils.isNotEmpty(recommendationsData
                .getAccomodationDatas())) {
            endecaAccomViewData = recommendationsPopulator
                    .getBrowseRecommendedAccommodationsViewData(
                            recommendationsData.getAccomodationDatas(),
                            endecaAccomViewData);

        }

        if (Boolean.TRUE.equals(cspSorting)) {
            endecaAccomViewData = CSPSorting
                    .getCSPSortingOrder(endecaAccomViewData);
        }
        return endecaAccomViewData;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.RecommendationsFacade#
     * getReccommendedHolidayPackageDataBook(java.lang.String,
     * java.util.ArrayList, java.lang.Boolean)
     */
    @Override
    public RecommendationsData getReccommendedHolidayPackageDataBook(
            final String siteBrand, final List<String> productCodes,
            final Boolean cspSorting) {
        LOGGER.debug("getReccommendedHolidayPackageDataBook");

        final Map<String, String> accomNameCode = new HashMap<String, String>();
        List<AccommodationViewData> bookFlowAccomData = null;
        HolidayViewData endecaHolidayViewData = null;
        RecommendationsData bookRecData = new RecommendationsData();

        if (ON.equalsIgnoreCase(baynoteStubStatus())) {
            bookRecData = recommendationsService.getRecommendations(
                    ACCOMODATION_REC, siteBrand);
        } else {
            final RecommendationsData recommendationsData = getProductProductRanges(
                    productCodes, siteBrand);

            final SearchResultsRequestData searchParameterInSession = cloneSearchCriteria((SearchResultsRequestData) sessionService
                    .getAttribute(LATEST_CRITERIA));

            if (CollectionUtils.isNotEmpty(recommendationsData
                    .getAccomodationDatas())
                    && recommendationsData.getAccomodationDatas() != null) {
                for (final AccommodationViewData accomData : recommendationsData
                        .getAccomodationDatas()) {
                    accomNameCode.put(accomData.getCode(), accomData.getName());
                }
                searchParameterInSession.setRecomData(accomNameCode);
                searchParameterInSession.setFirst(NUMBER_ONE);
                searchParameterInSession.setOffset(accomNameCode.size());

                try {
                    endecaHolidayViewData = componentFacade
                            .getReccommendedHolidayPackageData(
                                    searchParameterInSession, siteBrand);
                } catch (final SearchResultsBusinessException e) {
                    LOGGER.error(
                            "Error While caling getReccommendedHolidayPackageData : ",
                            e);
                }

                bookFlowAccomData = recommendationsPopulator
                        .getBookflowRecommendedAccommodationsViewData(
                                recommendationsData.getAccomodationDatas(),
                                endecaHolidayViewData);

                /* CSPSorting by DESCENDING Order */

                if (Boolean.TRUE.equals(cspSorting)) {
                    bookFlowAccomData = CSPSorting
                            .getCSPSortingOrder(bookFlowAccomData);
                }
                bookRecData.setAccomodationDatas(bookFlowAccomData);
                LOGGER.debug("Recommendations Size: "
                        + bookRecData.getAccomodationDatas().size());
            }
        }

        return bookRecData;
    }

    @Override
    public String baynoteStubStatus() {
        String baynoteStubCheck = null;
        baynoteStubCheck = configurationService.getConfiguration().getString(
                "recommendations.baynote.stubs");
        return baynoteStubCheck;
    }

}
