/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.DurationHaulItemModel;
import uk.co.portaltech.util.CatalogUtil;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author gaurav.b
 *
 */
public class DurationPriorityRuleService {


    @Resource
    private CatalogVersionService catalogVersionService;

    @Resource
    private CatalogUtil catalogUtil;

    private static final TUILogUtils LOG = new TUILogUtils("DurationPriorityRuleService");

    private static final String HAULTYPE_QUERY = "SELECT {pk} FROM {DurationHaulItem} WHERE {departure}=?departure and {arrival}=?arrival and {catalogVersion}=?catalogVersion";

    private static final String DEPARTURE = "departure";
    private static final String ARRIVAL = "arrival";
    private static final String CATALOG_VERSION = "catalogVersion";

    @Resource
    private FlexibleSearchService flexibleSearchService;




    /**
     * This method finds the haul type based on departureAirportCode and
     * arrivalAirportCode.
     *
     * @param departureAirportCode
     *            the departureAirportCode
     * @param arrivalAirportCode
     *            the arrivalAirportCode
     * @return haulType
     */
    public String findHaultype(String departureAirportCode,
            String arrivalAirportCode) {
        final CatalogVersionModel catalogVersion = getImportCatalogVersion();
        String haulType = "";
        DurationHaulItemModel duraionHaulItemModel = null;
        try {

            FlexibleSearchQuery searchDestination = new FlexibleSearchQuery(
                    HAULTYPE_QUERY);
            searchDestination
                    .addQueryParameter(DEPARTURE, departureAirportCode);
            searchDestination.addQueryParameter(ARRIVAL, arrivalAirportCode);
            searchDestination.addQueryParameter(CATALOG_VERSION,
                    catalogVersion.getPk());
            duraionHaulItemModel = flexibleSearchService
                    .searchUnique(searchDestination);
            haulType = duraionHaulItemModel.getHaul();
        } catch (ModelNotFoundException exception) {
            LOG.error("Error while fetching haul Type for : departureAirportCode: "
                    + departureAirportCode
                    + "and arrivalAirportCode : "
                    + arrivalAirportCode,exception);
        }
        return haulType;
    }


    /**
     * This method returns the CatalogVersionModel.
     *
     * @return CatalogVersionModel object.
     */
    private CatalogVersionModel getImportCatalogVersion() {
        CatalogModel catalogModel = catalogUtil.getActiveProductCatalog();
        String catalog = catalogModel.getId();
        return catalogVersionService.getCatalogVersion(catalog, "Online");
    }

}
