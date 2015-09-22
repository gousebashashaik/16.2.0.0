/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author s.consolino
 *
 */
public class ProductRangeHighlightsPopulator implements Populator<ProductRangeModel, ProductRangeViewData>
{

    @Resource
    private FeatureService featureService;

    @Resource
    private BrandUtils brandUtils;


    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final ProductRangeModel source, final ProductRangeViewData target) throws ConversionException
    {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");
        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "usps" }));
        target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, source, new Date(),
                brandUtils.getFeatureServiceBrand(source.getBrands())));
    }
}
