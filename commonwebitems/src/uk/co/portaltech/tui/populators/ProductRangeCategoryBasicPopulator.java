/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.facades.ProductRangeFacade;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author l.furrer
 *
 */
public class ProductRangeCategoryBasicPopulator implements Populator<ProductRangeCategoryModel, ProductRangeCategoryViewData>
{

    private ProductRangeFacade productRangeFacade;

    @Resource
    private FeatureService featureService;
    @Resource
    private BrandUtils brandUtils;

    @Required
    public void setProductRangeFacade(final ProductRangeFacade productRangeFacade)
    {
        this.productRangeFacade = productRangeFacade;
    }


    /**
     * This methods search for sub categories of the provided ProductRangeCategory.<br/>
     * If a child is a ProductRange it gets added in the productRanges list.<br/>
     * if it is a ProductRangeCategory it gets added to the product range categories list.
     */
    @Override
    public void populate(final ProductRangeCategoryModel sourceModel, final ProductRangeCategoryViewData targetData)
            throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        targetData.setCode(sourceModel.getCode());
        targetData.setName(sourceModel.getName());
        targetData.setDescription(sourceModel.getDescription());

        targetData.setPrCategoryName(sourceModel.getName());

        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "name" }));
        targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, sourceModel, new Date(),
                brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));

        // fetch child product ranges and populate them
        final List<ProductRangeViewData> productRangesViewDataList = new ArrayList<ProductRangeViewData>();
        final Iterator<CategoryModel> productRangesListItr = sourceModel.getCategories().iterator();
        while (productRangesListItr.hasNext())
        {
            productRangesViewDataList.add(productRangeFacade.getProductRangeByCode(productRangesListItr.next().getCode()));
        }

        targetData.setProductRanges(productRangesViewDataList);

    }

}
