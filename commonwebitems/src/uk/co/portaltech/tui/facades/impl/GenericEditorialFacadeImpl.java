/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.ItemModel;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.facades.GenericEditorialFacade;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author pts
 *
 */
public class GenericEditorialFacadeImpl implements GenericEditorialFacade
{

    @Resource
    private FeatureService featureService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private AccommodationService accommodationService;

    @Resource
    private CMSSiteService cmsSiteService;

    @Resource
    private BrandUtils brandUtils;

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.facades.GenericEditorialFacade#getConfiguredFeatureValues(uk.co.portaltech.tui.components
     * .model.GenericEditorialComponentModel)
     */
    @Override
    public String getConfiguredFeatureValueForCode(final String productCode, final String categoryCode, final String featureCode)
    {
        if (productCode != null && !StringUtils.isEmpty(productCode))
        {
            final AccommodationModel accomodation = accommodationService.getAccomodationByCodeAndCatalogVersion(productCode,
                    cmsSiteService.getCurrentCatalogVersion(), null);
            return this.getFeatureValues(featureCode, accomodation);
        }
        else if (categoryCode != null && !StringUtils.isEmpty(categoryCode))
        {
            final CategoryModel categoryModel = categoryService.getCategoryForCode(categoryCode);
            return this.getFeatureValues(featureCode, categoryModel);
        }
        return null;
    }

    private String getFeatureValues(final String featureCode, final ItemModel itemModel)
    {
        List<BrandType> brands = null;
        if (itemModel instanceof AccommodationModel)
        {
            brands = ((AccommodationModel) itemModel).getBrands();
        }
        else if (itemModel instanceof CategoryModel)
        {
            brands = ((CategoryModel) itemModel).getBrands();
        }
        return featureService.getFirstFeatureValueAsString(featureCode, itemModel, new Date(),
                brandUtils.getFeatureServiceBrand(brands));
    }
}
