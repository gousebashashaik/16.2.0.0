/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author l.furrer
 *
 */
public class ProductRangeBasicPopulator implements Populator<ProductRangeModel, ProductRangeViewData>
{

    @Resource
    private FeatureService featureService;

    @Resource
    private BrandUtils brandUtils;

    /*
     * (non-Javadoc)#
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final ProductRangeModel source, final ProductRangeViewData target) throws ConversionException
    {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");

        target.setCode(source.getCode());

        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "name", "strapline", "shortDescription", "overviewDescription" }));
        final Map<String, List<Object>> features = featureService.getOptimizedValuesForFeatures(featureDescriptorList, source, new Date(),
                null);
        if (null != features)
        {
            target.putFeatureCodesAndValues(escapeSpecialCharacters(features));
        }

        // fallback for name
        if (target.getFeatureCodesAndValues().get("name") == null)
        {
            final List<Object> name = new ArrayList<Object>();
            name.add(source.getName());
            target.getFeatureCodesAndValues().put("name", name);
        }

        if (source.getPicture() != null)
        {
            target.setPictureUrl(source.getPicture().getURL());
        }

        if (CollectionUtils.isNotEmpty(source.getBrands()))
        {
            target.setBrandType(brandUtils.getFeatureServiceBrand(source.getBrands()));
        }
    }

    /**
     * Escape special characters.
     *
     * @param features
     *           the features
     * @return the map
     */
    private Map<String, List<Object>> escapeSpecialCharacters(final Map<String, List<Object>> features)
    {
        final Map<String, List<Object>> productFeatures = new HashMap<String, List<Object>>();

        for (final Map.Entry<String, List<Object>> feature : features.entrySet())
        {
            final List<Object> featureDesc = new ArrayList<Object>();
            for (final Object object : feature.getValue())
            {
                featureDesc.add(StringEscapeUtils.escapeJavaScript((String) object));
            }
            productFeatures.put(feature.getKey(), featureDesc);
        }
        return productFeatures;
    }
}
