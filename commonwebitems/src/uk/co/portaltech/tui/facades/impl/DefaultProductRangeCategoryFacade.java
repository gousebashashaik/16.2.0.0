/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.services.ProductRangeCategoryService;
import uk.co.portaltech.tui.facades.ProductRangeCategoryFacade;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author l.furrer
 *
 */
public class DefaultProductRangeCategoryFacade implements ProductRangeCategoryFacade {

    @Resource
    private CategoryService                                                    categoryService;

    @Resource
    private ProductRangeCategoryService                                        productRangeCategoryService;

    @Resource
    private Converter<ProductRangeCategoryModel, ProductRangeCategoryViewData> productRangeCategoryConverter;

    @Resource
    private Populator<ProductRangeCategoryModel, ProductRangeCategoryViewData> productRangeCategoryBasicPopulator;

    @Resource
    private Populator<ProductRangeCategoryModel, ProductRangeCategoryViewData> productRangeCategoryCarouselOverviewPopulator;

    @Resource
    private CMSSiteService                                                     cmsSiteService;

    @Override
    public List<ProductRangeCategoryViewData> getAllProductRangeCategories() {

        Iterator<ProductRangeCategoryModel> productRangeCategoryModelsItr =
                productRangeCategoryService.getProductRangeCategories(cmsSiteService.getCurrentCatalogVersion()).iterator();
        List<ProductRangeCategoryViewData> productRangeCategoryViewDatas = new ArrayList<ProductRangeCategoryViewData>();

        // we remove the root category itself from the returned list
        // the list is itself unmodifiable so we force the first next() call.
        productRangeCategoryModelsItr.next();

        while (productRangeCategoryModelsItr.hasNext()) {
            ProductRangeCategoryModel currentProductRangeCategory = productRangeCategoryModelsItr.next();
            ProductRangeCategoryViewData currentViewData = productRangeCategoryConverter.convert(currentProductRangeCategory);
            productRangeCategoryBasicPopulator.populate(currentProductRangeCategory, currentViewData);
            productRangeCategoryViewDatas.add(currentViewData);
        }
        return productRangeCategoryViewDatas;
    }

    @Override
    public ProductRangeCategoryViewData getProductRangeCategoryByCode(String productRangeCategoryCode) {
        ProductRangeCategoryModel productRangeCategoryModel = (ProductRangeCategoryModel) categoryService.getCategoryForCode(productRangeCategoryCode);
        ProductRangeCategoryViewData productRangeCategoryViewData = productRangeCategoryConverter.convert(productRangeCategoryModel);
        productRangeCategoryBasicPopulator.populate(productRangeCategoryModel, productRangeCategoryViewData);
        return productRangeCategoryViewData;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.ProductRangeCategoryFacade#getProductOverviewCarouselData(int,
     * java.lang.String)
     */
    @Override
    public ProductRangeCategoryViewData getProductOverviewCarouselData(int visibleItems, String productRangeCode) {
        ProductRangeCategoryModel productRangeCategoryModel = (ProductRangeCategoryModel) categoryService.getCategoryForCode(productRangeCode);
        ProductRangeCategoryViewData productRangeCategoryViewData = productRangeCategoryConverter.convert(productRangeCategoryModel);
        productRangeCategoryCarouselOverviewPopulator.populate(productRangeCategoryModel, productRangeCategoryViewData);
        List<ProductRangeViewData> productRanges = productRangeCategoryViewData.getProductRanges();
        if (productRanges != null && productRanges.size() > visibleItems) {
            productRangeCategoryViewData.setProductRanges(productRanges.subList(0, visibleItems));
        }
        return productRangeCategoryViewData;
    }

}
