/**
 *
 */
package uk.co.tui.util;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import javax.annotation.Resource;

import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.util.CatalogUtil;

/**
 * Utility Class for handling Catalog and Category.
 *
 * @author amaresh.d
 */
public class CatalogAndCategoryUtil {

    @Resource
    private CatalogVersionService catalogVersionService;

    @Resource
    private TuiUtilityService tuiUtilityService;

    @Resource
    private CatalogUtil catalogUtil;

    /**
     * This method returns active product Catalogversion.
     *
     * @return CatalogVersionModel
     */
    public CatalogVersionModel getActiveCatalogversion() {
        CatalogModel catalogModel = catalogUtil.getDefaultProductCatalogForCMSSiteId(tuiUtilityService
                        .getSiteBrand());
        return catalogVersionService.getCatalogVersion(catalogModel.getId(),
                "Online");
    }
}
