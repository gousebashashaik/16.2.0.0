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

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author s.consolino
 *
 */
public class LocationHighlightsPopulator implements Populator<LocationModel, LocationData>
{
    @Resource
    private FeatureService featureService;

    @Resource
    private TUIUrlResolver<LocationModel> tuiCategoryModelUrlResolver;

    @Resource
    private ViewSelector viewSelector;

    @Resource
    private BrandUtils brandUtils;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final LocationModel source, final LocationData target) throws ConversionException
    {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");
        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "usps" }));
        target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, source, new Date(),
                brandUtils.getFeatureServiceBrand(source.getBrands())));
        if (viewSelector.checkIsMobile())
        {
            String url = tuiCategoryModelUrlResolver.resolve(source);
            url = url.replace("overview", "essential-info");
            target.setUrl(url);
        }
    }
}
