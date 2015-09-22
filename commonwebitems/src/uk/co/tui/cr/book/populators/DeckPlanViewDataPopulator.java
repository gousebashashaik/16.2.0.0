/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.cr.book.constants.BookFlowConstants;
import uk.co.tui.cr.book.criteria.DeckPlanCriteria;
import uk.co.tui.cr.book.view.data.CabinFacilitiesViewData;
import uk.co.tui.cr.book.view.data.DeckPlanViewData;
import uk.co.tui.cruise.browse.data.CabinContentData;
import uk.co.tui.cruise.browse.data.ItemContentData;
import uk.co.tui.cruise.browse.service.ProductBrowseService;

/**
 * @author ramkishore.p
 *
 */
public class DeckPlanViewDataPopulator
        implements
            Populator<DeckPlanCriteria, DeckPlanViewData>

{
    /** Logger for DeckPlanViewDataPopulator class. **/
    private static final TUILogUtils LOG = new TUILogUtils(
            "DeckPlanViewDataPopulator");

    /** The ProductBrowseService. */
    @Resource
    private ProductBrowseService productBrowseService;

    /** The cms site service. */
    @Resource
    private CMSSiteService cmsSiteService;

    /** The sessionService. */
    @Resource
    private SessionService sessionService;

    /**
     * @param deckPlanCriteria
     * @param deckPlanViewData
     */
    @Override
    public void populate(final DeckPlanCriteria deckPlanCriteria,
            final DeckPlanViewData deckPlanViewData) {
        final List<String> brandPks = getBrandPKs();
        final String shipCode = deckPlanCriteria.getShipCode();
        final String deckNo = deckPlanCriteria.getDeckNo();
        final CatalogVersionModel currentCatalog = cmsSiteService
                .getCurrentCatalogVersion();

        final Map<String, ItemContentData> deckDataMap = productBrowseService
                .getAllFloorContentByAccommAndFloorNo(shipCode, deckNo,
                        currentCatalog, brandPks, null,
                        BookFlowConstants.FEATURECODES_FOR_DECK);

        final List<String> cabinTypes = getCabinTypes(shipCode, deckNo,
                deckPlanCriteria.getCode(), brandPks, currentCatalog);

        final List<CabinContentData> cabinDatas = getCabinDatas(shipCode,
                deckNo, brandPks, currentCatalog, cabinTypes);

        final Map<String, List<FacilityModel>> facilityDataMap = productBrowseService
                .getAllFacilitiesByFloors(
                        new ArrayList<String>(Arrays
                                .asList(new String[]{shipCode + deckNo})),
                        currentCatalog);

        deckPlanViewData.setDeckNo(Integer.parseInt(deckNo));
        deckPlanViewData.setShipCode(shipCode);

        if (MapUtils.isNotEmpty(deckDataMap)) {
            for (final Entry<String, ItemContentData> entry : deckDataMap
                    .entrySet()) {
                final Map<String, String> featureMap = entry.getValue()
                        .getFeatures();
                // start populate svg , gif and Image Datas
                populateImageDatas(featureMap, deckPlanViewData);
                // here featuremap contains the floorcode in the name of
                // shipcode.
                populateFacilityDatas(facilityDataMap, featureMap,
                        deckPlanViewData);
                // start populate for cabin Option and Categories
                populateCabinDatas(cabinDatas, deckPlanViewData);
            }
        }
    }

    /**
     * @return list of Brand PKs.
     */
    private List<String> getBrandPKs() {
        final BrandDetails brandDetails = sessionService
                .getAttribute(BookFlowConstants.BRAND_DETAILS);
        return brandDetails.getRelevantBrands();
    }

    @SuppressWarnings("deprecation")
    private List<String> getCabinTypes(final String shipCode,
            final String floorNo, final String cabinType,
            final List<String> brandPks,
            final CatalogVersionModel currentCatalog) {
        List<String> cabinTypes = new ArrayList<String>();

        if (StringUtils.isEmpty(cabinType)) {
            // for deckplans.
            cabinTypes = productBrowseService.roomTypeCodes(shipCode, floorNo,
                    currentCatalog, brandPks);
        } else {
            // for cabins page.
            cabinTypes.add(cabinType);
        }
        return cabinTypes;
    }

    private List<CabinContentData> getCabinDatas(final String shipCode,
            final String floorNo, final List<String> brandPks,
            final CatalogVersionModel currentCatalog,
            final List<String> cabinTypes) {
        List<CabinContentData> cabinDatas = null;

        if (CollectionUtils.isEmpty(cabinTypes)) {
            LOG.warn("Cabin Types not found for shipcode : " + shipCode
                    + " floorNo : " + floorNo);
        } else {
            cabinDatas = productBrowseService.getRoomContent(shipCode, floorNo,
                    cabinTypes, currentCatalog, brandPks,
                    BookFlowConstants.FEATURECODES_FOR_ROOM);
        }
        return cabinDatas;
    }

    /**
     * @param cabinDatas
     * @param deckPlanViewData
     */
    private void populateCabinDatas(final List<CabinContentData> cabinDatas,
            final DeckPlanViewData deckPlanViewData) {
        if (cabinDatas != null) {
            poplateCabinCategories(cabinDatas, deckPlanViewData);
            deckPlanViewData.setCabinTypeMap(getCabinTypeMap(cabinDatas));
        }
    }

    /**
     * @param cabinDatas
     * @param deckPlanViewData
     */
    private void poplateCabinCategories(
            final List<CabinContentData> cabinDatas,
            final DeckPlanViewData deckPlanViewData) {
        for (final CabinContentData cabinContentData : cabinDatas) {
            final CabinFacilitiesViewData data = new CabinFacilitiesViewData();
            final Map<String, List<String>> cabinTypedata = convertSetToList(cabinContentData);

            data.setCode(cabinContentData.getCode());
            data.setName(cabinContentData.getName());
            data.setUsps(cabinTypedata);
            data.setTypeCode(cabinContentData.getCode().split(
                    BookFlowConstants.UNDERSCORE)[0]);
            data.setRoomsList(returnList(cabinContentData.getRoomNo()));
            deckPlanViewData.getCabinFacilitiesViewData().add(data);
        }
    }

    private List returnList(final Set<String> set) {
        return new ArrayList(set);
    }

    private Map<String, List<String>> getCabinTypeMap(
            final List<CabinContentData> cabinDatas) {
        final Map<String, List<String>> cabinTypeMap = new HashMap<String, List<String>>();
        for (final CabinContentData data : cabinDatas) {
            final Map<String, List<String>> cabinTypedata = convertSetToList(data);

            addToCabinDatas(cabinTypeMap, cabinTypedata);
        }
        return cabinTypeMap;
    }

    private void addToCabinDatas(final Map<String, List<String>> cabinTypeMap,
            final Map<String, List<String>> cabinTypedata) {
        for (final Entry<String, List<String>> entry : cabinTypedata.entrySet()) {
            final String[] usps = entry.getKey().split(
                    BookFlowConstants.REGEX_PIPE);
            for (final String s : usps) {
                if (CollectionUtils.isNotEmpty(cabinTypeMap.get(s))) {
                    cabinTypeMap.get(s).addAll(entry.getValue());
                } else {
                    cabinTypeMap.put(s, entry.getValue());
                }
            }
        }
    }

    private Map<String, List<String>> convertSetToList(
            final CabinContentData cabinContentData) {
        final Map<String, List<String>> cabinTypedata = new HashMap<String, List<String>>();
        for (final String s : cabinContentData.getUsps().keySet()) {
            cabinTypedata.put(s, addValidUsps(cabinContentData, s));
        }
        return cabinTypedata;
    }

    private List<String> addValidUsps(final CabinContentData cabinContentData,
            final String s) {
        final List<String> returnList = new ArrayList<String>();

        final Set<String> uspList = cabinContentData.getUsps().get(s);
        final Set<String> roomsList = cabinContentData.getRoomNo();

        for (final String room : uspList) {
            if (roomsList.contains(room)) {
                returnList.add(room);
            }
        }
        return returnList;
    }

    /**
     * @param facilityDataMap
     * @param featureMap
     * @param deckPlanViewData
     */
    private void populateFacilityDatas(
            final Map<String, List<FacilityModel>> facilityDataMap,
            final Map<String, String> featureMap,
            final DeckPlanViewData deckPlanViewData) {
        final String deckCode = featureMap.get(BookFlowConstants.SHIP_CODE);
        if (StringUtils.isNotBlank(deckCode)
                && facilityDataMap.get(deckCode) != null) {
            deckPlanViewData
                    .setFacilities(populateFacilityNameList(facilityDataMap
                            .get(deckCode)));
        }

    }

    /**
     * @param list
     * @return listOfFacilities
     */
    private List<String> populateFacilityNameList(final List<FacilityModel> list) {
        final List<String> facilityNameList = new ArrayList<String>();
        for (final FacilityModel facilityModel : list) {
            facilityNameList.add(facilityModel.getName());
        }
        return facilityNameList;
    }

    /**
     * @param featureMap
     * @param deckPlanViewData
     */
    private void populateImageDatas(final Map<String, String> featureMap,
            final DeckPlanViewData deckPlanViewData) {
        deckPlanViewData.setSvgImageUrl(featureMap
                .get(BookFlowConstants.SVG_IMAGE));
        deckPlanViewData.setGifImageUrl(featureMap
                .get(BookFlowConstants.GIF_IMAGE));
    }

}
